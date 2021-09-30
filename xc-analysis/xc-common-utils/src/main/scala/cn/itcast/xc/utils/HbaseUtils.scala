package cn.itcast.xc.utils

import cn.itcast.xc.entity.LearningCourseOnlineDwm
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf

import scala.collection.mutable.ArrayBuffer

/**
 * <P>
 * TODO
 * </p>
 *
 */
class HbaseUtils extends Serializable {

  @transient
  var hBaseAdmin: HBaseAdmin = null;
  @transient
  var connection: Connection = null;

  // new HbaseUtils 时初始化
  init

  /**
   * 初始化hbase
   */
  def init = {
    if (connection == null && hBaseAdmin == null) {
      connection = ConnectionFactory.createConnection(getConf)
      hBaseAdmin = connection.getAdmin().asInstanceOf[HBaseAdmin]
    }
  }

  def getConf = {
    val conf = HBaseConfiguration.create()
    conf.set(HConstants.ZOOKEEPER_QUORUM, "xc-online-zk")
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
    conf
  }

  def getJobConf(tbName: String) = {
    val jobConf = new JobConf(getConf)
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, tbName)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf
  }

  /**
   * 创建表
   */
  def createTable(tbName: String, columnName: String) = {
    if (!hBaseAdmin.tableExists(tbName)) {
      // 根据表名生成表描述
      val tableDesc = new HTableDescriptor(TableName.valueOf(tbName))
      // 生成表的相关列
      tableDesc.addFamily(new HColumnDescriptor(columnName))
      // 创建表
      hBaseAdmin.createTable(tableDesc)
    }
  }

  /**
   * 创建多个表
   */
  def createTables(tbName: String, columnNames: List[String]) = {
    if (!hBaseAdmin.tableExists(tbName)) {
      // 根据表名生成表描述
      val tableDesc = new HTableDescriptor(TableName.valueOf(tbName))
      // 生成表的相关列
      columnNames.foreach(columnName => tableDesc.addFamily(new HColumnDescriptor(columnName)))
      // 创建表
      hBaseAdmin.createTable(tableDesc)
    }
  }

  /**
   * 对象转为put
   *
   * @param columnName
   * @param cl
   * @return
   */
  def convertToPut(columnName: String, cl: LearningCourseOnlineDwm) = {
    // 配置rowKey
    val put = new Put(Bytes.toBytes(cl.learning_course_online_id))
    // 配置其他列
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("course_id"), Bytes.toBytes(cl.course_id))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("course_name"), Bytes.toBytes(cl.course_name))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("video_name"), Bytes.toBytes(cl.video_name))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("user_id"), Bytes.toBytes(cl.user_id))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("user_name"), Bytes.toBytes(cl.user_name))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("learn_time"), Bytes.toBytes(cl.learn_time))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("learn_count"), Bytes.toBytes(cl.learn_count))
    put.addColumn(Bytes.toBytes(columnName), Bytes.toBytes("date_info"), Bytes.toBytes(cl.date_info))
    put
  }


  /**
   * 单个入库
   */
  def put(tbName: String, put: Put) = {
    init
    // new对象是初始化hbase
    val table = connection.getTable(TableName.valueOf(tbName))
    try {
      table.put(put)
    } catch {
      case e => e.printStackTrace()
    }
    table.close()
  }

  /**
   * 批量入库
   */
  def putList(tbName: String, puts: java.util.List[Put]) = {
    init
    val table = connection.getTable(TableName.valueOf(tbName))
    try {
      table.put(puts)
    } catch {
      case e => e.printStackTrace()
    }
    table.close()
  }

  /**
   * 根据rowKey去hbase批量查询
   *
   */
  def getByRowKeyList(tbName: String, rowKeyList: ArrayBuffer[String]) = {
    // scala转java工具类
    import scala.collection.JavaConverters._
    var keyBuffer = ArrayBuffer[Get]()
    val table = connection.getTable(TableName.valueOf(tbName))
    for (rowKey <- rowKeyList) {
      keyBuffer += new Get(Bytes.toBytes(rowKey))
    }
    val results = table.get(keyBuffer.asJava)
    var resultBuffer = ArrayBuffer[String]()
    for (result <- results) {
      var tempBuffer = ArrayBuffer[String]()
      for (kv <- result.rawCells()) {
        val value = Bytes.toString(CellUtil.cloneValue(kv));
        tempBuffer += value
      }
      resultBuffer += tempBuffer.mkString(",") + "\t"
    }
    resultBuffer
  }

}
