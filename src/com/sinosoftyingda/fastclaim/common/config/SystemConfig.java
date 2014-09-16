package com.sinosoftyingda.fastclaim.common.config;


import android.content.Context;

import com.amap.mapapi.core.GeoPoint;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.model.LoginResponse;

public class SystemConfig {
	/** 手写签名限制 **/
	public static boolean isSignatureActivity=true;
	/** mainActiviy **/
	public static Context context;
	public static boolean isLoding = true;
	/***
	 * isExistNet是一个static布尔值 可以出现在程序需要判断网络情况的地方。 true，表示有网络；false,表示无网络
	 */
	public static boolean isExistNet;
	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;

	/******************* 协赔员/高级查勘（普通查勘员）权限 ********************/
	public static boolean UserRightIsAdvanced = true;

	/**
	 * 获取db使用
	 */
	public static FastClaimDbHelper dbhelp;
	/******************* 用户名与密码 ********************/
	public static String USERLOGINNAME = "00000000";
	public static String USERLOGINPASSWORD;

	/******************* 上下班标识（上班为on,下班为off） ********************/
	public static String DUTYFLAG="";
	/******************* 登录用户地区 ********************/
	public static String AREAN="";

	/******************* 目录结构 ********************/
	public static String PHOTO_DIR = "/cClaim/"; 	// 根目录
	public static String PHOTO_TEMP = "/cTemp/"; 	// 上传根目录

	/** /mnt/sdcard/cClaim/1001010010(报案号) */
	public static String PHOTO_CLAIM_DIR = "";
	public static String PHOTO_CLAIM_TEMP = "";

	public static String PHOTO_CLAIMNO = ""; // 报案号
	public static String LOSSNO = "-2"; // 定损损失编号

	// 照片分类
	public static final String PHOTO_TYEP_0 = "/05";
	public static final String PHOTO_TYEP_1 = "/06";
	public static final String PHOTO_TYEP_2 = "/01";
	public static final String PHOTO_TYEP_3 = "/02";
	public static final String PHOTO_TYEP_4 = "/03";
	public static final String PHOTO_TYEP_5 = "/04";
	public static final String PHOTO_TYEP_6 = "/07";

	public static final String[] PHOTO_TYPES = { PHOTO_TYEP_0, PHOTO_TYEP_1,
			PHOTO_TYEP_2, PHOTO_TYEP_3, PHOTO_TYEP_4, PHOTO_TYEP_5,
			PHOTO_TYEP_6 };

	/*************************** 登陆后返回对象 ****************************/
	public static LoginResponse loginResponse;

	// 案件状态
	/** 定损请求 */
	public static final String JYDeflossRequest = "001";
	/** 定损退回 */
	public static final String JYDeflossBack = "002";
	/** 定损查看 */
	public static final String JYDeflossQuery = "003";
	/** 定损强制改派请求*/
	public static final String JYDeflossReassign="004";
	
	public static String JYDeflossStatus = "";

	/**
	 * 当前位置
	 */
	public static GeoPoint currentGeoPoint = null;
	public static String currentThoroughfare = "";// 当前街道
	public static boolean isUserExperience = false;// 判断是否为用户体验

	/****************** 控件是否可以操作 *********************/
	public static boolean isOperate = false;
	/****************** 理赔同步撤销 *************************/
	public static boolean isContinue = false;
	public static boolean isDeflossPage = false;
	
	/** 登录用户名称 */
	public static String loginUserName = "";
	
	// 服务器时间同步
	public static String serverTime = "2010-10-10 10:10:10";
	public static boolean serverTimeIsRunning = true;
	
	//手机信号强度
	public static int mobileSingle = 0;
	//信号弱小与－100测试次数
	public static int mobileSingleTestTime = 0;
	/**
	 * 精友三者车标志
	 * 0 默认值
	 * 1 标的车
	 * 2 三者车
	 */
	public static int JYTCAR_TYPE = 0;
	public static boolean JYCommit = true;
	// 是否增加现场草图素材
	public static boolean isAddAccident = false;

	// 是否正常退出
	public static boolean isExit = false;
	public static boolean isToastMina = false;
	//任务状态
	public static String surveytaskstatus = "-1";
	public static String registNoCallphone = "";

	/**
	 * 根据任务设置是否可操作
	 * 
	 * @param checkTask
	 */
	public static void setOperate(CheckTask checkTask) {
		if (checkTask == null) {
			SystemConfig.isOperate = false;
		} else {
			if (checkTask.getSurveytaskstatus().equals("1")
					&& (checkTask.getApplycannelstatus().equals("") || checkTask
							.getApplycannelstatus().equals("cancel"))) {// 未处理任务
				if (checkTask.getIsaccept() == 1) {
					SystemConfig.isOperate = true;
				} else {
					SystemConfig.isOperate = false;
				}
			} else if (checkTask.getSurveytaskstatus().equals("2")) {// 正在处理任务
				if (checkTask.getSynchrostatus().equals("synchroApply")) {
					SystemConfig.isOperate = false;
				} else {
					SystemConfig.isOperate = true;
				}
			} else if (checkTask.getSurveytaskstatus().equals("4")) {// 完成任务
				SystemConfig.isOperate = false;
			} else if (checkTask.getSurveytaskstatus().equals("1")
					&& checkTask.getApplycannelstatus().equals("apply")) {
				SystemConfig.isOperate = false;
			} else {
				SystemConfig.isOperate = false;
			}
		}
	}

	/**
	 * 根据任务设置是否可操作
	 * 
	 * @param certainLossTask
	 */
	public static void setOperate(CertainLossTask certainLossTask) {
		if (certainLossTask == null) {
			SystemConfig.isOperate = false;
		} else {
			if (certainLossTask.getSurveytaskstatus().equals("1")
					&& (certainLossTask.getApplycannelstatus().equals("") || certainLossTask
							.getApplycannelstatus().equals("cancel"))) {// 未处理任务
				if (certainLossTask.getIsaccept() == 1) {
					SystemConfig.isOperate = true;
				} else {
					SystemConfig.isOperate = false;
				}
			} else if (certainLossTask.getSurveytaskstatus().equals("2")) {// 正在处理任务
				if (certainLossTask.getSynchrostatus().equals("synchroApply")) {
					SystemConfig.isOperate = false;
				} else {
					SystemConfig.isOperate = true;
				}
			} else if (certainLossTask.getSurveytaskstatus().equals("4")) {// 完成任务
				// if(certainLossTask.getVerifypassflag().equals("4")){//核损通过
				// SystemConfig.isOperate = false;
				// }else if(certainLossTask.getVerifypassflag().equals("1")){
				// SystemConfig.isOperate = false;
				// }
				SystemConfig.isOperate = false;
			} else if (certainLossTask.getSurveytaskstatus().equals("1")
					&& certainLossTask.getApplycannelstatus().equals("apply")) {// 改派任务
				SystemConfig.isOperate = false;
			} else if (certainLossTask.getSurveytaskstatus().equals("5")) {// 核损打回任务
				SystemConfig.isOperate = true;
			} else {
				SystemConfig.isOperate = false;
			}
		}
	}

}
