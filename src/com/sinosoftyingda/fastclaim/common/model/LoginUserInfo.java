package com.sinosoftyingda.fastclaim.common.model;

/**
 * 用户表
 * @author haoyun 20130408
 *
 */
public class LoginUserInfo {
	private String id;
	private String userName;//用户名
	private String passWord;//密码
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
