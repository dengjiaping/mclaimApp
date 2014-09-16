package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DInfoObjectView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DInfoObjectAdapter extends BaseAdapter {
	private DInfoObjectView dInfoObjectView;

	public DInfoObjectAdapter(DInfoObjectView dInfoObjectView) {
		this.dInfoObjectView = dInfoObjectView;
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
		return dInfoObjectView.getView();
	}

}
