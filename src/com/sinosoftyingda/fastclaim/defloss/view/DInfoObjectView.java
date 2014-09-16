package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.DefLossCarInfo;
import com.sinosoftyingda.fastclaim.common.model.ItemKind;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.SetTimeUsils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils.SpinnerOnItemClick;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DInfoObjectView implements OnTouchListener {
	private SetTimeUsils setTimeUsils;
	private Context context;
	private LayoutInflater inflater;
	private View gView;
	private TextView tvCaseCarType;// 涉案车辆
	private TextView tvCarNO;
	private TextView tvChassis;// 车架号
	private TextView tvFactoryNo;// 厂牌型号
	private LinearLayout llShangHai;// 是上海地区显示
	// 行驶证车辆描述
	private EditText etCarMiaoShu;
	// 车辆制造厂编码
	private EditText etFactoryno;
	// 车辆制造厂名称
	private EditText etFactoryName;
	//车辆初次登记日期
	private EditText etCarFirstLogin;
	// 三者新车购置价
	private View spinnerThreeCar;
	private SpinnerUtils spinnerNewCarPrice;
	private String newCarPriceArrs[];
	private List<SpinnerBean> sBeansNewCarPrice;
	private SpinnerBean sBeanDeflossType;
	//定损险别
	private View vSpinnerDeflossType;
	private LinearLayout llNewCar;
	private SpinnerBean spinnerBeanDeflossType;
	private List<SpinnerBean> listDeflossType;
	private SpinnerUtils spinnerDeflossType;
	// 数据
	private List<ItemKind> itemKinds;// 定损险别

	public DInfoObjectView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View getView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.deflossinfo_object_item, null);
		findView();
		setListener();
	}

	private void findView() {
		llNewCar = (LinearLayout) gView.findViewById(R.id.deflossinfo_boject_item_ll_newthreecar_price);
		tvCaseCarType = (TextView) gView.findViewById(R.id.deflossinfo_boject_item_tv_cartype);
		tvCarNO = (TextView) gView.findViewById(R.id.deflossinfo_boject_item_tv_cartno);
		tvChassis = (TextView) gView.findViewById(R.id.deflossinfo_boject_item_tv_chassis);
		tvFactoryNo = (TextView) gView.findViewById(R.id.deflossinfo_boject_item_tv_factoryno);
		llShangHai = (LinearLayout) gView.findViewById(R.id.deflossinfo_boject_item_shanghai);
		etCarMiaoShu = (EditText) gView.findViewById(R.id.deflossinfo_boject_item_ed_carmiaoshu);
		etFactoryno = (EditText) gView.findViewById(R.id.deflossinfo_boject_item_ed_factoryno);
		etFactoryName = (EditText) gView.findViewById(R.id.deflossinfo_boject_item_ed_factoryname);
		etCarFirstLogin = (EditText) gView.findViewById(R.id.deflossinfo_boject_item_ed_carfirstlogin);
		spinnerThreeCar = gView.findViewById(R.id.deflossinfo_boject_item_spinner_newthreecar_price);
		vSpinnerDeflossType = gView.findViewById(R.id.deflossinfo_boject_item_spinner_deflosstype);
		setTimeUsils = new SetTimeUsils(context);

		// itemKinds
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getItemKinds() != null) {
			String insurecarFlag = "1"; // 标的车1，三者车0
			for (int i = 0; i < DataConfig.defLossInfoQueryData.getDefLossCarInfos().size(); i++) {
				insurecarFlag = DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getInsurecarFlag().trim();
			}
			
			/* 第三者车辆只能选第三者责任险:B-机动车第三者责任保险条款  BZ-机动车交通事故责任强制保险     */ 
			List<ItemKind> kinds;// 定损险别
			kinds = DataConfig.defLossInfoQueryData.getItemKinds();
			itemKinds = new ArrayList<ItemKind>();
			// 筛选三者车定损险别
			if (kinds != null && insurecarFlag.equals("0")) {
				for (ItemKind itemKind : kinds) {
					String code = itemKind.getKindCode().trim(); 
					if(code.equals("B") || code.equals("BZ")){
						itemKinds.add(itemKind);
					}
				}
			}else{
				itemKinds = kinds;
			}
		}

		newCarPriceArrs = context.getResources().getStringArray(R.array.defloss_newthreecar_price);
		sBeansNewCarPrice = new ArrayList<SpinnerBean>();

		sBeanDeflossType = new SpinnerBean();
		for (int i = 0; i < newCarPriceArrs.length; i++) {
			sBeanDeflossType = new SpinnerBean();
			String str = newCarPriceArrs[i];
			sBeanDeflossType.setCode(str.split("-")[0]);
			sBeanDeflossType.setValue(str.split("-")[1]);
			sBeansNewCarPrice.add(sBeanDeflossType);
		}

		listDeflossType = new ArrayList<SpinnerBean>();

		if (itemKinds != null) {
			for (ItemKind itemKind : itemKinds) {
				spinnerBeanDeflossType = new SpinnerBean();
				spinnerBeanDeflossType.setCode(itemKind.getKindCode().trim());
				spinnerBeanDeflossType.setValue(itemKind.getKindName().trim());
				listDeflossType.add(spinnerBeanDeflossType);
			}
		}
		spinnerDeflossType = new SpinnerUtils(listDeflossType, context, vSpinnerDeflossType);
		spinnerNewCarPrice = new SpinnerUtils(sBeansNewCarPrice, context, spinnerThreeCar);
		
	}

	public void controlEd() {
		if (!SystemConfig.isOperate) {
			etCarFirstLogin.setEnabled(false);
			spinnerNewCarPrice.getSpinnerEt().setEnabled(false);
			spinnerDeflossType.getSpinnerEt().setEnabled(false);
			etCarMiaoShu.setEnabled(false);
			etFactoryno.setEnabled(false);
			etFactoryName.setEnabled(false);
		} else {
			etCarFirstLogin.setEnabled(true);
			spinnerNewCarPrice.getSpinnerEt().setEnabled(true);
			spinnerDeflossType.getSpinnerEt().setEnabled(true);
			etCarMiaoShu.setEnabled(true);
			etFactoryno.setEnabled(true);
			etFactoryName.setEnabled(true);
		}
	}

	private void setListener() {

		etCarFirstLogin.setOnTouchListener(this);
		// 险别保存
		spinnerDeflossType.OnspinnerItemClick(new SpinnerOnItemClick() {
			@Override
			public void onItemClick(String selectValue, int position) {
				DataConfig.defLossInfoQueryData.getDefLossContent().setDefLossRiskCode(listDeflossType.get(position).getCode());
			}
		});
		/***
		 * 选择三着车新车价保存
		 */
		spinnerNewCarPrice.OnspinnerItemClick(new SpinnerOnItemClick() {
			@Override
			public void onItemClick(String selectValue, int position) {
				
				if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossCarInfos() != null) {
					if (!DataConfig.defLossInfoQueryData.getDefLossCarInfos().isEmpty()) {
						DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).setNewPruchaseAmount(sBeansNewCarPrice.get(position).getValue());//by jingtuo
					}
				}
			}
		});
	}

	public void setData() {
		if ("上海".equals(SystemConfig.AREAN)) {
			List<DefLossCarInfo> defLossCarInfos = null;
			if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossCarInfos() != null) {
				defLossCarInfos = DataConfig.defLossInfoQueryData.getDefLossCarInfos();
			}
			
			if (llShangHai.getVisibility() == View.GONE)
				llShangHai.setVisibility(View.VISIBLE);
			// 保存操作
			if (defLossCarInfos != null && defLossCarInfos.size() > 0) {
				// 行驶证车辆描述
				etCarMiaoShu.setText(defLossCarInfos.get(0).getCarVehicleDesc());
				// 车辆制造厂编码
				etFactoryno.setText(defLossCarInfos.get(0).getCarFactoryCode());
				// 车辆制造厂名称
				etFactoryName.setText(defLossCarInfos.get(0).getCarFactoryName());
				// 车辆初次登记日期
				etCarFirstLogin.setText(defLossCarInfos.get(0).getCarRegisterDate());
			}

		} else {
			// 登录用户不是上海，不显示
			if (llShangHai.getVisibility() == View.VISIBLE)
				llShangHai.setVisibility(View.GONE);
		}

		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossCarInfos() != null) {
			if (!DataConfig.defLossInfoQueryData.getDefLossCarInfos().isEmpty()) {
				for (int i = 0; i < DataConfig.defLossInfoQueryData.getDefLossCarInfos().size(); i++) {
					if ("1".equals(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getInsurecarFlag())) {
						// 标的车
						if (llNewCar.getVisibility() == View.VISIBLE)
							llNewCar.setVisibility(View.GONE);
						
						SystemConfig.JYTCAR_TYPE = 1;
						tvCaseCarType.setText("标的车");
						tvCarNO.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getLicenseNo());
						tvChassis.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getCarframeNo());
						tvFactoryNo.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getBrandName());
					} else {
						if (llNewCar.getVisibility() == View.GONE)
							llNewCar.setVisibility(View.VISIBLE);

						SystemConfig.JYTCAR_TYPE = 0;
						tvCaseCarType.setText("三者车");
						tvCarNO.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getLicenseNo());
						tvChassis.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getCarframeNo());
						tvFactoryNo.setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getBrandName());
						// 如果有值赋值
						if (!TextUtils.isEmpty(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getNewPruchaseAmount())) {
							spinnerNewCarPrice.getSpinnerEt().setText(DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(i).getNewPruchaseAmount());//by jingtuo
						}
					}
				}
			} else {
				tvCaseCarType.setText("");
				tvCarNO.setText("");
				tvChassis.setText("");
				tvFactoryNo.setText("");
			}
		}

		// 定损险别
		if (DataConfig.defLossInfoQueryData != null && DataConfig.defLossInfoQueryData.getDefLossContent() != null) {
			for (int i = 0; i < itemKinds.size(); i++) {
				if (itemKinds.get(i).getKindCode().equals(DataConfig.defLossInfoQueryData.getDefLossContent().getDefLossRiskCode()))
					spinnerDeflossType.getSpinnerEt().setText(itemKinds.get(i).getKindName());
			}

		}
	}

	/****
	 * 保存数据
	 */
	public void saveData() {
		if (!TextUtils.isEmpty(etCarMiaoShu.getText().toString())) {
			// 行驶证车辆描述
			DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).setCarVehicleDesc(etCarMiaoShu.getText().toString());
		}
		if (!TextUtils.isEmpty(etFactoryno.getText().toString())) {
			// 车辆制造厂编码
			DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).setCarFactoryCode(etFactoryno.getText().toString());
		}
		if (!TextUtils.isEmpty(etFactoryName.getText().toString())) {
			// 车辆制造厂名称
			DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).setCarFactoryName(etFactoryName.getText().toString());
		}
		if (!TextUtils.isEmpty(etCarFirstLogin.getText().toString())) {
			DataConfig.defLossInfoQueryData.getDefLossCarInfos().get(0).setCarRegisterDate(etCarFirstLogin.getText().toString());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.deflossinfo_boject_item_ed_carfirstlogin:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setTimeUsils.showDateTimePicker(etCarFirstLogin);
			}
			break;
		}
		return false;
	}

}
