package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.AccidentAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.InsurearAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.InsurearDriverAdapter;

/****
 * 查勘要点
 * 
 * @author chenjianfan
 * 
 */
public class SSMainPointActivity extends BaseView {

	private View layout;
	// 标的查勘
	private ListView lvInsurear;
	private InsurearAdapter insurearAdapter;
	private SSInsurearView insurearView;
	private List<CheckExt> listInsurear;
	// 标的驾驶员
	private ListView lvInsurearDriver;
	private InsurearDriverAdapter driverAdapter;
	private SSInsurearDriverView DriverView;
	private List<CheckExt> listDrive;
    private FastClaimDbHelper claimDbHelper;
	// 事故信息
	private ListView lvAccident;
	private AccidentAdapter accidentAdapter;
	private SSAccidentView accidentView;
	private List<CheckExt> listAccident;

	public SSMainPointActivity(Context context, Bundle bundle) {
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
		claimDbHelper=new FastClaimDbHelper(context);
		setView();
		DataData();
		setViewData();
	}

	private void DataData() {

	}

	// 初始化数据
	private void setView() {

		if (DataConfig.tblTaskQuery.getCheckExtList().size() > 0)
			for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
				System.out.println("进入页面分类=" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelType() + "序号"
						+ DataConfig.tblTaskQuery.getCheckExtList().get(i).getSerialNo() + "代码" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode() + "  名字="
						+ DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelName() + "   选中="
						+ DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelSelect() + "否备注=" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckExtRemark());
			}
	}

	private void setViewData() {
		// 标的查勘
		if (insurearView == null) {
			insurearView = new SSInsurearView(context);
		}
		if (insurearAdapter == null) {
			insurearAdapter = new InsurearAdapter(insurearView);
			lvInsurear.setAdapter(insurearAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurear);
		} else {
			lvInsurear.setAdapter(insurearAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurear);
		}
		// 标的驾驶员
		if (DriverView == null)
			DriverView = new SSInsurearDriverView(context);
		if (driverAdapter == null) {
			driverAdapter = new InsurearDriverAdapter(DriverView);
			lvInsurearDriver.setAdapter(driverAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurearDriver);
		} else {
			lvInsurearDriver.setAdapter(driverAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvInsurearDriver);
		}
		// 事故信息
		if (accidentView == null)
			accidentView = new SSAccidentView(context);
		if (accidentAdapter == null) {
			accidentAdapter = new AccidentAdapter(accidentView);
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

		// 离线页面的时候，获取查勘要点的值
		listInsurear = insurearView.getPageCheckExts();
		listDrive = DriverView.getPageCheckExts();
		listAccident = accidentView.getPageCheckExts();
		for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
			System.out.println("分类=" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelType() + "序号"
					+ DataConfig.tblTaskQuery.getCheckExtList().get(i).getSerialNo() + "代码" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode() + "  名字="
					+ DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelName() + "   选中=" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelSelect()
					+ "否备注=" + DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckExtRemark());
		}
		TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true,false,true);
		DataConfig.tblTaskQuery=TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());

		super.onPause();
	}

}
