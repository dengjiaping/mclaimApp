package com.sinosoftyingda.fastclaim.common.model;
/**
 * 登录请求类
 * @author haoyun 20130222
 *
 */
public class LoginRequest extends BasicRequest{

	private String IMEI;//终端唯一标识号



	public LoginRequest() {
		super();
	}

	public LoginRequest(String iMEI) {
		super();
		IMEI = iMEI;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	
	
}
