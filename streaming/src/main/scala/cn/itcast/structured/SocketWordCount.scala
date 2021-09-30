package cn.itcast.structured

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SocketWordCount {

  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("socket_structured")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    // 2. 数据集的生成, 数据读取
    val source: DataFrame = spark.readStream
      .format("socket")
      .option("host", "192.168.169.101")
      .option("port", 9999)
      .load()

    val sourceDS: Dataset[String] = source.as[String]

    // 3. 数据的处理
    val words = sourceDS.flatMap(_.split(" "))
      .map((_, 1))
      .groupByKey(_._1)
      .count()

    // 4. 结果集的生成和输出
    words.writeStream
      .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }
}
