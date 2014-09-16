package com.sinosoftyingda.fastclaim.common.db.dao;

import java.util.Date;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.InfoBuffer;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 保存XML
 * 
 * @author haoyun 20130226
 * 
 */
public class TblInfoBuffer {
	
	/**
	 * 根据报案号判断该任务是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	public  static boolean isExist(String type,String registNo) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		Cursor cursor = db.query("infobuffer", null,InfoBuffer.t_TYPE+"=? and registNo=?", new String[]{type,registNo}, null, null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		return flag;
	}
	public static String getXmlByTypeAndRegistNo(String type,String registNo) {
		String xml = "";
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		String selection = InfoBuffer.t_TYPE + "=? and registNo=?";
		Cursor cursor = null;
		Cursor cursor1 = null;
		try {
			cursor = db.query("infobuffer", null, selection, new String[] { type,registNo }, null, null, null);
			String id = "";
			while (cursor.moveToNext()) {
				id = cursor.getString(cursor.getColumnIndex(InfoBuffer.t_ID));
			}
			cursor1 = db.query("infobufferContent", null, "infobufferId=?", new String[] { id }, null, null, null);
			while (cursor1.moveToNext()) {
				xml = cursor1.getString(cursor1.getColumnIndex("xmlContent"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (cursor1 != null)
				cursor1.close();
			db.close();

		}
		return xml;
	}

	/**
	 * 添加
	 * 
	 */
	public static void addinfobuffer1(InfoBuffer infoBuffer) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		db.beginTransaction(); // 手动设置开始事务

		try {

			ContentValues values = new ContentValues();
			values.put(InfoBuffer.t_TYPE, infoBuffer.getType());
			values.put(InfoBuffer.t_DATETIME,
					DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
			int id = (int) db.insert("infobuffer", InfoBuffer.t_ID, values);
			values = new ContentValues();
			/**
			 * 判断XML内容长度 超过350进行截取分段保存
			 */
			int count;
			boolean is = false;
			if (infoBuffer.getXmlContent().length() % 350 == 0) {
				count = infoBuffer.getXmlContent().length() / 350;

			} else {
				count = infoBuffer.getXmlContent().length() / 350 + 1;
				is = true;
			}
			int end;
			int begin = 0;
			for (int j = 1; j <= count; j++) {
				if (is) {
					end = j == count ? infoBuffer.getXmlContent().length() : j * 350;
				} else {
					end = j * 350;
				}

				values.put(InfoBuffer.t_INFOBUFFERID, id + "");
				values.put(InfoBuffer.t_XMLCONTENT, infoBuffer.getXmlContent().substring(begin, end));
				db.insert("infobufferContent", InfoBuffer.t_ID, values);
				begin = end;

			}

			db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交。
			// 在setTransactionSuccessful和endTransaction之间不进行任何数据库操作
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction(); // 处理完成
			db.close();
		}

	}

	/**
	 * 添加
	 * 
	 */
	public static void addinfobuffer(InfoBuffer infoBuffer) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		db.beginTransaction(); // 手动设置开始事务

		try {

			ContentValues values = new ContentValues();
			values.put(InfoBuffer.t_REGISTNO, infoBuffer.getRegistNo());
			values.put(InfoBuffer.t_TYPE, infoBuffer.getType());
			values.put(InfoBuffer.t_DATETIME,
					DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
			int id = (int) db.insert("infobuffer", InfoBuffer.t_ID, values);
			values = new ContentValues();
			/**
			 * 判断XML内容长度 超过350进行截取分段保存
			 */
			// int count;
			// boolean is = false;
			// if (infoBuffer.getXmlContent().length() % 350 == 0) {
			// count = infoBuffer.getXmlContent().length() / 350;
			//
			// } else {
			// count = infoBuffer.getXmlContent().length() / 350 + 1;
			// is = true;
			// }
			// int end;
			// int begin = 0;
			// for (int j = 1; j <= count; j++) {
			// if (is) {
			// end = j == count ? infoBuffer.getXmlContent().length() : j * 350;
			// } else {
			// end = j * 350;
			// }
			values.put(InfoBuffer.t_INFOBUFFERID, id + "");
			values.put(InfoBuffer.t_XMLCONTENT, infoBuffer.getXmlContent());
			db.insert("infobufferContent", InfoBuffer.t_ID, values);
			// begin = end;
			//
			// }

			db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交。
			// 在setTransactionSuccessful和endTransaction之间不进行任何数据库操作
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction(); // 处理完成
			db.close();
		}

	}
}
