package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DInfoContentView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 定损内容
 * @author JingTuo
 *
 */
public class DInfoContentAdapter extends BaseAdapter {
	private DInfoContentView dInfoContentView;

	public DInfoContentAdapter(DInfoContentView dInfoContentView) {
		this.dInfoContentView = dInfoContentView;
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
	
		return dInfoContentView.getView();
	}

}
