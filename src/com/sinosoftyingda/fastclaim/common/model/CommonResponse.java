package com.sinosoftyingda.fastclaim.common.model;
/**
 *通用返回类
 * @author haoyun 20130222
 *
 */
public class CommonResponse {
	private String reaponseType;//返回类型
	private String responseCode;//返回代码
	private String responseMessage;//返回信息
	
	private String handletime;//任务受理时间、完成时间
	
	public CommonResponse(String reaponseType, String responseCode,
			String responseMessage) {
		super();
		this.reaponseType = reaponseType;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	public CommonResponse() {
		super();
	}
	public String getReaponseType() {
		return reaponseType;
	}
	public void setReaponseType(String reaponseType) {
		this.reaponseType = reaponseType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getHandletime() {
		return handletime;
	}
	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}
	
}
