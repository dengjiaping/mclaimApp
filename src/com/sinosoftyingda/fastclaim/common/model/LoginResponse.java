package com.sinosoftyingda.fastclaim.common.model;
/**
 *登录返回类
 * @author haoyun
 *
 */
/**
 * @author Administrator
 *
 */
public class LoginResponse {
	private String reaponseType;//返回类型
	private String responseCode;//返回代码
	private String responseMessage;//返回信息
	private String dutyFlag;//上班标志位
	private String userName;//用户姓名
	private String role;//角色
	private String isXiepei;//是否是协赔员  1：协赔员0：非协赔员，包含普通查勘员、高级查勘员
	private String userComCode;//机构代码
	private String userComName;//机构名称
	private String tuoBaoFtpUserCode;//拓保FTP用戶名
	private String tuoBaoFtpPassWord;//拓保FTP密碼
	private String tuoBaoFtpUrl;//拓保FTP地址
	private String tuoBaoFtpPort;//拓保端口号
	private String consultTaskTime;//查阅任务时间
	private String contactclientTime;//成功联系客户时间
	private String uploadCoordinateTime;//GPS上传坐标
	private String serverTime;//快赔服务器时间
	
	private String deviceId;//视频ID
	
	private String hikserViceIp;//海康视频服务器ID
	private String hikserVicePort;//海康视频服务器端口
	private String minaIp;//MINAIP
	private String minaPort;//MINAPORT
	
	
	public String getHikserViceIp() {
		return hikserViceIp;
	}
	public void setHikserViceIp(String hikserViceIp) {
		this.hikserViceIp = hikserViceIp;
	}
	public String getHikserVicePort() {
		return hikserVicePort;
	}
	public void setHikserVicePort(String hikserVicePort) {
		this.hikserVicePort = hikserVicePort;
	}
	public String getMinaIp() {
		return minaIp;
	}
	public void setMinaIp(String minaIp) {
		this.minaIp = minaIp;
	}
	public String getMinaPort() {
		return minaPort;
	}
	public void setMinaPort(String minaPort) {
		this.minaPort = minaPort;
	}
	public String getConsultTaskTime() {
		return consultTaskTime;
	}
	public void setConsultTaskTime(String consultTaskTime) {
		this.consultTaskTime = consultTaskTime;
	}
	public String getContactclientTime() {
		return contactclientTime;
	}
	public void setContactclientTime(String contactclientTime) {
		this.contactclientTime = contactclientTime;
	}
	public String getUploadCoordinateTime() {
		return uploadCoordinateTime;
	}
	public void setUploadCoordinateTime(String uploadCoordinateTime) {
		this.uploadCoordinateTime = uploadCoordinateTime;
	}
	public String getUserComName() {
		return userComName;
	}
	public void setUserComName(String userComName) {
		this.userComName = userComName;
	}
	public String getTuoBaoFtpPort() {
		return tuoBaoFtpPort;
	}
	public void setTuoBaoFtpPort(String tuoBaoFtpPort) {
		this.tuoBaoFtpPort = tuoBaoFtpPort;
	}
	public String getTuoBaoFtpUserCode() {
		return tuoBaoFtpUserCode;
	}
	public void setTuoBaoFtpUserCode(String tuoBaoFtpUserCode) {
		this.tuoBaoFtpUserCode = tuoBaoFtpUserCode;
	}
	public String getTuoBaoFtpPassWord() {
		return tuoBaoFtpPassWord;
	}
	public void setTuoBaoFtpPassWord(String tuoBaoFtpPassWord) {
		this.tuoBaoFtpPassWord = tuoBaoFtpPassWord;
	}
	public String getTuoBaoFtpUrl() {
		return tuoBaoFtpUrl;
	}
	public void setTuoBaoFtpUrl(String tuoBaoFtpUrl) {
		this.tuoBaoFtpUrl = tuoBaoFtpUrl;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public LoginResponse() {
		super();
	}
	public LoginResponse(String reaponseType, String responseCode,
			String responseMessage, String dutyFlag, String userName,
			String role, String isXiepei, String userComCode) {
		super();
		this.reaponseType = reaponseType;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.dutyFlag = dutyFlag;
		this.userName = userName;
		this.role = role;
		this.isXiepei = isXiepei;
		this.userComCode = userComCode;
	}
	public String getReaponseType() {
		return reaponseType;
	}
	public void setReaponseType(String reaponseType) {
		this.reaponseType = reaponseType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getDutyFlag() {
		return dutyFlag;
	}
	public void setDutyFlag(String dutyFlag) {
		this.dutyFlag = dutyFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getIsXiepei() {
		return isXiepei;
	}
	public void setIsXiepei(String isXiepei) {
		this.isXiepei = isXiepei;
	}
	public String getUserComCode() {
		return userComCode;
	}
	public void setUserComCode(String userComCode) {
		this.userComCode = userComCode;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
