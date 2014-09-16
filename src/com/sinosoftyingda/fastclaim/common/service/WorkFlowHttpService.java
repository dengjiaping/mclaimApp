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

import com.sinosoftyingda.fastclaim.common.model.CertainLoss;
import com.sinosoftyingda.fastclaim.common.model.Check;

import com.sinosoftyingda.fastclaim.common.model.Schedule;
import com.sinosoftyingda.fastclaim.common.model.VerifyLoss;
import com.sinosoftyingda.fastclaim.common.model.VerifyPrice;
import com.sinosoftyingda.fastclaim.common.model.WorkFlowRequest;
import com.sinosoftyingda.fastclaim.common.model.WorkflowlogViewResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

public class WorkFlowHttpService {

	/**
	 * 工作流接口信息
	 * 
	 * @return
	 */
	public static WorkflowlogViewResponse workFlowService(WorkFlowRequest workFlowRequest, String url) {
		WorkflowlogViewResponse response = new WorkflowlogViewResponse();
		try {
			String requestXML = createWorkFlowRequestRequest(workFlowRequest);
			Log.i("mCliam", "工作流------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			// String responseXML = "<?xml version='1.0' encoding='UTF-8'?>"
			// + "<PACKET type='REQUEST' version='1.0'>" + "<HEAD>"
			// + "<RESPONSETYPE>LOGINBACK</RESPONSETYPE>"
			// + "<RESPONSECODE>YES</RESPONSECODE>"
			// + "<RESPONSEMESSAGE>Success</RESPONSEMESSAGE>"
			// "</HEAD>" + "<BODY>"
			// "</BODY>" + "</PACKET>";
			Log.i("mCliam", "工作流------------->responseXML:" + responseXML);
			response = workFlowResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("WORKFLOWLOGVIEW");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("WORKFLOWLOGVIEWBACK");
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
	 * 
	 * @throws Exception
	 */
	private static String createWorkFlowRequestRequest(WorkFlowRequest workFlowRequest) throws Exception {
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
		serializer.text("WorkFlowLogView");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(workFlowRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(workFlowRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(workFlowRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 用户名
		serializer.startTag("", "USERCODE");
		serializer.text(workFlowRequest.getUserCode());
		serializer.endTag("", "USERCODE");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static WorkflowlogViewResponse workFlowResponseXml(String responseXml) throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		WorkflowlogViewResponse workflowlogViewResponse = new WorkflowlogViewResponse();
		// 调度
		Schedule schedule = null;
		// 查勘
		Check check = null;
		// 定损
		CertainLoss certainLoss = null;
		// 核价
		VerifyPrice verifyPrice = null;
		// 核损
		VerifyLoss verifyLoss = null;
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			workflowlogViewResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				workflowlogViewResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				workflowlogViewResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
							workflowlogViewResponse.setReaponseType(value);
						} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
							value = parser.nextText().trim();
							workflowlogViewResponse.setResponseCode(value);
						} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
							value = parser.nextText().trim();
							workflowlogViewResponse.setResponseMessage(value);
						}
						/**
						 * 调度
						 */

						if (schedule != null) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("SCHEDULEUSERCODE")) {
								schedule.setScheduleuserCode(value);
							} else if (startTag.equalsIgnoreCase("SCHEDULEINPUTTIME")) {

								schedule.setScheduleinputTime(value);
							}
						}
						if (startTag.equalsIgnoreCase("SCHEDULE")) {
							schedule = new Schedule();
						}
						/**
						 * 查勘
						 */

						if (check != null) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("CHECKUSERCODE")) {
								check.setCheckUserCode(value);
							} else if (startTag.equalsIgnoreCase("CHECKACCEPTTIME")) {
								check.setCheckAcceptTime(value);
							} else if (startTag.equalsIgnoreCase("CHECKHANDLETIME")) {
								check.setCheckHandleTime(value);
							}
						}
						if (startTag.equalsIgnoreCase("CHECK")) {
							check = new Check();
						}
						/**
						 * 定损
						 */

