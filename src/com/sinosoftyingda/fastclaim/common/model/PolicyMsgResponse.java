package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 保单信息请求类
 * @author haoyun 20130225
 *
 */
public class PolicyMsgResponse extends CommonResponse {
	public PolicyMsgResponse() {
		super();
	}
	private String insuredName;//被保险人
	private String drivingLicenesOwner;//行驶证车主
	private String licenseNo;//号牌号码
	private String labelType;//厂牌类型
	private String vidNo;//VIN码
	private String engineNo;//发动机号
	private String carFirstregistration;//车辆初次登记日期
	private String useYears;//已使用年限
	private String newCarPurchaseprice;//新车购置价
	private String vehicleuse;//车辆使用性质
	private String qsPolicyNo;//交强险保单号
	private String qsPolicyInsureDate;//交强险期限
	private String busInessPolicyNo;//商业险保单号
	private String busInessPolicyInsureDate;//商业险期限
	private String basicClausetypes;//基本条款类型
	private String policyComCode;//归属部门
	private String busInessFlag;//业务标识
	private String policyEndOrses;//本保单批改次数
	private List<PolicyMsgInsuranceType>  policyMsgInsuranceTypes=new ArrayList<PolicyMsgInsuranceType>();//保单投保条款
	private List<AppointModel> appointModels=new ArrayList<AppointModel>();//特别约定
	public List<AppointModel> getAppointModels() {
		return appointModels;
	}
	public void setAppointModels(List<AppointModel> appointModels) {
		this.appointModels = appointModels;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getDrivingLicenesOwner() {
		return drivingLicenesOwner;
	}
	public void setDrivingLicenesOwner(String drivingLicenesOwner) {
		this.drivingLicenesOwner = drivingLicenesOwner;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLabelType() {
		return labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
	public String getVidNo() {
		return vidNo;
	}
	public void setVidNo(String vidNo) {
		this.vidNo = vidNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getCarFirstregistration() {
		return carFirstregistration;
	}
	public void setCarFirstregistration(String carFirstregistration) {
		this.carFirstregistration = carFirstregistration;
	}
	public String getUseYears() {
		return useYears;
	}
	public void setUseYears(String useYears) {
		this.useYears = useYears;
	}
	public String getNewCarPurchaseprice() {
		return newCarPurchaseprice;
	}
	public void setNewCarPurchaseprice(String newCarPurchaseprice) {
		this.newCarPurchaseprice = newCarPurchaseprice;
	}
	public String getVehicleuse() {
		return vehicleuse;
	}
	public void setVehicleuse(String vehicleuse) {
		this.vehicleuse = vehicleuse;
	}
	public String getQsPolicyNo() {
		return qsPolicyNo;
	}
	public void setQsPolicyNo(String qsPolicyNo) {
		this.qsPolicyNo = qsPolicyNo;
	}
	public String getQsPolicyInsureDate() {
		return qsPolicyInsureDate;
	}
	public void setQsPolicyInsureDate(String qsPolicyInsureDate) {
		this.qsPolicyInsureDate = qsPolicyInsureDate;
	}
	public String getBusInessPolicyNo() {
		return busInessPolicyNo;
	}
	public void setBusInessPolicyNo(String busInessPolicyNo) {
		this.busInessPolicyNo = busInessPolicyNo;
	}
	public String getBusInessPolicyInsureDate() {
		return busInessPolicyInsureDate;
	}
	public void setBusInessPolicyInsureDate(String busInessPolicyInsureDate) {
		this.busInessPolicyInsureDate = busInessPolicyInsureDate;
	}
	public String getBasicClausetypes() {
		return basicClausetypes;
	}
	public void setBasicClausetypes(String basicClausetypes) {
		this.basicClausetypes = basicClausetypes;
	}
	public String getPolicyComCode() {
		return policyComCode;
	}
	public void setPolicyComCode(String policyComCode) {
		this.policyComCode = policyComCode;
	}
	public String getBusInessFlag() {
		return busInessFlag;
	}
	public void setBusInessFlag(String busInessFlag) {
		this.busInessFlag = busInessFlag;
	}
	public String getPolicyEndOrses() {
		return policyEndOrses;
	}
	public void setPolicyEndOrses(String policyEndOrses) {
		this.policyEndOrses = policyEndOrses;
	}
	public List<PolicyMsgInsuranceType> getPolicyMsgInsuranceTypes() {
		return policyMsgInsuranceTypes;
	}
	public void setPolicyMsgInsuranceTypes(
			List<PolicyMsgInsuranceType> policyMsgInsuranceTypes) {
		this.policyMsgInsuranceTypes = policyMsgInsuranceTypes;
	}
	public PolicyMsgResponse(String reaponseType, String responseCode,
			String responseMessage, String insuredName,
			String drivingLicenesOwner, String licenseNo, String labelType,
			String vidNo, String engineNo, String carFirstregistration,
			String useYears, String newCarPurchaseprice, String vehicleuse,
			String qsPolicyNo, String qsPolicyInsureDate,
			String busInessPolicyNo, String busInessPolicyInsureDate,
			String basicClausetypes, String policyComCode, String busInessFlag,
			String policyEndOrses,
			List<PolicyMsgInsuranceType> policyMsgInsuranceTypes) {
		super(reaponseType, responseCode, responseMessage);
		this.insuredName = insuredName;
		this.drivingLicenesOwner = drivingLicenesOwner;
		this.licenseNo = licenseNo;
		this.labelType = labelType;
		this.vidNo = vidNo;
		this.engineNo = engineNo;
		this.carFirstregistration = carFirstregistration;
		this.useYears = useYears;
		this.newCarPurchaseprice = newCarPurchaseprice;
		this.vehicleuse = vehicleuse;
		this.qsPolicyNo = qsPolicyNo;
		this.qsPolicyInsureDate = qsPolicyInsureDate;
		this.busInessPolicyNo = busInessPolicyNo;
		this.busInessPolicyInsureDate = busInessPolicyInsureDate;
		this.basicClausetypes = basicClausetypes;
		this.policyComCode = policyComCode;
		this.busInessFlag = busInessFlag;
		this.policyEndOrses = policyEndOrses;
		this.policyMsgInsuranceTypes = policyMsgInsuranceTypes;
	}
	public PolicyMsgResponse(String reaponseType, String responseCode,
			String responseMessage) {
		super(reaponseType, responseCode, responseMessage);
	}
	

}
