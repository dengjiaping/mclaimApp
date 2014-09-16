package com.sinosoftyingda.fastclaim.survey.page;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskinfo;
import com.sinosoftyingda.fastclaim.common.model.ArrivedRequest;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.RedioRequest;
import com.sinosoftyingda.fastclaim.common.model.ScheDuleItem;
import com.sinosoftyingda.fastclaim.common.model.SendForRequest;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.ArrivedHttpService;
import com.sinosoftyingda.fastclaim.common.service.CheckSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.DetailTaskQueryHttpService;
import com.sinosoftyingda.fastclaim.common.service.RedioHttpService;
import com.sinosoftyingda.fastclaim.common.service.SendForHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.ui.utils.SetDateTimeUsils;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogGaiPai;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.DrawerUtils;
import com.sinosoftyingda.fastclaim.common.views.DrawerUtils.IBtnAccept;
import com.sinosoftyingda.fastclaim.common.views.DrawerUtils.IBtnGaiPai;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordSurveyTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitSurvery;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuSurveryContinue;
import com.sinosoftyingda.fastclaim.common.views.TopManager.xiePeiBtnVideo;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.hkvideo.MPUSDKActivity;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.page.PhotosView;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyCaseInfoAdapter;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyContactAdapter;
import com.sinosoftyingda.fastclaim.survey.adapter.SurveyTaskInfoAdapter;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageEditActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageQueryActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SGuaranteeslipActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SHistoryActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.WorkflowlogView;

/***
 * 查勘基本页面
 * 
 * @author chenjianfan
 * 
 */
public class SBasicActivity extends BaseView implements OnClickListener, OnTouchListener, TextWatcher {
	private CommonUtils commonUtils;
	private FastClaimDbHelper dbHelper;
	private DrawerUtils drawerUtils;
	private View gDrawer;
	private View layout;
	private AnimationDrawable rocketAnimation;
	private RelativeLayout caseinfoRl;
	private RelativeLayout taskinfoRl;
	private PromptManager promptManager;
	// 控件
	private ImageView ACaseImv;
	private ImageView ATaskImg;
	// 报案信息listview
	private ListView caseListView;
	private SurveyCaseInfoAdapter caseInfoAdapter;
	// 任务信息listview
	private ListView taskListView;
	private SurveyTaskInfoAdapter taskInfoAdapter;
	// 保单信息
	private RelativeLayout rlBaoDan;
	// 历史赔案
	private RelativeLayout rlHistory;
	// 报案号和任务类型
	private String registNo;
	private String checkStatue;
	// 预约时间
	private RelativeLayout rlOrderTime;
	private LinearLayout llOrderTime;
	private ImageView imvOrderTime;
	private SetDateTimeUsils setDateTimeUsils;
	private EditText edOrderTime;

	// 联系信息
	private RelativeLayout contactRL;
	private ImageView contactImv;
	private ListView contactListView;
	private SContactView sContactView;
	private SurveyContactAdapter contactAdapter;

	// 定损项目编号
	private CheckTask checkTask;
	private boolean isTongBuCancel = false;
	// 撰写留言
	private RelativeLayout messagesEditxiepei;
	// 留言查询
	private RelativeLayout messagesQuery;
	
