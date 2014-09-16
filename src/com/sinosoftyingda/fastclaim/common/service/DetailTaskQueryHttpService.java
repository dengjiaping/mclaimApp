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
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.model.CheckDriver;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

public class DetailTaskQueryHttpService {
	/**
	 * 查勘明细接口方法
	 * 
	 * @return
	 */
	public static DetailTaskQueryResponse detailTaskQuerySercive(
			DetailTaskQueryRequest detailTaskQueryRequest, String url,
			boolean isUpdate) {
		DetailTaskQueryResponse.detailTaskQueryResponse = null;
		DetailTaskQueryResponse response = DetailTaskQueryResponse
				.getDetailTaskQueryResponse();
		try {
			String requestXML = createDetailTaskQueryRequestRequest(detailTaskQueryRequest);
			Log.i("mCliam", "查勘明细------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "查勘明细------------->responseXML:" + responseXML);
			response = detailTaskQueryResponseXml(responseXML);
			/**
			 * 保存明细信息
			 */
			TblTaskQuery.insertOrUpdate(
					SystemConfig.dbhelp.getWritableDatabase(), response, true,
					true, true);

			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("DETAILTASKQUERY");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("DETAILTASKQUERYBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
		}
		return response;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createDetailTaskQueryRequestRequest(
			DetailTaskQueryRequest detailTaskQueryRequest) throws Exception {
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
		serializer.text("DETAILTASKQUERY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(detailTaskQueryRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(detailTaskQueryRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");

		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(detailTaskQueryRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 任务状态
		serializer.startTag("", "CHECKSTATUS");
		serializer.text(detailTaskQueryRequest.getCheckStatus());
		serializer.endTag("", "CHECKSTATUS");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	public static DetailTaskQueryResponse detailTaskQueryResponseXml(
			String responseXml) {

		/**
		 * 用于存放响应头和响应体
		 */
		DetailTaskQueryResponse detailTaskQueryResponse = DetailTaskQueryResponse
				.getDetailTaskQueryResponse();
		CarLoss carLoss = null;
		CheckExt checkExt = null;
		CheckDriver checkDriver = null;

		/**
		 * 响应头
		 */
		try {

			if (responseXml == null) {
				detailTaskQueryResponse
						.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
			} else {
				if (responseXml.equals("Timeout")) {
					detailTaskQueryResponse
							.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

				} else if (responseXml.equals("Post") || responseXml.equals("")) {
					detailTaskQueryResponse
							.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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

							if (startTag.equalsIgnoreCase("REGISTINFO"))
								eventType = parser.next();
							if (carLoss == null && checkExt == null
									&& checkDriver == null) {
								if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setReaponseType(value);
								} else if (startTag
										.equalsIgnoreCase("RESPONSECODE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setResponseCode(value);
								} else if (startTag
										.equalsIgnoreCase("RESPONSEMESSAGE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setResponseMessage(value);
								} else if (startTag
										.equalsIgnoreCase("REGISTNO")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setRegistNo(value);
								} else if (startTag
										.equalsIgnoreCase("INSUREDNAME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setInsrtedName(value);
								} else if (startTag
										.equalsIgnoreCase("LICENSENO")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setLicenseNo(value);
								} else if (startTag
										.equalsIgnoreCase("BRANDNAME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setBrandName(value);
								} else if (startTag
										.equalsIgnoreCase("DISPATCHPTIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDispatchptime(value);
								} else if (startTag
										.equalsIgnoreCase("REPORTTIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setReporttime(value);
								} else if (startTag
										.equalsIgnoreCase("DISPATCHPLACE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDispatchplace(value);
								} else if (startTag
										.equalsIgnoreCase("DRIVERNAME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDrivername(value);
								} else if (startTag
										.equalsIgnoreCase("PROMPTMESSAGE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setPromptmessage(value);
								} else if (startTag
										.equalsIgnoreCase("DAMAGEDAYP")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDamageDayp(value);
								} else if (startTag
										.equalsIgnoreCase("TASKRECEIPTTIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setTaskreceiptTime(value);
								} else if (startTag
										.equalsIgnoreCase("ARRIVESCENETIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setArricesceneTime(value);
								} else if (startTag
										.equalsIgnoreCase("LINKCUSTOMTIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setResponseMessage(value);
								} else if (startTag
										.equalsIgnoreCase("TASKHANDTIME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setTaskHandTime(value);
								} else if (startTag
										.equalsIgnoreCase("FIRSTSITEFLAG")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setFirstsiteFlag(value);
								} else if (startTag
										.equalsIgnoreCase("DAMAGENAME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDamageName(value);
								} else if (startTag
										.equalsIgnoreCase("DAMAGECODE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setDamageCode(value);
								} else if (startTag
										.equalsIgnoreCase("INDEMNITYDUTY")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setIndemnityDuty(value);
								} else if (startTag
										.equalsIgnoreCase("CLAIMTYPE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setClaimType(value);
								} else if (startTag
										.equalsIgnoreCase("ISCOMMONCLAIM")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setIsCommonClaim(value);
								} else if (startTag
										.equalsIgnoreCase("SUBROGATETYPE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setSubrogateType(value);
								} else if (startTag
										.equalsIgnoreCase("ACCIDENTBOOK")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setAccidentBook(value);
								} else if (startTag
										.equalsIgnoreCase("ACCIDENT")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setAccident(value);
								} else if (startTag
										.equalsIgnoreCase("INSUREDMOBILE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setInsuredMobile(value);
								} else if (startTag
										.equalsIgnoreCase("ENTRUSTNAME")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setEntrustName(value);
								} else if (startTag
										.equalsIgnoreCase("ENTRUSTMOBILE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setEntrustMobile(value);
								} else if (startTag
										.equalsIgnoreCase("CHECKREPORT")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setCheckReport(value);
								} else if (startTag
										.equalsIgnoreCase("SATISFACTION")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse
											.setSatisfacTion(value);
								} else if (startTag.equalsIgnoreCase("REMARK")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setRemark(value);
								} else if (startTag.equalsIgnoreCase("COMPENSATERATE")) {
									value = parser.nextText().trim();
									detailTaskQueryResponse.setCompensateRate(value);
								}

							}
							/**
							 * 涉損车辆
							 */
							if (carLoss != null) {

								if (startTag.equalsIgnoreCase("LICENSENO")) {
									value = parser.nextText().trim();
									carLoss.setLicenseNo(value);

								} else if (startTag.equalsIgnoreCase("INSURECARFLAG")) {
									value = parser.nextText().trim();
									carLoss.setInsureCarFlag(value);
								}
								if (startTag.equalsIgnoreCase("ITEMNO")) {
									value = parser.nextText().trim();
									carLoss.setCarNum(value);
								} else if (startTag.equalsIgnoreCase("FRAMENO")) {
									value = parser.nextText().trim();
									carLoss.setFrameNo(value);
								} else if (startTag.equalsIgnoreCase("BRANDNAME")) {
									value = parser.nextText().trim();
									carLoss.setBrandName(value);
								} else if (startTag.equalsIgnoreCase("DUTYPERCENT")) {
									value = parser.nextText().trim();
									carLoss.setDutyPercent(value);
								} else if (startTag.equalsIgnoreCase("POLICYNO")) {
									value = parser.nextText().trim();
									carLoss.setNullPolicyNo(value);
								} else if (startTag.equalsIgnoreCase("ENGINENO")) {
									value = parser.nextText().trim();
									carLoss.setEngineNo(value);
								} else if (startTag.equalsIgnoreCase("INSURECOMCODE")) {
									value = parser.nextText().trim();
									carLoss.setInsurecomCode(value);
								} else if (startTag.equalsIgnoreCase("INSURECOMNAME")) {
									value = parser.nextText().trim();
									carLoss.setInsurecomName(value);
								} else if (startTag.equalsIgnoreCase("CARKINDCODE")) {
									value = parser.nextText().trim();
									carLoss.setCarKindCode(value);
								} else if (startTag.equalsIgnoreCase("LICENSETYPE")) {
									value = parser.nextText().trim();
									carLoss.setLicenseType(value);
								}else if (startTag.equalsIgnoreCase("NULLDRIVERNAME")) {
									value = parser.nextText().trim();
									carLoss.setNullDriverName(value);
								}else if (startTag.equalsIgnoreCase("NULLDRIVERCODE")) {
									value = parser.nextText().trim();
									carLoss.setNullDriverCode(value);
								}else if (startTag.equalsIgnoreCase("NULLCERTITYPE")) {
									value = parser.nextText().trim();
									carLoss.setNullCertitypeCode(value);
								}

							}
							if (startTag.equalsIgnoreCase("CARLOSS")) {
								carLoss = new CarLoss();
							}
							/**
							 * 查勘要点
							 */
							if (checkExt != null) {

								if (startTag.equalsIgnoreCase("COLUMNNAME")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelCode(value);
								} else if (startTag
										.equalsIgnoreCase("CHECKKERNELSELECT")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelSelect(value);
								} else if (startTag
										.equalsIgnoreCase("CHECKKERNELTYPE")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelType(value);
								} else if (startTag
										.equalsIgnoreCase("CHECKKERNELCODE")) {
									value = parser.nextText().trim();
									checkExt.setCheckKernelName(value);
								} else if (startTag
										.equalsIgnoreCase("COLUMNINPUTTEXT")) {
									value = parser.nextText().trim();
									checkExt.setCheckExtRemark(value);

								}
							}
							if (startTag.equalsIgnoreCase("CHECKEXT")) {
								checkExt = new CheckExt();
							}
							/**
							 * 驾驶员信息
							 */
							if (checkDriver != null) {
								if (startTag.equalsIgnoreCase("SERIALNO")) {
									value = parser.nextText().trim();
									checkDriver.setSerialNo(value);
								} else if (startTag
										.equalsIgnoreCase("DRIVINGLICENSENO")) {
									value = parser.nextText().trim();
									checkDriver.setDrivinglicenseNo(value);
								} else if (startTag
										.equalsIgnoreCase("DRIVERNAME")) {
									value = parser.nextText().trim();
									checkDriver.setDriverName(value);
								} else if (startTag
										.equalsIgnoreCase("IDENTIFYNUMBER")) {
									value = parser.nextText().trim();
									checkDriver.setIdentifyNumber(value);
								} else if (startTag
										.equalsIgnoreCase("RECEIVELICENSEDATE")) {
									value = parser.nextText().trim();
									checkDriver.setReceivelicenseDate(value);
								} else if(startTag.equalsIgnoreCase("DRIVERCERTITYPE")){
									value = parser.nextText().trim();
									checkDriver.setDrivercertitypeCode(value);
								}
							}
							if (startTag.equalsIgnoreCase("CHECKDRIVER")) {
								checkDriver = new CheckDriver();
							}
							eventType = parser.next();

						} else if (eventType == XmlPullParser.END_TAG) {
							endTag = parser.getName();
							if (endTag.equalsIgnoreCase("CARLOSS")) {

								if (carLoss != null)
									detailTaskQueryResponse.getCarLossList()
											.add(carLoss);
								carLoss = null;
							} else if (endTag.equalsIgnoreCase("CHECKEXT")) {
								detailTaskQueryResponse.getCheckExtList().add(
										checkExt);
								checkExt = null;
							} else if (endTag.equalsIgnoreCase("CHECKDRIVER")) {
								detailTaskQueryResponse.getCheckDriver().add(
										checkDriver);
								checkDriver = null;
							}
							if (endTag.equalsIgnoreCase("BODY")) {
								if (detailTaskQueryResponse.getAccidentBook()
										.equals("")) {
									detailTaskQueryResponse.setAccidentBook("0");
								}
								if(detailTaskQueryResponse.getAccident().equals(""))
								{
									detailTaskQueryResponse.setAccident("0");
								}
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
		return detailTaskQueryResponse;
	}
}
