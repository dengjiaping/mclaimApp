package com.sinosoftyingda.fastclaim.common.db.table;

import java.util.ArrayList;
import java.util.List;

public class DeflossSynchroMain {
	private String registNo;//报案号
	private String lossNo;//标的序号
	private String lastUpdateTime;//最后一次更新时间，精确到毫秒
	private String submitType;//提交类型
	private String etimateNo;//定损单号，即报案号
	private String plateno;//车牌号码
	private String vehKindCode;//定损车辆种类代码 
	private String vehKindName;//定损车辆种类名称
	private String vehCertainCode;//定损车型编码
	private String vehCertainName;//定损车型名称
	private String vehGroupCode;//定损车组代码
	private String vehGroupName;//定损车组名称
	private String vehSeriCode;//定损车系编码
	private String vehSeriName;//定损车系名称
	private String vehYearType;//年款
	private String vehFactoryCode;//厂家编码
	private String vehFactoryName;//厂家名称
	private String vehBrandCode;//品牌编码
	private String vehBrandName;//品牌名称
	private String certainHandleCode;//定损处理人员代码
	private String certainHandleName;//定损处理人员名称
	private String repairFactoryCode;//修理厂代码
	private String repairFactoryName;//修理厂名称
	private String repairCooperateFlag;//
	private String repairMode;//
	private String repairReason;//
	private String repairAptitude;//
	private String subRoGateType;//
	private String excessType;//
	private String accidentBook;//
	private String insuredName;//
	private String insuredMobile;//
	private String entrustName;//
	private String entrustMobile;//
	private String selfConfigFlag;//
	private String version;//
	private String vehSeriGradeName;//
	private String carRegisterDate;//
	private String carVehicleDesc;//
	private String carFactoryCode;//
	private String carFactoryName;//
	private String claimType;//
	private String satisfaction;//
	private String sumFitsFee;//
	private String sumRepairFee;//
	private String sumVerirest;//
	private String sumCertainLoss;//
	private String textContent;//
	
