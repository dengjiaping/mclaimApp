package com.sinosoftyingda.fastclaim.common.model;
/**
 * 核价
 * @author haoyun 20130302
 *
 */
public class VerifyPrice {
	private String itemName;//标的车/三者车  +车牌号
	private String certainUserCode;//定损人员
	private String verifypriceUserCode;//核价人员
	private String verifyPriceHandleTime;//完成时间
	public VerifyPrice() {
		super();
	}
	public VerifyPrice(String itemName, String certainUserCode,
			String verifypriceUserCode, String verifyPriceHandleTime) {
		super();
		this.itemName = itemName;
		this.certainUserCode = certainUserCode;
		this.verifypriceUserCode = verifypriceUserCode;
		this.verifyPriceHandleTime = verifyPriceHandleTime;
	}
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
	public String getVerifypriceUserCode() {
		return verifypriceUserCode;
	}
	public void setVerifypriceUserCode(String verifypriceUserCode) {
		this.verifypriceUserCode = verifypriceUserCode;
	}
	public String getVerifyPriceHandleTime() {
		return verifyPriceHandleTime;
	}
	public void setVerifyPriceHandleTime(String verifyPriceHandleTime) {
		this.verifyPriceHandleTime = verifyPriceHandleTime;
	}
}
