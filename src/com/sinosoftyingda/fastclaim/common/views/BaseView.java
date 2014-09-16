package com.sinosoftyingda.fastclaim.common.views;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

/***
 * 所有界面的父类
 * 
 * @author chenjianfan
 * 
 */
public abstract class BaseView {
	private static final String Tag = "BaseView";

	protected static Handler handler;// 完善用户提示

	protected Context context;// 传递上下文对象，用户界面配置文件加载
	protected Bundle bundle;// 传递数据

	protected LayoutInflater inflater;// 加载xml文件的工具类

	public BaseView(Context context, Bundle bundle) {
		this.context = context;
		if (bundle != null)
			this.bundle = bundle;

		inflater = LayoutInflater.from(context);
		init();
		// 设置监听
		setListener();
	}

	/**
	 * 界面管理工具切换界面时使用
	 */
	public abstract View getView();

	/**
	 * 顶部四个按钮显示和标题显示
	 */
	public abstract Integer getExit();

	/**
	 * 直接返回主任务界面
	 */
	public abstract Integer getBackMain();

	/**
	 * 界面管理工具切换界面时调整标题和底部导航使用
	 */
	public abstract Integer getType();

	/**
	 * 设置Handler用于处理友好提示
	 * 
	 * @param handler
	 */
	public static void setHandler(Handler handler) {
		BaseView.handler = handler;
	}

	/**
	 * 子类初始化使用
	 */
	protected abstract void init();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 离开当前页面执行
	 */
	public void onPause() {
		Log.i(Tag, "-->离开当前页面");
	}

	/**
	 * 进入当前页面执行
	 */
	public void onResume() {
		Log.i(Tag, "-->进入当前页面");
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

}
