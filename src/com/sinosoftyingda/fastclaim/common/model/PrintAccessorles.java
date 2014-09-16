package com.sinosoftyingda.fastclaim.common.model;

/**
 * 打印配件信息类
 * @author haoyun 2013
 *
 */
public class PrintAccessorles {
	private String partName;//配件名称
	private String quanTity;//配件数量
	private String sumDefLoss;//定损价格
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getQuanTity() {
		return quanTity;
	}
	public void setQuanTity(String quanTity) {
		this.quanTity = quanTity;
	}
	public String getSumDefLoss() {
		return sumDefLoss;
	}
	public void setSumDefLoss(String sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}
	public PrintAccessorles(String partName, String quanTity, String sumDefLoss) {
		super();
		this.partName = partName;
		this.quanTity = quanTity;
		this.sumDefLoss = sumDefLoss;
	}
	public PrintAccessorles() {
		super();
	}

	
}
