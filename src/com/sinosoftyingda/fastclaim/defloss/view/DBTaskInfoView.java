package com.sinosoftyingda.fastclaim.defloss.view;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.TaskInfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DBTaskInfoView {

	private Context context;
	private LayoutInflater inflater;
	private View gView;
	private TextView tvReceivedTime;
	private TextView tvContactkehu;
	private TextView tvArriveTime;
	private TextView tvCompleteTime;
	private TaskInfo taskInfo;

	public DBTaskInfoView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();

	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.deflossbasic_taskinfo_listview_item, null);
		findView();
		setView();

	}

	private void findView() {
		tvReceivedTime = (TextView) gView.findViewById(R.id.deflossbasic_taskinfo_listview_item_receivedtime);
		tvContactkehu = (TextView) gView.findViewById(R.id.deflossbasic_taskinfo_listview_item_contactkehu);
		tvArriveTime = (TextView) gView.findViewById(R.id.deflossbasic_taskinfo_listview_item_arrivetime);
		tvCompleteTime = (TextView) gView.findViewById(R.id.deflossbasic_taskinfo_listview_item_completetime);
	}

	public void setView() {
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getTaskInfo() != null) {
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getTaskInfo().getTaskReceiptTime()))
				tvReceivedTime.setText(DataConfig.defLossInfoQueryData.getTaskInfo().getTaskReceiptTime());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getTaskInfo().getArrivesceneTime()))
				tvArriveTime.setText(DataConfig.defLossInfoQueryData.getTaskInfo().getArrivesceneTime());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getTaskInfo().getLinkCustomTime()))
				tvContactkehu.setText(DataConfig.defLossInfoQueryData.getTaskInfo().getLinkCustomTime());
			if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getTaskInfo().getTaskHandTime()))
				tvCompleteTime.setText(DataConfig.defLossInfoQueryData.getTaskInfo().getTaskHandTime());
			


		}

	}
}
