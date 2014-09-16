package com.sinosoftyingda.fastclaim.login.page;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.util.AmapUtils;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.definetime.TimeService;
import com.sinosoftyingda.fastclaim.common.model.LoginRequest;
import com.sinosoftyingda.fastclaim.common.model.LoginResponse;
import com.sinosoftyingda.fastclaim.common.service.LoginHttpService;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.login.service.JYLoginService;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;

/**
 * 登录页面
 * 
 * @author chenjianfan
 * 
 */
public class LoginView extends BaseView implements OnClickListener {
	/********** 布局 **************/
	private RelativeLayout		container;

	/********** 控件 **************/
	private EditText			etUser;
	private EditText			etPassword;
	private Button				btnLogin;

	private String				userName;
	private String				password;
	private String				firstUsername;

	private SharedPreferences	sp;

	public LoginView(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return container;
	}

	@Override
	public Integer getType() {

		/***
		 * 标识这个页面是登录页面（还有二级界面/三级页面/工作界面）
		 */
		return ConstantValue.Page_Login;

	}

	@Override
	protected void init() {
		// 第一步：加载layout
		container = (RelativeLayout) inflater.inflate(R.layout.login, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.setLayoutParams(params);

		if (sp == null) {
			sp = context.getSharedPreferences("LOGINDATA", 0);
		}

		// 设置layout的背景
		// 主要是为了px转dp
		// bitmapUtils.setWidgetBackdrop(drawable, linearLayout);
		// 第二步：初始化layout中控件
		etUser = (EditText) container.findViewById(R.id.login_et_uer);
		etPassword = (EditText) container.findViewById(R.id.login_et_pwd);
		btnLogin = (Button) container.findViewById(R.id.login_btn_confirm);

		/***** start*默认保存用户名字 add 陈建凡 ******/

		etUser.setText(sp.getString("LOGINNAME", ""));
		// 原来的用户
		firstUsername = sp.getString("LOGINNAME", "");
		// etPassword.setText(sp.getString("LOGINPWD",""));

		/****** end 默认保存用户名字 add 陈建凡 ******/
	}

	@Override
	protected void setListener() {
		// 确定按钮的点击事件
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onPause() {
		userName = null;
		password = null;
		if (gAdSetGps != null) {
			gAdSetGps.dismiss();
		}
		System.gc();
		super.onPause();
	}

	@Override
	public void onResume() {
		SystemConfig.isExit = false;
		super.onResume();
		setGps();

		closeService();
	}

	/**
	 * 关闭获取任务服务
	 */
	private void closeService() {
		TimeService task = new TimeService();
		task.onDestroy(); // 关闭线程
		context.stopService(new Intent(context, TimeService.class)); // 结束后台服务
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_confirm:
			userName = etUser.getText().toString().trim();
			password = etPassword.getText().toString().trim();
			// 判断空值
			if (TextUtils.isEmpty(userName)) {
				Animation shakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake_x);
				etUser.startAnimation(shakeAnim);
				return;
			}

			if (TextUtils.isEmpty(password)) {
				Animation shakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake_x);
				etPassword.startAnimation(shakeAnim);
				return;
			}

			// 精友需要传递用户代码
			SystemConfig.loginUserName = userName;
			judgeUser();
			break;
		}
	}

	/**
	 * 添加测试时用户名和密码
	 * 
	 * @param name
	 * @param password
	 */
	public void judgeUser(String name, String password) {
		// 初始化静态方法
		DataConfig.initDataValue();

		if (name.equals("00000000") && password.equals("0000")) {
			SystemConfig.UserRightIsAdvanced = true;
			SystemConfig.isUserExperience = true;
			UiManager.getInstance().changeView(SBasicActivity.class, null, true);
		} else if (name.equals("000000") && password.equals("0000")) {
			SystemConfig.UserRightIsAdvanced = false;
			SystemConfig.isUserExperience = true;

			// 登录拉取的标志位
			Bundle bundle = new Bundle();
			bundle.putInt("login", 1);
			UiManager.getInstance().changeView(SurveyTaskActivity.class, bundle, true);
		} else {
			Toast.showToastTest(context, "输入的用户名或密码有误!");
		}
	}

	/***
	 * 判断登录用户的
	 */
	private void judgeUser() {

		new AsyncTask<String, Void, LoginResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(LoginResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					// 关闭进度对话框
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					// 记录用户名和密码
					SystemConfig.USERLOGINNAME = etUser.getText().toString().trim();
					SystemConfig.USERLOGINPASSWORD = etPassword.getText().toString().trim();

					/******** start chenjianfan*****保存用户名和密码 **********/
					saveUserData(SystemConfig.USERLOGINNAME, SystemConfig.USERLOGINPASSWORD);
					/******** end chenjianfan*****保存用户名和密码 **********/
					SystemConfig.loginResponse = result;
					SystemConfig.isExit = true;

					// 权限
					setQuanXian(result);
					// 上班为on，下班为off
					setDutyFlag(result);
					// 开启实时获取定位
					int seconds = 10;
					if (result.getUploadCoordinateTime() != null && !result.getUploadCoordinateTime().equals("")) {
						seconds = Integer.parseInt(result.getUploadCoordinateTime());
					}
					AmapUtils.addListener(context, seconds * 1000, 0);

					// 设置快赔服务时间
					if (result.getServerTime() == null || result.getServerTime().trim().equals("")) {
						SystemConfig.serverTime = DateTimeUtils.getCurrentTime2();
					} else {
						SystemConfig.serverTime = result.getServerTime();
					}
					// 启动时间服务
					Intent defineTimeIntent = new Intent();
					defineTimeIntent.setClass(context, TimeService.class);
					context.startService(defineTimeIntent);
					try {
						// 如果是协赔员不提示精友数据更新
						if (SystemConfig.UserRightIsAdvanced) {
							JYLoginService jyLoginService = new JYLoginService(context);
							jyLoginService.noticeJYUpdatas("000");
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.showToast(context, "未安装精友定损工具");
					}

					// 删除原来用户的本机信息
					if (!firstUsername.equals("") && !firstUsername.equals(SystemConfig.USERLOGINNAME)) {
						PromptManager.showUpdateDialog(context, "将删除原先用户的信息，请重新登录!", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								deletFirstUserAllFile();
							}
						});
					} else {
						// 登录拉取的标志位
						Bundle bundle = new Bundle();
						bundle.putInt("login", 1);
						// 进度主页面
						UiManager.getInstance().changeView(SurveyTaskActivity.class, bundle, true);

						deleteOuttimeFile();
					}
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected LoginResponse doInBackground(String... params) {
				LoginRequest requstLogin = new LoginRequest(DeviceUtils.getDeviceId(context));
				requstLogin.setUserCode(params[0]);
				requstLogin.setPassWord(params[1]);
				requstLogin.setIMEI(DeviceUtils.getDeviceId(context));
				LoginResponse lr = LoginHttpService.loginService(requstLogin, context.getString(R.string.http_url), context);
				return lr;
			}
		}.execute(etUser.getText().toString().trim(), etPassword.getText().toString().trim());
	}

	/**
	 * 删除超过一月的 案件图片以及信息
	 */
	private void deleteOuttimeFile() {
		/* 获取任务列表 */
		List<CheckTask> checkTasks = CheckTaskAccess.getAllCompletedTask();
		List<CertainLossTask> certainLossTasks = CertainLossTaskAccess.getAllCompletedTask();
		List<CertainLossTask> certainLossTasksUnsubmit = CertainLossTaskAccess.getUnCompletedTask();
		/**
		 * 创建日期转换对象 用于字符串转换成date类型
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			/* 获取当前日期与流入快赔时间中间的天数,如果相隔天数等于或大于30则删除此报案号下内容 */
			Date serverDate = null; // 定义时间类型
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss");
			serverDate = inputFormat.parse(SystemConfig.serverTime); // 将字符型转换成日期型
			int a=0; 	//增加变量变化，如果此数值变化，怎不进行删除照片操作
			int b=0;
			/* 遍历查勘任务列表 */
			for (int i = 0; i < checkTasks.size(); i++) {
				int day = DateTimeUtils.getDays(sdf.parse(checkTasks.get(i).getInputedate().substring(0, 10)), serverDate);
				//查勘删除照片，两个条件判断：大于30天；未提交定损案件中没有该查勘报案号
				if (day >= 30) {
					//add by yxf 20140122 reason:增加判断，如果查勘环节已提交，定损环节未提交则不删除；如果查勘环节与定损环节都提交，则删除信息 begin
					for(int j=0;j<certainLossTasksUnsubmit.size();j++){
						if(checkTasks.get(i).getRegistno().equals(certainLossTasksUnsubmit.get(j).getRegistno())){
							a++;
						}
					}
					if(a==0){
						Log.i("yxf", "-----删除超过30天照片文件-----"+checkTasks.get(i).getRegistno());
						/*
						 * 根据地址删除对应报案号下照片文件夹
						 * 文件地址：sdDir(sd卡根目录)+SystemConfig.PHOTO_DIR
						 * (照片所在目录)+当前查勘案件报案号
						 */
						FileUtils.deleteFileByFile(new File(SystemConfig.PHOTO_DIR + checkTasks.get(i).getRegistno()));
					}
					//add by yxf 20140122 reason:增加判断，如果查勘环节已提交，定损环节未提交则不删除；如果查勘环节与定损环节都提交，则删除信息 end
				}
			}
			for (int i = 0; i < certainLossTasks.size(); i++) {
				/* 获取当前日期与流入快赔时间中间的天数,如果相隔天数等于或大于30则删除此报案号下内容 */
				int day = DateTimeUtils.getDays(sdf.parse(certainLossTasks.get(i).getInputedate().substring(0, 10)), serverDate);
				if (day >= 30) {
					for(int k=0;k<certainLossTasksUnsubmit.size();k++){
						if(certainLossTasks.get(i).getRegistno().equals(certainLossTasksUnsubmit.get(k).getRegistno())){
							b++;
						}
					}
					if(b==0){
						Log.i("yxf", "-----删除超过30天照片文件-----"+certainLossTasks.get(i).getRegistno());
						/*
						 * 根据地址删除对应报案号下照片文件夹
						 * 文件地址：sdDir(sd卡根目录)+SystemConfig.PHOTO_DIR
						 * (照片所在目录)+当前查勘案件报案号
						 */
						FileUtils.deleteFileByFile(new File(SystemConfig.PHOTO_DIR + certainLossTasks.get(i).getRegistno()));
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除原用户所有的数据
	 */
	private void deletFirstUserAllFile() {
		// 删除本地数据库claimData中文件
		String sdDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		String dbPath = sdDir + context.getString(R.string.DBPath);
		FileUtils.deleteFileByFile(new File(dbPath));

		// 删除照片cClaim目录
		FileUtils.deleteFileByFile(new File(SystemConfig.PHOTO_DIR));

		// 删除上传cTemp目录
		FileUtils.deleteFileByFile(new File(SystemConfig.PHOTO_TEMP));

		// 删除错误日志claimError目录
		String errorPath = sdDir + "/claimError/";
		FileUtils.deleteFileByFile(new File(errorPath));

		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/***
	 * 保存用户名和密码
	 * 
	 * @param uSERLOGINNAME
	 * @param uSERLOGINPASSWORD
	 */
	protected void saveUserData(String uSERLOGINNAME, String uSERLOGINPASSWORD) {
		if (sp != null) {
			Editor editore = sp.edit();
			editore.putString("LOGINNAME", uSERLOGINNAME);
			editore.putString("LOGINPWD", uSERLOGINPASSWORD);
			editore.commit();
		}
	}

	private void setDutyFlag(LoginResponse result) {
		if ("on".equals(result.getDutyFlag())) {
			SystemConfig.DUTYFLAG = "on";
		} else if ("off".equals(result.getDutyFlag())) {
			SystemConfig.DUTYFLAG = "off";
		} else if ("notonline".equals(result.getDutyFlag().toLowerCase())) {
			SystemConfig.DUTYFLAG = "off";
		} else {
			SystemConfig.DUTYFLAG = "on";
			handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
			Message message = Message.obtain();
			message.obj = "请先设置上下班状态！";
			message.what = ConstantValue.ERROE;
			handler.sendMessage(message);
		}
	}

	private void setQuanXian(LoginResponse result) {
		if ("1".equals(result.getIsXiepei())) {
			SystemConfig.UserRightIsAdvanced = false;
		} else if ("0".equals(result.getIsXiepei())) {
			SystemConfig.UserRightIsAdvanced = true;
		} else {
			Message message = Message.obtain();
			message.obj = "系统权限标识错误";
			message.what = ConstantValue.ERROE;
			handler.sendMessage(message);
		}
	}

	@Override
	public Integer getExit() {
		return null;
	}

	@Override
	public Integer getBackMain() {
		return null;
	};

	/**
	 * 设置GPS
	 */
	private AlertDialog	gAdSetGps;

	private void setGps() {
		if (gAdSetGps == null) {
			Builder builder = new Builder(context);
			builder.setIcon(R.drawable.mclaim_icon_small);
			builder.setTitle(R.string.app_name);
			builder.setMessage(R.string.please_open_gps);
			builder.setNegativeButton(R.string.menu_settings, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
				}
			});
			builder.setCancelable(false);
			gAdSetGps = builder.create();
		}
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled("gps")) {
			gAdSetGps.show();
		}
	}

}
