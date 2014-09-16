package com.sinosoftyingda.fastclaim.survey.adapter;

import com.sinosoftyingda.fastclaim.survey.page.SPointsView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/*****
 * 查勘信息中查勘记录的子段
 * 
 * @author chenjianfan
 * 
 */
public class SurveyRecordAdapter extends BaseAdapter {

	private SPointsView sPointsView;

	public SurveyRecordAdapter(SPointsView sPointsView) {
		this.sPointsView = sPointsView;

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

		// SPointsViewParam sPointsViewParam = pointsViewParams.get(position);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("SPointsViewParam", sPointsViewParam);
		// SPointsView sPointsView = new SPointsView(context, bundle);
		return sPointsView.getView();
	}
}
