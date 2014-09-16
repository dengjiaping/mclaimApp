package com.sinosoftyingda.fastclaim.survey.model;

import java.io.Serializable;


/**
 * SpointsView所需参数类
 * @author Administrator
 *
 */
public class SPointsViewParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean firstScene=true;//第一现场
	private String occurrenceReason="";//出险原因代码
	private String occurrenceName="";//出现原因名称
	private String accidentResponsibility="";//事故责任
	private String claimCaseProperties="";//赔案性质
	private String subrogation="";//代位求偿
	private String mutualTouchSince="";//是否通赔
	private String riskTip="0";//风险提示
	private Boolean trafficAccidentBook=true;//交管事故书
	private Boolean roadTrafficAccident=true;//道路交通事故
	private Boolean isBaijing=true;//是否是北京
	
	public String getOccurrenceName() {
		return occurrenceName;
	}
	public void setOccurrenceName(String occurrenceName) {
		this.occurrenceName = occurrenceName;
	}
	public Boolean getFirstScene() {
		return firstScene;
	}
	public void setFirstScene(Boolean firstScene) {
		this.firstScene = firstScene;
	}
	public String getOccurrenceReason() {
		return occurrenceReason;
	}
	public void setOccurrenceReason(String occurrenceReason) {
		this.occurrenceReason = occurrenceReason;
	}
	public String getAccidentResponsibility() {
		return accidentResponsibility;
	}
	public void setAccidentResponsibility(String accidentResponsibility) {
		this.accidentResponsibility = accidentResponsibility;
	}
	public String getClaimCaseProperties() {
		return claimCaseProperties;
	}
	public void setClaimCaseProperties(String claimCaseProperties) {
		this.claimCaseProperties = claimCaseProperties;
	}
	public String getSubrogation() {
		return subrogation;
	}
	public void setSubrogation(String subrogation) {
		this.subrogation = subrogation;
	}
	public String getMutualTouchSince() {
		return mutualTouchSince;
	}
	public void setMutualTouchSince(String mutualTouchSince) {
		this.mutualTouchSince = mutualTouchSince;
	}
	public String getRiskTip() {
		return riskTip;
	}
	public void setRiskTip(String riskTip) {
		this.riskTip = riskTip;
	}
	public Boolean getTrafficAccidentBook() {
		return trafficAccidentBook;
	}
	public void setTrafficAccidentBook(Boolean trafficAccidentBook) {
		this.trafficAccidentBook = trafficAccidentBook;
	}
	public Boolean getRoadTrafficAccident() {
		return roadTrafficAccident;
	}
	public void setRoadTrafficAccident(Boolean roadTrafficAccident) {
		this.roadTrafficAccident = roadTrafficAccident;
	}
	public Boolean getIsBaijing() {
		return isBaijing;
	}
	public void setIsBaijing(Boolean isBaijing) {
		this.isBaijing = isBaijing;
	}
	public SPointsViewParam(Boolean firstScene, String occurrenceReason,
			String accidentResponsibility, String claimCaseProperties,
			String subrogation, String mutualTouchSince, String riskTip,
			Boolean trafficAccidentBook, Boolean roadTrafficAccident,
			Boolean isBaijing) {
		super();
		this.firstScene = firstScene;
		this.occurrenceReason = occurrenceReason;
		this.accidentResponsibility = accidentResponsibility;
		this.claimCaseProperties = claimCaseProperties;
		this.subrogation = subrogation;
		this.mutualTouchSince = mutualTouchSince;
		this.riskTip = riskTip;
		this.trafficAccidentBook = trafficAccidentBook;
		this.roadTrafficAccident = roadTrafficAccident;
		this.isBaijing = isBaijing;
	}
	public SPointsViewParam() {
		super();
	}
	
	
	
}
