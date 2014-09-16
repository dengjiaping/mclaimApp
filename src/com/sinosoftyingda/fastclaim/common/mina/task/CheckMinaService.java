package com.sinosoftyingda.fastclaim.common.mina.task;

import java.util.Timer;

import com.sinosoftyingda.fastclaim.common.mina.ConnectionManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 检查通道的服务
 * @author DengGuang
 */
public class CheckMinaService extends Service {
	// 实例化对象
	Context context = CheckMinaService.this;
	private Timer timer = new Timer();
		
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Mina", "CheckMinaService ----> onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("Mina", "CheckMinaService ----> onDestroy()");
		timer.cancel();
		stopSelf();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		/* 
		 * 定时刷新获取数据
		 * delay 开始后多久开始刷新
		 * long 隔多久开始刷新
		 */
		Log.d("Mina", "CheckMinaService ----> onStart()");
		Log.d("Mina", "===================>>  开始走定时器方法  <<===================");
		timer.schedule(new CheckMinaTimerTask(context, ConnectionManager.getMinaClient(context)), 0, 30*1000);
	}
}
