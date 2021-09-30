package cn.itcast.structured

import java.sql.{Connection, DriverManager, Statement}

import org.apache.spark.sql.{Dataset, ForeachWriter, Row, SparkSession}

object ForeachSink {

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

    // 4. 落地到 MySQL
    class MySQLWriter extends ForeachWriter[Row] {
      private val driver = "com.mysql.jdbc.Driver"
      private var connection: Connection = _
      private val url = "jdbc::mysql://node01:3306/streaming-movies-result"
      private var statement: Statement = _

      override def open(partitionId: Long, version: Long): Boolean = {
        Class.forName(driver)
        connection = DriverManager.getConnection(url)
        statement = connection.createStatement()
        true
      }

      override def process(value: Row): Unit = {
        statement.executeUpdate(s"insert into movies values(${value.get(0)}, ${value.get(1)}, ${value.get(2)})")
      }

      override def close(errorOrNull: Throwable): Unit = {
        connection.close()
      }
    }

    result.writeStream
      .foreach(new MySQLWriter)
      .start()
      .awaitTermination()
  }
}
