package com.sinosoftyingda.fastclaim.defloss.adapter;

import com.sinosoftyingda.fastclaim.defloss.view.DInfoFactoryView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 
 * @author jianfan
 * 
 */
public class DInfoFactoryAdapter extends BaseAdapter {
	private DInfoFactoryView dInfoFactoryView;

	public DInfoFactoryAdapter(DInfoFactoryView dInfoFactoryView) {
		this.dInfoFactoryView = dInfoFactoryView;
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
		return dInfoFactoryView.getView();
	}

}
