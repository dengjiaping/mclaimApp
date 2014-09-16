package com.sinosoftyingda.fastclaim.common.model;
/**
 * 公用请求类
 * @author haoyun 20130225
 *
 */
public class BasicRequest {
	protected String requestType;//请求类型
	protected String interfaceUserCode="admin";//接口校验用户名
	protected String interfacePassWord="0000";//接口校验密码
	protected String language;//国际化
	protected String userCode;//系统登录名
	protected String passWord;//系统密码
	public BasicRequest() {
		super();
	}
	public BasicRequest(String requestType, String interfaceUserCode,
			String interfacePassWord, String language, String userCode,
			String passWord) {
		super();
		this.requestType = requestType;
		this.interfaceUserCode = interfaceUserCode;
		this.interfacePassWord = interfacePassWord;
		this.language = language;
		this.userCode = userCode;
		this.passWord = passWord;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getInterfaceUserCode() {
		return interfaceUserCode;
	}
	public void setInterfaceUserCode(String interfaceUserCode) {
		this.interfaceUserCode = interfaceUserCode;
	}
	public String getInterfacePassWord() {
		return interfacePassWord;
	}
	public void setInterfacePassWord(String interfacePassWord) {
		this.interfacePassWord = interfacePassWord;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
