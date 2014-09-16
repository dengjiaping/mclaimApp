package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SGuaranteeslipView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 查看基本信息中的 保单信息
 * 
 * @author chenjianfan
 * 
 */
public class GuaranteesLipBaseAdapter extends BaseAdapter {

	private PolicyMsgResponse policyMsgResponse;
	private Context context;
	
	

	public PolicyMsgResponse getPolicyMsgResponse() {
		return policyMsgResponse;
	}


	public void setPolicyMsgResponse(PolicyMsgResponse policyMsgResponse) {
		this.policyMsgResponse = policyMsgResponse;
	}


	public GuaranteesLipBaseAdapter(Context context, PolicyMsgResponse policyMsgResponse) {
		this.context = context;
		this.policyMsgResponse = policyMsgResponse;
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
		SGuaranteeslipView guaranteeslipView = new SGuaranteeslipView(context, policyMsgResponse);
		return guaranteeslipView.getView();
	}

}
