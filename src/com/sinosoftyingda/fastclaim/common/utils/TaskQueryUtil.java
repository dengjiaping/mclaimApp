package com.sinosoftyingda.fastclaim.common.utils;
import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
/**
 * 任务列表工具类
 * 
 * @author haoyun 20130306
 * 
 */
public class TaskQueryUtil {
	/**
	 * 查勘未处理
	 */
	public static List<CheckTask> checkTaskUntreated = new ArrayList<CheckTask>();
	/**
	 * 查勘正在处理
	 */
	public static List<CheckTask> checkTaskProcessing = new ArrayList<CheckTask>();
	/**
	 * 查看已完成
	 */
	public static List<CheckTask> checkTaskCompleted = new ArrayList<CheckTask>();
	/**
	 * 查勘改派
	 */
	public static List<CheckTask> checkTaskReassignment = new ArrayList<CheckTask>();
	/**
	 * 定损未处理
	 */
	public static List<CertainLossTask> certainLossTaskUntreated = new ArrayList<CertainLossTask>();
	/**
	 * 定损正在处理
	 */
	public static List<CertainLossTask> certainLossTaskProcessing = new ArrayList<CertainLossTask>();
	/**
	 * 定损已完成
	 */
	public static List<CertainLossTask> certainLossTaskCompleted = new ArrayList<CertainLossTask>();
	/**
	 * 定损改派
	 */
	public static List<CertainLossTask> certainLossTaskReassignment = new ArrayList<CertainLossTask>();
	/**
	 * 定损核损打回
	 */
	public static List<CertainLossTask> certainLossTaskNuclearDamageBrak = new ArrayList<CertainLossTask>();

	public TaskQueryUtil(TaskQueryResponse response) {
		init(response);
	}

	/**
	 * 任务分化类
	 * 
	 * @param request
	 * @param context
	 */
	private static void init(TaskQueryResponse response) {
		List<CheckTask> checkTask = response.getCheckTask();
		for (int i = 0; i < checkTask.size(); i++) {//查勘任务
			if (checkTask.get(i).getSurveytaskstatus().equals("1")) {//未完成
				checkTaskUntreated.add(checkTask.get(i));
			} else if (checkTask.get(i).getSurveytaskstatus().equals("2")) {//正在处理
				checkTaskProcessing.add(checkTask.get(i));
			} else if (checkTask.get(i).getSurveytaskstatus().equals("4")) {//已完成
				checkTaskCompleted.add(checkTask.get(i));
			}
		}
		for (int i = 0; i < checkTaskCompleted.size(); i++) {//获取改派申请中的任务
			if ("Apply".equals(checkTaskCompleted.get(i))) {
				checkTaskReassignment.add(checkTaskCompleted.get(i));
				checkTaskCompleted.remove(i);
			}
		}
		List<CertainLossTask> certainLossTask = response.getCertainLossTask();
		for (int i = 0; i < certainLossTask.size(); i++) {//定损任务
			if (certainLossTask.get(i).getSurveytaskstatus().equals("1")) {//未完成任务
				certainLossTaskUntreated.add(certainLossTask.get(i));
			} else if (certainLossTask.get(i).getSurveytaskstatus().equals("2"))//正在处理
			{
				certainLossTaskProcessing.add(certainLossTask.get(i));

			} else if (certainLossTask.get(i).getSurveytaskstatus().equals("4")) {//已完成
				certainLossTaskCompleted.add(certainLossTask.get(i));
			} else if (certainLossTask.get(i).getSurveytaskstatus().equals("5")) {//核损打回
				certainLossTaskNuclearDamageBrak.add(certainLossTask.get(i));
			}
		}
		for (int i = 0; i < certainLossTaskCompleted.size(); i++) {//改派任务
			if ("Apply".equals(certainLossTaskCompleted.get(i))) {
				certainLossTaskReassignment.add(certainLossTaskCompleted.get(i));
				certainLossTaskCompleted.remove(i);
			}
		}
	}
}
