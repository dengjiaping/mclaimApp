package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.DefLossContent;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils.SpinnerOnItemClick;

/**
 * 修理厂信息
 * 
 * @author chenjianfan
 * 
 */
public class DInfoFactoryView {
	private Context context;
	private LayoutInflater inflater;
	private View gView;

	private TextView tvRepairFactory; // 修理厂
	private TextView tvCooperation; // 合作性质
	private EditText etFactoryReason; // 外修原因
	private View spinnerRepairType;
	private TextView tvAptitude; // 资质
	private ArrayList<SpinnerBean> spinnerBeans;
	private SpinnerUtils itemSuRepairType;
	private String[] repairTyps;
	private String reasonStr;
	private boolean isSet = false;

	public DInfoFactoryView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.deflossinfo_factory_item, null);
		findView();
		setListener();
	}

	private void findView() {
		tvRepairFactory = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_facname);
		tvCooperation = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_cooperation);
		spinnerRepairType = gView.findViewById(R.id.deflossinfo_factory_item_spinner_style);
		tvAptitude = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_aptitude);
		etFactoryReason = (EditText) gView.findViewById(R.id.deflossinfo_factory_item_et_reason);
		repairTyps = context.getResources().getStringArray(R.array.defloss_sp_repairtyps);

		spinnerBeans = new ArrayList<SpinnerBean>();
		SpinnerBean spinnerBean;
		for (int i = 0; i < repairTyps.length; i++) {
			spinnerBean = new SpinnerBean();
			spinnerBean.setCode(repairTyps[i].split("-")[0]);
			spinnerBean.setValue(repairTyps[i].split("-")[1]);
			spinnerBeans.add(spinnerBean);
		}
		itemSuRepairType = new SpinnerUtils(spinnerBeans, context, spinnerRepairType);

		/**
		 * 
		 */
		etFactoryReason.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				DataConfig.defLossInfoQueryData.getDefLossContent().setRepairReason(arg0.toString());
				reasonStr = etFactoryReason.getText().toString();
			}
		});

	}

	public void controlEd() {
		// 控制控件是否可以操作
		if (SystemConfig.isOperate) {
			spinnerRepairType.setEnabled(true);
			itemSuRepairType.getSpinnerEt().setEnabled(true);
			etFactoryReason.setEnabled(true);
		} else {
			spinnerRepairType.setEnabled(false);
			itemSuRepairType.getSpinnerEt().setEnabled(false);
			etFactoryReason.setEnabled(false);
		}
	}

	/****
	 * 初始化界面值
	 */
	public void setData() {
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null) {
			for (int i = 0; i < spinnerBeans.size(); i++) {
				if (spinnerBeans.get(i).getCode().equals(DataConfig.defLossInfoQueryData.getDefLossContent().getRepairMode()))
					itemSuRepairType.getSpinnerEt().setText(spinnerBeans.get(i).getValue());
			}
		}

		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getRegist() != null) {
			/* 修理厂信息 */
			DefLossContent deflossContent = DataConfig.defLossInfoQueryData.getDefLossContent();
			if (deflossContent != null) {
				String factoryName = deflossContent.getRepairFactoryName();
				String aptitudeCode = deflossContent.getRepairapTitude();
				String flag = deflossContent.getRepairCooperateFlag();
				reasonStr = deflossContent.getRepairReason();
				if (aptitudeCode != null && aptitudeCode.trim().equals("000")) {
					aptitudeCode = "4S店";
				} else if (aptitudeCode != null && aptitudeCode.trim().equals("001")) {
					aptitudeCode = "一类厂";
				} else if (aptitudeCode != null && aptitudeCode.trim().equals("002")) {
					aptitudeCode = "二类厂";
				} else if (aptitudeCode != null && aptitudeCode.trim().equals("003")) {
					aptitudeCode = "三类厂";
				}

				// add chenjianfan
				String flagName = null;
				if ("1".equals(flag)) {
					flagName = "合作";
					isSet = false;
				} else if ("0".equals(flag)) {
					flagName = "非合作";
					isSet = true;
				} else {
					flagName = "传递数据为空";
				}

				// 保存值
				tvRepairFactory.setText(factoryName);
				tvCooperation.setText(flagName);
				tvAptitude.setText(aptitudeCode);
				etFactoryReason.setText(reasonStr);
			}
		}
	}

	private void setListener() {
		// 组织下拉菜单的数据
		itemSuRepairType.OnspinnerItemClick(new SpinnerOnItemClick() {
			@Override
			public void onItemClick(String selectValue, int position) {
				if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null) {
					DefLossContent deflossContent = DataConfig.defLossInfoQueryData.getDefLossContent();
					deflossContent.setRepairMode(spinnerBeans.get(position).getCode());
					// deflossContent.setRepairReason(reasonStr);
				}
			}
		});
	}
}
