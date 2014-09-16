package com.sinosoftyingda.fastclaim.certainLoss.model;
/**
 * 满意度调查
 * @author haoyun 20130301
 *
 */

public class RepairAndChangeMsg {
	private String msg1;
	private String msg2;
	private String msg3;
	public RepairAndChangeMsg() {
		super();
	}
	public RepairAndChangeMsg(String msg1, String msg2, String msg3) {
		super();
		this.msg1 = msg1;
		this.msg2 = msg2;
		this.msg3 = msg3;
	}
	public String getMsg1() {
		return msg1;
	}
	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}
	public String getMsg2() {
		return msg2;
	}
	
	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}
	public String getMsg3() {
		return msg3;
	}
	public void setMsg3(String msg3) {
		this.msg3 = msg3;
	}
}
