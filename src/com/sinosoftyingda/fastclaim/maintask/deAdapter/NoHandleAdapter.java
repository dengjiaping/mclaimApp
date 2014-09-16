package com.sinosoftyingda.fastclaim.maintask.deAdapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoHandleAdapter extends BaseAdapter {

	private List<View> views;
	public List<View> getViews() {
		return views;
	}

	public void setViews(List<View> views) {
		this.views = views;
	}

	public NoHandleAdapter(List<View> views) {
		this.views = views;
	}

	public List<View> getHandleBeans() {
		return views;
	}

	public void setHandleBeans(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object getItem(int position) {
		return views.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return views.get(position);
	}


}
