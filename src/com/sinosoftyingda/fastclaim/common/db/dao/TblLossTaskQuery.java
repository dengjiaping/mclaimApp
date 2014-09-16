//package com.sinosoftyingda.fastclaim.common.db.dao;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.sinosoftyingda.fastclaim.common.model.CarLossInfo;
//import com.sinosoftyingda.fastclaim.common.model.CheckExt;
//import com.sinosoftyingda.fastclaim.common.model.DefLossCarInfo;
//import com.sinosoftyingda.fastclaim.common.model.DefLossContent;
//import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
//import com.sinosoftyingda.fastclaim.common.model.ItemKind;
//import com.sinosoftyingda.fastclaim.common.model.SurveyKeyPoint;
//import com.sinosoftyingda.fastclaim.common.model.TaskInfo;
//import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
//
///**
// * 定损详细信息DB
// * 
// * @author haoyun 20130321
// * 
// */
//public class TblLossTaskQuery {
//	/**
//	 * 新增或者更新数据
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	public static void insertOrUpdate(SQLiteDatabase db, DefLossInfoQueryResponse loss, boolean isUpdate) {
//		ContentValues cv = null;
//		try {
//			db.beginTransaction();
//
//			cv = new ContentValues();
//			cv.put("REGISTNO", loss.getRegist().getRegistNo());
//			cv.put("LOSSNO", loss.getRegist().getLossNo());
//			cv.put("POLICYCOMCODE", loss.getRegist().getPolicyComCode());
//			cv.put("LICENSENO", loss.getRegist().getLicenseNo());
//			cv.put("BRANDNAME", loss.getRegist().getBrandName());
//			cv.put("DISPATCHPTIME", loss.getRegist().getDispatchpTime());
//			cv.put("REPORTTIME", loss.getRegist().getReportTime());
//			cv.put("OUTOFPLACE", loss.getRegist().getOutofPlace());
//			cv.put("DRIVERNAME", loss.getRegist().getDriverName());
//			cv.put("PROMPTMESSAGE", loss.getRegist().getPromptMessage());
//			cv.put("INSUREDNAME", loss.getRegist().getInsuredName());
//			cv.put("INSUREDMOBILE", loss.getRegist().getInsuredMobile());
//			cv.put("ENTRUSTNAME", loss.getRegist().getEntrustName());
//			cv.put("ENTRUSTMOBILE", loss.getRegist().getEntrustMobile());
//			cv.put("DAMAGEDAYP", loss.getRegist().getDamageDayp());
//
//			String where = "REGISTNO=? and LOSSNO=?";
//			String[] whereValue = { loss.getRegist().getRegistNo(), loss.getRegist().getLossNo() };
//			if (isExist(db, where, "LOSSTASKQUERY", whereValue)) {
//				/**
//				 * 判断是否需要更新操作 只有同步理赔的需要更新操作 其他只要本地存储过一次则全部使用本地数据
//				 */
//				if (isUpdate) {
//					int id = db.update("LOSSTASKQUERY", cv, "REGISTNO=? and LOSSNO=?", whereValue);
//					addItemTable(db, loss, id + "");
//				}
//			} else {
//				long id = db.insert("LOSSTASKQUERY", "id", cv);
//				addItemTable(db, loss, id + "");
//			}
//			db.setTransactionSuccessful();
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			db.endTransaction();
//			cv.clear();
//			db.close();
//		}
//	}
//
//	/**
//	 * 新增或者更新要点数据
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateTaskCheckExt(SQLiteDatabase db, CheckExt checkExt, String id) {
//
//		ContentValues cv = null;
//
//		cv = new ContentValues();
//
//		cv.put("TASKQUERYID", id + "");
//		cv.put("CHECKKERNELNAME", checkExt.getCheckKernelName());
//		cv.put("CHECKKERNELSELECT", checkExt.getCheckKernelSelect());
//		cv.put("CHECKKERNELTYPE", checkExt.getCheckKernelType());
//		cv.put("CHECKKERNELCODE", checkExt.getCheckKernelCode());
//		cv.put("SERIALNO", checkExt.getSerialNo());
//
//		String where = "TASKQUERYID=? and CHECKKERNELNAME=? ";
//		String[] whereValue = { id + "", checkExt.getCheckKernelName() };
//		if (isExist(db, where, "TASKCHECKEXT", whereValue)) {
//			db.update("TASKCHECKEXT", cv, "TASKQUERYID=? and CHECKKERNELNAME=? ", whereValue);
//		} else {
//
//			db.insert("TASKCHECKEXT", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	private static void addItemTable(SQLiteDatabase db, DefLossInfoQueryResponse loss, String taskId) {
//
//		/**
//		 * 添加查勘要点信息
//		 */
//		insertOrUpdateSurveyKeyPoint(db, loss.getSurveyKeyPoint(), taskId);
//		/**
//		 * 添加受损车辆
//		 */
//		for (int i = 0; i < loss.getCarLossInfos().size(); i++) {
//			insertOrUpdateCarLossInfo(db, loss.getCarLossInfos().get(i), taskId);
//		}
//		/**
//		 * 添加任务信息
//		 */
//		insertOrUpdateTaskInfo(db, loss.getTaskInfo(), taskId);
//		/**
//		 * 添加定损车信息
//		 */
//		for (int i = 0; i < loss.getDefLossCarInfos().size(); i++) {
//			insertOrUpdateDefLossCarInfo(db, loss.getDefLossCarInfos().get(i), taskId);
//
//		}
//		/**
//		 * 添加定损内容
//		 */
//		insertOrUpdateDefLossContent(db, loss.getDefLossContent(), taskId);
//		/**
//		 * 添加查勘扩展信息
//		 */
//		for (int i = 0; i < loss.getCheckExt().size(); i++) {
//			insertOrUpdateTaskCheckExt(db, loss.getCheckExt().get(i), taskId);
//		}
//		/**
//		 * 险别
//		 */
//		for (int i = 0; i < loss.getItemKinds().size(); i++) {
//			insertOrUpdateItemKind(db, loss.getItemKinds().get(i), taskId);
//		}
//	}
//
//	/**
//	 * 新增定损内容
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateDefLossContent(SQLiteDatabase db, DefLossContent dlc, String id) {
//
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("REPAIRFACTORYCODE", dlc.getRepairFactoryCode());
//		cv.put("REPAIRFACTORYNAME", dlc.getRepairFactoryName());
//		cv.put("REPAIRCOOPERATEFLAG", dlc.getRepairCooperateFlag());
//		cv.put("REPAIRMODE", dlc.getRepairMode());
//		cv.put("REPAIRAPTITUDE", dlc.getRepairapTitude());
//		cv.put("DEFLOSSRISKCODE", dlc.getDefLossRiskCode());
//		cv.put("SUMFITSFEE", dlc.getSumfitsfee());
//		cv.put("SUMREPAIRFEE", dlc.getSumRepairfee());
//		cv.put("SUMREST", dlc.getSumRest());
//		cv.put("SUMCERTAINLOSS", dlc.getSumCertainLoss());
//		cv.put("SUMCERTAINLOSSCH", dlc.getSumCerTainLossCh());
//		cv.put("DEFLOSSADVISE", dlc.getDefLossAdvise());
//		cv.put("SATISFIEDDEGREE", dlc.getSatisfieddegree());
//		String where = "TASKQUERYID=? ";
//		String[] whereValue = { id + "" };
//		if (isExist(db, where, "DEFLOSSCONTENT", whereValue)) {
//			db.update("DEFLOSSCONTENT", cv, where, whereValue);
//		} else {
//
//			db.insert("DEFLOSSCONTENT", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	/**
//	 * 新增定损车信息
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateDefLossCarInfo(SQLiteDatabase db, DefLossCarInfo dcl, String id) {
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("INSURECARFLAG", dcl.getInsurecarFlag());
//		cv.put("LICENSENO", dcl.getLicenseNo());
//		cv.put("CARFRAMENO", dcl.getCarframeNo());
//		cv.put("BRANDNAME", dcl.getBrandName());
//		cv.put("NEWPRUCHASEAMOUNT", dcl.getNewPruchaseAmount());
//		cv.put("CARVEHICLEDESC", dcl.getCarVehicleDesc());
//		cv.put("CARFACTORYCODE", dcl.getCarFactoryCode());
//		cv.put("CARFACTORYNAME", dcl.getCarFactoryName());
//		cv.put("CARREGISTERDATE", dcl.getCarRegisterDate());
//
//		String where = "TASKQUERYID=? and CARFRAMENO=?";
//		String[] whereValue = { id + "", dcl.getCarframeNo() };
//		if (isExist(db, where, "DEFLOSSCARINFO", whereValue)) {
//			db.update("DEFLOSSCARINFO", cv, where, whereValue);
//		} else {
//
//			db.insert("DEFLOSSCARINFO", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	/**
//	 * 新增险别信息
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateItemKind(SQLiteDatabase db, ItemKind itemKind, String id) {
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("ITEMCODE", itemKind.getKindCode());
//		cv.put("ITEMNAME", itemKind.getKindName());
//
//		String where = "TASKQUERYID=? and ITEMCODE=?";
//		String[] whereValue = { id + "", itemKind.getKindCode() };
//		if (isExist(db, where, "LOSSITEMKIND", whereValue)) {
//			db.update("LOSSITEMKIND", cv, where, whereValue);
//		} else {
//			db.insert("LOSSITEMKIND", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	/**
//	 * 新增查勘要点
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateSurveyKeyPoint(SQLiteDatabase db, SurveyKeyPoint skp, String id) {
//
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("FIRSTSITEFLAG", skp.getFirstsiteFlag());
//		cv.put("DAMAGECODE", skp.getDamageCode());
//		cv.put("DAMAGENAME", skp.getDamageName());
//		cv.put("INDEMNITYDUTY", skp.getIndemnityDuty());
//		cv.put("CLAIMTYPE", skp.getClaimType());
//		cv.put("ISCOMMONCLAIM", skp.getIsCommonClaim());
//		cv.put("SUBROGATETYPE", skp.getSubrogateType());
//		cv.put("ACCIDENTBOOK", skp.getAccidentBook());
//		cv.put("ACCIDENT", skp.getAccident());
//		cv.put("REMARK", skp.getRemark());
//		cv.put("CHECKREPORT", skp.getCheckReport());
//
//		String where = "TASKQUERYID=? ";
//		String[] whereValue = { id + "" };
//		if (isExist(db, where, "SURVEYKEYPOINT", whereValue)) {
//			db.update("SURVEYKEYPOINT", cv, "TASKQUERYID=?", whereValue);
//		} else {
//
//			db.insert("SURVEYKEYPOINT", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	/**
//	 * 新增受损车辆信息
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateCarLossInfo(SQLiteDatabase db, CarLossInfo cli, String id) {
//
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("INSURECARFLAG", cli.getInsureCarFlag());
//		cv.put("LICENSENO", cli.getLicenseNo());
//		cv.put("DEFINELOSSPERSON", cli.getDefineLossPerson());
//		cv.put("DEFINELOSSAMOUT", cli.getDefineLossAmount());
//		String where = "TASKQUERYID=? and LICENSENO=?";
//		String[] whereValue = { id + "", cli.getLicenseNo() };
//		if (isExist(db, where, "CARLOSSINFO", whereValue)) {
//			db.update("CARLOSSINFO", cv, "TASKQUERYID=? and LICENSENO=?", whereValue);
//		} else {
//
//			db.insert("CARLOSSINFO", "id", cv);
//		}
//		cv.clear();
//	}
//
//	/**
//	 * 新增任务信息
//	 * 
//	 * @param db
//	 * @param tasks
//	 */
//	private static void insertOrUpdateTaskInfo(SQLiteDatabase db, TaskInfo ti, String id) {
//
//		ContentValues cv = null;
//		cv = new ContentValues();
//		cv.put("TASKQUERYID", id + "");
//		cv.put("TASKRECEIPTTIME", ti.getTaskReceiptTime());
//		cv.put("ARRIVESCENETIME", ti.getArrivesceneTIme());
//		cv.put("LINKCUSTOMTIME", ti.getLinkCustomTime());
//		cv.put("TASKHANDTIME", ti.getTaskHandTime());
//
//		String where = "TASKQUERYID=? ";
//		String[] whereValue = { id + "" };
//		if (isExist(db, where, "TASKINFO", whereValue)) {
//			db.update("TASKINFO", cv, "TASKQUERYID=?", whereValue);
//		} else {
//
//			db.insert("TASKINFO", "id", cv);
//		}
//		cv.clear();
//
//	}
//
//	/**
//	 * 获取查勘详细信息任务
//	 * 
//	 * @param db
//	 * @return
//	 */
//	public static DefLossInfoQueryResponse getLossTaskQuery(SQLiteDatabase db, String registNo, String lossNo) {
//		String selection = "REGISTNO=? and LOSSNO=?";
//		Cursor cursor = db.query("LOSSTASKQUERY", null, selection, new String[] { registNo, lossNo }, null, null, null);
//		return toLossTaskQuery(db, cursor);
//
//	}
//
//	private static DefLossInfoQueryResponse toLossTaskQuery(SQLiteDatabase db, Cursor cursor) {
//		DefLossInfoQueryResponse.deflossinfoQueryResponse=null;
//		DefLossInfoQueryResponse taskQuery = DefLossInfoQueryResponse.getDefLossInfoQueryResponse();
//		try {
//			while (cursor.moveToNext()) {
//				taskQuery.getRegist().setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
//				taskQuery.getRegist().setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));
//				taskQuery.getRegist().setPolicyComCode(cursor.getString(cursor.getColumnIndex("POLICYCOMCODE")));
//				taskQuery.getRegist().setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
//				taskQuery.getRegist().setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
//				taskQuery.getRegist().setDispatchpTime(cursor.getString(cursor.getColumnIndex("DISPATCHPTIME")));
//				taskQuery.getRegist().setReportTime(cursor.getString(cursor.getColumnIndex("REPORTTIME")));
//				taskQuery.getRegist().setOutofPlace(cursor.getString(cursor.getColumnIndex("OUTOFPLACE")));
//				taskQuery.getRegist().setDriverName(cursor.getString(cursor.getColumnIndex("DRIVERNAME")));
//				taskQuery.getRegist().setPromptMessage(cursor.getString(cursor.getColumnIndex("PROMPTMESSAGE")));
//				taskQuery.getRegist().setInsuredName(cursor.getString(cursor.getColumnIndex("INSUREDNAME")));
//				taskQuery.getRegist().setInsuredMobile(cursor.getString(cursor.getColumnIndex("INSUREDMOBILE")));
//				taskQuery.getRegist().setEntrustName(cursor.getString(cursor.getColumnIndex("ENTRUSTNAME")));
//				taskQuery.getRegist().setEntrustMobile(cursor.getString(cursor.getColumnIndex("ENTRUSTMOBILE")));
//				taskQuery.getRegist().setDamageDayp(cursor.getString(cursor.getColumnIndex("DAMAGEDAYP")));
//				String taskQueryId = cursor.getString(cursor.getColumnIndex("id"));
//				/**
//				 * 查勘要点
//				 */
//				taskQuery.setSurveyKeyPoint(getSurveyKeyPoint(db, taskQueryId));
//				
//				/**
//				 * 案件涉损
//				 */
//				taskQuery.setCarLossInfos(getCarLossInfo(db, taskQueryId));
//				/**
//				 * 任务信息
//				 */
//				taskQuery.setTaskInfo(getTaskInfo(db, taskQueryId));
//				/**
//				 * 定损车信息
//				 */
//				taskQuery.setDefLossCarInfos(getDefLossCarInfo(db, taskQueryId));
//				/**
//				 * 定损内容
//				 */
//				taskQuery.setDefLossContent(getDefLossContent(db, taskQueryId));
//				/**
//				 * 扩展信息
//				 */
//				taskQuery.setCheckExt(getCheckExt(db, taskQueryId));
//				List<CheckExt> checkExts=CommonUtils.getCheckExtS();
//				for (int i = 0; i < taskQuery.getCheckExt().size(); i++) {
//					for (int j = 0; j < checkExts.size(); j++) {
//							if(taskQuery.getCheckExt().get(i).getCheckKernelCode().equals(checkExts.get(j).getCheckKernelCode()))
//							{
//								checkExts.get(j).setCheckKernelSelect(taskQuery.getCheckExt().get(i).getCheckKernelSelect());
//								checkExts.get(j).setCheckKernelType(taskQuery.getCheckExt().get(i).getCheckKernelType());
//								checkExts.get(j).setCheckExtRemark(taskQuery.getCheckExt().get(i).getCheckExtRemark());
//							}
//					}
//					
//				}
//				taskQuery.setCheckExt(checkExts);
//				/**
//				 * 险别信息
//				 */
//				taskQuery.setItemKinds(getItemKind(db, taskQuery.getRegist().getRegistNo(),  taskQuery.getRegist().getLossNo()));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			cursor.close();
//			db.close();
//		}
//		return taskQuery;
//	}
//
//	/**
//	 * 获取险别信息任务
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static ArrayList<ItemKind> getItemKind(SQLiteDatabase db, String regstiNo, String lossNo) {
//		String selection = "registNo=? and lossNo=?";
//		Cursor cursor = db.query("ClItemKind", null, selection, new String[] { regstiNo,lossNo  }, null, null, null);
//
//		ArrayList<ItemKind> itemKinds = new ArrayList<ItemKind>();
//		while (cursor.moveToNext()) {
//			itemKinds.add(toItemKind(cursor));
//		}
//		cursor.close();
//		return itemKinds;
//	}
//
//	private static ItemKind toItemKind(Cursor cursor) {
//		ItemKind itemKind = new ItemKind();
//		itemKind.setKindCode(cursor.getString(cursor.getColumnIndex("ITEMCODE")));
//		itemKind.setKindName(cursor.getString(cursor.getColumnIndex("ITEMNAME")));
//		return itemKind;
//	}
//
//	/**
//	 * 获取扩展信息任务
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static ArrayList<CheckExt> getCheckExt(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("LOSSTASKCHECKEXT", null, selection, new String[] { taskQueryId }, null, null, null);
//
//		ArrayList<CheckExt> taskQuerys = new ArrayList<CheckExt>();
//		while (cursor.moveToNext()) {
//			taskQuerys.add(toCheckExt(cursor));
//		}
//		cursor.close();
//		return taskQuerys;
//	}
//
//	private static CheckExt toCheckExt(Cursor cursor) {
//		CheckExt checkExt = new CheckExt();
//		checkExt.setCheckKernelName(cursor.getString(cursor.getColumnIndex("CHECKKERNELNAME")));
//		checkExt.setCheckKernelSelect(cursor.getString(cursor.getColumnIndex("CHECKKERNELSELECT")));
//		checkExt.setCheckKernelType(cursor.getString(cursor.getColumnIndex("CHECKKERNELTYPE")));
//		checkExt.setCheckKernelCode(cursor.getString(cursor.getColumnIndex("CHECKKERNELCODE")));
//		checkExt.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
//		return checkExt;
//	}
//
//	/**
//	 * 定损车辆信息
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static List<DefLossCarInfo> getDefLossCarInfo(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("DEFLOSSCARINFO", null, selection, new String[] { taskQueryId }, null, null, null);
//		List<DefLossCarInfo> defLossCarInfo = new ArrayList<DefLossCarInfo>();
//		while (cursor.moveToNext()) {
//			defLossCarInfo.add(toDefLossCarInfo(cursor));
//		}
//		cursor.close();
//		return defLossCarInfo;
//	}
//
//	private static DefLossCarInfo toDefLossCarInfo(Cursor cursor) {
//		DefLossCarInfo defLossCarInfo = new DefLossCarInfo();
//
//		defLossCarInfo.setInsurecarFlag(cursor.getString(cursor.getColumnIndex("INSURECARFLAG")));
//		defLossCarInfo.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
//		defLossCarInfo.setCarframeNo(cursor.getString(cursor.getColumnIndex("CARFRAMENO")));
//		defLossCarInfo.setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
//		defLossCarInfo.setNewPruchaseAmount(cursor.getString(cursor.getColumnIndex("NEWPRUCHASEAMOUNT")));
//
//		return defLossCarInfo;
//	}
//
//	/**
//	 * 受损车辆信息
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static List<CarLossInfo> getCarLossInfo(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("CARLOSSINFO", null, selection, new String[] { taskQueryId }, null, null, null);
//		List<CarLossInfo> carLossInfos = new ArrayList<CarLossInfo>();
//		while (cursor.moveToNext()) {
//			carLossInfos.add(toCarLossInfo(cursor));
//		}
//		cursor.close();
//		return carLossInfos;
//	}
//
//	private static CarLossInfo toCarLossInfo(Cursor cursor) {
//		CarLossInfo carLossInfo = new CarLossInfo();
//		carLossInfo.setInsureCarFlag(cursor.getString(cursor.getColumnIndex("INSURECARFLAG")));
//		carLossInfo.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
//		carLossInfo.setDefineLossPerson(cursor.getString(cursor.getColumnIndex("DEFINELOSSPERSON")));
//		carLossInfo.setDefineLossAmount(cursor.getString(cursor.getColumnIndex("DEFINELOSSAMOUT")));
//
//		return carLossInfo;
//	}
//
//	/**
//	 * 定损内容
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static DefLossContent getDefLossContent(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("DEFLOSSCONTENT", null, selection, new String[] { taskQueryId }, null, null, null);
//
//		DefLossContent defLossContent = new DefLossContent();
//		while (cursor.moveToNext()) {
//			defLossContent = toDefLossContent(cursor);
//		}
//		cursor.close();
//		return defLossContent;
//	}
//
//	private static DefLossContent toDefLossContent(Cursor cursor) {
//		DefLossContent defLossContent = new DefLossContent();
//
//		defLossContent.setRepairFactoryCode(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYCODE")));
//		defLossContent.setRepairFactoryName(cursor.getString(cursor.getColumnIndex("REPAIRFACTORYNAME")));
//		defLossContent.setRepairCooperateFlag(cursor.getString(cursor.getColumnIndex("REPAIRCOOPERATEFLAG")));
//		defLossContent.setRepairMode(cursor.getString(cursor.getColumnIndex("REPAIRMODE")));
//		defLossContent.setRepairapTitude(cursor.getString(cursor.getColumnIndex("REPAIRAPTITUDE")));
//		defLossContent.setDefLossRiskCode(cursor.getString(cursor.getColumnIndex("DEFLOSSRISKCODE")));
//		defLossContent.setSumfitsfee(cursor.getString(cursor.getColumnIndex("SUMFITSFEE")));
//		defLossContent.setSumRepairfee(cursor.getString(cursor.getColumnIndex("SUMREPAIRFEE")));
//		defLossContent.setSumRest(cursor.getString(cursor.getColumnIndex("SUMREST")));
//		defLossContent.setSumCertainLoss(cursor.getString(cursor.getColumnIndex("SUMCERTAINLOSS")));
//		defLossContent.setSumCerTainLossCh(cursor.getString(cursor.getColumnIndex("SUMCERTAINLOSSCH")));
//		defLossContent.setDefLossAdvise(cursor.getString(cursor.getColumnIndex("DEFLOSSADVISE")));
//		defLossContent.setSatisfieddegree(cursor.getString(cursor.getColumnIndex("SATISFIEDDEGREE")));
//
//		return defLossContent;
//	}
//
//	/**
//	 * 任务信息
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static TaskInfo getTaskInfo(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("TASKINFO", null, selection, new String[] { taskQueryId }, null, null, null);
//
//		TaskInfo taskInfo = new TaskInfo();
//		while (cursor.moveToNext()) {
//			taskInfo = toTaskInfo(cursor);
//		}
//		cursor.close();
//		return taskInfo;
//	}
//
//	private static TaskInfo toTaskInfo(Cursor cursor) {
//		TaskInfo taskInfo = new TaskInfo();
//		taskInfo.setTaskReceiptTime(cursor.getString(cursor.getColumnIndex("TASKRECEIPTTIME")));
//		taskInfo.setArrivesceneTIme(cursor.getString(cursor.getColumnIndex("ARRIVESCENETIME")));
//		taskInfo.setLinkCustomTime(cursor.getString(cursor.getColumnIndex("LINKCUSTOMTIME")));
//		taskInfo.setTaskHandTime(cursor.getString(cursor.getColumnIndex("TASKHANDTIME")));
//		return taskInfo;
//	}
//
//	/**
//	 * 获取要点信息任务
//	 * 
//	 * @param db
//	 * @return
//	 */
//	private static SurveyKeyPoint getSurveyKeyPoint(SQLiteDatabase db, String taskQueryId) {
//		String selection = "TASKQUERYID=?";
//		Cursor cursor = db.query("SURVEYKEYPOINT", null, selection, new String[] { taskQueryId }, null, null, null);
//
//		SurveyKeyPoint skp = new SurveyKeyPoint();
//		while (cursor.moveToNext()) {
//			skp = toSurveyKeyPoint(cursor);
//		}
//		cursor.close();
//		return skp;
//	}
//
//	private static SurveyKeyPoint toSurveyKeyPoint(Cursor cursor) {
//		SurveyKeyPoint surveyKeyPoint = new SurveyKeyPoint();
//
//		surveyKeyPoint.setFirstsiteFlag(cursor.getString(cursor.getColumnIndex("FIRSTSITEFLAG")));
//		surveyKeyPoint.setDamageCode(cursor.getString(cursor.getColumnIndex("DAMAGECODE")));
//		surveyKeyPoint.setDamageName(cursor.getString(cursor.getColumnIndex("DAMAGENAME")));
//		surveyKeyPoint.setIndemnityDuty(cursor.getString(cursor.getColumnIndex("INDEMNITYDUTY")));
//		surveyKeyPoint.setClaimType(cursor.getString(cursor.getColumnIndex("CLAIMTYPE")));
//		surveyKeyPoint.setIsCommonClaim(cursor.getString(cursor.getColumnIndex("ISCOMMONCLAIM")));
//		surveyKeyPoint.setSubrogateType(cursor.getString(cursor.getColumnIndex("SUBROGATETYPE")));
//		surveyKeyPoint.setAccidentBook(cursor.getString(cursor.getColumnIndex("ACCIDENTBOOK")));
//		surveyKeyPoint.setAccident(cursor.getString(cursor.getColumnIndex("ACCIDENT")));
//		surveyKeyPoint.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
//		surveyKeyPoint.setCheckReport(cursor.getString(cursor.getColumnIndex("CHECKREPORT")));
//
//		return surveyKeyPoint;
//	}
//
//	/**
//	 * 根据报案号判断该任务是否存在
//	 * 
//	 * @param db
//	 * @param registno
//	 * @return
//	 */
//	private static boolean isExist(SQLiteDatabase db, String where, String tableName, String[] whereValue) {
//		Cursor cursor = db.query(tableName, null, where, whereValue, null, null, null);
//		boolean flag = false;
//		if (cursor.moveToNext()) {
//			flag = true;
//		} else {
//			flag = false;
//		}
//		cursor.close();
//		return flag;
//	}
//
//}
