package cn.itcast.dao;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.User;

public interface NavigationAddressDao {

	int addNaviAddress(User user, NavigationAddress navigationAddress);

}
