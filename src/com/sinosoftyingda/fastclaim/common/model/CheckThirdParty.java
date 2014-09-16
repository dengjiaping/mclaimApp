package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;

/**
 * 查勘车辆信息
 * 
 * @author haoyun 20130307
 * 
 */
public class CheckThirdParty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String registNo = "";// 报案号
	private String serialNo = "";// 序号
	private String licenseNo = "";// 车牌号
	private String carkindCode = "";// 车辆种类代码
	private String licenseType = "";// 号牌种类
	private String licenseTypeCode = "";// 号牌种类
	private String insurecarFlag = "";// 是否为本保单车辆
	private String carOwner = "";// 车主
	private String engineNo = "";// 发动机号
	private String frameNo = "";// 车架号
	private String brandName = "";// 厂牌型号
	private String thirdPolicyNo = "";// 三者车保单号
	private String dutyPercent = "";// 本车责任比例
	private String insureComCode = "";// 承保公司代码
	private String insureComName = "";// 承保公司名称
	private String nullDriverName = "";// 驾驶员姓名
	private String nullDriverCode = "";// 驾驶员证件代码
	private String nullCertitype = "";// 驾驶员证件类型
	private String nullCertitypeCode = "";// 驾驶员证件类型代码

	public String getLicenseTypeCode() {
		return licenseTypeCode;
	}

	public void setLicenseTypeCode(String licenseTypeCode) {
		this.licenseTypeCode = licenseTypeCode;
	}

	public String getNullDriverName() {
		return nullDriverName;
	}

	public void setNullDriverName(String nullDriverName) {
		this.nullDriverName = nullDriverName;
	}

	public String getNullDriverCode() {
		return nullDriverCode;
	}

	public void setNullDriverCode(String nullDriverCode) {
		this.nullDriverCode = nullDriverCode;
	}

	public String getNullCertitype() {
		return nullCertitype;
	}

	public void setNullCertitype(String nullCertitype) {
		this.nullCertitype = nullCertitype;
	}

	public String getNullCertitypeCode() {
		return nullCertitypeCode;
	}

	public void setNullCertitypeCode(String nullCertitypeCode) {
		this.nullCertitypeCode = nullCertitypeCode;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getCarkindCode() {
		return carkindCode;
	}

	public void setCarkindCode(String carkindCode) {
		this.carkindCode = carkindCode;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getInsurecarFlag() {
		return insurecarFlag;
	}

	public void setInsurecarFlag(String insurecarFlag) {
		this.insurecarFlag = insurecarFlag;
	}

	public String getCarOwner() {
		return carOwner;
	}

	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getThirdPolicyNo() {
		return thirdPolicyNo;
	}

	public void setThirdPolicyNo(String thirdPolicyNo) {
		this.thirdPolicyNo = thirdPolicyNo;
	}

	public String getDutyPercent() {
		return dutyPercent;
	}

	public void setDutyPercent(String dutyPercent) {
		this.dutyPercent = dutyPercent;
	}

	public String getInsureComCode() {
		return insureComCode;
	}

	public void setInsureComCode(String insureComCode) {
		this.insureComCode = insureComCode;
	}

	public String getInsureComName() {
		return insureComName;
	}

	public void setInsureComName(String insureComName) {
		this.insureComName = insureComName;
	}

	public CheckThirdParty(String registNo, String serialNo, String licenseNo,
			String carkindCode, String licenseType, String insurecarFlag,
			String carOwner, String engineNo, String frameNo, String brandName,
			String thirdPolicyNo, String dutyPercent, String insureComCode,
			String insureComName, String nullDriverName, String nullDriverCode,
			String nullCertitype, String nullCertitypeCode) {
		super();
		this.registNo = registNo;
		this.serialNo = serialNo;
		this.licenseNo = licenseNo;
		this.carkindCode = carkindCode;
		this.licenseType = licenseType;
		this.insurecarFlag = insurecarFlag;
		this.carOwner = carOwner;
		this.engineNo = engineNo;
		this.frameNo = frameNo;
		this.brandName = brandName;
		this.thirdPolicyNo = thirdPolicyNo;
		this.dutyPercent = dutyPercent;
		this.insureComCode = insureComCode;
		this.insureComName = insureComName;
		this.nullDriverName = nullDriverName;
		this.nullDriverCode = nullDriverCode;
		this.nullCertitype = nullCertitype;
		this.nullCertitypeCode = nullCertitypeCode;
	}

	public CheckThirdParty() {
		super();
	}

}
