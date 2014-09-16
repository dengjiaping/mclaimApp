package com.sinosoftyingda.fastclaim.common.model;

import android.util.Log;

/**
 * 定损内容
 * 
 * @author haoyun 20130308
 * 
 */
public class DefLossContent {
	private String repairFactoryCode = "";// 修理厂代码
	private String repairFactoryName = "";// 修理厂名称
	private String repairCooperateFlag = "";// 修理厂合作性质
	private String repairMode = "";// 修理方式
	private String repairapTitude = "";// 修理厂资质
	private String defLossRiskCode = "";// 定损险别
	private String sumfitsfee = "";// 配件金额合计
	private String sumRepairfee = "";// 维修金额合计
	private String sumRest = "";// 残值合计
	private String sumCertainLoss = "";// 定损金额合计小写
	private String sumCerTainLossCh = "";// 定损金额合计大写
	private String defLossAdvise = "";// 定损意见
	private String undwrtRemark = "";// 核损退回意见
	private String satisfieddegree = "";// 满意度评价
	private String repairReason = "";// 外修原因
	private String shijiufee = "";//施救费 add by yxf 20140211 
	
	

	public String getShijiufee() {
		return shijiufee;
	}

	public void setShijiufee(String shijiufee) {
		this.shijiufee = shijiufee;
	}

	public String getUndwrtRemark() {
		return undwrtRemark;
	}

	public void setUndwrtRemark(String undwrtRemark) {
		this.undwrtRemark = undwrtRemark;
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

	public String getRepairapTitude() {
		return repairapTitude;
	}

	public void setRepairapTitude(String repairapTitude) {
		this.repairapTitude = repairapTitude;
	}

	public String getDefLossRiskCode() {
		return defLossRiskCode;
	}

	public void setDefLossRiskCode(String defLossRiskCode) {
		this.defLossRiskCode = defLossRiskCode;
	}

	public String getSumfitsfee() {
		return sumfitsfee;
	}

	public void setSumfitsfee(String sumfitsfee) {
		this.sumfitsfee = sumfitsfee;
	}

	public String getSumRepairfee() {
		return sumRepairfee;
	}

	public void setSumRepairfee(String sumRepairfee) {
		this.sumRepairfee = sumRepairfee;
	}

	public String getSumRest() {
		return sumRest;
	}

	public void setSumRest(String sumRest) {
		this.sumRest = sumRest;
	}

	public String getSumCertainLoss() {
		return sumCertainLoss;
	}

	public void setSumCertainLoss(String sumCertainLoss) {
		this.sumCertainLoss = sumCertainLoss;
	}

	public String getSumCerTainLossCh() {
		return sumCerTainLossCh;
	}

	public void setSumCerTainLossCh(String sumCerTainLossCh) {
		this.sumCerTainLossCh = sumCerTainLossCh;
	}

	public String getDefLossAdvise() {
		return defLossAdvise;
	}

	public void setDefLossAdvise(String defLossAdvise) {
		this.defLossAdvise = defLossAdvise;
	}

	public String getSatisfieddegree() {
		return satisfieddegree;
	}

	public void setSatisfieddegree(String satisfieddegree) {
		this.satisfieddegree = satisfieddegree;
	}

	public DefLossContent(String repairFactoryCode, String repairFactoryName, String repairCooperateFlag, String repairMode, String repairapTitude, String defLossRiskCode,
			String sumfitsfee, String sumRepairfee, String sumRest, String sumCertainLoss, String sumCerTainLossCh, String defLossAdvise, String satisfieddegree,String shijiufee) {
		super();
		this.repairFactoryCode = repairFactoryCode;
		this.repairFactoryName = repairFactoryName;
		this.repairCooperateFlag = repairCooperateFlag;
		this.repairMode = repairMode;
		this.repairapTitude = repairapTitude;
		this.defLossRiskCode = defLossRiskCode;
		this.sumfitsfee = sumfitsfee;
		this.sumRepairfee = sumRepairfee;
		this.sumRest = sumRest;
		this.sumCertainLoss = sumCertainLoss;
		this.sumCerTainLossCh = sumCerTainLossCh;
		this.defLossAdvise = defLossAdvise;
		this.satisfieddegree = satisfieddegree;
		this.shijiufee = shijiufee;	//施救费 add by yxf 20140211 
	}

	public DefLossContent() {
		super();
	}

	public String getRepairReason() {
		return repairReason;
	}

	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

}
