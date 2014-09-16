package com.sinosoftyingda.fastclaim.common.db.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

/**
 * 上传记录
 * 
 * @author haoyun
 * 
 */
public class TblUploadInfo {

	private static Cursor cursor;

	public TblUploadInfo() {
		super();

	}

	/**
	 * 添加
	 * 
	 * @param uploadInfo
	 */
	public static void addUploadInfo(UploadInfo uploadInfo) {
		// 设置表字段信息
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		ContentValues values = new ContentValues();

		// start Add Dengguang
		// 解决数据库保存路径重复
		String fileUrl = uploadInfo.getFileUrl();
		// 获取Sdcard路径
		File sdcardDir = Environment.getExternalStorageDirectory();
		String reDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
		// 查找是否有重复的路径
		int find = fileUrl.indexOf(reDir+reDir);
		if(find == 0){
			fileUrl = fileUrl.replaceAll(reDir+reDir, reDir);
			//add chenjianfan
			uploadInfo.setFileUrl(fileUrl);
		}
		// end 
		
		try {
			values.put(UploadInfo.t_PLATE_NO, uploadInfo.getPlateNo());
			values.put(UploadInfo.t_POLICY_NO, uploadInfo.getPolicyNo());
			values.put(UploadInfo.t_STATE, "0");
			values.put(UploadInfo.t_PARCENT, uploadInfo.getParcent());
			values.put(UploadInfo.t_FILE_SIZE, uploadInfo.getFilesize());
			values.put(UploadInfo.t_ACTION, uploadInfo.getAction());
			values.put(UploadInfo.t_FILE_URL, uploadInfo.getFileUrl());

			// 插入数据 用ContentValues对象也即HashMap操作,并返回ID号
			String where = UploadInfo.t_POLICY_NO + "=? and " + UploadInfo.t_PLATE_NO + "=?";
			String[] value = { uploadInfo.getPolicyNo(), uploadInfo.getPlateNo() };
			db.insert("UploadInfo_Tbl", UploadInfo.t_ID, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			values.clear();
			db.close();
		}
	}

	/**
	 * 根据id修改
	 * 
	 * @param uploadInfo
	 */
	public static void updateUploadInfo(UploadInfo uploadInfo) {
		// 设置表字段信息
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(UploadInfo.t_PLATE_NO, uploadInfo.getPlateNo());
		values.put(UploadInfo.t_POLICY_NO, uploadInfo.getPolicyNo());
		values.put(UploadInfo.t_STATE, "1");
		values.put(UploadInfo.t_PARCENT, uploadInfo.getParcent());
		values.put(UploadInfo.t_FILE_SIZE, uploadInfo.getFilesize());
		values.put(UploadInfo.t_ACTION, uploadInfo.getAction());
		values.put(UploadInfo.t_FILE_URL, uploadInfo.getFileUrl());

		// 插入数据 用ContentValues对象也即HashMap操作,并返回ID号
		db.update("UploadInfo_Tbl", values, "id=?",
				new String[] { uploadInfo.getId() });
		db.close();
	}

	/**
	 * 查询
	 * 
	 * 
	 * @return
	 */
	public static List<UploadInfo> queryUploadInfolist() {
		// start Add Dengguang
		// 获取Sdcard路径
//		File sdcardDir = Environment.getExternalStorageDirectory();
//		String reDir = sdcardDir.getParent() + "/" + sdcardDir.getName();
		// end 
		
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		List<UploadInfo> uploadInfoList = new ArrayList<UploadInfo>();
		// 查询表，得到cursor对象
		cursor = db.query("UploadInfo_Tbl", null, "state=?",
				new String[] { "0" }, null, null, "action ASC");
		// 将光标移动到第一行
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			// start Add Dengguang
			// 解决数据库保存路径重复
			String fileUrl = cursor.getString(7);
			// 查找是否有重复的路径
//			int find = fileUrl.indexOf(reDir+reDir);
//			if(find == 0){
//				fileUrl = fileUrl.replaceAll(reDir+reDir, reDir);
//			}
			UploadInfo uploadInfo = new UploadInfo();
			uploadInfo.setId(cursor.getString(0));
			uploadInfo.setPlateNo(cursor.getString(1));
			uploadInfo.setPolicyNo(cursor.getString(2));
			uploadInfo.setState(cursor.getString(3));
			uploadInfo.setParcent(cursor.getString(4));
			uploadInfo.setFilesize(cursor.getString(5));
			uploadInfo.setAction(cursor.getString(6));
			uploadInfo.setFileUrl(fileUrl);
			//end
			
			// 将对象添加都集合当中
			uploadInfoList.add(uploadInfo);
			// 将光标移动到下一行
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return uploadInfoList;
	}

	/**
	 * 删除选择的纪录
	 * 
	 * @param id
	 * 
	 */
	public static void delUploadInfoItem(String action) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		try {
			db.delete("UploadInfo_Tbl", "action=?  ", new String[] { action });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	/**
	 * 删除没有该路径下文件的记录
	 * @param path
	 */
	public static void delUploadItem(String path) {
		SQLiteDatabase db = SystemConfig.dbhelp.getWritableDatabase();
		try {
			db.delete("UploadInfo_Tbl", "file_url=?  ", new String[] { path });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
}
