package cn.itcast.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.service.NavigationAddressService;
import cn.itcast.service.RelationAddressService;
import cn.itcast.util.CustomerContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.User;

@Controller
public class NavigationAddressController {

	private NavigationAddressService addressService;
	private RelationAddressService relationAddressService;

	
	public RelationAddressService getRelationAddressService() {
		return relationAddressService;
	}

	@Resource
	public void setRelationAddressService(
			RelationAddressService relationAddressService) {
		this.relationAddressService = relationAddressService;
	}
	public NavigationAddressService getAddressService() {
		return addressService;
	}

	@Resource
	public void setAddressService(NavigationAddressService addressService) {
		this.addressService = addressService;
	}

	@RequestMapping(value="/addNaviAddress")
	public  @ResponseBody List<NavigationAddress> addNaviAddress(HttpServletRequest request,NavigationAddress navigationAddress){
		try {
			request.setCharacterEncoding("utf-8");
			
			//转换制定的数据源
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

			User user=(User)request.getSession().getAttribute("user");
			System.out.println(navigationAddress.getNvaName());
			int rs=addressService.addNaviAddress(user,navigationAddress);
			System.out.println(rs);

			List<NavigationAddress> navigationAddresses=relationAddressService.showAllNavAddresses(user);
			return navigationAddresses;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
