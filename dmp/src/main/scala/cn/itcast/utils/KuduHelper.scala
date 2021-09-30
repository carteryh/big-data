package cn.itcast.utils

import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.config.ConfigFactory
import org.apache.commons.lang.time.FastDateFormat
import org.apache.kudu.Schema
import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

class KuduHelper {
  private var kuduContext: KuduContext = _
  private var spark: SparkSession = _
  private var dataset: Dataset[_] = _

  private val config = ConfigFactory.load("kudu")

  def this(spark: SparkSession) = {
    this()
    this.spark = spark

    // create 1: 创建 KuduContext
    // create 2: 加载配置文件
    val master = config.getString("kudu.master")
    kuduContext = new KuduContext(master, spark.sparkContext, Some(900000))
  }

  def this(dataset: Dataset[_]) = {
    this(dataset.sparkSession)
    this.dataset = dataset
  }

  // 二: 具体功能的开发
  //    1. 创建表
  //    2. 读取表
  //    3. 写入数据

  /**
    * 1. 通过隐式转换, 将 SparkSession 对象转为 KuduHelper 的对象
    * 2. 通过 KuduHelper 中的 createKuduTable() 就可以创建一张表
    *
    * 调用方式: spark.createKuduTable()
    */
  def createKuduTable(tableName: String, schema: Schema, keys: List[String]): Unit = {
    createKuduTable(tableName, schema, keys, isDelete = true)
  }

  def createKuduTable(tableName: String, schema: Schema, keys: List[String], isDelete: Boolean): Unit = {
    // 问题一: KuduContext 来创建
    // 问题二: KuduContext 创建的时候, 需要 Kudu 服务器的地址, 还需要 SparkSession 对象, 超时时间
    // create 3: 如果存在则删掉旧的表
    if (kuduContext.tableExists(tableName)) {
      kuduContext.deleteTable(tableName)
    }

    // create 4: 创建表
    import scala.collection.JavaConverters._

    val options = new CreateTableOptions()
      .setNumReplicas(config.getInt("kudu.table.factor"))
      .addHashPartitions(keys.asJava, 2)

    kuduContext.createTable(tableName, schema, options)
  }

  /**
    * 参数: TableName
    * 返回: DataFrame
    * 问题: 要考虑表不存在的情况
    */
  def readKuduTable(tableName: String): Option[DataFrame] = {
    import org.apache.kudu.spark.kudu._

    if (kuduContext.tableExists(tableName)) {
      val result = spark.read
        .option("kudu.master", kuduContext.kuduMaster)
        .option("kudu.table", tableName)
//        .kudu
        .format("kudu")
        .load()

      Some(result)
    } else {
      None
    }

//    val m1 = Map("" -> "")
//    val v: Option[String] = m1.get("")
  }

  /**
    * spark.readKuduTable
    * spark.createKuduTable
    * dataset.saveToKudu
    */
  def saveToKudu(tableName: String): Unit = {
    // 1. 判断本方法是从 Dataset 上调用的
    if (dataset == null) {
      throw new RuntimeException("请从 Dataset 上开始调用")
    }

    // 2. 保存数据
    import org.apache.kudu.spark.kudu._
    dataset.write
      .option("kudu.master", kuduContext.kuduMaster)
      .option("kudu.table", tableName)
      .mode(SaveMode.Append)
//      .kudu
      .format("kudu")
      .save()
  }

}

object KuduHelper {
  // 一: 隐式转换, SparkSession -> KuduHelper   DataFrame -> KuduHelper

  /**
    * 设计隐式转换的时候, 只需要考虑一件事, 把 XX 转为 YY
    * XX 是 SparkSession, 转换函数的传入参数就是 SparkSession
    * YY 是 KuduHelper, 转换函数的结果类型应该就是 KuduHelper
    */
  implicit def sparkSessionToKuduHelper(spark: SparkSession): KuduHelper = {
    new KuduHelper(spark)
  }

  implicit def dataFrameToKudu(dataset: Dataset[_]): KuduHelper = {
    new KuduHelper(dataset)
  }

  def formattedDate(): String = {
    FastDateFormat.getInstance("yyyMMdd").format(new Date())
  }
}
