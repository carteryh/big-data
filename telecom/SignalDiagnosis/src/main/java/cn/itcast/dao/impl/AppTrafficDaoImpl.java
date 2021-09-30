package cn.itcast.dao.impl;

import cn.itcast.controller.NWQualityController;
import cn.itcast.dao.AppTrafficDao;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.util.DaoImplUtil;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("apptrafficDao")
public class AppTrafficDaoImpl implements AppTrafficDao {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(NWQualityController.class);
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public List<AppTraffic> getAppStreetScape(User user, AppTraffic appTraffic) {
		String sql = null;
		
		
		if ("admin".equals(user.getRole())
				&& "ALL".equals(appTraffic.getNetwork_name())) {
			sql = "select user_lon,user_lat,app_name from app_traffic where date >=? and date <=? ";
	
		} else {
			sql = "select user_lon,user_lat,app_name from app_traffic where date >=? and date <=? and  NETWORK_NAME = ? ";
	
		}
		
		List<AppTraffic> appTraffics= jdbcTemplate.query(
				sql,
				//先判断用户是否是admin
				"admin".equals(user.getRole()) &&
						"ALL".equals(appTraffic.getNetwork_name()) 
						? new Object[] {appTraffic.getStartDate(), appTraffic.getEndDate() }
						: new Object[] { appTraffic.getStartDate(), appTraffic.getEndDate(),appTraffic.getNetwork_name() } 
						,
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								AppTraffic appTraffic = new AppTraffic();

								appTraffic.setGpslat(rs.getString("user_lat"));
								appTraffic.setGpslon(rs.getString("user_lon"));
								appTraffic.setApp_name(rs.getString("app_name"));
								
								return appTraffic;
							}

						});


		return appTraffics;
	}

	
	//App流量排名(网络制式)
	public List<AppTraffic> getAppRateRank(AppTraffic appTraffic,User user) {
		 long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic + upload_traffic) as speed,app_name,network_type from APP_TRAFFIC where  date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,app_name,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";
			}else {
				sql = "select sum(upload_traffic) as speed,app_name,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic + upload_traffic) as speed,app_name,network_type from APP_TRAFFIC where  date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,app_name,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";
			}else {
				sql ="select sum(upload_traffic) as speed,app_name,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name,network_type order by speed desc limit 50";
			}
		}
		List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
						// 先判断用户是Admin运营商是All
						VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
								new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
								:VarDesc.ROLE.ADMIN.equals(user.getRole())?
									new Object[]{appTraffic.getStartDate(),appTraffic.getEndDate(),appTraffic.getNetwork_name()}:
									new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate(),user.getNWOperator()},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
								AppTraffic appTraffic = new AppTraffic();
								appTraffic.setSpeed(rs.getLong("speed"));
								appTraffic.setApp_name(rs.getString("app_name"));
								appTraffic.setNetworkType(rs.getString("network_type"));
								return appTraffic;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:App Traffic Rank3\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}
	//所有APP流量总和
	@SuppressWarnings("deprecation")
	public long getAppRAteSum(AppTraffic appTraffic,User user) {
		 long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic + upload_traffic) as sumSpeed from APP_TRAFFIC where  date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic'";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as sumSpeed from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic'";
			}else {
				sql = "select sum(upload_traffic) as sumSpeed from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic'";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic + upload_traffic) as sumSpeed from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic'";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as sumSpeed from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic'";
			}else {
				sql ="select sum(upload_traffic) as sumSpeed from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic'";
			}
		}
		
		long l = jdbcTemplate.queryForLong(sql,
				// 先判断用户是Admin运营商是All
				VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
						new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
						:VarDesc.ROLE.ADMIN.equals(user.getRole())?
							new Object[]{appTraffic.getStartDate(),appTraffic.getEndDate(),appTraffic.getNetwork_name()}:
							new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate(),user.getNWOperator()});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:App Traffic Rank1\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:1\tline:");
		return l;
	}
	//App流量排名(all)
	public List<AppTraffic> getAppRateRank_all(AppTraffic appTraffic,User user) {
		 long startTimeMills=System.currentTimeMillis();
			String sql = null;
			if (VarDesc.ROLE.ADMIN.equals(user.getRole())
					&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
				if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
					sql = "select sum(download_traffic + upload_traffic) as speed,app_name from APP_TRAFFIC where  date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";
				}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
					sql = "select sum(download_traffic) as speed,app_name from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";
				}else {
					sql = "select sum(upload_traffic) as speed,app_name from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";	
				}
			} else {
				if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
					sql = "select sum(download_traffic + upload_traffic) as speed,app_name from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";
				}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
					sql = "select sum(download_traffic) as speed,app_name from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";
				}else {
					sql ="select sum(upload_traffic) as speed,app_name from APP_TRAFFIC where date >= ? and date <= ? and network_type <> 'WI-FI' and network_name = ? and app_name <> 'phoneTotalTraffic' group by app_name order by speed desc limit 5";
				}
			}
			List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
							// 先判断用户是Admin运营商是All
							VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
									new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
									:VarDesc.ROLE.ADMIN.equals(user.getRole())?
										new Object[]{appTraffic.getStartDate(),appTraffic.getEndDate(),appTraffic.getNetwork_name()}:
										new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate(),user.getNWOperator()},
							new RowMapper<AppTraffic>() {
								@Override
								public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
									AppTraffic appTraffic = new AppTraffic();
									appTraffic.setSpeed(rs.getLong("speed"));
									appTraffic.setApp_name(rs.getString("app_name"));
									return appTraffic;
								}
							});
			long endTimeMills=System.currentTimeMillis();
			logger.info("report forms:App Traffic Rank2\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
			return appTraffics;
		}
	// 热门手机流量排名(各网络制式)
	public List<AppTraffic> getHotTelRank_networkType(AppTraffic appTraffic,User user) {
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,companyModel,network_type from APP_TRAFFIC where  date >= ? and date <= ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,companyModel,network_type from APP_TRAFFIC where date >= ? and date <= ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";
			}else {
				sql = "select sum(upload_traffic) as speed,companyModel,network_type from APP_TRAFFIC where date >= ? and date <= ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,companyModel,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,companyModel,network_type from APP_TRAFFIC where  date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";
			}else {
				sql ="select sum(upload_traffic) as speed,companyModel,network_type from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by companyModel,network_type order by speed desc limit 50";
			}
		}
		List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
						// 先判断用户是Admin运营商是All
						VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
								new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
								:VarDesc.ROLE.ADMIN.equals(user.getRole())?
									new Object[]{appTraffic.getStartDate(),appTraffic.getEndDate(),appTraffic.getNetwork_name()}:
									new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate(),user.getNWOperator()},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
								AppTraffic appTraffic = new AppTraffic();
								appTraffic.setSpeed(new Double(rs.getDouble("speed")).longValue());
								appTraffic.setCompanyModel(rs.getString("companyModel"));
								appTraffic.setNetworkType(rs.getString("network_type"));
								return appTraffic;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Traffic Rank3\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}
	// 热门手机流量排名(网络制式：ALL)
	public List<AppTraffic> getHotTelRank(AppTraffic appTraffic,User user) {
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,companyModel,network_type from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,companyModel from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";
			}else {
				sql = "select sum(upload_traffic) as speed,companyModel from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,companyModel from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,companyModel from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";
			}else {
				sql ="select sum(upload_traffic) as speed,companyModel from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and companyModel <> 'NULL' and network_type <> 'NULL' group by company,network_type order by speed desc limit 50";
			}
		}
		List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
						// 先判断用户是Admin运营商是All
						VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
								new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
								:VarDesc.ROLE.ADMIN.equals(user.getRole())?
									new Object[]{appTraffic.getNetwork_name(),appTraffic.getStartDate(),appTraffic.getEndDate()}:
									new Object[] {user.getNWOperator(),appTraffic.getStartDate(),appTraffic.getEndDate()},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
								AppTraffic appTraffic = new AppTraffic();
								appTraffic.setSpeed(new Double(rs.getDouble("speed")).longValue());
								appTraffic.setCompanyModel(rs.getString("companyModel"));
								return appTraffic;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Traffic Rank2\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}
	
	
	
	// 热门手机流量排名首页
	public List<AppTraffic> getHotTelRanktoppage(AppTraffic appTraffic,User user) {
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";
			}else {
				sql = "select sum(upload_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";
			}else {
				sql ="select sum(upload_traffic) as speed,company from APP_TRAFFIC where date >= ? and date <= ? and network_name = ? and company <> 'NULL' and network_type <> 'NULL' group by company order by speed desc limit 5";
			}
		}
		
		List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
						// 先判断用户是Admin运营商是All
						VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
								new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
								:VarDesc.ROLE.ADMIN.equals(user.getRole())?
									new Object[]{appTraffic.getNetwork_name(),appTraffic.getStartDate(),appTraffic.getEndDate()}:
									new Object[] {user.getNWOperator(),appTraffic.getStartDate(),appTraffic.getEndDate()},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
								AppTraffic appTraffic = new AppTraffic();
								appTraffic.setSpeed(new Double(rs.getDouble("speed")).longValue());
								appTraffic.setCompanyModel(rs.getString("company"));
								return appTraffic;
							}
						});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Traffic Rank2\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}
	//典型地标热门App流量排名
	public List<AppTraffic> getLandmarkAppTrafficRank(AppTraffic appTraffic, User user) {
		System.out.println("地表名： "+appTraffic.getLandmark());
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name())) {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,app_name,network_type from app_traffic where date >= ? and date <= ? and Landmark ='"+appTraffic.getLandmark()+"' and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,app_name,network_type from app_traffic where date >= ? and date <= ? and Landmark  ='"+appTraffic.getLandmark()+"'  and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";
			}else {
				sql = "select sum(upload_traffic) as speed,app_name,network_type from app_traffic where date >= ? and date <= ? and Landmark  ='"+appTraffic.getLandmark()+"' and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";	
			}
		} else {
			if (VarDesc.SPEEDTYPE.ALL.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(upload_traffic + download_traffic) as speed,app_name,network_type from app_traffic where  date >= ? and date <= ? and Landmark  ='"+appTraffic.getLandmark()+"' and network_name = ? and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";
			}else if (VarDesc.SPEEDTYPE.DLSPEED.equals(appTraffic.getSpeed_type())) {
				sql = "select sum(download_traffic) as speed,app_name,network_type from app_traffic where date >= ? and date <= ? and Landmark =  '"+appTraffic.getLandmark()+"' and network_name = ? and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";
			}else {
				sql ="select sum(upload_traffic) as speed,app_name,network_type from app_traffic where date >=? and date <= ? and Landmark =   '"+appTraffic.getLandmark()+"' and network_name = ? and network_type <> 'null' group by app_name,network_type order by speed desc limit 100";
			}
		}
 		List<AppTraffic> appTraffics = jdbcTemplate.query(sql,
			// 先判断用户是Admin运营商是All
			VarDesc.ROLE.ADMIN.equals(user.getRole()) && VarDesc.OPERATOR.ALL.equals(appTraffic.getNetwork_name()) ? 
					new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate()} 
					:VarDesc.ROLE.ADMIN.equals(user.getRole())?
					new Object[]{appTraffic.getStartDate(),appTraffic.getEndDate(),appTraffic.getNetwork_name()}:
					new Object[] {appTraffic.getStartDate(),appTraffic.getEndDate(),user.getNWOperator()},
			new RowMapper<AppTraffic>() {
			@Override
			public AppTraffic mapRow(ResultSet rs,int rowNum) throws SQLException {
					AppTraffic appTraffic = new AppTraffic();
					appTraffic.setSpeed(rs.getLong("speed"));
					appTraffic.setApp_name(rs.getString("app_name"));
					appTraffic.setNetworkType(rs.getString("network_type"));
					return appTraffic;
			}
		});
 		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Landmark App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}
	/**
	 热门手机统计直方图
 	 */
	@Override
	public List<AppTraffic> hotAppTrafficStatistics(AppTraffic appTrafficBean,
			User user) {
		String speedType=null;
		if(VarDesc.APPSPEEDTYPE.ALL.equals(appTrafficBean.getSpeed_type())){
		speedType=VarDesc.APPSPEEDTYPE.DLSPEED+"+"+VarDesc.APPSPEEDTYPE.ULSPEED;
		}else{
		speedType=appTrafficBean.getSpeed_type();
		}
		String networkName=appTrafficBean.getNetwork_name(); 
		int beginDate=appTrafficBean.getStartDate();
		int endDate=appTrafficBean.getEndDate();
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())){
			if(VarDesc.OPERATOR.ALL.equals(networkName)){
			sql="SELECT  SUM("+speedType+") as speed ,app_name,network_type FROM app_traffic  WHERE date >=? AND date <=?  GROUP BY app_name,network_type ORDER BY speed LIMIT 10";
			}else{
			sql="SELECT  SUM("+speedType+") as speed ,app_name,network_type FROM app_traffic  WHERE date >=? AND date <=?  AND network_name='"+networkName+"' GROUP BY network_type,app_name ORDER BY speed LIMIT 10";	
			}
			}else{
			sql="SELECT  SUM("+speedType+") as speed ,app_name,network_type FROM app_traffic  WHERE date >=? AND date <=?  AND network_name='"+networkName+"' GROUP BY network_type,app_name ORDER BY speed LIMIT 10";
		}
		///////////////////////////////测试sql和参数/////////////////////////////////
	
		List<AppTraffic> appTraffics = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								AppTraffic appTraffic = new AppTraffic();
								appTraffic.setSpeed(Long.valueOf(rs.getString("speed").replaceAll("-", "")));
								appTraffic.setNetworkType(rs.getString("network_type"));
								appTraffic.setApp_name(rs.getString("app_name"));
								return appTraffic;

							}
						});
		return appTraffics.size() != 0 ? appTraffics : null;
	}
	
 
	@Override
	public long hotAppTrafficStatistics_pie(AppTraffic appTrafficBean, User user) {
		String speedType=null;
		if(VarDesc.APPSPEEDTYPE.ALL.equals(appTrafficBean.getSpeed_type())){
		speedType=VarDesc.APPSPEEDTYPE.DLSPEED+"+"+VarDesc.APPSPEEDTYPE.ULSPEED;
		}else{
		speedType=appTrafficBean.getSpeed_type();
		}
		String networkName=appTrafficBean.getNetwork_name(); 
		int beginDate=appTrafficBean.getStartDate();
		int endDate=appTrafficBean.getEndDate();
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())){
			if(VarDesc.OPERATOR.ALL.equals(networkName)){
			sql="SELECT  SUM("+speedType+") as speed  FROM app_traffic  WHERE date >=? AND date <=? ;";
			}else{
			sql="SELECT  SUM("+speedType+") as speed  FROM app_traffic  WHERE date >=? AND date <=?  AND network_name='"+networkName+"' ;";	
			}
			}else{
			sql="SELECT  SUM("+speedType+") as speed  FROM app_traffic  WHERE date >=? AND date <=?  AND network_name='"+networkName+"' ;";
		}
		///////////////////////////////测试sql和参数/////////////////////////////////

		double allSpeed  = jdbcTemplate.queryForLong(sql,new Object[] {beginDate,endDate}); 
		return (long)allSpeed;
	}
	/**
	 手机OS流量统计直方图
 	 */
	@Override
	public List<AppTraffic>  getHotTelOsStatistics(AppTraffic appTrafficBean,User user) {
		
		logger.debug("appTrafficBean", appTrafficBean);
		String speedType=appTrafficBean.getSpeed_type();
		String nwOperator=appTrafficBean.getNetwork_name(); 
		long beginDate=appTrafficBean.getStartDate();
		long endDate=appTrafficBean.getEndDate();
		if(VarDesc.APPSPEEDTYPE.ALL.equals(speedType)){
			speedType=VarDesc.APPSPEEDTYPE.DLSPEED+"+"+VarDesc.APPSPEEDTYPE.ULSPEED;
		}
		String sqlTop=null;
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())&&VarDesc.OPERATOR.ALL.equals(nwOperator)){
			sqlTop="SELECT  SUM("+speedType+") AS speed,os_andversion FROM APP_TRAFFIC  WHERE date>=? AND date<=?  GROUP BY  os_andversion  order by speed desc limit 5";
		}else{
			sqlTop="SELECT  SUM("+speedType+") AS speed,os_andversion FROM APP_TRAFFIC  WHERE date>=? AND date<=?  AND network_name='"+nwOperator+"' GROUP BY os_andversion order by speed desc limit 5";
		}
		/*
		 *@method:从数据库获取前几行数据
		 *@param:Object[]占位符的参数topRow获取前几行数据new Object[] {beginDate,endDate}
		 *@param:sqlfield数据库对应的字段
		 *@return:返回值是一个sql中的in的内容的一个字符串
		 */
		String sqlInVal=DaoImplUtil.getSqlInVal(jdbcTemplate,sqlTop,new Object[]{beginDate,endDate},"os_andversion");
		logger.info("report forms:appTraffic\t 手机OS流量统计直方图\tsql query:"+sqlTop+"\t"+"execution time:"+beginDate+"-"+endDate+"s\tcollection size:"+sqlInVal+"\tline:");
		if(sqlInVal!=""){
			if(VarDesc.ROLE.ADMIN.equals(user.getRole())&&VarDesc.OPERATOR.ALL.equals(nwOperator)){
				sql="SELECT  SUM("+speedType+") AS speed,network_type,os_andversion FROM APP_TRAFFIC  WHERE date>=? AND date<=? AND os_andversion!='unknown'  AND network_type<>'null' "+sqlInVal+"   GROUP BY  network_type,os_andversion  order by speed desc";
			}else{
				sql="SELECT  SUM("+speedType+") AS speed,network_type,os_andversion FROM APP_TRAFFIC  WHERE date>=? AND date<=? AND os_andversion!='unknown'  AND network_type<>'null' AND network_name='"+nwOperator+"' "+sqlInVal+"  GROUP BY network_type,os_andversion order by speed desc";
			}
		}else{
			return null;
		}
		
		System.out.println("77777777777777777777"+sql);
		 List<AppTraffic> appTraffics = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								AppTraffic appTraffic =new AppTraffic();
								appTraffic.setSpeed((long)Math.abs(rs.getDouble("speed")));
								appTraffic.setNetworkType(rs.getString("network_type"));
								appTraffic.setOsAndVersion(rs.getString("os_andversion"));
								return appTraffic;
							}
						});
		 logger.info("report forms:appTraffic\t 手机OS流量统计直方图\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics.size() != 0 ? appTraffics : null;
	}
	
	
	/**
	 手机OS流量饼图中的总的部分
 	 */
	@Override
	public long getHotTelOsStatistics_pie(AppTraffic appTrafficBean, User user) {
		String speedType=appTrafficBean.getSpeed_type();
		String nwOperator=appTrafficBean.getNetwork_name(); 
		long beginDate=appTrafficBean.getStartDate();
		long endDate=appTrafficBean.getEndDate();
		if(VarDesc.APPSPEEDTYPE.ALL.equals(speedType)){
			speedType=VarDesc.APPSPEEDTYPE.DLSPEED+"+"+VarDesc.APPSPEEDTYPE.ULSPEED;
		}
		
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())&&VarDesc.OPERATOR.ALL.equals(nwOperator)){
			sql="SELECT  SUM("+speedType+") AS speed FROM APP_TRAFFIC  WHERE date>=? AND date<=?";
		}else{
			sql="SELECT  SUM("+speedType+") AS speed FROM APP_TRAFFIC  WHERE date>=? AND date<=?  AND network_name='"+nwOperator+"'";
		}
		///////////////////////////////测试sql和参数/////////////////////////////////
		double allSpeed  = jdbcTemplate.queryForLong(sql,new Object[] {beginDate,endDate}); 
		 logger.info("report forms:appTraffic\t 手机OS流量饼图中的总的部分\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tallSpeed:"+allSpeed+"\tline:");
		return (long)allSpeed;
	}

	/**
	 热门App流量跟踪图
 	 */
	@Override
	public List<AppTraffic> getAppTrafficTracks(AppTraffic appTrafficBean, User user) {
		String speedType=appTrafficBean.getSpeed_type();
		String nwOperator=appTrafficBean.getNetwork_name();
		String network_type=appTrafficBean.getNetworkType();

		String beginDate=String.valueOf(appTrafficBean.getStartDate()).substring(0, 10);
		String endDate=String.valueOf(appTrafficBean.getEndDate()).substring(0, 10);
		int startPos=0;
		String sqlTop=null;
		String sql=null;
		//String AND="";
		
		if(VarDesc.AXISTYPE.HOUR.equals(appTrafficBean.getX_type())){
			startPos=9;
	 	}else if(VarDesc.AXISTYPE.DAY.equals(appTrafficBean.getX_type())){
			startPos=7;
		}else if(VarDesc.AXISTYPE.MONTH.equals(appTrafficBean.getX_type())){
			startPos=5;
		}
		
		if(VarDesc.APPSPEEDTYPE.ALL.equals(speedType)){
			speedType=VarDesc.APPSPEEDTYPE.DLSPEED+"+"+VarDesc.APPSPEEDTYPE.ULSPEED;
		}
		
		Map db_v =new HashMap();
		db_v.put("network_type", network_type);
		db_v.put("network_name", nwOperator);
		/*
 		 *@method:拼接where条件，要放在group的前面
		 *@param:Map<String,String>前一个String是key也就是对应的是数据库的字段，
		 *第二个String是value也就是等号之后的值
		 *@return:返回值是一个sql中的where的字符串
 		 */
		String sqlWhere=DaoImplUtil.getSqlWhere(db_v);
		sqlTop="SELECT  SUM("+speedType+") AS speed,app_name FROM  app_traffic WHERE  start_time>=? AND start_time<=? "+sqlWhere+" GROUP BY app_name order by speed desc limit 5";

		System.out.println("11111111111111  "+sqlTop);
		/*
		 *@method:从数据库获取前几行数据
		 *@param:Object[]占位符的参数topRow获取前几行数据new Object[] {beginDate,endDate}
		 *@param:sqlfield数据库对应的字段
		 *@return:返回值是一个sql中的in的内容的一个字符串
		 */
		/*if(!AND.equals(sqlWhere)){
			AND="AND";
		}*/
		String sqlInVal=DaoImplUtil.getSqlInVal(jdbcTemplate,sqlTop,new Object[]{beginDate,endDate},"app_name");
		sql="SELECT  SUM("+speedType+") AS speed,SUBSTR(start_time,"+startPos+",2) AS x_rate,app_name FROM  app_traffic WHERE  start_time>=? AND start_time<=? "+sqlInVal+" "+sqlWhere+" GROUP BY x_rate,app_name";
		System.out.println("222222222222222  "+sql);
		List<AppTraffic> appTraffics = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<AppTraffic>() {
							@Override
							public AppTraffic mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								AppTraffic appTraffic =new AppTraffic();
								appTraffic.setSpeed((long)Math.abs(rs.getDouble("speed")));
								appTraffic.setX_rate(rs.getString("x_rate"));
								appTraffic.setApp_name(rs.getString("app_name"));
								return appTraffic;
							}
						});
		 logger.info("report forms:appTraffic\t 热门App流量跟踪图\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tallSpeed:"+appTraffics.size()+"\tline:");
		return appTraffics.size() != 0 ? appTraffics : null;
	}

 
	//热门app流量分布
	@Override
	public List<AppTraffic> getAppFlowDistribution(double smalllng,
			double biglng, double smalllat, double boglat ,AppTraffic appTraffic) {
		// TODO Auto-generated method stub
	
		// 定义查询数据的SQL语句
		String sql = "";
		String sql2 = "";
		// 如果是管理员查询     多天的       所有运营商      所有网络制式   总流量
		 if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";		
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";
			}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
		}
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time ="+appTraffic.getStartDate();		
				sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate();
				}
			//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time= "+appTraffic.getStartDate();
				}
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'";
				sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'";
				
				}
			//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'";
			}
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
				sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
				
				}
			//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			}
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
				sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time= "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
				
				}
			//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
			else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
				sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
			}
		 //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			/*
			 * 不是管理员 
			 */
				    else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
						//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
		 
		 
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
						//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
