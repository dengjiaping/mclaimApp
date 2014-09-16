package com.sinosoftyingda.fastclaim.common.views;

/***
 * 常量
 * 
 * @author chenjianfan
 * 
 */
public interface ConstantValue {

	/******************* 用户提示 ********************/
	/***
	 * 开启进度对话框
	 */
	int PROGRESSExit_OPEN = 100;
	/***
	 * 开启进度对话框
	 */
	int PROGRESSExit_CLOSE = 101;
	/***
	 * 与理赔同步开启进度对话框
	 */
	int PROGRESSTONGBU_OPEN = 102;
	/***
	 * 与理赔同步关闭进度对话框
	 */
	int PROGRESSTONGBU_CLOSE = 103;
	/***
	 * 与理赔提交开启进度对话框
	 */
	int PROGRESSSUMBIT_OPEN = 104;
	/***
	 * 与理赔提交关闭进度对话框
	 */
	int PROGRESSSUMBIT_CLOSE = 105;

	/***
	 * 开启进度对话框
	 */
	int PROGRESS_OPEN = 0;
	/***
	 * 关闭进度对话框
	 */
	int PROGRESS_CLOSE = 1;
	/***
	 * 显示错误信息
	 */
	int ERROE = 2;
	/****
	 * 退出系统提示对话框
	 */
	int EXIT = 3;

	/******************* 页面元素显示标识 ********************/

	/** 登录页面 **/
	int Page_Login = 4;
	/** 查勘页面 **/
	int Page_Survey = 6;
	/** 定损页面 **/
	int Page_Defloss = 10;
	/** 二级页面 **/
	int Page_second = 7;
	/** 三级页面 **/
	int Page_third = 8;

	/******************* splash页面常量 ********************/
	int Splash_Skip = 9;
	int Splash_LOCALE_SETTINGS = 11;
	int Splash_VERSION_UPDATE = 12;

	/******************* 二级页面直接退出 ********************/
	int Exit_Page_Second_More = 19;
	int Exit_Page_Second = 13;

	/******************* 4个导航和标题 ********************/

	int DAOHANG_SurveyBasic = 14;
	int DAOHANG_SurveyPhoto = 15;
	int DAOHANG_SurveyInfo = 16;
	int DAOHANG_SurveyWordFlow = 17;
	int DAOHANG_DeflossBasic = 18;
	int DAOHANG_DeflossInfo = 20;
	int DAOHANG_DeflossPhoto = 21;
	int DAOHANG_DeflossWordFlow = 22;
	int Page_Title_More = 23;
	int Page_Title_Survey_Task = 24;
	int Page_Title_Defloss_Task = 25;
	/**************** 上传队列 **********/
	int Page_Title_Upload = 26;
	int Page_Title_About = 27;
	int Page_Title_DriverInfo = 28;
	int Page_Title_Guaranteeslip = 29;
	int Page_Title_History = 30;
	int Page_Title_MainPoint = 31;
	int Page_Title_ObjectCar = 32;
	int Page_Title_ThreeCar = 33;
	int Page_Title_Signa = 34;
	int Page_Title_QueryHistory = 35;

	int Back_Survey = 36;
	int Back_Defloss = 37;
	int Page_Title_AboutInfo = 38; //状态信息
	int LeaveMessageEdit = 39;		//撰写留言
	int LeaveMessageQuery = 40;		//留言查询

}
