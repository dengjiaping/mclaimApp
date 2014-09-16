package com.sinosoftyingda.fastclaim.survey.page.three.page;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/****
 * 查勘保单的基本信息
 * 
 * @author chenjianfan
 * 
 */
public class SGuaranteeslipView {

	private Context context;

	private LayoutInflater inflater;
	private View gView;

	// 初始化控件
	private TextView tvAssured;
	private TextView tvRegistration;
	private TextView tvCarNo;
	private TextView tvFactoryNO;
	private TextView tvVin;
	private TextView tvEngineNO;
	private TextView tvFirstLoginDate;
	private TextView tvSpentYears;
	private TextView tvNewCarPrice;
	private TextView tvCarUseType;

	// 交强险保险单号
	private RelativeLayout rlQsPolicyNo;
	private TextView tvQsPolicyNo;
	private RelativeLayout rlQsPolicyNoTime;
	private TextView tvQsPolicyNoTime;
	// 商业险保险单号
	private RelativeLayout rlBusInessPolicyNo;
	private TextView tvBusInessPolicyNo;
	private RelativeLayout rlBusInessPolicyNoTime;
	private TextView tvBusInessPolicyNoTime;
	private RelativeLayout rlTvTyple;
	private TextView tvType;// 基本条款类型

	private TextView tvDepartment;
	private TextView tvServiceId;

	// 数据
	private PolicyMsgResponse policyMsgResponse;
	private String qsPolicyNo;
	private String busInessPolicyNo;

