package com.sinosoftyingda.fastclaim.more.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sinosoftyingda.fastclaim.common.utils.Log;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class MoreServlet {
	private Context context;

	public MoreServlet(Context context) {
		this.context = context;
	}

	/***
	 * 获取程序的版本
	 * 
	 * @return
	 */
	public String getVersion() {
		String versionString = null;
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageinfo = pm.getPackageInfo(context.getPackageName(), 0);
			versionString = packageinfo.versionName;
			return versionString;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 一般不会发生异常
			return null;
		}
	}

	/***
	 * 安装
	 * 
	 * @param file
	 */
	public void installApp(File file) {
		if (file != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

	/**
	 * 下载文件的操作
	 * 
	 * @param urlPath
	 *            文件的url路径
	 * @return
	 */
	public File download(String urlPath, ProgressDialog pd) {
		try {
			URL url = new URL(urlPath);
			int start = urlPath.lastIndexOf("/") + 1;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int total = conn.getContentLength();
			pd.setMax(total);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
					Log.i("MoreUoloadUtils", "手机sd没有安装上了");
					throw new RuntimeException("");
				}
				File file = new File(Environment.getExternalStorageDirectory(), urlPath.substring(start, urlPath.length()));
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				int process = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					process += len;
					pd.setProgress(process);
				}
				fos.flush();
				fos.close();
				is.close();
				return file;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
