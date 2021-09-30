package makedata;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MakeData {

    public static void main(String[] args) throws  Exception {
        //mysql数据库所在的节点ip
//        String ip=args[0];
//        //mysql数据库用户名
//        String username = args[1];
//        //mysql数据库密码
//        String password =   args[2];
//        //mysql数据库
//        String database=args[3];
//        //导入的表名(将数据插入的目标表)
//        String table=args[4];
//        //需要导入数据的条数
//        int number=Integer.parseInt(args[5]);


        //mysql数据库所在的节点ip
        String ip="192.168.3.60";
        //mysql数据库用户名
        String username = "root";
        //mysql数据库密码
        String password =   "12345678";
        //mysql数据库
        String database="APP";
        //导入的表名(将数据插入的目标表)
        // networkqualityinfo   data_connection cell_strength
        String table="data_connection";
        //需要导入数据的条数
        int number=Integer.parseInt("20");



        //连接数据库的字符串
        String connectStr = "jdbc:mysql://"+ip+":3306/"+database;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectStr, username,password);
        conn.setAutoCommit(false);
        //向想要的表中插入数据
        System.out.println("started  insert datas");
        if (table.equals("networkqualityinfo")) {//向networkqualityinfo表  插入数据
            //插入数据的语句
            String networkqualityinfo_sql = "INSERT INTO networkqualityinfo (id  ,ping ,ave_downloadSpeed ,max_downloadSpeed ,ave_uploadSpeed ,max_uploadSpeed ,rssi ,gps_lat ,gps_lon ,location_type ,imei ,server_url ,ant_version ,detail ,time_client_test ,time_server_insert ,networkType ,operator_name ,wifi_bss_id ,cell_id ,province ,city ,mobile_type ,street ,location_detail ,upload_traffle ,download_traffic) VALUES"
                    + " (1,'100','152','1923','385','511','-58','39.961248','116.358825','','357242041311082','http://ccpn.3322.org/speedtest','3.0h','','2017-05-19 20:11:08','2017-05-19 20:12:19','Wi-Fi','CUCC','00:1b:2f:63:97:aa','460015228355','BeiJing','BeiJing','','','','','')";
            //调用插入方法  插入数据
            InsertData(conn,number,networkqualityinfo_sql);

        }else if (table.equals("data_connection")) {  //向data_connection表  插入数据
            //插入数据的语句
            String data_connection_sql = "INSERT INTO data_connection (id  ,imei ,network_type ,wifi_bssid ,wifi_state ,wifi_rssi ,mobile_state ,mobile_network_type ,network_id ,gsm_strength ,cdma_dbm ,evdo_dbm ,internal_ip ,web_url ,ping_value ,user_lat ,user_lon ,user_location_info ,bs_lat ,bs_lon ,time_index_client ,version ,time_server_insert) VALUES "
                    + "(13,'867747016203539','Wi-Fi','00:24:a5:f0:1e:92','CONNECTED','','-1','mobile','46001-41023-5184265','99','-1','-1','192.168.102.98','http://192.168.102.65','00000','39.98949','116.33172','bd09ll-Network -40.0','-1','-1','2015-02-02 16:41:42','anttest6.3.3-docomo','2015-02-02 16:40:11')";
            //调用插入方法  插入数据
            InsertData(conn,number,data_connection_sql);

        }else if (table.equals("cell_strength")) {//向cell_strength表  插入数据
            //插入数据的语句
            String  cell_strength_sql = "INSERT INTO cell_strength (id  ,network_id ,network_type ,gsm_strength ,cdma_dbm ,evdo_dbm ,gsm_bit_errorrate ,cdma_ecio ,evdo_ecio ,user_lat ,user_lon ,user_location_info ,bs_lat ,bs_lon ,time_index ,imei) VALUES "
                    + "(17,'46001-41023-5184265','UMTS','17','-1','-1','-1','-1','-1','39.962232','116.360972','gcj02-Network -1507','-1','-1','2017-07-08 17:17:27','357841032749893')";
            //调用插入方法  插入数据
            InsertData(conn,number,cell_strength_sql);

        }
        //关闭链接
        conn.close();
        System.out.println("end  insert datas");
    }

    //向数据库插入数据的方法
    private static void InsertData(Connection conn,int number,String sql) throws  Exception {
        //遍历插入的条数据
        for (int a=0;a<number;a++) {
            PreparedStatement psts = conn.prepareStatement(sql);
            psts.execute(); // 执行批量处理
            conn.commit();  // 提交
            Thread.sleep(1000);
        }
    }
}
