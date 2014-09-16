package com.sinosoftyingda.fastclaim.common.utils;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class BlueToothUtils {
	
	
	/**
	 * 判断蓝牙设备是否打开
	 * Intent intent = new Intent();
	 * intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(intent, 2);
	 * @return
	 */
	public static boolean isOpen(){
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter!=null){
			return adapter.isEnabled();
		}
		return false;
	}
	
	/**
	 * 获取搜索到的蓝牙设备
	 * @param context
	 * @return
	 */
	public static Set<BluetoothDevice> getAllDevices(Context context){
		if(isOpen()){
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			Set<BluetoothDevice> devices = adapter.getBondedDevices();
			return devices;
		}else{
			return null;
		}
	}
}
