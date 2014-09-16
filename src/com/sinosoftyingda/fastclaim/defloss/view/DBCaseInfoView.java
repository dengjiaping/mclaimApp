package com.sinosoftyingda.fastclaim.defloss.view;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.Regist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * 定损基本信息中，报案信息内容
 * 
 * @author chenjianfan
 * 
 */
public class DBCaseInfoView {
	private Context context;
	private LayoutInflater inflater;
	private View gView;
	private TextView tvReportNo;
	private TextView tvAssured;
	private TextView tvCarNo;
	private TextView tvFactory;
	private TextView tvReasontime;// 出险时间
	private TextView tvReportTime;// 报案时间
	private TextView tvAddress;// 出险地点
	private TextView tvDriver;
	private TextView tvClaimnumber;// 出险次数
	private TextView tvStarttime;// 距保险起期
	private RelativeLayout rlClaimnumber;// 显示不显示 出险次数

	private RelativeLayout rlStarttime;// 显示不显示 距保险起期
	private TextView tvStartTimeName;
	// 报案信息
	private Regist regist;

	public DBCaseInfoView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.defloss_basic_caseinfo_item, null);
		findView();
		setView();
	}

	private void findView() {
		tvReportNo = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_baoan_no);
		tvAssured = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_assured);
		tvCarNo = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_carno);
		tvFactory = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_factory);
		tvReasontime = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_reasontime);
		tvReportTime = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_time);
		tvAddress = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_address);
		tvDriver = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_driver);
		tvClaimnumber = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_claimnumber);
		rlClaimnumber = (RelativeLayout) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_rl_claimnumber);

		rlStarttime = (RelativeLayout) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_rl_starttime);
		tvStartTimeName = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_tv_starttime);
		tvStarttime = (TextView) gView.findViewById(R.id.deflossbasic_caseinfo_listview_item_starttime);

	}

	private void setView() {
		if (DataConfig.defLossInfoQueryData != null) {
			if (DataConfig.defLossInfoQueryData.getRegist() != null) {
				regist = DataConfig.defLossInfoQueryData.getRegist();
				tvReportNo.setText(regist.getRegistNo());
				tvAssured.setText(regist.getInsuredName());
				tvCarNo.setText(regist.getLicenseNo());
				tvFactory.setText(regist.getBrandName());
				tvReasontime.setText(regist.getDispatchpTime());// 出险时间
				tvReportTime.setText(regist.getReportTime());// 报案时间
				tvAddress.setText(regist.getOutofPlace());// 出险地点
				tvDriver.setText(regist.getDriverName());

				if (regist.getDamageDayp() == null) {
					if (rlClaimnumber.getVisibility() == View.VISIBLE)
						rlClaimnumber.setVisibility(View.GONE);
				} else {
					if (rlClaimnumber.getVisibility() == View.GONE)
						rlClaimnumber.setVisibility(View.VISIBLE);
					tvClaimnumber.setText(regist.getDamageDayp());
				}

				// 距保险始期/距保险止期
				System.out.println("regist.getPromptMessage()"+regist.getPromptMessage());
				if (regist.getPromptMessage() == null) {
					if (rlStarttime.getVisibility() == View.VISIBLE)
						rlStarttime.setVisibility(View.GONE);
				} else {
					if (rlStarttime.getVisibility() == View.GONE)
						rlStarttime.setVisibility(View.VISIBLE);
					// 0 距保险始期
					if ("0".equals(regist.getPromptMessage().subSequence(0, 1))) {
						tvStartTimeName.setText("距保险始期");
						tvStarttime.setText(regist.getPromptMessage().subSequence(1, regist.getPromptMessage().length()));

					} else if ("1".equals(regist.getPromptMessage().subSequence(0, 1))) {
						tvStartTimeName.setText("距保险止期");
						tvStarttime.setText(regist.getPromptMessage().subSequence(1, regist.getPromptMessage().length()));

					} else if ("2".equals(regist.getPromptMessage().subSequence(0, 1))) {
						if (rlStarttime.getVisibility() == View.VISIBLE)
							rlStarttime.setVisibility(View.GONE);
					}

				}

			}
		}
	}
}
