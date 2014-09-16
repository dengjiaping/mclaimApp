package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;
/**
 * 接受任务接口
 * @author haoyun 20130307
 *
 */
public class ArrivedRequest  extends BasicRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String regisTno;//报案号
	private String nodeType;//节点类型
	private String lossNo;//损失项目编号

	
	public String getRegisTno() {
		return regisTno;
	}
	public void setRegisTno(String regisTno) {
		this.regisTno = regisTno;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	
}
