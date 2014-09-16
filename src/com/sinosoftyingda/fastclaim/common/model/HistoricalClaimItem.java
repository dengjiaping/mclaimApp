package com.sinosoftyingda.fastclaim.common.model;
/**
 * 历史赔案信息返回子类
 * @author haoyun 20130225
 *
 */
public class HistoricalClaimItem extends BasicRequest {
	private String registNo;//报案号
	private String damageDate;//出险时间
	private String registDate;//报案时间
	private String damageAddress;//出险地点
	private String claimStatus;//赔案状态
	private String claimAmount;//赔款金额
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	public String getDamageAddress() {
		return damageAddress;
	}
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}
	public HistoricalClaimItem(String registNo, String damageDate,
			String registDate, String damageAddress, String claimStatus,
			String claimAmount) {
		super();
		this.registNo = registNo;
		this.damageDate = damageDate;
		this.registDate = registDate;
		this.damageAddress = damageAddress;
		this.claimStatus = claimStatus;
		this.claimAmount = claimAmount;
	}
	public HistoricalClaimItem() {
		super();
	}
	

	
}
