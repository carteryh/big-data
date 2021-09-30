package cn.itcast;

import cn.itcast.telecom.entity.LandMark;
import cn.itcast.telecom.landmark.LandMarkUtil;
import org.dom4j.DocumentException;
import java.util.List;

public class TelecomUtils {

    /*
     信号强度转换
    */
    public static int getRssi(String gsm_strength,String cdma_dbm,String evdo_dbm,String network_type){
        //将字符串数据转换成Int数据类型
        int strength=Integer.valueOf(gsm_strength);
        int cdma=Integer.valueOf(cdma_dbm);
        int evdo=Integer.valueOf(evdo_dbm);
        int rssi=0;

        //若gsm_strength大于0并且不等于99,那么rssi等于2*gsm_strength-113
        if((strength>0) && (strength!=99)){
            rssi=2*strength-113;
        }else if(strength<0){//若gsm_strength小于0，那么rssi等于gsm_strength。
            rssi=strength;
        }else if((cdma!=-1)&&(cdma!=-120)&&(evdo!=-1)&&(evdo!=-120)){//在原始字段cdma_dbm、evdo_dbm均不等于-1、-120的前提下
            //若network_type字段包含“EVDO”，那么rssi等于evdo_dbm,反之rssi等于cdma_dbm。
            if(network_type.contains("EVDO")){
                rssi =evdo;
            }else{
                rssi =cdma;
            }
        }else if((evdo!=-1)&&(evdo!=-120)){//若evdo_dbm不等于-1、-120，那么rssi等于evdo_dbm。
            rssi=evdo;

        }else if((cdma!=-1)&&(cdma!=-120)){//若cdma_dbm不等于-1、-120，那么rssi等于cdma_dbm。
            rssi=cdma;
        }else{//其他情况rssi等于0。
            rssi=0;
        }
        return rssi;
    }


    /*
     拼装设备厂商和型号
    */
    public static String  getMobileModel (String DeviceCompany, String DeviceModel ) {
        String mobilemodel=null;
        //若model（型号）中包含company（厂商）那么直接返回model（型号）
        if (DeviceModel.toLowerCase().contains(DeviceCompany.toLowerCase())){
            mobilemodel=DeviceModel;
        }
        else{//若model中不包含company那么将company和model使用“ ”空格进行拼接，最终返回拼接后的结果
            mobilemodel=DeviceCompany+" "+DeviceModel;
        }
        return mobilemodel;
    }


    /*ok
    转换网络类型1
    */
    public static String  getNetworkType (String networktype,String mobile_type) {
        String network_type="";
        //根据原始表中的networktype、mobile_type，判断一个数据属于4G、3G还是2G。一个数据只能属于一种。

        //若mobile_type等于“None”并且networktype不等于“None”。那么返回networktype。
        if ("None".equals(mobile_type)&&!networktype.equals("None")){
            network_type=networktype;
        }else{
            String[] G2={"GPRS","EDGE","1xRTT","IDEN"};
            String[] G3={"UMTS","CDMA","EVDO_0","EVDO_A","HSDPA","HSUPA","HSPA","EVDO_B","eHRPD","HSPA+"};
            boolean flag=false;
            //若mobile_type等于“LTE”,那么结果表network_type等于4G。
            if("LTE".equals(mobile_type)){
                network_type="4G";
                flag=true;
            }
            if(!flag){
                //若mobile_type等于GPRS或EDGE或1xRTT或IDEN那么结果表network_type等于2G。
                for (String str : G2) {
                    if(str.equals(mobile_type)){
                        network_type="2G";
                        flag=true;
                        break;
                    }
                }
            }
            if(!flag){
                for (String str : G3) {
                    //若mobile_type等于UMTS或CDMA或EVDO_0或EVDO_A或HSDPA或HSUPA或HSPA或EVDO_B或eHRPD或HSPA+那么结果表network_type等于3G。
                    if(str.equals(mobile_type)){
                        network_type="3G";
                        flag=true;
                        break;
                    }
                }
            }
            //其他返回network_type
            if(flag){return network_type;}
        }
        return network_type;
    }


    /*
    转换网络类型2
    判断一个数据数据4G、3G还是2G。一个数据只能属于一种。
    */
    public static String  getNetworkType (String networktype) {
        String network_type="";

        String[] G2={"GPRS","EDGE","1xRTT","IDEN"};

        String[] G3={"UMTS","CDMA","EVDO_0","EVDO_A","HSDPA","HSUPA","HSPA","EVDO_B","eHRPD","HSPA+"};
        //是否需要继续判断
        boolean flag=false;
        //若原始表network_type等于“LTE”,那么结果表network_type等于4G。
        if("LTE".equals(networktype)){
            network_type="4G";
            flag=true;
        }
        //若network_type等于GPRS或EDGE或1xRTT或IDEN那么结果表network_type等于2G。
        if(!flag){
            for (String str : G2) {
                if(str.equals(networktype)){
                    network_type="2G";
                    flag=true;
                    break;
                }
            }
        }
        //若network_type等于UMTS或CDMA或EVDO_0或EVDO_A或HSDPA或HSUPA或HSPA或EVDO_B或eHRPD或HSPA+那么结果表network_type等于3G。
        if(!flag){
            for (String str : G3) {
                if(str.equals(networktype)){
                    network_type="3G";
                    flag=true;
                    break;
                }
            }
        }
        //其他返回network_type
        if(flag){return network_type;}

        return network_type;
    }


    /*
    拼接os和os_version
     */
    public  String  getOsVersion (String os, String os_version) {
        //在os、os_version均不等于null， “”， unknown的前提下，将os与os_version进行拼接并返回
        if(os!=null&&os_version!=null&&os!=""&&os_version!=""
                &&os!="unknown"&&os_version!="unknown"){
            return os+os_version;
        }
        return "unknown";

    }

    /*
    根据经纬度计算地标
     */
    //double类型数据匹配
    public static String getLandMark(double lon,double lat) throws DocumentException{
        //读取LandMark.xml地标配置文件，读取出文件中左下角经纬度、右上角经纬度（范围可以组成一个矩形）。
        List<LandMark> landmarks=LandMarkUtil.parseLandMark();
        //遍历所有的地标配置判断用户上报的经纬度数据属于哪一个地标范围。最终返回所属地标。
        for (LandMark landMark : landmarks) {
            if(landMark.getLowerLeftLon() <= lon && lon <= landMark.getTopRightLon()
                    && landMark.getLowerLeftLat() <= lat && lat <= landMark.getTopRightLat()){
                return landMark.getCategory();
                //return "right";
            }
        }
        return "unkown";
    }
    //String 数据剋剋行数据匹配
    public static String getLandMark(String lon,String lat){
        double lon_1=Double.valueOf(lon);
        double lat_1=Double.valueOf(lat);
        try {
            return getLandMark(lon_1,lat_1);
        } catch (DocumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public static String getAllMobileTraffic(String package_name) {
        //判断应用程序包名称是否为all,若等于all。返回yes反之返回no
        if (package_name.equalsIgnoreCase("all")) {
            return "yes";
        }
        return "no";
    }

    public static  String getCompanyModel(String company,String model){
        String companyModel=null;
        //若model（型号）中包含company（厂商）那么直接返回model（型号）
        if (model.toLowerCase().contains(company.toLowerCase())){
            companyModel=model;
        }else{
            //若model中不包含company那么将company和model使用“ ”空格进行拼接，最终返回拼接后的结果
            companyModel=company+" "+model;
        }
        return companyModel;
    }


    public static void main(String[] args) {
        System.out.println(getLandMark("116.365041", "39.96705"));
    }
}
