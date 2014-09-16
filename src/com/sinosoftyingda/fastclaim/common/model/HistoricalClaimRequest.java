package com.sinosoftyingda.fastclaim.common.model;
/**
 * 历史赔案信息请求类
 * @author haoyun 20130225
 *
 */
public class HistoricalClaimRequest extends BasicRequest {
	private String registNo;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public HistoricalClaimRequest(String registNo) {
		super();
		this.registNo = registNo;
	}

	public HistoricalClaimRequest() {
		super();
	}
	
}
