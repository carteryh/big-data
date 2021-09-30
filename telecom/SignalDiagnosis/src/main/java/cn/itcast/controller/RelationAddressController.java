package cn.itcast.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.service.RelationAddressService;
import cn.itcast.util.CustomerContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.RelationAddress;
import cn.itcast.entity.User;

@Controller
public class RelationAddressController {

	private RelationAddressService relationAddressService;

	
	public RelationAddressService getRelationAddressService() {
		return relationAddressService;
	}

	@Resource
	public void setRelationAddressService(
			RelationAddressService relationAddressService) {
		this.relationAddressService = relationAddressService;
	}
	
	
	@RequestMapping(value="/showAllAddress")
	public ModelAndView showAllAddress(HttpServletRequest request,RelationAddress relationAddress){
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user=(User) request.getSession().getAttribute("user");
		List<RelationAddress> addresses=relationAddressService.showAllAddress(user);
		List<NavigationAddress> navigationAddresses=relationAddressService.showAllNavAddresses(user);
		
		ModelAndView modelAndView=new ModelAndView("view/AddressManager/LinkAddressManager");
		
		modelAndView.addObject("addresses", addresses);
		modelAndView.addObject("navigationAddresses", navigationAddresses);
		return modelAndView;
	}
	
	@RequestMapping(value="/deleteAdress")
	public @ResponseBody String deleteAdress(HttpServletRequest request,RelationAddress relationAddress){
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user=(User) request.getSession().getAttribute("user");
		Integer rs=relationAddressService.deleteAdress(user,relationAddress);
		return rs.toString();
	}
	@RequestMapping(value="/addAdress")
	public  String addAdress(HttpServletRequest request,RelationAddress relationAddress){
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user=(User) request.getSession().getAttribute("user");
		
		System.out.println(relationAddress.getName()+" "+relationAddress.getAddress()+" "+relationAddress.getCategory());
		int rs=relationAddressService.addAdress(user,relationAddress);
		
		if (rs==1) {
			return "redirect:/showAllAddress";
		}
		return "redirect:/timeout.jsp";
	}
	
	@RequestMapping(value="/editRelaAddress")
	public  String editRelaAddress(HttpServletRequest request,RelationAddress relationAddress){
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user=(User) request.getSession().getAttribute("user");
		
		int rs=relationAddressService.editRelaAddress(user,relationAddress);
		
		if (rs==1) {
			return "redirect:/showAllAddress";
		}
		return "redirect:/timeout.jsp";
	}
	
	
	
	@RequestMapping(value="/deleteNaviAdress")
	public  String deleteNaviAdress(HttpServletRequest request,NavigationAddress navigationAddress){
		//转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user=(User) request.getSession().getAttribute("user");

		int rs=relationAddressService.deleteNaviAdress(user,navigationAddress);
		
		if (rs==1) {
			return "redirect:/showAllAddress";
		}
		return "redirect:/timeout.jsp";
	}
}
