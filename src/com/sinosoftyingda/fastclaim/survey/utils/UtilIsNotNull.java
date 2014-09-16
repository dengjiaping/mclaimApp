package com.sinosoftyingda.fastclaim.survey.utils;

import android.text.TextUtils;

import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.utils.Log;

/****
 * 查勘字段不能为空
 * 
 * @author chenjianfan
 * 
 */
public class UtilIsNotNull {

	public static String isNotNull(DetailTaskQueryResponse detailTaskQueryResponse, boolean isSubmit) {
		String str = "YES";
		if (isSubmit) {
			if (detailTaskQueryResponse != null) {
				if (TextUtils.isEmpty(detailTaskQueryResponse.getFirstsiteFlag())) {
					return str = "是否是第一现场不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getLinkcustomTime())) {
//					return str = "联系客户时间不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getClaimType())) {
					return str = "赔案性质不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getDamageCode())) {
					return str = "出险 原因代码不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getDamageName())) {
					return str = "出险原因不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getSubrogateType())) {
//					return str = "是否代为不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getIsCommonClaim())) {
//					return str = "是否通赔不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getAccidentBook()) && "北京".equals(SystemConfig.AREAN)) {
//					return str = "有无交管事故书不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getAccident()) && "北京".equals(SystemConfig.AREAN)) {
//					return str = "是否道路交通事故不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getIndemnityDuty())) {
					return str = "事故责任不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckReport().trim())) {
					return str = "查勘报告不能为空";
				}
				
				if (TextUtils.isEmpty(detailTaskQueryResponse.getInsuredMobile())) {
					return str = "被保险人电话不能为空";
				}
				if (detailTaskQueryResponse.getCheckDriver().size() > 0) {
					// 驾驶员信息
					if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckDriver().get(0).getSerialNo())) {
						return str = "驾驶员信息中的序号不能为空";
					} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckDriver().get(0).getDrivinglicenseNo())) {
						return str = "驾驶员信息中驾驶证号码不能为空";
					} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckDriver().get(0).getDriverName())) {
						return str = "驾驶员信息中驾驶员姓名不能为空";
					} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckDriver().get(0).getIdentifyNumber())) {
						return str = "驾驶员信息中身份证号码不能为空";
					} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckDriver().get(0).getReceivelicenseDate())) {
						return str = "驾驶员信息中领证时间不能为空";
					}

				}
				if (detailTaskQueryResponse.getCheckExtList().size() > 1) {
					// 查勘要点信息，有字段在判断
					for (int i = 0; i < detailTaskQueryResponse.getCheckExtList().size(); i++) {
						if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelName())) {
							return str = "查勘要点信息中查勘项目名称不能为空";
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelCode())) {
							return str = "查勘要点信息中" + detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelName() + "的查勘项目代码不能为空";
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelSelect())) {
							return str = "查勘要点信息中" + detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelName() + "的查勘字段内容（是否）不能为空";
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCheckExtList().get(i).getSerialNo())) {
							return str = "查勘要点信息中" + detailTaskQueryResponse.getCheckExtList().get(i).getCheckKernelName() + "序号不能为空";
						}
					}
				}
				if (detailTaskQueryResponse.getCheckReport().equals("2")) {
					if (detailTaskQueryResponse.getCarLossList().size() <= 1) {
						return str = "事故责任为同责时，必须添加三者车！";
					}

				} else if (detailTaskQueryResponse.getCarLossList().size() > 0) {
					// 涉损车辆
					for (int i = 0; i < detailTaskQueryResponse.getCarLossList().size(); i++) {
						if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getCarNum())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的序号不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getLicenseNo())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的车牌号不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getCarKindCode())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的车辆种类代码不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getLicenseType())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的号牌种类不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getEngineNo())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的发动机号不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getFrameNo())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的车架号不能为空";
							}
						} else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getBrandName())) {
							if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {

							} else {
								return str = "涉损车辆中三者车的厂牌型号不能为空";
							}
						} 
						//浙江分公司增加比录项判断
						String strComcode=DataConfig.tblTaskQuery.getRegistNo().toString().substring(1,3);
						if(strComcode.equals("18")){
							if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getNullDriverName())) {
								if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {
	
								} else {
									return str = "涉损车辆中三者车的驾驶员姓名不能为空，并确认驾驶员证件类型";
								}
							}else if (TextUtils.isEmpty(detailTaskQueryResponse.getCarLossList().get(i).getNullDriverCode())) {
								if ("1".equals(detailTaskQueryResponse.getCarLossList().get(i).getInsureCarFlag())) {
	
								} else {
									return str = "涉损车辆中三者车的驾驶员证件号码不能为空，并确认驾驶员证件类型";
								}
							}
						}
					}
				}
				
				if (TextUtils.isEmpty(detailTaskQueryResponse.getDisposeTaskDate())) {
					// return str = "查勘开始处理时间不能为空";
				} else if (TextUtils.isEmpty(detailTaskQueryResponse.getFirstphotoDate())) {
					return str = "首张照片时间不能为空";
				}
			}
		} else {
			if (TextUtils.isEmpty(detailTaskQueryResponse.getLinkcustomTime())) {
//				return str = "联系客户时间不能为空";
			}
		}

		return str;
	}

	/***
	 * 视频协助，联系客户时间不能为空 chenjianfan 20130-05-21 
	 * 
	 * true 为不空 false为空
	 * 
	 * @return
	 */
	public static boolean isNotNullContact(DetailTaskQueryResponse detailTaskQueryResponse) {
		boolean flage = false;
		if (detailTaskQueryResponse != null) {
			if (!TextUtils.isEmpty(detailTaskQueryResponse.getLinkcustomTime())) {
				flage = true;
			}
		}
		return flage;

	}
}
