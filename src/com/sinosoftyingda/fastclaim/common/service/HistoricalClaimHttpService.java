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
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
/**
 * 历史赔案信息
 * @author haoyun 20130226
 *
 */
public class HistoricalClaimHttpService {
	/**
	 * 历史赔案信息
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse historicalClaimService(HistoricalClaimRequest historicalClaimRequest,String url) {
		HistoricalClaimResponse responseDuty = new HistoricalClaimResponse();
		try {
		
			String requestXML = createHistoricalClaimRequest(historicalClaimRequest);
			Log.i("mCliam", "历史赔案------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML=httpUtils.doPost(url, params);
//			String responseXML = "<?xml version='1.0' encoding='UTF-8'?>"
//					+ "<PACKET type='REQUEST' version='1.0'>" + "<HEAD>"
//					+ "<RESPONSETYPE>LOGINBACK</RESPONSETYPE>"
//					+ "<RESPONSECODE>YES</RESPONSECODE>"
//					+ "<RESPONSEMESSAGE>Success</RESPONSEMESSAGE>"
//				 "</HEAD>" + "<BODY>"
//					"</BODY>" + "</PACKET>";
			Log.i("mCliam", "历史赔案------------->responseXML:" + responseXML);
			responseDuty = historicalClaimResponseXml(responseXML);
			/**
			 * 保存历史赔案信息
			 */
			TblHistoricalClaim.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), responseDuty,historicalClaimRequest.getRegistNo());
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer=new InfoBuffer();
//			infoBuffer.setType("HISTORICALCLAIMS");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
//			 */
//			infoBuffer.setType("HISTORICALCLAIMSBACK");
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
	private static String createHistoricalClaimRequest(HistoricalClaimRequest historicalClaimRequest)
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
		serializer.text("HisClaimInfo");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(historicalClaimRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(historicalClaimRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
		
		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
			//报案号
			serializer.startTag("", "REGISTNO");
			serializer.text(historicalClaimRequest.getRegistNo());
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
	private static HistoricalClaimResponse historicalClaimResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		HistoricalClaimResponse historicalClaimResponse = new HistoricalClaimResponse();
		HistoricalClaimItem historicalClaimItem=new HistoricalClaimItem();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			historicalClaimResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				historicalClaimResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				historicalClaimResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								&& !startTag.equals("HISCLAIMLIST")
								&& !startTag.equals("CLAIMBASIC")
								) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								historicalClaimResponse.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								historicalClaimResponse.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								historicalClaimResponse.setResponseMessage(value);
							}else if(startTag
									.equalsIgnoreCase("HISTORICALACCIDENT")) {
								historicalClaimResponse.setHistoricalAccident(value);
							}else if(startTag
									.equalsIgnoreCase("HISTORICALCLAIMTIMES")) {
								historicalClaimResponse.setHistoricalClaimsNumber(value);
							}else if(startTag
									.equalsIgnoreCase("HISTORICALCLAIMSUM")) {
								historicalClaimResponse.setHistoricalClaimsSum(value);
							}else if(startTag
									.equalsIgnoreCase("REGISTNO")) {
								historicalClaimItem.setRegistNo(value);
							}else if(startTag
									.equalsIgnoreCase("DAMAGEDATE")) {
								historicalClaimItem.setDamageDate(value);
							}else if(startTag
									.equalsIgnoreCase("REGISTDATE")) {
								historicalClaimItem.setRegistDate(value);
							}else if(startTag
									.equalsIgnoreCase("DAMAGEADDRESS")) {
								historicalClaimItem.setDamageAddress(value);
							}else if(startTag
									.equalsIgnoreCase("CLAIMSTATUS")) {
								historicalClaimItem.setClaimStatus(value);
							}else if(startTag
									.equalsIgnoreCase("CLAIMAMOUNT")) {
								historicalClaimItem.setClaimAmount(value);
							}
							
							if(historicalClaimItem.getRegistNo()!=null
									&&historicalClaimItem.getDamageDate()!=null
									&&historicalClaimItem.getClaimStatus()!=null
									&&historicalClaimItem.getClaimAmount()!=null
									&&historicalClaimItem.getDamageAddress()!=null
									&&historicalClaimItem.getRegistDate()!=null)
							{
								historicalClaimResponse.getHistoricalClaims().add(historicalClaimItem);
								historicalClaimItem=new HistoricalClaimItem();
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

		return historicalClaimResponse;
	}
}
