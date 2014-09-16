package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import com.sinosoftyingda.fastclaim.survey.page.three.page.SSInsurearDriverView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 
 * @author chenjianfan
 * 
 *         查勘要点中 标的车驾驶员字段
 * 
 *         listview 的适配器
 * 
 */
public class InsurearDriverAdapter extends BaseAdapter {
	private SSInsurearDriverView ssInsurearDriverView;

	public InsurearDriverAdapter(SSInsurearDriverView ssInsurearDriverView) {
		this.ssInsurearDriverView = ssInsurearDriverView;
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
		return ssInsurearDriverView.getView();
	}

}
