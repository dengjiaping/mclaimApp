package com.sinosoftyingda.fastclaim.splash.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class SplashService {

	/***
	 * 获取程序的版本
	 * 
	 * @return
	 */
	public  static String getVersion(Context context) {
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

}
