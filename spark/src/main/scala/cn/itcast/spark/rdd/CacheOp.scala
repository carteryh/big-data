package cn.itcast.spark.rdd

import org.apache.commons.lang3.StringUtils
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class CacheOp {

  /**
    * 1. 创建sc
    * 2. 读取文件
    * 3. 取出IP, 赋予初始频率
    * 4. 清洗
    * 5. 统计IP出现的次数
    * 6. 统计出现次数最少的IP
    * 7. 统计出现次数最多的IP
    */
  @Test
  def prepare(): Unit = {
    // 1. 创建 SC
    val conf = new SparkConf().setAppName("cache_prepare").setMaster("local[6]")
    val sc = new SparkContext(conf)

    // 2. 读取文件
    val source = sc.textFile("dataset/access_log_sample.txt")

    // 3. 取出IP, 赋予初始频率
    val countRDD = source.map( item => (item.split(" ")(0), 1) )

    // 4. 数据清洗
    val cleanRDD = countRDD.filter( item => StringUtils.isNotEmpty(item._1) )

    // 5. 统计IP出现的次数(聚合)
    val aggRDD = cleanRDD.reduceByKey( (curr, agg) => curr + agg )

    // 6. 统计出现次数最少的IP(得出结论)
    val lessIp = aggRDD.sortBy(item => item._2, ascending = true).first()

    // 7. 统计出现次数最多的IP(得出结论)
    val moreIp = aggRDD.sortBy(item => item._2, ascending = false).first()

    println((lessIp, moreIp))
  }

  @Test
  def cache(): Unit = {
    val conf = new SparkConf().setAppName("cache_prepare").setMaster("local[6]")
    val sc = new SparkContext(conf)

    // RDD 的处理部分
    val source = sc.textFile("dataset/access_log_sample.txt")
    val countRDD = source.map( item => (item.split(" ")(0), 1) )
    val cleanRDD = countRDD.filter( item => StringUtils.isNotEmpty(item._1) )
    var aggRDD = cleanRDD.reduceByKey( (curr, agg) => curr + agg )
    aggRDD = aggRDD.cache()

    // 两个 RDD 的 Action 操作
    // 每一个 Action 都会完整运行一下 RDD 的整个血统
    val lessIp = aggRDD.sortBy(item => item._2, ascending = true).first()
    val moreIp = aggRDD.sortBy(item => item._2, ascending = false).first()

    println((lessIp, moreIp))
  }

  @Test
  def persist(): Unit = {
    val conf = new SparkConf().setAppName("cache_prepare").setMaster("local[6]")
    val sc = new SparkContext(conf)

    // RDD 的处理部分
    val source = sc.textFile("dataset/access_log_sample.txt")
    val countRDD = source.map( item => (item.split(" ")(0), 1) )
    val cleanRDD = countRDD.filter( item => StringUtils.isNotEmpty(item._1) )
    var aggRDD = cleanRDD.reduceByKey( (curr, agg) => curr + agg )
    aggRDD = aggRDD.persist(StorageLevel.MEMORY_ONLY)
    println(aggRDD.getStorageLevel)

    // 两个 RDD 的 Action 操作
    // 每一个 Action 都会完整运行一下 RDD 的整个血统
//    val lessIp = aggRDD.sortBy(item => item._2, ascending = true).first()
//    val moreIp = aggRDD.sortBy(item => item._2, ascending = false).first()

//    println((lessIp, moreIp))
  }

  @Test
  def checkpoint(): Unit = {
    val conf = new SparkConf().setAppName("cache_prepare").setMaster("local[6]")
    val sc = new SparkContext(conf)
    // 设置保存 checkpoint 的目录, 也可以设置为 HDFS 上的目录
    sc.setCheckpointDir("checkpoint")

    // RDD 的处理部分
    val source = sc.textFile("dataset/access_log_sample.txt")
    val countRDD = source.map( item => (item.split(" ")(0), 1) )
    val cleanRDD = countRDD.filter( item => StringUtils.isNotEmpty(item._1) )
    var aggRDD = cleanRDD.reduceByKey( (curr, agg) => curr + agg )

    // checkpoint
    // aggRDD = aggRDD.cache
    // 不准确的说, Checkpoint 是一个 Action 操作, 也就是说
    // 如果调用 checkpoint, 则会重新计算一下 RDD, 然后把结果存在 HDFS 或者本地目录中
    // 所以, 应该在 Checkpoint 之前, 进行一次 Cache
    aggRDD = aggRDD.cache()

    aggRDD.checkpoint()

    // 两个 RDD 的 Action 操作
    // 每一个 Action 都会完整运行一下 RDD 的整个血统
    val lessIp = aggRDD.sortBy(item => item._2, ascending = true).first()
    val moreIp = aggRDD.sortBy(item => item._2, ascending = false).first()

    println((lessIp, moreIp))
  }
}
