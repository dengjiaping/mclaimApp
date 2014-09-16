package com.sinosoftyingda.fastclaim.common.views;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.view.View;

public class BackMainPageUtils implements Observer {

	@Override
	public void update(Observable observable, Object data) {

		if (data != null) {
			if (StringUtils.isNumeric(data.toString())) {
				int type = Integer.parseInt(data.toString());
				switch (type) {
				case 0:
					// 查勘员

					if (BottomManager.getInstance().getLayout2().getVisibility() == View.GONE)
						BottomManager.getInstance().getLayout2().setVisibility(View.VISIBLE);

					break;
				case 1:
				// 协赔员
				{
					if (BottomManager.getInstance().getLayout2().getVisibility() == View.VISIBLE)
						BottomManager.getInstance().getLayout2().setVisibility(View.INVISIBLE);

					TopManager.getInstance().controlSumitAndTongbu(false);
					// BottomManager.getInstance().getBottomContainer().setGravity(Gravity.CENTER);
					// @+id/layout1

					// RelativeLayout.LayoutParams lp1 = new
					// RelativeLayout.LayoutParams(10, 0);
					// RelativeLayout.LayoutParams lp2 = new
					// RelativeLayout.LayoutParams(10, 0);
					// BottomManager.getInstance().getLayout1().setLayoutParams(lp1);
					// BottomManager.getInstance().getLayout3().setLayoutParams(lp2);
					// BottomManager.getInstance().getLayout3().setGravity(Gravity.RIGHT);

				}
					break;
				}

			}

		}
	}
}
