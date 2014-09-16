package com.sinosoftyingda.fastclaim.work.adapter;



import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.Check;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 查勘 迭代器
 * @author haoyun 20130304
 *
 */
public class CheckAdapter extends BaseAdapter {
	private Check gCheckListValue;
	private View gView;
	public CheckAdapter(Context context,Check checkListValue)
	{
		LayoutInflater inflater =LayoutInflater.from(context);
		gView=inflater.inflate(R.layout.procedure_query_check, null);
		gCheckListValue=checkListValue;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gCheckListValue;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView checkUserCode,checkAccepTime,checkHandleTime;
		checkUserCode=(TextView)gView.findViewById(R.id.pqs_tv_check_user_code);
		checkAccepTime=(TextView)gView.findViewById(R.id.pqs_tv_check_accep_time);
		checkHandleTime=(TextView)gView.findViewById(R.id.pqs_tv_check_handle_time);
		
		checkUserCode.setText(gCheckListValue.getCheckUserCode());
		checkAccepTime.setText(gCheckListValue.getCheckAcceptTime());
		checkHandleTime.setText(gCheckListValue.getCheckHandleTime());
		return gView;
	}

}
