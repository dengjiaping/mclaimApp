package com.sinosoftyingda.fastclaim.common.mina.task;


import android.content.Context;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.mina.MinaClient;

/**
 * 建立链接
 * @author JingTuo
 *
 */
public class Connect implements Runnable {

	private MinaClient client;
	private Context context;
	
	public Connect(Context context, MinaClient client){
		this.context = context;
		this.client = client; 
	}
	
	@Override
	public void run() {
		// 判断有没有连接
		if(client.isConnected()){
			// 已经建立连接
			System.out.println("Connected...");
			TaskExecutor.getInstance().submitTask(new Online(client));
		}else{
			// 建立连接
			System.out.println("Connect...");
//			client.connect(context.getString(R.string.mina_ip), Integer.parseInt(context.getString(R.string.mina_port)));
			if(SystemConfig.loginResponse!=null){
//				client.connect(context.getString(R.string.mina_ip), Integer.parseInt(context.getString(R.string.mina_port)));
				client.connect(SystemConfig.loginResponse.getMinaIp(), Integer.parseInt(SystemConfig.loginResponse.getMinaPort()));
			}
			
			// 判断连接是否连接成功，上线
			if(client.isConnected()){
				// 校验建立成功
				System.out.println("Connect Success");
				TaskExecutor.getInstance().submitTask(new Online(client));
			}else{
				System.out.println("Connect failure");
			}
		}
	}
	
}
