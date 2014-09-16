package com.sinosoftyingda.fastclaim.survey.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskinfo;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.CheckSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordSurveyTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitSurvery;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuSurveryContinue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.page.PhotosView;
import com.sinosoftyingda.fastclaim.signature.view.SignatureActivity;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.AccidentSketchActivity;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.ResourceManager;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyCaseLossAdapter;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyContactAdapter;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyDriverInfoAdapter;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyRecordAdapter;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;
import com.sinosoftyingda.fastclaim.survey.page.three.page.DriverInfoView;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageEditActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SSMainPointActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.WorkflowlogView;

/****
 * 查勘信息页面
 * 
 * @author chenjianfan
 * 
 */
public class SSurveyActivity extends BaseView implements OnClickListener {

	private FastClaimDbHelper			dbHelper;
	// 数据信息
	// private SPointsViewParam surveyRecord;
	private View						layout;
	private CommonUtils					commonUtils;
	private AnimationDrawable			rocketAnimation;
	// 查勘记录
	private RelativeLayout				rlRecord;
	private ImageView					imvRecord;
	private ListView					listViewRecord;
	// private SPointsViewParam pointsViewParams;
	private SurveyRecordAdapter			recordAdapter;
	// 查勘要点
	private RelativeLayout				rlMainpoint;
	// 案件渉损
	private RelativeLayout				rlCaseloss;
	private ImageView					imvCaseloss;
	private ListView					listViewCaseloss;
	private SurveyCaseLossAdapter		caseLossAdapter;
	// 联系信息
	private RelativeLayout				rlContactInfo;
	private ImageView					imvContactInfo;
	private ListView					listViewContactInfo;
	private SurveyContactAdapter		contactAdapter;
	// 查勘报告
	private RelativeLayout				rlReport;
	private ImageView					imvReport;
	private EditText					etReport;
	// 现场草图
	private RelativeLayout				rlDraft;
	// 手写签名
	private RelativeLayout				rlSign;
	// 驾驶人信息
	private RelativeLayout				rlDriverInfo;
	private ListView					lvDriverInfo;
	private ImageView					imvDriverInfo;
	private SurveyDriverInfoAdapter		driverInfoAdapter;
	// 满意度调查
	// 三个脸谱
	private ImageView					imvSatisfaction;
	private ImageView					imvYiBan;
	private ImageView					imvUnSatisfaction;
	// 视图数据
	// 查勘记录
	private SPointsView					sPointsView;
	// 案件涉损
	private SurveyInvolvedCaseCarView	surveyInvolvedCaseCarView;
	// 联系人信息
	private SContactView				sContactView;
	// 标的车驾驶员信息
	private DriverInfoView				driverInfoView;
	// 满意度调查不可点击
	private boolean						isClickSatisfaction	= true;
	private String						registNo;
	// 撰写留言
	private RelativeLayout messagesEdit;
	
	public SSurveyActivity(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_Survey;
	}

