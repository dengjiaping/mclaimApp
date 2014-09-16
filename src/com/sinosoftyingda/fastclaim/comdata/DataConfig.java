package com.sinosoftyingda.fastclaim.comdata;

import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;

public class DataConfig {

	/**
	 * chenjianfan add 查勘静态变量
	 */
	public static DetailTaskQueryResponse tblTaskQuery = DetailTaskQueryResponse.getDetailTaskQueryResponse();
	/**
	 * chenjianfan add 定损静态变量
	 * 
	 * 
	 */
	public static DefLossInfoQueryResponse defLossInfoQueryData = null;
//	/**
//	 * haoyun add
//	 *	定损提交对象 
//	 */
//	public static VerifyLossSubmitRequest verifyLossSubmitRequest=VerifyLossSubmitRequest.getVerifyLossSubmitRequest();//定损同步 提交对象


	/**
	 * 测试用户调用此方法 （不用考虑该方法）
	 */
	public static void initDataValue() {
		tblTaskQuery = DetailTaskQueryResponse.getDetailTaskQueryResponse();;
//		defLossInfoQueryData = DefLossInfoQueryResponse.getDefLossInfoQueryResponse();

	}
}
