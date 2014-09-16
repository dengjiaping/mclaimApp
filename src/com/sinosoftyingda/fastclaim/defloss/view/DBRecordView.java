package com.sinosoftyingda.fastclaim.defloss.view;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.SurveyKeyPoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/****
 * 定损基本信息中，查勘记录的字段界面
 * 
 * @author chenjianfan
 * 
 */
public class DBRecordView {
	private Context context;
	private LayoutInflater inflater;
	private View gView;
	private TextView tvFirstScene;
	private TextView tvReason;
	private TextView tvResponsibility;
	private TextView tvProperties;
	private TextView tvSubrogation;
	private TextView tvSince;
	// 有无交管事故书
	private LinearLayout llAccidentBook;
	private TextView tvAccidentBook;
	// 是否交通事故
	private LinearLayout llAccident;
	private TextView tvAccident;
	private String[] gStOccurrenceReason, gStrAccidentResponsibility, gStrClaimCaseProperties;
	// 数据
	private SurveyKeyPoint surveyKeyPoint;

	public DBRecordView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		gView = inflater.inflate(R.layout.defloss_basic_record, null);
		findView();
		setView();
	}

	public View getView() {
		return gView;
	}

	private void findView() {
		tvFirstScene = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_first_scene);
		tvReason = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_occurrence_reason);
		tvResponsibility = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_responsibility);
		tvProperties = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_properties);
		tvSubrogation = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_subrogation);
		tvSince = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_sincetouch_since);
		llAccident = (LinearLayout) gView.findViewById(R.id.defloss_basic_record_ll_accident);
		llAccidentBook = (LinearLayout) gView.findViewById(R.id.defloss_basic_record_ll_sincetouch_accidentBook);
		tvAccident = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_accident);
		tvAccidentBook = (TextView) gView.findViewById(R.id.defloss_basic_record_tv_sincetouch_accidentBook);
		gStOccurrenceReason = context.getResources().getStringArray(R.array.occurrence_reason);
		gStrAccidentResponsibility = context.getResources().getStringArray(R.array.accident_responsibility);
		gStrClaimCaseProperties = context.getResources().getStringArray(R.array.claim_case_properties);
	}

	private void setView() {
		if (DataConfig.defLossInfoQueryData != null) {
			if (DataConfig.defLossInfoQueryData.getSurveyKeyPoint() != null) {
				surveyKeyPoint = DataConfig.defLossInfoQueryData.getSurveyKeyPoint();

				if ("1".equals(surveyKeyPoint.getFirstsiteFlag()))
					tvFirstScene.setText("是");
				else if ("0".equals(surveyKeyPoint.getFirstsiteFlag()))
					tvFirstScene.setText("否");

				// <item>碰撞-A01</item>
				System.out.println(surveyKeyPoint.getDamageCode() + "出险原因");
				for (int i = 0; i < gStOccurrenceReason.length; i++) {
					if (gStOccurrenceReason[i].split("-")[1].equals(surveyKeyPoint.getDamageCode())) {
						tvReason.setText(gStOccurrenceReason[i].split("-")[0]);
					}
				}
				// 事故责任
				System.out.println("事故责任" + surveyKeyPoint.getIndemnityDuty());
				for (int i = 0; i < gStrAccidentResponsibility.length; i++) {
					if (gStrAccidentResponsibility[i].split("-")[1].equals(surveyKeyPoint.getIndemnityDuty())) {
						tvResponsibility.setText(gStrAccidentResponsibility[i].split("-")[0]);
					}
				}

				// 赔案性质
				System.out.println("赔案性质" + surveyKeyPoint.getClaimType());
				for (int i = 0; i < gStrClaimCaseProperties.length; i++) {
					if (gStrClaimCaseProperties[i].split("-")[1].equals(surveyKeyPoint.getClaimType())) {
						tvProperties.setText(gStrClaimCaseProperties[i].split("-")[0]);
					}
				}

				// 是否代为
				if (surveyKeyPoint.getSubrogateType() == null) {
					if (tvSubrogation.getVisibility() == View.VISIBLE)
						tvSubrogation.setVisibility(View.GONE);

				} else {
					if (tvSubrogation.getVisibility() == View.GONE)
						tvSubrogation.setVisibility(View.VISIBLE);

					if ("1".equals(surveyKeyPoint.getSubrogateType()))
						tvSubrogation.setText("是");
					else if ("0".equals(surveyKeyPoint.getSubrogateType()))
						tvSubrogation.setText("否");
				}

				// 是否通赔
				if (surveyKeyPoint.getIsCommonClaim() == null) {
					if (tvSince.getVisibility() == View.VISIBLE)
						tvSince.setVisibility(View.GONE);

				} else {
					if (tvSince.getVisibility() == View.GONE)
						tvSince.setVisibility(View.VISIBLE);
					if ("1".equals(surveyKeyPoint.getIsCommonClaim()))
						tvSince.setText("是");
					else if ("0".equals(surveyKeyPoint.getIsCommonClaim()))
						tvSince.setText("否");

				}

				System.out.println("是否交通事故" + surveyKeyPoint.getAccident());
				System.out.println("有无交管事故书" + surveyKeyPoint.getAccidentBook());
				if ("北京".equals(SystemConfig.AREAN)) {
					llAccidentBook.setVisibility(View.VISIBLE);
					llAccident.setVisibility(View.VISIBLE);
					if ("1".equals(surveyKeyPoint.getAccident()))
						tvAccident.setText("有");
					else if ("0".equals(surveyKeyPoint.getAccident()))
						tvAccident.setText("无");
					if ("1".equals(surveyKeyPoint.getAccidentBook()))
						tvAccidentBook.setText("有");
					else if ("0".equals(surveyKeyPoint.getAccidentBook()))
						tvAccidentBook.setText("无");
				} else {
					llAccidentBook.setVisibility(View.GONE);
					llAccident.setVisibility(View.GONE);
				}
			}
		}
	}
}
