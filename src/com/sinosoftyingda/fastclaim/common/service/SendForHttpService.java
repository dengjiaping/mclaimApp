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

import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SendForRequest;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 改派申请接口类
 * 
 * @author haoyun 20130226
 * 
 */
public class SendForHttpService {
	/**
	 * 改派申请接口方法
	 * 
	 * @return
	 */
	public static CommonResponse sendForService(SendForRequest sendForRequest,
			String url) {
		CommonResponse responseDuty = new CommonResponse();
		try {
			String requestXML = createSendForRequest(sendForRequest);
			Log.i("mCliam", "改派申请------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "改派申请------------->responseXML:" + responseXML);
			responseDuty = sendForResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("PENDING");
//			infoBuffer.setXmlContent(requestXML);
////			TblInfoBuffer.addinfobuffer(infoBuffer);
//			/**
//			 * 保存响应报文
//			 */
//			infoBuffer.setType("PENDINGBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			responseDuty.setResponseMessage("终端与快赔交互失败:"+e.getMessage());
		}
		return responseDuty;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createSendForRequest(SendForRequest sendForRequest)
			throws Exception {
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
		serializer.text("REASSIGNAPPLY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(sendForRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(sendForRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 系统用户名
		serializer.startTag("", "USERCODE");
		serializer.text(sendForRequest.getUserCode());
		serializer.endTag("", "USERCODE");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(sendForRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 申请类型
		serializer.startTag("", "APPLYTYPE");
		serializer.text(sendForRequest.getApplyType());
		serializer.endTag("", "APPLYTYPE");

		// 改派申请原因
		serializer.startTag("", "REASSIGNPENDINGREASON");
		serializer.text(sendForRequest.getReassignPendingReason());
		serializer.endTag("", "REASSIGNPENDINGREASON");
		// 是否关联
		serializer.startTag("", "ISRELATED");
		serializer.text(sendForRequest.getIsRelated());
		serializer.endTag("", "ISRELATED");
		// 损失项目编号
		serializer.startTag("", "SCHEDULEITEMLIST");
		for (int i = 0; i < sendForRequest.getScheDuleItems().size(); i++) {
			serializer.startTag("", "SCHEDULEITEM");
			serializer.startTag("", "NODETYPE");
			serializer.text(sendForRequest.getScheDuleItems().get(i).getNodeType());
			serializer.endTag("", "NODETYPE");
			serializer.startTag("", "LOSSNO");
			serializer.text(sendForRequest.getScheDuleItems().get(i).getLossNo());
			serializer.endTag("", "LOSSNO");
			serializer.endTag("", "SCHEDULEITEM");
		}
		serializer.endTag("", "SCHEDULEITEMLIST");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse sendForResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		CommonResponse responseSendFor = new CommonResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			responseSendFor.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				responseSendFor.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				responseSendFor.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								responseSendFor.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								responseSendFor.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								responseSendFor.setResponseMessage(value);
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

		return responseSendFor;
	}
}
