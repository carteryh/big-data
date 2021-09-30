package cn.itcast.structured

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

object Triggers {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutil")
    // 创建数据源
    val spark = SparkSession.builder()
      .appName("triggers")
      .master("local[6]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    // timestamp, value
    val source = spark.readStream
      .format("rate")
      .load()

    // 简单处理
    //
    val result = source

    // 落地
    source.writeStream
      .format("console")
      .outputMode(OutputMode.Append())
      .trigger(Trigger.Once())
      .start()
      .awaitTermination()
  }
}
