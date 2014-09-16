package com.sinosoftyingda.fastclaim.common.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.location.Location;

public class MinaClient {

	public static final long TIMEOUT = 30 * 1000;

	public static final int BOTH_IDLE_TIME = 30;// 空闲时间

	public static final int MAX_LINE_LENGTH = 2 * 1024 * 1024;

	private NioSocketConnector connector;

	private ConnectFuture future;

	private IoSession session;

	private boolean online;

	private SocketSessionConfig sessionConfig;

	private Context context;

	public MinaClient(Context context) {
		this.context = context;
	}

	public void connect(String host, int port) {
		init(context);
		InetSocketAddress address = new InetSocketAddress(host, port);
		future = connector.connect(address);
		if (future != null) {
			future.awaitUninterruptibly();
			session = future.getSession();
		}
	}

	/**
	 * 初始化一些数据，如session
	 */
	private void init(Context context) {
		connector = new NioSocketConnector();
		connector.setHandler(new MinaClientIOHandler(context));
		TextLineCodecFactory tlcf = new TextLineCodecFactory(Charset.forName("UTF-8"));
		tlcf.setDecoderMaxLineLength(MAX_LINE_LENGTH);
		tlcf.setEncoderMaxLineLength(MAX_LINE_LENGTH);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(tlcf));
		connector.setConnectTimeoutMillis(TIMEOUT);
		sessionConfig = connector.getSessionConfig();
		sessionConfig.setUseReadOperation(true);
		// sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, BOTH_IDLE_TIME);
		online = false;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isConnected() {
		return session != null && session.isConnected();
	}

	/**
	 * 发送一个xml报文的String对象
	 * 
	 * @param obj
	 */
	public String sendMsg(Object obj) {
		String result = null;
		try {
			if (session != null && session.isConnected()) {
				session.write(obj).awaitUninterruptibly();
				// ReadFuture future = session.read();
				// future.awaitUninterruptibly();
				// result = future.getMessage().toString().trim();
				//
				// XmlPullParserFactory factory =
				// XmlPullParserFactory.newInstance();
				// XmlPullParser parser = factory.newPullParser();
				// parser.setInput(new StringReader(result));
				// int eventType = parser.getEventType();
				// String startTag = null;
				// String type = null;
				// while (eventType != XmlPullParser.END_DOCUMENT) {
				// if (eventType == XmlPullParser.START_DOCUMENT) {
				// eventType = parser.next();
				// } else if (eventType == XmlPullParser.END_DOCUMENT) {
				// break;
				// } else if (eventType == XmlPullParser.START_TAG) {
				// startTag = parser.getName();
				// if (startTag.equals("PACKET")) {
				// int count = parser.getAttributeCount();
				// for (int i = 0; i < count; i++) {
				// String name = parser.getAttributeName(i);
				// if (name.equals("type")) {
				// type = parser.getAttributeValue(i);
				// break;
				// }
				// }
				// }
				// eventType = parser.next();
				// } else if (eventType == XmlPullParser.END_TAG) {
				// eventType = parser.next();
				// } else {
				// eventType = parser.next();
				// }
				// }
				// if(type.equals("REQUEST")){
				// future = session.read();
				// future.awaitUninterruptibly();
				// result = future.getMessage().toString().trim();
				// }
			} else {
				result = response("", "0", "连接不存在，无法发送信息");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = response("", "0", "发送信息时出现异常");
		}
		return result;
	}

	public void close() {
		if (session != null) {
			session.close(true);
			session = null;
		}
		if (connector != null) {
			connector.dispose();
			connector = null;
		}
	}

	private static String response(String type, String code, String msg) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strBuffer.append("<PACKET type=\"RESPONSE\" version=\"1.0\">");
		strBuffer.append("<HEAD>");
		strBuffer.append("<RESPONSETYPE>" + type + "</RESPONSETYPE>");
		strBuffer.append("<RESPONSECODE>" + code + "</RESPONSECODE>");
		strBuffer.append("<RESPONSEMESSAGE>" + msg + "</RESPONSEMESSAGE>");
		strBuffer.append("</HEAD>");
		strBuffer.append("<BODY/>");
		strBuffer.append("</PACKET>");
		return strBuffer.toString();
	}

	public String createOfflineRequest(String usercode) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strBuffer.append("<PACKET type=\"REQUEST\" version=\"1.0\">");
		strBuffer.append("<HEAD>");
		strBuffer.append("<REQUESTTYPE>OFFLINE</REQUESTTYPE>");
		strBuffer.append("</HEAD>");
		strBuffer.append("<BODY>");
		strBuffer.append("<USERCODE>" + usercode + "</USERCODE>");
		strBuffer.append("</BODY>");
		strBuffer.append("</PACKET>");
		return strBuffer.toString();
	}

	public String createOnlineRequest(String usercode) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strBuffer.append("<PACKET type=\"REQUEST\" version=\"1.0\">");
		strBuffer.append("<HEAD>");
		strBuffer.append("<REQUESTTYPE>ONLINE</REQUESTTYPE>");
		strBuffer.append("</HEAD>");
		strBuffer.append("<BODY>");
		strBuffer.append("<USERCODE>" + usercode + "</USERCODE>");
		strBuffer.append("</BODY>");
		strBuffer.append("</PACKET>");
		return strBuffer.toString();
	}

	public String createUploadLocationRequest(String usercode, String password, String deviceId, Location location) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strBuffer.append("<PACKET type=\"REQUEST\" version=\"1.0\">");
		strBuffer.append("<HEAD>");
		strBuffer.append("<REQUESTTYPE>GPSUPLOAD</REQUESTTYPE>");
		strBuffer.append("<INTERFACEUSERCODE>" + usercode + "</INTERFACEUSERCODE>");
		strBuffer.append("<INTERFACEPASSWORD>" + password + "</INTERFACEPASSWORD>");
		strBuffer.append("<IMEI>" + deviceId + "</IMEI>");
		strBuffer.append("</HEAD>");
		strBuffer.append("<BODY>");
		strBuffer.append("<USERCODE>" + usercode + "</USERCODE>");
		if (location != null) {
			strBuffer.append("<XCOORDINATEDEGREE>" + location.getLatitude() + "</XCOORDINATEDEGREE>");
			strBuffer.append("<YCOORDINATEDEGREE>" + location.getLongitude() + "</YCOORDINATEDEGREE>");
			strBuffer.append("<SPEED>" + location.getSpeed() + "</SPEED>");
			strBuffer.append("<DIRECTIONSDEGREE>" + location.getBearing() + "</DIRECTIONSDEGREE>");
		} else {
			strBuffer.append("<XCOORDINATEDEGREE></XCOORDINATEDEGREE>");
			strBuffer.append("<YCOORDINATEDEGREE></YCOORDINATEDEGREE>");
			strBuffer.append("<SPEED></SPEED>");
			strBuffer.append("<DIRECTIONSDEGREE></DIRECTIONSDEGREE>");
		}
		strBuffer.append("</BODY>");
		strBuffer.append("</PACKET>");
		return strBuffer.toString();
	}

}
