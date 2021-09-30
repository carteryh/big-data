package cn.itcast.dao;

import java.util.List;

import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.User;

public interface AppTrafficDao {
	// App流量排名
	public List<AppTraffic> getAppRateRank(AppTraffic appTraffic, User user);

	/**
	 *  热门手机App统计直方图
 	 */
	public List<AppTraffic> hotAppTrafficStatistics(AppTraffic appTraffic,
			User user);

	/**
	 * 手机OS流量统计直方图
 	 */
	public List<AppTraffic> getHotTelOsStatistics(AppTraffic appTrafficBean,
			User user);

	/**
	 *  手机OS流量饼图中的总的部分
 	 */
	public long getHotTelOsStatistics_pie(AppTraffic appTraffic, User user);

	/**
	 * 热门手机App饼图中的总和部分
 	 */
	public long hotAppTrafficStatistics_pie(AppTraffic appTraffic, User user);

	/**
	 * 热门App流量跟踪图
 	 */
	public List<AppTraffic> getAppTrafficTracks(AppTraffic appTraffic, User user);

	/*
	 * 热门app流量分布图 
 	 */
	// 前10app
	public List<AppTraffic> gettop10app(AppTraffic appTraffic);

	// 前10手机
	public List<AppTraffic> gettop5mobile(AppTraffic appTraffic);

	// app使用分布
	public List<AppTraffic> getAppFlowDistribution(double smalllng,
			double biglng, double smalllat, double boglat, AppTraffic appTraffic);

	// 手机使用分布
	public List<AppTraffic> getMobileFlowDistribution(double smalllng,
			double biglng, double smalllat, double boglat, AppTraffic appTraffic);

	// 热门app分布图
	public List<AppTraffic> getheatApp(User user, AppTraffic appTraffic);

	// 热门mobile分布图
	public List<AppTraffic> getheatMobile(User user, AppTraffic appTraffic);
	
	// 用户附近mobile分布图
	public List<AppTraffic> getUserNearapp(User user, AppTraffic appTraffic);

	// 用户附近OS分布图
	public List<AppTraffic> getUserNearOS(User user, AppTraffic appTraffic);

	// 热门os分布图
	public List<AppTraffic> getheatOS(User user, AppTraffic appTraffic);

	// 所有APP流量总和
	public long getAppRAteSum(AppTraffic appTraffic, User user);

	// App流量排名(all)
	public List<AppTraffic> getAppRateRank_all(AppTraffic appTraffic, User user);

	// 热门手机流量排名(各网络制式)
	public List<AppTraffic> getHotTelRank_networkType(AppTraffic appTraffic,
			User user);

	// 热门手机流量排名(网络制式：ALL)
	public List<AppTraffic> getHotTelRank(AppTraffic appTraffic, User user);
	// 热门手机流量排名饼图
	public List<AppTraffic> getHotTelRanktoppage(AppTraffic appTraffic, User user);

	//典型地标热门App流量排名
	public List<AppTraffic> getLandmarkAppTrafficRank(AppTraffic appTraffic,User user);

	//不知道要干啥的街景图
	public List<AppTraffic> getAppStreetScape(User user, AppTraffic appTraffic);
}
