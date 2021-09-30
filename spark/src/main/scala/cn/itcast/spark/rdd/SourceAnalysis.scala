package cn.itcast.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class SourceAnalysis {

  @Test
  def wordCount(): Unit = {
    // 1. 创建 sc 对象
    val conf = new SparkConf().setMaster("local[6]").setAppName("wordCount_source")
    val sc = new SparkContext(conf)

    // 2. 创建数据集
    // textFile 算子作用是创建一个 HadoopRDD
    val textRDD = sc.textFile("...")

    // 3. 数据处理
    //     1. 拆词
    val splitRDD = textRDD.flatMap( _.split(" ") )
    //     2. 赋予初始词频
    val tupleRDD = splitRDD.map( item => (item, 1) )
    //     3. 聚合统计词频
    val reduceRDD = tupleRDD.reduceByKey( _ + _ )
    //     4. 将结果转为字符串
    val strRDD = reduceRDD.map( item => s"${item._1}, ${item._2}" )

    // 4. 结果获取
//    strRDD.collect().foreach( println(_) )
    println(strRDD.toDebugString)

    // 5. 关闭sc, 执行
    sc.stop()
  }

  @Test
  def narrowDependency(): Unit = {
    // 需求: 求得两个 RDD 之间的笛卡尔积

    // 1. 生成 RDD
    val conf = new SparkConf().setMaster("local[6]").setAppName("cartesian")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(Seq(1, 2, 3, 4, 5, 6))
    val rdd2 = sc.parallelize(Seq("a", "b", "c"))

    // 2. 计算
    val resultRDD = rdd1.cartesian(rdd2)

    // 3. 结果获取
    resultRDD.collect().foreach(println(_))

    sc.stop()
  }
}