///						////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						 else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
								sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
								sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
								
								}
								//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
								else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
								sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
								
								}
				 
				 
								else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
								sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
								sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
								
								}
								//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
								else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
								sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where user_lon>'"+ smalllng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  package_name = '"+appTraffic.getPackage_name()+"' and start_time = "+appTraffic.getStartDate()+" and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
								
								}


						// 定义集合，以便接收查到的数据
								long startTimeMills=System.currentTimeMillis();
						List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
						if (appTraffic.getFlow().equalsIgnoreCase("all")){
							List<AppTraffic> appTraffics1 = new ArrayList<AppTraffic>();
							appTraffics1 = jdbcTemplate.query(sql,
									new RowMapper<AppTraffic>() {
				
										@Override
										public AppTraffic mapRow(ResultSet rs, int rowNum)
												throws SQLException {
											AppTraffic appTraffic = new AppTraffic();
											
											
											
											appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
											appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
											appTraffic.setCount((int)(rs.getDouble("heat")));
											
											
											
											return appTraffic;
										}
				
									});
							for(int i=0;i<appTraffics1.size();i++){
								appTraffics.add(appTraffics1.get(i));
							}
							List<AppTraffic> appTraffics2 = new ArrayList<AppTraffic>();
							appTraffics2 = jdbcTemplate.query(sql2,
									new RowMapper<AppTraffic>() {
				
										@Override
										public AppTraffic mapRow(ResultSet rs, int rowNum)
												throws SQLException {
											AppTraffic appTraffic = new AppTraffic();
											appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
											appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
											appTraffic.setCount((int)(rs.getDouble("heat")));
											return appTraffic;
										}
				
									});
							for(int i=0;i<appTraffics2.size();i++){
								appTraffics.add(appTraffics2.get(i));
							}
						}else{
							appTraffics = jdbcTemplate.query(sql,
									new RowMapper<AppTraffic>() {

										@Override
										public AppTraffic mapRow(ResultSet rs, int rowNum)
												throws SQLException {
											AppTraffic appTraffic = new AppTraffic();
											appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
											appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
											appTraffic.setCount((int)(rs.getDouble("heat")));
											return appTraffic;
										}

									});
						}

						long endTimeMills=System.currentTimeMillis();
						logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");


		return appTraffics;
	}
	

	
	/*
	 *    热门手机的分布图
	 * @see com.redhadoop.dao.AppTrafficDao#getMobileFlowDistribution(double, double, double, double, com.redhadoop.entity.AppTraffic)
	 */
	@Override
	public List<AppTraffic> getMobileFlowDistribution(double smalllng,
			double biglng, double smalllat, double boglat, AppTraffic appTraffic) {
		// TODO Auto-generated method stub

		// 定义查询数据的SQL语句
		String sql = "";
		String sql2 = "";
		
		String newlng="";
		
		if (smalllng/100<1&&biglng/100>1){
			newlng="0"+ Double.toString(smalllng);
		}else{
			newlng=Double.toString(smalllng);
		}
		
		// 实际业务中  应该是   where allMobileTraffic='yes'  
		//这个数据较少，为了看效果，现在查allMobileTraffic='no'的 
		
		// 如果是管理员查询     多天的       所有运营商      所有网络制式   总流量
		 if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where allMobileTraffic='no'  and  user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";		
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where  allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'";
			}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic where  allMobileTraffic='no'  and    user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
		}
		 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else  if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate();		
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate();
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and    user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate();
			}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+" and  and network_type='"+appTraffic.getNetworkType()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where allMobileTraffic='no'  and    user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
		}
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
			sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
			
			}
		//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上/下流量
		else if ("admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetwork_name().equalsIgnoreCase("all"))&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
			sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic where allMobileTraffic='no'  and    user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+" and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";;
		}
				//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			/*
			 * 不是管理员 
			 */
				    else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
						//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where  allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
		 
		 
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,upload_traffic as heat from app_traffic   where allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where  allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
						//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()!=0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
						sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic  where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
						
						}
		 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
							sql="select user_lon, user_lat,upload_traffic as heat from app_traffic  where  allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
							sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where  allMobileTraffic='no'  and  user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
							
							}
							//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
							else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
							sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic   where allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"'";
							
							}
			 
			 
							else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(appTraffic.getFlow().equalsIgnoreCase("all"))) {
							sql="select user_lon, user_lat,upload_traffic as heat from app_traffic   where allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
							sql2="select user_lon, user_lat,download_traffic as heat from app_traffic  where allMobileTraffic='no'  and   user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
							
							}
							//// 如果是管理员查询     多天的       所有运营商      所有网络制式   上行流量
							else if (!"admin".equals(appTraffic.getRole())&&(appTraffic.getEndDate()==0)&&(!appTraffic.getNetworkType().equalsIgnoreCase("all"))&&(!appTraffic.getFlow().equalsIgnoreCase("all"))) {
							sql="select user_lon, user_lat,"+appTraffic.getFlow()+" as heat from app_traffic   where allMobileTraffic='no'  and user_lon>'"+ newlng+ "' and user_lon< '"+ biglng+ "' and user_lat> '"+ smalllat+ "' and user_lat< '"+ boglat+"' and  companyModel = '"+appTraffic.getCompanyModel()+"' and start_time = "+appTraffic.getStartDate()+" and network_type='"+appTraffic.getNetworkType()+"' and network_name='"+appTraffic.getNetwork_name()+"'";
							
							}


			long startTimeMills=System.currentTimeMillis();
		// 定义集合，以便接收查到的数据
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();

		if (appTraffic.getFlow().equalsIgnoreCase("all")){
			List<AppTraffic> appTraffics1 = new ArrayList<AppTraffic>();
			appTraffics1 = jdbcTemplate.query(sql,
					new RowMapper<AppTraffic>() {

						@Override
						public AppTraffic mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							AppTraffic appTraffic = new AppTraffic();
							appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
							appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
							appTraffic.setCount((int)(rs.getDouble("heat")));
							return appTraffic;
						}

					});
			for(int i=0;i<appTraffics1.size();i++){
				appTraffics.add(appTraffics1.get(i));
			}
			List<AppTraffic> appTraffics2 = new ArrayList<AppTraffic>();
			appTraffics2 = jdbcTemplate.query(sql2,
					new RowMapper<AppTraffic>() {

						@Override
						public AppTraffic mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							AppTraffic appTraffic = new AppTraffic();

							appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
							appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
							appTraffic.setCount((int)(rs.getDouble("heat")));

							return appTraffic;
						}

					});
			for(int i=0;i<appTraffics2.size();i++){
				
				appTraffics.add(appTraffics2.get(i));
			}
			
		}else{
			appTraffics = jdbcTemplate.query(sql,
					new RowMapper<AppTraffic>() {

						@Override
						public AppTraffic mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							AppTraffic appTraffic = new AppTraffic();
							appTraffic.setLng(Double.parseDouble(rs.getString("user_lon")));
							appTraffic.setLat(Double.parseDouble(rs.getString("user_lat")));
							appTraffic.setCount((int)(rs.getDouble("heat")));
							return appTraffic;
						}

					});
		}

		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");
		return appTraffics;
	}

	
	
	
	@Override
	public List<AppTraffic> gettop10app(AppTraffic appTraffic) {
		// TODO Auto-generated method stub
		
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
		String sql="SELECT SUM(upload_traffic+download_traffic) as heat ,package_name, app_name from APP_TRAFFIC where date >= "+appTraffic.getStartDate()+" and date <="+appTraffic.getEndDate()+" and network_type <> 'WI-FI' and app_name <> 'phoneTotalTraffic' GROUP BY  package_name,app_name ORDER BY heat desc LIMIT 10";
		long startTimeMills=System.currentTimeMillis();
 		appTraffics = jdbcTemplate.query(sql,
				new RowMapper<AppTraffic>() {

					@Override
					public AppTraffic mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						AppTraffic appTraffic1 = new AppTraffic();
						appTraffic1.setApp_name(rs.getString("app_name"));
						appTraffic1.setPackage_name(rs.getString("package_name"));
						return appTraffic1;
					}

				});

		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:get top 10 app \tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

		return appTraffics;
	}

	
	/*
	 * 获取前5手机
	 */
	

	@Override
	public List<AppTraffic> gettop5mobile(AppTraffic appTraffic) {
		// TODO Auto-generated method stub
		List<AppTraffic> appTraffics = new ArrayList<AppTraffic>();
 		String sql="select count(*) as heat,COMPANYMODEL from APP_TRAFFIC where  date >= "+appTraffic.getStartDate()+" and date <= "+appTraffic.getEndDate()+" and COMPANYMODEL <> 'NULL' group by COMPANYMODEL order by heat desc limit 5";

		long startTimeMills=System.currentTimeMillis();
		appTraffics = jdbcTemplate.query(sql,
				new RowMapper<AppTraffic>() {

					@Override
					public AppTraffic mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						AppTraffic appTraffic1 = new AppTraffic();
						appTraffic1.setCompanyModel(rs.getString("companyModel"));
						return appTraffic1;
					}

				});
		
		long endTimeMills=System.currentTimeMillis();
		logger.info("report get top 5 mobile \tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

		return appTraffics;
	}

	
	

	

	//热门app使用
	@Override
	public List<AppTraffic> getheatApp(User user, AppTraffic appTraffic) {
	 
		List<AppTraffic> appTraffics=new ArrayList<AppTraffic>();
		final List<AppTraffic> appTraffics1=new ArrayList<AppTraffic>();
		
		  //起始经度
		  double itemLng=appTraffic.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(appTraffic.getGpslat());
	      //经度分片大小
	      double tileLng=appTraffic.getTileLng();
	      //维度分片大小
	      double tileLat=appTraffic.getTileLat();
	      
	      double endlng=0;

	      String sql="";
	      
	      int a=0;
	    for (int i = 0; i < 25; i++) {
	    	for (int j = 0; j < 12; j++) {
	    		endlng=itemLng+tileLng;
	    		
	    	 final	double  TableCenterLng=itemLng+tileLng/2;
	    	 final	double TableCenterLat=itemLat-tileLat/2;
	    		  if ("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	      }else if("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	    sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'  GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
		  	 	 
	  	      }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	 	  
	  	      }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and network_type='"+appTraffic.getNetworkType()+"'   GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	 	  
	  	      }
	  	 	  //不是管理员
	  	 	  else if ((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name= '"+appTraffic.getNetwork_name()+"'  GROUP BY(package_name) ORDER BY heat desc LIMIT 1";
	  	      
	  	 	  }else if((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name= '"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"'  GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	 	  
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and  network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	 	  
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,package_name, app_name from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"' GROUP BY package_name,app_name ORDER BY heat desc LIMIT 1";
	  	 	 
	  	 	  }
	    		  a++; 
	    	long startTimeMills=System.currentTimeMillis();
	    		appTraffics = jdbcTemplate.query(sql,new RowMapper<AppTraffic>() {
	    					@Override
	    					public AppTraffic mapRow(ResultSet rs, int rowNum)
	    							throws SQLException {
	    					
	    						AppTraffic appTraffic1 = new AppTraffic();
	    						appTraffic1.setApp_name(rs.getString("app_name"));
	    						appTraffic1.setPackage_name(rs.getString("package_name"));
	    						appTraffic1.setCount((int)rs.getDouble("heat"));
	    						System.out.println(rs.getString("app_name")+">>>         "+rs.getString("package_name")+">>>>>>>>      "+rs.getDouble("heat"));
	    						appTraffic1.setTableCenterLng(TableCenterLng);
	    						appTraffic1.setTableCenterLat(TableCenterLat);
	    						appTraffics1.add(appTraffic1);
	    						return appTraffic1;
	    					}
	    				});
	    		long endTimeMills=System.currentTimeMillis();
	    		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

	    		 itemLat=itemLat-tileLat;
	    	}
	    	itemLat=Double.parseDouble(appTraffic.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
	    

		 return appTraffics1;
	}
	
	
	
	
	
	
	/*
	 * 热门手机分布图
	 * @see com.redhadoop.dao.AppTrafficDao#getheatMobile(com.redhadoop.entity.User, com.redhadoop.entity.AppTraffic)
	 */
	@Override
	public List<AppTraffic> getheatMobile(User user, AppTraffic appTraffic) {
		List<AppTraffic> appTraffics=new ArrayList<AppTraffic>();
		final List<AppTraffic> appTraffics1=new ArrayList<AppTraffic>();
		
		  //起始经度
		  double itemLng=appTraffic.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(appTraffic.getGpslat());
	      //经度分片大小
	      double tileLng=appTraffic.getTileLng();
	      //维度分片大小
	      double tileLat=appTraffic.getTileLat();
	      
	      double endlng=0;

	       
	      
	      
	      String sql="";
	      
	      int a=0;
	    for (int i = 0; i < 25; i++) {
	    	for (int j = 0; j < 12; j++) {
	    		endlng=itemLng+tileLng;
	    		
	    	 final	double  TableCenterLng=itemLng+tileLng/2;
	    	 final	double TableCenterLat=itemLat-tileLat/2;
	    		  if ("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	      }else if("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and network_type='"+appTraffic.getNetworkType()+"'   GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }
	  	 	  //不是管理员
	  	 	  else if ((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name= '"+appTraffic.getNetwork_name()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	      }else if((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name= '"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and  network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,companyModel from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"' GROUP BY companyModel ORDER BY heat desc LIMIT 1";
	  	 	  }
	    		  a++;
	    		   
		    	long startTimeMills=System.currentTimeMillis();
	    		appTraffics = jdbcTemplate.query(sql,new RowMapper<AppTraffic>() {
	    					@Override
	    					public AppTraffic mapRow(ResultSet rs, int rowNum)
	    							throws SQLException {
	    					
	    						AppTraffic appTraffic1 = new AppTraffic();
	    						appTraffic1.setCompanyModel(rs.getString("companyModel"));
	    						appTraffic1.setCount((int)rs.getDouble("heat"));
	    						System.out.println(rs.getString("companyModel")+"     "+rs.getDouble("heat"));
	    						appTraffic1.setTableCenterLng(TableCenterLng);
	    						appTraffic1.setTableCenterLat(TableCenterLat);
	    						appTraffics1.add(appTraffic1);
	    						return appTraffic1;
	    					}
	    				});

	    		long endTimeMills=System.currentTimeMillis();
	    		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

	    		 itemLat=itemLat-tileLat;
	    	}
	    	itemLat=Double.parseDouble(appTraffic.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
	    
	    System.out.println(appTraffics1.size()+">>>>>>>>>>>>>>>>");
		 return appTraffics1;
	}

	
	
	/*
	 * 热门OS
	 * @see com.redhadoop.dao.AppTrafficDao#getheatOS(com.redhadoop.entity.User, com.redhadoop.entity.AppTraffic)
	 */
	@Override
	public List<AppTraffic> getheatOS(User user, AppTraffic appTraffic) {
		List<AppTraffic> appTraffics=new ArrayList<AppTraffic>();
		final List<AppTraffic> appTraffics1=new ArrayList<AppTraffic>();
		
		  //起始经度
		  double itemLng=appTraffic.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(appTraffic.getGpslat());
	      //经度分片大小
	      double tileLng=appTraffic.getTileLng();
	      //维度分片大小
	      double tileLat=appTraffic.getTileLat();
	      
	      double endlng=0;

	      
	      
	      String sql="";
	      
	      int a=0;
	    for (int i = 0; i < 25; i++) {
	    	for (int j = 0; j < 12; j++) {
	    		endlng=itemLng+tileLng;
	    		
	    	 final	double  TableCenterLng=itemLng+tileLng/2;
	    	 final	double TableCenterLat=itemLat-tileLat/2;
	    		  if ("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no'  and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	      }else if("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and network_type='"+appTraffic.getNetworkType()+"'   GROUP BY os, os_version, os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }
	  	 	  //不是管理员
	  	 	  else if ((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name= '"+appTraffic.getNetwork_name()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	      }else if((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name= '"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and  network_name='"+appTraffic.getNetwork_name()+"'  GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,os, os_version from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"' GROUP BY os, os_version,os_andversion ORDER BY heat desc LIMIT 1";
	  	 	  }
	    		  a++;
		    		long startTimeMills=System.currentTimeMillis();
	    		appTraffics = jdbcTemplate.query(sql,new RowMapper<AppTraffic>() {
	    					@Override
	    					public AppTraffic mapRow(ResultSet rs, int rowNum)
	    							throws SQLException {
	    					
	    						AppTraffic appTraffic1 = new AppTraffic();
	    						appTraffic1.setOs(rs.getString("os"));
	    						appTraffic1.setOs_version(rs.getString("os_version"));
	    						appTraffic1.setCount((int)rs.getDouble("heat"));
	    						appTraffic1.setTableCenterLng(TableCenterLng);
	    						appTraffic1.setTableCenterLat(TableCenterLat);
	    						appTraffics1.add(appTraffic1);
	    						return appTraffic1;
	    					}
	    				});
	    		long endTimeMills=System.currentTimeMillis();
	    		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

	    		 itemLat=itemLat-tileLat;
	    	}
	    	itemLat=Double.parseDouble(appTraffic.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
	    
		 return appTraffics1;
	}
	
	
	

	
	/*
	 * (non-Javadoc)用户附近热门手机分布图
	 * @see com.redhadoop.dao.AppTrafficDao#getUserNearOS(com.redhadoop.entity.User, com.redhadoop.entity.AppTraffic)
	 */
	@Override
	public List<AppTraffic> getUserNearOS(User user, AppTraffic appTraffic) {
		// TODO Auto-generated method stub
		List<AppTraffic> appTraffics=new ArrayList<AppTraffic>();
		final List<AppTraffic> appTraffics1=new ArrayList<AppTraffic>();
		
		  //起始经度
		  double itemLng=appTraffic.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(appTraffic.getGpslat());
	      //经度分片大小
	      double tileLng=appTraffic.getTileLng();
	      //维度分片大小
	      double tileLat=appTraffic.getTileLat();
	      
	      double endlng=0;

	       
	      
	      
	      String sql="";
	      
	      int a=0;
	    for (int i = 0; i < 3; i++) {
	    	for (int j = 0; j < 3; j++) {
	    		endlng=itemLng+tileLng;
	    		
	    	 final	double  TableCenterLng=itemLng+tileLng/2;
	    	 final	double TableCenterLat=itemLat-tileLat/2;
	    		  if ("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and OS_ANDVERSION <> 'unknown'  GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	      }else if("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_type='"+appTraffic.getNetworkType()+"'  and OS_ANDVERSION <> 'unknown'  GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and OS_ANDVERSION <> 'unknown'  GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and network_type='"+appTraffic.getNetworkType()+"'   and OS_ANDVERSION <> 'unknown'  GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }
	  	 	  //不是管理员
	  	 	  else if ((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name= '"+appTraffic.getNetwork_name()+"'  and OS_ANDVERSION <> 'unknown'  GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	      }else if((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name= '"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"'   and OS_ANDVERSION <> 'unknown' GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and  network_name='"+appTraffic.getNetwork_name()+"'   and OS_ANDVERSION <> 'unknown' GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,OS_ANDVERSION,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"'  and OS_ANDVERSION <> 'unknown' GROUP BY OS_ANDVERSION,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }
	    		  a++;
		    	long startTimeMills=System.currentTimeMillis();
	    		appTraffics = jdbcTemplate.query(sql,new RowMapper<AppTraffic>() {
	    					@Override
	    					public AppTraffic mapRow(ResultSet rs, int rowNum)
	    							throws SQLException {
	    					
	    						AppTraffic appTraffic1 = new AppTraffic();
	    						appTraffic1.setOsAndVersion(rs.getString("OS_ANDVERSION"));
	    						appTraffic1.setCount((int)rs.getDouble("heat"));
	    						appTraffic1.setTableCenterLng(Double.parseDouble(rs.getString("user_lon")));
	    						appTraffic1.setTableCenterLat(Double.parseDouble(rs.getString("user_lat")));
	    						appTraffic1.setTableCenterLng(TableCenterLng);
	    						appTraffic1.setTableCenterLat(TableCenterLat);
	    						appTraffics1.add(appTraffic1);
	    						return appTraffic1;
	    					}
	    				});
	    		long endTimeMills=System.currentTimeMillis();
	    		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

	    		 itemLat=itemLat-tileLat;
	    	}
	    	itemLat=Double.parseDouble(appTraffic.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
	    
		 return appTraffics1;
	}
	 
	
/*
 * (non-Javadoc)
 * 
 * @see com.redhadoop.dao.AppTrafficDao#getUserNearapp(com.redhadoop.entity.User, com.redhadoop.entity.AppTraffic)
 */
	//用户附近热门app分布图
	@Override
	public List<AppTraffic> getUserNearapp(User user, AppTraffic appTraffic) {
		// TODO Auto-generated method stub
		List<AppTraffic> appTraffics=new ArrayList<AppTraffic>();
		final List<AppTraffic> appTraffics1=new ArrayList<AppTraffic>();
		
		  //起始经度
		  double itemLng=appTraffic.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(appTraffic.getGpslat());
	      //经度分片大小
	      double tileLng=appTraffic.getTileLng();
	      //维度分片大小
	      double tileLat=appTraffic.getTileLat();
	      
	      double endlng=0;

	      
	      
	      String sql="";
	      
	      int a=0;
	    for (int i = 0; i < 3; i++) {
	    	for (int j = 0; j < 3; j++) {
	    		endlng=itemLng+tileLng;
	    		
	    	 final	double  TableCenterLng=itemLng+tileLng/2;
	    	 final	double TableCenterLat=itemLat-tileLat/2;
	    		  if ("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    		sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	      }else if("admin".equals(user.getRole()) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_type='"+appTraffic.getNetworkType()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if("admin".equals(user.getRole()) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+ (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"' and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name='"+appTraffic.getNetwork_name()+"'  and network_type='"+appTraffic.getNetworkType()+"'  and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }
	  	 	  //不是管理员
	  	 	  else if ((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && "ALL".equals(appTraffic.getNetworkType())) {
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and network_name= '"+appTraffic.getNetwork_name()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	      }else if((!"admin".equals(user.getRole())) && "ALL".equals(appTraffic.getNetwork_name()) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name= '"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && ("ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"' and  network_name='"+appTraffic.getNetwork_name()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }else if((!"admin".equals(user.getRole())) &&(!"ALL".equals(appTraffic.getNetwork_name())) && (!"ALL".equals(appTraffic.getNetworkType()))){
	  	    	  sql="SELECT SUM(upload_traffic)+SUM(download_traffic) as heat ,app_name,user_lon,user_lat from app_traffic where allMobileTraffic='no' and  user_lon>'"+ itemLng+ "' and user_lon< '"+ endlng+ "' and user_lat> '"+  (itemLat-tileLat)+ "' and user_lat< '"+ itemLat+"'  and start_time > '"+appTraffic.getStartDate()+"'  and end_time <  '"+appTraffic.getEndDate()+"'  and network_name='"+appTraffic.getNetwork_name()+"' and  network_type='"+appTraffic.getNetworkType()+"' and app_name <> 'phoneTotalTraffic'  GROUP BY app_name,user_lon,user_lat ORDER BY heat desc LIMIT 1";
	  	 	  }
	    		  a++;
		    		long startTimeMills=System.currentTimeMillis();
	    		appTraffics = jdbcTemplate.query(sql,new RowMapper<AppTraffic>() {
	    					@Override
	    					public AppTraffic mapRow(ResultSet rs, int rowNum)
	    							throws SQLException {
	    					
	    						AppTraffic appTraffic1 = new AppTraffic();
	    						appTraffic1.setApp_name(rs.getString("app_name"));
	    						appTraffic1.setCount((int)rs.getDouble("heat"));
	    						appTraffic1.setTableCenterLng(Double.parseDouble(rs.getString("user_lon")));
	    						appTraffic1.setTableCenterLat(Double.parseDouble(rs.getString("user_lat")));
	    						appTraffic1.setTableCenterLng(TableCenterLng);
	    						appTraffic1.setTableCenterLat(TableCenterLat);
	    						appTraffics1.add(appTraffic1);
	    						return appTraffic1;
	    					}
	    				});
	    		long endTimeMills=System.currentTimeMillis();
	    		logger.info("report forms:App Traffic Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+appTraffics.size()+"\tline:");

	    		 itemLat=itemLat-tileLat;
	    	}
	    	itemLat=Double.parseDouble(appTraffic.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
	    
		 return appTraffics1;
	}
	
	 
	
	
	/*
 	 * 下面是get、set方法
 	 */
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	

}
