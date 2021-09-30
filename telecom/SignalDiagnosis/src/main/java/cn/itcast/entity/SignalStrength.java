package cn.itcast.entity;


public class SignalStrength {
	private int id;
	private String count;
	private String lng;
	private String lat;
	private String testtime;
	private String province;
	private String city;
	private String operator_name;
	private int gsm_strength;
	private int cdma_dbm;
	private int evdo_dbm;
	private String Landmark;
	
	private String networkname;
	private String networktype;
	
	/******************数据库字段结束******************/
	private boolean  isoneday;
	private int  oneday;
	private String  user;
	private int startdate;
	private int stopdate;
	private String daytime;
	//private String nwOperator;

	private int startDate;
	private int endDate;
	private String startDateToSecond;
	private String endDateToSecond;
	//x轴的粒度变化，小时、日、月、年。
	private String x_rate;
	//x轴的粒度变化，小时、日、月、年类型
	private String x_type;
	private long echart_rssi;
	
	private double rssi;
	
	public boolean getIsoneday() {
		return isoneday;
	}
	public void setIsoneday(boolean isoneday) {
		this.isoneday = isoneday;
	}
	
	public int getOneday() {
		return oneday;
	}
	public void setOneday(int oneday) {
		this.oneday = oneday;
	}
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getNetworkname() {
		return networkname;
	}
	public void setNetworkname(String networkname) {
		this.networkname = networkname;
	}
	public int getStartdate() {
		return startdate;
	}
	public void setStartdate(int startdate) {
		this.startdate = startdate;
	}
	public int getStopdate() {
		return stopdate;
	}
	public void setStopdate(int stopdate) {
		this.stopdate = stopdate;
	}
	public String getNetworktype() {
		return networktype;
	}
	public void setNetworktype(String network_type) {
		this.networktype = network_type;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTesttime() {
		return testtime;
	}
	public void setTesttime(String testtime) {
		this.testtime = testtime;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOperator_name() {
		return operator_name;
	}
	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
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
	public int getGsm_strength() {
		return gsm_strength;
	}
	public void setGsm_strength(int gsm_strength) {
		this.gsm_strength = gsm_strength;
	}
	public int getCdma_dbm() {
		return cdma_dbm;
	}
	public void setCdma_dbm(int cdma_dbm) {
		this.cdma_dbm = cdma_dbm;
	}
	public int getEvdo_dbm() {
		return evdo_dbm;
	}
	public void setEvdo_dbm(int evdo_dbm) {
		this.evdo_dbm = evdo_dbm;
	}
	public String getLandmark() {
		return Landmark;
	}
	public void setLandmark(String landmark) {
		Landmark = landmark;
	}
	public double getRssi() {
		return rssi;
	}
	public void setRssi(double rssi) {
		this.rssi = rssi;
	}
	public String getStartDateToSecond() {
		return startDateToSecond;
	}
	public void setStartDateToSecond(String startDateToSecond) {
		this.startDateToSecond = startDateToSecond;
	}
	public String getEndDateToSecond() {
		return endDateToSecond;
	}
	public void setEndDateToSecond(String endDateToSecond) {
		this.endDateToSecond = endDateToSecond;
	}
	public String getX_rate() {
		return x_rate;
	}
	public void setX_rate(String x_rate) {
		this.x_rate = x_rate;
	}
	public String getX_type() {
		return x_type;
	}
	public void setX_type(String x_type) {
		this.x_type = x_type;
	}
	public long getEchart_rssi() {
		return echart_rssi;
	}
	public void setEchart_rssi(long echart_rssi) {
		this.echart_rssi = echart_rssi;
	}
	
	
	
}
