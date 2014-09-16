package com.sinosoftyingda.fastclaim.common.model;
/**
 * 核损
 * @author haoyun 20130302
 *
 */
public class VerifyLoss {
	private String itemName;//标的车/三者车  +车牌号
	private String certainUserCode;//定损人员
	private String verifyLossUserCode;//核损人员
	private String verifyPriceHandleTime;//完成时间
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCertainUserCode() {
		return certainUserCode;
	}
	public void setCertainUserCode(String certainUserCode) {
		this.certainUserCode = certainUserCode;
	}
	public String getVerifyLossUserCode() {
		return verifyLossUserCode;
	}
	public void setVerifyLossUserCode(String verifyLossUserCode) {
		this.verifyLossUserCode = verifyLossUserCode;
	}
	public String getVerifyPriceHandleTime() {
		return verifyPriceHandleTime;
	}
	public void setVerifyPriceHandleTime(String verifyPriceHandleTime) {
		this.verifyPriceHandleTime = verifyPriceHandleTime;
	}
	public VerifyLoss(String itemName, String certainUserCode,
			String verifyLossUserCode, String verifyPriceHandleTime) {
		super();
		this.itemName = itemName;
		this.certainUserCode = certainUserCode;
		this.verifyLossUserCode = verifyLossUserCode;
		this.verifyPriceHandleTime = verifyPriceHandleTime;
	}
	public VerifyLoss() {
		super();
	}
	
}
