package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TradeTime {
	// 获取星期几
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	// 计算当前日期
	public static Date cmptDateNow() {
		Date time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		try {
			time = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new Date()为获取当前系统时间
		return time;
	}

	// 计算卖到二级市场的时间T，T+3
	public static Date cmptTradetoMarket(Date buyDate) {
		Date time = null;
		Calendar tradeTime = Calendar.getInstance();
		tradeTime.setTime(buyDate);
        String weekday = getWeekOfDate(buyDate);
        if( weekday.equals("星期一"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,3);//日期加3天
        }
        if( weekday.equals("星期二"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,3);//日期加3天
        }
        if( weekday.equals("星期三"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,5);//日期加5天
        }
        if( weekday.equals("星期四"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,5);//日期加5天
        }
        if( weekday.equals("星期五"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,5);//日期加5天
        }
        time =tradeTime.getTime();
		return time;
	}

	// 计算卖到场内的时间 T，T+4
	public static Date cmptTradetoVenue(Date buyDate) {
		Date time = null;
		Calendar tradeTime = Calendar.getInstance();
		tradeTime.setTime(buyDate);
        String weekday = getWeekOfDate(buyDate);
        if( weekday.equals("星期一"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,4);//日期加4天
        }
        if( weekday.equals("星期二"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,6);//日期加6天
        }
        if( weekday.equals("星期三"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,6);//日期加6天
        }
        if( weekday.equals("星期四"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,6);//日期加6天
        }
        if( weekday.equals("星期五"))
        {	
        	tradeTime.add(Calendar.DAY_OF_YEAR,6);//日期加6天
        }
        time = tradeTime.getTime();
		return time;
	}

}
