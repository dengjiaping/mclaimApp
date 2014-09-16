package com.sinosoftyingda.fastclaim.maintask.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;

/**
 * 拨打电话
 * @author DengGuang
 */
public class CallPhoneUtil {

	/** 来电:1 */
	public static final int			INCOMING_TYPE		= 1;
	/** 拨出:2 */
	public static final int			OUTGOING_TYPE		= 2;
	/** 未接:3 */
	public static final int			MISSED_TYPE			= 3;
	private Context context;
	
	public CallPhoneUtil(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 查找拨打电话的任务状态
	 * @param registNo
	 */
	public void queryCallHistroty(String registNo){
	    ContentResolver cr = context.getContentResolver();
	    final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
	                             new String[]{
	    							CallLog.Calls.NUMBER,
	    							CallLog.Calls.DURATION},
	    							CallLog.Calls.TYPE+"=?", new String[]{OUTGOING_TYPE+""},
	    							CallLog.Calls.DEFAULT_SORT_ORDER);
	    
	    // 查询是否有结果
	    if (cursor.moveToNext()) { 
		    String strNumber = cursor.getString(0);    	// 呼叫号码 
		    long duration = cursor.getLong(1);		 	// 通话时长
		    
		    // 校验最后一次是否拨打电话
		    MainTaskUtils.lastCallTime = getLastCallphoneDate();
		    boolean isCallPhone = false;
		    isCallPhone = checkIsCallphone(MainTaskUtils.firstCallTime, MainTaskUtils.lastCallTime);
			// 通话时长大于1
			if(duration > 0 && isCallPhone){
				insertCallTime(registNo, SystemConfig.serverTime);
				SystemConfig.registNoCallphone = "";
			}
		}else{
			SystemConfig.registNoCallphone = "";
		}
	}
	
	/**
	 * 离开查询最后一条记录
	 * @return
	 */
	public String getLastCallphoneDate(){
		String lastCallTime = "";
		ContentResolver cr = context.getContentResolver();
	    final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
	                             new String[]{CallLog.Calls.DATE},
	    							null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
	    // 查询是否有结果
	    if (cursor.moveToNext()) { 
	    	lastCallTime = cursor.getString(0);    //呼叫号码 
		    Log.e("CALLPHONE", "时间----------->"+lastCallTime);
		}
	    
	    return lastCallTime;
	}
	
	/**
	 * 比对两次打电话时间
	 * @param firstTime
	 * @param lastTime
	 * @return
	 */
	public boolean checkIsCallphone(String firstTime, String lastTime){
		// 判断两个时间是否一致
		if(!firstTime.equals(lastTime)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 插入联系客户时间
	 * @param registNo
	 * @param callTime
	 */
	public void insertCallTime(String registNo, String callTime){
		// 插入查勘环节时间
		CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
		if (checkTask!=null && "".equals(checkTask.getContacttime())) {
			CheckTaskAccess.update(registNo, null, null, null, null, null, Integer.valueOf(1), callTime);
		}
		
		// 插入定损环节时间
		CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, -100);
		if (certainLossTask!= null && "".equals(certainLossTask.getContacttime())) {
			CertainLossTaskAccess.update(registNo, -100, null, null, null, null, null, Integer.valueOf(1), callTime);
		}
	}
}
