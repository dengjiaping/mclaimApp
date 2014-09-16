package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.survey.service.SurveyService;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;
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
		setView();
	}

	private void setView() {
		commonUtils=new CommonUtils();
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getCheckExt() != null && !DataConfig.defLossInfoQueryData.getCheckExt().isEmpty()) {
			// 集合对象不等于空并且集合有数据
			checkExts = (ArrayList<CheckExt>) DataConfig.defLossInfoQueryData.getCheckExt();
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

		// false不可操作控件
		msCarLoss.setEnabled(false);
		msViolate.setEnabled(false);
		spinnerUtils.getSpinnerEt().setEnabled(false);
		spinnerUtilsBusiness.getSpinnerEt().setEnabled(false);
		spinnerStrong.setEnabled(false);
		spinnerStrong.setEnabled(false);
		edStrong.setEnabled(false);
		edBusiness.setEnabled(false);

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
		// 是否属交强险保险责任
		spinnerUtils = new SpinnerUtils(beans, context, spinnerStrong);
		// 是否属商业险保险责任
		spinnerUtilsBusiness = new SpinnerUtils(beans, context, spinnerBusiness);
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
