package cn.itcast.spark.rdd

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class TransformationOp {
  val conf = new SparkConf().setMaster("local[6]").setAppName("transformation_op")
  val sc = new SparkContext(conf)

  /**
    * mapPartitions 和 map 算子是一样的, 只不过 map 是针对每一条数据进行转换, mapPartitions 针对一整个分区的数据进行转换
    * 所以:
    * 1. map 的 func 参数是单条数据, mapPartitions 的 func 参数是一个集合(一个分区整个所有的数据)
    * 2. map 的 func 返回值也是单条数据, mapPartitions 的 func 返回值是一个集合
    */
  @Test
  def mapPartitions(): Unit = {
    // 1. 数据生成
    // 2. 算子使用
    // 3. 获取结果
    sc.parallelize(Seq(1, 2, 3, 4, 5, 6), 2)
      .mapPartitions(iter => {
        iter.foreach(item => println(item))
        iter
      })
      .collect()
  }

  @Test
  def mapPartitions2(): Unit = {
    // 1. 数据生成
    // 2. 算子使用
    // 3. 获取结果
    sc.parallelize(Seq(1, 2, 3, 4, 5, 6), 2)
      .mapPartitions(iter => {
        // 遍历 iter 其中每一条数据进行转换, 转换完成以后, 返回这个 iter
        // iter 是 scala 中的集合类型
        iter.map(item => item * 10)
      })
      .collect()
      .foreach(item => println(item))
  }

  /**
    * mapPartitionsWithIndex 和 mapPartitions 的区别是 func 中多了一个参数, 是分区号
    */
  @Test
  def mapPartitionsWithIndex(): Unit = {
    sc.parallelize(Seq(1, 2, 3, 4, 5, 6), 2)
      .mapPartitionsWithIndex( (index, iter) => {
        println("index: " + index)
        iter.foreach(item => println(item))
        iter
      } )
      .collect()
  }

  /**
    * filter 可以过滤掉数据集中一部分元素
    * filter 中接收的函数, 参数是 每一个元素, 如果这个函数返回 ture, 当前元素就会被加入新数据集, 如果返回 flase, 当前元素会被过滤掉
    */
  @Test
  def filter(): Unit = {
    // 1. 定义集合
    // 2. 过滤数据
    // 3. 收集结果
    sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
      .filter( item => item % 2 == 0 )
      .collect()
      .foreach(item => println(item))
  }

  /**
    * 作用: 把大数据集变小, 尽可能的减少数据集规律的损失
    * withReplacement: 指定为True的情况下, 可能重复, 如果是Flase, 无重复
    */
  @Test
  def sample(): Unit = {
    // 1. 定义集合
    // 2. 过滤数据
    // 3. 收集结果
    val rdd1 = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    val rdd2 = rdd1.sample(true, 0.6)
    val result = rdd2.collect()
    result.foreach(item => println(item))
  }

  /**
    * mapValue 也是 map, 只不过map作用于整条数据, mapValue 作用于 Value
    */
  @Test
  def mapValues(): Unit = {
    sc.parallelize(Seq(("a", 1), ("b", 2), ("c", 3)))
      .mapValues( item => item * 10 )
      .collect()
      .foreach(println(_))
  }

  /**
    * 交集
    */
  @Test
  def intersection(): Unit = {
    val rdd1 = sc.parallelize(Seq(1, 2, 3, 4, 5))
    val rdd2 = sc.parallelize(Seq(3, 4, 5, 6, 7))

    rdd1.intersection(rdd2)
      .collect()
      .foreach(println(_))
  }

  /**
    * 并集
    */
  @Test
  def union(): Unit = {
    val rdd1 = sc.parallelize(Seq(1, 2, 3, 4, 5))
    val rdd2 = sc.parallelize(Seq(3, 4, 5, 6, 7))

    rdd1.union(rdd2)
      .collect()
      .foreach(println(_))
  }

  /**
    * 差集
    */
  @Test
  def subtract(): Unit = {
    val rdd1 = sc.parallelize(Seq(1, 2, 3, 4, 5))
    val rdd2 = sc.parallelize(Seq(3, 4, 5, 6, 7))

    rdd1.subtract(rdd2)
      .collect()
      .foreach(println(_))
  }

  /**
    * groupByKey 运算结果的格式: (K, (value1, value2))
    * reduceByKey 能不能在 Map 端做 Combiner: 1. 能不能减少 IO
    * groupByKey 在 map 端做 Combiner 有没有意义? 没有的...
    */
  @Test
  def groupByKey(): Unit = {
    val rdd: RDD[(String, Int)] = sc.parallelize(Seq(("a", 1), ("a", 1), ("b", 1)))
    val rdd1: RDD[(String, Iterable[Int])] = rdd.groupByKey()
    val rdd2: Array[(String, Iterable[Int])] = rdd1.collect()
    rdd2.foreach(println(_))
  }

  /**
    * CombineByKey 这个算子中接收三个参数:
    * 转换数据的函数(初始函数, 作用于第一条数据, 用于开启整个计算), 在分区上进行聚合, 把所有分区的聚合结果聚合为最终结果
    */
  @Test
  def combineByKey(): Unit = {
    // 1. 准备集合
    val rdd: RDD[(String, Double)] = sc.parallelize(Seq(
      ("zhangsan", 99.0),
      ("zhangsan", 96.0),
      ("lisi", 97.0),
      ("lisi", 98.0),
      ("zhangsan", 97.0))
    )

    // 2. 算子操作
    //   2.1. createCombiner 转换数据
    //   2.2. mergeValue 分区上的聚合
    //   2.3. mergeCombiners 把所有分区上的结果再次聚合, 生成最终结果
    val combineResult = rdd.combineByKey(
      createCombiner = (curr: Double) => (curr, 1),
      mergeValue = (curr: (Double, Int), nextValue: Double) => (curr._1 + nextValue, curr._2 + 1),
      mergeCombiners = (curr: (Double, Int), agg: (Double, Int)) => (curr._1 + agg._1, curr._2 + agg._2)
    )
    // ("zhangsan", (99 + 96 + 97, 3))
    val resultRDD = combineResult.map( item => (item._1, item._2._1 / item._2._2) )

    // 3. 获取结果, 打印结果
    resultRDD.collect().foreach(println(_))
  }

  /**
    * foldByKey 和 Spark 中的 reduceByKey 的区别是可以指定初始值
    * foldByKey 和 Scala 中的 foldLeft 或者 foldRight 区别是, 这个初始值作用于每一个数据
    */
  @Test
  def foldByKey(): Unit = {
    sc.parallelize(Seq(("a", 1), ("a", 1), ("b", 1)))
      .foldByKey(10)((curr, agg) => curr + agg)
      .collect()
      .foreach(println(_))
  }

  /**
    * aggregateByKey(zeroValue)(seqOp, combOp)
    * zeroValue : 指定初始值
    * seqOp : 作用于每一个元素, 根据初始值, 进行计算
    * combOp : 将 seqOp 处理过的结果进行聚合
    *
    * aggregateByKey 特别适合针对每个数据要先处理, 后聚合
    */
  @Test
  def aggregateByKey(): Unit = {
    val rdd = sc.parallelize(Seq(("手机", 10.0), ("手机", 15.0), ("电脑", 20.0)))
    rdd.aggregateByKey(0.8)((zeroValue, item) => item * zeroValue, (curr, agg) => curr + agg)
      .collect()
      .foreach(println(_))
  }

  @Test
  def join(): Unit = {
    val rdd1 = sc.parallelize(Seq(("a", 1), ("a", 2), ("b", 1)))
    val rdd2 = sc.parallelize(Seq(("a", 10), ("a", 11), ("a", 12)))
    rdd1.join(rdd2)
      .collect()
      .foreach(println(_))
  }

  /**
    * sortBy 可以作用于任何类型数据的RDD, sortByKey 只有 KV 类型数据的RDD中才有
    * sortBy 可以按照任何部分来排序, sortByKey 只能按照 Key 来排序
    * sortByKey 写法简单, 不用编写函数了
    */
  @Test
  def sort(): Unit = {
    val rdd1 = sc.parallelize(Seq(2, 4, 1, 5, 1, 8))
    val rdd2 = sc.parallelize(Seq(("a", 1), ("b", 3), ("c", 2)))

    rdd1.sortBy(item => item)

    rdd2.sortBy(item => item._2)
    rdd2.sortByKey()
    rdd2.map(item => (item._2, item._1)).sortByKey().map(item => (item._2, item._1)).collect().foreach(println(_))
  }

  /**
    * repartition 进行重分区的时候, 默认是 Shuffle 的
    * coalesce 进行重分区的时候, 默认是不 Shuffle 的, coalesce 默认不能增大分区数
    */
  @Test
  def partitioning(): Unit = {
    val rdd = sc.parallelize(Seq(1, 2, 3, 4, 5), 2)

    // repartition
//    println(rdd.repartition(5).partitions.size)
//    println(rdd.repartition(1).partitions.size)

    // coalesce
    println(rdd.coalesce(5, shuffle = true).partitions.size)
  }
}
