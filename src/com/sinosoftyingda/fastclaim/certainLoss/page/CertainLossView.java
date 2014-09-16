package com.sinosoftyingda.fastclaim.certainLoss.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.certainLoss.adapter.RepairAndChangeAdapter;
import com.sinosoftyingda.fastclaim.certainLoss.model.RepairAndChangeMsg;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;

/**
 * 定损确认页面
 * 
 * @author haoyun 20130221
 * 
 */
public class CertainLossView extends BaseView implements OnClickListener {
	private View container;
	private Button gBtnOk, gBtnBaseMsg;// 确定，客户信息
	private CheckBox gCbPrint,gCbMessage;//打印和短信复选框
	private ListView gLvRepair, gLvChange;// 维修列表，换件列表
	private boolean isChoice = false;// 是否已经做的满意度评价 false为没做 true为做了
	private ImageView gIvFace1, gIvFace2, gIvFace3, gIvFace4, gIvFace5;// 满意度调查笑脸
	private ViewPager gViewPager;

	public CertainLossView(Context context, Bundle bundle) {
		super(context, bundle);

	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return container;
	}

	@Override
	public Integer getType() {
		// TODO Auto-generated method stub
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {

		// 第一步：加载layout

		initLinearLayout();
		// 初始化组建
		initView();

	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		gViewPager = (ViewPager) container.findViewById(R.id.clc_vp_menu);
//		gViewPager.setAdapter(new ViewPagerAdapter(addPage()));
		gViewPager.setCurrentItem(0);

	}

	/**
	 * 维修列表測試数据填充
	 */
	private List<RepairAndChangeMsg> addRepairMsg() {
		List<RepairAndChangeMsg> list = new ArrayList<RepairAndChangeMsg>();
		RepairAndChangeMsg rcm = new RepairAndChangeMsg("更换零部件", "800元",
				"400*2");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("全身打蜡", "200元", "200*1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("充气", "20元", "1");
		list.add(rcm);
		return list;
	}

	/**
	 * 換件列表測試数据填充
	 */
	private List<RepairAndChangeMsg> addChangeMsg() {
		List<RepairAndChangeMsg> list = new ArrayList<RepairAndChangeMsg>();
		RepairAndChangeMsg rcm = new RepairAndChangeMsg("发动机引擎罩", "5800元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车盖", "1200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);
		rcm = new RepairAndChangeMsg("车大灯", "200元", "1");
		list.add(rcm);

		return list;
	}

	/**
	 * 初始化布局
	 */
	private void initLinearLayout() {
		container = inflater
				.inflate(R.layout.certain_loss_confirm, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		container.setLayoutParams(params);

	}

	/**
	 * 为组件添加事件
	 */
	@Override
	protected void setListener() {

		/**
		 * 满意度调查笑脸
		 */
		gIvFace1.setOnClickListener(new FaceOnClick());
		gIvFace2.setOnClickListener(new FaceOnClick());
		gIvFace3.setOnClickListener(new FaceOnClick());
		gIvFace4.setOnClickListener(new FaceOnClick());
		gIvFace5.setOnClickListener(new FaceOnClick());
		
		
		gBtnOk.setOnClickListener(this);

	}

	/**
	 * 满意度笑脸单击事件
	 * 
	 * @author haoyun 20130221
	 * 
	 */
	class FaceOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!isChoice) {
				isChoice = true;
				if (v.getId() == R.id.clds_iv_fac1
						|| v.getId() == R.id.clds_iv_fac2
						|| v.getId() == R.id.clds_iv_fac3
						|| v.getId() == R.id.clds_iv_fac4
						|| v.getId() == R.id.clds_iv_fac5) {
					gIvFace1.setBackgroundResource(R.drawable.checklist_face1);
					gIvFace2.setBackgroundResource(R.drawable.checklist_face2);
					gIvFace3.setBackgroundResource(R.drawable.checklist_face1);
					gIvFace4.setBackgroundResource(R.drawable.checklist_face4);
					gIvFace5.setBackgroundResource(R.drawable.checklist_face1);
				}
				switch (v.getId()) {
				case R.id.clds_iv_fac1:
					gIvFace1.setBackgroundResource(R.drawable.checklist_face1_click);
					break;
				case R.id.clds_iv_fac2:
					gIvFace2.setBackgroundResource(R.drawable.checklist_face2_click);
					break;
				case R.id.clds_iv_fac3:
					gIvFace3.setBackgroundResource(R.drawable.checklist_face1_click);
					break;
				case R.id.clds_iv_fac4:
					gIvFace4.setBackgroundResource(R.drawable.checklist_face4_click);
					break;
				case R.id.clds_iv_fac5:
					gIvFace5.setBackgroundResource(R.drawable.checklist_face1_click);
					break;
				}
			} else {
				Toast.showToast(context, "您已经做过评价！");
			}

		}

	}

	/***
	 * 通用单击事件
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.clci_btn_customer_information:
			UiManager.getInstance().changeView(CertainLossBaseMsgView.class,
					null, false);
			break;
		case R.id.clci_btn_ok:
			if(gCbPrint.isChecked()&&gCbMessage.isChecked())
			{
				
				Toast.showToast(context,"需要打印并且需要发送短信!");
			}else if(gCbPrint.isChecked()&&!gCbMessage.isChecked())
			{
				Toast.showToast(context,"只需要打印!");
			}else if(!gCbPrint.isChecked()&&gCbMessage.isChecked())
			{
				Toast.showToast(context,"只需要发短信!");
			}
			else
			{
				Toast.showToast(context,"不需要打印并且不需要发送短信!");
			}
			break;

		}

	}

	/**
	 * 滑动翻页Adapter
	 * 
	 * @author haoyun 20130221
	 * 
	 */
	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> gListViews = new ArrayList<View>();

		public ViewPagerAdapter(List<View> listViews) {
			gListViews = listViews;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(gListViews.get(arg1));
		}

		// 获取当前窗体界面数
		@Override
		public int getCount() {
			return gListViews.size();
		}

		// 初始化arg0位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			Log.d("k", "instantiateItem");
			((ViewPager) arg0).addView(gListViews.get(arg1), 0);
			return gListViews.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			Log.d("k", "isViewFromObject");
			return arg0 == (arg1);
		}

	}

	/**
	 * 分页页面添加
	 */
	private List<View> addPage() {
		List<View> list = new ArrayList<View>();
		/**
		 * 第一页 begin
		 */
		View view = LayoutInflater.from(context).inflate(
				R.layout.certain_loss_confirm_item1, null);
		gLvChange = (ListView) view.findViewById(R.id.clci_lv_change);

		gLvChange
				.setAdapter(new RepairAndChangeAdapter(context, addChangeMsg()));
		gBtnBaseMsg = (Button) view
				.findViewById(R.id.clci_btn_customer_information);
		gBtnBaseMsg.setOnClickListener(this);
		list.add(view);
		/**
		 * 第一页添加及初始化完成 end
		 */
		/**
		 * 第二页 begin
		 */
		view = LayoutInflater.from(context).inflate(
				R.layout.certain_loss_confirm_item2, null);
		gBtnOk=(Button)view.findViewById(R.id.clci_btn_ok);
		gCbPrint=(CheckBox)view.findViewById(R.id.clci_cb_print);
		gCbMessage=(CheckBox)view.findViewById(R.id.clci_cb_message);
		gLvRepair = (ListView) view.findViewById(R.id.clci_lv_repair);
		gLvRepair
				.setAdapter(new RepairAndChangeAdapter(context, addRepairMsg()));
		/**
		 * 满意度调查笑脸
		 */
		gIvFace1 = (ImageView) view.findViewById(R.id.clds_iv_fac1);
		gIvFace2 = (ImageView) view.findViewById(R.id.clds_iv_fac2);
		gIvFace3 = (ImageView) view.findViewById(R.id.clds_iv_fac3);
		gIvFace4 = (ImageView) view.findViewById(R.id.clds_iv_fac4);
		gIvFace5 = (ImageView) view.findViewById(R.id.clds_iv_fac5);
		list.add(view);
		/**
		 * 第一页添加及初始化完成 end
		 */
		return list;
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
