package com.sinosoftyingda.fastclaim.maintask.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;

/***
 * 拨打电话
 * @author jianfan
 * 
 */
public class MainTaskUtils{

	/** 第一次呼出时间 */ 
	public static String firstCallTime = "";
	/** 最后一次呼出时间 */
	public static String lastCallTime = "";
	
	public static void CallPhone(final Context context, final String phone, final String registNo) {
		PromptManager promptManager = new PromptManager();
		promptManager.showDialog(context, "确定要拨打电话吗？", R.string.is_positive, R.string.is_negative, new ShowDialogPositiveButton() {
			@Override
			public void setPositiveButton() {
				Uri uri = Uri.parse("tel:" + phone);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				context.startActivity(intent);
				// 保存报案号
				SystemConfig.registNoCallphone = registNo;
				
				// 获取最后一次的通话时间
				CallPhoneUtil callPhoneUtil = new CallPhoneUtil(context);
				firstCallTime = callPhoneUtil.getLastCallphoneDate();
			}

			@Override
			public void setNegativeButton() {

			}
		});
	}

}
