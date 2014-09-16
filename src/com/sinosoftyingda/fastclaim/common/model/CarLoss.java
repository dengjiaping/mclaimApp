package com.sinosoftyingda.fastclaim.common.model;

/**
 * 三者车信息
 * @author DengGuang
 *
 */
public class CarLoss {
	private String id="";
	private String isEdited="";		// 是否可编辑(1,可编辑删除; 0,不可以编辑删除)
	private String carNum="";			// 序号
	private String licenseNo="";	// 车牌号
	private String insureCarFlag="";// 是否是标的车(1,标的车; 0,三者车)
	private String frameNo="";		// 车架号
	private String brandName="";	// 厂牌型号
	private String dutyPercent="";	// 责任比例
	private String nullPolicyNo="";	// 三者车保单
	private String engineNo="";		// 发动机号
	private String insurecomCode="";// 承保公司代码
	private String insurecomName="";// 承保公司名称
	private String carKindName="";	// 车辆种类名称
	private String carKindCode="";	// 车辆种类代码 add 20130322
	private String licenseType="";	// 号牌种类 add 20130322
	private String licenseTypeCode 	= ""; // 号牌选择代码
	private String nullDriverName	= "";	// 驾驶员姓名(三者车，浙江需求)
	private String nullCertiType	= "";	// 驾驶员证件类型(三者车，浙江需求)
	private String nullCertitypeCode	= "";	// 驾驶员证件类型代码(三者车，浙江需求)
	private String nullDriverCode	= "";	// 驾驶员证件号码(三者车，浙江需求)
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarKindCode() {
		return carKindCode;
	}
	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}
	public String getCarKindName() {
		return carKindName;
	}
	public void setCarKindName(String carKindName) {
		this.carKindName = carKindName;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseTypeCode() {
		return licenseTypeCode;
	}
	public void setLicenseTypeCode(String licenseTypeCode) {
		this.licenseTypeCode = licenseTypeCode;
	}
	public String getIsEdited() {
		return isEdited;
	}
	public void setIsEdited(String isEdited) {
		this.isEdited = isEdited;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getInsureCarFlag() {
		return insureCarFlag;
	}
	public void setInsureCarFlag(String insureCarFlag) {
		this.insureCarFlag = insureCarFlag;
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
	public String getDutyPercent() {
		return dutyPercent;
	}
	public void setDutyPercent(String dutyPercent) {
		this.dutyPercent = dutyPercent;
	}
	public String getNullPolicyNo() {
		return nullPolicyNo;
	}
	public void setNullPolicyNo(String nullPolicyNo) {
		this.nullPolicyNo = nullPolicyNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getInsurecomCode() {
		return insurecomCode;
	}
	public void setInsurecomCode(String insurecomCode) {
		this.insurecomCode = insurecomCode;
	}
	public String getInsurecomName() {
		return insurecomName;
	}
	public void setInsurecomName(String insurecomName) {
		this.insurecomName = insurecomName;
	}
	public String getNullDriverName() {
		return nullDriverName;
	}
	public void setNullDriverName(String nullDriverName) {
		this.nullDriverName = nullDriverName;
	}
	public String getNullCertiType() {
		return nullCertiType;
	}
	public void setNullCertiType(String nullCertiType) {
		this.nullCertiType = nullCertiType;
	}
	public String getNullCertitypeCode() {
		return nullCertitypeCode;
	}
	public void setNullCertitypeCode(String nullCertitypeCode) {
		this.nullCertitypeCode = nullCertitypeCode;
	}
	public String getNullDriverCode() {
		return nullDriverCode;
	}
	public void setNullDriverCode(String nullDriverCode) {
		this.nullDriverCode = nullDriverCode;
	}
	
}
