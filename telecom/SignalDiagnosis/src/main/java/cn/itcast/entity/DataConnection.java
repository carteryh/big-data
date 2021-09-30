package cn.itcast.entity;

public class DataConnection {
	private int id;
	private int tCount;//筛选总数
	private double user_lon;
	private double user_lat;
	private String time_index_client;//时间
	private int network_id;
	private String network_name;//运营商
	private String network_type;
	private String mobile_network_type;//网络制式
	private int ping_value;//此值多余1111表示连接成功
	private int startDate;//起始时间
	private int endDate;//结束时间
	private double d_percent; //百分比

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int gettCount() {
		return tCount;
	}
	public void settCount(int tCount) {
		this.tCount = tCount;
	}
	public double getUser_lon() {
		return user_lon;
	}
	public void setUser_lon(double user_lon) {
		this.user_lon = user_lon;
	}
	public double getUser_lat() {
		return user_lat;
	}
	public void setUser_lat(double user_lat) {
		this.user_lat = user_lat;
	}
	public String getTime_index_client() {
		return time_index_client;
	}
	public void setTime_index_client(String time_index_client) {
		this.time_index_client = time_index_client;
	}
	public int getNetwork_id() {
		return network_id;
	}
	public void setNetwork_id(int network_id) {
		this.network_id = network_id;
	}
	public String getNetwork_name() {
		return network_name;
	}
	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}
	public String getMobile_network_type() {
		return mobile_network_type;
	}
	public void setMobile_network_type(String mobile_network_type) {
		this.mobile_network_type = mobile_network_type;
	}
	public int getPing_value() {
		return ping_value;
	}
	public void setPing_value(int ping_value) {
		this.ping_value = ping_value;
	}
	public int getStartDate() {
		return startDate;
	}
	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}
	public int getEndDate() {
		return endDate;
	}
	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}
	public double getD_percent() {
		return d_percent;
	}
	public void setD_percent(double d_percent) {
		this.d_percent = d_percent;
	}
	public String getNetwork_type() {
		return network_type;
	}
	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}
	
}
