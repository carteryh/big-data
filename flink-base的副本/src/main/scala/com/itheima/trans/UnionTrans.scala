package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 将以下数据进行取并集操作
  *
  * 数据集1
  *
  * "hadoop", "hive", "flume"
  *
  * 数据集2
  *
  * "hadoop", "hive", "spark"
  */
object UnionTrans {

  def main(args: Array[String]): Unit = {
    // env
    val env = ExecutionEnvironment.getExecutionEnvironment

    // load list
    val dataset1: DataSet[String] = env.fromCollection(List("hadoop", "hive", "flume"))
    val dataset2: DataSet[String] = env.fromCollection(List("hadoop", "hive", "spark"))

    // union
    val newDataSet: DataSet[String] = dataset1.union(dataset2).distinct()

    // print
    newDataSet.print()
  }

}
