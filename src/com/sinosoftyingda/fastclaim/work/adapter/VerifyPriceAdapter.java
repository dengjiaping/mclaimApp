package com.sinosoftyingda.fastclaim.work.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.VerifyPrice;
/**
 * 核价 迭代器
 * @author haoyun 20130304
 *
 */
public class VerifyPriceAdapter extends BaseAdapter {
	private View gView;
	private List<VerifyPrice> gVerifyPriceListValue;
	private Context gContext;
	public VerifyPriceAdapter(Context context,List<VerifyPrice> verifyPriceListValue)
	{
		gContext=context;
		gVerifyPriceListValue =verifyPriceListValue;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gVerifyPriceListValue.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gVerifyPriceListValue.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int postion, View arg1, ViewGroup arg2) {
		LayoutInflater inflater =LayoutInflater.from(gContext);
		gView=inflater.inflate(R.layout.procedure_query_verifyprice, null);
		TextView itemName,certainUserCode,verifyPriceUserCode,certainHandleTime;
		itemName=(TextView)gView.findViewById(R.id.pqv_tv_item_name);
		certainUserCode=(TextView)gView.findViewById(R.id.pqv_tv_certain_user_code);
		verifyPriceUserCode=(TextView)gView.findViewById(R.id.pqv_tv_verify_price_user_code);
		certainHandleTime=(TextView)gView.findViewById(R.id.pqv_tv_certain_handle_time);
		
		itemName.setText(gVerifyPriceListValue.get(postion).getItemName());
		certainUserCode.setText(gVerifyPriceListValue.get(postion).getCertainUserCode());
		verifyPriceUserCode.setText(gVerifyPriceListValue.get(postion).getVerifypriceUserCode());
		certainHandleTime.setText(gVerifyPriceListValue.get(postion).getVerifyPriceHandleTime());
		return gView;
	}

}
