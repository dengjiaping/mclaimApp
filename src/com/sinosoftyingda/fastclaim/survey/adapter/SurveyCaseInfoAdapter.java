package com.sinosoftyingda.fastclaim.survey.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;

/*****
 * 查勘基本信息中的 报案信息的子段
 * 
 * @author chenjianfan
 * 
 */
public class SurveyCaseInfoAdapter extends BaseAdapter {

	private DetailTaskQueryResponse tblTaskQuery;
	private Context context;

	public SurveyCaseInfoAdapter(DetailTaskQueryResponse tblTaskQuery, Context context) {
		this.context = context;
		this.tblTaskQuery = tblTaskQuery;
	}

	@Override
	public int getCount() {

		return 1;
	}

	@Override
	public Object getItem(int position) {

		return position;
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
			view = View.inflate(context, R.layout.survrybasic_caseinfo_listview_item, null);
			holder.tvReportNo = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_baoan_no);
			holder.tvAssured = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_assured);
			holder.tvCarNo = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_carno);
			holder.tvFactory = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_factory);
			holder.tvReasonTime = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_reason);
			holder.tvTime = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_time);
			holder.tvAddress = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_address);
			holder.tvDriver = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_driver);
			holder.tvStarttime = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_starttime);

			holder.tvClaimNumberName = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_tv_starttime);
			holder.rlClaimNumber = (RelativeLayout) view.findViewById(R.id.survrybasic_caseinfo_listview_item_tv_starttime_ll);
			holder.tvClaimNumber = (TextView) view.findViewById(R.id.survrybasic_caseinfo_listview_item_claimnumber);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}

		if (!SystemConfig.isUserExperience) {
			holder.tvReportNo.setText(tblTaskQuery.getRegistNo());
			holder.tvAssured.setText(tblTaskQuery.getInsrtedName());
			holder.tvCarNo.setText(tblTaskQuery.getLicenseNo());
			holder.tvFactory.setText(tblTaskQuery.getBrandName());
			holder.tvReasonTime.setText(tblTaskQuery.getDispatchptime());
			holder.tvTime.setText(tblTaskQuery.getReporttime());
			holder.tvAddress.setText(tblTaskQuery.getDispatchplace());
			holder.tvDriver.setText(tblTaskQuery.getDrivername());
			holder.tvClaimNumber.setText(tblTaskQuery.getDamageDayp().equals("") ? "" : tblTaskQuery.getDamageDayp());

			// 距保险始期/距保险止期
			System.out.println("tblTaskQuery.getPromptMessage()" + tblTaskQuery.getPromptmessage());
			if (tblTaskQuery.getPromptmessage() == null) {
				if (holder.rlClaimNumber.getVisibility() == View.VISIBLE)
					holder.rlClaimNumber.setVisibility(View.GONE);
			} else {
				if (holder.rlClaimNumber.getVisibility() == View.GONE)
					holder.rlClaimNumber.setVisibility(View.VISIBLE);

				if (tblTaskQuery.getPromptmessage() != null) {
					// 0 距保险始期
					if ("0".equals(tblTaskQuery.getPromptmessage().subSequence(0, 1))) {
						holder.tvClaimNumberName.setText("距保险起期");
						holder.tvStarttime.setText(tblTaskQuery.getPromptmessage().subSequence(1, tblTaskQuery.getPromptmessage().length()));
					} else if ("1".equals(tblTaskQuery.getPromptmessage().subSequence(0, 1))) {
						holder.tvClaimNumberName.setText("距保险止期");
						holder.tvStarttime.setText(tblTaskQuery.getPromptmessage().subSequence(1, tblTaskQuery.getPromptmessage().length()));
					} else if ("2".equals(tblTaskQuery.getPromptmessage().subSequence(0, 1))) {
						if (holder.rlClaimNumber.getVisibility() == View.VISIBLE)
							holder.rlClaimNumber.setVisibility(View.GONE);
					}
				} else {
					holder.tvClaimNumberName.setText("空");
					holder.tvStarttime.setText("空");
				}

			}

		}
		return view;
	}

	private static class viewHolder {
		TextView tvReportNo;
		TextView tvAssured;
		TextView tvCarNo;
		TextView tvFactory;
		TextView tvReasonTime;// 出险时间
		TextView tvTime;
		TextView tvAddress;
		TextView tvDriver;
		TextView tvStarttime;
		TextView tvClaimNumber;
		RelativeLayout rlClaimNumber;
		TextView tvClaimNumberName;

	}

}
