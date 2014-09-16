package com.sinosoftyingda.fastclaim.defloss.page;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.ArrivedRequest;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.RedioRequest;
import com.sinosoftyingda.fastclaim.common.model.ScheDuleItem;
import com.sinosoftyingda.fastclaim.common.model.SendForRequest;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.ArrivedHttpService;
import com.sinosoftyingda.fastclaim.common.service.DefLossInfoQueryHttpService;
import com.sinosoftyingda.fastclaim.common.service.RedioHttpService;
import com.sinosoftyingda.fastclaim.common.service.SendForHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.VerifyLossSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogGaiPai;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.DeflossDrawerUtils;
import com.sinosoftyingda.fastclaim.common.views.DeflossDrawerUtils.IBtnAccept;
import com.sinosoftyingda.fastclaim.common.views.DeflossDrawerUtils.IBtnGaiPai;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordDeflossTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuDeflossContinue;
import com.sinosoftyingda.fastclaim.common.views.TopManager.xiePeiBtnVideo;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.adapter.DBCaseInfoAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.DBLossAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.DBRecordAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.DBTaskInfoAdapter;
import com.sinosoftyingda.fastclaim.defloss.service.DeflossinfoUploadToClaimSystem;
import com.sinosoftyingda.fastclaim.defloss.service.JYLioneyeToolsActivity;
import com.sinosoftyingda.fastclaim.defloss.util.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.hkvideo.MPUSDKActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.page.DeflossPhotosView;
import com.sinosoftyingda.fastclaim.survey.page.SContactView;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageEditActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.LeaveMessageQueryActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SGuaranteeslipActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SHistoryActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.DeflossWorkflowlogView;

/***
 * 定损基本页面
 * 
 * @author chenjianfan
 * 
 */
public class DeflossBasicActivity extends BaseView implements OnClickListener {
	private View layout;
	private PromptManager promptManager;
	// 报案信息
	private RelativeLayout rlBtnCaseInfo;
	private ImageView imvCaseinfo;
	private ListView lvCaseInfo;
	private DBCaseInfoAdapter caseInfoAdapter;
	// 查勘记录
	private RelativeLayout rlBtnSurveyInfo;
	private ImageView imvSurveyInfo;
	private ListView lvSurveyInfo;
	private DBRecordAdapter recordAdapter;
	// 查勘要点
	private RelativeLayout rlBtnMainPoint;
	// 案件涉损
	private RelativeLayout rlBtnLost;
	private ImageView imvLost;
	private ListView lvLost;
	private DBLossAdapter lossAdapter;
	// 联系信息
	private RelativeLayout rlContact;
	private ImageView imvContact;
	private LinearLayout llContact;
	private TextView tvAssuredName;// 被保险人
	private TextView tvAssurePhone;// 被保险人电话
	private TextView tvBaileName;// 受托人
	private TextView tvBailePhone;// 受托人电话
	// 保单信息
	private RelativeLayout rlBaoDanInfo;
	// 历史赔案
	private RelativeLayout rlHistory;
	// 任务信息
	private RelativeLayout rlTaskInfo;
	// 留言查询
	private RelativeLayout messagesQuery;
	//撰写留言
	private RelativeLayout messagesEditxiepei;
	
	private ImageView imvTaskInfo;
	private ListView lvTaskInfo;
	private DBTaskInfoAdapter taskInfoAdapter;
	private CommonUtils commonUtils;
	private AnimationDrawable rocketAnimation;
	// 定损的抽屉效果
	private View vDrawer;
	private DeflossDrawerUtils drawerUtils;
	// 查勘报告
	private EditText edReport;
	private RelativeLayout rlReport;
	private ImageView imvReport;
	// 报案号和任务类型
	private String registNo;
	private String lossNo;

	private CertainLossTask certainLossTask;

