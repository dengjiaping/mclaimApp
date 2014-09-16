package com.sinosoftyingda.fastclaim.common.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUserInfo;
import com.sinosoftyingda.fastclaim.common.model.LoginRequest;
import com.sinosoftyingda.fastclaim.common.model.LoginResponse;
import com.sinosoftyingda.fastclaim.common.model.LoginUserInfo;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.HttpUtils;

/**
 * 登录接口
 * 
 * @author haoyun 20130222
 * 
 */
public class LoginHttpService {
	/**
	 * 登录接口方法
	 * 
	 * @param requestLogin
	 * @return
	 */
	public static LoginResponse loginService(LoginRequest requestLogin, String url, Context context) {
		LoginResponse responseLogin = new LoginResponse();
		try {
			String requestXML = createLoginRequest(requestLogin);
			Log.i("mCliam", "登录------------->requestXML:" + requestXML);
			Log.i("mCliam", "登录------------->url:" + url);

			HttpUtils httpUtils = new HttpUtils();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("content", requestXML);
			String responseXML = httpUtils.doPost(url, params);			
			Log.i("mCliam", "登录------------->responseXML:" + responseXML);
			responseLogin = loginResponseXml(responseXML);
			/**
			 * 判断登陆是否成功如果成功，并且判断用户名是否是之前用户名如果不是则保存当前用户名，并且删除数据库
			 */
			LoginUserInfo userInfo = new LoginUserInfo();
			userInfo.setUserName(requestLogin.getUserCode());
			userInfo.setPassWord(requestLogin.getPassWord());
			if ("YES".equals(responseLogin.getResponseCode()))
				if (!TblUserInfo.isExist(userInfo.getUserName())) {
					FileUtils.deleteFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +context.getString(R.string.DBPath)+context.getString(R.string.DBName));
					SystemConfig.dbhelp = new FastClaimDbHelper(context);
					try {
						new FastClaimDbHelper(context).createDataBase();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TblUserInfo.insertOrUpdate(userInfo);
				}
			/**
			 * 保存请求报文
			 */
//			InfoBuffer infoBuffer = new InfoBuffer();
//			infoBuffer.setType("TMLOGIN");
//			infoBuffer.setXmlContent(requestXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
			/**
			 * 保存响应报文
			 */
//			infoBuffer.setType("TMLOGINBACK");
//			infoBuffer.setXmlContent(responseXML);
//			TblInfoBuffer.addinfobuffer(infoBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			responseLogin.setResponseCode("NO");
			responseLogin.setResponseMessage("终端与快赔交互失败:" + e.getMessage());
		}
		return responseLogin;
	}

	/**
	 * 登陆报文创建方法
	 * 
	 * @param requestLogin
	 * @return
	 * @throws Exception
	 */
	private static String createLoginRequest(LoginRequest requestLogin) throws Exception {
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
		serializer.text("TMLOGIN");
		serializer.endTag("", "REQUESTTYPE");
		// 接口用户名
		serializer.startTag("", "INTERFACEUSERCODE");
		serializer.text(requestLogin.getInterfaceUserCode());
		serializer.endTag("", "INTERFACEUSERCODE");
		// 接口密码
		serializer.startTag("", "INTERFACEPASSWORD");
		serializer.text(requestLogin.getInterfacePassWord());
		serializer.endTag("", "INTERFACEPASSWORD");

		// 终端唯一标识号
		serializer.startTag("", "IMEI");
		serializer.text(requestLogin.getIMEI());
		serializer.endTag("", "IMEI");
		serializer.endTag("", "HEAD");
		/**
		 * BODY
		 */
		serializer.startTag("", "BODY");
		// 系统用户名
		serializer.startTag("", "USERCODE");
		serializer.text(requestLogin.getUserCode());
		serializer.endTag("", "USERCODE");
		// 系统密码
		serializer.startTag("", "PASSWORD");
		serializer.text(requestLogin.getPassWord());
		serializer.endTag("", "PASSWORD");

		serializer.endTag("", "BODY");
		serializer.endTag("", "PACKET");
		serializer.endDocument();
		return writer.toString();
	}

