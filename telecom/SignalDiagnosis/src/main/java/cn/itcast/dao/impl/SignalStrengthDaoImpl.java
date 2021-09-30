package cn.itcast.dao.impl;

import cn.itcast.dao.SignalStrengthDao;
import cn.itcast.entity.SignalStrength;
import cn.itcast.entity.User;
import cn.itcast.entity.VarDesc;
import cn.itcast.util.DaoImplUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(value = "signalStrengthDao")
public class SignalStrengthDaoImpl implements SignalStrengthDao {
	private static Logger logger = LoggerFactory.getLogger(SignalStrengthDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	Connection conn = null;

	// 地图热图数据获取
	@Override
	public List<SignalStrength> getRssiHeatData(double smalllng, double biglng,
			double smalllat, double boglat, SignalStrength signalStrengths)
			throws SQLException {
		// TODO Auto-generated method stub

		// 定义查询数据的SQL语句
		String sql = "";
		// 系统当前日期，设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		// 获取一个月前的日期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
		// 将当前日期和一个月前的日期赋值给变量
		int today = Integer.parseInt(df.format(new Date()));
		int amonthago = Integer.parseInt(df.format(cal.getTime()));

		// 如果是管理员
		if ("admin".equals(signalStrengths.getUser())) {
			// 查询时间不是1天
			if (!signalStrengths.getIsoneday()) {
				// admin查询所有运营商时
				if (signalStrengths.getNetworkname().equalsIgnoreCase("all")&& (signalStrengths.getNetworktype().equalsIgnoreCase("all"))) {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index> "
							+ signalStrengths.getStartdate()
							+ " and  time_index < "
							+ signalStrengths.getStopdate()
							+" and rssi>=-120 and rssi<=1 "
							;

				}else if (signalStrengths.getNetworkname().equalsIgnoreCase("all")&& (!signalStrengths.getNetworktype().equalsIgnoreCase("all"))){
					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index> "
							+ signalStrengths.getStartdate()
							+ " and  time_index < "
							+ signalStrengths.getStopdate()
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_type='"
							+ signalStrengths.getNetworktype() + "'"
							;
				}
				// admin查询某一个运营商
				else {
					// 如果网络制式为all
					if (signalStrengths.getNetworktype()
							.equalsIgnoreCase("all")) {

						sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
								+ smalllng
								+ "' and user_lon< '"
								+ biglng
								+ "' and user_lat> '"
								+ smalllat
								+ "' and user_lat< '"
								+ boglat
								+ "' and time_index> "
								+ signalStrengths.getStartdate()
								+ " and  time_index < "
								+ signalStrengths.getStopdate()
								+" and rssi>=-120 and rssi<=1 "
								+ " and network_name='"
								+ signalStrengths.getNetworkname() + "'"
								;

					}
					// 选择了某一个网络制式
					else {

						sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
								+ smalllng
								+ "' and user_lon< '"
								+ biglng
								+ "' and user_lat> '"
								+ smalllat
								+ "' and user_lat< '"
								+ boglat
								+ "' and time_index> "
								+ signalStrengths.getStartdate()
								+ " and  time_index < "
								+ signalStrengths.getStopdate()
								+" and rssi>=-120 and rssi<=1 "
								+ " and network_name= '"
								+ signalStrengths.getNetworkname()
								+ "' and network_type='"
								+ signalStrengths.getNetworktype() + "'"
								;

					}
				}
			}
			// 查询的是一天
			else {
				// admin查询所有运营商时
				if (signalStrengths.getNetworkname().equalsIgnoreCase("all")&& (signalStrengths.getNetworktype().equalsIgnoreCase("all"))) {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index = "
							+ signalStrengths.getOneday()
							+" and rssi>=-120 and rssi<=1 "
							;

				}else if (signalStrengths.getNetworkname().equalsIgnoreCase("all")&& (!signalStrengths.getNetworktype().equalsIgnoreCase("all"))){
					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index = "
							+ signalStrengths.getOneday() 
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_type='"
							+ signalStrengths.getNetworktype()+ "'"
							;
				}
				// admin查询某一个运营商时
				else {
					// 查询运营商下的所有网络制式
					if (signalStrengths.getNetworktype()
							.equalsIgnoreCase("all")) {

						sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
								+ smalllng
								+ "' and user_lon< '"
								+ biglng
								+ "' and user_lat> '"
								+ smalllat
								+ "' and user_lat< '"
								+ boglat
								+ "' and time_index = "
								+ signalStrengths.getOneday()
								+" and rssi>=-120 and rssi<=1 "
								+ " and network_name='"
								+ signalStrengths.getNetworkname() + "'"
								;

					}
					// //查询运营商下的某一个网络制式
					else {

						sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
								+ smalllng
								+ "' and user_lon< '"
								+ biglng
								+ "' and user_lat> '"
								+ smalllat
								+ "' and user_lat< '"
								+ boglat
								+ "' and time_index = "
								+ signalStrengths.getOneday()
								+" and rssi>=-120 and rssi<=1 "
								+ " and network_name='"
								+ signalStrengths.getNetworkname()
								+ "' and network_type='"
								+ signalStrengths.getNetworktype() + "'"
								;
		}
	}
}
		}
		// 不是管理员(是运营商时)
		else {
			String user = signalStrengths.getUser();

			// 查询多天的
			if (!signalStrengths.getIsoneday()) {
				// 网络制式all
				if (signalStrengths.getNetworktype().equalsIgnoreCase("all")) {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index> "
							+ signalStrengths.getStartdate()
							+ " and  time_index < "
							+ signalStrengths.getStopdate()
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_name ='" + user + "'"
							;
				}
				// 某一个网络制式
				else {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index> "
							+ signalStrengths.getStartdate()
							+ " and  time_index < "
							+ signalStrengths.getStopdate()
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_name ='"
							+ user
							+ "' and network_type ='"
							+ signalStrengths.getNetworktype() + "'"
							;

				}
			}
			// 查询一天的
			else {
				// 网络制式 all
				if (signalStrengths.getNetworktype().equalsIgnoreCase("all")) {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index="
							+ signalStrengths.getOneday()
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_name ='" + user + "'"
							;

				}
				// 某一个网络制式
				else {

					sql = "select rssi,user_lon,user_lat,network_type  from Signal_Strength  where  user_lon> '"
							+ smalllng
							+ "' and user_lon< '"
							+ biglng
							+ "' and user_lat> '"
							+ smalllat
							+ "' and user_lat< '"
							+ boglat
							+ "' and time_index="
							+ signalStrengths.getOneday()
							+" and rssi>=-120 and rssi<=1 "
							+ " and network_name ='"
							+ user
							+ "' and network_type ='"
							+ signalStrengths.getNetworktype() + "'"
							;

				}
			}

		}
 		 long startTimeMills=System.currentTimeMillis();
		// 定义集合，以便接收查到的数据
		List<SignalStrength> signalStrength = new ArrayList<SignalStrength>();
		
		signalStrength = jdbcTemplate.query(sql,
				new RowMapper<SignalStrength>() {

					@Override
					public SignalStrength mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SignalStrength signalStrengths = new SignalStrength();
						signalStrengths.setLng(rs.getString("user_lon"));
						signalStrengths.setLat(rs.getString("user_lat"));
						Integer rssis=120+Integer.parseInt(rs.getString("rssi"));
						signalStrengths.setCount(rssis.toString());
						return signalStrengths;
					}

				});
		 long endTimeMills=System.currentTimeMillis();
		logger.info("report forms:Signal Strength Point Map\tsql query:"+sql+"\t"+"execution time:"+(endTimeMills-startTimeMills)/1000+"s\tcollection size:"+signalStrength.size()+"\tline:");
		
		return signalStrength;
	}
	
	
	
	
	
	
	
