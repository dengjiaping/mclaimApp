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
import com.sinosoftyingda.fastclaim.common.db.dao.TblInfoBuffer;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPolicyMsg;
import com.sinosoftyingda.fastclaim.common.model.AppointModel;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgInsuranceType;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgRequest;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 保单详细信息接口类
 * 
 * @author 郝运 20130225
 * 
 */
public class PolicyHttpService {
	/**
	 * 保单详细信息接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse policyMsgService(PolicyMsgRequest policyMsgRequest, String url) {
		PolicyMsgResponse policyMsgResponse = new PolicyMsgResponse();
		try {
			if (!TblInfoBuffer.isExist("POLICYNOBACK",policyMsgRequest.getRegistNo())) {

				String requestXML = createPolicyMsgRequest(policyMsgRequest);
				Log.i("mCliam", "保单详细信息------------->requestXML:" + requestXML);

				HttpUtils httpUtils = new HttpUtils();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("content", requestXML);
				String responseXML = httpUtils.doPost(url, params);
				Log.i("mCliam", "保单详细信息------------->responseXML:" + responseXML);
				policyMsgResponse = policyMsgResponseXml(responseXML);
				/**
				 * 保单信息保存本地
				 */
				TblPolicyMsg.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), policyMsgResponse, policyMsgRequest.getRegistNo());
				/**
				 * 保存请求报文
				 */
//				InfoBuffer infoBuffer = new InfoBuffer();
//				infoBuffer.setRegistNo(policyMsgRequest.getRegistNo());
//				infoBuffer.setType("POLICYNO");
//				infoBuffer.setXmlContent(requestXML);
//				TblInfoBuffer.addinfobuffer(infoBuffer);
				/**
				 * 保存响应报文
				 */
