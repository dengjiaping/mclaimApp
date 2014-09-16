package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

public class VerifyLossSubmitRequest extends BasicRequest {

	public static VerifyLossSubmitRequest verifyLossSubmitRequest;

	public synchronized static VerifyLossSubmitRequest getVerifyLossSubmitRequest() {
		if (verifyLossSubmitRequest == null) {
			verifyLossSubmitRequest = new VerifyLossSubmitRequest();
		}
		return verifyLossSubmitRequest;
	}

	public VerifyLossSubmitRequest() {
		super();
	}

	private String lossNo = "";// 损失项目编号
	private String submitType = "";// 提交类型
	private String estimateNo = "";// 定损单号
	private String registNo = "";// 报案号码
	private String plateNo = "";// 车牌号码
	private String kindCode = "";// 定损险别
	private String vehkindCode = "";// 定损车辆种类代码
	private String vehkindName = "";// 定损车辆种类名称
	private String vehcertainCode = "";// 定损车型编码
	private String vehcertaninName = "";// 定损车型名称
	private String vehgroupName = "";// 定损车组名称
	private String vehgroupcode = "";// 定损车组代码
	private String vehsericode = "";// 定损车系编码
	private String vehseriname = "";// 定损车系名称
	private String vehyeartype = "";// 年款
	private String vehfactorycode = "";// 厂家编码
	private String vehfactoryname = "";// 厂家名称
	private String vehbrandcode = "";// 品牌编码
	private String vehbrandname = "";// 品牌名称
	private String certainCalltime = "";// 定损成功联系客户时间
	private String certainFirstPicTime = "";// 定损第一张照片时间
	private String certainComCode = "";// 定损处理人员机构代码
	private String certainHandleCode = "";// 定损处理人员代码
	private String certainHandleName = "";// 定损处理人员名称
	private String repairfactoryCode = "";// 修理厂代码
	private String repairfactoryName = "";// 修理厂名称
	private String repaircooperateFlag = "";// 修理厂合作性质
	private String repairMode = "";// 修理方式
	private String repairReason = "";// 外修原因
	private String repairAptitude = "";// 修理厂资质
	private String subrogateType = "";// 是否代位求偿
	private String excessType = "";// 是否互碰自赔
	private String accidentBook = "";// 有无交管事故书 0否 1是
	private String accident = "";// 是否道路交通事故 0否 1是
	private String insuredName = "";// 被保险人姓名
	private String insureMobile = "";// 被保险人电话
	private String entrustName = "";// 受托人姓名
	private String entrustMobile = "";// 受托人电话
	private String slag = "";// 自定义车型标致
	private String version = "";// 版本号
	private String vehserigradename = "";// 三者车新车购置价
	private String carregisterdate = "";// 车辆初次登记日期
	private String carvehicledesc = "";// 行驶证车辆描述
	private String carfactorycode = "";// 车辆制造厂编码
	private String carfactoryname = "";// 车辆制造厂名称
	private String claimtype = "";// 赔案性质
	private String satisfaction = "";// 满意度评价
	private String sumfitsfee = "";// 合计换件金额
	private String sumrepairfee = "";// 合计修理金额
	private String sumverirest = "";// 合计残值金额
	private String sumcertainloss = "";// 定损合计金额
	private String textconent = "";// 定损备注
	private String shijiufee = "";	//施救费 add by yxf 20140211 

	private String lastUpdateTime = "";// 最后同步理赔时间
	private List<LossFitInfo> lossFitInfo = new ArrayList<LossFitInfo>();// 换件信息
	private List<LossRepairInfo> lossRepairInfo = new ArrayList<LossRepairInfo>();// 修理信息

	public String getShijiufee() {
		return shijiufee==null?"":shijiufee;
	}

	public void setShijiufee(String shijiufee) {
		this.shijiufee = shijiufee;
	}
	public String getKindCode() {
		return kindCode==null?"":kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime==null?"":lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLossNo() {
		return lossNo==null?"":lossNo;
	}

	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}

