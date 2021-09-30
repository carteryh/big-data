package cn.itcast.spark.rdd

import org.apache.commons.lang3.StringUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class AccessLogAgg {

  @Test
  def ipAgg(): Unit = {
    // 1. 创建 SparkContext
    val conf = new SparkConf().setMaster("local[6]").setAppName("ip_agg")
    val sc = new SparkContext(conf)

    // 2. 读取文件, 生成数据集
    val sourceRDD = sc.textFile("dataset/access_log_sample.txt")

    // 3. 取出IP, 赋予出现次数为1
    val ipRDD = sourceRDD.map(item => (item.split(" ")(0), 1))

    // 4. 简单清洗
    //     4.1. 去掉空的数据
    //     4.2. 去掉非法的数据
    //     4.3. 根据业务再规整一下数据
    val cleanRDD = ipRDD.filter(item => StringUtils.isNotEmpty(item._1))

    // 5. 根据IP出现的次数进行聚合
    val ipAggRDD = cleanRDD.reduceByKey( (curr, agg) => curr + agg )

    // 6. 根据IP出现的次数进行排序
    val sortedRDD = ipAggRDD.sortBy(item => item._2, ascending = false)

    // 7. 取出结果, 打印结果
    sortedRDD.take(10).foreach(item => println(item))
  }

}
