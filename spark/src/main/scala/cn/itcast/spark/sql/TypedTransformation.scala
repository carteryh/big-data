package cn.itcast.spark.sql

import java.lang

import org.apache.spark.sql.{DataFrame, Dataset, KeyValueGroupedDataset, Row, SparkSession}
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructField, StructType}
import org.junit.Test

class TypedTransformation {
  // 1. 创建 SparkSession
  val spark = SparkSession.builder().master("local[6]").appName("typed").getOrCreate()
  import spark.implicits._

  @Test
  def trans(): Unit = {
    // 3. flatMap
    val ds1 = Seq("hello spark", "hello hadoop").toDS
    ds1.flatMap( item => item.split(" ") ).show()

    // 4. map
    val ds2 = Seq(Person("zhangsan", 15), Person("lisi", 20)).toDS()
    ds2.map(person => Person(person.name, person.age * 2)).show()

    // 5. mapPartitions
    ds2.mapPartitions(
      // iter 不能大到每个 Executor 的内存放不下, 不然就会 OOM
      // 对每个元素进行转换, 后生成一个新的集合
      iter => {
        val result = iter.map(person => Person(person.name, person.age * 2))
        result
      }
    ).show()
  }

  @Test
  def trans1(): Unit = {
    val ds = spark.range(10)
    ds.transform(dataset => dataset.withColumn("doubled", 'id * 2))
      .show()
  }

  @Test
  def as(): Unit = {
    // 1. 读取
    val schema = StructType(
      Seq(
        StructField("name", StringType),
        StructField("age", IntegerType),
        StructField("gpa", FloatType)
      )
    )

    val df: DataFrame = spark.read
      .schema(schema)
      .option("delimiter", "\t")
      .csv("dataset/studenttab10k")

    // 2. 转换
    // 本质上: Dataset[Row].as[Student] => Dataset[Student]
    // Dataset[(String, int, float)].as[Student] => Dataset[Student]
    val ds: Dataset[Student] = df.as[Student]

    // 3. 输出
    ds.show()
  }

  @Test
  def filter(): Unit = {
    val ds = Seq(Person("zhangsan", 15), Person("lisi", 20)).toDS()
    ds.filter( person => person.age > 15 ).show()
  }

  @Test
  def groupByKey(): Unit = {
    val ds = Seq(Person("zhangsan", 15), Person("zhangsan", 16), Person("lisi", 20)).toDS()

    // select count(*) from person group by name
    val grouped: KeyValueGroupedDataset[String, Person] = ds.groupByKey(person => person.name)
    val result: Dataset[(String, Long)] = grouped.count()

    result.show()
  }

  @Test
  def split(): Unit = {
    val ds = spark.range(15)
    // randomSplit, 切多少份, 权重多少
    val datasets: Array[Dataset[lang.Long]] = ds.randomSplit(Array(5, 2, 3))
    datasets.foreach(_.show())

    // sample
    ds.sample(withReplacement = false, fraction = 0.4).show()
  }

  @Test
  def sort(): Unit = {
    val ds = Seq(Person("zhangsan", 12), Person("zhangsan", 8), Person("lisi", 15)).toDS()
    ds.orderBy('age.desc).show() // select * from ... order by ... asc
    ds.sort('age.asc).show()
  }

  @Test
  def dropDuplicates(): Unit = {
    val ds = spark.createDataset(Seq(Person("zhangsan", 15), Person("zhangsan", 15), Person("lisi", 15)))
    ds.distinct().show()
    ds.dropDuplicates("age").show()
  }

  @Test
  def collection(): Unit = {
    val ds1 = spark.range(1, 10)
    val ds2 = spark.range(5, 15)

    // 差集
    ds1.except(ds2).show()

    // 交集
    ds1.intersect(ds2).show()

    // 并集
    ds1.union(ds2).show()

    // limit
    ds1.limit(3).show()
  }

}

case class Student(name: String, age: Int, gpa: Float)
