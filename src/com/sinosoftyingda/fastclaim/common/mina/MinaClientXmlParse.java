package com.sinosoftyingda.fastclaim.common.mina;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.mina.model.Response;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.service.TaskQueryHttpService;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.notice.page.NoticeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

public class MinaClientXmlParse {

	private static int newtasknum = 0;
	private static int count = 0;// 记录推送过来的非新任务的任务数

	/**
	 * 处理交互报文
	 * 
	 * @param session
	 * @param context
	 * @param xml
	 */
	public static void handle(IoSession session, Context context, String xml) {
		if (xml == null || xml.equals("") || xml.equals("<pulse/>")) {// 脉冲
			return;
		}
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			String startTag = null;
			String type = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					eventType = parser.next();
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					break;
				} else if (eventType == XmlPullParser.START_TAG) {
					startTag = parser.getName();
					if (startTag.equals("PACKET")) {
						int count = parser.getAttributeCount();
						for (int i = 0; i < count; i++) {
							String name = parser.getAttributeName(i);
							if (name.equals("type")) {
								type = parser.getAttributeValue(i);
								if (type.equals("REQUEST")) {
									handleRequest(session, context, xml);
								} else if (type.equals("RESPONSE")) {
									handleResponse(context, xml);
								}
								break;
							}
						}
					}
					eventType = parser.next();
				} else if (eventType == XmlPullParser.END_TAG) {
					eventType = parser.next();
				} else {
					eventType = parser.next();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理响应报文
	 * 
	 * @param context
	 * @param xml
	 */
	private static void handleResponse(Context context, String xml) {
		Response response = new Response();
		if (xml != null && !xml.equals("")) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(new StringReader(xml));
				int eventType = parser.getEventType();
				String startTag = null;
				String value = null;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_DOCUMENT) {
						break;
					} else if (eventType == XmlPullParser.START_TAG) {
						startTag = parser.getName();
						if (startTag.equals("RESPONSETYPE")) {
							value = parser.nextText().trim();
							response.setType(value);
						} else if (startTag.equals("RESPONSECODE")) {
							value = parser.nextText().trim();
							response.setCode(value);
						} else if (startTag.equals("RESPONSEMESSAGE")) {
							value = parser.nextText().trim();
							response.setMsg(value);
						}
						eventType = parser.next();
					} else if (eventType == XmlPullParser.END_TAG) {
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setType("");
				response.setCode("NO");
				response.setMsg("上线响应报文解析异常");
			}
		}

		MinaClient client = ConnectionManager.getMinaClient(context);
		if ("YES".equals(response.getCode())) {
			client.setOnline(true);
		} else {
			client.setOnline(false);
		}
	}

	/**
	 * 处理请求报文
	 * 
	 * @param session
	 * @param context
	 * @param xml
	 */
	private static void handleRequest(IoSession session, Context context, String xml) {
		String response = null;
		List<CheckTask> checks = new ArrayList<CheckTask>();
		List<CertainLossTask> certainLossTasks = new ArrayList<CertainLossTask>();
		try {
			TaskQueryResponse tqr = TaskQueryHttpService.taskQueryResponseXml(xml);
			if ("NO".equals(tqr.getResponseCode())) {
				response = response("MAINTASKPUSH", "NO", "任务/信息推送失败，手机端获取任务信息出现异常");
			} else {
				checks = tqr.getCheckTask();
				certainLossTasks = tqr.getCertainLossTask();
				if (checks.size() >= 1) {
					CheckTaskAccess.insertOrUpdate(checks, false);
				}
				if (certainLossTasks.size() >= 1) {
					CertainLossTaskAccess.insertOrUpdate(certainLossTasks, false);
				}
				response = response("MAINTASKPUSH", "YES", "Success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = response("MAINTASKPUSH", "NO", "任务/信息推送失败，手机端获取任务信息出现异常");
		}
		session.write(response);
		try {
			notice(context, checks, certainLossTasks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// String logId, List<CheckTask> cts, List<CertainLossTask> clts
	private static String response(String type, String code, String msg) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strBuffer.append("<PACKET type=\"RESPONSE\" version=\"1.0\">");
		strBuffer.append("<HEAD>");
		strBuffer.append("<RESPONSETYPE>" + type + "</RESPONSETYPE>");
		strBuffer.append("<RESPONSECODE>" + code + "</RESPONSECODE>");
		strBuffer.append("<RESPONSEMESSAGE>" + msg + "</RESPONSEMESSAGE>");
		strBuffer.append("</HEAD>");
		strBuffer.append("<BODY>");
		strBuffer.append("</BODY>");
		strBuffer.append("</PACKET>");
		return strBuffer.toString();
	}

	private static void notice(Context context, List<CheckTask> cts, List<CertainLossTask> clts) {
		newtasknum = 0;
		count = 0;
		
		Intent intent = new Intent();
		intent.setClass(context, NoticeActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		if (cts != null && cts.size() >= 1) {
			for (int i = 0; i < cts.size(); i++) {
				if (cts.get(i).getSurveytaskstatus().equals("1") && cts.get(i).getApplycannelstatus().equals("")) {// 新任务到达
					newtasknum++;
				} else if (cts.get(i).getSurveytaskstatus().equals("1") && cts.get(i).getApplycannelstatus().equals("success")) {// 改派成功
					count++;
					DBUtils.delete("CheckTask", "registNo=?", new String[] { cts.get(i).getRegistno() });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, "查勘改派结果到达", -1, intent);
				} else if (cts.get(i).getSurveytaskstatus().equals("4")) {//服务端提交
					count++;
					ContentValues values = new ContentValues();
					values.put("completetime", DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
					DBUtils.update("CheckTask", values, "registNo=?", new String[] { cts.get(i).getRegistno() });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, "查勘任务完成结果到达", -1, intent);
				} else {
					count++;
				}
			}
		}

		if (clts != null && clts.size() >= 1) {
			for (int i = 0; i < clts.size(); i++) {
				if (clts.get(i).getSurveytaskstatus().equals("1") && clts.get(i).getApplycannelstatus().equals("")) {// 新任务到达
					newtasknum++;
				} else if (clts.get(i).getSurveytaskstatus().equals("1") && clts.get(i).getApplycannelstatus().equals("success")) {// 改派成功
					count++;
					DBUtils.delete("CheckTask", "registNo=? and itemno=?", new String[] { clts.get(i).getRegistno(), clts.get(i).getItemno() + "" });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, "定损改派结果到达", -1, intent);
				}else if (clts.get(i).getSurveytaskstatus().equals("4")) {// 服务端提交
					count++;
					ContentValues values = new ContentValues();
					values.put("completetime", DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
					DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[] { clts.get(i).getRegistno(),clts.get(i).getItemno() + "" });
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, "定损任务完成结果到达", -1, intent);
				} else if(clts.get(i).getSurveytaskstatus().equals("5")){//核损打回
					count++;
					NoticeManager.getInstance(context).notice(NoticeManager.TAG_RESULT, "定损任务核损结果到达", -1, intent);
				}else {
					count++;
				}
			}
		}
		
		if (newtasknum >= 1) {
			NoticeManager.getInstance(context).notice(NoticeManager.TAG_NEWTASK, "新任务(" + newtasknum + ")", R.raw.newtask, intent);
		}

	}

}
