package com.sinosoftyingda.fastclaim.common.model;
/**
 * 定损
 * @author haoyun 20130302
 *
 */
public class CertainLoss {
	private String itemName;//标的车/三者车   +车牌
	private String certinUserCode;//定损人员
	private String certinAcceptTime;//受理时间
	private String certinHandleTime;//完成时间
	private String certainAmount;//定损金额
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCertinUserCode() {
		return certinUserCode;
	}
	public void setCertinUserCode(String certinUserCode) {
		this.certinUserCode = certinUserCode;
	}
	public String getCertinAcceptTime() {
		return certinAcceptTime;
	}
	public void setCertinAcceptTime(String certinAcceptTime) {
		this.certinAcceptTime = certinAcceptTime;
	}
	public String getCertinHandleTime() {
		return certinHandleTime;
	}
	public void setCertinHandleTime(String certinHandleTime) {
		this.certinHandleTime = certinHandleTime;
	}
	public String getCertainAmount() {
		return certainAmount;
	}
	public void setCertainAmount(String certainAmount) {
		this.certainAmount = certainAmount;
	}
	public CertainLoss(String itemName, String certinUserCode,
			String certinAcceptTime, String certinHandleTime,
			String certainAmount) {
		super();
		this.itemName = itemName;
		this.certinUserCode = certinUserCode;
		this.certinAcceptTime = certinAcceptTime;
		this.certinHandleTime = certinHandleTime;
		this.certainAmount = certainAmount;
	}
	public CertainLoss() {
		super();
	}
	
}
