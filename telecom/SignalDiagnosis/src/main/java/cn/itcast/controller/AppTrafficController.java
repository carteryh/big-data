package cn.itcast.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.entity.DefaultConf;
import cn.itcast.util.CustomerContextHolder;
import cn.itcast.util.DateFormat;
import cn.itcast.util.DefaultConfUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.service.AppTrafficService;

@Controller
public class AppTrafficController {
	private AppTrafficService appTrafficService;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(NWQualityController.class);


	//不知道要干啥的街景图
	@RequestMapping(value="showAppStreetScape")
	public ModelAndView showAppStreetScape(HttpSession session,AppTraffic appTraffic){
		// 转换制定的数据源
		CustomerContextHolder
							.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		User user = (User) session.getAttribute("user");
		List<AppTraffic>  appTraffics=appTrafficService.getAppStreetScape(user,appTraffic);
		
		ModelAndView modelAndView=new ModelAndView("view/AppMap/AppStreetScape");
		
		String strappTraffics = JSON.toJSONString(appTraffics);
		modelAndView.addObject("strappTraffics", strappTraffics);
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="showAllStreetScape")
	public @ResponseBody List<AppTraffic> showAllStreetScape(HttpSession session,AppTraffic appTraffic){
		// 转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		User user = (User) session.getAttribute("user");
		List<AppTraffic>  appTraffics=appTrafficService.getAppStreetScape(user,appTraffic);
		
		return appTraffics;
	}
	// App流量排名
	@RequestMapping(value = "app_appRateRank")
	public ModelAndView getAppRateRank(HttpSession session,
			AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			//CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (appTraffic.getDate() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf= DefaultConfUtil.newInstance().getDefaultconf();
				appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
				appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
				appTraffic.setDateTime(dateFormat.showDate(appTraffic.getStartDate(), appTraffic.getEndDate()));
			}

			// 默认筛选所有运营商
			appTraffic.setNetwork_name(VarDesc.OPERATOR.ALL);
			// 默认筛选下行速率
			appTraffic.setSpeed_type(VarDesc.SPEEDTYPE.ALL);
			// 直方图
			String optionStr_historgram = appTrafficService
					.getAppRateRank_historgram(appTraffic, user);
			// 饼图
			String optionStr_pie = appTrafficService.getAppRateRank_pie(
					appTraffic, user);
			ModelAndView modelAndView = new ModelAndView(
					"view/MainPage/appTrafficRateRank");
			modelAndView
					.addObject("optionStr_historgram", optionStr_historgram);
			modelAndView.addObject("optionStr_pie", optionStr_pie);
			modelAndView.addObject("date", appTraffic.getDateTime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}

