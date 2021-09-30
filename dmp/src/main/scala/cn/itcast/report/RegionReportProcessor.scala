package cn.itcast.report

import cn.itcast.etl.ETLRunner
import cn.itcast.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.{DataFrame, SparkSession}

object RegionReportProcessor {

  /**
    * Kudu.ODS -> 分组统计 -> Kudu.Report
    */
  def main(args: Array[String]): Unit = {
    import cn.itcast.utils.SparkConfigHelper._
    import cn.itcast.utils.KuduHelper._

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("report_data_region")
      .master("local[6]")
      .loadConfig()
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 2. 读取 ODS 层数据
    val source: Option[DataFrame] = spark.readKuduTable(SOURCE_TABLE_NAME)

    if (source.isEmpty) return

    val sourceDF = source.get

    // 3. 按照省市进行分组求得结果
    val result = sourceDF.groupBy($"region", $"city")
      .agg(count("*") as "count")
      .select('region, 'city, 'count)

    // 4. 落地到 Report 表中
    spark.createKuduTable(TARGET_TABLE_NAME, schema, keys)
    result.saveToKudu(TARGET_TABLE_NAME)
  }

  private val SOURCE_TABLE_NAME = ETLRunner.ODS_TABLE_NAME
  private val TARGET_TABLE_NAME = "report_data_region_" + KuduHelper.formattedDate()

  private val keys = List("region", "city")

  import scala.collection.JavaConverters._
  private val schema = new Schema(
    List(
      new ColumnSchemaBuilder("region", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("city", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("count", Type.INT64).nullable(false).key(false).build()
    ).asJava
  )
}
