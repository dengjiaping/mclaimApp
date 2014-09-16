package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;

/**
 * 查勘信息列表返回类
 * @author haoyun 20130305
 *
 */
public class TaskQueryResponse extends CommonResponse{
	private List<CheckTask> checkTask;//查勘信息
	private List<CertainLossTask> certainLossTask;//定损任务
	public List<CheckTask> getCheckTask() {
		return checkTask;
	}
	public void setCheckTask(List<CheckTask> checkTask) {
		this.checkTask = checkTask;
	}
	public List<CertainLossTask> getCertainLossTask() {
		return certainLossTask;
	}
	public void setCertainLossTask(List<CertainLossTask> certainLossTask) {
		this.certainLossTask = certainLossTask;
	}
	public TaskQueryResponse(String reaponseType, String responseCode,
			String responseMessage, List<CheckTask> checkTask,
			List<CertainLossTask> certainLossTask) {
		super(reaponseType, responseCode, responseMessage);
		this.checkTask = checkTask;
		this.certainLossTask = certainLossTask;
	}
	public TaskQueryResponse() {
		super();
		this.checkTask = new ArrayList<CheckTask>();
		this.certainLossTask = new ArrayList<CertainLossTask>();
	}
	
	
}
