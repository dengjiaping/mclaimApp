package com.sinosoftyingda.fastclaim.maintask.deAdapter;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossPrintActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HandledAdapter extends BaseAdapter {

	private Context context;
	private List<CertainLossTask> certainLossTasks;
	private Handler handler;

	public List<CertainLossTask> getCertainLossTasks() {
		return certainLossTasks;
	}

	public void setCertainLossTasks(List<CertainLossTask> certainLossTasks) {
		this.certainLossTasks = certainLossTasks;
	}

	public HandledAdapter(Context context, List<CertainLossTask> certainLossTasks, Handler handler) {
		this.handler = handler;
		this.certainLossTasks = certainLossTasks;
		this.context = context;
	}

	public List<CertainLossTask> getHandleBeans() {
		return certainLossTasks;
	}

	public void setHandleBeans(List<CertainLossTask> certainLossTasks) {
		this.certainLossTasks = certainLossTasks;
	}

	@Override
	public int getCount() {

		return certainLossTasks.size();
	}

	@Override
	public Object getItem(int position) {

		return certainLossTasks.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			view = View.inflate(context, R.layout.maintask_group_item, null);
			holder.flBtn = (FrameLayout) view.findViewById(R.id.maintask_flout_btn);
			holder.ll2 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_2);
			holder.claiMNo = (TextView) view.findViewById(R.id.maintask_group_item_claimNo);
			holder.baoAnRenImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_1_imv);
			holder.banAnTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_name);
			holder.banAnPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_phone);
			holder.contactImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv);
			holder.contactName = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_name);
			holder.contactPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_phone);
			holder.TimeImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv_time);
			holder.TimeTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_tv_time);
			holder.btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
			holder.assuredTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_name);
			holder.carNoTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_carno);
			holder.carTyple = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_cattyple);
			holder.llTime = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_al);
			holder.btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
			holder.tvCarType = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_cattyple);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}
		holder.llTime.setVisibility(View.GONE);
		// 不显示联系人等信息
		if (holder.ll2.getVisibility() == View.VISIBLE)
			holder.ll2.setVisibility(View.GONE);
		// 地图按钮不显示
		if (holder.btnMap.getVisibility() == View.VISIBLE)
			holder.btnMap.setVisibility(View.GONE);
		// 默认显示撤回按钮

		if (holder.btnRepeal.getVisibility() == View.GONE)
			holder.btnRepeal.setVisibility(View.VISIBLE);

		final CertainLossTask certainLossTask = certainLossTasks.get(position);

		if ("4".equals(certainLossTask.getVerifypassflag())) {
			// 核损通过
			if (holder.btnRepeal.getVisibility() == View.VISIBLE)
				holder.btnRepeal.setVisibility(View.GONE);
			if (holder.btnPrint.getVisibility() == View.GONE)
				holder.btnPrint.setVisibility(View.VISIBLE);
		} else {// 初始状态
			if (holder.btnPrint.getVisibility() == View.VISIBLE)
				holder.btnPrint.setVisibility(View.GONE);
			if (holder.btnRepeal.getVisibility() == View.GONE)
				holder.btnRepeal.setVisibility(View.VISIBLE);
		}
		holder.claiMNo.setText(certainLossTask.getRegistno());
		holder.banAnTV.setText(certainLossTask.getReportorname());
		holder.banAnPhone.setText(certainLossTask.getReportorphoneno());
		holder.assuredTV.setText(certainLossTask.getInsrtedname());

		// 定损车类型
		String carType = certainLossTask.getItemtype();
		if ("thisCar".equals(carType)) {
			holder.carTyple.setVisibility(View.VISIBLE);
			holder.carTyple.setText("标的车");
		} else if ("thirdCar".equals(carType)) {
			holder.carTyple.setVisibility(View.VISIBLE);
			holder.carTyple.setText("三者车");
		} else {
			holder.carTyple.setVisibility(View.GONE);
		}

		holder.carNoTV.setText(certainLossTask.getLicenseno());

		// 撤回按钮事件
		holder.btnRepeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDefloss(certainLossTask.getRegistno(), certainLossTask.getItemno());
			}
		});
		// 打印按钮事件
		holder.btnPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("registno", certainLossTask.getRegistno());
				bundle.putInt("itemno", certainLossTask.getItemno());
				UiManager.getInstance().changeView(DeflossPrintActivity.class, false, bundle, true);
			}
		});

		// 跳转到任务详细页面
		// DengGuang
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 精友定损退回标志
				SystemConfig.JYDeflossStatus = SystemConfig.JYDeflossQuery;
				SystemConfig.isOperate = false;

				// 正在处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", certainLossTask.getRegistno());
				bundle1.putString("checkStatue", "4");
				bundle1.putString("itemNo", certainLossTask.getItemno() + "");
				UiManager.getInstance().changeView(DeflossBasicActivity.class, false, bundle1, true);
			}
		});

		return view;

	}

	/****
	 * 交互
	 */
	protected void cancelDefloss(final String registNo, final Integer lossNo) {

		new AsyncTask<String, Void, CommonResponse>() {

			@Override
			protected void onPreExecute() {

				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				if ("YES".equals(result.getResponseCode())) {
					Toast.showToast(context, "撤销任务成功");
					CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, lossNo);
					certainLossTask.setSurveytaskstatus("2");
					certainLossTask.setVerifypassflag("");
					certainLossTask.setSynchrostatus("");
					List<CertainLossTask> list = new ArrayList<CertainLossTask>();
					list.add(certainLossTask);
					CertainLossTaskAccess.insertOrUpdate(list, true);
					// 跳转
					UiManager.getInstance().changeView(DelossTaskActivity.class, null, false);
				} else {

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
				requestSynchro.setNodeType("certainLoss");
				requestSynchro.setLossNo(lossNo + "");
				requestSynchro.setCancleType("Certainback");
				return SynchroClaimHttpService.synchroClaimService(requestSynchro, params[0]);
			}

		}.execute(context.getString(R.string.http_url));

	}

	private static class viewHolder {
		TextView claiMNo;
		ImageView baoAnRenImv;
		TextView banAnTV;
		TextView banAnPhone;
		ImageView contactImv;
		TextView contactName;
		TextView contactPhone;
		ImageView TimeImv;
		TextView TimeTV;
		TextView assuredTV;// 被保险人
		TextView carNoTV;// 车牌号
		TextView carTyple;// 车牌号
		Button btnMap;
		Button btnPrint;
		Button btnRepeal;
		FrameLayout flBtn;
		LinearLayout ll2;
		LinearLayout llTime;
		TextView tvCarType;
	}

}
