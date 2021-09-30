package cn.itcast.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.itcast.entity.DefaultConf;
import cn.itcast.entity.NWQuality;
import cn.itcast.service.NWQualityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.DataConnection;
import cn.itcast.entity.TableCounts;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.service.AppTrafficService;
import cn.itcast.service.DataConnectionService;
import cn.itcast.util.CustomerContextHolder;
import cn.itcast.util.DateFormat;
import cn.itcast.util.DefaultConfUtil;
import cn.itcast.util.EhcacheUtil;
import cn.itcast.util.ReadFileCounts;

@Controller
public class NWQualityController {
	private static Logger logger = LoggerFactory.getLogger(NWQualityController.class);
	
	private NWQualityService nwQualityService;
	private DataConnectionService dataConnService;
	private AppTrafficService appTrafficService;
	private ReadFileCounts readFileCounts;
	
	private EhcacheUtil ehcacheUtil;
	
	//仪表盘
	@RequestMapping(value = "dashBoard")
	public ModelAndView getDashBoard(HttpSession session, NWQuality nwQuality, DataConnection dataConnection, AppTraffic appTraffic) {
		try{
			User user = (User) session.getAttribute("user");
			if (user instanceof User) {
				//默认数据源
				CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			
				//默认查询当前一个月数据
				if (nwQuality.getDaytime() == null) {
					DateFormat dateFormat = new DateFormat();
					DefaultConf defaultConf= DefaultConfUtil.newInstance().getDefaultconf();
					dataConnection.setEndDate(Integer.valueOf(defaultConf.getEndDateDateConnection().substring(0, 8)));
					dataConnection.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateDateConnection(), defaultConf.getIntervalDayDateConnection()).substring(0, 8)));
					appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
					appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
					nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
					nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
					nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
				}
				//连接点总数
				if(user.getRole().equals(VarDesc.ROLE.ADMIN))
					nwQuality.setNwOperator("ALL");
				else
					nwQuality.setNwOperator(user.getNWOperator());	
				
				
				
				//三个表总数
				//cache
				String tableCountsKey = "tableCounts"+user.toString();
				List<TableCounts> tableCountsVal;
				if(null == ehcacheUtil.getList(tableCountsKey)){
					tableCountsVal = nwQualityService.getTableCounts(user);
					ehcacheUtil.putList(tableCountsKey, tableCountsVal);
				}else{
					tableCountsVal = ehcacheUtil.getList(tableCountsKey);
				}
				//List<TableCounts> tableCounts = nwQualityService.getTableCounts(user);
				long nwQualityCount = 0;
				long signalStrengthCount = 0;
				long dataConnectionCount = 0;
				for (TableCounts tableCount : tableCountsVal) {
					// Network Quality总数
					nwQualityCount = tableCount.getNWQulity_count();
					// Signal Strength总数
					signalStrengthCount = tableCount.getSignal_Strength_count();
					// Data Connection总数
					dataConnectionCount = tableCount.getDataConnection_count();
					
				}
				//转换制定的数据源
				CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
	    		//热门App流量排名
				appTraffic.setNetwork_name(VarDesc.OPERATOR.ALL);
				appTraffic.setSpeed_type(VarDesc.SPEEDTYPE.ALL);
				//cache
				String appTrafficKey = "appTraffic"+appTraffic.toString()+user.toString();
				String appTrafficVal;
				if(null == ehcacheUtil.get(appTrafficKey)){
					appTrafficVal = appTrafficService.getAppRateRank_pie(appTraffic, user);
					ehcacheUtil.put(appTrafficKey, appTrafficVal);
				}else{
					appTrafficVal = ehcacheUtil.get(appTrafficKey);
				}
				
