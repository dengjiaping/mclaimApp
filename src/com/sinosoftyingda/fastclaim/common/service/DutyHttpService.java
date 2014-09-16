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

import com.sinosoftyingda.fastclaim.common.model.DutyRequest;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
/**
 * 上班下班接口类
 * @author haoyun 20130225
 *
 */
public class DutyHttpService {
	/**
	 * 上班下班接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse dutyService(DutyRequest requestDuty,String url) {
		CommonResponse responseDuty = new CommonResponse();
		try {
			String requestXML = createDutyRequest(requestDuty);
			Log.i("mCliam", "上下班------------->requestXML:" + requestXML);
			
		
			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML=httpUtils.doPost(url, params);
			Log.i("mCliam", "上下班------------->responseXML:" + responseXML);
			responseDuty = dutyResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer=new InfoBuffer();
//			infoBuffer.setType("ISDUTY");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("ISDUTYBACK");
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
	private static String createDutyRequest(DutyRequest requestDuty)
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
		serializer.text("ISDUTY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(requestDuty.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(requestDuty.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
		// 终端唯一标识号
		serializer.startTag("", "IMEI");
		serializer.text(requestDuty.getIMEI());
		serializer.endTag("", "IMEI");
		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
			// 系统用户名
			serializer.startTag("", "USERCODE");
			serializer.text(requestDuty.getUserCode());
			serializer.endTag("", "USERCODE");
	
			// 上下班标志
			serializer.startTag("", "DUTYFLAG");
			serializer.text(requestDuty.getDutyFlag());
			serializer.endTag("", "DUTYFLAG");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse dutyResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
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
