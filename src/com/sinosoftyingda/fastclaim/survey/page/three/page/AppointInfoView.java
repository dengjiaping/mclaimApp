package com.sinosoftyingda.fastclaim.survey.page.three.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;

/**
 * 特别约定
 * 
 * @author DengGuang
 *
 */
public class AppointInfoView extends BaseView {

	private View gView;
	private TextView gTvTitle;
	private TextView gTvContext;
	
	public AppointInfoView(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return gView;
	}

	@Override
	public Integer getExit() {
		return null;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		
		gView = inflater.inflate(R.layout.survey_appoint, null);
		gTvTitle = (TextView)gView.findViewById(R.id.appoint_tv_title);
		gTvContext = (TextView)gView.findViewById(R.id.appoint_tv_content);
	
		String title = (String) bundle.get("aTitle");
		String context = (String) bundle.get("aContext");
		context = context.replace("&lt;br&gt;", "");
		context = context.replace("<br>", "\n");
		gTvTitle.setText(title);
		gTvContext.setText("\t"+context);
	}

	@Override
	protected void setListener() {

	}
}
