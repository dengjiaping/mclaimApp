package com.sinosoftyingda.fastclaim.common.db.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;

public class CertainLossTaskAccess {
	/**
	 * 新增或更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	public synchronized static void insertOrUpdate(List<CertainLossTask> tasks, boolean isDel) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		if (tasks != null && tasks.size() >= 1) {
			ContentValues cv = null;
			try {
				db.beginTransaction();
				for (CertainLossTask certainLossTask : tasks) {
					cv = new ContentValues();
					cv.put("registno", certainLossTask.getRegistno());
					cv.put("itemno", certainLossTask.getItemno());
					cv.put("itemtype", certainLossTask.getItemtype());
					cv.put("latitude", certainLossTask.getLatitude());
					cv.put("longitude", certainLossTask.getLongitude());
					cv.put("printnumber", certainLossTask.getPrintnumber());
					cv.put("linkername", certainLossTask.getLinkername());
					cv.put("linkerphoneno", certainLossTask.getLinkerphoneno());
					cv.put("reportorname", certainLossTask.getReportorname());
					cv.put("reportorphoneno", certainLossTask.getReportorphoneno());
					cv.put("insuredname", certainLossTask.getInsuredname());
					cv.put("insuredphoneno", certainLossTask.getInsuredphoneno());
					cv.put("inputedate", certainLossTask.getInputedate());
					cv.put("surveytaskstatus", certainLossTask.getSurveytaskstatus());
					cv.put("verifypassflag", certainLossTask.getVerifypassflag());
					cv.put("applycannelstatus", certainLossTask.getApplycannelstatus());
					cv.put("synchrostatus", certainLossTask.getSynchrostatus());
					cv.put("insrtedname", certainLossTask.getInsrtedname());
					cv.put("licenseno", certainLossTask.getLicenseno());
					cv.put("completetime", certainLossTask.getTaskFinishTime()==null?"":certainLossTask.getTaskFinishTime());
					cv.put("SERIALNO", certainLossTask.getSeriaNo());
					
					if (isExist(db, certainLossTask.getRegistno(), certainLossTask.getItemno())) {
						db.update("certainlosstask", cv, "registno=? and itemno=?", new String[] { certainLossTask.getRegistno(), certainLossTask.getItemno() + "" });
					} else {
						cv.put("isread", "0");
						cv.put("isaccept", "0");
						cv.put("createtime", DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
						cv.put("ordertime", "");
						cv.put("alarmtime", 0);
						cv.put("dispatchreason", "");
						cv.put("iscontact", 0);
						cv.put("contacttime", "");
						db.insert("certainlosstask", null, cv);
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db.endTransaction();
				if (cv != null) {
					cv.clear();
				}
				db.close();
			}
		}
	}

	/**
	 * 本方法用于更新手机端需要维护的数据，如果为null，则不需要会更新该字段
	 * 
	 * @param registno
	 * @param itemno
	 * @param isread
	 * @param isaccept
	 * @param ordertime
	 * @param alarmtime
	 * @param dispatchreason
	 * @param iscontact
	 * @param contacttime
	 */
	public synchronized static void update(String registno, int itemno, Integer isread, Integer isaccept, String ordertime, Integer alarmtime, String dispatchreason,
			Integer iscontact, String contacttime) {
		SQLiteDatabase db = null;
		try {
			db = SystemConfig.dbhelp.getWritableDatabase();
			db.beginTransaction();
			ContentValues values = new ContentValues();
			if (isread != null) {
				values.put("isread", isread);
			}
			if (isaccept != null) {
				values.put("isaccept", isaccept);
			}
			if (ordertime != null) {
				values.put("ordertime", ordertime);
			}
			if (alarmtime != null) {
				values.put("alarmtime", alarmtime);
			}
			if (dispatchreason != null) {
				values.put("dispatchreason", dispatchreason);
			}
			if (iscontact != null) {
				values.put("iscontact", iscontact);
			}
			if (contacttime != null) {
				values.put("contacttime", contacttime);
			}
			
			if(itemno == -100){
				db.update("certainlosstask", values, "registno=? ", new String[] { registno});
			}else{
				db.update("certainlosstask", values, "registno=? and itemno=?", new String[] { registno, itemno + "" });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 根据报案号和标的序号判断该任务是此任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @param itemno
	 * @return
	 */
	private static boolean isExist(SQLiteDatabase db, String registno, int itemno) {
		String selection = "registno=? and itemno=?";
		String[] selectionArgs = { registno, "" + itemno };
		Cursor cursor = db.query("certainlosstask", null, selection, selectionArgs, null, null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		return flag;
	}

	/**
	 * 根据报案号和标的序号判断该任务是此任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @param itemno
	 * @return
	 */
	public synchronized static CertainLossTask find(String registno, int itemno) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		CertainLossTask task = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			if(itemno == -100){
				cursor = db.query("certainlosstask", null, "registno=? ", new String[]{ registno }, null, null, null);
			}else{
				cursor = db.query("certainlosstask", null, "registno=? and itemno=?", new String[]{ registno, itemno+""}, null, null, null);
			}
			
			if (cursor.moveToNext()) {
				task = toCertainLossTask(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return task;
	}

	/**
	 * 获取未处理任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getNoHandleTask() {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=? and (applycannelstatus=? or applycannelstatus=? )";
			cursor = db.query("certainlosstask", null, selection, new String[] { "1", "", "cancel" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取定损编号集合
	 * 
	 * @param registNo
	 * @return
	 */
	public synchronized static List<CertainLossTask> getByRegistNo(String registNo) {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "REGISTNO=? and surveytaskstatus=? and (applycannelstatus=? or applycannelstatus=? ) and isaccept=?";
			cursor = db.query("certainlosstask", null, selection, new String[] { registNo, "1", "", "cancel", "0" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取报案号相同、标的序号不同的其他为未处理的定损单
	 * 
	 * @param registNo
	 * @param itemno
	 * @return
	 */
	public synchronized static List<CertainLossTask> getNoHandleByRegistNo(String registNo, String itemno) {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "registno=? and itemno<>? and surveytaskstatus=? and (applycannelstatus=? or applycannelstatus=? ) and isaccept=?";
			cursor = db.query("certainlosstask", null, selection, new String[] { registNo, itemno, "1", "", "cancel", "0" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取正在处理任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getHandleTask() {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=?  and (applycannelstatus=? or applycannelstatus=? )";
			cursor = db.query("certainlosstask", null, selection, new String[] { "2", "", "cancel"  }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取当天完成任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getCompletedTask() {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			String current = DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd);
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=? and strftime('%Y-%m-%d',completetime)=?  and (applycannelstatus=? or applycannelstatus=? )";// and
			cursor = db.query("certainlosstask", null, selection, new String[] { "4", current , "", "cancel" }, null, null, "createtime desc, registno asc");// "",
																																				// "1",
																																				// "4",
			while (cursor.moveToNext()) {
				CertainLossTask certainLossTask = toCertainLossTask(cursor);
				list.add(certainLossTask);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取所有完成任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getAllCompletedTask() {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=?  and (applycannelstatus=? or applycannelstatus=?)";// and verifypassflag=?, "4"
			cursor = db.query("certainlosstask", null, selection, new String[] { "4" ,"", "cancel" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}
	/**
	 * 获取所有未完成任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getUnCompletedTask() {
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus!=?  and (applycannelstatus=? or applycannelstatus=?)";
			cursor = db.query("certainlosstask", null, selection, new String[] { "4" ,"", "cancel" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}
	/**
	 * 获取改派任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getDispatchTask() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = " applycannelstatus=?";
			cursor = db.query("certainlosstask", null, selection, new String[] { "apply" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	/**
	 * 获取核损退回任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CertainLossTask> getVerifypassTask() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<CertainLossTask> list = new ArrayList<CertainLossTask>();
		;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=?  and (applycannelstatus=? or applycannelstatus=? )";
			cursor = db.query("certainlosstask", null, selection, new String[] { "5" , "", "cancel" }, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCertainLossTask(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return list;
	}

	private static CertainLossTask toCertainLossTask(Cursor cursor) {
		CertainLossTask clt = new CertainLossTask();
		clt.setRegistno(cursor.getString(cursor.getColumnIndex("registno")));
		clt.setItemno(cursor.getInt(cursor.getColumnIndex("itemno")));
		clt.setItemtype(cursor.getString(cursor.getColumnIndex("itemtype")));
		clt.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
		clt.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
		clt.setPrintnumber(cursor.getInt(cursor.getColumnIndex("printnumber")));
		clt.setLinkername(cursor.getString(cursor.getColumnIndex("linkername")));
		clt.setLinkerphoneno(cursor.getString(cursor.getColumnIndex("linkerphoneno")));
		clt.setReportorname(cursor.getString(cursor.getColumnIndex("reportorname")));
		clt.setReportorphoneno(cursor.getString(cursor.getColumnIndex("reportorphoneno")));
		clt.setInsuredname(cursor.getString(cursor.getColumnIndex("insuredname")));
		clt.setInsuredphoneno(cursor.getString(cursor.getColumnIndex("insuredphoneno")));
		clt.setInputedate(cursor.getString(cursor.getColumnIndex("inputedate")));
		clt.setSurveytaskstatus(cursor.getString(cursor.getColumnIndex("surveytaskstatus")));
		clt.setVerifypassflag(cursor.getString(cursor.getColumnIndex("verifypassflag")));
		clt.setApplycannelstatus(cursor.getString(cursor.getColumnIndex("applycannelstatus")));
		clt.setSynchrostatus(cursor.getString(cursor.getColumnIndex("synchrostatus")));
		clt.setInsrtedname(cursor.getString(cursor.getColumnIndex("insrtedname")));
		clt.setLicenseno(cursor.getString(cursor.getColumnIndex("licenseno")));
		clt.setSeriaNo(cursor.getString(cursor.getColumnIndex("SERIALNO")));
		clt.setIsread(cursor.getInt(cursor.getColumnIndex("isread")));
		clt.setIsaccept(cursor.getInt(cursor.getColumnIndex("isaccept")));
		clt.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
		clt.setAlarmtime(cursor.getInt(cursor.getColumnIndex("alarmtime")));
		clt.setOrdertime(cursor.getString(cursor.getColumnIndex("ordertime")));
		clt.setDispatchreason(cursor.getString(cursor.getColumnIndex("dispatchreason")));
		clt.setIscontact(cursor.getInt(cursor.getColumnIndex("iscontact")));
		clt.setContacttime(cursor.getString(cursor.getColumnIndex("contacttime")));
		clt.setCompletetime(cursor.getString(cursor.getColumnIndex("completetime")));
		return clt;
	}

	/******
	 * 
	 * @param registno
	 * @return
	 */
	public static boolean relevanceCheckcontacts(String registno) {
		boolean flag = false;
		CheckTask checkTask = CheckTaskAccess.findByRegistno(registno);
		// 是否联系 1.是联系，0 为不联系
		if (checkTask != null) {
			if (1 == checkTask.getIscontact()) {
				flag = true;
			} else if (0 == checkTask.getIscontact()) {
				flag = false;
			}
		}
		return flag;

	}

}
