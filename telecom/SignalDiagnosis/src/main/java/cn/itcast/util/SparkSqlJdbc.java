package cn.itcast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class SparkSqlJdbc {

	 
	  /**
	   * @param args
	   * @throws SQLException
	   */
	  
		static {
			try {
				Class.forName("org.apache.hive.jdbc.HiveDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		public static Connection getConnection() {
			//jdbc:hive2://192.168.1.130:10000/default
			try {
				Connection conn = DriverManager
						.getConnection("jdbc:hive2://s1:14000/default","hive", "hive");
			
				return conn;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	  
	  
	  public static void main(String[] args) throws SQLException {
		  Connection conn=getConnection();
		  Statement statement=conn.createStatement();
		  System.out.println("1111111111");
		  ResultSet rs=statement.executeQuery("select * from nwquality_spark limit 10");
		  
		  while (rs.next()) {
			  System.out.println(rs.getInt("id"));
			  System.out.println(rs.getString("daytime"));
			  System.out.println(rs.getString("nwoperator"));
			  System.out.println(rs.getString("tcount"));
			  System.out.println(rs.getString("dlspeed"));
			  System.out.println(rs.getString("ulspeed"));
	
		}
		  
		  String str="";
		  System.out.println(str=="");
		  
	  }
	}