package com.sinosoftyingda.fastclaim.notice.page;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.login.page.LoginView;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;

import android.app.Activity;
import android.os.Bundle;

/**
 * 任务通知
 * @author JingTuo
 */
public class NoticeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// UiManager.getInstance().changeView(SurveyTaskActivity.class,
		// false,false, savedInstanceState, false);
		Bundle bundle=new Bundle();
		bundle.putString("11111111", "11111111");
		if(SystemConfig.isExit){
			UiManager.getInstance().changeView(SurveyTaskActivity.class,bundle, false);
		}else{
			UiManager.getInstance().changeView(LoginView.class,false,bundle, false);
		}
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}
}
