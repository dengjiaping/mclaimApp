package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.AppointModel;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.survey.page.three.page.AppointInfoView;

/**
 * 特别约定
 * 
 * @author DengGuang
 * 
 */
public class AppointAdapter extends BaseAdapter implements OnItemClickListener {
	private Context context;
	private List<AppointModel> types;

	public List<AppointModel> getTypes() {
		return types;
	}

	public void setTypes(List<AppointModel> types) {
		this.types = types;
	}

	public AppointAdapter(Context context, List<AppointModel> types) {
		this.context = context;
		this.types = types;
	}

	@Override
	public int getCount() {
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		return types.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		// 类型
		TextView tvTitle;
		RelativeLayout rl;
		if (convertView == null) {
			view = View.inflate(context, R.layout.survey_guaranteeslip_appoint, null);
		} else {
			view = convertView;
		}
		tvTitle = (TextView) view.findViewById(R.id.appoint_tv_title);
		rl = (RelativeLayout) view.findViewById(R.id.survey_agreeon);
		AppointModel appointModelType = types.get(position);
		final String title = appointModelType.getClause();
		final String context = appointModelType.getClauseDetall();
		tvTitle.setText(title);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("aTitle", title);
				bundle.putString("aContext", context);

				// 跳转到详细信息页面
				UiManager.getInstance().changeView(AppointInfoView.class, false, bundle, false);
			}
		});

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {

	}

}
