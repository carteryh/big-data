package cn.itcast.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2
import org.junit.Test

import scala.collection.mutable

class Accumulator {

  /**
    * RDD -> (1, 2, 3, 4, 5) -> Set(1,2,3,4,5)
    */
  @Test
  def acc(): Unit = {
    val config = new SparkConf().setAppName("acc").setMaster("local[6]")
    val sc = new SparkContext(config)

    val numAcc = new NumAccumulator()
    // 注册给 Spark
    sc.register(numAcc, "num")

    sc.parallelize(Seq("1", "2", "3"))
      .foreach(item => numAcc.add(item))

    println(numAcc.value)

    sc.stop()
  }
}

class NumAccumulator extends AccumulatorV2[String, Set[String]] {
  private val nums: mutable.Set[String] = mutable.Set()

  /**
    * 告诉 Spark 框架, 这个累加器对象是否是空的
    */
  override def isZero: Boolean =  {
    nums.isEmpty
  }

  /**
    * 提供给 Spark 框架一个拷贝的累加器
    * @return
    */
  override def copy(): AccumulatorV2[String, Set[String]] = {
    val newAccumulator = new NumAccumulator()
    nums.synchronized {
      newAccumulator.nums ++= this.nums
    }
    newAccumulator
  }

  /**
    * 帮助 Spark 框架, 清理累加器的内容
    */
  override def reset(): Unit = {
    nums.clear()
  }

  /**
    * 外部传入要累加的内容, 在这个方法中进行累加
    */
  override def add(v: String): Unit = {
    nums += v
  }

  /**
    * 累加器在进行累加的时候, 可能每个分布式节点都有一个实例
    * 在最后 Driver 进行一次合并, 把所有的实例的内容合并起来, 会调用这个 merge 方法进行合并
    */
  override def merge(other: AccumulatorV2[String, Set[String]]): Unit = {
    nums ++= other.value
  }

  /**
    * 提供给外部累加结果
    * 为什么一定要给不可变的, 因为外部有可能再进行修改, 如果是可变的集合, 其外部的修改会影响内部的值
    */
  override def value: Set[String] = {
    nums.toSet
  }
}
