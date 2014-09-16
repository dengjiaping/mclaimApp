package com.sinosoftyingda.fastclaim.common.model;
/**
 * 定损详细  查勘要点
 * @author haoyun 20130308
 *
 */
public class SurveyKeyPoint {
	private String firstsiteFlag="";//是否第一现场
	private String damageCode="";//出险原因
	private String damageName="";//出险说明
	private String indemnityDuty="";//事故责任
	private String claimType="";//赔案性质
	private String isCommonClaim="";//是否通赔
	private String subrogateType="";//是否代为
	private String accidentBook="";//有无交管事故书
	private String accident="";//是否交通事故
	private String remark="";//其他
	private String checkReport="";//查看报告
	public String getFirstsiteFlag() {
		return firstsiteFlag;
	}
	public void setFirstsiteFlag(String firstsiteFlag) {
		this.firstsiteFlag = firstsiteFlag;
	}
	public String getDamageCode() {
		return damageCode;
	}
	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}
	public String getDamageName() {
		return damageName;
	}
	public void setDamageName(String damageName) {
		this.damageName = damageName;
	}
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getIsCommonClaim() {
		return isCommonClaim;
	}
	public void setIsCommonClaim(String isCommonClaim) {
		this.isCommonClaim = isCommonClaim;
	}
	public String getSubrogateType() {
		return subrogateType;
	}
	public void setSubrogateType(String subrogateType) {
		this.subrogateType = subrogateType;
	}
	public String getAccidentBook() {
		return accidentBook;
	}
	public void setAccidentBook(String accidentBook) {
		this.accidentBook = accidentBook;
	}
	public String getAccident() {
		return accident;
	}
	public void setAccident(String accident) {
		this.accident = accident;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckReport() {
		return checkReport;
	}
	public void setCheckReport(String checkReport) {
		this.checkReport = checkReport;
	}
	public SurveyKeyPoint(String firstsiteFlag, String damageCode,
			String damageName, String indemnityDuty, String claimType,
			String isCommonClaim, String subrogateType, String accidentBook,
			String accident, String remark, String checkReport) {
		super();
		this.firstsiteFlag = firstsiteFlag;
		this.damageCode = damageCode;
		this.damageName = damageName;
		this.indemnityDuty = indemnityDuty;
		this.claimType = claimType;
		this.isCommonClaim = isCommonClaim;
		this.subrogateType = subrogateType;
		this.accidentBook = accidentBook;
		this.accident = accident;
		this.remark = remark;
		this.checkReport = checkReport;
	}
	public SurveyKeyPoint() {
		super();
	};
	
}
