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

/****
 * 查勘要点中 标的车驾驶员字段
 * 
 * @author chenjianfan
 * 
 */
public class SSInsurearDriverView {

	private ArrayList<CheckExt> checkExts;

	private Context context;
	private LayoutInflater inflater;
	private View gView;
	// 是否为报案驾驶员
	private MySlipSwitch msBaoanDriver;
	private EditText etBaoanDriver;
	private CheckExt ceBaoanDriver = new CheckExt();
	// 是否为允许的驾驶员
	private MySlipSwitch msAllowdDriver;
	private CheckExt ceAllowdDriver = new CheckExt();
	// 准驾车型是否相符
	private MySlipSwitch msModelstype;
	private CheckExt ceModelstype = new CheckExt();
	// 驾驶证是否有效
	private MySlipSwitch msValidDriver;
	private CheckExt ceValidDriver = new CheckExt();
	// 是否酒后驾车
	private MySlipSwitch msDrunkDriving;
	private CheckExt ceDrunkDriving = new CheckExt();

	private CommonUtils commonUtils;

	public SSInsurearDriverView(Context context) {
		this.context = context;
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
		setViewData();
	}

	private void setView() {

		if (DataConfig.tblTaskQuery != null && DataConfig.tblTaskQuery.getCheckExtList() != null && !DataConfig.tblTaskQuery.getCheckExtList().isEmpty()) {
			// 集合对象不等于空并且集合有数据
			checkExts = (ArrayList<CheckExt>) DataConfig.tblTaskQuery.getCheckExtList();
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

		if (!SystemConfig.isOperate) {
			// 控件不可操作
			msBaoanDriver.setEnabled(false);
			msDrunkDriving.setEnabled(false);
			msModelstype.setEnabled(false);
			msValidDriver.setEnabled(false);
			etBaoanDriver.setEnabled(false);
			msAllowdDriver.setEnabled(false);
		} else {
			msBaoanDriver.setEnabled(true);
			msDrunkDriving.setEnabled(true);
			msModelstype.setEnabled(true);
			msValidDriver.setEnabled(true);
			etBaoanDriver.setEnabled(true);
			msAllowdDriver.setEnabled(true);
		}

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

	private void setViewData() {
		commonUtils = new CommonUtils();
		// 是否为报案人
		commonUtils.setYseAndrNo(msBaoanDriver, new ISlipSwitch() {
			@Override
			public void setYesButton() {
				if (etBaoanDriver.getVisibility() == View.VISIBLE)
					etBaoanDriver.setVisibility(View.GONE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt06".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}

				}

			}

			@Override
			public void setNoButton() {
				if (etBaoanDriver.getVisibility() == View.GONE)
					etBaoanDriver.setVisibility(View.VISIBLE);

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt06".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}
		});
		// 是否为允许驾驶员
		commonUtils.setYseAndrNo(msAllowdDriver, new ISlipSwitch() {

			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt07".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt07".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}
		});
		// 准驾车型是否相符
		commonUtils.setYseAndrNo(msModelstype, new ISlipSwitch() {

			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt08".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}

			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt08".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}

			}
		});
		// 驾驶证是否有效
		commonUtils.setYseAndrNo(msValidDriver, new ISlipSwitch() {

			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt09".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt09".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}
		});
		commonUtils.setYseAndrNo(msDrunkDriving, new ISlipSwitch() {
			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt10".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}
			}

			@Override
			public void setNoButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt10".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}
		});

	}

	public List<CheckExt> getPageCheckExts() {
		for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
			// 车牌号是否相符
			if ("CheckExt06".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(etBaoanDriver.getText().toString().trim());
			}
		}
		return null;
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
