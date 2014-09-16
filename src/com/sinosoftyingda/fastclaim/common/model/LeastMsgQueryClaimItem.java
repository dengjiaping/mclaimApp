package com.sinosoftyingda.fastclaim.common.model;
/**
 * 历史赔案信息返回子类
 * @author haoyun 20130225
 *
 */
public class LeastMsgQueryClaimItem extends BasicRequest {
	private String registNo;//报案号
	private String submitTimes;//提交时间
	private String nodeType;//节点类型
	private String leaveMsgPersonName;//留言人
	private String leaveMsgContent;//留言内容
	private String serialNo;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getSubmitTimes() {
		return submitTimes;
	}
	public void setSubmitTimes(String submitTimes) {
		this.submitTimes = submitTimes;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getLeaveMsgPersonName() {
		return leaveMsgPersonName;
	}
	public void setLeaveMsgPersonName(String leaveMsgPersonName) {
		this.leaveMsgPersonName = leaveMsgPersonName;
	}
	public String getLeaveMsgContent() {
		return leaveMsgContent;
	}
	public void setLeaveMsgContent(String leaveMsgContent) {
		this.leaveMsgContent = leaveMsgContent;
	}
	public LeastMsgQueryClaimItem(String registNo, String submitTimes,
			String nodeType, String leaveMsgPersonName, String leaveMsgContent) {
		super();
		this.registNo = registNo;
		this.submitTimes=submitTimes;//提交时间
		this.nodeType=nodeType;//节点类型
		this.leaveMsgPersonName=leaveMsgPersonName;//留言人
		this.leaveMsgContent=leaveMsgContent;//留言内容
	}
	public LeastMsgQueryClaimItem() {
		super();
	}
	

	
}
