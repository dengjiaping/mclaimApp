package com.sinosoftyingda.fastclaim.common.model;
/**
 * 工作流请求类
 * @author haoyun 20130304
 *
 */
public class WorkFlowRequest extends BasicRequest {
	private String registNo;//报案号

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public WorkFlowRequest(String registNo) {
		super();
		this.registNo = registNo;
	}

	public WorkFlowRequest() {
		super();
	}
	
}
