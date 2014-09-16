package com.sinosoftyingda.fastclaim.common.model;
/**
 * 保单信息请求类
 * @author haoyun 20130225
 *
 */
public class PolicyMsgRequest extends BasicRequest {
	private String registNo;//报案号

	public PolicyMsgRequest() {
		super();
	}

	public PolicyMsgRequest(String registNo) {
		super();
		this.registNo = registNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
}
