package cn.itcast.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.NWQualityDao;
import cn.itcast.dao.SignalStrengthDao;
import cn.itcast.entity.NWQuality;
import cn.itcast.util.DateFormat;
import cn.itcast.util.DefaultConfUtil;
import org.springframework.stereotype.Component;

import cn.itcast.dao.DataConnectionDao;
import cn.itcast.echarts.EchartsBar;
import cn.itcast.echarts.EchartsLine;
import cn.itcast.echarts.EchartsPie;
import cn.itcast.entity.TableCounts;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;

@Component
public class NWQualityService {
	private NWQualityDao nwQualityDao;
	private SignalStrengthDao signalStrengthDao;
	private EchartsBar echartsBar;
	private EchartsLine echartsLine;
	private EchartsPie echartsPie;
	private DataConnectionDao dataConnectionDao;


	// 获取NWQuality总数
	public Long getNWQualityCount(User user) {
		return nwQualityDao.getNWQualityCount(user);
	}

	// 获取Signal Strength总数
	public Long getSignalStrengthCount(User user) {
		return signalStrengthDao.getSignalStrengthCount(user);
	}

	// 获取DataConnection总数
	public Long getDataConnectionCount(User user) {
		return dataConnectionDao.getDataConnectionCount(user);
	}
	// NWQuality Signal_Strength DataConnection 三表总数
	public List<TableCounts> getTableCounts(User user) {
		return nwQualityDao.getTableCounts(user);
	}

