package com.sinosoftyingda.fastclaim.common.mina;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.service.TaskQueryHttpService;
import com.sinosoftyingda.fastclaim.notice.page.NoticeActivity;

/**
 * 后台自动拉取任务Service
 * @author JingTuo
 */
public class MinaService extends Service {

	public static final String ACTION_REFRESH_DATA = "com.sinosoftyingda.fastclaim.RefreshData";
	private MinaClient client;
	private Timer timer;
	private MinaReceiver receiver;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public MinaService() {
		client = ConnectionManager.getMinaClient(this);
		timer = new Timer();
		
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		timer.schedule(new KeepAlive(), 30 * 1000, 30 * 1000);
		
		
	}
	// add by yxf 20140124 添加监听网络信号强度功能 begin
			TelephonyManager tel = null;
			PhoneStateListener phoneStateListener = new PhoneStateListener() {
				public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
					super.onSignalStrengthsChanged(signalStrength);
					int dBm = -113 + signalStrength.getGsmSignalStrength() * 2;
					SystemConfig.mobileSingle = dBm;
					if (dBm < -100) {
						SystemConfig.mobileSingleTestTime++;
						if(SystemConfig.mobileSingleTestTime==3){
							SystemConfig.mobileSingleTestTime=0;
							Toast.makeText(getApplicationContext(),R.string.toast_network_mobile_bad_signal,Toast.LENGTH_SHORT).show();
						}
					}
				};
			};
			// add by yxf 20140124 添加监听网络信号强度功能 end
	   public void onCreate() {  
	        super.onCreate();  
	     // add by yxf 20140124 获取网络信号强度监听 begin
			tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			tel.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			// add by yxf 20140124 获取网络信号强度监听 end
	    }  

	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.purge();
			timer.cancel();
			timer = null;
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (client != null) {
			client.close();
			client = null;
		}
		super.onDestroy();
	}

	/**
	 * 30秒拉取一次任务
	 * @author JingTuo
	 */
	class KeepAlive extends TimerTask {
		@Override
		public void run() {
			if (existsConnectdNetwork()) {
				TaskQueryRequest taskQueryRequest = new TaskQueryRequest();
				taskQueryRequest.setTaskType("all");
				taskQueryRequest.setTaskStatus("0");
				taskQueryRequest.setUserCode(SystemConfig.USERLOGINNAME);
				TaskQueryResponse result = TaskQueryHttpService.taskQuerySercive(taskQueryRequest, getString(R.string.http_url));
				if (result != null && result.getResponseCode().equals("YES")) {
					if (result.getCheckTask() != null && result.getCheckTask().size() >= 1 || result.getCertainLossTask() != null && result.getCertainLossTask().size() >= 1) {
						notice(getBaseContext(), result.getCheckTask(), result.getCertainLossTask());
						Intent broadcast = new Intent();
						broadcast.setAction(ACTION_REFRESH_DATA);
						sendBroadcast(broadcast);
					}
				}
			}
		}

		public boolean existsConnectdNetwork() {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			boolean flag = wifi.isConnected() || mobile.isConnected();
			return flag;
		}

	}

	public class MinaReceiver extends BroadcastReceiver {

		public MinaReceiver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi.isConnected() || mobile.isConnected()) {
					if (!client.isConnected()) {
						// TaskExecutor.getInstance().submitTask(new
						// Connect(context, client));
					}
				} else {
					Toast.makeText(context, R.string.toast_network_mobile_wifi_not_available, Toast.LENGTH_SHORT).show();
					// client.close();
				}
			}
		}

	}

	private void notice(Context context, List<CheckTask> cts, List<CertainLossTask> clts) {
		int newtasknum = 0;
		int count = 0;

		Intent intent = new Intent();
		intent.setClass(context, NoticeActivity.class);
		if (cts != null && cts.size() >= 1) {
			for (int i = 0; i < cts.size(); i++) {
				if (cts.get(i).getSurveytaskstatus().equals("1") && cts.get(i).getApplycannelstatus().equals("")) {// 新任务到达
					newtasknum++;
				} else if (cts.get(i).getSurveytaskstatus().equals("1") && cts.get(i).getApplycannelstatus().equals("success")) {// 改派成功
					count++;
					DBUtils.delete("CheckTask", "registNo=?", new String[] { cts.get(i).getRegistno() });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.check_task_dispatch_result), -1, intent);
				} else if (cts.get(i).getSurveytaskstatus().equals("4")) {// 服务端提交
					count++;
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.check_task_complete_resut), -1, intent);
				} else {
					count++;
				}
			}
		}

		if (clts != null && clts.size() >= 1) {
			for (int i = 0; i < clts.size(); i++) {
				if (clts.get(i).getSurveytaskstatus().equals("1") && clts.get(i).getApplycannelstatus().equals("")) {// 新任务到达
					newtasknum++;
				} else if (clts.get(i).getSurveytaskstatus().equals("1") && clts.get(i).getApplycannelstatus().equals("success")) {// 改派成功
					count++;
					DBUtils.delete("certainlosstask", "registNo=? and itemno=?", new String[] { clts.get(i).getRegistno(), clts.get(i).getItemno() + "" });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.certainloss_task_dispatch_result), -1, intent);
				} else if (clts.get(i).getSurveytaskstatus().equals("4") && clts.get(i).getVerifypassflag().equals("1")) {// 服务端提交
					count++;
					// ContentValues values = new ContentValues();
					// values.put("completetime",
					// DateTimeUtils.parseDateToString(new
					// Date(System.currentTimeMillis()),
					// DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
					// DBUtils.update("certainlosstask", values,
					// "registno=? and itemno=?", new String[] {
					// clts.get(i).getRegistno(),clts.get(i).getItemno() + ""
					// });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.certainloss_task_complete_resut), -1, intent);
				} else if (clts.get(i).getSurveytaskstatus().equals("5")) {// 核损退回
					count++;
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.certainloss_task_verify_result), -1, intent);
				} else if (clts.get(i).getSurveytaskstatus().equals("4") && clts.get(i).getVerifypassflag().equals("4")) {// 核损通过
					count++;
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, getString(R.string.certainloss_task_verify_result), -1, intent);
				} else {
					count++;
				}
			}
		}

		if (newtasknum >= 1) {
			NoticeManager.getInstance(context).notice(NoticeManager.TAG_NEWTASK, getString(R.string.new_task) + "(" + newtasknum + ")", R.raw.newtask, intent);
		}

	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}
