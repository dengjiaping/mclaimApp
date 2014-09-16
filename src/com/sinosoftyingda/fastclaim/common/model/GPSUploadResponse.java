package com.sinosoftyingda.fastclaim.common.model;

/**
 * 坐标实时上传返回类
 * @author haoyun 20130425
 *
 */
public class GPSUploadResponse extends CommonResponse {
	/**
	 * 当前位置
	 */
	private String location;
	
	/**
	 * 系统服务时间
	 */
	private String serverTime;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
}
