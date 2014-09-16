package com.sinosoftyingda.fastclaim.work.page;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskinfo;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.WorkFlowRequest;
import com.sinosoftyingda.fastclaim.common.model.WorkflowlogViewResponse;
import com.sinosoftyingda.fastclaim.common.service.CheckSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.WorkFlowHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
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
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;
import com.sinosoftyingda.fastclaim.survey.page.SSurveyActivity;
import com.sinosoftyingda.fastclaim.survey.utils.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.adapter.CertainLossAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.CheckAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.SchedulesAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.VerifyLossAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.VerifyPriceAdapter;

/**
 * 查勘工作流查看
 * 
 * @author haoyun 20130302
 * 
 */
public class WorkflowlogView extends BaseView implements OnClickListener {
	private View					gView;
	private ImageView				gIvDispatch, gIvSurveyLine, gIvSurvey, gIvSetlossLine, gIvSetloss, gIvPriceLine, gIvPrice, gIvDamageLine, gIvDamage;
	private WorkflowlogViewResponse	gWorkflowlogResponse	= new WorkflowlogViewResponse();
	private List<ImageView>			isClick					= new ArrayList<ImageView>();
	private ListView				gLvContent;
	private TextView				gTvTitle;

	public WorkflowlogView(Context context, Bundle bundle) {
		super(context, bundle);

	}

	/***
	 * 判断登录用户的
	 */
	private void workService() {

		new AsyncTask<String, Void, WorkflowlogViewResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(WorkflowlogViewResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					// 关闭进度对话框
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					gWorkflowlogResponse = result;
					initBackground();
				} else {

					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected WorkflowlogViewResponse doInBackground(String... params) {
				WorkFlowRequest workFlowRequest = new WorkFlowRequest();
				workFlowRequest.setRegistNo(SystemConfig.PHOTO_CLAIMNO);
				workFlowRequest.setUserCode(SystemConfig.USERLOGINNAME);
				return WorkFlowHttpService.workFlowService(workFlowRequest, context.getString(R.string.http_url));
			}

		}.execute();

	}

	@Override
	public View getView() {
		return gView;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_Survey;
	}

	private String	registNo;

