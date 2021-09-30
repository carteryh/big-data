package com.itheima.stream.trans

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator

object SplitAndSelect {

  def main(args: Array[String]): Unit = {

    // 1. 创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 2. 设置并行度
    env.setParallelism(1)
    // 3. 加载本地集合(1-6)
    val listDataStream: DataStream[Int] = env.fromElements(1, 2, 3, 4, 5, 6)

    // 4. 数据分流,分为奇数和偶数
    val all: DataStream[Int] = listDataStream.filter(_ % 2 == 0)

    //    listDataStream.filter(_ % 2 == 0).lis
//    val splitStream: SplitStream[Int] = listDataStream.split {
//      (num: Int) =>
//        num % 2 match {
//          case 0 => List("even")
//          case 1 => List("odd")
//        }
//    }
//
//    // 5. 数据查询
//    val even: DataStream[Int] = splitStream.select("even")
//    val odd: DataStream[Int] = splitStream.select("odd")
//    val all: DataStream[Int] = splitStream.select("odd","even")
//
    // 6. 打印数据
    all.print()

    // 7. 执行任务
    env.execute()

  }

}
