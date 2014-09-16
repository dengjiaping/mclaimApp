package com.sinosoftyingda.fastclaim.common.model;
/**
 * 换件信息
 * @author haoyun 20130308
 *
 */
public class LossFitInfo {
	private String lossNo;//损失编号
	private String registNo;//报案号
	private String partid="";//零件唯一ID
	private String serialNo="";//定损单唯一序号
	private String originalId="";//零配件原厂编码
	private String originalName="";//零配件原厂名称
	private String partstandardCode="";//配件标准代码
	private String partstandard="";//配件标准名称
	private String partgroupCode="";//配件部位代码
	private String partgroupName="";//配件部位名称
	private String priceModel="";//价格方案
	private String systemPrice001="";//4s 系统价
	private String localPrice001="";//4s 本地价
	private String systemPrice002="";//市场系统价
	private String localPrice002="";//市场本地价
	private String lossFee="";//定损价格
	private String restFee="";//定损残值
	private String selfconfigflag="";//自定义配件标记
	private String lossCount="";//数量换件
	private String remark="";//备注
	private String systemPrice="";//定损选中系统价
	private String localPrice="";//定损选中本地价
	
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
	public String getPartid() {
		return partid==null?"":partid;
	}
	public void setPartid(String partid) {
		this.partid = partid;
	}
	public String getSerialNo() {
		return serialNo==null?"":serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getOriginalId() {
		return originalId==null?"":originalId;
	}
	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	public String getOriginalName() {
		return originalName==null?"":originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getPartstandardCode() {
		return partstandardCode==null?"":partstandardCode;
	}
	public void setPartstandardCode(String partstandardCode) {
		this.partstandardCode = partstandardCode;
	}
	public String getPartstandard() {
		return partstandard==null?"":partstandard;
	}
	public void setPartstandard(String partstandard) {
		this.partstandard = partstandard;
	}
	public String getPartgroupCode() {
		return partgroupCode==null?"":partgroupCode;
	}
	public void setPartgroupCode(String partgroupCode) {
		this.partgroupCode = partgroupCode;
	}
	public String getPartgroupName() {
		return partgroupName==null?"":partgroupName;
	}
	public void setPartgroupName(String partgroupName) {
		this.partgroupName = partgroupName;
	}
	public String getPriceModel() {
		return priceModel==null?"":priceModel;
	}
	public void setPriceModel(String priceModel) {
		this.priceModel = priceModel;
	}
	public String getSystemPrice001() {
		return systemPrice001==null?"":systemPrice001;
	}
	public void setSystemPrice001(String systemPrice001) {
		this.systemPrice001 = systemPrice001;
	}
	public String getLocalPrice001() {
		return localPrice001==null?"":localPrice001;
	}
	public void setLocalPrice001(String localPrice001) {
		this.localPrice001 = localPrice001;
	}
	public String getSystemPrice002() {
		return systemPrice002==null?"":systemPrice002;
	}
	public void setSystemPrice002(String systemPrice002) {
		this.systemPrice002 = systemPrice002;
	}
	public String getLocalPrice002() {
		return localPrice002==null?"":localPrice002;
	}
	public void setLocalPrice002(String localPrice002) {
		this.localPrice002 = localPrice002;
	}
	public String getLossFee() {
		return lossFee==null?"":lossFee;
	}
	public void setLossFee(String lossFee) {
		this.lossFee = lossFee;
	}
	public String getSelfconfigflag() {
		return selfconfigflag==null?"":selfconfigflag;
	}
	public void setSelfconfigflag(String selfconfigflag) {
		this.selfconfigflag = selfconfigflag;
	}
	public String getLossCount() {
		return lossCount==null?"":lossCount;
	}
	public void setLossCount(String lossCount) {
		this.lossCount = lossCount;
	}
	public String getRemark() {
		return remark==null?"":remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSystemPrice() {
		return systemPrice==null?"":systemPrice;
	}
	public void setSystemPrice(String systemPrice) {
		this.systemPrice = systemPrice;
	}
	public String getLocalPrice() {
		return localPrice==null?"":localPrice;
	}
	public void setLocalPrice(String localPrice) {
		this.localPrice = localPrice;
	}
	public String getRestFee() {
		return restFee==null?"":restFee;
	}
	public void setRestFee(String restFee) {
		this.restFee = restFee;
	}

	
	
}
