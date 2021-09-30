package cn.itcast.xc.first

import cn.itcast.xc.common.EtlEnvironment
import cn.itcast.xc.first.WordCount.spark
import org.apache.spark.sql.SparkSession

/**
 * 项目名称：xc-analysis
 * 类 名 称：test
 * 类 描 述：TODO
 * 创建时间：2021/4/30 下午11:02
 * 创 建 人：chenyouhong
 */
class test {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = EtlEnvironment.getSparkSession(this.getClass.getSimpleName)

    // 获取spark context
    val sc = spark.sparkContext

    val arr = sc.parallelize(Array(("A", 1), ("B", 2), ("C", 3)))
    arr.flatmap(x => (x._1 + x._2)).foreach(println)

  }

}
