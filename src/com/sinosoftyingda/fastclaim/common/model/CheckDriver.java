package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;
/**
 * 驾驶员
 * @author haoyun 20130307
 *
 */
public class CheckDriver implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String registNo="";//报案号
	private String serialNo="";//序号
	private String drivinglicenseNo="";//(当班)驾驶员驾驶证号码
	private String driverName="";//(当班)驾驶员姓名
	private String identifyNumber="";//身份证号码
	private String receivelicenseDate="";//(当班)驾驶员领证时间
	private String drivercertitype =""; //证件类型
	private String drivercertitypeCode =""; //证件类型代码
	public String getDrivercertitype() {
		return drivercertitype;
	}
	public String getDrivercertitypeCode() {
		return drivercertitypeCode;
	}
	public void setDrivercertitypeCode(String drivercertitypeCode) {
		this.drivercertitypeCode = drivercertitypeCode;
	}
	public void setDrivercertitype(String drivercertitype) {
		this.drivercertitype = drivercertitype;
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
	public String getDrivinglicenseNo() {
		return drivinglicenseNo;
	}
	public void setDrivinglicenseNo(String drivinglicenseNo) {
		this.drivinglicenseNo = drivinglicenseNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getIdentifyNumber() {
		return identifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}
	public String getReceivelicenseDate() {
		return receivelicenseDate;
	}
	public void setReceivelicenseDate(String receivelicenseDate) {
		this.receivelicenseDate = receivelicenseDate;
	}
	public CheckDriver(String registNo, String serialNo,
			String drivinglicenseNo, String driverName, String identifyNumber,
			String receivelicenseDate,String drivercertitype,String drivercertitypeCode) {
		super();
		this.registNo = registNo;
		this.serialNo = serialNo;
		this.drivinglicenseNo = drivinglicenseNo;
		this.driverName = driverName;
		this.identifyNumber = identifyNumber;
		this.receivelicenseDate = receivelicenseDate;
		this.drivercertitype=drivercertitype;
		this.drivercertitypeCode=drivercertitypeCode;
	}
	public CheckDriver() {
		super();
	}
	
}
