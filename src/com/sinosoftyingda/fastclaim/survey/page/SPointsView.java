package com.sinosoftyingda.fastclaim.survey.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;

/**
 * 报案信息
 * 
 * @author haoyun 20130228
 * 
 */
public class SPointsView implements OnClickListener {

	private View gView;
	private Context gContext;
	public Boolean gBFirstScene, gBTrafficAccidentBook, gBRoadTrafficAccident;
	private RadioButton gRbFirstSceneY, gRbFirstSceneN,
			gRbTrafficAccidentBookY, gRbTrafficAccidentBookN,
			gRbRoadTrafficAccidentY, gRbRoadTrafficAccidentN;
	private View gVOccurrenceReason, gVAccidentResponsibility,
			gVClaimCaseProperties;

	public SpinnerUtils gSuOccurrenceReason;
	public SpinnerUtils gSuAccidentResponsibility;
	public SpinnerUtils gSuClaimCaseProperties;

	private String[] gStOccurrenceReason = null,
			gStrAccidentResponsibility = null, gStrClaimCaseProperties = null;
	public TextView gTvSubrogation, gTvMutualTouchSince, gTvRiskTip;
	private LinearLayout gLlRiskTip, gLlBeijing;
	protected LayoutInflater inflater;

	private SSurveyActivity activity;
	
	public SPointsView(Context context, SSurveyActivity activity) {
		inflater = LayoutInflater.from(context);
		gView = inflater.inflate(R.layout.survey_main_points, null);
		gContext = context;
		this.activity = activity;
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		gStOccurrenceReason = gContext.getResources().getStringArray(
				R.array.occurrence_reason);
		gStrAccidentResponsibility = gContext.getResources().getStringArray(
				R.array.accident_responsibility);
		gStrClaimCaseProperties = gContext.getResources().getStringArray(
				R.array.claim_case_properties);

		/**
		 * TextView
		 */
		gTvSubrogation = (TextView) gView.findViewById(R.id.smp_tv_subrogation);
		gTvMutualTouchSince = (TextView) gView
				.findViewById(R.id.smp_tv_mutual_touch_since);
		gTvRiskTip = (TextView) gView.findViewById(R.id.smp_tv_risk_tip);
		/**
		 * RadioGroup
		 */
		// gRgFirstScene = (RadioGroup)
		// gView.findViewById(R.id.smp_rg_first_scene);
		// gRgTrafficAccidentBook = (RadioGroup)
		// gView.findViewById(R.id.smp_rg_traffic_accident_book);
		// gRgRoadTrafficAccident = (RadioGroup)
		// gView.findViewById(R.id.smp_rg_road_traffic_accident);

		/**
		 * RadioButton
		 */
		gRbFirstSceneY = (RadioButton) gView
				.findViewById(R.id.smp_rd_first_scene_y);
		gRbFirstSceneN = (RadioButton) gView
				.findViewById(R.id.smp_rd_first_scene_n);
		gRbTrafficAccidentBookY = (RadioButton) gView
				.findViewById(R.id.smp_rd_traffic_accident_book_y);
		gRbTrafficAccidentBookN = (RadioButton) gView
				.findViewById(R.id.smp_rd_traffic_accident_book_n);
		gRbRoadTrafficAccidentY = (RadioButton) gView
				.findViewById(R.id.smp_rd_road_traffic_accident_y);
		gRbRoadTrafficAccidentN = (RadioButton) gView
				.findViewById(R.id.smp_rd_road_traffic_accident_n);
		/**
		 * SpinnerUtils
		 */
		gVOccurrenceReason = gView.findViewById(R.id.smp_i_occurrence_reason);
		gVAccidentResponsibility = gView
				.findViewById(R.id.smp_i_accident_responsibility);
		gVClaimCaseProperties = gView
				.findViewById(R.id.smp_i_claim_case_properties);
		/**
		 * LinearLayout
		 */
		gLlRiskTip = (LinearLayout) gView.findViewById(R.id.smp_ll_risk_tip);
		gLlBeijing = (LinearLayout) gView.findViewById(R.id.smp_ll_beijing);
		/**
		 * 初始化数据
		 */
		Assignment();
		setListener();
		// 控制控件的操作性
		controlEd();

	}