	@Override
	protected void init() {
		/**
		 * 初始化组件
		 */
		gView = inflater.inflate(R.layout.procedure_query, null);
		/**
		 * ImageView
		 */
		gIvDispatch = (ImageView) gView.findViewById(R.id.pq_iv_gray_dispatch);
		gIvSurveyLine = (ImageView) gView.findViewById(R.id.pq_iv_gray_survey_line);
		gIvSurvey = (ImageView) gView.findViewById(R.id.pq_iv_gray_survey);
		gIvSetlossLine = (ImageView) gView.findViewById(R.id.pq_iv_gray_setloss_line);
		gIvSetloss = (ImageView) gView.findViewById(R.id.pq_iv_gray_setloss);
		gIvPriceLine = (ImageView) gView.findViewById(R.id.pq_iv_gray_price_line);
		gIvPrice = (ImageView) gView.findViewById(R.id.pq_iv_gray_price);
		gIvDamageLine = (ImageView) gView.findViewById(R.id.pq_iv_gray_damage_line);
		gIvDamage = (ImageView) gView.findViewById(R.id.pq_iv_gray_damage);
		/**
		 * ListView
		 */
		gLvContent = (ListView) gView.findViewById(R.id.pq_lv_content);
		/**
		 * TextView
		 */
		gTvTitle = (TextView) gView.findViewById(R.id.pq_tv_title);
		if (bundle != null) {
			registNo = bundle.getString("registNo");
		} else {
			registNo = "报案号错误";
		}
		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.BOLD));
		TopManager.getInstance().getBtnDeflossProcess().setBackgroundResource(R.drawable.tasks_processestab_click);
		TopManager.getInstance().getBtnSurveyProcess().setBackgroundResource(R.drawable.tasks_processestab_click);

	}

	/**
	 * 初始化导航图片方法
	 */
	private void initBackground() {
		ImageView imageView = null;
		if (gWorkflowlogResponse.getSchedule() != null) {
			gIvDispatch.setBackgroundResource(R.drawable.green_dispatch);
			isClick.add(gIvDispatch);
			imageView = gIvDispatch;
		}
		if (gWorkflowlogResponse.getCheck() != null) {
			gIvSurveyLine.setBackgroundResource(R.drawable.process_greenline);
			gIvSurvey.setBackgroundResource(R.drawable.green_survey);
			isClick.add(gIvSurvey);
			imageView = gIvSurvey;
		}
		if (gWorkflowlogResponse.getCertainLossList().size() > 0) {
			gIvSetlossLine.setBackgroundResource(R.drawable.process_greenline);
			gIvSetloss.setBackgroundResource(R.drawable.green_setloss);
			isClick.add(gIvSetloss);
			imageView = gIvSetloss;
		}
		if (gWorkflowlogResponse.getVerifyPriceList().size() > 0) {
			gIvPriceLine.setBackgroundResource(R.drawable.process_greenline);
			gIvPrice.setBackgroundResource(R.drawable.green_price);
			isClick.add(gIvPrice);
			imageView = gIvPrice;
		}
		if (gWorkflowlogResponse.getVerifyLossList().size() > 0) {
			gIvDamageLine.setBackgroundResource(R.drawable.process_greenline);
			gIvDamage.setBackgroundResource(R.drawable.green_survey);
			isClick.add(gIvDamage);
			imageView = gIvDamage;
		}
		if (imageView != null)
			replaceImage(imageView);

	}

	@Override
	protected void setListener() {
		/**
		 * ImageView
		 */
		gIvDispatch.setOnClickListener(this);
		gIvSurvey.setOnClickListener(this);
		gIvSetloss.setOnClickListener(this);
		gIvPrice.setOnClickListener(this);
		gIvDamage.setOnClickListener(this);
	}

	private void replaceImage(View v) {
		switch (v.getId()) {
		case R.id.pq_iv_gray_dispatch:// 调度
			v.setBackgroundResource(R.drawable.bgreen_dispatch);
			SchedulesAdapter schedulesAdapter = new SchedulesAdapter(context, gWorkflowlogResponse.getSchedule());
			gLvContent.setAdapter(schedulesAdapter);
			gTvTitle.setText("调度");
			break;

		case R.id.pq_iv_gray_survey:// 查勘
			v.setBackgroundResource(R.drawable.bgreen_survey);
			CheckAdapter checkAdapter = new CheckAdapter(context, gWorkflowlogResponse.getCheck());
			gLvContent.setAdapter(checkAdapter);
			gTvTitle.setText("查勘");
			break;
		case R.id.pq_iv_gray_setloss:// 定损
			v.setBackgroundResource(R.drawable.bgreen_setloss);
			CertainLossAdapter certainLossAdapter = new CertainLossAdapter(context, gWorkflowlogResponse.getCertainLossList());
			gLvContent.setAdapter(certainLossAdapter);
			gTvTitle.setText("定损");
			break;
		case R.id.pq_iv_gray_price:// 核价
			v.setBackgroundResource(R.drawable.bgreen_price);
			VerifyPriceAdapter verifyPriceAdapter = new VerifyPriceAdapter(context, gWorkflowlogResponse.getVerifyPriceList());
			gLvContent.setAdapter(verifyPriceAdapter);
			gTvTitle.setText("核价");
			break;
		case R.id.pq_iv_gray_damage:// 核损
			v.setBackgroundResource(R.drawable.bgreen_damage);
			VerifyLossAdapter verifyLossAdapter = new VerifyLossAdapter(context, gWorkflowlogResponse.getVerifyLossList());
			gLvContent.setAdapter(verifyLossAdapter);
			gTvTitle.setText("核损");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		boolean isOnClick = false;
		/**
		 * 用于判断当前点击的按钮是否任务已经到达此环节 如果没有到达不可以点击 将已到达环节的按钮背景都改为绿色
		 * 
		 */
		for (int i = 0; i < isClick.size(); i++) {

			if (isClick.get(i).getId() == v.getId()) {
				isOnClick = true;
				break;
			}

		}

		if (isOnClick) {

			for (int i = 0; i < isClick.size(); i++) {
				switch (isClick.get(i).getId()) {
				case R.id.pq_iv_gray_dispatch:
					isClick.get(i).setBackgroundResource(R.drawable.green_dispatch);
					break;
				case R.id.pq_iv_gray_survey:
					isClick.get(i).setBackgroundResource(R.drawable.green_survey);
					break;
				case R.id.pq_iv_gray_setloss:
					isClick.get(i).setBackgroundResource(R.drawable.green_setloss);
					break;
				case R.id.pq_iv_gray_price:
					isClick.get(i).setBackgroundResource(R.drawable.green_price);
					break;
				case R.id.pq_iv_gray_damage:
					isClick.get(i).setBackgroundResource(R.drawable.green_damage);
					break;
				}
			}
			/**
			 * 满足上面条件则更换选中图片
			 */
			replaceImage(v);
		}
	}

	@Override
	public Integer getExit() {
		return ConstantValue.DAOHANG_SurveyWordFlow;
	}

	/***
	 * chenjianfan
	 */
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
	public Integer getBackMain() {

		return ConstantValue.Back_Survey;

	}

	@Override
	public void onResume() {
		// 顶部四个按钮
		setFourDopBtn();
		// 理赔交互和提交
		TopManager.getInstance().setTopBtnSumbitSurvery(new TopBtnSumbitSurvery() {
			// 判断是否有联系过客户
			boolean isTakephotos = TblTaskinfo.checkIsTakephotos(registNo);
			
			@Override
			public void OnclickSumbit() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘提交】  <<<<<<==============
				 *                              ***********************
				 */ 
				if (SystemConfig.isOperate) {
					// 校验身份证号码是否为空
					String idCard = DataConfig.tblTaskQuery.getCheckDriver().get(0).getIdentifyNumber();
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else if (!CheckSurveyValue.idCardIsNull || !CheckSurveyValue.idCard(idCard)) {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
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
									if (CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)) {
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
									} else {
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

			@Override
			public void OnclickTongBu() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘同步】  <<<<<<==============
				 *                              ***********************
				 */ 
				// 交互
				if (SystemConfig.isOperate) {
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
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
									TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
	
									// 判断是否有上传队列
									if (CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)) {
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
	
										// Toast.showToast(context, "开始上传，请稍等...");
									} else {
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
							UiManager.getInstance().changeView(WorkflowlogView.class, null, false);

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
		// 与服务端交互
		workService();
		super.onResume();
	}

}
