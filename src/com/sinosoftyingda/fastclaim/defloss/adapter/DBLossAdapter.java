package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DBLossView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DBLossAdapter extends BaseAdapter {
	private Context context;

	public DBLossAdapter(Context context) {
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

		DBLossView dbLossView = new DBLossView(context);
		return dbLossView.gView();
	}

}