	public SGuaranteeslipView(Context context, PolicyMsgResponse policyMsgResponse) {
		this.context = context;
		this.policyMsgResponse = policyMsgResponse;
		inflater = LayoutInflater.from(context);
		init();

	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.survey_guaranteeslip_basic, null);
		findView();
		setView();

	}

	private void findView() {
		rlBusInessPolicyNo = (RelativeLayout) gView.findViewById(R.id.survry_guaranteeslop_rl_busInesspolicyno);
		tvBusInessPolicyNo = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_busInesspolicyno);
		rlBusInessPolicyNoTime = (RelativeLayout) gView.findViewById(R.id.survry_guaranteeslop_rl_busInesspolicynotime);
		tvBusInessPolicyNoTime = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_busInesspolicynotime);
		rlQsPolicyNo = (RelativeLayout) gView.findViewById(R.id.survry_guaranteeslop_rl_qspolicyno);
		rlQsPolicyNoTime = (RelativeLayout) gView.findViewById(R.id.survry_guaranteeslop_rl_qspolicynotitme);
		tvAssured = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_assuredname);
		tvRegistration = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_registrationname);
		tvCarNo = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_carno);

		tvFactoryNO = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_factory);
		tvVin = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_VIN);
		tvEngineNO = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_engine);
		tvFirstLoginDate = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_firstdate);
		tvSpentYears = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_spentyears);
		tvNewCarPrice = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_newcarprice);
		tvCarUseType = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_carusetype);
		tvQsPolicyNo = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_claimno);
		tvQsPolicyNoTime = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_claimtime);
		tvType = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_type);
		tvDepartment = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_department);
		tvServiceId = (TextView) gView.findViewById(R.id.survry_guaranteeslip_basic_Serviceid);
		rlTvTyple = (RelativeLayout) gView.findViewById(R.id.survry_guaranteeslip_rl_basic_typle);
	}

	private void setView() {
		
		
		
		if (policyMsgResponse != null) {
			tvAssured.setText(policyMsgResponse.getInsuredName());
			tvRegistration.setText(policyMsgResponse.getDrivingLicenesOwner());
			tvCarNo.setText(policyMsgResponse.getLicenseNo());
			tvFactoryNO.setText(policyMsgResponse.getLabelType());
			tvVin.setText(policyMsgResponse.getVidNo());
			tvEngineNO.setText(policyMsgResponse.getEngineNo());
			tvFirstLoginDate.setText(policyMsgResponse.getCarFirstregistration());
			tvSpentYears.setText(policyMsgResponse.getUseYears());
			tvNewCarPrice.setText(policyMsgResponse.getNewCarPurchaseprice());
			tvCarUseType.setText(policyMsgResponse.getVehicleuse());
			// 保险单号（交强险） 仅涉及商业险，本项目不显示
			// 保险期间： 仅涉及商业险，本项目不显示
			// 保单号码（商业险）： 仅涉及交强险险，本项目不显示
			// 保险期间： 仅涉及交强险险，本项目不显示
			// 基本条款类别： 仅涉及交强险险，本项目不显示
			// 交强险保单号
			if (TextUtils.isEmpty(policyMsgResponse.getQsPolicyNo())) {
				qsPolicyNo = "";
			} else {
				qsPolicyNo = policyMsgResponse.getQsPolicyNo();
			}

			// 商业险保单号
			if (TextUtils.isEmpty(policyMsgResponse.getBusInessPolicyNo())) {
				busInessPolicyNo = "";
			} else {
				busInessPolicyNo = policyMsgResponse.getBusInessPolicyNo();
			}
			// 单号
			if (!TextUtils.isEmpty(busInessPolicyNo) && TextUtils.isEmpty(qsPolicyNo)) {
				// 商业险保单号不为空,交强险保单号为空
				// 显示商业险保单号，不显示交强险保单
				if (rlQsPolicyNo.getVisibility() == View.VISIBLE)
					rlQsPolicyNo.setVisibility(View.GONE);
				if (rlQsPolicyNoTime.getVisibility() == View.VISIBLE)
					rlQsPolicyNoTime.setVisibility(View.GONE);

				if (rlBusInessPolicyNo.getVisibility() == View.GONE)
					rlBusInessPolicyNo.setVisibility(View.VISIBLE);
				if (rlBusInessPolicyNoTime.getVisibility() == View.GONE)
					rlBusInessPolicyNoTime.setVisibility(View.VISIBLE);
				if (rlTvTyple.getVisibility() == View.GONE)
					rlTvTyple.setVisibility(View.VISIBLE);

				tvBusInessPolicyNo.setText(busInessPolicyNo);
				tvBusInessPolicyNoTime.setText(policyMsgResponse.getBusInessPolicyInsureDate());
				tvType.setText(policyMsgResponse.getBasicClausetypes());
			} else if (TextUtils.isEmpty(busInessPolicyNo) && !TextUtils.isEmpty(qsPolicyNo)) {
				// 商业险保单号为空,交强险保单号不为空
				// bu显示商业险保单号，显示交强险保单
				if (rlQsPolicyNo.getVisibility() == View.GONE)
					rlQsPolicyNo.setVisibility(View.VISIBLE);
				if (rlQsPolicyNoTime.getVisibility() == View.GONE)
					rlQsPolicyNoTime.setVisibility(View.VISIBLE);
				if (rlBusInessPolicyNo.getVisibility() == View.VISIBLE)
					rlBusInessPolicyNo.setVisibility(View.GONE);
				if (rlBusInessPolicyNoTime.getVisibility() == View.VISIBLE)
					rlBusInessPolicyNoTime.setVisibility(View.GONE);
				if (rlTvTyple.getVisibility() == View.VISIBLE)
					rlTvTyple.setVisibility(View.GONE);
				tvQsPolicyNoTime.setText(policyMsgResponse.getQsPolicyInsureDate());
				tvQsPolicyNo.setText(policyMsgResponse.getQsPolicyNo());
			} else if (!TextUtils.isEmpty(busInessPolicyNo) && !TextUtils.isEmpty(qsPolicyNo)) {

				if (rlQsPolicyNo.getVisibility() == View.GONE)
					rlQsPolicyNo.setVisibility(View.VISIBLE);
				if (rlQsPolicyNoTime.getVisibility() == View.GONE)
					rlQsPolicyNoTime.setVisibility(View.VISIBLE);

				if (rlBusInessPolicyNo.getVisibility() == View.GONE)
					rlBusInessPolicyNo.setVisibility(View.VISIBLE);
				if (rlBusInessPolicyNoTime.getVisibility() == View.GONE)
					rlBusInessPolicyNoTime.setVisibility(View.VISIBLE);
				if (rlTvTyple.getVisibility() == View.GONE)
					rlTvTyple.setVisibility(View.VISIBLE);

				tvQsPolicyNo.setText(qsPolicyNo);
				tvQsPolicyNoTime.setText(policyMsgResponse.getQsPolicyInsureDate());
				tvBusInessPolicyNo.setText(busInessPolicyNo);
				tvBusInessPolicyNoTime.setText(policyMsgResponse.getBusInessPolicyInsureDate());
				tvType.setText(policyMsgResponse.getBasicClausetypes());
			}
			tvDepartment.setText(policyMsgResponse.getPolicyComCode());
			tvServiceId.setText(policyMsgResponse.getBusInessFlag());
		}

	}
}
