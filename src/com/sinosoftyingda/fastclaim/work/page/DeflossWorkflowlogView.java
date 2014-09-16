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
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.WorkFlowRequest;
import com.sinosoftyingda.fastclaim.common.model.WorkflowlogViewResponse;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.VerifyLossSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.WorkFlowHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordSurveyTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuDeflossContinue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossInfoActivity;
import com.sinosoftyingda.fastclaim.defloss.service.DeflossinfoUploadToClaimSystem;
import com.sinosoftyingda.fastclaim.defloss.service.JYLioneyeToolsActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.page.DeflossPhotosView;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.adapter.CertainLossAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.CheckAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.SchedulesAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.VerifyLossAdapter;
import com.sinosoftyingda.fastclaim.work.adapter.VerifyPriceAdapter;

/**
 * 工作流查看
 * 
 * @author haoyun 20130302 copy chenjianfan
 */
public class DeflossWorkflowlogView extends BaseView implements OnClickListener {
	private View					gView;
	private ImageView				gIvDispatch, gIvSurveyLine, gIvSurvey, gIvSetlossLine, gIvSetloss, gIvPriceLine, gIvPrice, gIvDamageLine, gIvDamage;
	private WorkflowlogViewResponse	gWorkflowlogResponse	= new WorkflowlogViewResponse();
	private List<ImageView>			isClick					= new ArrayList<ImageView>();
	private ListView				gLvContent;
	private TextView				gTvTitle;
	private String					registNo;
	private String					lossNo;

	public DeflossWorkflowlogView(Context context, Bundle bundle) {
		super(context, bundle);
		// gWorkflowlogResponse = (WorkflowlogViewResponse) bundle
		// .getSerializable("gWorkflowlogResponse");
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return gView;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_Defloss;
	}

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
			lossNo = bundle.getString("itemNo");
		} else {
			registNo = "报案号出错";
			lossNo = "-3";
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

	/***
	 * 加载数据
	 */
	private void initData() {

		new AsyncTask<String, Void, WorkflowlogViewResponse>() {
			@Override
			protected void onPreExecute() {
				// 在 doInBackground(Params...)之前被调用，在ui线程执行

				// 开启进度对话框
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(WorkflowlogViewResponse result) {
				// 在 doInBackground(Params...)之后被调用，在ui线程执行
				handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				if (result != null && result.getResponseCode() != null) {
					if (result.getResponseCode().equalsIgnoreCase("YES")) {
						gWorkflowlogResponse = result;
						initBackground();
					} else {
						Message message = Message.obtain();
						message.obj = result.getResponseMessage();
						message.what = ConstantValue.ERROE;
						handler.sendMessage(message);
					}

				}
				super.onPostExecute(result);
			}

			@Override
			protected WorkflowlogViewResponse doInBackground(String... params) {
				// 处理后台执行的任务，在后台线程执行

				try {
					WorkFlowRequest wfr = new WorkFlowRequest();
					wfr.setRegistNo(SystemConfig.PHOTO_CLAIMNO);
					wfr.setUserCode(SystemConfig.USERLOGINNAME);
					return WorkFlowHttpService.workFlowService(wfr, context.getString(R.string.http_url));
				} catch (Exception e) {

					e.printStackTrace();
					return null;
				}

			}

		}.execute();

	}

	@Override
	public Integer getExit() {
		return ConstantValue.DAOHANG_DeflossWordFlow;
	}

	/***
	 * chenjianfan
	 */
	private void setFourDopBtn() {
		TopManager.getInstance().surveyPageTopBtn(new IWordSurveyTopButton() {
			@Override
			public void onSurveyWorkFlowlogClick() {
				// 定损流程
				UiManager.getInstance().changeView(DeflossWorkflowlogView.class, bundle, false);
			}

			@Override
			public void onSurveyPhotoClick() {
				// 拍照
				UiManager.getInstance().changeView(DeflossPhotosView.class, bundle, false);
			}

			@Override
			public void onSurveyInfoClick() {
				// 定损信息页面 不要乱改
				UiManager.getInstance().changeView(DeflossInfoActivity.class, bundle, true);
			}

			@Override
			public void onSurveyBasicClick() {
				// 定损基本信息 不要乱改
				UiManager.getInstance().changeView(DeflossBasicActivity.class, bundle, true);
			}
		});
	}

	@Override
	public Integer getBackMain() {
		return ConstantValue.Back_Defloss;
	}

	@Override
	public void onResume() {
		setFourDopBtn();
		// 理赔同步撤销
		TopManager.getInstance().btnTongBuDeflossContinue(new TopBtnTongBuDeflossContinue() {
			@Override
			public void onClick() {
				synchCancel();
			}
		});

		// 理赔交互和提交
		TopManager.getInstance().setTopBtnSumbitDefloss(new TopBtnSumbitDefloss() {
			TopBtnSumbitDefloss	topBtnSumbitDefloss;
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

			/**
			 * 同步或提交方法
			 */
			@Override
			public void synchOrSubmit(final String type) {
				saveData();
				// 提交理赔 add by jingtuo start
				new AsyncTask<String, Void, CommonResponse>() {
					@Override
					protected void onPreExecute() {
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
							// TblLossTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(),
							// DataConfig.defLossInfoQueryData, true);
							CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.defLossInfoQueryData, false);

							// 判断是否有上传队列
							if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
								// Toast.showToast(context, "开始上传，请稍等...");
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
				// 提交理赔 add by jingtuo start
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
				// 提交理赔 add by jingtuo end
			}
		});
		initData();
		initBackground();
		super.onResume();
	}

	/**
	 * 保存数据
	 */
	public void saveData() {
		// 修理厂信息
		if (bundle != null && DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null) {
			// 修理厂代码
			DataConfig.defLossInfoQueryData.getDefLossContent().setRepairFactoryCode(bundle.getString(JYLioneyeToolsActivity.KEY_FACTORY_CODE));
			// 修理厂名称
			DataConfig.defLossInfoQueryData.getDefLossContent().setRepairFactoryName(bundle.getString(JYLioneyeToolsActivity.KEY_FACTORY_NAME));
			// 修理厂合作性质
			DataConfig.defLossInfoQueryData.getDefLossContent().setRepairCooperateFlag(bundle.getString(JYLioneyeToolsActivity.KEY_FACTORY_FLAG));
			// 修理厂资质
			DataConfig.defLossInfoQueryData.getDefLossContent().setRepairapTitude(bundle.getString(JYLioneyeToolsActivity.KEY_FACTORY_APTITUDE));
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
					SystemConfig.isOperate = true;
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
					UiManager.getInstance().changeView(DeflossWorkflowlogView.class, null, false);

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
