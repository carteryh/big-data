package cn.itcast.dao.impl;

import javax.annotation.Resource;

import cn.itcast.dao.DeviceDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component(value = "deviceDao")
public class DeviceDaoImpl implements DeviceDao {
	private JdbcTemplate jdbcTemplate;

	
/*********************************set方法****************************************/
@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


}
