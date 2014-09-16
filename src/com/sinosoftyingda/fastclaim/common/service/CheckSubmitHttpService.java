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
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;
import com.sinosoftyingda.fastclaim.survey.utils.UtilIsNotNull;

/**
 * 查勘提交接口
 * 
 * @author haoyun 20130308
 * 
 */
public class CheckSubmitHttpService {
	/**
	 * 查勘提交接口方法
	 * 
	 * @param responseDuty
	 * @return
	 */
	public static CommonResponse checkSubmitService(
			DetailTaskQueryResponse checkSubmitRequest, String url) {
		CommonResponse responseDuty = new CommonResponse();
		boolean isSubmit = false;
		try {
			String message = "";
			if (!checkSubmitRequest.getSubmitType().equals("synch")) {
				// 非空校验
				message = UtilIsNotNull.isNotNull(checkSubmitRequest,true);				
			} else {
				// 非空校验
				message = UtilIsNotNull.isNotNull(checkSubmitRequest,false);
			}
			if (message.equals("YES")) {
				isSubmit = true;
			}
			if (isSubmit) {
				// 查找预约时间
				String orderTime = CheckTaskAccess.findByRegistno(checkSubmitRequest.getRegistNo()).getOrdertime();
				String requestXML = createCheckSubmitRequest(checkSubmitRequest, orderTime);
				/**
				 * 输出请求报文 由于报文太长无法显示全了所以1000字输出一次
				 */
				int index = 0;
				if (requestXML.length() % 1000 == 0) {
					index = requestXML.length() / 1000;
				} else {
					index = (requestXML.length() / 1000) + 1;
				}
				int begin = 0;
				for (int i = 1; i <= index; i++) {
					int end = 1000 * i > requestXML.length() ? requestXML
							.length() : 1000 * i;
					Log.i("mCliam", "查勘提交同步------------->requestXML500:"
							+ requestXML.substring(begin, end));
					begin = end;

				}

				HttpUtils httpUtils = new HttpUtils();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("content", requestXML);
				String responseXML = httpUtils.doPost(url, params);
				Log.i("mCliam", "查勘提交同步------------->responseXML:"
						+ responseXML);
				responseDuty = checkSubmitResponseXml(responseXML);

				/**
				 * 保存请求报文
				 */
//				InfoBuffer infoBuffer = new InfoBuffer();
//				infoBuffer.setType("CHECKSUBMIT");
//				infoBuffer.setXmlContent(requestXML);
//				TblInfoBuffer.addinfobuffer(infoBuffer);
				/**
				 * 保存响应报文
				 */
//				infoBuffer.setType("CHECKSUBMITBACK");
//				infoBuffer.setXmlContent(responseXML);
//				TblInfoBuffer.addinfobuffer(infoBuffer);
			} else {
				responseDuty.setResponseCode("NO");
				responseDuty.setResponseMessage(message);
			}
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
	private static String createCheckSubmitRequest(DetailTaskQueryResponse request, String orderTime) throws Exception {
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
		serializer.text("CHECKSUBMIT");
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
		serializer.startTag("", "CHECK");
		// 系统用户名
		serializer.startTag("", "USERCODE");
		serializer.text(SystemConfig.USERLOGINNAME);
		serializer.endTag("", "USERCODE");
		// 报案号
		serializer.startTag("", "REGISTNO");
		serializer.text(request.getRegistNo());
		serializer.endTag("", "REGISTNO");
		// 提交类型
		serializer.startTag("", "SUBMITTYPE");
		serializer.text(request.getSubmitType());
		serializer.endTag("", "SUBMITTYPE");
		// 提交时间
		serializer.startTag("", "SUBMITDATE");
		serializer.text("");
		serializer.endTag("", "SUBMITDATE");
		// 查勘类型 (L-查勘 D-代查勘 B-被查勘 )默认：L
		serializer.startTag("", "CHECKTYPE");
		serializer.text("L");
		serializer.endTag("", "CHECKTYPE");
		// 是否第一现场
		serializer.startTag("", "FIRSTSITEFLAG");
		serializer.text(request.getFirstsiteFlag());
		serializer.endTag("", "FIRSTSITEFLAG");
		// 案件类型
		serializer.startTag("", "CLAIMTYPE");
		serializer.text(request.getClaimType());
		serializer.endTag("", "CLAIMTYPE");
		// 出险原因代码
		serializer.startTag("", "DAMAGECODE");
		serializer.text(request.getDamageCode());
		serializer.endTag("", "DAMAGECODE");
		// 出险原因名称
		serializer.startTag("", "DAMAGENAME");
		serializer.text(request.getDamageName());
		serializer.endTag("", "DAMAGENAME");
		// 事故类型代码(理赔待定)
		serializer.startTag("", "DAMAGETYPECODE");
		serializer.text(request.getDamageTypeCode());
		serializer.endTag("", "DAMAGETYPECODE");
		// 事故类型说明(理赔待定)
		serializer.startTag("", "DAMAGETYPENAME");
		serializer.text(request.getDamageTypeName());
		serializer.endTag("", "DAMAGETYPENAME");
		// 是否代位求偿
		serializer.startTag("", "SUBROGATETYPE");
		serializer.text(request.getSubrogateType());
		serializer.endTag("", "SUBROGATETYPE");
		// 是否通赔
		serializer.startTag("", "ISCOMMONCLAIM");
		serializer.text(request.getIsCommonClaim());
		serializer.endTag("", "ISCOMMONCLAIM");
		// 保险事故分类 没有数据可为空
		serializer.startTag("", "ACCIDENTTYPE");
		serializer.text("");
		serializer.endTag("", "ACCIDENTTYPE");
		// 有无交管事故
		serializer.startTag("", "ACCIDENTBOOK");
		serializer.text(request.getAccidentBook().equals("") ? "0" : request
				.getAccidentBook());
		serializer.endTag("", "ACCIDENTBOOK");
		// 是否道路交通事故
		serializer.startTag("", "ACCIDENT");
		serializer.text(request.getAccident().equals("") ? "0" : request
				.getAccident());
		serializer.endTag("", "ACCIDENT");
		// 被保险人姓名
		serializer.startTag("", "INSUREDNAME");
		serializer.text(request.getInsrtedName());
		serializer.endTag("", "INSUREDNAME");
		// 被保险人电话
		serializer.startTag("", "INSUREDMOBILE");
		serializer.text(request.getInsuredMobile());
		serializer.endTag("", "INSUREDMOBILE");
		// 受托人姓名
		serializer.startTag("", "ENTRUSTNAME");
		serializer.text(request.getEntrustName());
		serializer.endTag("", "ENTRUSTNAME");
		// 受托人电话
		serializer.startTag("", "ENTRUSTMOBILE");
		serializer.text(request.getEntrustMobile());
		serializer.endTag("", "ENTRUSTMOBILE");
		// 异地理赔调查标识
		serializer.startTag("", "OTHERCLAIM");
		serializer.text("");
		serializer.endTag("", "OTHERCLAIM");
		// 满意度调查
		serializer.startTag("", "SATISFACTION");
		serializer.text(request.getSatisfacTion());
		serializer.endTag("", "SATISFACTION");
		// 查勘报告
		serializer.startTag("", "CHECKREPORT");
		serializer.text(request.getCheckReport());
		serializer.endTag("", "CHECKREPORT");
		// 查勘成功联系客户时间
		serializer.startTag("", "CALLCLIENTDATE");
		serializer.text(request.getLinkcustomTime());
		serializer.endTag("", "CALLCLIENTDATE");
		// 查勘首张照片时间(终端增加)
		serializer.startTag("", "FIRSTPHOTODATE");
		serializer.text(request.getArricesceneTime());
		serializer.endTag("", "FIRSTPHOTODATE");
		// // 查勘任务受理时间
		// serializer.startTag("", "ACCEPTTASKDATE");
		// serializer.text(request.getAcceptTaskDate());
		// serializer.endTag("", "ACCEPTTASKDATE");
		// 查勘开始处理时间
		serializer.startTag("", "DISPOSETASKDATE");
		serializer.text(request.getArricesceneTime());
		serializer.endTag("", "DISPOSETASKDATE");
		// 事故责任代码
		serializer.startTag("", "INDEMNITYDUTY");
		serializer.text(request.getIndemnityDuty());
		serializer.endTag("", "INDEMNITYDUTY");
		
		// Add start DengGuang 2013-07-18
		// 预约时间
		serializer.startTag("", "ORDERTIME");
		serializer.text(orderTime);
		serializer.endTag("", "ORDERTIME");
		// Add end
		
		serializer.endTag("", "CHECK");
		// 查勘扩展信息集合
		serializer.startTag("", "CHECKEXTLIST");
		int checkExtNo = 0;
		boolean isAdd = false;
		for (int i = 0; i < request.getCheckExtList().size(); i++) {
			if (request.getCheckExtList().get(i).getCheckKernelSelect().equals("0")&& (request.getCheckExtList().get(i).getCheckExtRemark() != null && !request.getCheckExtList().get(i).getCheckExtRemark().equals(""))) {
					isAdd = true;
			} else if (request.getCheckExtList().get(i).getCheckKernelSelect().equals("1")) {
				isAdd = true;
			} else if (request.getCheckExtList().get(i).getCheckKernelSelect().equals("2")&& (request.getCheckExtList().get(i).getCheckExtRemark() != null && !request.getCheckExtList().get(i).getCheckExtRemark().equals(""))) {
				isAdd = true;
			}
			if (isAdd) {
				// 查勘信息
				serializer.startTag("", "CHECKEXT");
				// 报案号码
				serializer.startTag("", "REGISTNO");
				serializer.text(request.getRegistNo());
				serializer.endTag("", "REGISTNO");
				// 序号
				serializer.startTag("", "SERIALNO");
				// serializer.text(request.get);
				serializer.text(request.getCheckExtList().get(i).getSerialNo());
				serializer.endTag("", "SERIALNO");
				// 查勘项目分类
				serializer.startTag("", "COLUMNTYPE");
				serializer.text(request.getCheckExtList().get(i)
						.getCheckKernelType());
				serializer.endTag("", "COLUMNTYPE");
				// 查勘项目代码
				serializer.startTag("", "COLUMNNAME");
				serializer.text(request.getCheckExtList().get(i)
						.getCheckKernelCode());
				serializer.endTag("", "COLUMNNAME");
				// 查勘项目名称
				serializer.startTag("", "DISPLAYNAME");
				serializer.text(request.getCheckExtList().get(i)
						.getCheckKernelName());
				serializer.endTag("", "DISPLAYNAME");

				// 查勘字段内容（是否）
				serializer.startTag("", "COLUMNVALUE");
				serializer.text(request.getCheckExtList().get(i)
						.getCheckKernelSelect());
				serializer.endTag("", "COLUMNVALUE");
				// 查勘录入文本
				serializer.startTag("", "COLUMNINPUTTEXT");
				try {
					serializer.text(request.getCheckExtList().get(i)
							.getCheckExtRemark());

				} catch (Exception e) {
					serializer.text("");
				}
				serializer.endTag("", "COLUMNINPUTTEXT");
				serializer.endTag("", "CHECKEXT");
			}
		}

		serializer.endTag("", "CHECKEXTLIST");
		// 查勘车辆信息集合
		serializer.startTag("", "CHECKTHIRDPARTYLIST");
		int CarLossListNo = 1;
		for (int i = 0; i < request.getCarLossList().size(); i++) {
			// 查勘车辆信息
			serializer.startTag("", "CHECKTHIRDPARTY");
			// 报案号码
			serializer.startTag("", "REGISTNO");
			serializer.text(request.getRegistNo());
			serializer.endTag("", "REGISTNO");
			// 序号
			serializer.startTag("", "SERIALNO");
			serializer.text(request.getCarLossList().get(i).getCarNum());
			serializer.endTag("", "SERIALNO");

			// 车牌号
			serializer.startTag("", "LICENSENO");
			serializer.text(request.getCarLossList().get(i).getLicenseNo());
			serializer.endTag("", "LICENSENO");
			// 车辆种类代码
			serializer.startTag("", "CARKINDCODE");
			serializer.text(request.getCarLossList().get(i).getCarKindCode());
			serializer.endTag("", "CARKINDCODE");
			// 号牌种类
			serializer.startTag("", "LICENSETYPE");
			serializer.text(request.getCarLossList().get(i).getLicenseType());
			serializer.endTag("", "LICENSETYPE");
			// 是否为本保单车辆(没有)
			serializer.startTag("", "INSURECARFLAG");
			serializer.text(request.getCarLossList().get(i).getInsureCarFlag());
			serializer.endTag("", "INSURECARFLAG");
			// 车主
			serializer.startTag("", "CAROWNER");
			serializer.text("");
			serializer.endTag("", "CAROWNER");
			// 发动机号
			serializer.startTag("", "ENGINENO");
			serializer.text(request.getCarLossList().get(i).getEngineNo());
			serializer.endTag("", "ENGINENO");
			// 车架号
			serializer.startTag("", "FRAMENO");
			serializer.text(request.getCarLossList().get(i).getFrameNo());
			serializer.endTag("", "FRAMENO");
			// 厂牌型号
			serializer.startTag("", "BRANDNAME");
			serializer.text(request.getCarLossList().get(i).getBrandName());
			serializer.endTag("", "BRANDNAME");
			// 三者车保单号
			serializer.startTag("", "THIRDPOLICYNO");
			serializer.text(request.getCarLossList().get(i).getNullPolicyNo());
			serializer.endTag("", "THIRDPOLICYNO");

			// 本车责任比例
			serializer.startTag("", "DUTYPERCENT");
			serializer.text(request.getCarLossList().get(i).getDutyPercent());
			serializer.endTag("", "DUTYPERCENT");

			// 承保公司代码
			serializer.startTag("", "INSURECOMCODE");
			serializer.text(request.getCarLossList().get(i).getInsurecomCode());
			serializer.endTag("", "INSURECOMCODE");
			// 承保公司名称
			serializer.startTag("", "INSURECOMNAME");
			serializer.text(request.getCarLossList().get(i).getInsurecomName());
			serializer.endTag("", "INSURECOMNAME");
			//驾驶员姓名
			serializer.startTag("", "NULLDRIVERNAME");
			serializer.text(request.getCarLossList().get(i).getNullDriverName());
			serializer.endTag("", "NULLDRIVERNAME");
			//驾驶员证件类型
			serializer.startTag("", "NULLCERTITYPE");
			serializer.text(request.getCarLossList().get(i).getNullCertitypeCode());
			serializer.endTag("", "NULLCERTITYPE");
			//驾驶员证件号码
			serializer.startTag("", "NULLDRIVERCODE");
			serializer.text(request.getCarLossList().get(i).getNullDriverCode());
			serializer.endTag("", "NULLDRIVERCODE");
			
			serializer.endTag("", "CHECKTHIRDPARTY");
		}
		serializer.endTag("", "CHECKTHIRDPARTYLIST");
		// 驾驶人信息集合
		serializer.startTag("", "CHECKDRIVERLIST");
		for (int i = 0; i < request.getCheckDriver().size(); i++) {
			// 驾驶员信息
			serializer.startTag("", "CHECKDRIVER");
			// 报案号码
			serializer.startTag("", "REGISTNO");
			serializer.text(request.getRegistNo());
			serializer.endTag("", "REGISTNO");
			// 序号
			serializer.startTag("", "SERIALNO");
			serializer.text(request.getCheckDriver().get(i).getSerialNo());
			serializer.endTag("", "SERIALNO");
			// 驾驶员驾驶证号码
			serializer.startTag("", "DRIVINGLICENSENO");
			serializer.text(request.getCheckDriver().get(i)
					.getDrivinglicenseNo());
			serializer.endTag("", "DRIVINGLICENSENO");
			// 驾驶员姓名
			serializer.startTag("", "DRIVERNAME");
			serializer.text(request.getCheckDriver().get(i).getDriverName());
			serializer.endTag("", "DRIVERNAME");
			// 身份证号
			serializer.startTag("", "IDENTIFYNUMBER");
			serializer
					.text(request.getCheckDriver().get(i).getIdentifyNumber());
			serializer.endTag("", "IDENTIFYNUMBER");
			// 驾驶员领证时间
			serializer.startTag("", "RECEIVELICENSEDATE");
			serializer.text(request.getCheckDriver().get(i).getReceivelicenseDate().trim());
			serializer.endTag("", "RECEIVELICENSEDATE");
			serializer.startTag("", "DRIVERCERTITYPE");
			serializer.text(request.getCheckDriver().get(i).getDrivercertitypeCode().trim());
			serializer.endTag("", "DRIVERCERTITYPE");

			serializer.endTag("", "CHECKDRIVER");
		}
		serializer.endTag("", "CHECKDRIVERLIST");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static CommonResponse checkSubmitResponseXml(String responseXml)
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

							} else if (startTag.equalsIgnoreCase("HANDLETIME")) {
								responseDuty.setHandletime(value);

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
