package com.sinosoftyingda.fastclaim.upload.config;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.upload.util.FTPBp;
import com.sinosoftyingda.fastclaim.upload.util.UploadListviewAdapter;

/**
 * 上传队列配置文件
 * 
 * @author HaoYun
 */
public class HomeConfig {
	public static List<FTPBp>			ftpBPRList				= new ArrayList<FTPBp>();	// 每一条任务有一个独立的断电上传线程
	public static List<View>			listView				= new ArrayList<View>();	// 每一个view代表页面显示的一条记录组件
	public static TblUploadInfo			gUploadInfo;										// 上传数据库对象
	public static UploadListviewAdapter	uploadListViewAdapter	= null;						// 上传列表迭代器
	public static boolean				isStart					= false;					// 是否开始上传

	public static UploadInfo			gDelUploadInfo;	
}
