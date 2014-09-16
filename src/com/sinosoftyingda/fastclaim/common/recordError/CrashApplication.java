package com.sinosoftyingda.fastclaim.common.recordError;

import android.app.Application;

public class CrashApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ExceptionMesHandler crashHandler = ExceptionMesHandler.getInstance();
		crashHandler.init(getApplicationContext()); 
	}
}
