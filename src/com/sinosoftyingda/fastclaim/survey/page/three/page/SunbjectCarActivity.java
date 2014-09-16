package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.survey.page.SSurveyActivity;

/**
 * 标的车
 * 
 * @author jianfan
 * 
 */
public class SunbjectCarActivity extends BaseView implements OnClickListener {
	private static final String Tag = "SunbjectCarActivity";
	private View layout;
	private EditText etDuty;
	private Button btnSave;
	private PromptManager promptManager;
	private List<CarLoss> carLosses;
	private TextView tvSunbjectCarNo;
	private TextView tvSunbjectframeNo;
	private TextView tvSunbjectBrandName;

	public SunbjectCarActivity(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public View getView() {
		return layout;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		// 加载布局文件
		layout = inflater.inflate(R.layout.sunbjectcar, null);
		// 填充布局
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(
				SystemConfig.dbhelp.getWritableDatabase(),
				DataConfig.tblTaskQuery.getRegistNo());
		if (DataConfig.tblTaskQuery != null) {
			carLosses = DataConfig.tblTaskQuery.getCarLossList();
			for (int i = 0; i < carLosses.size(); i++) {
				System.out.println("涉损车辆="
						+ carLosses.get(i).getInsureCarFlag() + "车牌号"
						+ carLosses.get(i).getLicenseNo());
			}
		}
		findView();
		setView();
	}

	private void findView() {
		etDuty = (EditText) layout.findViewById(R.id.sunbject_duty_et_value);
		btnSave = (Button) layout.findViewById(R.id.sunbject_btn_save);
		tvSunbjectCarNo = (TextView) layout
				.findViewById(R.id.sunbject_carno_tv_value);
		tvSunbjectBrandName = (TextView) layout
				.findViewById(R.id.sunbject_factory_tv_value);
		tvSunbjectframeNo = (TextView) layout
				.findViewById(R.id.sunbject_chassis_tv_value);
	}

	private void setView() {
		promptManager = new PromptManager();
		// 标的车
		if (carLosses != null && carLosses.size() >= 1) {
			for (int i = 0; i < carLosses.size(); i++) {
				if ("1".equals(carLosses.get(i).getInsureCarFlag())) {
					tvSunbjectCarNo.setText(carLosses.get(i).getLicenseNo());
					tvSunbjectBrandName.setText(carLosses.get(i).getBrandName());
					tvSunbjectframeNo.setText(carLosses.get(i).getFrameNo());
					// 判断是否带过来为默认值
					String percentStr = carLosses.get(i).getDutyPercent().trim();
					etDuty.setText(percentStr);
				
				}
			}
		}
	}

	@Override
	protected void setListener() {
		btnSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sunbject_btn_save:

			if (TextUtils.isEmpty(etDuty.getText().toString().trim())) {
				Toast.showToast(context, "责任比例为空");
			} else {
				// 标的车
				for (int i = 0; i < DataConfig.tblTaskQuery.getCarLossList()
						.size(); i++) {
					if ("1".equals(carLosses.get(i).getInsureCarFlag())) {
						String dutyPercent = etDuty.getText().toString().trim();
						DataConfig.tblTaskQuery.getCarLossList().get(i)
								.setDutyPercent(dutyPercent);
						TblTaskQuery.insertOrUpdate(
								SystemConfig.dbhelp.getWritableDatabase(),
								DataConfig.tblTaskQuery, false, true, true);

						UiManager.getInstance().changeView(

						SSurveyActivity.class, null, false);
						break;

					}
				}
			}

		}
	}

	@Override
	public void onPause() {
		Log.i(Tag, "标的车保存吗？");
		super.onPause();
	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_ObjectCar;
	}

	@Override
	public Integer getBackMain() {

		return null;
	}

	@Override
	public void onResume() {
		for (int i = 0; i < DataConfig.tblTaskQuery.getCarLossList().size(); i++) {
			if ("1".equals(carLosses.get(i).getInsureCarFlag())) {
				DataConfig.tblTaskQuery.getCarLossList().get(i).setCarNum("1");
			}
		}

		if (!SystemConfig.isOperate) {
			btnSave.setEnabled(false);
			etDuty.setEnabled(false);
		} else {
			btnSave.setEnabled(true);
			etDuty.setEnabled(true);

		}
		super.onResume();
	}

}
