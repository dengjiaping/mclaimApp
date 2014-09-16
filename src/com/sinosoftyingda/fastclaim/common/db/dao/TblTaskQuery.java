package com.sinosoftyingda.fastclaim.common.db.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.model.CheckDriver;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;

/**
 * 查勘明细信息Table类
 * 
 * @author haoyun 20130304
 * 
 */
public class TblTaskQuery implements Serializable {

	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	public static void insertOrUpdate(SQLiteDatabase db, DetailTaskQueryResponse task, boolean isUpdate, boolean isAddCar, boolean isAddExt) {
		ContentValues cv = null;
		try {
			db.beginTransaction();
			cv = new ContentValues();
			cv.put("REGISTNO", task.getRegistNo());
			cv.put("INSRTEDNAME", task.getInsrtedName());
			cv.put("LICENSENO", task.getLicenseNo());
			cv.put("BRANDNAME", task.getBrandName());
			cv.put("DISPATCHPTIME", task.getDispatchptime());
			cv.put("REPORTTIME", task.getReporttime());
			cv.put("DISPATCHPLACE", task.getDispatchplace());
			cv.put("DRIVERNAME", task.getDrivername());
			cv.put("PROMPTMESSAGE", task.getPromptmessage());
			cv.put("DAMAGEDAYP", task.getDamageDayp());
			cv.put("TASKRECEIPTTIME", task.getTaskreceiptTime());
			cv.put("ARRIVESCENETIME", task.getArricesceneTime());
			cv.put("LINKCUSTOMTIME", task.getLinkcustomTime());
			cv.put("TASKHANDTIME", task.getTaskHandTime());
			cv.put("FIRSTSITEFLAG", task.getFirstsiteFlag());
			cv.put("DAMAGENAME", task.getDamageName());
			cv.put("DAMAGECODE", task.getDamageCode());
			cv.put("INDEMNITYDUTY", task.getIndemnityDuty());
			cv.put("CLAIMTYPE", task.getClaimType());
			cv.put("ISCOMMONCLAIM", task.getIsCommonClaim());
			cv.put("SUBROGATETYPE", task.getSubrogateType());
			cv.put("ACCIDENTBOOK", task.getAccidentBook());
			cv.put("ACCIDENT", task.getAccident());
			cv.put("INSUREDMOBILE", task.getInsuredMobile());
			cv.put("ENTRUSTNAME", task.getEntrustName());
			cv.put("ENTRUSTMOBILE", task.getEntrustMobile());
			cv.put("CHECKREPORT", task.getCheckReport());
			cv.put("SATISFACTION", task.getSatisfacTion());
			cv.put("ACCEPTTASKDATE", task.getAcceptTaskDate());
			cv.put("DISPOSETASKDATE", task.getDisposeTaskDate());
			cv.put("REMARK", task.getRemark());
			cv.put("FIRSTPHOTODATE", task.getFirstphotoDate());
			cv.put("COMPENSATERATE", task.getCompensateRate());
			String where = "REGISTNO=?";
			String[] whereValue = { task.getRegistNo() };
			if (isExist(db, where, "TASKQUERY", whereValue, false)) {
				/**
				 * 判断是否需要更新操作 只有同步理赔的需要更新操作 其他只要本地存储过一次则全部使用本地数据
				 */
				String taskQueryId = "";
				db.update("TASKQUERY", cv, "REGISTNO=?", new String[] { task.getRegistNo() });

				String selection = "REGISTNO=?";
				String registNo = task.getRegistNo();
				Cursor cursor = db.query("TASKQUERY", null, selection, new String[] { registNo }, null, null, null);
				while (cursor.moveToNext()) {
					taskQueryId = cursor.getString(cursor.getColumnIndex("id"));
				}
				if (isUpdate) {
					addItemTable(db, task, taskQueryId + "", isAddCar, isAddExt);
				} else {
					addItemTable(db, task, taskQueryId + "", isAddCar, isAddExt);
				}
			} else {
				long id = db.insert("TASKQUERY", "id", cv);
				addItemTable(db, task, id + "", isAddCar, isAddExt);
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

	private static void addItemTable(SQLiteDatabase db, DetailTaskQueryResponse task, String taskId, boolean isAdd, boolean isAddExt) {
		if (isAdd) {
			db.delete("TASKCARLOSS", "TASKQUERYID=?", new String[] { taskId });
			for (int i = 0; i < task.getCarLossList().size(); i++) {
				insertOrUpdateTaskCarLoss(db, task.getCarLossList().get(i), taskId);
			}
		}
		if (isAddExt) {
			for (int i = 0; i < task.getCheckExtList().size(); i++) {
				insertOrUpdateTaskCheckExt(db, task.getCheckExtList().get(i), taskId);
			}
		}
		for (int i = 0; i < task.getCheckDriver().size(); i++) {
			insertOrUpdateTaskCheckDriver(db, task.getCheckDriver().get(i), taskId);
		}
	}

	/**
	 * 新增或者更新涉损数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateTaskCarLoss(SQLiteDatabase db, CarLoss carLoss, String id) {
		ContentValues cv = null;  
		cv = new ContentValues();
		cv.put("TASKQUERYID", id + "");
		cv.put("ISEDITED", carLoss.getIsEdited());
		cv.put("LICENSENO", carLoss.getLicenseNo());
		cv.put("SERIALNO", carLoss.getCarNum());
		cv.put("INSURECARFLAG", carLoss.getInsureCarFlag());
		cv.put("FRAMENO", carLoss.getFrameNo());
		cv.put("BRANDNAME", carLoss.getBrandName());
		cv.put("DUTYPERCENT", carLoss.getDutyPercent());
		cv.put("POLICYNO", carLoss.getNullPolicyNo());
		cv.put("ENGINENO", carLoss.getEngineNo());
		cv.put("INSURECOMCODE", carLoss.getInsurecomCode());
		cv.put("INSURECOMNAME", carLoss.getInsurecomName());
		cv.put("CARKINDCODE", carLoss.getCarKindCode());
		cv.put("LICENSETYPE", carLoss.getLicenseType());
		
		cv.put("NULLDRIVERNAME", carLoss.getNullDriverName());
		cv.put("NULLDRIVERCODE", carLoss.getNullDriverCode());
		cv.put("NULLCERTITYPE", carLoss.getNullCertiType());
		cv.put("NULLCERTITYPECODE", carLoss.getNullCertitypeCode());

		db.insert("TASKCARLOSS", "id", cv);
		cv.clear();
	}

	/**
	 * 获取查勘详细信息任务
	 * 
	 * @param db
	 * @return
	 */
	public static DetailTaskQueryResponse getTaskQuery(SQLiteDatabase db, String registNo) {
		String selection = "REGISTNO=?";
		Cursor cursor = db.query("TASKQUERY", null, selection, new String[] { registNo }, null, null, null);

		return toTaskQuery(db, cursor);

	}

	private static DetailTaskQueryResponse toTaskQuery(SQLiteDatabase db, Cursor cursor) {
		DetailTaskQueryResponse.detailTaskQueryResponse = null;
		DetailTaskQueryResponse taskQuery = DetailTaskQueryResponse.getDetailTaskQueryResponse();
		try {
			while (cursor.moveToNext()) {
				taskQuery.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
				taskQuery.setInsrtedName(cursor.getString(cursor.getColumnIndex("INSRTEDNAME")));
				taskQuery.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
				taskQuery.setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
				taskQuery.setDispatchptime(cursor.getString(cursor.getColumnIndex("DISPATCHPTIME")));
				taskQuery.setReporttime(cursor.getString(cursor.getColumnIndex("REPORTTIME")));
				taskQuery.setDispatchplace(cursor.getString(cursor.getColumnIndex("DISPATCHPLACE")));
				taskQuery.setDrivername(cursor.getString(cursor.getColumnIndex("DRIVERNAME")));
				taskQuery.setPromptmessage(cursor.getString(cursor.getColumnIndex("PROMPTMESSAGE")));
				taskQuery.setDamageDayp(cursor.getString(cursor.getColumnIndex("DAMAGEDAYP")));
				taskQuery.setTaskreceiptTime(cursor.getString(cursor.getColumnIndex("TASKRECEIPTTIME")));
				taskQuery.setArricesceneTime(cursor.getString(cursor.getColumnIndex("ARRIVESCENETIME")));
				taskQuery.setLinkcustomTime(cursor.getString(cursor.getColumnIndex("LINKCUSTOMTIME")));
				taskQuery.setTaskHandTime(cursor.getString(cursor.getColumnIndex("TASKHANDTIME")));
				taskQuery.setFirstsiteFlag(cursor.getString(cursor.getColumnIndex("FIRSTSITEFLAG")));
				taskQuery.setDamageName(cursor.getString(cursor.getColumnIndex("DAMAGENAME")));
				taskQuery.setDamageCode(cursor.getString(cursor.getColumnIndex("DAMAGECODE")));
				taskQuery.setIndemnityDuty(cursor.getString(cursor.getColumnIndex("INDEMNITYDUTY")));
				taskQuery.setClaimType(cursor.getString(cursor.getColumnIndex("CLAIMTYPE")));
				taskQuery.setIsCommonClaim(cursor.getString(cursor.getColumnIndex("ISCOMMONCLAIM")));
				taskQuery.setSubrogateType(cursor.getString(cursor.getColumnIndex("SUBROGATETYPE")));
				taskQuery.setAccidentBook(cursor.getString(cursor.getColumnIndex("ACCIDENTBOOK")));
				taskQuery.setAccident(cursor.getString(cursor.getColumnIndex("ACCIDENT")));
				taskQuery.setInsuredMobile(cursor.getString(cursor.getColumnIndex("INSUREDMOBILE")));
				taskQuery.setEntrustName(cursor.getString(cursor.getColumnIndex("ENTRUSTNAME")));
				taskQuery.setEntrustMobile(cursor.getString(cursor.getColumnIndex("ENTRUSTMOBILE")));
				taskQuery.setCheckReport(cursor.getString(cursor.getColumnIndex("CHECKREPORT")));
				taskQuery.setSatisfacTion(cursor.getString(cursor.getColumnIndex("SATISFACTION")));
				taskQuery.setAcceptTaskDate(cursor.getString(cursor.getColumnIndex("ACCEPTTASKDATE")));
				taskQuery.setDisposeTaskDate(cursor.getString(cursor.getColumnIndex("DISPOSETASKDATE")));
				taskQuery.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
				taskQuery.setFirstphotoDate(cursor.getString(cursor.getColumnIndex("FIRSTPHOTODATE")));
				taskQuery.setCompensateRate(cursor.getString(cursor.getColumnIndex("COMPENSATERATE")));

				String taskQueryId = cursor.getString(cursor.getColumnIndex("id"));
				/**
				 * 获取涉损信息
				 */
				taskQuery.setCarLossList(getCarLoss(db, taskQueryId));
				/**
				 * 获取要点信息
				 */
				taskQuery.setCheckExtList(getCheckExt(db, taskQueryId));

				List<CheckExt> checkExts = CommonUtils.getCheckExtS();
				for (int i = 0; i < taskQuery.getCheckExtList().size(); i++) {
					for (int j = 0; j < checkExts.size(); j++) {
						if (taskQuery.getCheckExtList().get(i).getCheckKernelCode().equals(checkExts.get(j).getCheckKernelCode())) {
							checkExts.get(j).setCheckKernelSelect(taskQuery.getCheckExtList().get(i).getCheckKernelSelect());
							checkExts.get(j).setCheckKernelType(taskQuery.getCheckExtList().get(i).getCheckKernelType());
							checkExts.get(j).setCheckExtRemark(taskQuery.getCheckExtList().get(i).getCheckExtRemark());
						}
					}

				}
				taskQuery.setCheckExtList(checkExts);
				/**
				 * 获取驾驶员信息
				 */
				taskQuery.setCheckDriver(getCheckDriver(db, taskQueryId));
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
	 * 获取要点信息任务
	 * 
	 * @param db
	 * @return
	 */
	private static ArrayList<CheckExt> getCheckExt(SQLiteDatabase db, String taskQueryId) {
		String selection = "TASKQUERYID=?";
		Cursor cursor = db.query("TASKCHECKEXT", null, selection, new String[] { taskQueryId }, null, null, null);

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
		checkExt.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		return checkExt;
	}

	/**
	 * 获取驾驶员信息
	 * 
	 * @param db
	 * @return
	 */
	private static List<CheckDriver> getCheckDriver(SQLiteDatabase db, String taskQueryId) {
		String selection = "TASKQUERYID=?";
		Cursor cursor = db.query("CHECKDRIVER", null, selection, new String[] { taskQueryId }, null, null, null);

		List<CheckDriver> taskQuerys = new ArrayList<CheckDriver>();
		while (cursor.moveToNext()) {
			taskQuerys.add(toCheckDriver(cursor));
		}
		cursor.close();
		return taskQuerys;
	}

	private static CheckDriver toCheckDriver(Cursor cursor) {
		CheckDriver check = new CheckDriver();
		check.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		check.setDrivinglicenseNo(cursor.getString(cursor.getColumnIndex("DRIVINGLICENSENO")));
		check.setDriverName(cursor.getString(cursor.getColumnIndex("DRIVERNAME")));
		check.setIdentifyNumber(cursor.getString(cursor.getColumnIndex("IDENTIFYNUMBER")));
		check.setReceivelicenseDate(cursor.getString(cursor.getColumnIndex("RECEIVELICENSEDATE")));
		check.setDrivercertitype(cursor.getString(cursor.getColumnIndex("DRIVERCERTITYPE")));
		check.setDrivercertitypeCode(cursor.getString(cursor.getColumnIndex("DRIVERCERTITYPECODE")));

		return check;
	}

	/**
	 * 获取涉损信息任务
	 * 
	 * @param db
	 * @return
	 */
	private static List<CarLoss> getCarLoss(SQLiteDatabase db, String taskQueryId) {
		String selection = "TASKQUERYID=?";
		Cursor cursor = db.query("TASKCARLOSS", null, selection, new String[] { taskQueryId }, null, null, null);

		List<CarLoss> carLossList = new ArrayList<CarLoss>();
		while (cursor.moveToNext()) {
			carLossList.add(toCarLoss(cursor));
		}
		cursor.close();
		return carLossList;
	}

	private static CarLoss toCarLoss(Cursor cursor) {
		CarLoss carLoss = new CarLoss();
		carLoss.setId(cursor.getString(cursor.getColumnIndex("id")));
		carLoss.setIsEdited(cursor.getString(cursor.getColumnIndex("ISEDITED")));
		carLoss.setLicenseNo(cursor.getString(cursor.getColumnIndex("LICENSENO")));
		carLoss.setCarNum(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		carLoss.setInsureCarFlag(cursor.getString(cursor.getColumnIndex("INSURECARFLAG")));
		carLoss.setFrameNo(cursor.getString(cursor.getColumnIndex("FRAMENO")));
		carLoss.setBrandName(cursor.getString(cursor.getColumnIndex("BRANDNAME")));
		carLoss.setDutyPercent(cursor.getString(cursor.getColumnIndex("DUTYPERCENT")));
		carLoss.setNullPolicyNo(cursor.getString(cursor.getColumnIndex("POLICYNO")));
		carLoss.setEngineNo(cursor.getString(cursor.getColumnIndex("ENGINENO")));
		carLoss.setInsurecomCode(cursor.getString(cursor.getColumnIndex("INSURECOMCODE")));
		carLoss.setInsurecomName(cursor.getString(cursor.getColumnIndex("INSURECOMNAME")));
		carLoss.setCarKindCode(cursor.getString(cursor.getColumnIndex("CARKINDCODE")));
		carLoss.setLicenseType(cursor.getString(cursor.getColumnIndex("LICENSETYPE")));
		carLoss.setNullDriverCode(cursor.getString(cursor.getColumnIndex("NULLDRIVERCODE")));
		carLoss.setNullDriverName(cursor.getString(cursor.getColumnIndex("NULLDRIVERNAME")));
		carLoss.setNullCertiType(cursor.getString(cursor.getColumnIndex("NULLCERTITYPE")));
		carLoss.setNullCertitypeCode(cursor.getString(cursor.getColumnIndex("NULLCERTITYPECODE")));
		return carLoss; 
	}

	/**
	 * 新增或者更新要点数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateTaskCheckExt(SQLiteDatabase db, CheckExt checkExt, String id) {
		ContentValues cv = null;
		cv = new ContentValues();
		cv.put("TASKQUERYID", id + "");
		cv.put("CHECKKERNELNAME", checkExt.getCheckKernelName());
		cv.put("CHECKKERNELSELECT", checkExt.getCheckKernelSelect());
		cv.put("CHECKKERNELTYPE", checkExt.getCheckKernelType());
		cv.put("CHECKKERNELCODE", checkExt.getCheckKernelCode());
		cv.put("CHECKEXTREMARK", checkExt.getCheckExtRemark());
		cv.put("SERIALNO", checkExt.getSerialNo());
		String where = "TASKQUERYID=? and CHECKKERNELNAME=? ";
		String[] whereValue = { id + "", checkExt.getCheckKernelName() };
		if ((checkExt.getCheckExtRemark() != null && !checkExt.getCheckKernelCode().equals(""))
				|| (checkExt.getCheckKernelName() != null && !checkExt.getCheckKernelName().equals(""))
				|| (checkExt.getCheckKernelSelect() != null && !checkExt.getCheckKernelSelect().equals(""))
				|| (checkExt.getCheckKernelType() != null && !checkExt.getCheckKernelType().equals("")) || (checkExt.getSerialNo() != null && !checkExt.getSerialNo().equals(""))
				|| (checkExt.getCheckKernelCode() != null && !checkExt.getCheckKernelCode().equals(""))) {
			if (isExist(db, where, "TASKCHECKEXT", whereValue, false)) {
				// db.delete("TASKCHECKEXT", "TASKQUERYID=?", whereValue);
				// 我改
				db.delete("TASKCHECKEXT", "TASKQUERYID=?", new String[] { id });
				db.insert("TASKCHECKEXT", "id", cv);
			} else {

				db.insert("TASKCHECKEXT", "id", cv);
			}
		}
		cv.clear();
	}

	/**
	 * 新增或者更新车主数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateTaskCheckDriver(SQLiteDatabase db, CheckDriver check, String id) {

		ContentValues cv = null;

		cv = new ContentValues();

		cv.put("TASKQUERYID", id + "");
		cv.put("SERIALNO", check.getSerialNo());
		cv.put("DRIVINGLICENSENO", check.getDrivinglicenseNo());
		cv.put("DRIVERNAME", check.getDriverName());
		cv.put("IDENTIFYNUMBER", check.getIdentifyNumber());
		cv.put("RECEIVELICENSEDATE", check.getReceivelicenseDate());
		cv.put("DRIVERCERTITYPE", check.getDrivercertitype());
		cv.put("DRIVERCERTITYPECODE", check.getDrivercertitypeCode());
		
		
		
		String where = "TASKQUERYID=? and SERIALNO=? ";
		String[] whereValue = { id + "", check.getSerialNo() };
		if (isExist(db, where, "CHECKDRIVER", whereValue, false)) {
			db.update("CHECKDRIVER", cv, "TASKQUERYID=? and SERIALNO=? ", whereValue);
		} else {

			db.insert("CHECKDRIVER", "id", cv);
		}
		cv.clear();

	}

	/**
	 * 根据报案号判断该任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	public static boolean isExist(SQLiteDatabase db, String where, String tableName, String[] whereValue, boolean isClose) {
		SQLiteDatabase dba = db;
		if (db == null)
			dba = SystemConfig.dbhelp.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, where, whereValue, null, null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		if (isClose)
			dba.close();
		return flag;
	}

	public static void UpdatePhoto(SQLiteDatabase db, DetailTaskQueryResponse task) {

		ContentValues cv = null;
		try {

			db.beginTransaction();
			cv = new ContentValues();
			cv.put("INSUREDMOBILE", task.getInsuredMobile());
			cv.put("ENTRUSTNAME", task.getEntrustName());
			cv.put("ENTRUSTMOBILE", task.getEntrustMobile());
			String whereClause = "REGISTNO=?";
			String[] whereArgs = new String[] { task.getRegistNo() };
			db.update("TASKQUERY", cv, whereClause, whereArgs);
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			if (db != null)
				db.close();
		}

	}

}
