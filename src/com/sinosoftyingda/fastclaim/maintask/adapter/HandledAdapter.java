package com.sinosoftyingda.fastclaim.maintask.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HandledAdapter extends BaseAdapter {

	private Context context;
	private List<CheckTask> checkTasks;
	

	public List<CheckTask> getCheckTasks() {
		return checkTasks;
	}

	public void setCheckTasks(List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
	}

	public HandledAdapter(Context context, List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
		this.context = context;
	}

	public List<CheckTask> getHandleBeans() {
		return checkTasks;
	}

	public void setHandleBeans(List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
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
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			view = View.inflate(context, R.layout.maintask_group_item, null);
			holder.flBtn = (FrameLayout) view.findViewById(R.id.maintask_flout_btn);
			// 被保险人区域
			holder.assuredLL = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_3);
			holder.assuredTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_name);
			holder.carNoTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_carno);
			// 联系人
			holder.ll2 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_2);
			holder.contactImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv);
			holder.contactName = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_name);
			holder.contactPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_phone);
			// 报案信息
			holder.registNo = (TextView) view.findViewById(R.id.maintask_group_item_claimNo);
			holder.baoAnRenImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_1_imv);
			holder.banAnTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_name);
			holder.banAnPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_phone);
			// 时间
			holder.llTime = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_al);
			holder.TimeImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv_time);
			holder.TimeTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_tv_time);
			holder.btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
			holder.btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}
		holder.llTime.setVisibility(View.GONE);
		// 不显示联系人等信息
		if (holder.ll2.getVisibility() == View.VISIBLE)
			holder.ll2.setVisibility(View.GONE);
		// 按钮不显示
		holder.flBtn.setVisibility(View.GONE);
		final CheckTask checktask = checkTasks.get(position);
		System.out.println(checktask.getRegistno()+" checktask.getRegistno() "+checktask.getInsrtedname()+" checktask.getInsrtedname() "+checktask.getLicenseno()+" checktask.getLicenseno()");
		holder.registNo.setText(checktask.getRegistno());
		holder.assuredTV.setText(checktask.getInsrtedname());
		holder.carNoTV.setText(checktask.getLicenseno());
		holder.banAnTV.setText(checktask.getReportorname());
		holder.banAnPhone.setText(checktask.getReportorphoneno());
		
		

		// 跳转到任务详细页面
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SystemConfig.isOperate = false;
				
				// 已经完成处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", checktask.getRegistno());
				bundle1.putString("checkStatue", "4");
				UiManager.getInstance().changeView(SBasicActivity.class, false, bundle1, true);
			}
		});

		return view;
	}

	private static class viewHolder {
		public LinearLayout assuredLL;
		TextView registNo;
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
		FrameLayout flBtn;
		Button btnMap;
		Button btnPrint;
		Button btnRepeal;
		LinearLayout ll2;
		LinearLayout llTime;
	}
}
