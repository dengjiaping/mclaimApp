package com.sinosoftyingda.fastclaim.common.views;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import org.apache.commons.lang3.StringUtils;

import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossInfoActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.more.page.MoreActivity;
import com.sinosoftyingda.fastclaim.photoes.page.DeflossPhotosView;
import com.sinosoftyingda.fastclaim.photoes.page.PhotosView;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;
import com.sinosoftyingda.fastclaim.survey.page.SSurveyActivity;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.work.page.DeflossWorkflowlogView;
import com.sinosoftyingda.fastclaim.work.page.WorkflowlogView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * 控制内容部分切换控制器
 * 
 * @author chenjianfan
 * 
 */
public class UiManager extends Observable {
	private static final String TAG = "UiManager";
	/************* 简单的实现单例模式 *****/
	private static UiManager uiManager;

	private UiManager() {
	}

	public static UiManager getInstance() {
		if (uiManager == null)
			uiManager = new UiManager();
		return uiManager;
	}

	/***********************************/

	RelativeLayout continer;// 内容部分容器

	/**
	 * 设置容器
	 * 
	 * @param continer
	 */
	public void setContiner(RelativeLayout continer) {
		this.continer = continer;
	}

	/****
	 * 返回容器
	 * 
	 * @return
	 */
	public RelativeLayout getContiner() {
		return this.continer;
	}

	/***************** 界面缓存 ******************/
	private static Map<String, BaseView> VIEWCACHE = new HashMap<String, BaseView>();
	/***************** 返回界面缓存 ******************/
	private static LinkedList<String> BACKVIEW = new LinkedList<String>();

	public static Map<String, BaseView> getVIEWCACHE() {
		return VIEWCACHE;
	}

	public static void setVIEWCACHE(Map<String, BaseView> vIEWCACHE) {
		VIEWCACHE = vIEWCACHE;
	}

	public static LinkedList<String> getBACKVIEW() {
		return BACKVIEW;
	}

	public static void setBACKVIEW(LinkedList<String> bACKVIEW) {
		BACKVIEW = bACKVIEW;
	}

	private BaseView currentView = null;// 当前界面

	/****
	 * 清空缓存
	 * 
	 * @return
	 */
	public void emptyViewCache() {
		VIEWCACHE.clear();
		System.gc();
		System.out.println("VIEWCACHEd的大小是多少" + VIEWCACHE.size());
		System.out.println("BACKVIEW的大小是多少" + BACKVIEW.size());
	}

	/****
	 * 清空缓存
	 * 
	 * @return
	 */
	public void clearViewCache() {
		VIEWCACHE.clear();
		BACKVIEW.clear();
		System.gc();
		System.out.println("VIEWCACHEd的大小是多少" + VIEWCACHE.size());
		System.out.println("BACKVIEW的大小是多少" + BACKVIEW.size());
	}

