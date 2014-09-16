package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 定损信息返回类
 * @author haoyun 20130308
 *
 */
public class DefLossInfoQueryResponse extends CommonResponse {
	public static DefLossInfoQueryResponse deflossinfoQueryResponse;

	private String submitType="";//提交类型
	private Regist regist=new Regist();//报案信息
	private SurveyKeyPoint surveyKeyPoint=new SurveyKeyPoint();//查勘要点
	private List<CarLossInfo> carLossInfos=new ArrayList<CarLossInfo>();//案件涉损(LOSSOFCASELIST)多条车损信息(CARLOSSINFO)(一个对象中某属性区分是标的或三者)
	private TaskInfo taskInfo =new TaskInfo();//任务信息
	private List<DefLossCarInfo> defLossCarInfos=new ArrayList<DefLossCarInfo>();//定损对象多条定损车辆信息
	private DefLossContent defLossContent = new DefLossContent();//定损内容
	private List<CheckExt> checkExt=new ArrayList<CheckExt>();//查勘要点信息
	private List<ItemKind> itemKinds=new ArrayList<ItemKind>();//险别
	
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public List<ItemKind> getItemKinds() {
		return itemKinds;
	}
	public void setItemKinds(List<ItemKind> itemKinds) {
		this.itemKinds = itemKinds;
	}
	public List<CheckExt> getCheckExt() {
		return checkExt;
	}
	public void setCheckExt(List<CheckExt> checkExt) {
		this.checkExt = checkExt;
	}
	public DefLossInfoQueryResponse() {
		super();
	}
	
	public Regist getRegist() {
		return regist;
	}
	public void setRegist(Regist regist) {
		this.regist = regist;
	}
	public SurveyKeyPoint getSurveyKeyPoint() {
		return surveyKeyPoint;
	}
	public void setSurveyKeyPoint(SurveyKeyPoint surveyKeyPoint) {
		this.surveyKeyPoint = surveyKeyPoint;
	}
	public List<CarLossInfo> getCarLossInfos() {
		return carLossInfos;
	}
	public void setCarLossInfos(List<CarLossInfo> carLossInfos) {
		this.carLossInfos = carLossInfos;
	}
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	public List<DefLossCarInfo> getDefLossCarInfos() {
		return defLossCarInfos;
	}
	public void setDefLossCarInfos(List<DefLossCarInfo> defLossCarInfos) {
		this.defLossCarInfos = defLossCarInfos;
	}
	public DefLossContent getDefLossContent() {
		return defLossContent;
	}
	public void setDefLossContent(DefLossContent defLossContent) {
		this.defLossContent = defLossContent;
	}
}
