package com.sinosoftyingda.fastclaim.common.mina;


import com.sinosoftyingda.fastclaim.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * 
 * @author JingTuo
 *
 */
public class NoticeManager {
	
	private static NoticeManager noticeManager;
	
	private NotificationManager notificationManager;
	
	public static final String TAG_NEWTASK = "NEW TASK";
	public static final String TAG_NOREAD = "NO READ";
	public static final String TAG_NOCONTACT = "NO CONTACT";
	public static final String TAG_ORDER = "ORDER";
	public static final String TAG_DISPATCH = "DISPATCH";
	public static final String TAG_NETWORK = "NETWORK";
	public static final String TAG_RESULT = "RESULT";

	private Context context;
	
	private Notification notification;
	
	private NoticeManager(Context context){
		this.context = context;
		this.notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notification = new Notification();
		
		notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
//		notification.contentView.setTextViewText(R.id.notification_tv_title, title);
//		notification.contentView.setTextViewText(R.id.notification_tv_msg, msg);
		notification.contentView.setImageViewResource(R.id.notification_iv, R.drawable.mclaim_icon_small);
		
        notification.icon = R.drawable.mclaim_icon_small;
        notification.iconLevel = 1;

//        notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
        notification.number = 1;
        
        notification.ledARGB = 0xffBA0701;
        notification.ledOnMS = 0;
        notification.ledOffMS = 5*1000;
        
        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.vibrate = new long[]{0,100,200,300};

        notification.when = System.currentTimeMillis();
        
        /**
         * 一直存在于状态下拉的列表中  
         * Notification.FLAG_INSISTENT
         * Notification.FLAG_ONLY_ALERT_ONCE
         * Notification.FLAG_ONGOING_EVENT
         * Notification.FLAG_FOREGROUND_SERVICE
         * 点击状态下拉列表之后会删除该通知
         * Notification.FLAG_AUTO_CANCEL
         */
        notification.flags = Notification.FLAG_AUTO_CANCEL;
	}
	
	public synchronized static NoticeManager getInstance(Context context){
		if(noticeManager==null){
			noticeManager = new NoticeManager(context);
		}
		return noticeManager;
	}
	
	
	/**
	 * @param tag
	 * @param msg	提示信息
	 * @param resid		raw下的音频文件
	 * @param intent	意图，准备跳转到那个页面
	 */
	public void notice(String tag, String msg, int resid ,Intent intent){
		notificationManager.cancel(tag, 1);
		notification.contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView.setTextViewText(R.id.notification_tv_title, msg);
//		notification.contentView.setTextViewText(R.id.notification_tv_msg, "");
		if(resid!=-1){
			notification.sound =  Uri.parse("android.resource://" + context.getPackageName() + "/" + resid);;
		}
		notificationManager.notify(tag, 1, notification);	
	}
	
	/**
	 * 移除所有通知
	 */
	public void cancelAll(){
		notificationManager.cancelAll();
	}
}
