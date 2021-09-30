package cn.itcast.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.DataConnectionDao;
import cn.itcast.entity.DataConnection;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component(value = "dataConnectionDao")
public class DataConnectionDaoImpl implements DataConnectionDao {
	private static Logger logger = LoggerFactory.getLogger(DataConnectionDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	// 获取DataConnection总数
	@SuppressWarnings("deprecation")// 表示不检测过期的方法
	public long getDataConnectionCount(User user) {

		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			sql = "select count(*) from DataConnection";
		} else {
			sql = "select count(*) from DataConnection where network_name = ?";
		}
		return jdbcTemplate.queryForLong(sql,
				// 先判断用户是否是admin
				VarDesc.ROLE.ADMIN.equals(user.getRole()) ?new Object[] {}
						: new Object[] {user.getNWOperator() });
	}

	// 数据连接成功率统计图(成功)
	public List<DataConnection> getDataConnectionStatistics_success(
			DataConnection dataConnection, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL
						.equals(dataConnection.getNetwork_name())) {
			sql = "select count(*) as tCount,network_name,network_type from DataConnection where time_index_client >= ? and time_index_client <= ? and ping_value like ('%1%1%1%1%') and network_type <> 'NULL' and network_type <> 'WI-FI' group by network_name,network_type order by network_type asc";
 		} else {
			sql = "select count(*) as tCount,network_name,network_type from DataConnection where time_index_client >= ? and time_index_client <= ? and network_name= ? and ping_value like ('%1%1%1%1%') and network_type <> 'NULL' and network_type <> 'WI-FI' group by network_name,network_type order by network_type asc";
 		}
		List<DataConnection> dataConnections = jdbcTemplate
				.query(sql,
						// 先判断用户是否是admin
						VarDesc.ROLE.ADMIN.equals(user.getRole()) ?

						// 在判断选择的运营商
						VarDesc.OPERATOR.ALL.equals(dataConnection
								.getNetwork_name()) ? new Object[] {
								dataConnection.getStartDate(),
								dataConnection.getEndDate() } : new Object[] {
								dataConnection.getStartDate(),
								dataConnection.getEndDate(),
								dataConnection.getNetwork_name() }
								: new Object[] { dataConnection.getStartDate(),
										dataConnection.getEndDate(),
										user.getNWOperator() },
						new RowMapper<DataConnection>() {
							@Override
							public DataConnection mapRow(ResultSet rs,int rowNum) throws SQLException {
								DataConnection dataConnection = new DataConnection();
								dataConnection.settCount(rs.getInt("tCount"));
								dataConnection.setNetwork_name(rs
										.getString("network_name"));
								dataConnection.setNetwork_type(rs.getString("network_type"));
								return dataConnection;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Data Connection Statistics(success)\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+dataConnections.size()+"\tline:");
		return dataConnections;
	}

	// 数据连接率统计图（所有）
	public List<DataConnection> getDataConnectionStatistics_all(
			DataConnection dataConnection, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL
						.equals(dataConnection.getNetwork_name())) {
			sql = "select count(*) as tCount,network_name,network_type from DataConnection where time_index_client >= ? and time_index_client <= ? and network_type <> 'NULL'                     and network_type <> 'WI-FI' group by network_name,network_type order by network_type asc";
		} else {
			sql = "select count(*) as tCount,network_name,network_type from DataConnection where time_index_client >= ? and time_index_client <= ? and network_name= ? and network_type <> 'NULL' and network_type <> 'WI-FI' group by network_name,network_type order by network_type asc";
		}
		List<DataConnection> dataConnections = jdbcTemplate
				.query(sql,
						// 先判断用户是否是admin
						VarDesc.ROLE.ADMIN.equals(user.getRole()) ?

						// 在判断选择的运营商
						VarDesc.OPERATOR.ALL.equals(dataConnection
								.getNetwork_name()) ? new Object[] {
								dataConnection.getStartDate(),
								dataConnection.getEndDate() } : new Object[] {
								dataConnection.getStartDate(),
								dataConnection.getEndDate(),
								dataConnection.getNetwork_name() }
								: new Object[] { dataConnection.getStartDate(),
										dataConnection.getEndDate(),
										user.getNWOperator() },
						new RowMapper<DataConnection>() {
							@Override
							public DataConnection mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								DataConnection dataConnection = new DataConnection();
								dataConnection.settCount(rs.getInt("tCount"));
								dataConnection.setNetwork_name(rs
										.getString("network_name"));
								dataConnection.setNetwork_type(rs.getString("network_type"));
								return dataConnection;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Data Connection Statistics(all)\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+dataConnections.size()+"\tline:");
		return dataConnections;
	}


	//筛所包含的选网络制式
	public List<DataConnection> getMobileNetworkType() {
		List<DataConnection> dataConnections = jdbcTemplate
				.query("select distinct mobile_network_type from DataConnection where mobile_network_type is not null;",
						new RowMapper<DataConnection>() {

							@Override
							public DataConnection mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								DataConnection dataConnection = new DataConnection();
								dataConnection.setMobile_network_type(rs.getString("mobile_network_type"));
								return dataConnection;
							}
						});
		//return dataConnections.size() != 0 ? dataConnections : null;
		return dataConnections;
	}

