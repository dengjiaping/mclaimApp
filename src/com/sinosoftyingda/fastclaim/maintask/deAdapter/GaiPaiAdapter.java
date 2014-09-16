package com.sinosoftyingda.fastclaim.maintask.deAdapter;

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
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.ScheDuleItem;
import com.sinosoftyingda.fastclaim.common.model.SendForRequest;
import com.sinosoftyingda.fastclaim.common.service.SendForHttpService;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;

public class GaiPaiAdapter extends BaseAdapter {

	private Context context;
	private List<CertainLossTask> handleBeans;

	private Handler handler;
	private PromptManager promptManager;

	public GaiPaiAdapter(Context context, Handler handler, List<CertainLossTask> handleBeans) {
		this.handleBeans = handleBeans;
		this.context = context;
		this.handler = handler;
		promptManager = new PromptManager();
	}

	public List<CertainLossTask> getHandleBeans() {
		return handleBeans;
	}

	public void setHandleBeans(List<CertainLossTask> handleBeans) {
		this.handleBeans = handleBeans;
	}

	@Override
	public int getCount() {

		return handleBeans.size();
	}

	@Override
	public Object getItem(int position) {

		return handleBeans.get(position);
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
			holder.ll1 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_1);
			holder.ll2 = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_2);
			holder.claiMNo = (TextView) view.findViewById(R.id.maintask_group_item_claimNo);
			holder.baoAnRenImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_1_imv);
			holder.banAnTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_name);
			holder.banAnPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_1_phone);
			holder.contactImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv);
			holder.contactName = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_name);
			holder.contactPhone = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_phone);
			holder.TimeImv = (ImageView) view.findViewById(R.id.maintask_group_item_ll_2_imv_time);
			holder.TimeTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_2_tv_time);
			holder.btnMap = (Button) view.findViewById(R.id.maintask_btn_map);
			holder.btnPrint = (Button) view.findViewById(R.id.maintask_btn_print);
			holder.btnRepeal = (Button) view.findViewById(R.id.maintask_btn_repeal);
			holder.assuredTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_name);
			holder.carNoTV = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_carno);
			holder.llTime = (LinearLayout) view.findViewById(R.id.maintask_group_item_ll_al);
			holder.tvCarType = (TextView) view.findViewById(R.id.maintask_group_item_ll_3_cattyple);
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
		final CertainLossTask certainLossTask = handleBeans.get(position);
		holder.claiMNo.setText(certainLossTask.getRegistno());
		holder.assuredTV.setText(certainLossTask.getInsrtedname());
		holder.carNoTV.setText(certainLossTask.getLicenseno());
		// 定损车类型
		String carType = certainLossTask.getItemtype();
		if ("thisCar".equals(carType)) {
			holder.tvCarType.setVisibility(View.VISIBLE);
			holder.tvCarType.setText("标的车");
		} else if ("thirdCar".equals(carType)) {
			holder.tvCarType.setVisibility(View.VISIBLE);
			holder.tvCarType.setText("三者车");
		} else {
			holder.tvCarType.setVisibility(View.GONE);
		}

		// 改派取消按钮
		holder.btnRepeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				promptManager.showDialog(context, "是否撤销此改派任务", R.string.yes, R.string.no, new ShowDialogPositiveButton() {

					@Override
					public void setPositiveButton() {
						cancel(certainLossTask);
					}

					@Override
					public void setNegativeButton() {

					}
				});

			}
		});

		// 跳转到任务详细页面
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SystemConfig.isOperate = false;

				// 已经处理任务
				Bundle bundle1 = new Bundle();
				bundle1.putString("registNo", certainLossTask.getRegistno());
				bundle1.putString("itemNo", certainLossTask.getItemno() + "");
				UiManager.getInstance().changeView(DeflossBasicActivity.class, false, bundle1, true);
			}
		});
		return view;
	}

	private static class viewHolder {
		TextView claiMNo;
		ImageView baoAnRenImv;
		TextView banAnTV;
		TextView banAnPhone;
		ImageView contactImv;
		TextView contactName;
		TextView contactPhone;
		ImageView TimeImv;
		TextView TimeTV;
		Button btnMap;
		Button btnPrint;
		Button btnRepeal;
		TextView assuredTV;// 被保险人
		TextView carNoTV;// 车牌号
		LinearLayout ll2;
		LinearLayout ll1;
		LinearLayout llTime;
		FrameLayout flBtn;
		TextView tvCarType;
	}

	/**
	 * 改派撤销
	 * 
	 * @param registNo
	 */
	private void cancel(final CertainLossTask clt) {
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
					clt.setSurveytaskstatus("1");
					clt.setApplycannelstatus("cancel");
					List<CertainLossTask> clts = new ArrayList<CertainLossTask>();
					clts.add(clt);
					CertainLossTaskAccess.insertOrUpdate(clts, false);
					UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);
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
				sendForRequest.setRegistNo(clt.getRegistno());
				sendForRequest.setApplyType("cancel");
				sendForRequest.setUserCode(SystemConfig.USERLOGINNAME);
				sendForRequest.setIsRelated("0");
				List<ScheDuleItem> list = new ArrayList<ScheDuleItem>();
				ScheDuleItem sdi = new ScheDuleItem();
				sdi.setLossNo(clt.getItemno() + "");
				sdi.setNodeType("certainLoss");
				list.add(sdi);
				sendForRequest.setScheDuleItems(list);
				return SendForHttpService.sendForService(sendForRequest, context.getString(R.string.http_url));
			}
		}.execute();

	}
}
