package com.sinosoftyingda.fastclaim.more.page;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.util.AmapUtils;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.mina.ConnectionManager;
import com.sinosoftyingda.fastclaim.common.mina.task.Offline;
import com.sinosoftyingda.fastclaim.common.mina.task.TaskExecutor;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.DutyRequest;
import com.sinosoftyingda.fastclaim.common.model.UpdateVersionRequest;
import com.sinosoftyingda.fastclaim.common.model.UpdateVersionResponse;
import com.sinosoftyingda.fastclaim.common.service.DutyHttpService;
import com.sinosoftyingda.fastclaim.common.service.UpdateVersionHttpService;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch.OnSwitchListener;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.more.service.JYDataUPdataOfflineActivity;
import com.sinosoftyingda.fastclaim.more.service.MoreServlet;

/**
 * 更多界面
 * 
 * @author chenjianfan
 * 
 *         for update haoyun 20130313
 * 
 */
public class MoreActivity extends BaseView implements OnClickListener {

	protected static final String	Tag	= "MoreActivity";
	private RelativeLayout			layout;
	private RelativeLayout			llState;
	private LinearLayout			llLogin;
	private LinearLayout			llVersion;
	private LinearLayout			llOfflineData;
	private LinearLayout			llAbout;
	private LinearLayout			llAboutInfo;
	private boolean					isDuty;
	private MySlipSwitch			btnSlipSwitch;
	private PromptManager			promptManager;

	private MoreServlet				moreServlet;
	private ProgressDialog			mProgressDialog;

	private TextView				tvName;
	private TextView				tvJiGou;
	private Bitmap					bitmap;

	public MoreActivity(Context context, Bundle bundle) {
		super(context, bundle);

	}

	@Override
	public View getView() {

		return layout;
	}

	@Override
	public Integer getType() {

		return ConstantValue.Page_second;
	}

	@Override
	protected void init() {
		// 第一步：加载layout
		layout = (RelativeLayout) inflater.inflate(R.layout.more, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		promptManager = new PromptManager();
		// 初始化控件
		llState = (RelativeLayout) layout.findViewById(R.id.moreactivity_ll_state);
		llLogin = (LinearLayout) layout.findViewById(R.id.moreactivity_ll_login);
		llVersion = (LinearLayout) layout.findViewById(R.id.moreactivity_ll_version);
		llAbout = (LinearLayout) layout.findViewById(R.id.moreactivity_ll_about);
		llAboutInfo = (LinearLayout) layout.findViewById(R.id.moreactivity_ll_aboutinfo);
		llOfflineData = (LinearLayout) layout.findViewById(R.id.moreactivity_ll_offlinedata);
		moreServlet = new MoreServlet(context);
		tvName = (TextView) layout.findViewById(R.id.moreactivity_ll_state2_name);
		tvJiGou = (TextView) layout.findViewById(R.id.moreactivity_ll_state2_name_jigou);
		// 协赔员不显示离线缓存
		if (!SystemConfig.UserRightIsAdvanced)
			if (llOfflineData.getVisibility() == View.VISIBLE)
				llOfflineData.setVisibility(View.GONE);
		tvName.setText(SystemConfig.loginResponse.getUserName());
		tvJiGou.setText(SystemConfig.loginResponse.getUserComName());
		// 设置顶部导航标题
		TopManager.getInstance().setHeadTitle("更多", 22, Typeface.defaultFromStyle(Typeface.NORMAL));

		// 滑动按钮
		btnSlipSwitch = (MySlipSwitch) layout.findViewById(R.id.moreactivity_ll_btn);
		btnSlipSwitch.setImageResource(R.drawable.unwork, R.drawable.unwork, R.drawable.work_btn);
		if ("on".equals(SystemConfig.DUTYFLAG)) {			// 上班
			btnSlipSwitch.setSwitchState(false);
		} else if ("off".equals(SystemConfig.DUTYFLAG)) {	// 下班
			btnSlipSwitch.setSwitchState(true);
		} else if ("notonline".equals(SystemConfig.DUTYFLAG)) {
			btnSlipSwitch.setSwitchState(false);
		} else {
			btnSlipSwitch.setSwitchState(false);
		}

		btnSlipSwitch.setOnSwitchListener(new OnSwitchListener() {
			@Override
			public void onSwitched(boolean isSwitchOn) {
				if (isSwitchOn) {
					// haoyun add begin
					Log.i(Tag, "下班");
					isDuty = true;
					/**
					 * 下班交互方法
					 */
					dutyService("off");
					// haoyun add end
				} else {
					// haoyun add begin
					Log.i(Tag, "上班");
					isDuty = false;
					/**
					 * 上班交互方法
					 */
					dutyService("on");
					// haoyun add end
				}
			}
		});

	}

	@Override
	protected void setListener() {
		llState.setOnClickListener(this);
		llLogin.setOnClickListener(this);
		llVersion.setOnClickListener(this);
		llAbout.setOnClickListener(this);
		llOfflineData.setOnClickListener(this);
		llAboutInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.moreactivity_ll_state:

			break;
		case R.id.moreactivity_ll_login:
			// 移除位置监听
			AmapUtils.removeListener();
			// 断开常连接
			TaskExecutor.getInstance().submitTask(new Offline(context, ConnectionManager.getMinaClient(context)));
			// 注销账户
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected void onPreExecute() {
					handler.sendEmptyMessage(ConstantValue.PROGRESSExit_OPEN);
				};

				@Override
				protected void onPostExecute(Void result) {
					SystemConfig.isExit = true;
					handler.sendEmptyMessage(ConstantValue.PROGRESSExit_CLOSE);
					android.os.Process.killProcess(android.os.Process.myPid());
				};

				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}

			}.execute();

