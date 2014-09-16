package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;

public class SynchroClaimResponse extends CommonResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String registNo;//报案号
	private String nodeType;//节点类型
	private String lossNo;//损失编号
	public SynchroClaimResponse() {
		super();
	}
	public SynchroClaimResponse(String reaponseType, String responseCode,
			String responseMessage, String registNo, String nodeType,
			String lossNo) {
		super(reaponseType, responseCode, responseMessage);
		this.registNo = registNo;
		this.nodeType = nodeType;
		this.lossNo = lossNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}

	
}
