package com.sinosoftyingda.fastclaim.common.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimItem;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimResponse;

/**
 * 历史赔案DB
 * 
 * @author haoyun 20130320
 * 
 */
public class TblHistoricalClaim {
	/**
	 * 新增或者更新数据
	 * 
	 * @param db
	 * @param tasks
	 */
	public static void insertOrUpdate(SQLiteDatabase db,
			HistoricalClaimResponse historicalClaim, String registNo) {
		ContentValues cv = null;
		try {
			db.beginTransaction();

			cv = new ContentValues();
			cv.put("REGISTNO", registNo);
			cv.put("HISTORICALACCIDENT",
					historicalClaim.getHistoricalAccident());
			cv.put("HISTORICALCLAIMTIMES",
					historicalClaim.getHistoricalClaimsNumber());
			cv.put("HISTORICALCLAIMSUM",
					historicalClaim.getHistoricalClaimsSum());

			String where = "REGISTNO=?";
			String[] whereValue = { registNo };
			String taskId = "";
			if (isExist(db, where, "CLAIMBASIC", whereValue)) {
				int id = db.update("CLAIMBASIC", cv, "REGISTNO=?",
						new String[] { registNo });
				taskId = id + "";
			} else {
				long id = db.insert("CLAIMBASIC", "id", cv);
				taskId = id + "";
			}
			for (int i = 0; i < historicalClaim.getHistoricalClaims().size(); i++) {
				insertOrUpdateHisClaim(db, historicalClaim
						.getHistoricalClaims().get(i), taskId);
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
	 * 新增或者更新涉损数据
	 * 
	 * @param db
	 * @param tasks
	 */
	private static void insertOrUpdateHisClaim(SQLiteDatabase db,
			HistoricalClaimItem hisClaimItem, String id) {

		ContentValues cv = null;
		cv = new ContentValues();
		cv.put("CLAIMBASICID", id + "");
		cv.put("REGISTNO", hisClaimItem.getRegistNo());
		cv.put("DAMAGEDATE", hisClaimItem.getDamageDate());
		cv.put("REGISTDATE", hisClaimItem.getRegistDate());
		cv.put("DAMAGEADDRESS", hisClaimItem.getDamageAddress());
		cv.put("CLAIMSTATUS", hisClaimItem.getClaimStatus());
		cv.put("CLAIMAMOUNT", hisClaimItem.getClaimAmount());

		String where = "CLAIMBASICID=? and REGISTNO=? ";
		String[] whereValue = { id + "", hisClaimItem.getRegistNo() };
		if (isExist(db, where, "HISCLAIM", whereValue)) {
			db.update("HISCLAIM", cv, "CLAIMBASICID=? and REGISTNO=? ",
					whereValue);
		} else {

			db.insert("HISCLAIM", "id", cv);
		}
		cv.clear();

	}

	/**
	 *历史赔案信息
	 * 
	 * @param db
	 * @return
	 */
	public static HistoricalClaimResponse getTaskQuery(SQLiteDatabase db,
			String registNo) {
		String selection = "REGISTNO=?";
		Cursor cursor = db.query("CLAIMBASIC", null, selection,
				new String[] { registNo }, null, null, null);
		
		return toTaskQuery(db, cursor);

	}

	private static HistoricalClaimResponse toTaskQuery(SQLiteDatabase db,
			Cursor cursor) {

			
		
		HistoricalClaimResponse his = new HistoricalClaimResponse();
		try {
			String hisId="";
			while (cursor.moveToNext()) {
				his.setHistoricalAccident(cursor.getString(cursor
						.getColumnIndex("HISTORICALACCIDENT")));
				his.setHistoricalClaimsNumber(cursor.getString(cursor
						.getColumnIndex("HISTORICALCLAIMTIMES")));
				his.setHistoricalClaimsSum(cursor.getString(cursor
						.getColumnIndex("HISTORICALCLAIMSUM")));
	
				hisId = cursor.getString(cursor.getColumnIndex("id"));
			}
			/**
			 *历史赔案明细
			 */
			his.setHistoricalClaims(getHisClaim(db, hisId));
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return his;
	}

	/**
	 *历史赔案明细
	 * @param db
	 * @return
	 */
	private static List<HistoricalClaimItem> getHisClaim(SQLiteDatabase db,
			String taskQueryId) {
		String selection = "CLAIMBASICID=?";
		Cursor cursor = db.query("HISCLAIM", null, selection,
				new String[] { taskQueryId }, null, null, null);
		List<HistoricalClaimItem> his = new ArrayList<HistoricalClaimItem>();
		while (cursor.moveToNext()) {
			his.add(toCheckExt(cursor));
		}
		cursor.close();
		return his;
	}

	private static HistoricalClaimItem toCheckExt(Cursor cursor) {
		HistoricalClaimItem his = new HistoricalClaimItem();
		his.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
		his.setDamageDate(cursor.getString(cursor.getColumnIndex("DAMAGEDATE")));
		his.setRegistDate(cursor.getString(cursor.getColumnIndex("REGISTDATE")));
		his.setDamageAddress(cursor.getString(cursor
				.getColumnIndex("DAMAGEADDRESS")));
		his.setClaimStatus(cursor.getString(cursor
				.getColumnIndex("CLAIMSTATUS")));
		his.setClaimAmount(cursor.getString(cursor
				.getColumnIndex("CLAIMAMOUNT")));

		return his;
	}

	/**
	 * 判断当前插入数据是否存在
	 * 
	 * @param db
	 * @param registno
	 * @return
	 */
	private static boolean isExist(SQLiteDatabase db, String where,
			String tableName, String[] whereValue) {
		Cursor cursor = db.query(tableName, null, where, whereValue, null,
				null, null);
		boolean flag = false;
		if (cursor.moveToNext()) {
			flag = true;
		} else {
			flag = false;
		}
		cursor.close();
		return flag;
	}
}
