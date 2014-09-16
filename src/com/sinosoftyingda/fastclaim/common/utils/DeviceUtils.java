package com.sinosoftyingda.fastclaim.common.utils;

import java.util.Random;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 本类主要用于获取设备的一些固有信息
 * @author JingTuo
 *
 */
public class DeviceUtils {
	
	private static String sDeviceId;
	
	/**
	 * 获得当前设备ID
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if(deviceId==null||deviceId.equals("")){//针对无法获得设备Id，获取一个随机数来表示本设备的Id
			if(sDeviceId==null||sDeviceId.equals("")){
				Random random = new Random(System.currentTimeMillis());
				sDeviceId = "" + random.nextLong();
			}
			return sDeviceId;
		}else{
			return deviceId;
		}
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static String getSimOperator(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimOperator();
	}
}
