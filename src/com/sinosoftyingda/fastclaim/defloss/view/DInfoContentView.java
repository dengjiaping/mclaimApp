package com.sinosoftyingda.fastclaim.defloss.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.DeflossSynchroAccess;
import com.sinosoftyingda.fastclaim.common.model.DefLossInfoQueryResponse;
import com.sinosoftyingda.fastclaim.common.model.VerifyLossSubmitRequest;
import com.sinosoftyingda.fastclaim.common.utils.MoneyUtil;
import com.sinosoftyingda.fastclaim.defloss.service.JYLioneyeToolsActivity;
import com.sinosoftyingda.fastclaim.defloss.util.UtilIsNotNull;

/**
 * 定损内容，调用精友定损工具
 * 
 * @author DengGuang
 */
public class DInfoContentView implements OnClickListener {
	private Context context;
	private LayoutInflater inflater;
	private View gView;

	private Button btnSelect;
	private TextView tvSumChgComFee;
	private TextView tvSumRepairFee;
	private TextView tvRemnant;
	private TextView tvLossFeff;
	private TextView tvLossBigFeff;
	private EditText etLossShijiufee; // 外修原因
	private String shijiufee;	//施救费 add by yxf 20140211 reason:增加施救费字段
	public View getView() {
		return gView;
	}

	public DInfoContentView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		gView = inflater.inflate(R.layout.deflossinfo_deflosscontent_item, null);
		findView();
		setView();
		setListener();
	}

	public void setData() {
		// 定损内容显示
		if(DataConfig.defLossInfoQueryData!=null&&DataConfig.defLossInfoQueryData.getRegist()!=null){
			VerifyLossSubmitRequest deflossContent = DeflossSynchroAccess.find(DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), DataConfig.defLossInfoQueryData.getRegist().getLossNo());
			DataConfig.defLossInfoQueryData = CertainLossInfoAccess.getLossTaskQuery(SystemConfig.dbhelp.getReadableDatabase(), DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), DataConfig.defLossInfoQueryData.getRegist().getLossNo());
			if (deflossContent != null) {
				String sumFitsfeeStr = deflossContent.getSumfitsfee();
				String sumRepairfeeStr = deflossContent.getSumrepairfee();
				String sumVeriRestStr = deflossContent.getSumverirest();
				String sumCertainlossStr = deflossContent.getSumcertainloss();
				shijiufee = DataConfig.defLossInfoQueryData.getDefLossContent().getShijiufee();
				if(sumFitsfeeStr.equals("")){
					sumFitsfeeStr = "0.0";
				}
				if(sumRepairfeeStr.equals("")){
					sumRepairfeeStr = "0.0";
				}
				if(sumVeriRestStr.equals("")){
					sumVeriRestStr = "0.0";		
				}
				if(sumCertainlossStr.equals("")){
					sumCertainlossStr = "0.0";
				}
				if(shijiufee.equals("")){
					shijiufee = "0.0";
				}
				tvSumChgComFee.setText(sumFitsfeeStr);
				tvSumRepairFee.setText(sumRepairfeeStr);
				tvRemnant.setText(sumVeriRestStr);
				tvLossFeff.setText(sumCertainlossStr);
				if (!deflossContent.getSumcertainloss().equals("")) {
					double moneyFeff = Double.parseDouble(deflossContent.getSumcertainloss());
					tvLossBigFeff.setText(MoneyUtil.change(moneyFeff));
				} else {
					tvLossBigFeff.setText("零元");
				}
				
				etLossShijiufee.setText(shijiufee);	//add by yxf 20140211 reason:对施救费在页面进行赋值
			}
		}
	}

	private void findView() {
		btnSelect = (Button) gView.findViewById(R.id.deflossinfo_content_item_btn_select);
		tvSumChgComFee = (TextView) gView.findViewById(R.id.deflossinfo_content_item_part_price);
		tvSumRepairFee = (TextView) gView.findViewById(R.id.deflossinfo_content_item_weixu_price);
		tvRemnant = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_canzhi);
		tvLossFeff = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_deflossprice);
		tvLossBigFeff = (TextView) gView.findViewById(R.id.deflossinfo_factory_item_tv_deflossprice_daxie);
		etLossShijiufee = (EditText) gView.findViewById(R.id.deflossinfo_factory_item_et_shijiufee); //add by yxf 20140211 reason:对施救费控件在页面中进行id查找
		
		
		/**
		 * add by yxf 20140211 reason:对edittext录入框进行监听，每次有值有变化都会重新赋值
		 */
		etLossShijiufee.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				DataConfig.defLossInfoQueryData.getDefLossContent().setShijiufee(arg0.toString());
				shijiufee = etLossShijiufee.getText().toString();
			}
		});
		
		
		
		
	}

	public void setView() {
	}

	/***
	 * 控制控件的操作性质
	 */
	public void controlEd() {
		// 控制控件是否可以操作
		if (SystemConfig.isOperate) {
			etLossShijiufee.setEnabled(true);
		} else {
			etLossShijiufee.setEnabled(false);
		}
	}

	private void setListener() {
		btnSelect.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 配件数据
		case R.id.deflossinfo_content_item_btn_select:
			
			UtilIsNotNull utilIsNotNull = new UtilIsNotNull();
			// 互碰自赔的案件：三者车无损提交即0提交
			boolean isOCommit = utilIsNotNull.checkOCommitRule();
			// 一般案件：标的车不可以录入BZ险下的损失项，即BZ险0赔付
			boolean isBZCommit = utilIsNotNull.checkBZCommitRule();
			// 定损险别(BZ险)
			String defLossRiskCode = DataConfig.defLossInfoQueryData.getDefLossContent().getDefLossRiskCode().trim().toUpperCase();
						
			if(isOCommit){
				Toast.makeText(context, "互碰自赔的案件：三者车无损 0提交！", Toast.LENGTH_LONG).show();
			}else if(isBZCommit){
				Toast.makeText(context, "一般案件：标的车(机动车交通事故责任强制保险)0赔付！", Toast.LENGTH_LONG).show();
			}else{
				if(defLossRiskCode.equals("")){
					Toast.makeText(context, "请选择【定损险别】！", Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent();
					intent.setClass(context, JYLioneyeToolsActivity.class);
					context.startActivity(intent);
				}
			}
			break;
		}
	}
	public void saveData(){
		if(DataConfig.defLossInfoQueryData!=null&&DataConfig.defLossInfoQueryData.getDefLossContent()!=null){
			DataConfig.defLossInfoQueryData.getDefLossContent().setSumfitsfee(tvSumChgComFee.getText().toString());
			DataConfig.defLossInfoQueryData.getDefLossContent().setSumRepairfee(tvSumRepairFee.getText().toString());
			DataConfig.defLossInfoQueryData.getDefLossContent().setSumRest(tvRemnant.getText().toString());
			DataConfig.defLossInfoQueryData.getDefLossContent().setSumCertainLoss(tvLossFeff.getText().toString());
			DataConfig.defLossInfoQueryData.getDefLossContent().setSumCerTainLossCh(tvLossBigFeff.getText().toString());
			DataConfig.defLossInfoQueryData.getDefLossContent().setShijiufee(etLossShijiufee.getText().toString());
		}
	}
}
