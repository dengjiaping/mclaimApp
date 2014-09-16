package com.sinosoftyingda.fastclaim.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 下拉控件的数据
 * 
 * @author chenjianfan
 * 
 */
public class SpinnerBean implements Parcelable {

	private String id;
	private String value;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SpinnerBean [id=" + id + ", value=" + value + "]";
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		

	}

}
