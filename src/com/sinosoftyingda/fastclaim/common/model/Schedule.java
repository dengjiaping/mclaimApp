package com.sinosoftyingda.fastclaim.common.model;
/**
 * 调度实体类
 * @author haoyun 20130302
 *
 */
public class Schedule {
	private String scheduleuserCode;//调度人员
	private String scheduleinputTime;//调度时间
	public Schedule() {
		super();
	}
	public Schedule(String scheduleuserCode, String scheduleinputTime) {
		super();
		this.scheduleuserCode = scheduleuserCode;
		this.scheduleinputTime = scheduleinputTime;
	}
	public String getScheduleuserCode() {
		return scheduleuserCode;
	}
	public void setScheduleuserCode(String scheduleuserCode) {
		this.scheduleuserCode = scheduleuserCode;
	}
	public String getScheduleinputTime() {
		return scheduleinputTime;
	}
	public void setScheduleinputTime(String scheduleinputTime) {
		this.scheduleinputTime = scheduleinputTime;
	}
}
