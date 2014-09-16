package com.sinosoftyingda.fastclaim.common.model;
/**
 * 定损对象类
 * @author haoyun 20130308
 *
 */
public class DefLossCarInfo {
	private String insurecarFlag="";//是否标的车
	private String licenseNo="";//号牌号码
	private String carframeNo="";//车架号
	private String brandName="";//厂牌型号
	private String newPruchaseAmount="";//三者车新车购置税
	private String carVehicleDesc="";//车辆行驶证描述
	private String carFactoryCode="";//车辆制造厂编码
	private String carFactoryName="";//车辆制造厂名称
	private String carRegisterDate="";//车辆初次登记日期
	private String insurevehiCode="";//车辆型号编码
	
	public String getInsurevehiCode() {
		return insurevehiCode;
	}
	public void setInsurevehiCode(String insurevehiCode) {
		this.insurevehiCode = insurevehiCode;
	}
	public String getCarVehicleDesc() {
		return carVehicleDesc;
	}
	public void setCarVehicleDesc(String carVehicleDesc) {
		this.carVehicleDesc = carVehicleDesc;
	}
	public String getCarFactoryCode() {
		return carFactoryCode;
	}
	public void setCarFactoryCode(String carFactoryCode) {
		this.carFactoryCode = carFactoryCode;
	}
	public String getCarFactoryName() {
		return carFactoryName;
	}
	public void setCarFactoryName(String carFactoryName) {
		this.carFactoryName = carFactoryName;
	}
	public String getCarRegisterDate() {
		return carRegisterDate;
	}
	public void setCarRegisterDate(String carRegisterDate) {
		this.carRegisterDate = carRegisterDate;
	}
	public String getInsurecarFlag() {
		return insurecarFlag;
	}
	public void setInsurecarFlag(String insurecarFlag) {
		this.insurecarFlag = insurecarFlag;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getCarframeNo() {
		return carframeNo;
	}
	public void setCarframeNo(String carframeNo) {
		this.carframeNo = carframeNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getNewPruchaseAmount() {
		return newPruchaseAmount;
	}
	public void setNewPruchaseAmount(String newPruchaseAmount) {
		this.newPruchaseAmount = newPruchaseAmount;
	}
	public DefLossCarInfo() {
		super();
	}
	public DefLossCarInfo(String insurecarFlag, String licenseNo,
			String carframeNo, String brandName, String newPruchaseAmount) {
		super();
		this.insurecarFlag = insurecarFlag;
		this.licenseNo = licenseNo;
		this.carframeNo = carframeNo;
		this.brandName = brandName;
		this.newPruchaseAmount = newPruchaseAmount;
	}
	
}
