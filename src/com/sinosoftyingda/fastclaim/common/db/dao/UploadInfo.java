package com.sinosoftyingda.fastclaim.common.db.dao;

/**
 * 文件上传记录
 * 
 * @author haoyun
 * 
 */
public class UploadInfo {
	public static String t_ID="id";
	public static String t_PLATE_NO = "plate_no";
	public static String t_POLICY_NO = "policy_no";
	public static String t_STATE = "state";
	public static String t_PARCENT = "parcent";
	public static String t_FILE_SIZE = "file_size";
	public static String t_ACTION = "action";
	public static String t_FILE_URL = "file_url";

	private String id;
	private String plateNo; // 损失编号
	private String policyNo; // 报案号
	private String state; // 状态
	private String parcent; // 上传进度
	private String filesize; // 文件大小
	private String action; // 文件名称
	private String fileUrl;//本地路径
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParcent() {
		return parcent;
	}

	public void setParcent(String parcent) {
		this.parcent = parcent;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
