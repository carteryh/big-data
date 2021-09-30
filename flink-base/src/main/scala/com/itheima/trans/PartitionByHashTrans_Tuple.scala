package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 基于以下列表数据来创建数据源，并按照hashPartition进行分区，然后输出到文件。
  *
  * List(1,1,1,1,1,1,1,2,2,2,2,2)
  */
object PartitionByHashTrans_Tuple {

  def main(args: Array[String]): Unit = {

    // 1. 创建批处理环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    // 2. 设置并行度为2
    env.setParallelism(2)

    // 3. 加载本地集合
    val listDataSet = env.fromCollection(
      List(("flink",1),("spark",2),("flume",3)))

    // 4. 进行分区
    val hashDataSet: DataSet[(String, Int)] = listDataSet.partitionByHash(0)

    // 5. 写入文件
    hashDataSet.writeAsText("./data/patitions")

    // 6. 打印输出
    hashDataSet.print()

  }
}
