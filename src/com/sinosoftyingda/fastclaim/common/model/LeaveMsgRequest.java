package com.sinosoftyingda.fastclaim.common.model;

/**
 * 撰写留言保存请求类
 * 
 * @author haoyun 20130225
 * 
 */
public class LeaveMsgRequest extends BasicRequest {
	public LeaveMsgRequest() {
		super();
	}

	private String leavemsgtime;// 撰写留言时间
	private String leavemsgnodetype;// 撰写留言节点
	private String leavemsgperson;// 撰写留言人姓名
	private String leavemsgpersoncode;// 撰写留言人姓名
	private String leavemsgContent;// 撰写留言内容
	private String registNo;
	public String getLeavemsgpersoncode() {
		return leavemsgpersoncode;
	}

	public void setLeavemsgpersoncode(String leavemsgpersoncode) {
		this.leavemsgpersoncode = leavemsgpersoncode;
	}



	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public LeaveMsgRequest(String reaponseType, String responseCode,
			String responseMessage) {
	}

	public String getLeavemsgContent() {
		return leavemsgContent;
	}

	public void setLeavemsgContent(String leavemsgContent) {
		this.leavemsgContent = leavemsgContent;
	}

	public String getLeavemsgtime() {
		return leavemsgtime;
	}

	public void setLeavemsgtime(String leavemsgtime) {
		this.leavemsgtime = leavemsgtime;
	}

	public String getLeavemsgnodetype() {
		return leavemsgnodetype;
	}

	public void setLeavemsgnodetype(String leavemsgnodetype) {
		this.leavemsgnodetype = leavemsgnodetype;
	}

	public String getLeavemsgperson() {
		return leavemsgperson;
	}

	public void setLeavemsgperson(String leavemsgperson) {
		this.leavemsgperson = leavemsgperson;
	}

}
