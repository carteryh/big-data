package canal;
import com.alibaba.otter.canal.protocol.CanalEntry;

import java.io.IOException;
import java.util.List;

public class Analysis {
    public static void analysis(List<CanalEntry.Entry> entries ) throws IOException {

        for(CanalEntry.Entry entry:entries){
            System.out.println(entry.getEntryType());

            //mysql的事务开始前 和事务结束后的内容  不需要
            if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND ){
                continue;
            }

            //如果不是以上的事务，那么解析binlog
            CanalEntry.RowChange rowChange = null ;
            try{
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            }catch (Exception e){
                e.printStackTrace();
            }

            //获取关键字段 哪一个数据库有事务发生  那张表 、 增加  删除  修改
            CanalEntry.EventType eventType = rowChange.getEventType();//操作的是insert 还是delete 还是update
            String logfileName = entry.getHeader().getLogfileName();//当前读取的是哪一个binlog文件
            long logfileOffset = entry.getHeader().getLogfileOffset();//当前读取的binlog文件位置
            String dbName = entry.getHeader().getSchemaName();//当前操作的mysql数据库
            String tableName = entry.getHeader().getTableName();//当前操作的是哪一张表

            //迭代所有获取到的binlog数据，然后根据当前mysql的INSERT  UPDATE  DELETE操作，进行解析
            for(CanalEntry.RowData rowData : rowChange.getRowDatasList()){
                //判断：当前是什么操作
                if(eventType == CanalEntry.EventType.DELETE){//当前是删除操作
                   DataDetail.dataDetails(rowData.getBeforeColumnsList() , logfileName , logfileOffset , dbName , tableName , eventType );

                }else if(eventType == CanalEntry.EventType.INSERT){//当前是插入操作
                    DataDetail.dataDetails(rowData.getAfterColumnsList() , logfileName , logfileOffset , dbName , tableName , eventType );
                }else{//update 当前是更新操作
                    DataDetail.dataDetails(rowData.getAfterColumnsList() , logfileName , logfileOffset , dbName , tableName , eventType );
                }
            }
        }
    }
}
