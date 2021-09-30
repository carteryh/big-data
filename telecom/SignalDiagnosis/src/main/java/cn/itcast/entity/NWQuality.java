package cn.itcast.entity;

import java.util.List;


public class NWQuality {
	private int id;
	private int tCount;
	private String gpslat;
	private String gpslon;
	private String daytime;
	private String province;
	private String city;
	private String nwOperator;
	private String speedss;
	

	private String imei;
	private String nwType;
	private String dlSpeed;
	private String ulSpeed;
	private String latency;
	private String deviceOS;
	private String deviceVersion;
	private String deviceCompany;
	private String deviceModel;
	private String landmark;
	private int startDate;
	private int endDate;
	private String viewId;
	private String speedType; //网络质量类型
	private String mobileType;
	//对应数据库字段（手机的上行流量和下行流量）
	private long upload_traffic;
	private long download_traffic;
	//对应数据库字段（手机的设备的os以及版本）=deviceOS+deviceVersion
	private String DeviceOsVersion;

	
	
	//瓷砖分片经纬度大小
	private double tileLng;
	private double tileLat;
	//最小经纬度
	private double minGpslat;
	private double minGpslon;
	//上行、下行、延迟平均值
	private long sumSpeed;
	//x轴的粒度变化，小时、日、月、年。
	private String x_rate;
	//x轴的粒度变化，小时、日、月、年类型
	private String x_type;
	//speed速率,上行、下行、或者延迟等。
	private long speed;
	//日期精确到秒专用字段，bean字段
	private String startDateToSecond;
	private String endDateToSecond;
	private String showType; //标识仪表盘显示
	private String nWType_type; //网络制式类型
	
	private double dspeed;
	
	
	public String getSpeedss() {
		return speedss;
	}

	public void setSpeedss(String speeds) {
		this.speedss = speeds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void settCount(int tCount) {
		this.tCount = tCount;
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
	

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public int gettCount() {
		return tCount;
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

	public String getNwOperator() {
		return nwOperator;
	}

	public void setNwOperator(String nwOperator) {
		this.nwOperator = nwOperator;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getNwType() {
		return nwType;
	}

	public void setNwType(String nwType) {
		this.nwType = nwType;
	}

	public String getDlSpeed() {
		return dlSpeed;
	}

	public void setDlSpeed(String dlSpeed) {
		this.dlSpeed = dlSpeed;
	}

	public String getUlSpeed() {
		return ulSpeed;
	}

	public void setUlSpeed(String ulSpeed) {
		this.ulSpeed = ulSpeed;
	}

	public String getLatency() {
		return latency;
	}

	public void setLatency(String latency) {
		this.latency = latency;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getDeviceCompany() {
		return deviceCompany;
	}

	public void setDeviceCompany(String deviceCompany) {
		this.deviceCompany = deviceCompany;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
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

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}


	public String getSpeedType() {
		return speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}


	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
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

	public long getSumSpeed() {
		return sumSpeed;
	}

	public void setSumSpeed(long sumSpeed) {
		this.sumSpeed = sumSpeed;
	}

	public String getX_rate() {
		return x_rate;
	}
	public void setX_rate(String x_rate) {
		this.x_rate = x_rate;
	}


	public long getSpeed() {
		return speed;
	}


	public void setSpeed(long speed) {
		this.speed = speed;
	}

	
	public long getUpload_traffic() {
		return upload_traffic;
	}

	public void setUpload_traffic(long upload_traffic) {
		this.upload_traffic = upload_traffic;
	}

	public long getDownload_traffic() {
		return download_traffic;
	}

	public void setDownload_traffic(long download_traffic) {
		this.download_traffic = download_traffic;
	}

	

	public String getX_type() {
		return x_type;
	}

	public void setX_type(String x_type) {
		this.x_type = x_type;
	}

	public String getDeviceOsVersion() {
		return DeviceOsVersion;
	}

	public void setDeviceOsVersion(String deviceOsVersion) {
		DeviceOsVersion = deviceOsVersion;
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

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getnWType_type() {
		return nWType_type;
	}

	public void setnWType_type(String nWType_type) {
		this.nWType_type = nWType_type;
	}

	public double getDspeed() {
		return dspeed;
	}

	public void setDspeed(double dspeed) {
		this.dspeed = dspeed;
	}

	@Override
	public String toString() {
		return "NWQuality [id=" + id + ", tCount=" + tCount + ", gpslat="
				+ gpslat + ", gpslon=" + gpslon + ", daytime=" + daytime
				+ ", province=" + province + ", city=" + city + ", nwOperator="
				+ nwOperator + ", imei=" + imei + ", nwType=" + nwType
				+ ", dlSpeed=" + dlSpeed + ", ulSpeed=" + ulSpeed
				+ ", latency=" + latency + ", deviceOS=" + deviceOS
				+ ", deviceVersion=" + deviceVersion + ", deviceCompany="
				+ deviceCompany + ", deviceModel=" + deviceModel
				+ ", landmark=" + landmark + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", viewId=" + viewId
				+ ", speedType=" + speedType + ", mobileType=" + mobileType
				+ ", upload_traffic=" + upload_traffic + ", download_traffic="
				+ download_traffic + ", DeviceOsVersion=" + DeviceOsVersion
				+ ", tileLng=" + tileLng + ", tileLat=" + tileLat
				+ ", minGpslat=" + minGpslat + ", minGpslon=" + minGpslon
				+ ", sumSpeed=" + sumSpeed + ", x_rate=" + x_rate + ", x_type="
				+ x_type + ", speed=" + speed + ", startDateToSecond="
				+ startDateToSecond + ", endDateToSecond=" + endDateToSecond
				+ ", showType=" + showType + ", nWType_type=" + nWType_type
				+ ", dspeed=" + dspeed + "]";
	}



	
}
