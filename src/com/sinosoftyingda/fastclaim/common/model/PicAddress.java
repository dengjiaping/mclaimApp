package com.sinosoftyingda.fastclaim.common.model;
/**
 * 照片名称类
 * @author haoyun 20130326
 *
 */
public class PicAddress {
	private String id;
	private String registNo;//报案号
	private String lossNo;//损失编号
	private String picName;//照片名称
	private String picAddress;//拍摄地点
	private String remark;//备注
	private String starte;//状态 0为未上传 1为已上传
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStarte() {
		return starte;
	}
	public void setStarte(String starte) {
		this.starte = starte;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getPicAddress() {
		return picAddress;
	}
	public void setPicAddress(String picAddress) {
		this.picAddress = picAddress;
	}
	
}
