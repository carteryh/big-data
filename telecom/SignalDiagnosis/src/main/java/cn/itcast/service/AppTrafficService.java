package cn.itcast.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.AppTrafficDao;
import cn.itcast.echarts.EchartsBar;
import cn.itcast.echarts.EchartsLine;
import cn.itcast.echarts.EchartsPie;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.PieObject;
import cn.itcast.entity.User;
import cn.itcast.util.DateFormat;
import org.springframework.stereotype.Component;

import cn.itcast.entity.VarDesc;

@Component("appTrafficService")
public class AppTrafficService {
	private AppTrafficDao appTrafficDao;
	private EchartsBar echartsBar;
	private EchartsPie echartsPie;
	private EchartsLine echartsLine;

	// App流量排名（直方图）
	
	public String getAppRateRank_historgram(AppTraffic appTraffic, User user) {
		List<AppTraffic> appTraffics = appTrafficDao.getAppRateRank(appTraffic, user);
		String optionStr = echartsBar.AppRateRank(appTraffics);
		return optionStr;
	}
	//App流量排名（饼图）
	public String getAppRateRank_pie(AppTraffic appTraffic, User user) {
		//前台显示类型
		String showType = appTraffic.getShowType();
		//所有app流量的总和
		long sum = appTrafficDao.getAppRAteSum(appTraffic, user);
		//显示每个App的流量的总和
		List<AppTraffic> appTraffics = appTrafficDao.getAppRateRank_all(appTraffic, user);
		List<Object> appPies = new ArrayList<Object>();
		List<Object> app_names = new ArrayList<Object>();
		double other = 100;
		DecimalFormat df=new DecimalFormat("######0.00");
		for (int i = 0; i < appTraffics.size(); i++) {
			PieObject pieObject = new PieObject();
			pieObject.setValue(Double.parseDouble(df.format(((double)appTraffics.get(i).getSpeed()/sum)*100)));
			pieObject.setName(appTraffics.get(i).getApp_name());
			appPies.add(pieObject);
			app_names.add(appTraffics.get(i).getApp_name());
			other = other - (int)pieObject.getValue();
		}
		PieObject pieObject2 = new PieObject();
		pieObject2.setValue(other);
		pieObject2.setName("其他");
		appPies.add(pieObject2);
		app_names.add("其他");
		String optionStr = echartsPie.AppRateRankPie(appPies, app_names,showType);
		return optionStr;
	}
	// 热门手机流量排名(直方图)
	public String getHotTelRank_historgram(AppTraffic appTraffic,User user){
		DateFormat dateFormat = new DateFormat();
		int dayCount = dateFormat.dayCount(appTraffic.getStartDate(), appTraffic.getEndDate());
		List<AppTraffic> appTraffics = appTrafficDao.getHotTelRank_networkType(appTraffic, user);
		String optionStr = echartsBar.terminalStatistics(appTraffics, dayCount);
		return optionStr;
	}
	
	//首页手机饼图
	// 热门手机流量排名(饼图)
	public String getHotTelRank_pietoppage(AppTraffic appTraffic,User user){
		//手机流量总和
		List<AppTraffic> appTraffics = appTrafficDao.getHotTelRanktoppage(appTraffic, user);
		DateFormat dateFormat = new DateFormat();
		int dayCount = dateFormat.dayCount(appTraffic.getStartDate(), appTraffic.getEndDate());
		long sum = 0l;
		for (AppTraffic app : appTraffics) {
			sum = sum + app.getSpeed();
		}
		long daySum = sum / dayCount;
		//每个手机类型流量总和
		List<Object> tel_name = new ArrayList<Object>();
		List<Object> telPies = new ArrayList<Object>();
		double other = 100;
		DecimalFormat df=new DecimalFormat("######0.00");
		int flag = 1;
		for (AppTraffic app : appTraffics) {
			PieObject pieObject = new PieObject();
			pieObject.setValue(Double.parseDouble(df.format((((double)app.getSpeed()/dayCount)/daySum)*100)));
			pieObject.setName(app.getCompanyModel());
			telPies.add(pieObject);
			tel_name.add(app.getCompanyModel());
			other = other - (int)pieObject.getValue();
			if (flag >= 5 ) {
				break;
			}
			flag++;
		}
		PieObject pieObject2 = new PieObject();
		if (other < 0) {
			other = 0;
		}
		pieObject2.setValue(other);
		pieObject2.setName("其他");
		telPies.add(pieObject2);
		tel_name.add("其他");
		String optionStr = echartsPie.HotTelRank(telPies, tel_name, appTraffic.getShowType());
		return optionStr;
	}
	
