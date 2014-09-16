package com.sinosoftyingda.fastclaim.common.model;

/**
 * 实时视频上传接口请求类
 * 
 * @author haoyun 20130226
 * 
 */
public class RedioRequest extends BasicRequest {
	private String registNo = "";// 报案号
	private String nodeType = "";// 节点类型
	private String lossNo = "";// 损失项目编号
	private String isXeipei = "";// 是否协赔员
	private String IMEI = "";
	private String cerTainCallTime = "";// 联系客户时间
	private String insuredPhone = ""; // 被保险人联系电话
	private String linkPhone = ""; // 受托人联系电话
	private String linkName = "";// 受托人姓名
	
	

	public String getInsuredPhone() {
		return insuredPhone;
	}

	public void setInsuredPhone(String insuredPhone) {
		this.insuredPhone = insuredPhone;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getCerTainCallTime() {
		return cerTainCallTime;
	}

	public void setCerTainCallTime(String cerTainCallTime) {
		this.cerTainCallTime = cerTainCallTime;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getLossNo() {
		return lossNo;
	}

	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}

	public String getIsXeipei() {
		return isXeipei;
	}

	public void setIsXeipei(String isXeipei) {
		this.isXeipei = isXeipei;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
}
