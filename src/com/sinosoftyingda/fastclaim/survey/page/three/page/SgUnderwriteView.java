package com.sinosoftyingda.fastclaim.survey.page.three.page;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgInsuranceType;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/*****
 * 查看保单的 承保险种信息
 * 
 * @author chenjianfan
 * 
 */
public class SgUnderwriteView {

	private static final String Tag = "SgUnderwriteView";
	private Context context;
	private PolicyMsgInsuranceType policyMsgInsuranceType;
	private LayoutInflater inflater;
	private View gView;

	// 控件初始化

	// 类型
	private TextView tvType;
	// 值
	private TextView tvValue;

	public SgUnderwriteView(Context context, PolicyMsgInsuranceType policyMsgInsuranceType) {
		this.context = context;
		this.policyMsgInsuranceType = policyMsgInsuranceType;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {

		gView = inflater.inflate(R.layout.survey_guaranteeslip_underwrite, null);
		findView();
		setView();
	}

	private void findView() {
		tvType = (TextView) gView.findViewById(R.id.underwrite_tv_011);
		tvValue = (TextView) gView.findViewById(R.id.underwrite_tv_01);
	}

	private void setView() {

		if (policyMsgInsuranceType != null) {
			tvType.setText(policyMsgInsuranceType.getItemKindCode());
			tvValue.setText(policyMsgInsuranceType.getItemKindAmount());
		} else {
			Log.i(Tag, "查看信息保单中的承保信息数据为空");
		}

	}
}
