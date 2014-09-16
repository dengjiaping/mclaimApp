package com.sinosoftyingda.fastclaim.more.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.splash.service.SplashService;

public class AboutActivity extends BaseView {
	private View layout;
	private TextView tvVersion;
	

	public AboutActivity(Context context, Bundle bundle) {
		super(context, bundle);
		
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		layout = inflater.inflate(R.layout.about, null);
		LinearLayout.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		findView();
	}

	private void findView() {
		Log.d("更多页面传值", bundle.getString("aa"));
		tvVersion = (TextView) layout.findViewById(R.id.about_tv_version);
		tvVersion.setText("版本号:" + SplashService.getVersion(context));

	}

	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {
		
		return ConstantValue.Page_Title_About;
	}

	@Override
	public Integer getBackMain() {
		
		return null;
	}
}
