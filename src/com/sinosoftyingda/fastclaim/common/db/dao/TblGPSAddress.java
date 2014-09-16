package com.sinosoftyingda.fastclaim.common.db.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;


/**
 * 保存街道信息表
 * 
 * @author haoyun
 * 
 */
public class TblGPSAddress {

	/**
	 * 新增或者更新数据
	 * 
	 * @param tasks
	 */
	public synchronized static void insert(String address) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		ContentValues cv = null;
		try {
			db.beginTransaction();
			db.delete("GPSADDRESS", null, null);
			cv = new ContentValues();
			cv.put("ADDRESS", address);
			db.insert("GPSADDRESS", "id", cv);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	/**
	 * 获取GPS街道信息
	 * 
	 * @return
	 */
	public static String getAddress() {
		String address = "";
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		Cursor cursor = null;

		try {
			cursor = db.query("GPSADDRESS", null, null, null, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
			} else {
				address = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();

			db.close();

		}
		return address;
	}
}
