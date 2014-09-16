package com.sinosoftyingda.fastclaim.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 查勘明细信息返回报文
 * @author haoyun 20130226
 *
 */
/**
 * @author Administrator
 *
 */
public class DetailTaskQueryResponse extends CommonResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6180633816552620268L;
	public static DetailTaskQueryResponse detailTaskQueryResponse;
	public  static DetailTaskQueryResponse getDetailTaskQueryResponse(){
		if(detailTaskQueryResponse==null){
			detailTaskQueryResponse = new DetailTaskQueryResponse();
		}
		return detailTaskQueryResponse;
	}
	//add by jingtuo 同步提交时候需要该字段
	private String reason = "";//事故原因
	private String damageTypeCode = "";//事故类型代码
	private String damageTypeName = "";//事故类型名称
	//add by jingtuo
	
	private String registNo="";//报案号
	private String insrtedName="";//被保险人姓名
	private String licenseNo="";//车牌号
	private String brandName="";//厂牌型号
	private String dispatchptime="";//出险时间
	private String reporttime="";//报案时间
	private String dispatchplace="";//出险地点
	private String drivername="";//驾驶员姓名
	private String promptmessage="";//距保险起或止日期提示
	private String damageDayp="";//近几天多次出现提示
	private String taskreceiptTime="";//任务接收时间
	private String arricesceneTime="";//到达时间
	private String linkcustomTime="";//联系客户时间
	private String taskHandTime="";//任务完成时间
	private String firstsiteFlag="";//是否是第一现场
	private String damageName="";//出现原因
	private String damageCode="";//出现原因代码
	private String indemnityDuty="";//事故责任
	private String claimType="";//赔案性质
	private String isCommonClaim="";//是否通赔
	private String subrogateType="";//是否代为
	private String accidentBook="";//有无交管事故书
	private String accident="";//是否道路交通事故
	private String insuredMobile="";//被保险人电话
	private String entrustName="";//受托人姓名
	private String entrustMobile="";//受托人电话
	private String checkReport="";//查勘报告
	private String satisfacTion="";//满意度调查
	private String acceptTaskDate="";//查勘任务受理时间
	private String disposeTaskDate="";//查勘开始处理时间(与首张照片时间是一个时间)
	private String remark="";//备注
	private String submitType="";//提交类型 进行提交或同步操作时必填  synch：同步submit：提交 add 20130322
	private String firstphotoDate="";//首张照片时间 add 20130322
	private String orderTime = ""; // 查勘预约时间 add 20130718 DengGuang
	private String compensateRate = ""; // 增加赔付率 add 20140522 yxf
	private List<CarLoss> carLossList = new ArrayList<CarLoss>();//涉损车辆
	private List<CheckExt> checkExtList = new ArrayList<CheckExt>();//查勘要点
	private List<CheckDriver> checkDriver = new ArrayList<CheckDriver>();//驾驶员信息
	
	public String getCompensateRate() {
		return compensateRate;
	}
	public void setCompensateRate(String compensateRate) {
		this.compensateRate = compensateRate;
	}
	
	public String getFirstphotoDate() {
		return firstphotoDate;
	}
	public void setFirstphotoDate(String firstphotoDate) {
		this.firstphotoDate = firstphotoDate;
	}
	/**
	 * 提交类型 进行提交或同步操作时必填  synch：同步submit：提交
	 * @return
	 */
	public String getSubmitType() {
		return submitType;
	}
	/**
	 * 提交类型 进行提交或同步操作时必填  synch：同步submit：提交
	 * @return
	 */
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAcceptTaskDate() {
		return acceptTaskDate;
	}
	public void setAcceptTaskDate(String acceptTaskDate) {
		this.acceptTaskDate = acceptTaskDate;
	}
	public String getDisposeTaskDate() {
		return disposeTaskDate;
	}
	public void setDisposeTaskDate(String disposeTaskDate) {
		this.disposeTaskDate = disposeTaskDate;
	}
	public String getDamageCode() {
		return damageCode;
	}
	public List<CheckDriver> getCheckDriver() {
		return checkDriver;
	}
	public void setCheckDriver(List<CheckDriver> checkDriver) {
		this.checkDriver = checkDriver;
	}
	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getInsrtedName() {
		return insrtedName;
	}
	public void setInsrtedName(String insrtedName) {
		this.insrtedName = insrtedName;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDispatchptime() {
		return dispatchptime;
	}
	public void setDispatchptime(String dispatchptime) {
		this.dispatchptime = dispatchptime;
	}
	public String getReporttime() {
		return reporttime;
	}
	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}
	public String getDispatchplace() {
		return dispatchplace;
	}
	public void setDispatchplace(String dispatchplace) {
		this.dispatchplace = dispatchplace;
	}
	public String getDrivername() {
		return drivername;
	}
	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	public String getPromptmessage() {
		return promptmessage;
	}
	public void setPromptmessage(String promptmessage) {
		this.promptmessage = promptmessage;
	}
	public String getDamageDayp() {
		return damageDayp;
	}
	public void setDamageDayp(String damageDayp) {
		this.damageDayp = damageDayp;
	}
	public String getTaskreceiptTime() {
		return taskreceiptTime;
	}
	public void setTaskreceiptTime(String taskreceiptTime) {
		this.taskreceiptTime = taskreceiptTime;
	}
	public String getArricesceneTime() {
		return arricesceneTime;
	}
	public void setArricesceneTime(String arricesceneTime) {
		this.arricesceneTime = arricesceneTime;
	}
	public String getLinkcustomTime() {
		return linkcustomTime;
	}
	public void setLinkcustomTime(String linkcustomTime) {
		this.linkcustomTime = linkcustomTime;
	}
	public String getTaskHandTime() {
		return taskHandTime;
	}
	public void setTaskHandTime(String taskHandTime) {
		this.taskHandTime = taskHandTime;
	}
	public String getFirstsiteFlag() {
		return firstsiteFlag;
	}
	public void setFirstsiteFlag(String firstsiteFlag) {
		this.firstsiteFlag = firstsiteFlag;
	}
	public String getDamageName() {
		return damageName;
	}
	public void setDamageName(String damageName) {
		this.damageName = damageName;
	}
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getIsCommonClaim() {
		return isCommonClaim;
	}
	public void setIsCommonClaim(String isCommonClaim) {
		this.isCommonClaim = isCommonClaim;
	}
	public String getSubrogateType() {
		return subrogateType;
	}
	public void setSubrogateType(String subrogateType) {
		this.subrogateType = subrogateType;
	}
	public String getAccidentBook() {
		return accidentBook;
	}
	public void setAccidentBook(String accidentBook) {
		this.accidentBook = accidentBook;
	}
	public String getAccident() {
		return accident;
	}
	public void setAccident(String accident) {
		this.accident = accident;
	}
	public String getInsuredMobile() {
		return insuredMobile;
	}
	public void setInsuredMobile(String insuredMobile) {
		this.insuredMobile = insuredMobile;
	}
	public String getEntrustName() {
		return entrustName;
	}
	public void setEntrustName(String entrustName) {
		this.entrustName = entrustName;
	}
	public String getEntrustMobile() {
		return entrustMobile;
	}
	public void setEntrustMobile(String entrustMobile) {
		this.entrustMobile = entrustMobile;
	}
	public String getCheckReport() {
		return checkReport;
	}
	public void setCheckReport(String checkReport) {
		this.checkReport = checkReport;
	}
	public String getSatisfacTion() {
		return satisfacTion;
	}
	public void setSatisfacTion(String satisfacTion) {
		this.satisfacTion = satisfacTion;
	}
	public List<CarLoss> getCarLossList() {
		return carLossList;
	}
	public void setCarLossList(List<CarLoss> carLossList) {
		this.carLossList = carLossList;
	}
	public List<CheckExt> getCheckExtList() {
		return checkExtList;
	}
	public void setCheckExtList(List<CheckExt> checkExtList) {
		this.checkExtList = checkExtList;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public DetailTaskQueryResponse() {
		super();
	
	}
	public String getDamageTypeCode() {
		return damageTypeCode;
	}
	public void setDamageTypeCode(String damageTypeCode) {
		this.damageTypeCode = damageTypeCode;
	}
	public String getDamageTypeName() {
		return damageTypeName;
	}
	public void setDamageTypeName(String damageTypeName) {
		this.damageTypeName = damageTypeName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
	
}
