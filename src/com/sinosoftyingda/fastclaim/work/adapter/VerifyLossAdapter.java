package com.sinosoftyingda.fastclaim.work.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.VerifyLoss;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 核损 迭代器
 * @author haoyun 20130304
 *
 */
public class VerifyLossAdapter extends BaseAdapter {
	private View gView;
	private List<VerifyLoss> gVerifyLossListValue;
	private Context gContext;
	public VerifyLossAdapter(Context context,List<VerifyLoss> verifyLossListValue)
	{
		gContext=context;
		
		gVerifyLossListValue =verifyLossListValue;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gVerifyLossListValue.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gVerifyLossListValue.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int postion, View arg1, ViewGroup arg2) {
		LayoutInflater inflater =LayoutInflater.from(gContext);
		gView=inflater.inflate(R.layout.procedure_query_verifyloss, null);
		TextView itemName,certainUserCode,verifyPriceUserCode,certainHandleTime;
		itemName=(TextView)gView.findViewById(R.id.pqv_tv_item_name);
		certainUserCode=(TextView)gView.findViewById(R.id.pqv_tv_certain_user_code);
		verifyPriceUserCode=(TextView)gView.findViewById(R.id.pqv_tv_verify_loss_user_code);
		certainHandleTime=(TextView)gView.findViewById(R.id.pqv_tv_certain_handle_time);
		
		itemName.setText(gVerifyLossListValue.get(postion).getItemName());
		certainUserCode.setText(gVerifyLossListValue.get(postion).getCertainUserCode());
		verifyPriceUserCode.setText(gVerifyLossListValue.get(postion).getVerifyLossUserCode());
		certainHandleTime.setText(gVerifyLossListValue.get(postion).getVerifyPriceHandleTime());
		return gView;
	}
}
