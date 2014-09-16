package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 查勘提交同步请求类
 * @author haoyun 20130307
 *
 */
public class CheckSubmitRequest extends BasicRequest {
	private String registNo="";//报案号
	private String submitType="";//提交类型
	private String submitDate="";//提交时间
	private String checkType="";//查勘类型
	private String firstsiteFlag="";//是否第一现场
	private String claimType="";//案件类型
	private String damagetCode="";//出险原因代码
	private String damageName="";//出险原因名称
	private String damageTypeCode="";//事故类型代码
	private String damageTypeName="";//事故类型说明
	private String subrogateType="";//是否代为求偿
	private String excessType="";//是否通赔
	private String accidentType="";//保险事故分类
	private String accidentBook="";//有无交管事故书
	private String accident="";//是否道路交通事故
	private String insuredName="";//被保险人姓名
	private String insuredMoble="";//被保险人电话
	private String entrustName="";//受托人姓名
	private String entrustMobile="";//受托人电话
	private String otherClaim="";//异地理赔标识
	private String satisfAction="";//满意度调查
	private String checkReport="";//查勘报告
	private String callclientDate="";//查勘成功联系客户时间
	private String firstphotoDate="";//查勘首张照片时间
//	private String acceptTaskDate="";//查勘任务受理时间
	private String disposeTaskDate="";//查勘开始处理时间(与首张照片时间是一个时间)
//	public String getAcceptTaskDate() {
//		return acceptTaskDate;
//	}
//	public void setAcceptTaskDate(String acceptTaskDate) {
//		this.acceptTaskDate = acceptTaskDate;
//	}
	public String getDisposeTaskDate() {
		return disposeTaskDate;
	}
	public void setDisposeTaskDate(String disposeTaskDate) {
		this.disposeTaskDate = disposeTaskDate;
	}
	public String getCallclientDate() {
		return callclientDate;
	}
	public void setCallclientDate(String callclientDate) {
		this.callclientDate = callclientDate;
	}
	public String getFirstphotoDate() {
		return firstphotoDate;
	}
	public void setFirstphotoDate(String firstphotoDate) {
		this.firstphotoDate = firstphotoDate;
	}
	private List<CheckExtSubmit> checkExtSumbit=new ArrayList<CheckExtSubmit>();//查勘信息扩展
	private List<CheckThirdParty> checkThirdParyList=new ArrayList<CheckThirdParty>();//查勘车辆
	private List<CheckDriver> checkDriverList=new ArrayList<CheckDriver>();//驾驶员
	
	public List<CheckDriver> getCheckDriverList() {
		return checkDriverList;
	}
	public void setCheckDriverList(List<CheckDriver> checkDriverList) {
		this.checkDriverList = checkDriverList;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getFirstsiteFlag() {
		return firstsiteFlag;
	}
	public void setFirstsiteFlag(String firstsiteFlag) {
		this.firstsiteFlag = firstsiteFlag;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getDamagetCode() {
		return damagetCode;
	}
	public void setDamagetCode(String damagetCode) {
		this.damagetCode = damagetCode;
	}
	public String getDamageName() {
		return damageName;
	}
	public void setDamageName(String damageName) {
		this.damageName = damageName;
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
	public String getSubrogateType() {
		return subrogateType;
	}
	public void setSubrogateType(String subrogateType) {
		this.subrogateType = subrogateType;
	}
	public String getExcessType() {
		return excessType;
	}
	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
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
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredMoble() {
		return insuredMoble;
	}
	public void setInsuredMoble(String insuredMoble) {
		this.insuredMoble = insuredMoble;
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
	public String getOtherClaim() {
		return otherClaim;
	}
	public void setOtherClaim(String otherClaim) {
		this.otherClaim = otherClaim;
	}
	public String getSatisfAction() {
		return satisfAction;
	}
	public void setSatisfAction(String satisfAction) {
		this.satisfAction = satisfAction;
	}
	public String getCheckReport() {
		return checkReport;
	}
	public void setCheckReport(String checkReport) {
		this.checkReport = checkReport;
	}
	public List<CheckExtSubmit> getCheckExtSumbit() {
		return checkExtSumbit;
	}
	public void setCheckExtSumbit(List<CheckExtSubmit> checkExtSumbit) {
		this.checkExtSumbit = checkExtSumbit;
	}
	public List<CheckThirdParty> getCheckThirdParyList() {
		return checkThirdParyList;
	}
	public void setCheckThirdParyList(List<CheckThirdParty> checkThirdParyList) {
		this.checkThirdParyList = checkThirdParyList;
	}
	
}
