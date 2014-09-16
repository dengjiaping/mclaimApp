package com.sinosoftyingda.fastclaim.maintask.deAdapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.drivingline.AutoDriving;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.maintask.utils.MainTaskUtils;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*****
 * 核损打回
 * 
 * @author jianfan
 * 
 */
public class HeSunBackAdapter extends BaseAdapter {

	private Context context;
	private List<CertainLossTask> certainLossTasks;

	public List<CertainLossTask> getCertainLossTasks() {
		return certainLossTasks;
	}

	public void setCertainLossTasks(List<CertainLossTask> certainLossTasks) {
		this.certainLossTasks = certainLossTasks;
	}

	public HeSunBackAdapter(Context context, List<CertainLossTask> certainLossTasks) {
		this.certainLossTasks = certainLossTasks;
		this.context = context;
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
		final viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			view = View.inflate(context, R.layout.maintask_group_item, null);
			holder.assuredLL = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_3);
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
			holder.btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
			holder.llTime = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_al);
			holder.tvCarType = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_cattyple);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}
		// 显示地图，不显示打印和撤销
		if (holder.btnMap.getVisibility() == View.GONE)
			holder.btnMap.setVisibility(View.VISIBLE);
		if (holder.btnPrint.getVisibility() == View.VISIBLE)
			holder.btnPrint.setVisibility(View.GONE);
		if (holder.btnRepeal.getVisibility() == View.VISIBLE)
			holder.btnRepeal.setVisibility(View.GONE);

		// 不显示闹钟时间
		holder.llTime.setVisibility(View.GONE);
		// 被保险人信息不显示
		holder.assuredLL.setVisibility(View.GONE);

		final CertainLossTask certainLossTask = certainLossTasks.get(position);
		// 定损车类型
		String carType = certainLossTask.getItemtype();
		if ("thisCar".equals(carType)) {
			holder.tvCarType.setVisibility(View.VISIBLE);
			holder.tvCarType.setText("标的车");
		} else if ("thirdCar".equals(carType)) {
			holder.tvCarType.setVisibility(View.VISIBLE);
			holder.tvCarType.setText("三者车");
		} else {
			holder.tvCarType.setVisibility(View.GONE);
		}
		// 报案号
		holder.claiMNo.setText(certainLossTask.getRegistno());
		// 如果报案人和联系人相同
		if (certainLossTask.getReportorname().equals(certainLossTask.getLinkername())) {
			// 报案人信息不显示
			holder.baoAnRenImv.setVisibility(View.GONE);
			holder.banAnTV.setVisibility(View.GONE);
			holder.banAnPhone.setVisibility(View.GONE);
		} else {
			holder.baoAnRenImv.setVisibility(View.VISIBLE);
			holder.banAnTV.setVisibility(View.VISIBLE);
			holder.banAnPhone.setVisibility(View.VISIBLE);
			holder.banAnTV.setText(certainLossTask.getReportorname());
			holder.banAnPhone.setText(certainLossTask.getReportorphoneno());
			holder.banAnPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		}

		// 跳转到任务详细页面
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 精友定损退回标志
				SystemConfig.JYDeflossStatus = SystemConfig.JYDeflossBack;
				// 正在处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", certainLossTask.getRegistno());
				bundle1.putString("checkStatue", "5");
				bundle1.putString("itemNo", certainLossTask.getItemno() + "");
				UiManager.getInstance().changeView(DeflossBasicActivity.class, false, bundle1, true);
			}
		});
		// 联系人
		holder.contactName.setText(certainLossTask.getLinkername());
		holder.contactPhone.setText(certainLossTask.getLinkerphoneno());
		holder.contactPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

		holder.banAnPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, holder.banAnPhone.getText().toString().trim(), certainLossTask.getRegistno());
			}
		});
		holder.contactPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, holder.contactPhone.getText().toString().trim(), certainLossTask.getRegistno());
			}
		});

		// 地图
		holder.btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 导航地图 */
				double lat = Double.parseDouble(certainLossTask.getLatitude());
				double lng = Double.parseDouble(certainLossTask.getLongitude());
				AutoDriving autoDriving = new AutoDriving(context);
				autoDriving.gotoMapABC(lng, lat);
			}
		});

		return view;
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
		Button btnMap;
		Button btnRepeal;
		Button btnPrint;
		LinearLayout ll2;
		LinearLayout llTime;
		LinearLayout assuredLL;
		TextView tvCarType;
	}

}
