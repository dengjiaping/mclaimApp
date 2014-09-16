package com.sinosoftyingda.fastclaim.more.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblGPSAddress;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryRequest;
import com.sinosoftyingda.fastclaim.common.model.TaskQueryResponse;
import com.sinosoftyingda.fastclaim.common.service.TaskQueryHttpService;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.BottomManager;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.maintask.utils.CallPhoneUtil;

public class AboutInfoActivity extends BaseView {
	private View layout;
	private TextView tvVersion;

	public AboutInfoActivity(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_third;
	}

	Button refreshNewRegist_bt = null;
	TextView refreshUserLocation_tv = null;
	TextView refreshUserLocation_HHmmss_tv = null;
	TextView refreshUserLocation_yyyyMMdd_tv = null;
	Thread th = null;
	Boolean bl = true;

	@Override
	protected void init() {
		layout = inflater.inflate(R.layout.aboutinfo, null);
		LinearLayout.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		refreshNewRegist_bt = (Button) layout.findViewById(R.id.refreshNewRegist_bt);
		refreshNewRegist_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshDataInner();
			}
		});

	}

	@Override
	public Integer getExit() {

		return ConstantValue.Page_Title_AboutInfo;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	private void refreshDataInner() {
				// 登录拉取的标志位
				SystemConfig.isLoding = true;
				Bundle bundle = new Bundle();
				bundle.putInt("login", 1);
				// 进度主页面
				UiManager.getInstance().clearViewCache();
				UiManager.getInstance().changeView(SurveyTaskActivity.class,bundle, false);
				BottomManager.getInstance().daoHang(R.id.activity_main_bottom_task);
	}

	private Handler refreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String address = "";
			String tblGPSAddress = null;
			tblGPSAddress = TblGPSAddress.getAddress();
			if (!TextUtils.isEmpty(SystemConfig.currentThoroughfare)) {
				address = SystemConfig.currentThoroughfare;
			} else if (tblGPSAddress != null
					&& !TextUtils.isEmpty(tblGPSAddress)) {
				address = tblGPSAddress;
			} else {
				address = "地址暂无";
			}
			refreshUserLocation_tv = (TextView) layout.findViewById(R.id.refreshUserLocation_tv);
			refreshUserLocation_HHmmss_tv=(TextView) layout.findViewById(R.id.refreshServerTime_HHmmss_tv);
			refreshUserLocation_yyyyMMdd_tv=(TextView) layout.findViewById(R.id.refreshServerTime_yyyyMMdd_tv);
			refreshUserLocation_tv.setText(address+"");
			SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			String d_t_yyyy =null;
			String d_t_MM =null;
			try {
				date = aa.parse(SystemConfig.serverTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			aa.applyPattern("yyyy年MM月dd日");
			d_t_yyyy = aa.format(date);

			aa.applyPattern("HH:mm:ss");
			d_t_MM = aa.format(date);
			refreshUserLocation_HHmmss_tv.setText(d_t_MM);
			refreshUserLocation_yyyyMMdd_tv.setText(d_t_yyyy);
		}
	};

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}
	Runnable run = new Runnable() {
		public void run() {
			Thread.currentThread().getId();
			int i = 0;
			while (bl) {
				Message msg = new Message();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msg.what = 0;
				refreshHandler.sendMessage(msg);
			}
		}
	};
	@Override
	public void onResume() {
		super.onResume();
		th = new Thread(run);
		bl=true;
		th.start();
	}

	public void onPause() {
		super.onPause();
		bl=false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		th.interrupt();
	}

}
