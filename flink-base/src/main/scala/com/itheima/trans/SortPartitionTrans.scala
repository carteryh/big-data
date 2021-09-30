package com.itheima.trans

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._

/**
  * 按照以下列表来创建数据集
  *
  * List("hadoop", "hadoop", "hadoop", "hive", "hive", "spark", "spark", "flink")
  *
  * 对分区进行排序后，输出到文件。
  */
object SortPartitionTrans {

  def main(args: Array[String]): Unit = {

    // 1. 创建批处理环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    // 2. 加载本地集合
    val listDataSet: DataSet[String] = env.fromCollection(List("hadoop", "hadoop", "hadoop", "hive", "hive", "spark", "spark", "flink"))

    // 3. 排序
    val sortDataSet: DataSet[String] = listDataSet.sortPartition((x: String) =>x,Order.DESCENDING)

    // 4. 写入文件
    sortDataSet.writeAsText("./data/sort_output")

    // 5. 打印输出
    sortDataSet.print()

  }

}
