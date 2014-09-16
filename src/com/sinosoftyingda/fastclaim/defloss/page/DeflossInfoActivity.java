package com.sinosoftyingda.fastclaim.defloss.page;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.VerifyLossSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordDeflossTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuDeflossContinue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.adapter.DInfoContentAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.DInfoFactoryAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.DInfoObjectAdapter;
import com.sinosoftyingda.fastclaim.defloss.service.DeflossinfoUploadToClaimSystem;
import com.sinosoftyingda.fastclaim.defloss.view.DInfoContentView;
import com.sinosoftyingda.fastclaim.defloss.view.DInfoFactoryView;
import com.sinosoftyingda.fastclaim.defloss.view.DInfoObjectView;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.page.DeflossPhotosView;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageEditActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.DeflossWorkflowlogView;

/**
 * 定损信息
 * @author Jianfan
 */
public class DeflossInfoActivity extends BaseView implements OnClickListener {
	private View layout;
	private CommonUtils commonUtils;
	private AnimationDrawable rocketAnimation;
	// 定损对象
	private RelativeLayout rlObject;
	private ListView lvObject;
	private ImageView imvObject;
	private DInfoObjectAdapter objectAdapter;
	private DInfoObjectView dInfoObjectView;
	// 修理厂信息
	private RelativeLayout rlFactory;
	private ListView lvFactory;
	private ImageView imvFactory;
	private DInfoFactoryAdapter factoryAdapter;
	private DInfoFactoryView dInfoFactoryView;
	// 定损内容
	private RelativeLayout rlContent;
	private ListView lvContent;
	private ImageView imvContent;
	private DInfoContentAdapter contentAdapter;
	private DInfoContentView dInfoContentView;
	// 撰写留言
	private RelativeLayout messagesEdit;
	// 定损意见
	private RelativeLayout rlDeflossPoint;
	private ImageView imvDeflossPoint;
	private EditText edDeflossPoint;
	// 满意度调查
	private ImageView imvSatisfaction;
	private ImageView imvYiBan;
	private ImageView imvUnSatisfaction;
	private String satisfaction = "";
	private boolean satisfactionFlag = true;
	private String registNo;
	private String lossNo;
	// 核损退回意见
	private RelativeLayout rlDeflossBackPoint;
	private ImageView imvDeflossBackPoint;
	private EditText edDeflossBackPoint;

	public DeflossInfoActivity(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_Defloss;
	}

