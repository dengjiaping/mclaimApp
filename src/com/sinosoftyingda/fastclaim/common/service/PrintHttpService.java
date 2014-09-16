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

import com.sinosoftyingda.fastclaim.common.model.PrintAccessorles;
import com.sinosoftyingda.fastclaim.common.model.PrintRepair;
import com.sinosoftyingda.fastclaim.common.model.PrintRequest;
import com.sinosoftyingda.fastclaim.common.model.PrintResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 打印信息查詢接口类
 * 
 * @author 郝运 20130227
 * 
 */
public class PrintHttpService {
	/**
	 * 打印接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static PrintResponse printService(PrintRequest printRequest,String url) {
		PrintResponse responsePrint = new PrintResponse();
		try {
			String requestXML = createPrintRequest(printRequest);
			Log.i("mCliam", "打印信息查询------------->requestXML:" + requestXML);

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
			Log.i("mCliam", "打印信息查询------------->responseXML:" + responseXML);

			responsePrint = printResponseXml(responseXML);
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("PRINT");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("PRINTBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			responsePrint.setResponseMessage("终端与快赔交互失败:"+e.getMessage());
		}
		return responsePrint;
	}

	/**
	 * 报文创建方法
	 * 
	 * 
	 * @throws Exception
	 */
	private static String createPrintRequest(PrintRequest printRequest)
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
		serializer.text("VerifyLossPrint");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(printRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(printRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");
	

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
	
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(printRequest.getRegistNo());
		serializer.endTag("", "REGISTNO");
		//定损编号
		serializer.startTag("", "LOSSNO");
		serializer.text(printRequest.getLossNo());
		serializer.endTag("", "LOSSNO");
		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static PrintResponse printResponseXml(String responseXml)
			throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		PrintResponse printResponse = new PrintResponse();
		PrintAccessorles printAccessorles = new PrintAccessorles();
		PrintRepair printRepair = new PrintRepair();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			printResponse.setResponseMessage("服务器返回数据错误!请联系管理员!(终端)");
		} else {
			if (responseXml.equals("Timeout")) {
				printResponse.setResponseMessage("移动端与快赔系统连接超时,请检查网络后重新连接!(终端)");

			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				printResponse.setResponseMessage("移动端与快赔连接失败,请联系管理员!(终端)");
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
								&& !startTag.equals("COMPONENTLIST")
								&& !startTag.equals("COMPONENT")
								&& !startTag.equals("REPAIRFEELIST")
								&& !startTag.equals("REPAIRFEE")) {
							value = parser.nextText().trim();
							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								printResponse.setReaponseType(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSECODE")) {
								printResponse.setResponseCode(value);
							} else if (startTag
									.equalsIgnoreCase("RESPONSEMESSAGE")) {
								printResponse.setResponseMessage(value);
							} else if (startTag
									.equalsIgnoreCase("REGISTNO")) {
								printResponse.setRegistNo(value);
							} else if (startTag
									.equalsIgnoreCase("VERIFYCOMCODE")) {
								printResponse.setVerifyComCode(value);
							} else if (startTag.equalsIgnoreCase("VERIFYUSERCODE")) {
								printResponse.setVerifyUserCode(value);
							} else if (startTag
									.equalsIgnoreCase("QSPOLICYNO")) {
								printResponse.setQsPolicyNo(value);
							} else if (startTag
									.equalsIgnoreCase("BUSINESSPOLICYNO")) {
								printResponse.setBusInessPolicyNo(value);
							} else if (startTag.equalsIgnoreCase("INSURENAME")) {
								printResponse.setInsureName(value);
							} else if (startTag
									.equalsIgnoreCase("LOSSNO")) {
								printResponse.setLossNo(value);
							} else if (startTag.equalsIgnoreCase("LOSSNAME")) {
								printResponse.setLossName(value);
							} else if (startTag.equalsIgnoreCase("PLATENO")) {
								printResponse.setPlateNo(value);
							} else if (startTag.equalsIgnoreCase("BRANDNAME")) {
								printResponse.setBrandName(value);
							} else if (startTag
									.equalsIgnoreCase("VINNO")) {
								printResponse.setVinNo(value);
							} else if (startTag.equalsIgnoreCase("COMPONENTSUMFEE")) {
								printResponse.setComponentSumFee(value);
							} else if (startTag.equalsIgnoreCase("REPAIRFEESUMFEE")) {
								printResponse.setRepairfeesSumFee(value);
							} else if (startTag
									.equalsIgnoreCase("RESETFEE")) {
								printResponse.setResetFee(value);
							} else if (startTag
									.equalsIgnoreCase("VERIFYLOSSFEE")) {
								printResponse.setVerifyLossFee(value);
							} else if (startTag
									.equalsIgnoreCase("VERIFYLOSSFEE1")) {
								printResponse
										.setVertfyLossfee1(value);
							} else if (startTag
									.equalsIgnoreCase("PARTNAME")) {
								printAccessorles.setPartName(value);
							} else if (startTag
									.equalsIgnoreCase("QUANTITY")) {
								printAccessorles.setQuanTity(value);
							} else if (startTag
									.equalsIgnoreCase("SUMDEFLOSS")) {
								printAccessorles.setSumDefLoss(value);
							}else if (startTag
									.equalsIgnoreCase("COMPNAME")) {
								printRepair.setRepairName(value);
							}else if (startTag
									.equalsIgnoreCase("REPAIRFEEPRO")) {
								printRepair.setRepairPro(value);
							}else if (startTag
									.equalsIgnoreCase("REPAIRFEEPRICE")) {
								printRepair.setRepairPrice(value);
							}
							
							if(printRepair.getRepairName()!=null
									&&printRepair.getRepairPrice()!=null
									&&printRepair.getRepairPro()!=null)
							{
								printResponse.getPrintRepair().add(printRepair);
								printRepair=new PrintRepair();
							}
							if(printAccessorles.getPartName()!=null
									&&printAccessorles.getQuanTity()!=null
									&&printAccessorles.getSumDefLoss()!=null)
							{
								printResponse.getPrintAccessorles().add(printAccessorles);
								printAccessorles=new PrintAccessorles();
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

		return printResponse;
	}
}