	public DeflossBasicActivity(Context context, Bundle bundle) {
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
		// 报案号和任务类型
		if (bundle != null) {
			registNo = bundle.getString("registNo");
			lossNo = bundle.getString("itemNo");
			// 地区
			String arean = registNo.substring(1, 3);
			if ("01".equals(arean)) {
				SystemConfig.AREAN = "北京";
			} else if ("11".equals(arean)) {
				SystemConfig.AREAN = "北京";
			} else if ("02".equals(arean)) {
				SystemConfig.AREAN = "上海";
			}
		} else {
			registNo = "报案号出错";
			lossNo = "-3";
		}
		DataConfig.defLossInfoQueryData = null;
		SystemConfig.PHOTO_CLAIMNO = registNo;
		SystemConfig.LOSSNO = lossNo;
		layout = inflater.inflate(R.layout.defloss_basic, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		promptManager = new PromptManager();
		// 标识位不要动
		SystemConfig.isDeflossPage = true;
		initView();
	}

	/**
	 * 设置组件是否可用
	 */
	private void setUsable() {
		if (!TextUtils.isEmpty(certainLossTask.getSurveytaskstatus())) {
			SystemConfig.surveytaskstatus=certainLossTask.getSurveytaskstatus();
		}
		if ("1".equals(certainLossTask.getSurveytaskstatus()) && ("".equals(certainLossTask.getApplycannelstatus()) || "cancel".equals(certainLossTask.getApplycannelstatus()))) {
			vDrawer.setVisibility(View.VISIBLE);
			if(certainLossTask != null && 1 == certainLossTask.getIsaccept()){
				TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
			}else{
				TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
			}
			
			// 标志该任务是否受理
			if (1 == certainLossTask.getIsaccept()) {	// 任务受理
				drawerUtils.setAcceptBtn(false);
				SystemConfig.isOperate = true;
				TopManager.getInstance().getImbtnTongBu().setEnabled(false);
			} else {									// 未受理
				drawerUtils.setAcceptBtn(true);
				SystemConfig.isOperate = false;
				TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			}
		} else if ("2".equals(certainLossTask.getSurveytaskstatus())) {
			vDrawer.setVisibility(View.GONE);
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			SystemConfig.isOperate = true;
		} else if ("4".equals(certainLossTask.getSurveytaskstatus())) {
			vDrawer.setVisibility(View.GONE);
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
			SystemConfig.isOperate = false;
		} else if ("5".equals(certainLossTask.getSurveytaskstatus())) {
			vDrawer.setVisibility(View.GONE);
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.VISIBLE);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			SystemConfig.isOperate = true;
		} else if ("1".equals(certainLossTask.getSurveytaskstatus()) && "apply".equals(certainLossTask.getApplycannelstatus())) {
			vDrawer.setVisibility(View.GONE);
			TopManager.getInstance().getImbtnTongBu().setVisibility(View.GONE);
			SystemConfig.isOperate = false;
		}

		// 同步和撤销按钮控制
		if ("synchroApply".equals(certainLossTask.getSynchrostatus())) {
			// 同步状态不可操作 显示撤回按钮
			TopManager.getInstance().TongBuDeflossContinue(false);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
			SystemConfig.isOperate = false;
		} else {// 可以操作 显示同步按钮
			TopManager.getInstance().TongBuDeflossContinue(true);
			TopManager.getInstance().getImbtnTongBu().setEnabled(true);
		}

		// 协陪员不显示右边顶部按钮
		if (SystemConfig.UserRightIsAdvanced) {
			TopManager.getInstance().getFrameLayoutRight().setVisibility(View.VISIBLE);
			messagesEditxiepei.setVisibility(View.GONE);
		} else {
			TopManager.getInstance().getFrameLayoutRight().setVisibility(View.GONE);
			messagesEditxiepei.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {
		commonUtils = new CommonUtils();
		// 联系客户时间和是否任务的标识
		tvAssuredName = (TextView) layout.findViewById(R.id.defloss_basic_assuredname);
		tvAssurePhone = (TextView) layout.findViewById(R.id.defloss_basic_acceptedphone);
		tvBaileName = (TextView) layout.findViewById(R.id.defloss_basic_bailename);
		tvBailePhone = (TextView) layout.findViewById(R.id.defloss_basic_baileephone);
		rlBtnCaseInfo = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_caseinfo);
		imvCaseinfo = (ImageView) layout.findViewById(R.id.defloss_basic_caseinfo_ll);
		lvCaseInfo = (ListView) layout.findViewById(R.id.defloss_basic_caseinfo_listview);
		rlBtnSurveyInfo = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_surveyinfo);
		imvSurveyInfo = (ImageView) layout.findViewById(R.id.defloss_basic_imv_surveyinfo);
		lvSurveyInfo = (ListView) layout.findViewById(R.id.defloss_basic_listview_surveyinfo);
		rlBtnMainPoint = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_mainpoint);
		rlBtnLost = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_loss);
		imvLost = (ImageView) layout.findViewById(R.id.defloss_basic_loss_ll);
		lvLost = (ListView) layout.findViewById(R.id.defloss_basic_loss_listview);
		rlContact = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_contact);
		imvContact = (ImageView) layout.findViewById(R.id.defloss_basic_contact_ll);
		llContact = (LinearLayout) layout.findViewById(R.id.defloss_basic_ll_btn_contact);
		rlBaoDanInfo = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_baodaninfo);
		rlHistory = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_history);
		rlTaskInfo = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_taskinfo);
		messagesQuery = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_messagesQuery);
		messagesEditxiepei =(RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_messagesEditxiepei);
		imvTaskInfo = (ImageView) layout.findViewById(R.id.defloss_basic_taskinfo_ll);
		lvTaskInfo = (ListView) layout.findViewById(R.id.defloss_basic_listview_taskinfo);
		imvReport = (ImageView) layout.findViewById(R.id.defloss_basic_loss_ll_repotr);
		rlReport = (RelativeLayout) layout.findViewById(R.id.defloss_basic_btn_loss_repotr);
		edReport = (EditText) layout.findViewById(R.id.defloss_survey_btn_report_et);
		// 抽屉按钮
		vDrawer = layout.findViewById(R.id.defloss_basic_drawer);
		drawerUtils = new DeflossDrawerUtils(vDrawer);
	}

	// 初始化界面数据
	private void setData() {
		certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
		DataConfig.defLossInfoQueryData.getTaskInfo().setLinkCustomTime(certainLossTask.getContacttime());
		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.BOLD));
		TopManager.getInstance().getBtnDeflossBasicMsg().setBackgroundResource(R.drawable.tasks_infortab_click);
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getRegist() != null) {
			// 联系信息
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getRegist().getInsuredName()))
				tvAssuredName.setText(DataConfig.defLossInfoQueryData.getRegist().getInsuredName());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getRegist().getInsuredMobile()))
				tvAssurePhone.setText(DataConfig.defLossInfoQueryData.getRegist().getInsuredMobile());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getRegist().getEntrustName()))
				tvBaileName.setText(DataConfig.defLossInfoQueryData.getRegist().getEntrustName());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getRegist().getEntrustMobile()))
				tvBailePhone.setText(DataConfig.defLossInfoQueryData.getRegist().getEntrustMobile());

			// 查勘报告信息
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getSurveyKeyPoint().getCheckReport())) {
				edReport.setText(DataConfig.defLossInfoQueryData.getSurveyKeyPoint().getCheckReport());
			} else {
				edReport.setText("");
			}

			// 报案信息
			if (caseInfoAdapter == null) {
				caseInfoAdapter = new DBCaseInfoAdapter(context);
				lvCaseInfo.setAdapter(caseInfoAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvCaseInfo);
			} else {
				caseInfoAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvCaseInfo);
			}

			// 查勘记录
			if (recordAdapter == null) {
				recordAdapter = new DBRecordAdapter(context);
				lvSurveyInfo.setAdapter(recordAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvSurveyInfo);
			} else {
				recordAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvSurveyInfo);
			}

			// 案件涉损
			if (lossAdapter == null) {
				lossAdapter = new DBLossAdapter(context);
				lvLost.setAdapter(lossAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvLost);
			} else {
				lossAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvLost);
			}
			// 任务信息
			taskInfoAdapter = new DBTaskInfoAdapter(context);
			lvTaskInfo.setAdapter(taskInfoAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvTaskInfo);
		}
		setUsable();
	}

	/***
	 * 接受任務服务器交互 haoyun add for update by jingtuo
	 */
	private void arrivedService() {
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
					try {
						ContentValues values = new ContentValues();
						values.put("isaccept", "1");
						DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[] { registNo, lossNo });
						DataConfig.defLossInfoQueryData.getTaskInfo().setTaskReceiptTime(result.getHandletime());// add
						CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.defLossInfoQueryData, false);

						drawerUtils.setAcceptBtn(false);// 接受按钮不可用
						SystemConfig.isOperate = true;
						// 刷新
						certainLossTask.setIsaccept(1);// add by // jingtuo如果删除，会导致二次受理
						UiManager.getInstance().changeView(DeflossBasicActivity.class, null, false);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
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
			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				ArrivedRequest arrivedRequest = new ArrivedRequest();
				arrivedRequest.setUserCode(SystemConfig.USERLOGINNAME);
				arrivedRequest.setRegisTno(registNo);// 报案号
				arrivedRequest.setNodeType("certainLoss");
				arrivedRequest.setLossNo(lossNo);// 定损编号
				return ArrivedHttpService.arrivedService(arrivedRequest, context.getString(R.string.http_url));
			}
		}.execute();
	}

	/***
	 * 改派申请服务器交互 haoyun add for update by jingtuo
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
					CertainLossTask clt = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
					clt.setSurveytaskstatus("1");
					clt.setApplycannelstatus("apply");
					List<CertainLossTask> list = new ArrayList<CertainLossTask>();
					list.add(clt);
					CertainLossTaskAccess.insertOrUpdate(list, false);
					drawerUtils.setGaiPaiBtn(false);
					if (isCertainralAtion.equals("1")) {
						List<CertainLossTask> clts = CertainLossTaskAccess.getNoHandleByRegistNo(registNo, lossNo);
						for (int i = 0; i < clts.size(); i++) {
							clts.get(i).setSurveytaskstatus("1");
							clts.get(i).setApplycannelstatus("apply");
						}
						CertainLossTaskAccess.insertOrUpdate(clts, true);
					}
					UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
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
				CommonResponse response = null;
				SendForRequest sendForRequest = new SendForRequest();
				sendForRequest.setRegistNo(registNo);
				sendForRequest.setApplyType("apply");
				sendForRequest.setReassignPendingReason(params[1]);
				sendForRequest.setUserCode(SystemConfig.USERLOGINNAME);
				sendForRequest.setIsRelated(params[0]);
				List<ScheDuleItem> list = new ArrayList<ScheDuleItem>();
				ScheDuleItem sdi = new ScheDuleItem();
				sdi.setNodeType("certainLoss");
				sdi.setLossNo(lossNo);
				list.add(sdi);
				if ("1".equals(params[0])) {
					List<CertainLossTask> clts = CertainLossTaskAccess.getNoHandleByRegistNo(registNo, lossNo);
					if (clts != null && clts.size() >= 1) {
						for (int i = 0; i < clts.size(); i++) {
							sdi = new ScheDuleItem();
							sdi.setLossNo(clts.get(i).getItemno() + "");
							sdi.setNodeType("certainLoss");
							list.add(sdi);
						}
					}
				}
				sendForRequest.setScheDuleItems(list);
				response = SendForHttpService.sendForService(sendForRequest, context.getString(R.string.http_url));
				return response;
			}
		}.execute(isCertainralAtion, reassignPendingReason);
	}

	@Override
	protected void setListener() {
		rlBtnCaseInfo.setOnClickListener(this);
		rlBtnSurveyInfo.setOnClickListener(this);
		rlBtnMainPoint.setOnClickListener(this);
		rlBtnLost.setOnClickListener(this);
		rlContact.setOnClickListener(this);
		rlBaoDanInfo.setOnClickListener(this);
		rlHistory.setOnClickListener(this);
		rlTaskInfo.setOnClickListener(this);
		rlReport.setOnClickListener(this);
		messagesQuery.setOnClickListener(this);
		messagesEditxiepei.setOnClickListener(this);
		drawerUtils.setAcceptOnClick(new IBtnAccept() {
			@Override
			public void onClick() {
				// 如果没和客户联系成功，则不允许受理任务
				boolean isCallPhone = UtilIsNotNull.isCallPhone(registNo);
				if(isCallPhone){
					// 接受任务服务器交互方法 
					arrivedService();
				}else{
					Toast.showToast(context, "请先与客户取得联系！");
				}
			}
		});
		drawerUtils.setGaiPaiOnClick(new IBtnGaiPai() {
			@Override
			public void onClick() {
				promptManager.showSingnRadio(context, new ShowDialogGaiPai() {
					@Override
					public void setPositiveButton(String reason) {
						// 确定，注意reason的返回值可能会有空，要做判断
						final String str = reason;
						List<CertainLossTask> clts = CertainLossTaskAccess.getNoHandleByRegistNo(registNo, lossNo);
						if (clts != null && clts.size() >= 1) {
							promptManager.showDialog(context, "是否关联其他定损", R.string.yes, R.string.no, new ShowDialogPositiveButton() {
								@Override
								public void setPositiveButton() {
									// for update by haoyun
									// 20130401 begin
									if (SystemConfig.loginResponse.getDutyFlag().equals("on")) {
										sendForService("1", str);
									} else {
										handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
										Message message = Message.obtain();
										message.obj = "下班状态不可以改派任务!";
										message.what = ConstantValue.ERROE;
										handler.sendMessage(message);

									}
									// for update by haoyun
									// 20130401 end
								}

								@Override
								public void setNegativeButton() {
									sendForService("0", str);
								}
							});
						} else {
							sendForService("0", reason);
						}
					}

					@Override
					public void setNegativeButton() {

					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.defloss_basic_btn_loss_repotr:
			commonUtils.showView(context, rlReport, rocketAnimation, edReport, imvReport, new Ifunction() {
				@Override
				public void setFunction() {
					edReport.setEnabled(false);
				}
			});
			break;

		case R.id.defloss_basic_btn_caseinfo:
			// 报案信息
			commonUtils.showView(context, rlBtnCaseInfo, rocketAnimation, lvCaseInfo, imvCaseinfo, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作
				}
			});
			break;
		case R.id.defloss_basic_btn_surveyinfo:
			// 查勘记录
			commonUtils.showView(context, rlBtnSurveyInfo, rocketAnimation, lvSurveyInfo, imvSurveyInfo, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;
		case R.id.defloss_basic_btn_mainpoint:
			// 查勘要点
			UiManager.getInstance().changeView(DeflossMainPointActivity.class, false, null, false);
			break;
		case R.id.defloss_basic_btn_loss:
			// 案件涉损
			commonUtils.showView(context, rlBtnLost, rocketAnimation, lvLost, imvLost, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;
		case R.id.defloss_basic_btn_contact:
			// 联系信息
			commonUtils.showView(context, rlContact, rocketAnimation, llContact, imvContact, new Ifunction() {
				@Override
				public void setFunction() {
					// 操作

				}
			});
			break;
		case R.id.defloss_basic_btn_baodaninfo:
			// 保单信息
			UiManager.getInstance().changeView(SGuaranteeslipActivity.class, bundle, true);
			break;
		case R.id.defloss_basic_btn_history:
			// 历史赔案
			UiManager.getInstance().changeView(SHistoryActivity.class, bundle, false);
			break;
		case R.id.defloss_basic_btn_taskinfo:
			// 任务信息
			commonUtils.showView(context, rlTaskInfo, rocketAnimation, lvTaskInfo, imvTaskInfo, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;
		case R.id.defloss_basic_btn_messagesQuery:
			// 留言查询
			UiManager.getInstance().changeView(LeaveMessageQueryActivity.class, bundle, false);
			break;
		case R.id.defloss_basic_btn_messagesEditxiepei:
			// 撰写留言
			bundle.putString("time", SystemConfig.serverTime);
			bundle.putString("nodetype", "定损");
			bundle.putString("LeavePerson", SystemConfig.loginResponse.getUserName());
			UiManager.getInstance().changeView(LeaveMessageEditActivity.class, bundle, false);
			break;
		}
	}

	@Override
	public Integer getExit() {
		return ConstantValue.DAOHANG_DeflossBasic;
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
		super.onResume();
		if (DataConfig.defLossInfoQueryData != null) {
			setData();
		} else {
			getData();
		}

		// 顶部四个定损页面跳转按钮
		setFourTop();
		
		// 与理赔同步和提交
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
							handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
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
						//handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
					};

					@Override
					protected void onPostExecute(CommonResponse result) {
						Toast.showToast(context, "同步理赔");
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
								values.put("synchrostatus", "");
								values.put("completetime", result.getHandletime());
								DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[] { registNo, lossNo });
							}
							CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.defLossInfoQueryData, false);

							// 判断是否有上传队列
							if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
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

		// 理赔同步撤销
		TopManager.getInstance().btnTongBuDeflossContinue(new TopBtnTongBuDeflossContinue() {
			@Override
			public void onClick() {
				synchCancel();
			}
		});
		

		// 视频协助按钮
		TopManager.getInstance().xiePeiYuanBtnViedo(new xiePeiBtnVideo() {
			@Override
			public void onClick() {
				if (SystemConfig.isOperate) {
					if (registNo != null && lossNo != null) {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected CommonResponse doInBackground(Void... arg0) {
								RedioRequest redioRequest = new RedioRequest();
								redioRequest.setIsXeipei("1");
								redioRequest.setLossNo(lossNo);
								redioRequest.setNodeType("certainLoss");
								redioRequest.setRegistNo(registNo);
								redioRequest.setUserCode(SystemConfig.USERLOGINNAME);
								redioRequest.setIMEI(DeviceUtils.getDeviceId(context));
								redioRequest.setCerTainCallTime(certainLossTask.getContacttime());
								if (certainLossTask.getContacttime() == null || certainLossTask.getContacttime().equals("")) {
									CommonResponse result = new CommonResponse();
									result.setResponseCode("NO");
									result.setResponseMessage("请联系完客户后在进行视频协助操作！");
									return result;
								} else {
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
						Toast.showToast(context, "获取查看任务信息失败");
					}
				}
			}
		});
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

			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					// 控件可以操作
					SystemConfig.isOperate = true;
					Toast.showToast(context, "任务理赔同步撤销成功");
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
					UiManager.getInstance().changeView(DeflossBasicActivity.class, null, false);

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

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 获取定损数据
	 */
	private void getData() {
		certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
		if (certainLossTask.getSurveytaskstatus().equals("1") && (certainLossTask.getApplycannelstatus().equals("") || certainLossTask.getApplycannelstatus().equals("cancel"))
				&& certainLossTask.getIsaccept() == 1 
				|| certainLossTask.getSurveytaskstatus().equals("2") && !certainLossTask.getSynchrostatus().equals("synchroApply")// 非同步
				|| certainLossTask.getSurveytaskstatus().equals("1") && certainLossTask.getApplycannelstatus().equals("apply")) {// 改派
			// 只拉一次
			DataConfig.defLossInfoQueryData = CertainLossInfoAccess.getLossTaskQuery(SystemConfig.dbhelp.getReadableDatabase(), registNo, lossNo);
			if (DataConfig.defLossInfoQueryData == null) {
				new AsyncTask<Void, Void, DefLossInfoQueryResponse>() {
					@Override
					protected void onPreExecute() {
						handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
					};

					@Override
					protected void onPostExecute(DefLossInfoQueryResponse result) {
						if (result != null && "YES".equals(result.getResponseCode())) {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
							DataConfig.defLossInfoQueryData = CertainLossInfoAccess.getLossTaskQuery(SystemConfig.dbhelp.getReadableDatabase(), registNo, lossNo);
							setData();
						} else {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
							String msg = null;
							if (result == null) {
								msg = "交互失败";
							} else {
								msg = result.getResponseMessage();
							}
							Message message = Message.obtain();
							message.obj = msg;
							message.what = ConstantValue.ERROE;
							handler.sendMessage(message);
							UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);
						}
					};

					@Override
					protected DefLossInfoQueryResponse doInBackground(Void... arg0) {
						DefLossInfoQueryRequest defLossInfoQueryRequest = new DefLossInfoQueryRequest();
						defLossInfoQueryRequest.setLossNo(lossNo);
						defLossInfoQueryRequest.setRegistNo(registNo);
						defLossInfoQueryRequest.setDeflossTaskStatus(certainLossTask.getSurveytaskstatus());
						return DefLossInfoQueryHttpService.defLossInfoQuerySercive(defLossInfoQueryRequest, context.getString(R.string.http_url), false);
					}
				}.execute();
			} else {
				setData();
			}
		} else if (certainLossTask.getSurveytaskstatus().equals("2")
				&& certainLossTask.getSynchrostatus().equals("synchroApply")// 同步理赔
				|| certainLossTask.getSurveytaskstatus().equals("4")// 已完成任务
				|| certainLossTask.getSurveytaskstatus().equals("5")// 核损打回
				|| certainLossTask.getSurveytaskstatus().equals("1")
				&& (certainLossTask.getApplycannelstatus().equals("") 
				|| certainLossTask.getApplycannelstatus().equals("cancel")) && certainLossTask.getIsaccept() == 0) {// 未接受任务
			// 只从服务器拉取
			new AsyncTask<Void, Void, DefLossInfoQueryResponse>() {
				@Override
				protected void onPreExecute() {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
				};

				@Override
				protected void onPostExecute(DefLossInfoQueryResponse result) {
					if (result != null && "YES".equals(result.getResponseCode())) {
						handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
						DataConfig.defLossInfoQueryData = CertainLossInfoAccess.getLossTaskQuery(SystemConfig.dbhelp.getReadableDatabase(), registNo, lossNo);
						setData();
					} else {
						handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
						String msg = null;
						if (result == null) {
							msg = "交互失败";
						} else {
							msg = result.getResponseMessage();
						}
						Message message = Message.obtain();
						message.obj = msg;
						message.what = ConstantValue.ERROE;
						handler.sendMessage(message);
						UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
					}
				};

				@Override
				protected DefLossInfoQueryResponse doInBackground(Void... arg0) {
					DefLossInfoQueryRequest defLossInfoQueryRequest = new DefLossInfoQueryRequest();
					defLossInfoQueryRequest.setLossNo(lossNo);
					defLossInfoQueryRequest.setRegistNo(registNo);
					defLossInfoQueryRequest.setDeflossTaskStatus(certainLossTask.getSurveytaskstatus());
					return DefLossInfoQueryHttpService.defLossInfoQuerySercive(defLossInfoQueryRequest, context.getString(R.string.http_url), false);
				}
			}.execute();
		} else {
			Message message = Message.obtain();
			message.obj = "任务状态异常";
			message.what = ConstantValue.ERROE;
			handler.sendMessage(message);
			UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
		}
	}

}
