package cn.itcast.report

import org.apache.spark.sql.SparkSession

object DailyReportRunner {

  def main(args: Array[String]): Unit = {
    import cn.itcast.utils.SparkConfigHelper._
    import cn.itcast.utils.KuduHelper._

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("daily report runner")
      .loadConfig()
      .getOrCreate()

    // 2. 创建一个容器, 放置所有的 Processor
    val processors = List[ReportProcessor](
      NewRegionReportProcessor,
      AdsRegionReportProcessor
    )

    // 3. 循环容器, 拿到每一个 Processor, 每一个 Processor 都代表了一个报表的统计过程
    for (processor <- processors) {
      // 4. 读取源表
      val source = spark.readKuduTable(processor.sourceTableName())
      if (source.isDefined) {
        val sourceDF = source.get
        // 5. 数据处理
        val result = processor.process(sourceDF)
        // 6. 数据落地
        spark.createKuduTable(
          processor.targetTableName(),
          processor.targetTableSchema(),
          processor.targetTableKeys()
        )
        result.saveToKudu(processor.targetTableName())
      }
    }
  }
}
