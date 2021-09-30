package cn.itcast.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.DataConnectionDao;
import cn.itcast.echarts.EchartsBar;
import cn.itcast.entity.DataConnection;
import cn.itcast.entity.User;
import cn.itcast.util.DefaultConfUtil;
import org.springframework.stereotype.Component;

@Component("dataConnectionService")
public class DataConnectionService {
	private DataConnectionDao dataConnectionDao;
	private EchartsBar echartsBar;

	// 数据连接成功率统计图
	public String getDataConnectionStatistics(DataConnection dataConnection,
											  User user) {
		
		// 首先获取所有数据连接总数（all）
		List<DataConnection> dataConn_alls = dataConnectionDao
				.getDataConnectionStatistics_all(dataConnection, user);
		// 再获取成功数据连接总数（success）
		List<DataConnection> dataConn_successs = dataConnectionDao
				.getDataConnectionStatistics_success(dataConnection, user);
		List<DataConnection> dataConns = new ArrayList<DataConnection>();
		for (int i = 0; i < dataConn_alls.size(); i++) {
			DataConnection dataConn = new DataConnection();
			for (int j = 0; j < dataConn_successs.size(); j++) {
				if (dataConn_alls.get(i).getNetwork_name().equals(dataConn_successs.get(j).getNetwork_name())
						&& dataConn_alls.get(i).getNetwork_type()
								.equals(dataConn_successs.get(j).getNetwork_type())) {
					dataConn.setD_percent(((double) dataConn_successs.get(j).gettCount()) / dataConn_alls.get(i).gettCount()* 100);
					dataConn.setNetwork_name(dataConn_alls.get(i).getNetwork_name());
					dataConn.setNetwork_type(dataConn_alls.get(i).getNetwork_type());
				}
			}
			if (dataConn.getD_percent()==0.0) {
				dataConn.setNetwork_name(dataConn_alls.get(i).getNetwork_name());
				dataConn.setNetwork_type(dataConn_alls.get(i).getNetwork_type());
			}
			dataConns.add(dataConn);
		}
		// 通过Echarts返回JSON数据
		String optionStr = echartsBar.DataConnectionSStatistics(dataConns);
		return optionStr;
	}

	/*
	 * 下面是get、set方法
	 */

	public DataConnectionDao getDataConnectionDao() {
		return dataConnectionDao;
	}

	@Resource
	public void setDataConnectionDao(DataConnectionDao dataConnectionDao) {
		this.dataConnectionDao = dataConnectionDao;
	}

	public EchartsBar getEchartsBar() {
		return echartsBar;
	}

	@Resource
	public void setEchartsBar(EchartsBar echartsBar) {
		this.echartsBar = echartsBar;
	}

	// 数据连接点成功分布图
	@SuppressWarnings("deprecation")
	public List<DataConnection> showPointConnDist(User user,
			DataConnection dataConnection) {

		// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

		
		String strdate= DefaultConfUtil.newInstance().getDefaultconf().getEndDateDateConnection().substring(0, 8);


		Date date=null;
		if (dataConnection.getTime_index_client() == null) {
		
		try {
			
			date=sf.parse(strdate);
			long temp=30 * 24 * 60 * 60 * 1000l;
			long lDate=date.getTime()-temp;
			
			dataConnection.setStartDate(Integer.parseInt(sf.format(lDate)));
			
			dataConnection.setEndDate(Integer.parseInt(sf.format(date)));

			
			sf = new SimpleDateFormat("MM/dd/yyyy");
			dataConnection.setTime_index_client(sf.format(lDate)+ " - " + sf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {

			if (dataConnection.getTime_index_client().contains("-")) {

				// 通过筛选条件传参，分割daytime
				String[] str = dataConnection.getTime_index_client().split(
						" - ");

				dataConnection.setStartDate(Integer.parseInt(sf
						.format(new Date(str[0]))));
				dataConnection.setEndDate(Integer.parseInt(sf.format(new Date(
						str[1]))));

			} else {
				// 通过筛选条件传参，分割daytime
				dataConnection
						.setStartDate(Integer.parseInt(sf.format(new Date(
								dataConnection.getTime_index_client()))));
				dataConnection.setEndDate(Integer.parseInt(sf.format(new Date(
						dataConnection.getTime_index_client()))));
			}
		}
		return dataConnectionDao.showPointConnDist(user, dataConnection);
	}

	// 获取运营商级联的网络制式
	public List<DataConnection> showAcquireNwType(User user,
			DataConnection dataConnection) {

		return dataConnectionDao.showAcquireNwType(user, dataConnection);
	}

	@SuppressWarnings("deprecation")
	public List<DataConnection> showPointConnDistFails(User user,
			DataConnection dataConnection) {

		// 判断daytime是否为NULL——NULL：左边树点击的链接；NOT NULL：筛选条件
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

		
		String strdate=DefaultConfUtil.newInstance().getDefaultconf().getEndDateDateConnection().substring(0, 8);


		Date date=null;
		if (dataConnection.getTime_index_client() == null) {
	
			try {
				date=sf.parse(strdate);
				long temp=30 * 24 * 60 * 60 * 1000l;
				long lDate=date.getTime()-temp;
				
				dataConnection.setStartDate(Integer.parseInt(sf.format(lDate)));
				
				dataConnection.setEndDate(Integer.parseInt(sf.format(date)));

				sf = new SimpleDateFormat("MM/dd/yyyy");
				dataConnection.setTime_index_client(sf.format(lDate)+ " - " + sf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		} else {

			if (dataConnection.getTime_index_client().contains("-")) {

				// 通过筛选条件传参，分割daytime
				String[] str = dataConnection.getTime_index_client().split(
						" - ");

				dataConnection.setStartDate(Integer.parseInt(sf
						.format(new Date(str[0]))));
				dataConnection.setEndDate(Integer.parseInt(sf.format(new Date(
						str[1]))));


			} else {
				// 通过筛选条件传参，分割daytime

				dataConnection
						.setStartDate(Integer.parseInt(sf.format(new Date(
								dataConnection.getTime_index_client()))));
				dataConnection.setEndDate(Integer.parseInt(sf.format(new Date(
						dataConnection.getTime_index_client()))));

			}

		}

		return dataConnectionDao.showPointConnDistFails(user, dataConnection);
	}
}
