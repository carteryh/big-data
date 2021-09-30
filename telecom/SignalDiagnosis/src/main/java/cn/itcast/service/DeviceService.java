package cn.itcast.service;

import javax.annotation.Resource;

import cn.itcast.dao.DeviceDao;
import cn.itcast.echarts.EchartsBar;
import org.springframework.stereotype.Component;

@Component
public class DeviceService {
private DeviceDao deviceDao;
private EchartsBar echartsBar;
	


	
/****************************SET方法***********************************/
	@Resource
	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	@Resource
	public void setEchartsBar(EchartsBar echartsBar) {
	this.echartsBar = echartsBar;
	}



}
