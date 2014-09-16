package com.sinosoftyingda.fastclaim.certainLoss.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.certainLoss.model.RepairAndChangeMsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 维修和换件列表迭代器
 * 
 * @author haoyun 20130221
 * 
 */
public class RepairAndChangeAdapter extends BaseAdapter {
	TextView tv1, tv2, tv3;
	private Context gContext;
	private List<RepairAndChangeMsg> gRcList;

	public RepairAndChangeAdapter(Context context,
			List<RepairAndChangeMsg> rcList) {
		this.gContext = context;
		this.gRcList = rcList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gRcList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gRcList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int posion, View v, ViewGroup arg2) {
		v = initLayout();
		initView(v);
		tv1.setText(gRcList.get(posion).getMsg1());
		tv2.setText(gRcList.get(posion).getMsg2());
		tv3.setText(gRcList.get(posion).getMsg3());
		return v;
	}

	/**
	 * 初始化组件
	 */
	private void initView(View v) {
		tv1 = (TextView) v.findViewById(R.id.clcii_tv_1);
		tv2 = (TextView) v.findViewById(R.id.clcii_tv_2);
		tv3 = (TextView) v.findViewById(R.id.clcii_tv_3);
	}

	/**
	 * 初始化布局
	 * 
	 * @return view
	 */
	private View initLayout() {
		LayoutInflater linflater = LayoutInflater.from(gContext);
		return linflater.inflate(
				R.layout.certain_loss_confirm_item2_item, null);
	}

}
