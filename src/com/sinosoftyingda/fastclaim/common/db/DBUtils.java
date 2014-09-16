package com.sinosoftyingda.fastclaim.common.db;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils {
	
	/**
	 * 根据查询条件查找数据，只判断是否存在
	 * @param db
	 * @param registno
	 * @return
	 */
	public static boolean isExist(SQLiteDatabase db, String table, String where, String[] whereValue) {
		Cursor cursor = null;
		boolean flag = false;;
		try {
			cursor = db.query(table, null, where, whereValue, null, null, null);
			flag = false;
			if (cursor.moveToNext()) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		return flag;
	}
	
	
	/**
	 * 删除数据，内部不含事务
	 * @param db 
	 * @param registno
	 * @return
	 */
	public static void delete(SQLiteDatabase db, String table, String where, String[] whereValue) {
		if(db!=null){
			db.delete(table, where, whereValue);
		}
	}
	
	
	/**
	 * 删除数据，内部不含事务
	 * @param db
	 * @param registno
	 * @return
	 */
	public static void delete(String table, String where, String[] whereValue) {
		SQLiteDatabase db = null;
		try {
			db = SystemConfig.dbhelp.getWritableDatabase();
			db.beginTransaction();
			db.delete(table, where, whereValue);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(db!=null){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**
	 * 更新表中字段值
	 * @param table
	 * @param values
	 * @param where
	 * @param whereValue
	 */
	public static void update(String table, ContentValues values, String where, String[] whereValue) {
		SQLiteDatabase db = null;
		Cursor  cursor = null;
		try {
			db = SystemConfig.dbhelp.getWritableDatabase();
			db.beginTransaction();
			db.update(table, values, where, whereValue);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			if(db!=null){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**
	 * 根据主键查找表中某些指定字段
	 * @param table
	 * @param columns	建议里面字段的为TEXT类型，内部使用的cursor.getString()
	 * @param where
	 * @param whereValue
	 * @return
	 */
	public static ContentValues find(String table, String[] columns, String where, String[] whereValue) {
		SQLiteDatabase db = null;
		Cursor  cursor = null;
		ContentValues values = new ContentValues();
		try {
			db = SystemConfig.dbhelp.getReadableDatabase();
			cursor = db.query(table, columns, where, whereValue, null, null, null);
			if(cursor.moveToNext()){
				for (int i = 0; i < columns.length; i++) {
					values.put(columns[i], cursor.getString(cursor.getColumnIndex(columns[i])));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return values;
	}
	
}
