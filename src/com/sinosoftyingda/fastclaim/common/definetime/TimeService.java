package com.sinosoftyingda.fastclaim.common.definetime;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

/**
 * 时钟服务
 * @author DengGuang
 */
public class TimeService extends Service {

	private Timer	timer	= new Timer(); 
	private String defineTime;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("TIMER", " ----> onCreate()");
		SystemConfig.serverTimeIsRunning = true;
		
		defineTime = SystemConfig.serverTime;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TIMER", " ----> onDestroy()");
		SystemConfig.serverTimeIsRunning = false;
		
		timer.cancel();
		stopSelf();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("TIMER", " ----> onStart()");
		SystemConfig.serverTimeIsRunning = true;
		
		/*
		 * 定时刷新获取数据 delay 开始后多久开始刷新 long 隔多久开始刷新
		 */
		timer.schedule(new TimeTimer(defineTime), 0, 1000);
	}
	
}
