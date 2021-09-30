package cn.itcast.spark.rdd

import org.apache.commons.lang3.StringUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class StagePractice {

  @Test
  def pmProcess(): Unit = {
    // 1. 创建sc对象
    val conf = new SparkConf().setMaster("local[6]").setAppName("stage_practice")
    val sc = new SparkContext(conf)

    // 2. 读取文件
    val source = sc.textFile("dataset/BeijingPM20100101_20151231_noheader.csv")

    // 3. 通过算子处理数据
    //    1. 抽取数据, 年, 月, PM, 返回结果: ((年, 月), PM)
    //    2. 清洗, 过滤掉空的字符串, 过滤掉 NA
    //    3. 聚合
    //    4. 排序
    val resultRDD = source.map( item => ((item.split(",")(1), item.split(",")(2)), item.split(",")(6)) )
      .filter( item => StringUtils.isNotEmpty(item._2) && ! item._2.equalsIgnoreCase("NA") )
      .map( item => (item._1, item._2.toInt) )
      .reduceByKey( (curr, agg) => curr + agg )
      .sortBy( item => item._2, ascending = false)

    // 4. 获取结果
    resultRDD.take(10).foreach(item => println(item))

    // 5. 运行测试
    sc.stop()
  }
}