	public void controlEd() {
		if (!SystemConfig.isOperate) {
			// 为false 不可操作
			gSuOccurrenceReason.getSpinnerEt().setEnabled(false);
			gSuAccidentResponsibility.getSpinnerEt().setEnabled(false);
			gSuClaimCaseProperties.getSpinnerEt().setEnabled(false);

			gRbFirstSceneY.setClickable(false);
			gRbFirstSceneN.setClickable(false);

			gRbTrafficAccidentBookY.setClickable(false);
			gRbTrafficAccidentBookN.setClickable(false);
			gRbRoadTrafficAccidentY.setClickable(false);
			gRbRoadTrafficAccidentN.setClickable(false);

		} else {
			gSuOccurrenceReason.getSpinnerEt().setEnabled(true);
			gSuAccidentResponsibility.getSpinnerEt().setEnabled(true);
			gSuClaimCaseProperties.getSpinnerEt().setEnabled(true);

			gRbFirstSceneY.setClickable(true);
			gRbFirstSceneN.setClickable(true);

			gRbTrafficAccidentBookY.setClickable(true);
			gRbTrafficAccidentBookN.setClickable(true);
			gRbRoadTrafficAccidentY.setClickable(true);
			gRbRoadTrafficAccidentN.setClickable(true);
		}

		String editable = DataConfig.tblTaskQuery.getIndemnityDuty();
		// 检验的规则
		if ("0".equals(editable.toString())) {
			gSuClaimCaseProperties.getSpinnerEt().setText(
					gStrClaimCaseProperties[0].split("-")[0]);
			DataConfig.tblTaskQuery.setClaimType(gStrClaimCaseProperties[0]
					.split("-")[1]);
			gSuClaimCaseProperties.getSpinnerEt().setEnabled(false);
		} else if ("1".equals(editable.toString())) {
			gSuClaimCaseProperties.getSpinnerEt().setText(
					gStrClaimCaseProperties[0].split("-")[0]);
			DataConfig.tblTaskQuery.setClaimType(gStrClaimCaseProperties[0]
					.split("-")[1]);
			gSuClaimCaseProperties.getSpinnerEt().setEnabled(false);
		} else {
			gSuClaimCaseProperties.getSpinnerEt().setEnabled(true);
		}

	}

