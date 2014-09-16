package com.sinosoftyingda.fastclaim.survey.page.three.model;

public class GuaranteelipBeam {

	private String assured;
	private String registration;
	private String carNo;
	private String factoryNO;
	private String vin;
	private String engineNO;
	private String firstLoginDate;
	private String spentYears;
	private String newCarPrice;
	private String carUseType;
	private String claimNO;
	private String claimTime;
	private String type;
	private String department;
	private String serviceId;

	/**
	 * 机动车第三者责任保险条款
	 */
	private String ThressInsurance;

	/**
	 * 机动车交通事故责任强制保险"
	 */
	private String comInsurance;
	/***
	 * 机动车损失保险条款
	 */
	private String lossInsurance;

	/**
	 * 机动车车上人员责任保险条款(驾驶员)
	 */
	private String driverInsurance;
	/***
	 * 机动车车上人员责任保险条款(乘客)
	 */
	private String passengerInsurance;

	/***
	 * 机动车车上人员责任保险条款(乘客)
	 * 
	 * @return
	 */
	public String getPassengerInsurance() {
		return passengerInsurance;
	}

	/***
	 * 机动车车上人员责任保险条款(乘客)
	 * 
	 * @param passengerInsurance
	 */
	public void setPassengerInsurance(String passengerInsurance) {
		this.passengerInsurance = passengerInsurance;
	}

	/**
	 * 机动车车上人员责任保险条款(驾驶员)
	 * 
	 * @return
	 */
	public String getDriverInsurance() {
		return driverInsurance;
	}

	/**
	 * 机动车车上人员责任保险条款(驾驶员)
	 * 
	 * @param driverInsurance
	 */
	public void setDriverInsurance(String driverInsurance) {
		this.driverInsurance = driverInsurance;
	}

	/***
	 * 机动车交通事故责任强制保险"
	 * 
	 * @return
	 */
	public String getComInsurance() {
		return comInsurance;
	}

	/**
	 * 机动车交通事故责任强制保险"
	 * 
	 * @param comInsurance
	 */
	public void setComInsurance(String comInsurance) {
		this.comInsurance = comInsurance;
	}

	/**
	 * 机动车损失保险条款
	 * 
	 * @return
	 */
	public String getLossInsurance() {
		return lossInsurance;
	}

	/**
	 * 机动车第三者责任保险条款
	 * 
	 * @return
	 */

	public String getThressInsurance() {
		return ThressInsurance;
	}

	/***
	 * 机动车第三者责任保险条款
	 * 
	 * @param thressInsurance
	 */
	public void setThressInsurance(String thressInsurance) {
		ThressInsurance = thressInsurance;
	}

	/***
	 * 机动车损失保险条款
	 * 
	 * @param lossInsurance
	 */
	public void setLossInsurance(String lossInsurance) {
		this.lossInsurance = lossInsurance;
	}

	public String getAssured() {
		return assured;
	}

	public void setAssured(String assured) {
		this.assured = assured;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getFactoryNO() {
		return factoryNO;
	}

	public void setFactoryNO(String factoryNO) {
		this.factoryNO = factoryNO;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getEngineNO() {
		return engineNO;
	}

	public void setEngineNO(String engineNO) {
		this.engineNO = engineNO;
	}

	public String getFirstLoginDate() {
		return firstLoginDate;
	}

	public void setFirstLoginDate(String firstLoginDate) {
		this.firstLoginDate = firstLoginDate;
	}

	public String getSpentYears() {
		return spentYears;
	}

	public void setSpentYears(String spentYears) {
		this.spentYears = spentYears;
	}

	public String getNewCarPrice() {
		return newCarPrice;
	}

	public void setNewCarPrice(String newCarPrice) {
		this.newCarPrice = newCarPrice;
	}

	public String getCarUseType() {
		return carUseType;
	}

	public void setCarUseType(String carUseType) {
		this.carUseType = carUseType;
	}

	public String getClaimNO() {
		return claimNO;
	}

	public void setClaimNO(String claimNO) {
		this.claimNO = claimNO;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	/****
	 * 基本条款类型
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 基本条款类型
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	/****
	 * 业务标识
	 * 
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 业务标识
	 * 
	 * @param serviceId
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
