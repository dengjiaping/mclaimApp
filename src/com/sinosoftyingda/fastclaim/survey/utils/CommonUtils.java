package com.sinosoftyingda.fastclaim.survey.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.CheckExt;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch;
import com.sinosoftyingda.fastclaim.common.ui.utils.MySlipSwitch.OnSwitchListener;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;

/***
 * 
 * @author chenjianfan
 * 
 */
public class CommonUtils {

	protected static final String Tag = "CommonUtils";
	private Ifunction ifunction;
	// 滑动按钮
	private ISlipSwitch iSlipSwitch;

	public interface ISlipSwitch {

		/***
		 * 是按钮
		 */
		void setYesButton();

		/**
		 * 否按钮
		 */
		void setNoButton();

	}

	public interface Ifunction {

		void setFunction();

	}

	/*****
	 * 控件的收缩功能
	 * 
	 * @param rocketAnimation
	 *            动画AnimationDrawable
	 * @param listview
	 *            控件listview
	 * @param ACaseImv
	 *            要展示动画的控件imagevie
	 * @param ifunction
	 *            暴露出来的方法（每个继承的中可以自定义自己的方法）
	 */
	public void showView(Context context, RelativeLayout rlLayout,
			AnimationDrawable rocketAnimation, View listview,
			ImageView aCaseImv, Ifunction ifunction) {
		this.ifunction = ifunction;
		if (listview.getVisibility() == View.GONE) {
			// 伸开
			listview.setVisibility(View.VISIBLE);
			rlLayout.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.tasks_opentop));

