package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import com.sinosoftyingda.fastclaim.survey.page.three.page.SSInsurearView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 
 * @author chenjianfan
 * 
 *         查勘要点中 标的车查勘字段
 *         listview 的适配器
 * 
 */
public class InsurearAdapter extends BaseAdapter {
	private SSInsurearView ssInsurearView;
	public InsurearAdapter(SSInsurearView ssInsurearView) {
		this.ssInsurearView = ssInsurearView;
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

		return ssInsurearView.getView();
	}

}
