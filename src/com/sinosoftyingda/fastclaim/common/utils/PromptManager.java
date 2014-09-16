package com.sinosoftyingda.fastclaim.common.utils;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.definetime.TimeService;
import com.sinosoftyingda.fastclaim.common.mina.ConnectionManager;
import com.sinosoftyingda.fastclaim.common.mina.MinaClient;
import com.sinosoftyingda.fastclaim.common.mina.NoticeManager;
import com.sinosoftyingda.fastclaim.login.service.JYLoginService;

/***
 * 统一管理系统提示用户的内容
 * 
 * @author chenjianfan
 * 
 */
public class PromptManager {

	private ShowDialogPositiveButton positiveButton;
	private ShowDialogGaiPai showDialogDaiPai;

	public interface ShowDialogGaiPai {
		/****
		 * 按钮确定操作
		 */
		void setPositiveButton(String reason);

		/****
		 * 按钮取消操作
		 */
		void setNegativeButton();
	}

	public interface ShowDialogPositiveButton {
		/****
		 * 按钮确定操作
		 */
		void setPositiveButton();

		/***
		 * 取消操作
		 */
		void setNegativeButton();
	}

	/**
	 * 显示退出对话框
	 * 
	 * @param context
	 */
	public static void showExitDialog(final Context context) {

		new AlertDialog.Builder(context).setIcon(R.drawable.mclaim_icon_small).setTitle(R.string.app_name).setMessage(R.string.is_exit)
				.setPositiveButton(context.getString(R.string.is_positive), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						SystemConfig.isExit = true;						
						// 通知精友退出
						try {
							JYLoginService jyLoginService = new JYLoginService(context);
							jyLoginService.noticeJYUpdatas("009");
						} catch (Exception e) {
							e.printStackTrace();
							Toast.showToast(context, "未安装精友定损工具");
						}

						// 关闭时钟服务 
						TimeService task = new TimeService();
						task.onDestroy();											// 关闭线程
						context.stopService(new Intent(context, TimeService.class));			// 结束后台服务
						
						// 对缓存数据进行管理
						// 断开常连接
						if (progressDialog == null) {
							progressDialog = new ProgressDialog(context);
							progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
							progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setMessage("正在退出");
							progressDialog.setCancelable(false);
							progressDialog.setCanceledOnTouchOutside(false);
							progressDialog.show();
						} else {
							if (!progressDialog.isShowing()) {
								progressDialog.show();
							}
						}
						new Thread() {
							@Override
							public void run() {
								MinaClient client = ConnectionManager.getMinaClient(context);
								if (client.isConnected()) {
									if (client.isOnline()) {
										client.sendMsg(client.createOfflineRequest(SystemConfig.USERLOGINNAME));
										client.close();
										client.setOnline(false);
										NoticeManager.getInstance(context).cancelAll();
										handler.sendMessage(Message.obtain());
									}
								} else {
									NoticeManager.getInstance(context).cancelAll();
									handler.sendMessage(Message.obtain());
								}
							};
						}.start();
					}
				}).setNegativeButton(context.getString(R.string.is_negative), null).show();
	}

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog.cancel();
				progressDialog = null;
			}
			SystemConfig.loginResponse = null;//清空登陆历史 add by jingtuo 
			android.os.Process.killProcess(android.os.Process.myPid());
		};
	};

	private static ProgressDialog progressDialog;

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new AlertDialog.Builder(context).setIcon(R.drawable.mclaim_icon_small).setTitle(R.string.app_name).setMessage(msg)
				.setNegativeButton(context.getString(R.string.is_positive), null).show();
	}
	/**
	 * 更新提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showUpdateDialog(Context context, String msg,DialogInterface.OnClickListener onClick) {
		new AlertDialog.Builder(context).setIcon(R.drawable.mclaim_icon_small).setTitle(R.string.app_name).setMessage(msg)
				.setNegativeButton(context.getString(R.string.is_positive),onClick).show();
	}

	/**
	 * 设置简单的进度框
	 * 
	 * @param progressDialog
	 *            需要展示的进度框
	 * @param msg
	 *            需要展示内容
	 */
	public static void showSimpleProgressDialog(ProgressDialog progressDialog, String msg) {
		
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.setIcon(R.drawable.mclaim_icon_small);
			progressDialog.setTitle(R.string.app_name);
			progressDialog.setMessage(msg);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}

	/**
	 * 关闭当前进度条
	 * 
	 * @param progressDialog
	 */
	public static void closeProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 显示一个对话框
	 * @param context
	 * @param msg	提示信息
	 * @param leftTextId	底部左侧按钮内容
	 * @param rightTextId	底部右侧按钮内容
	 * @param positiveButton	底部按钮事件
	 */
	public void showDialog(Context context, String msg,int leftTextId, int rightTextId, final ShowDialogPositiveButton positiveButton) {

		this.positiveButton = positiveButton;
		
		new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(msg).setIcon(R.drawable.mclaim_icon_small).setPositiveButton(leftTextId, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 点击“确认”后的操作
				positiveButton.setPositiveButton();

			}
		}).setNegativeButton(rightTextId, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 点击“返回”后的操作,这里不设置没有任何操作
				positiveButton.setNegativeButton();
			}
		}).show();

	}
	
	private MyOnCheckedChangeListener moccl;

	public void showSingnRadio(final Context context, final ShowDialogGaiPai showDialogDaiPai) {
		this.showDialogDaiPai = showDialogDaiPai;
		LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.dialog_gaipai, null);
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("改派原因");
		builder.setIcon(R.drawable.mclaim_icon_small);
		builder.setView(view);
		
		final RadioGroup rg = (RadioGroup) view.findViewById(R.id.dialog_gaipai_item_rd);
		
		moccl = new MyOnCheckedChangeListener();
		rg.setOnCheckedChangeListener(moccl);

		final EditText et = (EditText) view.findViewById(R.id.gaipai_et);
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				RadioButton rb1 = (RadioButton)rg.findViewById(R.id.dialog_gaipai_item_rd1);
				RadioButton rb2 = (RadioButton)rg.findViewById(R.id.dialog_gaipai_item_rd2);
				RadioButton rb3 = (RadioButton)rg.findViewById(R.id.dialog_gaipai_item_rd3);
				if(arg0.toString().equals("")){
					rb1.setEnabled(true);
					rb2.setEnabled(true);
					rb3.setEnabled(true);
				}else{					
					rb1.setChecked(false);
					rb2.setChecked(false);
					rb3.setChecked(false);
					rb1.setEnabled(false);
					rb2.setEnabled(false);
					rb3.setEnabled(false);
				}
			}
		});
		builder.setPositiveButton(R.string.is_positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				String reason = et.getText().toString().trim();
				if (TextUtils.isEmpty(reason)){
					if(TextUtils.isEmpty(moccl.getResult())){
						Toast.showToast(context, "请输入改派原因或选择一个改派原因");
					}else{
						showDialogDaiPai.setPositiveButton(moccl.getResult());
					}
				}else{
					showDialogDaiPai.setPositiveButton(reason);
				}	
			}
		});
		builder.setNegativeButton(R.string.is_negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

				showDialogDaiPai.setNegativeButton();
			}
		}).create().show();

	}
	
	/**
	 * 
	 * @author JingTuo
	 *
	 */
	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		private String result = "";
		
		public MyOnCheckedChangeListener(){
			
		}
		
		@Override
		public void onCheckedChanged(RadioGroup group, int resId) {
			// TODO Auto-generated method stub
			switch (resId) {
			case R.id.dialog_gaipai_item_rd1:
				// 道路
				result = "道路拥堵,无法到达现场。";
				break;
			case R.id.dialog_gaipai_item_rd2:
				// 车坏了
				result = "车辆损坏,无法到达现场。";
				break;
			case R.id.dialog_gaipai_item_rd3:
				// 车维修中
				result = "车辆维修中,无法到达现场。";
				break;
			default:
				break;
			}
		}

		public String getResult() {
			return result;
		}		
		
	}

}
