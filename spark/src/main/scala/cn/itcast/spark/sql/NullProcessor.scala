package cn.itcast.spark.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField, StructType}
import org.junit.Test

class NullProcessor {
  // 1. 创建 SparkSession
  val spark = SparkSession.builder()
    .master("local[6]")
    .appName("null processor")
    .getOrCreate()

  @Test
  def nullAndNaN(): Unit = {


    // 2. 导入数据集

    // 3. 读取数据集
    //    1. 通过Saprk-csv自动的推断类型来读取, 推断数字的时候会将 NaN 推断为 字符串
//    spark.read
//      .option("header", true)
//      .option("inferSchema", true)
//      .csv(...)
    //    2. 直接读取字符串, 在后续的操作中使用 map 算子转类型
//    spark.read.csv().map( row => row... )
    //    3. 指定 Schema, 不要自动推断
    val schema = StructType(
      List(
        StructField("id", LongType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val sourceDF = spark.read
      .option("header", value = true)
      .schema(schema)
      .csv("dataset/beijingpm_with_nan.csv")

    sourceDF.show()

    // 4. 丢弃
    // 2019, 12, 12, NaN
    // 规则:
    //      1. any, 只有有一个 NaN 就丢弃
    sourceDF.na.drop("any").show()
    sourceDF.na.drop().show()
    //      2. all, 所有数据都是 NaN 的行才丢弃
    sourceDF.na.drop("all").show()
    //      3. 某些列的规则
    sourceDF.na.drop("any", List("year", "month", "day", "hour")).show()

    // 5. 填充
    // 规则:
    //     1. 针对所有列数据进行默认值填充
    sourceDF.na.fill(0).show()
    //     2. 针对特定列填充
    sourceDF.na.fill(0, List("year", "month")).show()
  }

  @Test
  def strProcessor(): Unit = {
    // 读取数据集
    val sourceDF = spark.read
      .option("header", value = true)
      .option("inferSchema", value = true)
      .csv("dataset/BeijingPM20100101_20151231.csv")

//    sourceDF.show()

    // 1. 丢弃
    import spark.implicits._
//    sourceDF.where('PM_Dongsi =!= "NA").show()

    // 2. 替换
    import org.apache.spark.sql.functions._
    // select name, age, case
    // when ... then ...
    // when ... then ...
    // else
    sourceDF.select(
      'No as "id", 'year, 'month, 'day, 'hour, 'season,
      when('PM_Dongsi === "NA", Double.NaN)
        .otherwise('PM_Dongsi cast DoubleType)
        .as("pm")
    ).show()

    // 原类型和转换过后的类型, 必须一致
    sourceDF.na.replace("PM_Dongsi", Map("NA" -> "NaN", "NULL" -> "null")).show()
  }

}