				//热门手机流量排名
				//cache
				String mobileTrafficKey = "mobileTraffic"+appTraffic.toString()+user.toString();
				String mobileTrafficVal;
				if(null == ehcacheUtil.get(mobileTrafficKey)){
					mobileTrafficVal = appTrafficService.getHotTelRank_pietoppage(appTraffic, user);
					ehcacheUtil.put(mobileTrafficKey, mobileTrafficVal);
				}else{
					mobileTrafficVal = ehcacheUtil.get(mobileTrafficKey);
				}
			
				//连接点地理分布
				
				String pointGeogKey = "pointGeog"+user.toString()+nwQuality.toString();
				List pointGeogVal;
				if(null == ehcacheUtil.getList(pointGeogKey)){
					pointGeogVal = nwQualityService.showAllPointGeog(user,nwQuality);
					ehcacheUtil.putList(pointGeogKey, pointGeogVal);
				}else{
					pointGeogVal = ehcacheUtil.getList(pointGeogKey);
					nwQuality.setStartDate(nwQuality.getStartDate()*100);
					nwQuality.setEndDate(nwQuality.getEndDate()*100);
				}
		
				
 				//网络速率（上行）
				//-----------------------------------
				nwQuality.setSpeedType(VarDesc.SPEEDTYPE.ULSPEED);
				nwQuality.setStartDate(nwQuality.getStartDate()/100);
				nwQuality.setEndDate(nwQuality.getEndDate()/100);
				//cache
				String nwqSpeedUpKey = "nwqSpeedUp"+nwQuality.toString()+user.toString();
				String nwqSpeedUpVal;
				if(null == ehcacheUtil.get(nwqSpeedUpKey)){
					nwqSpeedUpVal = nwQualityService.getNetworkQualityStatistics(nwQuality, user);
					ehcacheUtil.put(nwqSpeedUpKey, nwqSpeedUpVal);
				}else{
					nwqSpeedUpVal = ehcacheUtil.get(nwqSpeedUpKey);
				}

				//网络速率（下行）
				//------------------------------------
				nwQuality.setSpeedType(VarDesc.SPEEDTYPE.DLSPEED);
				nwQuality.setStartDate(nwQuality.getStartDate());
				nwQuality.setEndDate(nwQuality.getEndDate());
				//cache
				String nwqSpeedDownKey = "nwqSpeedDown"+nwQuality.toString()+user.toString();
				String nwqSpeedDownVal;
				if(null == ehcacheUtil.get(nwqSpeedDownKey)){
					nwqSpeedDownVal = nwQualityService.getNetworkQualityStatistics(nwQuality, user);
					ehcacheUtil.put(nwqSpeedDownKey, nwqSpeedDownVal);
				}else{
					nwqSpeedDownVal = ehcacheUtil.get(nwqSpeedDownKey);
				}
		
				//网络速率排名上行
				//cache
				String networkStaKey = "networkSta"+nwQuality.toString()+user.toString();
				String networkStaVal;
				if(null == ehcacheUtil.get(networkStaKey)){
					networkStaVal = nwQualityService.getNetworkQualityStatistics(nwQuality, user);
					ehcacheUtil.put(networkStaKey, networkStaVal);
				}else{
					networkStaVal = ehcacheUtil.get(networkStaKey);
				}
				
				
				ModelAndView modelAndView = new ModelAndView("view/MainPage/dashBoard");
				modelAndView.addObject("signalStrengthCount", signalStrengthCount);
				modelAndView.addObject("nwQualityCount", nwQualityCount);
				modelAndView.addObject("dataConnectionCount", dataConnectionCount);
				modelAndView.addObject("networkqualityU", nwqSpeedUpVal);
				modelAndView.addObject("networkqualityD", nwqSpeedDownVal);
				modelAndView.addObject("networkStaStr", networkStaVal);
				modelAndView.addObject("app_optionStr_pie", appTrafficVal);
				modelAndView.addObject("md_optionStr_pie", mobileTrafficVal);
				//连接点分布集合

