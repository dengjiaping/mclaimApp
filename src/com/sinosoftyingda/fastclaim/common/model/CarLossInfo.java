package com.sinosoftyingda.fastclaim.common.model;

/**
 * 案件涉案车辆信息
 * 
 * @author haoyun 20130308
 * 
 */
public class CarLossInfo {
	private String insureCarFlag="";// 是否标的车
	private String licenseNo="";// 车牌
	private String defineLossPerson="";// 定损员
	private String defineLossAmount="";// 定损金额

	public String getInsureCarFlag() {
		return insureCarFlag;
	}

	public void setInsureCarFlag(String insureCarFlag) {
		this.insureCarFlag = insureCarFlag;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getDefineLossPerson() {
		return defineLossPerson;
	}

	public void setDefineLossPerson(String defineLossPerson) {
		this.defineLossPerson = defineLossPerson;
	}

	public String getDefineLossAmount() {
		return defineLossAmount;
	}

	public void setDefineLossAmount(String defineLossAmount) {
		this.defineLossAmount = defineLossAmount;
	}

	public CarLossInfo(String insureCarFlag, String licenseNo, String defineLossPerson, String defineLossAmount) {
		super();
		this.insureCarFlag = insureCarFlag;
		this.licenseNo = licenseNo;
		this.defineLossPerson = defineLossPerson;
		this.defineLossAmount = defineLossAmount;
	}

	public CarLossInfo() {
		super();
	}

}
