package cn.itcast.dao;

import java.util.List;

import cn.itcast.entity.NWQuality;
import cn.itcast.entity.TableCounts;
import cn.itcast.entity.User;

public interface NWQualityDao {

	// NWQuality总数
	public long getNWQualityCount(User user);

	// NWQuality Signal_Strength DataConnection 三表总数
	public List<TableCounts> getTableCounts(User user);

	// 显示所有点的地理坐标
	public List<NWQuality> showAllPointGeog(User user, NWQuality nwQuality);

	// 连接点数排名
	public List<NWQuality> getPointRank(NWQuality nwQuality, User user);

	// 网络速率排名
	public List<NWQuality> getNetworkRateRank(NWQuality nwQuality);

	// APP流量排名
	public List<NWQuality> getTerminalTrafficRateRank(NWQuality nwQuality,
			User user);

	// 网络质量统计直方图
	public List<NWQuality> getNetworkQualityStatistics(NWQuality nwQuality,
			User user);
	// 网络质量瓷砖图
	public List<Object> showNwQuaTileSpeed(User user, NWQuality nwQuality);

	// 热门手机网络质量排名
	public List<NWQuality> getMobileNetworkQualityRank2D(NWQuality nwQuality,User user);
	public List<NWQuality> getMobileNetworkQualityRank(NWQuality nwQuality,User user);

	// 热门手机网络质量排名(时间段内总条数)
	public int getMobileNQRankCounts2D(NWQuality nwQuality, User user);
	public int getMobileNQRankCounts(NWQuality nwQuality, User user);

	// 典型地标网络质量统计图
	public List<NWQuality> getLandmarkNetworkQualityStatistics(
			NWQuality nwQuality, User user);

	// 手机网络质量瓷砖图
	public List<Object> showTePhNwQuaTileMap(User user, NWQuality nwQuality);

	// 查询NWQuality手机型号类型
	public List<NWQuality> showTePhNwQuaType();

	/**
	 *  LandMark网络质量跟踪图
	 */
	public List<NWQuality> getNetworkQualityTracks(NWQuality nwQuality,
			User user);

	//用户瓷砖图
	public List<NWQuality> showAllUserPointTileGeog(User user, NWQuality nwQuality);

	// 测点地图用户总览
	/*public List<NWQuality> showAllUserPointGeog(User user, NWQuality nwQuality);*/
}
