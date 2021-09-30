package cn.itcast.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import cn.itcast.dao.NavigationAddressDao;
import org.springframework.stereotype.Component;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.User;

@Component("addressService")
public class NavigationAddressService {
	private NavigationAddressDao navigationAddressDao;
	
	
	public NavigationAddressDao getNavigationAddressDao() {
		return navigationAddressDao;
	}
	@Resource
	public void setNavigationAddressDao(NavigationAddressDao navigationAddressDao) {
		this.navigationAddressDao = navigationAddressDao;
	}


	public int addNaviAddress(User user, NavigationAddress navigationAddress) {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		navigationAddress.setCredate(dateFormat.format(new Date()));
		return navigationAddressDao.addNaviAddress(user,navigationAddress);
	}

}