	/**
	 * 页面数值填充
	 */
	private void Assignment() {
		if (DataConfig.tblTaskQuery != null) {
			if ("1".equals(DataConfig.tblTaskQuery.getFirstsiteFlag())) {
				gRbFirstSceneY.setChecked(true);
				gRbFirstSceneN.setChecked(false);
			} else {
				gRbFirstSceneY.setChecked(false);
				gRbFirstSceneN.setChecked(true);
			}

			if ("北京".equals(SystemConfig.AREAN)) {
				/**
				 * 显示以下
				 */
				gLlBeijing.setVisibility(View.VISIBLE);
				/**
				 * 交管事故书
				 */
				if ("1".equals(DataConfig.tblTaskQuery.getAccidentBook())) {
					gRbTrafficAccidentBookY.setChecked(true);
					gRbTrafficAccidentBookN.setChecked(false);
					DataConfig.tblTaskQuery.setAccidentBook("1");
				} else {
					gRbTrafficAccidentBookY.setChecked(false);
					gRbTrafficAccidentBookN.setChecked(true);
					DataConfig.tblTaskQuery.setAccidentBook("0");
				}
				/**
				 * 道路交通事故
				 */
				if ("1".equals(DataConfig.tblTaskQuery.getAccident())) {
					gRbRoadTrafficAccidentY.setChecked(true);
					gRbRoadTrafficAccidentN.setChecked(false);

				} else {
					gRbRoadTrafficAccidentY.setChecked(false);
					gRbRoadTrafficAccidentN.setChecked(true);

				}
			} else {
				gLlBeijing.setVisibility(View.GONE);
			}
			/**
			 * 出险原因
			 */
			gSuOccurrenceReason = getSpinnerUtils(gVOccurrenceReason,
					gStOccurrenceReason,
					DataConfig.tblTaskQuery.getDamageCode());
			System.out.println(gSuOccurrenceReason.getSpinnerEt().getText());
			/**
			 * 事故责任
			 */
			gSuAccidentResponsibility = getSpinnerUtils(
					gVAccidentResponsibility, gStrAccidentResponsibility,
					DataConfig.tblTaskQuery.getIndemnityDuty());

			/**
			 * 赔案性质
			 */
			gSuClaimCaseProperties = getSpinnerUtils(gVClaimCaseProperties,
					gStrClaimCaseProperties,
					DataConfig.tblTaskQuery.getClaimType());
			/**
			 * 代位求偿
			 */
			gTvSubrogation.setText("1".equals(DataConfig.tblTaskQuery
					.getSubrogateType()) ? "是" : "否");
			/**
			 * 是否通赔
			 */
			gTvMutualTouchSince.setText("1".equals(DataConfig.tblTaskQuery
					.getIsCommonClaim()) ? "是" : "否");
			/**
			 * 风险提示 如果出现次数为0则显示风险提示
			 */
			if (DataConfig.tblTaskQuery.getDamageDayp() != null
					&& !DataConfig.tblTaskQuery.getDamageDayp().equals("")) {
				gTvRiskTip.setText(DataConfig.tblTaskQuery.getDamageDayp());
			}

			for (int i = 0; i < gStOccurrenceReason.length; i++) {
				String[] a = gStOccurrenceReason[i].split("-");

				if (a[1].equals(DataConfig.tblTaskQuery.getDamageCode())) {
					gSuOccurrenceReason.getSpinnerEt().setText(a[0]);
				}
			}
		}
	}

	/**
	 * 设置SpinnerUtils
	 * 
	 * @return
	 */
	private SpinnerUtils getSpinnerUtils(View view, String[] strs, String p) {
		int id = 0;
		String value = "";
		List<SpinnerBean> spinnerBeans = new ArrayList<SpinnerBean>();
		for (int i = 0; i < strs.length; i++) {
			String[] str = strs[i].split("-");
			SpinnerBean bean = new SpinnerBean();
			bean.setValue(str[0]);
			bean.setCode(str[1]);
			spinnerBeans.add(bean);
			if (p.equals(str[1])) {
				id = i;
				value = str[0];
			}
		}
		SpinnerUtils spinnerUtils = new SpinnerUtils(spinnerBeans, gContext,
				view);
		spinnerUtils.setId(id);
		spinnerUtils.getSpinnerEt().setText(value);
		return spinnerUtils;
	}

