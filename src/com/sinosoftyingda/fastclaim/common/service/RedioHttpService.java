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
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.RedioRequest;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 后援视频接口类
 * 
 * @author haoyun 20130226
 * 
 */
public class RedioHttpService {
	/**
	 * 后援视频接口方法
	 * 
	 * 
	 * @return
	 */
	public static CommonResponse redioService(RedioRequest redioRequest, String url) {
		CommonResponse responseDuty = new CommonResponse();
		boolean isSubmit = false;
		try {

			String message = "";

			String requestXML = createRedioRequest(redioRequest);
			Log.i("mCliam", "实时视频------------->requestXML:" + requestXML);
			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "实时视频------------->responseXML:" + responseXML);
			responseDuty = redioResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("REDIO");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
//			 */
//			infoBuffer.setType("REDIOBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			responseDuty.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
		}
		return responseDuty;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createRedioRequest(RedioRequest redioRequest) throws Exception {
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
		serializer.text("VIDEOAPPLY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(redioRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(redioRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
		// 终端唯一标识号
		serializer.startTag("", "DEVICEID");
		if (SystemConfig.loginResponse != null && SystemConfig.loginResponse.getDeviceId() != null) {
			serializer.text(SystemConfig.loginResponse.getDeviceId());
		} else {
			serializer.text("");
		}
		serializer.endTag("", "DEVICEID");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(redioRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 定损号
		serializer.startTag("", "NODETYPE");
		serializer.text(redioRequest.getNodeType());
		serializer.endTag("", "NODETYPE");
		// 查勘定损标志
		serializer.startTag("", "LOSSNO");
		serializer.text(redioRequest.getLossNo());
		serializer.endTag("", "LOSSNO");
		// 是否协赔员
		serializer.startTag("", "ISXEIPEI");
		serializer.text(redioRequest.getIsXeipei());
		serializer.endTag("", "ISXEIPEI");
		// 联系客户时间
		serializer.startTag("", "CALLCLIENTDATE");
		serializer.text(redioRequest.getCerTainCallTime());
		serializer.endTag("", "CALLCLIENTDATE");

		// 被保险人联系电话
		serializer.startTag("", "INSUREDPHONE");
		serializer.text(redioRequest.getInsuredPhone());
		serializer.endTag("", "INSUREDPHONE");

		// 受托人姓名
		serializer.startTag("", "LINKNAME");
		serializer.text(redioRequest.getLinkName());
		serializer.endTag("", "LINKNAME");

		// 受托人联系电话
		serializer.startTag("", "LINKPHONE");
		serializer.text(redioRequest.getLinkPhone());
		serializer.endTag("", "LINKPHONE");

		// 用户名
		serializer.startTag("", "USERCODE");
		serializer.text(redioRequest.getUserCode());
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
	private static CommonResponse redioResponseXml(String responseXml) throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		CommonResponse commonResponse = new CommonResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			commonResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				commonResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				commonResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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

						if (!startTag.equals("PACKET") && !startTag.equals("HEAD") && !startTag.equals("BODY")) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								commonResponse.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								commonResponse.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								commonResponse.setResponseMessage(value);
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

		return commonResponse;
	}
}
