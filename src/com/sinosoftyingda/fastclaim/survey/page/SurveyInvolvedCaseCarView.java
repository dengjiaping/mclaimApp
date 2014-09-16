package com.sinosoftyingda.fastclaim.survey.page;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.survey.page.three.page.SunbjectCarActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.page.ThreeCarActivity;

/**
 * 案件涉损
 * 
 * @author haoyun 20130228
 */
public class SurveyInvolvedCaseCarView implements OnClickListener {
	private static final String Tag = "SurveyInvolvedCaseCarView";
	private Context gContext;
	private View gView;
	protected LayoutInflater inflater;// 加载xml文件的工具类

	private List<CarLoss> carLosses;
	/**
	 * 标的车
	 */
	public ImageView gViSubjectCarLeft;
	private RelativeLayout rlSubjectCar;
	/**
	 * 三者车
	 */
	public ImageView gViThreePartyCarLeft;
	private RelativeLayout rlThreePartyCar;
	private TextView gTvTNum;

	/**
	 * 物损
	 */
	public ImageView gViPropertyDamageLeft;
	private RelativeLayout rlPropertyDamage;

	/**
	 * 人伤
	 */
	public ImageView gViPeopleLeft;
	private RelativeLayout rlPeopleDamage;

	/**
	 * 驾驶人信息
	 */
	public ImageView gViDriverMsgLeft;
	private RelativeLayout rlDriverMsg;

	public SurveyInvolvedCaseCarView(Context context) {
		inflater = LayoutInflater.from(context);
		gView = inflater.inflate(R.layout.survey_involved_case_car, null);
		gContext = context;
		init();
	}

	public View getView() {
		return gView;
	}

	protected void init() {
		gViSubjectCarLeft = (ImageView) gView.findViewById(R.id.sicc_iv_subject_car_left);
		rlSubjectCar = (RelativeLayout) gView.findViewById(R.id.survey_btn_involved_case_car);
		gViThreePartyCarLeft = (ImageView) gView.findViewById(R.id.sicc_iv_three_party_car_left);
		rlThreePartyCar = (RelativeLayout) gView.findViewById(R.id.survey_btn_involved_three_car);
		gTvTNum = (TextView) gView.findViewById(R.id.survey_tv_totalnum);
		gViPropertyDamageLeft = (ImageView) gView.findViewById(R.id.sicc_iv_property_damage_left);
		rlPropertyDamage = (RelativeLayout) gView.findViewById(R.id.survey_btn_involved_thingloss);
		gViPeopleLeft = (ImageView) gView.findViewById(R.id.sicc_iv_people_left);
		rlPeopleDamage = (RelativeLayout) gView.findViewById(R.id.survey_btn_involved_peopleloss);
		gViDriverMsgLeft = (ImageView) gView.findViewById(R.id.sicc_iv_driver_msg_left);
		rlDriverMsg = (RelativeLayout) gView.findViewById(R.id.survey_btn_involved_driver);
		setListener();
		setView();
	}

	public void setView() {
		if (DataConfig.tblTaskQuery != null) {
			DataConfig.tblTaskQuery=TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(),DataConfig.tblTaskQuery.getRegistNo());
			carLosses = DataConfig.tblTaskQuery.getCarLossList();
			int num = 0;
			for (int i = 0; i < carLosses.size(); i++) {
				if (carLosses.get(i).getInsureCarFlag().trim().equals("0")) {
					num++;
				}
			}
			// 显示三者车个数
		    gTvTNum.setText("(" + num + ")");

			if (!carLosses.isEmpty()) {
				for (int i = 0; i < carLosses.size(); i++) {
					if ("1".equals(carLosses.get(i).getInsureCarFlag())) {
						if (TextUtils.isEmpty(carLosses.get(i).getDutyPercent())) {
							Log.i(Tag, "保存了，勾选红色变红" + carLosses.get(i).getDutyPercent());
							gViSubjectCarLeft.setBackgroundDrawable(gContext.getResources().getDrawable(R.drawable.filled));
						} else {
							Log.i(Tag, "保存了，勾选红色变灰" + carLosses.get(i).getDutyPercent());
							gViSubjectCarLeft.setBackgroundDrawable(gContext.getResources().getDrawable(R.drawable.filled));
						}
					}
					if ("0".equals(carLosses.get(i).getInsureCarFlag())) {
						if (TextUtils.isEmpty(carLosses.get(i).getNullPolicyNo()) && TextUtils.isEmpty(carLosses.get(i).getLicenseNo())
								&& TextUtils.isEmpty(carLosses.get(i).getInsurecomName()) && TextUtils.isEmpty(carLosses.get(i).getInsurecomCode())
								&& TextUtils.isEmpty(carLosses.get(i).getFrameNo()) && TextUtils.isEmpty(carLosses.get(i).getEngineNo())
								&& TextUtils.isEmpty(carLosses.get(i).getBrandName()) && TextUtils.isEmpty(carLosses.get(i).getDutyPercent())) {
							gViThreePartyCarLeft.setBackgroundDrawable(gContext.getResources().getDrawable(R.drawable.filled));
						} else {
							gViThreePartyCarLeft.setBackgroundDrawable(gContext.getResources().getDrawable(R.drawable.filled));
						}
					}
				}
			}
		}

		if (!SystemConfig.isOperate) {
			// bu可操作
			rlSubjectCar.setFocusable(false);
			rlThreePartyCar.setFocusable(false);
			rlDriverMsg.setFocusable(false);
		}else{
			rlSubjectCar.setFocusable(true);
			rlThreePartyCar.setFocusable(true);
			rlDriverMsg.setFocusable(true);
		}
	}

	protected void setListener() {
		rlSubjectCar.setOnClickListener(this);
		rlThreePartyCar.setOnClickListener(this);
		rlPropertyDamage.setOnClickListener(this);
		rlPeopleDamage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.survey_btn_involved_case_car:
			UiManager.getInstance().changeView(SunbjectCarActivity.class,false, null, false);
			break;
		// 三者车信息
		case R.id.survey_btn_involved_three_car:
			UiManager.getInstance().changeView(ThreeCarActivity.class,false, null, false);
			break;
		case R.id.survey_btn_involved_thingloss:
			Log.i(Tag, "物损界面未开发");
			break;
		case R.id.survey_btn_involved_peopleloss:
			Log.i(Tag, "人伤界面未开发");
			break;

		case R.id.survey_btn_involved_driver:
			Log.i(Tag, "界面未开发");
			break;
		}
	}

}