	public String getSubmitType() {
		return submitType==null?"":submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getRegistNo() {
		return registNo==null?"":registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getPlateNo() {
		return plateNo==null?"":plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getVehkindCode() {
		return vehkindCode==null?"":vehkindCode;
	}

	public void setVehkindCode(String vehkindCode) {
		this.vehkindCode = vehkindCode;
	}

	public String getVehkindName() {
		return vehkindName==null?"":vehkindName;
	}

	public void setVehkindName(String vehkindName) {
		this.vehkindName = vehkindName;
	}

	public String getVehcertainCode() {
		return vehcertainCode==null?"":vehcertainCode;
	}

	public void setVehcertainCode(String vehcertainCode) {
		this.vehcertainCode = vehcertainCode;
	}

	public String getVehcertaninName() {
		return vehcertaninName==null?"":vehcertaninName;
	}

	public void setVehcertaninName(String vehcertaninName) {
		this.vehcertaninName = vehcertaninName;
	}

	public String getCertainHandleCode() {
		return certainHandleCode==null?"":certainHandleCode;
	}

	public void setCertainHandleCode(String certainHandleCode) {
		this.certainHandleCode = certainHandleCode;
	}

	public String getCertainHandleName() {
		return certainHandleName==null?"":certainHandleName;
	}

	public void setCertainHandleName(String certainHandleName) {
		this.certainHandleName = certainHandleName;
	}

	public String getRepairfactoryCode() {
		return repairfactoryCode==null?"":repairfactoryCode;
	}

	public void setRepairfactoryCode(String repairfactoryCode) {
		this.repairfactoryCode = repairfactoryCode;
	}

	public String getRepairfactoryName() {
		return repairfactoryName==null?"":repairfactoryName;
	}

	public void setRepairfactoryName(String repairfactoryName) {
		this.repairfactoryName = repairfactoryName;
	}

	public String getRepaircooperateFlag() {
		return repaircooperateFlag==null?"":repaircooperateFlag;
	}

	public void setRepaircooperateFlag(String repaircooperateFlag) {
		this.repaircooperateFlag = repaircooperateFlag;
	}

	public String getRepairMode() {
		return repairMode==null?"":repairMode;
	}

	public void setRepairMode(String repairMode) {
		this.repairMode = repairMode;
	}

	public String getRepairReason() {
		return repairReason==null?"":repairReason;
	}

	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

	public String getRepairAptitude() {
		return repairAptitude==null?"":repairAptitude;
	}

	public void setRepairAptitude(String repairAptitude) {
		this.repairAptitude = repairAptitude;
	}

	public String getSubrogateType() {
		return subrogateType==null?"":subrogateType;
	}

	public void setSubrogateType(String subrogateType) {
		this.subrogateType = subrogateType;
	}

	public String getExcessType() {
		return excessType==null?"":excessType;
	}

	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}

	public String getAccidentBook() {
		return accidentBook==null?"":accidentBook;
	}

	public void setAccidentBook(String accidentBook) {
		this.accidentBook = accidentBook;
	}

	public String getAccident() {
		return accident==null?"":accident;
	}

	public void setAccident(String accident) {
		this.accident = accident;
	}

	public String getInsuredName() {
		return insuredName==null?"":insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsureMobile() {
		return insureMobile==null?"":insureMobile;
	}

	public void setInsureMobile(String insureMobile) {
		this.insureMobile = insureMobile;
	}

	public String getEntrustName() {
		return entrustName==null?"":entrustName;
	}

	public void setEntrustName(String entrustName) {
		this.entrustName = entrustName;
	}

	public String getEntrustMobile() {
		return entrustMobile==null?"":entrustMobile;
	}

	public void setEntrustMobile(String entrustMobile) {
		this.entrustMobile = entrustMobile;
	}

	public String getEstimateNo() {
		return estimateNo==null?"":estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public String getVehgroupName() {
		return vehgroupName==null?"":vehgroupName;
	}

	public void setVehgroupName(String vehgroupName) {
		this.vehgroupName = vehgroupName;
	}

	public String getVehgroupcode() {
		return vehgroupcode==null?"":vehgroupcode;
	}

	public void setVehgroupcode(String vehgroupcode) {
		this.vehgroupcode = vehgroupcode;
	}

	public String getVehsericode() {
		return vehsericode==null?"":vehsericode;
	}

	public void setVehsericode(String vehsericode) {
		this.vehsericode = vehsericode;
	}

	public String getVehseriname() {
		return vehseriname==null?"":vehseriname;
	}

	public void setVehseriname(String vehseriname) {
		this.vehseriname = vehseriname;
	}

	public String getVehyeartype() {
		return vehyeartype==null?"":vehyeartype;
	}

	public void setVehyeartype(String vehyeartype) {
		this.vehyeartype = vehyeartype;
	}

	public String getVehfactorycode() {
		return vehfactorycode==null?"":vehfactorycode;
	}

	public void setVehfactorycode(String vehfactorycode) {
		this.vehfactorycode = vehfactorycode;
	}

	public String getVehfactoryname() {
		return vehfactoryname==null?"":vehfactoryname;
	}

	public void setVehfactoryname(String vehfactoryname) {
		this.vehfactoryname = vehfactoryname;
	}

	public String getVehbrandcode() {
		return vehbrandcode==null?"":vehbrandcode;
	}

	public void setVehbrandcode(String vehbrandcode) {
		this.vehbrandcode = vehbrandcode;
	}

	public String getVehbrandname() {
		return vehbrandname==null?"":vehbrandname;
	}

	public void setVehbrandname(String vehbrandname) {
		this.vehbrandname = vehbrandname;
	}

	public String getCertainComCode() {
		return certainComCode==null?"":certainComCode;
	}

	public void setCertainComCode(String certainComCode) {
		this.certainComCode = certainComCode;
	}

	public String getSlag() {
		return slag==null?"":slag;
	}

	public void setSlag(String slag) {
		this.slag = slag;
	}

	public String getVersion() {
		return version==null?"":version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVehserigradename() {
		return vehserigradename==null?"":vehserigradename;
	}

	public void setVehserigradename(String vehserigradename) {
		this.vehserigradename = vehserigradename;
	}

	public String getCarregisterdate() {
		return carregisterdate==null?"":carregisterdate;
	}

	public void setCarregisterdate(String carregisterdate) {
		this.carregisterdate = carregisterdate;
	}

	public String getCarvehicledesc() {
		return carvehicledesc==null?"":carvehicledesc;
	}

	public void setCarvehicledesc(String carvehicledesc) {
		this.carvehicledesc = carvehicledesc;
	}

	public String getCarfactorycode() {
		return carfactorycode==null?"":carfactorycode;
	}

	public void setCarfactorycode(String carfactorycode) {
		this.carfactorycode = carfactorycode;
	}

	public String getCarfactoryname() {
		return carfactoryname==null?"":carfactoryname;
	}

	public void setCarfactoryname(String carfactoryname) {
		this.carfactoryname = carfactoryname;
	}

	public String getClaimtype() {
		return claimtype==null?"":claimtype;
	}

	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}

	public String getSatisfaction() {
		return satisfaction==null?"":satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getSumfitsfee() {
		return sumfitsfee==null?"":sumfitsfee;
	}

	public void setSumfitsfee(String sumfitsfee) {
		this.sumfitsfee = sumfitsfee;
	}

	public String getSumrepairfee() {
		return sumrepairfee==null?"":sumrepairfee;
	}

	public void setSumrepairfee(String sumrepairfee) {
		this.sumrepairfee = sumrepairfee;
	}

	public String getSumverirest() {
		return sumverirest==null?"":sumverirest;
	}

	public void setSumverirest(String sumverirest) {
		this.sumverirest = sumverirest;
	}

	public String getSumcertainloss() {
		return sumcertainloss==null?"":sumcertainloss;
	}

	public void setSumcertainloss(String sumcertainloss) {
		this.sumcertainloss = sumcertainloss;
	}

	public String getTextconent() {
		return textconent==null?"":textconent;
	}

	public void setTextconent(String textconent) {
		this.textconent = textconent;
	}

	public List<LossFitInfo> getLossFitInfo() {
		return lossFitInfo;
	}

	public void setLossFitInfo(List<LossFitInfo> lossFitInfo) {
		this.lossFitInfo = lossFitInfo;
	}

	public List<LossRepairInfo> getLossRepairInfo() {
		return lossRepairInfo;
	}

	public void setLossRepairInfo(List<LossRepairInfo> lossRepairInfo) {
		this.lossRepairInfo = lossRepairInfo;
	}

	public String getCertainCalltime() {
		return certainCalltime;
	}

	public void setCertainCalltime(String certainCalltime) {
		this.certainCalltime = certainCalltime;
	}

	public String getCertainFirstPicTime() {
		return certainFirstPicTime;
	}

	public void setCertainFirstPicTime(String certainFirstPicTime) {
		this.certainFirstPicTime = certainFirstPicTime;
	}
	
	
}
