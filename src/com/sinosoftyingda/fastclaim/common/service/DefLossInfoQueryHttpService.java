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
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;
import com.sinosoftyingda.fastclaim.common.model.CarLossInfo;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.DefLossCarInfo;
import com.sinosoftyingda.fastclaim.common.model.DefLossContent;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.ItemKind;
import com.sinosoftyingda.fastclaim.common.model.Regist;
import com.sinosoftyingda.fastclaim.common.model.SurveyKeyPoint;
import com.sinosoftyingda.fastclaim.common.model.TaskInfo;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 定损明细接口类
 * 
 * @author haoyun 20130308
 * 
 */
public class DefLossInfoQueryHttpService {
	/**
	 * 定损明细接口方法
	 * 
	 * @return
	 */
	public static DefLossInfoQueryResponse defLossInfoQuerySercive(DefLossInfoQueryRequest defLossInfoQueryRequest, String url, boolean isUpdate) {
		DefLossInfoQueryResponse response = new DefLossInfoQueryResponse();
		try {
			String requestXML = createDefLossInfoQueryRequestRequest(defLossInfoQueryRequest);
			Log.i("mCliam", "定损明细------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "定损明细------------->responseXML:" + responseXML);
			response = defLossInfoQueryResponseXml(responseXML);
			/**
			 * 保存定损明细信息
			 */
			// TblLossTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(),response,isUpdate);
			CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), response, false);
			VerifyLossSubmitRequest verifyLossSubmitRequest = DeflossSynchroAccess.find(response.getRegist().getRegistNo(), response.getRegist().getLossNo());
			verifyLossSubmitRequest.setRegistNo(response.getRegist().getRegistNo());
			verifyLossSubmitRequest.setLossNo(response.getRegist().getLossNo());
			verifyLossSubmitRequest.setSumfitsfee(response.getDefLossContent().getSumfitsfee());// 合计换件金额
			verifyLossSubmitRequest.setSumrepairfee(response.getDefLossContent().getSumRepairfee());// 合计修理金额
			verifyLossSubmitRequest.setSumverirest(response.getDefLossContent().getSumRest());// 合计残值金额
			verifyLossSubmitRequest.setSumcertainloss(response.getDefLossContent().getSumCertainLoss());// 定损合计金额
			DeflossSynchroAccess.insertOrUpdate(verifyLossSubmitRequest);

			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("DEFLOSSINFOQUERY");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("DEFLOSSINFOQUERYBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
			response.setResponseCode("NO");
		}
		return response;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createDefLossInfoQueryRequestRequest(DefLossInfoQueryRequest defLossInfoQueryRequest) throws Exception {
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
		serializer.text("DefLossInfoQuery");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(defLossInfoQueryRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(defLossInfoQueryRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");

		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(defLossInfoQueryRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 损失项目编号
		serializer.startTag("", "LOSSNO");
		serializer.text(defLossInfoQueryRequest.getLossNo());
		serializer.endTag("", "LOSSNO");
		// 定损状态
		serializer.startTag("", "DEFLOSSTASKSTATUS");
		serializer.text(defLossInfoQueryRequest.getDeflossTaskStatus());
		serializer.endTag("", "DEFLOSSTASKSTATUS");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	public static DefLossInfoQueryResponse defLossInfoQueryResponseXml(String responseXml) {

		/**
		 * 用于存放响应头和响应体
		 */
		DefLossInfoQueryResponse response = new DefLossInfoQueryResponse();
		Regist regist = null;// 报案信息
		SurveyKeyPoint surveyKeyPoint = null;// 查勘要点
		ItemKind itemKind = null;
		TaskInfo taskInfo = null;// 任务信息
		DefLossContent defLossContent = null;// 定损内容
		DefLossCarInfo defLossCarInfo = null;// 定损对象多条定损车辆信息
		CarLossInfo carLossInfo = null;// 案件涉损(LOSSOFCASELIST)多条车损信息(CARLOSSINFO)(一个对象中某属性区分是标的或三者)
		CheckExt checkExt = null;// 查勘扩展信息
		/**
		 * 响应头
		 */
		try {

			if (responseXml == null) {
				response.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
				response.setResponseCode("NO");
			} else {
				if (responseXml.equals("Timeout")) {
					response.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");
					response.setResponseCode("NO");
				} else if (responseXml.equals("Post") || responseXml.equals("")) {
					response.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
					response.setResponseCode("NO");
				} else {
					/**
					 * 创建解析xml的解析器工厂XmlPullParserFactory
					 */
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
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

							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								value = parser.nextText().trim();
								response.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								value = parser.nextText().trim();
								response.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								value = parser.nextText().trim();
								response.setResponseMessage(value);
							}
							/**
							 * 报案信息
							 */
							if (regist != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("REGISTNO")) {
									regist.setRegistNo(value);
								} else if (startTag.equalsIgnoreCase("LOSSNO")) {
									regist.setLossNo(value);
								} else if (startTag.equalsIgnoreCase("POLICYCOMCODE")) {
									regist.setPolicyComCode(value);
								} else if (startTag.equalsIgnoreCase("LICENSENO")) {
									regist.setLicenseNo(value);
								} else if (startTag.equalsIgnoreCase("BRANDNAME")) {
									regist.setBrandName(value);
								} else if (startTag.equalsIgnoreCase("DISPATCHPTIME")) {
									regist.setDispatchpTime(value);
								} else if (startTag.equalsIgnoreCase("REPORTTIME")) {
									regist.setReportTime(value);
								} else if (startTag.equalsIgnoreCase("OUTOFPLACE")) {
									regist.setOutofPlace(value);
								} else if (startTag.equalsIgnoreCase("DRIVERNAME")) {
									regist.setDriverName(value);
								} else if (startTag.equalsIgnoreCase("PROMPTMESSAGE")) {
									regist.setPromptMessage(value);
								} else if (startTag.equalsIgnoreCase("INSUREDNAME")) {
									regist.setInsuredName(value);
								} else if (startTag.equalsIgnoreCase("INSUREDMOBILE")) {
									regist.setInsuredMobile(value);
								} else if (startTag.equalsIgnoreCase("ENTRUSTNAME")) {
									regist.setEntrustName(value);
								} else if (startTag.equalsIgnoreCase("ENTRUSTMOBILE")) {
									regist.setEntrustMobile(value);
								} else if (startTag.equalsIgnoreCase("DAMAGEDAYP")) {
									regist.setDamageDayp(value);
								}
							}
							if (startTag.equalsIgnoreCase("REGIST")) {
								regist = new Regist();
							}
							/**
							 * 查勘要点
							 */
							if (surveyKeyPoint != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("FIRSTSITEFLAG")) {
									surveyKeyPoint.setFirstsiteFlag(value);
								} else if (startTag.equalsIgnoreCase("DAMAGECODE")) {
									surveyKeyPoint.setDamageCode(value);
								} else if (startTag.equalsIgnoreCase("DAMAGENAME")) {
									surveyKeyPoint.setDamageName(value);
								} else if (startTag.equalsIgnoreCase("INDEMNITYDUTY")) {
									surveyKeyPoint.setIndemnityDuty(value);
								} else if (startTag.equalsIgnoreCase("CLAIMTYPE")) {
									surveyKeyPoint.setClaimType(value);
								} else if (startTag.equalsIgnoreCase("ISCOMMONCLAIM")) {
									surveyKeyPoint.setIsCommonClaim(value);
								} else if (startTag.equalsIgnoreCase("SUBROGATETYPE")) {
									surveyKeyPoint.setSubrogateType(value);
								} else if (startTag.equalsIgnoreCase("ACCIDENTBOOK")) {
									surveyKeyPoint.setAccidentBook(value);
								} else if (startTag.equalsIgnoreCase("ACCIDENT")) {
									surveyKeyPoint.setAccident(value);
								} else if (startTag.equalsIgnoreCase("REMARK")) {
									surveyKeyPoint.setRemark(value);
								} else if (startTag.equalsIgnoreCase("CHECKREPORT")) {
									surveyKeyPoint.setCheckReport(value);
								}
							}
							if (startTag.equalsIgnoreCase("SURVEYKEYPOINT")) {
								surveyKeyPoint = new SurveyKeyPoint();
							}
							/**
							 * 案件涉损车辆
							 */
							if (carLossInfo != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("INSURECARFLAG")) {
									carLossInfo.setInsureCarFlag(value);
								} else if (startTag.equalsIgnoreCase("LICENSENO")) {
									carLossInfo.setLicenseNo(value);
								} else if (startTag.equalsIgnoreCase("DEFINELOSSPERSON")) {
									carLossInfo.setDefineLossPerson(value);
								} else if (startTag.equalsIgnoreCase("DEFINELOSSAMOUT")) {
									carLossInfo.setDefineLossAmount(value);
								}
							}
							if (startTag.equalsIgnoreCase("CARLOSSINFO")) {
								carLossInfo = new CarLossInfo();
							}
							/**
							 * 任务信息
							 */
							if (taskInfo != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("TASKRECEIPTTIME")) {
									taskInfo.setTaskReceiptTime(value);
								} else if (startTag.equalsIgnoreCase("ARRIVESCENETIME")) {
									taskInfo.setArrivesceneTime(value);
								} else if (startTag.equalsIgnoreCase("LINKCUSTOMTIME")) {
									taskInfo.setLinkCustomTime(value);
								} else if (startTag.equalsIgnoreCase("TASKHANDTIME")) {
									taskInfo.setTaskHandTime(value);
								}
							}
							if (startTag.equalsIgnoreCase("TASKINFO")) {
								taskInfo = new TaskInfo();
							}
							/**
							 * 定损对象
							 */
							if (defLossCarInfo != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("INSURECARFLAG")) {
									defLossCarInfo.setInsurecarFlag(value);
								} else if (startTag.equalsIgnoreCase("LICENSENO")) {
									defLossCarInfo.setLicenseNo(value);
								} else if (startTag.equalsIgnoreCase("CARFRAMENO")) {
									defLossCarInfo.setCarframeNo(value);
								} else if (startTag.equalsIgnoreCase("BRANDNAME")) {
									defLossCarInfo.setBrandName(value);
								} else if (startTag.equalsIgnoreCase("NEWPRUCHASEAMOUNT")) {
									defLossCarInfo.setNewPruchaseAmount(value);
								} else if (startTag.equalsIgnoreCase("CARVEHICLEDESC")) {
									defLossCarInfo.setCarVehicleDesc(value);
								} else if (startTag.equalsIgnoreCase("CARFACTORYCODE")) {
									defLossCarInfo.setCarFactoryCode(value);
								} else if (startTag.equalsIgnoreCase("CARFACTORYNAME")) {
									defLossCarInfo.setCarFactoryName(value);
								} else if (startTag.equalsIgnoreCase("CARREGISTERDATE")) {
									defLossCarInfo.setCarRegisterDate(value);
								} else if (startTag.equalsIgnoreCase("INSUREVEHICODE")) {
									defLossCarInfo.setInsurevehiCode(value);
								}
							}
							if (startTag.equalsIgnoreCase("DEFLOSSCARINFO")) {
								defLossCarInfo = new DefLossCarInfo();
							}

							/**
							 * 定损内容
							 */
							if (defLossContent != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("REPAIRFACTORYCODE")) {
									defLossContent.setRepairFactoryCode(value);
								} else if (startTag.equalsIgnoreCase("REPAIRFACTORYNAME")) {
									defLossContent.setRepairFactoryName(value);
								} else if (startTag.equalsIgnoreCase("REPAIRCOOPERATEFLAG")) {
									defLossContent.setRepairCooperateFlag(value);
								} else if (startTag.equalsIgnoreCase("REPAIRMODE")) {
									defLossContent.setRepairMode(value);
								} else if (startTag.equalsIgnoreCase("REPAIRAPTITUDE")) {
									defLossContent.setRepairapTitude(value);
								} else if (startTag.equalsIgnoreCase("DEFLOSSRISKCODE")) {
									defLossContent.setDefLossRiskCode(value);
								} else if (startTag.equalsIgnoreCase("SUMFITSFEE")) {
									defLossContent.setSumfitsfee(value);
								} else if (startTag.equalsIgnoreCase("SUMREPAIRFEE")) {
									defLossContent.setSumRepairfee(value);
								} else if (startTag.equalsIgnoreCase("SUMREST")) {
									defLossContent.setSumRest(value);
								} else if (startTag.equalsIgnoreCase("SUMCERTAINLOSS")) {
									defLossContent.setSumCertainLoss(value);
								} else if (startTag.equalsIgnoreCase("SUMCERTAINLOSSCH")) {
									defLossContent.setSumCerTainLossCh(value);
								} else if (startTag.equalsIgnoreCase("DEFLOSSADVISE")) {
									defLossContent.setDefLossAdvise(value);
								} else if (startTag.equalsIgnoreCase("SATISFIEDDEGREE")) {
									defLossContent.setSatisfieddegree(value);
								} else if (startTag.equalsIgnoreCase("REPAIRREASON")) {
									defLossContent.setRepairReason(value);
								} else if (startTag.equalsIgnoreCase("UNDWRTREMARK")) {
									defLossContent.setUndwrtRemark(value);
								}  else if (startTag.equalsIgnoreCase("SHIJIUFEE")) {
									defLossContent.setShijiufee(value);
								}
							}
							if (startTag.equalsIgnoreCase("DEFLOSSCONTENT")) {
								defLossContent = new DefLossContent();
							}
							/**
							 * 查勘扩展信息
							 */
							if (checkExt != null) {

								if (startTag.equalsIgnoreCase("COLUMNNAME")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelCode(value);
								} else if (startTag.equalsIgnoreCase("CHECKKERNELSELECT")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelSelect(value);
								} else if (startTag.equalsIgnoreCase("CHECKKERNELTYPE")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelType(value);
								} else if (startTag.equalsIgnoreCase("COLUMNINPUTTEXT")) {
									value = parser.nextText().trim();
									checkExt.setCheckExtRemark(value);
								}
							}
							if (startTag.equalsIgnoreCase("CHECKEXT")) {
								checkExt = new CheckExt();
							}
							/**
							 * 险别
							 */
							if (itemKind != null) {// add by jingtuo
								if (startTag.equalsIgnoreCase("KINDCODE")) {
									value = parser.nextText().trim();
									itemKind.setKindCode(value);
								} else if (startTag.equalsIgnoreCase("KINDNAME")) {
									value = parser.nextText().trim();
									itemKind.setKindName(value);
								}
							}
							if (startTag.equalsIgnoreCase("ITEMKIND")) {
								itemKind = new ItemKind();
							}
							eventType = parser.next();

						} else if (eventType == XmlPullParser.END_TAG) {
							endTag = parser.getName();
							if (endTag.equalsIgnoreCase("REGIST")) {
								response.setRegist(regist);
								regist = null;
							} else if (endTag.equalsIgnoreCase("SURVEYKEYPOINT")) {
								response.setSurveyKeyPoint(surveyKeyPoint);
								surveyKeyPoint = null;
							} else if (endTag.equalsIgnoreCase("TASKINFO")) {
								response.setTaskInfo(taskInfo);
								taskInfo = null;
							} else if (endTag.equalsIgnoreCase("CARLOSSINFO")) {
								response.getCarLossInfos().add(carLossInfo);
								carLossInfo = null;
							} else if (endTag.equalsIgnoreCase("DEFLOSSCARINFO")) {
								response.getDefLossCarInfos().add(defLossCarInfo);
								defLossCarInfo = null;
							} else if (endTag.equalsIgnoreCase("DEFLOSSCONTENT")) {
								response.setDefLossContent(defLossContent);
								defLossContent = null;
							} else if (endTag.equalsIgnoreCase("CHECKEXT")) {
								if (checkExt.getCheckKernelType().equals("") && checkExt.getCheckKernelCode().equals("") && checkExt.getCheckKernelName().equals("")
										&& checkExt.getCheckKernelSelect().equals("") && checkExt.getCheckKernelType().equals("") && checkExt.getSerialNo().equals("")) {
								} else {
									response.getCheckExt().add(checkExt);
								}
								checkExt = null;
							} else if (endTag.equalsIgnoreCase("ITEMKIND")) {
								response.getItemKinds().add(itemKind);
								itemKind = null;
							}
							eventType = parser.next();
						} else {
							eventType = parser.next();
						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
