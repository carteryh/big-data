package cn.itcast.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.itcast.entity.DefaultConf;
import cn.itcast.entity.VarDesc;

public class DefaultConfUtil {
	private static DefaultConfUtil defaultConfUtil;
	private  DefaultConf defaultconf =new DefaultConf();
	
	public DefaultConfUtil() {
		DateFormat dateFormat = new DateFormat();
		Properties p = new Properties();
		InputStream isStream = DefaultConfUtil.class.getResourceAsStream("/defaultConf.properties");
		try {
			p.load(isStream);
			if(!VarDesc.NOVALUE.equals(p.getProperty("endDate.nwquality.dayTime")))
			defaultconf.setEndDateNwquality(p.getProperty("endDate.nwquality.dayTime"));
			else{
			defaultconf.setEndDateNwquality(String.valueOf(dateFormat.getMonthAgo_start()*100));	
			}
			if(!VarDesc.NOVALUE.equals(p.getProperty("intervalDay.nwquality"))){
				defaultconf.setIntervalDayNwquality(Integer.valueOf(p.getProperty("intervalDay.nwquality")));
			}else{
				defaultconf.setIntervalDayNwquality(7);
			}
			
			if(!VarDesc.NOVALUE.equals(p.getProperty("endDate.appTraffic.start_time"))){
				defaultconf.setEndDateAppTraffic((p.getProperty("endDate.appTraffic.start_time")));
			}else{
				defaultconf.setEndDateAppTraffic(String.valueOf(dateFormat.getMonthAgo_start()*100));	
			}
			if(!VarDesc.NOVALUE.equals(p.getProperty("intervalDay.appTraffic"))){
				defaultconf.setIntervalDayAppTraffic(Integer.valueOf(p.getProperty("intervalDay.appTraffic")));
			}else{
				defaultconf.setIntervalDayAppTraffic(7);
			}
			
			
			if(!VarDesc.NOVALUE.equals(p.getProperty("endDate.signalStrength.time_index"))){
				defaultconf.setEndDateSignalStrength((p.getProperty("endDate.signalStrength.time_index")));
			}else{
				defaultconf.setEndDateSignalStrength(String.valueOf(dateFormat.getMonthAgo_start()*100));	
			}
			//System.out.println(VarDesc.NOVALUE.equals(p.getProperty("intervalDay.signalStrength")));
			if(!VarDesc.NOVALUE.equals(p.getProperty("intervalDay.signalStrength"))){
				defaultconf.setIntervalDaySignalStrength(Integer.valueOf(p.getProperty("intervalDay.signalStrength")));
			}else{
				defaultconf.setIntervalDaySignalStrength(7);
				
			}
			
			
			
			
			if(!VarDesc.NOVALUE.equals(p.getProperty("endDate.dataConnection.startDate"))){
				defaultconf.setEndDateDateConnection((p.getProperty("endDate.dataConnection.startDate")));
			}else{
				defaultconf.setEndDateDateConnection(String.valueOf(dateFormat.getMonthAgo_start()*100));	
			}
			//System.out.println(VarDesc.NOVALUE.equals(p.getProperty("intervalDay.signalStrength")));
			if(!VarDesc.NOVALUE.equals(p.getProperty("intervalDay.dataConnection"))){
				defaultconf.setIntervalDayDateConnection(Integer.valueOf(p.getProperty("intervalDay.dataConnection")));
			}else{
				defaultconf.setIntervalDayDateConnection(7);
				
			}
			
			
	
		} catch (IOException e) {
			System.err.println("错误：没有读取配置文件，请确定defaultConf.properties文件是否存在");
			e.printStackTrace();
		} finally {
			try {
				if (isStream != null)
					isStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	public synchronized static DefaultConfUtil newInstance() {
		if (defaultConfUtil == null)
			return new DefaultConfUtil();
		return defaultConfUtil;
	}
	public static DefaultConfUtil getDefaultConfUtil() {
		return defaultConfUtil;
	}
	public static void setDefaultConfUtil(DefaultConfUtil defaultConfUtil) {
		DefaultConfUtil.defaultConfUtil = defaultConfUtil;
	}
	public DefaultConf getDefaultconf() {
		return defaultconf;
	}
	public void setDefaultconf(DefaultConf defaultconf) {
		this.defaultconf = defaultconf;
	}
	
public static void main(String[] args) {
	DateFormat dateFormat = new DateFormat();
	System.out.println(dateFormat.getMonthAgo_start());
	
}
	
}
