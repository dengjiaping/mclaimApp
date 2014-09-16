package com.sinosoftyingda.fastclaim.common.model;
/**
 * 到达现场请求类
 * @author haoyun 20130225
 *
 */
public class ArriveSceneRequest extends BasicRequest {
	
	private String registNo;//报案号
	private String flag;//区分查勘或定损任务标志
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public ArriveSceneRequest(String registNo, String flag) {
		super();
		this.registNo = registNo;
		this.flag = flag;
	}
	public ArriveSceneRequest() {
		super();
	}

	
	
}
