package com.sinosoftyingda.fastclaim.common.db.access;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.model.CarLossInfo;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.DefLossCarInfo;
import com.sinosoftyingda.fastclaim.common.model.DefLossContent;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.ItemKind;
import com.sinosoftyingda.fastclaim.common.model.SurveyKeyPoint;
import com.sinosoftyingda.fastclaim.common.model.TaskInfo;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;

public class CertainLossInfoAccess {
	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	public static void insertOrUpdate(SQLiteDatabase db, DefLossInfoQueryResponse loss, boolean isUpdate) {
		ContentValues cv = null;
		try {
			db.beginTransaction();
			cv = new ContentValues();
			if (loss.getRegist() != null) {
				cv.put("REGISTNO", loss.getRegist().getRegistNo());
				cv.put("LOSSNO", loss.getRegist().getLossNo());
				cv.put("POLICYCOMCODE", loss.getRegist().getPolicyComCode());
				cv.put("LICENSENO", loss.getRegist().getLicenseNo());
				cv.put("BRANDNAME", loss.getRegist().getBrandName());
				cv.put("DISPATCHPTIME", loss.getRegist().getDispatchpTime());
				cv.put("REPORTTIME", loss.getRegist().getReportTime());
				cv.put("OUTOFPLACE", loss.getRegist().getOutofPlace());
				cv.put("DRIVERNAME", loss.getRegist().getDriverName());
				cv.put("PROMPTMESSAGE", loss.getRegist().getPromptMessage());
				cv.put("INSUREDNAME", loss.getRegist().getInsuredName());
				cv.put("INSUREDMOBILE", loss.getRegist().getInsuredMobile());
				cv.put("ENTRUSTNAME", loss.getRegist().getEntrustName());
				cv.put("ENTRUSTMOBILE", loss.getRegist().getEntrustMobile());
				cv.put("DAMAGEDAYP", loss.getRegist().getDamageDayp()); 
				
				String where = "REGISTNO=? and LOSSNO=?";
				String[] whereValue = { loss.getRegist().getRegistNo(), loss.getRegist().getLossNo() };
				if (DBUtils.isExist(db, "CertainLossMain", where, whereValue)) {
					db.update("CertainLossMain", cv, "REGISTNO=? and LOSSNO=?", whereValue);
				} else {
					db.insert("CertainLossMain", null, cv);
				}
				addItemTable(db, loss);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			cv.clear();
			db.close();
		}
	}

	/**
	 * 添加其他信息
	 * 
	 * @param db
	 * @param loss
	 */
	private static void addItemTable(SQLiteDatabase db, DefLossInfoQueryResponse loss) {
		String registNo = loss.getRegist().getRegistNo();
		String lossNo = loss.getRegist().getLossNo();
		/**
		 * 查勘记录以报案号+标的号为主键
		 */
		insertOrUpdateCheckRecord(db, registNo, lossNo, loss.getSurveyKeyPoint());
		/**
		 * 车辆车损信息
		 */
		DBUtils.delete(db, "ClCarInfo", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertCarInfo(db, registNo, lossNo, loss.getCarLossInfos());
		/**
		 * 任务信息
		 */
		DBUtils.delete(db, "ClTaskInfo", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertOrUpdateTaskInfo(db, registNo, lossNo, loss.getTaskInfo());
		/**
		 * 定损对象
		 */
		DBUtils.delete(db, "ClObject", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertObject(db, registNo, lossNo, loss.getDefLossCarInfos());
		/**
		 * 定损内容
		 */

		DBUtils.delete(db, "ClContent", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertOrUpdateContent(db, registNo, lossNo, loss.getDefLossContent());

		/**
		 * 查勘扩展信息
		 */
		DBUtils.delete(db, "ClCheckExt", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertCheckExts(db, registNo, lossNo, loss.getCheckExt());
		/**
		 * 险别
		 */
		DBUtils.delete(db, "ClItemKind", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
		insertItemKinds(db, registNo, lossNo, loss.getItemKinds());
	}

	/**
	 * 查勘记录
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param skp
	 */
	private static void insertOrUpdateCheckRecord(SQLiteDatabase db, String registNo, String lossNo, SurveyKeyPoint skp) {
		if (skp != null) {
			ContentValues cv = null;
			cv = new ContentValues();
			cv.put("REGISTNO", registNo);
			cv.put("LOSSNO", lossNo);
			cv.put("FIRSTSITEFLAG", skp.getFirstsiteFlag());
			cv.put("DAMAGECODE", skp.getDamageCode());
			cv.put("DAMAGENAME", skp.getDamageName());
			cv.put("INDEMNITYDUTY", skp.getIndemnityDuty());
			cv.put("CLAIMTYPE", skp.getClaimType());
			cv.put("ISCOMMONCLAIM", skp.getIsCommonClaim());
			cv.put("SUBROGATETYPE", skp.getSubrogateType());
			cv.put("ACCIDENTBOOK", skp.getAccidentBook());
			cv.put("ACCIDENT", skp.getAccident());
			cv.put("REMARK", skp.getRemark());
			cv.put("CHECKREPORT", skp.getCheckReport());

			String where = "REGISTNO=? and LOSSNO=?";
			String[] whereValue = { registNo, lossNo };
			if (DBUtils.isExist(db, "ClCheckRecord", where, whereValue)) {
				db.update("ClCheckRecord", cv, where, whereValue);
			} else {
				db.insert("ClCheckRecord", null, cv);
			}
			cv.clear();
		}
	}

	/**
	 * 车辆信息
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param carInfos
	 */
	private static void insertCarInfo(SQLiteDatabase db, String registNo, String lossNo, List<CarLossInfo> carInfos) {
		ContentValues cv = null;
		if (carInfos != null && carInfos.size() >= 1) {
			for (int i = 0; i < carInfos.size(); i++) {
				cv = new ContentValues();
				cv.put("REGISTNO", registNo);
				cv.put("LOSSNO", lossNo);
				cv.put("SERIALNO", i);
				cv.put("INSURECARFLAG", carInfos.get(i).getInsureCarFlag());
				cv.put("LICENSENO", carInfos.get(i).getLicenseNo());
				cv.put("DEFINELOSSPERSON", carInfos.get(i).getDefineLossPerson());
				cv.put("DEFINELOSSAMOUT", carInfos.get(i).getDefineLossAmount());
				db.insert("ClCarInfo", null, cv);
			}
			cv.clear();
		}
	}

	/**
	 * 任务信息
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param ti
	 */
	private static void insertOrUpdateTaskInfo(SQLiteDatabase db, String registNo, String lossNo, TaskInfo ti) {
		ContentValues cv = null;
		cv = new ContentValues();
		cv.put("REGISTNO", registNo);
		cv.put("LOSSNO", lossNo);
		cv.put("TASKRECEIPTTIME", ti.getTaskReceiptTime());
		cv.put("ARRIVESCENETIME", ti.getArrivesceneTime());
		cv.put("LINKCUSTOMTIME", ti.getLinkCustomTime());
		cv.put("TASKHANDTIME", ti.getTaskHandTime());

		String where = "REGISTNO=? and LOSSNO=?";
		String[] whereValue = { registNo, lossNo };
		if (DBUtils.isExist(db, "ClTaskInfo", where, whereValue)) {
			db.update("ClTaskInfo", cv, where, whereValue);
		} else {
			db.insert("ClTaskInfo", null, cv);
		}
		cv.clear();
	}

	/**
	 * 定损对象
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param dcls
	 */
	private static void insertObject(SQLiteDatabase db, String registNo, String lossNo, List<DefLossCarInfo> dcls) {
		ContentValues cv = null;
		if (dcls != null && dcls.size() >= 1) {
			for (int i = 0; i < dcls.size(); i++) {
				cv = new ContentValues();
				cv.put("REGISTNO", registNo);
				cv.put("LOSSNO", lossNo);
				cv.put("SERIALNO", i);
				cv.put("INSURECARFLAG", dcls.get(i).getInsurecarFlag());
				cv.put("LICENSENO", dcls.get(i).getLicenseNo());
				cv.put("CARFRAMENO", dcls.get(i).getCarframeNo());
				cv.put("BRANDNAME", dcls.get(i).getBrandName());
				cv.put("NEWPRUCHASEAMOUNT", dcls.get(i).getNewPruchaseAmount());
				cv.put("CARVEHICLEDESC", dcls.get(i).getCarVehicleDesc());
				cv.put("CARFACTORYCODE", dcls.get(i).getCarFactoryCode());
				cv.put("CARFACTORYNAME", dcls.get(i).getCarFactoryName());
				cv.put("CARREGISTERDATE", dcls.get(i).getCarRegisterDate());
				cv.put("INSUREVEHICODE", dcls.get(i).getInsurevehiCode());
				db.insert("ClObject", null, cv);
			}
			cv.clear();
		}
	}

	/**
	 * 定损内容
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param dlc
	 */
	private static void insertOrUpdateContent(SQLiteDatabase db, String registNo, String lossNo, DefLossContent dlc) {
		if (dlc == null) {
			return;
		}
		ContentValues cv = null;
		cv = new ContentValues();
		cv.put("REGISTNO", registNo);
		cv.put("LOSSNO", lossNo);
		cv.put("REPAIRFACTORYCODE", dlc.getRepairFactoryCode());
		cv.put("REPAIRFACTORYNAME", dlc.getRepairFactoryName());
		cv.put("REPAIRCOOPERATEFLAG", dlc.getRepairCooperateFlag());
		cv.put("REPAIRMODE", dlc.getRepairMode());
		cv.put("REPAIRAPTITUDE", dlc.getRepairapTitude());
		cv.put("DEFLOSSRISKCODE", dlc.getDefLossRiskCode());
		cv.put("SUMFITSFEE", dlc.getSumfitsfee());
		cv.put("SUMREPAIRFEE", dlc.getSumRepairfee());
		cv.put("SUMREST", dlc.getSumRest());
		cv.put("SUMCERTAINLOSS", dlc.getSumCertainLoss());
		cv.put("SUMCERTAINLOSSCH", dlc.getSumCerTainLossCh());
		cv.put("DEFLOSSADVISE", dlc.getDefLossAdvise());
		cv.put("UNDWRTREMARK", dlc.getUndwrtRemark());
		cv.put("SATISFIEDDEGREE", dlc.getSatisfieddegree());
		cv.put("REPAIRREASON", dlc.getRepairReason());
		cv.put("SHIJIUFEE", dlc.getShijiufee());	//施救费 add by yxf 20140211 reason:增加施救费字段
		String where = "REGISTNO=? and LOSSNO=?";
		String[] whereValue = { registNo, lossNo };
		if (DBUtils.isExist(db, "ClContent", where, whereValue)) {
			db.update("ClContent", cv, where, whereValue);
		} else {
			db.insert("ClContent", null, cv);
		}
		cv.clear();
	}

	/**
	 * 查勘扩展信息
	 * 
	 * @param db
	 * @param registNo
	 * @param lossNo
	 * @param checkExts
	 */
	private static void insertCheckExts(SQLiteDatabase db, String registNo, String lossNo, List<CheckExt> checkExts) {
		if (checkExts != null && checkExts.size() >= 1) {
			ContentValues cv = null;
			for (int i = 0; i < checkExts.size(); i++) {
				cv = new ContentValues();
				cv.put("REGISTNO", registNo);
				cv.put("LOSSNO", lossNo);
				cv.put("SERIALNO", i);
				cv.put("CHECKKERNELNAME", checkExts.get(i).getCheckKernelName());
				cv.put("CHECKKERNELSELECT", checkExts.get(i).getCheckKernelSelect());
				cv.put("CHECKKERNELTYPE", checkExts.get(i).getCheckKernelType());
				cv.put("CHECKKERNELCODE", checkExts.get(i).getCheckKernelCode());
				cv.put("CHECKEXTREMARK", checkExts.get(i).getCheckExtRemark());
				db.insert("ClCheckExt", null, cv);
			}
			cv.clear();
		}
	}

	/**
	 * 新增险别信息
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertItemKinds(SQLiteDatabase db, String registNo, String lossNo, List<ItemKind> itemKinds) {
		if (itemKinds != null && itemKinds.size() >= 1) {
			ContentValues cv = null;
			for (int i = 0; i < itemKinds.size(); i++) {
				cv = new ContentValues();
				cv.put("REGISTNO", registNo);
				cv.put("LOSSNO", lossNo);
				cv.put("ITEMCODE", itemKinds.get(i).getKindCode());
				cv.put("ITEMNAME", itemKinds.get(i).getKindName());
				db.insert("ClItemKind", null, cv);
			}
			cv.clear();
		}
	}

	/**
	 * 获取查勘详细信息任务
	 * 
	 * @param db
	 * @return
	 */
	public static DefLossInfoQueryResponse getLossTaskQuery(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "REGISTNO=? and LOSSNO=?";
		Cursor cursor = db.query("CertainLossMain", null, selection, new String[] { registNo, lossNo }, null, null, null);
		DefLossInfoQueryResponse response = toLossTaskQuery(db, cursor);
		return response;

	}

	private static DefLossInfoQueryResponse toLossTaskQuery(SQLiteDatabase db, Cursor cursor) {
		DefLossInfoQueryResponse taskQuery = null;
		try {
			if (cursor.moveToNext()) {
				taskQuery = new DefLossInfoQueryResponse();
				taskQuery.getRegist().setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
				taskQuery.getRegist().setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));
				taskQuery.getRegist().setPolicyComCode(cursor.getString(cursor.getColumnIndex("POLICYCOMCODE")));
				taskQuery.getRegist().setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
				taskQuery.getRegist().setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
				taskQuery.getRegist().setDispatchpTime(cursor.getString(cursor.getColumnIndex("DISPATCHPTIME")));
				taskQuery.getRegist().setReportTime(cursor.getString(cursor.getColumnIndex("REPORTTIME")));
				taskQuery.getRegist().setOutofPlace(cursor.getString(cursor.getColumnIndex("OUTOFPLACE")));
				taskQuery.getRegist().setDriverName(cursor.getString(cursor.getColumnIndex("DRIVERNAME")));
				taskQuery.getRegist().setPromptMessage(cursor.getString(cursor.getColumnIndex("PROMPTMESSAGE")));
				taskQuery.getRegist().setInsuredName(cursor.getString(cursor.getColumnIndex("INSUREDNAME")));
				taskQuery.getRegist().setInsuredMobile(cursor.getString(cursor.getColumnIndex("INSUREDMOBILE")));
				taskQuery.getRegist().setEntrustName(cursor.getString(cursor.getColumnIndex("ENTRUSTNAME")));
				taskQuery.getRegist().setEntrustMobile(cursor.getString(cursor.getColumnIndex("ENTRUSTMOBILE")));
				taskQuery.getRegist().setDamageDayp(cursor.getString(cursor.getColumnIndex("DAMAGEDAYP")));

				if (taskQuery.getRegist() != null) {
					String registNo = taskQuery.getRegist().getRegistNo();
					String lossNo = taskQuery.getRegist().getLossNo();
					/**
					 * 查勘要点
					 */
					taskQuery.setSurveyKeyPoint(getCheckRecord(db, registNo, lossNo));
					/**
					 * 案件涉损
					 */
					taskQuery.setCarLossInfos(getCarLossInfo(db, registNo, lossNo));
					/**
					 * 任务信息
					 */
					taskQuery.setTaskInfo(getTaskInfo(db, registNo, lossNo));
					/**
					 * 定损车信息
					 */
					taskQuery.setDefLossCarInfos(getDefLossCarInfo(db, registNo, lossNo));
					/**
					 * 定损内容
					 */
					taskQuery.setDefLossContent(getDefLossContent(db, registNo, lossNo));
					/**
					 * 扩展信息
					 */
					taskQuery.setCheckExt(getCheckExts(db, registNo, lossNo));
					List<CheckExt> checkExts = CommonUtils.getCheckExtS();
					for (int i = 0; i < taskQuery.getCheckExt().size(); i++) {
						for (int j = 0; j < checkExts.size(); j++) {
							if (taskQuery.getCheckExt().get(i).getCheckKernelCode().equals(checkExts.get(j).getCheckKernelCode())) {
								checkExts.get(j).setCheckKernelSelect(taskQuery.getCheckExt().get(i).getCheckKernelSelect());
								checkExts.get(j).setCheckKernelType(taskQuery.getCheckExt().get(i).getCheckKernelType());
								checkExts.get(j).setCheckExtRemark(taskQuery.getCheckExt().get(i).getCheckExtRemark());
							}
						}

					}
					taskQuery.setCheckExt(checkExts);
					/**
					 * 险别信息
					 */
					taskQuery.setItemKinds(getItemKinds(db, registNo, lossNo));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return taskQuery;
	}

	/**
	 * 获取险别信息任务
	 * 
	 * @param db
	 * @return
	 */
	private static ArrayList<ItemKind> getItemKinds(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClItemKind", null, selection, new String[] { registNo, lossNo }, null, null, null);
		ArrayList<ItemKind> itemKinds = new ArrayList<ItemKind>();
		while (cursor.moveToNext()) {
			itemKinds.add(toItemKind(cursor));
		}
		cursor.close();
		return itemKinds;
	}

	private static ItemKind toItemKind(Cursor cursor) {
		ItemKind itemKind = new ItemKind();
		itemKind.setKindCode(cursor.getString(cursor.getColumnIndex("ITEMCODE")));
		itemKind.setKindName(cursor.getString(cursor.getColumnIndex("ITEMNAME")));
		return itemKind;
	}

	/**
	 * 获取扩展信息任务
	 * 
	 * @param db
	 * @return
	 */
	private static ArrayList<CheckExt> getCheckExts(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClCheckExt", null, selection, new String[] { registNo, lossNo }, null, null, null);
		ArrayList<CheckExt> taskQuerys = new ArrayList<CheckExt>();
		while (cursor.moveToNext()) {
			taskQuerys.add(toCheckExt(cursor));
		}
		cursor.close();
		return taskQuerys;
	}

	private static CheckExt toCheckExt(Cursor cursor) {
		CheckExt checkExt = new CheckExt();
		checkExt.setCheckKernelName(cursor.getString(cursor.getColumnIndex("CHECKKERNELNAME")));
		checkExt.setCheckKernelSelect(cursor.getString(cursor.getColumnIndex("CHECKKERNELSELECT")));
		checkExt.setCheckKernelType(cursor.getString(cursor.getColumnIndex("CHECKKERNELTYPE")));
		checkExt.setCheckKernelCode(cursor.getString(cursor.getColumnIndex("CHECKKERNELCODE")));
		checkExt.setCheckExtRemark(cursor.getString(cursor.getColumnIndex("CHECKEXTREMARK")));
		checkExt.setSerialNo("" + cursor.getInt(cursor.getColumnIndex("SERIALNO")));
		return checkExt;
	}

	/**
	 * 定损车辆信息
	 * 
	 * @param db
	 * @return
	 */
	private static List<DefLossCarInfo> getDefLossCarInfo(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClObject", null, selection, new String[] { registNo, lossNo }, null, null, null);
		List<DefLossCarInfo> defLossCarInfo = new ArrayList<DefLossCarInfo>();
		while (cursor.moveToNext()) {
			defLossCarInfo.add(toDefLossCarInfo(cursor));
		}
		cursor.close();
		return defLossCarInfo;
	}

	private static DefLossCarInfo toDefLossCarInfo(Cursor cursor) {
		DefLossCarInfo defLossCarInfo = new DefLossCarInfo();
		defLossCarInfo.setInsurecarFlag(cursor.getString(cursor.getColumnIndex("INSURECARFLAG")));
		defLossCarInfo.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
		defLossCarInfo.setCarframeNo(cursor.getString(cursor.getColumnIndex("CARFRAMENO")));
		defLossCarInfo.setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
		defLossCarInfo.setNewPruchaseAmount(cursor.getString(cursor.getColumnIndex("NEWPRUCHASEAMOUNT")));
		defLossCarInfo.setCarVehicleDesc(cursor.getString(cursor.getColumnIndex("CARVEHICLEDESC")));
		defLossCarInfo.setCarFactoryCode(cursor.getString(cursor.getColumnIndex("CARFACTORYCODE")));
		defLossCarInfo.setCarFactoryName(cursor.getString(cursor.getColumnIndex("CARFACTORYNAME")));
		defLossCarInfo.setCarRegisterDate(cursor.getString(cursor.getColumnIndex("CARREGISTERDATE")));
		defLossCarInfo.setInsurevehiCode(cursor.getString(cursor.getColumnIndex("INSUREVEHICODE")));
		return defLossCarInfo;
	}

	/**
	 * 车辆信息
	 * 
	 * @param db
	 * @return
	 */
	private static List<CarLossInfo> getCarLossInfo(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClCarInfo", null, selection, new String[] { registNo, lossNo }, null, null, null);
		List<CarLossInfo> carLossInfos = new ArrayList<CarLossInfo>();
		while (cursor.moveToNext()) {
			carLossInfos.add(toCarLossInfo(cursor));
		}
		cursor.close();
		return carLossInfos;
	}

	private static CarLossInfo toCarLossInfo(Cursor cursor) {
		CarLossInfo carLossInfo = new CarLossInfo();
		carLossInfo.setInsureCarFlag(cursor.getString(cursor.getColumnIndex("INSURECARFLAG")));
		carLossInfo.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
		carLossInfo.setDefineLossPerson(cursor.getString(cursor.getColumnIndex("DEFINELOSSPERSON")));
		carLossInfo.setDefineLossAmount(cursor.getString(cursor.getColumnIndex("DEFINELOSSAMOUT")));

		return carLossInfo;
	}

	/**
	 * 定损内容
	 * 
	 * @param db
	 * @return
	 */
	private static DefLossContent getDefLossContent(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClContent", null, selection, new String[] { registNo, lossNo }, null, null, null);
		DefLossContent defLossContent = new DefLossContent();
		while (cursor.moveToNext()) {
			defLossContent = toDefLossContent(cursor);
		}
		cursor.close();
		return defLossContent;
	}

	private static DefLossContent toDefLossContent(Cursor cursor) {
		DefLossContent defLossContent = new DefLossContent();
		defLossContent.setRepairFactoryCode(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYCODE")));
		defLossContent.setRepairFactoryName(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYNAME")));
		defLossContent.setRepairCooperateFlag(cursor.getString(cursor.getColumnIndex("REPAIRCOOPERATEFLAG")));
		defLossContent.setRepairMode(cursor.getString(cursor.getColumnIndex("REPAIRMODE")));
		defLossContent.setRepairReason(cursor.getString(cursor.getColumnIndex("REPAIRREASON")));
		defLossContent.setRepairapTitude(cursor.getString(cursor.getColumnIndex("REPAIRAPTITUDE")));
		defLossContent.setDefLossRiskCode(cursor.getString(cursor.getColumnIndex("DEFLOSSRISKCODE")));
		defLossContent.setSumfitsfee(cursor.getString(cursor.getColumnIndex("SUMFITSFEE")));
		defLossContent.setSumRepairfee(cursor.getString(cursor.getColumnIndex("SUMREPAIRFEE")));
		defLossContent.setSumRest(cursor.getString(cursor.getColumnIndex("SUMREST")));
		defLossContent.setSumCertainLoss(cursor.getString(cursor.getColumnIndex("SUMCERTAINLOSS")));
		defLossContent.setSumCerTainLossCh(cursor.getString(cursor.getColumnIndex("SUMCERTAINLOSSCH")));
		defLossContent.setDefLossAdvise(cursor.getString(cursor.getColumnIndex("DEFLOSSADVISE")));
		defLossContent.setUndwrtRemark(cursor.getString(cursor.getColumnIndex("UNDWRTREMARK")));
		defLossContent.setSatisfieddegree(cursor.getString(cursor.getColumnIndex("SATISFIEDDEGREE")));
		defLossContent.setShijiufee(cursor.getString(cursor.getColumnIndex("SHIJIUFEE")));	//施救费 add by yxf 20140211 reason:增加施救费字段
		return defLossContent;
	}

	/**
	 * 任务信息
	 * 
	 * @param db
	 * @return
	 */
	private static TaskInfo getTaskInfo(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClTaskInfo", null, selection, new String[] { registNo, lossNo }, null, null, null);
		TaskInfo taskInfo = new TaskInfo();
		while (cursor.moveToNext()) {
			taskInfo = toTaskInfo(cursor);
		}
		cursor.close();
		return taskInfo;
	}

	private static TaskInfo toTaskInfo(Cursor cursor) {
		TaskInfo taskInfo = new TaskInfo();
		taskInfo.setTaskReceiptTime(cursor.getString(cursor.getColumnIndex("TASKRECEIPTTIME")));
		taskInfo.setArrivesceneTime(cursor.getString(cursor.getColumnIndex("ARRIVESCENETIME")));
		taskInfo.setLinkCustomTime(cursor.getString(cursor.getColumnIndex("LINKCUSTOMTIME")));
		taskInfo.setTaskHandTime(cursor.getString(cursor.getColumnIndex("TASKHANDTIME")));
		return taskInfo;
	}

	/**
	 * 获取要点信息任务
	 * 
	 * @param db
	 * @return
	 */
	private static SurveyKeyPoint getCheckRecord(SQLiteDatabase db, String registNo, String lossNo) {
		String selection = "registNo=? and lossNo=?";
		Cursor cursor = db.query("ClCheckRecord", null, selection, new String[] { registNo, lossNo }, null, null, null);
		SurveyKeyPoint skp = new SurveyKeyPoint();
		while (cursor.moveToNext()) {
			skp = toSurveyKeyPoint(cursor);
		}
		cursor.close();
		return skp;
	}

	private static SurveyKeyPoint toSurveyKeyPoint(Cursor cursor) {
		SurveyKeyPoint surveyKeyPoint = new SurveyKeyPoint();
		surveyKeyPoint.setFirstsiteFlag(cursor.getString(cursor.getColumnIndex("FIRSTSITEFLAG")));
		surveyKeyPoint.setDamageCode(cursor.getString(cursor.getColumnIndex("DAMAGECODE")));
		surveyKeyPoint.setDamageName(cursor.getString(cursor.getColumnIndex("DAMAGENAME")));
		surveyKeyPoint.setIndemnityDuty(cursor.getString(cursor.getColumnIndex("INDEMNITYDUTY")));
		surveyKeyPoint.setClaimType(cursor.getString(cursor.getColumnIndex("CLAIMTYPE")));
		surveyKeyPoint.setIsCommonClaim(cursor.getString(cursor.getColumnIndex("ISCOMMONCLAIM")));
		surveyKeyPoint.setSubrogateType(cursor.getString(cursor.getColumnIndex("SUBROGATETYPE")));
		surveyKeyPoint.setAccidentBook(cursor.getString(cursor.getColumnIndex("ACCIDENTBOOK")));
		surveyKeyPoint.setAccident(cursor.getString(cursor.getColumnIndex("ACCIDENT")));
		surveyKeyPoint.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
		surveyKeyPoint.setCheckReport(cursor.getString(cursor.getColumnIndex("CHECKREPORT")));
		return surveyKeyPoint;
	}

}