			break;
		case R.id.moreactivity_ll_version:
			updatVersion();
			break;

		// 精友离线数据更新
		case R.id.moreactivity_ll_offlinedata:
			Intent intent = new Intent();
			intent.setClass(context, JYDataUPdataOfflineActivity.class);
			context.startActivity(intent);
			break;

		case R.id.moreactivity_ll_about:
			Bundle bundle = new Bundle();
			bundle.putString("aa", "bb");
			UiManager.getInstance().changeView(AboutActivity.class, bundle, false);
			break;
			
		case R.id.moreactivity_ll_aboutinfo:
			Bundle bundleInfo = new Bundle();
//			bundleInfo.putString("aa", "bb");
			UiManager.getInstance().changeView(AboutInfoActivity.class, null, false);
			break;
		}
	}

	/****
	 * 版本更新
	 */
	private void updatVersion() {
		if (!HttpUtils.isNetworkConnected(context)) {
			Toast.showToast(context, R.string.error_not_connection);
			return;
		}

		new AsyncTask<String, Void, UpdateVersionResponse>() {

			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(UpdateVersionResponse result) {
				final UpdateVersionResponse result1 = result;
				if ("YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					if (TextUtils.isEmpty(result.getUpdateUrl())) {
						Toast.showToast(context, "此版本是最新的版本,不用更新");
					} else {
						promptManager.showDialog(context, "有新版本，请更新版本!", R.string.is_positive, R.string.is_negative, new ShowDialogPositiveButton() {
							@Override
							public void setPositiveButton() {
								download(result1.getUpdateUrl());
							}

							@Override
							public void setNegativeButton() {

							}
						});
					}
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}

			};

			@Override
			protected UpdateVersionResponse doInBackground(String... params) {
				UpdateVersionRequest updateVersionRequest = new UpdateVersionRequest();
				updateVersionRequest.setVersion(moreServlet.getVersion());
				return UpdateVersionHttpService.updateVersionService(updateVersionRequest, params[0]);
			}

		}.execute(context.getString(R.string.http_url));

	}

	/***
	 * 下载安装
	 */
	protected void download(String urlPath) {

		new AsyncTask<String, Void, File>() {
			@Override
			protected void onPreExecute() {
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setTitle("下载进度");
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setProgress(100);
				// 设置ProgressDialog 是否可以按退回键取消
				mProgressDialog.setCancelable(false);
				mProgressDialog.incrementProgressBy(1);
				mProgressDialog.show();
			};

			@Override
			protected void onPostExecute(File result) {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				if (result != null) {
					moreServlet.installApp(result);
				}
			};

			@Override
			protected File doInBackground(String... params) {
				return moreServlet.download(params[0], mProgressDialog);

			}

		}.execute(urlPath);
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(Tag, "更多页面");
		super.onResume();
	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_More;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	/***
	 * 上下班服务器交互 haoyun add
	 */
	private void dutyService(final String dutyFlag) {
		new AsyncTask<String, Void, CommonResponse>() {

			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					if (dutyFlag.equals("off")) {
						SystemConfig.DUTYFLAG = "off";
						SystemConfig.loginResponse.setDutyFlag("off");
					} else {
						SystemConfig.DUTYFLAG = "on";
						SystemConfig.loginResponse.setDutyFlag("on");
					}
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				DutyRequest requestDuty = new DutyRequest();
				requestDuty.setUserCode(SystemConfig.USERLOGINNAME);
				requestDuty.setIMEI(DeviceUtils.getDeviceId(context));
				requestDuty.setDutyFlag(params[0]);
				return DutyHttpService.dutyService(requestDuty, context.getString(R.string.http_url));
			}

		}.execute(dutyFlag);

	}

}
