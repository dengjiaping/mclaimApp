package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.CarLoss;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils;
import com.sinosoftyingda.fastclaim.common.ui.utils.SpinnerUtils.SpinnerOnItemClick;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.survey.page.SSurveyActivity;
import com.sinosoftyingda.fastclaim.survey.utils.CommonUtils;

/**
 * 增加三者车
 * 
 * @author DengGuang
 * 
 */
public class ThreeCarActivity extends BaseView implements OnClickListener {
	private View			gView;

	// 三者车信息UI
	private Button			gBtnSave;
	private LinearLayout	gBtnAddTCar;
	// 新增布局UI(三者车)
	private LinearLayout	gLayTCars;
	private List<CarLoss>	tCarLists;
	// 对话框
	private PromptManager	promptManager;
	private String strComcode="";
	public ThreeCarActivity(Context context, Bundle bundle) {
		super(context, bundle);

	}

	@Override
	public View getView() {
		return gView;
	}

	@Override
	public Integer getType() {
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		gView = inflater.inflate(R.layout.threecar, null);
		initThreeCarUI(gView);
		DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
		tCarLists = DataConfig.tblTaskQuery.getCarLossList();
		strComcode=DataConfig.tblTaskQuery.getRegistNo().toString().substring(1,3);
		for (int i = 0; i < tCarLists.size(); i++) {
			Log.d("tCarLists", i + "--->" + tCarLists.get(i).getCarNum());
		}
		setTCarinfo(tCarLists);
		promptManager = new PromptManager();
	}

	/**
	 * 初始化三者车信息页面
	 */
	private void initThreeCarUI(View view) {
		// 初始化组件
		gBtnSave = (Button) view.findViewById(R.id.threecar_btn_save);
		gBtnAddTCar = (LinearLayout) view.findViewById(R.id.threecar_car_btn_add);
		gLayTCars = (LinearLayout) view.findViewById(R.id.threecar_lay_threecars);
	}

	/**
	 * 单个三者车
	 * 
	 * @author DengGuang
	 */
	class ViewHolder {
		int				itemIndex;				// 序号
		ImageView		itemDel;				// 删除
		TextView		itemID;				// 三者车编号
		EditText		itemPlate;				// 车牌
		EditText		itemVin;				// 车架号
		EditText		itemFactoryType;		// 厂牌型号
		EditText		itemBlameNum;			// 责任比例
		EditText		itemClaimNo;			// 报案号
		EditText		itemEngine;				// 发动机号
		EditText		itemdrivername;			// 驾驶员姓名（三者车，浙江）
		EditText		itemdrivercode;			// 驾驶员证件号码（三者车，浙江）

		View			itemVCartype;			// 车辆种类
		View			itemVCompany;			// 承保公司
		View			itemVPlateType;			// 号牌类型
		View			itemVCertitype;			// 驾驶员证件类型（三者车，浙江）

		SpinnerUtils	itemSuCartype;
		SpinnerUtils	itemSuCompany;
		SpinnerUtils	itemSuPlateType;
		SpinnerUtils	itemSuCertitype;			// 驾驶员证件类型（三者车，浙江）
		
		
		
		

		// 公司代码
		String			companyCode		= "";
		// 号牌名称
		String			plateTypeCode	= "";
		// 车辆种类代码
		String			carTypeCode		= "";
		//驾驶员证件类型代码
		String 			itemCertitypeCode	="";

		public String getItemCertitypeCode() {
			return itemCertitypeCode;
		}

		public void setItemCertitypeCode(String itemCertitypeCode) {
			this.itemCertitypeCode = itemCertitypeCode;
		}

		public String getCompanyCode() {
			return companyCode;
		}

		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}

		public String getPlateTypeCode() {
			return plateTypeCode;
		}

		public void setPlateTypeCode(String plateTypeCode) {
			this.plateTypeCode = plateTypeCode;
		}

		public String getCarTypeCode() {
			return carTypeCode;
		}

