package cn.itcast.spark.sql

import org.apache.spark.sql.{RelationalGroupedDataset, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.junit.Test

class AggProcessor {
  // 1. 创建 SparkSession
  val spark = SparkSession.builder()
    .master("local[6]")
    .appName("agg processor")
    .getOrCreate()

  import spark.implicits._

  @Test
  def groupBy(): Unit = {
    // 2. 数据读取
    val schema = StructType(
      List(
        StructField("id", IntegerType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val sourceDF = spark.read
      .schema(schema)
      .option("header", value = true)
      .csv("dataset/beijingpm_with_nan.csv")

    // 3. 数据去掉空值
    val cleanDF = sourceDF.where('pm =!= Double.NaN)

    // 分组
    val groupedDF: RelationalGroupedDataset = cleanDF.groupBy('year, $"month")

    // 4. 使用 functions 函数来完成聚合
    import org.apache.spark.sql.functions._

    // 本质上, avg 这个函数定义了一个操作, 把表达式设置给 pm 列
    // select avg(pm) from ... group by
    groupedDF.agg(avg('pm) as "pm_avg")
      .orderBy('pm_avg.desc)
      .show()

    groupedDF.agg(stddev(""))
      .orderBy('pm_avg.desc)
      .show()

    // 5. 使用 GroupedDataset 的 API 来完成聚合
    groupedDF.avg("pm")
      .select($"avg(pm)" as "pm_avg")
      .orderBy("pm_avg")
      .show()

    groupedDF.sum()
      .select($"avg(pm)" as "pm_avg")
      .orderBy("pm_avg")
      .show()
  }

  @Test
  def multiAgg(): Unit = {
    val schemaFinal = StructType(
      List(
        StructField("source", StringType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val pmFinal = spark.read
      .schema(schemaFinal)
      .option("header", value = true)
      .csv("dataset/pm_final.csv")

    import org.apache.spark.sql.functions._

    // 需求1: 不同年, 不同来源, PM 值的平均数
    // select source, year, avg(pm) as pm from ... group by source, year
    val postAndYearDF = pmFinal.groupBy('source, 'year)
      .agg(avg('pm) as "pm")

    // 需求2: 在整个数据集中, 按照不同的来源来统计 PM 值的平均数
    // select source, avg(pm) as pm from ... group by source
    val postDF = pmFinal.groupBy('source)
      .agg(avg('pm) as "pm")
      .select('source, lit(null) as "year", 'pm)

    // 合并在同一个结果集中
    postAndYearDF.union(postDF)
      .sort('source, 'year.asc_nulls_last, 'pm)
      .show()
  }

  @Test
  def rollup(): Unit = {
    import org.apache.spark.sql.functions._

    val sales = Seq(
      ("Beijing", 2016, 100),
      ("Beijing", 2017, 200),
      ("Shanghai", 2015, 50),
      ("Shanghai", 2016, 150),
      ("Guangzhou", 2017, 50)
    ).toDF("city", "year", "amount")

    // 滚动分组, A, B 两列, AB, A, null
    sales.rollup('city, 'year)
      .agg(sum('amount) as "amount")
      .sort('city.asc_nulls_last, 'year.asc_nulls_last)
      .show()
  }

  @Test
  def rollup1(): Unit = {
    import org.apache.spark.sql.functions._

    // 1. 数据集读取
    val schemaFinal = StructType(
      List(
        StructField("source", StringType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val pmFinal = spark.read
      .schema(schemaFinal)
      .option("header", value = true)
      .csv("dataset/pm_final.csv")

    // 2. 聚合和统计
    // 需求1: 每个PM值计量者, 每年PM值统计的平均数 groupby source year
    // 需求2: 每个PM值计量者, 整体上的PM平均值 groupby source
    // 需求3: 全局所有的计量者, 和日期的PM值的平均值 groupby null
    pmFinal.rollup('source, 'year)
      .agg(avg('pm) as "pm")
      .sort('source.asc_nulls_last, 'year.asc_nulls_last)
      .show()
  }

  @Test
  def cube(): Unit = {
    val schemaFinal = StructType(
      List(
        StructField("source", StringType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val pmFinal = spark.read
      .schema(schemaFinal)
      .option("header", value = true)
      .csv("dataset/pm_final.csv")

    import org.apache.spark.sql.functions._

    pmFinal.cube('source, 'year)
      .agg(avg('pm) as "pm")
      .sort('source.asc_nulls_last, 'year.asc_nulls_last)
      .show()
  }

  @Test
  def cubeSql(): Unit = {
    val schemaFinal = StructType(
      List(
        StructField("source", StringType),
        StructField("year", IntegerType),
        StructField("month", IntegerType),
        StructField("day", IntegerType),
        StructField("hour", IntegerType),
        StructField("season", IntegerType),
        StructField("pm", DoubleType)
      )
    )

    val pmFinal = spark.read
      .schema(schemaFinal)
      .option("header", value = true)
      .csv("dataset/pm_final.csv")

    pmFinal.createOrReplaceTempView("pm_final")

    val result = spark.sql("select source, year, avg(pm) as pm from pm_final group by source, year " +
      "grouping sets ((source, year), (source), (year), ())" +
      "order by source asc nulls last, year asc nulls last")

    result.show()
  }

}
