package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils.SpinnerOnItemClick;
import com.sinosoftyingda.fastclaim.survey.service.SurveyService;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils.ISlipSwitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/****
 * 查勘要点中 事故信息的字段布局
 * 
 * @author chenjianfan
 * 
 */
public class SSAccidentView {

	private Context context;
	private LayoutInflater inflater;
	private View gView;
	// 车损与事故是否相符
	private MySlipSwitch msCarLoss;
	private CheckExt ceCarLoss = new CheckExt();
	// 是否违反装载规定
	private MySlipSwitch msViolate;
	private CheckExt ceViolate = new CheckExt();
	// 是否属交强险保险责任
	private View spinnerStrong;
	private EditText edStrong;
	private SpinnerUtils spinnerUtils;
	private List<SpinnerBean> beans;
	private CheckExt ceStrong = new CheckExt();
	// 是否属商业险保险责任"
	private View spinnerBusiness;
	private EditText edBusiness;
	private SpinnerUtils spinnerUtilsBusiness;
	private CommonUtils commonUtils;
	private CheckExt ceBusiness = new CheckExt();
	private ArrayList<CheckExt> checkExts;

	public SSAccidentView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		beans = SurveyService.getSpinnerData();
		gView = inflater.inflate(R.layout.survey_survey_mainpoint_accident, null);
		findView();
		setViewData();
		setView();
	}

	private void setView() {
		if (DataConfig.tblTaskQuery != null && DataConfig.tblTaskQuery.getCheckExtList() != null && !DataConfig.tblTaskQuery.getCheckExtList().isEmpty()) {
			// 集合对象不等于空并且集合有数据
			checkExts = (ArrayList<CheckExt>) DataConfig.tblTaskQuery.getCheckExtList();
			for (int i = 0; i < checkExts.size(); i++) {
				// 3：事故信息
				if ("CheckExt11".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msCarLoss, checkExts.get(i));
				}
				if ("CheckExt12".equals(checkExts.get(i).getCheckKernelCode())) {
					commonUtils.setSlipSwitchBtnShowView(i, msViolate, checkExts.get(i));
				}
				if ("CheckExt13".equals(checkExts.get(i).getCheckKernelCode())) {

					if ("2".equals(checkExts.get(i).getCheckKernelSelect())) {
						spinnerUtils.getSpinnerEt().setText("待定");
						if (edStrong.getVisibility() == View.GONE) {
							edStrong.setVisibility(View.VISIBLE);
							edStrong.setText(checkExts.get(i).getCheckExtRemark());
						}

					} else if ("1".equals(checkExts.get(i).getCheckKernelSelect())) {
						spinnerUtils.getSpinnerEt().setText("是");
						if (edStrong.getVisibility() == View.VISIBLE) {
							edStrong.setVisibility(View.GONE);
						}

					} else if ("0".equals(checkExts.get(i).getCheckKernelSelect())) {

						spinnerUtils.getSpinnerEt().setText("否");
						if (edStrong.getVisibility() == View.VISIBLE) {
							edStrong.setVisibility(View.GONE);
						}

					} else {
						spinnerUtils.getSpinnerEt().setText("否");
						if (edStrong.getVisibility() == View.VISIBLE) {
							edStrong.setVisibility(View.GONE);
						}
					}
				}
				if ("CheckExt14".equals(checkExts.get(i).getCheckKernelCode())) {
					if ("2".equals(checkExts.get(i).getCheckKernelSelect())) {
						spinnerUtilsBusiness.getSpinnerEt().setText("待定");
						if (edBusiness.getVisibility() == View.GONE) {
							edBusiness.setVisibility(View.VISIBLE);
							edBusiness.setText(checkExts.get(i).getCheckExtRemark());
						}

					} else if ("1".equals(checkExts.get(i).getCheckKernelSelect())) {
						spinnerUtilsBusiness.getSpinnerEt().setText("是");
						if (edBusiness.getVisibility() == View.VISIBLE) {
							edBusiness.setVisibility(View.GONE);
						}
					} else if ("0".equals(checkExts.get(i).getCheckKernelSelect())) {
						spinnerUtilsBusiness.getSpinnerEt().setText("否");
						if (edBusiness.getVisibility() == View.VISIBLE) {
							edBusiness.setVisibility(View.GONE);
						}
					} else {
						spinnerUtilsBusiness.getSpinnerEt().setText("否");
						if (edBusiness.getVisibility() == View.VISIBLE) {
							edBusiness.setVisibility(View.GONE);
						}

					}
				}
			}

		}

		if (!SystemConfig.isOperate) {
			// false不可操作控件
			msCarLoss.setEnabled(false);
			msViolate.setEnabled(false);
			spinnerUtils.getSpinnerEt().setEnabled(false);
			spinnerUtilsBusiness.getSpinnerEt().setEnabled(false);
			spinnerStrong.setEnabled(false);
			spinnerStrong.setEnabled(false);
			edStrong.setEnabled(false);
			edBusiness.setEnabled(false);
		} else {
			msCarLoss.setEnabled(true);
			msViolate.setEnabled(true);
			spinnerUtils.getSpinnerEt().setEnabled(true);
			spinnerUtilsBusiness.getSpinnerEt().setEnabled(true);
			spinnerStrong.setEnabled(true);
			spinnerStrong.setEnabled(true);
			edStrong.setEnabled(true);
			edBusiness.setEnabled(true);
		}
	}

	private void findView() {
		msCarLoss = (MySlipSwitch) gView.findViewById(R.id.accident_tv_carloss_btn);
		msViolate = (MySlipSwitch) gView.findViewById(R.id.accident_tv_violate_btn);
		spinnerStrong = gView.findViewById(R.id.accident_spinner_strong);
		spinnerBusiness = gView.findViewById(R.id.accident_spinner_business);
		edStrong = (EditText) gView.findViewById(R.id.accident_ed_strong);
		edBusiness = (EditText) gView.findViewById(R.id.accident_ed_business);
		msCarLoss.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);
		msViolate.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno, R.drawable.yes_green);

	}

	private void setViewData() {
		// 默认
		// 3 事故信息
		commonUtils = new CommonUtils();
		// 车损与事故是否相符
		commonUtils.setYseAndrNo(msCarLoss, new ISlipSwitch() {
			@Override
			public void setYesButton() {

				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt11".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}

			}

			@Override
			public void setNoButton() {
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt11".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}
			}
		});

		// 是否违反装载规定

		commonUtils.setYseAndrNo(msViolate, new ISlipSwitch() {

			@Override
			public void setYesButton() {
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt12".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
					}
				}

			}

			@Override
			public void setNoButton() {
				for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {

					if ("CheckExt12".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
						DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
					}
				}

			}
		});

		// 是否属交强险保险责任
		spinnerUtils = new SpinnerUtils(beans, context, spinnerStrong);

		spinnerUtils.OnspinnerItemClick(new SpinnerOnItemClick() {

			@Override
			public void onItemClick(String selectValue, int position) {
				if ("待定".equals(selectValue)) {
					if (edStrong.getVisibility() == View.GONE)
						edStrong.setVisibility(View.VISIBLE);
				} else {
					if (edStrong.getVisibility() == View.VISIBLE)
						edStrong.setVisibility(View.GONE);
				}
				if ("是".equals(selectValue)) {

					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt13".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
						}
					}

				} else if ("否".equals(selectValue)) {

					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt13".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
						}
					}
				} else if ("待定".equals(selectValue)) {

					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt13".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("2");
						}
					}

				}
			}

		});
		// 是否属商业险保险责任
		spinnerUtilsBusiness = new SpinnerUtils(beans, context, spinnerBusiness);
		spinnerUtilsBusiness.OnspinnerItemClick(new SpinnerOnItemClick() {

			@Override
			public void onItemClick(String selectValue, int position) {
				if ("待定".equals(selectValue)) {
					if (edBusiness.getVisibility() == View.GONE)
						edBusiness.setVisibility(View.VISIBLE);
				} else {
					if (edBusiness.getVisibility() == View.VISIBLE)
						edBusiness.setVisibility(View.GONE);
				}
				if ("是".equals(selectValue)) {
					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt14".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("1");
						}
					}
				} else if ("否".equals(selectValue)) {
					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt14".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("0");
						}
					}
				}

				else if ("待定".equals(selectValue)) {
					for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
						if ("CheckExt14".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
							DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckKernelSelect("2");
						}
					}

				}

			}

		});

	}

	public List<CheckExt> getPageCheckExts() {

		for (int i = 0; i < DataConfig.tblTaskQuery.getCheckExtList().size(); i++) {
			// 是否交强
			if ("CheckExt13".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(edStrong.getText().toString().trim());
			}
			// 是否商业
			if ("CheckExt14".equals(DataConfig.tblTaskQuery.getCheckExtList().get(i).getCheckKernelCode())) {
				DataConfig.tblTaskQuery.getCheckExtList().get(i).setCheckExtRemark(edBusiness.getText().toString().trim());
			}
		}

		return null;
	}

	public void controlEd() {

		msCarLoss.setEnabled(false);
		msViolate.setEnabled(false);
		spinnerUtils.getSpinnerEt().setEnabled(false);
		spinnerUtilsBusiness.getSpinnerEt().setEnabled(false);
		spinnerStrong.setEnabled(false);
		spinnerStrong.setEnabled(false);
		edStrong.setEnabled(false);
		edBusiness.setEnabled(false);

	}

}
