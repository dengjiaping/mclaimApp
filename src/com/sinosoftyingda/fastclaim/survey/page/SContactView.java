package com.sinosoftyingda.fastclaim.survey.page;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SContactView {

	private Context context;
	private View gView;
	protected LayoutInflater inflater;// 加载xml文件的工具类
	private TextView etAssuredName;
	private EditText etAssuredPhone;
	private EditText etBaileeName;
	private EditText etBaileePhone;

	public View getView() {
		return gView;
	}

	/****
	 * 
	 * @param context
	 * @param bundle
	 */
	public SContactView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		gView = inflater.inflate(R.layout.survey_survey_contact, null);
		init();
	}

	private void init() {
		findView();
		setView();
		setListened();
	}

	private void findView() {
		// 被保险人
		etAssuredName = (TextView) gView.findViewById(R.id.survey_survey_assured_et);
		etAssuredPhone = (EditText) gView.findViewById(R.id.survey_survey_phone_et);
		etBaileeName = (EditText) gView.findViewById(R.id.survey_survey_bailee_et);
		etBaileePhone = (EditText) gView.findViewById(R.id.survey_survey_bailee_phone_et);
	}

	private void setView() {
		if (DataConfig.tblTaskQuery != null) {
			if (!TextUtils.isEmpty(DataConfig.tblTaskQuery.getInsrtedName())) {
				etAssuredName.setText(DataConfig.tblTaskQuery.getInsrtedName());
			}

			if (!TextUtils.isEmpty(DataConfig.tblTaskQuery.getInsuredMobile())) {
				etAssuredPhone.setText(DataConfig.tblTaskQuery.getInsuredMobile());
			}

			if (!TextUtils.isEmpty(DataConfig.tblTaskQuery.getEntrustName())) {
				etBaileeName.setText(DataConfig.tblTaskQuery.getEntrustName());
			}
			if (!TextUtils.isEmpty(DataConfig.tblTaskQuery.getEntrustMobile())) {
				etBaileePhone.setText(DataConfig.tblTaskQuery.getEntrustMobile());
			}
		}

		controlEd();

	}

	/*****
	 * 控制控件的操作
	 */
	public void controlEd() {
		if (!SystemConfig.isOperate) {
			// bu可操作
			// etAssuredName.setEnabled(false);
			etAssuredPhone.setEnabled(false);
			etBaileeName.setEnabled(false);
			etBaileePhone.setEnabled(false);
		} else {
			// etAssuredName.setEnabled(true);
			etAssuredPhone.setEnabled(true);
			etBaileeName.setEnabled(true);
			etBaileePhone.setEnabled(true);
		}
	}

	private void setListened() {

	}

	/*****
	 * 获取页面数据
	 */
	public void getPageValue() {

		// 被保险人电话
		DataConfig.tblTaskQuery.setInsuredMobile(etAssuredPhone.getText().toString());
		// 受托人姓名
		DataConfig.tblTaskQuery.setEntrustName(etBaileeName.getText().toString());
		// 受托人电话
		DataConfig.tblTaskQuery.setEntrustMobile(etBaileePhone.getText().toString());

	}

	/****
	 * 1 AssuredPhone
	 * 
	 * 2 BaileeName
	 * 
	 * 3 BaileePhone
	 * 
	 * @return
	 */
	public List<String> getXiePeiViewValue() {
		List<String> list = new ArrayList<String>();
		list.add(etAssuredPhone.getText().toString());
		list.add(etBaileeName.getText().toString());
		list.add(etBaileePhone.getText().toString());
		return list;
	}

	/******
	 * 协赔员显示 保险人电话 和受托人姓名和电话
	 * 
	 * @param insrtedPhone
	 * @param baileeName
	 * @param baileePhone
	 */
	public void setXiePeiViewValue() {

		etAssuredName.setText(DataConfig.tblTaskQuery.getInsrtedName());
		etAssuredPhone.setText(DataConfig.tblTaskQuery.getInsuredMobile());
		etBaileeName.setText(DataConfig.tblTaskQuery.getEntrustName());
		etBaileePhone.setText(DataConfig.tblTaskQuery.getEntrustMobile());
	}

}
