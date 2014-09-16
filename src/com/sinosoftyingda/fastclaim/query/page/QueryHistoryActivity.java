package com.sinosoftyingda.fastclaim.query.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossPrintActivity;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;

/**
 * 历史任务查询
 * @author DengGuang
 */
public class QueryHistoryActivity extends BaseView implements OnClickListener {

	private View layout;
	private ImageView btnSearch;
	private ListView historyListview;
	private List<? extends Object> tasks;
	private EditText et;
	private TextView tv;
	private String type;
	public QueryHistoryActivity(Context context, Bundle bundle) {
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
		// 加载布局
		layout = inflater.inflate(R.layout.queryhistory, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setLayoutParams(params);
		// 初始化控件
		btnSearch = (ImageView) layout.findViewById(R.id.queryhistory_btn_search);
		historyListview = (ListView) layout.findViewById(R.id.queryhistory_listview);
		et = (EditText) layout.findViewById(R.id.queryhistory_et);
		tv = (TextView) layout.findViewById(R.id.queryhistory_tv);
		type = bundle.getString("type");
		if (type.equals("check")) {
			tasks = CheckTaskAccess.getAllCompletedTask();
		} else {
			tasks = CertainLossTaskAccess.getAllCompletedTask();
		}
		tv.setText("共有" + tasks.size() + "条历史记录");
		historyListview.setAdapter(new Adapter(tasks));
	}

	@Override
	protected void setListener() {
		btnSearch.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.queryhistory_btn_search:
			List<? extends Object> list = search();
			historyListview.setAdapter(new Adapter(list));
			tv.setText("共有" + list.size() + "条历史记录");
			break;

		default:
			break;
		}

	}

	@Override
	public Integer getExit() {

		return ConstantValue.Page_Title_QueryHistory;
	}

	@Override
	public Integer getBackMain() {
		// TODO Auto-generated method stub
		return null;
	}

	class Adapter extends BaseAdapter {
		private List<? extends Object> tasks;

		public Adapter(List<? extends Object> tasks) {
			this.tasks = tasks;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.tasks.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return this.tasks.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg1 == null) {
				arg1 = View.inflate(context, R.layout.queryhistory_listview_item, null);
			}
			TextView registno = (TextView) arg1.findViewById(R.id.query_listview_item_tv_registno);
			TextView name = (TextView) arg1.findViewById(R.id.query_listview_item_tv_name);
			TextView carno = (TextView) arg1.findViewById(R.id.query_listview_item_tv_carNo);
			TextView time = (TextView) arg1.findViewById(R.id.query_listview_item_tv_time);
			Button print = (Button)arg1.findViewById(R.id.query_listview_item_btn_print);
			print.setVisibility(View.GONE);
			print.setEnabled(false);
			
			Object obj = tasks.get(arg0);
			if (obj instanceof CheckTask) {
				final CheckTask ct = (CheckTask) obj;
				registno.setText(ct.getRegistno());
				name.setText(ct.getInsrtedname());
				String licenseno = ct.getLicenseno();
				if (licenseno == null || licenseno.equals("")) {
					licenseno = "新车";
				}
				carno.setText(licenseno);
				time.setText(ct.getCompletetime());
				arg1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						bundle.putString("registNo", ct.getRegistno());
						bundle.putString("checkStatue",ct.getSurveytaskstatus());
						UiManager.getInstance().changeView(SBasicActivity.class, bundle, true);
					}
				});
			} else {
				TextView itemtype = (TextView) arg1.findViewById(R.id.query_listview_item_tv_itemtype);
				final CertainLossTask clt = (CertainLossTask) obj;
				registno.setText(clt.getRegistno());
				name.setText(clt.getInsrtedname());
				if(clt.getItemtype().equals("thisCar")){
					itemtype.setText("标的车");
				}else if(clt.getItemtype().equals("thirdCar")){
					itemtype.setText("三者车");
				}else{
					itemtype.setText(clt.getItemtype());
				}
				String licenseno = clt.getLicenseno();
				if (licenseno == null || licenseno.equals("")) {
					licenseno = "新车";
				}
				
				// 添加打印按钮
				if("4".equals(clt.getSurveytaskstatus()) && clt.getVerifypassflag().equals("4")){
					print.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("registno", clt.getRegistno());
							bundle.putInt("itemno", clt.getItemno());
							UiManager.getInstance().changeView(DeflossPrintActivity.class, false, bundle, true);
						}
					});
					
					print.setVisibility(View.VISIBLE);
					print.setEnabled(true);
				}else{
					print.setVisibility(View.GONE);
					print.setEnabled(false);
				}
				
				carno.setText(licenseno);
				time.setText(clt.getCompletetime());
				arg1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						SystemConfig.JYDeflossStatus = SystemConfig.JYDeflossQuery;
						bundle.putString("registNo", clt.getRegistno());
						bundle.putString("itemNo", clt.getItemno() + "");
						UiManager.getInstance().changeView(DeflossBasicActivity.class, bundle, true);
					}
				});
			}

			return arg1;
		}
	}

	private List<? extends Object> search() {
		String str = et.getText().toString().trim();
		if (str.equals("")) {
			return tasks;
		} else {
			List<CheckTask> cts = null;
			List<CertainLossTask> clts = null;
			for (int i = 0; i < tasks.size(); i++) {
				Object object = tasks.get(i);
				if (object instanceof CheckTask) {
					if (cts == null) {
						cts = new ArrayList<CheckTask>();
					}
					CheckTask checkTask = (CheckTask) object;
					if (checkTask.getRegistno().contains(str) || checkTask.getInsrtedname().contains(str) || checkTask.getLicenseno().contains(str)) {
						cts.add(checkTask);
					}
				} else {
					if (clts == null) {
						clts = new ArrayList<CertainLossTask>();
					}
					CertainLossTask certainLossTask = (CertainLossTask) object;
					if (certainLossTask.getRegistno().contains(str) || certainLossTask.getInsrtedname().contains(str) || certainLossTask.getLicenseno().contains(str)) {
						clts.add(certainLossTask);
					}
				}
			}
			if (cts != null)
				return cts;
			if (clts != null)
				return clts;
			return tasks;
		}
	}
}
