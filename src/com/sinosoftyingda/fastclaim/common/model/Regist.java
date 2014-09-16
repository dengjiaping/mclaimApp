package com.sinosoftyingda.fastclaim.common.model;
/**
 * 定损详细接口 报案信息类
 * @author haoyun 20130308
 *
 */
public class Regist {
	private String registNo="";//报案号
	private String lossNo="";//损失编号
	private String policyComCode="";//总机构号
	private String licenseNo="";//号牌号码
	private String brandName="";//厂牌型号
	private String dispatchpTime="";//出险时间
	private String reportTime="";//报案时间
	private String outofPlace="";//出险地点
	private String driverName="";//驾驶员姓名
	private String promptMessage="";//距保险起或止期提示
	private String insuredName="";//被保险人姓名
	private String insuredMobile="";//被保险人电话
	private String entrustName="";//受托人姓名
	private String entrustMobile="";//受托人电话
	private String damageDayp="";//近几天多次出现提示
	
	public String getPolicyComCode() {
		return policyComCode;
	}
	public void setPolicyComCode(String policyComCode) {
		this.policyComCode = policyComCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDispatchpTime() {
		return dispatchpTime;
	}
	public void setDispatchpTime(String dispatchpTime) {
		this.dispatchpTime = dispatchpTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getOutofPlace() {
		return outofPlace;
	}
	public void setOutofPlace(String outofPlace) {
		this.outofPlace = outofPlace;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getPromptMessage() {
		return promptMessage;
	}
	public void setPromptMessage(String promptMessage) {
		this.promptMessage = promptMessage;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredMobile() {
		return insuredMobile;
	}
	public void setInsuredMobile(String insuredMobile) {
		this.insuredMobile = insuredMobile;
	}
	public String getEntrustName() {
		return entrustName;
	}
	public void setEntrustName(String entrustName) {
		this.entrustName = entrustName;
	}
	public String getEntrustMobile() {
		return entrustMobile;
	}
	public void setEntrustMobile(String entrustMobile) {
		this.entrustMobile = entrustMobile;
	}
	public String getDamageDayp() {
		return damageDayp;
	}
	public void setDamageDayp(String damageDayp) {
		this.damageDayp = damageDayp;
	}
	public Regist(String registNo, String lossNo, String licenseNo,
			String brandName, String dispatchpTime, String reportTime,
			String outofPlace, String driverName, String promptMessage,
			String insuredName, String insuredMobile, String entrustName,
			String entrustMobile, String damageDayp) {
		super();
		this.registNo = registNo;
		this.lossNo = lossNo;
		this.licenseNo = licenseNo;
		this.brandName = brandName;
		this.dispatchpTime = dispatchpTime;
		this.reportTime = reportTime;
		this.outofPlace = outofPlace;
		this.driverName = driverName;
		this.promptMessage = promptMessage;
		this.insuredName = insuredName;
		this.insuredMobile = insuredMobile;
		this.entrustName = entrustName;
		this.entrustMobile = entrustMobile;
		this.damageDayp = damageDayp;
	}
	public Regist() {
		super();
	}
	

}