	// 通过AJax方式触发App流量排名(直方图)
	@RequestMapping(value = "appRateHistorgram_control")
	public @ResponseBody String getAppRateHistorgramControl(
			HttpServletResponse response, HttpSession session,
			AppTraffic appTraffic) {
		// 转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			if (appTraffic.getDate() != null) {
				DateFormat dateFormat = new DateFormat(appTraffic.getDate());
				appTraffic.setStartDate(dateFormat.getStartDate());
				appTraffic.setEndDate(dateFormat.getEndDate());
			}
			String optionStr_historgram = appTrafficService
					.getAppRateRank_historgram(appTraffic, user);
			return optionStr_historgram;
		}
		return "redirect:/timeout.jsp";
	}

	// 通过AJax方式触发App流量排名(饼图)
	@RequestMapping(value = "appRatePie_control")
	public @ResponseBody String getAppRatePieControl(
			HttpServletResponse response, HttpSession session,
			AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			if (appTraffic.getDate() != null) {
				DateFormat dateFormat = new DateFormat(appTraffic.getDate());
				appTraffic.setStartDate(dateFormat.getStartDate());
				appTraffic.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = appTrafficService.getAppRateRank_pie(appTraffic,
					user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

	// 热门手机流量排名（直方图）
	@RequestMapping(value = "app_hotTelRank")
	public ModelAndView getHotTelRank(HttpSession session, AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (appTraffic.getDate() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
				appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
				appTraffic.setDateTime(dateFormat.showDate(appTraffic.getStartDate(), appTraffic.getEndDate()));
			}
			// 默认筛选所有运营商
			appTraffic.setNetwork_name(VarDesc.OPERATOR.ALL);
			// 默认筛选下行速率
			appTraffic.setSpeed_type(VarDesc.SPEEDTYPE.ALL);
			String optionStr_historgram = appTrafficService
					.getHotTelRank_historgram(appTraffic, user);
			String optionStr_pie = appTrafficService.getHotTelRank_pie(
					appTraffic, user);
			ModelAndView modelAndView = new ModelAndView(
					"view/DeviceMap/hotTelRank");
			modelAndView
					.addObject("optionStr_historgram", optionStr_historgram);
			modelAndView.addObject("optionStr_pie", optionStr_pie);
			modelAndView.addObject("dayTime", appTraffic.getDateTime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");

	}

	// 通过AJAX触发热门手机流量排名（直方图）
	@RequestMapping(value = "hotTelRankHistorgram_control")
	public @ResponseBody String getHotTelRankHistorgramControl(
			HttpSession session, AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			// 分割时间
			if (appTraffic.getDate() != null) {
				DateFormat dateFormat = new DateFormat(appTraffic.getDate());
				appTraffic.setStartDate(dateFormat.getStartDate());
				appTraffic.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = appTrafficService.getHotTelRank_historgram(
					appTraffic, user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

	// 通过AJAX触发热门手机流量排名（饼图）
	@RequestMapping(value = "hotTelRankPie_control")
	public @ResponseBody String getHotTelRankPieControl(HttpSession session,
			AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			// 分割时间
			if (appTraffic.getDate() != null) {
				DateFormat dateFormat = new DateFormat(appTraffic.getDate());
				appTraffic.setStartDate(dateFormat.getStartDate());
				appTraffic.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = appTrafficService.getHotTelRank_pie(appTraffic,
					user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

	// 典型地标热门App流量排名
	@RequestMapping(value = "app_landmarkAppTrafficRank")
	public ModelAndView getLandmarkAppTrafficRank(HttpSession session,
			AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder
					.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (appTraffic.getDate() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
				appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
				//appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
				appTraffic.setStartDate(20100125);
				appTraffic.setDateTime(dateFormat.showDate(appTraffic.getStartDate(), appTraffic.getEndDate()));
			}
			// 默认筛选所有运营商
			appTraffic.setNetwork_name(VarDesc.OPERATOR.ALL);
			// 默认筛选总流量
			appTraffic.setSpeed_type(VarDesc.SPEEDTYPE.ALL);
			//默认landmark地标
			appTraffic.setLandmark("university");
			String optionStr= appTrafficService.getLandmarkAppTrafficRank(appTraffic, user);
			ModelAndView modelAndView = new ModelAndView("view/MainPage/landmarkAppTrafficRank");
			modelAndView.addObject("optionStr", optionStr);
			modelAndView.addObject("date", appTraffic.getDateTime());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}
	// 通过Ajax触发典型地标热门App流量排名
	@RequestMapping(value = "landmarkAppRank_control")
	public @ResponseBody String getLandmarkAppRankControl(HttpSession session,
			AppTraffic appTraffic) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			// 转换制定的数据源
			CustomerContextHolder
					.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			// 分割时间
			if (appTraffic.getDate() != null) {
				DateFormat dateFormat = new DateFormat(appTraffic.getDate());
				appTraffic.setStartDate(dateFormat.getStartDate());
				appTraffic.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = appTrafficService.getLandmarkAppTrafficRank(appTraffic, user);
			System.out.println("cccccccccc+"+optionStr);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

	@RequestMapping(value = "appTraffic_hotAppTrafficStatistics")
	public ModelAndView hotAppTrafficStatistics(HttpServletRequest request,
			HttpSession session, AppTraffic appTraffic) {
		if (appTraffic.getDateTime() == null) {
			DateFormat dateFormat = new DateFormat();
			appTraffic.setEndDate(dateFormat.getMonthAgo_end());
			appTraffic.setStartDate(dateFormat.getMonthAgo_start());
			// nwQuality.setStartDate(20111001);
			appTraffic.setDateTime(dateFormat.showDate(
					appTraffic.getStartDate(), appTraffic.getEndDate()));
		} else {
			DateFormat dateFormat = new DateFormat(appTraffic.getDateTime());
			appTraffic.setStartDate(dateFormat.getStartDate());
			appTraffic.setEndDate(dateFormat.getEndDate());
		}
		User user = (User) session.getAttribute("user");
		String optionStr = appTrafficService.hotAppTrafficStatistics(
				appTraffic, user);
		ModelAndView modelAndView = new ModelAndView(
				"view/AppMap/appTraffic_hotAppTrafficStatistics");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("nwOperator", appTraffic.getNetwork_name());
		modelAndView.addObject("speedType", appTraffic.getSpeed_type());
		modelAndView.addObject("dayTime", appTraffic.getDateTime());
		return modelAndView;
	}


	// 表示不检测过期的方法
	@RequestMapping(value = "appTraffic_appTrafficTracks")
	public ModelAndView getappTrafficTracks(HttpServletRequest request,
			HttpSession session, AppTraffic appTraffic) {
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		DateFormat dateFormat = new DateFormat();
		DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
		if (appTraffic.getEndDateToSecond() == null) {
			appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic()));
		} else {
			appTraffic.setEndDate(dateFormat.convertDateToSecondInt(appTraffic.getEndDateToSecond()));
		}
		if (appTraffic.getStartDateToSecond() == null) {
			appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic())));
		} else {
			appTraffic.setStartDate(dateFormat.convertDateToSecondInt(appTraffic.getStartDateToSecond()));
		}
		
		User user = (User) session.getAttribute("user");
		String optionStr = appTrafficService.getAppTrafficTracks(appTraffic,
				user);
		System.out.println(optionStr);

		ModelAndView modelAndView = new ModelAndView(
				"view/AppMap/appTrafficTracks");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("nwType", appTraffic.getNetworkType());
		modelAndView.addObject("nwOperator", appTraffic.getNetwork_name());
		modelAndView.addObject("speedType", appTraffic.getSpeed_type());
		modelAndView.addObject("startDateToSecond",
				dateFormat.showDateToSecond(appTraffic.getStartDate()));
		modelAndView.addObject("endDateToSecond",
				dateFormat.showDateToSecond(appTraffic.getEndDate()));
		modelAndView.addObject("x_type", appTraffic.getX_type());
		return modelAndView;
	}


	// 表示不检测过期的方法
	@RequestMapping(value = "appTraffic_hotTelOsStatistics")
	public ModelAndView getHotTelOsStatistics(HttpServletRequest request,
			HttpSession session, AppTraffic appTraffic) {
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		// CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		if (appTraffic.getDateTime() == null) {
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
			appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
			appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
			appTraffic.setDateTime(dateFormat.showDate(appTraffic.getStartDate(), appTraffic.getEndDate()));
			
		} else {
			DateFormat dateFormat = new DateFormat(appTraffic.getDateTime());
			appTraffic.setStartDate(dateFormat.getStartDate());
			appTraffic.setEndDate(dateFormat.getEndDate());
		}

		

		User user = (User) session.getAttribute("user");
		String optionStr = appTrafficService.getHotTelOsStatistics(appTraffic,
				user);
		// System.out.println(optionStr);
		String pieStr = appTrafficService.getHotTelOsStatistics_pie(appTraffic,
				user);
		// System.out.println(pieStr);
		ModelAndView modelAndView = new ModelAndView(
				"view/DeviceMap/hotTelOsStatistics");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("pieStr", pieStr);
		modelAndView.addObject("nwOperator", appTraffic.getNetwork_name());
		modelAndView.addObject("speedType", appTraffic.getSpeed_type());
		modelAndView.addObject("dayTime", appTraffic.getDateTime());
		return modelAndView;
	}

	/*
	 * 获得前一个时间
	 */
	@RequestMapping(value = "getdate2")
	public @ResponseBody String getdate2(HttpServletRequest request,
			AppTraffic appTraffic) throws SQLException {

		if (appTraffic.getStart_time() == null) {

			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();

			appTraffic.setEnd_time(dateFormat.showDateToSecond( (long)Integer.parseInt(defaultConf.getEndDateAppTraffic().substring(0, 8))*10));
			appTraffic.setStart_time(dateFormat.showDateToSecond( (long)Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8))*10));

			}

		String datas = JSON.toJSONString(appTraffic);
		return datas;
	}
	
	/*
	 * 获得前一个时间
	 */
	@RequestMapping(value = "getdate")
	public @ResponseBody String getdate(HttpServletRequest request,
			AppTraffic appTraffic) throws SQLException {

		if (appTraffic.getStart_time() == null) {
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
			appTraffic.setDate(dateFormat.showDates(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)),Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8))));
			
		}

		String datas = JSON.toJSONString(appTraffic);
		return datas;

	}
	
	
	
	
	
	

	@RequestMapping(value = "top10app")
	public @ResponseBody String getnwtype(HttpServletRequest request,AppTraffic appTraffic) throws SQLException {
		// 转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		DateFormat dateFormat = new DateFormat();
		DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
		appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
		appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
		//System.out.println("selecting  top10 app data");
		logger.info("selecting  top10 app data");
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
		appTraffics = appTrafficService.gettop10app(appTraffic);

		String apptop10 = JSON.toJSONString(appTraffics);
		//System.out.println("selected  top10 app data");
		logger.info("selected  top10 app data");
		return apptop10;

	}

	@RequestMapping(value = "top5mobile")
	public @ResponseBody String top5Mobile(HttpServletRequest request,
			AppTraffic appTraffic) throws SQLException {
		// 转换制定的数据源
		//CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		DateFormat dateFormat = new DateFormat();
		DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
		appTraffic.setEndDate(Integer.valueOf(defaultConf.getEndDateAppTraffic().substring(0, 8)));
		appTraffic.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateAppTraffic(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
		System.out.println(appTraffic.getStartDate());
		System.out.println(appTraffic.getEndDate());
	
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
		logger.info("selecting  top5 mobile data");
		
		appTraffics = appTrafficService.gettop5mobile(appTraffic);
		logger.info("selected  top5 mobile data");
		
		String mobiletop10 = JSON.toJSONString(appTraffics);
		return mobiletop10;

	}

	/*
	 * 热门app分布图、 heatappdistribution
	 */
	@RequestMapping(value = "heatappdistribution")
	public @ResponseBody String HeatAppDistribution(HttpServletRequest request,
			AppTraffic appTraffic, HttpSession session) throws SQLException {
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
		DateFormat dateFormat = new DateFormat();
		// 获取前台传过来的数据
		// 经度最小值
		double bssw_lng = Double.parseDouble(request.getParameter("bssw_lng"));
		// 经度最大值
		double bsne_lng = Double.parseDouble(request.getParameter("bsne_lng"));
		// 纬度最小值
		double bssw_lat = Double.parseDouble(request.getParameter("bssw_lat"));
		// 纬度最大值
		double bsne_lat = Double.parseDouble(request.getParameter("bsne_lat"));

		// 起始时间
		String sDate = request.getParameter("sDate");
		// 结束时间
		String eDate = request.getParameter("eDate");

		User user = (User) session.getAttribute("user");
		// 如果是admin用户，将“admin”添加进user属性内
		if (user.getRole().equalsIgnoreCase("admin")) {
			appTraffic.setRole("admin");
		}
		// 如果不是admin用户，将真实用户名称添加进user属性内
		else {
			appTraffic.setRole(user.getNWOperator());
		}

		String data = request.getParameter("daytime");
		String networkname = request.getParameter("nwOperator");
		String network_type = request.getParameter("networktype");
		String flow = request.getParameter("flow");
		String app = request.getParameter("app");

		int stopdate = dateFormat.getMonthAgo_end() * 100;
		int startdate = dateFormat.getMonthAgo_start() * 100;

		appTraffic.setNetwork_name(networkname);
		appTraffic.setNetworkType(network_type);

		appTraffic.setFlow(flow);
		appTraffic.setPackage_name(app);
		if (flow.equalsIgnoreCase("download_traffic")) {
			appTraffic.setFlow("download_traffic");
		} else if (flow.equalsIgnoreCase("upload_traffic")) {
			appTraffic.setFlow("upload_traffic");
		} else {
			appTraffic.setFlow("ALL");
		}

		if (sDate.equals("") && eDate.equals("")) {
			appTraffic.setStartDate(startdate);
			appTraffic.setEndDate(stopdate);
		} else if ((!sDate.equals("")) && eDate.equals("")) {
			startdate = Integer.parseInt(((sDate.replace("-", "")).replace(" ",
					"")).substring(0,
					((sDate.replace("-", "")).replace(" ", "")).indexOf(":")));
			appTraffic.setStartDate(startdate);
			appTraffic.setEndDate(stopdate);
		} else if (sDate.equals("") && (!eDate.equals(""))) {
			appTraffic.setStartDate(startdate);
			stopdate = Integer.parseInt(((eDate.replace("-", "")).replace(" ",
					"")).substring(0,
					((eDate.replace("-", "")).replace(" ", "")).indexOf(":")));
			appTraffic.setEndDate(stopdate);
		} else if (sDate.equals(eDate)) {
			startdate = Integer.parseInt(((sDate.replace("-", "")).replace(" ",
					"")).substring(0,
					((sDate.replace("-", "")).replace(" ", "")).indexOf(":")));
			appTraffic.setStartDate(startdate);
			appTraffic.setEndDate(stopdate);
		} else {
			startdate = Integer.parseInt(((sDate.replace("-", "")).replace(" ",
					"")).substring(0,
					((sDate.replace("-", "")).replace(" ", "")).indexOf(":")));
			appTraffic.setStartDate(startdate);
			stopdate = Integer.parseInt(((eDate.replace("-", "")).replace(" ",
					"")).substring(0,
					((eDate.replace("-", "")).replace(" ", "")).indexOf(":")));
			appTraffic.setEndDate(stopdate);
		}

		// 将对应的数据传到service层
		     
		appTraffics = appTrafficService.getappdistribution(bssw_lng, bsne_lng,
				bssw_lat, bsne_lat, appTraffic);
		String datas = JSON.toJSONString(appTraffics);
		
		return datas;
	}

	/*
	 * 热门手机分布图、 heatmobiledistribution
	 */
	@RequestMapping(value = "heatmobiledistribution")
	public @ResponseBody String HeatMobileDistribution(
			HttpServletRequest request, AppTraffic appTraffic,
			HttpSession session) throws SQLException {

		// 转换制定的数据源
		// CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
		// 获取前台传过来的数据
		// 经度最小值
		double bssw_lng = Double.parseDouble(request.getParameter("bssw_lng"));
		// 经度最大值
		double bsne_lng = Double.parseDouble(request.getParameter("bsne_lng"));
		// 纬度最小值
		double bssw_lat = Double.parseDouble(request.getParameter("bssw_lat"));
		// 纬度最大值
		double bsne_lat = Double.parseDouble(request.getParameter("bsne_lat"));
		
		if (appTraffic.getStart_time() == null) {
			DateFormat dateFormat = new DateFormat();
			appTraffic.setEndDate(dateFormat.getMonthAgo_end() * 100);
			appTraffic.setStartDate(dateFormat.getMonthAgo_start() * 100);
			appTraffic.setDate(dateFormat.showDate(
					dateFormat.getMonthAgo_start(),
					dateFormat.getMonthAgo_end()));

		}

		User user = (User) session.getAttribute("user");
		// 如果是admin用户，将“admin”添加进user属性内
		if (user.getRole().equalsIgnoreCase("admin")) {
			appTraffic.setRole("admin");
		}
		// 如果不是admin用户，将真实用户名称添加进user属性内
		else {
			appTraffic.setRole(user.getNWOperator());
		}

		String data = request.getParameter("daytime");
		String networkname = request.getParameter("nwOperator");
		String network_type = request.getParameter("networktype");
		String flow = request.getParameter("flow");
		String mobile = request.getParameter("mobile");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String startdatas = "";
		String stopdatas = "";
		int startdate = 0;
		int stopdate = 0;

		appTraffic.setNetwork_name(networkname);
		appTraffic.setNetworkType(network_type);

		appTraffic.setCompanyModel(mobile.trim());
		if (flow.equalsIgnoreCase("download_traffic")) {
			appTraffic.setFlow("download_traffic");
		} else if (flow.equalsIgnoreCase("upload_traffic")) {
			appTraffic.setFlow("upload_traffic");
		} else {
			appTraffic.setFlow("ALL");
		}
		// 过去查询时间段 查询多天
		if (data.contains("-")) {
			startdatas = data.substring(0, data.indexOf("-")).trim();
			stopdatas = data.substring(data.indexOf("-") + 1, data.length())
					.trim();
			startdate = Integer.parseInt(df.format(new Date(startdatas))) * 100;
			// 查询的结束时间
			stopdate = Integer.parseInt(df.format(new Date(stopdatas))) * 100;
			appTraffic.setStartDate(startdate);
			appTraffic.setEndDate(stopdate);
		}
		// 查询一天
		else if ((!data.contains("-")) && (!data.equals(""))) {
			startdate = Integer.parseInt(df.format(new Date(data))) * 100;
			appTraffic.setStartDate(startdate);
			// 当查询一天时，结束时间设为0
			appTraffic.setEndDate(stopdate);
		}

		// 将对应的数据传到service层

		appTraffics = appTrafficService.getmobiledistribution(bssw_lng,
				bsne_lng, bssw_lat, bsne_lat, appTraffic);
		String datas = JSON.toJSONString(appTraffics);
		return datas;
	}

	// app瓷砖图
	@RequestMapping(value = "showHeadApp")
	public @ResponseBody String showHeadApp(AppTraffic appTraffic,
			HttpSession session) throws Exception {

		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

		User user = (User) session.getAttribute("user");
		List<AppTraffic> qualities = appTrafficService.getheatApp(user,
				appTraffic);
		int a;
		String topapp = JSON.toJSONString(qualities);
		return topapp;
	}

	// mobile瓷砖图
	@RequestMapping(value = "showHeadMobile")
	public @ResponseBody String showHeadMobile(AppTraffic appTraffic,
			HttpSession session) throws Exception {

		
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		// 转换制定的数据源
		// CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		User user = (User) session.getAttribute("user");
		List<AppTraffic> qualities = appTrafficService.getheatMobile(user,
				appTraffic);
		int a;
		
		String topmobile = JSON.toJSONString(qualities);
		System.out.println(qualities.size()+"<<<<<<<<<<<<<<<<<<<<<<+    "+ topmobile);
		return topmobile;
	}

	//用户附近mobile街景图
	@RequestMapping(value = "showNearapp")
	public @ResponseBody String showNearapp(AppTraffic appTraffic,
			HttpSession session) throws Exception {
		
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		User user = (User) session.getAttribute("user");
		List<AppTraffic> qualities = appTrafficService.getUserNearapp(user,
				appTraffic);
		int a;

		String topmobile = JSON.toJSONString(qualities);
		return topmobile;
	}
	//用户附近mobile街景图
	@RequestMapping(value = "showNearOS")
	public @ResponseBody String showNearOS(AppTraffic appTraffic,
			HttpSession session) throws Exception {
		
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		User user = (User) session.getAttribute("user");
		List<AppTraffic> qualities = appTrafficService.getUserNearOS(user,
				appTraffic);
		int a;

		String topmobile = JSON.toJSONString(qualities);
		return topmobile;
	}

	
	//OS瓷砖图
		@RequestMapping(value = "showHeadOS")
		public @ResponseBody String showHeadOS(AppTraffic appTraffic,
				HttpSession session) throws Exception {
			
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
			User user = (User) session.getAttribute("user");
			List<AppTraffic> qualities = appTrafficService.getheatOS(user,
					appTraffic);
			int a;
			for (a = 0; a < qualities.size(); a++) {
			}
			
			String topos = JSON.toJSONString(qualities);
			System.out.println(qualities.size()+"  QQQQQQQQQQQQQQQ    "+topos);
			return topos;

	}



	public AppTrafficService getAppTrafficService() {
		return appTrafficService;
	}

	@Resource
	public void setAppTrafficService(AppTrafficService appTrafficService) {
		this.appTrafficService = appTrafficService;
	}

}