	@Override
	protected void init() {
		// 加载布局文件
		layout = inflater.inflate(R.layout.survey_survey, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);

		commonUtils = new CommonUtils();
		dbHelper = new FastClaimDbHelper(context);
		if (bundle != null) {
			registNo = bundle.getString("registNo");
		} else {
			registNo = "报案号错误";
		}
		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.BOLD));
		TopManager.getInstance().getBtnSurveyMsg().setBackgroundResource(R.drawable.tasks_chakantab_click);
		// 初始化控件
		findView();
	}

	private void findView() {
		rlDriverInfo = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_driverinfo);
		lvDriverInfo = (ListView) layout.findViewById(R.id.survey_survey_driverinfo_listview);
		imvDriverInfo = (ImageView) layout.findViewById(R.id.survey_survey_driverinfo_ll);
		rlRecord = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_record);
		imvRecord = (ImageView) layout.findViewById(R.id.survey_survey_record_ll);
		listViewRecord = (ListView) layout.findViewById(R.id.survey_survey_record_listview);
		rlMainpoint = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_mainpoint);
		rlCaseloss = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_caseloss);
		imvCaseloss = (ImageView) layout.findViewById(R.id.survey_survey_caseloss_ll);
		listViewCaseloss = (ListView) layout.findViewById(R.id.survey_survey_caseloss_listview);
		rlContactInfo = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_contactinfo);
		imvContactInfo = (ImageView) layout.findViewById(R.id.survey_survey_contactinfo_ll);
		listViewContactInfo = (ListView) layout.findViewById(R.id.survey_survey_contactinfo_listview);
		rlReport = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_report);
		imvReport = (ImageView) layout.findViewById(R.id.survey_survey_report_ll);
		etReport = (EditText) layout.findViewById(R.id.survey_survey_btn_report_et);
		rlDraft = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_draft);
		rlSign = (RelativeLayout) layout.findViewById(R.id.survey_survey_btn_sign);
		imvSatisfaction = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_1);
		imvYiBan = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_2);
		imvUnSatisfaction = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_3);
		messagesEdit = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_messagesEdit);
		// 初始化界面数据
		setFirstView();
		setViewData();
	}

	private void setFirstView() {
		// 报告
		if (DataConfig.tblTaskQuery != null) {
			if (!TextUtils.isEmpty(DataConfig.tblTaskQuery.getCheckReport())) {
				etReport.setText(DataConfig.tblTaskQuery.getCheckReport());
				// etReport.setText("周璟驾驶标的车鲁B611JE在");
			} else {
				createReport();
			}
		}

		// 满意度调查
		if (DataConfig.tblTaskQuery != null && DataConfig.tblTaskQuery.getSatisfacTion() != null) {
			// 2：满意:
			// 1：一般
			// 0：不满意
			isClickSatisfaction = false;
			if ("0".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {
				// 不满意
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4_click);

			} else if ("1".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {
				// 一般
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2_click);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);

			} else if ("2".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {
				// 满意
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1_click);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);

			}
			imvSatisfaction.setFocusable(false);
			imvYiBan.setFocusable(false);
			imvUnSatisfaction.setFocusable(false);

		}

	}

	private void setViewData() {
		// 案件涉损
		if (surveyInvolvedCaseCarView == null) {
			surveyInvolvedCaseCarView = new SurveyInvolvedCaseCarView(context);
		}
		// 联系人信息
		if (sContactView == null)
			sContactView = new SContactView(context);

		if (driverInfoView == null)
			driverInfoView = new DriverInfoView(context);

		// // 查勘记录数据
		// if (pointsViewParams == null) {
		// pointsViewParams = new SPointsViewParam();
		// // 获取查勘记录的数据
		// surveyRecordData();
		// } else {
		// surveyRecordData();
		// }
		if (sPointsView == null) {
			sPointsView = new SPointsView(context, this);// modify by jingtuo
		}
	}

	@Override
	protected void setListener() {
		rlRecord.setOnClickListener(this);
		rlMainpoint.setOnClickListener(this);
		rlCaseloss.setOnClickListener(this);
		rlContactInfo.setOnClickListener(this);
		rlReport.setOnClickListener(this);
		rlDraft.setOnClickListener(this);
		rlSign.setOnClickListener(this);
		rlDriverInfo.setOnClickListener(this);
		messagesEdit.setOnClickListener(this);
		if (DataConfig.tblTaskQuery != null) {
			if ("2".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {
			} else if ("1".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {

			} else if ("0".equals(DataConfig.tblTaskQuery.getSatisfacTion())) {

			} else {
				imvSatisfaction.setOnClickListener(this);
				imvYiBan.setOnClickListener(this);
				imvUnSatisfaction.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.survey_survey_btn_record:
			// 查勘记录
			commonUtils.showView(context, rlRecord, rocketAnimation, listViewRecord, imvRecord, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作
					if (recordAdapter == null) {
						recordAdapter = new SurveyRecordAdapter(sPointsView);
						listViewRecord.setAdapter(recordAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewRecord);
					} else {
						listViewRecord.setAdapter(recordAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewRecord);
					}
				}

			});
			break;
		case R.id.survey_survey_btn_mainpoint:
			// 查勘要点
			UiManager.getInstance().changeView(SSMainPointActivity.class, false, null, false);
			break;
		case R.id.survey_survey_btn_caseloss:
			// 案件渉损
			commonUtils.showView(context, rlCaseloss, rocketAnimation, listViewCaseloss, imvCaseloss, new Ifunction() {
				@Override
				public void setFunction() {
					if (caseLossAdapter == null) {
						caseLossAdapter = new SurveyCaseLossAdapter(surveyInvolvedCaseCarView);
						listViewCaseloss.setAdapter(caseLossAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewCaseloss);
					} else {
						listViewCaseloss.setAdapter(caseLossAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewCaseloss);
					}
				}
			});
			break;
		case R.id.survey_survey_btn_contactinfo:
			// 联系信息
			commonUtils.showView(context, rlContactInfo, rocketAnimation, listViewContactInfo, imvContactInfo, new Ifunction() {
				@Override
				public void setFunction() {
					if (contactAdapter == null) {
						contactAdapter = new SurveyContactAdapter(sContactView);
						listViewContactInfo.setAdapter(contactAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewCaseloss);
					} else {
						listViewContactInfo.setAdapter(contactAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(listViewCaseloss);
					}
				}
			});
			break;
		case R.id.survey_survey_btn_report:
			// 报告
			commonUtils.showView(context, rlReport, rocketAnimation, etReport, imvReport, new Ifunction() {
				@Override
				public void setFunction() {
					// 不需要操作
				}
			});
			break;
		case R.id.survey_survey_btn_draft:
			// 现场草图
			if (!SystemConfig.isOperate) {
				// 查找是否已完成的现场草图图片
				serarch(SystemConfig.PHOTO_TYEP_6, "_Sketch.png");
			} else {
				Intent accidentIntent = new Intent();
				accidentIntent.setClass(context, AccidentSketchActivity.class);
				context.startActivity(accidentIntent);
			}
			break;
		case R.id.survey_survey_btn_sign:
			// 手写签名
			// UiManager.getInstance().changeView(SignatureActivity.class, false, null, true);
			if (SystemConfig.isAddAccident)
				ResourceManager.release();

			// 手写签名
			if (!SystemConfig.isOperate) {
				// 查找是否已完成的现场草图图片
				serarch(SystemConfig.PHOTO_TYEP_0, "_sign.png");
			} else {
				if (SystemConfig.isSignatureActivity) {
					Intent intent = new Intent();
					intent.setClass(context, SignatureActivity.class);
					context.startActivity(intent);
					SystemConfig.isSignatureActivity = false;
				}
			}
			break;
		case R.id.survey_survey_satisfaction_1:
			// 满意
			if (isClickSatisfaction) {
				isClickSatisfaction = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1_click);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);
				DataConfig.tblTaskQuery.setSatisfacTion("2");
			}
			break;
		case R.id.survey_survey_satisfaction_2:
			// 一般
			if (isClickSatisfaction) {
				isClickSatisfaction = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2_click);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);
				DataConfig.tblTaskQuery.setSatisfacTion("1");
			}
			break;
		case R.id.survey_survey_satisfaction_3:
			// 不满意
			if (isClickSatisfaction) {
				isClickSatisfaction = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4_click);
				DataConfig.tblTaskQuery.setSatisfacTion("0");
			}
			break;
		case R.id.survey_basic_btn_messagesEdit:
			// 撰写留言
			bundle.putString("time", SystemConfig.serverTime);
			bundle.putString("nodetype", "查勘");
			bundle.putString("LeavePerson", SystemConfig.loginResponse.getUserName());
			UiManager.getInstance().changeView(LeaveMessageEditActivity.class, bundle, false);
			break;
		case R.id.survey_survey_btn_driverinfo:
			// 驾驶人信息
			commonUtils.showView(context, rlDriverInfo, rocketAnimation, lvDriverInfo, imvDriverInfo, new Ifunction() {
				@Override
				public void setFunction() {
					if (driverInfoAdapter == null) {
						driverInfoAdapter = new SurveyDriverInfoAdapter(driverInfoView);
						lvDriverInfo.setAdapter(driverInfoAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvDriverInfo);
					} else {
						lvDriverInfo.setAdapter(driverInfoAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvDriverInfo);
					}
				}
			});
			break;
		}
	}

	/**
	 * 查找是否存在现场事故草图
	 * 
	 * @return
	 */
	private void serarch(String picDir, String pngType) {
		boolean isSerarch = false;
		// /mnt/sdcard/cClaim/1001010010(报案号)/signture
		String signDir = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(signDir));
		signDir = signDir + picDir;
		FileUtils.mkDir(new File(signDir));
		String signtureName = SystemConfig.PHOTO_CLAIMNO + pngType;
		String signturePath = signDir + "/" + signtureName;

		// 文件名集合
		List<String> signtureImages = new ArrayList<String>();
		FileUtils fileUtils = new FileUtils();
		// 查找当前目录里面的文件
		signtureImages = fileUtils.getFileName(new File(signDir), signDir);
		for (int i = 0; i < signtureImages.size(); i++) {
			if (signturePath.equals(signtureImages.get(i))) {
				isSerarch = true;
			}
		}

		if (isSerarch) {
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(Uri.fromFile(new File(signturePath)), "image/*");
			context.startActivity(it);
		} else {
			Toast.showToast(context, "无图片");
		}

	}

	private void setFourDopBtn() {
		TopManager.getInstance().surveyPageTopBtn(new IWordSurveyTopButton() {
			@Override
			public void onSurveyWorkFlowlogClick() {
				// 查勘流程
				UiManager.getInstance().changeView(WorkflowlogView.class, bundle, false);
			}

			@Override
			public void onSurveyPhotoClick() {
				// 拍照
				UiManager.getInstance().changeView(PhotosView.class, bundle, false);
			}

			@Override
			public void onSurveyInfoClick() {
				// 查勘信息页面
				UiManager.getInstance().changeView(SSurveyActivity.class, bundle, true);
			}

			@Override
			public void onSurveyBasicClick() {
				// 查勘基本信息
				UiManager.getInstance().changeView(SBasicActivity.class, bundle, true);
			}
		});
	}

	@Override
	public Integer getExit() {
		return ConstantValue.DAOHANG_SurveyInfo;
	}

	@Override
	public Integer getBackMain() {
		return ConstantValue.Back_Survey;

	}

	@Override
	public void onPause() {
		// 离开当前页面执行
		saveData();
		System.gc();
		super.onPause();
	}

	@Override
	public void onResume() {
		setFourDopBtn();
		// 控制控件的操作性
		if (sPointsView != null)
			sPointsView.controlEd();

		if (sContactView != null)
			sContactView.controlEd();

		if (driverInfoView != null)
			driverInfoView.controlEd();

		if (!SystemConfig.isOperate) {
			etReport.setEnabled(false);
			imvSatisfaction.setEnabled(false);
			imvYiBan.setEnabled(false);
			imvUnSatisfaction.setEnabled(false);
		} else {
			etReport.setEnabled(true);
			imvSatisfaction.setEnabled(true);
			imvYiBan.setEnabled(true);
			imvUnSatisfaction.setEnabled(true);
		}

		if (surveyInvolvedCaseCarView != null) {
			surveyInvolvedCaseCarView.setView();
		}

		// 理赔交互和提交
		TopManager.getInstance().setTopBtnSumbitSurvery(new TopBtnSumbitSurvery() {
			// 判断是否有联系过客户
			boolean isTakephotos = TblTaskinfo.checkIsTakephotos(registNo);
						
			@Override
			public void OnclickTongBu() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘同步】  <<<<<<==============
				 *                              ***********************
				 */ 
				if (SystemConfig.isOperate) {
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								saveData();
								handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_OPEN);
							};
	
							@Override
							protected void onPostExecute(CommonResponse result) {
	
								if ("YES".equals(result.getResponseCode())) {
									handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
									Toast.showToastTest(context, "同步理赔成功！");
									CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
									checkTask.setSurveytaskstatus("2");
									checkTask.setSynchrostatus("synchroApply");
									List<CheckTask> list = new ArrayList<CheckTask>();
									list.add(checkTask);
									CheckTaskAccess.insertOrUpdate(list, true);
									
									// 判断是否有上传队列
									if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
									}else{
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
									}
								} else {
									handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
									Message message = Message.obtain();
									message.obj = result.getResponseMessage();
									message.what = ConstantValue.ERROE;
									handler.sendMessage(message);
								}
	
							};
	
							@Override
							protected CommonResponse doInBackground(Void... params) {
								DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
								DataConfig.tblTaskQuery.setSubmitType("synch");
								return CheckSubmitHttpService.checkSubmitService(DataConfig.tblTaskQuery, context.getString(R.string.http_url));
							}
						}.execute();
					}
				}else{
					Toast.showToast(context, "不能【查勘同步】，请确定是否“受理任务”或“继续处理”！");
				}
			}

			@Override
			public void OnclickSumbit() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘提交】  <<<<<<==============
				 *                              ***********************
				 */ 
				// 校验身份证号码是否为空
				if (SystemConfig.isOperate) {
					// 校验身份证号码是否为空
					String idCard = DataConfig.tblTaskQuery.getCheckDriver().get(0).getIdentifyNumber();
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else if (!CheckSurveyValue.idCardIsNull || !CheckSurveyValue.idCard(idCard)) {
						// 交互
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								saveData();
								handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
							};

							@Override
							protected void onPostExecute(CommonResponse result) {
								if ("YES".equals(result.getResponseCode())) {
									handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
									Toast.showToastTest(context, "查勘提交成功！");
									ContentValues values = new ContentValues();
									values.put("surveytaskstatus", "4");
									values.put("completetime", result.getHandletime());
									DBUtils.update("checktask", values, "registno=?", new String[] { registNo });
									TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
									// 判断是否有上传队列
									if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
										// Toast.showToast(context, "开始上传，请稍等...");
									}else{
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
									}
								} else {
									handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
									Message message = Message.obtain();
									message.obj = result.getResponseMessage();
									message.what = ConstantValue.ERROE;
									handler.sendMessage(message);
								}
							};

							@Override
							protected CommonResponse doInBackground(Void... params) {
								DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
								DataConfig.tblTaskQuery.setSubmitType("submit");
								return CheckSubmitHttpService.checkSubmitService(DataConfig.tblTaskQuery, context.getString(R.string.http_url));
							}
						}.execute();
					} else {
						Toast.showToast(context, "驾驶人信息-身份证号码不能为空！");
					}
				}else{
					Toast.showToast(context, "不能【查勘提交】，请确定是否“受理任务”或“继续处理”！");
				}
			}
		});

		// 理赔同步撤销
		TopManager.getInstance().btnTongBuSurveryContinue(new TopBtnTongBuSurveryContinue() {

			@Override
			public void onClick() {
				synchCancel();
			}

			private void synchCancel() {
				new AsyncTask<String, Void, CommonResponse>() {

					@Override
					protected void onPreExecute() {
						handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
					};

					@Override
					protected void onPostExecute(CommonResponse result) {
						if ("YES".equals(result.getResponseCode())) {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
							Toast.showToast(context, "任务理赔同步撤销成功");

							// 把状态改变
							CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
							checkTask.setSurveytaskstatus("2");
							checkTask.setSynchrostatus("synchroCannel");
							List<CheckTask> list = new ArrayList<CheckTask>();
							list.add(checkTask);
							CheckTaskAccess.insertOrUpdate(list, true);

							// 同步按钮显示
							TopManager.getInstance().TongBuSurveryContinue(true);
							// 控件可操作
							SystemConfig.isOperate = true;
							// 界面刷新
							UiManager.getInstance().changeView(SSurveyActivity.class, null, false);
						} else {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
							Message message = Message.obtain();
							message.obj = result.getResponseMessage();
							message.what = ConstantValue.ERROE;
							handler.sendMessage(message);
						}
					};

					@Override
					protected CommonResponse doInBackground(String... params) {
						SynchroClaimRequest requestSynchro = new SynchroClaimRequest();
						requestSynchro.setRegistNo(registNo);
						requestSynchro.setCancleType("Synchback");
						requestSynchro.setNodeType("check");
						requestSynchro.setLossNo("-2");
						return SynchroClaimHttpService.synchroClaimService(requestSynchro, params[0]);
					}

				}.execute(context.getString(R.string.http_url));
			}
		});

		super.onResume();
	}

	protected void saveData() {
		// 保存查勘记录信息
		// 联系人信息
		if (sContactView != null)
			sContactView.getPageValue();
		// 标的车驾驶员信息保存
		if (driverInfoView != null)
			driverInfoView.saveData();
		// 报告信息
		DataConfig.tblTaskQuery.setCheckReport(etReport.getText().toString());

		TblTaskQuery.insertOrUpdate(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
		DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
	}

	public void createReport() {
		// 自定义查勘报告
		List<CarLoss> carlosses = DataConfig.tblTaskQuery.getCarLossList();
		String report = "";
		report += DataConfig.tblTaskQuery.getDispatchptime() + ",";// 出险时间
		CheckTask checkTask = CheckTaskAccess.findByRegistno(DataConfig.tblTaskQuery.getRegistNo());
		String linkername = "";
		if (checkTask != null) {
			linkername = DataConfig.tblTaskQuery.getDrivername();
		}
		report += linkername + "驾驶标的车";
		String licenseno = "";
		if (DataConfig.tblTaskQuery.getLicenseNo() != null && !DataConfig.tblTaskQuery.getLicenseNo().equals("")) {// 车牌号在出险地点
			licenseno = DataConfig.tblTaskQuery.getLicenseNo();
		} else {
			licenseno = "新车";
		}
		report += licenseno + "在" + DataConfig.tblTaskQuery.getDispatchplace() + "行驶,";
		if (carlosses.size() > 1) {
			report += "由于";
			if (!DataConfig.tblTaskQuery.getReason().equals("")) {
				report += DataConfig.tblTaskQuery.getReason() + "原因";
			}
			report += "与";
			String carinfo = "";
			for (int i = 0; i < carlosses.size(); i++) {
				if (!carlosses.get(i).getInsureCarFlag().equals("1")) {
					if (!carlosses.get(i).getLicenseNo().equals("")) {
						carinfo += carlosses.get(i).getLicenseNo();
					} else {
						carinfo += "新车";
					}
					carinfo += "、";
				}
			}
			carinfo = carinfo.substring(0, carinfo.length() - 1);
			report += carinfo + "发生" + DataConfig.tblTaskQuery.getDamageName() + "事故,";

			carinfo = "";
			for (int i = 0; i < carlosses.size(); i++) {
				if (carlosses.get(i).getInsureCarFlag().equals("1")) {
					carinfo += "导致标的车";
				} else {
					carinfo += "导致三者车";
				}
				if (!carlosses.get(i).getLicenseNo().equals("")) {
					carinfo += carlosses.get(i).getLicenseNo();
				} else {
					carinfo += "新车";
				}
				carinfo += "受损、";
			}
			carinfo = carinfo.substring(0, carinfo.length() - 1);
			report += carinfo + "。";
		} else {
			report += "发生" + DataConfig.tblTaskQuery.getDamageName() + "事故,导致标的车" + licenseno + "受损。";
		}
		String type = "";
		if (DataConfig.tblTaskQuery.getIsCommonClaim().equals("1")) {
			type = "当事人自行协商处理费互碰自赔案件";
		} else {
			type = "交警一般程序处理非互碰自赔案件";
		}
		report += "已经" + type + "处理。";

		etReport.setText(report);
	}

}
