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

import com.sinosoftyingda.fastclaim.Test;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 查勘定损任务类表�?
 * 
 * @author haoyun
 * 
 */
public class TaskQueryHttpService {
	/**
	 * 查勘定损任务接口方法
	 * 
	 * @return
	 */
	public static TaskQueryResponse taskQuerySercive(TaskQueryRequest taskQueryRequest, String url) {
		TaskQueryResponse response = new TaskQueryResponse();
		try {
			String requestXML = createTaskQueryRequest(taskQueryRequest);
			Log.i("mCliam", "查勘定损任务列表------------->requestXML:" + requestXML);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = null;
			if (SystemConfig.isUserExperience) {
				responseXML = Test.RESPONSE_MAINTASKQUERYBACK;
			} else {
				responseXML = httpUtils.doPost(url, params);
			}
			Log.i("mCliam", "查勘定损任务列表-----------" + url + "-->responseXML:" + responseXML);
			response = taskQueryResponseXml(responseXML);
			Log.i("mCliam", response.getCertainLossTask().size() + "+++++++++++++");
			/**
			 * 查勘任务插入数据�?
			 */
			CheckTaskAccess.insertOrUpdate(response.getCheckTask(), true);
			/**
			 * 定损任务插入数据�?
			 */
			CertainLossTaskAccess.insertOrUpdate(response.getCertainLossTask(), true);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode("NO");
			response.setResponseMessage("终端与快赔交互失�?" + e.getMessage());
		}
		return response;
	}

