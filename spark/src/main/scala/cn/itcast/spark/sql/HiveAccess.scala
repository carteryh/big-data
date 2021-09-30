package cn.itcast.spark.sql

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructField, StructType}

object HiveAccess {

  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkSession
    //    1. 开启 Hive 支持
    //    2. 指定 Metastore 的位置
    //    3. 指定 Warehouse 的位置
    val spark = SparkSession.builder()
      .appName("hive access1")
      .enableHiveSupport()
      .config("hive.metastore.uris", "thrift://node01:9083")
      .config("spark.sql.warehouse.dir", "/dataset/hive")
//      .config("spark.sql.warehouse.dir", "hdfs://node01:9000/dataset/hive")
      .getOrCreate()

    import spark.implicits._

    // 2. 读取数据
    //    1. 上传 HDFS, 因为要在集群中执行, 没办法保证程序在哪个机器中执行
    //        所以, 要把文件上传到所有的机器中, 才能读取本地文件
    //        上传到 HDFS 中就可以解决这个问题, 所有的机器都可以读取 HDFS 中的文件
    //        它是一个外部系统
    //    2. 使用 DF 读取数据

    val schema = StructType(
      List(
        StructField("name", StringType),
        StructField("age", IntegerType),
        StructField("gpa", FloatType)
      )
    )

    val dataframe = spark.read
      .option("delimiter", "\t")
      .schema(schema)
      .csv("hdfs:///dataset/studenttab10k")

    val resultDF = dataframe.where('age > 50)

    // 3. 写入数据, 使用写入表的 API, saveAsTable
    resultDF.write.mode(SaveMode.Overwrite).saveAsTable("spark03.student")
  }
}
