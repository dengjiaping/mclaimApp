package com.sinosoftyingda.fastclaim.common.model;

/**
 * xml报文类
 * 
 * @author haoyun 20130226
 * 
 */
public class InfoBuffer {

	public static String t_ID = "id";
	public static String t_TYPE = "type";
	public static String t_DATETIME = "dateTime";
	public static String t_REGISTNO="registNo";
	public static String t_INFOBUFFERID = "infobufferId";
	public static String t_XMLCONTENT = "xmlContent";

	public String getInfoBufferId() {
		return infoBufferId;
	}

	public void setInfoBufferId(String infoBufferId) {
		this.infoBufferId = infoBufferId;
	}

	public String getXmlContent() {
		return xmlContent;

	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public InfoBuffer() {
		super();
	}

	public InfoBuffer(String id, String type, String dateTime, String infoBufferId, String xmlContent) {
		super();
		this.id = id;
		this.type = type;
		this.dateTime = dateTime;
		this.infoBufferId = infoBufferId;
		this.xmlContent = xmlContent;
	}

	private String id;
	private String type;
	private String dateTime;
	private String infoBufferId;
	private String registNo;
	private String xmlContent;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
}