	/**
	 * 响应xml进行解析
	 * 
	 */
	private static LoginResponse loginResponseXml(String responseXml) throws Exception {

		/**
		 * 用于存放响应头和响应体
		 */
		HashMap<String, Object> response = new HashMap<String, Object>();
		LoginResponse responseLogin = new LoginResponse();
		/**
		 * 响应头
		 */
		if (responseXml == null) {
			responseLogin.setResponseCode("NO");
			responseLogin.setResponseMessage("登录请求无响应，返回null！");
		} else {
			if (responseXml.equals("Timeout")) {
				responseLogin.setResponseCode("NO");
				responseLogin.setResponseMessage("系统连接超时，请检查网络后重新登录!");
			} else if (responseXml.equals("Post") || responseXml.equals("")) {
				responseLogin.setResponseCode("NO");
				responseLogin.setResponseMessage("系统连接失败，请检查网络连接！");
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
								responseLogin.setReaponseType(value);
							} else if (startTag.equalsIgnoreCase("RESPONSECODE")) {
								responseLogin.setResponseCode(value);
							} else if (startTag.equalsIgnoreCase("RESPONSEMESSAGE")) {
								responseLogin.setResponseMessage(value);
							} else if (startTag.equalsIgnoreCase("DUTYFLAG")) {
								responseLogin.setDutyFlag(value);
							} else if (startTag.equalsIgnoreCase("USERNAME")) {
								responseLogin.setUserName(value);
							} else if (startTag.equalsIgnoreCase("ROLE")) {
								responseLogin.setRole(value);
							} else if (startTag.equalsIgnoreCase("ISXIEPEI")) {
								responseLogin.setIsXiepei(value);
							} else if (startTag.equalsIgnoreCase("USERCOMCODE")) {
								responseLogin.setUserComCode(value);
							} else if (startTag.equalsIgnoreCase("TUOBAOUSERCODE")) {
								responseLogin.setTuoBaoFtpUserCode(value);//拓保用户名
							} else if (startTag.equalsIgnoreCase("TUOBAOPWD")) {
								responseLogin.setTuoBaoFtpPassWord(value);//拓保密码
							} else if (startTag.equalsIgnoreCase("TUOBAOURL")) {
								responseLogin.setTuoBaoFtpUrl(value);//拓保ip
							} else if (startTag.equalsIgnoreCase("TUOBAOPORT")) {
								responseLogin.setTuoBaoFtpPort(value);//拓保端口
							} else if (startTag.equalsIgnoreCase("USERCOMNAME")) {
								responseLogin.setUserComName(value);
							} else if (startTag.equalsIgnoreCase("CONSULTTASKTIME")) {
								responseLogin.setConsultTaskTime(value);//查阅任务时间
							} else if (startTag.equalsIgnoreCase("CONTACTCLIENTTIME")) {
								responseLogin.setContactclientTime(value);//成功联系客户时间
							} else if (startTag.equalsIgnoreCase("UPLOADCOORDINATETIME")) {
								responseLogin.setUploadCoordinateTime(value);//gps上传坐标时间
							} else if (startTag.equalsIgnoreCase("DEVICEID")) {
								responseLogin.setDeviceId(value);//视频ID
							} else if (startTag.equalsIgnoreCase("HIKSERVICEIP")) {
								responseLogin.setHikserViceIp(value);//海康服务器ID
							} else if (startTag.equalsIgnoreCase("HIKSERVICEPORT")) {
								responseLogin.setHikserVicePort(value);//海康服务器端口
							} else if (startTag.equalsIgnoreCase("MINAIP")) {
								responseLogin.setMinaIp(value);//MINAID
							} else if (startTag.equalsIgnoreCase("MINAPORT")) {
								responseLogin.setMinaPort(value);//MINA端口
							} else if (startTag.equalsIgnoreCase("SERVERTIME")) {
								responseLogin.setServerTime(value);//MINA端口
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

		return responseLogin;
	}
}
