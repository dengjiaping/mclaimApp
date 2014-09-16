package com.sinosoftyingda.fastclaim.common.mina.task;

import java.util.TimerTask;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.mina.MinaClient;

import android.content.Context;
import android.util.Log;

/**
 * 定时检查长连接是否存在
 * @author DengGuang
 */
public class CheckMinaTimerTask extends TimerTask {

	private Context context;
	private MinaClient client;
	
	public CheckMinaTimerTask(Context context, MinaClient client){
		this.context = context;
		this.client = client;
	}
	
	@Override
	public void run() {
		// 判断有没有连接
		if(client.isConnected()){
			// 已经建立连接
			TaskExecutor.getInstance().submitTask(new Online(client));
			Log.i("Mina", "------------>连接成功");
		}else{
			// 建立连接
			Log.d("Mina", "------------>建立连接");
			if(SystemConfig.loginResponse!=null){
				Log.d("Mina", "------------>"+SystemConfig.loginResponse.getMinaIp()+":"+SystemConfig.loginResponse.getMinaPort());
				client.connect(SystemConfig.loginResponse.getMinaIp(), Integer.parseInt(SystemConfig.loginResponse.getMinaPort()));
			}else{
				Log.e("Mina", "------------>无Mina地址和端口信息");
			}
			
			// 判断连接是否连接成功，上线
			if(client.isConnected()){
				// 校验建立成功
				Log.i("Mina", "------------>已经连接成功");
				TaskExecutor.getInstance().submitTask(new Online(client));
			}else{
				Log.e("Mina", "------------>连接失败");
			}
		}
	}

}
