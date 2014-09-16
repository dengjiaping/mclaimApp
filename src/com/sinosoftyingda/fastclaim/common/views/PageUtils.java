package com.sinosoftyingda.fastclaim.common.views;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.graphics.Typeface;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;

/*****
 * 
 * @author jianfan
 * 
 */
public class PageUtils implements Observer {

	private PromptManager promptManager;
	private Context context;

	public PageUtils(Context context) {
		this.context = context;
		promptManager = new PromptManager();
	}

	private static final String Tag = "PageUtils";

	@Override
	public void update(Observable observable, Object data) {
		if (data != null) {
			Log.i(Tag, data.toString());
			if (StringUtils.isNumeric(data.toString())) {
				int type = Integer.parseInt(data.toString());
				switch (type) {

				case ConstantValue.Exit_Page_Second_More:
					// promptManager.showExitDialog(context);
					break;
				case ConstantValue.DAOHANG_SurveyBasic:

					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("查勘基本信息");
					// TopManager.getInstance().setHeadTitle(DataConfig.tblTaskQuery.getRegistNo());
					TopManager.getInstance().getBtnSurveyBasicMsg().setBackgroundResource(R.drawable.tasks_infortab_click);
					TopManager.getInstance().getBtnSurveyMsg().setBackgroundResource(R.drawable.tasks_chakantab);
					TopManager.getInstance().getBtnSurveyPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnSurveyProcess().setBackgroundResource(R.drawable.tasks_processestab);
					break;
				case ConstantValue.DAOHANG_DeflossBasic:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("定损基本信息");
					TopManager.getInstance().getBtnDeflossBasicMsg().setBackgroundResource(R.drawable.tasks_infortab_click);
					TopManager.getInstance().getBtnDeflossMsg().setBackgroundResource(R.drawable.tasks_setlosstab);
					TopManager.getInstance().getBtnDeflossPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnDeflossProcess().setBackgroundResource(R.drawable.tasks_processestab);

					break;
				case ConstantValue.DAOHANG_SurveyInfo:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("查勘信息");
					TopManager.getInstance().getBtnSurveyBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnSurveyMsg().setBackgroundResource(R.drawable.tasks_chakantab_click);
					TopManager.getInstance().getBtnSurveyPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnSurveyProcess().setBackgroundResource(R.drawable.tasks_processestab);

					break;
				case ConstantValue.DAOHANG_DeflossInfo:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("定损信息");
					TopManager.getInstance().getBtnDeflossBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnDeflossMsg().setBackgroundResource(R.drawable.tasks_setlosstab_click);
					TopManager.getInstance().getBtnDeflossPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnDeflossProcess().setBackgroundResource(R.drawable.tasks_processestab);

					break;
				case ConstantValue.DAOHANG_SurveyPhoto:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("查勘拍照");
					TopManager.getInstance().getBtnSurveyBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnSurveyMsg().setBackgroundResource(R.drawable.tasks_chakantab);
					TopManager.getInstance().getBtnSurveyPhoto().setBackgroundResource(R.drawable.tasks_phototab_click);
					TopManager.getInstance().getBtnSurveyProcess().setBackgroundResource(R.drawable.tasks_processestab);

					break;
				case ConstantValue.DAOHANG_DeflossPhoto:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("定损拍照");
					TopManager.getInstance().getBtnDeflossBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnDeflossMsg().setBackgroundResource(R.drawable.tasks_setlosstab);
					TopManager.getInstance().getBtnDeflossPhoto().setBackgroundResource(R.drawable.tasks_phototab_click);
					TopManager.getInstance().getBtnDeflossProcess().setBackgroundResource(R.drawable.tasks_processestab);

					break;
				case ConstantValue.DAOHANG_SurveyWordFlow:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("查勘流程查询");
					TopManager.getInstance().getBtnSurveyBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnSurveyMsg().setBackgroundResource(R.drawable.tasks_chakantab);
					TopManager.getInstance().getBtnSurveyPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnSurveyProcess().setBackgroundResource(R.drawable.tasks_processestab_click);

					break;
				case ConstantValue.DAOHANG_DeflossWordFlow:
					TopManager.getInstance().setHeadTitle(SystemConfig.PHOTO_CLAIMNO, 16, Typeface.defaultFromStyle(Typeface.BOLD));
					// TopManager.getInstance().setHeadTitle("定损流程查询");
					TopManager.getInstance().getBtnDeflossBasicMsg().setBackgroundResource(R.drawable.tasks_infortab);
					TopManager.getInstance().getBtnDeflossMsg().setBackgroundResource(R.drawable.tasks_setlosstab);
					TopManager.getInstance().getBtnDeflossPhoto().setBackgroundResource(R.drawable.tasks_phototab);
					TopManager.getInstance().getBtnDeflossProcess().setBackgroundResource(R.drawable.tasks_processestab_click);
					break;
				case ConstantValue.Page_Title_Upload:
					TopManager.getInstance().setHeadTitle("上传队列", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_More:
					TopManager.getInstance().setHeadTitle("更多", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_Defloss_Task:
					TopManager.getInstance().setHeadTitle("定损任务列表", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_Survey_Task:
					TopManager.getInstance().setHeadTitle("查勘任务列表", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_About:
					TopManager.getInstance().setHeadTitle("关于", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_AboutInfo:
					TopManager.getInstance().setHeadTitle("状态信息", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_DriverInfo:
					TopManager.getInstance().setHeadTitle("驾驶人信息", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_Guaranteeslip:
					TopManager.getInstance().setHeadTitle("保单信息", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_History:
					TopManager.getInstance().setHeadTitle("历史赔案信息", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_MainPoint:
					TopManager.getInstance().setHeadTitle("查勘要点", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_ObjectCar:
					TopManager.getInstance().setHeadTitle("标的车", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_ThreeCar:
					TopManager.getInstance().setHeadTitle("三者车", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_Signa:
					TopManager.getInstance().setHeadTitle("客户签名", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
				case ConstantValue.Page_Title_QueryHistory:
					TopManager.getInstance().setHeadTitle("历史任务查询", 22, Typeface.defaultFromStyle(Typeface.NORMAL));
					break;
					
				}
			}
		}

	}
}
