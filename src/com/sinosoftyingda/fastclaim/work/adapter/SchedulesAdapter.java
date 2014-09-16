package com.sinosoftyingda.fastclaim.work.adapter;


import com.sinosoftyingda.fastclaim.R;

import com.sinosoftyingda.fastclaim.common.model.Schedule;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 调度 迭代器
 * @author haoyun 20130304
 *
 */
public class SchedulesAdapter extends BaseAdapter {
	private Schedule gSchedulesListValue;
	private View gView;
	public SchedulesAdapter(Context context,Schedule schedulesListValue)
	{
		LayoutInflater inflater =LayoutInflater.from(context);
		gView=inflater.inflate(R.layout.procedure_query_schedule, null);
		gSchedulesListValue=schedulesListValue;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gSchedulesListValue;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView scheduleUserCode,scheduleInputTime;
		scheduleUserCode=(TextView)gView.findViewById(R.id.pqs_tv_schedule_user_code);
		scheduleInputTime=(TextView)gView.findViewById(R.id.pqs_tv_schedule_input_time);
		scheduleUserCode.setText(gSchedulesListValue.getScheduleuserCode());
		scheduleInputTime.setText(gSchedulesListValue.getScheduleinputTime());
		return gView;
	}

}
