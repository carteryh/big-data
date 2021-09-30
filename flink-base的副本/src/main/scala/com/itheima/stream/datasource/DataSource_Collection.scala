package com.itheima.stream.datasource

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

object DataSource_Collection {

  def main(args: Array[String]): Unit = {
    // 1. 创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 设置并行度,默认和CPU的核数相同
    env.setParallelism(1)

    // 2. 指定数据源,加载本地集合
    val listDataStream: DataStream[Int] = env.fromElements(1,2,3,4,5,6,7,8,9,10)

    // 3. 打印数据
    listDataStream.print()

    // 4. 执行任务,在批处理时,print方法是可以触发任务的,但是在流环境下,必须手动执行任务
    env.execute()


  }

}