	protected void setListener() {
		/**
		 * RadioButton
		 */
		gRbFirstSceneY.setOnClickListener(this);
		gRbFirstSceneN.setOnClickListener(this);
		gRbTrafficAccidentBookY.setOnClickListener(this);
		gRbTrafficAccidentBookN.setOnClickListener(this);
		gRbRoadTrafficAccidentY.setOnClickListener(this);
		gRbRoadTrafficAccidentN.setOnClickListener(this);

		gSuOccurrenceReason.setOnTextChangeListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				DataConfig.tblTaskQuery.setDamageCode(gSuOccurrenceReason.getCode(editable.toString()));
				DataConfig.tblTaskQuery.setDamageName(editable.toString());
				activity.createReport();
			}
		});

		gSuClaimCaseProperties.setOnTextChangeListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				DataConfig.tblTaskQuery.setClaimType(gSuClaimCaseProperties
						.getCode(editable.toString()));
			}
		});

		gSuAccidentResponsibility.setOnTextChangeListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable editable) {

				for (int i = 0; i < gStrAccidentResponsibility.length; i++) {
					if (editable.toString().equals(
							gStrAccidentResponsibility[i].split("-")[0])) {
						DataConfig.tblTaskQuery
								.setIndemnityDuty(gStrAccidentResponsibility[i]
										.split("-")[1]);
						/**
						 * 判断事故责任显示不同责任比例
						 * 
						 * 0：全责 1：主责 2：同责 3：次责 4：无责
						 */
						String duty="";
						switch (Integer.parseInt(gStrAccidentResponsibility[i]
								.split("-")[1])) {
						case 0:
							duty="100";
							break;
							
						case 1:
							duty="70";
							break;
						case 2:
							duty="50";
							break;
						case 3:
							duty="30";
							break;
						case 4:
							duty="0";
							break;
						}
						/**
						 * 如果标的车责任比例不是空 或 0 或者0.0那么就按照选中的责任比例赋值
						 */
						for (int j = 0; j < DataConfig.tblTaskQuery.getCarLossList()
								.size(); j++) {
							if ("1".equals(DataConfig.tblTaskQuery.getCarLossList().get(j).getInsureCarFlag())) {
								
								DataConfig.tblTaskQuery.getCarLossList().get(j)
										.setDutyPercent(duty);
								TblTaskQuery.insertOrUpdate(
										SystemConfig.dbhelp.getWritableDatabase(),
										DataConfig.tblTaskQuery, false, true, true);

							
								break;

							}
						}
						break;
					}
				}

				// 检验的规则
				if ("主责".equals(editable.toString())) {
					gSuClaimCaseProperties.getSpinnerEt().setText(
							gStrClaimCaseProperties[0].split("-")[0]);
					DataConfig.tblTaskQuery
							.setClaimType(gStrClaimCaseProperties[0].split("-")[1]);
					gSuClaimCaseProperties.getSpinnerEt().setEnabled(false);
				} else if ("全责".equals(editable.toString())) {
					gSuClaimCaseProperties.getSpinnerEt().setText(
							gStrClaimCaseProperties[0].split("-")[0]);
					DataConfig.tblTaskQuery
							.setClaimType(gStrClaimCaseProperties[0].split("-")[1]);
					gSuClaimCaseProperties.getSpinnerEt().setEnabled(false);
				} else {
					gSuClaimCaseProperties.getSpinnerEt().setEnabled(true);
				}

			}
		});

	}

	@Override
	public void onClick(View view) {
		if (DataConfig.tblTaskQuery != null) {
			switch (view.getId()) {
			case R.id.smp_rd_first_scene_y:
				if (gRbFirstSceneY.isChecked()) {
					DataConfig.tblTaskQuery.setFirstsiteFlag("1");
				}
				break;
			case R.id.smp_rd_first_scene_n:
				if (gRbFirstSceneN.isChecked()) {
					DataConfig.tblTaskQuery.setFirstsiteFlag("0");
				}
				break;
			case R.id.smp_rd_traffic_accident_book_y:
				if (gRbTrafficAccidentBookY.isChecked()) {
					DataConfig.tblTaskQuery.setAccidentBook("1");
				}
				break;
			case R.id.smp_rd_traffic_accident_book_n:
				if (gRbTrafficAccidentBookN.isChecked()) {
					DataConfig.tblTaskQuery.setAccidentBook("0");
				}
				break;
			case R.id.smp_rd_road_traffic_accident_y:
				if (gRbRoadTrafficAccidentY.isChecked()) {
					DataConfig.tblTaskQuery.setAccident("1");
				}
				break;
			case R.id.smp_rd_road_traffic_accident_n:
				if (gRbRoadTrafficAccidentN.isChecked()) {
					DataConfig.tblTaskQuery.setAccident("0");
				}
				break;
			}
		}
	}

}
