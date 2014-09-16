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
import com.sinosoftyingda.fastclaim.common.model.PicUploadRequest;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgInsuranceType;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 文件上传
 * 
 * @author haoyun 
 * 
 */
public class PicUploadHttpService {
	/**
	 * 保单详细信息接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse picUploadService(PicUploadRequest pic,
			String url) {
		CommonResponse commonResponse = new CommonResponse();
		try {
			String requestXML = createPicUploadRequest(pic);
			Log.i("mCliam", "拓保图片上传地址------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);
			Log.i("mCliam", "拓保图片上传地址------------->responseXML:" + responseXML);
			commonResponse = policyPicUploadResponseXml(responseXML);
		} catch (Exception e) {
			e.printStackTrace();
			commonResponse.setResponseCode("fail");
			commonResponse.setResponseMessage("上传交互失败！");
		}
		return commonResponse;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createPicUploadRequest(PicUploadRequest pic)
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
		serializer.text("FileUpload");
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
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(pic.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 节点类型
		serializer.startTag("", "NODETYPE");
		serializer.text(pic.getModelCode());
		serializer.endTag("", "NODETYPE");
		// 损失编号
		serializer.startTag("", "LOSSNO");
		serializer.text(pic.getLossNo());
		serializer.endTag("", "LOSSNO");
		// 文件名称
		serializer.startTag("", "ZIPNAME");
		serializer.text(pic.getFileName());
		serializer.endTag("", "ZIPNAME");
		// 用户名
		serializer.startTag("", "USERCODE");
		serializer.text(SystemConfig.USERLOGINNAME);
		serializer.endTag("", "USERCODE");
		// 机构代码
		serializer.startTag("", "DPTCODE");
		serializer.text(SystemConfig.loginResponse.getUserComCode());
		serializer.endTag("", "DPTCODE");
	
		// 文件信息列表
		serializer.startTag("", "IMAGEITEMLIST");

		for (int i = 0; i < pic.getImageItem().size(); i++) {

			serializer.startTag("", "IMAGEITEM");
			// 图片名称
			serializer.startTag("", "IMAGENAME");
			serializer.text(pic.getImageItem().get(i).getImageName());
			serializer.endTag("", "IMAGENAME");
			// 图片拍摄地址
			serializer.startTag("", "IMAGEMAKEADDRESS");
			serializer.text(pic.getImageItem().get(i).getImageMakeAddress());
			serializer.endTag("", "IMAGEMAKEADDRESS");
			// 备注
			serializer.startTag("", "REMARK");
			serializer.text(pic.getImageItem().get(i).getRemark());
			serializer.endTag("", "REMARK");
			serializer.endTag("", "IMAGEITEM");

		}
		serializer.endTag("", "IMAGEITEMLIST");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse policyPicUploadResponseXml(String responseXml)
			throws Exception {

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
								policyMsgResponse.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								policyMsgResponse.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								policyMsgResponse.setResponseMessage(value);
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

		return policyMsgResponse;
	}
}
