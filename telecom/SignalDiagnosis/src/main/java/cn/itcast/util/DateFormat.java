package cn.itcast.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.itcast.entity.DefaultConf;
import cn.itcast.entity.NWQuality;

public class DateFormat {
	private Calendar cal;
	private SimpleDateFormat sf;
	private SimpleDateFormat sf1;
	private SimpleDateFormat sf2;
	private SimpleDateFormat sf3;
	private SimpleDateFormat sf4;

	private String[] str;
	private String dateTime;

	public DateFormat() {
		sf = new SimpleDateFormat("yyyyMMdd");
		sf1 = new SimpleDateFormat("MM/dd/yyyy");
		sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		sf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf4 = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前日期
		cal = Calendar.getInstance();
	}

	// 分割字符串
	public DateFormat(String s) {
		sf = new SimpleDateFormat("yyyyMMdd");
		if (s.contains(" - ")) {
			str = s.split(" - ");
		} else {
			this.dateTime = s;
		}
	}

	// 获取当前时间的之前一个月的起始日期
	public int getMonthAgo_start() {
		// 一个月前的日期
		cal.add(Calendar.MONTH, -1);
		return Integer.parseInt(sf.format(cal.getTime()));
	}

	// 获取当前时间
	public int getMonthAgo_end() {
		//System.out.print(Integer.parseInt(sf.format(cal.getTime())));
		return Integer.parseInt(sf.format(cal.getTime()));
	}

	// 分割时间：开始时间
	@SuppressWarnings("deprecation")
	public int getStartDate() {
		if (dateTime != null) {
			return Integer.parseInt(sf.format(new Date(dateTime)));
		}
		return Integer.parseInt(sf.format(new Date(str[0])));
	}

	// 分割时间：结束时间
	@SuppressWarnings("deprecation")
	public int getEndDate() {
		if (dateTime != null) {
			return Integer.parseInt(sf.format(new Date(dateTime)));
		}
		return Integer.parseInt(sf.format(new Date(str[1])));
	}

	// 显示标准格式的时间@@@@@@@@@@@
	public String showDate(int startDate, int endDate) {
		String start = null;
		String end = null;
		try {
			start = sf1.format(sf.parse("" + startDate));
			end = sf1.format(sf.parse("" + endDate));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return start + " - " + end;
	}
	// 显示标准格式的时间hxj
	public String showDates(int startDate, int endDate) {
		String start = null;
		String end = null;
		
		try {
			end = sf1.format(sf.parse("" + endDate));
			start = sf1.format(sf.parse("" + startDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  start+ " - " +end ;
	}

	public int getDateToSecond_start() {
		// 一个月前的日期
		cal.add(Calendar.MONTH, -1);
		return Integer.valueOf(String.valueOf(
				Long.parseLong(sf2.format(cal.getTime()))).substring(0, 10));
	}

	public int getDateToSecond_end() {
		return Integer.valueOf(String.valueOf(
				Long.parseLong(sf2.format(cal.getTime()))).substring(0, 10));
	}

	// 显示标准格式的时间
	public String showDateToSecond(long d) {
		d = d * 10000;
		String dateTime = null;
		try {
			dateTime = sf3.format(sf2.parse("" + d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	// 显示标准格式的时间
	public int convertDateToSecondInt(String d) {
		String dateTime = null;
		try {
			dateTime = sf2.format(sf3.parse("" + d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Integer.valueOf(String.valueOf(dateTime).substring(0, 10));
	}

	// 计算时间段内的天数
	public int dayCount(int startDate,int endDate) {
		long between_days = 0l;
		try {
			cal.setTime(sf.parse(""+startDate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sf.parse(""+endDate));
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1)/(1000*3600*24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	public String getStartDateByDefaultConf(String endDate,int intervalDay){

		try {
			Date date=sf2.parse(endDate+"0000");
			cal.setTime(date); 
			cal.set(Calendar.DATE,cal.get(Calendar.DATE)-intervalDay); 

			return sf2.format(cal.getTime()).substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void dateForAllData(int startDate,int endDate){
		//起始日期
		//截止日期
		
		if(0 == endDate){
			endDate = getMonthAgo_end();
		}
		
		System.out.println(startDate);
		System.out.println(endDate);
		
	}
	
	
	
	
	public static void main(String[] args) {
		/*如果是nwquality这张表就调用defaultConf.getEndDateNwquality()
		和defaultConf.getIntervalDayNwquality()
		反之要调用对应的表的对应函数*/
		
		//时间精确到天
		DateFormat dateFormat = new DateFormat();
		NWQuality  nwQuality =new NWQuality();
		DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
		nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
		nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
		nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
		System.out.println("精确到天----------start--"+nwQuality.getStartDate()+"end--"+nwQuality.getEndDate()+"daytime--"+nwQuality.getDaytime());

		//时间精确到小时
		DateFormat dateFormat2 = new DateFormat();
		DefaultConf defaultConf2=DefaultConfUtil.newInstance().getDefaultconf();
		nwQuality.setEndDate(Integer.valueOf(defaultConf2.getEndDateNwquality()));
		nwQuality.setStartDate(Integer.valueOf(dateFormat2.getStartDateByDefaultConf(defaultConf2.getEndDateNwquality(), defaultConf2.getIntervalDayNwquality())));
		
		System.out.println("精确到小时——————————————start--"+nwQuality.getStartDate()+"end--"+nwQuality.getEndDate());

		new DateFormat().dateForAllData(20130101,0);
		
	}
	
	
	
	

}
