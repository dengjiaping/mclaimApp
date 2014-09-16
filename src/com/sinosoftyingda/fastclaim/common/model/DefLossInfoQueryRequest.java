package com.sinosoftyingda.fastclaim.common.model;
/**
 * 定损查询详细接口请求类
 * @author haoyun 20130308
 *
 */
public class DefLossInfoQueryRequest extends BasicRequest {
		private String registNo;//报案号
		private String deflossTaskStatus;//定损状态
		private String lossNo;//损失项目编号
		public String getRegistNo() {
			return registNo;
		}
		public void setRegistNo(String registNo) {
			this.registNo = registNo;
		}
		public String getDeflossTaskStatus() {
			return deflossTaskStatus;
		}
		public void setDeflossTaskStatus(String deflossTaskStatus) {
			this.deflossTaskStatus = deflossTaskStatus;
		}
		public String getLossNo() {
			return lossNo;
		}
		public void setLossNo(String lossNo) {
			this.lossNo = lossNo;
		}
		public DefLossInfoQueryRequest(String registNo,
				String deflossTaskStatus, String lossNo) {
			super();
			this.registNo = registNo;
			this.deflossTaskStatus = deflossTaskStatus;
			this.lossNo = lossNo;
		}
		public DefLossInfoQueryRequest() {
			super();
		}
		
}
