package cn.itcast.kudu

import org.apache.kudu.client.{CreateTableOptions, KuduTable}
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
import org.junit.Test

class KuduSparkAPI {

  @Test
  def ddl(): Unit = {
    // 1. 创建 KuduContext 和 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("kudu")
      .getOrCreate()

    val KUDU_MASTERS = "192.168.169.101:7051,192.168.169.102:7051,192.168.169.103:7051,"
    val kuduContext = new KuduContext(KUDU_MASTERS, spark.sparkContext)

    // 2. 判断表是否存在, 如果存在则删除表
    val TABLE_NAME = "students"
    if (kuduContext.tableExists(TABLE_NAME)) {
      kuduContext.deleteTable(TABLE_NAME)
    }

    // 3. 创建一张 Kudu 表
    val schema = StructType(
      StructField("name", StringType, nullable = false) ::
      StructField("age", IntegerType, nullable = false) ::
      StructField("gpa", DoubleType, nullable = false) :: Nil
    )

    val keys = Seq("name")

    import scala.collection.JavaConverters._
    val options = new CreateTableOptions()
        .setRangePartitionColumns(List("name").asJava)
        .setNumReplicas(1)

      kuduContext.createTable(tableName = TABLE_NAME,
      schema = schema,
      keys = keys,
      options = options)
  }

  @Test
  def crud(): Unit = {
    // 1. 创建 KuduContext 和 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("kudu")
      .getOrCreate()

    val KUDU_MASTERS = "192.168.169.101:7051,192.168.169.102:7051,192.168.169.103:7051"
    val kuduContext = new KuduContext(KUDU_MASTERS, spark.sparkContext)

    // 2. 增
    import spark.implicits._
    val df = Seq(Student("zhangsan", 15, 60.1), Student("lisi", 10, 50.6)).toDF()

    val TABLE_NAME = "students"
    kuduContext.insertRows(df, TABLE_NAME)

    // 3. 删
    kuduContext.deleteRows(df.select($"name"), TABLE_NAME)

    // 4. 增改
    kuduContext.upsertRows(df, TABLE_NAME)

    // 5. 改
    kuduContext.updateRows(df, TABLE_NAME)
  }

  @Test
  def dfWrite(): Unit = {
    // 1. 创建 KuduContext 和 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("kudu")
      .getOrCreate()

    // 2. 读取数据
    val schema = StructType(
      StructField("name", StringType, nullable = false) ::
      StructField("age", IntegerType, nullable = false) ::
      StructField("gpa", DoubleType, nullable = false) :: Nil
    )

    val originDF = spark.read
      .option("header", value = false)
      .option("delimiter", value = "\t")
      .schema(schema)
      .csv("dataset/studenttab10k")

    // 3. 写入 Kudu 中
    val TABLE_NAME = "students"
    val KUDU_MASTERS = "192.168.169.101:7051,192.168.169.102:7051,192.168.169.103:7051,"

    import org.apache.kudu.spark.kudu._

    originDF.write
      .option("kudu.table", TABLE_NAME)
      .option("kudu.master", KUDU_MASTERS)
      .mode(SaveMode.Append)
      .kudu
  }

  @Test
  def dfRead(): Unit = {
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("kudu")
      .getOrCreate()

    val TABLE_NAME = "students"
    val KUDU_MASTERS = "192.168.169.101:7051,192.168.169.102:7051,192.168.169.103:7051"

    import org.apache.kudu.spark.kudu._

    val kuduDF = spark.read
      .option("kudu.table", TABLE_NAME)
      .option("kudu.master", KUDU_MASTERS)
      .kudu

    kuduDF.createOrReplaceTempView("kudu_students")

    val projectDF = spark.sql("select name from kudu_students where gpa > 2")

    projectDF.show()
  }
}

case class Student(name: String, age: Int, gpa: Double)
