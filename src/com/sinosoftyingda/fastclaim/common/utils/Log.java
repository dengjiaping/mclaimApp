package com.sinosoftyingda.fastclaim.common.utils;

/***
 * 一个具有开关的日志工具 代替系统的Log类,当LOGLEVEL等于0时， 
 * 程序中不显示log,当LOGLEVEL大于7时，程序中显示log
 * 
 * @author chenjianfan
 * 
 */
public class Log {
	private static int LOGLEVEL = 7;
	private static int VERBOSE = 1;
	private static int DEBUG = 2;
	private static int INFO = 3;
	private static int WARN = 4;
	private static int ERROR = 5;

	public static void v(String tag, String msg) {
		if (LOGLEVEL > VERBOSE)
			android.util.Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (LOGLEVEL > DEBUG)
			android.util.Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (LOGLEVEL > INFO)
			android.util.Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (LOGLEVEL > WARN)
			android.util.Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (LOGLEVEL > ERROR)
			android.util.Log.e(tag, msg);
	}

}
