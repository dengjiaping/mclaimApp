package com.sinosoftyingda.fastclaim.defloss.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jy.lioneye.client.request.EvalRequest;
import com.jy.lioneye.client.response.EvalLossInfo;
import com.jy.lioneye.client.response.EvalPartInfo;
import com.jy.lioneye.client.response.EvalRepairInfo;
import com.jy.lioneye.client.response.EvalResponse;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;
import com.sinosoftyingda.fastclaim.common.model.DefLossCarInfo;
import com.sinosoftyingda.fastclaim.common.model.DefLossContent;
import com.sinosoftyingda.fastclaim.common.model.LossFitInfo;
import com.sinosoftyingda.fastclaim.common.model.LossRepairInfo;
import com.sinosoftyingda.fastclaim.common.model.Regist;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossInfoActivity;

/**
 * 精友离线数据更新
 * 
 * @author DengGuang
 * 
 */
public class JYLioneyeToolsActivity extends Activity {
	
	/** 修理厂代码 */
	public static final String KEY_FACTORY_CODE = "RepairFactoryCode";
	/** 修理厂名称 */
	public static final String KEY_FACTORY_NAME = "RepairFactoryName";
	/** 合作标志 */
	public static final String KEY_FACTORY_FLAG = "CooperateFlag";
	/** 修理厂资质 */
	public static final String KEY_FACTORY_APTITUDE = "RepairFactoryAptitude";
	/** 配件金额合计 */
	public static final String KEY_SUMCHGCOMFEE = "SumChgCompFee";
	/** 维修金额合计 */
	public static final String KEY_SUMREPAIRFEE = "SumRepairFee";
	/** 残值合计 */
	public static final String KEY_SUMREMNANT = "SumRemnant";
	/** 定损金额合计 */
	public static final String KEY_SUMLOSSFEE = "SumLossFee";

	/** 新车购置价 */ 
	private String newCarPriceArrs[];
	