	/**
	 * 报文创建方法
	 * 
	 * @throws Exception
	 */
	private static String createTaskQueryRequest(TaskQueryRequest taskQueryRequest) throws Exception {
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
		serializer.text("MAINTASKQUERY");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户�?
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(taskQueryRequest.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(taskQueryRequest.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 系统用户�?
		serializer.startTag("", "USERCODE");
		serializer.text(taskQueryRequest.getUserCode());
		serializer.endTag("", "USERCODE");
		// 任务类型
		serializer.startTag("", "TASKTYPE");
		serializer.text(taskQueryRequest.getTaskType());
		serializer.endTag("", "TASKTYPE");
		// 任务状�?
		serializer.startTag("", "TASKSTATUS");
		serializer.text(taskQueryRequest.getTaskStatus());
		serializer.endTag("", "TASKSTATUS");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	public static TaskQueryResponse taskQueryResponseXml(String responseXml) {

		/**
		 * 用于存放响应头和响应�?
		 */
		TaskQueryResponse taskQueryResponse = new TaskQueryResponse();
		CheckTask checkTask = null;
		CertainLossTask certainLossTask = null;
		/**
		 * 响应�?
		 */
		try {

			if (responseXml == null) {
				taskQueryResponse.setResponseMessage("服务器返回数据错�?);
				taskQueryResponse.setResponseCode("NO");
			} else {
				if (responseXml.equals("Timeout")) {
					taskQueryResponse.setResponseMessage("移动端与快赔系统连接超时");
					taskQueryResponse.setResponseCode("NO");
				} else if (responseXml.equals("Post") || responseXml.equals("")) {
					taskQueryResponse.setResponseMessage("移动端与快赔连接失败");
					taskQueryResponse.setResponseCode("NO");
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
					 * 设置要解析的xml�?若果解析出现乱码,可以尝试使用 parser.setInput(InputStream
					 * inputStream, String inputEncoding)
					 */
					parser.setInput(new StringReader(responseXml));
					/**
					 * 开始解析事�?
					 */
					int eventType = parser.getEventType();
					String startTag = null;
					String endTag = null;
					String value = null;
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_DOCUMENT) {// 开�?
							eventType = parser.next();
						} else if (eventType == XmlPullParser.END_DOCUMENT) {// 结尾
							break;
						} else if (eventType == XmlPullParser.START_TAG) {// 标签�?
							startTag = parser.getName();

							if (startTag.equalsIgnoreCase("RESPONSETYPE")) {
								value = parser.nextText().trim();
								taskQueryResponse.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								value = parser.nextText().trim();
								taskQueryResponse.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								value = parser.nextText().trim();
								taskQueryResponse.setResponseMessage(value);
							}
							/**
							 * 查勘
							 */
							if (checkTask != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("REGISTNO")) {
									checkTask.setRegistno(value);
								} else if (startTag.equalsIgnoreCase("DAMAGEADDRESSXPOSITION")) {
									checkTask.setLatitude(value);
								} else if (startTag.equalsIgnoreCase("DAMAGEADDRESSYPOSITION")) {
									checkTask.setLongitude(value);
								} else if (startTag.equalsIgnoreCase("LINKERNAME")) {
									checkTask.setLinkername(value);
								} else if (startTag.equalsIgnoreCase("LINKERPHONENUMBER")) {
									checkTask.setLinkerphoneno(value);
								} else if (startTag.equalsIgnoreCase("REPORTORNAME")) {
									checkTask.setReportorname(value);
								} else if (startTag.equalsIgnoreCase("REPORTORPHONENUMBER")) {
									checkTask.setReportorphoneno(value);

								} else if (startTag.equalsIgnoreCase("INPUTEDATE")) {
									checkTask.setInputedate(value);
								} else if (startTag.equalsIgnoreCase("SURVEYTASKSTATUS")) {
									checkTask.setSurveytaskstatus(value);
								} else if (startTag.equalsIgnoreCase("APPLYCANNELSTATUS")) {
									checkTask.setApplycannelstatus(value);
								} else if (startTag.equalsIgnoreCase("SYNCHROSTATUS")) {
									checkTask.setSynchrostatus(value);
								} else if (startTag.equalsIgnoreCase("INSUREDNAME")) {
									checkTask.setInsrtedname(value);
								} else if (startTag.equalsIgnoreCase("LICENSENO")) {
									checkTask.setLicenseno(value);
								} else if(startTag.equalsIgnoreCase("TASKFINISHTIME")){
									checkTask.setTaskFinishTime(value);
								} else if(startTag.equalsIgnoreCase("SERIALNO")){
									checkTask.setSeriaNo(value);
								} 
							}
							if (startTag.equalsIgnoreCase("SURVEYTASK")) {
								checkTask = new CheckTask();
							}
							/**
							 * 定损
							 */
							if (certainLossTask != null) {
								value = parser.nextText().trim();
								if (startTag.equalsIgnoreCase("REGISTNO")) {
									certainLossTask.setRegistno(value);
								} else if (startTag.equalsIgnoreCase("ITEMNO")) {
									if (value != null && !"".equals(value)) {
										certainLossTask.setItemno(Integer.parseInt(value));
									} else {
										certainLossTask.setItemno(0);
									}
								} else if (startTag.equalsIgnoreCase("ITEMTYPE")) {
									certainLossTask.setItemtype(value);
								} else if (startTag.equalsIgnoreCase("DAMAGEADDRESSXPOSITION")) {
									certainLossTask.setLatitude(value);
								} else if (startTag.equalsIgnoreCase("DAMAGEADDRESSYPOSITION")) {
									certainLossTask.setLongitude(value);
								} else if (startTag.equalsIgnoreCase("PRINTNUMBER")) {
									if (value != null && !"".equals(value)) {
										certainLossTask.setPrintnumber(Integer.parseInt(value));
									} else {
										certainLossTask.setPrintnumber(0);
									}
								} else if (startTag.equalsIgnoreCase("LINKERNAME")) {
									certainLossTask.setLinkername(value);
								} else if (startTag.equalsIgnoreCase("LINKERPHONENUMBER")) {
									certainLossTask.setLinkerphoneno(value);
								} else if (startTag.equalsIgnoreCase("REPORTORNAME")) {
									certainLossTask.setReportorname(value);
								} else if (startTag.equalsIgnoreCase("REPORTORPHONENUMBER")) {
									certainLossTask.setReportorphoneno(value);
								} else if (startTag.equalsIgnoreCase("INPUTEDATE")) {
									certainLossTask.setInputedate(value);
								} else if (startTag.equalsIgnoreCase("SURVEYTASKSTATUS")) {
									certainLossTask.setSurveytaskstatus(value);
								} else if (startTag.equalsIgnoreCase("VERIFYPASSFLAG")) {
									certainLossTask.setVerifypassflag(value);
								} else if (startTag.equalsIgnoreCase("APPLYCANNELSTATUS")) {
									certainLossTask.setApplycannelstatus(value);
								} else if (startTag.equalsIgnoreCase("SYNCHROSTATUS")) {
									certainLossTask.setSynchrostatus(value);
								} else if (startTag.equalsIgnoreCase("INSUREDNAME")) {
									certainLossTask.setInsrtedname(value);
								} else if (startTag.equalsIgnoreCase("LICENSENO")) {
									certainLossTask.setLicenseno(value);
								}else if(startTag.equalsIgnoreCase("TASKFINISHTIME")){
									certainLossTask.setTaskFinishTime(value);
								} else if(startTag.equalsIgnoreCase("SERIALNO")){
									certainLossTask.setSeriaNo(value);
								}
							}
							if (startTag.equalsIgnoreCase("SETLOSSTASK")) {
								certainLossTask = new CertainLossTask();
							}
							eventType = parser.next();

						} else if (eventType == XmlPullParser.END_TAG) {
							endTag = parser.getName();
							if (endTag.equalsIgnoreCase("SURVEYTASK")) {
								taskQueryResponse.getCheckTask().add(checkTask);
								checkTask = null;
							} else if (endTag.equalsIgnoreCase("SETLOSSTASK")) {
								taskQueryResponse.getCertainLossTask().add(certainLossTask);
								certainLossTask = null;
							}
							eventType = parser.next();
						} else {
							eventType = parser.next();
						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			taskQueryResponse.setResponseCode("NO");
			taskQueryResponse.setResponseMessage("解析异常�?终端)");
		}
		return taskQueryResponse;
	}
}
