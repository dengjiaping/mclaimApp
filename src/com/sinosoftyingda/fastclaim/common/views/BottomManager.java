package com.sinosoftyingda.fastclaim.common.views;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.more.page.MoreActivity;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;

/****
 * 底部导航控制器
 * @author chenjianfan
 * 
 */
public class BottomManager implements Observer {
	private static final BottomManager bottommanager = new BottomManager();
	private static final String Tag = "BottomManager";
	private RelativeLayout bottomContainer;
	private Activity activity;
	// 滑动的范围
	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout.LayoutParams rl;

	public RelativeLayout.LayoutParams getRl() {
		return rl;
	}

	public void setRl(RelativeLayout.LayoutParams rl) {
		this.rl = rl;
	}

	public RelativeLayout getBottomContainer() {
		return bottomContainer;
	}

	public void setBottomContainer(RelativeLayout bottomContainer) {
		this.bottomContainer = bottomContainer;
	}

	public RelativeLayout getLayout1() {
		return layout1;
	}

	public void setLayout1(RelativeLayout layout1) {
		this.layout1 = layout1;
	}

	public RelativeLayout getLayout3() {
		return layout3;
	}

	public void setLayout3(RelativeLayout layout3) {
		this.layout3 = layout3;
	}

	public RelativeLayout getLayout2() {
		return layout2;
	}

	public void setLayout2(RelativeLayout layout2) {
		this.layout2 = layout2;
	}

	private Button taskButton;
	private Button uploadButton;
	private Button moreButton;

	private ImageView first;
	private int current = 1;

	private boolean isAdd = false;
	private int select_width;
	private int select_height;
	private int firstLeft;
	private int startLeft;

	public ImageView getFirst() {
		return first;
	}

	public void setFirst(ImageView first) {
		this.first = first;
	}

	private BottomManager() {

	}

	public static BottomManager getInstance() {
		return bottommanager;
	}

	/***
	 * 控制顶部导航显隐
	 * 
	 * @param showBottomFlage
	 */
	public void isShowBottom(boolean showBottomFlage) {
		if (showBottomFlage) {
			if (bottomContainer.getVisibility() == View.GONE)
				bottomContainer.setVisibility(View.VISIBLE);
		} else {
			if (bottomContainer.getVisibility() == View.VISIBLE)
				bottomContainer.setVisibility(View.GONE);
		}
	}

	public void init(Activity activity) {
		this.activity = activity;

		// 抽屉

		// 导航按钮
		bottomContainer = (RelativeLayout) activity.findViewById(R.id.activity_main_bottom_r1);
		taskButton = (Button) activity.findViewById(R.id.activity_main_bottom_task);
		uploadButton = (Button) activity.findViewById(R.id.activity_main_bottom_upload);
		moreButton = (Button) activity.findViewById(R.id.activity_main_bottom_more);

		layout1 = (RelativeLayout) activity.findViewById(R.id.layout1);
		layout2 = (RelativeLayout) activity.findViewById(R.id.layout2);
		layout3 = (RelativeLayout) activity.findViewById(R.id.layout3);

		taskButton.setOnClickListener(onClickListener);
		uploadButton.setOnClickListener(onClickListener);
		moreButton.setOnClickListener(onClickListener);

		rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		first = new ImageView(activity);
		first.setTag("first");

		// first.setImageBitmap(bm);

		first.setImageResource(R.drawable.click);

		switch (current) {
		case 1:

			layout1.addView(first, rl);
			current = R.id.activity_main_bottom_task;
			break;
		case 2:

			layout2.addView(first, rl);
			current = R.id.activity_main_bottom_upload;
			break;
		case 3:

			layout3.addView(first, rl);
			current = R.id.activity_main_bottom_more;
			break;
		default:
			break;
		}

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isAdd) {
				replace();
				isAdd = true;
			}

			ImageView top_select = (ImageView) bottomContainer.findViewWithTag("move");
			int tabLeft;
			int endLeft = 0;

			boolean run = false;