	/** 新车购置价实体类 */
	private SpinnerBean sBeanDeflossType;
	private List<SpinnerBean> sBeansNewCarPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//数据库中查询serialno 	serialnoAccessInt
		int serialnoAccessInt=0;
		//sharepreferences中取出缓存值		serialnoSharepre
		int serialnoSharepre=0;
		String serialnoAccess="";
		SharedPreferences spReassign=SystemConfig.context.getSharedPreferences("reassign", SystemConfig.context.MODE_APPEND);
		SharedPreferences.Editor spReassignEdit=spReassign.edit();
		// 精友定损标志
		String requestType = SystemConfig.JYDeflossStatus;
		//数据库读取serialno的值
		serialnoAccess=CertainLossTaskAccess.find(DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), Integer.valueOf(DataConfig.defLossInfoQueryData.getRegist().getLossNo())).getSeriaNo();
		//字符串类型转换
		if("".equals(serialnoAccess.trim())){
			serialnoAccess="0";
		}
		serialnoAccessInt=Integer.parseInt(serialnoAccess);
		//从share中读取该报案号的值，如果没有
		serialnoSharepre=spReassign.getInt(DataConfig.defLossInfoQueryData.getRegist().getRegistNo()+"",0);
		if(serialnoAccessInt>serialnoSharepre){  
			requestType=SystemConfig.JYDeflossReassign;
			spReassignEdit.putInt(DataConfig.defLossInfoQueryData.getRegist().getRegistNo()+"", Integer.valueOf(serialnoAccess)).commit();
		}
		try {
			getJYEvalData(requestType);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "未安装精友定损工具!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// 获取返回值
			GsonBuilder builder = new GsonBuilder();
			builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
			Gson gson = builder.create();
			// 获取定损信息
			String claimEvalData = data.getStringExtra("CLAIMEVAL_DATA");
			Log.e("responseData", claimEvalData);
			if (claimEvalData != null && !"".equals(claimEvalData.trim())) {

				EvalResponse evalResponse = gson.fromJson(claimEvalData, EvalResponse.class);
				if ("1".equals(evalResponse.getResponseCode().trim())) {
					// 正常返回
					EvalLossInfo evalLossInfo = evalResponse.getEvalLossInfo();
					VerifyLossSubmitRequest verifyLossSubmitRequest = VerifyLossSubmitRequest.getVerifyLossSubmitRequest();

					// 换件信息
					List<EvalPartInfo> evalPartList = evalLossInfo.getEvalPartList();
					List<LossFitInfo> lossFitInfoList = new ArrayList<LossFitInfo>();
					// 维修信息
					List<EvalRepairInfo> evalRepairList = evalLossInfo.getEvalRepairList();
					List<LossRepairInfo> lossRepairInfoList = new ArrayList<LossRepairInfo>();
					
					// ----- 定损信息转换
					verifyLossSubmitRequest.setRegistNo(DataConfig.defLossInfoQueryData.getRegist().getRegistNo());
					verifyLossSubmitRequest.setLossNo(DataConfig.defLossInfoQueryData.getRegist().getLossNo());
					verifyLossSubmitRequest.setPlateNo(evalLossInfo.getPlateNo());
					verifyLossSubmitRequest.setVehkindCode(evalLossInfo.getVehKindCode());
					verifyLossSubmitRequest.setVehkindName(evalLossInfo.getVehKindName());
					
					// start DengGuang
					// 判断选择的车型编码是否为空，如果为空，则加理赔带过来的车型编码
					if(evalLossInfo.getVehCertainCode().equals("") && DataConfig.defLossInfoQueryData.getRegist().getLossNo().equals("1")){
						verifyLossSubmitRequest.setVehcertainCode(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).getInsurevehiCode());
					}else{
						verifyLossSubmitRequest.setVehcertainCode(evalLossInfo.getVehCertainCode());
					}
					// end DengGuang
					verifyLossSubmitRequest.setVehcertaninName(evalLossInfo.getVehCertainName());
					verifyLossSubmitRequest.setRepairfactoryCode(evalLossInfo.getRepairFactoryCode());
					verifyLossSubmitRequest.setRepairfactoryName(evalLossInfo.getRepairFactoryName());
					verifyLossSubmitRequest.setRepaircooperateFlag(evalLossInfo.getCooperateFlag()==null?"0":evalLossInfo.getCooperateFlag());    // ---------------------JY新增字段
					
					System.out.println("----------verifyLossSubmitRequest精友返回的修理厂合作性质------------："+evalLossInfo.getCooperateFlag());
					verifyLossSubmitRequest.setRepairMode(evalLossInfo.getCooperationCause());				// ---------------------JY新增字段
//					verifyLossSubmitRequest.setRepairReason("");										// ---------------------非必传
//					verifyLossSubmitRequest.setCertainStartDate("");
					verifyLossSubmitRequest.setRepairAptitude(evalLossInfo.getRepairFactoryAptitude());			// ---------------------JY新增字段
					
					verifyLossSubmitRequest.setInsuredName(evalLossInfo.getInsuredName());
					verifyLossSubmitRequest.setVehfactoryname(evalLossInfo.getVehFactoryName());
					verifyLossSubmitRequest.setVehfactorycode(evalLossInfo.getVehFactoryCode());
					verifyLossSubmitRequest.setVehbrandcode(evalLossInfo.getVehBrandCode());
					verifyLossSubmitRequest.setVehbrandname(evalLossInfo.getVehBrandName());
					verifyLossSubmitRequest.setSlag(evalLossInfo.getSelfConfigFlag());
//					verifyLossSubmitRequest.setCarfactorycode(evalLossInfo.GET)
					
					verifyLossSubmitRequest.setSumfitsfee(evalLossInfo.getSumChgCompFee() + "");// 合计换件金额
					verifyLossSubmitRequest.setSumrepairfee(evalLossInfo.getSumRepairFee() + "");// 合计修理金额
					verifyLossSubmitRequest.setSumverirest(evalLossInfo.getSumRemnant() + "");// 合计残值金额
					verifyLossSubmitRequest.setSumcertainloss(evalLossInfo.getSumLossFee() + "");// 定损合计金额

					// ----- 换件信息转换
					for (int i = 0; i < evalPartList.size(); i++) {
						EvalPartInfo evalPartInfo = evalPartList.get(i);
						LossFitInfo lossFitInfo = new LossFitInfo();
						lossFitInfo.setPartid(evalPartInfo.getPartId());
						lossFitInfo.setSerialNo(evalPartInfo.getSerialNo() + "");
						lossFitInfo.setOriginalId(evalPartInfo.getOriginalId()+"");
						lossFitInfo.setOriginalName(evalPartInfo.getOriginalName()+"");
						lossFitInfo.setPartstandardCode(evalPartInfo.getPartStandardCode()+"");
						lossFitInfo.setPartstandard(evalPartInfo.getPartStandard()+"");
						lossFitInfo.setPartgroupCode(evalPartInfo.getPartGroupCode()+"");
						lossFitInfo.setPartgroupName(evalPartInfo.getPartGroupName()+"");
						
						// 换件价格方案
						String priceModel = "";
						String priceModelCode = "001";
						if(evalPartInfo.getPriceModel() != null && !evalPartInfo.getPriceModel().equals("")){
							priceModel = evalPartInfo.getPriceModel().trim();
						}
						if(priceModel.equals("普通修理厂")){
							priceModelCode = "002";
						}else if(priceModel.equals("4S店方案")){
							priceModelCode = "001";
						}
						lossFitInfo.setPriceModel(priceModelCode);//价格方案名称getPriceModel
						lossFitInfo.setSystemPrice001(evalPartInfo.getSystemPrice001() + "");
						lossFitInfo.setLocalPrice001(evalPartInfo.getLocalPrice001() + "");
						lossFitInfo.setSystemPrice002(evalPartInfo.getSystemPrice002() + "");
						lossFitInfo.setLocalPrice002(evalPartInfo.getLocalPrice002() + "");
						lossFitInfo.setLossFee(evalPartInfo.getLossPrice() + ""); // 定损价格
						lossFitInfo.setRestFee(evalPartInfo.getRemanent() + ""); // 定损残值
						lossFitInfo.setSelfconfigflag(evalPartInfo.getSelfConfigFlag() + "");
						lossFitInfo.setLossCount(evalPartInfo.getLossCount() + "");
						lossFitInfo.setRemark(evalPartInfo.getRemark()+"");
						lossFitInfo.setSystemPrice(evalPartInfo.getChgRefPrice() + ""); // 定损选中系统价
						lossFitInfo.setLocalPrice(evalPartInfo.getChgLocPrice() + ""); // 定损选中本地价

						Log.e("Tag", "evalPartInfo.getLossCount():"+evalPartInfo.getLossCount());
						lossFitInfoList.add(lossFitInfo);
					}

					// ----- 维修信息转换
					for (int i = 0; i < evalRepairList.size(); i++) {
						EvalRepairInfo evalRepairInfo = evalRepairList.get(i);
						LossRepairInfo lossRepairInfo = new LossRepairInfo();
						lossRepairInfo.setRepairId(evalRepairInfo.getRepairId()+"");
						lossRepairInfo.setSerialNo(evalRepairInfo.getSerialNo() + "");
						lossRepairInfo.setRepairCode(evalRepairInfo.getRepairCode()+"");
						lossRepairInfo.setRepairName(evalRepairInfo.getRepairName()+"");
						lossRepairInfo.setRepairitemCode(evalRepairInfo.getRepairItemCode() + "");
						lossRepairInfo.setRepairitemName(evalRepairInfo.getRepairItemName()+"");
						lossRepairInfo.setRepairfee(evalRepairInfo.getRepairFee() + "");
						lossRepairInfo.setSystemprice(evalRepairInfo.getManHour()+"");		// 系统价格.
						lossRepairInfo.setSelfConfigFlag(evalRepairInfo.getSelfConfigFlag()+"");
						lossRepairInfo.setPriceModel(evalRepairInfo.getPriceSourceCode()); // ------->>> 维修价格方案

						lossRepairInfoList.add(lossRepairInfo);
					}
					verifyLossSubmitRequest.setLossFitInfo(lossFitInfoList);
					verifyLossSubmitRequest.setLossRepairInfo(lossRepairInfoList);
					DeflossSynchroAccess.insertOrUpdate(verifyLossSubmitRequest);
				
					// 返回修理厂信息
					String factoryCode = evalLossInfo.getRepairFactoryCode();
					String factoryName = evalLossInfo.getRepairFactoryName();
					String aptitudeCode = evalLossInfo.getRepairFactoryAptitude();
					String flag = evalLossInfo.getCooperateFlag();
					
					System.out.println("----------精油返回的修理厂合作性质------------"+flag);
					
					String factoryAptitude = "";
					if(aptitudeCode!= null && aptitudeCode.trim().equals("000")){
						factoryAptitude = "4S店";
					}else if(aptitudeCode!= null && aptitudeCode.trim().equals("001")){
						factoryAptitude = "一类厂";
					}else if(aptitudeCode!= null && aptitudeCode.trim().equals("002")){
						factoryAptitude = "二类厂";
					}else if(aptitudeCode!= null && aptitudeCode.trim().equals("003")){
						factoryAptitude = "三类厂";
					}
					
//					if(flag!= null && flag.trim().equals("1")){
//						factoryFlag = "合作";
//					}else{
//						factoryFlag = "非合作";
//					}

					String sumChgCompFeeStr = evalLossInfo.getSumChgCompFee() + "";
					String sumRepairFeeStr = evalLossInfo.getSumRepairFee() + "";
					String sumRemnantStr = evalLossInfo.getSumRemnant() + "";
					String sumLossFeeStr = evalLossInfo.getSumLossFee() + "";
					
					// 精友保存页面值 
					DefLossContent lossContent = DataConfig.defLossInfoQueryData.getDefLossContent();
					lossContent.setRepairFactoryCode(factoryCode);
					lossContent.setRepairFactoryName(factoryName);
					lossContent.setRepairCooperateFlag(flag);
					lossContent.setRepairapTitude(factoryAptitude);
					lossContent.setSumfitsfee(sumChgCompFeeStr);
					lossContent.setSumRepairfee(sumRepairFeeStr);
					lossContent.setSumRest(sumRemnantStr);
					lossContent.setSumCertainLoss(sumLossFeeStr);
					
					//add by jingtuo 添加上海制造厂伤信息 start
					List<DefLossCarInfo> list = DataConfig.defLossInfoQueryData.getDefLossCarInfos();
					if(list==null){
						list = new ArrayList<DefLossCarInfo>();
					}
					if(list.size()<=0){
						DefLossCarInfo defLossCarInfo = new DefLossCarInfo();
						defLossCarInfo.setCarFactoryCode(evalLossInfo.getVehFactoryCode());
						defLossCarInfo.setCarFactoryName(evalLossInfo.getVehFactoryName());
						list.add(defLossCarInfo);
					}else if(list.size()>=1){
						list.get(0).setCarFactoryCode(evalLossInfo.getVehFactoryCode());
						list.get(0).setCarFactoryName(evalLossInfo.getVehFactoryName());
					}
					DataConfig.defLossInfoQueryData.setDefLossCarInfos(list);
					//add by jingtuo 添加上海制造厂伤信息 end
					
					Bundle bundle = new Bundle();
					bundle.putString(KEY_FACTORY_CODE, factoryCode);
					bundle.putString(KEY_FACTORY_NAME, factoryName);
					bundle.putString(KEY_FACTORY_FLAG, flag);
					bundle.putString(KEY_FACTORY_APTITUDE, factoryAptitude);
					bundle.putString(KEY_SUMCHGCOMFEE, sumChgCompFeeStr);
					bundle.putString(KEY_SUMREPAIRFEE, sumRepairFeeStr);
					bundle.putString(KEY_SUMREMNANT, sumRemnantStr);
					bundle.putString(KEY_SUMLOSSFEE, sumLossFeeStr);
					
					UiManager.getInstance().changeView(DeflossInfoActivity.class, bundle, false);
				} else if ("2".equals(evalResponse.getResponseCode())) {
					// 返回键
					Toast.makeText(this, evalResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
				} else if ("0".equals(evalResponse.getResponseCode())) {
					// 错误
					Toast.makeText(this, evalResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
				} else {
					// 未知错误，联系技术支持
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}

	/**
	 * 离线数据更新页面a
	 *     001：定损请求
	 *     002：定损退回
	 *     003：定损查看
	 */
	private void getJYEvalData(String requestType) {
		Intent mIntent = new Intent();
		mIntent.setAction("com.jy.lioneye.EVAL");
		mIntent.addCategory("android.intent.category.DEFAULT");
		EvalRequest evalRequest = new EvalRequest();
		evalRequest.setUserCode("jy");
		evalRequest.setPassword("jy");
		evalRequest.setRequestType(requestType);
		evalRequest.setPower("1111111111111");

		// 精友请求参数
		EvalLossInfo evalLossInfo = new EvalLossInfo();
		
		if (DataConfig.defLossInfoQueryData != null) {

			DefLossCarInfo carInfo = new DefLossCarInfo();
			Regist regist = new Regist();
			regist = DataConfig.defLossInfoQueryData.getRegist();
			carInfo = DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0);
			
			evalLossInfo.setLossNo(regist.getLossNo());
			evalLossInfo.setReportNo(regist.getRegistNo());
			evalLossInfo.setEstimateNo(regist.getRegistNo());
			evalLossInfo.setComCode(regist.getPolicyComCode().substring(0, 2));
			evalLossInfo.setComName(SystemConfig.loginResponse.getUserComName());
			evalLossInfo.setHandlerCode(SystemConfig.loginResponse.getUserName());
			evalLossInfo.setPlateNo(regist.getLicenseNo());
			evalLossInfo.setHandlerName(SystemConfig.loginResponse.getUserName());
			evalLossInfo.setInsuredName(regist.getInsuredName());
			evalLossInfo.setInsureVehiCode(carInfo.getInsurevehiCode());
			evalLossInfo.setTaskId(regist.getRegistNo());
			evalLossInfo.setTargetFlag(SystemConfig.JYTCAR_TYPE+"");
			evalLossInfo.setInsureVehicle(carInfo.getBrandName());
			
			// 价格区间&新车购置价
			String claimNewCarprice = carInfo.getNewPruchaseAmount();
			evalLossInfo.setPriceSection("0");				// 价格区间编码 ： A#
			// ---------->>> 标的车
			if(SystemConfig.JYTCAR_TYPE == 1){
				Double newPrice = Double.parseDouble(claimNewCarprice);
				claimNewCarprice = (newPrice/10000)+"";
				evalLossInfo.setPriceSection(claimNewCarprice);				// 价格区间编码 ： A#
				evalLossInfo.setInsurancePrice(claimNewCarprice);			// 现场购置价     ： 5000
			}
			// ---------->>> 三者车
			else if(SystemConfig.JYTCAR_TYPE == 0){
				// 新车购置价区间
				newCarPriceArrs = JYLioneyeToolsActivity.this.getResources().getStringArray(R.array.defloss_newthreecar_price);
				sBeansNewCarPrice = new ArrayList<SpinnerBean>();
				sBeanDeflossType = new SpinnerBean();
				for (int i = 0; i < newCarPriceArrs.length; i++) {
					sBeanDeflossType = new SpinnerBean();
					String str = newCarPriceArrs[i];
					sBeanDeflossType.setCode(str.split("-")[0]);
					sBeanDeflossType.setValue(str.split("-")[1]);
					sBeansNewCarPrice.add(sBeanDeflossType);
				}
				// 转码
				for(int i = 0; i<sBeansNewCarPrice.size(); i++){
					String newCarprice = sBeansNewCarPrice.get(i).getValue().trim();
					if(newCarprice.equals(claimNewCarprice)){
						evalLossInfo.setPriceSection(sBeansNewCarPrice.get(i).getCode());	
						evalLossInfo.setInsurancePrice("0");
					}
				}
			}else{
				evalLossInfo.setPriceSection("0");				// 价格区间编码 ： A#
				evalLossInfo.setInsurancePrice("0");			// 现场购置价     ： 5000
			}
//			evalLossInfo.setVIN("");           	//	VIN码未传
			Log.e("VehiCode", "-->"+DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).getInsurevehiCode());
		}
		evalRequest.setEvalLossInfo(evalLossInfo);

		Bundle bundle = new Bundle();
		GsonBuilder builder = new GsonBuilder();
		builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

		Gson gson = builder.create();
		String requestData = gson.toJson(evalRequest);
		bundle.putString("REQUEST_DATA", requestData);
		mIntent.putExtras(bundle);
		startActivityForResult(mIntent, 1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
