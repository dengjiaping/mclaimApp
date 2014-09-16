package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流查看接口返回类
 * @author haoyun 20130302
 *
 */
public class WorkflowlogViewResponse extends CommonResponse{
	/**
	 * 调度	
	 */
	private Schedule schedule;
	/**
	 * 查勘	
	 */
	private Check check;//查勘
		/**
		 * 定损
		 */
		private List<CertainLoss> certainLossList=new ArrayList<CertainLoss>();
		/**
		 * 核价
		 */
		private List<VerifyPrice> verifyPriceList=new ArrayList<VerifyPrice>();
		/**
		 * 核损
		 */
		private List<VerifyLoss> verifyLossList=new ArrayList<VerifyLoss>();
		public Schedule getSchedule() {
			return schedule;
		}
		public void setSchedule(Schedule schedule) {
			this.schedule = schedule;
		}
		public Check getCheck() {
			return check;
		}
		public void setCheck(Check check) {
			this.check = check;
		}
		public List<CertainLoss> getCertainLossList() {
			return certainLossList;
		}
		public void setCertainLossList(List<CertainLoss> certainLossList) {
			this.certainLossList = certainLossList;
		}
		public List<VerifyPrice> getVerifyPriceList() {
			return verifyPriceList;
		}
		public void setVerifyPriceList(List<VerifyPrice> verifyPriceList) {
			this.verifyPriceList = verifyPriceList;
		}
		public List<VerifyLoss> getVerifyLossList() {
			return verifyLossList;
		}
		public void setVerifyLossList(List<VerifyLoss> verifyLossList) {
			this.verifyLossList = verifyLossList;
		}
		public WorkflowlogViewResponse(String reaponseType,
				String responseCode, String responseMessage, Schedule schedule,
				Check check, List<CertainLoss> certainLossList,
				List<VerifyPrice> verifyPriceList,
				List<VerifyLoss> verifyLossList) {
			super(reaponseType, responseCode, responseMessage);
			this.schedule = schedule;
			this.check = check;
			this.certainLossList = certainLossList;
			this.verifyPriceList = verifyPriceList;
			this.verifyLossList = verifyLossList;
		}
		public WorkflowlogViewResponse() {
			super();
		}

}