	/*
 	 * -------------- 下面是get、set方法
 	 */

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//数据连接点分布图
		@Override
		public List<DataConnection> showPointConnDist(User user,DataConnection dataConnection) {
			 long startTimeMills=System.currentTimeMillis();
			String sql = null;
			Object[] objs;
			if("admin".equals(user.getRole())&& "ALL".equals(dataConnection.getNetwork_name())  &&"ALL".equals( dataConnection.getMobile_network_type()) ) {
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and ping_value like '%1%1%1%1%'";
				objs = new Object[] {dataConnection.getStartDate(), dataConnection.getEndDate()};
			}else if("admin".equals(user.getRole())&& "ALL".equals(dataConnection.getNetwork_name())  ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=?   and network_type=? and ping_value like '%1%1%1%1%'";
				objs = new Object[] {dataConnection.getStartDate(), dataConnection.getEndDate(),dataConnection.getMobile_network_type() };
			}else if("admin".equals(user.getRole()) &&"ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ? and ping_value like '%1%1%1%1%'";
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),dataConnection.getNetwork_name()};
 			}else if ("admin".equals(user.getRole())&& !"ALL".equals(dataConnection.getNetwork_name())  &&!"ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ?  and network_type=? and ping_value like '%1%1%1%1%'";	
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),dataConnection.getNetwork_name(),dataConnection.getMobile_network_type() };
 			}else if("ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ? and ping_value like '%1%1%1%1%'";
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),user.getNWOperator() };
			}else {
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ?  and network_type=? and ping_value like '%1%1%1%1%'";
				objs = new Object[] {dataConnection.getStartDate(), dataConnection.getEndDate(),user.getNWOperator(),dataConnection.getMobile_network_type() };
			}
 
			List<DataConnection> connections = jdbcTemplate.query(sql,objs,
					new RowMapper<DataConnection>() {
						@Override
						public DataConnection mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							DataConnection connection = new DataConnection();

							connection.setUser_lat(rs.getDouble("user_lat"));
							connection.setUser_lon(rs.getDouble("user_lon"));
							connection.setNetwork_name(rs.getString("network_name"));

							return connection;
						}

					});
			
 
		    long endTimeMills=System.currentTimeMillis();
		    logger.info("report forms:Data Connection Point Map\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+connections.size()+"\tline:");
			return connections;
		}

		@Override
		public List<DataConnection> showAcquireNwType(
				User user,DataConnection dataConnection) {
			
			String sql=null;
			if (!("admin".trim().toLowerCase().equals(user.getRole()))) {
				sql="select distinct mobile_network_type from DataConnection where mobile_network_type is not null  and network_name = ?;";
			}else{
				sql="select distinct mobile_network_type from DataConnection where mobile_network_type is not null;";
			}
			
			List<DataConnection> dataConnections = jdbcTemplate
					.query(sql,("admin".trim().toLowerCase().equals(user.getRole()))?new Object[]{}:new Object[]{user.getNWOperator()},
							new RowMapper<DataConnection>() {

								@Override
								public DataConnection mapRow(ResultSet rs,
										int rowNum) throws SQLException {
									DataConnection dataConnection = new DataConnection();
									dataConnection.setMobile_network_type(rs.getString("mobile_network_type"));
									return dataConnection;
								}
							});
			return dataConnections;
		}

		@Override
		public List<DataConnection> showPointConnDistFails(User user,
				DataConnection dataConnection) {
			 long startTimeMills=System.currentTimeMillis();
			String sql = null;
			Object[] objs;
			if("admin".equals(user.getRole())&& "ALL".equals(dataConnection.getNetwork_name())  &&"ALL".equals( dataConnection.getMobile_network_type()) ) {
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=?  and  ping_value like '%0%0%'";
				objs = new Object[] {dataConnection.getStartDate(), dataConnection.getEndDate()};
			}else if("admin".equals(user.getRole())&& "ALL".equals(dataConnection.getNetwork_name())  ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=?   and network_type=? and ping_value like '%0%0%'";
				objs = new Object[] {dataConnection.getStartDate(), dataConnection.getEndDate(),dataConnection.getMobile_network_type() };
			}else if ("admin".equals(user.getRole()) &&"ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ? and ping_value like '%0%0%'";
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),dataConnection.getNetwork_name()};
 			}else if ("admin".equals(user.getRole())&& !"ALL".equals(dataConnection.getNetwork_name())  &&!"ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ?  and network_type=? and ping_value like '%0%0%'";	
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),dataConnection.getNetwork_name(),dataConnection.getMobile_network_type() };
 			}else if("ALL".equals( dataConnection.getMobile_network_type()) ){
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ?  and ping_value like '%0%0%'";
				objs = new Object[]{ dataConnection.getStartDate(),dataConnection.getEndDate(),user.getNWOperator() };
			}else {
				sql = "select user_lat,user_lon,network_name,mobile_network_type from DataConnection where time_index_client >=? and time_index_client <=? and network_name = ?  and network_type=? and ping_value like '%0%0%'";
				objs = new Object[] { };
			}
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    "+sql);
			System.out.println(dataConnection.getStartDate()+"   |  "+dataConnection.getEndDate()+"  |    "+dataConnection.getNetwork_name()+"  |    "+dataConnection.getMobile_network_type());
			 logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    "+sql);
				
			List<DataConnection> connectionFails = jdbcTemplate.query(sql,objs,
							
						
							new RowMapper<DataConnection>() {
								@Override
								public DataConnection mapRow(ResultSet rs, int rowNum)
										throws SQLException {
									DataConnection connection = new DataConnection();

									connection.setUser_lat(rs.getDouble("user_lat"));
									connection.setUser_lon(rs.getDouble("user_lon"));
									connection.setNetwork_name(rs.getString("network_name"));

									return connection;
								}

							});
 
		    long endTimeMills=System.currentTimeMillis();
		    logger.info("report forms:Data Connection Point Map\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+connectionFails.size()+"\tline:");
	
			return connectionFails;
		}

}
