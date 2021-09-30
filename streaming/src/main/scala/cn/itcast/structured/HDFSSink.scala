package cn.itcast.structured

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object HDFSSink {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutil")

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("hdfs_sink")
      .master("local[6]")
      .getOrCreate()

    import spark.implicits._

    // 2. 读取 Kafka 数据
    val source: Dataset[String] = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      .option("subscribe", "streaming_test_2")
      .option("startingOffsets", "earliest")
      .load()
      .selectExpr("CAST(value AS STRING) as value")
      .as[String]

    // 1::Toy Story (1995)::Animation|Children's|Comedy

    // 3. 处理 CSV, Dataset(String), Dataset(id, name, category)
    val result = source.map(item => {
      val arr = item.split("::")
      (arr(0).toInt, arr(1).toString, arr(2).toString)
    }).as[(Int, String, String)].toDF("id", "name", "category")

    // 4. 落地到 HDFS 中
    result.writeStream
      .format("parquet")
      .option("path", "dataset/streaming/moives/")
      .option("checkpointLocation", "checkpoint")
      .start()
      .awaitTermination()
  }
}
