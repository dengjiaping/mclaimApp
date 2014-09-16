package com.sinosoftyingda.fastclaim.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查勘要点
 * 
 * @author haoyun 20130307
 * 
 */
public class CheckExt implements Parcelable {
	private String checkKernelCode="";// 查勘要点代码
	private String serialNo="";//序号
	private String checkKernelName="";//查勘要点名称
	private String checkKernelSelect="";// 查勘要点选项
	private String checkKernelType="";//查勘分类
	private String checkExtRemark ="";//备注


	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCheckKernelName() {
		return checkKernelName;
	}

	public void setCheckKernelName(String checkKernelName) {
		this.checkKernelName = checkKernelName;
	}

	public String getCheckKernelCode() {
		return checkKernelCode;
	}

	public void setCheckKernelCode(String checkKernelCode) {
		this.checkKernelCode = checkKernelCode;
	}

	public String getCheckExtRemark() {
		return checkExtRemark;
	}

	public void setCheckExtRemark(String checkExtRemark) {
		this.checkExtRemark = checkExtRemark;
	}

	public CheckExt() {
		super();
	}

	

	public String getCheckKernelSelect() {
		return checkKernelSelect;
	}

	public void setCheckKernelSelect(String checkKernelSelect) {
		this.checkKernelSelect = checkKernelSelect;
	}

	public String getCheckKernelType() {
		return checkKernelType;
	}

	public void setCheckKernelType(String checkKernelType) {
		this.checkKernelType = checkKernelType;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
}