	@Override
	protected void init() {
		layout = inflater.inflate(R.layout.defloss_defloss, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		findView();
		setViewData();
		setView();
	}

	private void setView() {
		// 满意度控制点击
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null) {
			// 2：满意:
			// 1：一般
			// 0：不满意
			satisfaction = DataConfig.defLossInfoQueryData.getDefLossContent().getSatisfieddegree();
			if ("0".equals(satisfaction)) {
				// 不满意
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4_click);
			} else if ("1".equals(satisfaction)) {
				// 一般
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2_click);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);

			} else if ("2".equals(satisfaction)) {
				// 满意
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1_click);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);
			}
		}
	}

	private void setViewData() {

		if (dInfoObjectView == null) {
			dInfoObjectView = new DInfoObjectView(context);
		}
		if (dInfoFactoryView == null) {
			dInfoFactoryView = new DInfoFactoryView(context);
		}
		if (dInfoContentView == null) {
			dInfoContentView = new DInfoContentView(context);
		}
		if (DataConfig.defLossInfoQueryData != null) {
			edDeflossPoint.setText(DataConfig.defLossInfoQueryData.getDefLossContent().getDefLossAdvise());
		}

		if ("5".equals(SystemConfig.surveytaskstatus)) {
			// 核损任务显示核损退回意见
			// 核算退回有数据
			rlDeflossBackPoint.setVisibility(View.VISIBLE);
			edDeflossBackPoint.setText(DataConfig.defLossInfoQueryData.getDefLossContent().getUndwrtRemark());
		} else {
			edDeflossBackPoint.setVisibility(View.GONE);
			rlDeflossBackPoint.setVisibility(View.GONE);
		}

	}

	private void findView() {
		rlDeflossBackPoint = (RelativeLayout) layout.findViewById(R.id.defloss_back_btn_deflosspoint);
		imvDeflossBackPoint = (ImageView) layout.findViewById(R.id.defloss_back_imv_deflosspoint);
		edDeflossBackPoint = (EditText) layout.findViewById(R.id.defloss_back_et_deflosspoint);
		rlDeflossBackPoint.setOnClickListener(this);
		rlObject = (RelativeLayout) layout.findViewById(R.id.defloss_info_btn_object);
		lvObject = (ListView) layout.findViewById(R.id.defloss_info_lv_object);
		imvObject = (ImageView) layout.findViewById(R.id.defloss_info_imv_object);
		rlFactory = (RelativeLayout) layout.findViewById(R.id.defloss_info_btn_factory);
		lvFactory = (ListView) layout.findViewById(R.id.defloss_info_lv_factory);
		imvFactory = (ImageView) layout.findViewById(R.id.defloss_info_imv_factory);
		rlContent = (RelativeLayout) layout.findViewById(R.id.defloss_info_btn_deflosscontent);
		messagesEdit = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_messagesEdit);
		lvContent = (ListView) layout.findViewById(R.id.defloss_info_lv_deflosscontent);
		imvContent = (ImageView) layout.findViewById(R.id.defloss_info_imv_deflosscontent);
		rlDeflossPoint = (RelativeLayout) layout.findViewById(R.id.defloss_info_btn_deflosspoint);
		imvDeflossPoint = (ImageView) layout.findViewById(R.id.defloss_info_imv_deflosspoint);
		edDeflossPoint = (EditText) layout.findViewById(R.id.defloss_info_et_deflosspoint);
		imvSatisfaction = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_1);
		imvYiBan = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_2);
		imvUnSatisfaction = (ImageView) layout.findViewById(R.id.survey_survey_satisfaction_3);
		edDeflossPoint.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				DataConfig.defLossInfoQueryData.getDefLossContent().setDefLossAdvise(edDeflossPoint.getText().toString());
			}
		});
		
		if (bundle != null) {
			registNo = bundle.getString("registNo");
			lossNo = bundle.getString("itemNo");
		} else {
			registNo = "报案号出错";
			lossNo = "-3";
		}

		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.BOLD));
		TopManager.getInstance().getBtnDeflossMsg().setBackgroundResource(R.drawable.tasks_setlosstab_click);
	}

	@Override
	protected void setListener() {
		commonUtils = new CommonUtils();
		rlObject.setOnClickListener(this);
		rlFactory.setOnClickListener(this);
		rlContent.setOnClickListener(this);
		rlDeflossPoint.setOnClickListener(this);
		messagesEdit.setOnClickListener(this);
		if (DataConfig.defLossInfoQueryData != null) {
			if ("2".equals(DataConfig.defLossInfoQueryData.getDefLossContent().getSatisfieddegree())) {
			} else if ("1".equals(DataConfig.defLossInfoQueryData.getDefLossContent().getSatisfieddegree())) {

			} else if ("0".equals(DataConfig.defLossInfoQueryData.getDefLossContent().getSatisfieddegree())) {

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
		// 定损对象
		case R.id.defloss_info_btn_object:
			commonUtils.showView(context, rlObject, rocketAnimation, lvObject, imvObject, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作
					if (objectAdapter == null) {
						objectAdapter = new DInfoObjectAdapter(dInfoObjectView);
						lvObject.setAdapter(objectAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvObject);
					} else {
						lvObject.setAdapter(objectAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvObject);
					}
				}
			});
			break;
			
		// 修理厂信息
		case R.id.defloss_info_btn_factory:
			commonUtils.showView(context, rlFactory, rocketAnimation, lvFactory, imvFactory, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作
					if (factoryAdapter == null) {
						factoryAdapter = new DInfoFactoryAdapter(dInfoFactoryView);
						lvFactory.setAdapter(factoryAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvFactory);
					} else {
						lvFactory.setAdapter(factoryAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvFactory);
					}
				}
			});
			break;
		case R.id.defloss_basic_btn_messagesEdit:
			// 撰写留言
			bundle.putString("time", SystemConfig.serverTime);
			bundle.putString("nodetype", "定损");
			bundle.putString("LeavePerson", SystemConfig.loginResponse.getUserName());
			UiManager.getInstance().changeView(LeaveMessageEditActivity.class, bundle, false);
			break;	
		// 定损内容
		case R.id.defloss_info_btn_deflosscontent:
			commonUtils.showView(context, rlContent, rocketAnimation, lvContent, imvContent, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作
					if (contentAdapter == null) {
						contentAdapter = new DInfoContentAdapter(dInfoContentView);
						lvContent.setAdapter(contentAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvContent);
					} else {
						lvContent.setAdapter(contentAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvContent);
					}
				}
			});
			break;
			
		// 定损意见
		case R.id.defloss_info_btn_deflosspoint:
			commonUtils.showView(context, rlDeflossPoint, rocketAnimation, edDeflossPoint, imvDeflossPoint, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作

				}
			});
			break;
			
		// 核损退回意见
		case R.id.defloss_back_btn_deflosspoint:
			commonUtils.showView(context, rlDeflossBackPoint, rocketAnimation, edDeflossBackPoint, imvDeflossBackPoint, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作

				}
			});
			break;

		// 满意度调查
		case R.id.survey_survey_satisfaction_1:
			// 满意
			if (satisfactionFlag) {
				satisfactionFlag = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1_click);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);
				if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null)
					DataConfig.defLossInfoQueryData.getDefLossContent().setSatisfieddegree("2");
			}
			break;
		case R.id.survey_survey_satisfaction_2:
			// 一般
			if (satisfactionFlag) {
				satisfactionFlag = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2_click);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4);
				if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null)
					DataConfig.defLossInfoQueryData.getDefLossContent().setSatisfieddegree("1");
			}
			break;
		case R.id.survey_survey_satisfaction_3:
			// 不满意
			if (satisfactionFlag) {
				satisfactionFlag = false;
				imvSatisfaction.setBackgroundResource(R.drawable.checklist_face1);
				imvYiBan.setBackgroundResource(R.drawable.checklist_face2);
				imvUnSatisfaction.setBackgroundResource(R.drawable.checklist_face4_click);
				if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null)
					DataConfig.defLossInfoQueryData.getDefLossContent().setSatisfieddegree("0");
			}
			break;
		}
	}

	/**
	 * 保存数据
	 */
	public void saveData() {
		// 定损基本信息对象信息保存
		if (dInfoObjectView != null)
			dInfoObjectView.saveData();
		// 定损数据保存
		if (dInfoContentView != null) {
			dInfoContentView.saveData();
		}
		CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.defLossInfoQueryData, false);
	}

	@Override
	public void onPause() {
		// 保存数据
		saveData();
		super.onPause();
	}

	@Override
	public Integer getExit() {

		return ConstantValue.DAOHANG_DeflossInfo;
	}

	@Override
	public Integer getBackMain() {

		return ConstantValue.Back_Defloss;
	}

	private void setFourTop() {
		TopManager.getInstance().deflossPageTopBtn(new IWordDeflossTopButton() {
			@Override
			public void onDeflossWorkFlowlogClick() {
				// 流程
				UiManager.getInstance().changeView(DeflossWorkflowlogView.class, bundle, false);
			}

			@Override
			public void onDeflossPhotoClick() {
				// 拍照
				UiManager.getInstance().changeView(DeflossPhotosView.class, bundle, false);
			}

			@Override
			public void onDeflossInfoClick() {
				// 定损信息
				UiManager.getInstance().changeView(DeflossInfoActivity.class, bundle, true);
			}

			@Override
			public void onDeflossBasicClick() {
				// 定损基本信息
				UiManager.getInstance().changeView(DeflossBasicActivity.class, bundle, true);
			}
		});
	}

	@Override
	public void onResume() {
		// 顶部四个按钮
		setFourTop();
		// 控制控件的可操作性
		if (!SystemConfig.isOperate) {
			edDeflossPoint.setEnabled(false);
			imvSatisfaction.setEnabled(false);
			imvYiBan.setEnabled(false);
			imvUnSatisfaction.setEnabled(false);
		} else {
			edDeflossPoint.setEnabled(true);
			imvSatisfaction.setEnabled(true);
			imvYiBan.setEnabled(true);
			imvUnSatisfaction.setEnabled(true);
		}

		// 理赔同步撤销
		TopManager.getInstance().btnTongBuDeflossContinue(new TopBtnTongBuDeflossContinue() {
			@Override
			public void onClick() {
				synchCancel();
			}

		});

		// 理赔同步和提交按钮
		TopManager.getInstance().setTopBtnSumbitDefloss(new TopBtnSumbitDefloss() {
			TopBtnSumbitDefloss topBtnSumbitDefloss;

			@Override
			public void OnclickTongBu() {
				topBtnSumbitDefloss = this;
				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【定损同步】  <<<<<<==============
					 *                              ***********************
					 */ 
					new AsyncTask<String, Void, CommonResponse>() {
						protected void onPreExecute() {
							//handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
						};
						protected void onPostExecute(CommonResponse result) {
							if("YES".equals(result.getResponseCode())){
								DeflossinfoUploadToClaimSystem uploadToClaimSystem = new DeflossinfoUploadToClaimSystem(context);
								boolean isSynch = uploadToClaimSystem.synch(registNo, lossNo, topBtnSumbitDefloss);
								if(isSynch){
									synchOrSubmit("synch");
								}
							}else{
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								// 提示错误信息
								Message message = Message.obtain();
								if (result != null) {
									message.obj = result.getResponseMessage();
								} else {
									message.obj = "提交失败!";
								}
								message.what = ConstantValue.ERROE;
								handler.sendMessage(message);
							}
						};
						protected CommonResponse doInBackground(String... params) {
							return VerifyLossSubmitHttpService.lossApplyService(registNo,lossNo,SystemConfig.USERLOGINNAME,context.getString(R.string.http_url));
						}
					}.execute("");
				}else{
					Toast.showToast(context, "不能【定损同步】，请确定是否“受理任务”或“继续处理”！");
				}
			}

			@Override
			public void synchOrSubmit(final String type) {
				// 提交理赔 add by jingtuo start
				new AsyncTask<String, Void, CommonResponse>() {
					@Override
					protected void onPreExecute() {
						saveData();
						handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
					};

					@Override
					protected void onPostExecute(CommonResponse result) {
						Toast.showToast(context, "同步/提交理赔");
						if ("YES".equals(result.getResponseCode())) {
							handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
							if (type.equals("synch")) {
								CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
								certainLossTask.setSurveytaskstatus("2");
								certainLossTask.setSynchrostatus("synchroApply");
								List<CertainLossTask> list = new ArrayList<CertainLossTask>();
								list.add(certainLossTask);
								CertainLossTaskAccess.insertOrUpdate(list, true);

							} else if (type.equals("submit")) {
								ContentValues values = new ContentValues();
								values.put("surveytaskstatus", "4");
								values.put("verifypassflag", "1");
								values.put("completetime", result.getHandletime());
								DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[] { registNo, lossNo });
							}

							// 判断是否有上传队列
							if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
								
								//Toast.showToast(context, "开始上传，请稍等...");
							}else{
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
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
					protected CommonResponse doInBackground(String... params) {
						DataConfig.defLossInfoQueryData.setSubmitType(params[0]);
						return VerifyLossSubmitHttpService.lossSubmitService(DataConfig.defLossInfoQueryData, context.getString(R.string.http_url));
					}

				}.execute(type);
				// 提交理赔 add by jingtuo end
			}

			@Override
			public void OnclickSumbit() {
				topBtnSumbitDefloss = this;
				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【定损提交】  <<<<<<==============
					 *                              ***********************
					 */  
					new AsyncTask<String, Void, CommonResponse>() {
						protected void onPreExecute() {
							//handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
						};
						protected void onPostExecute(CommonResponse result) {
							if("YES".equals(result.getResponseCode())){
								DeflossinfoUploadToClaimSystem uploadToClaimSystem = new DeflossinfoUploadToClaimSystem(context);
								boolean isSynch = uploadToClaimSystem.submit(registNo, lossNo, topBtnSumbitDefloss);
								if(isSynch){
									synchOrSubmit("submit");
								}
							}else{
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								// 提示错误信息
								Message message = Message.obtain();
								if (result != null) {
									message.obj = result.getResponseMessage();
								} else {
									message.obj = "提交失败!";
								}
								message.what = ConstantValue.ERROE;
								handler.sendMessage(message);
							}
						};
						protected CommonResponse doInBackground(String... params) {
							return VerifyLossSubmitHttpService.lossApplyService(registNo,lossNo,SystemConfig.USERLOGINNAME,context.getString(R.string.http_url));
						}
					}.execute("");
				}else{
					Toast.showToast(context, "不能【定损提交】，请确定是否“受理任务”或“继续处理”！");
				}
			}
		});

		if (dInfoObjectView != null) {
			dInfoObjectView.controlEd();
			dInfoObjectView.setData();
		}

		if (dInfoFactoryView != null) {
			dInfoFactoryView.controlEd();
			dInfoFactoryView.setData();
		}

		if (dInfoContentView != null) {
			dInfoContentView.controlEd();
			dInfoContentView.setData();
		}
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
					// 控件可以操作
					Toast.showToast(context, "任务理赔同步撤销成功");
					CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
					certainLossTask.setSurveytaskstatus("2");
					certainLossTask.setSynchrostatus("synchroCancel");
					List<CertainLossTask> list = new ArrayList<CertainLossTask>();
					list.add(certainLossTask);
					CertainLossTaskAccess.insertOrUpdate(list, true);

					// 显示同步按钮
					TopManager.getInstance().TongBuDeflossContinue(true);
					// 控件可操作
					SystemConfig.isOperate = true;
					// 界面刷新
					UiManager.getInstance().changeView(DeflossInfoActivity.class, null, false);

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
				requestSynchro.setLossNo(lossNo);
				requestSynchro.setCancleType("Synchback");
				requestSynchro.setNodeType("certainLoss");
				return SynchroClaimHttpService.synchroClaimService(requestSynchro, params[0]);
			}

		}.execute(context.getString(R.string.http_url));
	}
}