	public SBasicActivity(Context context, Bundle bundle) {
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
		// 标识位不要动
		SystemConfig.isDeflossPage = false;

		// 报案号和任务类型
		if (bundle != null) {
			registNo = bundle.getString("registNo");
			// 地区
			String arean = registNo.substring(1, 3);
			if ("01".equals(arean)) {
				SystemConfig.AREAN = "北京";
			} else if ("11".equals(arean)) {
				SystemConfig.AREAN = "北京";
			} else if ("02".equals(arean)) {
				SystemConfig.AREAN = "上海";
			} else {
				SystemConfig.AREAN = "";
			}
			checkStatue = bundle.getString("checkStatue");
			SystemConfig.PHOTO_CLAIMNO = registNo;
			checkTask = CheckTaskAccess.findByRegistno(registNo);
		} else {
			registNo = "报案号错误";
			SystemConfig.PHOTO_CLAIMNO = "00000000";
			checkTask = new CheckTask();
		}

		// 加载布局文件
		layout = inflater.inflate(R.layout.survey_basic, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);

		promptManager = new PromptManager();

		// 初始化抽屉
		gDrawer = layout.findViewById(R.id.survey_basic_drawer);
		drawerUtils = new DrawerUtils(gDrawer);

		dbHelper = new FastClaimDbHelper(context);
		// 获取报案号
		commonUtils = new CommonUtils();
		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.BOLD));
		TopManager.getInstance().getBtnSurveyBasicMsg().setBackgroundResource(R.drawable.tasks_infortab_click);
		caseinfoRl = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_caseinfo);
		taskinfoRl = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_taskinfo);
		ACaseImv = (ImageView) layout.findViewById(R.id.survey_basic_caseinfo_ll);
		ATaskImg = (ImageView) layout.findViewById(R.id.survey_basic_taskinfo_ll);
		caseListView = (ListView) layout.findViewById(R.id.survey_basic_caseinfo_listview);
		taskListView = (ListView) layout.findViewById(R.id.survey_basic_taskinfo_listview);
		rlBaoDan = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_baodaninfo);
		rlHistory = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_history);
		rlOrderTime = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_predicttime);
		llOrderTime = (LinearLayout) layout.findViewById(R.id.survey_basic_ll_ordertime);
		imvOrderTime = (ImageView) layout.findViewById(R.id.survey_basic_taskinfo_ll_ordertime);
		messagesQuery = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_messagesQuery);
		messagesEditxiepei=(RelativeLayout) layout.findViewById(R.id.survey_basic_btn_messagesEditxiepei);
		edOrderTime = (EditText) layout.findViewById(R.id.survrybasic_taskinfo_listview_item_ed_ordertime);
		setDateTimeUsils = new SetDateTimeUsils(context);
		edOrderTime.setText(checkTask.getOrdertime());

		contactRL = (RelativeLayout) layout.findViewById(R.id.survey_basic_btn_contactinfo);
		if (!SystemConfig.UserRightIsAdvanced) {
			contactRL.setVisibility(View.VISIBLE);
			contactImv = (ImageView) layout.findViewById(R.id.survey_basic_contactinfo_ll);
			contactListView = (ListView) layout.findViewById(R.id.survey_basic_contactinfo_listview);
			messagesEditxiepei.setVisibility(View.VISIBLE);
			// 联系人信息
			if (sContactView == null)
				sContactView = new SContactView(context);
		} else {
			contactRL.setVisibility(View.GONE);
			messagesEditxiepei.setVisibility(View.GONE);
		}

		// 初始化数据
		setData();
		controlEd();

	}

	/***
	 * 控制显示
	 */
	private void controlEd() {
		SystemConfig.setOperate(checkTask);
		
		// 根据任务状态是否显示抽屉
		if (checkStatue == null || TextUtils.isEmpty(checkStatue)) {
			if (gDrawer.getVisibility() == View.VISIBLE){
				gDrawer.setVisibility(View.GONE);
			}
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
		} else if ("4".equals(checkStatue)) {
			if (gDrawer.getVisibility() == View.VISIBLE){
				gDrawer.setVisibility(View.GONE);
			}
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
		} else if ("1".equals(checkStatue)) {
			if (gDrawer.getVisibility() == View.GONE){
				gDrawer.setVisibility(View.VISIBLE);
			}
			
			// 任务已经受理才显示同步和提交按钮
			if(checkTask != null && 1 == checkTask.getIsaccept()){
				TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
			}else{
				TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
			}
		} else if ("2".equals(checkStatue)) {
			if (gDrawer.getVisibility() == View.VISIBLE){
				gDrawer.setVisibility(View.GONE);
			}
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
		} else {
			if (gDrawer.getVisibility() == View.VISIBLE){
				gDrawer.setVisibility(View.GONE);
			}
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
		}
		// 判断是否接受按钮
		if (checkTask != null && 1 == checkTask.getIsaccept()) {
			SystemConfig.isOperate = true;
			drawerUtils.setAcceptBtn(false);
		}
		// 判断是否为改派申请
		if (checkTask != null && "apply".equals(checkTask.getApplycannelstatus())) {
			drawerUtils.setGaiPaiBtn(false);
			SystemConfig.isOperate = false;
		}
		// 同步和撤销按钮控制
		if ("synchroApply".equals(checkTask.getSynchrostatus())) {
			// 显示同步撤回按钮
			isTongBuCancel = true;
			TopManager.getInstance().TongBuSurveryContinue(false);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			SystemConfig.isOperate = false;
		} else {
			TopManager.getInstance().TongBuSurveryContinue(true);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			isTongBuCancel = false;
		}

	}

	private void setData() {
		Log.i(SBasicActivity.class.getSimpleName(), "报案号" + registNo);
		Log.i(SBasicActivity.class.getSimpleName(), "案件类型" + checkStatue);
		if (!TextUtils.isEmpty(checkStatue) || !TextUtils.isEmpty(registNo)) {
			new AsyncTask<String, Void, DetailTaskQueryResponse>() {
				@Override
				protected void onPreExecute() {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
				};

				@Override
				protected DetailTaskQueryResponse doInBackground(String... params) {
					// 联系客户时间和任务是否受理了
					DetailTaskQueryRequest detailTaskQueryRequest = new DetailTaskQueryRequest();
					detailTaskQueryRequest.setCheckStatus(params[1]);
					detailTaskQueryRequest.setRegistNo(params[0]);
					boolean isUpdate = true;
					if (registNo != null) {
						if (CheckTaskAccess.findByRegistno(registNo).getSynchrostatus().equals("synchroApply"))
							isUpdate = true;
					}
					DetailTaskQueryResponse detailTaskQueryResponse = null;
					if (checkStatue != null) {
						if (checkStatue.equals("1") || checkStatue.equals("") || (checkStatue.equals("2") && !checkTask.getSynchrostatus().equals("synchroApply"))) {
							SQLiteDatabase db = dbHelper.getReadableDatabase();
							if (TblTaskQuery.isExist(db, "REGISTNO=?", "TASKQUERY", new String[] { params[0] }, true)) {
								detailTaskQueryResponse = DetailTaskQueryResponse.getDetailTaskQueryResponse();
								detailTaskQueryResponse.setResponseCode("YES");
							} else {
								detailTaskQueryResponse = DetailTaskQueryHttpService.detailTaskQuerySercive(detailTaskQueryRequest, context.getString(R.string.http_url), isUpdate);
							}
						} else {
							detailTaskQueryResponse = DetailTaskQueryHttpService.detailTaskQuerySercive(detailTaskQueryRequest, context.getString(R.string.http_url), isUpdate);
						}
					}
					return detailTaskQueryResponse;
				}

				@Override
				protected void onPostExecute(DetailTaskQueryResponse result) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					if (result != null && "YES".equals(result.getResponseCode())) {
						SQLiteDatabase db = dbHelper.getReadableDatabase();
						// 把数据放在全局变量里面
						DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(db, registNo);
						DataConfig.tblTaskQuery.setLinkcustomTime(String.valueOf(checkTask.getContacttime()));
						// 显示联系人信息
						if (!SystemConfig.UserRightIsAdvanced)
							if (sContactView != null)
								sContactView.setXiePeiViewValue();
					} else {
						if (result != null) {
							Message message = Message.obtain();
							message.obj = result.getResponseMessage();
							message.what = ConstantValue.ERROE;
							handler.sendMessage(message);
							UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
						}
					}
					SQLiteDatabase db = dbHelper.getReadableDatabase();
					// 把数据放在全局变量里面
					DetailTaskQueryResponse.detailTaskQueryResponse = null;
					DataConfig.tblTaskQuery = DetailTaskQueryResponse.getDetailTaskQueryResponse();
					DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(db, registNo);
					// 联系客户时间
					Log.i(SBasicActivity.class.getSimpleName(), "联系客户时间" + checkTask.getContacttime());
					DataConfig.tblTaskQuery.setLinkcustomTime(String.valueOf(checkTask.getContacttime()));
					db = dbHelper.getReadableDatabase();
					TblTaskQuery.insertOrUpdate(db, DataConfig.tblTaskQuery, true, false, false);
					Log.i(SBasicActivity.class.getSimpleName(), "显示同步撤销" + checkTask.getSynchrostatus());

					if ("synchroApply".equals(checkTask.getSynchrostatus())) {
						// 显示同步撤销
						TopManager.getInstance().TongBuSurveryContinue(false);
					} else {
						// 显示理赔同步
						TopManager.getInstance().TongBuSurveryContinue(true);
					}
					String compensateRate=DataConfig.tblTaskQuery.getCompensateRate();
					android.util.Log.i("yxf", "----compensateRate--"+compensateRate);
					if("".equals(compensateRate.trim())||compensateRate==null){
						
					}else{
						PromptManager.showUpdateDialog(context, "赔付率系数为"+compensateRate+"%", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
					}
					
					
					
					
					
				};
			}.execute(registNo, checkStatue);
		} else {
			Toast.showToast(context, "报案号和任务类型为空");
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
	protected void setListener() {
		contactRL.setOnClickListener(this);
		rlOrderTime.setOnClickListener(this);
		caseinfoRl.setOnClickListener(this);
		taskinfoRl.setOnClickListener(this);
		rlBaoDan.setOnClickListener(this);
		rlHistory.setOnClickListener(this);
		messagesQuery.setOnClickListener(this);
		edOrderTime.setOnTouchListener(this);
		edOrderTime.addTextChangedListener(this);
		messagesEditxiepei.setOnClickListener(this);
		drawerUtils.setAcceptOnClick(new IBtnAccept() {
			@Override
			public void onClick() {
				/**
				 * 接受任务，接受任务时间插入查勘详细表
				 */
				promptManager.showDialog(context, "是否接受此任务?", R.string.yes, R.string.no, new ShowDialogPositiveButton() {
					@Override
					public void setPositiveButton() {
						// 确定
						if (!SystemConfig.isUserExperience) {
							// 判断是否有联系过客户
							boolean isContact = UtilIsNotNull.isNotNullContact(DataConfig.tblTaskQuery);
							if(isContact){
								arrivedService(registNo);
							}else{
								Toast.showToast(context, "请先与客户取得联系！");
							}
						}
					}

					@Override
					public void setNegativeButton() {
						// 取消
					}
				});
			}
		});
		drawerUtils.setGaiPaiOnClick(new IBtnGaiPai() {
			@Override
			public void onClick() {
				promptManager.showSingnRadio(context, new ShowDialogGaiPai() {
					@Override
					public void setPositiveButton(String reason) {
						final String str = reason;
						if (!TextUtils.isEmpty(str)) {
							List<CertainLossTask> clts = CertainLossTaskAccess.getByRegistNo(registNo);
							if (clts != null && clts.size() >= 1) {
								promptManager.showDialog(context, "是否关联定损任务", R.string.yes, R.string.no, new ShowDialogPositiveButton() {
									@Override
									public void setPositiveButton() {
										if (!SystemConfig.isUserExperience) {
											// for update by haoyun
											// 20130401
											// begin
											if (SystemConfig.loginResponse.getDutyFlag().equals("on")) {
												sendForService("1", str);
											} else {
												handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
												Message message = Message.obtain();
												message.obj = "下班状态不可以改派任务!";
												message.what = ConstantValue.ERROE;
												handler.sendMessage(message);
											}
										}
									}

									@Override
									public void setNegativeButton() {
										if (!SystemConfig.isUserExperience) {
											if (SystemConfig.loginResponse.getDutyFlag().equals("on")) {
												sendForService("0", str);
											} else {
												handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
												Message message = Message.obtain();
												message.obj = "下班状态不可以改派任务!";
												message.what = ConstantValue.ERROE;
												handler.sendMessage(message);
											}
										}
									}
								});
							} else {
								sendForService("0", reason);
							}
						} else {
							Toast.showToast(context, "请输入改派原因");
						}
					}

					@Override
					public void setNegativeButton() {

					}
				});
			}
		});
	}

	/***
	 * haoyun add for update jingtuo
	 */
	private void arrivedService(final String registNo) {
		new AsyncTask<String, Void, CommonResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if (result != null && "YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Toast.showToastTest(context, "任务受理成功!");
					ContentValues values = new ContentValues();
					values.put("isaccept", "1");
					DBUtils.update("checktask", values, "registno=?", new String[] { registNo });
					DataConfig.tblTaskQuery.setAcceptTaskDate(result.getHandletime());
					DataConfig.tblTaskQuery.setTaskreceiptTime(result.getHandletime());// add
					TblTaskQuery.insertOrUpdate(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
					drawerUtils.setAcceptBtn(false);// 接受按钮不可用
					SystemConfig.isOperate = true;
					TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
					
					// 刷新
					UiManager.getInstance().changeView(SBasicActivity.class, null, false);
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					if (result != null) {
						message.obj = result.getResponseMessage();
					} else {
						message.obj = "任务受理失败!";
					}
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			};

			@Override
			protected CommonResponse doInBackground(String... params) {
				ArrivedRequest requestArrived = new ArrivedRequest();
				requestArrived.setRegisTno(params[0]);
				requestArrived.setNodeType("check");
				requestArrived.setLossNo("-2");
				requestArrived.setUserCode(SystemConfig.USERLOGINNAME);
				return ArrivedHttpService.arrivedService(requestArrived, context.getString(R.string.http_url));

			}
		}.execute(registNo);
	}

	/***
	 * 改派申请服务器交互 haoyun add for update jingtuo
	 */
	private void sendForService(final String isCertainralAtion, String reassignPendingReason) {
		new AsyncTask<String, Void, CommonResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if (result != null && "YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Toast.showToastTest(context, "改派申请成功!");
					checkTask.setSurveytaskstatus("1");
					checkTask.setApplycannelstatus("apply");
					List<CheckTask> cts = new ArrayList<CheckTask>();
					cts.add(checkTask);
					CheckTaskAccess.insertOrUpdate(cts, false);
					drawerUtils.setGaiPaiBtn(false);

					if (isCertainralAtion.equals("1")) {
						List<CertainLossTask> clts = CertainLossTaskAccess.getByRegistNo(registNo);
						for (int i = 0; i < clts.size(); i++) {
							clts.get(i).setSurveytaskstatus("1");
							clts.get(i).setApplycannelstatus("apply");
						}
						CertainLossTaskAccess.insertOrUpdate(clts, true);
					}
					UiManager.getInstance().changeView(SurveyTaskActivity.class, null, false);
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					if (result != null) {
						message.obj = result.getResponseMessage();
					} else {
						message.obj = "改派申请失败!";
					}
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				SendForRequest sendForRequest = new SendForRequest();
				sendForRequest.setRegistNo(registNo);
				sendForRequest.setApplyType("apply");
				sendForRequest.setReassignPendingReason(params[1]);
				sendForRequest.setUserCode(SystemConfig.USERLOGINNAME);
				sendForRequest.setIsRelated(params[0]);
				List<ScheDuleItem> list = new ArrayList<ScheDuleItem>();
				ScheDuleItem sdi = new ScheDuleItem();
				sdi.setLossNo("-2");
				sdi.setNodeType("check");
				list.add(sdi);
				if ("1".equals(params[0])) {
					List<CertainLossTask> ctls = CertainLossTaskAccess.getByRegistNo(registNo);
					if (ctls != null && ctls.size() >= 1) {
						for (int i = 0; i < ctls.size(); i++) {
							sdi = new ScheDuleItem();
							sdi.setLossNo(ctls.get(i).getItemno() + "");
							sdi.setNodeType("certainLoss");
							list.add(sdi);
						}
					}
				}
				sendForRequest.setScheDuleItems(list);
				return SendForHttpService.sendForService(sendForRequest, context.getString(R.string.http_url));

			}
		}.execute(isCertainralAtion, reassignPendingReason);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.survey_basic_btn_contactinfo:
			commonUtils.showView(context, contactRL, rocketAnimation, contactListView, contactImv, new Ifunction() {
				@Override
				public void setFunction() {
					if (contactAdapter == null) {
						contactAdapter = new SurveyContactAdapter(sContactView);
						contactListView.setAdapter(contactAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(contactListView);
					} else {
						contactListView.setAdapter(contactAdapter);
						new ScrollListViewUtils().setListViewHeightBasedOnChildren(contactListView);
					}

				}
			});
			break;

		case R.id.survey_basic_btn_caseinfo:
			commonUtils.showView(context, caseinfoRl, rocketAnimation, caseListView, ACaseImv, new Ifunction() {
				@Override
				public void setFunction() {
					setCaseListviewData();
				}
			});
			break;
		case R.id.survey_basic_btn_taskinfo:
			commonUtils.showView(context, taskinfoRl, rocketAnimation, taskListView, ATaskImg, new Ifunction() {

				@Override
				public void setFunction() {
					setTaskListviewData();
				}
			});

			break;
		case R.id.survey_basic_btn_baodaninfo:
			// 保单信息
			UiManager.getInstance().changeView(SGuaranteeslipActivity.class, bundle, true);
			break;
		case R.id.survey_basic_btn_history:
			// 历史赔案
			UiManager.getInstance().changeView(SHistoryActivity.class, bundle, false);
			break;
		case R.id.survey_basic_btn_predicttime:
			// 预约时间
			commonUtils.showView(context, rlOrderTime, rocketAnimation, llOrderTime, imvOrderTime, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;
		case R.id.survey_basic_btn_messagesEditxiepei:
			// 撰写留言
			bundle.putString("time", SystemConfig.serverTime);
			bundle.putString("nodetype", "查勘");
			bundle.putString("LeavePerson", SystemConfig.loginResponse.getUserName());
			UiManager.getInstance().changeView(LeaveMessageEditActivity.class, bundle, false);
			break;
		case R.id.survey_basic_btn_messagesQuery:
			// 留言查询
			UiManager.getInstance().changeView(LeaveMessageQueryActivity.class, bundle, false);
			break;
		}

	}

	/*****
	 * 案件信息录入
	 */
	private void setCaseListviewData() {
		if (caseInfoAdapter == null) {
			caseInfoAdapter = new SurveyCaseInfoAdapter(DataConfig.tblTaskQuery, context);
			caseListView.setAdapter(caseInfoAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(caseListView);
		} else {
			caseListView.setAdapter(caseInfoAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(caseListView);
		}
	}

	/*****
	 * 任务信息录入
	 */
	private void setTaskListviewData() {
		if (taskInfoAdapter == null) {
			taskInfoAdapter = new SurveyTaskInfoAdapter(DataConfig.tblTaskQuery, context);
			taskListView.setAdapter(taskInfoAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(taskListView);
		} else {
			taskInfoAdapter = new SurveyTaskInfoAdapter(DataConfig.tblTaskQuery, context);
			taskListView.setAdapter(taskInfoAdapter);
		}
	}

	@Override
	public Integer getExit() {

		return ConstantValue.DAOHANG_SurveyBasic;
	}

	@Override
	public Integer getBackMain() {
		return ConstantValue.Back_Survey;

	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (!SystemConfig.isOperate){
			edOrderTime.setEnabled(false);
		}else{
			edOrderTime.setEnabled(true);
		}
		// 已经是完成的任务
		if ("4".equals(checkStatue)) {
			SystemConfig.isOperate = false;
			TopManager.getInstance().getImbtnTongBu().setEnabled(false);
		}
		// 控制按钮操作性
		if (sContactView != null){
			sContactView.controlEd();
		}
		setFourDopBtn();
		/**
		 *                              #######################
		 *  ============================>>>>>> 【视频协助】  <<<<<<==============
		 *                              #######################
		 */
		TopManager.getInstance().xiePeiYuanBtnViedo(new xiePeiBtnVideo() {
			@Override
			public void onClick() {
				if (SystemConfig.isOperate) {
					// 可操作
					if (registNo != null) {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected CommonResponse doInBackground(Void... arg0) {
								RedioRequest redioRequest = new RedioRequest();
								redioRequest.setIsXeipei("1");
								redioRequest.setLossNo("");
								redioRequest.setNodeType("check");
								redioRequest.setRegistNo(registNo);
								redioRequest.setUserCode(SystemConfig.USERLOGINNAME);
								redioRequest.setIMEI(DeviceUtils.getDeviceId(context));
								redioRequest.setCerTainCallTime(checkTask.getContacttime());

								redioRequest.setInsuredPhone(sContactView.getXiePeiViewValue().get(0));
								redioRequest.setLinkName(sContactView.getXiePeiViewValue().get(1));
								redioRequest.setLinkPhone(sContactView.getXiePeiViewValue().get(2));
								redioRequest.setCerTainCallTime(checkTask.getContacttime());
								if (checkTask.getContacttime() == null || checkTask.getContacttime().equals("")) {
									CommonResponse result = new CommonResponse();
									result.setResponseCode("NO");
									result.setResponseMessage("请联系完客户后在进行视频协助操作！");
									return result;
								} else if ("".equals(redioRequest.getInsuredPhone())) {
									CommonResponse result = new CommonResponse();
									result.setResponseCode("NO");
									result.setResponseMessage("请填写被保险人电话,再在进行视频协助操作！");
									return result;
								}
								else {
									saveData();
									return RedioHttpService.redioService(redioRequest, context.getString(R.string.http_url));
								}
							}

							@Override
							protected void onPreExecute() {
								super.onPreExecute();
								handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
							}

							@Override
							protected void onPostExecute(CommonResponse result) {
								if (result != null && "YES".equals(result.getResponseCode())) {
									handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
									Intent hkVideoIntent = new Intent();
									hkVideoIntent.setClass(context, MPUSDKActivity.class);
									context.startActivity(hkVideoIntent);
								} else {
									handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
									// 提示错误信息
									Message message = Message.obtain();
									if (result != null) {
										message.obj = result.getResponseMessage();
									} else {
										message.obj = "请求协助失败";
									}
									message.what = ConstantValue.ERROE;
									handler.sendMessage(message);
								}
							};
						}.execute();

					} else {
						Toast.showToast(context, "获取报案号失败");
					}
				}
			}
		});
		

		// 理赔交互和提交
		TopManager.getInstance().setTopBtnSumbitSurvery(new TopBtnSumbitSurvery() {
			@Override
			public void OnclickSumbit() {
				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【查勘提交】  <<<<<<==============
					 *                              ***********************
					 */ 
					// 校验身份证号码是否为空
					String idCard = DataConfig.tblTaskQuery.getCheckDriver().get(0).getIdentifyNumber();
					// 判断是否有联系过客户
					boolean isTakephotos = TblTaskinfo.checkIsTakephotos(registNo);
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else if (!CheckSurveyValue.idCardIsNull || !CheckSurveyValue.idCard(idCard)) {					
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
								TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
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
				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【查勘同步】  <<<<<<==============
					 *                              ***********************
					 */ 
					// 判断是否有联系过客户
					boolean isTakephotos = TblTaskinfo.checkIsTakephotos(registNo);
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else{
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_OPEN);
								TblTaskQuery.insertOrUpdate(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
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
										
										// Toast.showToast(context, "开始上传，请稍等...");
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
		});

		// 理赔同步撤销
		TopManager.getInstance().btnTongBuSurveryContinue(new TopBtnTongBuSurveryContinue() {
			@Override
			public void onClick() {
				synchCancel();
			}

		});

		taskInfoAdapter = new SurveyTaskInfoAdapter(DataConfig.tblTaskQuery, context);
		taskListView.setAdapter(taskInfoAdapter);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.survrybasic_taskinfo_listview_item_ed_ordertime:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setDateTimeUsils.showDateTimePicker(edOrderTime);
			}
			break;
		}
		return false;
	}
	

	@Override
	public void afterTextChanged(Editable arg0) {
		// 保存预约时间
		String ordertime = edOrderTime.getText().toString().trim();
		CheckTaskAccess.update(registNo, null, null, ordertime, null, null, null, null);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}


	@Override
	public void onPause() {
		saveData();
		super.onPause();
	}

	/****
	 * 同步撤销交互方法
	 */
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
					UiManager.getInstance().changeView(SBasicActivity.class, null, false);

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

	public void saveData() {
		if (!SystemConfig.UserRightIsAdvanced) {
			sContactView.getPageValue();
			TblTaskQuery.UpdatePhoto(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery);
			DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(dbHelper.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
		}

	}
}
