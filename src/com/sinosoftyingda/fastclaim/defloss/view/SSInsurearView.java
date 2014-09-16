package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/***
 * 查勘要点 中 标的查勘字段
 * 
 * @author chenjianfan
 */
public class SSInsurearView {
	private List<CheckExt> checkExts;
	private LayoutInflater inflater;
	private CommonUtils commonUtils;
	public View gView;
	// 车牌号是否相符
	private MySlipSwitch btnCarNo;
	private EditText edCarNo;
	// 发动机是否相符
	private MySlipSwitch btnEngine;
	private EditText edEngine;
	// 车架号是否相符
	private MySlipSwitch btnChassis;
	private EditText edChassis;
	// 使用性质是否相符
	private MySlipSwitch btnUseType;
	// 行使张是否有效
	private MySlipSwitch btnDriver;

	public View getView() {
		return gView;
	}

	public SSInsurearView(Context context) {
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		gView = inflater.inflate(R.layout.survey_survey_mainpoint_insurear, null);
		findView();
		setView();
	}

	private void setView() {
		if (DataConfig.defLossInfoQueryData != null) {
			if (DataConfig.defLossInfoQueryData.getCheckExt() != null && !DataConfig.defLossInfoQueryData.getCheckExt().isEmpty()) {
				// 集合对象不等于空并且集合有数据
				checkExts = DataConfig.defLossInfoQueryData.getCheckExt();
				for (int i = 0; i < checkExts.size(); i++) {
					// 标的查勘车
					if (commonUtils == null) {
						commonUtils = new CommonUtils();
					}
					// 车牌号是否相符
					if ("CheckExt01".equals(checkExts.get(i).getCheckKernelCode())) {
						commonUtils.setSlipSwitchBtnShowView(i, btnCarNo, edCarNo, checkExts.get(i));
					}
					if ("CheckExt02".equals(checkExts.get(i).getCheckKernelCode())) {
						commonUtils.setSlipSwitchBtnShowView(i, btnEngine, edEngine, checkExts.get(i));
					}
					if ("CheckExt03".equals(checkExts.get(i).getCheckKernelCode())) {
						commonUtils.setSlipSwitchBtnShowView(i, btnChassis, edChassis, checkExts.get(i));
					}
					if ("CheckExt04".equals(checkExts.get(i).getCheckKernelCode())) {
						commonUtils.setSlipSwitchBtnShowView(i, btnUseType, checkExts.get(i));
					}
					if ("CheckExt05".equals(checkExts.get(i).getCheckKernelCode())) {
						commonUtils.setSlipSwitchBtnShowView(i, btnDriver, checkExts.get(i));
					}
				}
			}

		}

		btnCarNo.setEnabled(false);
		btnChassis.setEnabled(false);
		btnDriver.setEnabled(false);
		btnEngine.setEnabled(false);
		btnUseType.setEnabled(false);
		edCarNo.setEnabled(false);
		edEngine.setEnabled(false);
		edChassis.setEnabled(false);

	}

	private void findView() {
		btnCarNo = (MySlipSwitch) gView.findViewById(R.id.insurear_tv_carno_btn);
		edCarNo = (EditText) gView.findViewById(R.id.insurear_ed_carno);
		btnEngine = (MySlipSwitch) gView.findViewById(R.id.insurear_tv_engine_btn);
		edEngine = (EditText) gView.findViewById(R.id.insurear_ed_engine);
		btnChassis = (MySlipSwitch) gView.findViewById(R.id.insurear_tv_chassis_btn);
		edChassis = (EditText) gView.findViewById(R.id.insurear_ed_chassis);
		btnUseType = (MySlipSwitch) gView.findViewById(R.id.insurear_tv_usetype_btn);
		btnDriver = (MySlipSwitch) gView.findViewById(R.id.insurear_tv_drivingpermit_btn);

		btnCarNo.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		btnEngine.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		btnChassis.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		btnUseType.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		btnDriver.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
	}

	// 定损查勘按钮都不可动
	public void controlEd() {
		btnCarNo.setEnabled(false);
		btnChassis.setEnabled(false);
		btnDriver.setEnabled(false);
		btnEngine.setEnabled(false);
		btnUseType.setEnabled(false);
		edCarNo.setEnabled(false);
		edEngine.setEnabled(false);
		edChassis.setEnabled(false);

	}

}
