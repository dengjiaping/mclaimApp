package com.sinosoftyingda.fastclaim.common.db.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;

public class CheckTaskAccess {
	/**
	 * 新增或者更新数据
	 * 
	 * @param tasks
	 */
	public synchronized static void insertOrUpdate(List<CheckTask> tasks, boolean isDel) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		if (tasks != null && tasks.size() >= 1) {
			ContentValues cv = null;
			try {
				db.beginTransaction();
				for (CheckTask checkTask : tasks) {
					cv = new ContentValues();
					cv.put("registno", checkTask.getRegistno());
					cv.put("latitude", checkTask.getLatitude());
					cv.put("longitude", checkTask.getLongitude());
					cv.put("linkername", checkTask.getLinkername());
					cv.put("linkerphoneno", checkTask.getLinkerphoneno());
					cv.put("reportorname", checkTask.getReportorname());
					cv.put("reportorphoneno", checkTask.getReportorphoneno());
					cv.put("insuredname", checkTask.getInsuredname());
					cv.put("insuredphoneno", checkTask.getInsuredphoneno());
					cv.put("inputedate", checkTask.getInputedate());
					cv.put("surveytaskstatus", checkTask.getSurveytaskstatus());
					cv.put("applycannelstatus", checkTask.getApplycannelstatus());
					cv.put("synchrostatus", checkTask.getSynchrostatus());
					cv.put("insrtedname", checkTask.getInsrtedname());
					cv.put("licenseno", checkTask.getLicenseno());
					cv.put("completetime", checkTask.getTaskFinishTime() == null ? "" : checkTask.getTaskFinishTime());
					if (isExist(db, checkTask.getRegistno())) {
						db.update("checktask", cv, "registno=?", new String[] { checkTask.getRegistno() });
					} else {
						cv.put("isread", "0");
						cv.put("isaccept", "0");
						cv.put("createtime", DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
						cv.put("ordertime", "");
						cv.put("alarmtime", 0);
						cv.put("dispatchreason", "");
						cv.put("iscontact", 0);
						cv.put("contacttime", "");
						db.insert("checktask", null, cv);
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cv != null) {
					cv.clear();
				}
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			}
		}
	}

	/**
	 * 本方法用于更新手机端需要维护的数据，如果为null，则不需要会更新该字段
	 * 
	 * @param registno
	 * @param isread
	 * @param isaccept
	 * @param ordertime
	 * @param alarmtime
	 * @param dispatchreason
	 * @param iscontact
	 * @param contacttime
	 */
	public synchronized static void update(String registno, Integer isread, Integer isaccept, String ordertime, Integer alarmtime, String dispatchreason, Integer iscontact,
			String contacttime) {
		contacttime = SystemConfig.serverTime;
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

			db.update("checktask", values, "registno=?", new String[] { registno });
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 获取未处理任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CheckTask> getNoHandleTask() {
		List<CheckTask> list = new ArrayList<CheckTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=? and (applycannelstatus=? or applycannelstatus=?)";
			cursor = db.query("checktask", null, selection, new String[] { "1", "", "cancel" }, null, null, "createtime desc");
			while (cursor.moveToNext()) {
				list.add(toCheckTask(cursor));
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
	public synchronized static List<CheckTask> getHandleTask() {
		List<CheckTask> list = new ArrayList<CheckTask>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=? and (applycannelstatus=? or applycannelstatus=?)";
			cursor = db.query("checktask", null, selection, new String[] { "2", "", "cancel"  }, null, null, "createtime desc, registno asc");
			list = new ArrayList<CheckTask>();
			while (cursor.moveToNext()) {
				list.add(toCheckTask(cursor));
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
	 * 获取所有完成任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CheckTask> getAllCompletedTask() {
		List<CheckTask> list = new ArrayList<CheckTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=?  and (applycannelstatus=? or applycannelstatus=?)";
			cursor = db.query("checktask", null, selection, new String[] { "4", "", "cancel"}, null, null, "createtime desc, registno asc");
			list = new ArrayList<CheckTask>();
			while (cursor.moveToNext()) {
				list.add(toCheckTask(cursor));
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
	 * 获取当天完成任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CheckTask> getCompletedTask() {
		List<CheckTask> list = new ArrayList<CheckTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			String current = DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd);
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "surveytaskstatus=? and strftime('%Y-%m-%d',completetime)=? and (applycannelstatus=? or applycannelstatus=?)";// and
			cursor = db.query("checktask", null, selection, new String[] { "4", current , "", "cancel" }, null, null, "createtime desc, registno asc");
			list = new ArrayList<CheckTask>();
			while (cursor.moveToNext()) {
				CheckTask checkTask = toCheckTask(cursor);
				list.add(checkTask);
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
	 * 获取改派任务
	 * 
	 * @param db
	 * @return
	 */
	public synchronized static List<CheckTask> getDispatchTask() {
		List<CheckTask> list = new ArrayList<CheckTask>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			String selection = "applycannelstatus=?";
			cursor = db.query("checktask", null, selection, new String[] {"apply"}, null, null, "createtime desc, registno asc");
			while (cursor.moveToNext()) {
				list.add(toCheckTask(cursor));
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
	 * 根据报案号判断该任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	private static boolean isExist(SQLiteDatabase db, String registno) {
		String selection = "registno=?";
		String[] selectionArgs = { registno };
		Cursor cursor = db.query("checktask", null, selection, selectionArgs, null, null, null);
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
	 * 根据报案号获取报案任务
	 * 
	 * @param registno
	 * @return
	 */
	public synchronized static CheckTask findByRegistno(String registno) {
		String selection = "registno=?";
		String[] selectionArgs = { registno };
		SQLiteDatabase db = null;
		Cursor cursor = null;
		CheckTask task = null;
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			cursor = db.query("checktask", null, selection, selectionArgs, null, null, null);

			if (cursor.moveToNext()) {
				task = toCheckTask(cursor);
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

	private static CheckTask toCheckTask(Cursor cursor) {
		CheckTask ct = new CheckTask();
		ct.setRegistno(cursor.getString(cursor.getColumnIndex("registno")));
		ct.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
		ct.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
		ct.setLinkername(cursor.getString(cursor.getColumnIndex("linkername")));
		ct.setLinkerphoneno(cursor.getString(cursor.getColumnIndex("linkerphoneno")));
		ct.setReportorname(cursor.getString(cursor.getColumnIndex("reportorname")));
		ct.setReportorphoneno(cursor.getString(cursor.getColumnIndex("reportorphoneno")));
		ct.setInsuredname(cursor.getString(cursor.getColumnIndex("insuredname")));
		ct.setInsuredphoneno(cursor.getString(cursor.getColumnIndex("insuredphoneno")));
		ct.setInputedate(cursor.getString(cursor.getColumnIndex("inputedate")));
		ct.setSurveytaskstatus(cursor.getString(cursor.getColumnIndex("surveytaskstatus")));
		ct.setApplycannelstatus(cursor.getString(cursor.getColumnIndex("applycannelstatus")));
		ct.setSynchrostatus(cursor.getString(cursor.getColumnIndex("synchrostatus")));
		ct.setInsrtedname(cursor.getString(cursor.getColumnIndex("insrtedname")));
		ct.setLicenseno(cursor.getString(cursor.getColumnIndex("licenseno")));

		ct.setIsread(cursor.getInt(cursor.getColumnIndex("isread")));
		ct.setIsaccept(cursor.getInt(cursor.getColumnIndex("isaccept")));
		ct.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
		ct.setAlarmtime(cursor.getInt(cursor.getColumnIndex("alarmtime")));
		ct.setOrdertime(cursor.getString(cursor.getColumnIndex("ordertime")));
		ct.setDispatchreason(cursor.getString(cursor.getColumnIndex("dispatchreason")));
		ct.setIscontact(cursor.getInt(cursor.getColumnIndex("iscontact")));
		ct.setContacttime(cursor.getString(cursor.getColumnIndex("contacttime")));
		ct.setCompletetime(cursor.getString(cursor.getColumnIndex("completetime")));
		return ct;
	}
}
