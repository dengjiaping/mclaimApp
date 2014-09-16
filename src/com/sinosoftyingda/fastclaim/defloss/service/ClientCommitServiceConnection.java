package com.sinosoftyingda.fastclaim.defloss.service;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jy.lioneye.client.request.EvalRequest;
import com.jy.lioneye.client.response.EvalLossInfo;
import com.jy.lioneye.client.response.EvalResponse;
import com.jy.lioneye.client.service.LioneyeService;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;

/**
 * 定损同步/提交请求
 * 
 * @author DengGuang
 * 
 */
public class ClientCommitServiceConnection implements ServiceConnection {
	private String requestCode;
	private Context context;
	private TopBtnSumbitDefloss topBtnSumbitDefloss;

	public ClientCommitServiceConnection(Context context, String requestCode,
			TopBtnSumbitDefloss topBtnSumbitDefloss) {
		this.requestCode = requestCode;
		this.context = context;
		this.topBtnSumbitDefloss = topBtnSumbitDefloss;
	}

	@Override
	public void onServiceConnected(ComponentName arg0, final IBinder arg1) {
		Log.i("JSON", "连接成功");

		new AsyncTask<String, Void, String>() {
			ProgressDialog progressDialog = null;

			@Override
			protected void onPreExecute() {
				// progressDialog = ProgressDialog.show(EvalTestActivity.this,
				// "上传定损数据", "正在上传数据，请稍后...");
			}

			@Override
			protected String doInBackground(String... parms) {
				String responseData = null;
				Log.i("TAG", "连接成功");
				GsonBuilder buidler = new GsonBuilder();
				buidler.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
				Gson gson = buidler.create();

				// 1.获取豹眼服务
				LioneyeService lioneyeService = LioneyeService.Stub
						.asInterface(arg1);
				// 2.封装请求数据
				String lossNo = SystemConfig.LOSSNO;
				String reportNo = SystemConfig.PHOTO_CLAIMNO;
				String taskId = reportNo;
				// 请求头
				EvalRequest evalRequest = new EvalRequest();
				evalRequest.setUserCode("jy");
				evalRequest.setPassword("jy");
				evalRequest.setRequestType(requestCode);
				evalRequest.setPower("1111111111111");
				// 请求体
				EvalLossInfo evalLossInfo = new EvalLossInfo();
				evalLossInfo.setLossNo(lossNo);
				evalLossInfo.setReportNo(reportNo);
				evalLossInfo.setEstimateNo(reportNo);
				evalLossInfo.setTaskId(taskId);
				evalRequest.setEvalLossInfo(evalLossInfo);

				// 请求报文
				String requestData = gson.toJson(evalRequest);
				// 3.请求豹眼服务，上传
				try {
					responseData = lioneyeService.evalFinish(requestData);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return responseData;
			}

			@Override
			protected void onPostExecute(String responseData) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				GsonBuilder buidler = new GsonBuilder();
				buidler.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
				Gson gson = buidler.create();
				// 4.解析请求数据
				EvalResponse evalResponse = gson.fromJson(responseData, EvalResponse.class);
				if (evalResponse != null) {
					if ("1".equals(evalResponse.getResponseCode())) {
						SystemConfig.JYCommit = true;
						Toast.showToast(context, "精友同步/提交成功！");
						if ("004".equals(requestCode)) {
							topBtnSumbitDefloss.synchOrSubmit("synch");
						} else {
							topBtnSumbitDefloss.synchOrSubmit("submit");
						}

					} else {
						SystemConfig.JYCommit = false;
						Toast.showToast(context, "精友系统交互返回错误代码："+evalResponse.getResponseCode());
					}
				} else {
					Toast.showToast(context, "精友系统交互未知错误");
				}
			}

		}.execute();
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
