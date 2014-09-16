package com.sinosoftyingda.fastclaim.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史赔案信息返回类
 * @author haoyun 20130225
 *
 */
public class HistoricalClaimResponse extends CommonResponse {
	
	private String historicalAccident;//历史出险次数
	private String historicalClaimsNumber;//历史赔款次数
	private String historicalClaimsSum;//历史赔款总计
	private List<HistoricalClaimItem> historicalClaims=new ArrayList<HistoricalClaimItem>();//理赔案件信息明细

	public HistoricalClaimResponse() {
		super();
	}

	public HistoricalClaimResponse(String historicalAccident,
			String historicalClaimsNumber, String historicalClaimsSum,
			List<HistoricalClaimItem> historicalClaims) {
		super();
		this.historicalAccident = historicalAccident;
		this.historicalClaimsNumber = historicalClaimsNumber;
		this.historicalClaimsSum = historicalClaimsSum;
		this.historicalClaims = historicalClaims;
	}

	public String getHistoricalAccident() {
		return historicalAccident;
	}

	public void setHistoricalAccident(String historicalAccident) {
		this.historicalAccident = historicalAccident;
	}

	public String getHistoricalClaimsNumber() {
		return historicalClaimsNumber;
	}

	public void setHistoricalClaimsNumber(String historicalClaimsNumber) {
		this.historicalClaimsNumber = historicalClaimsNumber;
	}

	public String getHistoricalClaimsSum() {
		return historicalClaimsSum;
	}

	public void setHistoricalClaimsSum(String historicalClaimsSum) {
		this.historicalClaimsSum = historicalClaimsSum;
	}

	public List<HistoricalClaimItem> getHistoricalClaims() {
		return historicalClaims;
	}

	public void setHistoricalClaims(List<HistoricalClaimItem> historicalClaims) {
		this.historicalClaims = historicalClaims;
	}
}