	private List<DeflossSynchroReplace> replaces = new ArrayList<DeflossSynchroReplace>();
	private List<DeflossSynchroRepair> repairs = new ArrayList<DeflossSynchroRepair>();
	
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
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getEtimateNo() {
		return etimateNo;
	}
	public void setEtimateNo(String etimateNo) {
		this.etimateNo = etimateNo;
	}
	public String getPlateno() {
		return plateno;
	}
	public void setPlateno(String plateno) {
		this.plateno = plateno;
	}
	public String getVehKindCode() {
		return vehKindCode;
	}
	public void setVehKindCode(String vehKindCode) {
		this.vehKindCode = vehKindCode;
	}
	public String getVehKindName() {
		return vehKindName;
	}
	public void setVehKindName(String vehKindName) {
		this.vehKindName = vehKindName;
	}
	public String getVehCertainCode() {
		return vehCertainCode;
	}
	public void setVehCertainCode(String vehCertainCode) {
		this.vehCertainCode = vehCertainCode;
	}
	public String getVehCertainName() {
		return vehCertainName;
	}
	public void setVehCertainName(String vehCertainName) {
		this.vehCertainName = vehCertainName;
	}
	public String getVehGroupCode() {
		return vehGroupCode;
	}
	public void setVehGroupCode(String vehGroupCode) {
		this.vehGroupCode = vehGroupCode;
	}
	public String getVehGroupName() {
		return vehGroupName;
	}
	public void setVehGroupName(String vehGroupName) {
		this.vehGroupName = vehGroupName;
	}
	public String getVehSeriCode() {
		return vehSeriCode;
	}
	public void setVehSeriCode(String vehSeriCode) {
		this.vehSeriCode = vehSeriCode;
	}
	public String getVehSeriName() {
		return vehSeriName;
	}
	public void setVehSeriName(String vehSeriName) {
		this.vehSeriName = vehSeriName;
	}
	public String getVehYearType() {
		return vehYearType;
	}
	public void setVehYearType(String vehYearType) {
		this.vehYearType = vehYearType;
	}
	public String getVehFactoryCode() {
		return vehFactoryCode;
	}
	public void setVehFactoryCode(String vehFactoryCode) {
		this.vehFactoryCode = vehFactoryCode;
	}
	public String getVehFactoryName() {
		return vehFactoryName;
	}
	public void setVehFactoryName(String vehFactoryName) {
		this.vehFactoryName = vehFactoryName;
	}
	public String getVehBrandCode() {
		return vehBrandCode;
	}
	public void setVehBrandCode(String vehBrandCode) {
		this.vehBrandCode = vehBrandCode;
	}
	public String getVehBrandName() {
		return vehBrandName;
	}
	public void setVehBrandName(String vehBrandName) {
		this.vehBrandName = vehBrandName;
	}
	public String getCertainHandleCode() {
		return certainHandleCode;
	}
	public void setCertainHandleCode(String certainHandleCode) {
		this.certainHandleCode = certainHandleCode;
	}
	public String getCertainHandleName() {
		return certainHandleName;
	}
	public void setCertainHandleName(String certainHandleName) {
		this.certainHandleName = certainHandleName;
	}
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}
	public String getRepairFactoryName() {
		return repairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	public String getRepairCooperateFlag() {
		return repairCooperateFlag;
	}
	public void setRepairCooperateFlag(String repairCooperateFlag) {
		this.repairCooperateFlag = repairCooperateFlag;
	}
	public String getRepairMode() {
		return repairMode;
	}
	public void setRepairMode(String repairMode) {
		this.repairMode = repairMode;
	}
	public String getRepairReason() {
		return repairReason;
	}
	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}
	public String getRepairAptitude() {
		return repairAptitude;
	}
	public void setRepairAptitude(String repairAptitude) {
		this.repairAptitude = repairAptitude;
	}
	public String getSubRoGateType() {
		return subRoGateType;
	}
	public void setSubRoGateType(String subRoGateType) {
		this.subRoGateType = subRoGateType;
	}
	public String getExcessType() {
		return excessType;
	}
	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}
	public String getAccidentBook() {
		return accidentBook;
	}
	public void setAccidentBook(String accidentBook) {
		this.accidentBook = accidentBook;
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
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVehSeriGradeName() {
		return vehSeriGradeName;
	}
	public void setVehSeriGradeName(String vehSeriGradeName) {
		this.vehSeriGradeName = vehSeriGradeName;
	}
	public String getCarRegisterDate() {
		return carRegisterDate;
	}
	public void setCarRegisterDate(String carRegisterDate) {
		this.carRegisterDate = carRegisterDate;
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
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}
	public String getSumFitsFee() {
		return sumFitsFee;
	}
	public void setSumFitsFee(String sumFitsFee) {
		this.sumFitsFee = sumFitsFee;
	}
	public String getSumRepairFee() {
		return sumRepairFee;
	}
	public void setSumRepairFee(String sumRepairFee) {
		this.sumRepairFee = sumRepairFee;
	}
	public String getSumVerirest() {
		return sumVerirest;
	}
	public void setSumVerirest(String sumVerirest) {
		this.sumVerirest = sumVerirest;
	}
	public String getSumCertainLoss() {
		return sumCertainLoss;
	}
	public void setSumCertainLoss(String sumCertainLoss) {
		this.sumCertainLoss = sumCertainLoss;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public List<DeflossSynchroReplace> getReplaces() {
		return replaces;
	}
	public void setReplaces(List<DeflossSynchroReplace> replaces) {
		this.replaces = replaces;
	}
	public List<DeflossSynchroRepair> getRepairs() {
		return repairs;
	}
	public void setRepairs(List<DeflossSynchroRepair> repairs) {
		this.repairs = repairs;
	}
	
	
}