	/*
	 *  显示所有的地理连接点
	 */
	@SuppressWarnings("deprecation")
	public List<NWQuality> showAllPointGeog(User user, NWQuality nwQuality) {
		// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");

		
		String strdate= DefaultConfUtil.newInstance().getDefaultconf().getEndDateNwquality();

		
		Date date=null;
		if (nwQuality.getDaytime() == null||nwQuality.getDaytime() == "") {
 
			try {
				
				date=sf.parse(strdate);
				long temp=7 * 24 * 60 * 60 * 1000l;
				long lDate=date.getTime()-temp;
				

				nwQuality.setStartDate(Integer.parseInt(sf.format(lDate)));
				nwQuality.setEndDate(Integer.parseInt(sf.format(date)));


				sf = new SimpleDateFormat("MM/dd/yyyy");
				nwQuality.setDaytime(sf.format(lDate) + "-"+ sf.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {

			if (nwQuality.getDaytime().contains("-")) {

				// 通过筛选条件传参，分割daytime
				String[] str = nwQuality.getDaytime().split("-");

				nwQuality.setStartDate(Integer.parseInt(sf.format(new Date(
						str[0]))));
				nwQuality.setEndDate(Integer.parseInt(sf
						.format(new Date(str[1]))));
			} else {
				// 通过筛选条件传参，分割daytime
				nwQuality.setStartDate(Integer.parseInt(sf.format(new Date(
						nwQuality.getDaytime()))));
				nwQuality.setEndDate(Integer.parseInt(sf.format(new Date(
						nwQuality.getDaytime()))));
			}
		}

		return nwQualityDao.showAllPointGeog(user, nwQuality);
	}

	// 连接点排名
	public String getPointRank(NWQuality nwQuality, User user) {
		List<NWQuality> nwQualities = nwQualityDao
				.getPointRank(nwQuality, user);
		String optionStr = echartsBar.TestOptionRank(nwQualities);
		return optionStr;
	}
	
	/**
	 *网络速率排名
	 */
	public String getNetworkRateRank(NWQuality nwQuality) {
		if (nwQualityDao.getNetworkRateRank(nwQuality) != null) {
			List<NWQuality> nwQualities = nwQualityDao
					.getNetworkRateRank(nwQuality);

			String optionStr = echartsBar.networkRateRank(nwQualities);
			return optionStr;
		} else {
			return VarDesc.NODATA;
		}
	}

	/**
	 *终端流量排名
	 */
	public String getTerminalTrafficRateRank(NWQuality nwQuality, User user) {

		if (nwQualityDao.getTerminalTrafficRateRank(nwQuality, user) != null) {
			List<NWQuality> nwQualitys = nwQualityDao
					.getTerminalTrafficRateRank(nwQuality, user);

			String optionStr = echartsBar.terminalTrafficRateRank(nwQualitys);
			return optionStr;
		} else {
			return VarDesc.NODATA;
		}

	}

	/**
	 * 网络质量统计直方图
	 */
	
	public String getNetworkQualityStatistics(NWQuality nwQuality, User user) {

		if (nwQualityDao.getNetworkQualityStatistics(nwQuality, user) != null) {
			List<NWQuality> nwQualitys = (List<NWQuality>) nwQualityDao
					.getNetworkQualityStatistics(nwQuality, user);

			String optionStr = echartsBar.networkQualityStatistics(nwQualitys,nwQuality.getSpeedType());
			return optionStr;
		} else {
			return VarDesc.NODATA;
		}
	}
	


	public String getNetworkQualityTracks(NWQuality nwQuality, User user) {

		if (nwQualityDao.getNetworkQualityTracks(nwQuality, user) != null) {
			List<NWQuality> nwQualities = (List<NWQuality>) nwQualityDao
					.getNetworkQualityTracks(nwQuality, user);

			String optionStr = echartsLine.networkQualityTracks(nwQualities,
					nwQuality.getX_type(),nwQuality.getSpeedType());
			return optionStr;
		} else {
			return VarDesc.NODATA;
		}
	}

	/*
	 *  网络质量瓷砖图
	 */
	@SuppressWarnings("deprecation")
	public List<Object> showNwQuaTileSpeed(User user, NWQuality nwQuality) {
		if (!"admin".equals(user.getRole())) {
			nwQuality.setNwOperator(user.getNWOperator());
		}
		// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
		if (nwQuality.getDaytime() == null || nwQuality.getDaytime() == "") {

			Calendar cal1 = Calendar.getInstance();
			// 一个月前的日期
			cal1.add(Calendar.WEEK_OF_MONTH, -1);

			nwQuality.setStartDate(Integer.parseInt(sf.format(cal1.getTime())));

			// 获取当前日期
			Calendar cal2 = Calendar.getInstance();
			nwQuality.setEndDate(Integer.parseInt(sf.format(cal2.getTime())));

			sf = new SimpleDateFormat("MM/dd/yyyy");
			nwQuality.setDaytime(sf.format(cal1.getTime()) + "-"
					+ sf.format(cal2.getTime()));
		} else {
			if (nwQuality.getDaytime().contains("-")) {
				// 通过筛选条件传参，分割daytime
				String[] str = nwQuality.getDaytime().split("-");
				nwQuality.setStartDate(Integer.parseInt(sf.format(new Date(str[0]))));
				nwQuality.setEndDate(Integer.parseInt(sf.format(new Date(str[1]))));
			} else {
				if("allDate".equals(nwQuality.getDaytime())){
					nwQuality.setStartDate(2010010100);
					nwQuality.setEndDate((new DateFormat().getMonthAgo_end())*100);
					nwQuality.setDaytime(new DateFormat().showDate(nwQuality.getStartDate(), nwQuality.getEndDate()));
				}else{
					// 通过筛选条件传参，分割daytime
					nwQuality.setStartDate(Integer.parseInt(sf.format(new Date(
							nwQuality.getDaytime()))));
					nwQuality.setEndDate(Integer.parseInt(sf.format(new Date(
							nwQuality.getDaytime()))));
				}
			}

		}
		return nwQualityDao.showNwQuaTileSpeed(user, nwQuality);
	}

	/*
	 *  手机网络质量瓷砖图
	 */
	public List<Object> showTePhNwQuaTileMap(User user, NWQuality nwQuality) {
		return nwQualityDao.showTePhNwQuaTileMap(user, nwQuality);
	}

	/*
	 *  热门手机网络质量排名（直方图）
	 */
	public String getMobileNetworkQualityRank(NWQuality nwQuality, User user) {
		// 时间段内总条数

		int sum = nwQualityDao.getMobileNQRankCounts2D(nwQuality, user);
		List<NWQuality> nwQualities = nwQualityDao.getMobileNetworkQualityRank2D(nwQuality, user);
		String optionStr = echartsBar
					.MobileNetworkQualityRank2D(nwQuality,nwQualities, sum);
		

		return optionStr;
	}

	/*
	 *  典型地标网络质量统计图
	 */
	public String getLandmarkNetworkQualityStatistics(NWQuality nwQuality,
			User user) {
		List<NWQuality> nwQualities = nwQualityDao
				.getLandmarkNetworkQualityStatistics(nwQuality, user);
		return echartsBar.LandmarkNetworkQualityStatistics(nwQualities);
	}

	/*
	 *  查询手机类型
	 */
	public List<NWQuality> showTePhNwQuaType() {

		return nwQualityDao.showTePhNwQuaType();
	}

	/*
	 * 用户瓷砖图
	 */
	public List<NWQuality> showAllUserPointTileGeog(User user, NWQuality nwQuality) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String strStartTime = nwQuality.getStartDateToSecond();
		String strEndTime = nwQuality.getEndDateToSecond();

		if (strStartTime == null || strStartTime == "" || strEndTime == null
				|| strEndTime == "") {

			Calendar cal1 = Calendar.getInstance();

			// 一个月前的日期
			cal1.add(Calendar.WEEK_OF_MONTH, -1);

			nwQuality.setStartDateToSecond(sf.format(cal1.getTime()));
			// 获取当前日期
			Calendar cal2 = Calendar.getInstance();
			nwQuality.setEndDateToSecond(sf.format(cal2.getTime()));

		} else {

			try {
				nwQuality.setStartDateToSecond(sf.format(sf1
						.parse(strStartTime)));
				nwQuality.setEndDateToSecond(sf.format(sf1.parse(strEndTime)));

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return nwQualityDao.showAllUserPointTileGeog(user, nwQuality);
	}



	public NWQualityDao getNwQualityDao() {
		return nwQualityDao;
	}

	@Resource
	public void setNwQualityDao(NWQualityDao nwQualityDao) {
		this.nwQualityDao = nwQualityDao;
	}

	public SignalStrengthDao getSignalStrengthDao() {
		return signalStrengthDao;
	}

	@Resource
	public void setSignalStrengthDao(SignalStrengthDao signalStrengthDao) {
		this.signalStrengthDao = signalStrengthDao;
	}

	public EchartsBar getEchartsBar() {
		return echartsBar;
	}

	@Resource
	public void setEchartsBar(EchartsBar echartsBar) {
		this.echartsBar = echartsBar;
	}

	public DataConnectionDao getDataConnectionDao() {
		return dataConnectionDao;
	}

	@Resource
	public void setDataConnectionDao(DataConnectionDao dataConnectionDao) {
		this.dataConnectionDao = dataConnectionDao;
	}

	@Resource
	public void setEchartsLine(EchartsLine echartsLine) {
		this.echartsLine = echartsLine;
	}

	public EchartsPie getEchartsPie() {
		return echartsPie;
	}

	@Resource
	public void setEchartsPie(EchartsPie echartsPie) {
		this.echartsPie = echartsPie;
	}

}
