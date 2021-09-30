package cn.itcast.spark.sql

import org.apache.spark.sql.SparkSession
import org.junit.Test

class UntypedTransformation {
  val spark = SparkSession.builder().master("local[6]").appName("typed").getOrCreate()
  import spark.implicits._

  @Test
  def select(): Unit = {
    val ds = Seq(Person("zhangsan", 12), Person("lisi", 18), Person("zhangsan", 8)).toDS

    // select * from ...
    // from ... select ...
    // 在 Dataset 中, select 可以在任何位置调用
    // select count(*)
    ds.select('name).show()

    ds.selectExpr("sum(age)").show()

    import org.apache.spark.sql.functions._

    ds.select(expr("sum(age)")).show()
  }

  @Test
  def column(): Unit = {
    val ds = Seq(Person("zhangsan", 12), Person("lisi", 18), Person("zhangsan", 8)).toDS

    import org.apache.spark.sql.functions._

    // select rand() from ...
    // 如果想使用函数功能
    // 1. 使用 functions.xx
    // 2. 使用表达式, 可以使用 expr("...") 随时随地编写表达式
    ds.withColumn("random", expr("rand()")).show()

    ds.withColumn("name_new", 'name).show()

    ds.withColumn("name_jok", 'name === "").show()

    ds.withColumnRenamed("name", "new_name").show()
  }

  @Test
  def groupBy(): Unit = {
    val ds = Seq(Person("zhangsan", 12), Person("zhangsan", 8), Person("lisi", 15)).toDS()

    // 为什么 GroupByKey 是有类型的, 最主要的原因是因为 groupByKey 所生成的对象中的算子是有类型的
//    ds.groupByKey( item => item.name ).mapValues()

    // 为什么  GroupBy 是无类型的, 因为 groupBy 所生成的对象中的算子是无类型的, 针对列进行处理的
    import org.apache.spark.sql.functions._

    ds.groupBy('name).agg(mean("age")).show()
  }
}
