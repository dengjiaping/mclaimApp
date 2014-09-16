package com.sinosoftyingda.fastclaim.common.mina.task;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.mina.MinaClient;

import android.content.Context;


public class Offline implements Runnable {

	private Context context;
	private MinaClient client;
	
	public Offline(Context context, MinaClient client){
		this.context = context;
		this.client = client; 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Offline...");
		if(client.isOnline()){
			client.sendMsg(client.createOfflineRequest(SystemConfig.USERLOGINNAME));
			client.close();
		}
	}
	
}
