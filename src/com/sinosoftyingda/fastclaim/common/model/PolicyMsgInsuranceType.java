package com.sinosoftyingda.fastclaim.common.model;
/**
 * 承保险种类型类
 * @author haoyun 20130225
 *
 */
public class PolicyMsgInsuranceType {
	private String itemKindCode;//承保险种名称
	private String itemKindAmount;//承保险种金额
	public String getItemKindCode() {
		return itemKindCode;
	}
	public void setItemKindCode(String itemKindCode) {
		this.itemKindCode = itemKindCode;
	}
	public String getItemKindAmount() {
		return itemKindAmount;
	}
	public void setItemKindAmount(String itemKindAmount) {
		this.itemKindAmount = itemKindAmount;
	}
	public PolicyMsgInsuranceType(String itemKindCode, String itemKindAmount) {
		super();
		this.itemKindCode = itemKindCode;
		this.itemKindAmount = itemKindAmount;
	}
	public PolicyMsgInsuranceType() {
		super();
	}
	
	
}
