package cn.itcast.xc.first

import cn.itcast.xc.common.EtlEnvironment
import org.apache.spark.sql.SparkSession

/**
 * <p>
 * 统计单词个数
 * </p>
 **/
object WordCount {

  /**
   * 初始化 spark session
   */
  val spark: SparkSession = EtlEnvironment.getSparkSession(this.getClass.getSimpleName)

  def main(args: Array[String]): Unit = {
//    var lines = List("hello java hello scala", "hello tom", "today is good day hello scala", "day by day")
//
//    //切分并压平
//    val words = lines.flatMap(_.split(" "))
//
//    // 把每个单词生成一个一个pair（key, 1）
//    val tuples = words.map((_, 1))
//
//    //以key进行分组 第一个_代表元组，第二个_1 代表key（单词）
//    val grouped = tuples.groupBy(_._1)
//
//    //统计value的长度
//    val sumed = grouped.mapValues(_.size)
//
//    //排序
//    val sorted = sumed.toList.sortBy(_._2).reverse
//    println(sorted)



    // 获取spark context
    val sc = spark.sparkContext

    // 读取文件
    val input = sc.textFile("hdfs://node01:9000/wordcount/wordcount.txt")
//    val input = sc.textFile("/Users/chenyouhong/Desktop/test/wordcount")

//    val b = List("1", "2", "4", "5")
//    val mapRDD = b.map { input => (input, 1) }
//    mapRDD.foreach(f => println(f._1 + "  " + f._2))
//
//    val flatmapRDD = b.flatMap { input => List((input, 1)) }
//    flatmapRDD.foreach(f => println(f._1 + "  " + f._2))
//

    // 进行统计
    val counts = input
      // 对单词进行分割使用“\t”
      .flatMap(line => line.split(","))
      // 对单词进行计数
      .map(word => (word, 1))
      // 根据key, 相同的计数相加
      .reduceByKey(_ + _)
      // 排序
      .sortBy(_._2, false)

    // 输出结果
    counts.collect().foreach(println(_))

    // 关闭资源
    sc.stop()
    spark.close()

  }

}
