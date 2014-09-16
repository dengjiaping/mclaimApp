package com.sinosoftyingda.fastclaim.common.ui.utils;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;

/***
 * 下拉控件
 * 
 * @author chenjianfan
 * 
 */
public class SpinnerUtils implements OnItemClickListener, OnClickListener {

	private EditText spinnerEt;
	private ImageButton spinnerImbth;
	private ListView listView;
	private PopupWindow pop;
	private List<SpinnerBean> spinnerBeans;
	private Context context;
	private MyAdapter adapter;
	private int id;
	private View gView;

	private SpinnerOnItemClick spinnerOnItemClick;

	/**
	 * 下拉菜单item的点击事件
	 * 
	 * @author jianfan
	 * 
	 */
	public interface SpinnerOnItemClick {

		void onItemClick(String selectValue, int position);

	}

	public EditText getSpinnerEt() {
		return spinnerEt;
	}

	public void setSpinnerEt(EditText spinnerEt) {
		this.spinnerEt = spinnerEt;
	}

	public List<SpinnerBean> getSpinnerBeans() {
		return spinnerBeans;
	}

	public void setSpinnerBeans(List<SpinnerBean> spinnerBeans) {
		this.spinnerBeans = spinnerBeans;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	boolean isPopShow = false;

	/**
	 * 构造函数
	 * 
	 * @param spinnerBeans
	 * @param context
	 */
	public SpinnerUtils(List<SpinnerBean> spinnerBeans, Context context, View view) {
		this.spinnerBeans = spinnerBeans;
		this.context = context;
		this.gView = view;
		init();
	}

	/*****
	 * 下拉菜单选中某一个项目的点击事件
	 * 
	 * @param spinnerOnItemClick
	 */
	public void OnspinnerItemClick(SpinnerOnItemClick spinnerOnItemClick) {
		this.spinnerOnItemClick = spinnerOnItemClick;
	}

	/*****
	 * 初始化控件以及调用控件
	 * 
	 * @param activity
	 */
	public void init() {

		spinnerEt = (EditText) gView.findViewById(R.id.report_day_select_group);
		spinnerEt.setFocusable(false);
		spinnerImbth = (ImageButton) gView.findViewById(R.id.report_day_select_group_imbtn);
		spinnerEt.setOnClickListener(this);

	}

	class MyAdapter extends BaseAdapter {

		private TextView groupNameTV;

		@Override
		public int getCount() {
			return spinnerBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return spinnerBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.common_spinner_item, null);
			}
			SpinnerBean spinnerBean = spinnerBeans.get(position);
			groupNameTV = (TextView) convertView.findViewById(R.id.report_spinner_item_tv);
			groupNameTV.setText(spinnerBean.getValue());
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		id = position;

		SpinnerBean spinnerBean = spinnerBeans.get(position);
		spinnerEt.setText(spinnerBean.getValue());
		if (spinnerOnItemClick != null)
			spinnerOnItemClick.onItemClick(spinnerEt.getText().toString().trim(), position);
		pop.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		if (pop == null) {
			// isPopShow=true;
			listView = new ListView(context);
			adapter = new MyAdapter();
			listView.setAdapter(adapter);
			// listview上下滑动时，颜色不改变
			listView.setCacheColorHint(Color.WHITE);
			// listView背景圆角样式
			listView.setBackgroundResource(R.drawable.list_corner_round);
			// listView.setBackgroundColor(Color.WHITE);

			// listView 分割线进行改造
			listView.setDivider(null);

			listView.setVerticalScrollBarEnabled(false);
			listView.setOnItemClickListener(this);

			pop = new PopupWindow(listView, spinnerEt.getWidth(), LayoutParams.WRAP_CONTENT, true);

			// 设置popupwindow背景色 功能点击popupwindow之外的部分 可以将popupwindow隐藏
			pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			pop.showAsDropDown(spinnerEt);
		}
		if (isPopShow) {
			pop.dismiss();
		} else {
			pop.showAsDropDown(spinnerEt);
		}
	}

	/**
	 * 添加文本改变监听
	 * @param textWatcher
	 */
	public void setOnTextChangeListener(TextWatcher textWatcher){
		spinnerEt.addTextChangedListener(textWatcher);
	}
	
	public String getCode(String value){
		String code = null;
		if(this.spinnerBeans!=null&&value!=null){
			for (int i = 0; i < this.spinnerBeans.size(); i++) {
				if(spinnerBeans.get(i).getValue().equals(value)){
					return spinnerBeans.get(i).getCode();
				}
			}
			code = "";
		}else{
			code = "";
		}
		return code;
	}
	
	
	public String getValue(String code){
		String value = null;
		if(this.spinnerBeans!=null&&code!=null){
			for (int i = 0; i < this.spinnerBeans.size(); i++) {
				if(spinnerBeans.get(i).getCode().equals(code)){
					return spinnerBeans.get(i).getValue();
				}
			}
			value = "";
		}else{
			value = "";
		}
		return value;
	}
	
}
