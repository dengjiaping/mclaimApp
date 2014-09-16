package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DBCaseInfoView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DBCaseInfoAdapter extends BaseAdapter {
	private Context context;

	public DBCaseInfoAdapter(Context context) {
		this.context = context;
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
		DBCaseInfoView caseInfoView = new DBCaseInfoView(context);
		return caseInfoView.getView();
	}

}
