package com.sinosoftyingda.fastclaim.login.service;

import android.content.Context;
import android.content.Intent;

/**
 * 登录后请求精友工具
 * @author DengGuang
 *
 */
public class JYLoginService {

	private Context context;
	public JYLoginService(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 登录成功通知精友更新
	 *  000：登陆系统
	 *  009：退出系统
	 */
	public void noticeJYUpdatas(String requestType){
		Intent service = new Intent();
		service.setAction("com.jy.lioneye.ClientLoginService");
		service.addCategory("android.intent.category.DEFAULT");
		
		try {
			context.bindService(service, new ClientLoginServiceConnection(context, requestType), Context.BIND_AUTO_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