						if (certainLoss != null) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("ITEMNAME")) {
								certainLoss.setItemName(value);
							} else if (startTag.equalsIgnoreCase("CERTAINUSERCODE")) {
								certainLoss.setCertinUserCode(value);
							} else if (startTag.equalsIgnoreCase("CERTAINACCEPTTIME")) {
								certainLoss.setCertinAcceptTime(value);
							} else if (startTag.equalsIgnoreCase("CERTAINHANDLETIME")) {
								certainLoss.setCertinHandleTime(value);
							} else if (startTag.equalsIgnoreCase("CERTAINAMOUNT")) {
								certainLoss.setCertainAmount(value);
							}
						}
						if (startTag.equalsIgnoreCase("CERTAINLOSS")) {
							certainLoss = new CertainLoss();
						}
						/**
						 * 核价
						 */

						if (verifyPrice != null) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("ITEMNAME")) {
								verifyPrice.setItemName(value);
							} else if (startTag.equalsIgnoreCase("CERTAINUSERCODE")) {
								verifyPrice.setCertainUserCode(value);
							} else if (startTag.equalsIgnoreCase("VERIFYPRICEUSERCODE")) {
								verifyPrice.setVerifypriceUserCode(value);
							} else if (startTag.equalsIgnoreCase("VERIFYPRICEHANDLETIME")) {
								verifyPrice.setVerifyPriceHandleTime(value);
							}
						}
						if (startTag.equalsIgnoreCase("VERIFYPRICE")) {
							verifyPrice = new VerifyPrice();
						}
						/**
						 * 核损
						 */

						if (verifyLoss != null) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("ITEMNAME")) {
								verifyLoss.setItemName(value);
							} else if (startTag.equalsIgnoreCase("CERTAINUSERCODE")) {
								verifyLoss.setCertainUserCode(value);
							} else if (startTag.equalsIgnoreCase("VERIFYLOSSUSERCODE")) {
								verifyLoss.setVerifyLossUserCode(value);
							} else if (startTag.equalsIgnoreCase("VERIFYLOSSHANDLETIME")) {
								verifyLoss.setVerifyPriceHandleTime(value);
							}
						}
						if (startTag.equalsIgnoreCase("VERIFYLOSS")) {
							verifyLoss = new VerifyLoss();
						}
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_TAG) {
						endTag = parser.getName();
						if (endTag.equalsIgnoreCase("SCHEDULE")) {
							if ((schedule.getScheduleinputTime() != null && !schedule.getScheduleinputTime().equals(""))
									|| (schedule.getScheduleuserCode() != null && !schedule.getScheduleuserCode().equals(""))) {
								workflowlogViewResponse.setSchedule(schedule);
							}

							schedule = null;
						} else if (endTag.equalsIgnoreCase("CHECK")) {
							if ((check.getCheckAcceptTime() != null && !check.getCheckAcceptTime().equals(""))
									|| (check.getCheckHandleTime() != null && !check.getCheckHandleTime().equals(""))
									|| (check.getCheckUserCode() != null && !check.getCheckUserCode().equals(""))) {
								workflowlogViewResponse.setCheck(check);
							}
							check = null;
						} else if (endTag.equalsIgnoreCase("CERTAINLOSS")) {
							if ((certainLoss.getCertainAmount() != null && !certainLoss.getCertainAmount().equals(""))
									|| (certainLoss.getCertinAcceptTime() != null && !certainLoss.getCertinAcceptTime().equals(""))
									|| (certainLoss.getCertinHandleTime() != null && !certainLoss.getCertinHandleTime().equals(""))
									|| (certainLoss.getCertinUserCode() != null && !certainLoss.getCertinUserCode().equals(""))
									|| (certainLoss.getItemName() != null && !certainLoss.getItemName().equals(""))) {

								workflowlogViewResponse.getCertainLossList().add(certainLoss);
							}
							certainLoss = null;
						} else if (endTag.equalsIgnoreCase("VERIFYPRICE")) {
							if ((verifyPrice.getCertainUserCode() != null && !verifyPrice.getCertainUserCode().equals(""))
									|| (verifyPrice.getItemName() != null && !verifyPrice.getItemName().equals(""))
									|| (verifyPrice.getVerifyPriceHandleTime() != null && !verifyPrice.getVerifyPriceHandleTime().equals(""))
									|| (verifyPrice.getVerifypriceUserCode() != null && !verifyPrice.getVerifypriceUserCode().equals(""))) {

								workflowlogViewResponse.getVerifyPriceList().add(verifyPrice);
							}
							verifyPrice = null;
						} else if (endTag.equalsIgnoreCase("VERIFYLOSS")) {
							if ((verifyLoss.getCertainUserCode() != null && !verifyLoss.getCertainUserCode().equals(""))
									|| (verifyLoss.getItemName() != null && !verifyLoss.getItemName().equals(""))
									|| (verifyLoss.getVerifyPriceHandleTime() != null && !verifyLoss.getVerifyPriceHandleTime().equals(""))
									|| (verifyLoss.getVerifyLossUserCode() != null && !verifyLoss.getVerifyLossUserCode().equals(""))) {
								workflowlogViewResponse.getVerifyLossList().add(verifyLoss);
							}
							verifyLoss = null;
						}
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				}

			}
		}

		return workflowlogViewResponse;
	}

}
