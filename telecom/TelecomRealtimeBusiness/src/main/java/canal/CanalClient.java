package canal;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;

import java.net.InetSocketAddress;

public class CanalClient {
	//networkqualityinfo输出数据
	static  String networkqualityinfo_datas="";
	static int networkqualityinfo_number=0;
	
	//data_connection输出数据
	static String data_connection_datas="";
	static int data_connection_number=0;
	
	//cell_strength_datas输出数据
	static String cell_strength_datas="";
	static int cell_strength_number=0;
 
    public static void main(String[] args) {
    	 
        //链接开启binlog的mysql数据库
    	//node01  canal所在的节点
    	//11111  canal 所在的节点，配置的canal的端口
    	//example  固定写法
    	//root  mysql的用户名
    	//123456    mysql的密码
        CanalConnector conn = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.3.60", 11111),  "example", "root", "12345678");
        //连接上canal之后，开始订阅canal的binlog日志
        //
        int batchSize  = 100 ;
        int emptyCount = 1;
        System.out.println(1);

        try{
            conn.connect();
//            conn.subscribe(".*\\..*");//.*表示数据库             ..*表示数据库内的表
            conn.subscribe("APP.*");//.*表示数据库             ..*表示数据库内的表  canal.test1
            conn.rollback(); //回滚
            int totalCount = 120 ; //循环次数
            //
            while (totalCount > emptyCount){
                //获取数据
                Message message = conn.getWithoutAck(batchSize);
                //获取数据的ID
                long id = message.getId();
                //获取数据的size
                int size = message.getEntries().size();
                
                if(id == -1 || size == 0){
                    //没有读取到任何数据
                    //System.out.println(id +"      "+size);
                }else{
                    //有数据，那么解析binlog日志
                    Analysis.analysis(message.getEntries() );
                    emptyCount ++ ;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
    }
}
