package cn.itcast.entity;

public class Device {
	private int id;
	private String imei;
	private String company;
	private String model;
	private String os;
	private String osVersion;
	/************数据库字段结束************/
	
	private String dlSpeed;
	private String ulSpeed;
	private String daytime;
	private int startDate;
	private int endDate;
	private String nwOperator;
	private String speedType;
	private String companyModel;

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
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
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
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
	public String getNwOperator() {
		return nwOperator;
	}
	public void setNwOperator(String nwOperator) {
		this.nwOperator = nwOperator;
	}
	public String getSpeedType() {
		return speedType;
	}
	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}
	public String getCompanyModel() {
		return companyModel;
	}
	public void setCompanyModel(String companyModel) {
		this.companyModel = companyModel;
	}
	
	
	

}
