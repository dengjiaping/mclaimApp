package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 打印相应类
 * @author haoyun 20130227
 *
 */
public class PrintResponse extends CommonResponse {
	private String verifyComCode;//定损机构
	private String verifyUserCode;//定损人员
	private String registNo;//报案号
	private String qsPolicyNo;//保单号（交强险）
	private String busInessPolicyNo;//保单号（商业险）
	private String insureName;//被保险人；
	private String lossNo;//定损标的编号
	private String lossName;//定损标的名称
	private String plateNo;//车牌号码
	private String brandName;//厂牌型号
	private String vinNo;//VIN码
	private String componentSumFee;//配件金额合计
	private String repairfeesSumFee;//维修金额合计
	private String resetFee;//残值合计
	private String verifyLossFee;//定损金额合计小写
	private String vertfyLossfee1;//定损金额合计大写
	private String printsinglegenerationTime;//打印单生成时间
	private List<PrintAccessorles> printAccessorles=new ArrayList<PrintAccessorles>();//配件列表
	private List<PrintRepair> printRepair=new ArrayList<PrintRepair>();//维修价格列表
	public String getVerifyComCode() {
		return verifyComCode;
	}
	public void setVerifyComCode(String verifyComCode) {
		this.verifyComCode = verifyComCode;
	}
	public String getVerifyUserCode() {
		return verifyUserCode;
	}
	public void setVerifyUserCode(String verifyUserCode) {
		this.verifyUserCode = verifyUserCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getQsPolicyNo() {
		return qsPolicyNo;
	}
	public void setQsPolicyNo(String qsPolicyNo) {
		this.qsPolicyNo = qsPolicyNo;
	}
	public String getBusInessPolicyNo() {
		return busInessPolicyNo;
	}
	public void setBusInessPolicyNo(String busInessPolicyNo) {
		this.busInessPolicyNo = busInessPolicyNo;
	}
	public String getInsureName() {
		return insureName;
	}
	public void setInsureName(String insureName) {
		this.insureName = insureName;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getLossName() {
		return lossName;
	}
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getComponentSumFee() {
		return componentSumFee;
	}
	public void setComponentSumFee(String componentSumFee) {
		this.componentSumFee = componentSumFee;
	}
	public String getRepairfeesSumFee() {
		return repairfeesSumFee;
	}
	public void setRepairfeesSumFee(String repairfeesSumFee) {
		this.repairfeesSumFee = repairfeesSumFee;
	}
	public String getResetFee() {
		return resetFee;
	}
	public void setResetFee(String resetFee) {
		this.resetFee = resetFee;
	}
	public String getVerifyLossFee() {
		return verifyLossFee;
	}
	public void setVerifyLossFee(String verifyLossFee) {
		this.verifyLossFee = verifyLossFee;
	}
	public String getVertfyLossfee1() {
		return vertfyLossfee1;
	}
	public void setVertfyLossfee1(String vertfyLossfee1) {
		this.vertfyLossfee1 = vertfyLossfee1;
	}
	public String getPrintsinglegenerationTime() {
		return printsinglegenerationTime;
	}
	public void setPrintsinglegenerationTime(String printsinglegenerationTime) {
		this.printsinglegenerationTime = printsinglegenerationTime;
	}
	public List<PrintAccessorles> getPrintAccessorles() {
		return printAccessorles;
	}
	public void setPrintAccessorles(List<PrintAccessorles> printAccessorles) {
		this.printAccessorles = printAccessorles;
	}
	public List<PrintRepair> getPrintRepair() {
		return printRepair;
	}
	public void setPrintRepair(List<PrintRepair> printRepair) {
		this.printRepair = printRepair;
	}
	public PrintResponse(String reaponseType, String responseCode,
			String responseMessage, String verifyComCode,
			String verifyUserCode, String registNo, String qsPolicyNo,
			String busInessPolicyNo, String insureName, String lossNo,
			String lossName, String plateNo, String brandName, String vinNo,
			String componentSumFee, String repairfeesSumFee, String resetFee,
			String verifyLossFee, String vertfyLossfee1,
			String printsinglegenerationTime,
			List<PrintAccessorles> printAccessorles,
			List<PrintRepair> printRepair) {
		super(reaponseType, responseCode, responseMessage);
		this.verifyComCode = verifyComCode;
		this.verifyUserCode = verifyUserCode;
		this.registNo = registNo;
		this.qsPolicyNo = qsPolicyNo;
		this.busInessPolicyNo = busInessPolicyNo;
		this.insureName = insureName;
		this.lossNo = lossNo;
		this.lossName = lossName;
		this.plateNo = plateNo;
		this.brandName = brandName;
		this.vinNo = vinNo;
		this.componentSumFee = componentSumFee;
		this.repairfeesSumFee = repairfeesSumFee;
		this.resetFee = resetFee;
		this.verifyLossFee = verifyLossFee;
		this.vertfyLossfee1 = vertfyLossfee1;
		this.printsinglegenerationTime = printsinglegenerationTime;
		this.printAccessorles = printAccessorles;
		this.printRepair = printRepair;
	}
	public PrintResponse() {
		super();
	}
	
}
