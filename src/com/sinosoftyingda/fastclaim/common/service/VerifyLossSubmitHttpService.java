package com.sinosoftyingda.fastclaim.common.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
import com.sinosoftyingda.fastclaim.defloss.util.UtilIsNotNull;

public class VerifyLossSubmitHttpService {
	/**
	 * 定损提交接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse lossSubmitService(
			DefLossInfoQueryResponse defLossInfoQueryResponse, String url) {
		CommonResponse responseDuty = new CommonResponse();
		boolean isSubmit = false;
		try {
			String message = "";
			VerifyLossSubmitRequest request = tranisition(defLossInfoQueryResponse);
			if (!request.getSubmitType().equals("synch")) {

				/* DengGuang 添加，定损业务规则无损0提交 */
				UtilIsNotNull utilIsNotNull = new UtilIsNotNull();
				// 互碰自赔的案件：三者车无损提交即0提交
				boolean isOCommit = utilIsNotNull.checkOCommitRule();
				// 一般案件：标的车不可以录入BZ险下的损失项，即BZ险0赔付
				boolean isBZCommit = utilIsNotNull.checkBZCommitRule();
				if (isOCommit || isBZCommit) {
					message = "YES";
				} else {
					// 非空校验
					message = UtilIsNotNull.isNotNull(request,true,defLossInfoQueryResponse.getCarLossInfos().get(0).getInsureCarFlag());
				}
				if (message.equals("YES")) {
					isSubmit = true;
				}
			} else {
				
				message = UtilIsNotNull.isNotNull(request,false,defLossInfoQueryResponse.getCarLossInfos().get(0).getInsureCarFlag());
				if (message.equals("YES")) {
					isSubmit = true;
				}
			}
			if (isSubmit) {
				String requestXML = createLossSubmitRequest(request);
				Log.i("mCliam", "定损提交同步------------->requestXML:" + requestXML);

				HttpUtils httpUtils = new HttpUtils();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("content", requestXML);
				String responseXML = httpUtils.doPost(url, params);
				Log.i("mCliam", "定损提交同步------------->responseXML:"
						+ responseXML);
				responseDuty = checkSubmitResponseXml(responseXML);
//				/**
//				 * 保存请求报文
//				 */
//				InfoBuffer infoBuffer = new InfoBuffer();
//				infoBuffer.setType("VERIFYLOSSSUBMIT");
//				infoBuffer.setXmlContent(requestXML);
////				TblInfoBuffer.addinfobuffer(infoBuffer);
//				/**
//				 * 保存响应报文
//				 */
//				infoBuffer.setType("VERIFYLOSSSUBMITBACK");
//				infoBuffer.setXmlContent(responseXML);
//				TblInfoBuffer.addinfobuffer(infoBuffer);
			} else {
				responseDuty.setResponseCode("NO");
				responseDuty.setResponseMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseDuty.setResponseMessage("终端与快赔交互失败:" + e.getMessage());

		}
		return responseDuty;
	}

	/**
	 * 对象转换
	 */
	private static VerifyLossSubmitRequest tranisition(
			DefLossInfoQueryResponse defloss) {
		// private String lossNo="";//损失项目编号
		VerifyLossSubmitRequest verifyLossSubmitRequest = DeflossSynchroAccess
				.find(defloss.getRegist().getRegistNo(), defloss.getRegist()
						.getLossNo());
		verifyLossSubmitRequest.setLossNo(defloss.getRegist().getLossNo());
		// private String submitType="";//提交类型
		verifyLossSubmitRequest.setSubmitType(defloss.getSubmitType());
		// private String estimateNo="";//定损单号
		verifyLossSubmitRequest.setEstimateNo(defloss.getRegist().getLossNo());
		// private String registNo="";//报案号码
		verifyLossSubmitRequest.setRegistNo(defloss.getRegist().getRegistNo());
		// private String plateNo="";//车牌号码
		verifyLossSubmitRequest.setPlateNo(defloss.getRegist().getLicenseNo());
		// private String vehkindCode="";//定损车辆种类代码
		// DataConfig.verifyLossSubmitRequest.setVehkindCode("");
		// // private String vehkindName="";//定损车辆种类名称
		// DataConfig.verifyLossSubmitRequest.setVehkindName("");
		// // private String vehcertainCode="";//定损车型编码
		// DataConfig.verifyLossSubmitRequest.setVehcertainCode("");
		// // private String vehcertaninName="";//定损车型名称
		// DataConfig.verifyLossSubmitRequest.setVehcertaninName("");
		// // private String vehgroupName;//定损车组名称
		// DataConfig.verifyLossSubmitRequest.setVehgroupName("");
		// // private String vehgroupcode;//定损车组代码
		// DataConfig.verifyLossSubmitRequest.setVehgroupcode("");
		// // private String vehsericode;//定损车系编码
		// DataConfig.verifyLossSubmitRequest.setVehsericode("");
		// // private String vehseriname;//定损车系名称
		// DataConfig.verifyLossSubmitRequest.setVehseriname("");
		// // private String vehyeartype;//年款
		// DataConfig.verifyLossSubmitRequest.setVehyeartype("");
		// // private String vehfactorycode;//厂家编码
		// DataConfig.verifyLossSubmitRequest.setVehfactorycode("");
		// // private String vehfactoryname;//厂家名称
		// DataConfig.verifyLossSubmitRequest.setVehfactoryname("");
		// // private String vehbrandcode;//品牌编码
		// DataConfig.verifyLossSubmitRequest.setVehbrandcode("");
		// // private String vehbrandname;//品牌名称
		// DataConfig.verifyLossSubmitRequest.setVehbrandname("");
		verifyLossSubmitRequest.setCertainCalltime(defloss.getTaskInfo()
				.getLinkCustomTime());// 联系客户时间
		verifyLossSubmitRequest.setCertainFirstPicTime(defloss.getTaskInfo()
				.getArrivesceneTime());// 第一张照片时间
		// private String certainComCode="";//定损处理人员机构代码
		verifyLossSubmitRequest.setCertainComCode(SystemConfig.loginResponse
				.getUserComCode());
		// private String certainHandleCode="";//定损处理人员代码
		verifyLossSubmitRequest
				.setCertainHandleCode(SystemConfig.USERLOGINNAME);
		// private String certainHandleName="";//定损处理人员名称
		verifyLossSubmitRequest.setCertainHandleName(SystemConfig.loginResponse
				.getUserName());
		// // private String repairfactoryCode="";//修理厂代码
		// verifyLossSubmitRequest.setRepairfactoryCode("");
		// // private String repairfactoryName="";//修理厂名称
		// verifyLossSubmitRequest.setRepairfactoryName("");
		// // private String repaircooperateFlag="";//修理厂合作性质
		// DataConfig.verifyLossSubmitRequest.setRepaircooperateFlag("");
		verifyLossSubmitRequest.setRepairMode(defloss.getDefLossContent()
				.getRepairMode());// 修理方式
		verifyLossSubmitRequest.setRepairReason(defloss.getDefLossContent()
				.getRepairReason());// 外修原因
		// // private String repairAptitude="";//修理厂资质
		// DataConfig.verifyLossSubmitRequest.setRepairAptitude("");
		// private String subrogateType="";//是否代位求偿
		verifyLossSubmitRequest.setSubrogateType(defloss.getSurveyKeyPoint()
				.getSubrogateType());
		// private String excessType="";//是否互碰自赔
		if (defloss.getSurveyKeyPoint().getClaimType().equals("0")||defloss.getSurveyKeyPoint().getClaimType().equals(""))
			verifyLossSubmitRequest.setExcessType("0");
		else
			verifyLossSubmitRequest.setExcessType("1");
		// private String accidentBook="";//有无交管事故书 0否 1是
		verifyLossSubmitRequest.setAccidentBook(defloss.getSurveyKeyPoint()
				.getAccidentBook());
		// private String accident="";//是否道路交通事故 0否 1是
		verifyLossSubmitRequest.setAccident(defloss.getSurveyKeyPoint()
				.getAccident());
		// private String insuredName="";//被保险人姓名
		verifyLossSubmitRequest.setInsuredName(defloss.getRegist()
				.getInsuredName());
		// private String insureMobile="";//被保险人电话
		verifyLossSubmitRequest.setInsureMobile(defloss.getRegist()
				.getInsuredMobile());
		// private String entrustName="";//受托人姓名
		verifyLossSubmitRequest.setEntrustName(defloss.getRegist()
				.getEntrustName());
		// private String entrustMobile="";//受托人电话
		verifyLossSubmitRequest.setEntrustMobile(defloss.getRegist()
				.getEntrustMobile());
		// private String slag;//自定义车型标致
		// DataConfig.verifyLossSubmitRequest.setSlag("");
		// // private String version;//版本号
		// DataConfig.verifyLossSubmitRequest.setVersion("");
		// 定损险别代码
		verifyLossSubmitRequest
				.setKindCode(defloss.getDefLossContent().getDefLossRiskCode());
		// private String vehserigradename;//三者车新车购置价
		verifyLossSubmitRequest.setVehserigradename(defloss
				.getDefLossCarInfos().size() > 0 ? defloss.getDefLossCarInfos()
				.get(0).getNewPruchaseAmount() : "");
		// private String carregisterdate;//车辆初次登记日期
		verifyLossSubmitRequest.setCarregisterdate(defloss.getDefLossCarInfos()
				.size() > 0 ? defloss.getDefLossCarInfos().get(0)
				.getCarRegisterDate() : "");
		// private String carvehicledesc;//行驶证车辆描述
		verifyLossSubmitRequest.setCarvehicledesc(defloss.getDefLossCarInfos()
				.size() > 0 ? defloss.getDefLossCarInfos().get(0)
				.getCarVehicleDesc() : "");
		// private String carfactorycode;//车辆制造厂编码
		verifyLossSubmitRequest.setCarfactorycode(defloss.getDefLossCarInfos()
				.size() > 0 ? defloss.getDefLossCarInfos().get(0)
				.getCarFactoryCode() : "");
		// private String carfactoryname;//车辆制造厂名称
		verifyLossSubmitRequest.setCarfactoryname(defloss.getDefLossCarInfos()
				.size() > 0 ? defloss.getDefLossCarInfos().get(0)
				.getCarFactoryName() : "");
		// private String claimtype;//赔案性质
		verifyLossSubmitRequest.setClaimtype(defloss.getSurveyKeyPoint()
				.getClaimType());
		// private String satisfaction;//满意度评价
		verifyLossSubmitRequest.setSatisfaction(defloss.getDefLossContent()
				.getSatisfieddegree());
		// // private String sumfitsfee;//合计换件金额
		// DataConfig.verifyLossSubmitRequest.setSumfitsfee(defloss.getDefLossContent().getSumfitsfee());
		// // private String sumrepairfee;//合计修理金额
		// DataConfig.verifyLossSubmitRequest.setSumrepairfee(defloss.getDefLossContent().getSumRepairfee());
		// // private String sumverirest;//合计残值金额
		// DataConfig.verifyLossSubmitRequest.setSumverirest(defloss.getDefLossContent().getSumRest());
		// // private String sumcertainloss;//定损合计金额
		// DataConfig.verifyLossSubmitRequest.setSumcertainloss(defloss.getDefLossContent().getSumCertainLoss());
		verifyLossSubmitRequest.setShijiufee(defloss.getDefLossContent().getShijiufee());// 施救费 add by yxf 20140212
		verifyLossSubmitRequest.setTextconent(defloss.getDefLossContent().getDefLossAdvise());// 定损意见
		// DeflossSynchroAccess.insertOrUpdate(DataConfig.verifyLossSubmitRequest);

		return verifyLossSubmitRequest;
	}

	/**
	 * 报文创建方法
	 * 
	 * 
	 * @throws Exception
	 */
	private static String createLossSubmitRequest(
			VerifyLossSubmitRequest request) throws Exception {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();

		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);

		// 创建根标记packet
		serializer.startTag("", "PACKET");
		serializer.attribute("", "type", "REQUEST");
		serializer.attribute("", "version", "1.0");
		/**
		 * HEAD
		 */
		serializer.startTag("", "HEAD");
		// 请求类型
		serializer.startTag("", "REQUESTTYPE");
		serializer.text("VerifyLossSubmit");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text("admin");
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text("0000");
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");

		// 定损编号
		serializer.startTag("", "LOSSNO");
		serializer.text(request.getLossNo() + "");
		serializer.endTag("", "LOSSNO");
		// 提交类型
		serializer.startTag("", "SUBMITTYPE");
		serializer.text(request.getSubmitType() + "");
		serializer.endTag("", "SUBMITTYPE");
		// 定损单号
		serializer.startTag("", "ESTIMATENO");
		serializer.text(request.getEstimateNo() + "");
		serializer.endTag("", "ESTIMATENO");
		// 报案号码
		serializer.startTag("", "REGISTNO");
		serializer.text(request.getRegistNo() + "");
		serializer.endTag("", "REGISTNO");
		// 报案号码
		serializer.startTag("", "KINDCODE");
		serializer.text(request.getKindCode() + "");
		serializer.endTag("", "KINDCODE");
		// 车牌号码
		serializer.startTag("", "PLATENO");
		serializer.text(request.getPlateNo() + "");
		serializer.endTag("", "PLATENO");
		// 定损车辆种类代码
		serializer.startTag("", "VEHKINDCODE");
		serializer.text(request.getVehkindCode() + "");
		serializer.endTag("", "VEHKINDCODE");
		// 定损车辆种类名称
		serializer.startTag("", "VEHKINDNAME");
		serializer.text(request.getVehkindName() + "");
		serializer.endTag("", "VEHKINDNAME");
		// 定损车型编码
		serializer.startTag("", "VEHCERTAINCODE");
		serializer.text(request.getVehcertainCode() + "");
		serializer.endTag("", "VEHCERTAINCODE");
		// 定损车型名称
		serializer.startTag("", "VEHCERTAINNAME");
		serializer.text(request.getVehcertaninName() + "");
		serializer.endTag("", "VEHCERTAINNAME");
		// 定损车组名称(新增)
		serializer.startTag("", "VEHGROUPNAME");
		serializer.text(request.getVehgroupName() + "");
		serializer.endTag("", "VEHGROUPNAME");
		// 定损车组代码(新增)
		serializer.startTag("", "VEHGROUPCODE");
		serializer.text(request.getVehgroupcode() + "");
		serializer.endTag("", "VEHGROUPCODE");
		// 定损车系编码(新增)
		serializer.startTag("", "VEHSERICODE");
		serializer.text(request.getVehsericode() + "");
		serializer.endTag("", "VEHSERICODE");
		// 定损车系名称(新增)
		serializer.startTag("", "VEHSERINAME");
		serializer.text(request.getVehseriname() + "");
		serializer.endTag("", "VEHSERINAME");
		// 年款(新增)
		serializer.startTag("", "VEHYEARTYPE");
		serializer.text(request.getVehyeartype() + "");
		serializer.endTag("", "VEHYEARTYPE");
		// 厂家编码(新增)
		serializer.startTag("", "VEHFACTORYCODE");
		serializer.text(request.getVehfactorycode() + "");
		serializer.endTag("", "VEHFACTORYCODE");
		// 厂家名称(新增)
		serializer.startTag("", "VEHFACTORYNAME");
		serializer.text(request.getVehfactoryname() + "");
		serializer.endTag("", "VEHFACTORYNAME");
		// 品牌编码(新增)
		serializer.startTag("", "VEHBRANDCODE");
		serializer.text(request.getVehbrandcode() + "");
		serializer.endTag("", "VEHBRANDCODE");
		// 品牌名称(新增)
		serializer.startTag("", "VEHBRANDNAME");
		serializer.text(request.getVehbrandname() + "");
		serializer.endTag("", "VEHBRANDNAME");
		// 定损成功联系客户
		serializer.startTag("", "CERTAINCALLTIME");
		serializer.text(request.getCertainCalltime() + "");
		serializer.endTag("", "CERTAINCALLTIME");
		// 定损第一张照片时间
		serializer.startTag("", "CERTAINFIRSTPICTIME");
		serializer.text(request.getCertainFirstPicTime() + "");
		serializer.endTag("", "CERTAINFIRSTPICTIME");
		// 定损处理人员机构代码
		serializer.startTag("", "CERTAINCOMCODE");
		serializer.text(request.getCertainComCode() + "");
		serializer.endTag("", "CERTAINCOMCODE");
		// 定损处理人员代码
		serializer.startTag("", "CERTAINHANDLECODE");
		serializer.text(request.getCertainHandleCode() + "");
		serializer.endTag("", "CERTAINHANDLECODE");
		// 定损处理人员名称
		serializer.startTag("", "CERTAINHANDLENAME");
		serializer.text(request.getCertainHandleName() + "");
		serializer.endTag("", "CERTAINHANDLENAME");
		// 修理厂代码
		serializer.startTag("", "REPAIRFACTORYCODE");
		serializer.text(request.getRepairfactoryCode());
		serializer.endTag("", "REPAIRFACTORYCODE");
		// 修理厂名称
		serializer.startTag("", "REPAIRFACTORYNAME");
		serializer.text(request.getRepairfactoryName() + "");
		serializer.endTag("", "REPAIRFACTORYNAME");
		// 修理厂合作性质
		serializer.startTag("", "REPAIRCOOPERATEFLAG");
		serializer.text(request.getRepaircooperateFlag() + "");
		serializer.endTag("", "REPAIRCOOPERATEFLAG");
		// 修理方式
		serializer.startTag("", "REPAIRMODE");
		serializer.text(request.getRepairMode() + "");
		serializer.endTag("", "REPAIRMODE");
		// 外修原因
		serializer.startTag("", "REPAIRREASON");
		serializer.text(request.getRepairReason() + "");
		serializer.endTag("", "REPAIRREASON");
		// 修理厂资质
		serializer.startTag("", "REPAIRAPTITUDE");
		serializer.text(request.getRepairAptitude() + "");
		serializer.endTag("", "REPAIRAPTITUDE");
		// 是否代位求偿
		serializer.startTag("", "SUBROGATETYPE");
		serializer.text(request.getSubrogateType() + "");
		serializer.endTag("", "SUBROGATETYPE");
		// 是否互碰自赔
		serializer.startTag("", "EXCESSTYPE");
		serializer.text(request.getExcessType() + "");
		serializer.endTag("", "EXCESSTYPE");
		// 有无交管事故书 0否
		serializer.startTag("", "ACCIDENTBOOK");
		serializer.text(request.getAccidentBook() + "");
		serializer.endTag("", "ACCIDENTBOOK");
		// 是否道路交通事故 0否 1是
		serializer.startTag("", "ACCIDENT");
		serializer.text(request.getAccident() + "");
		serializer.endTag("", "ACCIDENT");
		// 被保险人姓名
		serializer.startTag("", "INSUREDNAME");
		serializer.text(request.getInsuredName() + "");
		serializer.endTag("", "INSUREDNAME");
		// 被保险人电话
		serializer.startTag("", "INSUREDMOBILE");
		serializer.text(request.getInsureMobile() + "");
		serializer.endTag("", "INSUREDMOBILE");
		// 受托人姓名
		serializer.startTag("", "ENTRUSTNAME");
		serializer.text(request.getEntrustName() + "");
		serializer.endTag("", "ENTRUSTNAME");
		// 受托人电话
		serializer.startTag("", "ENTRUSTMOBILE");
		serializer.text(request.getEntrustMobile() + "");
		serializer.endTag("", "ENTRUSTMOBILE");
		// //厂家名称
		// serializer.startTag("", "VEHFACTORYNAME");
		// serializer.text(request.getVehfactoryName());
		// serializer.endTag("", "VEHFACTORYNAME");
		// //品牌编码
		// serializer.startTag("", "VEHBRANDCODE");
		// serializer.text(request.getVehbrandCode());
		// serializer.endTag("", "VEHBRANDCODE");
		// //品牌名称
		// serializer.startTag("", "VEHBRANDNAME");
		// serializer.text(request.getVehbrandName());
		// serializer.endTag("", "VEHBRANDNAME");
		// 自定义车型标志
		serializer.startTag("", "SELFCONFIGFLAG");
		serializer.text(request.getSlag() + "");
		serializer.endTag("", "SELFCONFIGFLAG");
		// 版本号（新增）
		serializer.startTag("", "VERSION");
		serializer.text(request.getVersion() + "");
		serializer.endTag("", "VERSION");
		// 三者车新车购置价
		serializer.startTag("", "VEHSERIGRADENAME");
		serializer.text(request.getVehserigradename() + "");
		serializer.endTag("", "VEHSERIGRADENAME");
		// 车辆初次登记日期
		serializer.startTag("", "CARREGISTERDATE");
		serializer.text(request.getCarregisterdate() + "");
		serializer.endTag("", "CARREGISTERDATE");
		// 行驶证车辆描述
		serializer.startTag("", "CARVEHICLEDESC");
		serializer.text(request.getCarvehicledesc() + "");
		serializer.endTag("", "CARVEHICLEDESC");
		// 车辆制造厂编码
		serializer.startTag("", "CARFACTORYCODE");
		serializer.text(request.getCarfactorycode() + "");
		serializer.endTag("", "CARFACTORYCODE");
		// 车辆制造厂名称
		serializer.startTag("", "CARFACTORYNAME");
		serializer.text(request.getCarfactoryname() + "");
		serializer.endTag("", "CARFACTORYNAME");
		// 赔案性质
		serializer.startTag("", "CLAIMTYPE");
		serializer.text(request.getClaimtype() + "");
		serializer.endTag("", "CLAIMTYPE");
		// 满意度评价
		serializer.startTag("", "SATISFACTION");
		serializer.text(request.getSatisfaction() + "");
		serializer.endTag("", "SATISFACTION");

		// 合计定损换件费用
		serializer.startTag("", "SUMFITSFEE");
		serializer.text(request.getSumfitsfee() + "");
		serializer.endTag("", "SUMFITSFEE");
		// 合计定损修理费用
		serializer.startTag("", "SUMREPAIRFEE");
		serializer.text(request.getSumrepairfee() + "");
		serializer.endTag("", "SUMREPAIRFEE");
		// 合计残值剩余
		serializer.startTag("", "SUMVERIREST");
		serializer.text(request.getSumverirest() + "");
		serializer.endTag("", "SUMVERIREST");
		// 定损合计费用
		serializer.startTag("", "SUMCERTAINLOSS");
		serializer.text(request.getSumcertainloss() + "");
		serializer.endTag("", "SUMCERTAINLOSS");
		// 定损备注
		serializer.startTag("", "SHIJIUFEE");
		serializer.text(request.getShijiufee() + "");
		serializer.endTag("", "SHIJIUFEE");
		// 定损备注
		serializer.startTag("", "TEXTCONENT");
		serializer.text(request.getTextconent() + "");
		serializer.endTag("", "TEXTCONENT");

		// 换件信息集合
		serializer.startTag("", "LOSSFITINFOLIST");
		for (int i = 0; i < request.getLossFitInfo().size(); i++) {
			// 换件信息
			serializer.startTag("", "LOSSFITINFO");
			// 零件唯一ID
			serializer.startTag("", "PARTID");
			serializer.text(request.getLossFitInfo().get(i).getPartid());
			serializer.endTag("", "PARTID");
			// 定损单唯一序号
			serializer.startTag("", "SERIALNO");
			serializer.text(request.getLossFitInfo().get(i).getSerialNo());
			serializer.endTag("", "SERIALNO");
			// 零配件原厂编码
			serializer.startTag("", "ORIGINALID");
			serializer.text(request.getLossFitInfo().get(i).getOriginalId());
			serializer.endTag("", "ORIGINALID");
			// 零配件原厂名称
			serializer.startTag("", "ORIGINALNAME");
			serializer.text(request.getLossFitInfo().get(i).getOriginalName());
			serializer.endTag("", "ORIGINALNAME");
			// 配件标准代码
			serializer.startTag("", "PARTSTANDARDCODE");
			serializer.text(request.getLossFitInfo().get(i)
					.getPartstandardCode());
			serializer.endTag("", "PARTSTANDARDCODE");
			// 配件标准名称
			serializer.startTag("", "PARTSTANDARD");
			serializer.text(request.getLossFitInfo().get(i).getPartstandard());
			serializer.endTag("", "PARTSTANDARD");
			// 配件部位代码
			serializer.startTag("", "PARTGROUPCODE");
			serializer.text(request.getLossFitInfo().get(i).getPartgroupCode());
			serializer.endTag("", "PARTGROUPCODE");
			// 配件部位名称
			serializer.startTag("", "PARTGROUPNAME");
			serializer.text(request.getLossFitInfo().get(i).getPartgroupName());
			serializer.endTag("", "PARTGROUPNAME");
			// 价格方案
			serializer.startTag("", "PRICEMODEL");
			serializer.text(request.getLossFitInfo().get(i).getPriceModel());
			serializer.endTag("", "PRICEMODEL");
			// 4s 系统价
			serializer.startTag("", "SYSTEMPRICE001");
			serializer
					.text(request.getLossFitInfo().get(i).getSystemPrice001());
			serializer.endTag("", "SYSTEMPRICE001");
			// 4s 本地价
			serializer.startTag("", "LOCALPRICE001");
			serializer.text(request.getLossFitInfo().get(i).getLocalPrice001());
			serializer.endTag("", "LOCALPRICE001");
			// 市场系统价
			serializer.startTag("", "SYSTEMPRICE002");
			serializer
					.text(request.getLossFitInfo().get(i).getSystemPrice002());
			serializer.endTag("", "SYSTEMPRICE002");
			// 市场本地价
			serializer.startTag("", "LOCALPRICE002");
			serializer.text(request.getLossFitInfo().get(i).getLocalPrice002());
			serializer.endTag("", "LOCALPRICE002");
			// 定损价格
			serializer.startTag("", "LOSSFEE");
			serializer.text(request.getLossFitInfo().get(i).getLossFee());
			serializer.endTag("", "LOSSFEE");
			// 定损残值
			serializer.startTag("", "RESTFEE");
			serializer.text(request.getLossFitInfo().get(i).getRestFee());
			serializer.endTag("", "RESTFEE");
			// 自定义配件标记
			serializer.startTag("", "SELFCONFIGFLAG");
			serializer
					.text(request.getLossFitInfo().get(i).getSelfconfigflag());
			serializer.endTag("", "SELFCONFIGFLAG");
			// 数量换件
			serializer.startTag("", "LOSSCOUNT");
			serializer.text(request.getLossFitInfo().get(i).getLossCount());
			serializer.endTag("", "LOSSCOUNT");
			// 备注
			serializer.startTag("", "REMARK");
			serializer.text(request.getLossFitInfo().get(i).getRemark());
			serializer.endTag("", "REMARK");
			// 定损选中系统价
			serializer.startTag("", "SYSTEMPRICE");
			serializer.text(request.getLossFitInfo().get(i).getSystemPrice());
			serializer.endTag("", "SYSTEMPRICE");
			// 定损选中本地价
			serializer.startTag("", "LOCALPRICE");
			serializer.text(request.getLossFitInfo().get(i).getLocalPrice());
			serializer.endTag("", "LOCALPRICE");

			serializer.endTag("", "LOSSFITINFO");
		}
		serializer.endTag("", "LOSSFITINFOLIST");
		// 换件信息集合
		serializer.startTag("", "LOSSREPAIRINFOLIST");
		for (int i = 0; i < request.getLossRepairInfo().size(); i++) {
			// 换件信息
			serializer.startTag("", "LOSSREPAIRINFO");
			// 定损系统修理唯一ID
			serializer.startTag("", "REPAIRID");
			serializer.text(request.getLossRepairInfo().get(i).getRepairId());
			serializer.endTag("", "REPAIRID");
			// 增量序列号码
			serializer.startTag("", "SERIALNO");
			serializer.text(request.getLossRepairInfo().get(i).getSerialNo());
			serializer.endTag("", "SERIALNO");
			// 工种代码
			serializer.startTag("", "REPAIRCODE");
			serializer.text(request.getLossRepairInfo().get(i).getRepairCode());
			serializer.endTag("", "REPAIRCODE");
			// 工种名称
			serializer.startTag("", "REPAIRNAME");
			serializer.text(request.getLossRepairInfo().get(i).getRepairName());
			serializer.endTag("", "REPAIRNAME");
			// 维修项代码
			serializer.startTag("", "REPAIRITEMCODE");
			serializer.text(request.getLossRepairInfo().get(i)
					.getRepairitemCode());
			serializer.endTag("", "REPAIRITEMCODE");
			// 维修项名称
			serializer.startTag("", "REPAIRITEMNAME");
			serializer.text(request.getLossRepairInfo().get(i)
					.getRepairitemName());
			serializer.endTag("", "REPAIRITEMNAME");
			// 修理费用
			serializer.startTag("", "REPAIRFEE");
			serializer.text(request.getLossRepairInfo().get(i).getRepairfee());
			serializer.endTag("", "REPAIRFEE");
			// 自定义修理件标记
			serializer.startTag("", "SELFCONFIGFLAG");
			serializer.text(request.getLossRepairInfo().get(i).getSelfConfigFlag());
			serializer.endTag("", "SELFCONFIGFLAG");

			// 价格方案（新增）
			serializer.startTag("", "PRICEMODEL");
			serializer.text(request.getLossRepairInfo().get(i).getPriceModel());
			serializer.endTag("", "PRICEMODEL");
			// 系统价/本地价
			serializer.startTag("", "SYSTEMPRICE");
			serializer.text(request.getLossRepairInfo().get(i).getSystemprice().equals("") ? "0.0" : request.getLossRepairInfo().get(i).getSystemprice());
			serializer.endTag("", "SYSTEMPRICE");

			serializer.endTag("", "LOSSREPAIRINFO");
		}
		serializer.endTag("", "LOSSREPAIRINFOLIST");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse checkSubmitResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		CommonResponse responseDuty = new CommonResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			responseDuty.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				responseDuty.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				responseDuty.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
			} else {
				/**
				 * 创建解析xml的解析器工厂XmlPullParserFactory
				 */
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				/**
				 * 生成xml解析器XmlPullParser
				 */
				XmlPullParser parser = factory.newPullParser();
				/**
				 * 设置要解析的xml串,若果解析出现乱码,可以尝试使用 parser.setInput(InputStream
				 * inputStream, String inputEncoding)
				 */
				parser.setInput(new StringReader(responseXml));
				/**
				 * 开始解析事件
				 */
				int eventType = parser.getEventType();
				String startTag = null;
				String endTag = null;
				String value = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {// 开头
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_DOCUMENT) {// 结尾
						break;
					} else if (eventType == XmlPullParser.START_TAG) {// 标签头
						startTag = parser.getName();

						if (!startTag.equals("PACKET")
								&& !startTag.equals("HEAD")
								&& !startTag.equals("BODY")) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								responseDuty.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								responseDuty.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								responseDuty.setResponseMessage(value);
							} else if (startTag.equalsIgnoreCase("HANDLETIME")) {
								responseDuty.setHandletime(value);
							}
							eventType = parser.next();
						} else {
							eventType = parser.next();
						}

					} else if (eventType == XmlPullParser.END_TAG) {
						endTag = parser.getName();
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				}

			}
		}

		return responseDuty;
	}
	
	/**
	 * 定损提交接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse lossApplyService(String registNo, String lossNo,String userCode, String url) {
		CommonResponse responseDuty =null;
		try {
			Log.i("chong", "lossApplyService");
			HttpUtils httpUtils = new HttpUtils();
			String requestXML = isReassignApply( registNo,  lossNo, userCode);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			Log.i("chong", "lossApplyService"+requestXML);
			String responseXML = httpUtils.doPost(url, params);
			responseDuty=isReassignApplyResponseXml(responseXML);
		}catch(Exception e){
			responseDuty.setResponseCode("NO");
			responseDuty.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
			e.printStackTrace();
		}
		return responseDuty;
		
	}
	
	/**
	 * 强制改派报文拼接
	 * @param registNo
	 * @param lossNo
	 * @param userCode
	 * @throws Exception
	 */
	private static String isReassignApply(String registNo, String lossNo,String userCode) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();

		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);

		// 创建根标记packet
		serializer.startTag("", "PACKET");
		serializer.attribute("", "type", "REQUEST");
		serializer.attribute("", "version", "1.0");
		/**
		 * HEAD
		 */
		serializer.startTag("", "HEAD");
		// 请求类型
		serializer.startTag("", "REQUESTTYPE");
		serializer.text("ISREASSIGNAPPLY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text("admin");
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text("0000");
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 系统用户名
		serializer.startTag("", "USERCODE");
		serializer.text(userCode);
		serializer.endTag("", "USERCODE");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(registNo);
		serializer.endTag("", "REGISTNO");
		// 申请类型
		serializer.startTag("", "LOSSNO");
		serializer.text(lossNo);
		serializer.endTag("", "LOSSNO");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}
	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse isReassignApplyResponseXml(String responseXml)throws Exception {
		/**
		 * 用于存放响应头和响应体
		 */
		CommonResponse responseDuty = new CommonResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			responseDuty.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				responseDuty.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				responseDuty.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
			} else {
				/**
				 * 创建解析xml的解析器工厂XmlPullParserFactory
				 */
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				/**
				 * 生成xml解析器XmlPullParser
				 */
				XmlPullParser parser = factory.newPullParser();
				/**
				 * 设置要解析的xml串,若果解析出现乱码,可以尝试使用 parser.setInput(InputStream
				 * inputStream, String inputEncoding)
				 */
				parser.setInput(new StringReader(responseXml));
				/**
				 * 开始解析事件
				 */
				int eventType = parser.getEventType();
				String startTag = null;
				String endTag = null;
				String value = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {// 开头
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_DOCUMENT) {// 结尾
						break;
					} else if (eventType == XmlPullParser.START_TAG) {// 标签头
						startTag = parser.getName();

						if (!startTag.equals("PACKET")
								&& !startTag.equals("HEAD")
								&& !startTag.equals("BODY")) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								responseDuty.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								responseDuty.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								responseDuty.setResponseMessage(value);
							} 
							eventType = parser.next();
						} else {
							eventType = parser.next();
						}

					} else if (eventType == XmlPullParser.END_TAG) {
						endTag = parser.getName();
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				}

			}
		}

		return responseDuty;
	}
}
