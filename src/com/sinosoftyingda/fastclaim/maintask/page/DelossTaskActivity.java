package com.sinosoftyingda.fastclaim.maintask.page;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.drivingline.AutoDriving;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.mina.NoticeManager;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.service.TaskQueryHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.maintask.deAdapter.GaiPaiAdapter;
import com.sinosoftyingda.fastclaim.maintask.deAdapter.HandledAdapter;
import com.sinosoftyingda.fastclaim.maintask.deAdapter.HandleingAdapter;
import com.sinosoftyingda.fastclaim.maintask.deAdapter.HeSunBackAdapter;
import com.sinosoftyingda.fastclaim.maintask.deAdapter.NoHandleAdapter;
import com.sinosoftyingda.fastclaim.maintask.utils.CallPhoneUtil;
import com.sinosoftyingda.fastclaim.maintask.utils.MainTaskUtils;
import com.sinosoftyingda.fastclaim.notice.page.NoticeActivity;
import com.sinosoftyingda.fastclaim.query.page.QueryHistoryActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;

/**
 * 定损页面
 * @author chenjianfan
 */
public class DelossTaskActivity extends BaseView implements OnClickListener {
	private CommonUtils commonUtils;
	private LinearLayout layout;
	private List<View> listView = new ArrayList<View>();
	// group
	private RelativeLayout rBtnNoHandle;
	private RelativeLayout rBtnHandleing;
	private RelativeLayout rBtnHanded;
	private RelativeLayout rBtnGaiPai;
	// listview
	private ListView noHandleListview;
	private ListView handleingListview;
	private ListView handedListview;
	private ListView gaiPaiListview;
	// listview的适配器
	private NoHandleAdapter noHandleAdapter;
	private HandleingAdapter handleingAdapter;
	private HandledAdapter handledAdapter;
	private GaiPaiAdapter gaiPaiAdapter;
	// 适配器数据
	private List<CertainLossTask> noHandleBeans, handleBeans, completedBeans, dispatchBeans, verifypassTasks;
	private ImageButton btnHistorySur;
	// 控件
	private TextView btnSurvey;
	private TextView btnDeloss;
	// 核损退回
	private RelativeLayout rlHeSunBack;
	private ListView lvHeSunBack;
	private HeSunBackAdapter heSunBackAdapter;

	private TextView tvNoHandNumber;
	private TextView tvHandingNumber;
	private TextView tvHandedNumber;
	private TextView tvGaiPaiNumber;
	private TextView tvHeSunBackNumber;

