package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimItem;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/****
 * 历史赔案信息
 * 
 * @author chenjianfan
 * 
 */
public class HistoryClaimAdapter extends BaseAdapter {

	private List<HistoricalClaimItem> historyClaimBeans;
	private Context context;

	
	
	public List<HistoricalClaimItem> getHistoryClaimBeans() {
		return historyClaimBeans;
	}

	public void setHistoryClaimBeans(List<HistoricalClaimItem> historyClaimBeans) {
		this.historyClaimBeans = historyClaimBeans;
	}

	public HistoryClaimAdapter(List<HistoricalClaimItem> historyClaimBeans, Context context) {
		this.historyClaimBeans = historyClaimBeans;
		this.context = context;
	}

	@Override
	public int getCount() {
		return historyClaimBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return historyClaimBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.survey_history_insurancetype, null);
			viewHolder.tvclaimNoTitle = (TextView) view.findViewById(R.id.survey_history_in_report_title);
			viewHolder.tvclaimNo = (TextView) view.findViewById(R.id.survey_history_in_report);
			viewHolder.tvgoTime = (TextView) view.findViewById(R.id.survey_history_in_go_time);
			viewHolder.reportTime = (TextView) view.findViewById(R.id.survey_history_in_report_time);
			viewHolder.goAddress = (TextView) view.findViewById(R.id.survey_history_in_go_address);
			viewHolder.claimState = (TextView) view.findViewById(R.id.survey_history_in_claimstate);
			viewHolder.claimPrice = (TextView) view.findViewById(R.id.survey_history_in_claim_price);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		HistoricalClaimItem historyClaimBean = historyClaimBeans.get(position);
		viewHolder.tvclaimNo.setText(historyClaimBean.getRegistNo());
		viewHolder.tvgoTime.setText(historyClaimBean.getDamageDate());
		viewHolder.reportTime.setText(historyClaimBean.getRegistDate());
		viewHolder.goAddress.setText(historyClaimBean.getDamageAddress());
		if ("未结案".equals(historyClaimBean.getClaimStatus())) {
			viewHolder.claimState.setTextColor(Color.RED);
			viewHolder.claimState.setText(historyClaimBean.getClaimStatus());
			// 未结案赔款金额为空
			viewHolder.claimPrice.setText("");
		} else if ("已结案".equals(historyClaimBean.getClaimStatus())) {
			viewHolder.claimState.setTextColor(Color.GREEN);
			viewHolder.claimState.setText(historyClaimBean.getClaimStatus());
			// 赔款金额
			viewHolder.claimPrice.setText(historyClaimBean.getClaimAmount());
		}

		return view;
	}

	private static class ViewHolder {
		private TextView tvclaimNo;
		private TextView tvclaimNoTitle;
		private TextView tvgoTime;
		private TextView reportTime;
		private TextView goAddress;
		private TextView claimState;
		private TextView claimPrice;

	}

}
