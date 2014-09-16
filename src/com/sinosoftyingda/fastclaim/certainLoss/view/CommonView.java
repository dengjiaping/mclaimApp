package com.sinosoftyingda.fastclaim.certainLoss.view;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class CommonView {
	
	protected Context context;
	
	protected View view;

	protected Map<String, ? extends Object> data;
	
	/**
	 * 
	 * @param context
	 * @param layout
	 */
	public CommonView(Context context, int layout){
		this.context = context;
		this.view = LayoutInflater.from(context).inflate(layout, null);
	}
	
	/**
	 * ����ʼ����Ҫ��ݿ���ʹ�ô˹��췽��
	 * @param context
	 * @param layout
	 * @param data
	 */
	public CommonView(Context context, int layout, Map<String, ? extends Object> data){
		this.context = context;
		this.view = LayoutInflater.from(context).inflate(layout, null);
		this.data = data;
	}
	
	public View getView() {
		return view;
	}
	
}
