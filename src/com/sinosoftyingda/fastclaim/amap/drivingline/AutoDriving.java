package com.sinosoftyingda.fastclaim.amap.drivingline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 调用高德自动导航
 * 
 * @author DengGuang
 */
public class AutoDriving {

	private Context context;
	public AutoDriving(Context context) {
		super();
		this.context = context;
	}


	/**
	 * 高德导航地图
	 * @param lat
	 * @param lng
	 */
	public void gotoMapABC(double lat, double lng){
		try {
			Intent intent = new Intent(); 
			Bundle bundle = new Bundle();
			ComponentName comp = new ComponentName("com.mapabc.android.activity", "com.mapabc.android.activity.AutoNaviStartActicity" ); 
			bundle.putDouble("Latitude", lat);
			bundle.putDouble("Longitude", lng);
			intent.putExtras(bundle);
			intent.setComponent(comp);
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, "未安装高德导航软件！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
}
