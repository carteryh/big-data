package cn.itcast.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class ActionOp {
  val conf = new SparkConf().setMaster("local[6]").setAppName("transformation_op")
  val sc = new SparkContext(conf)

  /**
    * 需求, 最终生成 ("结果", price)
    *
    * 注意点:
    * 1. 函数中传入的 curr 参数, 并不是 Value, 而是一整条数据
    * 2. reduce 整体上的结果, 只有一个
    */
  @Test
  def reduce(): Unit = {
    val rdd = sc.parallelize(Seq(("手机", 10.0), ("手机", 15.0), ("电脑", 20.0)))
    val result: (String, Double) = rdd.reduce((curr, agg) => ("总价", curr._2 + agg._2) )
    println(result)
  }

  @Test
  def foreach(): Unit = {
    val rdd = sc.parallelize(Seq(1, 2, 3))
    rdd.foreach(item => println(item))
  }

  /**
    * count 和 countByKey 的结果相距很远很远, 每次调用 Action 都会生成一个 job, job 会运行获取结果
    * 所以在两个 job 中间有大量的 Log 打出, 其实就是在启动 job
    *
    * countByKey 的运算结果是 Map(Key, Value -> Key 的count)
    *
    * 数据倾斜, 如果要解决数据倾斜的问题, 是不是要先知道谁倾斜, 通过 countByKey 是不是可以查看 Key 对应的数据总数, 从而解决倾斜问题
    */
  @Test
  def count(): Unit = {
    val rdd = sc.parallelize(Seq(("a", 1), ("a", 2), ("c", 3), ("c", 4)))
    println(rdd.count())
    println(rdd.countByKey())
  }

  /**
    * take 和 takeSample 都是获取数据, 一个是直接获取, 一个是采样获取
    * first: 一般情况下, action 会从所有分区获取数据, 相对来说速度就比较慢, first 只是获取第一个元素, 所以first 只会处理第一个分区, 所以速度很快, 无序处理所有数据
    */
  @Test
  def take(): Unit = {
    val rdd = sc.parallelize(Seq(1, 2, 3, 4, 5, 6))
    rdd.take(3).foreach(item => println(item))
    println(rdd.first())
    rdd.takeSample(withReplacement = false, num = 3).foreach(item => println(item))
  }

  /**
    * 除了这四个支持以外, 还有其它很多特殊的支持
    * 这些对于数字类型的支持, 都是Action
    */
  @Test
  def numberic(): Unit = {
    val rdd = sc.parallelize(Seq(1, 2, 3, 4, 10, 20, 30, 50, 100))
    println(rdd.max()) // 100
    println(rdd.min()) // 1
    println(rdd.mean()) // ...
    println(rdd.sum())
  }

}
