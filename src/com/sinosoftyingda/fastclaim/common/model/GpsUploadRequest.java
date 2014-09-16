package com.sinosoftyingda.fastclaim.common.model;
/**
 * 位置实时上传接口请求类
 * @author haoyun 20130226
 *
 */
public class GpsUploadRequest extends BasicRequest {
	private String xCoordinateDegree;//当前位置X坐标
	private String yCoordinateDegree;//当前位置Y坐标
	private String speed;//当前时速
	private String directionsdegree;//当前方向
	private String IMEI;//终端唯一标示
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public GpsUploadRequest() {
		super();
	}
	public GpsUploadRequest(String xCoordinateDegree, String yCoordinateDegree,
			String speed, String directionsdegree) {
		super();
		this.xCoordinateDegree = xCoordinateDegree;
		this.yCoordinateDegree = yCoordinateDegree;
		this.speed = speed;
		this.directionsdegree = directionsdegree;
	}
	public String getxCoordinateDegree() {
		return xCoordinateDegree;
	}
	public void setxCoordinateDegree(String xCoordinateDegree) {
		this.xCoordinateDegree = xCoordinateDegree;
	}
	public String getyCoordinateDegree() {
		return yCoordinateDegree;
	}
	public void setyCoordinateDegree(String yCoordinateDegree) {
		this.yCoordinateDegree = yCoordinateDegree;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getDirectionsdegree() {
		return directionsdegree;
	}
	public void setDirectionsdegree(String directionsdegree) {
		this.directionsdegree = directionsdegree;
	}
	
}
