package com.sinosoftyingda.fastclaim.common.model;

public class StandardCar {
	private String licenseNo;//车牌号
	private String framNo;//车架号
	private String brandName;//厂牌型号
	private String dutyPercent;//责任比例
	public StandardCar(String licenseNo, String framNo, String brandName,
			String dutyPercent) {
		super();
		this.licenseNo = licenseNo;
		this.framNo = framNo;
		this.brandName = brandName;
		this.dutyPercent = dutyPercent;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getFramNo() {
		return framNo;
	}
	public void setFramNo(String framNo) {
		this.framNo = framNo;
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
	public StandardCar() {
		super();
	}
	
}
