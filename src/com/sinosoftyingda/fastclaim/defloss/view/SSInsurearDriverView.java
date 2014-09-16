package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.ArrayList;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/****
 * 查勘要点中 标的车驾驶员字段
 * 
 * @author chenjianfan
 * 
 */
public class SSInsurearDriverView {

	private ArrayList<CheckExt> checkExts;

	private LayoutInflater inflater;
	private View gView;
	// 是否为报案驾驶员
	private MySlipSwitch msBaoanDriver;
	private EditText etBaoanDriver;
	// 是否为允许的驾驶员
	private MySlipSwitch msAllowdDriver;
	// 准驾车型是否相符
	private MySlipSwitch msModelstype;
	// 驾驶证是否有效
	private MySlipSwitch msValidDriver;
	// 是否酒后驾车
	private MySlipSwitch msDrunkDriving;

	private CommonUtils commonUtils;

	public SSInsurearDriverView(Context context) {
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.survey_survey_mainpoint_driver, null);
		findView();
		setView();

	}

	private void setView() {
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getCheckExt() != null && !DataConfig.defLossInfoQueryData.getCheckExt().isEmpty()) {
			// 集合对象不等于空并且集合有数据
			checkExts = (ArrayList<CheckExt>) DataConfig.defLossInfoQueryData.getCheckExt();
			for (int i = 0; i < checkExts.size(); i++) {
				if (commonUtils == null) {
					commonUtils = new CommonUtils();
				}
				// 标的车驾驶员
				if ("CheckExt06".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msBaoanDriver, etBaoanDriver, checkExts.get(i));
				}
				if ("CheckExt07".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msAllowdDriver, checkExts.get(i));
				}
				if ("CheckExt08".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msModelstype, checkExts.get(i));
				}
				if ("CheckExt09".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msValidDriver, checkExts.get(i));
				}
				if ("CheckExt10".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msDrunkDriving, checkExts.get(i));
				}
			}

		}

		// 控件不可操作
		msBaoanDriver.setEnabled(false);
		msDrunkDriving.setEnabled(false);
		msModelstype.setEnabled(false);
		msValidDriver.setEnabled(false);
		etBaoanDriver.setEnabled(false);
		msAllowdDriver.setEnabled(false);

	}

	private void findView() {
		etBaoanDriver = (EditText) gView.findViewById(R.id.driver_ed_baoandriver);
		msBaoanDriver = (MySlipSwitch) gView.findViewById(R.id.driver_tv_baoandriver_btn);
		msAllowdDriver = (MySlipSwitch) gView.findViewById(R.id.driver_tv_allowdriver_btn);
		msModelstype = (MySlipSwitch) gView.findViewById(R.id.driver_tv_modelstype_btn);
		msValidDriver = (MySlipSwitch) gView.findViewById(R.id.driver_tv_validdriverlicense_btn);
		msDrunkDriving = (MySlipSwitch) gView.findViewById(R.id.driver_tv_drunkdriving_btn);
		msBaoanDriver.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		msAllowdDriver.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		msModelstype.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		msValidDriver.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		msDrunkDriving.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);

	}

	public void controlEd() {
		msBaoanDriver.setEnabled(false);
		msDrunkDriving.setEnabled(false);
		msModelstype.setEnabled(false);
		msValidDriver.setEnabled(false);
		etBaoanDriver.setEnabled(false);
		msAllowdDriver.setEnabled(false);
	}

}
