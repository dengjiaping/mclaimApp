package com.sinosoftyingda.fastclaim.survey.page.three.page;

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
import com.sinosoftyingda.fastclaim.common.model.AppointModel;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgRequest;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import com.sinosoftyingda.fastclaim.common.service.PolicyHttpService;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.AppointAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.GuaranteesLipBaseAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.GuaranteesLipUnderAdapter;

/*****
 * 查看基本信息中，保险信息页面
 * 
 * @author chenjianfan
 */
public class SGuaranteeslipActivity extends BaseView {
	private View layout;

	private PolicyMsgResponse policyMsgData;
	private FastClaimDbHelper fastClaimDbHelper;
	// 备注
	private TextView tvCorrect;
	// 基本信息
	private ListView lvBasic;
	private GuaranteesLipBaseAdapter BasicBaseAdapter;
	// 承保险种信息
	private ListView lvUnderwrite;
	private GuaranteesLipUnderAdapter lipUnderAdapter;

	// 特别约定
	// Add Dengguang
	private ListView lvAppoint;
	private AppointAdapter appointAdapter;
	private AppointModel appointModel;

	// 保单号
	private String registNo;

	public SGuaranteeslipActivity(Context context, Bundle bundle) {
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
		layout = inflater.inflate(R.layout.survey_guaranteeslip, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		registNo = bundle.getString("registNo");
		fastClaimDbHelper = new FastClaimDbHelper(context);
		TopManager.getInstance().setHeadTitle("保单信息", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
		tvCorrect = (TextView) layout.findViewById(R.id.survry_guaranteeslip_correct);

		// 本保险单批改次数
		tvCorrect.setText("0");
		lvUnderwrite = (ListView) layout.findViewById(R.id.survey_guaranteeslip_underwrite_lv);
		lvAppoint = (ListView) layout.findViewById(R.id.survey_guaranteeslip_appoint_lv);
		lvBasic = (ListView) layout.findViewById(R.id.survey_guaranteeslip_basic_lv);
	}

	@Override
	public void onResume() {
		setData();
		super.onResume();
	}

	private void setData() {

		new AsyncTask<String, Void, PolicyMsgResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(PolicyMsgResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					policyMsgData = result;
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
			protected PolicyMsgResponse doInBackground(String... params) {
				PolicyMsgRequest policyMsgRequest = new PolicyMsgRequest();
				policyMsgRequest.setRegistNo(params[0]);
				return (PolicyMsgResponse) PolicyHttpService.policyMsgService(policyMsgRequest, context.getString(R.string.http_url));
			}
		}.execute(registNo);

	}

	// 初始化页面的值
	private void setView() {

		if (policyMsgData == null) {
			// 错误信息
		} else {
			tvCorrect.setText(policyMsgData.getPolicyEndOrses());
			// 基本信息数据
			if (BasicBaseAdapter == null) {
				BasicBaseAdapter = new GuaranteesLipBaseAdapter(context, policyMsgData);
				lvBasic.setAdapter(BasicBaseAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvBasic);
			} else {
				BasicBaseAdapter.setPolicyMsgResponse(policyMsgData);
				BasicBaseAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvBasic);
			}

			// 承保险种信息
			if (lipUnderAdapter == null) {
				lipUnderAdapter = new GuaranteesLipUnderAdapter(context, policyMsgData.getPolicyMsgInsuranceTypes());
				lvUnderwrite.setAdapter(lipUnderAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvUnderwrite);
			} else {
				lvUnderwrite.setAdapter(lipUnderAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvUnderwrite);
			}

			// 特别约定
			// Add-DengGuang
			if (appointAdapter == null) {
				appointAdapter = new AppointAdapter(context, policyMsgData.getAppointModels());
				lvAppoint.setAdapter(appointAdapter);
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvAppoint);
			} else {
				appointAdapter.setTypes(policyMsgData.getAppointModels());
				appointAdapter.notifyDataSetChanged();
				new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvAppoint);
			}

		}
	}

	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {

		return ConstantValue.Page_Title_Guaranteeslip;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	@Override
	public void onPause() {
		if (policyMsgData != null) {
			policyMsgData = null;
		}
		System.gc();
		super.onPause();
	}
}
