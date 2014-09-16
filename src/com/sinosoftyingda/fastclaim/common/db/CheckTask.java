package com.sinosoftyingda.fastclaim.common.db;


import com.sinosoftyingda.fastclaim.common.widget.MyExpandableListAdapter.Group.Child;

import android.widget.TextView;

/**
 * 查勘任务列表�?
 * 
 * @author JingTuo
 * 
 */
public class CheckTask extends Child{
	private String registno = "";// 报案�?
	private String latitude = "";// 出现地位置x坐标
	private String longitude = "";// 出现地位置y坐标
	private String linkername = "";// 联系人姓�?
	private String linkerphoneno = "";// 联系人电话号�?
	private String reportorname = "";// 报案人姓�?
	private String reportorphoneno = "";// 报案人电话号�?
	private String insuredname = "";// 投保人姓�?
	private String insuredphoneno = "";// 投保人电话号�?
	private String inputedate = "";// 流入快赔日期
	private String surveytaskstatus = "";// 任务状�?
	private String applycannelstatus = "";// 申请改派状�?
	private String synchrostatus = "";// 同步状�?
	private String insrtedname = "";// 被保险人姓名
	private String licenseno = "";// 号牌号码
	private String taskFinishTime="";//任务完成时间
	private String SeriaNo="";	//强制改派状态为序号
	// by jingtuo start
	private int isread = 0;// 是否已读标识
	private int isaccept = 0;// 是否接受标识
	private String createtime = "";// 创建时间
	private String ordertime = "";// 预约时间
	private int alarmtime = 0;// 闹钟时间
	private String dispatchreason = "";// 改派原因
	private int iscontact = 0;// 是否联系联系�?
	private String contacttime = "";
	private String completetime = "";
	private TextView tvTime;// 显示闹钟时间

	// by jingtuo end

	
	public String getSeriaNo() {
		return SeriaNo;
	}

	public void setSeriaNo(String seriaNo) {
		SeriaNo = seriaNo;
	}
	public String getRegistno() {
		return registno;
	}

	public String getTaskFinishTime() {
		return taskFinishTime;
	}

	public void setTaskFinishTime(String taskFinishTime) {
		this.taskFinishTime = taskFinishTime;
	}

	public void setRegistno(String registno) {
		this.registno = registno;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLinkername() {
		return linkername;
	}

	public void setLinkername(String linkername) {
		this.linkername = linkername;
	}

	public String getLinkerphoneno() {
		return linkerphoneno;
	}

	public void setLinkerphoneno(String linkerphoneno) {
		this.linkerphoneno = linkerphoneno;
	}

	public String getReportorname() {
		return reportorname;
	}

	public void setReportorname(String reportorname) {
		this.reportorname = reportorname;
	}

	public String getReportorphoneno() {
		return reportorphoneno;
	}

	public void setReportorphoneno(String reportorphoneno) {
		this.reportorphoneno = reportorphoneno;
	}

	public String getInsuredname() {
		return insuredname;
	}

	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}

	public String getInsuredphoneno() {
		return insuredphoneno;
	}

	public void setInsuredphoneno(String insuredphoneno) {
		this.insuredphoneno = insuredphoneno;
	}

	public String getInputedate() {
		return inputedate;
	}

	public void setInputedate(String inputedate) {
		this.inputedate = inputedate;
	}

	public String getSurveytaskstatus() {
		return surveytaskstatus;
	}

	public void setSurveytaskstatus(String surveytaskstatus) {
		this.surveytaskstatus = surveytaskstatus;
	}

	public String getApplycannelstatus() {
		return applycannelstatus;
	}

	public void setApplycannelstatus(String applycannelstatus) {
		this.applycannelstatus = applycannelstatus;
	}

	public String getSynchrostatus() {
		return synchrostatus;
	}

	public void setSynchrostatus(String synchrostatus) {
		this.synchrostatus = synchrostatus;
	}

	public String getInsrtedname() {
		return insrtedname;
	}

	public void setInsrtedname(String insrtedname) {
		this.insrtedname = insrtedname;
	}

	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public int getAlarmtime() {
		return alarmtime;
	}

	public void setAlarmtime(int alarmtime) {
		this.alarmtime = alarmtime;
	}

	public String getDispatchreason() {
		return dispatchreason;
	}

	public void setDispatchreason(String dispatchreason) {
		this.dispatchreason = dispatchreason;
	}

	public TextView getTvTime() {
		return tvTime;
	}

	public void setTvTime(TextView tvTime) {
		this.tvTime = tvTime;
	}

	public int getIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public int getIsaccept() {
		return isaccept;
	}

	public void setIsaccept(int isaccept) {
		this.isaccept = isaccept;
	}

	public int getIscontact() {
		return iscontact;
	}

	public void setIscontact(int iscontact) {
		this.iscontact = iscontact;
	}

	public String getContacttime() {
		return contacttime;
	}

	public void setContacttime(String contacttime) {
		this.contacttime = contacttime;
	}

	public String getCompletetime() {
		return completetime;
	}

	public void setCompletetime(String completetime) {
		this.completetime = completetime;
	}

	
	
}
