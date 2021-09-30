package cn.itcast.report
import cn.itcast.etl.ETLRunner
import cn.itcast.utils.KuduHelper
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.DataFrame

object NewRegionReportProcessor extends ReportProcessor {

  override def sourceTableName(): String = {
    ETLRunner.ODS_TABLE_NAME
  }

  override def process(dataFrame: DataFrame): DataFrame = {
    import org.apache.spark.sql.functions._
    import dataFrame.sparkSession.implicits._

    dataFrame.groupBy('region, 'city)
      .agg(count("*") as "count")
      .select('region, 'city, 'count)
  }

  override def targetTableName(): String = {
    "report_data_region_" + KuduHelper.formattedDate()
  }

  override def targetTableSchema(): Schema = {
    import scala.collection.JavaConverters._
    new Schema(
      List(
        new ColumnSchemaBuilder("region", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("city", Type.STRING).nullable(false).key(true).build(),
        new ColumnSchemaBuilder("count", Type.INT64).nullable(false).key(false).build()
      ).asJava
    )
  }

  override def targetTableKeys(): List[String] = {
    List("region", "city")
  }
}
