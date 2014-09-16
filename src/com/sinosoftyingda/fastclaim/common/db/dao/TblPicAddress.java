package com.sinosoftyingda.fastclaim.common.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.ImageItem;
import com.sinosoftyingda.fastclaim.common.model.PicAddress;

/**
 * 
 * 照片地址保存
 * 
 * @author haoyun 201300327
 * 
 */
public class TblPicAddress {

	/**
	 * 添加
	 * 
	 */
	public static void insertPicAddress(PicAddress picAddress) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();
			values.put("REGISTNO", picAddress.getRegistNo());
			values.put("LOSSNO", picAddress.getLossNo());
			values.put("PICNAME", picAddress.getPicName());
			values.put("PICADDRESS", picAddress.getPicAddress());
			values.put("REMARK", picAddress.getRemark());
			values.put("STARTE", picAddress.getStarte());
			db.insert("PICADDRESS", "id", values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			values.clear();
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 根据照片名称修改照片状态 改为未上传状态
	 * 
	 */
	public static void updatePicAddressByPicName(String picName) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		String selection = "PICNAME=?";
		Cursor cursor = db.query("PICADDRESS", null, selection, new String[] { picName }, null, null, null);
		List<PicAddress> pics = new ArrayList<PicAddress>();
		while (cursor.moveToNext()) {
			pics.add(toPicAddress(cursor));
		}
		cursor.close();
		db.close();
		for (int i = 0; i < pics.size(); i++) {
			PicAddress picAddress = pics.get(i);
			ContentValues values = new ContentValues();
			try {
				db = SystemConfig.dbhelp.getReadableDatabase();
				db.beginTransaction();
				values.put("STARTE", "0");
				db.update("PICADDRESS", values, "PICNAME=?", new String[] { picAddress.getPicName() });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				values.clear();
				db.endTransaction();
				db.close();
			}
		}

	}

	/**
	 * 删除单张图片
	 */
	public static void delPicAddress(String picName) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();
			db.delete("PICADDRESS", "PICNAME=?", new String[] { picName });
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			values.clear();
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 删除照片（根据报案号和损失编号）
	 */
	public static void delPicAddressByRegistNoAndLossNo(String registNo, String lossNo) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();
			db.delete("PICADDRESS", "REGISTNO=? and LOSSNO=?", new String[] { registNo, lossNo });
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			values.clear();
			db.endTransaction();
			db.close();
		}
	}

	
	/**
	 * 删除上传记录
	 * @param registNo
	 * @param lossNo
	 */
	public static void delPicAddress(String registNo, String lossNo){
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();
			db.delete("PICADDRESS", "REGISTNO=? and LOSSNO=?", new String[] {registNo, lossNo});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			values.clear();
			db.endTransaction();
			db.close();
		}
	}
	
	
	/**
	 * 修改状态
	 */
	public static void updatePicAddress(String registNo, String lossNo) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		String selection = "REGISTNO=? and LOSSNO=? and STARTE=?";
		Cursor cursor = db.query("PICADDRESS", null, selection, new String[] { registNo, lossNo, "0" }, null, null, null);
		List<PicAddress> pics = new ArrayList<PicAddress>();
		while (cursor.moveToNext()) {
			pics.add(toPicAddress(cursor));
		}
		cursor.close();
		db.close();
		for (int i = 0; i < pics.size(); i++) {
			PicAddress picAddress = pics.get(i);
			ContentValues values = new ContentValues();
			try {
				db = SystemConfig.dbhelp.getReadableDatabase();
				db.beginTransaction();
				values.put("STARTE", "1");
				db.update("PICADDRESS", values, "REGISTNO=? and LOSSNO=? ", new String[] { picAddress.getRegistNo(), picAddress.getLossNo() });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				values.clear();
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 获取
	 */
	public static List<ImageItem> getPicAddress(String registNo, String lossNo, String starte) {
		SQLiteDatabase db = SystemConfig.dbhelp.getReadableDatabase();
		String selection = "REGISTNO=? and LOSSNO=?";
		Cursor cursor = db.query("PICADDRESS", null, selection, new String[] {registNo, lossNo}, null, null, null);
		List<ImageItem> pics = new ArrayList<ImageItem>();
		while (cursor.moveToNext()) {
			pics.add(toPicAddressImageItem(cursor));
		}
		cursor.close();
		db.close();
		return pics;

	}

	private static ImageItem toPicAddressImageItem(Cursor cursor) {
		ImageItem pic = new ImageItem();
		pic.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
		pic.setImageName(cursor.getString(cursor.getColumnIndex("PICNAME")));
		pic.setImageMakeAddress(cursor.getString(cursor.getColumnIndex("PICADDRESS")));

		return pic;
	}

	private static PicAddress toPicAddress(Cursor cursor) {
		PicAddress pic = new PicAddress();
		pic.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
		pic.setPicName(cursor.getString(cursor.getColumnIndex("PICNAME")));
		pic.setPicAddress(cursor.getString(cursor.getColumnIndex("PICADDRESS")));
		pic.setStarte(cursor.getString(cursor.getColumnIndex("STARTE")));
		pic.setId(cursor.getString(cursor.getColumnIndex("id")));
		pic.setRegistNo(cursor.getString(cursor.getColumnIndex("REGISTNO")));
		pic.setLossNo(cursor.getString(cursor.getColumnIndex("LOSSNO")));

		return pic;
	}

	/**
	 * 根据照片名称判断照片是否已经被上传
	 * 
	 * @param db
	 * @param
	 * @return
	 */
	public static boolean isExist(String picName) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		boolean flag = true;

		String selection = "PICNAME=?";
		Cursor cursor = db.query("PICADDRESS", null, selection, new String[] { picName }, null, null, null);
		List<PicAddress> pics = new ArrayList<PicAddress>();
		while (cursor.moveToNext()) {
			pics.add(toPicAddress(cursor));
		}
		for (int i = 0; i < pics.size(); i++) {
			if (pics.get(i).getStarte().equals("1")) {
				flag = false;
				break;
			}
		}
		cursor.close();
		db.close();

		return flag;
	}
}
