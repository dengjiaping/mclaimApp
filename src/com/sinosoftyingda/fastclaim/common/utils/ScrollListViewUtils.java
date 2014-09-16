package com.sinosoftyingda.fastclaim.common.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/***
 * 解决在ScrollView中嵌套使用ListView，ListView只会显示一行多一点的问题
 * 是要注意的是，子ListView的每个Item必须是LinearLayout
 * ，不能是其他的，因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
 * 
 * @author chenjianfan
 * 
 */
public class ScrollListViewUtils {

	/***
	 * 解决在ScrollView中嵌套使用ListView，ListView只会显示一行多一点的问题
	 * 
	 * 使用 在setAdapter方法之后调用 new Utility().setListViewHeightBasedOnChildren(lv);
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
}
