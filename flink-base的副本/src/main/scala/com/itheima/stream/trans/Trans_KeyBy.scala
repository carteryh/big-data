package com.itheima.stream.trans

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

object Trans_KeyBy {

  def main(args: Array[String]): Unit = {

    // 1. 获取流环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 2. 设置并行度
    env.setParallelism(1)

    // 3. 获取Socket连接
    val socketDataStream: DataStream[String] = env.socketTextStream("node01", 9999)

    // 4. 转换 以空格切分单词,单词计数1,以单词分组,进行求和
    val wordCountDataStream = socketDataStream.
      flatMap(_.split("\\s")).
      map((_, 1)).
      keyBy(_._1).
      sum(1)

    // 5. 打印数据
    wordCountDataStream.print()

    // 6. 执行任务
    env.execute()

  }

}
