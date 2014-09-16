package com.sinosoftyingda.fastclaim.common.model;
/**
 * 文件信息列表
 * @author haoyun 20130320
 *
 */
public class ImageItem {
	private String imageName;//图片名称
	private String imageMakeAddress;//图片拍摄地址
	private String remark;//备注
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageMakeAddress() {
		return imageMakeAddress;
	}
	public void setImageMakeAddress(String imageMakeAddress) {
		this.imageMakeAddress = imageMakeAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
