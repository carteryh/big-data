package cn.itcast.entity;

import java.awt.Color;


public class VarDesc {
	
	/********************统一用大写定义变量名，值要对应数据库的值***********************/
	public static final String NOVALUE = "";
	public static final String NODATA = "NODATA";
	public static final String ALL = "ALL";//针对所有的all都要这样写
	//角色
	public static enum ROLE {
        //注：枚举写在最前面，否则编译出错
		ROLE;
        public static final String ADMIN  = "admin";
		public static final String OPERATOR  = "operator";
    }
	//运营商
	public static enum OPERATOR {
        //注：枚举写在最前面，否则编译出错
		OPERATOR;
        public static final String CMCC  = "CMCC";
		public static final String CUCC  = "CUCC";
		public static final String CTCC  = "CTCC";
		public static final String ALL  = "ALL";
    }
	//运营商
		public static enum OPERATOR_FOR {CMCC,CUCC,CTCC;}
	//网络速率
	public static enum SPEEDTYPE {
        //注：枚举写在最前面，否则编译出错
		SPEEDTYPE;
        public static final String DLSPEED  = "DLSpeed";
		public static final String ULSPEED  = "ULSpeed";
		public static final String LATENCY  = "Latency";
        public static final String ALL  = "ALL";
		
    }
	//网络制式
	public static enum NETWORKTYPE {
        //注：枚举写在最前面，否则编译出错
		NETWORKTYPE;
        public static final String ALL  = "ALL";
        public static final String G4  = "4G";
        public static final String G3  = "3G";
        public static final String G2  = "2G";
        public static final String WIFI  = "WI-FI";
		
    }
	//手机流量
	public static enum APPSPEEDTYPE {
        //注：枚举写在最前面，否则编译出错
		APPSPEEDTYPE;
		public static final String ALL  = "ALL";
        public static final String DLSPEED  = "download_traffic";
		public static final String ULSPEED  = "upload_traffic";
    }
	//手机流量
		public static enum AXISTYPE {
	        //注：枚举写在最前面，否则编译出错
			AXISTYPE;
			public static final String HOUR  = "hour";
			public static final String DAY  = "day";
	        public static final String MONTH  = "month";
			
	    }
		
	
	
	
	
	
	public static String getNodata() {
		return NODATA;
	}
	public static String getRole(String roleType){
		switch (roleType) {
		case "ADMIN":
			return ROLE.ADMIN;
		case "OPERATOR":
			return ROLE.OPERATOR;
		default:
			return null;
		}
	}

	public static String getOperator(String operatorType){
		switch (operatorType) {
		case "CMCC":
			return OPERATOR.CMCC;
		case "CUCC":
			return OPERATOR.CUCC;
		case "CTCC":
			return OPERATOR.CTCC;
		case "ALL":
			return OPERATOR.ALL;
		default:
			return null;
		}
	}
	
	public static String getSpeedType(String speedType){
		switch (speedType) {
		case "DLSPEED":
			return SPEEDTYPE.DLSPEED;
		case "ULSPEED":
			return SPEEDTYPE.ULSPEED;
		case "LATENCY":
			return SPEEDTYPE.LATENCY;
		default:
			return null;
		}
	}
	
	public static String getAppSpeedType(String speedType){
		switch (speedType) {
		case "DLSPEED":
			return APPSPEEDTYPE.DLSPEED;
		case "ULSPEED":
			return APPSPEEDTYPE.ULSPEED;
		default:
			return null;
		}
	}
	
	
	public static String getNetworkType(String networkType){
		switch (networkType) {
		case "ALL":
			return NETWORKTYPE.ALL;
		case "4G":
			return NETWORKTYPE.G4;
		case "3G":
			return NETWORKTYPE.G3;
		case "2G":
			return NETWORKTYPE.G2;
		case "WI-FI":
			return NETWORKTYPE.WIFI;
		default:
			return null;
		}
	}
	
	public static String getAxisType(String axisType){
		switch (axisType) {
		case "HOUR":
			return AXISTYPE.HOUR;
		case "DAY":
			return AXISTYPE.DAY;
		case "MONTH":
			return AXISTYPE.MONTH;
		default:
			return null;
		}
	}
	
	
	
}

