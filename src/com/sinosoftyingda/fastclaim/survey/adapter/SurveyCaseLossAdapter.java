package com.sinosoftyingda.fastclaim.survey.adapter;

import com.sinosoftyingda.fastclaim.survey.page.SurveyInvolvedCaseCarView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 查勘信息中案件渉损的字段
 * 
 * @author chenjianfan
 * 
 */
public class SurveyCaseLossAdapter extends BaseAdapter {

	private SurveyInvolvedCaseCarView caseLossView;

	public SurveyCaseLossAdapter(SurveyInvolvedCaseCarView caseLossView) {
		this.caseLossView = caseLossView;
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

		return caseLossView.getView();
	}

}
