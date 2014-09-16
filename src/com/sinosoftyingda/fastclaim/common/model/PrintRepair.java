package com.sinosoftyingda.fastclaim.common.model;

/**
 * 打印维修类
 * @author haoyun 20130227
 *
 */
public class PrintRepair {
	private String repairName;//维修名称
	private String repairPro;//维修项目
	private String repairPrice;//维修价格
	public String getRepairName() {
		return repairName;
	}
	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}
	public String getRepairPro() {
		return repairPro;
	}
	public void setRepairPro(String repairPro) {
		this.repairPro = repairPro;
	}
	public String getRepairPrice() {
		return repairPrice;
	}
	public void setRepairPrice(String repairPrice) {
		this.repairPrice = repairPrice;
	}
	public PrintRepair(String repairName, String repairPro, String repairPrice) {
		super();
		this.repairName = repairName;
		this.repairPro = repairPro;
		this.repairPrice = repairPrice;
	}
	public PrintRepair() {
		super();
	}
	
}
