package com.sinosoftyingda.fastclaim.maintask.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.ScheDuleItem;
import com.sinosoftyingda.fastclaim.common.model.SendForRequest;
import com.sinosoftyingda.fastclaim.common.service.SendForHttpService;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;

public class GaiPaiAdapter extends BaseAdapter {

	private Context context;
	private List<CheckTask> checkTasks;

	public List<CheckTask> getCheckTasks() {
		return checkTasks;
	}

	public void setCheckTasks(List<CheckTask> checkTasks) {
		this.checkTasks = checkTasks;
	}

	private PromptManager promptManager;
	private Handler handler;

	public GaiPaiAdapter(Context context, Handler handler, List<CheckTask> checktasks) {
		this.checkTasks = checktasks;
		this.context = context;
		this.handler = handler;
		promptManager = new PromptManager();
	}

	public List<CheckTask> getHandleBeans() {
		return checkTasks;
	}

	public void setHandleBeans(List<CheckTask> handleBeans) {
		this.checkTasks = handleBeans;
	}

	@Override
	public int getCount() {

		return checkTasks.size();
	}

	@Override
	public Object getItem(int position) {

		return checkTasks.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			view = View.inflate(context, R.layout.maintask_group_item, null);
			holder.flBtn = (FrameLayout) view.findViewById(R.id.maintask_flout_btn);
			// 被保险人区域
			holder.assuredLL = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_3);
			holder.assuredTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_name);
			holder.carNoTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_carno);
			// 联系人
			holder.ll2 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_2);
			holder.contactImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv);
			holder.contactName = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_name);
			holder.contactPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_phone);
			// 报案信息
			holder.ll1 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_1);
			holder.registNo = (TextView) view.findViewById(R.id.maintask_group_item_claimNo);
			holder.baoAnRenImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_1_imv);
			holder.banAnTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_name);
			holder.banAnPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_phone);
			// 时间
			holder.llTime = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_al);
			holder.TimeImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv_time);
			holder.TimeTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_tv_time);
			holder.btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
			holder.btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (viewHolder) view.getTag();
		}
		holder.llTime.setVisibility(View.GONE);
		// 不显示联系人等信息
		if (holder.ll2.getVisibility() == View.VISIBLE)
			holder.ll2.setVisibility(View.GONE);
		// 不显示报案人等信息
		if (holder.ll1.getVisibility() == View.VISIBLE)
			holder.ll1.setVisibility(View.GONE);
		// 地图按钮不显示
		if (holder.btnMap.getVisibility() == View.VISIBLE)
			holder.btnMap.setVisibility(View.GONE);
		if (holder.btnPrint.getVisibility() == View.VISIBLE)
			holder.btnPrint.setVisibility(View.GONE);

		// 改派按钮显示
		if (holder.btnRepeal.getVisibility() == View.GONE)
			holder.btnRepeal.setVisibility(View.VISIBLE);

		if (holder.baoAnRenImv.getVisibility() == View.VISIBLE)
			holder.baoAnRenImv.setVisibility(View.GONE);
		if (holder.contactPhone.getVisibility() == View.VISIBLE)
			holder.contactPhone.setVisibility(View.GONE);

		final CheckTask checktask = checkTasks.get(position);
		holder.registNo.setText(checktask.getRegistno());
		holder.assuredTV.setText(checktask.getInsrtedname());
		holder.carNoTV.setText(checktask.getLicenseno());

		// 跳转到任务详细页面
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SystemConfig.isOperate = false;
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", holder.registNo.getText().toString());
				bundle1.putString("checkStatue", "");
				UiManager.getInstance().changeView(SBasicActivity.class, false, bundle1, true);
			}
		});

		// 改派取消按钮
		holder.btnRepeal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				promptManager.showDialog(context, "是否撤销此改派任务", R.string.yes, R.string.no, new ShowDialogPositiveButton() {
					@Override
					public void setPositiveButton() {
						cancel(checktask);
					}

					@Override
					public void setNegativeButton() {

					}
				});
			}
		});
		return view;
	}

	private static class viewHolder {
		public Button btnPrint;
		public LinearLayout assuredLL;
		public FrameLayout flBtn;
		TextView registNo;
		ImageView baoAnRenImv;
		TextView banAnTV;
		TextView banAnPhone;
		ImageView contactImv;
		TextView contactName;
		TextView contactPhone;
		ImageView TimeImv;
		TextView TimeTV;
		Button btnMap;
		Button btnRepeal;
		TextView assuredTV;// 被保险人
		TextView carNoTV;// 车牌号
		LinearLayout ll2;
		LinearLayout ll1;
		LinearLayout llTime;
	}

	/**
	 * 改派撤销
	 * 
	 * @param registNo
	 */
	private void cancel(final CheckTask ct) {
		new AsyncTask<String, Void, CommonResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if (result != null && "YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Toast.showToastTest(context, "撤销改派成功!");
					ct.setSurveytaskstatus("1");
					ct.setApplycannelstatus("cancel");
					List<CheckTask> cts = new ArrayList<CheckTask>();
					cts.add(ct);
					CheckTaskAccess.insertOrUpdate(cts, false);
					UiManager.getInstance().changeView(SurveyTaskActivity.class, null, true);
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					if (result != null) {
						message.obj = result.getResponseMessage();
					} else {
						message.obj = "撤销改派失败!";
					}
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				SendForRequest sendForRequest = new SendForRequest();
				sendForRequest.setRegistNo(ct.getRegistno());
				sendForRequest.setApplyType("cancel");
				sendForRequest.setUserCode(SystemConfig.USERLOGINNAME);
				sendForRequest.setIsRelated("0");
				List<ScheDuleItem> list = new ArrayList<ScheDuleItem>();
				ScheDuleItem sdi = new ScheDuleItem();
				sdi.setLossNo("-2");
				sdi.setNodeType("check");
				list.add(sdi);
				sendForRequest.setScheDuleItems(list);
				return SendForHttpService.sendForService(sendForRequest, context.getString(R.string.http_url));
			}
		}.execute();
	}
}
