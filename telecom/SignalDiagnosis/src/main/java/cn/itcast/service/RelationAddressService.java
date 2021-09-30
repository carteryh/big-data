package cn.itcast.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.RelationAddressDao;
import org.springframework.stereotype.Component;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.RelationAddress;
import cn.itcast.entity.User;

@Component("relationAddressService")
public class RelationAddressService {

	
	private RelationAddressDao relationAddressDao;
	
	public RelationAddressDao getRelationAddressDao() {
		return relationAddressDao;
	}

	@Resource
	public void setRelationAddressDao(RelationAddressDao relationAddressDao) {
		this.relationAddressDao = relationAddressDao;
	}

	public List<RelationAddress> showAllAddress(User user) {
		return relationAddressDao.showAllAddress(user);
	}

	public List<NavigationAddress> showAllNavAddresses(User user) {
		// TODO Auto-generated method stub
		return relationAddressDao.showAllNavAddresses(user);
	}

	public int deleteAdress(User user, RelationAddress relationAddress) {
		return relationAddressDao.deleteAdress(user,relationAddress);
	}

	public int addAdress(User user, RelationAddress relationAddress) {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		relationAddress.setCredate(dateFormat.format(new Date()));
		return relationAddressDao.addAdress(user,relationAddress);
	}

	public int editRelaAddress(User user, RelationAddress relationAddress) {
		
		return relationAddressDao.editRelaAddress(user,relationAddress);
	}

	public int deleteNaviAdress(User user, NavigationAddress navigationAddress) {
		
		return relationAddressDao.deleteNaviAdress(user,navigationAddress);
	}

}
