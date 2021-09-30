package cn.itcast.dao;

import java.util.List;

import cn.itcast.entity.DataConnection;
import cn.itcast.entity.User;

public interface DataConnectionDao {
	// 获取DataConnection总数
	public long getDataConnectionCount(User user);

	// 数据连接成功率统计图(成功)
	public List<DataConnection> getDataConnectionStatistics_success(
			DataConnection dataConnection, User user);

	// 数据连接率统计图（所有）
	public List<DataConnection> getDataConnectionStatistics_all(
			DataConnection dataConnection, User user);

	// 筛所包含的选网络制式
	public List<DataConnection> getMobileNetworkType();

	//数据连接点分布图
	public List<DataConnection> showPointConnDist(User user, DataConnection dataConnection);
	//获取运营商级联的网络制式
	public List<DataConnection> showAcquireNwType(User user, DataConnection dataConnection);
	//数据连接失败点分布图
	public List<DataConnection> showPointConnDistFails(User user,
			DataConnection dataConnection);

}
