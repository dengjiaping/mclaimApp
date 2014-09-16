package com.sinosoftyingda.fastclaim.maintask.utils;

import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/*****
 * 刷新界面
 * 
 * @author jianfan
 * 
 */
public class MainShuaXinActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		String str = intent.getStringExtra("com.maclaim");
		if ("1".equals(str)) {
			UiManager.getInstance().changeView(SurveyTaskActivity.class, null, true);
		} else if ("2".equals(str)) {
			UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
		}
		finish();

		super.onCreate(savedInstanceState);
	}
}
