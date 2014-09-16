package com.sinosoftyingda.fastclaim.common.model;
/**
 * 任务信息
 * @author haoyun 20130308
 *
 */
public class TaskInfo {
	private String taskReceiptTime="";//任务接受时间
	private String arrivesceneTime="";//到达现场时间
	private String linkCustomTime="";//联系客户时间
	private String taskHandTime="";//任务完成时间
	public TaskInfo() {
		super();
	}
	public TaskInfo(String taskReceiptTime, String arrivesceneTime,
			String linkCustomTime, String taskHandTime) {
		super();
		this.taskReceiptTime = taskReceiptTime;
		this.arrivesceneTime = arrivesceneTime;
		this.linkCustomTime = linkCustomTime;
		this.taskHandTime = taskHandTime;
	}
	public String getTaskReceiptTime() {
		return taskReceiptTime;
	}
	public void setTaskReceiptTime(String taskReceiptTime) {
		this.taskReceiptTime = taskReceiptTime;
	}
	public String getArrivesceneTime() {
		return arrivesceneTime;
	}
	public void setArrivesceneTime(String arrivesceneTime) {
		this.arrivesceneTime = arrivesceneTime;
	}
	public String getLinkCustomTime() {
		return linkCustomTime;
	}
	public void setLinkCustomTime(String linkCustomTime) {
		this.linkCustomTime = linkCustomTime;
	}
	public String getTaskHandTime() {
		return taskHandTime;
	}
	public void setTaskHandTime(String taskHandTime) {
		this.taskHandTime = taskHandTime;
	}
	
}
