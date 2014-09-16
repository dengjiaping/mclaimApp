package com.sinosoftyingda.fastclaim.maintask.page;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.sinosoftyingda.fastclaim.MainActivity;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.drivingline.AutoDriving;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.definetime.TimeService;
import com.sinosoftyingda.fastclaim.common.mina.NoticeManager;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.service.TaskQueryHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.BottomManager;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.adapter.GaiPaiAdapter;
import com.sinosoftyingda.fastclaim.maintask.adapter.HandledAdapter;
import com.sinosoftyingda.fastclaim.maintask.adapter.HandleingAdapter;
import com.sinosoftyingda.fastclaim.maintask.adapter.NoHandleAdapter;
import com.sinosoftyingda.fastclaim.maintask.utils.CallPhoneUtil;
import com.sinosoftyingda.fastclaim.maintask.utils.MainTaskUtils;
import com.sinosoftyingda.fastclaim.notice.page.NoticeActivity;
import com.sinosoftyingda.fastclaim.query.page.QueryHistoryActivity;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.Ifunction;

/***
 * 查勘页面
 * @author JingTuo
 */
public class SurveyTaskActivity extends BaseView implements OnClickListener {
	public static final int		TASK_UPDATING	= 100001;
	private CommonUtils			commonUtils;
	private LinearLayout		layout;
	private List<View>			listView		= new ArrayList<View>();
	/*********** 控件 ******************/
	private RelativeLayout		rlBg;
	private TextView			btnSurvey;
	private TextView			btnDeflos;
	// group
	private RelativeLayout		RbtnNoHandle;
	private RelativeLayout		RbtnHandleing;
	private RelativeLayout		RbtnHanded;
	private RelativeLayout		RbtnGaiPai;
	// listview
	private ListView			noHandleListview;
	private ListView			handleingListview;
	private ListView			handedListview;
	private ListView			gaiPaiListview;
	// listview的适配器
	private NoHandleAdapter		noHandleAdapter;
	private List<CheckTask>		noHandleData;
	private HandleingAdapter	handleingAdapter;
	private List<CheckTask>		handleData;
	private HandledAdapter		handledAdapter;
	private List<CheckTask>		handledData;
	private GaiPaiAdapter		gaiPaiAdapter;
	private List<CheckTask>		gaiPaiData;
	private ImageButton			BtnHistorySur;
	private TextView			tvNoHandleNumber;
	private TextView			tvHandleingNumber;
	private TextView			tvHandledNumber;
	private TextView			tvGaiPaiNumber;

	private TextView			tvBaoAn;
	private TextView			tvcontactPhone;
	private int					loginGetTasks	= 0;

