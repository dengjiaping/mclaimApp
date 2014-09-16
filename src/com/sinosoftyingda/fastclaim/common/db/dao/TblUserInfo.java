package com.sinosoftyingda.fastclaim.common.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import com.sinosoftyingda.fastclaim.common.model.LoginUserInfo;

public class TblUserInfo {
	/**
	 * 新增或者更新数据
	 * 
	 * @param tasks
	 */
	public synchronized static void insertOrUpdate(LoginUserInfo userInfo) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		ContentValues cv = null;
		try {
			db.beginTransaction();
			cv = new ContentValues();
				db.delete("USERINFO", null, null);
				cv.put("USERNAME", userInfo.getUserName());
				cv.put("PASSWORD", "");// 为了安全起见暂时没有存储
				db.insert("USERINFO", "id", cv);
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

	/**
	 * 根据报案号判断该任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	public static boolean isExist(String username) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		String selection = "USERNAME=?";
		String[] selectionArgs = { username };
		Cursor cursor = db.query("USERINFO", null, selection, selectionArgs, null, null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		db.close();
		return flag;
	}
}
