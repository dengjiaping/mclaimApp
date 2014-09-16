package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传成功接口
 * 
 * @author haoyun 20130320
 * 
 */
public class PicUploadRequest {

	private String userCode="";//用户名
	private String modelCode="";//模块编码 05为 查勘 07为定损
	private String dptCode="";//部门编码 （机构号）
	private String registNo="";//报案号
	private String lossNo="";//损失编号
	private String fileName="";//文件名称
	private String keyFld1="";//关键字段1(报案号)
	private String keyTime="";//关键时间（报案时间）
	private List<ImageItem> imageItem=new ArrayList<ImageItem>();//文件信息列表
	
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
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getDptCode() {
		return dptCode;
	}
	public void setDptCode(String dptCode) {
		this.dptCode = dptCode;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getKeyFld1() {
		return keyFld1;
	}
	public void setKeyFld1(String keyFld1) {
		this.keyFld1 = keyFld1;
	}
	public String getKeyTime() {
		return keyTime;
	}
	public void setKeyTime(String keyTime) {
		this.keyTime = keyTime;
	}
	public List<ImageItem> getImageItem() {
		return imageItem;
	}
	public void setImageItem(List<ImageItem> imageItem) {
		this.imageItem = imageItem;
	}


}