	// 热门手机流量排名(饼图)
	public String getHotTelRank_pie(AppTraffic appTraffic,User user){
		//手机流量总和
		List<AppTraffic> appTraffics = appTrafficDao.getHotTelRank_networkType(appTraffic, user);
		DateFormat dateFormat = new DateFormat();
		int dayCount = dateFormat.dayCount(appTraffic.getStartDate(), appTraffic.getEndDate());
		long sum = 0l;
		for (AppTraffic app : appTraffics) {
			sum = sum + app.getSpeed();
		}
		long daySum = sum / dayCount;
		//每个手机类型流量总和
		List<Object> tel_name = new ArrayList<Object>();
		List<Object> telPies = new ArrayList<Object>();
		double other = 100;
		DecimalFormat df=new DecimalFormat("######0.00");
		int flag = 1;
		for (AppTraffic app : appTraffics) {
			PieObject pieObject = new PieObject();
			pieObject.setValue(Double.parseDouble(df.format((((double)app.getSpeed()/dayCount)/daySum)*100)));
			pieObject.setName(app.getCompanyModel());
			telPies.add(pieObject);
			tel_name.add(app.getCompanyModel());
			other = other - (int)pieObject.getValue();
			if (flag >= 5 ) {
				break;
			}
			flag++;
		}
		PieObject pieObject2 = new PieObject();
		if (other < 0) {
			other = 0;
		}
		pieObject2.setValue(other);
		pieObject2.setName("其他");
		telPies.add(pieObject2);
		tel_name.add("其他");
		String optionStr = echartsPie.HotTelRank(telPies, tel_name, appTraffic.getShowType());
		return optionStr;
	}
	//典型地标热门App流量排名
	public String getLandmarkAppTrafficRank(AppTraffic appTraffic,User user){
 
		List<AppTraffic> appTraffics = appTrafficDao.getLandmarkAppTrafficRank(appTraffic, user);
		if (appTraffics.size() != 0) {
			String optionStr = echartsBar.landmarkAppTrafficRank(appTraffics);
			System.out.println("5.5:  "+optionStr);
			return optionStr;			
		}
		String optionStr = echartsBar.landmarkAppTrafficRankNull(appTraffics);
		return optionStr;	
 
	}
	
	
	/**
	 *热门手机App统计直方图
	 */
	public String hotAppTrafficStatistics(AppTraffic appTraffic, User user) {
		//System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
		/*********************************参数测试*********************************/
		if(appTrafficDao.hotAppTrafficStatistics(appTraffic,user)!=null){
			List<AppTraffic> appTraffics = appTrafficDao.hotAppTrafficStatistics(appTraffic,user);
			//System.out.println(appTraffics.size()+"SSSSSSSSSSSSS");
			//System.out.println(appTraffic.getSpeed_type()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS!!!!!!!!!!!!!!!!!!!!!");
			String optionStr = echartsBar.hotAppTrafficStatistics(appTraffics);
			return optionStr;
		}else{
			return VarDesc.NODATA;
		}
	
	}
	
	
	/**
	 *热门手机App流量饼图
	 */
	public String hotAppTrafficStatistics_pie(AppTraffic appTraffic, User user) {
		if(appTrafficDao.hotAppTrafficStatistics_pie(appTraffic,user)!=0){
			List<AppTraffic> appTraffics  = (List<AppTraffic>) appTrafficDao.hotAppTrafficStatistics(appTraffic,user);
			long allSpeed=appTrafficDao.hotAppTrafficStatistics_pie(appTraffic,user);
			//System.out.println(allSpeed+"SSSSSSSSSSSSSSSSSSSSSSSSSs");
			String pieStr = echartsPie.hotAppTrafficStatistics_pie(appTraffics,allSpeed);
			return pieStr;
		}else{
			return VarDesc.NODATA;
		}
	
	}
	
	
	
