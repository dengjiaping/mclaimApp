package com.sinosoftyingda.fastclaim.common.db.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.LossFitInfo;
import com.sinosoftyingda.fastclaim.common.model.LossRepairInfo;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;

public class DeflossSynchroAccess {
	
	/**
	 * 查看定损同步信息
	 * @param registNo
	 * @param lossNo
	 * @return
	 * for update by haoyun   DeflossSynchroRepair表增加SYSTEMPRICE(系统价格)字段
	 */
	public static VerifyLossSubmitRequest find(String registNo, String lossNo){
		VerifyLossSubmitRequest.verifyLossSubmitRequest=null;
		VerifyLossSubmitRequest dsm = VerifyLossSubmitRequest.getVerifyLossSubmitRequest();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			cursor = db.query("DeflossSynchroMain", null, "registNo=? and lossNo=?", new String[]{registNo, lossNo}, null, null, null);
			if(cursor!=null&&cursor.moveToNext()){
				dsm = toDeflossSynchroMain(cursor);
			}
			if(dsm!=null){
				cursor = db.query("DeflossSynchroReplace", null, "registNo=? and lossNo=?", new String[]{registNo, lossNo}, null, null, null);
				List<LossFitInfo> replaces = new ArrayList<LossFitInfo>(); 
				while(cursor!=null&&cursor.moveToNext()){
					replaces.add(toDeflossSynchroReplace(cursor));
				}
				if(replaces.size()>=1){
					dsm.setLossFitInfo(replaces);
				}
				
				cursor = db.query("DeflossSynchroRepair", null, "registNo=? and lossNo=?", new String[]{registNo, lossNo}, null, null, null);
				List<LossRepairInfo> repairs = new ArrayList<LossRepairInfo>(); 
				while(cursor!=null&&cursor.moveToNext()){
					repairs.add(toDeflossSynchroRepair(cursor));
				}
				if(repairs.size()>=1){
					dsm.setLossRepairInfo(repairs);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(cursor!=null){
				cursor.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return dsm;
	}
	
	
	/**
	 * 查看定损同步信息
	 * @param registNo
	 * @param lossNo
	 * @return
	 */
	public static boolean findJYDatas(String registNo, String lossNo){
		boolean isFind = false;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			cursor = db.query("DeflossSynchroMain", null, "registNo=? and lossNo=?", new String[]{registNo, lossNo}, null, null, null);
			if(cursor!=null&&cursor.moveToNext()){
				isFind = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor!=null){
				cursor.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return isFind;
	}
	
	
	/**
	 * 插入定损同步数据，主信息如果存在则更新，否则插入，换件和维修信息先删除后插入
	 * @param deflossSynchroMain
	 */
	public static void insertOrUpdate(VerifyLossSubmitRequest deflossSynchroMain){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		ContentValues cv = null;
		try {
			db = SystemConfig.dbhelp.getWritableDatabase();
			db.beginTransaction();
			String whereClause = "registNo=? and lossNo=?";
			String[] whereArgs = new String[]{deflossSynchroMain.getRegistNo(), deflossSynchroMain.getLossNo()};
			cursor = db.query("DeflossSynchroMain", null, whereClause, whereArgs, null, null, null);
			if(cursor!=null&&cursor.moveToNext()){
				db.update("DeflossSynchroMain", toValues(deflossSynchroMain), whereClause, whereArgs);
			}else{
				db.insert("DeflossSynchroMain", null, toValues(deflossSynchroMain));
			}
			db.delete("DeflossSynchroReplace", whereClause, whereArgs);
			db.delete("DeflossSynchroRepair", whereClause, whereArgs);
			for (int i = 0; i < deflossSynchroMain.getLossRepairInfo().size(); i++) {
				db.insert("DeflossSynchroRepair", null, toValues(deflossSynchroMain.getLossRepairInfo().get(i),deflossSynchroMain.getRegistNo(),deflossSynchroMain.getLossNo()));
			}
			for (int i = 0; i < deflossSynchroMain.getLossFitInfo().size(); i++) {
				db.insert("DeflossSynchroReplace", null, toValues(deflossSynchroMain.getLossFitInfo().get(i),deflossSynchroMain.getRegistNo(),deflossSynchroMain.getLossNo()));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			if(cv!=null){
				cv.clear();
			}
			if(db!=null){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	private static VerifyLossSubmitRequest toDeflossSynchroMain(Cursor cursor){
		VerifyLossSubmitRequest deflossSynchroMain = new VerifyLossSubmitRequest();
		deflossSynchroMain.setAccidentBook(cursor.getString(cursor.getColumnIndex("ACCIDENTBOOK")));
		deflossSynchroMain.setCarfactorycode(cursor.getString(cursor.getColumnIndex("CARFACTORYCODE")));
		deflossSynchroMain.setCarfactoryname(cursor.getString(cursor.getColumnIndex("CARFACTORYNAME")));
		deflossSynchroMain.setCarregisterdate(cursor.getString(cursor.getColumnIndex("CARREGISTERDATE")));
		deflossSynchroMain.setCarvehicledesc(cursor.getString(cursor.getColumnIndex("CARVEHICLEDESC")));
		deflossSynchroMain.setCertainHandleCode(cursor.getString(cursor.getColumnIndex("CERTAINHANDLECODE")));
		deflossSynchroMain.setCertainHandleName(cursor.getString(cursor.getColumnIndex("CERTAINHANDLENAME")));
		deflossSynchroMain.setClaimtype(cursor.getString(cursor.getColumnIndex("CLAIMTYPE")));
		deflossSynchroMain.setEntrustMobile(cursor.getString(cursor.getColumnIndex("ENTRUSTMOBILE")));
		deflossSynchroMain.setEntrustName(cursor.getString(cursor.getColumnIndex("ENTRUSTNAME")));
		deflossSynchroMain.setEstimateNo(cursor.getString(cursor.getColumnIndex("ESTIMATENO")));
		deflossSynchroMain.setExcessType(cursor.getString(cursor.getColumnIndex("EXCESSTYPE")));
		deflossSynchroMain.setInsureMobile(cursor.getString(cursor.getColumnIndex("INSUREDMOBILE")));
		deflossSynchroMain.setInsuredName(cursor.getString(cursor.getColumnIndex("INSUREDNAME")));
		deflossSynchroMain.setKindCode(cursor.getString(cursor.getColumnIndex("KINDCODE")));
		deflossSynchroMain.setLastUpdateTime(cursor.getString(cursor.getColumnIndex("LASTUPDATETIME")));
		deflossSynchroMain.setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));
		deflossSynchroMain.setPlateNo(cursor.getString(cursor.getColumnIndex("PLATENO")));
		deflossSynchroMain.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
		deflossSynchroMain.setRepairAptitude(cursor.getString(cursor.getColumnIndex("REPAIRAPTITUDE")));

		deflossSynchroMain.setRepairfactoryCode(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYCODE")));
		deflossSynchroMain.setRepairfactoryName(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYNAME")));
		deflossSynchroMain.setRepairMode(cursor.getString(cursor.getColumnIndex("REPAIRMODE")));
		deflossSynchroMain.setRepairReason(cursor.getString(cursor.getColumnIndex("REPAIRREASON")));
		deflossSynchroMain.setRepaircooperateFlag(cursor.getString(cursor.getColumnIndex("REPAIRCOOPERATEFLAG")));//
		deflossSynchroMain.setSatisfaction(cursor.getString(cursor.getColumnIndex("SATISFACTION")));
		deflossSynchroMain.setSlag(cursor.getString(cursor.getColumnIndex("SELFCONFIGFLAG")));
		deflossSynchroMain.setSubmitType(cursor.getString(cursor.getColumnIndex("SUBMITTYPE")));
		deflossSynchroMain.setSubrogateType(cursor.getString(cursor.getColumnIndex("SUBROGATETYPE")));
		deflossSynchroMain.setSumcertainloss(cursor.getString(cursor.getColumnIndex("SUMCERTAINLOSS")));
		deflossSynchroMain.setSumfitsfee(cursor.getString(cursor.getColumnIndex("SUMFITSFEE")));
		deflossSynchroMain.setSumrepairfee(cursor.getString(cursor.getColumnIndex("SUMREPAIRFEE")));
		deflossSynchroMain.setSumverirest(cursor.getString(cursor.getColumnIndex("SUMVERIREST")));
		deflossSynchroMain.setTextconent(cursor.getString(cursor.getColumnIndex("TEXTCONTENT")));
		deflossSynchroMain.setVehbrandcode(cursor.getString(cursor.getColumnIndex("VEHBRANDCODE")));
		deflossSynchroMain.setVehbrandname(cursor.getString(cursor.getColumnIndex("VEHBRANDNAME")));
		deflossSynchroMain.setVehcertainCode(cursor.getString(cursor.getColumnIndex("VEHCERTAINCODE")));
		deflossSynchroMain.setVehcertaninName(cursor.getString(cursor.getColumnIndex("VEHCERTAINNAME")));
		deflossSynchroMain.setVehfactorycode(cursor.getString(cursor.getColumnIndex("VEHFACTORYCODE")));
		deflossSynchroMain.setVehfactoryname(cursor.getString(cursor.getColumnIndex("VEHFACTORYNAME")));
		deflossSynchroMain.setVehgroupcode(cursor.getString(cursor.getColumnIndex("VEHGROUPCODE")));
		deflossSynchroMain.setVehgroupName(cursor.getString(cursor.getColumnIndex("VEHGROUPNAME")));
		deflossSynchroMain.setVehkindCode(cursor.getString(cursor.getColumnIndex("VEHKINDCODE")));
		deflossSynchroMain.setVehkindName(cursor.getString(cursor.getColumnIndex("VEHKINDNAME")));
		deflossSynchroMain.setVehsericode(cursor.getString(cursor.getColumnIndex("VEHSERICODE")));
		deflossSynchroMain.setVehserigradename(cursor.getString(cursor.getColumnIndex("VEHSERIGRADENAME")));
		deflossSynchroMain.setVehseriname(cursor.getString(cursor.getColumnIndex("VEHSERINAME")));
		deflossSynchroMain.setVehyeartype(cursor.getString(cursor.getColumnIndex("VEHYEARTYPE")));
		deflossSynchroMain.setVersion(cursor.getString(cursor.getColumnIndex("VERSION")));
		return deflossSynchroMain;
	}
	
	
	
	private static LossFitInfo toDeflossSynchroReplace(Cursor cursor){
		LossFitInfo deflossSynchroReplace = new LossFitInfo();
		deflossSynchroReplace.setLocalPrice(cursor.getString(cursor.getColumnIndex("LOCALPRICE")));
		deflossSynchroReplace.setLocalPrice001(cursor.getString(cursor.getColumnIndex("LOCALPRICE001")));
		deflossSynchroReplace.setLocalPrice002(cursor.getString(cursor.getColumnIndex("LOCALPRICE002")));
		deflossSynchroReplace.setLossCount(cursor.getString(cursor.getColumnIndex("LOSSCOUNT")));
		deflossSynchroReplace.setLossFee(cursor.getString(cursor.getColumnIndex("LOSSFEE")));
		deflossSynchroReplace.setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));
		deflossSynchroReplace.setOriginalId(cursor.getString(cursor.getColumnIndex("ORIGINALID")));
		deflossSynchroReplace.setOriginalName(cursor.getString(cursor.getColumnIndex("ORIGINALNAME")));
		deflossSynchroReplace.setPartgroupCode(cursor.getString(cursor.getColumnIndex("PARTGROUPCODE")));
		deflossSynchroReplace.setPartgroupName(cursor.getString(cursor.getColumnIndex("PARTGROUPNAME")));
		deflossSynchroReplace.setPartid(cursor.getString(cursor.getColumnIndex("PARTID")));
		deflossSynchroReplace.setPartstandard(cursor.getString(cursor.getColumnIndex("PARTSTANDARD")));
		deflossSynchroReplace.setPartstandardCode(cursor.getString(cursor.getColumnIndex("PARTSTANDARDCODE")));
		deflossSynchroReplace.setPriceModel(cursor.getString(cursor.getColumnIndex("PRICEMODEL")));
		deflossSynchroReplace.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
		deflossSynchroReplace.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
		deflossSynchroReplace.setRestFee(cursor.getString(cursor.getColumnIndex("RESTFEE")));
		deflossSynchroReplace.setSelfconfigflag(cursor.getString(cursor.getColumnIndex("SELFCONFIGFLAG")));
		deflossSynchroReplace.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		deflossSynchroReplace.setSystemPrice(cursor.getString(cursor.getColumnIndex("SYSTEMPRICE")));
		deflossSynchroReplace.setSystemPrice001(cursor.getString(cursor.getColumnIndex("SYSTEMPRICE001")));
		deflossSynchroReplace.setSystemPrice002(cursor.getString(cursor.getColumnIndex("SYSTEMPRICE002")));
		return deflossSynchroReplace;
	}
	
	private static LossRepairInfo toDeflossSynchroRepair(Cursor cursor){
		LossRepairInfo deflossSynchroRepair = new LossRepairInfo();
		deflossSynchroRepair.setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));
		deflossSynchroRepair.setPriceModel(cursor.getString(cursor.getColumnIndex("PRICEMODEL")));
		deflossSynchroRepair.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
		deflossSynchroRepair.setRepairCode(cursor.getString(cursor.getColumnIndex("REPAIRCODE")));
		deflossSynchroRepair.setRepairfee(cursor.getString(cursor.getColumnIndex("REPAIRFEE")));
		deflossSynchroRepair.setRepairId(cursor.getString(cursor.getColumnIndex("REPAIRID")));
		deflossSynchroRepair.setRepairitemCode(cursor.getString(cursor.getColumnIndex("REPAIRITEMCODE")));
		deflossSynchroRepair.setRepairitemName(cursor.getString(cursor.getColumnIndex("REPAIRITEMNAME")));
		deflossSynchroRepair.setRepairName(cursor.getString(cursor.getColumnIndex("REPAIRNAME")));
		deflossSynchroRepair.setSelfConfigFlag(cursor.getString(cursor.getColumnIndex("SELFCONFIGFLAG")));
		deflossSynchroRepair.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		deflossSynchroRepair.setSystemprice(cursor.getString(cursor.getColumnIndex("SYSTEMPRICE")));//add haoyun by 20130420
		return deflossSynchroRepair;
	}
	
	private static ContentValues toValues(VerifyLossSubmitRequest deflossSynchroMain){
		ContentValues values = new ContentValues();
		values.put("AccidentBook", deflossSynchroMain.getAccidentBook());
		values.put("CarFactoryCode", deflossSynchroMain.getCarfactorycode());
		values.put("CarFactoryName", deflossSynchroMain.getCarfactoryname());
		values.put("CarRegisterDate", deflossSynchroMain.getCarregisterdate());
		values.put("CarVehicleDesc", deflossSynchroMain.getCarvehicledesc());
		values.put("CertainHandleCode", deflossSynchroMain.getCertainHandleCode());
		values.put("CertainHandleName", deflossSynchroMain.getCertainHandleName());
		values.put("ClaimType", deflossSynchroMain.getClaimtype());
		values.put("EntrustMobile", deflossSynchroMain.getEntrustMobile());
		values.put("EntrustName", deflossSynchroMain.getEntrustName());
		values.put("EstimateNo", deflossSynchroMain.getEstimateNo());
		values.put("ExcessType", deflossSynchroMain.getExcessType());
		values.put("InsuredMobile", deflossSynchroMain.getInsureMobile());
		values.put("InsuredName", deflossSynchroMain.getInsuredName());
		values.put("lastUpdateTime", DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss_SSS));
		values.put("LossNo", deflossSynchroMain.getLossNo());
		values.put("Plateno", deflossSynchroMain.getPlateNo());
		values.put("RegistNo", deflossSynchroMain.getRegistNo());
		values.put("RepairAptitude", deflossSynchroMain.getRepairAptitude());
		values.put("RepairCooperateFlag", deflossSynchroMain.getRepaircooperateFlag());
		values.put("RepairFactoryCode", deflossSynchroMain.getRepairfactoryCode());
		values.put("RepairFactoryName", deflossSynchroMain.getRepairfactoryName());
		values.put("RepairMode", deflossSynchroMain.getRepairMode());
		values.put("RepairReason", deflossSynchroMain.getRepairReason());
		values.put("Satisfaction", deflossSynchroMain.getSatisfaction());
		values.put("SelfConfigFlag", deflossSynchroMain.getSlag());
		values.put("SubmitType", deflossSynchroMain.getSubmitType());
		values.put("SubRoGateType", deflossSynchroMain.getSubrogateType());
		values.put("SumCertainLoss", deflossSynchroMain.getSumcertainloss());
		values.put("SumFitsFee", deflossSynchroMain.getSumfitsfee());
		values.put("SumRepairFee", deflossSynchroMain.getSumrepairfee());
		values.put("SumVerirest", deflossSynchroMain.getSumverirest());
		values.put("TextContent", deflossSynchroMain.getTextconent());
		values.put("VehBrandCode", deflossSynchroMain.getVehbrandcode());
		values.put("VehBrandName", deflossSynchroMain.getVehbrandname());
		values.put("VehCertainCode", deflossSynchroMain.getVehcertainCode());
		values.put("VehCertainName", deflossSynchroMain.getVehcertaninName());
		values.put("VehFactoryCode", deflossSynchroMain.getVehfactorycode());
		values.put("VehFactoryName", deflossSynchroMain.getVehfactoryname());
		values.put("VehGroupCode", deflossSynchroMain.getVehgroupcode());
		values.put("VehGroupName", deflossSynchroMain.getVehgroupName());
		values.put("VehKindCode", deflossSynchroMain.getVehkindCode());
		values.put("VehKindName", deflossSynchroMain.getVehkindName());
		values.put("VehSeriCode", deflossSynchroMain.getVehsericode());
		values.put("VehSeriGradeName", deflossSynchroMain.getVehserigradename());
		values.put("VehSeriName", deflossSynchroMain.getVehseriname());
		values.put("VehYearType", deflossSynchroMain.getVehyeartype());
		values.put("Version", deflossSynchroMain.getVersion());
		return values;
	}
	
	private static ContentValues toValues(LossFitInfo deflossSynchroReplace,String registNo,String lossNo){
		ContentValues values = new ContentValues();
		values.put("LocalPrice", deflossSynchroReplace.getLocalPrice());
		values.put("LocalPrice001", deflossSynchroReplace.getLocalPrice001());
		values.put("LocalPrice002", deflossSynchroReplace.getLocalPrice002());
		values.put("LossCount", deflossSynchroReplace.getLossCount());
		values.put("LossFee", deflossSynchroReplace.getLossFee());
		values.put("LossNo", lossNo);
		values.put("OriginalId", deflossSynchroReplace.getOriginalId());
		values.put("OriginalName", deflossSynchroReplace.getOriginalName());
		values.put("PartGroupCode", deflossSynchroReplace.getPartgroupCode());
		values.put("PartGroupName", deflossSynchroReplace.getPartgroupName());
		values.put("PartId", deflossSynchroReplace.getPartid());
		values.put("PartStandard", deflossSynchroReplace.getPartstandard());
		values.put("PartStandardCode", deflossSynchroReplace.getPartstandardCode());
		values.put("PriceModel", deflossSynchroReplace.getPriceModel());
		values.put("RegistNo", registNo);
		values.put("Remark", deflossSynchroReplace.getRemark());
		values.put("RestFee", deflossSynchroReplace.getRestFee());
		values.put("SelfConfigFlag", deflossSynchroReplace.getSelfconfigflag());
		values.put("SerialNo", deflossSynchroReplace.getSerialNo());
		values.put("SystemPrice", deflossSynchroReplace.getSystemPrice());
		values.put("SystemPrice001", deflossSynchroReplace.getSystemPrice001());
		values.put("SystemPrice002", deflossSynchroReplace.getSystemPrice002());
		return values;
	}
	
	private static ContentValues toValues(LossRepairInfo deflossSynchroRepair,String registNo,String lossNo){
		ContentValues values = new ContentValues();
		values.put("LossNo", lossNo);
		values.put("PriceModel", deflossSynchroRepair.getPriceModel());
		values.put("RegistNo", registNo);
		values.put("RepairCode", deflossSynchroRepair.getRepairCode());
		values.put("RepairFee", deflossSynchroRepair.getRepairfee());
		values.put("RepairId", deflossSynchroRepair.getRepairId());
		values.put("RepairItemCode", deflossSynchroRepair.getRepairitemCode());
		values.put("RepairItemName", deflossSynchroRepair.getRepairitemName());
		values.put("RepairName", deflossSynchroRepair.getRepairName());
		values.put("SelfConfigFlag", deflossSynchroRepair.getSelfConfigFlag());
		values.put("SerialNo", deflossSynchroRepair.getSerialNo());
		values.put("SYSTEMPRICE", deflossSynchroRepair.getSystemprice());//add haoyun by 20130420
		return values;
	}
}