	public SurveyTaskActivity(Context context, Bundle bundle) {
		super(context, bundle);
		MainActivity activity = (MainActivity) context;
		activity.startService();
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
		layout = (LinearLayout) inflater.inflate(R.layout.maintask, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		// 第二步：初始化layout中控件
		rlBg = (RelativeLayout) layout.findViewById(R.id.maintask_rl_1);
		btnSurvey = (TextView) layout.findViewById(R.id.maintask_btn_survey);
		btnDeflos = (TextView) layout.findViewById(R.id.maintask_btn_defloss);
		RbtnNoHandle = (RelativeLayout) layout.findViewById(R.id.maintask_btn_no_handle);
		RbtnHandleing = (RelativeLayout) layout.findViewById(R.id.maintask_btn_handleing);
		RbtnHanded = (RelativeLayout) layout.findViewById(R.id.maintask_btn_handled);
		RbtnGaiPai = (RelativeLayout) layout.findViewById(R.id.maintask_btn_gaipai);
		// listview
		noHandleListview = (ListView) layout.findViewById(R.id.maintask_no_handle_listview);
		handleingListview = (ListView) layout.findViewById(R.id.maintask_no_handleing_listview);
		handedListview = (ListView) layout.findViewById(R.id.maintask_no_handled_listview);
		gaiPaiListview = (ListView) layout.findViewById(R.id.maintask_no_gaipai_listview);
		BtnHistorySur = (ImageButton) layout.findViewById(R.id.maintask_no_handled_imbtn_history);
		tvNoHandleNumber = (TextView) layout.findViewById(R.id.maintask_no_handle_number);
		tvHandleingNumber = (TextView) layout.findViewById(R.id.maintask_no_handleing_numbe);
		tvHandledNumber = (TextView) layout.findViewById(R.id.maintask_no_handled_numbe);
		tvGaiPaiNumber = (TextView) layout.findViewById(R.id.maintask_no_gaipai_numbe);
		commonUtils = new CommonUtils();

		// 设置顶部标题
		TopManager.getInstance().setHeadTitle("任务管理", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
	}

	private void LoadingData() {
		if (SystemConfig.isLoding) {// 与服务器交互拉取数据
			setDate();
		} else {
			refreshData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 底部导航错位解决
		if (bundle != null) {
			String str11 = bundle.getString("11111111");
			if ("11111111".equals(str11)) {
				BottomManager.getInstance().daoHang(R.id.activity_main_bottom_task);
			}
			
			// 获取登录进来的标志位
			loginGetTasks = bundle.getInt("login");
		}
		
		// 查找拨打电话记录
		if(!SystemConfig.registNoCallphone.equals("")){
			CallPhoneUtil phoneUtil = new CallPhoneUtil(context);
			phoneUtil.queryCallHistroty(SystemConfig.registNoCallphone);
		}
		
		LoadingData();
	}

	private void setDate() {
		new AsyncTask<String, Void, TaskQueryResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(TaskQueryResponse result) {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				refreshData();
				SystemConfig.isLoding = false;
			};

			@Override
			protected TaskQueryResponse doInBackground(String... params) {
				TaskQueryRequest taskQueryRequest = new TaskQueryRequest();
				// 拉取登录过来的接口信息
				if (loginGetTasks == 1) {
					taskQueryRequest.setTaskType("login");
					loginGetTasks = 0;
				} else {
					taskQueryRequest.setTaskType("all");
				}
				taskQueryRequest.setTaskStatus("0");
				taskQueryRequest.setUserCode(params[0]);
				TaskQueryResponse result = TaskQueryHttpService.taskQuerySercive(taskQueryRequest, context.getString(R.string.http_url));
				return result;
			}
		}.execute(SystemConfig.USERLOGINNAME);
	}

	@Override
	protected void setListener() {
		btnSurvey.setOnClickListener(this);
		btnDeflos.setOnClickListener(this);
		RbtnNoHandle.setOnClickListener(this);
		RbtnHandleing.setOnClickListener(this);
		RbtnHanded.setOnClickListener(this);
		RbtnGaiPai.setOnClickListener(this);
		BtnHistorySur.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 查勘任务 */
		case R.id.maintask_btn_survey:
			UiManager.getInstance().changeView(SurveyTaskActivity.class, null, true);
			break;
			
		/** 定损任务 */
		case R.id.maintask_btn_defloss:
			UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);
			break;
			
		/** 历史完成记录 */
		case R.id.maintask_no_handled_imbtn_history:
			// 跳转到查勘历史查询页面
			getAllComplete();
			break;
			
		/** --------------> 【未处理查勘记录】 */	
		case R.id.maintask_btn_no_handle:
			// 查找拨打电话记录
			if(!SystemConfig.registNoCallphone.equals("")){
				CallPhoneUtil phoneUtil = new CallPhoneUtil(context);
				phoneUtil.queryCallHistroty(SystemConfig.registNoCallphone);
				
				LoadingData();
			}
			
			if(noHandleData != null){
				// 隐藏任务条
				commonUtils.showView(context, noHandleListview, new Ifunction() {
					@Override
					public void setFunction() {
	
					}
				});
			}
			break;
			
		/** --------------> 【正在处理查勘记录】 */	
		case R.id.maintask_btn_handleing:
			// 查找拨打电话记录
			if(!SystemConfig.registNoCallphone.equals("")){
				CallPhoneUtil phoneUtil = new CallPhoneUtil(context);
				phoneUtil.queryCallHistroty(SystemConfig.registNoCallphone);
				
				LoadingData();
			}
			
			if (handleData != null) {
				// 隐藏任务条
				commonUtils.showView(context, handleingListview, new Ifunction() {
					@Override
					public void setFunction() {
	
					}
				});
			}
			break;
			
		/** --------------> 【已完成查勘记录】 */	
		case R.id.maintask_btn_handled:
			// 完成的理查勘任务
			if (handledData != null) {
				commonUtils.showView(context, handedListview, new Ifunction() {
					@Override
					public void setFunction() {

					}
				});
			}
			break;
			
		/** --------------> 【改派查勘记录】 */	
		case R.id.maintask_btn_gaipai:
			// 改派的任务
			if (gaiPaiData != null) {
				commonUtils.showView(context, gaiPaiListview, new Ifunction() {
					@Override
					public void setFunction() {
						
					}
				});
			}
			break;
		}

	}

