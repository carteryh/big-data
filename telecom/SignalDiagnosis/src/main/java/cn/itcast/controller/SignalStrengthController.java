package cn.itcast.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.itcast.entity.SignalStrength;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import cn.itcast.entity.DefaultConf;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.service.SignalStrengthServce;
import cn.itcast.util.CustomerContextHolder;
import cn.itcast.util.DateFormat;
import cn.itcast.util.DefaultConfUtil;

//信号强度热图
@Controller
public class SignalStrengthController {

	private SignalStrengthServce signalStrengthServce;

	// 锟斤拷锟皆碉拷锟斤拷锟斤拷
	// 娴嬭瘯鐐规�鏁�
	@RequestMapping(value = "gettime")
	public @ResponseBody String getdate(HttpServletRequest request,
			SignalStrength signalStrength) throws SQLException {
		//转换制定的数据源
		String datas="";
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		if (signalStrength.getStartdate()==0) {
		
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();

			signalStrength.setStopdate(Integer.parseInt(defaultConf.getEndDateSignalStrength().substring(0, 8)));
			signalStrength.setStartdate(Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8)));
			signalStrength.setDaytime(dateFormat.showDates(signalStrength.getStartdate(),signalStrength.getStopdate()));
			
			
			
		}
		 datas = JSON.toJSONString(signalStrength);
		return datas;

	}
	/*
	 * 信号强度分布
	 */
	@RequestMapping(value = "loadheatmap")
	public @ResponseBody String getHeadMap(HttpServletRequest request,
			SignalStrength signalStrength, HttpSession session)
			throws SQLException {

		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		List<SignalStrength> signalStrengths = new ArrayList<SignalStrength>();
		// 获取前台传过来的数据
		// 经度最小值
		double bssw_lng = Double.parseDouble(request.getParameter("bssw_lng"));
		// 经度最大值
		double bsne_lng = Double.parseDouble(request.getParameter("bsne_lng"));
		// 纬度最小值
		double bssw_lat = Double.parseDouble(request.getParameter("bssw_lat"));
		// 纬度最大值
		double bsne_lat = Double.parseDouble(request.getParameter("bsne_lat"));
		
		
		User user = (User) session.getAttribute("user");

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String data = request.getParameter("daytime");
		
		if (data==""){
			
			
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();

			signalStrength.setStopdate(Integer.parseInt(defaultConf.getEndDateSignalStrength().substring(0, 8))*100);
			signalStrength.setStartdate(Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8))*100);
			signalStrength.setDaytime(dateFormat.showDates(Integer.parseInt(defaultConf.getEndDateSignalStrength().substring(0, 8))*100,Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8))*100));
	
		}
		
		String startdatas = "";
		String stopdatas = "";
		int oneday = 0;
		int startdate = 0;
		int stopdate = 0;
		if (data.contains("-")) {
			startdatas = data.substring(0, data.indexOf("-")).trim();
			stopdatas = data.substring(data.indexOf("-") + 1, data.length())
					.trim();
			signalStrength.setIsoneday(false);
			// 查询的开始时间
			startdate = Integer.parseInt(df.format(new Date(startdatas)))*100;
			// 查询的结束时间
			stopdate = Integer.parseInt(df.format(new Date(stopdatas)))*100;
			signalStrength.setStartdate(startdate);
			signalStrength.setStopdate(stopdate);
		} else if((!data.contains("-"))&&(!data.equals("")))  {
			signalStrength.setIsoneday(true);
			oneday = Integer.parseInt(df.format(new Date(data)))*100;
		}
		// 不是admin ,查询的运营商.
		String networkname = request.getParameter("nwOperator");
		// 查询的网络制式
		String network_type = request.getParameter("networktype");
		// 查询的省
		String province = "";
		// 查询的市
		String city = "";
		// 将前台或得到的值添加进对象属性内
		signalStrength.setNetworkname(networkname);
		signalStrength.setNetworktype(network_type);
		signalStrength.setOneday(oneday);
		signalStrength.setProvince(province);
		signalStrength.setCity(city);
		// 如果是admin用户，将“admin”添加进user属性内
		if (user.getRole().equalsIgnoreCase("admin")) {
			signalStrength.setUser("admin");
		}
		// 如果不是admin用户，将真实用户名称添加进user属性内
		else {
			signalStrength.setUser(user.getNWOperator());
		}
		// 将对应的数据传到service层
		System.out.println(signalStrength.getStopdate());
		System.out.println(signalStrength.getStartdate());
		signalStrengths = SignalStrengthServce.getRssiHeatData(bssw_lng,
				bsne_lng, bssw_lat, bsne_lat, signalStrength);
		// 将数据转换为JSONString
		String datas = JSON.toJSONString(signalStrengths);
		//System.out.println(datas);
		
		return datas;
	}

	
	/*
	 * 个人信号强度热图
	 */
	

	@RequestMapping(value = "oneloadheatmap")
	public @ResponseBody String getoneHeadMap(HttpServletRequest request,
			SignalStrength signalStrength, HttpSession session)
			throws SQLException {

		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		List<SignalStrength> signalStrengths = new ArrayList<SignalStrength>();
		// 获取前台传过来的数据
		// 经度最小值
		double bssw_lng = Double.parseDouble(request.getParameter("bssw_lng"));
		// 经度最大值
		double bsne_lng = Double.parseDouble(request.getParameter("bsne_lng"));
		// 纬度最小值
		double bssw_lat = Double.parseDouble(request.getParameter("bssw_lat"));
		// 纬度最大值
		double bsne_lat = Double.parseDouble(request.getParameter("bsne_lat"));
		
		
		User user = (User) session.getAttribute("user");

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String data = request.getParameter("daytime");
		 	
		if (data==""){
	
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();

			signalStrength.setStopdate(Integer.parseInt(defaultConf.getEndDateSignalStrength().substring(0, 8))*10);
			signalStrength.setStartdate(Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8))*10);
			signalStrength.setDaytime(dateFormat.showDates(Integer.parseInt(defaultConf.getEndDateSignalStrength().substring(0, 8))*10,Integer.parseInt(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDayAppTraffic()).substring(0, 8))*10));

		}
		 
		
		String startdatas = "";
		String stopdatas = "";
		int oneday = 0;
		int startdate = 0;
		int stopdate = 0;
		if (data.contains("-")) {
			startdatas = data.substring(0, data.indexOf("-")).trim();
			stopdatas = data.substring(data.indexOf("-") + 1, data.length())
					.trim();
			signalStrength.setIsoneday(false);
			// 查询的开始时间
			startdate = Integer.parseInt(df.format(new Date(startdatas)))*100;
			// 查询的结束时间
			stopdate = Integer.parseInt(df.format(new Date(stopdatas)))*100;
			signalStrength.setStartdate(startdate);
			signalStrength.setStopdate(stopdate);
		} else if((!data.contains("-"))&&(!data.equals("")))  {
			signalStrength.setIsoneday(true);
			oneday = Integer.parseInt(df.format(new Date(data)))*100;
		}
	 
		// 不是admin ,查询的运营商.
		String networkname = request.getParameter("nwOperator");
		// 查询的网络制式
		String network_type = request.getParameter("networktype");
		// 查询的省
		String province = "";
		// 查询的市
		String city = "";
		// 将前台或得到的值添加进对象属性内
		signalStrength.setNetworkname(networkname);
		signalStrength.setNetworktype(network_type);
		signalStrength.setOneday(oneday);
		signalStrength.setProvince(province);
		signalStrength.setCity(city);
		// 如果是admin用户，将“admin”添加进user属性内
		if (user.getRole().equalsIgnoreCase("admin")) {
			signalStrength.setUser("admin");
		}
		// 如果不是admin用户，将真实用户名称添加进user属性内
		else {
			signalStrength.setUser(user.getNWOperator());
		}
		 	// 将对应的数据传到service层
		signalStrengths = SignalStrengthServce.getRssiHeatData(bssw_lng,
				bsne_lng, bssw_lat, bsne_lat, signalStrength);
		// 将数据转换为JSONString
		String datas = JSON.toJSONString(signalStrengths);
		return datas;
	}
	
	
	
	// Landmark信号强度跟踪图
	public ModelAndView getSignalStrengthTrack(HttpSession session,
			SignalStrength signalStrength) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (signalStrength.getDaytime() == null) {
				DateFormat dateFormat = new DateFormat();
				signalStrength.setEndDate(dateFormat.getMonthAgo_end());
				signalStrength.setStartDate(20141001);
				signalStrength.setDaytime(dateFormat.showDate(
						signalStrength.getStartDate(), signalStrength.getEndDate()));
			}
			signalStrength.setLandmark("");
			signalStrength.setNetworkname(VarDesc.OPERATOR.ALL);
			signalStrength.setNetworktype(VarDesc.NETWORKTYPE.ALL);
			String optionStr = signalStrengthServce.signalStrengthTrack(signalStrength, user);
			ModelAndView modelAndView = new ModelAndView("view/SignalCoverage/signalStrengthTrack");
			modelAndView.addObject("optionStr", optionStr);
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}
	
	
 
	@SuppressWarnings("deprecation")
	// 表示不检测过期的方法
	@RequestMapping(value = "signalStrength_getSignalStrengthTracks")
	public ModelAndView getSignalStrengthTracks(HttpServletRequest request,
			HttpSession session, SignalStrength signalStrength) {
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		DateFormat dateFormat = new DateFormat();

		DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
		if (signalStrength.getEndDateToSecond() == null) {
			signalStrength.setEndDate(Integer.valueOf(defaultConf.getEndDateSignalStrength()));
		} else {
			signalStrength.setEndDate(dateFormat.convertDateToSecondInt(signalStrength.getEndDateToSecond()));
		}
		if (signalStrength.getStartDateToSecond() == null) {
			signalStrength.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDaySignalStrength())));
		} else {
			signalStrength.setStartDate(dateFormat.convertDateToSecondInt(signalStrength.getStartDateToSecond()));
		}
		
		
		
		User user = (User) session.getAttribute("user");
		String optionStr = signalStrengthServce.getSignalStrengthTracks(signalStrength,user);
		
		ModelAndView modelAndView = new ModelAndView("view/SignalCoverage/signalStrengthTracks");
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("landmark", signalStrength.getLandmark());
		modelAndView.addObject("nwType", signalStrength.getNetworktype());
		modelAndView.addObject("startDateToSecond", dateFormat.showDateToSecond(signalStrength.getStartDate()));
		modelAndView.addObject("endDateToSecond",dateFormat.showDateToSecond(signalStrength.getEndDate()));
		modelAndView.addObject("x_type",signalStrength.getX_type());
		return modelAndView;
	}
	
	

 
	@SuppressWarnings("deprecation")
	// 表示不检测过期的方法
	@RequestMapping(value = "signalStrength_getSignalStrengthStatistics")
	public ModelAndView getSignalStrengthStatistics(HttpServletRequest request,
			HttpSession session, SignalStrength signalStrength) {
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);
		
		if (signalStrength.getDaytime() == null) {
			DateFormat dateFormat = new DateFormat();
			DefaultConf defaultConf=DefaultConfUtil.newInstance().getDefaultconf();
			signalStrength.setEndDate(Integer.valueOf(defaultConf.getEndDateSignalStrength().substring(0, 8)));
			signalStrength.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateSignalStrength(), defaultConf.getIntervalDaySignalStrength()).substring(0, 8)));
			signalStrength.setDaytime(dateFormat.showDate(signalStrength.getStartDate(), signalStrength.getEndDate()));
		}else {
			DateFormat dateFormat = new DateFormat(signalStrength.getDaytime());
			signalStrength.setStartDate(dateFormat.getStartDate());
			signalStrength.setEndDate(dateFormat.getEndDate());
		}
	
		User user = (User) session.getAttribute("user");
		String optionStr = signalStrengthServce.getSignalStrengthStatistics(signalStrength,user);
		System.out.println("options#######################      "+optionStr);
		ModelAndView modelAndView = new ModelAndView("view/SignalCoverage/signalStrengthStatistics");
		modelAndView.addObject("daytime", signalStrength.getDaytime());
		modelAndView.addObject("optionStr", optionStr);
		modelAndView.addObject("nwOperator", signalStrength.getNetworkname());
		
		return modelAndView;
	}
	
	

	/***************************** set/get方法 **************************************/
	public SignalStrengthServce getSignalStrengthServce() {
		return signalStrengthServce;
	}

	@Resource
	public void setSignalStrengthServce(
			SignalStrengthServce signalStrengthServce) {
		this.signalStrengthServce = signalStrengthServce;
	}

}
