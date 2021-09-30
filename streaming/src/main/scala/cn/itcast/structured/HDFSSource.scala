package cn.itcast.structured

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StructType

object HDFSSource {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutil")

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("hdfs_source")
      .master("local[6]")
      .getOrCreate()

    // 2. 数据读取, 目录只能是文件夹, 不能是某一个文件
    val schema = new StructType()
      .add("name", "string")
      .add("age", "integer")

    val source = spark.readStream
      .schema(schema)
      .json("hdfs://node01:8020/dataset/dataset")

    // 3. 输出结果
    source.writeStream
      .outputMode(OutputMode.Append())
      .format("console")
      .start()
      .awaitTermination()
  }
}
