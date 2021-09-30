package cn.itcast.entity;


public class AppTraffic {
	private int id;
	private double user_lon;
	private double user_lat;
	private String date;
	private String start_time;
	private String end_time;
	private String mobile_type;
	private double upload_traffic;
	private double download_traffic;
	private String package_name;
	private String app_name;
	private String cell_id;
	private String network_name;
	private String imei;
	private String province;
	private String speed_type;
	private long speed;
	private int startDate;
	private int endDate;
	private String networkType;
	private String osAndVersion;
	private String dateTime;
	private String showType;

	

	private String os;
	private String os_version;
	private String companyModel;
	private String company;

	private String mobel;
	private String flow;
	private String role;
	private int count;
	private double lng;
	private double lat;
	private String  Daytime;
	private double  tableCenterLng ;
	private double  tableCenterLat ;
	//瓷砖分片经纬度大小
	private double tileLng;
	private double tileLat;
	//最小经纬度
	private double minGpslat;
	private double minGpslon;
	private String gpslat;
	private String gpslon;
	
	private double percentPie;
	private String landmark;
	
	private double dspeed;
	
	//x轴的粒度变化，小时、日、月、年。
	private String x_rate;
	//x轴的粒度变化，小时、日、月、年类型
	private String x_type;
	//speed流量,上行、下行、或者延迟等。
	//日期精确到秒专用字段，bean字段
	private String startDateToSecond;
	private String endDateToSecond;
	
	

	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMobel() {
		return mobel;
	}
	public void setMobel(String mobel) {
		this.mobel = mobel;
	}

	public String getCompanyModel() {
		return companyModel;
	}
	public void setCompanyModel(String companyModel) {
		this.companyModel = companyModel;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getDaytime() {
		return Daytime;
	}
	public void setDaytime(String daytime) {
		Daytime = daytime;
	}
	public double getTableCenterLng() {
		return tableCenterLng;
	}
	public void setTableCenterLng(double tableCenterLng) {
		this.tableCenterLng = tableCenterLng;
	}
	public double getTableCenterLat() {
		return tableCenterLat;
	}
	public void setTableCenterLat(double tableCenterLat) {
		this.tableCenterLat = tableCenterLat;
	}
	public double getTileLng() {
		return tileLng;
	}
	public void setTileLng(double tileLng) {
		this.tileLng = tileLng;
	}
	public double getTileLat() {
		return tileLat;
	}
	public void setTileLat(double tileLat) {
		this.tileLat = tileLat;
	}
	public double getMinGpslat() {
		return minGpslat;
	}
	public void setMinGpslat(double minGpslat) {
		this.minGpslat = minGpslat;
	}
	public double getMinGpslon() {
		return minGpslon;
	}
	public void setMinGpslon(double minGpslon) {
		this.minGpslon = minGpslon;
	}
	public String getGpslat() {
		return gpslat;
	}
	public void setGpslat(String gpslat) {
		this.gpslat = gpslat;
	}
	public String getGpslon() {
		return gpslon;
	}
	public void setGpslon(String gpslon) {
		this.gpslon = gpslon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getMobile_type() {
		return mobile_type;
	}
	public void setMobile_type(String mobile_type) {
		this.mobile_type = mobile_type;
	}
	public double getUpload_traffic() {
		return upload_traffic;
	}
	public void setUpload_traffic(double upload_traffic) {
		this.upload_traffic = upload_traffic;
	}
	public double getDownload_traffic() {
		return download_traffic;
	}
	public void setDownload_traffic(double download_traffic) {
		this.download_traffic = download_traffic;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getNetwork_name() {
		return network_name;
	}
	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getSpeed_type() {
		return speed_type;
	}
	public void setSpeed_type(String speed_type) {
		this.speed_type = speed_type;
	}
	
	public long getSpeed() {
		return speed;
	}
	public void setSpeed(long speed) {
		this.speed = speed;
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
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getOsAndVersion() {
		return osAndVersion;
	}
	public void setOsAndVersion(String osAndVersion) {
		this.osAndVersion = osAndVersion;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public double getPercentPie() {
		return percentPie;
	}
	public void setPercentPie(double percentPie) {
		this.percentPie = percentPie;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
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
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public double getDspeed() {
		return dspeed;
	}
	public void setDspeed(double dspeed) {
		this.dspeed = dspeed;
	}
	@Override
	public String toString() {
		return "AppTraffic [id=" + id + ", user_lon=" + user_lon
				+ ", user_lat=" + user_lat + ", date=" + date + ", start_time="
				+ start_time + ", end_time=" + end_time + ", mobile_type="
				+ mobile_type + ", upload_traffic=" + upload_traffic
				+ ", download_traffic=" + download_traffic + ", package_name="
				+ package_name + ", app_name=" + app_name + ", cell_id="
				+ cell_id + ", network_name=" + network_name + ", imei=" + imei
				+ ", province=" + province + ", speed_type=" + speed_type
				+ ", speed=" + speed + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", networkType=" + networkType
				+ ", osAndVersion=" + osAndVersion + ", dateTime=" + dateTime
				+ ", showType=" + showType + ", os=" + os + ", os_version="
				+ os_version + ", companyModel=" + companyModel + ", company="
				+ company + ", mobel=" + mobel + ", flow=" + flow + ", role="
				+ role + ", count=" + count + ", lng=" + lng + ", lat=" + lat
				+ ", Daytime=" + Daytime + ", tableCenterLng=" + tableCenterLng
				+ ", tableCenterLat=" + tableCenterLat + ", tileLng=" + tileLng
				+ ", tileLat=" + tileLat + ", minGpslat=" + minGpslat
				+ ", minGpslon=" + minGpslon + ", gpslat=" + gpslat
				+ ", gpslon=" + gpslon + ", percentPie=" + percentPie
				+ ", landmark=" + landmark + ", dspeed=" + dspeed + ", x_rate="
				+ x_rate + ", x_type=" + x_type + ", startDateToSecond="
				+ startDateToSecond + ", endDateToSecond=" + endDateToSecond
				+ "]";
	}
	
	
	
	
	
	
	

}
