package cn.itcast.structured

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{BooleanType, DateType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object KafkaSource {

  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("hdfs_source")
      .master("local[6]")
      .getOrCreate()

    import spark.implicits._

    // 2. 读取 Kafka 数据
    val source: DataFrame = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "node01:9092,node02:9092,node03:9092")
      .option("subscribe", "streaming_test_1")
      .option("startingOffsets", "earliest")
      .load()

    // 3. 定义 JSON 中的类型
    val eventType = new StructType()
      .add("has_sound", BooleanType)
      .add("has_motion", BooleanType)
      .add("has_person", BooleanType)
      .add("start_time", DateType)
      .add("end_time", DateType)

    val cameraType = new StructType()
      .add("device_id", StringType)
      .add("last_event", eventType)

    val deviceType = new StructType()
      .add("cameras", cameraType)

    val schema = new StructType()
      .add("devices", deviceType)

    // 4. 解析 JSON
    // 需求: DataFrame(time, has_person)
    import org.apache.spark.sql.functions._

    val jsonOptions = Map("timestampFormat" -> "yyyy-MM-dd'T'HH:mm:ss.sss'Z'")

    val result = source.selectExpr("CAST(key AS STRING) as key", "CAST(value AS STRING) as value")
      .select(from_json('value, schema, jsonOptions).alias("parsed_value"))
      .selectExpr("parsed_value.devices.cameras.last_event.start_time", "parsed_value.devices.cameras.last_event.has_person")

    // 5. 打印数据
    result.writeStream
      .format("console")
      .outputMode(OutputMode.Append())
      .start()
      .awaitTermination()
  }
}
