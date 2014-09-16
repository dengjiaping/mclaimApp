package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;
/**
 * 同步理赔撤回接口请求类
 * @author haoyun 20130307
 *
 */
public class SynchroClaimRequest extends BasicRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String registNo="";//报案号
	private String nodeType="";//节点类型
	private String lossNo="";//损失编号
	private String cancleType="";//撤回类型
	
	public String getCancleType() {
		return cancleType;
	}
	public void setCancleType(String cancleType) {
		this.cancleType = cancleType;
	}
	public SynchroClaimRequest() {
		super();
	}
	public SynchroClaimRequest(String registNo, String nodeType, String lossNo) {
		super();
		this.registNo = registNo;
		this.nodeType = nodeType;
		this.lossNo = lossNo;
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
}