			ifunction.setFunction();
			// 动画
			aCaseImv.setBackgroundResource(R.drawable.survery_rotate);
			rocketAnimation = (AnimationDrawable) aCaseImv.getBackground();
			rocketAnimation.start();
		} else if (listview.getVisibility() == View.VISIBLE) {
			// 收回来
			listview.setVisibility(View.GONE);
			rlLayout.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.tasks_first));

			aCaseImv.setBackgroundResource(R.drawable.reverse_survery_rotate);
			rocketAnimation = (AnimationDrawable) aCaseImv.getBackground();
			rocketAnimation.start();
		}

	}

	/*****
	 * 控件的收缩功能
	 * 
	 * @param rocketAnimation
	 *            动画AnimationDrawable
	 * @param listview
	 *            控件listview
	 * 
	 * @param ifunction
	 *            暴露出来的方法（每个继承的中可以自定义自己的方法）
	 */
	public void showView(Context context, ListView listview, Ifunction ifunction) {
		this.ifunction = ifunction;
		if (listview.getVisibility() == View.GONE) {
			// 伸开
			listview.setVisibility(View.VISIBLE);
			ifunction.setFunction();
			// 解决在ScrollView中嵌套使用ListView，ListView只会显示一行多一点的问题
			new ScrollListViewUtils()
					.setListViewHeightBasedOnChildren(listview);
		} else if (listview.getVisibility() == View.VISIBLE) {
			// 收回来
			listview.setVisibility(View.GONE);
		}

	}

	public void showView(Context context, View rlLayout,
			AnimationDrawable rocketAnimation, View listview,
			ImageView ACaseImv, Ifunction ifunction) {
		this.ifunction = ifunction;
		if (listview.getVisibility() == View.GONE) {
			// 伸开
			listview.setVisibility(View.VISIBLE);
			rlLayout.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.tasks_opentop));

			ifunction.setFunction();
			// 动画
			ACaseImv.setBackgroundResource(R.drawable.survery_rotate);
			rocketAnimation = (AnimationDrawable) ACaseImv.getBackground();
			rocketAnimation.start();
		} else if (listview.getVisibility() == View.VISIBLE) {
			// 收回来
			listview.setVisibility(View.GONE);
			rlLayout.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.tasks_first));

			ACaseImv.setBackgroundResource(R.drawable.reverse_survery_rotate);
			rocketAnimation = (AnimationDrawable) ACaseImv.getBackground();
			rocketAnimation.start();
		}

	}

	/*****
	 * 滑动按钮
	 * 
	 * @param btnSlipSwitch
	 */
	public void setYseAndrNo(final MySlipSwitch btnSlipSwitch,
			final ISlipSwitch iSlipSwitch) {
		this.iSlipSwitch = iSlipSwitch;
		// 滑动按钮

		btnSlipSwitch.setOnSwitchListener(new OnSwitchListener() {
			@Override
			public void onSwitched(boolean isSwitchOn) {

				if (isSwitchOn) {
					Log.i(Tag, "否");

					iSlipSwitch.setNoButton();
				} else {
					Log.i(Tag, "是");

					iSlipSwitch.setYesButton();
				}
			}
		});
	}

	/*****
	 * 滑动按钮和备注输入框的初始化数据
	 * 
	 * @param i
	 * @param btn
	 * @param edView
	 */
	public void setSlipSwitchBtnShowView(int i, MySlipSwitch btn,
			EditText edView, CheckExt checkExts) {
		Log.i(Tag, "选中的项目" + checkExts.getCheckKernelSelect());
		if ("1".equals(checkExts.getCheckKernelSelect())) {
			// 是
			btn.setSwitchState(false);
			Log.i(Tag, "111111=" + checkExts.getCheckKernelSelect());
			btn.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno,
					R.drawable.yes_green);
			if (edView.getVisibility() == View.VISIBLE)
				edView.setVisibility(View.GONE);
		} else if ("0".equals(checkExts.getCheckKernelSelect())) {// 否
			Log.i(Tag, "00000=" + checkExts.getCheckKernelSelect());
			btn.setSwitchState(true);
			btn.setImageResource(R.drawable.btn_yesno, R.drawable.btn_yesno,
					R.drawable.yes_green);
			if (edView.getVisibility() == View.GONE) {
				edView.setVisibility(View.VISIBLE);
			}
			edView.setText(checkExts.getCheckExtRemark());
		}
	}

	/*****
	 * 滑动按钮和备注输入框的初始化数据
	 * 
	 * @param i
	 * @param btn
	 * @param edView
	 */
	public void setSlipSwitchBtnShowView(int i, MySlipSwitch btn,
			CheckExt checkExts) {
		if ("1".equals(checkExts.getCheckKernelSelect())) {
			// 是
			btn.setSwitchState(false);

		} else if ("0".equals(checkExts.getCheckKernelSelect())) {
			// 否
			btn.setSwitchState(true);

		}
	}

	/********
	 * 十四个查勘要点
	 * 
	 * @return
	 */
	public static List<CheckExt> getCheckExtS() {

		List<CheckExt> checkExts = new ArrayList<CheckExt>();

		// 1：标的勘验
		CheckExt ceCarNo = new CheckExt();
		ceCarNo.setCheckKernelType("1");
		ceCarNo.setCheckKernelName("车牌号是否相符");
		ceCarNo.setSerialNo("1");
		ceCarNo.setCheckKernelCode("CheckExt01");
		ceCarNo.setCheckKernelSelect("1");
		ceCarNo.setCheckExtRemark("");
		checkExts.add(ceCarNo);

		CheckExt ceChassis = new CheckExt();
		ceChassis.setCheckKernelType("1");// 标的勘验
		ceChassis.setCheckKernelName("发动机号是否相符");
		ceChassis.setSerialNo("2");
		ceChassis.setCheckKernelCode("CheckExt02");
		ceChassis.setCheckKernelSelect("1");
		ceChassis.setCheckExtRemark("");
		checkExts.add(ceChassis);

		CheckExt ceDriver = new CheckExt();
		ceDriver.setCheckKernelType("1");// 标的勘验
		ceDriver.setCheckKernelName("车架号是否相符");
		ceDriver.setSerialNo("3");
		ceDriver.setCheckKernelCode("CheckExt03");
		ceDriver.setCheckKernelSelect("1");
		ceDriver.setCheckExtRemark("");
		checkExts.add(ceDriver);

		CheckExt ceEngine = new CheckExt();
		ceEngine.setCheckKernelType("1");// 标的勘验
		ceEngine.setCheckKernelName("使用性质是否相符");
		ceEngine.setSerialNo("4");
		ceEngine.setCheckKernelCode("CheckExt04");
		ceEngine.setCheckKernelSelect("1");
		checkExts.add(ceEngine);

		CheckExt cedriver = new CheckExt();
		cedriver.setCheckKernelType("1");// 标的勘验
		cedriver.setCheckKernelName("行驶证是否有效");
		cedriver.setSerialNo("5");
		cedriver.setCheckKernelCode("CheckExt05");
		cedriver.setCheckKernelSelect("1");
		checkExts.add(cedriver);

		// 2：标的车驾驶员

		CheckExt ceBaoanDriver = new CheckExt();
		ceBaoanDriver.setCheckKernelType("2");
		ceBaoanDriver.setCheckKernelName("是否为报案驾驶员");
		ceBaoanDriver.setSerialNo("6");
		ceBaoanDriver.setCheckKernelCode("CheckExt06");
		ceBaoanDriver.setCheckKernelSelect("1");
		ceBaoanDriver.setCheckExtRemark("");
		checkExts.add(ceBaoanDriver);

		CheckExt ceAllowdDriver = new CheckExt();
		ceAllowdDriver.setCheckKernelType("2");
		ceAllowdDriver.setCheckKernelName("是否为允许的驾驶员");
		ceAllowdDriver.setSerialNo("7");
		ceAllowdDriver.setCheckKernelCode("CheckExt07");
		ceAllowdDriver.setCheckKernelSelect("1");
		checkExts.add(ceAllowdDriver);

		CheckExt ceModelstype = new CheckExt();
		ceModelstype.setCheckKernelType("2");
		ceModelstype.setCheckKernelName("准驾车型是否相符");
		ceModelstype.setSerialNo("8");
		ceModelstype.setCheckKernelCode("CheckExt08");
		ceModelstype.setCheckKernelSelect("1");
		checkExts.add(ceModelstype);

		CheckExt ceValidDriver = new CheckExt();
		ceValidDriver.setCheckKernelType("2");
		ceValidDriver.setCheckKernelName("驾驶证是否有效");
		ceValidDriver.setSerialNo("9");
		ceValidDriver.setCheckKernelCode("CheckExt09");
		ceValidDriver.setCheckKernelSelect("1");
		checkExts.add(ceValidDriver);

		CheckExt ceDrunkDriving = new CheckExt();
		ceDrunkDriving.setCheckKernelType("2");
		ceDrunkDriving.setCheckKernelName("是否酒后驾车");
		ceDrunkDriving.setSerialNo("10");
		ceDrunkDriving.setCheckKernelCode("CheckExt10");
		ceDrunkDriving.setCheckKernelSelect("0");
		checkExts.add(ceDrunkDriving);

		// 3 事故信息
		CheckExt ceCarLoss = new CheckExt();
		ceCarLoss.setCheckKernelType("3");
		ceCarLoss.setCheckKernelName("车损与事故是否相符");
		ceCarLoss.setSerialNo("11");
		ceCarLoss.setCheckKernelCode("CheckExt11");
		ceCarLoss.setCheckKernelSelect("1");
		checkExts.add(ceCarLoss);

		CheckExt ceViolate = new CheckExt();
		ceViolate.setCheckKernelType("3");
		ceViolate.setCheckKernelName("是否违反装载规定");
		ceViolate.setSerialNo("12");
		ceViolate.setCheckKernelCode("CheckExt12");
		ceViolate.setCheckKernelSelect("0");
		checkExts.add(ceViolate);

		CheckExt ceStrong = new CheckExt();
		ceStrong.setCheckKernelType("3");
		ceStrong.setCheckKernelName("是否属交强险保险责任");
		ceStrong.setSerialNo("13");
		ceStrong.setCheckKernelCode("CheckExt13");
		ceStrong.setCheckKernelSelect("1");
		checkExts.add(ceStrong);

		CheckExt ceBusiness = new CheckExt();
		ceBusiness.setCheckKernelType("3");
		ceBusiness.setCheckKernelName("是否属商业险保险责任");
		ceBusiness.setSerialNo("14");
		ceBusiness.setCheckKernelCode("CheckExt14");
		ceBusiness.setCheckKernelSelect("1");
		checkExts.add(ceBusiness);

		return checkExts;
	}

	/**
	 * 
	 * @param carNo
	 * @return true 正确的格式 false 错误格式
	 */
	public static boolean vehicleNoStyleMethod(String carNo) {
		boolean matchesvaule=false;
		String vehicleNoStyle = "^[陆海空军京津冀晋蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新北南济广成沈兰][A-Z]+[A-Z0-9]+[\u4e00-\u9fa5]?$";
		if(carNo.substring(0, 2).equals("WJ")){
			matchesvaule = true;
		}else{
			Pattern pattern = Pattern.compile(vehicleNoStyle);
			Matcher matcher = pattern.matcher(carNo);
			matchesvaule	=	matcher.matches();
		}

		return matchesvaule;
	}
}
