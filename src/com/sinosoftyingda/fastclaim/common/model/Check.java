package com.sinosoftyingda.fastclaim.common.model;
/**
 * 查勘
 * @author haoyun 20130302
 *
 */

public class Check {
	private String checkUserCode;//查勘人员
	private String checkAcceptTime;//受理时间
	private String checkHandleTime;//完成时间
	public String getCheckUserCode() {
		return checkUserCode;
	}
	public void setCheckUserCode(String checkUserCode) {
		this.checkUserCode = checkUserCode;
	}
	public String getCheckAcceptTime() {
		return checkAcceptTime;
	}
	public void setCheckAcceptTime(String checkAcceptTime) {
		this.checkAcceptTime = checkAcceptTime;
	}
	public String getCheckHandleTime() {
		return checkHandleTime;
	}
	public void setCheckHandleTime(String checkHandleTime) {
		this.checkHandleTime = checkHandleTime;
	}
	public Check(String checkUserCode, String checkAcceptTime,
			String checkHandleTime) {
		super();
		this.checkUserCode = checkUserCode;
		this.checkAcceptTime = checkAcceptTime;
		this.checkHandleTime = checkHandleTime;
	}
	public Check() {
		super();
	}
	
}
