package cn.itcast.dao;

import java.util.List;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.RelationAddress;
import cn.itcast.entity.User;

public interface RelationAddressDao {

	List<RelationAddress> showAllAddress(User user);

	List<NavigationAddress> showAllNavAddresses(User user);

	int deleteAdress(User user, RelationAddress relationAddress);

	int addAdress(User user, RelationAddress relationAddress);

	int editRelaAddress(User user, RelationAddress relationAddress);

	int deleteNaviAdress(User user, NavigationAddress navigationAddress);

}
