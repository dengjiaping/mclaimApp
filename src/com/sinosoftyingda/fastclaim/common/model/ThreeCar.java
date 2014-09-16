package com.sinosoftyingda.fastclaim.common.model;

public class ThreeCar {
	private String licenseNo;//车牌号
	private String frameNo;//车架号
	private String brandName;//厂牌型号
	private String dutyPercent;//责任比例
	private String nullPolicyNo;//三者车保单
	private String insurecomName;//承保公司
	public ThreeCar() {
		super();
	}
	public ThreeCar(String licenseNo, String frameNo, String brandName,
			String dutyPercent, String nullPolicyNo, String insurecomName) {
		super();
		this.licenseNo = licenseNo;
		this.frameNo = frameNo;
		this.brandName = brandName;
		this.dutyPercent = dutyPercent;
		this.nullPolicyNo = nullPolicyNo;
		this.insurecomName = insurecomName;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
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
	public String getInsurecomName() {
		return insurecomName;
	}
	public void setInsurecomName(String insurecomName) {
		this.insurecomName = insurecomName;
	}
	
}