	public DelossTaskActivity(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {

		return layout;
	}

	@Override
	public Integer getType() {

		return ConstantValue.Page_second;
	}

	@Override
	protected void init() {
		// 第一步：加载layout
		layout = (LinearLayout) inflater.inflate(R.layout.delossmaintask, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		// 初始化控件
		btnSurvey = (TextView) layout.findViewById(R.id.delosmaintask_btn_survey);
		btnDeloss = (TextView) layout.findViewById(R.id.delosmaintask_btn_defloss);
		rBtnNoHandle = (RelativeLayout) layout.findViewById(R.id.delosmaintask_btn_no_handle);
		rBtnHandleing = (RelativeLayout) layout.findViewById(R.id.delosmaintask_btn_handleing);
		rBtnHanded = (RelativeLayout) layout.findViewById(R.id.delosmaintask_btn_handled);
		rBtnGaiPai = (RelativeLayout) layout.findViewById(R.id.delosmaintask_btn_gaipai);
		// listview
		noHandleListview = (ListView) layout.findViewById(R.id.delosmaintask_no_handle_listview);
		handleingListview = (ListView) layout.findViewById(R.id.delosmaintask_no_handleing_listview);
		handedListview = (ListView) layout.findViewById(R.id.delosmaintask_no_handled_listview);
		gaiPaiListview = (ListView) layout.findViewById(R.id.delosmaintask_no_gaipai_listview);
		btnHistorySur = (ImageButton) layout.findViewById(R.id.delosmaintask_no_handled_imbtn_history);
		rlHeSunBack = (RelativeLayout) layout.findViewById(R.id.delosmaintask_btn_hesunback);
		tvHeSunBackNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_hesunback_numbeer);
		lvHeSunBack = (ListView) layout.findViewById(R.id.delosmaintask_no_hesunback_listview);
		tvNoHandNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_handle_number);
		tvHandingNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_handleing_numbe);
		tvHandedNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_handled_numbe);
		tvGaiPaiNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_gaipai_numbe);
		tvHeSunBackNumber = (TextView) layout.findViewById(R.id.delosmaintask_no_hesunback_numbeer);
		
		// 初始化任务数据add by jingtuo
		commonUtils = new CommonUtils();

		// 协赔远不显示核损打回任务
		if (!SystemConfig.UserRightIsAdvanced) {
			if (rlHeSunBack.getVisibility() == View.VISIBLE)
				rlHeSunBack.setVisibility(View.GONE);
			if (lvHeSunBack.getVisibility() == View.VISIBLE)
				lvHeSunBack.setVisibility(View.GONE);
		}

	}

	@Override
	public void onResume() {
		LoadingData();
		super.onResume();
	}

	/**
	 * 加载数据
	 */
	private void LoadingData() {
		noHandleBeans = CertainLossTaskAccess.getNoHandleTask();
		handleBeans = CertainLossTaskAccess.getHandleTask();
		completedBeans = CertainLossTaskAccess.getCompletedTask();
		dispatchBeans = CertainLossTaskAccess.getDispatchTask();
		verifypassTasks = CertainLossTaskAccess.getVerifypassTask();
		tvNoHandNumber.setText(noHandleBeans.size() + "");
		tvHandingNumber.setText(handleBeans.size() + "");
		tvHandedNumber.setText(completedBeans.size() + "");
		tvGaiPaiNumber.setText(dispatchBeans.size() + "");
		tvHeSunBackNumber.setText(verifypassTasks.size() + "");
		listView = new ArrayList<View>();
		for (int i = 0; i < noHandleBeans.size(); i++) {
			addView(noHandleBeans.get(i));
		}
		// 未处理任务
		if (noHandleAdapter == null) {
			noHandleAdapter = new NoHandleAdapter(listView);
			noHandleListview.setAdapter(noHandleAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(noHandleListview);
		} else {
			noHandleAdapter.setViews(listView);
			noHandleAdapter.notifyDataSetChanged();
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(noHandleListview);
		}
		// 核损打回
		if (heSunBackAdapter == null) {
			heSunBackAdapter = new HeSunBackAdapter(context, verifypassTasks);
			lvHeSunBack.setAdapter(heSunBackAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvHeSunBack);
		} else {
			heSunBackAdapter.setCertainLossTasks(verifypassTasks);
			heSunBackAdapter.notifyDataSetChanged();
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvHeSunBack);
		}
		// 正在处理任务
		if (handleingAdapter == null) {
			handleingAdapter = new HandleingAdapter(context, handleBeans);
			handleingListview.setAdapter(handleingAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
		} else {
			handleingAdapter.setCertainLossTasks(handleBeans);
			handleingAdapter.notifyDataSetChanged();
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
		}
		// 完成任务
		handledAdapter = new HandledAdapter(context, completedBeans, handler);
		handedListview.setAdapter(handledAdapter);
		new ScrollListViewUtils().setListViewHeightBasedOnChildren(handedListview);
		// 改派任务
		if (gaiPaiAdapter == null) {
			gaiPaiAdapter = new GaiPaiAdapter(context, handler, dispatchBeans);
			gaiPaiListview.setAdapter(gaiPaiAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(gaiPaiListview);
		} else {
			gaiPaiAdapter.setHandleBeans(dispatchBeans);
			gaiPaiAdapter.notifyDataSetChanged();
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(gaiPaiListview);
		}
	}

	@Override
	protected void setListener() {
		btnSurvey.setOnClickListener(this);
		rBtnNoHandle.setOnClickListener(this);
		rBtnHandleing.setOnClickListener(this);
		rBtnHanded.setOnClickListener(this);
		rBtnGaiPai.setOnClickListener(this);
		btnHistorySur.setOnClickListener(this);
		rlHeSunBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** --------------> 【查勘任务】 */	
		case R.id.delosmaintask_btn_survey:
			UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
			break;
			
		/** --------------> 【定损任务】 */	
		case R.id.delosmaintask_btn_defloss:
			UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
			break;

		/** --------------> 【历史记录任务】 */	
		case R.id.delosmaintask_no_handled_imbtn_history:
			getAllComplete();
			break;

		/** --------------> 【未处理定损任务】 */	
		case R.id.delosmaintask_btn_no_handle:
			// 查找拨打电话记录
			if(!SystemConfig.registNoCallphone.equals("")){
				CallPhoneUtil phoneUtil = new CallPhoneUtil(context);
				phoneUtil.queryCallHistroty(SystemConfig.registNoCallphone);
				
				LoadingData();
			}
			
			// 未处理定损任务
			commonUtils.showView(context, noHandleListview, new Ifunction() {
				@Override
				public void setFunction() {
					
				}
			});
			break;

		/** --------------> 【正在处理定损任务】 */	
		case R.id.delosmaintask_btn_handleing:
			// 查找拨打电话记录
			if(!SystemConfig.registNoCallphone.equals("")){
				CallPhoneUtil phoneUtil = new CallPhoneUtil(context);
				phoneUtil.queryCallHistroty(SystemConfig.registNoCallphone);
				
				LoadingData();
			}
			
			// 在处理定损任务
			commonUtils.showView(context, handleingListview, new Ifunction() {
				@Override
				public void setFunction() {
					// 填充数据
				}
			});

			break;

		/** --------------> 【已完成定损任务】 */	
		case R.id.delosmaintask_btn_handled:
			// 完成的理定损任务
			commonUtils.showView(context, handedListview, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;

		/** --------------> 【改派定损任务】 */	
		case R.id.delosmaintask_btn_gaipai:
			// 改派的任务
			commonUtils.showView(context, gaiPaiListview, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;

		/** --------------> 【核损打回定损任务】 */	
		case R.id.delosmaintask_btn_hesunback:
			// 核损退回
			commonUtils.showView(context, lvHeSunBack, new Ifunction() {
				@Override
				public void setFunction() {

				}
			});
			break;
		}
	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_Defloss_Task;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	/***
	 * 改派任务成功后刷新任务列表
	 * 
	 * @author jianfan
	 * 
	 */
	class NoticeInsertReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			dispatchBeans = CertainLossTaskAccess.getDispatchTask();
			// 改派任务
			if (gaiPaiAdapter == null) {
				gaiPaiAdapter = new GaiPaiAdapter(context, handler, dispatchBeans);
				gaiPaiListview.setAdapter(gaiPaiAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(gaiPaiListview);
			} else {
				gaiPaiAdapter.setHandleBeans(dispatchBeans);
				gaiPaiAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(gaiPaiListview);
			}
			tvGaiPaiNumber.setText(dispatchBeans.size() + "");
		}
	}

	public void addView(final CertainLossTask certainLossTask) {
		View view = View.inflate(context, R.layout.maintask_group_item, null);
		RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.defloss_maintask_aa);
		LinearLayout assuredLL = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_3);
		final TextView claiMNo = (TextView) view.findViewById(R.id.maintask_group_item_claimNo);
		ImageView baoAnRenImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_1_imv);
		TextView banAnTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_name);
		final TextView banAnPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_phone);
		ImageView contactImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv);
		TextView contactName = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_name);
		final TextView contactPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_phone);
		ImageView timeImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv_time);
		Chronometer chronometer = (Chronometer) view.findViewById(R.id.maintask_group_item_ll_2_tv_time);
		TextView receivetime = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_tv_receivetime);
		Button btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
		Button btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
		Button btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
		TextView tvCarType = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_cattyple);
		// 按钮
		btnMap.setVisibility(View.VISIBLE);
		btnPrint.setVisibility(View.GONE);
		btnRepeal.setVisibility(View.GONE);
		tvCarType.setVisibility(View.VISIBLE);
		// 定损车类型
		String carType = certainLossTask.getItemtype();
		if ("thisCar".equals(carType)) {
			tvCarType.setVisibility(View.VISIBLE);
			tvCarType.setText("标的车");
		} else if ("thirdCar".equals(carType)) {
			tvCarType.setVisibility(View.VISIBLE);
			tvCarType.setText("三者车");
		} else {
			tvCarType.setVisibility(View.GONE);
		}
		// 被保险人信息不显示
		assuredLL.setVisibility(View.GONE);
		// 如果报案人和联系人相同
		if (certainLossTask.getReportorname().equals(certainLossTask.getLinkername())) {
			// 报案人信息不显示
			baoAnRenImv.setVisibility(View.GONE);
			banAnTV.setVisibility(View.GONE);
			banAnPhone.setVisibility(View.GONE);
		} else {
			baoAnRenImv.setVisibility(View.VISIBLE);
			banAnTV.setVisibility(View.VISIBLE);
			banAnPhone.setVisibility(View.VISIBLE);
			banAnTV.setText(certainLossTask.getReportorname());
			banAnPhone.setText(certainLossTask.getReportorphoneno());
			banAnPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		}

		claiMNo.setText(certainLossTask.getRegistno());
		contactName.setText(certainLossTask.getLinkername());
		contactPhone.setText(certainLossTask.getLinkerphoneno());
		contactPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		if (CertainLossTaskAccess.relevanceCheckcontacts(certainLossTask.getRegistno())) {
			contactPhone.setTextColor(Color.RED);
			banAnPhone.setTextColor(Color.RED);
			// 查勘联系客户时间
			CheckTask checkTask = CheckTaskAccess.findByRegistno(certainLossTask.getRegistno());
			CertainLossTaskAccess.update(certainLossTask.getRegistno(), certainLossTask.getItemno(), null, null, null, null, null, Integer.parseInt("1"),
					checkTask.getContacttime());
		} else {
			if (certainLossTask.getIscontact() != 1) {// 未联系联系人 by jingtuo
				contactPhone.setTextColor(Color.GRAY);
				banAnPhone.setTextColor(Color.GRAY);
			} else {
				contactPhone.setTextColor(Color.RED);
				banAnPhone.setTextColor(Color.RED);
			}
		}

		/**
		 * 添加一個時間組件
		 */
		chronometer.setText(DateTimeUtils.format(certainLossTask.getAlarmtime()));
		certainLossTask.setTvTime(chronometer);
		AlarmClockManager.getInstance(context).addTask(certainLossTask);

		// 拨打电话
		banAnPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commonUtils.showView(context, noHandleListview, new Ifunction() {
					@Override
					public void setFunction() {
						
					}
				});
				
				MainTaskUtils.CallPhone(context, ((TextView) v).getText().toString().trim(), claiMNo.getText().toString());
			}
		});
		contactPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commonUtils.showView(context, noHandleListview, new Ifunction() {
					@Override
					public void setFunction() {
						
					}
				});
				
				SystemConfig.isOperate = false;
				MainTaskUtils.CallPhone(context, ((TextView) v).getText().toString().trim(), claiMNo.getText().toString());
			}
		});

		// 任务到达时间或预约时间
		if (certainLossTask.getOrdertime() == null || certainLossTask.getOrdertime().equals("")) {
			receivetime.setText(certainLossTask.getInputedate());
		} else {
			receivetime.setText(certainLossTask.getOrdertime());
		}

		// 地图
		btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 导航地图 */
				double lat = Double.parseDouble(certainLossTask.getLatitude());
				double lng = Double.parseDouble(certainLossTask.getLongitude());
				AutoDriving autoDriving = new AutoDriving(context);
				autoDriving.gotoMapABC(lng, lat);
			}
		});

		// 跳转到任务详细页面
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 精友定损退回标志
				SystemConfig.JYDeflossStatus = SystemConfig.JYDeflossRequest;
				TextView timeTV = (TextView) v.findViewById(R.id.maintask_group_item_ll_2_tv_time);
				if (certainLossTask.getIsread() != 1) {
					certainLossTask.setIsread(1);
					certainLossTask.setAlarmtime(DateTimeUtils.toSeconds(timeTV.getText().toString()));
					CertainLossTaskAccess.update(certainLossTask.getRegistno(), certainLossTask.getItemno(), Integer.valueOf(1), null, null,
							Integer.valueOf(certainLossTask.getAlarmtime()), null, null, null);
					AlarmClockManager.getInstance(context).addTask(certainLossTask);
				}

				// 未处理任务
				SystemConfig.isOperate = false;
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", certainLossTask.getRegistno());
				bundle1.putString("checkStatue", "1");
				bundle1.putString("itemNo", certainLossTask.getItemno() + "");
				UiManager.getInstance().changeView(DeflossBasicActivity.class, false, bundle1, true);
			}
		});

		listView.add(view);
	}

	private void getAllComplete() {
		new AsyncTask<String, Void, TaskQueryResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(TaskQueryResponse result) {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				bundle = new Bundle();
				bundle.putString("type", "certainLoss");
				UiManager.getInstance().changeView(QueryHistoryActivity.class, false, bundle, true);
			};

			@Override
			protected TaskQueryResponse doInBackground(String... params) {
				TaskQueryRequest tqr = new TaskQueryRequest();
				tqr.setTaskType("certainLoss");
				tqr.setTaskStatus("1");
				tqr.setUserCode(SystemConfig.USERLOGINNAME);
				return TaskQueryHttpService.taskQuerySercive(tqr, context.getString(R.string.http_url));
			}

		}.execute();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * @author JingTuo
	 * 
	 */
	class MyOnChronometerTickListener implements OnChronometerTickListener {
		private CertainLossTask certainLossTask;
		private Chronometer chronometer;

		public MyOnChronometerTickListener(Chronometer chronometer, CertainLossTask certainLossTask) {
			this.chronometer = chronometer;
			this.certainLossTask = certainLossTask;
		}

		@Override
		public void onChronometerTick(Chronometer arg0) {
			int current = DateTimeUtils.toSeconds(chronometer.getText().toString());
			certainLossTask.setAlarmtime(current);
			CertainLossTaskAccess.update(certainLossTask.getRegistno(), certainLossTask.getItemno(), null, null, null, 
					Integer.valueOf(certainLossTask.getAlarmtime()), null, null, null);

			if (current == 180 || current == 360 || current == 540) {// 每走三分钟
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context, NoticeActivity.class);
				NoticeManager.getInstance(context).notice(NoticeManager.TAG_NOREAD, "有新任务尚未阅读", R.raw.noread, intent);
			}
			if (current >= 540) {// 九分钟
				chronometer.stop();
			}
		}
	}
}
