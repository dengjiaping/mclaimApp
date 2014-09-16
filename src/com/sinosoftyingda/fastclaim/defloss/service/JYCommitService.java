package com.sinosoftyingda.fastclaim.defloss.service;

import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import android.content.Context;
import android.content.Intent;


/**
 * 精友同步/提交
 * @author DengGuang
 *
 */
public class JYCommitService {

	/**
	 * 定损提交完成
	 * 004：同步数据 005：提交数据
	 */
	public boolean onEvalFinish(Context context, String requestType,TopBtnSumbitDefloss topBtnSumbitDefloss) {
		Intent service = new Intent();
		
		service.setAction("com.jy.lioneye.service");
		service.addCategory("android.intent.category.DEFAULT");
		ClientCommitServiceConnection commitService = new ClientCommitServiceConnection(context, requestType,topBtnSumbitDefloss);
		boolean success = context.bindService(service, commitService, Context.BIND_AUTO_CREATE);
		if (success) { 
			//Toast.makeText(context, "请求豹眼服务器成功", Toast.LENGTH_LONG).show();
		} else {
			//Toast.makeText(context, "请求豹眼服务器失败", Toast.LENGTH_LONG).show();
		}
		
		return success;
	}
}
