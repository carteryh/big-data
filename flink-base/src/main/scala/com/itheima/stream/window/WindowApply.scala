package com.itheima.stream.window

import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object WindowApply {

  def main(args: Array[String]): Unit = {

    //  1. 获取流处理运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //  2. 构建socket流数据源，并指定IP地址和端口号
    val socketDataStream: DataStream[String] = env.socketTextStream("node01", 9999)
    //  3. 对接收到的数据转换成单词元组 以空格切分单词
    val wordsDataStream: DataStream[(String, Int)] = socketDataStream.flatMap {
      text => text.split(" ").map(_ -> 1)
    }
    //  4. 使用`keyBy`进行分流（分组）
    val keyedStream: KeyedStream[(String, Int), String] = wordsDataStream.keyBy(_._1)
    //  5. 使用`timeWinodw`指定窗口的长度（每3秒计算一次）
    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.timeWindow(Time.seconds(3))
    //  6. 实现一个WindowFunction匿名内部类
    val result: DataStream[(String, Int)] = windowedStream.apply(new WindowFunction[(String, Int), (String, Int), String, TimeWindow] {
      override def apply(key: String, window: TimeWindow, input: Iterable[(String, Int)], out: Collector[(String, Int)]): Unit = {
        //  - 在apply方法中实现聚合计算
        val tuple: (String, Int) = input.reduce {
          (p1, p2) => (p1._1, p1._2 + p2._2)
        }
        //- 使用Collector.collect收集数据
        out.collect(tuple)
      }
    })

    //  7. 打印输出
    result.print()
    //  8. 启动执行
    env.execute()
    //  9. 在Linux中，使用`nc -lk 端口号`监听端口，并发送单词

  }
}
