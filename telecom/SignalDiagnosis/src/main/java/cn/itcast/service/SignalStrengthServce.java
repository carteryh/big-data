package cn.itcast.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.SignalStrengthDao;
import cn.itcast.entity.SignalStrength;
import org.springframework.stereotype.Component;

import cn.itcast.echarts.EchartsBar;
import cn.itcast.echarts.EchartsLine;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;

@Component
public class SignalStrengthServce {
	private static SignalStrengthDao signalStrengthDao;
	private EchartsBar echartsBar;
	private EchartsLine echartsLine;
	

	//信号强度热图
	public static List<SignalStrength> getRssiHeatData(double smalllng, double biglng, double smalllat, double boglat , SignalStrength signalStrength) throws SQLException {
		
		return signalStrengthDao.getRssiHeatData(smalllng,biglng,smalllat,boglat, signalStrength);
			
	}
	
	// 获取网络制式列表
	public static List<String> getnetworktype(String  network_name) throws SQLException {
		
		return signalStrengthDao.getnetworktype(network_name);
			
	}

	//鏄剧ず鎵�湁鐨勫湴鐞嗘祴璇曠偣
	public List<SignalStrength> showAllPointGeog(List<SignalStrength> signalStrength) {
		return signalStrengthDao.showAllPointGeog(signalStrength);
	}
	
	
	public SignalStrengthDao getSignalStrengthDao() {
		return signalStrengthDao;
	}
	
	/**
	 *landmark信号强度跟踪图
	 */
	public String getSignalStrengthTracks(SignalStrength signalStrength,
			User user) {
		if(signalStrengthDao.getSignalStrengthTracks(signalStrength,user)!=null){
			List<SignalStrength> signalStrengths = (List<SignalStrength>) signalStrengthDao.getSignalStrengthTracks(signalStrength,user);
			String optionStr = echartsLine.getSignalStrengthTracks(signalStrengths,signalStrength.getX_type());
			return optionStr;
		}else{
			return VarDesc.NODATA;
		}
	}
	
	
	
	/**
	 *典型地标信号强度统计直方图
	 */
	public String getSignalStrengthStatistics(SignalStrength signalStrength,
			User user) {
				/*********************************参数测试*********************************/
				if(signalStrengthDao.getSignalStrengthStatistics(signalStrength,user)!=null){
					List<SignalStrength> signalStrengths = (List<SignalStrength>) signalStrengthDao.getSignalStrengthStatistics(signalStrength,user);
					String optionStr = echartsBar.signalStrengthStatistics(signalStrengths);
					return optionStr;
				}else{
					return VarDesc.NODATA;
				}
	}
	//Landmark信号强度跟踪图（曲线图）
	public String signalStrengthTrack(SignalStrength signalStrength,User user) {
		//先获取5个LandmarkName
		List<SignalStrength> landmarkNames = signalStrengthDao.getLandmarkName();
		for (SignalStrength landmarkName : landmarkNames) {
			signalStrength.setLandmark(landmarkName.getLandmark());
			List<SignalStrength> signalStrengths = signalStrengthDao.getSignalStrengthTrack(signalStrength, user);
		}
		return null;
	}

	
	
	
	/****************************SET方法存放处**************************************/
	@Resource
	public void setSignalStrengthDao(SignalStrengthDao signalStrengthDao) {
		this.signalStrengthDao = signalStrengthDao;
	}

	@Resource
	public void setEchartsBar(EchartsBar echartsBar) {
		this.echartsBar = echartsBar;
	}
	@Resource
	public void setEchartsLine(EchartsLine echartsLine) {
		this.echartsLine = echartsLine;
	}

	


	
}
