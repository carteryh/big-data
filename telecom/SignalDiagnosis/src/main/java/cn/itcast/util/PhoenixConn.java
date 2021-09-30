package cn.itcast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class PhoenixConn {

	static {
		try {             
			 Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			Connection conn = DriverManager
					.getConnection("jdbc:phoenix:192.168.100.100:2181");
			conn.setAutoCommit(true);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select   *   from  NWQUALITY limit 10 ");
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			int id = rs.getInt(1);
			System.out.println("@@@@@@@" + id);
		}
	}
}
