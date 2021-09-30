package canal;

        import com.alibaba.otter.canal.protocol.CanalEntry;

        import java.io.IOException;
        import java.util.List;

        import static canal.CanalClient.*;

public class DataDetail {

    public static void dataDetails(List<CanalEntry.Column> columns , String logFileName , Long logFileOffset ,
                                   String dbName , String tableName , CanalEntry.EventType eventType ) throws IOException {

        //获取数据内的network_id
        String network_id="";
        //存储数据内所有的数据值（不包含字段）
        String columnString="";

        for(CanalEntry.Column column:columns){
            //获取数据内字段名是network_id的值
            if ("network_id".equals(column.getName())) {
                network_id=column.getValue();
            }
            //将一条（一行）数据的所有的数据值拼接在一起，并使用\t分割各个字段
            columnString+=column.getValue();
            columnString+='\t';
        }
        //去掉最后一个\t
        columnString=columnString.substring(0, columnString.lastIndexOf("\t"));
        //将本数据的相关信息使用#CS#拼接起来
        String data =eventType + "#CS#" + dbName + "#CS#" + tableName + "#CS#"+network_id+"#CS#"+columnString;
        System.out.println(data);

        if (tableName.equals("cell_strength")) {//若是表cell_strength的操作
            //记录cell_strength数据的条数
            cell_strength_number++;
            //将多条cell_strength数据拼接在一起
            cell_strength_datas+=data+"\r\n";
            //当数据的条数达到5条时，将这五条数据写入到一个独立的文档内
            if (cell_strength_number==5) {
                //WriteData.writeData("E:\\Telecom\\Data\\cell_strength_"+System.currentTimeMillis()+".txt",cell_strength_datas);
                WriteData.writeData("/opt/Telecom/Data/cell_strength/cell_strength_"+System.currentTimeMillis()+".txt",cell_strength_datas);
                //将记录cell_strength数据条数的变量和记录数据的变量清空
                cell_strength_datas="";
                cell_strength_number=0;
            }

        }else if(tableName.equals("data_connection")){//若是表data_connection的操作
            //记录data_connection数据的条数
            data_connection_number++;
            //将多条data_connection数据拼接在一起
            data_connection_datas+=data+"\r\n";
            //当数据的条数达到5条时，将这五条数据写入到一个独立的文档内
            if (data_connection_number==5) {
                //WriteData.writeData("E:\\Telecom\\Data\\data_connection_"+System.currentTimeMillis()+".txt",data_connection_datas);
                WriteData.writeData("/opt/Telecom/Data/data_connection/data_connection_"+System.currentTimeMillis()+".txt",data_connection_datas);
                //将记录data_connection数据条数的变量和记录数据的变量清空
                data_connection_datas="";
                data_connection_number=0;
            }

        }else if (tableName.equals("networkqualityinfo")) { //若是表networkqualityinfo的操作
            //记录networkqualityinfo数据的条数
            networkqualityinfo_number++;
            //将多条networkqualityinfo数据拼接在一起
            networkqualityinfo_datas+=data+"\r\n";
            //当数据的条数达到5条时，将这五条数据写入到一个独立的文档内
            if (networkqualityinfo_number==5) {
                //WriteData.writeData("E:\\Telecom\\Data\\networkqualityinfo_"+System.currentTimeMillis()+".txt",networkqualityinfo_datas);
                //opt/Telecom/Data
                WriteData.writeData("/opt/Telecom/Data/networkqualityinfo/networkqualityinfo_"+System.currentTimeMillis()+".txt",networkqualityinfo_datas);
                //将记录networkqualityinfo数据条数的变量和记录数据的变量清空
                networkqualityinfo_datas="";
                networkqualityinfo_number=0;
            }
        }
    }
}
