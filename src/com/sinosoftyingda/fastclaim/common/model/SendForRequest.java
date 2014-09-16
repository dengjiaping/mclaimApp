package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 改派申请请求类
 * @author haoyun 20130225
 *
 */
public class SendForRequest extends BasicRequest {
	private String reassignPendingReason = "";//改派申请原因
	private String registNo = "";//报案号
	private String applyType = "";//申请类型
	private String isRelated = "0";//是否关联
	private List<ScheDuleItem> scheDuleItems=new ArrayList<ScheDuleItem>();//调度对象
	
	
	public String getIsRelated() {
		return isRelated;
	}
	public void setIsRelated(String isRelated) {
		this.isRelated = isRelated;
	}
	public String getReassignPendingReason() {
		return reassignPendingReason;
	}
	public void setReassignPendingReason(String reassignPendingReason) {
		this.reassignPendingReason = reassignPendingReason;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	
	
	public List<ScheDuleItem> getScheDuleItems() {
		return scheDuleItems;
	}
	public void setScheDuleItems(List<ScheDuleItem> scheDuleItems) {
		this.scheDuleItems = scheDuleItems;
	}
	public SendForRequest() {
		super();
	}
	
	
	
	
	
}
