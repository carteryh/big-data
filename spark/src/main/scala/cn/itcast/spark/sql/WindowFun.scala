package cn.itcast.spark.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window

object WindowFun {

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

//    // 1. 定义窗口
//    val window = Window.partitionBy('category)
//      .orderBy('revenue.desc)
//
//    // 2. 数据处理
//    import org.apache.spark.sql.functions._
//    source.select('product, 'category, dense_rank() over window as "rank")
//      .where('rank <= 2)
//      .show()

    source.createOrReplaceTempView("productRevenue")
    spark.sql("select product, category, revenue from " +
      "(select *, dense_rank() over (partition by category order by revenue desc) as rank from productRevenue) " +
      "where rank <= 2")
      .show()
  }
}
