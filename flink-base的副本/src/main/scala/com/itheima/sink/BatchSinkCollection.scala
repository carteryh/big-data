package com.itheima.sink

import org.apache.flink.api.scala._

/**
  * 基于下列数据,分别 进行打印输出,error输出,collect()
  *
  * ```
  * (19, "zhangsan", 178.8),
  * (17, "lisi", 168.8),
  * (18, "wangwu", 184.8),
  * (21, "zhaoliu", 164.8)
  * ```
  */
object BatchSinkCollection {

  def main(args: Array[String]): Unit = {
    // 1. env
    val env=ExecutionEnvironment.getExecutionEnvironment

    // 2. 加载集合
    val listDataSet: DataSet[(Int, String, Double)] = env.fromCollection(List(
      (19, "zhangsan", 178.8),
      (17, "lisi", 168.8),
      (18, "wangwu", 184.8),
      (21, "zhaoliu", 164.8)))

    // 3. 打印输出 错误输出 collect
    listDataSet.print()

    listDataSet.printToErr()

    val tuples: Seq[(Int, String, Double)] = listDataSet.collect()

    println(tuples)

  }
}
