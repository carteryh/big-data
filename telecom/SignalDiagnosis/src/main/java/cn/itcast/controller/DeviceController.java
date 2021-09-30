package cn.itcast.controller;

import javax.annotation.Resource;

import cn.itcast.service.DeviceService;
import org.springframework.stereotype.Controller;

@Controller
public class DeviceController {
	private DeviceService deviceService;
	
	
	
/***************************SET方法***********************************/
@Resource
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	
	
}
