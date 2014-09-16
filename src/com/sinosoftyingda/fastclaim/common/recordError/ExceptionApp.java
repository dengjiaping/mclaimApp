package com.sinosoftyingda.fastclaim.common.recordError;

import android.app.Application;

/**
 * 程序异常监控
 * @author DengGuang
 */
public class ExceptionApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		// 日志记录
		ExceptionMesHandler crashHandler = ExceptionMesHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
}
