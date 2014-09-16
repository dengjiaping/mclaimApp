package com.sinosoftyingda.fastclaim.certainLoss.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;

/**
 * 定损确认客户信息页面
 * 
 * @author haoyun 20130221
 * 
 */
public class CertainLossBaseMsgView extends BaseView implements OnClickListener{
	private View gView;// 布局文件
	private EditText gEtName, gEtIdcardNo, gEtAccountNo;// 开户行姓名，开户行身份证号，开户行账号
	private Button gBtnOK;// 确定


	public CertainLossBaseMsgView(Context context, Bundle bundle) {
		super(context, bundle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return gView;
	}

	@Override
	public Integer getType() {
		// TODO Auto-generated method stub
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		// 初始化布局
		initLinearLayout();
		// 初始化组件
		initView();
	}

	/**
	 * 初始化布局方法
	 */
	private void initLinearLayout() {

		gView = inflater.inflate(R.layout.certain_loss_confirm_base_msg, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		gView.setLayoutParams(params);
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		gEtName = (EditText) gView
				.findViewById(R.id.clcm_et_open_an_account_name);
		gEtAccountNo = (EditText) gView
				.findViewById(R.id.clcm_et_open_an_account_number);
		gEtIdcardNo = (EditText) gView.findViewById(R.id.clcm_et_idcard_no);

		gBtnOK = (Button) gView.findViewById(R.id.clcm_btn_ok);
	}

	@Override
	protected void setListener() {
		
		gBtnOK.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.clcm_btn_ok:
			UiManager.getInstance().changeView(CertainLossView.class, null, true);
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public Integer getExit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getBackMain() {
		// TODO Auto-generated method stub
		return null;
	}

}
