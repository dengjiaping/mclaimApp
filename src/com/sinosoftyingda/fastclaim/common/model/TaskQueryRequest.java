package com.sinosoftyingda.fastclaim.common.model;

/**
 * 查勘信息列表请求类
 * @author haoyun 20130305
 *
 */
public class TaskQueryRequest extends BasicRequest{
	private String taskType;
	private String taskStatus;
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public TaskQueryRequest(String taskType, String taskStatus) {
		super();
		this.taskType = taskType;
		this.taskStatus = taskStatus;
	}
	public TaskQueryRequest() {
		super();
	}
	
	
}
