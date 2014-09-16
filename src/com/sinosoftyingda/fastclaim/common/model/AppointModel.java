package com.sinosoftyingda.fastclaim.common.model;

/**
 * 特别约定
 * 
 * @author DengGuang
 */
public class AppointModel {
	/** 序号 */
	private String serialNo;
	
	/** 特约代码 */
	private String clauseCode;
	
	/** 特约名称 */
	private String clause;
	
	/** 详细信息 */
	private String clauseDetall;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getClauseCode() {
		return clauseCode;
	}

	public void setClauseCode(String clauseCode) {
		this.clauseCode = clauseCode;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getClauseDetall() {
		return clauseDetall;
	}

	public void setClauseDetall(String clauseDetall) {
		this.clauseDetall = clauseDetall;
	}
}
