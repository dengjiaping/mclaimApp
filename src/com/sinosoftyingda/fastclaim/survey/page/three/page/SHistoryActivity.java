package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.dao.TblHistoricalClaim;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimRequest;
import com.sinosoftyingda.fastclaim.common.model.HistoricalClaimResponse;
import com.sinosoftyingda.fastclaim.common.service.HistoricalClaimHttpService;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.HistoryClaimAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.model.HistoryClaimBean;

/***
 * 查勘基本信息中，历史赔案信息 页面
 * 
 * @author chenjianfan
 * 
 */
public class SHistoryActivity extends BaseView {
	private View layout;
	private List<HistoryClaimBean> historyClaimBeans;
	private FastClaimDbHelper dbHelper;
	// 历史出险次数
	private TextView tvGoNumber;
	// 历史赔款次数
	private TextView tvIndemnityNumber;
	// 历史赔款总计
	private TextView tvIndemnityPrice;
	// 承保险种信息
	private ListView llLv;
	private HistoryClaimAdapter historyClaimAdapter;

	// 保单号
	private String registNo;

	// 数据
	private HistoricalClaimResponse historicalClaimData;

	public SHistoryActivity(Context context, Bundle bundle) {
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
		// 加载布局文件
		layout = inflater.inflate(R.layout.survey_history, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		TopManager.getInstance().setHeadTitle("历史赔案明细", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
		registNo = bundle.getString("registNo");
		dbHelper = new FastClaimDbHelper(context);
		findView();

	}

	@Override
	public void onResume() {
		setData();
		super.onResume();
	}

	private void setData() {

		new AsyncTask<String, Void, HistoricalClaimResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(HistoricalClaimResponse result) {

				if ("YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					historicalClaimData = TblHistoricalClaim.getTaskQuery(dbHelper.getReadableDatabase(), registNo);
					// 初始化数据
					setView();
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			};

			@Override
			protected HistoricalClaimResponse doInBackground(String... params) {
				HistoricalClaimRequest request = new HistoricalClaimRequest();
				request.setRegistNo(params[0]);
				return (HistoricalClaimResponse) HistoricalClaimHttpService.historicalClaimService(request, context.getString(R.string.http_url));
			}
		}.execute(registNo);

	}

	private void findView() {
		tvGoNumber = (TextView) layout.findViewById(R.id.survry_history_go_number);
		tvIndemnityNumber = (TextView) layout.findViewById(R.id.survry_history_indemnity_number);
		tvIndemnityPrice = (TextView) layout.findViewById(R.id.survry_history_indemnity_price);
		llLv = (ListView) layout.findViewById(R.id.survey_history_insurancetype);
	}

	private void setView() {
		if (historicalClaimData != null) {
			tvGoNumber.setText(historicalClaimData.getHistoricalAccident());
			tvIndemnityNumber.setText(historicalClaimData.getHistoricalClaimsNumber());
			tvIndemnityPrice.setText(historicalClaimData.getHistoricalClaimsSum());
			if (historyClaimAdapter == null) {
				historyClaimAdapter = new HistoryClaimAdapter(historicalClaimData.getHistoricalClaims(), context);
				llLv.setAdapter(historyClaimAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(llLv);
			} else {
				tvGoNumber.setText(historicalClaimData.getHistoricalAccident());
				tvIndemnityNumber.setText(historicalClaimData.getHistoricalClaimsNumber());
				tvIndemnityPrice.setText(historicalClaimData.getHistoricalClaimsSum());
				historyClaimAdapter.setHistoryClaimBeans(historicalClaimData.getHistoricalClaims());
				historyClaimAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(llLv);
			}
		} else {
			// 错误信息

		}

	}

	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {

		return ConstantValue.Page_Title_History;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	@Override
	public void onPause() {
		if (historyClaimBeans != null)
			historyClaimBeans = null;

		System.gc();

		super.onPause();
	}
}
