package com.sinosoftyingda.fastclaim.survey.config;

import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;

/**
 * 校验 查勘页面值
 * 
 * @author DengGuang
 *
 */
public class CheckSurveyValue {

	/**
	 * 驾驶员信息-身份证号码是否为空
	 * true 为空
	 * false 不为空
	 */
	public static boolean idCardIsNull = true;
	
	/**
	 * 是否存在手写签名
	 * true 存在
	 * false 不存在
	 */
	public static boolean isSignature = false;
	
	/**
	 * 是否保存草图
	 * true 存在
	 * false 不存在
	 */
	public static boolean isAddAccident = false;
	
	/**
	 * 修理厂合作性质
	 *    true 合作
	 *    false 非合作
	 * @param reason
	 * @return
	 */
	public static boolean isComplate(String reason){
		
		if(reason.trim().equals("")){
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 查找本地是否存在精友数据
	 * @param reportNo
	 * @param lossNo
	 */
	public boolean findJYData(String reportNo, String lossNo){
		boolean isQuestJYTools = false;
		isQuestJYTools = DeflossSynchroAccess.findJYDatas(reportNo, lossNo); 
		return isQuestJYTools;
	}
	
	
	/**
	 * 判断身份证号码为空
	 * @return
	 */
	public static boolean idCard(String idCard){
		if(idCard.equals("")){
			return true;
		}
		return false;
	}
}