	/***
	 * 改派任务成功后刷新任务列表
	 * @author jianfan
	 * 
	 */
	class NoticeInsertReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取改派任务状态
			gaiPaiData = CheckTaskAccess.getDispatchTask();
			if (gaiPaiAdapter == null) {
				gaiPaiAdapter = new GaiPaiAdapter(context, handler, gaiPaiData);
				gaiPaiListview.setAdapter(gaiPaiAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
			} else {
				gaiPaiAdapter.setCheckTasks(gaiPaiData);
				gaiPaiAdapter.notifyDataSetChanged();
				tvGaiPaiNumber.setText(gaiPaiData.size() + "");
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
			}
		}
	}

	/**
	 * 获取查勘为处理任务ItemView
	 * @param handleBean
	 */
	private void addHandleBeanView(final CheckTask checkTask) {
		View convertView = View.inflate(context, R.layout.maintask_group_item, null);
		RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.defloss_maintask_aa);
		LinearLayout assuredLl = (LinearLayout) convertView.findViewById(R.id.maintask_group_item_ll_3);
		final TextView registNo = (TextView) convertView.findViewById(R.id.maintask_group_item_claimNo);
		ImageView baoAnRenImv = (ImageView) convertView.findViewById(R.id.maintask_group_item_ll_1_imv);
		TextView banAnTV = (TextView) convertView.findViewById(R.id.maintask_group_item_ll_1_name);
		final TextView banAnPhone = (TextView) convertView.findViewById(R.id.maintask_group_item_ll_1_phone);
		TextView contactName = (TextView) convertView.findViewById(R.id.maintask_group_item_ll_2_name);
		final TextView contactPhone = (TextView) convertView.findViewById(R.id.maintask_group_item_ll_2_phone);
		Chronometer chronometer = (Chronometer) convertView.findViewById(R.id.maintask_group_item_ll_2_tv_time);
		TextView receivetime = (TextView) convertView.findViewById(R.id.maintask_group_item_ll_2_tv_receivetime);
		Button btnMap = (Button) convertView.findViewById(R.id.maintask_btn_map);
		Button btnPrint = (Button) convertView.findViewById(R.id.maintask_btn_print);
		Button btnRepeal = (Button) convertView.findViewById(R.id.maintask_btn_repeal);
		// 被保险人信息不显示
		assuredLl.setVisibility(View.GONE);
		btnMap.setVisibility(View.VISIBLE);
		btnPrint.setVisibility(View.GONE);
		btnRepeal.setVisibility(View.GONE);
		receivetime.setVisibility(View.VISIBLE);
		// 如果报案人和联系人相同
		if (checkTask.getReportorname().equals(checkTask.getLinkername())) {
			// 报案人信息不显示
			baoAnRenImv.setVisibility(View.GONE);
			banAnTV.setVisibility(View.GONE);
			banAnPhone.setVisibility(View.GONE);
		} else {
			baoAnRenImv.setVisibility(View.VISIBLE);
			banAnTV.setVisibility(View.VISIBLE);
			banAnPhone.setVisibility(View.VISIBLE);
			banAnTV.setText(checkTask.getReportorname());
			banAnPhone.setText(checkTask.getReportorphoneno());
			banAnPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		}
		registNo.setText(checkTask.getRegistno());
		contactName.setText(checkTask.getLinkername());
		contactPhone.setText(checkTask.getLinkerphoneno());
		contactPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		
		// 拨打电话
		banAnPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, ((TextView) v).getText().toString().trim(), registNo.getText().toString());
				commonUtils.showView(context, noHandleListview, new Ifunction() {
					@Override
					public void setFunction() {

					}
				});
			}
		});

		tvBaoAn = banAnPhone;
		tvcontactPhone = contactPhone;
		contactPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, ((TextView) v).getText().toString().trim(), registNo.getText().toString());
				commonUtils.showView(context, noHandleListview, new Ifunction() {
					@Override
					public void setFunction() {

					}
				});
			}
		});

		// 显示案件状态
		if (checkTask.getIscontact() != 1) {
			contactPhone.setTextColor(Color.GRAY);
			banAnPhone.setTextColor(Color.GRAY);
		} else {
			contactPhone.setTextColor(Color.RED);
			banAnPhone.setTextColor(Color.RED);
		}

		// 任务到达时间或预约时间
		if (checkTask.getOrdertime() == null || checkTask.getOrdertime().equals("")) {
			receivetime.setText(checkTask.getInputedate());
		} else {
			receivetime.setText(checkTask.getOrdertime());
		}
		
		// 地图
		btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 导航地图 */
				double lat = Double.parseDouble(checkTask.getLatitude());
				double lng = Double.parseDouble(checkTask.getLongitude());
				AutoDriving autoDriving = new AutoDriving(context);
				autoDriving.gotoMapABC(lng, lat);
			}
		});

		/**
		 * 添加一個時間組件
		 */
		chronometer.setText(DateTimeUtils.format(checkTask.getAlarmtime()));
		checkTask.setTvTime(chronometer);
		AlarmClockManager.getInstance(context).addTask(checkTask);

		// 跳转到任务详细页面
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkTask.getIsread() != 1) {
					Chronometer chronometer = (Chronometer) v.findViewById(R.id.maintask_group_item_ll_2_tv_time);
					chronometer.stop();
					checkTask.setIsread(1);
					checkTask.setAlarmtime(DateTimeUtils.toSeconds(chronometer.getText().toString()));
					AlarmClockManager.getInstance(context).addTask(checkTask);// 重置任务
					ContentValues values = new ContentValues();
					values.put("isread", "1");
					values.put("alarmtime", DateTimeUtils.toSeconds(chronometer.getText().toString()));
					DBUtils.update("checktask", values, "registno=?", new String[] { checkTask.getRegistno() });
				}
				// 未处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", checkTask.getRegistno());
				bundle1.putString("checkStatue", "1");
				UiManager.getInstance().changeView(SBasicActivity.class, false, bundle1, true);
			}
		});
		listView.add(convertView);
	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_Survey_Task;
	}

	@Override
	public Integer getBackMain() {
		return null;
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
				Bundle bundle1 = new Bundle();
				bundle1.putString("type", "check");
				UiManager.getInstance().changeView(QueryHistoryActivity.class, false, bundle1, true);
			};

			@Override
			protected TaskQueryResponse doInBackground(String... params) {
				TaskQueryRequest tqr = new TaskQueryRequest();
				tqr.setTaskType("check");
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
	 */
	class MyOnChronometerTickListener implements OnChronometerTickListener {
		private CheckTask	checkTask;
		private Chronometer	chronometer;

		public MyOnChronometerTickListener(Chronometer chronometer, CheckTask checkTask) {
			this.chronometer = chronometer;
			this.checkTask = checkTask;
		}

		@Override
		public void onChronometerTick(Chronometer arg0) {
			int current = DateTimeUtils.toSeconds(chronometer.getText().toString());
			checkTask.setAlarmtime(current);
			ContentValues values = new ContentValues();
			values.put("alarmtime", current);
			DBUtils.update("checktask", values, "registno=?", new String[] { checkTask.getRegistno() });
			if (current == 180 || current == 360 || current == 540) {
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

	public void refreshData() {
		new Thread() {
			@Override
			public void run() {
				noHandleData = CheckTaskAccess.getNoHandleTask();
				handleData = CheckTaskAccess.getHandleTask();
				handledData = CheckTaskAccess.getCompletedTask();
				gaiPaiData = CheckTaskAccess.getDispatchTask();
				Message message = new Message();
				message.what = TASK_UPDATING;
				handler.sendMessage(message);
			};
		}.start();
	}

	private Handler	handler	= new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TASK_UPDATING:
				tvNoHandleNumber.setText(noHandleData.size() + "");
				tvHandleingNumber.setText(handleData.size() + "");
				tvHandledNumber.setText(handledData.size() + "");
				tvGaiPaiNumber.setText(gaiPaiData.size() + "");
				listView = new ArrayList<View>();
				for (int i = 0; i < noHandleData.size(); i++) {
					addHandleBeanView(noHandleData.get(i));
				}
				// 未在处理
				noHandleAdapter = new NoHandleAdapter(listView);
				noHandleListview.setAdapter(noHandleAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(noHandleListview);
				// 正在处理
				if (handleingAdapter == null) {
					handleingAdapter = new HandleingAdapter(context, handleData);
					handleingListview.setAdapter(handleingAdapter);
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				} else {
					handleingAdapter.setCheckTasks(handleData);
					handleingAdapter.notifyDataSetChanged();
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				}
				// 已完成
				if (handledAdapter == null) {
					handledAdapter = new HandledAdapter(context, handledData);
					handedListview.setAdapter(handledAdapter);
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				} else {
					handledAdapter.setCheckTasks(handledData);
					handledAdapter.notifyDataSetChanged();
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				}
				// 改派
				if (gaiPaiAdapter == null) {
					gaiPaiAdapter = new GaiPaiAdapter(context, handler, gaiPaiData);
					gaiPaiListview.setAdapter(gaiPaiAdapter);
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				} else {
					gaiPaiAdapter.setCheckTasks(gaiPaiData);
					gaiPaiAdapter.notifyDataSetChanged();
					tvGaiPaiNumber.setText(gaiPaiData.size() + "");
					new ScrollListViewUtils().setListViewHeightBasedOnChildren(handleingListview);
				}
				break;
			case ConstantValue.ERROE:
				// 显示错误信息
				PromptManager.showErrorDialog(context,msg.obj.toString());
				break;
			}
			
		};
	};
}
