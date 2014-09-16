package com.sinosoftyingda.fastclaim.survey.page.three.page;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.amap.util.Constants;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.FastClaimDbHelper;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.model.AppointModel;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.LeaveMsgRequest;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgRequest;
import com.sinosoftyingda.fastclaim.common.model.PolicyMsgResponse;
import com.sinosoftyingda.fastclaim.common.service.LeaveMessageSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.PolicyHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.AppointAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.GuaranteesLipBaseAdapter;
import com.sinosoftyingda.fastclaim.survey.page.three.adapter.GuaranteesLipUnderAdapter;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;

/*****
 * 查看基本信息中，保险信息页面
 * 
 * @author chenjianfan
 */
public class LeaveMessageEditActivity extends BaseView implements
		OnClickListener {
	private View layout;
	LeaveMsgRequest leaveMsgData = new LeaveMsgRequest();
	
	private String leavemsg_content; // 撰写留言内容
	private String leavemsg_nodetype; // 节点类型
	private String leavemsg_time; // 撰写留言时间
	private String leavemsg_editperson; // 撰写留言 人员
	String servierTime=null;//截取字符串后的时间
	// 保单号
	private String registNo;

	public LeaveMessageEditActivity(Context context, Bundle bundle) {
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
		TopManager.getInstance().setHeadTitle("撰写留言", 22, Typeface.defaultFromStyle(Typeface.NORMAL));//设置页面标题
		registNo = bundle.getString("registNo");
		leavemsg_time=bundle.getString("time");
		leavemsg_nodetype=bundle.getString("nodetype");
		leavemsg_editperson=bundle.getString("LeavePerson");
		// 加载布局文件
		layout = inflater.inflate(R.layout.leavemsgedit, null);
		// 填充布局
		ViewHolder.getInstance().btn_leavemsg_submit = (Button) getView().findViewById(R.id.button_submit_leavemessage);
		ViewHolder.getInstance().leavemsg_registNo = (TextView) getView().findViewById(R.id.leavemsg_registNo_num); // 时间
		ViewHolder.getInstance().leavemsg_time_tv = (TextView) getView().findViewById(R.id.leavemsg_time_value); // 时间
		ViewHolder.getInstance().leavemsg_nodetype_tv = (TextView) getView().findViewById(R.id.leavemsg_nodetype_value); // 节点类型
		ViewHolder.getInstance().leavemsg_editperson_tv = (TextView) getView().findViewById(R.id.leavemsg_person_value); // 撰写留言 人员
		ViewHolder.getInstance().leavemsg_edittxt = (EditText) getView().findViewById(R.id.leavemsg_content_value);
		ViewHolder.getInstance().leavemsg_edittxt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });//设置edittext得录入长度
		ViewHolder.getInstance().leavemsg_time_tv = (TextView) getView().findViewById(R.id.leavemsg_time_value); // 时间
		ViewHolder.getInstance().leavemsg_nodetype_tv = (TextView) getView().findViewById(R.id.leavemsg_nodetype_value); // 节点类型
		//add by yxf 20140122 reason:修改年月日截取方式，通过simpledateformat获取 begin
		servierTime =getCurrenttime(leavemsg_time);
		ViewHolder.getInstance().leavemsg_registNo.setText(registNo);		//报案号码
		ViewHolder.getInstance().leavemsg_time_tv.setText(servierTime); // 撰写留言 时间
		ViewHolder.getInstance().leavemsg_nodetype_tv.setText(leavemsg_nodetype); // 撰写留言 节点
		ViewHolder.getInstance().leavemsg_editperson_tv.setText(leavemsg_editperson); // 撰写留言 人员
		
		this.findView();

	}
	private String getCurrenttime(String serverTime){
		SimpleDateFormat aa=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			date = aa.parse(serverTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		aa.applyPattern("yyyy");
		String d_t_yyyy=aa.format(date);
		aa.applyPattern("MM");
		String d_t_MM=aa.format(date);
		aa.applyPattern("dd");
		String d_t_dd=aa.format(date);
		String ymd=d_t_yyyy+"-"+d_t_MM+"-"+d_t_dd;
		return ymd;
	}

	private void findView() {
		/**
		 * add by yxf 20140211 reason:对edittext录入框进行监听，每次有值有变化都会重新赋值
		 */
		ViewHolder.getInstance().leavemsg_edittxt.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
			}
			public void afterTextChanged(Editable arg0) {
				leaveMsgData.setLeavemsgtime(leavemsg_time);
				leaveMsgData.setLeavemsgnodetype(ViewHolder.getInstance().leavemsg_nodetype_tv.getText().toString());
				leaveMsgData.setLeavemsgperson(ViewHolder.getInstance().leavemsg_editperson_tv.getText().toString());
				leaveMsgData.setLeavemsgContent(ViewHolder.getInstance().leavemsg_edittxt.getText().toString());
				leaveMsgData.setLeavemsgpersoncode(SystemConfig.USERLOGINNAME);
				leaveMsgData.setRegistNo(registNo);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if(leavemsg_nodetype.equals("查勘")){
			for(int i=0;i<CheckTaskAccess.getDispatchTask().size();i++){
				if(CheckTaskAccess.getDispatchTask().get(i).getRegistno().equals(registNo)){
					ViewHolder.getInstance().leavemsg_edittxt.setEnabled(false);
				}
			}
		}else if(leavemsg_nodetype.equals("定损")){
			for(int j=0;j<CertainLossTaskAccess.getDispatchTask().size();j++){
				if(CertainLossTaskAccess.getDispatchTask().get(j).getRegistno().equals(registNo)){
					ViewHolder.getInstance().leavemsg_edittxt.setEnabled(false);
				}
			}
		}
	}

	private void setData() {
		new AsyncTask<String, Void, CommonResponse>() {
			protected void onPreExecute() {
				//提交之前开启提示标识
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			protected void onPostExecute(CommonResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					//提交之前关闭提示标识
					handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
					Toast.showToastTest(context, "撰写留言提交成功！");
					//提交成功之后对撰写留言内容得编辑框进行清空
					setView();
				} else {
					//提交之前关闭提示标识
					handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;	
					handler.sendMessage(message);
				}
				
				
			};

			protected CommonResponse doInBackground(String... params) {
				return LeaveMessageSubmitHttpService.leaveMsgService(leaveMsgData, context.getString(R.string.http_url));
			}
		}.execute();
	}

	@Override
	protected void setListener() {
		//对撰写留言提交按钮进行监听
		ViewHolder.getInstance().btn_leavemsg_submit.setOnClickListener(this);
	}

	@Override
	public Integer getExit() {
		//页面标题显示为 	撰写留言
		return ConstantValue.LeaveMessageEdit;
	}

	@Override
	public Integer getBackMain() {
		return null;
	}

	@Override
	public void onPause() {
		System.gc();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_leavemessage:
			String aa=ViewHolder.getInstance().leavemsg_edittxt.getText().toString().trim();
			if(!"".equals(aa)){
				setData();
			}else{
				Toast.showToast(context, "留言信息不能为空！");
			}
			break;
		}
	}

	private void setView() {
		// 提交成功之后清空edittext内容
		ViewHolder.getInstance().leavemsg_edittxt.setText("");
		ViewHolder.getInstance().leavemsg_time_tv.setText(this.getCurrenttime(SystemConfig.serverTime));
	}
	
	private static class ViewHolder {
		private static ViewHolder instance = null;
		private View layout;
		public static ViewHolder getInstance() {
			if (instance == null) {
				instance = new ViewHolder();
			}
			return instance;
		}
		private Button btn_leavemsg_submit; // 提交留言按钮
		private TextView leavemsg_time_tv; // 时间
		private TextView leavemsg_nodetype_tv; // 节点类型
		private TextView leavemsg_editperson_tv;// 撰写留言 人员
		private EditText leavemsg_edittxt; // 撰写留言内容
		private TextView leavemsg_registNo;
	}


	
	
	
	
	
	
	
	
	
	
	
	
}
