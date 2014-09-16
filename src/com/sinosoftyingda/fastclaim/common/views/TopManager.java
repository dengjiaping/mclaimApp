package com.sinosoftyingda.fastclaim.common.views;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;

import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;

/***
 * 顶部导航控制器
 * 
 * @author chenjianfan
 * 
 */
public class TopManager implements Observer {

	private static final TopManager topmanager = new TopManager();
	private static final String Tag = "TopManager";
	private RelativeLayout topContainer;
	// 左边顶部按钮组
	private FrameLayout frameLayoutLeft;
	// 右边顶部按钮组
	private FrameLayout frameLayoutRight;
	// 右边顶部按钮组中控件
	private ImageButton imbtnTongBu;
	private ListView listView;
	private MyAdapter adapter;
	// 左边顶部按钮组中控件
	private ImageButton returnButton;
	private ImageButton homeButton;
	private ImageButton exitButton;
	// 顶部标题
	private TextView titleText;
	private Context context;
	/***** 查勘/定损工作导航 ******/
	private LinearLayout LossOfPartContainer;// 用户权限
	// 查勘导航菜单
	private LinearLayout surveyContainer;// 查勘导航容器
	private Button btnSurveyBasicMsg;// 查勘基本信息
	private Button btnSurveyMsg;// 查勘信息
	private Button btnSurveyPhoto;// 查勘拍照
	private Button btnSurveyProcess;// 查勘流程
	private PopupWindow pop;
	boolean isPopShow = false;
	private IWordSurveyTopButton isurveyTopButton;
	private Button BtnVideo;
	// 理赔同步和提交
	private TopBtnSumbitView topBtnSumbitView;

	private TopBtnSumbitSurvery topBtnSumbitSurvery;
	private TopBtnSumbitDefloss topBtnSumbitDefloss;
	private TopBtnTongBuSurveryContinue btnTongBuSurveryContinue;
	private TopBtnTongBuDeflossContinue btnTongBuDeflossContinue;

	public xiePeiBtnVideo btnVideo;

	/***
	 * 查勘同步继续操作
	 * 
	 * @author jianfan
	 * 
	 */
	public interface TopBtnTongBuSurveryContinue {

		void onClick();

	};

	/*****
	 * 定损同步继续操作
	 * 
	 * @author jianfan
	 * 
	 */
	public interface TopBtnTongBuDeflossContinue {

		void onClick();

	};

	public interface xiePeiBtnVideo {

		void onClick();
	}

	public interface TopBtnSumbitSurvery {
		void OnclickSumbit();

		void OnclickTongBu();
	}

	public interface TopBtnSumbitDefloss {
		void OnclickSumbit();

		void synchOrSubmit(final String type);

		void OnclickTongBu();
	}

