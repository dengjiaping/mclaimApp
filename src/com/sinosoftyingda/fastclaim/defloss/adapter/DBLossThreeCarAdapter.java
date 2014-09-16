package com.sinosoftyingda.fastclaim.defloss.adapter;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.defloss.bean.ThreeCarBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DBLossThreeCarAdapter extends BaseAdapter {

	private Context context;
	private List<ThreeCarBean> threeCarBeans;

	public DBLossThreeCarAdapter(Context context, List<ThreeCarBean> threeCarBeans) {
		this.threeCarBeans = threeCarBeans;
		this.context = context;
	}

	@Override
	public int getCount() {

		return threeCarBeans.size();
	}

	@Override
	public Object getItem(int position) {

		return threeCarBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view;
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.defloss_basic_loss_listview_item, null);
			holder.tvTitle = (TextView) view.findViewById(R.id.defloss_basic_loss_lv_item_threecar_title);
			holder.tvCarNo = (TextView) view.findViewById(R.id.defloss_basic_loss_lv_item_threecar_carno);
			holder.tvDeflossName = (TextView) view.findViewById(R.id.defloss_basic_loss_lv_item_threecar_deflossname);
			holder.tvDeflossPrice = (TextView) view.findViewById(R.id.defloss_basic_loss_lv_item_threecar_deflossprice);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		ThreeCarBean threeCarBean=threeCarBeans.get(position);
		holder.tvTitle.setText(threeCarBean.getThreeCarTitle());
		holder.tvCarNo.setText(threeCarBean.getThreeCarCarNo());
		holder.tvDeflossName.setText(threeCarBean.getDeflossName());
		holder.tvDeflossPrice.setText(threeCarBean.getDeflossPrice());
		return view;
	}

	private static class ViewHolder {
		TextView tvTitle;
		TextView tvCarNo;
		TextView tvDeflossName;
		TextView tvDeflossPrice;
	}

}
