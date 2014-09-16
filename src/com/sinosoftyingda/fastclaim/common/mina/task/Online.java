package com.sinosoftyingda.fastclaim.common.mina.task;


import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.mina.MinaClient;

public class Online implements Runnable {

	private MinaClient client;
	
	public Online(MinaClient client){
		this.client = client; 
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Online...");
		if(client.isConnected()){
			if(!client.isOnline()){
				client.sendMsg(client.createOnlineRequest(SystemConfig.USERLOGINNAME));
			}
		}
	}
	
}
