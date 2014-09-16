package com.sinosoftyingda.fastclaim.common.views;

import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 自定义秒表
 * @author JingTuo
 *
 */
public class Stopwatch implements Runnable{
	
	/**
	 * 顺时针
	 */
	public static final int TYPE_CLOCKWISE = 0;
	
	/**
	 * 逆时针
	 */
	public static final int TYPE_ANTICLOCKWISE = 1;
	
	private int type;
	private long duration;
	private int count;
	private Listener listener;	
	private TextView tv;
	
	/**
	 * 
	 * @param duration 单位为毫秒
	 * @param count
	 */
	public Stopwatch(TextView tv, int type, long duration, int count, Listener listener){
		this.tv = tv;
		this.type = type;
		this.duration = duration;
		this.count = count;
		this.listener = listener;
		if(type==TYPE_CLOCKWISE){
			tv.setText(DateTimeUtils.format(0));
		}else if(type==TYPE_ANTICLOCKWISE){
			tv.setText(DateTimeUtils.format((int)(duration/1000)));
		}else{
			return;
		}

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long start = 0;
		long current = 0;
		long end = 0;
		long es = 0;
		long ec = 0;
		for (int i = 0; i < count; i++) {
			start = System.currentTimeMillis();
			current = start;
			while(true){
				end = System.currentTimeMillis();
				es = end - start;
				ec = end - current;
				if(ec<1000l&&ec>=0l){
					continue;
				}else if(ec==1000l){
					Time time = new Time();
					time.setWait(es);
					Message msg = new Message();
					msg.what = type;
					msg.obj = time;
					handler.sendMessage(msg);
				}else{//不应该出现的情况
					break;
				}
				if(es==duration){
					if (listener != null) {
						listener.run();
					}
					break;
				}else if(es<duration){
					continue;
				}else{//不应该出现的情况
					break;
				}
			}
			
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			Time wait = (Time)msg.obj;
			if(msg.what==TYPE_CLOCKWISE){
				tv.setText(DateTimeUtils.format((int)((wait.getWait())/1000)));
			}else{
				tv.setText(DateTimeUtils.format((int)((duration-wait.getWait())/1000)));
			}
		};
	};
	
	private class Time{
		
		private long wait;

		public long getWait() {
			return wait;
		}

		public void setWait(long wait) {
			this.wait = wait;
		} 
	}
	
	/**
	 * 时间到达监听
	 * @author JingTuo
	 *
	 */
	public interface Listener{
		/**
		 * 当时间到达时要做的事情
		 */
		public void run();
	}
}
