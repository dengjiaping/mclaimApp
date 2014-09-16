package com.sinosoftyingda.fastclaim;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.common.apn.model.ApnDto;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.definetime.TimeService;
import com.sinosoftyingda.fastclaim.common.mina.MinaService;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.views.BackMainPageUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.BottomManager;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.PageUtils;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.splash.page.SplashView;

/**
 * 
 * @author DengGuang
 */
public class MainActivity extends Activity {
	private static final String	Tag		= "MainActivity";
	private PageUtils			pageUtils;
	private RelativeLayout		layout;
	private ProgressDialog		progressDialog;
	private BackMainPageUtils	backMainPageUtils;

	// add by yxf 20140124 添加监听网络信号强度功能 begin
	private TelephonyManager tel = null;
	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int dBm = -113 + signalStrength.getGsmSignalStrength() * 2;
			
			if (dBm < -100) {
				Toast.makeText(getApplicationContext(),R.string.toast_network_mobile_bad_signal,Toast.LENGTH_SHORT).show();
			}
		};
	};
	// add by yxf 20140124 添加监听网络信号强度功能 end
	
	private ApnDto				oldApnDto, newApnDto;
	private Handler				handler	= new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantValue.PROGRESS_OPEN:
				// 信息加载进度条
				PromptManager.showSimpleProgressDialog(progressDialog, getString(R.string.is_loading));
				break;
			case ConstantValue.PROGRESS_CLOSE:
				// 关闭进度条
				PromptManager.closeProgressDialog(progressDialog);
				break;
			case ConstantValue.ERROE:
				// 显示错误信息
				PromptManager.showErrorDialog(MainActivity.this, msg.obj.toString());
				break;
			case ConstantValue.EXIT:
				stopService();
				/**
				 * 判断街道信息是否为空如果不为空则保存至数据库
				 * add by haoyun
				 */
				if (!TextUtils.isEmpty(SystemConfig.currentThoroughfare)) {
					TblGPSAddress.insert(SystemConfig.currentThoroughfare);
				}
				// 退出系统
				PromptManager.showExitDialog(MainActivity.this);
				break;
			case ConstantValue.PROGRESSExit_OPEN:
				// 关闭时钟服务 
				TimeService task = new TimeService();
				task.onDestroy();											// 关闭线程
				stopService(new Intent(MainActivity.this, TimeService.class));			// 结束后台服务

				// 显示登录注销进度条
				PromptManager.showSimpleProgressDialog(progressDialog, getString(R.string.is_extiting));
				break;
			case ConstantValue.PROGRESSExit_CLOSE:
				// 关闭登录注销进度条
				PromptManager.closeProgressDialog(progressDialog);
				break;

			case ConstantValue.PROGRESSTONGBU_OPEN:
				// 理赔同步
				PromptManager.showSimpleProgressDialog(progressDialog, getString(R.string.is_claimtongbuting));
				break;
			case ConstantValue.PROGRESSTONGBU_CLOSE:
				// 理赔同步
				PromptManager.closeProgressDialog(progressDialog);
				break;
			case ConstantValue.PROGRESSSUMBIT_OPEN:
				// 理赔同步
				PromptManager.showSimpleProgressDialog(progressDialog, getString(R.string.is_claimsumbitting));
				break;
			case ConstantValue.PROGRESSSUMBIT_CLOSE:
				// 理赔同步
				PromptManager.closeProgressDialog(progressDialog);
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		SystemConfig.context = this;

		HttpUtils.isNetworkConnected(this);
		SystemConfig.dbhelp = new FastClaimDbHelper(this);
		// 获取屏幕高度
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		SystemConfig.SCREENHEIGHT = displayMetrics.heightPixels;
		try {
			new FastClaimDbHelper(this).createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}

		// add by yxf 20140124 获取网络信号强度监听 begin
		tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tel.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		// add by yxf 20140124 获取网络信号强度监听 end
		
		init();
	}

	// add by yxf 20140124 获取网络信号强度监听 begin
	@Override
	protected void onPause() {
		super.onPause();
		tel.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tel.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	// add by yxf 20140124 获取网络信号强度监听 end
	
	private void init() {
		pageUtils = new PageUtils(this);
		backMainPageUtils = new BackMainPageUtils();
		UiManager.getInstance().addObserver(pageUtils);
		UiManager.getInstance().addObserver(backMainPageUtils);
		UiManager.getInstance().addObserver(TopManager.getInstance());
		progressDialog = new ProgressDialog(this);
		// 初始化顶部导航
		TopManager.getInstance().init(this);
		// 初始化底部导航
		BottomManager.getInstance().init(this);
		// 显示的内容
		layout = (RelativeLayout) findViewById(R.id.ii_main_middle);
		// 内容管理器
		UiManager.getInstance().setContiner(layout);
		// 切换界面管理类
		UiManager.getInstance().changeView(SplashView.class, null, false, false);
		BaseView.setHandler(handler);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean result = true;
			result = UiManager.getInstance().changeCacheView();
			if (result == false) {
				handler.sendEmptyMessage(ConstantValue.EXIT);
			}
			return result;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Intent	service;
	public void startService() {
		if (service == null) {
			service = new Intent();
			service.setClass(getBaseContext(), MinaService.class);
			startService(service);
		}
	}

	public void stopService() {
		if (service != null) {
			stopService(service);
			service = null;
		}
	}

	@Override
	protected void onDestroy() {
		stopService();
		super.onDestroy();
	}

}
