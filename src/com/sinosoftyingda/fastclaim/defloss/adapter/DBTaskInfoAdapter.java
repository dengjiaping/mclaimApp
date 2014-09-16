package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DBTaskInfoView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DBTaskInfoAdapter extends BaseAdapter {
	private Context context;

	public DBTaskInfoAdapter(Context context) {
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
		DBTaskInfoView taskInfoView = new DBTaskInfoView(context);
		return taskInfoView.getView();
	}

}
