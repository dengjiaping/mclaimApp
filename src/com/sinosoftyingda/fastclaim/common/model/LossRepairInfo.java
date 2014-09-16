package com.sinosoftyingda.fastclaim.common.model;
/**
 * 修理信息
 * @author haoyun 20130308
 *
 */
public class LossRepairInfo {
	private String lossNo;//损失编号
	private String registNo;//报案号
	private String repairId="";//定损系统修理唯一ID 
	private String serialNo="";//增量序列号码
	private String repairCode="";//工种代码
	private String repairName="";//工种名称
	private String repairitemCode="";//维修项代码
	private String repairitemName="";//维修项名称
	private String repairfee="";//修理费用
	private String selfConfigFlag="";//自定义修理件标记
	private String priceModel="";//价格方案
	private String systemprice="";//系统价格
	
	public String getSystemprice() {
		return systemprice==null?"":systemprice;
	}
	public void setSystemprice(String systemprice) {
		this.systemprice = systemprice;
	}
	public String getLossNo() {
		return lossNo==null?"":lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getRegistNo() {
		return registNo==null?"":registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public LossRepairInfo() {
		super();
	}
	public LossRepairInfo(String repairId, String serialNo, String repairCode,
			String repairName, String repairitemCode, String repairitemName,
			String repairfee, String selfConfigFlag, String priceModel) {
		super();
		this.repairId = repairId;
		this.serialNo = serialNo;
		this.repairCode = repairCode;
		this.repairName = repairName;
		this.repairitemCode = repairitemCode;
		this.repairitemName = repairitemName;
		this.repairfee = repairfee;
		this.selfConfigFlag = selfConfigFlag;
		this.priceModel = priceModel;
	}
	public String getRepairId() {
		return repairId==null?"":repairId;
	}
	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}
	public String getSerialNo() {
		return serialNo==null?"":serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getRepairCode() {
		return repairCode==null?"":repairCode;
	}
	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}
	public String getRepairName() {
		return repairName==null?"":repairName;
	}
	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}
	public String getRepairitemCode() {
		return repairitemCode==null?"":repairitemCode;
	}
	public void setRepairitemCode(String repairitemCode) {
		this.repairitemCode = repairitemCode;
	}
	public String getRepairitemName() {
		return repairitemName==null?"":repairitemName;
	}
	public void setRepairitemName(String repairitemName) {
		this.repairitemName = repairitemName;
	}
	public String getRepairfee() {
		return repairfee==null?"":repairfee;
	}
	public void setRepairfee(String repairfee) {
		this.repairfee = repairfee;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag==null?"":selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getPriceModel() {
		return priceModel==null?"":priceModel;
	}
	public void setPriceModel(String priceModel) {
		this.priceModel = priceModel;
	}
	
}
