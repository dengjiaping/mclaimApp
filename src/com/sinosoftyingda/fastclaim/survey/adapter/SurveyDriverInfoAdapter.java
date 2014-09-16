package com.sinosoftyingda.fastclaim.survey.adapter;

import com.sinosoftyingda.fastclaim.survey.page.three.page.DriverInfoView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SurveyDriverInfoAdapter extends BaseAdapter {

	private DriverInfoView driverInfoView;

	public SurveyDriverInfoAdapter(DriverInfoView driverInfoView) {
		this.driverInfoView = driverInfoView;
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
		return driverInfoView.getView();
	}

}
