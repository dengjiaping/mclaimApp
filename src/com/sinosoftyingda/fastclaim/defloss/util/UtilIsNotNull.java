package com.sinosoftyingda.fastclaim.defloss.util;

import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;

import android.content.ContentValues;
import android.text.TextUtils;

/**
 * 定损提交类 进行非空校验
 * 
 * @author haoyun 20130506
 * 
 */
public class UtilIsNotNull {

	/**
	 * 判断是否为空
	 * @param verifyLossSubmitRequest
	 * @param isSubmit
	 * @param isThress
	 * @return
	 */
	public static String isNotNull(VerifyLossSubmitRequest verifyLossSubmitRequest, boolean isSubmit, String isThress) {
		String message = "YES";
		if (isSubmit) {
			if (TextUtils.isEmpty(verifyLossSubmitRequest.getLossNo())) {
				return "损失项目编号不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSubmitType())) {
				return "提交类型不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getEstimateNo())) {
				return "定损单号 不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getRegistNo())) {
				return "报案号不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getPlateNo())) {
				return "车牌号码 ！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getKindCode())) {
				return "定损险别！";
			}

			else if (TextUtils.isEmpty(verifyLossSubmitRequest.getVehkindCode())) {
				// return "定损车辆种类代码！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getVehkindName())) {
				// return "定损车辆种类名称！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getVehcertainCode())) {
				// return "定损车型编码 ！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getVehcertaninName())) {
				// return "定损车型名称！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainCalltime())) {
				// return "请联系客户后提交!";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainFirstPicTime())) {
				// return "请拍摄照片后提交!";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainComCode())) {
				// return "定损处理人员机构代码不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainHandleCode())) {
				// return "定损处理人员代码不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainHandleName())) {
				// return "定损处理人员名称不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getRepairfactoryCode())) {
				// return "修理厂代码不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getRepairfactoryName())) {
				// return "修理厂名称 不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getRepaircooperateFlag())) {
				// return "修理厂合作性质不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getRepairAptitude())) {
				// return "修理厂资质 不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getExcessType())) {
				return "是否互碰自赔 不能为空！";
			}
			// private String vehserigradename = "";// 三者车新车购置价 上海 not null
			else if (isThress.equals("0") && TextUtils.isEmpty(verifyLossSubmitRequest.getVehserigradename())) {
				return "三者车新车购置价不能为空！";
			} else if (SystemConfig.AREAN.equals("上海")) {// 判断是否是上海如果不是则可以为空
				// private String carregisterdate = "";// 车辆初次登记日期 上海 not null
				if (TextUtils.isEmpty(verifyLossSubmitRequest.getCarregisterdate())) {
					return "车辆初次登记日期 不能为空！";
				}
				// private String carvehicledesc = "";// 行驶证车辆描述 上海 not null
				else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCarvehicledesc())) {
					return "行驶证车辆描述 不能为空！";
				}
				// private String carfactorycode = "";// 车辆制造厂编码 上海 not null
				else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCarfactorycode())) {
					return "车辆制造厂编码 不能为空！";
				}
				// private String carfactoryname = "";// 车辆制造厂名称 上海 not null
				else if (TextUtils.isEmpty(verifyLossSubmitRequest.getCarfactoryname())) {
					return "车辆制造厂名称不能为空！";
				}
			} else if (SystemConfig.AREAN.equals("北京")) {// 判断是否是北京 如果不是北京则可以为空
				// private String claimtype = "";// 赔案性质 北京 not null
				if (TextUtils.isEmpty(verifyLossSubmitRequest.getClaimtype())) {
					return "赔案性质不能为空！";
				}// private String accidentBook = "";// 有无交管事故书 0否 1是 not null
				else if (TextUtils.isEmpty(verifyLossSubmitRequest.getAccidentBook())) {
					return "有无交管事故书不能为空！";
				}
				// private String accident = "";// 是否道路交通事故 0否 1是 not null
				else if (TextUtils.isEmpty(verifyLossSubmitRequest.getAccident())) {
					return "是否道路交通事故不能为空！";
				}
			}else if (TextUtils.isEmpty(verifyLossSubmitRequest.getTextconent())) {
				return "定损意见不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSatisfaction())) {
				return "满意度评价不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSumfitsfee())) {
				// return "合计换件金额不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSumrepairfee())) {
				// return "合计修理金额不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSumverirest())) {
				// return "合计残值金额不能为空！";
			} else if (TextUtils.isEmpty(verifyLossSubmitRequest.getSumcertainloss())) {
				// return "定损合计金额不能为空！";
			} 
		} else {
			if (TextUtils.isEmpty(verifyLossSubmitRequest.getCertainCalltime())) {
				// return "请联系客户后提交!";
			}
		}
		return message;
	}

	/**
	 * 互碰自赔的案件：三者车无损提交即0提交
	 * @return
	 */
	public boolean checkOCommitRule() {
		boolean isOCommit = false;
		if (DataConfig.defLossInfoQueryData != null) {
			// 标的车1，三者车0
			String insurecarFlag = "1";
			for (int i = 0; i < DataConfig.defLossInfoQueryData.getDefLossCarInfos().size(); i++) {
				insurecarFlag = DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getInsurecarFlag().trim();
			}
			// 赔案性质 （一般赔案0，交强险T）
			String claimType = DataConfig.defLossInfoQueryData.getSurveyKeyPoint().getClaimType();

			/* 互碰自赔的案件：三者车无损提交即0提交 */
			if (claimType.equals("T") && insurecarFlag.equals("0")) {
				isOCommit = true;
			}
		}
		return isOCommit;
	}

	/**
	 * 一般案件：标的车不可以录入BZ险下的损失项，即BZ险0赔付
	 * 
	 * @return
	 */
	public boolean checkBZCommitRule() {
		boolean isBZCoomit = false;
		if (DataConfig.defLossInfoQueryData != null) {
			// 标的车1，三者车0
			String insurecarFlag = "1";
			for (int i = 0; i < DataConfig.defLossInfoQueryData.getDefLossCarInfos().size(); i++) {
				insurecarFlag = DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getInsurecarFlag().trim();
			}
			// 赔案性质 （一般赔案0，交强险T）
			String claimType = DataConfig.defLossInfoQueryData.getSurveyKeyPoint().getClaimType();
			// 定损险别(BZ险)
			String defLossRiskCode = DataConfig.defLossInfoQueryData.getDefLossContent().getDefLossRiskCode().trim().toUpperCase();

			if (insurecarFlag.equals("1") && claimType.equals("0") && defLossRiskCode.equals("BZ")) {
				isBZCoomit = true;
			}
		}
		return isBZCoomit;
	}

	/***
	 * 视频协助，联系客户时间不能为空 chenjianfan 20130-05-21
	 * 
	 * true 为不空 false为空
	 * 
	 * @return
	 */
	public static boolean isNotNullContact(String contactTime) {
		boolean flage = false;
		if (!TextUtils.isEmpty(contactTime)) {
			flage = true;
		}
		return flage;
	}
	
	
	/**
	 * 判断是否有拍照
	 * @return
	 */
	public static boolean checkIsArrived(){
		boolean isArrived = false;
		ContentValues checkValues = DBUtils.find("ClTaskInfo", new String[] { "ARRIVESCENETIME" }, "REGISTNO=? and LOSSNO=?", 
				new String[] {DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), DataConfig.defLossInfoQueryData.getRegist().getLossNo() });
		
		String arrivetime = checkValues.get("ARRIVESCENETIME").toString();
		if (arrivetime == null || arrivetime.equals("")) {	
			isArrived = false;
		}else{
			isArrived = true;
		}
		
		return isArrived;
	}
	
	
	/**
	 * 是否联系过客户
	 * @return
	 */
	public static boolean isCallPhone(String registNo){
		// 查询是否成功联系客户
		CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, -100);
		if (certainLossTask != null && !"".equals(certainLossTask.getContacttime())) {
			return true;
		}
		return false;
	}
}