//				infoBuffer.setType("POLICYNOBACK");
//				infoBuffer.setXmlContent(responseXML);
//				TblInfoBuffer.addinfobuffer(infoBuffer);
			}else
			{
				
				policyMsgResponse = policyMsgResponseXml(TblInfoBuffer.getXmlByTypeAndRegistNo("POLICYNOBACK",policyMsgRequest.getRegistNo()));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			policyMsgResponse.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
		}
		return policyMsgResponse;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createPolicyMsgRequest(PolicyMsgRequest policyMsgRequest) throws Exception {
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
		serializer.text("PolicyInfo");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(policyMsgRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(policyMsgRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(policyMsgRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static PolicyMsgResponse policyMsgResponseXml(String responseXml) throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		PolicyMsgResponse policyMsgResponse = new PolicyMsgResponse();
		PolicyMsgInsuranceType policyMsgInsuranceType = new PolicyMsgInsuranceType();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			policyMsgResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				policyMsgResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				policyMsgResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
				AppointModel appointModel = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {// 开头
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_DOCUMENT) {// 结尾
						break;
					} else if (eventType == XmlPullParser.START_TAG) {// 标签头
						startTag = parser.getName();

						if (!startTag.equals("PACKET") && !startTag.equals("HEAD") && !startTag.equals("BODY")
								&& !startTag.equals("INSURANCETYPELIST") && !startTag.equals("INSURANCETYPE") && !startTag.equals("ITEMKIND")
								&& !startTag.equals("ITEMKINDLIST") && !startTag.equals("CENGAGELIST")) {
							System.out.println("startTag:++++++++++" + startTag);
							if (!startTag.equalsIgnoreCase("CENGAGE"))
								value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								policyMsgResponse.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								policyMsgResponse.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								policyMsgResponse.setResponseMessage(value);
							} else if (startTag.equalsIgnoreCase("INSURENAME")) {
								policyMsgResponse.setInsuredName(value);
							} else if (startTag.equalsIgnoreCase("DRIVINGLICENSEOWNER")) {
								policyMsgResponse.setDrivingLicenesOwner(value);
							} else if (startTag.equalsIgnoreCase("LICENSENO")) {
								policyMsgResponse.setLicenseNo(value);
							} else if (startTag.equalsIgnoreCase("LABELTYPE")) {
								policyMsgResponse.setLabelType(value);
							} else if (startTag.equalsIgnoreCase("VINNO")) {
								policyMsgResponse.setVidNo(value);
							} else if (startTag.equalsIgnoreCase("ENGINENO")) {
								policyMsgResponse.setEngineNo(value);
							} else if (startTag.equalsIgnoreCase("CARFIRSTREGISTRATION")) {
								policyMsgResponse.setCarFirstregistration(value);
							} else if (startTag.equalsIgnoreCase("USEYEARS")) {
								policyMsgResponse.setUseYears(value);
							} else if (startTag.equalsIgnoreCase("NEWCARPURCHASEPRICE")) {
								policyMsgResponse.setNewCarPurchaseprice(value);
							} else if (startTag.equalsIgnoreCase("VEHICLEUSE")) {
								policyMsgResponse.setVehicleuse(value);
							} else if (startTag.equalsIgnoreCase("QSPOLICYNO")) {
								policyMsgResponse.setQsPolicyNo(value);
							} else if (startTag.equalsIgnoreCase("QSPOLICYINSUREDATE")) {
								policyMsgResponse.setQsPolicyInsureDate(value);
							} else if (startTag.equalsIgnoreCase("BUSINESSPOLICYNO")) {
								policyMsgResponse.setBusInessPolicyNo(value);
							} else if (startTag.equalsIgnoreCase("BUSINESSPOLICYINSUREDATE")) {
								policyMsgResponse.setBusInessPolicyInsureDate(value);
							} else if (startTag.equalsIgnoreCase("BASICCLAUSETYPES")) {
								policyMsgResponse.setBasicClausetypes(value);
							} else if (startTag.equalsIgnoreCase("POLICYCOMCODE")) {
								policyMsgResponse.setPolicyComCode(value);
							} else if (startTag.equalsIgnoreCase("BUSINESSFLAG")) {
								policyMsgResponse.setBusInessFlag(value);
							} else if (startTag.equalsIgnoreCase("POLICYENDORSES")) {
								policyMsgResponse.setPolicyEndOrses(value);
							} else if (startTag.equalsIgnoreCase("ITEMKINDNAME")) {
								policyMsgInsuranceType.setItemKindCode(value);
							} else if (startTag.equalsIgnoreCase("ITEMKINDAMOUNT")) {
								policyMsgInsuranceType.setItemKindAmount(value);
							}
							if (policyMsgInsuranceType.getItemKindCode() != null && policyMsgInsuranceType.getItemKindAmount() != null) {
								policyMsgResponse.getPolicyMsgInsuranceTypes().add(policyMsgInsuranceType);
								policyMsgInsuranceType = new PolicyMsgInsuranceType();
							}

							if (appointModel != null) {
								if (startTag.equalsIgnoreCase("SERIALNO")) {
									appointModel.setSerialNo(value);
								} else if (startTag.equalsIgnoreCase("CLAUSECODE")) {
									appointModel.setClauseCode(value);
								} else if (startTag.equalsIgnoreCase("CLAUSE")) {
									appointModel.setClause(value);
								} else if (startTag.equalsIgnoreCase("CLAUSEDETAIL")) {
									appointModel.setClauseDetall(value);
								}
							}
							if (startTag.equalsIgnoreCase("CENGAGE")) {
								appointModel = new AppointModel();
							}
							eventType = parser.next();
						} else {
							eventType = parser.next();
						}

					} else if (eventType == XmlPullParser.END_TAG) {
						endTag = parser.getName();
						if (endTag.equalsIgnoreCase("CENGAGE")) {
							policyMsgResponse.getAppointModels().add(appointModel);
							appointModel = null;
						}
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				}

			}
		}

		return policyMsgResponse;
	}
}