		public void setCarTypeCode(String carTypeCode) {
			this.carTypeCode = carTypeCode;
		}
	}

	/**
	 * 添加三者车信息
	 * 
	 * @param carLists
	 */
	private void setTCarinfo(List<CarLoss> items) {
		LinearLayout layCliam;

		for (int i = 0; i < items.size(); i++) {
			// 判断是否为三者车信息
			if (items.get(i).getInsureCarFlag().trim().equals("0")) {
				// 显示三者车信息
				LayoutInflater gInflater = LayoutInflater.from(context);
				layCliam = (LinearLayout) gInflater.inflate(R.layout.threecar_dongtai, null);
				// 声明组件
				final ViewHolder holder = new ViewHolder();
				holder.itemIndex = i;
				holder.itemDel = (ImageView) layCliam.findViewById(R.id.threecar_imv_delete);
				holder.itemID = (TextView) layCliam.findViewById(R.id.threecar_tv_threecarname);
				holder.itemPlate = (EditText) layCliam.findViewById(R.id.threecar_ed_carno); // 车牌
				holder.itemVin = (EditText) layCliam.findViewById(R.id.threecar_ed_chassis); // 车架号
				holder.itemFactoryType = (EditText) layCliam.findViewById(R.id.threecar_ed_factory); // 厂牌型号
				holder.itemBlameNum = (EditText) layCliam.findViewById(R.id.threecar_ed_duty); // 责任比例
				holder.itemClaimNo = (EditText) layCliam.findViewById(R.id.threecar_ed_policyno); // 报案号
				holder.itemEngine = (EditText) layCliam.findViewById(R.id.threecar_ed_engineno); // 发动机号
				holder.itemdrivername = (EditText) layCliam.findViewById(R.id.threecar_ed_nulldrivername); // 驾驶员姓名
				holder.itemdrivercode = (EditText) layCliam.findViewById(R.id.threecar_ed_nulldrivercode); // 驾驶员证件号码

				// 车辆种类
				String[] carTypeS = context.getResources().getStringArray(R.array.threecar_cartype);
				holder.itemVCartype = layCliam.findViewById(R.id.threecar_spinner_insurance_cartype); // 车牌种类
				holder.itemSuCartype = spinnerValues(holder.itemVCartype, carTypeS);
				String carKindCode = items.get(i).getCarKindCode();
				holder.setCarTypeCode(carKindCode);
				for (int arr = 0; arr < carTypeS.length; arr++) {
					String[] carKindStrs = carTypeS[arr].split("-");
					String code = carKindStrs[0];
					String name = carKindStrs[1];
					if (carKindCode.equals(code)) {
						holder.itemSuCartype = spinnerValues(holder.itemVCartype, carTypeS);
						holder.itemSuCartype.getSpinnerEt().setText(name);
					}
				}

				// 承保公司
				String[] companyS = context.getResources().getStringArray(R.array.threecar_insurecomname);
				holder.itemVCompany = layCliam.findViewById(R.id.threecar_spinner_insurance_company); // 承保公司
				holder.itemSuCompany = spinnerValues(holder.itemVCompany, companyS);
				String companyCode = items.get(i).getInsurecomCode();
				holder.setCompanyCode(companyCode);
				for (int arr = 0; arr < companyS.length; arr++) {
					String[] companyStrs = companyS[arr].split("-");
					String code = companyStrs[0];
					String name = companyStrs[1];
					if (companyCode.equals(code)) {
						holder.itemSuCompany = spinnerValues(holder.itemVCompany, companyS);
						holder.itemSuCompany.getSpinnerEt().setText(name);
					}
				}

				// 号牌种类
				String[] plateS = context.getResources().getStringArray(R.array.threecar_platetype);
				holder.itemVPlateType = layCliam.findViewById(R.id.threecar_spinner_flappertype);
				holder.itemSuPlateType = spinnerValues(holder.itemVPlateType, plateS);
				String plateCode = items.get(i).getLicenseType();
				holder.setPlateTypeCode(plateCode);
				for (int arr = 0; arr < plateS.length; arr++) {
					String[] plateStrs = plateS[arr].split("-");
					String code = plateStrs[0];
					String name = plateStrs[1];
					if (plateCode.equals(code)) {
						holder.itemSuPlateType = spinnerValues(holder.itemVPlateType, plateS);
						holder.itemSuPlateType.getSpinnerEt().setText(name);
					}
				}
				// 驾驶员证件类型（浙江）
				String[] strNullcertitype = context.getResources().getStringArray(R.array.threecar_nullcertitype);
				holder.itemVCertitype = layCliam.findViewById(R.id.threecar_spinner_nullcertitype);
				holder.itemSuCertitype = spinnerValues(holder.itemVCertitype, strNullcertitype);
				String strCertitypeCode = items.get(i).getNullCertitypeCode();
				if(strCertitypeCode==null){
					strCertitypeCode="";
				}
				holder.setItemCertitypeCode(strCertitypeCode);
				for (int arr = 0; arr < strNullcertitype.length; arr++) {
					String[] plateStrs = strNullcertitype[arr].split("-");
					String code = plateStrs[0];
					String name = plateStrs[1];
					if (strCertitypeCode.equals(code)) {
						holder.itemSuCertitype = spinnerValues(holder.itemVCertitype, strNullcertitype);
						holder.itemSuCertitype.getSpinnerEt().setText(name);
					}
				}
				// 设置值
				final CarLoss sThreeCar = items.get(i);
				String strItemBlameNum = sThreeCar.getDutyPercent();
				holder.itemID.setText("三者车" + (holder.itemIndex));
				holder.itemPlate.setText(sThreeCar.getLicenseNo());
				holder.itemVin.setText(sThreeCar.getFrameNo());
				holder.itemFactoryType.setText(sThreeCar.getBrandName());
				holder.itemBlameNum.setText(strItemBlameNum);
				holder.itemClaimNo.setText(sThreeCar.getNullPolicyNo());
				holder.itemEngine.setText(sThreeCar.getEngineNo());
				
				holder.itemdrivername.setText(sThreeCar.getNullDriverName());
				holder.itemdrivercode.setText(sThreeCar.getNullDriverCode());

				// 动态保存值
				holder.itemPlate.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemVin.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemFactoryType.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemBlameNum.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemClaimNo.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemEngine.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemdrivercode.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemdrivername.addTextChangedListener(new ItemEdittextOnFocusListener(holder, i));
				holder.itemSuCartype.OnspinnerItemClick(new SpinnerOnItemClick() {
					@Override
					public void onItemClick(String selectValue, int position) {
						String carNameStr = holder.itemSuCartype.getSpinnerBeans().get(holder.itemSuCartype.getId()).getValue();
						String carCodeStr = holder.itemSuCartype.getSpinnerBeans().get(holder.itemSuCartype.getId()).getCode();
						sThreeCar.setInsurecomCode(carCodeStr);
						sThreeCar.setInsurecomName(carNameStr);
						holder.setCarTypeCode(carCodeStr);

						// 保存下拉数据
						getItemValues(holder, holder.itemIndex);

					}
				});
				holder.itemSuCompany.OnspinnerItemClick(new SpinnerOnItemClick() {
					@Override
					public void onItemClick(String selectValue, int position) {
						String companyNameStr = holder.itemSuCompany.getSpinnerBeans().get(holder.itemSuCompany.getId()).getValue();
						String companyCodeStr = holder.itemSuCompany.getSpinnerBeans().get(holder.itemSuCompany.getId()).getCode();
						sThreeCar.setInsurecomCode(companyCodeStr);
						sThreeCar.setInsurecomName(companyNameStr);
						holder.setCompanyCode(companyCodeStr);

						// 保存下拉数据
						getItemValues(holder, holder.itemIndex);
					}
				});
				holder.itemSuPlateType.OnspinnerItemClick(new SpinnerOnItemClick() {
					@Override
					public void onItemClick(String selectValue, int position) {
						String plateNameStr = holder.itemSuPlateType.getSpinnerBeans().get(holder.itemSuPlateType.getId()).getValue();
						String plateCodeStr = holder.itemSuPlateType.getSpinnerBeans().get(holder.itemSuPlateType.getId()).getCode();
						sThreeCar.setLicenseType(plateNameStr);
						sThreeCar.setLicenseTypeCode(plateCodeStr);
						holder.setPlateTypeCode(plateCodeStr);

						// 保存下拉数据
						getItemValues(holder, holder.itemIndex);
					}
				});
				// 驾驶员证件类型（浙江）
				holder.itemSuCertitype.OnspinnerItemClick(new SpinnerOnItemClick() {
					@Override
					public void onItemClick(String selectValue, int position) {
						String strCertitype 	= holder.itemSuCertitype.getSpinnerBeans().get(holder.itemSuCertitype.getId()).getValue();
						String strCertitypeCode = holder.itemSuCertitype.getSpinnerBeans().get(holder.itemSuCertitype.getId()).getCode();
						sThreeCar.setNullCertiType(strCertitype);
						sThreeCar.setNullCertitypeCode(strCertitypeCode);
						holder.setItemCertitypeCode(strCertitypeCode);
						// 保存下拉数据
						getItemValues(holder, holder.itemIndex);
					}
				});
				
				
				
				

				// 后台带过来的三者车不能删除
				String isEditedStr = sThreeCar.getIsEdited().trim();
				if (isEditedStr != null && isEditedStr.equals("1")) {
					holder.itemDel.setEnabled(true);
					holder.itemDel.setOnClickListener(new ItemBtnOnClickListener(sThreeCar, holder.itemIndex));
					holder.itemDel.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.third_deleteicon));
				} else {
					holder.itemDel.setEnabled(false);
					holder.itemDel.setBackgroundDrawable(null);
				}
				if (!SystemConfig.isOperate) {
					holder.itemDel.setEnabled(false);
					holder.itemID.setEnabled(false);
					holder.itemPlate.setEnabled(false);
					holder.itemVin.setEnabled(false);
					holder.itemFactoryType.setEnabled(false);
					holder.itemBlameNum.setEnabled(false);
					holder.itemClaimNo.setEnabled(false);
					holder.itemEngine.setEnabled(false);
					holder.itemSuCompany.getSpinnerEt().setEnabled(false);
					holder.itemSuPlateType.getSpinnerEt().setEnabled(false);
					holder.itemSuCartype.getSpinnerEt().setEnabled(false);

					holder.itemSuCertitype.getSpinnerEt().setEnabled(false);
					holder.itemdrivercode.setEnabled(false);
					holder.itemdrivername.setEnabled(false);
				} else {
					holder.itemDel.setEnabled(true);
					holder.itemID.setEnabled(true);
					holder.itemPlate.setEnabled(true);
					holder.itemVin.setEnabled(true);
					holder.itemFactoryType.setEnabled(true);
					holder.itemBlameNum.setEnabled(true);
					holder.itemClaimNo.setEnabled(true);
					holder.itemEngine.setEnabled(true);
					holder.itemSuCompany.getSpinnerEt().setEnabled(true);
					holder.itemSuPlateType.getSpinnerEt().setEnabled(true);
					holder.itemSuCartype.getSpinnerEt().setEnabled(true);
					//浙江分公司可对以下三个字段进行编辑
					holder.itemSuCertitype.getSpinnerEt().setEnabled(true);
					holder.itemdrivercode.setEnabled(true);
					holder.itemdrivername.setEnabled(true);
					
				}
				// 新增三者车
				gLayTCars.addView(layCliam);
			}
		}
	}

	/**
	 * 设置SpinnerUtils
	 * 
	 * @return
	 */
	private SpinnerUtils spinnerValues(View view, String[] strs) {
		ArrayList<SpinnerBean> spinnerBeans = new ArrayList<SpinnerBean>();
		for (int i = 0; i < strs.length; i++) {
			String[] str = strs[i].split("-");
			SpinnerBean bean = new SpinnerBean();
			bean.setCode(str[0]);
			bean.setValue(str[1]);
			spinnerBeans.add(bean);
		}
		return new SpinnerUtils(spinnerBeans, context, view);
	}

	/**
	 * Item选项点击事件
	 * 
	 * @author DengGuang
	 */
	class ItemBtnOnClickListener implements OnClickListener {
		private int		index;
		private CarLoss	sItem;

		private ItemBtnOnClickListener(CarLoss sItem, int index) {
			this.sItem = sItem;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/* 删除按钮 */
			case R.id.threecar_imv_delete:
				delThreeCarItem(index);
				break;
			}
		}
	}

	/**
	 * 删除三者车
	 * 
	 * @param delIndex
	 */
	private void delThreeCarItem(final int delIndex) {
		promptManager.showDialog(context, "是否删除【三者车" + (delIndex) + "】？", R.string.yes, R.string.no, new ShowDialogPositiveButton() {

			@Override
			public void setPositiveButton() {
				tCarLists.remove(delIndex);
				gLayTCars.removeAllViews();
				setTCarinfo(tCarLists);
			}

			@Override
			public void setNegativeButton() {

			}
		});
	}

	/**
	 * 输入框事件
	 * 
	 * @author DengGuang
	 */
	class ItemEdittextOnFocusListener implements TextWatcher {
		ViewHolder	holder;
		int			index;

		public ItemEdittextOnFocusListener(ViewHolder holder, int index) {
			super();
			this.index = index;
			this.holder = holder;
		}

		@Override
		public void afterTextChanged(Editable s) {
			getItemValues(holder, index);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	}

	/**
	 * 获取单个Item值
	 * 
	 * @param holder
	 * @param index
	 */
	private void getItemValues(ViewHolder holder, int index) {
		String strItemPlate = holder.itemPlate.getText().toString(); // 车牌
		String strCarTypeName = holder.itemSuCartype.getSpinnerEt().getText().toString(); // 车辆种类
		String strCarTypeCode = holder.getCarTypeCode();
		String strItemVin = holder.itemVin.getText().toString(); // 发动机号
		String strItemFactoryType = holder.itemFactoryType.getText().toString(); // 厂牌型号
		String strItemBlameNum = holder.itemBlameNum.getText().toString(); // 责任比例
		String strItemClaimNo = holder.itemClaimNo.getText().toString(); // 报案号
		String strItemInsurecomName = holder.itemSuCompany.getSpinnerEt().getText().toString(); // 公司名称
		String strItemInsurecomCode = holder.getCompanyCode();
		String strItemEngine = holder.itemEngine.getText().toString(); // 发动机号
		String strItemCarPlateName = holder.itemSuPlateType.getSpinnerEt().getText().toString(); // 车辆种类
		String strItemCarPlateCode = holder.getPlateTypeCode(); // 号牌种类
		String strDrivername = holder.itemdrivername.getText().toString(); 				   	// 驾驶员姓名(三者车，浙江需求)
		String strItemCertitype = holder.itemSuCertitype.getSpinnerEt().getText().toString(); // 驾驶员证件类型(三者车，浙江需求)
		String strItemCertitypeCode = holder.getItemCertitypeCode(); 						// 驾驶员证件类型代码(三者车，浙江需求)
		String strDriverCode = holder.itemdrivercode.getText().toString(); 				   	// 驾驶员证件号码(三者车，浙江需求)
		
		CarLoss carlossInfo = tCarLists.get(index);
		carlossInfo.setLicenseNo(strItemPlate.trim());
		carlossInfo.setLicenseType(strItemCarPlateCode.trim());
		carlossInfo.setFrameNo(strItemVin.trim());
		carlossInfo.setBrandName(strItemFactoryType.trim());
		carlossInfo.setDutyPercent(strItemBlameNum.trim());
		carlossInfo.setNullPolicyNo(strItemClaimNo.trim());
		carlossInfo.setInsurecomCode(strItemInsurecomCode.trim());
		carlossInfo.setInsurecomName(strItemInsurecomName.trim());
		carlossInfo.setEngineNo(strItemEngine.trim());
		carlossInfo.setCarKindCode(strCarTypeCode.trim());
		
		carlossInfo.setNullDriverName(strDrivername.trim());
		carlossInfo.setNullDriverCode(strDriverCode.trim());
		carlossInfo.setNullCertiType(strItemCertitype.trim());
		carlossInfo.setNullCertitypeCode(strItemCertitypeCode.trim());
	}

	@Override
	protected void setListener() {
		gBtnSave.setOnClickListener(this);
		gBtnAddTCar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 保存
		case R.id.threecar_btn_save:
			String message = checkPlateNo();
			if (message.equals("YES")) {
				// 保存数据
				DataConfig.tblTaskQuery.setCarLossList(tCarLists);
				TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, false, true, false);
				UiManager.getInstance().changeView(SSurveyActivity.class, null, false);
			} else {
				Toast.showToast(context, message);
			}
			break;

		// 增加三者车
		case R.id.threecar_car_btn_add:
			tCarLists.add(addNewSThreeCar());
			// 移除所有组件
			gLayTCars.removeAllViews();
			setTCarinfo(tCarLists);
			break;
		}
	}

	/**
	 * 三者车保存规则校验方法 for update By haoyun 20130507
	 * 
	 * @return
	 */
	private String checkPlateNo() {
		String msg = "YES";
		strComcode=DataConfig.tblTaskQuery.getRegistNo().toString().substring(1,3);
		// 只有一个标的车 则不需要走下面判断
		if (tCarLists.size() == 1) {
			return msg;
		}
		/**
		 * 判断三者车车牌号是否和标的车相同
		 */
		for (int i = 1; i < tCarLists.size(); i++) {
			if (tCarLists.get(0).getLicenseNo().equals(tCarLists.get(i).getLicenseNo())) {
				return "三者车不能与标的车【号牌号码】重复！";
			}
		}
		/**
		 * 校验三者车车牌号码
		 */
		for (int i = 1; i < tCarLists.size(); i++) {
			String plateNo = tCarLists.get(i).getLicenseNo().trim();
			String tFlag = tCarLists.get(i).getInsureCarFlag().trim();

			// 车牌号不为空
			if (!plateNo.trim().equals("") && tFlag.equals("0")) {
				if (!CommonUtils.vehicleNoStyleMethod(plateNo))
					return "三者车【车牌号码】错误！";
			}
			if (TextUtils.isEmpty(tCarLists.get(i).getNullPolicyNo()))// 三者车保单号不能为空
			{
				return "三者车【保单号】不能为空！";
			}

			// 如果是北京的案子 车架号 与发动机号为必填
			if ("北京".equals(SystemConfig.AREAN)) {
				// 车架号非空校验
				if (TextUtils.isEmpty(tCarLists.get(i).getFrameNo())) {
					return "三者车【车架号】不能为空！";
				}// 发动机号非空校验
				else if (TextUtils.isEmpty(tCarLists.get(i).getEngineNo())) {
					return "三者车【发动机号】不能为空！";
				}
			}
			// 如果是浙江的案子 驾驶员姓名，驾驶员证件类型，驾驶员证件号码 不能为空
			if (strComcode.equals("18")) {
				// 车架号非空校验
				if (TextUtils.isEmpty(tCarLists.get(i).getNullDriverName())) {
					return "三者车【驾驶员姓名】不能为空！并确认驾驶员证件类型";
				}// 发动机号非空校验
				else if (TextUtils.isEmpty(tCarLists.get(i).getNullDriverCode())) {
					return "三者车【驾驶员证件号码】不能为空！并确认驾驶员证件类型";
				}
			}
			
			
			// 号牌号码不能重复
			int index = 0;
			for (int j = 0; j < tCarLists.size(); j++) {
				if (tCarLists.get(j).getLicenseNo().equals(tCarLists.get(i).getLicenseNo())) {
					index++;
					if (index > 1) {

						return "三者车【号牌号码】不能重复！";

					}
				}
			}

		}

		return msg;
	}

	/**
	 * 添加三者车信息
	 * 
	 * @return
	 */
	private CarLoss addNewSThreeCar() {
		CarLoss sThreeCar = new CarLoss();
		sThreeCar.setIsEdited("1");
		sThreeCar.setInsureCarFlag("0");
		sThreeCar.setCarNum("" + (tCarLists.size() + 1));
		sThreeCar.setDutyPercent("0.0");
		sThreeCar.setCarKindCode("A0");
		sThreeCar.setLicenseNo("");
		sThreeCar.setBrandName("");
		sThreeCar.setFrameNo("");
		sThreeCar.setNullPolicyNo("");
		sThreeCar.setEngineNo("");
		sThreeCar.setInsurecomCode("00120");
		sThreeCar.setInsurecomName("");
		sThreeCar.setLicenseTypeCode("99");
		sThreeCar.setLicenseType("99");
		sThreeCar.setNullDriverCode("");
		sThreeCar.setNullDriverName("");
		sThreeCar.setNullCertitypeCode("99");
		sThreeCar.setNullCertiType("");
		return sThreeCar;
	}

	@Override
	public Integer getExit() {
		return ConstantValue.Page_Title_ThreeCar;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	@Override
	public void onResume() {
		if (!SystemConfig.isOperate) {
			gBtnSave.setEnabled(false);
			gBtnAddTCar.setEnabled(false);
		} else {
			gBtnSave.setEnabled(true);
			gBtnAddTCar.setEnabled(true);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
