package com.sinosoftyingda.fastclaim.amap.drivingline;

import java.util.ArrayList;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RouteInfoActivity extends Activity {
	
	private TextView tvStartPlace, tvTargetPlace;
	
	private Intent intent;
	
	private ListView lv;
	
	private ArrayList<String> routeInfos;
	
	private Button btnMapInfo;
	private Button btnHome;
	private TextView tvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.routeinfo);
		intent = getIntent();
		tvStartPlace = (TextView)findViewById(R.id.routeinfo_tv_start);
		tvTargetPlace = (TextView)findViewById(R.id.routeinfo_tv_end);
		lv = (ListView)findViewById(R.id.routeinfo_lv);
		btnMapInfo = (Button)findViewById(R.id.routeinfo_btn_back);
		
		btnHome = (Button)findViewById(R.id.btn_home);
		tvTitle = (TextView)findViewById(R.id.tv_title);
		btnHome.setBackgroundResource(R.drawable.head_back_bg);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UiManager.getInstance().changeView(SurveyTaskActivity.class,false, null, false);
				finish();
			}
		});
		tvTitle.setText(R.string.route_info);
		btnMapInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tvStartPlace.setText(intent.getStringExtra("StartPlace"));
		tvTargetPlace.setText(intent.getStringExtra("TargetPlace"));
		routeInfos = intent.getStringArrayListExtra("routeinfos");
		if(routeInfos!=null&&routeInfos.size()>=1){
			routeInfos.remove(0);
			routeInfos.remove(routeInfos.size()-1);
			lv.setAdapter(new RouteInfoAdapter());
		}
	}

	class RouteInfoAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return routeInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return routeInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			if(view==null){
				view = LayoutInflater.from(RouteInfoActivity.this).inflate(R.layout.routeinfo_lv, null);
			}
			TextView serialno = (TextView)view.findViewById(R.id.routeinfo_lv_tv_serialno);
			TextView content = (TextView)view.findViewById(R.id.routeinfo_lv_tv_content);

			serialno.setText((position+1) + ".");
			content.setText(routeInfos.get(position));
			return view;
		}
	}
}
