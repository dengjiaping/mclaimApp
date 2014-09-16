package com.sinosoftyingda.fastclaim.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 获取系统时间类，对一些的常用的时间操作的封装
 * 
 * @author JingTuo
 */
public class DateTimeUtils {
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMM = "yyyyMM";
	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static final String yyyy_MM_dd1HH_mm_ss = "yyyy-MM-dd/HH:mm:ss";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

	public static Date parseStringToDate(String dateString, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.parse(dateString);
	}

	public static String parseDateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 如果start==end，则返回0<br>
	 * 如果start>end,则返回负值<br>
	 * 如果start<end,则返回正值
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDays(Date start, Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Calendar startC = Calendar.getInstance();
		Calendar endC = Calendar.getInstance();
		startC.setTime(start);
		endC.setTime(end);

		startC.clear(Calendar.HOUR_OF_DAY);
		startC.clear(Calendar.MINUTE);
		startC.clear(Calendar.SECOND);
		startC.clear(Calendar.MILLISECOND);

		endC.clear(Calendar.HOUR_OF_DAY);
		endC.clear(Calendar.MINUTE);
		endC.clear(Calendar.SECOND);
		endC.clear(Calendar.MILLISECOND);
		if (startC.after(endC)) {
			int days = 0;
			while (endC.before(startC)) {
				endC.add(Calendar.DAY_OF_YEAR, 1);
				days++;
			}
			return 0 - days;
		} else {
			int days = 0;
			while (startC.before(endC)) {
				startC.add(Calendar.DAY_OF_YEAR, 1);
				days++;
			}
			return days;
		}
	}

	/**
	 * 将秒数转换成如下格式：<br>
	 * 00:00:00、00:00<br>
	 * 如果>=1天，则转换成：<br>
	 * >=1天
	 * 
	 * @param seconds
	 * @return
	 */
	public static String format(int seconds) {
		int hours = 0;
		int minutes = 0;
		if (seconds >= 0 && seconds < 60) {

		} else if (seconds >= 60 && seconds < 3600) {
			minutes = seconds / 60;
			seconds = seconds % 60;
		} else if (seconds >= 3600 && seconds < 24 * 3600) {
			hours = seconds / 3600;
			seconds = seconds % 3600;
			minutes = seconds / 60;
			seconds = seconds % 60;
		} else {
			return ">=1天";
		}
		String result = "";
		if (hours > 0 && hours <= 9) {
			result += "0" + hours + ":";
		} else if (hours >= 10) {
			result += hours + ":";
		}
		if (minutes >= 0 && minutes <= 9) {
			result += "0" + minutes + ":";
		} else {
			result += minutes + ":";
		}

		if (seconds >= 0 && seconds <= 9) {
			result += "0" + seconds;
		} else {
			result += seconds;
		}
		return result;
	}

	public static long getDaySub(String startTime, String endTime) {
		long day = 0;
		try {
			Date beginDate = new SimpleDateFormat("yyyy-mm-dd").parse(startTime);
			Date endDate = new SimpleDateFormat("yyyy-mm-dd").parse(endTime);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return day;
	}

//	/****
//	 * 获取2个日期直接所有的日期（包括这2个日期）
//	 * 
//	 * @author chenjianfan
//	 * @param date1
//	 * @param date2
//	 */
//	public static ArrayList<String> get2DateBetween(String date1, String date2) {
//		ArrayList<String> arrayList = new ArrayList<String>();
//		arrayList.add("     " + date1);
//		String tmp;
//		if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
//			tmp = date1;
//			date1 = date2;
//			date2 = tmp;
//		}
//		tmp = format.format(str2Date(date1).getTime() + 3600 * 24 * 1000);
//		int num = 0;
//		while (tmp.compareTo(date2) < 0) {
//			arrayList.add("     " + tmp);
//			num++;
//			tmp = format.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
//		}
//		return arrayList;
//	}

//	private static Date str2Date(String str) {
//		if (str == null)
//			return null;
//		try {
//			return format.parse(str);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

//	/****
//	 * 获取系统当前的时间
//	 * 
//	 * @author chenjianfan
//	 * @return
//	 */
//	public static String getCurrentTime() {
//		Calendar calendar = Calendar.getInstance();
//		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		return format.format(calendar.getTime());
//	}

	/****
	 * 获取系统当前的时间的日期和小时
	 * 
	 * @author chenjianfan
	 * @return
	 */
	public static String getCurrentTime2() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(calendar.getTime());
	}

//	/***
//	 * 比较2个日期的大小
//	 * 
//	 * @param date1
//	 * @param date2
//	 * @return 0 相等 1：date1大于 date2 -1：date1笑小于 date2
//	 * @author chenjianfan
//	 */
//	public static int dateCompare(String date1, String date2) {
//		int result = 0;
//		Calendar calendar1 = Calendar.getInstance();
//		Calendar calendar2 = Calendar.getInstance();
//		try {
//			calendar1.setTime(format.parse(date1));
//			calendar2.setTime(format.parse(date2));
//		} catch (ParseException e) {
//			Log.i("DateTimeUtils", "时间格式不正确");
//			e.printStackTrace();
//		}
//		int result1 = calendar1.compareTo(calendar2);
//		if (result1 == 0)
//			result = 0;
//		if (result1 > 0)
//			result = 1;
//		if (result1 < 0)
//			result = -1;
//		return result;
//	}

	/**
	 * 由于业务需要，需要将时间转换成秒
	 * 
	 * @param time
	 *            HH:mm:ss
	 * @return
	 */
	public static int toSeconds(String time) {
		if (time == null || time.equals("")) {
			return 0;
		}
		String[] array = time.split(":");
		if (array.length == 1) {
			return Integer.parseInt(array[0]);
		} else if (array.length == 2) {
			return Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1]);
		} else {
			return Integer.parseInt(array[array.length - 3]) * 3600 + Integer.parseInt(array[array.length - 2]) * 60
					+ Integer.parseInt(array[array.length - 1]);
		}
	}

}
