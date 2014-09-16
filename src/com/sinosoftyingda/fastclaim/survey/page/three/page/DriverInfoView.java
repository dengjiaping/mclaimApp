package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.CheckDriver;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.SetTimeUsils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;

/**
 * 驾驶员信息
 * 
 * @author Jianfan
 */
public class DriverInfoView implements OnTouchListener {
	private Context context;
	private View gView;
	private LayoutInflater inflater;
	private EditText etIdentitycard;// 身份证号
	private EditText etTime;// 领驾驶证时间
	private EditText etDrivinglicence;// 驾驶员驾驶证号码
	private List<CheckDriver> checkDrivers;
	private SetTimeUsils setTimeUsils;
	private EditText etDriverName;
	private View gVdrivercertitype;
	private String[] gSdrivercertitype=null;
	public SpinnerUtils gSudrivercertitype;
	private LinearLayout gLbeijing_drivercertitype;
	public DriverInfoView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	protected void init() {
		gView = inflater.inflate(R.layout.driverinfo, null);
		gSdrivercertitype = context.getResources().getStringArray(R.array.driver_certitype);
		
		findView();
		setView();
//		Assignment();
		setListener();
	}

	private void findView() {
		etIdentitycard = (EditText) gView.findViewById(R.id.driverinfo_et_identitycard);
		etTime = (EditText) gView.findViewById(R.id.driverinfo_et_time);
		etDrivinglicence = (EditText) gView.findViewById(R.id.driverinfo_et_drivinglicence);
		setTimeUsils = new SetTimeUsils(context);
		etDriverName = (EditText) gView.findViewById(R.id.driverinfo_et_identity_name);
		gVdrivercertitype=gView.findViewById(R.id.smp_i_driverinfo_certitype);
		gLbeijing_drivercertitype=(LinearLayout) gView.findViewById(R.id.beijing_driverinfo);
		etTime.setOnTouchListener(this);
	}

	private void setView() {
		if (DataConfig.tblTaskQuery != null) {
			if (DataConfig.tblTaskQuery.getCheckDriver() != null && !DataConfig.tblTaskQuery.getCheckDriver().isEmpty()) {
				checkDrivers = DataConfig.tblTaskQuery.getCheckDriver();
				if (checkDrivers.size() > 0) {
					if (!TextUtils.isEmpty(checkDrivers.get(0).getIdentifyNumber())) {
						etIdentitycard.setText(checkDrivers.get(0).getIdentifyNumber());
					} else {
						if (!TextUtils.isEmpty(checkDrivers.get(0).getDrivinglicenseNo())) {
							etIdentitycard.setText(checkDrivers.get(0).getDrivinglicenseNo());
						}
					}
					if (!TextUtils.isEmpty(checkDrivers.get(0).getReceivelicenseDate())) {
						etTime.setText(checkDrivers.get(0).getReceivelicenseDate());
					}
					if (!TextUtils.isEmpty(checkDrivers.get(0).getDrivinglicenseNo())) {
						etDrivinglicence.setText(checkDrivers.get(0).getDrivinglicenseNo());
					}
					if (!TextUtils.isEmpty(checkDrivers.get(0).getDriverName())) {
						etDriverName.setText(checkDrivers.get(0).getDriverName());
					}
					if ("北京".equals(SystemConfig.AREAN)) {
					gLbeijing_drivercertitype.setVisibility(View.VISIBLE);
					if(DataConfig.tblTaskQuery.getCheckDriver().get(0).getDrivercertitypeCode().trim().equals("")){
						//如果数据库中为空传入默认值，01为居民身份证
						gSudrivercertitype = getSpinnerUtils(gVdrivercertitype,gSdrivercertitype,"01");
					}else{
						gSudrivercertitype = getSpinnerUtils(gVdrivercertitype,gSdrivercertitype,DataConfig.tblTaskQuery.getCheckDriver().get(0).getDrivercertitypeCode());
					}
					}else{
						gLbeijing_drivercertitype.setVisibility(View.GONE);
					}
					
				}

			}
		}

		controlEd();
	}

	/****
	 * 控制控件的操作性
	 */
	public void controlEd() {
		if (!SystemConfig.isOperate) {
			// bu可操作
			etDrivinglicence.setEnabled(false);
			etIdentitycard.setEnabled(false);
			etDriverName.setEnabled(false);
			etTime.setEnabled(false);
			if(gSudrivercertitype!=null){
				gSudrivercertitype.getSpinnerEt().setEnabled(false);
			}
		} else {
			etDrivinglicence.setEnabled(true);
			etIdentitycard.setEnabled(true);
			etDriverName.setEnabled(true);
			etTime.setEnabled(true);
			if(gSudrivercertitype!=null){
				gSudrivercertitype.getSpinnerEt().setEnabled(true);
			}
		}
	}

	protected void setListener() {
		etIdentitycard.addTextChangedListener(new ETChangedListener());
		if(gSudrivercertitype!=null){
		gSudrivercertitype.setOnTextChangeListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
			}
			@Override
			public void afterTextChanged(Editable editable) {
				DataConfig.tblTaskQuery.getCheckDriver().get(0).setDrivercertitype(editable.toString());
				DataConfig.tblTaskQuery.getCheckDriver().get(0).setDrivercertitypeCode(gSudrivercertitype.getCode(editable.toString()));
			}
		});
		}
	}

	/****
	 * 获取页面上面的值
	 */
	public void saveData() {
		// 驾驶员信息保存
		if (DataConfig.tblTaskQuery != null) {
			if (DataConfig.tblTaskQuery.getCheckDriver() != null && !DataConfig.tblTaskQuery.getCheckDriver().isEmpty()) {

				if (DataConfig.tblTaskQuery.getCheckDriver().size() > 0) {
					DataConfig.tblTaskQuery.getCheckDriver().get(0).setIdentifyNumber(etIdentitycard.getText().toString());
					DataConfig.tblTaskQuery.getCheckDriver().get(0).setReceivelicenseDate(etTime.getText().toString());
					DataConfig.tblTaskQuery.getCheckDriver().get(0).setDrivinglicenseNo(etDrivinglicence.getText().toString());
					DataConfig.tblTaskQuery.getCheckDriver().get(0).setDriverName(etDriverName.getText().toString());
					if(gSudrivercertitype!=null){
						DataConfig.tblTaskQuery.getCheckDriver().get(0).setDrivercertitype(gSudrivercertitype.getSpinnerEt().getText().toString());
						DataConfig.tblTaskQuery.getCheckDriver().get(0).setDrivercertitypeCode(gSudrivercertitype.getCode(gSudrivercertitype.getSpinnerEt().getText().toString()));
					}
				}

			}
		}
	}

	/**
	 * 监听输入框是否有值
	 * 
	 * @author DengGuang
	 * 
	 */
	class ETChangedListener implements TextWatcher {
		@Override
		public void afterTextChanged(Editable arg0) {
			String idCardStr = etIdentitycard.getText().toString().trim();
			if (idCardStr.equals("")) {
				CheckSurveyValue.idCardIsNull = true;
			} else {
				CheckSurveyValue.idCardIsNull = false;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.driverinfo_et_time:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setTimeUsils.showDateTimePicker(etTime);
			}
			break;
		}

		return false;
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
		SpinnerUtils spinnerUtils = new SpinnerUtils(spinnerBeans, context,view);
		spinnerUtils.setId(id);
		spinnerUtils.getSpinnerEt().setText(value);
		return spinnerUtils;
	}
	

}
