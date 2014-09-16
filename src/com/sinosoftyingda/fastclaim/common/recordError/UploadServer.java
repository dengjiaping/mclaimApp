package com.sinosoftyingda.fastclaim.common.recordError;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 上传异常后台服务
 * @author TanYanqiang
 */
public class UploadServer extends Service {
	private static final String addressURl = "http://192.168.1.110:8080";
	private String path;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		path = intent.getStringExtra("path");
		final File file = new File(path);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("doc", "doc");

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String log = Upload.getInstance().post(addressURl, params, file);
				Log.i("TAG", log);
			}
		});
		
		thread.start();
	}

}
