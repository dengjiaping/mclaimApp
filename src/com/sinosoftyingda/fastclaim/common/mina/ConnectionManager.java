package com.sinosoftyingda.fastclaim.common.mina;

import android.content.Context;



public class ConnectionManager {
	
	
	private static MinaClient client;
	
	public synchronized static MinaClient getMinaClient(Context context){
		if(client==null){
			client = new MinaClient(context);
		}
		return client;
	}

	
	
}
