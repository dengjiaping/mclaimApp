package com.sinosoftyingda.fastclaim.common.model;
/**
 * 版本更新返回类
 * @author haoyun 20130304
 *
 */
public class UpdateVersionResponse extends CommonResponse {
	private String updateUrl;//更新地址

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public UpdateVersionResponse(String reaponseType, String responseCode,
			String responseMessage, String updateUrl) {
		super(reaponseType, responseCode, responseMessage);
		this.updateUrl = updateUrl;
	}

	public UpdateVersionResponse() {
		super();
	}
	
}
