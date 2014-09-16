package com.sinosoftyingda.fastclaim.common.model;
/**
 * 查勘详细信息查询接口请求类
 * @author haoyun 20130226
 *
 */
public class DetailTaskQueryRequest extends BasicRequest{
	private String registNo;//报案号
	private String checkStatus;//任务状态
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public DetailTaskQueryRequest(String registNo, String checkStatus) {
		super();
		this.registNo = registNo;
		this.checkStatus = checkStatus;
	}
	public DetailTaskQueryRequest() {
		super();
	}

	
	
}
