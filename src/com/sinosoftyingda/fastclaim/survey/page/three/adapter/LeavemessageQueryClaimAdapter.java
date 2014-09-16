package com.sinosoftyingda.fastclaim.survey.page.three.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.LeastMsgQueryClaimItem;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeavemessageQueryClaimAdapter extends BaseAdapter {
	private List<LeastMsgQueryClaimItem> leaveMsgClaimBeans;
	private Context context;
	
	public List<LeastMsgQueryClaimItem> getLeaveMsgClaimBeans() {
		return leaveMsgClaimBeans;
	}

	public void setLeaveMsgClaimBeans(
			List<LeastMsgQueryClaimItem> leaveMsgClaimBeans) {
		this.leaveMsgClaimBeans = leaveMsgClaimBeans;
	}

	@Override
	public int getCount() {
		return leaveMsgClaimBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return leaveMsgClaimBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.leavemsgquery_listview, null);
			viewHolder.registno = (TextView) view.findViewById(R.id.leaveMsg_registno_title_value);
			viewHolder.submitTime = (TextView) view.findViewById(R.id.leaveMsg_submittime_value);
			viewHolder.leaveMsgnodeType = (TextView) view.findViewById(R.id.leaveMsg_nodeType_value);
			viewHolder.leaveMsgName = (TextView) view.findViewById(R.id.leaveMsg_personName_value);
			viewHolder.LeaveMsgContent = (TextView) view.findViewById(R.id.leaveMsg_content_value);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		LeastMsgQueryClaimItem leaveMsgClaimBean = leaveMsgClaimBeans.get(position);
		viewHolder.registno.setText(leaveMsgClaimBean.getRegistNo());
		viewHolder.submitTime.setText(leaveMsgClaimBean.getSubmitTimes());
		viewHolder.leaveMsgnodeType.setText(leaveMsgClaimBean.getNodeType());
		viewHolder.leaveMsgName.setText(leaveMsgClaimBean.getLeaveMsgPersonName());
		viewHolder.LeaveMsgContent.setText(leaveMsgClaimBean.getLeaveMsgContent());
		return view;
	}
	public LeavemessageQueryClaimAdapter(List<LeastMsgQueryClaimItem> leaveMsgClaimBeans, Context context) {
		this.leaveMsgClaimBeans = leaveMsgClaimBeans;
		this.context = context;
//		paixu(leaveMsgClaimBeans);
		
	}
	private static class ViewHolder {
		private TextView registno;
		private TextView submitTime;
		private TextView leaveMsgnodeType;
		private TextView leaveMsgName;
		private TextView LeaveMsgContent;

	}
	
//	public List<LeastMsgQueryClaimItem> paixu(List<LeastMsgQueryClaimItem> leaveMsgClaimBeans){
//		List<LeastMsgQueryClaimItem> leaveMsgClaimss=new ArrayList<LeastMsgQueryClaimItem>();
//		int listsize=leaveMsgClaimBeans.size();
//		for(int i=0;i<listsize;i++){
//			for(int j=0;j<listsize;j++){
//				if(){
//					
//				}
//				leaveMsgClaimss.add(leaveMsgClaimBeans.get(j));
//			}
//		}
//		return leaveMsgClaimBeans;
//	}
//	
	
	
	
	
}
