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
import com.sinosoftyingda.fastclaim.common.db.dao.TblHistoricalClaim;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimItem;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimResponse;
import com.sinosoftyingda.fastclaim.common.model.LeastMsgQueryClaimItem;
import com.sinosoftyingda.fastclaim.common.model.LeaveMsgQueryClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.LeaveMsgQueryClaimResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
/**
 * 历史赔案信息
 * @author yxf 20140225
 *
 */
public class LeaveMessageQueryClaimHttpService {
	/**
	 * 历史赔案信息
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse leaveMsgQueryClaimService(LeaveMsgQueryClaimRequest leaveMsgQueryClaimRequest,String url) {
		LeaveMsgQueryClaimResponse response = new LeaveMsgQueryClaimResponse();
		try {
			String requestXML = createLeaveMsgQueryClaimRequest(leaveMsgQueryClaimRequest);
			Log.i("mCliam", "留言查看------------->requestXML:" + requestXML);
			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML=httpUtils.doPost(url, params);
			Log.i("mCliam", "留言查看------------->responseXML:" + responseXML);
			response = leaveMsgQueryClaimResponseXml(responseXML);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseMessage("终端与快赔交互失败:"+e.getMessage());
		}
		return response;
	}

	/**
	 * 报文创建方法
	 * 
	
	 * @throws Exception
	 */
	private static String createLeaveMsgQueryClaimRequest(LeaveMsgQueryClaimRequest leaveMsgQueryClaimRequest)
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
		serializer.text("MessageInfo");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(leaveMsgQueryClaimRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(leaveMsgQueryClaimRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
		
		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
			//报案号
			serializer.startTag("", "REGISTNO");
			serializer.text(leaveMsgQueryClaimRequest.getRegistNo());
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
	private static LeaveMsgQueryClaimResponse leaveMsgQueryClaimResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		LeaveMsgQueryClaimResponse leaveMsgQueryClaimResponse = new LeaveMsgQueryClaimResponse();
		LeastMsgQueryClaimItem leaveMsgQueryClaimItem=new LeastMsgQueryClaimItem();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			leaveMsgQueryClaimResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				leaveMsgQueryClaimResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				leaveMsgQueryClaimResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								&& !startTag.equals("BODY")
								&& !startTag.equals("MESSAGEINFOLIST")
								&& !startTag.equals("MESSAGEINFO") 
								) {
							value = parser.nextText().trim(); 
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								leaveMsgQueryClaimResponse.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								leaveMsgQueryClaimResponse.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								leaveMsgQueryClaimResponse.setResponseMessage(value);
							}else if(startTag.equalsIgnoreCase("SERIALNO")) {	
								//报案号
								leaveMsgQueryClaimItem.setRegistNo(value);
							}else if(startTag.equalsIgnoreCase("NODETYPE")) {
								//节点类型
								leaveMsgQueryClaimItem.setNodeType(value);
							}else if(startTag.equalsIgnoreCase("INPUTDATE")) {
								//输单日期
								leaveMsgQueryClaimItem.setSubmitTimes(value);
							}else if(startTag.equalsIgnoreCase("OPERATORNAME")) {
								//留言人姓名
								leaveMsgQueryClaimItem.setLeaveMsgPersonName(value);
							}else if(startTag.equalsIgnoreCase("CONTEXT")) {
								//留言内容
								leaveMsgQueryClaimItem.setLeaveMsgContent(value);
							}
							
							if(leaveMsgQueryClaimItem.getRegistNo()!=null
									&&leaveMsgQueryClaimItem.getLeaveMsgContent()!=null
									&&leaveMsgQueryClaimItem.getSubmitTimes()!=null
									&&leaveMsgQueryClaimItem.getLeaveMsgPersonName()!=null
									&&leaveMsgQueryClaimItem.getNodeType()!=null)
							{
								leaveMsgQueryClaimResponse.getLeaveMsgClaims().add(leaveMsgQueryClaimItem);
								leaveMsgQueryClaimItem=new LeastMsgQueryClaimItem();
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
		return leaveMsgQueryClaimResponse ;
	}
}