			switch (v.getId()) {
			case R.id.activity_main_bottom_task:
				// layout2.setBackgroundDrawable(null);
				if (current != R.id.activity_main_bottom_task) {
					tabLeft = ((RelativeLayout) taskButton.getParent()).getLeft() + taskButton.getLeft() + taskButton.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.activity_main_bottom_task;
					run = true;

					UiManager.getInstance().changeView(SurveyTaskActivity.class, null, false);
				}
				break;
			case R.id.activity_main_bottom_upload:
				// layout2.setBackgroundDrawable(null);
				if (current != R.id.activity_main_bottom_upload) {

					tabLeft = ((RelativeLayout) uploadButton.getParent()).getLeft() + uploadButton.getLeft() + uploadButton.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.activity_main_bottom_upload;
					run = true;
					UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
				}
				break;
			case R.id.activity_main_bottom_more:
				// layout2.setBackgroundDrawable(null);
				if (current != R.id.activity_main_bottom_more) {
					// taskButton uploadButton moreButton
					tabLeft = ((RelativeLayout) moreButton.getParent()).getLeft() + moreButton.getLeft() + moreButton.getWidth() / 2;
					endLeft = tabLeft - select_width / 2;
					current = R.id.activity_main_bottom_more;
					run = true;
					UiManager.getInstance().changeView(MoreActivity.class, null, true);

				}
				break;

			}

			if (run) {
				TranslateAnimation animation = new TranslateAnimation(startLeft, endLeft - firstLeft, 0, 0);
				startLeft = endLeft - firstLeft;
				animation.setDuration(400);
				animation.setFillAfter(true);
				if (top_select != null) {
					top_select.bringToFront();
					top_select.startAnimation(animation);
				}
			}

		}

	};

	private void replace() {
		switch (current) {
		case R.id.activity_main_bottom_task:
			changeTop(layout1);
			break;
		case R.id.activity_main_bottom_upload:
			changeTop(layout2);
			break;
		case R.id.activity_main_bottom_more:
			changeTop(layout3);
			break;
		default:
			break;
		}
	}

	private void changeTop(RelativeLayout relativeLayout) {
		ImageView old = (ImageView) relativeLayout.findViewWithTag("first");
		select_width = old.getWidth();
		select_height = old.getHeight();
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(select_width, select_height);
		rl.leftMargin = old.getLeft() + ((RelativeLayout) old.getParent()).getLeft();
		rl.topMargin = old.getTop() + ((RelativeLayout) old.getParent()).getTop();

		firstLeft = old.getLeft() + ((RelativeLayout) old.getParent()).getLeft();
		ImageView iv = new ImageView(activity);
		iv.setTag("move");
		iv.setImageResource(R.drawable.click);
		bottomContainer.addView(iv, rl);
		relativeLayout.removeView(old);
	}

	@Override
	public void update(Observable observable, Object data) {

	}

	/*****
	 * 导航错位的问题
	 * 
	 * @param id
	 */
	public void daoHang(int id) {
		if (!isAdd) {
			replace();
			isAdd = true;
		}
		ImageView top_select = (ImageView) bottomContainer.findViewWithTag("move");
		int tabLeft;
		int endLeft = 0;

		boolean run = false;

		switch (id) {
		case R.id.activity_main_bottom_task:
			// layout2.setBackgroundDrawable(null);
			if (current != R.id.activity_main_bottom_task) {
				tabLeft = ((RelativeLayout) taskButton.getParent()).getLeft() + taskButton.getLeft() + taskButton.getWidth() / 2;
				endLeft = tabLeft - select_width / 2;
				current = R.id.activity_main_bottom_task;
				run = true;

			}
			break;
		case R.id.activity_main_bottom_upload:
			// layout2.setBackgroundDrawable(null);
			if (current != R.id.activity_main_bottom_upload) {

				tabLeft = ((RelativeLayout) uploadButton.getParent()).getLeft() + uploadButton.getLeft() + uploadButton.getWidth() / 2;
				endLeft = tabLeft - select_width / 2;
				current = R.id.activity_main_bottom_upload;
				run = true;
			}

			break;
		case R.id.activity_main_bottom_more:
			// layout2.setBackgroundDrawable(null);
			if (current != R.id.activity_main_bottom_more) {
				// taskButton uploadButton moreButton
				tabLeft = ((RelativeLayout) moreButton.getParent()).getLeft() + moreButton.getLeft() + moreButton.getWidth() / 2;
				endLeft = tabLeft - select_width / 2;
				current = R.id.activity_main_bottom_more;
				run = true;
			}
			break;

		}

		if (run) {
			TranslateAnimation animation = new TranslateAnimation(startLeft, endLeft - firstLeft, 0, 0);
			startLeft = endLeft - firstLeft;
			animation.setDuration(400);
			animation.setFillAfter(true);
			top_select.bringToFront();
			top_select.startAnimation(animation);
		}
	}

}
