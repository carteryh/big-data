package cn.itcast.dao;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.entity.SignalStrength;
import cn.itcast.entity.User;

public interface SignalStrengthDao {

	// SignalStrength总数
	public long getSignalStrengthCount(User user);

	// 信号强度图形
	public List<SignalStrength> getRssiHeatData(double smalllng, double biglng,
                                                double smalllat, double boglat, SignalStrength signalStrength)
			throws SQLException;

	// 获取网络制式
	public List<String> getnetworktype(String network_name) throws SQLException;

	public List<SignalStrength> showAllPointGeog(
			List<SignalStrength> SignalStrengths);

	/**
     landmark网络质量统计直方图
 	 */
	public List<SignalStrength> getSignalStrengthStatistics(
			SignalStrength signalStrength, User user);

	// 信号强度跟踪图（曲线图）获取前5Landmark
	public List<SignalStrength> getLandmarkName();

	// 信号强度跟踪图(曲线图)
	public List<SignalStrength> getSignalStrengthTrack(SignalStrength ss,
			User user);

	/**
	 *  landmark信号强度跟踪图
 	 */
	public Object getSignalStrengthTracks(SignalStrength signalStrength,
			User user);

	
}
