package cn.itcast.spark.sql

import org.apache.spark.sql.SparkSession

object UDF {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("window")
      .master("local[6]")
      .getOrCreate()

    import spark.implicits._

    val source = Seq(
      ("Thin", "Cell phone", 6000),
      ("Normal", "Tablet", 1500),
      ("Mini", "Tablet", 5500),
      ("Ultra thin", "Cell phone", 5000),
      ("Very thin", "Cell phone", 6000),
      ("Big", "Tablet", 2500),
      ("Bendable", "Cell phone", 3000),
      ("Foldable", "Cell phone", 3000),
      ("Pro", "Tablet", 4500),
      ("Pro2", "Tablet", 6500)
    ).toDF("product", "category", "revenue")

    // 需求一: 聚合每个类别的总价
    // 1. 分组, 2. 对每一组的数据进行聚合
    import org.apache.spark.sql.functions._
//    source.groupBy('category)
//      .agg(sum('revenue))
//      .show()

    // 需求二: 把名称变为小写
//    source.select(lower('product))
//      .show()

    // 需求三: 把价格变为字符串形式
    // 6000 6K
    val toStrUDF = udf(toStr _)
    source.select('product, 'category, toStrUDF('revenue))
      .show()
  }

  def toStr(revenue: Long): String = {
    (revenue / 1000) + "K"
  }
}
