package com.sinosoftyingda.fastclaim.survey.adapter;

import com.sinosoftyingda.fastclaim.survey.page.SContactView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/*****
 * 查勘信息中联系信息的字段
 * 
 * @author chenjianfan
 * 
 */
public class SurveyContactAdapter extends BaseAdapter {

	private SContactView sContactView;

	public SurveyContactAdapter(SContactView sContactView) {

		this.sContactView = sContactView;
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

		return sContactView.getView();
	}

}