	/***
	 * 界面切换
	 * 
	 * @param newView
	 *            要切换到的那个界面
	 * @param bundle
	 *            界面切换要传递的数据
	 * @param needBack
	 *            当前界面被切换时，这个界面是否需要返回，true，表示要返回这个界面： false:表示不返回
	 * 
	 * @param needBack
	 *            isCaseView 界面不需要缓存
	 * 
	 * @return
	 */
	public boolean changeView(Class<? extends BaseView> newView, Bundle bundle, boolean needBack, boolean isCaseView) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;
		if (currentView != null) {
			currentView.onPause();
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				currentView.onResume();
				return false;
			}
		}
		continer.removeAllViews();
		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
				if (bundle != null)
					newViewInstance.setBundle(bundle);
				Log.i(TAG, "case instance");
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
			}

			continer.addView(newViewInstance.getView());
			if (isCaseView)
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);

			currentView = newViewInstance;
			currentView.onResume();
			if (needBack) {
				BACKVIEW.addFirst(newView.getSimpleName());
			}

			changeTitleAndBottom(newViewInstance.getType());
			exitPageSecondMethod(currentView.getExit());
			wordBackMainPageMethod(SystemConfig.UserRightIsAdvanced == true ? 0 : 1);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/***
	 * 界面切换
	 * 
	 * @param newView
	 *            要切换到的那个界面
	 * @param bundle
	 *            界面切换要传递的数据
	 * @param needBack
	 *            当前界面被切换时，这个界面是否需要返回，true，表示要返回这个界面： false:表示不返回
	 * @return
	 */
	public boolean changeView(Class<? extends BaseView> newView, Bundle bundle, boolean needBack) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;
		if (currentView != null) {
			currentView.onPause();
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				currentView.onResume();
				return false;
			}
		}
		continer.removeAllViews();
		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
				if (bundle != null)
					newViewInstance.setBundle(bundle);
				Log.i(TAG, "case instance");
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
			}

			continer.addView(newViewInstance.getView());
			VIEWCACHE.put(newView.getSimpleName(), newViewInstance);

			currentView = newViewInstance;
			currentView.onResume();
			if (needBack) {
				BACKVIEW.addFirst(newView.getSimpleName());
			}

			changeTitleAndBottom(newViewInstance.getType());
			exitPageSecondMethod(currentView.getExit());
			wordBackMainPageMethod(SystemConfig.UserRightIsAdvanced == true ? 0 : 1);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/***
	 * * 界面切换
	 * 
	 * @param newView
	 *            要切换到的那个界面
	 * @param needCase
	 *            要跳转显示的页面是否在缓存中拿出来，还是重新创建一个
	 * @param bundle
	 *            界面切换要传递的数据
	 * @param needBack
	 *            当前界面被切换时，这个界面是否需要返回，true，表示要返回这个界面： false:表示不返回
	 * @return
	 */
	public boolean changeView(Class<? extends BaseView> newView, boolean needCase, Bundle bundle, boolean needBack) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;
		if (currentView != null) {
			currentView.onPause();
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				currentView.onResume();
				return false;
			}
		}
		continer.removeAllViews();
		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				if (needCase) {
					newViewInstance = VIEWCACHE.get(newView.getSimpleName());
					Log.i(TAG, "case instance");
					if (bundle != null)
						newViewInstance.setBundle(bundle);
					Log.i(TAG, "case instance");
				} else {
					newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
					newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
					Log.i(TAG, "new instance");
				}

			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
			}

			continer.addView(newViewInstance.getView());

			VIEWCACHE.put(newView.getSimpleName(), newViewInstance);

			currentView = newViewInstance;
			currentView.onResume();
			if (needBack) {
				BACKVIEW.addFirst(newView.getSimpleName());
			}

			changeTitleAndBottom(newViewInstance.getType());
			exitPageSecondMethod(currentView.getExit());
			wordBackMainPageMethod(SystemConfig.UserRightIsAdvanced == true ? 0 : 1);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/***
	 * * 界面切换
	 * 
	 * @param newView
	 *            要切换到的那个界面
	 * @param needCase
	 *            要跳转显示的页面是否在缓存中拿出来，还是重新创建一个
	 * @param bundle
	 *            界面切换要传递的数据
	 * @param needCread
	 * 
	 * @param needBack
	 *            当前界面被切换时，这个界面是否需要返回，true，表示要返回这个界面： false:表示不返回
	 * 
	 * 
	 * @return
	 */
	public boolean changeView(Class<? extends BaseView> newView, boolean needCread, boolean needCase, Bundle bundle, boolean needBack) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;
		if (!needCread) {
			if (currentView != null) {
				currentView.onPause();
				if (currentView.getClass() == newView) {
					Log.i(TAG, "same view");
					currentView.onResume();
					return false;
				}
			}
		}
		continer.removeAllViews();
		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {

				if (needCase) {
					newViewInstance = VIEWCACHE.get(newView.getSimpleName());
					Log.i(TAG, "case instance");
					if (bundle != null)
						newViewInstance.setBundle(bundle);
					Log.i(TAG, "case instance");
				} else {
					newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
					newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
					Log.i(TAG, "new instance");
				}

			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
			}

			continer.addView(newViewInstance.getView());

			VIEWCACHE.put(newView.getSimpleName(), newViewInstance);

			currentView = newViewInstance;
			currentView.onResume();
			if (needBack) {
				BACKVIEW.addFirst(newView.getSimpleName());
			}

			changeTitleAndBottom(newViewInstance.getType());
			exitPageSecondMethod(currentView.getExit());
			wordBackMainPageMethod(SystemConfig.UserRightIsAdvanced == true ? 0 : 1);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 切换返回缓存信息
	 * 
	 * @return
	 */
	public boolean changeCacheView() {
		boolean result = false;
		// 获取但不移除此列表的头（第一个元素)
		String poll = null;
		if (!BACKVIEW.isEmpty())
			poll = BACKVIEW.getFirst();
		if (StringUtils.isNotBlank(poll)) {
			// 判断某字符串是否不为空且长度不为0且不由空白符(whitespace)构成
			if (!currentView.getClass().getSimpleName().equals(poll)) {
				// 不是当前页面
				if (DeflossWorkflowlogView.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);
					return true;
				} else if (DeflossPhotosView.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);
					return true;
				} else if (DeflossBasicActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);

					return true;
				} else if (DeflossInfoActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, true);

					return true;
				} else if (WorkflowlogView.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
					return true;
				} else if (SBasicActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
					return true;
				} else if (PhotosView.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
					return true;
				} else if (SSurveyActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 清理缓存页面数据
					emptyViewCache();
					BACKVIEW.clear();
					System.gc();
					UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, true);
					return true;
				} else if (MoreActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 如果是二级页面点击返回提示退出按钮
					return false;
				} else if (UploadActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 如果是二级页面点击返回提示退出按钮
					return false;
				} else if (SurveyTaskActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 如果是二级页面点击返回提示退出按钮
					return false;
				} else if (DelossTaskActivity.class.getSimpleName().equals(currentView.getClass().getSimpleName())) {
					// 如果是二级页面点击返回提示退出按钮
					return false;
				} else {
					if (VIEWCACHE.size() > 0) {
						currentView.onPause();
						continer.removeAllViews();
						continer.addView(VIEWCACHE.get(poll).getView());
						currentView = VIEWCACHE.get(poll);
						currentView.onResume();
						changeTitleAndBottom(currentView.getType());
						exitPageSecondMethod(currentView.getExit());
						wordBackMainPageMethod(SystemConfig.UserRightIsAdvanced == true ? 0 : 1);
						result = true;
					}
				}
			} else {
				// 是当前页面
				if (BACKVIEW.size() > 1) {
					BACKVIEW.remove(poll);
					result = changeCacheView();
				} else
					return false;
			}
		}
		return result;
	}

	private void wordBackMainPageMethod(Integer backMain) {
		setChanged();
		notifyObservers(backMain);
	}

	private void exitPageSecondMethod(Integer exit) {
		setChanged();
		notifyObservers(exit);

	}

	/**
	 * 切换标题和底部导航
	 */
	public void changeTitleAndBottom(int type) {
		setChanged();
		notifyObservers(type);

	}

	public BaseView getCurrentView() {
		return currentView;
	}

	public void clearBackCache() {
		BACKVIEW.clear();
	}

}
