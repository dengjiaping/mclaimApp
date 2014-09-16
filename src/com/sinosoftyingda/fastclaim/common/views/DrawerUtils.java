package com.sinosoftyingda.fastclaim.common.views;

import com.sinosoftyingda.fastclaim.R;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

/****
 * 抽屉
 * 
 * @author chenjianfan
 * 
 */
public class DrawerUtils {
	private View gView;
	// 抽屉
	private RelativeLayout rldrawer;
	private SlidingDrawer sd;
	private Button btnAccept;
	private Button btnGaiPai;
	private ImageView imageViewIcon;
	private RelativeLayout rllayout;
	private IBtnAccept iBtnAccept;
	private IBtnGaiPai iBtnGaiPai;

	public interface IBtnAccept {
		void onClick();
	}

	public interface IBtnGaiPai {
		void onClick();
	}

	public DrawerUtils(View gView) {
		this.gView = gView;
		init();
	}

	private void init() {
		findView();
		setView();
	}

	private void findView() {
		// 抽屉
		sd = (SlidingDrawer) gView.findViewById(R.id.sliding);
		btnAccept = (Button) gView.findViewById(R.id.activity_main_bottom_btn_accept);
		btnGaiPai = (Button) gView.findViewById(R.id.activity_main_bottom_btn_gaipai);
		imageViewIcon = (ImageView) gView.findViewById(R.id.imageViewIcon);
		rllayout = (RelativeLayout) gView.findViewById(R.id.allApps);
		// LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// SystemConfig.SCREENHEIGHT - (SystemConfig.SCREENHEIGHT - 60));
		// rllayout.setLayoutParams(params);

		
	}

	private void setView() {
		setDrawer();

	}

	/**
	 * 设置抽屉的效果
	 */
	private void setDrawer() {

		sd.setOnDrawerScrollListener(new OnDrawerScrollListener() {

			@Override
			public void onScrollStarted() {
				Log.i("Tag", "开始拖动");
				imageViewIcon.setBackgroundResource(0);
				rllayout.setBackgroundResource(R.drawable.showbox);
			}

			@Override
			public void onScrollEnded() {
				Log.i("Tag", "结束拖动");
				imageViewIcon.setBackgroundResource(R.drawable.hiddenbox);
				rllayout.setBackgroundResource(R.drawable.show_bg);

			}
		});
		// 开启抽屉
		sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				imageViewIcon.setVisibility(View.INVISIBLE);
			}
		});
		// 关抽屉
		sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				imageViewIcon.setBackgroundResource(R.drawable.hiddenbox);

			}
		});

	}

	/***
	 * 接受任务按钮点击事件
	 */
	public void setAcceptOnClick(final IBtnAccept ibtnAccept) {
		this.iBtnAccept = ibtnAccept;

		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ibtnAccept.onClick();

			}
		});

	}

	/****
	 * 改派按钮点击事件
	 */
	public void setGaiPaiOnClick(final IBtnGaiPai iBtnGaiPai) {
		this.iBtnGaiPai = iBtnGaiPai;

		btnGaiPai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iBtnGaiPai.onClick();
			}
		});

	}

	/**
	 * 设置任务受理按钮是否可用
	 * 
	 * @param enabled
	 */
	public void setAcceptBtn(boolean enabled) {
		btnAccept.setEnabled(enabled);
	}

	/**
	 * 设置改派申请按钮是否可用
	 * 
	 * @param enabled
	 */
	public void setGaiPaiBtn(boolean enabled) {
		btnGaiPai.setEnabled(enabled);
	}

}
