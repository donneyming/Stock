package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static String getDate(long time) {
		Date date = new Date(time*1000);
		String result = getTime2(date);
		return result;
	}

	public static String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}

	public static String getTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
		String dateStr = df.format(date);// new Date()为获取当前系统时间
		return dateStr;
	}

	public static String getTime2(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
		String dateStr = df.format(date);// new Date()为获取当前系统时间
		return dateStr;
	}

	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmm");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}

	public static String getDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		return date;
	}

	public static String getWeekEnd(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
}
