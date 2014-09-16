package com.sinosoftyingda.fastclaim.login.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jy.lioneye.client.request.EvalRequest;
import com.jy.lioneye.client.request.UserInfo;
import com.jy.lioneye.client.service.ClientLoginService;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

/**
 * 登录请求
 * @author DengGuang
 *
 */
public class ClientLoginServiceConnection implements ServiceConnection {
	private String requestCode;
	private Context context;

	public ClientLoginServiceConnection(Context context, String requestCode) {
		this.requestCode = requestCode;
		this.context = context;
	}

	@Override
	public void onServiceConnected(ComponentName arg0, final IBinder binder) {
		Log.i("JSON", "连接成功");
		// 1.获取豹眼服务
		ClientLoginService clientLoginService = ClientLoginService.Stub.asInterface(binder);
		// 2.封装请求数据
		String comCode = "14";
		String handlerCode= "000000000";
		if(SystemConfig.loginResponse != null){
			comCode = SystemConfig.loginResponse.getUserComCode();
			handlerCode = SystemConfig.loginResponse.getUserName();
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setComCode(comCode);
		userInfo.setHandlerCode(handlerCode);
		// 请求头
		EvalRequest evalRequest = new EvalRequest();
		evalRequest.setUserCode("jy");
		evalRequest.setPassword("jy");
		evalRequest.setRequestType(this.requestCode);
		evalRequest.setPower("1111111111111");
		evalRequest.setUserInfo(userInfo);
		
		// 请求报文
		GsonBuilder buidler = new GsonBuilder();
		buidler.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		Gson gson = buidler.create();
		String requestData = gson.toJson(evalRequest);
		// 3.请求豹眼服务，上传
		try {
			String responseData = clientLoginService.login(requestData);
			Log.e("JSON", responseData);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		unbind();
	}

	/**
	 * 释放绑定
	 */
	private void unbind() {
		Intent service = new Intent();
		service.setAction("com.jy.lioneye.ClientLoginService");
		service.addCategory("android.intent.category.DEFAULT");
		
		// 断开连接
		context.unbindService(this);
	}
};
