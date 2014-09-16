package com.sinosoftyingda.fastclaim.common.widget;

import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

public class MyExpandableListAdapter implements ExpandableListAdapter {

	protected List<? extends Group> list;
	
	public MyExpandableListAdapter(List<? extends Group> list){
		this.list = list;
	}
	

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getChild(int groupId, int childId) {
		// TODO Auto-generated method stub
		return list.get(groupId).getChilds().get(childId);
	}

	@Override
	public long getChildId(int groupId, int childId) {
		// TODO Auto-generated method stub
		return childId;
	}

	@Override
	public View getChildView(int groupId, int childId, boolean flag, View view, ViewGroup group) {
		// TODO Auto-generated method stub
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupId) {
		// TODO Auto-generated method stub
		return list.get(groupId).getChilds().size();
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return childId;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return groupId;
	}

	@Override
	public Object getGroup(int groupId) {
		// TODO Auto-generated method stub
		return list.get(groupId);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int groupId) {
		// TODO Auto-generated method stub
		return groupId;
	}

	@Override
	public View getGroupView(int groupId, boolean flag, View view, ViewGroup group) {
		// TODO Auto-generated method stub
		
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupId, int childId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return list.isEmpty();
	}

	@Override
	public void onGroupCollapsed(int groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupExpanded(int groupId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static class Group {

		private String title;

		private List<? extends Child> childs;

		public Group(String title) {
			this.title = title;
		}

		
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		public List<? extends Child> getChilds() {
			return childs;
		}

		public void setChilds(List<? extends Child> childs) {
			this.childs = childs;
		}

		public static class Child {

		}

	}
}
