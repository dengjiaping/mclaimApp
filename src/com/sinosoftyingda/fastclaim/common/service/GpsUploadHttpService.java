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

import com.sinosoftyingda.fastclaim.common.model.GPSUploadResponse;
import com.sinosoftyingda.fastclaim.common.model.GpsUploadRequest;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 实时接口类
 * 
 * @author haoyun 20130225
 * 
 */
public class GpsUploadHttpService {
	/**
	 * 实时位置接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static GPSUploadResponse gpsUploadService(GpsUploadRequest gpsUploadRequest, String url) {
		GPSUploadResponse responseDuty = new GPSUploadResponse();
		try {
			String requestXML = createGpsUploadRequest(gpsUploadRequest);
			Log.i("mCliam", "上传坐标------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "上传坐标------------->responseXML:" + responseXML);
			responseDuty = gpsUploadResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
			// InfoBuffer infoBuffer=new InfoBuffer();
			// infoBuffer.setType("GPSUPLOAD");
			// infoBuffer.setXmlContent(requestXML);
			// TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
			// infoBuffer.setType("GPSUPLOADBACK");
			// infoBuffer.setXmlContent(responseXML);
			// TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			responseDuty.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
		}
		return responseDuty;
	}

	/**
	 * 报文创建方法
	 * 
	 * 
	 * @throws Exception
	 */
	private static String createGpsUploadRequest(GpsUploadRequest gpsUploadRequest) throws Exception {
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
		serializer.text("GPSUPLOAD");
		serializer.endTag("", "REQUESTTYPE");

		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(gpsUploadRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(gpsUploadRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		// 终端唯一标识号
		serializer.startTag("", "IMEI");
		serializer.text(gpsUploadRequest.getIMEI());
		serializer.endTag("", "IMEI");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 系统用户名
		serializer.startTag("", "USERCODE");
		serializer.text(gpsUploadRequest.getUserCode());
		serializer.endTag("", "USERCODE");

		// 当前位置X坐标
		serializer.startTag("", "XCOORDINATEDEGREE");
		serializer.text(gpsUploadRequest.getxCoordinateDegree());
		serializer.endTag("", "XCOORDINATEDEGREE");
		// 当前位置Y坐标
		serializer.startTag("", "YCOORDINATEDEGREE");
		serializer.text(gpsUploadRequest.getyCoordinateDegree());
		serializer.endTag("", "YCOORDINATEDEGREE");
		// 当前时速
		serializer.startTag("", "SPEED");
		serializer.text(gpsUploadRequest.getSpeed());
		serializer.endTag("", "SPEED");
		// 当前方向角度
		serializer.startTag("", "DIRECTIONSDEGREE");
		serializer.text(gpsUploadRequest.getDirectionsdegree());
		serializer.endTag("", "DIRECTIONSDEGREE");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static GPSUploadResponse gpsUploadResponseXml(String responseXml) throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		GPSUploadResponse gpsUploadResponse = new GPSUploadResponse();
		/** 响应头 */
		if (responseXml == null) {
			gpsUploadResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				gpsUploadResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				gpsUploadResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								gpsUploadResponse.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								gpsUploadResponse.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								gpsUploadResponse.setResponseMessage(value);
							} else if (startTag.equalsIgnoreCase("LOCATION")) {
								gpsUploadResponse.setLocation(value);
							} else if (startTag.equalsIgnoreCase("SERVERTIME")) {
								gpsUploadResponse.setServerTime(value);
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

		return gpsUploadResponse;
	}
}
