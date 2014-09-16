package com.sinosoftyingda.fastclaim.survey.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.DetailTaskQueryResponse;

/***
 * 查勘基本信息中的 任务信息中的字段
 * 
 * @author chenjianfan
 * 
 */
public class SurveyTaskInfoAdapter extends BaseAdapter {

	private DetailTaskQueryResponse tblTaskQuery;
	private Context context;
	private TextView tvContacts;

	public SurveyTaskInfoAdapter(DetailTaskQueryResponse tblTaskQuery, Context context) {
		this.context = context;
		this.tblTaskQuery = tblTaskQuery;
	}

	@Override
	public int getCount() {

		return 1;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			view = View.inflate(context, R.layout.survrybasic_taskinfo_listview_item, null);
			holder.tvContacts = (TextView) view.findViewById(R.id.survrybasic_taskinfo_listview_item_linkkehu);
			holder.tvReceiveTime = (TextView) view.findViewById(R.id.survrybasic_taskinfo_listview_item_receivetime);
			holder.tvArriveTime = (TextView) view.findViewById(R.id.survrybasic_taskinfo_listview_item_arrivetime);
			holder.tvFinish = (TextView) view.findViewById(R.id.survrybasic_taskinfo_listview_item_finish);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}

		if (!SystemConfig.isUserExperience) {
			//到达现场时间
			if (TextUtils.isEmpty(tblTaskQuery.getArricesceneTime()))
				holder.tvArriveTime.setText("");
			else
				holder.tvArriveTime.setText(tblTaskQuery.getArricesceneTime());
			//任务受理时间
			if (TextUtils.isEmpty(tblTaskQuery.getTaskreceiptTime()))
				holder.tvReceiveTime.setText("");
			else
				holder.tvReceiveTime.setText(tblTaskQuery.getTaskreceiptTime());
			//联系客户时间
			if (TextUtils.isEmpty(tblTaskQuery.getLinkcustomTime()))
				holder.tvContacts.setText("");
			else
				holder.tvContacts.setText(tblTaskQuery.getLinkcustomTime());

			//任务完成时间
//			ContentValues values = DBUtils.find("checktask", new String[]{"completetime"}, "registno=?", new String[]{tblTaskQuery.getRegistNo()});
//			String completetime = values.getAsString("completetime");
			if (TextUtils.isEmpty(tblTaskQuery.getTaskHandTime()))
				holder.tvFinish.setText("");
			else
				holder.tvFinish.setText(tblTaskQuery.getTaskHandTime());

		}
		return view;
	}

	private static class viewHolder {
		TextView tvReceiveTime;
		TextView tvArriveTime;
		TextView tvContacts;
		TextView tvFinish;

	}
}
