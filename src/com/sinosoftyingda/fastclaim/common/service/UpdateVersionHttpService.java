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

import com.sinosoftyingda.fastclaim.common.model.UpdateVersionRequest;
import com.sinosoftyingda.fastclaim.common.model.UpdateVersionResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

public class UpdateVersionHttpService {
	/**
	 * 版本更新接口方法
	 * 
	 * 
	 * @return
	 */
	public static UpdateVersionResponse updateVersionService(UpdateVersionRequest updateVersionRequest,String url) {
		UpdateVersionResponse updateVersionResponse = new UpdateVersionResponse();
		try {
			String requestXML = createUpdateVersionRequest(updateVersionRequest);
			Log.i("mCliam", "更新------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML=httpUtils.doPost(url, params);
//			String responseXML = "<?xml version='1.0' encoding='UTF-8'?>"
//					+ "<PACKET type='REQUEST' version='1.0'>" + "<HEAD>"
//					+ "<RESPONSETYPE>LOGINBACK</RESPONSETYPE>"
//					+ "<RESPONSECODE>YES</RESPONSECODE>"
//					+ "<RESPONSEMESSAGE>Success</RESPONSEMESSAGE>"
//					+ "<INTERFACEUSERCODE>user</INTERFACEUSERCODE>"
//					+ "<INTERFACEPASSWORD>password</INTERFACEPASSWORD>"
//					+ "<LANGUAGE>zh</LANGUAGE>" + "</HEAD>" + "<BODY>"
//					+ "<USERCODE>user</USERCODE>"
//					+ "<PASSWORD>password</PASSWORD>"
//					+ "<TASKREFRESHTIME>30</TASKREFRESHTIME>"
//					+ "<DUTYFLAG>on</DUTYFLAG>" + "<USERNAME>张三</USERNAME>"
//					+ "<ROLE>查勘员</ROLE>" + "</BODY>" + "</PACKET>";
			Log.i("mCliam", "更新------------->responseXML:" + responseXML);
			updateVersionResponse = updateVersionResponseXml(responseXML);
//			/**
//			 * 保存请求报文
//			 */
//			InfoBuffer infoBuffer=new InfoBuffer();
//			infoBuffer.setType("UPDATE");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
//			/**
//			 * 保存响应报文
//			 */
//			infoBuffer.setType("UPDATEBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			updateVersionResponse.setResponseMessage("终端与快赔交互失败:"+e.getMessage());
		}
		return updateVersionResponse;
	}

	/**
	 * 报文创建方法
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String createUpdateVersionRequest(UpdateVersionRequest request)
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
		serializer.text("VersionUpgrade");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(request.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(request.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
		
	

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 版本号
		serializer.startTag("", "VERSIONNO");
		serializer.text(request.getVersion());
		serializer.endTag("", "VERSIONNO");
	
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static UpdateVersionResponse updateVersionResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		
		UpdateVersionResponse responseUpdateVersion = new UpdateVersionResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			responseUpdateVersion.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				responseUpdateVersion.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				responseUpdateVersion.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								responseUpdateVersion.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								responseUpdateVersion.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								responseUpdateVersion.setResponseMessage(value);
							} else if (startTag.equalsIgnoreCase("UPDATEURL")) {
								responseUpdateVersion.setUpdateUrl(value);
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

		return responseUpdateVersion;
	}
}
