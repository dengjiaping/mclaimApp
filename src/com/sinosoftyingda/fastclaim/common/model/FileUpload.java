package com.sinosoftyingda.fastclaim.common.model;
/**
 * 影响资料主表
 * @author haoyun 20130327
 *
 */
public class FileUpload {
	private String id;//主键ID
	private String registNo;//报案号
	private String lossNo;//定损编号
	private String fileName;//文件名称
	private String zipPath;//zip上传路径
	private String keyTime;//报案时间
	private String uploadStarte;//上传是否成功 0为未成功 1为已成功
	private String parcent; //上传进度
	public String getParcent() {
		return parcent;
	}
	public void setParcent(String parcent) {
		this.parcent = parcent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getZipPath() {
		return zipPath;
	}
	public void setZipPath(String zipPath) {
		this.zipPath = zipPath;
	}
	public String getKeyTime() {
		return keyTime;
	}
	public void setKeyTime(String keyTime) {
		this.keyTime = keyTime;
	}
	public String getUploadStarte() {
		return uploadStarte;
	}
	public void setUploadStarte(String uploadStarte) {
		this.uploadStarte = uploadStarte;
	}

}
