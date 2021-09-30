package cn.itcast.dao.impl;

import cn.itcast.dao.NWQualityDao;
import cn.itcast.entity.NWQuality;
import cn.itcast.entity.TableCounts;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.util.DaoImplUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(value = "nwQualityDao1")
public class NWQualityDaoImpl implements NWQualityDao {

	private JdbcTemplate jdbcTemplate;
	private static Logger logger = Logger.getLogger(NWQualityDaoImpl.class);

	// 获取NWQuality总数
	@SuppressWarnings("deprecation")//表示不检测过期的方法
	
	public long getNWQualityCount(User user) {
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			sql = "select count(*) from NWQuality1";
		}else {
			sql = "select count(*) from NWQuality1 where NWOperator = ?";
		}	
		return jdbcTemplate.queryForLong(sql,
				//先判断用户是否是admin
				VarDesc.ROLE.ADMIN.equals(user.getRole()) ? 
						 new Object[] {}
						: new Object[] {user.getNWOperator() } );
	}
	// NWQuality Signal_Strength DataConnection 三表总数
	public List<TableCounts> getTableCounts(User user){
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			//sql = "SELECT SUM(NWQuality_count), SUM(Signal_Strength_count), SUM(DataConnection_count),DATETIME FROM tb_counts GROUP BY DATETIME ORDER BY DATETIME DESC LIMIT 1";
			sql = "SELECT NWQuality_count,Signal_Strength_count,DataConnection_count,DATETIME FROM tb_counts where NWOperator ='ALL' GROUP BY DATETIME ORDER BY DATETIME DESC LIMIT 1";
		}else {
			sql = "SELECT NWQuality_count,Signal_Strength_count,DataConnection_count FROM tb_counts WHERE NWOperator=? ORDER BY DATETIME DESC LIMIT 1";
		}
		List<TableCounts> tableCounts = jdbcTemplate.query(sql,VarDesc.ROLE.ADMIN.equals(user.getRole()) ?
				new Object[]{}:new Object[] {user.getNWOperator() },
				new RowMapper<TableCounts>(){

					@Override
					public TableCounts mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						TableCounts tableCount = new TableCounts();
						tableCount.setNWQulity_count(rs.getInt(1));
						tableCount.setSignal_Strength_count(rs.getInt(2));
						tableCount.setDataConnection_count(rs.getInt(3));
						
						return tableCount;
					}
				});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:NQ SS SC counts\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+tableCounts.size()+"\tline:");
	
		return tableCounts;
	}

	// 连接点数排名
	public List<NWQuality> getPointRank(NWQuality nwQuality, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(nwQuality.getNwOperator())) {
			sql = "select count(*) as count,Province from NWQuality where dayTime >= ? and dayTime <= ? group by Province order by count desc limit 10";
		} else {
			sql = "select count(*) as count,Province from NWQuality where dayTime >= ? and dayTime <= ? and NWOperator = ? group by Province order by count desc limit 10";
		}
		List<NWQuality> nwQualities = jdbcTemplate.query(
				sql,
				//先判断用户是否是admin
				VarDesc.ROLE.ADMIN.equals(user.getRole()) ? 
						
						//在判断选择的运营商
						VarDesc.OPERATOR.ALL.equals(nwQuality.getNwOperator()) 
						? new Object[] {nwQuality.getStartDate()*100, nwQuality.getEndDate()*100 }
						: new Object[] { nwQuality.getStartDate()*100,nwQuality.getEndDate()*100,nwQuality.getNwOperator() } 
						: new Object[] {nwQuality.getStartDate()*100, nwQuality.getEndDate()*100,user.getNWOperator() },
						new RowMapper<NWQuality>() {
					@Override
					public NWQuality mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						NWQuality nwQuality = new NWQuality();
						nwQuality.settCount(rs.getInt("count"));
						nwQuality.setProvince(rs.getString("Province"));
						return nwQuality;
					}
				});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Test Point Rank\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities;
	}
	/**
	  网络速率直方图
	 */
	public List<NWQuality> getNetworkRateRank(NWQuality nwQualityBean) {
		final String speedType=nwQualityBean.getSpeedType();
		String nwOperator=nwQualityBean.getNwOperator(); 
		int beginDate=nwQualityBean.getStartDate()*100;
		int endDate=nwQualityBean.getEndDate()*100;
		
		String sqlTop=null;
		String sql=null;
		if(VarDesc.OPERATOR.ALL.equals(nwOperator)){
			sqlTop="SELECT  AVG("+speedType+") AS  speed ,province FROM NWQuality  WHERE daytime>=? AND daytime<=? AND NWOperator in ('CMCC','CUCC','CTCC') GROUP BY  province order by speed desc LIMIT 10";
		}else{
			sqlTop="SELECT  AVG("+speedType+") AS  speed ,province FROM NWQuality  WHERE daytime>=? AND daytime<=? AND NWOperator='"+nwOperator+"' GROUP BY  province order by speed desc LIMIT 10";
			}
		/*
		 *@method:从数据库获取前几行数据
		 *@param:Object[]占位符的参数topRow获取前几行数据new Object[] {beginDate,endDate}
		 *@param:sqlfield数据库对应的字段
		 *@return:返回值是一个sql中的in的内容的一个字符串
		 */
		String sqlInVal=DaoImplUtil.getSqlInVal(jdbcTemplate,sqlTop,new Object[]{beginDate,endDate},"Province");

		if(sqlInVal!=""){
			if(VarDesc.OPERATOR.ALL.equals(nwOperator)){
				sql="SELECT  AVG("+speedType+") AS  speed ,province, NWOperator FROM NWQuality  WHERE daytime>=? AND daytime<=? AND NWOperator in ('CMCC','CUCC','CTCC') "+sqlInVal+"  GROUP BY  province,NWOperator order by speed desc ";	
			}else{
				sql="SELECT  AVG("+speedType+") AS  speed ,province, NWOperator FROM NWQuality  WHERE daytime>=? AND daytime<=?  AND NWOperator='"+nwOperator+"' "+sqlInVal+"  GROUP BY  province,NWOperator order by speed desc ";	
			}	
		}else{
			return null;
		}

		List<NWQuality> nwQualities = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<NWQuality>() {
							@Override
							public NWQuality mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								NWQuality nwQuality = new NWQuality();
								nwQuality.setSpeed((long)Math.abs(rs.getDouble("speed")));
								nwQuality.setProvince(rs.getString("Province"));
								nwQuality.setNwOperator(rs.getString("NWOperator"));
								return nwQuality;

							}
						});
		logger.info("report forms:NWQuality\t 网络速率直方图\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities.size() != 0 ? nwQualities : null;
	}
	
	/**
	 APP流量直方图
	 */
	public List<NWQuality> getTerminalTrafficRateRank(NWQuality nwQualityBean, User user) {
			final String speedType=nwQualityBean.getSpeedType();
			String nwOperator=nwQualityBean.getNwOperator(); 
			int beginDate=nwQualityBean.getStartDate()*100;
			int endDate=nwQualityBean.getEndDate()*100;
			
			String sql=null;
			if(VarDesc.ROLE.ADMIN.equals(user.getRole())&&VarDesc.OPERATOR.ALL.equals(nwOperator)){
				sql="SELECT  AVG("+speedType+") AS  speed ,DeviceCompany,DeviceModel FROM NWQuality  WHERE daytime>=? AND daytime<=? AND NWOperator in ('CMCC','CUCC','CTCC') GROUP BY  DeviceCompany , DeviceModel order by speed desc LIMIT 10 ;";
			}else{
				sql="SELECT  AVG("+speedType+") AS  speed ,DeviceCompany,DeviceModel FROM NWQuality  WHERE daytime>=? AND daytime<=?  AND NWOperator='"+nwOperator+"' GROUP BY  DeviceCompany , DeviceModel  order by speed desc LIMIT 10 ;";
			}
			List<NWQuality> nwQualities = jdbcTemplate
					.query(sql,new Object[] {beginDate,endDate},
							new RowMapper<NWQuality>() {
								@Override
								public NWQuality mapRow(ResultSet rs, int rowNum)
										throws SQLException {
									NWQuality nWQuality = new NWQuality();
									nWQuality.setSpeed((long)Math.abs(rs.getDouble("speed")));
									nWQuality.setDeviceCompany(rs.getString("DeviceCompany"));
									nWQuality.setDeviceModel(rs.getString("DeviceModel"));
									return nWQuality;

								}
							});
			return nwQualities.size() != 0 ? nwQualities : null;
		}
	/**
	 网络质量统计直方图
	 */
	public List<NWQuality> getNetworkQualityStatistics(NWQuality nwQualityBean,
			User user) {

		final String speedType=nwQualityBean.getSpeedType();
		String nwOperator=nwQualityBean.getNwOperator();
		//int beginDate=nwQualityBean.getStartDate();
		//int endDate=nwQualityBean.getEndDate();
		int beginDate=nwQualityBean.getStartDate()*100;
		int endDate=nwQualityBean.getEndDate()*100;
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())&&VarDesc.OPERATOR.ALL.equals(nwOperator)){
			sql="SELECT  AVG("+speedType+") AS speed ,NWOperator,NWType FROM NWQuality  WHERE daytime>=? AND daytime<=? AND NWOperator in ('CMCC','CUCC','CTCC') AND NWType!='null' GROUP BY  NWOperator,NWType order by speed desc";
		}else{
			sql="SELECT  AVG("+speedType+") AS speed ,NWOperator,NWType FROM NWQuality  WHERE daytime>=? AND daytime<=?  AND NWOperator='"+nwOperator+"' AND NWType!='null' GROUP BY  NWOperator,NWType  order by speed desc";
		}
		List<NWQuality> nwQualities = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<NWQuality>() {
							@Override
							public NWQuality mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								NWQuality nWQuality = new NWQuality();
								nWQuality.setSpeed((long)Math.abs(rs.getDouble(1)));
								nWQuality.setNwOperator((null != rs.getString(2) && !"NULL".equals(rs.getString(2).toUpperCase())) ? rs.getString(2) : "Others");
								nWQuality.setNwType((null != rs.getString(3) && !"NULL".equals(rs.getString(3).toUpperCase())) ? rs.getString(3) : "Others");
								return nWQuality;
							}
						});
		logger.info("report forms:NWQuality\t 网络质量统计直方图\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities.size() != 0 ? nwQualities : null;
	}

	/**
	 LandMark网络质量跟踪图
	 */
	@Override
	public List<NWQuality> getNetworkQualityTracks(NWQuality nwQualityBean, User user) {
		final String speedType=nwQualityBean.getSpeedType();
		String nwType=nwQualityBean.getNwType(); 
		String nwOperator=nwQualityBean.getNwOperator(); 
		String landmark=nwQualityBean.getLandmark();
		int beginDate=Integer.valueOf(String.valueOf(nwQualityBean.getStartDate()).substring(0, 10));
		int endDate=Integer.valueOf(String.valueOf(nwQualityBean.getEndDate()).substring(0, 10));
		int startPos=0;

		if(VarDesc.AXISTYPE.HOUR.equals(nwQualityBean.getX_type())){
			startPos=9;
	 	}else if(VarDesc.AXISTYPE.DAY.equals(nwQualityBean.getX_type())){
			startPos=7;
		}else if(VarDesc.AXISTYPE.MONTH.equals(nwQualityBean.getX_type())){
			startPos=5;
		}
		
		String sql=null;
		//SUBSTR(TO_CHAR(daytime,'#'),7,2) 
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())){
			sql="SELECT  AVG("+speedType+") AS speed, SUBSTR(TO_CHAR(daytime,'#'), "+startPos+",2) AS x_rate,NWOperator FROM  NWQuality WHERE daytime>=?  AND daytime<=? AND nwType='"+nwType+"' AND NWOperator in ('CMCC','CUCC','CTCC') AND landmark='"+landmark+"' GROUP BY x_rate,NWOperator";
		}else{
			nwOperator=user.getNWOperator(); 
			sql="SELECT  AVG("+speedType+") AS speed, SUBSTR(TO_CHAR(daytime,'#'), "+startPos+",2) AS x_rate,NWOperator FROM  NWQuality WHERE daytime>=?  AND daytime<=? AND nwType='"+nwType+"'  AND landmark='"+landmark+"' AND NWOperator='"+nwOperator+"' GROUP BY x_rate,NWOperator";
		}

		List<NWQuality> nwQualities = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<NWQuality>() {
							@Override
							public NWQuality mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								NWQuality nWQuality = new NWQuality();
								nWQuality.setSpeed((long)Math.abs(rs.getDouble("speed")));
								nWQuality.setX_rate(rs.getString("x_rate"));
								nWQuality.setNwOperator(rs.getString("NWOperator"));
								return nWQuality;

							}
						});
		logger.info("report forms:NWQuality\t LandMark网络质量跟踪图\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities.size() != 0 ? nwQualities : null;
	}
	

	//网络质量瓷砖图
	@Override
	public List<Object> showNwQuaTileSpeed(User user, NWQuality nwQuality) {
		List<Object> nwQualities=new ArrayList<Object>();
		
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		
		  //起始经度
		  double itemLng=nwQuality.getMinGpslon();
		  //起始维度
	      double itemLat=Double.parseDouble(nwQuality.getGpslat());
	      //经度分片大小
	      double tileLng=nwQuality.getTileLng();
	      //维度分片大小
	      double tileLat=nwQuality.getTileLat();

	      String sql=null;
	      if ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) && "ALL".equals(nwQuality.getNwType())) {
	    		sql="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? ";
			}else if("ALL".equals(nwQuality.getNwType())){
				sql="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? and NWOperator=? ";
			}else{
				sql="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? and NWOperator=? and NWType=?";
			}
	      
		    String strMinLat=null;
	  		String strMaxLat=null;
	  		String strMinLon=null;
	  		String strMaxLon=null;
	  		
	  		int a=0;
	  		
		   for (int i = 0; i < 25; i++) {
	    	for (int j = 0; j < 12; j++) {
	    		a++;
	    		strMinLat=Double.toString(itemLat-tileLat);
	    		strMaxLat=Double.toString(itemLat);
	    		strMinLon=Double.toString(itemLng);
	    		strMaxLon=Double.toString(itemLng+tileLng);
	    		System.out.println(a+"   "+sql+ "   @@@   "+strMinLat+"       "+strMaxLat+"       "+strMinLon+"       "+strMaxLon+"       "+nwQuality.getStartDate()+"       "+nwQuality.getEndDate()+"       "+nwQuality.getNwOperator()+"       "+nwQuality.getNwType());  			
	    		  
	    		Double double1=	jdbcTemplate.queryForObject(sql, ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) && "ALL".equals(nwQuality.getNwType()))?new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getStartDate(),nwQuality.getEndDate()}:"ALL".equals(nwQuality.getNwType())?new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getStartDate(),nwQuality.getEndDate(),nwQuality.getNwOperator()}:new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getStartDate(),nwQuality.getEndDate(),nwQuality.getNwOperator(),nwQuality.getNwType()}, Double.class);
	    		   itemLat=itemLat-tileLat;
	    			//System.out.println(a+"   "+sql+ "   @@@   "+strMinLat+"       "+strMaxLat+"       "+strMinLon+"       "+strMaxLon+"       "+nwQuality.getStartDate()+"       "+nwQuality.getEndDate()+"       "+nwQuality.getNwOperator()+"       "+nwQuality.getNwType());  			
	    		 System.out.println("@@@@@"+double1+"@@@@@");
	    		 nwQualities.add(double1);
	    		  
	    	}
	    	
	    	itemLat=Double.parseDouble(nwQuality.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
		
	    long endTimeMills=System.currentTimeMillis();
	    logger.info("report forms:Network Quality Map\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
		 return nwQualities;
	}
	//用户瓷砖图
	//@Override
	public List<NWQuality> showAllUserPointTileGeog(User user, NWQuality nwQuality) {
		final List<NWQuality> NWQualitys=new ArrayList<NWQuality>();
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		
		//List<Object> nwQualities=new ArrayList<Object>();
		//起始经度
	
		  double itemLng=nwQuality.getMinGpslon()-0.025;

		  //起始维度

	      double itemLat=nwQuality.getMinGpslat()+0.01;

	      //经度分片大小
	      double tileLng=nwQuality.getTileLng();
	      //维度分片大小
	      double tileLat=nwQuality.getTileLat();
	      
	      
	      long startDaytime=Long.parseLong(nwQuality.getStartDateToSecond());
	      long endDaytime=Long.parseLong(nwQuality.getEndDateToSecond());
	      
	      String sqlM=null;
	      String sqlU=null;
	      String sqlT=null;
	      String sql=null;
	      if ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) ) {
	    		sqlM="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? and NWOPERATOR='CMCC'";
	    		sqlU="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? and NWOPERATOR='CUCC'";
	    		sqlT="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<? and daytime>=? and daytime<=? and NWOPERATOR='CTCC'";
	      
	      }else{
				sql="select avg("+nwQuality.getSpeedType()+")  from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<?  and NWOperator=? and daytime>=? and daytime<=?";
			}
	      
	      
	    String strMinLat=null;
  		String strMaxLat=null;
  		String strMinLon=null;
  		String strMaxLon=null;
  		
		String speedLon=null;
  		String speedLat=null;
  		
  
	    for (int i = 0; i < 3; i++) {
	    	for (int j = 0; j < 3; j++) {
	    		
	    		NWQuality NWQuality1=new NWQuality();

				// todo
//	    		strMinLat=Double.toString(itemLat-tileLat);as speed,CompanyModel,NWOperator from NWQUALITY where CompanyModel
	    		strMaxLat=Double.toString(itemLat);
	    		strMinLon=Double.toString(itemLng);
	    		strMaxLon=Double.toString(itemLng+tileLng);
	    		
	    		speedLon=Double.toString((itemLng+tileLng/2));
	    		speedLat=Double.toString((itemLat-tileLat/2));
	    		String datas = null;
	    		
	    		Double doubleM=	jdbcTemplate.queryForObject(sqlM, ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) )?new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,startDaytime,endDaytime}:new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getNwOperator(),startDaytime,endDaytime}, Double.class);
	    		Double doubleU=	jdbcTemplate.queryForObject(sqlU, ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) )?new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,startDaytime,endDaytime}:new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getNwOperator(),startDaytime,endDaytime}, Double.class);
	    		Double doubleT=	jdbcTemplate.queryForObject(sqlT, ("admin".equals(user.getRole()) && "ALL".equals(nwQuality.getNwOperator()) )?new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,startDaytime,endDaytime}:new Object[]{strMinLat,strMaxLat,strMinLon,strMaxLon,nwQuality.getNwOperator(),startDaytime,endDaytime}, Double.class);
	    		if (doubleM==null){
	    			doubleM=0.0;
	    		};
	    		if (doubleU==null){
	    			doubleU=0.0;
	    		};
	    		if (doubleT==null){
	    			doubleT=0.0;
	    		};
	    		/////////////////////
	    		
	    		if (doubleM!=0.0){
	    			datas="CMCC: "+doubleM+"KB/s ";
	    		}
	    		if (doubleU!=0.0&&doubleU>doubleM&&doubleU>doubleT){
	    			datas="CUCC: "+doubleU+"KB/s ";
	    		}
	    		if (doubleT!=0.0&&doubleT>doubleM&&doubleT>doubleU){
	    			datas="CTCC: "+doubleT+"KB/s ";
	    		}
	    		/////////////////////
	    		//datas="CMCC: "+doubleM+"KB/s "+"#CUCC: "+doubleU+"KB/s "+"#CTCC: "+doubleT+"KB/s ";
	    		
	    		NWQuality1.setGpslon(speedLon);
	    		NWQuality1.setGpslat(speedLat);
	    		NWQuality1.setSpeedss(datas);
	    		NWQualitys.add(NWQuality1);
	    		
	    		itemLat=itemLat-tileLat;
	    		   //nwQualities.add(double1);
	    	}
	    	
	    	itemLat=nwQuality.getMinGpslat()+0.01;
	    	itemLng=itemLng+tileLng;
	    } 
	    long endTimeMills=System.currentTimeMillis();
	    logger.info("report forms:Test poinsts tile map for User\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+NWQualitys.size()+"\tline:");
	System.out.println(NWQualitys.size());
		 return NWQualitys;///nwQualities;
	}
	//手机网络质量瓷砖图
	@Override
	public List<Object> showTePhNwQuaTileMap(User user, NWQuality nwQuality) {
		List<Object> nwQualities=new ArrayList<Object>();
		
		  double itemLng=nwQuality.getMinGpslon();
	      double itemLat=Double.parseDouble(nwQuality.getGpslat());
	      double tileLng=nwQuality.getTileLng();
	      double tileLat=nwQuality.getTileLat();
	    for (int i = 0; i < 50; i++) {
	    	for (int j = 0; j < 25; j++) {
	    		Double double1=	jdbcTemplate.queryForObject("select avg(DLSpeed) as avgspeed from NWQuality  where GpsLat>? and GpsLat<=? and GpsLon>=? and GpsLon<?", new Object[]{itemLat-tileLat,itemLat,itemLng,itemLng+tileLng}, Double.class);
	    		   itemLat=itemLat-tileLat;
	    		   nwQualities.add(double1);
	    	}
	    	itemLat=Double.parseDouble(nwQuality.getGpslat());
	    	itemLng=itemLng+tileLng;
	    } 
			 return nwQualities;
	}
	
	// 热门手机网络质量排名
	public List<NWQuality> getMobileNetworkQualityRank2D(NWQuality nwQuality, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				if(VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwOperator())){
					sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}else{
					sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator = '"+ nwQuality.getNwOperator() +"' and CompanyModel <> 'NULL' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}
									
			}else {
				if(VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwOperator())){
					sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and NWType = '"+nwQuality.getNwType()+"' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}else{
					sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator = '"+ nwQuality.getNwOperator() +"' and CompanyModel <> 'NULL' and NWType = '"+nwQuality.getNwType()+"' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}
			}
		} else {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where  dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and CompanyModel <> 'NULL' and NWOperator= '"+nwQuality.getNwOperator()+"'";
			}else {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and  CompanyModel <> 'NULL' and NWOperator= '"+user.getNWOperator()+"' and NWType = '"+nwQuality.getNwType();
			}
		}
		
		
		System.out.println("55555555555555555  "+sql);
		System.out.println("66666666666666666  "+ nwQuality.getSpeedType()+   "      "+nwQuality.getNwOperator()+"    "+ nwQuality.getNwType()+"      "+nwQuality.getStartDate()*100+"     "+nwQuality.getEndDate()*100);
		
		//group by
		sql += " group by CompanyModel,NWOperator";
		
		// order by speed desc limit 50
		if(VarDesc.SPEEDTYPE.LATENCY.equals(nwQuality.getSpeedType())){
 
			sql += " order by speed desc limit 50";
		}else{
			sql += " order by speed desc limit 50";
		}
		
		List<NWQuality> nwQualities = jdbcTemplate.query(sql,new RowMapper<NWQuality>() {
			@Override
			public NWQuality mapRow(ResultSet rs, int rowNum) throws SQLException {
				NWQuality nwQuality = new NWQuality();
				nwQuality.setSpeed(rs.getLong("speed"));
				nwQuality.setDeviceModel(rs.getString("CompanyModel"));
				nwQuality.setNwOperator(rs.getString("NWOperator"));
				return nwQuality;
			}
		});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Network Quality Rank3\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities;
	}
	public List<NWQuality> getMobileNetworkQualityRank(NWQuality nwQuality, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100+" group by CompanyModel,NWOperator order by speed desc limit 50";
			}else {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and NWType = '"+nwQuality.getNwType()+"' and dayTime >= "+nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100+" group by CompanyModel,NWOperator order by speed desc limit 50";
			}
		} else {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and  CompanyModel <> 'NULL' and NWOperator= '"+nwQuality.getNwOperator()+" group by CompanyModel,NWOperator order by speed desc limit 50";
			}else {
				sql = "select sum("+nwQuality.getSpeedType()+") as speed,CompanyModel,NWOperator from NWQUALITY where    dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and  CompanyModel <> 'NULL' and NWOperator= '"+user.getNWOperator()+"' and NWType = '"+nwQuality.getNwType()+" group by CompanyModel,NWOperator order by speed desc limit 50";
			}
		}
		List<NWQuality> nwQualities = jdbcTemplate.query(sql,new RowMapper<NWQuality>() {
			@Override
			public NWQuality mapRow(ResultSet rs, int rowNum) throws SQLException {
				NWQuality nwQuality = new NWQuality();
				nwQuality.setSpeed(rs.getLong("speed"));
				nwQuality.setDeviceModel(rs.getString("CompanyModel"));
				nwQuality.setNwOperator(rs.getString("NWOperator"));
				return nwQuality;
			}
		});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Network Quality Rank3\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities;
	}
	// 热门手机网络质量排名(时间段内总条数)
	@SuppressWarnings("deprecation")
	public int getMobileNQRankCounts2D(NWQuality nwQuality, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		//判断是否为admin用户
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			//判断网络制式是否为ALL（4G，3G，2G）
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				//判断运营商是否为ALL(CMCC,CUCC,CTCC)
				if (VarDesc.OPERATOR.ALL.equals(nwQuality.getNwOperator())) {
					sql = "select count(*) from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;					
				}else{
					sql = "select count(*) from NWQUALITY where NWOperator = '"+ nwQuality.getNwOperator() +"' and CompanyModel <> 'NULL' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;					
				}
			}else {
				if (VarDesc.OPERATOR.ALL.equals(nwQuality.getNwOperator())) {
					sql = "select count(*) from NWQUALITY where NWOPERATOR in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and NWType = '"+ nwQuality.getNwType() +"' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}else{
					sql = "select count(*) from NWQUALITY where NWOperator = '"+ nwQuality.getNwOperator() +"' and CompanyModel <> 'NULL' and NWType = '"+ nwQuality.getNwType() +"' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
				}
			}
		} else {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select count(*) from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and   CompanyModel <> 'NULL' and NWOperator= "+nwQuality.getNwOperator();
			}else {
				sql = "select count(*) from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and  CompanyModel <> 'NULL' and NWOperator= "+user.getNWOperator()+" and NWType = '"+nwQuality.getNwType();
			}
		}
 
		System.out.println(sql+"555555555555555");
		System.out.println(nwQuality.getNwOperator()+"    "+nwQuality.getNwType()+"    "+"6666666666666666");
		
		int i = jdbcTemplate.queryForInt(sql);
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Network Quality Rank1\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+1+"\tline:");
		return i;
	}
	@SuppressWarnings("deprecation")
	public int getMobileNQRankCounts(NWQuality nwQuality, User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select count(*) from NWQUALITY where NWOperator in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
			}else {
				sql = "select count(*) from NWQUALITY where NWOPERATOR in ('CMCC','CUCC','CTCC') and CompanyModel <> 'NULL' and NWType = '"+ nwQuality.getNwType() +"' and dayTime >= "+ nwQuality.getStartDate()*100+" and dayTime <= "+nwQuality.getEndDate()*100;
			}
		} else {
			if (VarDesc.NETWORKTYPE.ALL.equals(nwQuality.getNwType())) {
				sql = "select count(*) from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and   CompanyModel <> 'NULL' and NWOperator= "+nwQuality.getNwOperator();
			}else {
				sql = "select count(*) from NWQUALITY where   dayTime >= nwQuality.getStartDate()*100+ and dayTime <= nwQuality.getEndDate()*100  and  CompanyModel <> 'NULL' and NWOperator= "+user.getNWOperator()+" and NWType = '"+nwQuality.getNwType();
			}
		}
		int i = jdbcTemplate.queryForInt(sql);
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Mobile Device Network Quality Rank1\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+1+"\tline:");
		return i;
	}
	
	//典型地标网络质量统计图
	public List<NWQuality> getLandmarkNetworkQualityStatistics(NWQuality nwQuality,User user) {
		//起始时间
		long startTimeMills=System.currentTimeMillis();
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
				sql = "select avg("+nwQuality.getSpeedType()+") as speed,Landmark,NWOperator from NWQUALITY where  dayTime >= ? and dayTime <= ? and NWType = ? and NWOperator in ('CMCC','CUCC','CTCC') group by Landmark,NWOperator order by speed desc limit 50";
		} else {
				sql = "select avg("+nwQuality.getSpeedType()+") as speed,Landmark,NWOperator from NWQUALITY where  dayTime >= ? and dayTime <= ? and NWType = ? and NWOperator= ? group by Landmark,NWOperator order by speed desc limit 50";
		}
		List<NWQuality> nwQualities = jdbcTemplate.query(sql,
				VarDesc.ROLE.ADMIN.equals(user.getRole())
					?new Object[]{nwQuality.getStartDate()*100,nwQuality.getEndDate()*100,nwQuality.getNwType()}
		        	:new Object[]{nwQuality.getStartDate()*100,nwQuality.getEndDate()*100,nwQuality.getNwType(),user.getNWOperator()},
						new RowMapper<NWQuality>() {
			@Override
			public NWQuality mapRow(ResultSet rs, int rowNum) throws SQLException {
				NWQuality nwQuality = new NWQuality();
				nwQuality.setDspeed(rs.getDouble("speed"));
				nwQuality.setLandmark(rs.getString("Landmark"));
				nwQuality.setNwOperator(rs.getString("NWOperator"));
				return nwQuality;
			}
		});
		long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Landmark Network Quality Statistics\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
		return nwQualities;
	}
	//显示所有点的地理坐标
		@Override
		public List<NWQuality> showAllPointGeog(User user,NWQuality nwQuality) {
			 long startTimeMills=System.currentTimeMillis();
			String sql = null;
			
			if ("admin".equals(user.getRole())
					&& "ALL".equals(nwQuality.getNwOperator())) {
				sql = "select GpsLat,GpsLon,NWOperator from NWQuality where NWOperator in('CMCC','CUCC','CTCC') and dayTime >=? and dayTime <=? ";
		
			} else {
				sql = "select GpsLat,GpsLon,NWOperator from NWQuality where NWOperator in('CMCC','CUCC','CTCC') and dayTime >=? and dayTime <=? and NWOperator = ? ";
		
			}
			List<NWQuality> nwQualities = jdbcTemplate.query(
					sql,
					//先判断用户是否是admin
					"admin".equals(user.getRole()) ? 
							//在判断选择的运营商
							"ALL".equals(nwQuality.getNwOperator()) 
							? new Object[] {nwQuality.getStartDate(), nwQuality.getEndDate() }
							: new Object[] { nwQuality.getStartDate(),nwQuality.getEndDate(),nwQuality.getNwOperator() } 
							: new Object[] {nwQuality.getStartDate(), nwQuality.getEndDate(),user.getNWOperator() },
							new RowMapper<NWQuality>() {
								@Override
								public NWQuality mapRow(ResultSet rs, int rowNum)
										throws SQLException {
									NWQuality nw = new NWQuality();

									nw.setGpslat(rs.getString("GpsLat"));
									nw.setGpslon(rs.getString("GpsLon"));
									
									nw.setNwOperator(rs.getString("NWOperator"));									
									return nw;
								}

							});
			  long endTimeMills=System.currentTimeMillis();
			  logger.info("report forms:Test Point Map\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+nwQualities.size()+"\tline:");
			return nwQualities;
		}

		//查询NWQuality手机型号类型
		@Override
		public List<NWQuality> showTePhNwQuaType() {	
 
			List<NWQuality> nwQualities = jdbcTemplate.query("select  devicemodel from nwquality  where devicemodel not like '%None%' group by devicemodel",new Object[]{},
							new RowMapper<NWQuality>() {
								@Override
								public NWQuality mapRow(ResultSet rs, int rowNum)throws SQLException {
									NWQuality nw = new NWQuality();
									nw.setDeviceModel(rs.getString("devicemodel"));
									return nw;
								}
							});
			return nwQualities;
		}	

	/*
	 *  下面是get、set方法
 	 */


	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
