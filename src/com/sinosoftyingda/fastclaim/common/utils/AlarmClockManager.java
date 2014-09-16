package com.sinosoftyingda.fastclaim.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.mina.NoticeManager;
import com.sinosoftyingda.fastclaim.notice.page.NoticeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class AlarmClockManager {
	/**
	 * 管理
	 */
	private static AlarmClockManager manager;
	
	public static final int MAX_MINUTE = 9;
	
	public static final int INTERVAL_MINUETE = 3;
	
	private Context context;
		
	private static final byte[] lock = new byte[0];
	
	private Timer timer;
	
	public static final long PERIOD = 1*60*1000;
	
	public static final long DELAY = 1*60*1000;
	
	private List<Object> tasks;
	
	private Intent intent;
	
	private boolean start = false;
	
	private Task task;
	
	private AlarmClockManager(Context context){
		this.context = context;
		tasks = new ArrayList<Object>();
		timer = new Timer();
		intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, NoticeActivity.class);
	}
	public synchronized static AlarmClockManager getInstance(Context context){
		if(manager==null){
			manager = new AlarmClockManager(context);
		}
		return manager;
	}

	/**
	 * 时间每走一秒就修改视图的内容
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			synchronized (lock) {
				boolean isRead = false;
				boolean isOrder = false;//0表示无预约,1表示有预约，2表示预约
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < tasks.size(); i++) {
					Object task = tasks.get(i);
					if (task instanceof CheckTask) {
						CheckTask ct = (CheckTask)task;
						//判断闹钟
						int alarmtime = ct.getAlarmtime();
						if(ct.getIsread()==0){//尚未点击任务查看详细信息
							if(alarmtime<MAX_MINUTE*60){
								alarmtime += 60;
								ct.setAlarmtime(alarmtime);
								ct.getTvTime().setText(DateTimeUtils.format(alarmtime));
								ContentValues values = new ContentValues();
								values.put("alarmtime", alarmtime);
								DBUtils.update("checktask", values, "registno=?", new String[]{ct.getRegistno()});
								if(alarmtime%(INTERVAL_MINUETE*60)==0){//每隔三分钟的闹钟时间到达，发送通知
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_NOREAD, "有新任务尚未阅读", R.raw.noread, intent);
								}
								isRead = false;
							}else{//超出9分钟则不在提醒，视为已读
								isRead = true;
							}
						}else{
							isRead = true;
						}
						//预约时间
						if(ct.getOrdertime()!=null&&!ct.getOrdertime().equals("")){//添加了预约时间
							Date orderDate;
							try {
								orderDate = DateTimeUtils.parseStringToDate(ct.getOrdertime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
								long wait = orderDate.getTime() - System.currentTimeMillis();
								if(wait>=-30*1000&&wait<30*1000){
									isOrder = false;
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_ORDER, ct.getRegistno() + "预约时间到达", -1, intent);
								}else if(wait>=30*1000){
									isOrder = false;
								}else{
									isOrder = true;
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								isOrder = false;
							}
							
						}else{
							isOrder = false;
						}
						//未联系联系人
						if(ct.getIscontact()==0){
							try {
								Date start = DateTimeUtils.parseStringToDate(ct.getCreatetime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
								long wait = System.currentTimeMillis() - start.getTime();
								if(wait%(1*INTERVAL_MINUETE*60*1000)==0
										||wait%(2*INTERVAL_MINUETE*60*1000)==0
										||wait%(3*INTERVAL_MINUETE*60*1000)==0){//只提醒三次
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_NOCONTACT, "有任务尚未联系联系人", R.raw.nocontact, intent);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(isRead&&!isOrder&&ct.getIscontact()==1){
							list.add(task);
						}
					} else if(task instanceof CertainLossTask){
						CertainLossTask clt = (CertainLossTask)task;
						//判断闹钟
						int alarmtime = clt.getAlarmtime();
						if(clt.getIsread()==0){//尚未点击任务查看详细信息
							if(alarmtime<MAX_MINUTE*60){
								alarmtime+=60;
								clt.setAlarmtime(alarmtime);
								clt.getTvTime().setText(DateTimeUtils.format(alarmtime));
								ContentValues values = new ContentValues();
								values.put("alarmtime", alarmtime);
								DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[]{clt.getRegistno(), clt.getItemno()+""});
								if(alarmtime%(INTERVAL_MINUETE*60)==0){//每隔三分钟的闹钟时间到达，发送通知
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_NOREAD, "有新任务尚未阅读", R.raw.noread, intent);
								}
								isRead = false;
							}else{
								isRead = true;
							}
						}else{
							isRead = true;
						}
						//预约时间
						if(clt.getOrdertime()!=null&&!clt.getOrdertime().equals("")){//添加了预约时间
							try {
								Date orderDate = DateTimeUtils.parseStringToDate(clt.getOrdertime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
								long wait = orderDate.getTime() - System.currentTimeMillis();
								if(wait>=-30*1000&&wait<30*1000){
									isOrder = false;
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_ORDER, clt.getRegistno() + "预约时间到达", -1, intent);
								}else if(wait>=30*1000){
									isOrder = false;
								}else{
									isOrder = true;
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								isOrder = false;
							}
						}else{
							isOrder = false;
						}
						//未联系联系人
						if(clt.getIscontact()==0){
							try {
								Date start = DateTimeUtils.parseStringToDate(clt.getCreatetime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
								long wait = System.currentTimeMillis() - start.getTime();
								if(wait%(1*INTERVAL_MINUETE*60*1000)==0
										||wait%(2*INTERVAL_MINUETE*60*1000)==0
										||wait%(3*INTERVAL_MINUETE*60*1000)==0){//只提醒三次
									NoticeManager.getInstance(context).notice(NoticeManager.TAG_NOCONTACT, "有任务尚未联系联系人", R.raw.nocontact, intent);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(isRead&&!isOrder&&clt.getIscontact()==1){
							list.add(task);
						}
					}
				}
				//移除不需要提醒的任务
				remove(list);
				
				//如果没有需要修改的任务停止线程
				if(tasks.size()<=0){
					task.cancel();
					task = null;
					timer.purge();
					start = false;
				}
			}
		}
	};
	
	/**
	 * 销毁任务
	 * @throws Throwable
	 */
	public synchronized void destory() throws Throwable{
		if(task!=null){
			task.cancel();
		}
		if(timer!=null){
			timer.cancel();
		}
		finalize();
	}
	
	/**
	 * 添加任务
	 * @param task
	 */
	public synchronized void addTask(Object task){
		synchronized (lock) {
			if(!start){
				if(this.task==null){
					this.task = new Task();
				}
				start = true;
				timer.schedule(this.task, DELAY, PERIOD);
			}
			int index = -1;
			if (task instanceof CheckTask) {
				CheckTask newCt = (CheckTask) task;
				CheckTask oldCt = null;
				for (int i = 0; i < tasks.size(); i++) {
					Object oldObj = tasks.get(i);
					if (oldObj instanceof CheckTask) {
						oldCt = (CheckTask) oldObj;
						if (oldCt.getRegistno().equals(newCt.getRegistno())) {
//							oldCt.setIsread(newCt.getIsread());
//							oldCt.setIsaccept(newCt.getIscontact());
//							oldCt.setOrdertime(newCt.getOrdertime());
//							oldCt.setTvTime(newCt.getTvTime());//更新时间组件
							index = i;
							break;
						}
					}
				}
			} else if (task instanceof CertainLossTask) {
				CertainLossTask newClt = (CertainLossTask) task;
				CertainLossTask oldClt = null;
				for (int i = 0; i < tasks.size(); i++) {
					Object oldObj = tasks.get(i);
					if (oldObj instanceof CertainLossTask) {
						oldClt = (CertainLossTask) oldObj;
						if (oldClt.getRegistno().equals(newClt.getRegistno())
								&& oldClt.getItemno() == newClt.getItemno()) {
//							oldClt.setIsread(newClt.getIsread());
//							oldClt.setIsaccept(newClt.getIscontact());
//							oldClt.setOrdertime(newClt.getOrdertime());
//							oldClt.setTvTime(newClt.getTvTime());//更新时间组件
							index = i;
							break;
						}
					}
				}
			}
			if(index>=0&&index<tasks.size()){//如果已经存在需要更新任务信息，主要是时间组件
				tasks.set(index, task);
			}else{//否则直接添加任务
				tasks.add(task);
			}
		}
	}
	
	/**
	 * 移除不需要提醒的任务
	 * @param removes
	 */
	private synchronized void remove(List<Object> removes){
		for (int j = 0; j < removes.size(); j++) {
			Object removeObj = removes.get(j);
			if(removeObj instanceof CheckTask){//查勘任务
				for (int j2 = 0; j2 < tasks.size(); j2++) {
					Object exits = tasks.get(j2);
					if(exits instanceof CheckTask){
						if(((CheckTask) exits).getRegistno().equals(((CheckTask) removeObj).getRegistno())){
							tasks.remove(exits);
							break;
						}
					}
				}
			}else if(removeObj instanceof CertainLossTask){//定损任务
				for (int j2 = 0; j2 < tasks.size(); j2++) {
					Object exits = tasks.get(j2);
					if(exits instanceof CertainLossTask){
						if(((CertainLossTask) exits).getRegistno().equals(((CertainLossTask) removeObj).getRegistno())
								&&((CertainLossTask) exits).getItemno()==((CertainLossTask) removeObj).getItemno()){
							tasks.remove(exits);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 启动任务
	 */
	public synchronized void startTask(){
		if(!start){
			if(this.task==null){
				this.task = new Task();
			}
			timer.schedule(this.task, DELAY, PERIOD);
			start = true;
		}
	}
	
	/**
	 * 停止任务
	 */
	public synchronized void stopTask(){
		if(start){
			if(task!=null){
				task.cancel();
				task = null;
				timer.purge();
			}
			start = false;
		}
	}
	
	/**
	 * 移除不需要提醒的任务
	 * @param removes
	 */
	public synchronized void remove(Object task){
		if (task instanceof CheckTask) {// 查勘任务
			for (int j2 = 0; j2 < tasks.size(); j2++) {
				Object exits = tasks.get(j2);
				if (exits instanceof CheckTask) {
					if (((CheckTask) exits).getRegistno().equals(((CheckTask) task).getRegistno())) {
						tasks.remove(exits);
						break;
					}
				}
			}
		} else if (task instanceof CertainLossTask) {// 定损任务
			for (int j2 = 0; j2 < tasks.size(); j2++) {
				Object exits = tasks.get(j2);
				if (exits instanceof CertainLossTask) {
					if (((CertainLossTask) exits).getRegistno().equals(((CertainLossTask) task).getRegistno())
							&& ((CertainLossTask) exits).getItemno() == ((CertainLossTask) task).getItemno()) {
						tasks.remove(exits);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 自定义时间任务
	 * @author JingTuo
	 *
	 */
	class Task extends TimerTask{

		public Task(){
			
		}
		
		@Override
		public void run() {
			Message msg = new Message();
			handler.sendMessage(msg);
		}
		
	}
}
