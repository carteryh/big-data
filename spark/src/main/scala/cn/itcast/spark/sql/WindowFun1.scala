package cn.itcast.spark.sql

import org.apache.spark.sql
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window

object WindowFun1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("window")
      .master("local[6]")
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

    val data = Seq(
      ("Thin", "Cell phone", 6000),
      ("Normal", "Tablet", 1500),
      ("Mini", "Tablet", 5500),
      ("Ultra thin", "Cell phone", 5500),
      ("Very thin", "Cell phone", 6000),
      ("Big", "Tablet", 2500),
      ("Bendable", "Cell phone", 3000),
      ("Foldable", "Cell phone", 3000),
      ("Pro", "Tablet", 4500),
      ("Pro2", "Tablet", 6500)
    )

    val source = data.toDF("product", "category", "revenue")

    // 1. 定义窗口, 按照分类进行倒叙排列
    val window = Window.partitionBy('category)
      .orderBy('revenue.desc)

    // 2. 找到最贵的的商品价格
    val maxPrice: sql.Column = max('revenue) over window

    // 3. 得到结果
    source.select('product, 'category, 'revenue, (maxPrice - 'revenue) as "revenue_difference")
      .show()
  }
}
