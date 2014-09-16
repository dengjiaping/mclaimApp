package com.sinosoftyingda.fastclaim.common.utils;

import android.content.Context;


/***
 * 代替系统的Toast
 * 
 * @author chenjianfan
 * 
 */
public class Toast {

	/**
	 * 控制测试Toast显示
	 */
	private static boolean isShow = true;

	/***
	 * 需要在程序中弹出给用户提示的
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show();
	}

	/**
	 * 需要在程序中弹出给用户提示的
	 * 
	 * @param context
	 * @param msgResId
	 */
	public static void showToast(Context context, int msgResId) {
		android.widget.Toast.makeText(context, msgResId, android.widget.Toast.LENGTH_LONG).show();
	}

	/**
	 * 这个是测试用的 不需要给用户看到的
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastTest(Context context, String msg) {
		if (isShow) {
			android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show();
		}
	}

}
