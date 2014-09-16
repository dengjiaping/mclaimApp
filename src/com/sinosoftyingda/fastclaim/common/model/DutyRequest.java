package com.sinosoftyingda.fastclaim.common.model;
/**
 * 上下班请求类
 * @author haoyun 20130222
 *
 */
public class DutyRequest extends BasicRequest {

	private String dutyFlag;//上下班标志位
	private String IMEI;//终端唯一标示
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getDutyFlag() {
		return dutyFlag;
	}

	public void setDutyFlag(String dutyFlag) {
		this.dutyFlag = dutyFlag;
	}

	public DutyRequest(String dutyFlag) {
		super();
		this.dutyFlag = dutyFlag;
	}

	public DutyRequest() {
		super();
	}
	
	
	
	
}
