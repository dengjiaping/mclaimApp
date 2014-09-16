package com.sinosoftyingda.fastclaim.maintask.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.drivingline.AutoDriving;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.utils.MainTaskUtils;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;

public class HandleingAdapter extends BaseAdapter {

	private Context context;
	private List<CheckTask> checkTasks;
	private boolean ReceiverFlage = true;

	public List<CheckTask> getCheckTasks() {
		return checkTasks;
	}

	public void setCheckTasks(List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
	}

	public HandleingAdapter(Context context, List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
		this.context = context;
	}

	@Override
	public int getCount() {

		return checkTasks.size();
	}

	@Override
	public Object getItem(int position) {

		return checkTasks.get(position);
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
			// 被保险人区域
			holder.assuredLL = (LinearLayout) view
					.findViewById(R.id.maintask_group_item_ll_3);
			// 联系人
			holder.ll2 = (LinearLayout) view
					.findViewById(R.id.maintask_group_item_ll_2);
			holder.contactImv = (ImageView) view
					.findViewById(R.id.maintask_group_item_ll_2_imv);
			holder.contactName = (TextView) view
					.findViewById(R.id.maintask_group_item_ll_2_name);
			holder.contactPhone = (TextView) view
					.findViewById(R.id.maintask_group_item_ll_2_phone);
			// 报案信息
			holder.registNo = (TextView) view
					.findViewById(R.id.maintask_group_item_claimNo);
			holder.baoAnRenImv = (ImageView) view
					.findViewById(R.id.maintask_group_item_ll_1_imv);
			holder.banAnTV = (TextView) view
					.findViewById(R.id.maintask_group_item_ll_1_name);
			holder.banAnPhone = (TextView) view
					.findViewById(R.id.maintask_group_item_ll_1_phone);
			// 时间
			holder.llTime = (LinearLayout) view
					.findViewById(R.id.maintask_group_item_ll_al);
			holder.TimeImv = (ImageView) view
					.findViewById(R.id.maintask_group_item_ll_2_imv_time);
			holder.TimeTV = (TextView) view
					.findViewById(R.id.maintask_group_item_ll_2_tv_time);
			holder.btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
			holder.btnPrint = (Button) view
					.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view
					.findViewById(R.id.maintask_btn_repeal);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}
		holder.llTime.setVisibility(View.GONE);
		// 被保险人信息不显示
		holder.assuredLL.setVisibility(View.GONE);
		holder.btnPrint.setVisibility(View.GONE);
		holder.btnRepeal.setVisibility(View.GONE);
		holder.btnMap.setVisibility(View.VISIBLE);
			
		final CheckTask checktask = checkTasks.get(position);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SystemConfig.isOperate = true;

				// 正在处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", checktask.getRegistno());
				bundle1.putString("checkStatue", "2");
				UiManager.getInstance().changeView(SBasicActivity.class, false,
						bundle1, true);
			}
		});

		// 如果报案人和联系人相同
		if (checktask.getReportorname().equals(checktask.getLinkername())) {
			// 报案人信息不显示
			holder.baoAnRenImv.setVisibility(View.GONE);
			holder.banAnTV.setVisibility(View.GONE);
			holder.banAnPhone.setVisibility(View.GONE);

		} else {
			holder.baoAnRenImv.setVisibility(View.VISIBLE);
			holder.banAnTV.setVisibility(View.VISIBLE);
			holder.banAnPhone.setVisibility(View.VISIBLE);
			holder.banAnTV.setText(checktask.getReportorname());
			holder.banAnPhone.setText(checktask.getReportorphoneno());
			holder.banAnPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		}
		holder.registNo.setText(checktask.getRegistno());
		holder.contactName.setText(checktask.getLinkername());
		holder.contactPhone.setText(checktask.getLinkerphoneno());
		holder.contactPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		CheckTask checkTask2 = CheckTaskAccess.findByRegistno(checktask
				.getRegistno());
		
		
		
		if ("".equals(checkTask2.getContacttime())) {
			holder.contactPhone.setTextColor(Color.GRAY);
			holder.banAnPhone.setTextColor(Color.GRAY);
		} else {
			holder.contactPhone.setTextColor(Color.RED);
			holder.banAnPhone.setTextColor(Color.RED);

		}
		// 拨打电话
		holder.banAnPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, holder.banAnPhone.getText().toString().trim(), checktask.getRegistno());
				/*
				if (ReceiverFlage) {
					phoneStateReceiver.registerBroad(context, phoneStateReceiver);
					ReceiverFlage = false;
				}
				if (phoneStateReceiver != null) {
					phoneStateReceiver.setRegistno(checktask.getRegistno());
				}
				*/
			}
		});
		holder.contactPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainTaskUtils.CallPhone(context, holder.contactPhone.getText().toString().trim(), checktask.getRegistno());
				/*
				if (ReceiverFlage) {
					phoneStateReceiver.registerBroad(context,
							phoneStateReceiver);
					ReceiverFlage = false;
				}
				if (phoneStateReceiver != null) {
					phoneStateReceiver.setRegistno(checktask.getRegistno());
				}
				*/
			}
		});
		// 地图
		holder.btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 导航地图 */
				double lat = Double.parseDouble(checktask.getLatitude());
				double lng = Double.parseDouble(checktask.getLongitude());
				AutoDriving autoDriving = new AutoDriving(context);
				autoDriving.gotoMapABC(lng, lat);
			}
		});
		return view;
	}

	private static class viewHolder {
		TextView registNo;
		ImageView baoAnRenImv;
		TextView banAnTV;
		TextView banAnPhone;
		ImageView contactImv;
		TextView contactName;
		TextView contactPhone;
		ImageView TimeImv;
		TextView TimeTV;
		Button btnMap;
		Button btnPrint;
		Button btnRepeal;
		LinearLayout ll2;
		LinearLayout llTime;
		LinearLayout assuredLL;
	}

}
