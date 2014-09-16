package com.sinosoftyingda.fastclaim.sketchmap.standtable;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Vibrator;

public class SketchTool {
	public static SimpleDateFormat formaterTime = new SimpleDateFormat(
			"yyyy_MM_dd_HH_mm_ss_SSS");
	public static SimpleDateFormat yyyymmddFormater = new SimpleDateFormat(
			"yyyyMMddhh");

	private SketchTool() {
	}

	public static String getTimeString() {
		Date date = new Date(System.currentTimeMillis());
		return formaterTime.format(date);
	}

	public static int getYYYYMMDDHH() {
		Date date = new Date(System.currentTimeMillis());
		return Integer.parseInt(yyyymmddFormater.format(date));
	}

	public static void virbrate() {
		Vibrator vibrator = (Vibrator)AccidentSketchActivity.getInstance().getSystemService(Context.VIBRATOR_SERVICE);    
		//long[] pattern = {0, 100}; // OFF/ON/OFF/ON...    
		vibrator.vibrate(SketchConfig.getVirbrate_time());
		//vibrator.vibrate(pattern, -1);
		//-1不重复，非-1为从pattern的指定下标开始重复    
	}
}
