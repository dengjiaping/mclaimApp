package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgInsuranceType;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GuaranteesLipUnderAdapter extends BaseAdapter {
	private Context context;
	private List<PolicyMsgInsuranceType> types;

	public GuaranteesLipUnderAdapter(Context context, List<PolicyMsgInsuranceType> types) {
		this.context = context;
		this.types = types;
	}

	@Override
	public int getCount() {
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		return types.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		// 类型
		TextView tvType;
		// 值
		TextView tvValue;
		if (convertView == null) {
			view = View.inflate(context, R.layout.survey_guaranteeslip_underwrite, null);
		}
		tvType = (TextView) view.findViewById(R.id.underwrite_tv_011);
		tvValue = (TextView) view.findViewById(R.id.underwrite_tv_01);
		PolicyMsgInsuranceType policyMsgInsuranceType = types.get(position);
		tvType.setText(policyMsgInsuranceType.getItemKindCode());
		tvValue.setText(policyMsgInsuranceType.getItemKindAmount());
		return view;
	}
}
