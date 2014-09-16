package com.sinosoftyingda.fastclaim.common.db.dao;

import android.content.ContentValues;
import android.provider.ContactsContract.Contacts.Data;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;

/**
 * 任务信息主表
 * @author DengGuang
 */
public class TblTaskinfo {

	/**
	 * 查询查勘到达现场时间
	 * @param registNo
	 * @return
	 */
	public static String queryCheckArriveTime(String registNo){
		ContentValues checkValues = DBUtils.find("TASKQUERY", new String[] { "ARRIVESCENETIME" }, "REGISTNO=?", new String[] { registNo });
		String checkArrivetime = "";
		if(checkValues != null && checkValues.get("ARRIVESCENETIME")!=null){
			checkArrivetime = checkValues.get("ARRIVESCENETIME").toString();
		}
//		else{
//			checkArrivetime = SystemConfig.serverTime;
//		}
		return checkArrivetime;
	}
	
	
	/**
	 * 校验是否拍照  true
	 * @param registNo
	 * @return
	 */
	public static boolean checkIsTakephotos(String registNo){
		boolean isTakephotos = false;
		String arriveTime = queryCheckArriveTime(registNo);
		if(arriveTime == null || arriveTime.equals("")){
			isTakephotos = true;
		}
		
		return isTakephotos;
	}
}