				///modelAndView.addObject("nwQualities", JSON.toJSONString(nwQualities));
				modelAndView.addObject("nwQualities", JSON.toJSONString(pointGeogVal));
				
			
				return modelAndView;
			}
			return new ModelAndView("redirect:/timeout.jsp");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	//仪表盘通过Ajax刷新实现实时增加
	@RequestMapping(value="dashBoard_flash")
	public @ResponseBody String getDashBoardFlash(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//默认数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			// Network Quality当天产生数据总数
			int nCurrentCount = readFileCounts.getFileCounts("NWQuality");
		
			// Signal Strength当天产生数据总数
			int sCurrentCount = readFileCounts.getFileCounts("SignalStrength");
			// Data Connection当天产生数据总数
			int dCurrentCount = readFileCounts.getFileCounts("DataConnection");
			long[] counts = new long[3];
			//三个表总数
			List<TableCounts> tableCounts = nwQualityService.getTableCounts(user);
			for (TableCounts tableCount : tableCounts) {
				// Network Quality总数
				counts[0] = tableCount.getNWQulity_count()+nCurrentCount;
				// Signal Strength总数
				counts[1] = tableCount.getSignal_Strength_count()+sCurrentCount;
				// Data Connection总数
				counts[2] = tableCount.getDataConnection_count()+dCurrentCount;
			}
			String value = JSON.toJSONString(counts);
			return value;
		}
		return "redirect:/timeout.jsp";

	}

	
	
	
	//仪表盘地图增量点刷新
	@RequestMapping(value="freshMapPoint")
	public @ResponseBody List<NWQuality> freshMapPoint(HttpSession session) {
		
		//System.out.println(4564646);

		//读取字节
		long nCurrentCount = readFileCounts.fileLength();
		
		//把当前nwquality的数据总函数放入session
		Object strnWquaCount=(Object)session.getAttribute("currnWquality");
		
		long nWquaCount=0l;

		if (strnWquaCount!=null) {
	
			nWquaCount=(Long)strnWquaCount;
		}else{
		
			nWquaCount=0l;
		}
		List<NWQuality> nwQualities_new=null;
		
		//session有值
		if (nWquaCount!=0l ) {
			if (nCurrentCount==nWquaCount) {
				session.setAttribute("currnWquality", nCurrentCount);			
				//查找以前的点
				//nwQualities_old = readFileCounts.freshMapPoint(0l);
				return null;
			}else if(nCurrentCount>nWquaCount){
				session.setAttribute("currnWquality", nCurrentCount);
				//nwQualities_old = readFileCounts.freshMapPoint(0l);
				nwQualities_new = readFileCounts.freshMapPoint(nWquaCount);
				return nwQualities_new;
			}else{
				session.setAttribute("currnWquality", nCurrentCount);
				nwQualities_new = readFileCounts.freshMapPoint(0l);
				return nwQualities_new;
				
			}
		}else{
		//session没值
			session.setAttribute("currnWquality", nCurrentCount);
			nwQualities_new = readFileCounts.freshMapPoint(0l);
			return nwQualities_new;
		}
		
		
	}
	
	
	
	//仪表盘地图增量点刷新
		@RequestMapping(value="freshMapOldPoint")
		public @ResponseBody List<NWQuality> freshMapOldPoint(HttpSession session) {

			//读取字节
			long nCurrentCount = readFileCounts.fileLength();
			
			//把当前nwquality的数据总函数放入session
			//long nWquaCount=(long)session.getAttribute("currnWquality");
			
			Object strnWquaCount=(Object)session.getAttribute("currnWquality");
	
			
			long nWquaCount=0l;

			if (strnWquaCount!=null) {

				nWquaCount=(Long)strnWquaCount;
			}else{

				nWquaCount=0l;
			}
			
			
			List<NWQuality> nwQualities_old=null;
			
			//session有值
			if (nWquaCount!=0l ) {
				if (nCurrentCount==nWquaCount) {
					//session.setAttribute("currnWquality", nCurrentCount);			
					//查找以前的点
					nwQualities_old = readFileCounts.freshMapOldPoint(0l,nWquaCount);
					return null;
				}else if(nCurrentCount>nWquaCount){
					//session.setAttribute("currnWquality", nCurrentCount);
					nwQualities_old = readFileCounts.freshMapOldPoint(0l,nWquaCount);
					
					return nwQualities_old;
				}else{
					//session.setAttribute("currnWquality", nCurrentCount);
					//nwQualities_new = readFileCounts.freshMapPoint(0l);
					return null;
					
				}
			}else{
			//session没值
				//session.setAttribute("currnWquality", nCurrentCount);
				//nwQualities_new= readFileCounts.freshMapPoint(0l);
				return null;
			}
			
			
		}
	// 连接点总数
	@RequestMapping(value = "nwquality_testpointCount")
	public ModelAndView getTestpointCount(NWQuality nwQuality,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (nwQuality.getDaytime() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
				nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
				nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
			}
			nwQuality.setNwOperator("ALL");
			
	
			// Network Quality总数
			long nwQualityCount = nwQualityService.getNWQualityCount(user);
			// Signal Strength总数
			long signalStrengthCount = nwQualityService.getSignalStrengthCount(user);
			// Data Connection总数
			long dataConnectionCount = nwQualityService.getDataConnectionCount(user);
			ModelAndView modelAndView = new ModelAndView(
					"view/MainPage/testPointCount");
			modelAndView.addObject("signalStrengthCount", signalStrengthCount);
			modelAndView.addObject("nwQualityCount", nwQualityCount);
			modelAndView.addObject("dataConnectionCount", dataConnectionCount);
			modelAndView.addObject("dayTime", nwQuality.getDaytime());
	
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");

	}

	// 通过AJax方式触发连接点数统计
	@RequestMapping(value = "nwquality_testpointCount_control")
	public @ResponseBody String getPointCountControl(NWQuality nwQuality,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 分割时间
			if (nwQuality.getDaytime() != null) {
				DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
				nwQuality.setStartDate(dateFormat.getStartDate());
				nwQuality.setEndDate(dateFormat.getEndDate());
			}
			long[] counts = new long[3];
			// Network Quality总数
			counts[0] = nwQualityService.getNWQualityCount(user);
			// Signal Strength总数
			counts[1] = nwQualityService
					.getSignalStrengthCount(user);
			// Data Connection总数
			counts[2] = nwQualityService
					.getDataConnectionCount(user);
			String value = JSON.toJSONString(counts);
			return value;
		}
		return "redirect:/timeout.jsp";
	}

	// 连接点地理分布
	@RequestMapping(value = "showAllPointGeog")
	public ModelAndView showAllPointGeog(HttpServletRequest request,
			NWQuality nwQuality) {

		User user = (User) request.getSession().getAttribute("user");
		
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
 
		
		ModelAndView modelAndView = new ModelAndView(
				"view/MainPage/pointGeogRank");
		List<NWQuality> nwQualities = nwQualityService.showAllPointGeog(user,
				nwQuality);
		String val = JSON.toJSONString(nwQualities);
		modelAndView.addObject("nwQualities", val);
		modelAndView.addObject("nwQuality", nwQuality);
//		System.out.println(val);
		return modelAndView;
	}

	// 连接点数排名
	@RequestMapping(value = "nwquality_testpointRank")
	public ModelAndView getPointRank(HttpServletRequest request,
			HttpSession session, NWQuality nwQuality) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (nwQuality.getDaytime() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
				nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
				nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
			}
			nwQuality.setNwOperator("ALL");
			String optionStr = nwQualityService.getPointRank(nwQuality, user);
			ModelAndView modelAndView = new ModelAndView(
					"view/MainPage/testPointRank");
			modelAndView.addObject("optionStr", optionStr);
			modelAndView.addObject("dayTime", nwQuality.getDaytime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");

	}

	// 通过AJax方式触发连接点数排名
	@RequestMapping(value = "nwquality_testpointRank_control")
	public @ResponseBody String getPointRankingControl(NWQuality nwQuality,
			HttpSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 分割时间
			if (nwQuality.getDaytime() != null) {
				DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
				nwQuality.setStartDate(dateFormat.getStartDate());
				nwQuality.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = nwQualityService.getPointRank(nwQuality, user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

 
	
	// 表示不检测过期的方法
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "nwquality_networkRateRank")
	public ModelAndView getNetworkRateRank(HttpServletRequest request,
			NWQuality nwQuality) {
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		if (nwQuality.getDaytime() == null) {
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
			nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
			nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
			nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
		} else {
			DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
			nwQuality.setStartDate(dateFormat.getStartDate());
			nwQuality.setEndDate(dateFormat.getEndDate());
		}
		String viewId = request.getParameter("viewId");
		String optionStr = nwQualityService.getNetworkRateRank(nwQuality);
		ModelAndView modelAndView = new ModelAndView(
				"view/MainPage/networkRateRank");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("viewId", viewId);
		modelAndView.addObject("nwOperator", nwQuality.getNwOperator());
		modelAndView.addObject("speedType", nwQuality.getSpeedType());
		modelAndView.addObject("dayTime", nwQuality.getDaytime());
		return modelAndView;
	}

 
	@SuppressWarnings("deprecation")
	// 表示不检测过期的方法
	@RequestMapping(value = "nwquality_terminalTrafficRateRank")
	public ModelAndView getTerminalTrafficRateRank(HttpServletRequest request,
			HttpSession session, NWQuality nwQuality) {
		if (nwQuality.getDaytime() == null) {
			DateFormat dateFormat = new DateFormat();
			nwQuality.setEndDate(dateFormat.getMonthAgo_end());
			nwQuality.setStartDate(dateFormat.getMonthAgo_start());
			//nwQuality.setStartDate(20111001);
			nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
		} else {
			DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
			nwQuality.setStartDate(dateFormat.getStartDate());
			nwQuality.setEndDate(dateFormat.getEndDate());
		}
		User user = (User) session.getAttribute("user");
		String optionStr = nwQualityService.getTerminalTrafficRateRank(nwQuality,user);

		ModelAndView modelAndView = new ModelAndView(
				"view/MainPage/terminalTrafficRateRank");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("nwOperator", nwQuality.getNwOperator());
		modelAndView.addObject("speedType", nwQuality.getSpeedType());
		modelAndView.addObject("dayTime", nwQuality.getDaytime());
		return modelAndView;
	}

 
	@SuppressWarnings("deprecation")
	// 表示不检测过期的方法
	@RequestMapping(value = "nwquality_networkQualityStatistics")
	public ModelAndView getNetworkQualityStatistics(HttpServletRequest request,
			HttpSession session, NWQuality nwQuality) {
		
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		if (nwQuality.getDaytime() == null) {
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
			nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
			nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
			nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
			} else {

			DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
			nwQuality.setStartDate(dateFormat.getStartDate());
			nwQuality.setEndDate(dateFormat.getEndDate());
		}
		User user = (User) session.getAttribute("user");
		
 
		
		String optionStr = nwQualityService.getNetworkQualityStatistics(nwQuality,user);
		ModelAndView modelAndView = new ModelAndView("view/NetworkQuality/networkQualityStatistics");
		modelAndView.addObject("optionStr", optionStr);
//		modelAndView.addObject("optionStr", val);
		modelAndView.addObject("nwOperator", nwQuality.getNwOperator());
		modelAndView.addObject("speedType", nwQuality.getSpeedType());
		modelAndView.addObject("dayTime", nwQuality.getDaytime());
		return modelAndView;
	}
 
		@SuppressWarnings("deprecation")
		// 表示不检测过期的方法
		@RequestMapping(value = "nwquality_networkQualityTracks")
		public ModelAndView getNetworkQualityTracks(HttpServletRequest request,
				HttpSession session, NWQuality nwQuality) {
			try{
				CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				if (nwQuality.getEndDateToSecond() == null) {
					nwQuality.setEndDate((new DateFormat().getMonthAgo_end())*100);
				} else {

					nwQuality.setEndDate((new DateFormat().getMonthAgo_end())*100);
				}
				if (nwQuality.getStartDateToSecond() == null) {
					nwQuality.setStartDate(2010010101);
				} else {
					nwQuality.setStartDate(dateFormat.convertDateToSecondInt(nwQuality.getStartDateToSecond()));
				}
				User user = (User) session.getAttribute("user");
				String optionStr = nwQualityService.getNetworkQualityTracks(nwQuality,user);
				ModelAndView modelAndView = new ModelAndView("view/NetworkQuality/networkQualityTracks");
				modelAndView.addObject("optionStr", optionStr);
				modelAndView.addObject("nwType", nwQuality.getNwType());
				modelAndView.addObject("speedType", nwQuality.getSpeedType());
				modelAndView.addObject("landmark", nwQuality.getLandmark());
				modelAndView.addObject("startDateToSecond", dateFormat.showDateToSecond(nwQuality.getStartDate()));
				modelAndView.addObject("endDateToSecond",dateFormat.showDateToSecond(nwQuality.getEndDate()));
				modelAndView.addObject("x_type",nwQuality.getX_type());
				return modelAndView;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	
	
	

		/*
		 * 网络质量瓷砖图
		 */
		@RequestMapping(value = "showNwQuaTileSpeed")
		public @ResponseBody String showNwQuaTileSpeed(NWQuality nwQuality,
				HttpSession session) throws Exception {
			
		/*	logger.info("网络质量瓷砖图测试开始——————-————————————-——————————-————————-——————————");*/
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			User user = (User) session.getAttribute("user");

			List<Object> qualities = nwQualityService.showNwQuaTileSpeed(user,nwQuality);

			String strNwQuaTileSpeed = JSON.toJSONString(qualities);
			
			return strNwQuaTileSpeed;
		}

	
	/*
	 * 用户瓷砖图
	 */
	@RequestMapping(value="showAllUserPointTileGeog")
	public @ResponseBody String showAllUserPointTileGeog(NWQuality nwQuality,
			HttpSession session){
		List<NWQuality> NWQualitys = new ArrayList<NWQuality>();
		logger.info("用户瓷砖图测试开始——————-————————————-——————————-————————-——————————");
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		User user = (User) session.getAttribute("user");
		
		NWQualitys = nwQualityService.showAllUserPointTileGeog(user,nwQuality);
		for (int q=0;q<NWQualitys.size();q++){
			System.out.println(NWQualitys.get(q));
		}
		System.out.println(NWQualitys.size());
		String datas = JSON.toJSONString(NWQualitys);
		System.out.println(datas);
		
		return datas;
		//return qualities;
	}
	
	// 热门手机网络质量排名（直方图）
	@RequestMapping(value = "nwquality_mobileNQRank")
	public ModelAndView getMobileNQRank(HttpSession session,NWQuality nwQuality) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
		//	CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_SPARKSQL);
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (nwQuality.getDaytime() == null) {
				
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				nwQuality.setStartDate(20100101);
				nwQuality.setEndDate(new DateFormat().getMonthAgo_end());
				nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
			}
			// 默认设置网络质量下行速率
			nwQuality.setSpeedType(VarDesc.SPEEDTYPE.DLSPEED);
			// 默认设置网络制式All
			nwQuality.setNwType(VarDesc.NETWORKTYPE.ALL);
			// 默认运营商All
			nwQuality.setNwOperator(VarDesc.OPERATOR.ALL);
			String optionStr = nwQualityService.getMobileNetworkQualityRank(nwQuality, user);
			System.out.println("optionStr:"+optionStr);
			ModelAndView modelAndView = new ModelAndView("view/DeviceMap/mobileNetworkQualityRank");
			modelAndView.addObject("optionStr", optionStr);
			modelAndView.addObject("dayTime", nwQuality.getDaytime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");

	}


	// 典型地标网络质量统计图
	@RequestMapping(value="nwquality_landmarkNQStatistics")
	public ModelAndView getLandmarkNetworkQualityStatistics(HttpSession session,NWQuality nwQuality) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (nwQuality.getDaytime() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				nwQuality.setEndDate(Integer.valueOf(defaultConf.getEndDateNwquality().substring(0, 8)));
				nwQuality.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateNwquality(), defaultConf.getIntervalDayNwquality()).substring(0, 8)));
				nwQuality.setDaytime(dateFormat.showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
			}
			// 默认设置网络质量下行速率
			nwQuality.setSpeedType(VarDesc.SPEEDTYPE.DLSPEED);
			// 默认设置网络制式All
			nwQuality.setNwType(VarDesc.NETWORKTYPE.G3);
			String optionStr = nwQualityService.getLandmarkNetworkQualityStatistics(nwQuality, user);
			ModelAndView modelAndView = new ModelAndView("view/DeviceMap/landmarkNQStatistics");
			modelAndView.addObject("optionStr", optionStr);
			modelAndView.addObject("dayTime", nwQuality.getDaytime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");

	}
	//通过Ajax触发典型地标网络质量统计图
	@RequestMapping(value="landmarkNQStatistics_control")
	public @ResponseBody String getLandmarkNQStatisticsControl(HttpSession session,NWQuality nwQuality) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 分割时间
			if (nwQuality.getDaytime() != null) {
				DateFormat dateFormat = new DateFormat(nwQuality.getDaytime());
				nwQuality.setStartDate(dateFormat.getStartDate());
				nwQuality.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = nwQualityService.getLandmarkNetworkQualityStatistics(nwQuality, user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}


	// 手机网络质量瓷砖图
	@RequestMapping(value = "showTePhNwQuaTileMap")
	public @ResponseBody String showTePhNwQuaTileMap(NWQuality nwQuality,HttpSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		List<Object>  qualities=nwQualityService.showTePhNwQuaTileMap(user,nwQuality);

		
		//List<NWQuality>  tePhNwQuaTypes=nwQualityService.showTePhNwQuaType();
		String strNwQuaTileSpeed = JSON.toJSONString(qualities);
		
		return strNwQuaTileSpeed;
	}
	
	//查询所有手机类型
	@RequestMapping(value = "showTePhNwQuaType")
	public @ResponseBody List<NWQuality> showTePhNwQuaType(NWQuality nwQuality,HttpSession session) throws Exception {
		
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		List<NWQuality>  tePhNwQuaTypes=nwQualityService.showTePhNwQuaType();

//		String strtePhNwQuaTypes = JSON.toJSONString(tePhNwQuaTypes);
		return tePhNwQuaTypes;
	}
	
	/*
 	 * 下面是get、set方法
 	 */

	public NWQualityService getnWQualityService() {
		return nwQualityService;
	}

	@Resource
	public void setnWQualityService(NWQualityService nwQualityService) {
		this.nwQualityService = nwQualityService;
	}

	public DataConnectionService getDataConnService() {
		return dataConnService;
	}

	@Resource
	public void setDataConnService(DataConnectionService dataConnService) {
		this.dataConnService = dataConnService;
	}

	public AppTrafficService getAppTrafficService() {
		return appTrafficService;
	}

	@Resource
	public void setAppTrafficService(AppTrafficService appTrafficService) {
		this.appTrafficService = appTrafficService;
	}
	public ReadFileCounts getReadFileCounts() {
		return readFileCounts;
	}

	@Resource
	public void setReadFileCounts(ReadFileCounts readFileCounts) {
		this.readFileCounts = readFileCounts;
	}
	
	public EhcacheUtil getEhcacheUtil() {
		return ehcacheUtil;
	}
	@Resource
	public void setEhcacheUtil(EhcacheUtil ehcacheUtil) {
		this.ehcacheUtil = ehcacheUtil;
	}

}