 	// SignalStrength总数
	@SuppressWarnings("deprecation")
	public long getSignalStrengthCount(User user) {
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())) {
			sql = "select count(*) from Signal_Strength1";
		} else {
			sql = "select count(*) from Signal_Strength1 where network_name = ?";
		}
		return jdbcTemplate
				.queryForLong(sql,// 先判断用户是否是admin
						VarDesc.ROLE.ADMIN.equals(user.getRole()) ?new Object[] {}
								: new Object[] { user.getNWOperator() });
	}

	// 获取某一个运营商的网络制式集合
	@Override
	public List<String> getnetworktype(String network_name) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT network_type FROM  Signal_Strength  where network_name='"
				+ network_name + "'";
		System.out.println("123456789       " + sql);
		List<String> networktypes = jdbcTemplate.query(sql,
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String networkType = rs.getString("network_type");
						return networkType;
					}
				});
		return networktypes.size() != 0 ? networktypes : null;

	}

 
	@Override
	public List<SignalStrength> getSignalStrengthStatistics(
			SignalStrength signalStrengthBean, User user) {
		String network_name = signalStrengthBean.getNetworkname();
		String network_type = signalStrengthBean.getNetworktype();
		long beginDate = signalStrengthBean.getStartDate()*100;
		long endDate = signalStrengthBean.getEndDate()*100;
		String sql = null;
		Map db_v =new HashMap();
		db_v.put("network_name", network_name);
 
		String sqlWhere=DaoImplUtil.getSqlWhere(db_v);

	sql="SELECT AVG(rssi) AS rssi,landmark,network_name,network_type FROM signal_strength WHERE  rssi>=-120 AND rssi<=0   AND network_type <>'null' AND time_index >=? And time_index<=? "+sqlWhere+" GROUP BY landmark,network_name,network_type";
		// /////////////////////////////测试sql和参数/////////////////////////////////
 		List<SignalStrength> signalStrengths = jdbcTemplate.query(sql,
				new Object[] { beginDate, endDate },
				new RowMapper<SignalStrength>() {
					@Override
					public SignalStrength mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SignalStrength signalStrength = new SignalStrength();
						signalStrength.setEchart_rssi((long)rs.getDouble("rssi"));
						signalStrength.setLandmark(rs.getString("landmark"));
						signalStrength.setNetworkname(rs.getString("network_name"));
						signalStrength.setNetworktype(rs.getString("network_type"));
						return signalStrength;
					}
				});

		//System.out.println(signalStrengths.size()+"---------------0000000000000000000000000000000");
		logger.info("report forms:SignalStrength\t  landmark网络质量统计直方图 counts\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+signalStrengths.size()+"\tline:");
		return signalStrengths.size() != 0 ? signalStrengths : null;
	}
	
 
	@Override
	public Object getSignalStrengthTracks(SignalStrength signalStrengthBean,
			User user) {
		String network_type=signalStrengthBean.getNetworktype(); 
		String network_name=signalStrengthBean.getNetworkname(); 
		String landmark=signalStrengthBean.getLandmark();
		int beginDate=Integer.valueOf(String.valueOf(signalStrengthBean.getStartDate()).substring(0, 10));
		int endDate=Integer.valueOf(String.valueOf(signalStrengthBean.getEndDate()).substring(0, 10));
		int startPos=0;
		if(VarDesc.AXISTYPE.HOUR.equals(signalStrengthBean.getX_type())){
			startPos=9;
	 	}else if(VarDesc.AXISTYPE.DAY.equals(signalStrengthBean.getX_type())){
			startPos=7;
		}else if(VarDesc.AXISTYPE.MONTH.equals(signalStrengthBean.getX_type())){
			startPos=5;
		}
		String sql=null;
		if(VarDesc.ROLE.ADMIN.equals(user.getRole())){
			if(VarDesc.NETWORKTYPE.ALL.equals(network_type)){
			sql="SELECT  AVG(rssi) rssi,SUBSTR(TO_CHAR(time_index,'#'), "+startPos+",2) AS x_rate,network_name FROM  signal_strength WHERE rssi>-120 AND rssi<0 AND time_index>=? AND time_index<=? AND landmark='"+landmark+"' GROUP BY x_rate,network_name";
			}else{
			sql="SELECT  AVG(rssi) rssi,SUBSTR(TO_CHAR(time_index,'#'), "+startPos+",2) AS x_rate,network_name FROM  signal_strength WHERE rssi>-120 AND rssi<0 AND time_index>=? AND time_index<=? AND network_type='"+network_type+"'AND landmark='"+landmark+"'  GROUP BY x_rate,network_name";
			}
		}else{
			 if(VarDesc.NETWORKTYPE.ALL.equals(network_type)){
				sql="SELECT  AVG(rssi) rssi,SUBSTR(TO_CHAR(time_index,'#'), "+startPos+",2) AS x_rate,network_name FROM  signal_strength WHERE  rssi>-120 AND rssi<0 AND  NETWORK_NAME='"+network_name+"' AND time_index>=? AND time_index<=? AND landmark='"+landmark+"'  GROUP BY x_rate,network_name";
			}else{
				sql="SELECT  AVG(rssi) rssi,SUBSTR(TO_CHAR(time_index,'#'), "+startPos+",2) AS x_rate,network_name FROM  signal_strength WHERE rssi>-120 AND rssi<0 AND time_index>=? AND time_index<=? AND network_type='"+network_type+"' AND network_name='"+network_name+"' AND landmark='"+landmark+"'   GROUP BY x_rate,network_name";
			}
		}
 
		List<SignalStrength> signalStrengths = jdbcTemplate
				.query(sql,new Object[] {beginDate,endDate},
						new RowMapper<SignalStrength>() {
							@Override
							public SignalStrength mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								SignalStrength signalStrength = new SignalStrength();
								signalStrength.setRssi((long)Math.abs(rs.getDouble("rssi")));
								signalStrength.setX_rate(rs.getString("x_rate"));
								signalStrength.setNetworkname(rs.getString("network_name"));
								return signalStrength;

							}
						});
		logger.info("report forms:SignalStrength\t  landmark信号强度跟踪图 counts\tsql query:"+sql+"\t"+"beginDate-endDate:"+beginDate+"-"+endDate+"\tcollection size:"+signalStrengths.size()+"\tline:");
		return signalStrengths.size() != 0 ? signalStrengths : null;
	}

	
	
	
	

	// 信号强度跟踪图（曲线图）获取前5Landmark
	public List<SignalStrength> getLandmarkName() {
		String sql = "select  count(*) as sCount,Landmark from Signal_Strength1 where Landmark is not null group by Landmark order by  sCount desc limit 5";
		List<SignalStrength> signalStrengths = jdbcTemplate.query(sql,
				new RowMapper<SignalStrength>() {

					@Override
					public SignalStrength mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SignalStrength signalStrength = new SignalStrength();
						signalStrength.setLandmark(rs.getString("Landmark"));
						return signalStrength;
					}
				});
		return signalStrengths;
	}

	// 信号强度跟踪图(曲线图)
	public List<SignalStrength> getSignalStrengthTrack(
			SignalStrength ss, User user) {
		String sql = null;
		if (VarDesc.ROLE.ADMIN.equals(user.getRole())
				&& VarDesc.OPERATOR.ALL.equals(ss.getNetworkname())) {
			if (VarDesc.NETWORKTYPE.ALL.equals(ss.getNetworktype())) {
				sql = "select rssi,time_index from Signal_Strength1 where Landmark = ? and time_index >= ? and time_index <= ? order by time_index desc";
			} else {
				sql = "select rssi,time_index from Signal_Strength1 where Landmark = ? and network_type =? and time_index >= ? and time_index <= ? order by time_index desc";
			}
		} else {
			if (VarDesc.NETWORKTYPE.ALL.equals(ss.getNetworktype())) {
				sql = "select rssi,time_index from Signal_Strength1 where Landmark = ? and network_name = ? and time_index >= ? and time_index <= ? order by time_index desc";
			} else {
				sql = "select rssi,time_index from Signal_Strength1 where Landmark = ? and network_type = ? and network_name = ? and time_index >= ? and time_index <= ? order by time_index desc";
			}
		}
		List<SignalStrength> signalStrengths = jdbcTemplate
				.query(sql,
						VarDesc.ROLE.ADMIN.equals(user.getRole())&& VarDesc.OPERATOR.ALL.equals(ss.getNetworkname())
						? VarDesc.NETWORKTYPE.ALL.equals(ss.getNetworktype()) 
							? new Object[] {ss.getLandmark(),ss.getStartDate(),ss.getEndDate()}
							: new Object[] {ss.getLandmark(),ss.getNetworktype(),ss.getStartDate(),ss.getEndDate()}
						: VarDesc.NETWORKTYPE.ALL.equals(ss.getNetworktype()) 
							? new Object[] {ss.getLandmark(),ss.getNetworkname(),ss.getStartDate(),ss.getEndDate()}
							: new Object[] {ss.getLandmark(),ss.getNetworktype(),ss.getNetworkname(),ss.getStartDate(),ss.getEndDate()},

						new RowMapper<SignalStrength>() {
							@Override
							public SignalStrength mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								SignalStrength signalStrength = new SignalStrength();
								signalStrength.setRssi(rs.getDouble("rssi"));
								signalStrength.setTesttime(rs
										.getString("time_index"));
								return signalStrength;
							}
						});
		return signalStrengths;
	}

	// //////////////////////////////////////get set//// //////////////////////////////////////

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// ///////无用

	@Override
	public List<SignalStrength> showAllPointGeog(
			List<SignalStrength> SignalStrengths) {
		// TODO Auto-generated method stub
		return null;
	}

}
