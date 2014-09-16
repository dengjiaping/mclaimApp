package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.ISlipSwitch;

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
	private Context context;
	private LayoutInflater inflater;
	private CommonUtils commonUtils;
	public View gView;
	// 车牌号是否相符
	private MySlipSwitch btnCarNo;
	private EditText edCarNo;
	private CheckExt ceCarNo = new CheckExt();
	// 发动机是否相符
	private MySlipSwitch btnEngine;
	private EditText edEngine;
	private CheckExt ceEngine = new CheckExt();
	// 车架号是否相符
	private MySlipSwitch btnChassis;
	private EditText edChassis;
	private CheckExt ceChassis = new CheckExt();
	// 使用性质是否相符
	private MySlipSwitch btnUseType;
	private CheckExt ceUseType = new CheckExt();
	// 行使张是否有效
	private MySlipSwitch btnDriver;
	private CheckExt ceDriver = new CheckExt();

	public View getView() {
		return gView;
	}

	public SSInsurearView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		gView = inflater.inflate(R.layout.survey_survey_mainpoint_insurear, null);
		findView();
		setData();
		setView();
	}

	private void setView() {
		if (DataConfig.tblTaskQuery != null) {
			if (DataConfig.tblTaskQuery.getCheckExtList() != null && !DataConfig.tblTaskQuery.getCheckExtList().isEmpty()) {
				// 集合对象不等于空并且集合有数据
				checkExts = DataConfig.tblTaskQuery.getCheckExtList();
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

		if (!SystemConfig.isOperate) {
			// 控件不可操作
			btnCarNo.setEnabled(false);
			btnChassis.setEnabled(false);
			btnDriver.setEnabled(false);
			btnEngine.setEnabled(false);
			btnUseType.setEnabled(false);
			edCarNo.setEnabled(false);
			edEngine.setEnabled(false);
			edChassis.setEnabled(false);
		} else {
			btnCarNo.setEnabled(true);
			btnChassis.setEnabled(true);
			btnDriver.setEnabled(true);
			btnEngine.setEnabled(true);
			btnUseType.setEnabled(true);
			edCarNo.setEnabled(true);
			edEngine.setEnabled(true);
			edChassis.setEnabled(true);
		}

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

	private void setData() {

		commonUtils = new CommonUtils();

		// 车牌号是否相符
		commonUtils.setYseAndrNo(btnCarNo, new ISlipSwitch() {
			@Override
			public void setYesButton() {
				if (edCarNo.getVisibility() == View.VISIBLE)
					edCarNo.setVisibility(View.GONE);
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 车牌号是否相符
					if ("CheckExt01".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}

				}

			}

			@Override
			public void setNoButton() {
				if (edCarNo.getVisibility() == View.GONE)
					edCarNo.setVisibility(View.VISIBLE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 车牌号是否相符
					if ("CheckExt01".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}

				}

			}
		});

		// 发动机是否相符
		commonUtils.setYseAndrNo(btnEngine, new ISlipSwitch() {

			@Override
			public void setYesButton() {
				if (edEngine.getVisibility() == View.VISIBLE)
					edEngine.setVisibility(View.GONE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt02".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {
				if (edEngine.getVisibility() == View.GONE)
					edEngine.setVisibility(View.VISIBLE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt02".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}

				}

			}

		});

		// 车架号
		commonUtils.setYseAndrNo(btnChassis, new ISlipSwitch() {
			@Override
			public void setYesButton() {
				if (edChassis.getVisibility() == View.VISIBLE)
					edChassis.setVisibility(View.GONE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt03".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {
				if (edChassis.getVisibility() == View.GONE)
					edChassis.setVisibility(View.VISIBLE);
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt03".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}

		});

		// CheckExt04使用性质是否相符
		commonUtils.setYseAndrNo(btnUseType, new ISlipSwitch() {

			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt04".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					// 发动机是否相符
					if ("CheckExt04".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}

		});
		// 行驶证是否有效
		commonUtils.setYseAndrNo(btnDriver, new ISlipSwitch() {

			@Override
			public void setYesButton() {
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					if ("CheckExt05".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
					if ("CheckExt05".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}

		});
	}

	/******
	 * 保存数据
	 * 
	 * @return
	 */
	public List<CheckExt> getPageCheckExts() {
		for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
			// 车牌号是否相符
			if ("CheckExt01".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(edCarNo.getText().toString().trim());
			}
			// 车牌号是否相符
			if ("CheckExt02".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(edEngine.getText().toString().trim());
			}
			// 车架号
			if ("CheckExt03".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(edChassis.getText().toString().trim());
			}
		}

		return null;
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