	/**
	 *手机OS流量统计直方图
	 */
	public String getHotTelOsStatistics(AppTraffic appTraffic, User user) {
		//System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		if(appTrafficDao.getHotTelOsStatistics(appTraffic,user)!=null){
			List<AppTraffic> appTraffics  = (List<AppTraffic>) appTrafficDao.getHotTelOsStatistics(appTraffic,user);
			//System.out.println(appTraffics.size()+"<<<<<<<<<<<<<<<<<<SSSSSSSSSSSSS");
			String optionStr = echartsBar.hotTelOsStatistics(appTraffics);
			return optionStr;
		}else{
			return VarDesc.NODATA;
		}
	}
	
	/**
	 手机OS流量饼图
	 */
	public String getHotTelOsStatistics_pie(AppTraffic appTraffic, User user) {
		if(appTrafficDao.getHotTelOsStatistics_pie(appTraffic,user)!=0){
			List<AppTraffic> appTraffics  = (List<AppTraffic>) appTrafficDao.getHotTelOsStatistics(appTraffic,user);
			long allSpeed=appTrafficDao.getHotTelOsStatistics_pie(appTraffic,user);
			//System.out.println(allSpeed+"SSSSSSSSSSSSSSSSSSSSSSSSSs");
			String pieStr = echartsPie.hotTelOsStatistics_pie(appTraffics,allSpeed);
			return pieStr;
		}else{
			return VarDesc.NODATA;
		}
	
	}
	/**
	 *热门App流量跟踪图
	 */
	public String getAppTrafficTracks(AppTraffic appTraffic, User user) {
		/*********************************参数测试*********************************/
		if(appTrafficDao.getAppTrafficTracks(appTraffic,user)!=null){
			List<AppTraffic> signalStrengths = (List<AppTraffic>) appTrafficDao.getAppTrafficTracks(appTraffic,user);
			//System.out.println(signalStrengths.size()+"SSSSSSSSSSSSS-------------------------");
			String optionStr = echartsLine.getAppTrafficTracks(signalStrengths,appTraffic.getX_type());
			return optionStr;
		}else{
			return VarDesc.NODATA;
		}
	}
	
	
	
	
		//前10App
		public    List<AppTraffic> gettop10app(AppTraffic appTraffic) {
			
			return  appTrafficDao.gettop10app(appTraffic);	
		}
		//前10mobile
				public    List<AppTraffic> gettop5mobile(AppTraffic appTraffic) {
					
					return  appTrafficDao.gettop5mobile(appTraffic);	
				}
		//热门App流量分布图
		public  List<AppTraffic> getappdistribution(double smalllng,double biglng,double smalllat,double boglat,AppTraffic appTraffic) {
			 
			return  appTrafficDao.getAppFlowDistribution(smalllng,biglng,smalllat,boglat ,appTraffic);	
		}
	
	
	
		public List<AppTraffic> getmobiledistribution(double smalllng,double biglng,double smalllat,double boglat,AppTraffic appTraffic) {
			// TODO Auto-generated method stub
			return  appTrafficDao.getMobileFlowDistribution(smalllng,biglng,smalllat,boglat ,appTraffic);	
		}

	
		//获得热门app
		public List<AppTraffic> getheatApp(User user, AppTraffic appTraffic) {
			
			
			if (!"admin".equals(user.getRole())) {
				appTraffic.setRole(user.getNWOperator());
			}
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			
			
			
			
			

			if (appTraffic.getStart_time() == null) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime()))*100);

				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime()))*100);

				sf = new SimpleDateFormat("MM/dd/yyyy");
				appTraffic.setDaytime(sf.format(cal1.getTime()) + "-"
						+ sf.format(cal2.getTime()));
			} else {

				if (appTraffic.getDaytime().contains("-")) {

					// 通过筛选条件传参，分割daytime
					String[] str = appTraffic.getDaytime().split(" - ");
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+appTraffic.getDaytime());
					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
					appTraffic.setEndDate(Integer.parseInt(sf
							.format(new Date(str[1]))));

				} else {
					// 通过筛选条件传参，分割daytime

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(appTraffic.getDaytime()))));
					appTraffic.setEndDate(0);

				}

			}
			return appTrafficDao.getheatApp(user,appTraffic);
		}
		
		
		
		//获得热门Mobile
		public List<AppTraffic> getheatMobile(User user, AppTraffic appTraffic) {
			
			
			if (!"admin".equals(user.getRole())) {
				appTraffic.setRole(user.getNWOperator());
			}
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			if (appTraffic.getStart_time() == null) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime()))*100);

				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime()))*100);

				sf = new SimpleDateFormat("MM/dd/yyyy");
				appTraffic.setDaytime(sf.format(cal1.getTime()) + "-"
						+ sf.format(cal2.getTime()));
			} else {

				if (appTraffic.getDaytime().contains("-")) {

					// 通过筛选条件传参，分割daytime
					String[] str = appTraffic.getDaytime().split(" - ");

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
					appTraffic.setEndDate(Integer.parseInt(sf
							.format(new Date(str[1]))));

				} else {
					// 通过筛选条件传参，分割daytime

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(appTraffic.getDaytime()))));
					appTraffic.setEndDate(0);

				}

			}
			return appTrafficDao.getheatMobile(user,appTraffic);
		}
		
		
		//用户附近获得热门Mobile
		public List<AppTraffic> getUserNearapp(User user, AppTraffic appTraffic) {
			
			
			if (!"admin".equals(user.getRole())) {
				appTraffic.setRole(user.getNWOperator());
			}
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			if (appTraffic.getStart_time() == null) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime()))*100);

				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime()))*100);

				sf = new SimpleDateFormat("MM/dd/yyyy");
				appTraffic.setDaytime(sf.format(cal1.getTime()) + "-"
						+ sf.format(cal2.getTime()));
			} else {

				if (appTraffic.getDaytime().contains("-")) {

					// 通过筛选条件传参，分割daytime
					String[] str = appTraffic.getDaytime().split(" - ");

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
					appTraffic.setEndDate(Integer.parseInt(sf
							.format(new Date(str[1]))));

				} else {
					// 通过筛选条件传参，分割daytime

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(appTraffic.getDaytime()))));
					appTraffic.setEndDate(0);

				}

			}
			return appTrafficDao.getUserNearapp(user,appTraffic);
		}
		
		//用户附近获得热门Mobile
		public List<AppTraffic> getUserNearOS(User user, AppTraffic appTraffic) {
			
			
			if (!"admin".equals(user.getRole())) {
				appTraffic.setRole(user.getNWOperator());
			}
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			if (appTraffic.getStart_time() == null) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime()))*100);

				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime()))*100);

				sf = new SimpleDateFormat("MM/dd/yyyy");
				appTraffic.setDaytime(sf.format(cal1.getTime()) + "-"
						+ sf.format(cal2.getTime()));
			} else {

				if (appTraffic.getDaytime().contains("-")) {

					// 通过筛选条件传参，分割daytime
					String[] str = appTraffic.getDaytime().split(" - ");

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
					appTraffic.setEndDate(Integer.parseInt(sf
							.format(new Date(str[1]))));

				} else {
					// 通过筛选条件传参，分割daytime

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(appTraffic.getDaytime()))));
					appTraffic.setEndDate(0);

				}

			}
			return appTrafficDao.getUserNearOS(user,appTraffic);
		}
		
		
		
		
		//获得热门Mobile
		public List<AppTraffic> getheatOS(User user, AppTraffic appTraffic) {
			
			
			if (!"admin".equals(user.getRole())) {
				appTraffic.setRole(user.getNWOperator());
			}
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

			if (appTraffic.getStart_time() == null) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime()))*100);

				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime()))*100);

				sf = new SimpleDateFormat("MM/dd/yyyy");
				appTraffic.setDaytime(sf.format(cal1.getTime()) + "-"
						+ sf.format(cal2.getTime()));
			} else {

				if (appTraffic.getDaytime().contains("-")) {

					// 通过筛选条件传参，分割daytime
					String[] str = appTraffic.getDaytime().split(" - ");

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
					appTraffic.setEndDate(Integer.parseInt(sf
							.format(new Date(str[1]))));

				} else {
					// 通过筛选条件传参，分割daytime

					appTraffic.setStartDate(Integer.parseInt(sf.format(new Date(appTraffic.getDaytime()))));
					appTraffic.setEndDate(0);

				}

			}
			return appTrafficDao.getheatOS(user,appTraffic);
		}
		
		
		
	
		//不知道要干啥的街景图
		public List<AppTraffic> getAppStreetScape(User user, AppTraffic appTraffic) {
			
			if(appTraffic.getNetwork_name()=="" || appTraffic.getNetwork_name()==null ){
				appTraffic.setNetwork_name("CMCC");
			}
			
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


		
			
			String strStart_time=appTraffic.getStart_time();
			String strEnd_time=appTraffic.getEnd_time();
			
			System.out.println(strStart_time+"::::::"+strEnd_time);
			if (strStart_time=="" || strStart_time==null || strEnd_time==null || strEnd_time=="" ) {

				Calendar cal1 = Calendar.getInstance();

				// 一个月前的日期
				cal1.add(Calendar.MONTH, -84);

				//nwQuality.setStartDate(Integer.parseInt(sf.format(cal1.getTime())));
				appTraffic.setStartDate(Integer.parseInt(sf.format(cal1.getTime())));
				
				//appTraffic.setStart_time(sf.format(cal1.getTime()));
				// 获取当前日期
				Calendar cal2 = Calendar.getInstance();
				//appTraffic.setEnd_time(sf.format(cal2.getTime()));
				appTraffic.setEndDate(Integer.parseInt(sf.format(cal2.getTime())));

			} else{
				
				
				try {
					appTraffic.setStartDate(Integer.parseInt(sf.format(sf1.parse(strStart_time))));
					appTraffic.setEndDate(Integer.parseInt(sf.format(sf1.parse(strEnd_time))));
					System.out.println(sf.format(sf1.parse(strEnd_time)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			return appTrafficDao.getAppStreetScape(user,appTraffic);
		}
	
	
	
	
	/*
	 * 下面是get、set方法
	 */

	public AppTrafficDao getAppTrafficDao() {
		return appTrafficDao;
	}

	@Resource
	public void setAppTrafficDao(AppTrafficDao appTrafficDao) {
		this.appTrafficDao = appTrafficDao;
	}

	public EchartsBar getEchartsBar() {
		return echartsBar;
	}

	@Resource
	public void setEchartsBar(EchartsBar echartsBar) {
		this.echartsBar = echartsBar;
	}
	
	public EchartsPie getEchartsPie() {
		return echartsPie;
	}
	
	@Resource
	public void setEchartsPie(EchartsPie echartsPie) {
		this.echartsPie = echartsPie;
	}
	@Resource
	public void setEchartsLine(EchartsLine echartsLine) {
		this.echartsLine = echartsLine;
	}
}
