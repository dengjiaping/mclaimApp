package com.sinosoftyingda.fastclaim.common.definetime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

/**
 * 定时器
 * @author DengGuang
 */
public class TimeTimer extends TimerTask {

	private String defineTime;
	
	// 2013-10-10 10:10:10
	private static int d_t_yyyy = 0;
	private static int d_t_MM = 0;
	private static int d_t_dd = 0;

	private static int d_t_hh = 0;
	private static int d_t_mm = 0;
	private static int d_t_ss = 0;

	private static String s_t_yyyy = "1990";
	private static String s_t_MM = "01";
	private static String s_t_dd = "01";
	private static String s_t_hh = "10";
	private static String s_t_mm = "10";
	private static String s_t_ss = "10";
	
	public TimeTimer(String defineTime) {
		super();
		this.defineTime = defineTime;

		setTimeInitValue();
	}

	@Override
	public void run() {
		changeTime();
	}
	
	/**
	 * 获取各时间信息
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 */
	private int getTimeValue(String string, int start, int end){
		String findStr = string.substring(start, end);
		int timeValue = Integer.parseInt(findStr);
		return timeValue;
	}
	
	/**
	 * 设置单个时间的值
	 */
	private void setTimeInitValue(){
		// 截取日期数字
		//String dataT = DateTimeUtils.getCurrentTime2();
		//defineTime = "2012-12-31 23:59:50";
		//Log.e("TIMER", " ===>> "+defineTime);
		
		String dataT = defineTime;
		//add by yxf 20140122 reason:修改年月日截取方式，通过simpledateformat获取 begin
		SimpleDateFormat aa=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			date = aa.parse(dataT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		aa.applyPattern("yyyy");
		d_t_yyyy=Integer.parseInt(aa.format(date));
		
		aa.applyPattern("MM");
		d_t_MM=Integer.parseInt(aa.format(date));
		
		aa.applyPattern("dd");
		d_t_dd=Integer.parseInt(aa.format(date));
		
		aa.applyPattern("HH");
		d_t_hh=Integer.parseInt(aa.format(date));
		
		aa.applyPattern("mm");
		d_t_mm=Integer.parseInt(aa.format(date));
		
		aa.applyPattern("ss");
		d_t_ss=Integer.parseInt(aa.format(date));
		//add by yxf 20140122 reason:修改年月日截取方式，通过simpledateformat获取 end
		
//		d_t_yyyy = getTimeValue(dataT, 0, dataT.indexOf("-"));
//		d_t_MM = getTimeValue(dataT, dataT.indexOf("-")+1, dataT.lastIndexOf("-"));
//		d_t_dd = getTimeValue(dataT, dataT.lastIndexOf("-")+1, dataT.indexOf(" "));
//		
//		// 截取时间数字
//		String defineT = defineTime.substring(defineTime.indexOf(" ")+1, defineTime.length());
//		d_t_hh = getTimeValue(defineT, 0, defineT.indexOf(":"));
//		d_t_mm = getTimeValue(defineT, defineT.indexOf(":")+1, defineT.indexOf(":")+3);
//		d_t_ss = getTimeValue(defineT, defineT.lastIndexOf(":")+1, defineT.length());
	}
	
	private void changeTime(){
		d_t_ss = d_t_ss+1;
		if(d_t_ss >= 60){
			d_t_ss = 0;
			d_t_mm = d_t_mm+1;
		}
		if(d_t_mm >= 60){
			d_t_mm = 0;
			d_t_hh = d_t_hh+1;
		}
		if(d_t_hh >= 24){
			d_t_hh = 0; 
		}
		
		
		/** ************************************************************* */
		if(d_t_MM==2){	// 2
			if(isLeapYear(d_t_yyyy)){	// 闰年
				if(d_t_dd>=29 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
					d_t_dd = 1;
					d_t_MM = d_t_MM+1;
				}else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
					d_t_dd = d_t_dd+1;
				}
			}else{
				if(d_t_dd >= 28 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
					d_t_dd = 1;
					d_t_MM = d_t_MM+1;
				}else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
					d_t_dd = d_t_dd+1;
				}
			}
		}
		// 月份+1
		if(d_t_MM<=7 && d_t_MM%2 == 1){	// 1、3、5、7
			if(d_t_dd >= 31 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = 1;
				d_t_MM = d_t_MM+1;
			}// 日期+1
			else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = d_t_dd+1;
			}
		}
		if(d_t_MM>7 && d_t_MM%2 == 1){	// 9、11
			if(d_t_dd >= 30 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = 1;
				d_t_MM = d_t_MM+1;
			}else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = d_t_dd+1;
			}
		}
		if(d_t_MM>2 && d_t_MM<7 && d_t_MM%2 == 0){	// 4、6
			if(d_t_dd >= 30 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = 1;
				d_t_MM = d_t_MM+1;
			}else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = d_t_dd+1;
			}
		}
		if(d_t_MM>7 && d_t_MM%2 == 0){	// 8、10、12
			if(d_t_dd >= 31 && d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				// 年+1
				if(d_t_MM == 12){// 12
					d_t_dd = 1;
					d_t_MM = 1;
					d_t_yyyy = d_t_yyyy+1;
				}
			}else if(d_t_ss==0 && d_t_mm==0 && d_t_hh==0){
				d_t_dd = d_t_dd+1;
			}
		}
		
		
		// 年
		s_t_yyyy = ""+d_t_yyyy;
		// 月
		if(d_t_MM>=0 && d_t_MM<10){
			s_t_MM = "0"+d_t_MM;
		}else{
			s_t_MM = ""+d_t_MM;
		}
		// 日
		if(d_t_dd>=0 && d_t_dd<10){
			s_t_dd = "0"+d_t_dd;
		}else{
			s_t_dd = ""+d_t_dd;
		}
		// 时
		if(d_t_hh>=0 && d_t_hh<10){
			s_t_hh = "0"+d_t_hh;
		}else{
			s_t_hh = ""+d_t_hh;
		}
		// 分
		if(d_t_mm>=0 && d_t_mm<10){
			s_t_mm = "0"+d_t_mm;
		}else{
			s_t_mm = ""+d_t_mm;
		}
		// 秒
		if(d_t_ss>=0 && d_t_ss<10){
			s_t_ss = "0"+d_t_ss;
		}else{
			s_t_ss = ""+d_t_ss;
		}
		
		SystemConfig.serverTime = s_t_yyyy+"-"+s_t_MM+"-"+s_t_dd+" "+s_t_hh+":"+s_t_mm+":"+s_t_ss;
		//Log.i("TIMER", " ----> "+SystemConfig.serverTime);
	}
	

	private boolean isLeapYear(int year) {
		boolean isLeap = false;
		isLeap = (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
		return isLeap;
	}

}
