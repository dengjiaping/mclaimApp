package com.sinosoftyingda.fastclaim.more.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jy.lioneye.client.request.EvalRequest;
import com.jy.lioneye.client.request.UserInfo;
import com.jy.lioneye.client.response.EvalResponse;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

/**
 * 精友离线数据更新
 * @author DengGuang
 *
 */
public class JYDataUPdataOfflineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		try {
			getOfflineData("008");
		} catch (Exception e) {
			e.printStackTrace();

			Toast.makeText(this, "未安装精友定损工具!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// 获取返回值
			GsonBuilder builder = new GsonBuilder();
			builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
			Gson gson = builder.create();
			// 获取定损信息
			String claimEvalData = data.getStringExtra("CLAIMEVAL_DATA");
			Log.e("responseData", claimEvalData);
			if (claimEvalData != null && !"".equals(claimEvalData.trim())) {
				EvalResponse evalResponse = gson.fromJson(claimEvalData, EvalResponse.class);
				if ("1".equals(evalResponse.getResponseCode())) {
					// 正常返回
					finish();
				} else if ("2".equals(evalResponse.getResponseCode())) {
					// 返回键
					Toast.makeText(this, evalResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
				} else if ("0".equals(evalResponse.getResponseCode())) {
					// 错误
					Toast.makeText(this, evalResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
				} else {
					// 未知错误，联系技术支持
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}

	/**
	 * 离线数据更新页面a
	 * "008"
	 */
	private void getOfflineData(String requestType) {
		Intent mIntent = new Intent();
		mIntent.setAction("com.jy.lioneye.DATA_MANAGER");
		mIntent.addCategory("android.intent.category.DEFAULT");
		
		// 用户信息
		String comCode = SystemConfig.loginResponse.getUserComCode();
		String handlerCode = SystemConfig.loginUserName;
		UserInfo userInfo = new UserInfo();
		userInfo.setComCode(comCode);
		userInfo.setHandlerCode(handlerCode);
		// 请求头
		EvalRequest evalRequest = new EvalRequest();
		evalRequest.setUserCode("jy");
		evalRequest.setPassword("jy");
		evalRequest.setRequestType(requestType);
		evalRequest.setPower("1111111111111");
		evalRequest.setUserInfo(userInfo);

		Bundle bundle = new Bundle();
		GsonBuilder builder = new GsonBuilder();
		builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		Gson gson = builder.create();
		String requestData = gson.toJson(evalRequest);
		bundle.putString("REQUEST_DATA", requestData);
		Log.i("REQUEST_DATA", requestData);
		
		mIntent.putExtras(bundle);
		startActivityForResult(mIntent, 1);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
