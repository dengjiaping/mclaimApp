package com.sinosoftyingda.fastclaim.common.ui.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.ui.wheel.NumericWheelAdapter;
import com.sinosoftyingda.fastclaim.common.ui.wheel.OnWheelChangedListener;
import com.sinosoftyingda.fastclaim.common.ui.wheel.WheelView;


/**
 * 时间选择类
 * @author haoyun
 */
public class SetDateTimeUsils {
	private static final int START_YEAR = 1990;
	private static final int END_YEAR = 2100;
	private Dialog gTimeDialog;
	private  TimePicker picker;
	private Context context;

	public SetDateTimeUsils(Context context) {
		this.context = context;
	}

	public void showDateTimePicker(final EditText et1) {
		Calendar calendar = Calendar.getInstance();
		int year;
		int month;
		int day;
		int hour;
		int minute;
		
		
		
		if (et1.getText().toString().equals("")) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DATE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
		} else {
			String[] s = et1.getText().toString().split("-");
			year = Integer.parseInt(s[0].trim());
			month = Integer.parseInt(s[1].trim()) - 1;
			day = Integer.parseInt(s[2].trim().substring(0, 2));
			
			String [] timesString=et1.getText().toString().substring(11,et1.getText().toString().length()).split(":");
			hour=Integer.parseInt(timesString[0]);
			minute=Integer.parseInt(timesString[1]);
		}
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		gTimeDialog = new Dialog(context, R.style.Dialog);
		gTimeDialog.setTitle("请选择日期与时间");
		// 找到dialog的布局文件
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_time_layout, null);
		

		// 年
		final WheelView wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		final WheelView wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");

		wv_month.setCurrentItem(month);

		// 日
		final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据

		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}

		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);
		

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		/**
		 * 时间
		 */
		picker = (TimePicker) view.findViewById(R.id.time);
		picker.setIs24HourView(true);
		picker.setCurrentHour(hour);
		picker.setCurrentMinute(minute);
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;
		textSize = 25;

		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
		Button btn_clear = (Button)view.findViewById(R.id.btn_datetime_clear);
		// 确定
		btn_sure.setOnClickListener(new TimeOnClick(et1, wv_year, wv_month, wv_day,picker));
		
		// 取消
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gTimeDialog.dismiss();
			}
		});
		btn_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				gTimeDialog.dismiss();
				et1.setText("");
			}
		});
		// 设置dialog的布局,并显示
		gTimeDialog.setContentView(view);
		gTimeDialog.show();
	}

	class TimeOnClick implements OnClickListener {
		private EditText gEt;
		private WheelView wv_year, wv_month, wv_day;
		private TimePicker gtime;

		public TimeOnClick(EditText et, WheelView wv_year, WheelView wv_month, WheelView wv_day,TimePicker time) {
			this.wv_year = wv_year;
			this.wv_month = wv_month;
			this.wv_day = wv_day;
			gEt = et;
			gtime=time;
		}

		@Override
		public void onClick(View v) {
			// 如果是个数,则显示为"02"的样式
			String parten = "00";
			DecimalFormat decimal = new DecimalFormat(parten);
			// 设置日期的显示
			String hour=gtime.getCurrentHour()+"";
			String minute=gtime.getCurrentMinute()+"";
			gEt.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
					+ decimal.format((wv_month.getCurrentItem() + 1)) + "-"
					+ decimal.format((wv_day.getCurrentItem() + 1)) + " "+(hour.length()==1?"0"+hour:hour)+":"+(minute.length()==1?"0"+minute:minute)+":00");

			gTimeDialog.dismiss();
		}

	}
}
