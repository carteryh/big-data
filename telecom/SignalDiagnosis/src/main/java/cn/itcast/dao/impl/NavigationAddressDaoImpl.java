package cn.itcast.dao.impl;

import cn.itcast.dao.NavigationAddressDao;
import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("navigationAddressDao")
public class NavigationAddressDaoImpl implements NavigationAddressDao {

	
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int addNaviAddress(User user, NavigationAddress navigationAddress) {
		System.out.println(navigationAddress.getNvaName()+"###################");
		
		int rs=jdbcTemplate.update("insert into navigation_address(name,credate,uid)  values(?,?,?)", new Object[]{navigationAddress.getNvaName(),navigationAddress.getCredate(),user.getId()});
		System.out.println(rs+"&&&&&&&&&&&&&&&&&&&&&&&&");
		return rs;
	}
}
