package com.sinosoftyingda.fastclaim.common.views;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class TopBtnSumbitView {

	private LayoutInflater inflater;
	private Context context;
	private View gView;
	private FastClaimDbHelper dbHelper;
	// 同步理赔和提交按钮
	private Button btnSurveySumbit;
	private Button btnSurveyTongBu;
	private Button btnDeflossSumbit;
	private Button btnDeflossTongBu;
	//
	private Button btnSurveyContinue;
	private Button btnDeflossContinue;

	public Button getBtnSurveyContinue() {
		return btnSurveyContinue;
	}

	public void setBtnSurveyContinue(Button btnSurveyContinue) {
		this.btnSurveyContinue = btnSurveyContinue;
	}

	public Button getBtnDeflossContinue() {
		return btnDeflossContinue;
	}

	public void setBtnDeflossContinue(Button btnDeflossContinue) {
		this.btnDeflossContinue = btnDeflossContinue;
	}

	public Button getBtnSurveySumbit() {
		return btnSurveySumbit;
	}

	public void setBtnSurveySumbit(Button btnSurveySumbit) {
		this.btnSurveySumbit = btnSurveySumbit;
	}

	public Button getBtnSurveyTongBu() {
		return btnSurveyTongBu;
	}

	public void setBtnSurveyTongBu(Button btnSurveyTongBu) {
		this.btnSurveyTongBu = btnSurveyTongBu;
	}

	public Button getBtnDeflossSumbit() {
		return btnDeflossSumbit;
	}

	public void setBtnDeflossSumbit(Button btnDeflossSumbit) {
		this.btnDeflossSumbit = btnDeflossSumbit;
	}

	public Button getBtnDeflossTongBu() {
		return btnDeflossTongBu;
	}

	public void setBtnDeflossTongBu(Button btnDeflossTongBu) {
		this.btnDeflossTongBu = btnDeflossTongBu;
	}

	private ProgressDialog progressDialog;

	public View getView() {
		return gView;
	}

	public TopBtnSumbitView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		gView = inflater.inflate(R.layout.btn_top_tongbu, null);
		findView();
		setListener();
		progressDialog = new ProgressDialog(context);
		dbHelper = new FastClaimDbHelper(context);
	}

	private void findView() {
		btnSurveySumbit = (Button) gView.findViewById(R.id.btn_top_tongbu_submit);
		btnSurveyTongBu = (Button) gView.findViewById(R.id.btn_top_tongbu_tongbu);
		btnDeflossSumbit = (Button) gView.findViewById(R.id.btn_defloss_top_tongbu_submit);
		btnDeflossTongBu = (Button) gView.findViewById(R.id.btn_defloss_top_tongbu_tongbu);
		btnSurveyContinue = (Button) gView.findViewById(R.id.btn_top_tongbu_tongbu_continue);
		btnDeflossContinue = (Button) gView.findViewById(R.id.btn_defloss_top_tongbu_tongbu_continue);
	}

	/*****
	 * 
	 * @param showDefloll
	 *            是否显示定损按钮
	 * @param showSurvey
	 *            是否显示查勘按钮
	 */
	public void controlShow(boolean showDefloll, boolean showSurvey) {
		if (showSurvey) {

			if (btnSurveySumbit.getVisibility() == View.GONE)
				btnSurveySumbit.setVisibility(View.VISIBLE);
			if (btnSurveyTongBu.getVisibility() == View.GONE)
				btnSurveyTongBu.setVisibility(View.VISIBLE);

			if (SystemConfig.isContinue) {
				if (btnSurveyContinue.getVisibility() == View.GONE)
					btnSurveyContinue.setVisibility(View.VISIBLE);
				if (btnSurveyTongBu.getVisibility() == View.VISIBLE)
					btnSurveyTongBu.setVisibility(View.GONE);
			}

		} else {

			if (btnSurveySumbit.getVisibility() == View.VISIBLE)
				btnSurveySumbit.setVisibility(View.GONE);
			if (btnSurveyTongBu.getVisibility() == View.VISIBLE)
				btnSurveyTongBu.setVisibility(View.GONE);

			if (btnSurveyContinue.getVisibility() == View.VISIBLE)
				btnSurveyContinue.setVisibility(View.GONE);
		}

		if (showDefloll) {
			if (btnDeflossSumbit.getVisibility() == View.GONE)
				btnDeflossSumbit.setVisibility(View.VISIBLE);
			if (btnDeflossTongBu.getVisibility() == View.GONE)
				btnDeflossTongBu.setVisibility(View.VISIBLE);

			if (SystemConfig.isContinue) {
				if (btnDeflossContinue.getVisibility() == View.GONE)
					btnDeflossContinue.setVisibility(View.VISIBLE);
				if (btnDeflossTongBu.getVisibility() == View.VISIBLE)
					btnDeflossTongBu.setVisibility(View.GONE);
			}
		} else {
			if (btnDeflossSumbit.getVisibility() == View.VISIBLE)
				btnDeflossSumbit.setVisibility(View.GONE);
			if (btnDeflossTongBu.getVisibility() == View.VISIBLE)
				btnDeflossTongBu.setVisibility(View.GONE);
			if (btnDeflossContinue.getVisibility() == View.VISIBLE)
				btnDeflossContinue.setVisibility(View.GONE);
		}

	}

	private void setListener() {

	}

	private Handler submitHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PromptManager.closeProgressDialog(progressDialog);
			CommonResponse commonResponse = (CommonResponse) msg.obj;
			if ("YES".equals(commonResponse.getResponseCode())) {
				Toast.showToastTest(context, "查勘提交成功！");
			} else {
				PromptManager.showErrorDialog(context, commonResponse.getResponseMessage());
			}
		};
	};
	private Handler synchHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PromptManager.closeProgressDialog(progressDialog);
			CommonResponse commonResponse = (CommonResponse) msg.obj;
			if ("YES".equals(commonResponse.getResponseCode())) {
				Toast.showToastTest(context, "同步理赔成功！");
			} else {
				PromptManager.showErrorDialog(context, commonResponse.getResponseMessage());
			}

		};
	};
}
