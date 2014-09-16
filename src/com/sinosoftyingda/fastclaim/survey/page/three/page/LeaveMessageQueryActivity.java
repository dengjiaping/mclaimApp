package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimResponse;
import com.sinosoftyingda.fastclaim.common.model.LeastMsgQueryClaimItem;
import com.sinosoftyingda.fastclaim.common.model.LeaveMsgQueryClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.LeaveMsgQueryClaimResponse;
import com.sinosoftyingda.fastclaim.common.service.HistoricalClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.LeaveMessageQueryClaimHttpService;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.HistoryClaimAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.LeavemessageQueryClaimAdapter;

/***
 * 查勘基本信息中，历史赔案信息 页面
 * 
 * @author yxf
 * 
 */
public class LeaveMessageQueryActivity extends BaseView {
	private View layout;
	private List<LeastMsgQueryClaimItem> leavemessageQueryClaimBeans;
	private ListView leaveMsg_listview;
	private LeavemessageQueryClaimAdapter leavemessageQueryClaimAdapter;
	private LeaveMsgQueryClaimResponse leaveMsgQueryClaimResponse;
	// 保单号
	private String registNo;


	public LeaveMessageQueryActivity(Context context, Bundle bundle) {
		super(context, bundle);

	}

	@Override
	public View getView() {
		
		return layout;
	}

	@Override
	public Integer getType() {

		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		registNo=bundle.getString("registNo");
		// 加载布局文件
		layout = inflater.inflate(R.layout.leavemsgquery, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		TopManager.getInstance().setHeadTitle("留言查询", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
		findView();
		setData();
	}

	@Override
	public void onResume() {
		
		super.onResume();
	}
	private void setData() {

		new AsyncTask<String, Void, LeaveMsgQueryClaimResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			protected void onPostExecute(LeaveMsgQueryClaimResponse result) {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
				setView(result);

			};

			protected LeaveMsgQueryClaimResponse doInBackground(String... params) {
				LeaveMsgQueryClaimRequest request = new LeaveMsgQueryClaimRequest();
				request.setRegistNo(params[0]);
				return (LeaveMsgQueryClaimResponse) LeaveMessageQueryClaimHttpService.leaveMsgQueryClaimService(request, context.getString(R.string.http_url));
			}
		}.execute(registNo);

	}

	private void findView() {
		leaveMsg_listview=(ListView) layout.findViewById(R.id.leavemsgquery_listview);
	}

	private void setView(LeaveMsgQueryClaimResponse result) {
//				leavemessageQueryClaimAdapter = new LeavemessageQueryClaimAdapter(result.getLeaveMsgClaims(),context);
//				leaveMsg_listview.setAdapter(leavemessageQueryClaimAdapter);
			if (leavemessageQueryClaimAdapter == null) {
				leavemessageQueryClaimAdapter = new LeavemessageQueryClaimAdapter(result.getLeaveMsgClaims(), context);
				leaveMsg_listview.setAdapter(leavemessageQueryClaimAdapter);
			} else {
				leavemessageQueryClaimAdapter.setLeaveMsgClaimBeans(result.getLeaveMsgClaims());
				leavemessageQueryClaimAdapter.notifyDataSetChanged();
//				new ScrollListViewUtils().setListViewHeightBasedOnChildren(llLv);
			}
	}
	
	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {

		return ConstantValue.LeaveMessageQuery;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	@Override
	public void onPause() {
		if (leavemessageQueryClaimBeans != null)
			leavemessageQueryClaimBeans = null;
		System.gc();
		super.onPause();
	}
}
