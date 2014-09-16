package com.sinosoftyingda.fastclaim.common.model;

public class PrintRequest extends BasicRequest {
	private String registNo;//报案号
	private String isSendMessage;//是否發送短信標誌
	private String isPrint;//是否打印標誌
	private String lossNo;//定损标的编号
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getIsSendMessage() {
		return isSendMessage;
	}
	public void setIsSendMessage(String isSendMessage) {
		this.isSendMessage = isSendMessage;
	}
	public String getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public PrintRequest(String registNo, String isSendMessage, String isPrint,
			String lossNo) {
		super();
		this.registNo = registNo;
		this.isSendMessage = isSendMessage;
		this.isPrint = isPrint;
		this.lossNo = lossNo;
	}
	public PrintRequest() {
		super();
	}}
