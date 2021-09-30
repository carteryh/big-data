package cn.itcast.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.entity.DataConnection;
import cn.itcast.entity.DefaultConf;
import cn.itcast.entity.User;
import cn.itcast.service.DataConnectionService;
import cn.itcast.util.CustomerContextHolder;
import cn.itcast.util.DateFormat;
import cn.itcast.util.DefaultConfUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

@Controller
public class DataConnectionController {
	private DataConnectionService dataConnectionService;

	// 数据连接成功率统计图
	@RequestMapping(value = "dataConnectionStatistics")
	public ModelAndView getDataConnectionStatistics(
			DataConnection dataConnection, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			//CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

			// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
			if (dataConnection.getTime_index_client() == null) {
				DateFormat dateFormat = new DateFormat();
				DefaultConf defaultConf= DefaultConfUtil.newInstance().getDefaultconf();
				dataConnection.setEndDate(Integer.valueOf(defaultConf.getEndDateDateConnection().substring(0, 8)));
				dataConnection.setStartDate(Integer.valueOf(dateFormat.getStartDateByDefaultConf(defaultConf.getEndDateDateConnection(), defaultConf.getIntervalDayDateConnection()).substring(0, 8)));
				dataConnection.setTime_index_client(dateFormat.showDate(dataConnection.getStartDate(), dataConnection.getEndDate()));
			}
			dataConnection.setNetwork_name("ALL");
			ModelAndView modelAndView = new ModelAndView(
					"view/NetworkQuality/dataConnectionStatistics");
			String optionStr = dataConnectionService.getDataConnectionStatistics(dataConnection, user);
			modelAndView.addObject("optionStr", optionStr);
			modelAndView.addObject("dayTime", dataConnection.getTime_index_client());
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}

	// 通过Ajax触发数据连接成功率统计图
	@RequestMapping(value = "dataConnectionStatistics_control")
	public @ResponseBody String getDataConnStatisticsControl(
			HttpServletResponse response, DataConnection dataConnection,
			HttpSession session) throws IOException {
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
			if (dataConnection.getTime_index_client() != null) {
				// 通过筛选条件传参，分割daytime
				DateFormat dateFormat = new DateFormat(
						dataConnection.getTime_index_client());
				dataConnection.setStartDate(dateFormat.getStartDate());
				dataConnection.setEndDate(dateFormat.getEndDate());
			}
			String optionStr = dataConnectionService
					.getDataConnectionStatistics(dataConnection, user);
			return optionStr;
		}
		return "redirect:/timeout.jsp";
	}

	/*
	 * 下面是get、set方法
	 */

	public DataConnectionService getDataConnectionService() {
		return dataConnectionService;
	}

	@Resource
	public void setDataConnectionService(
			DataConnectionService dataConnectionService) {
		this.dataConnectionService = dataConnectionService;
	}

	// 数据连接点分布图
	/*
	 * 有红蓝点分布
	 */
	@RequestMapping(value = "showPointConnDist")
	public ModelAndView showPointConnDist(HttpServletRequest request,
			DataConnection dataConnection) {

		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_PHOENIX);

		//CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user = (User) request.getSession().getAttribute("user");
		//链接成功的数据
		List<DataConnection> connections = dataConnectionService
				.showPointConnDist(user, dataConnection);
		//链接失败的数据
		List<DataConnection> connectionFails = dataConnectionService
				.showPointConnDistFails(user, dataConnection);

	
		List<DataConnection> nwType = null;
		if (!("admin".trim().toLowerCase()).equals(user.getRole())) {
			nwType = dataConnectionService.showAcquireNwType(user,
					dataConnection);
		}

		ModelAndView modelAndView = new ModelAndView(
				"view/NetworkQuality/pointConnDist");
		String strnwQualities = JSON.toJSONString(connections);
		String strnwConnectionFails = JSON.toJSONString(connectionFails);
		String strnwType = JSON.toJSONString(nwType);

		modelAndView.addObject("strnwQualities", strnwQualities);
		modelAndView.addObject("strnwConnectionFails", strnwConnectionFails);
		modelAndView.addObject("strnwType", strnwType);
		modelAndView.addObject("dataConnection", dataConnection);
		return modelAndView;
	}

	// 获取运营商级联的网络制式
	@RequestMapping(value = "showAcquireNwType")
	public @ResponseBody String showAcquireNwType(HttpServletRequest request,
			DataConnection dataConnection) {
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user = (User) request.getSession().getAttribute("user");
		List<DataConnection> connecs = dataConnectionService.showAcquireNwType(
				user, dataConnection);
		String conns = JSON.toJSONString(connecs);
		return conns;
	}
}
