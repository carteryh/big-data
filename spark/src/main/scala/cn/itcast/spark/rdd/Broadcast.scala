package cn.itcast.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class Broadcast {

  /**
    * 资源占用比较大, 有十个对应的 value
    */
  @Test
  def bc1(): Unit = {
    // 数据, 假装这个数据很大, 大概一百兆
    val v = Map("Spark" -> "http://spark.apache.cn", "Scala" -> "http://www.scala-lang.org")

    val config = new SparkConf().setMaster("local[6]").setAppName("bc")
    val sc = new SparkContext(config)

    // 将其中的 Spark 和 Scala 转为对应的网址
    val r = sc.parallelize(Seq("Spark", "Scala"))
    val result = r.map(item => v(item)).collect()

    println(result)
  }

  /**
    * 使用广播, 大幅度减少 value 的复制
    */
  @Test
  def bc2(): Unit = {
    // 数据, 假装这个数据很大, 大概一百兆
    val v = Map("Spark" -> "http://spark.apache.cn", "Scala" -> "http://www.scala-lang.org")

    val config = new SparkConf().setMaster("local[6]").setAppName("bc")
    val sc = new SparkContext(config)

    // 创建广播
    val bc = sc.broadcast(v)

    // 将其中的 Spark 和 Scala 转为对应的网址
    val r = sc.parallelize(Seq("Spark", "Scala"))

    // 在算子中使用广播变量代替直接引用集合, 只会复制和executor一样的数量
    // 在使用广播之前, 复制 map 了 task 数量份
    // 在使用广播以后, 复制次数和 executor 数量一致
    val result = r.map(item => bc.value(item)).collect()

    result.foreach(println(_))
  }
}