	/*****
	 * 理赔查勘任务同步继续操作
	 * 
	 * @param btnTongBuSurveryContinue
	 */
	public void btnTongBuSurveryContinue(final TopBtnTongBuSurveryContinue btnTongBuSurveryContinue) {

		this.btnTongBuSurveryContinue = btnTongBuSurveryContinue;

		topBtnSumbitView.getBtnSurveyContinue().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnTongBuSurveryContinue.onClick();

				pop.dismiss();

			}
		});
	}

	/***
	 * 理赔查勘任务同步继续操作 控制控件显示 false 显示撤销按钮
	 */
	public void TongBuSurveryContinue(boolean isTongbu) {
		if (isTongbu) {
			if (topBtnSumbitView.getBtnSurveyTongBu().getVisibility() == View.GONE)
				topBtnSumbitView.getBtnSurveyTongBu().setVisibility(View.VISIBLE);

			if (topBtnSumbitView.getBtnSurveyContinue().getVisibility() == View.VISIBLE)
				topBtnSumbitView.getBtnSurveyContinue().setVisibility(View.GONE);

		} else {

			if (topBtnSumbitView.getBtnSurveyTongBu().getVisibility() == View.VISIBLE)
				topBtnSumbitView.getBtnSurveyTongBu().setVisibility(View.GONE);
			if (topBtnSumbitView.getBtnSurveyContinue().getVisibility() == View.GONE)
				topBtnSumbitView.getBtnSurveyContinue().setVisibility(View.VISIBLE);

		}

	}

	/*****
	 * 理赔定损任务同步继续操作 控制控件显示
	 * 
	 * @param btnTongBuDeflossContinue
	 */
	public void TongBuDeflossContinue(boolean isTongbu) {
		if (isTongbu) {

			if (topBtnSumbitView.getBtnDeflossTongBu().getVisibility() == View.GONE)
				topBtnSumbitView.getBtnDeflossTongBu().setVisibility(View.VISIBLE);

			if (topBtnSumbitView.getBtnDeflossContinue().getVisibility() == View.VISIBLE)
				topBtnSumbitView.getBtnDeflossContinue().setVisibility(View.GONE);

		} else {

			if (topBtnSumbitView.getBtnDeflossTongBu().getVisibility() == View.VISIBLE)
				topBtnSumbitView.getBtnDeflossTongBu().setVisibility(View.GONE);
			if (topBtnSumbitView.getBtnDeflossContinue().getVisibility() == View.GONE)
				topBtnSumbitView.getBtnDeflossContinue().setVisibility(View.VISIBLE);
		}

	}

	/*****
	 * 理赔定损任务同步继续操作
	 * 
	 * @param btnTongBuDeflossContinue
	 */
	public void btnTongBuDeflossContinue(final TopBtnTongBuDeflossContinue btnTongBuDeflossContinue) {

		this.btnTongBuDeflossContinue = btnTongBuDeflossContinue;

		topBtnSumbitView.getBtnDeflossContinue().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btnTongBuDeflossContinue.onClick();

				pop.dismiss();

			}
		});

	}

	/***
	 * 定损理赔同步和提交按钮
	 * 
	 * @param topBtnSumbitDefloss
	 */
	public void setTopBtnSumbitDefloss(final TopBtnSumbitDefloss topBtnSumbitDefloss) {
		this.topBtnSumbitDefloss = topBtnSumbitDefloss;

		topBtnSumbitView.getBtnDeflossTongBu().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				topBtnSumbitDefloss.OnclickTongBu();

				pop.dismiss();

			}
		});

		topBtnSumbitView.getBtnDeflossSumbit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				topBtnSumbitDefloss.OnclickSumbit();

				pop.dismiss();

			}
		});

	}

	/*****
	 * 查勘理赔同步和提交按钮
	 * 
	 * @param topBtnSumbitSurvery
	 */
	public void setTopBtnSumbitSurvery(final TopBtnSumbitSurvery topBtnSumbitSurvery) {
		this.topBtnSumbitSurvery = topBtnSumbitSurvery;

		topBtnSumbitView.getBtnSurveySumbit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				topBtnSumbitSurvery.OnclickSumbit();

				pop.dismiss();

			}
		});

		topBtnSumbitView.getBtnSurveyTongBu().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				topBtnSumbitSurvery.OnclickTongBu();

				pop.dismiss();

			}
		});

	}

	public interface IWordSurveyTopButton {
		/***
		 * 查勘基本信息按钮
		 */
		void onSurveyBasicClick();

		/***
		 * 查勘信息按钮
		 */
		void onSurveyInfoClick();

		/***
		 * 查勘拍照按钮
		 */
		void onSurveyPhotoClick();

		/***
		 * 查勘流程按钮
		 */
		void onSurveyWorkFlowlogClick();
	}

	// 定损导航菜单
	private LinearLayout deflossContainer;// 定损导航容器
	private Button btnDeflossBasicMsg;// 定损基本信息
	private Button btnDeflossMsg;// 定损信息
	private Button btnDeflossPhoto;// 定损拍照
	private Button btnDeflossProcess;// 定损流程
	private IWordDeflossTopButton ideflossTopButton;

	public interface IWordDeflossTopButton {
		/***
		 * 定损基本信息按钮
		 */
		void onDeflossBasicClick();

		/***
		 * 定损信息按钮
		 */
		void onDeflossInfoClick();

		/***
		 * 定损拍照按钮
		 */
		void onDeflossPhotoClick();

		/***
		 * 定损流程按钮
		 */
		void onDeflossWorkFlowlogClick();
	}

	private TopManager() {
	}

	/***
	 * 获取单例实例
	 * 
	 * @return
	 */
	public static TopManager getInstance() {
		return topmanager;
	}

	/****
	 * 视频协助按钮
	 * 
	 * @param btnVideo
	 */
	public void xiePeiYuanBtnViedo(final xiePeiBtnVideo btnVideo) {
		this.btnVideo = btnVideo;

		// 视频协助按钮
		BtnVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnVideo.onClick();

			}
		});

	}

	public void init(final Activity activity) {

		this.context = activity;
		topBtnSumbitView = new TopBtnSumbitView(activity);
		topContainer = (RelativeLayout) activity.findViewById(R.id.activity_main_top_r1);
		frameLayoutLeft = (FrameLayout) activity.findViewById(R.id.activity_main_top_fl_left);
		frameLayoutRight = (FrameLayout) activity.findViewById(R.id.activity_main_top_fl_right);
		returnButton = (ImageButton) activity.findViewById(R.id.activity_main_top_r1_imb_back);
		homeButton = (ImageButton) activity.findViewById(R.id.activity_main_top_r1_imb_home);
		exitButton = (ImageButton) activity.findViewById(R.id.activity_main_top_r1_imb_exit);
		imbtnTongBu = (ImageButton) activity.findViewById(R.id.activity_main_top_r1_imb_tongbu);
		titleText = (TextView) activity.findViewById(R.id.activity_main_top_r1_title);
		BtnVideo = (Button) activity.findViewById(R.id.activity_main_top_r3_video);

		listView = new ListView(context);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);

		// 顶部导航按钮
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(Tag, "返回按钮");
				boolean result = true;

				result = UiManager.getInstance().changeCacheView();

				System.out.println("返回界面缓存大小" + UiManager.getBACKVIEW().size());
				System.out.println("界面缓存" + UiManager.getVIEWCACHE().size());

			}
		});

		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(Tag, "返回主界面");

				if (SystemConfig.isDeflossPage) {
					// 清空数据缓存
					UiManager.getInstance().emptyViewCache();
					UiManager.getInstance().changeView(DelossTaskActivity.class, null, true);
				} else {
					// 清空数据缓存
					UiManager.getInstance().emptyViewCache();
					UiManager.getInstance().changeView(SurveyTaskActivity.class, null, true);
				}

			}
		});
		exitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(Tag, "退出这个系统");

				PromptManager.showExitDialog(activity);

			}
		});
		// 理赔和提交按钮
		imbtnTongBu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(Tag, "理赔和提交按钮");
				if (pop == null) {

					pop = new PopupWindow(listView, imbtnTongBu.getWidth() + 250, LayoutParams.WRAP_CONTENT, true);
					// 设置popupwindow背景色 功能点击popupwindow之外的部分 可以将popupwindow隐藏
					pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
					pop.showAsDropDown(imbtnTongBu);
				}
				if (isPopShow) {
					pop.dismiss();
				} else {
					pop.showAsDropDown(imbtnTongBu);
				}
			}
		});

		LossOfPartContainer = (LinearLayout) activity.findViewById(R.id.activity_main_top_r3);
		surveyContainer = (LinearLayout) activity.findViewById(R.id.activity_main_top_survey);
		btnSurveyBasicMsg = (Button) activity.findViewById(R.id.activity_main_top_survey_basic_msg);
		btnSurveyMsg = (Button) activity.findViewById(R.id.activity_main_top_survey_survey_msg);
		btnSurveyPhoto = (Button) activity.findViewById(R.id.activity_main_top_survey_photograph_msg);
		btnSurveyProcess = (Button) activity.findViewById(R.id.activity_main_top_survey_process_query);
		deflossContainer = (LinearLayout) activity.findViewById(R.id.activity_main_top_defloss);

		btnDeflossMsg = (Button) activity.findViewById(R.id.activity_main_top_defloss1_claim_msg);
		btnDeflossBasicMsg = (Button) activity.findViewById(R.id.activity_main_top_defloss1_basic_msg);
		btnDeflossPhoto = (Button) activity.findViewById(R.id.activity_main_top_defloss1_photograph_msg);
		btnDeflossProcess = (Button) activity.findViewById(R.id.activity_main_top_defloss1_process_query);

	}

	// 定损按钮页面
	public void deflossPageTopBtn(final IWordDeflossTopButton ideflossTopButton) {
		this.ideflossTopButton = ideflossTopButton;
		btnDeflossBasicMsg.setOnClickListener(new OnClickListener() {
			// 基本信息按钮
			@Override
			public void onClick(View v) {
				btnDeflossBasicMsg.setBackgroundResource(R.drawable.tasks_infortab_click);
				btnDeflossMsg.setBackgroundResource(R.drawable.tasks_setlosstab);
				btnDeflossPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnDeflossProcess.setBackgroundResource(R.drawable.tasks_processestab);
				ideflossTopButton.onDeflossBasicClick();

			}
		});

		btnDeflossMsg.setOnClickListener(new OnClickListener() {
			// 定损信息按钮
			@Override
			public void onClick(View v) {
				btnDeflossBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnDeflossMsg.setBackgroundResource(R.drawable.tasks_setlosstab_click);
				btnDeflossPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnDeflossProcess.setBackgroundResource(R.drawable.tasks_processestab);
				ideflossTopButton.onDeflossInfoClick();
			}
		});
		btnDeflossPhoto.setOnClickListener(new OnClickListener() {
			// 拍照按钮
			@Override
			public void onClick(View v) {
				btnDeflossBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnDeflossMsg.setBackgroundResource(R.drawable.tasks_setlosstab);
				btnDeflossPhoto.setBackgroundResource(R.drawable.tasks_phototab_click);
				btnDeflossProcess.setBackgroundResource(R.drawable.tasks_processestab);
				ideflossTopButton.onDeflossPhotoClick();
			}
		});

		btnDeflossProcess.setOnClickListener(new OnClickListener() {
			// 流程信息按钮
			@Override
			public void onClick(View v) {
				btnDeflossBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnDeflossMsg.setBackgroundResource(R.drawable.tasks_setlosstab);
				btnDeflossPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnDeflossProcess.setBackgroundResource(R.drawable.tasks_processestab_click);
				ideflossTopButton.onDeflossWorkFlowlogClick();
			}
		});
	}

	/***
	 * 查勘页面顶部4个按钮
	 * 
	 * @param isurveyTopButton
	 */
	public void surveyPageTopBtn(final IWordSurveyTopButton isurveyTopButton) {
		this.isurveyTopButton = isurveyTopButton;
		btnSurveyBasicMsg.setOnClickListener(new OnClickListener() {
			// 基本信息按钮
			@Override
			public void onClick(View v) {
				btnSurveyBasicMsg.setBackgroundResource(R.drawable.tasks_infortab_click);
				btnSurveyMsg.setBackgroundResource(R.drawable.tasks_chakantab);
				btnSurveyPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnSurveyProcess.setBackgroundResource(R.drawable.tasks_processestab);
				isurveyTopButton.onSurveyBasicClick();

			}
		});

		btnSurveyMsg.setOnClickListener(new OnClickListener() {
			// 查勘信息按钮
			@Override
			public void onClick(View v) {
				btnSurveyBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnSurveyMsg.setBackgroundResource(R.drawable.tasks_chakantab_click);
				btnSurveyPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnSurveyProcess.setBackgroundResource(R.drawable.tasks_processestab);
				isurveyTopButton.onSurveyInfoClick();

			}
		});
		btnSurveyPhoto.setOnClickListener(new OnClickListener() {
			// 拍照按钮
			@Override
			public void onClick(View v) {
				btnSurveyBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnSurveyMsg.setBackgroundResource(R.drawable.tasks_chakantab);
				btnSurveyPhoto.setBackgroundResource(R.drawable.tasks_phototab_click);
				btnSurveyProcess.setBackgroundResource(R.drawable.tasks_processestab);
				isurveyTopButton.onSurveyPhotoClick();

			}
		});

		btnSurveyProcess.setOnClickListener(new OnClickListener() {
			// 流程信息按钮
			@Override
			public void onClick(View v) {
				btnSurveyBasicMsg.setBackgroundResource(R.drawable.tasks_infortab);
				btnSurveyMsg.setBackgroundResource(R.drawable.tasks_chakantab);
				btnSurveyPhoto.setBackgroundResource(R.drawable.tasks_phototab);
				btnSurveyProcess.setBackgroundResource(R.drawable.tasks_processestab_click);
				isurveyTopButton.onSurveyWorkFlowlogClick();
			}
		});
	}

	/****
	 * 是否显示查勘/定损工作导航菜单
	 * 
	 * @param isShow
	 */
	public void isShowWordTop(boolean isShow) {
		if (isShow) {
			// 显示
			if (surveyContainer.getVisibility() == View.GONE)
				surveyContainer.setVisibility(View.VISIBLE);
			if (deflossContainer.getVisibility() == View.GONE)
				deflossContainer.setVisibility(View.VISIBLE);

		} else {
			// 不显示
			if (surveyContainer.getVisibility() == View.VISIBLE)
				surveyContainer.setVisibility(View.GONE);
			if (deflossContainer.getVisibility() == View.VISIBLE)
				deflossContainer.setVisibility(View.GONE);
		}
	}

	/****
	 * 控制顶部导航显隐
	 * 
	 * @param showTopFlage
	 *            是否显示顶部导航按钮
	 * @param showTopSurvey
	 *            是否显示查勘按钮
	 * @param showTopDefloss
	 *            是否显示定损按钮
	 * @param showLossOfPart
	 *            是否显示协助员的按钮导航
	 */
	public void isShowTop(boolean showTopFlage, boolean showTopSurvey, boolean showTopDefloss, boolean showLossOfPart) {
		if (showTopFlage) {
			if (topContainer.getVisibility() == View.GONE)
				topContainer.setVisibility(View.VISIBLE);

		} else {
			if (topContainer.getVisibility() == View.VISIBLE)
				topContainer.setVisibility(View.GONE);
		}

		// 查勘的导航按钮是否显示
		if (showTopSurvey) {
			if (surveyContainer.getVisibility() == View.GONE)
				surveyContainer.setVisibility(View.VISIBLE);

		} else {
			if (surveyContainer.getVisibility() == View.VISIBLE)
				surveyContainer.setVisibility(View.GONE);

		}

		// 定损的导航按钮是否显示
		if (showTopDefloss) {
			if (deflossContainer.getVisibility() == View.GONE)
				deflossContainer.setVisibility(View.VISIBLE);

		} else {
			if (deflossContainer.getVisibility() == View.VISIBLE)
				deflossContainer.setVisibility(View.GONE);

		}

		// 协助员的导航按钮是否显示
		if (showLossOfPart) {

			if (LossOfPartContainer.getVisibility() == View.GONE)
				LossOfPartContainer.setVisibility(View.VISIBLE);
		} else {
			if (LossOfPartContainer.getVisibility() == View.VISIBLE)
				LossOfPartContainer.setVisibility(View.GONE);
		}
	}

	// /***
	// * 设置顶部导航的标题
	// *
	// * @param title
	// */
	// public void setHeadTitle(String title) {
	// titleText.setText(title);
	// }

	/***
	 * 设置顶部导航的标题
	 * 
	 * @param title
	 */
	public void setHeadTitle(String title, float size, Typeface tf) {
		titleText.setText(title);
		titleText.setTextSize(size);
		titleText.setTypeface(tf);
	}

	/***
	 * 
	 * @param showBack
	 * @param showHome
	 * @param showExit
	 * @param showRight
	 */
	public void showTopTitle(boolean showBack, boolean showHome, boolean showExit, boolean showRight) {
		// 设置返回按钮显示状态
		setTopButtonVisible(showBack, returnButton);
		// 设置返回主界面按钮显示状态
		setTopButtonVisible(showHome, homeButton);
		// 设置退出按钮显示状态
		setTopButtonVisible(showExit, exitButton);
		// 设置视频协助按钮显示状态
		setTopButtonVisible(showRight, frameLayoutRight);
	}

	/**
	 * 控制控件的显示情况
	 * 
	 * @param isShow
	 * @param view
	 */
	private void setTopButtonVisible(boolean isShow, View view) {
		if (isShow) {
			int visibility = view.getVisibility();
			if (visibility != View.VISIBLE) {
				view.setVisibility(View.VISIBLE);
			}
		} else {
			int visibility = view.getVisibility();
			if (visibility == View.VISIBLE)
				view.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 控制控件的显示情况 chen jianfan add 2013- 05-21
	 * 
	 * @param isShow
	 * @param view
	 */
	private void setTopButtonVisible1(boolean isShow, View view) {
		if (isShow) {

			if (view.getVisibility() == View.GONE) {
				view.setVisibility(View.VISIBLE);
			}
		} else {

			if (view.getVisibility() == View.VISIBLE)
				view.setVisibility(View.GONE);
		}
	}

	/***
	 * 根据ConstantValue
	 */
	@Override
	public void update(Observable observable, Object data) {
		if (data != null) {
			Log.i(Tag, data.toString());
			if (StringUtils.isNumeric(data.toString())) {
				int type = Integer.parseInt(data.toString());
				switch (type) {
				case ConstantValue.Page_Login:
					// 顶部导航不显示
					TopManager.getInstance().isShowTop(false, false, false, false);
					// 底部导航不显示
					BottomManager.getInstance().isShowBottom(false);
					break;
				case ConstantValue.Page_second:
					// 顶部导航显示
					TopManager.getInstance().showTopTitle(false, false, false, false);
					TopManager.getInstance().isShowTop(true, false, false, false);
					// 底部导航显示
					BottomManager.getInstance().isShowBottom(true);
					break;
				case ConstantValue.Page_Defloss:// 定损
					// 顶部导航显示

					if (SystemConfig.UserRightIsAdvanced)
						TopManager.getInstance().isShowTop(true, false, true, false);
					else {

						TopManager.getInstance().isShowTop(true, false, false, true);

					}

					TopManager.getInstance().showTopTitle(false, true, false, true);

					// 底部导航显示
					BottomManager.getInstance().isShowBottom(false);

					// 提交和同步按钮
					topBtnSumbitView.controlShow(true, false);
					break;

				case ConstantValue.Page_Survey:// 查勘
					// 顶部导航显示
					if (SystemConfig.UserRightIsAdvanced)
						TopManager.getInstance().isShowTop(true, true, false, false);
					else {
						TopManager.getInstance().isShowTop(true, false, false, true);
					}

					TopManager.getInstance().showTopTitle(false, true, false, true);

					// 底部导航显示
					BottomManager.getInstance().isShowBottom(false);
					// 提交和同步按钮
					topBtnSumbitView.controlShow(false, true);
					break;

				case ConstantValue.Page_third:
					// 顶部导航显示
					TopManager.getInstance().isShowTop(true, false, false, false);
					TopManager.getInstance().showTopTitle(true, false, false, false);
					// 底部导航显示
					BottomManager.getInstance().isShowBottom(false);
					break;
				}

			}

		}

	}

	public Button getBtnSurveyBasicMsg() {
		return btnSurveyBasicMsg;
	}

	public void setBtnSurveyBasicMsg(Button btnSurveyBasicMsg) {
		this.btnSurveyBasicMsg = btnSurveyBasicMsg;
	}

	public Button getBtnSurveyMsg() {
		return btnSurveyMsg;
	}

	public void setBtnSurveyMsg(Button btnSurveyMsg) {
		this.btnSurveyMsg = btnSurveyMsg;
	}

	public Button getBtnSurveyPhoto() {
		return btnSurveyPhoto;
	}

	public void setBtnSurveyPhoto(Button btnSurveyPhoto) {
		this.btnSurveyPhoto = btnSurveyPhoto;
	}

	public Button getBtnSurveyProcess() {
		return btnSurveyProcess;
	}

	public void setBtnSurveyProcess(Button btnSurveyProcess) {
		this.btnSurveyProcess = btnSurveyProcess;
	}

	public Button getBtnDeflossBasicMsg() {
		return btnDeflossBasicMsg;
	}

	public void setBtnDeflossBasicMsg(Button btnDeflossBasicMsg) {
		this.btnDeflossBasicMsg = btnDeflossBasicMsg;
	}

	public Button getBtnDeflossMsg() {
		return btnDeflossMsg;
	}

	public void setBtnDeflossMsg(Button btnDeflossMsg) {
		this.btnDeflossMsg = btnDeflossMsg;
	}

	public Button getBtnDeflossPhoto() {
		return btnDeflossPhoto;
	}

	public void setBtnDeflossPhoto(Button btnDeflossPhoto) {
		this.btnDeflossPhoto = btnDeflossPhoto;
	}

	public Button getBtnDeflossProcess() {
		return btnDeflossProcess;
	}

	public void setBtnDeflossProcess(Button btnDeflossProcess) {
		this.btnDeflossProcess = btnDeflossProcess;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return 1;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return topBtnSumbitView.getView();
		}

	}

	/*****
	 * 协助和同步按钮显示和不显示
	 */
	public void controlSumitAndTongbu(boolean showFlage) {
		if (showFlage) {
			if (imbtnTongBu.getVisibility() == View.GONE)
				imbtnTongBu.setVisibility(View.VISIBLE);

		} else {
			if (imbtnTongBu.getVisibility() == View.VISIBLE)
				imbtnTongBu.setVisibility(View.GONE);
		}

	}

	public ImageButton getImbtnTongBu() {
		return imbtnTongBu;
	}

	public void setImbtnTongBu(ImageButton imbtnTongBu) {
		this.imbtnTongBu = imbtnTongBu;
	}

	public FrameLayout getFrameLayoutRight() {
		return frameLayoutRight;
	}

	public void setFrameLayoutRight(FrameLayout frameLayoutRight) {
		this.frameLayoutRight = frameLayoutRight;
	}

}
