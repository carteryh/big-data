package cn.itcast.echarts;

import java.util.List;

import cn.itcast.entity.NWQuality;
import cn.itcast.entity.SignalStrength;
import cn.itcast.util.EchartsUtil;
import org.springframework.stereotype.Component;

import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.VarDesc;

@Component
public class EchartsLine {
	// Landmark信号强度跟踪图（曲线图）
	public String signalStrengthTrack() {

		return null;
	}

	/**
	 *  LandMark网络质量跟踪图
	 */
	public String networkQualityTracks(List<NWQuality> nwQualities, String x_type, String speedType) {
		String option = null;
		try {
			EchartsUtil<NWQuality> echartsUtil = new  EchartsUtil<NWQuality>();
			/*
			 *@param:objects带泛型的list对象	 
			 *@param:UnitType，y轴的单位是AVG，或者是SUM
			 *@param:x_type曲线图x轴按照月、日、小时的类型		
			 *@param:m_x_g，对应x轴clazz中的get方法名
			 *@param:m_y_g，对应y轴clazz中的get方法名
			 *@param:m_z_g，对应z轴clazz中的get方法名
			 */
			if(speedType.equals(VarDesc.SPEEDTYPE.LATENCY)){
				option=echartsUtil.getEchartsLineDWMS(nwQualities, "典型地标网络质量跟踪图", x_type,EchartsUtil.UNIT.AVG, "getX_rate", "getSpeed", "getNwOperator");
			}else{
				option=echartsUtil.getEchartsLine(nwQualities, "典型地标网络质量跟踪图", x_type,EchartsUtil.UNIT.AVG, "getX_rate", "getSpeed", "getNwOperator");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}
	/**
	 * 信号强度跟踪图
	 */
	public String getSignalStrengthTracks(List<SignalStrength> signalStrengths,
			String x_type) {
		String option = null;
		try {
			EchartsUtil<SignalStrength> echartsUtil = new  EchartsUtil<SignalStrength>();
			option=echartsUtil.getEchartsLineSST(signalStrengths, "典型地标信号强度跟踪图", x_type,EchartsUtil.UNIT.DBM, "getX_rate", "getRssi", "getNetworkname");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}
	/**
	 *  热门App流量跟踪图
	 */
	public String getAppTrafficTracks(List<AppTraffic> appTraffics,String x_type) {
		/*for (AppTraffic appTraffic : appTraffics) {
			System.out.println(appTraffic.getApp_name()+"\t\t"+appTraffic.getX_rate()+"\t\t"+appTraffic.getSpeed());
		}*/
		String option = null;
		try {
			EchartsUtil<AppTraffic> echartsUtil = new  EchartsUtil<AppTraffic>();
			option=echartsUtil.getEchartsLine(appTraffics, "热门App流量跟踪图", x_type,EchartsUtil.UNIT.SUM, "getX_rate", "getSpeed", "getApp_name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}

}
