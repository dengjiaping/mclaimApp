package com.sinosoftyingda.fastclaim.common.apn.model;

/**
 * 手机默认会有两个APN:CMNET(cmnet)、CMWAP(cmwap)<br>
 * 如上：name-->CMNET，apn-->cmnet<br>
 * @author JingTuo
 *
 */
public class ApnDto {
	
	/**
	 * 主键
	 */
	private int id;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * APN
	 */
	private String apn;

	/**
	 * 代理
	 */
	private String proxy;

	/**
	 * 端口
	 */
	private String port;

	/**
	 * 用户名
	 */
	private String user;

	/**
	 * 服务器
	 */
	private String server;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 彩信代码
	 */
	private String mmsc;

	/**
	 * 彩信代理
	 */
	private String mmsproxy;

	/**
	 * 采集端口
	 */
	private String mmsport;

	/**
	 * 移动国家代码
	 */
	private String mcc;

	/**
	 * 移动网络代码
	 */
	private String mnc;

	/**
	 * 号码
	 */
	private String numeric;

	/**
	 * APN类型
	 */
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMmsc() {
		return mmsc;
	}

	public void setMmsc(String mmsc) {
		this.mmsc = mmsc;
	}

	public String getMmsproxy() {
		return mmsproxy;
	}

	public void setMmsproxy(String mmsproxy) {
		this.mmsproxy = mmsproxy;
	}

	public String getMmsport() {
		return mmsport;
	}

	public void setMmsport(String mmsport) {
		this.mmsport = mmsport;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getNumeric() {
		return numeric;
	}

	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
