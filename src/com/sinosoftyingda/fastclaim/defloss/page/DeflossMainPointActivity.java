package com.sinosoftyingda.fastclaim.defloss.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.defloss.adapter.AccidentAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.InsurearAdapter;
import com.sinosoftyingda.fastclaim.defloss.adapter.InsurearDriverAdapter;
import com.sinosoftyingda.fastclaim.defloss.view.SSAccidentView;
import com.sinosoftyingda.fastclaim.defloss.view.SSInsurearDriverView;
import com.sinosoftyingda.fastclaim.defloss.view.SSInsurearView;

/****
 * 定损的查勘要点
 * 
 * @author chenjianfan
 * 
 */
public class DeflossMainPointActivity extends BaseView {

	private View layout;
	// 标的查勘
	private ListView lvInsurear;
	private InsurearAdapter insurearAdapter;
	private SSInsurearView ssInsurearView;
	// 标的驾驶员
	private ListView lvInsurearDriver;
	private InsurearDriverAdapter driverAdapter;
	private SSInsurearDriverView ssInsurearDriverView;
	// 事故信息
	private ListView lvAccident;
	private AccidentAdapter accidentAdapter;
	private SSAccidentView ssAccidentView;

	public DeflossMainPointActivity(Context context, Bundle bundle) {
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
		layout = inflater.inflate(R.layout.survey_survey_mainpoint, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		// 初始化控件
		lvInsurear = (ListView) layout.findViewById(R.id.mainpoint_lv_insurear);
		lvInsurearDriver = (ListView) layout.findViewById(R.id.mainpoint_lv_insurear_driver);
		lvAccident = (ListView) layout.findViewById(R.id.mainpoint_lv_accident);
		setView();

	}

	private void setView() {
		// 控制控件都不能用

		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getCheckExt().size() > 0)
			for (int i = 0; i < DataConfig.defLossInfoQueryData.getCheckExt().size(); i++) {
				System.out.println("分类=" + DataConfig.defLossInfoQueryData.getCheckExt().get(i).getCheckKernelType() + "序号"
						+ DataConfig.defLossInfoQueryData.getCheckExt().get(i).getSerialNo() + "代码" + DataConfig.defLossInfoQueryData.getCheckExt().get(i).getCheckKernelCode()
						+ "  名字=" + DataConfig.defLossInfoQueryData.getCheckExt().get(i).getCheckKernelName() + "   选中="
						+ DataConfig.defLossInfoQueryData.getCheckExt().get(i).getCheckKernelSelect() + "否备注="
						+ DataConfig.defLossInfoQueryData.getCheckExt().get(i).getCheckExtRemark());
			}

		if (ssInsurearView == null) {
			ssInsurearView = new SSInsurearView(context);
			ssInsurearView.controlEd();
		}
		if (ssInsurearDriverView == null) {
			ssInsurearDriverView = new SSInsurearDriverView(context);
			ssInsurearDriverView.controlEd();

		}
		if (ssAccidentView == null) {
			ssAccidentView = new SSAccidentView(context);
			ssAccidentView.controlEd();
		}

		// 标的查勘
		if (insurearAdapter == null) {
			insurearAdapter = new InsurearAdapter(ssInsurearView);
			lvInsurear.setAdapter(insurearAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurear);
		} else {
			lvInsurear.setAdapter(insurearAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurear);
		}
		// 标的驾驶员
		if (driverAdapter == null) {
			driverAdapter = new InsurearDriverAdapter(ssInsurearDriverView);
			lvInsurearDriver.setAdapter(driverAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurearDriver);
		} else {
			lvInsurearDriver.setAdapter(driverAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurearDriver);
		}

		// 事故信息
		if (accidentAdapter == null) {
			accidentAdapter = new AccidentAdapter(ssAccidentView);
			lvAccident.setAdapter(accidentAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvAccident);
		} else {
			lvAccident.setAdapter(accidentAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvAccident);
		}

	}

	@Override
	protected void setListener() {

	}

	@Override
	public Integer getExit() {

		return ConstantValue.Page_Title_MainPoint;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	@Override
	public void onPause() {

		super.onPause();
	}

}
