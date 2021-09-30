package com.itheima.stream.trans

import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

object Trans_Connect {

  def main(args: Array[String]): Unit = {

    // 1. env
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 2. 配置数据源
    val longDataStream: DataStream[Long] = env.addSource(new MyLongSource)
    val stringDataStream: DataStream[String] = env.addSource(new MyStringSource)

    // 3. connect
    val connectedStreams: ConnectedStreams[Long, String] = longDataStream.connect(stringDataStream)

    // 4. 转换
    val ds: DataStream[Any] = connectedStreams.map(
      (line1: Long) => line1,
      (line2: String) => line2
    )

    // 5. 打印数据
    ds.print().setParallelism(1)

    // 6. 执行任务
    env.execute()

  }
}


// 实现一个从1开始递增的数字,每隔一秒生成一次

class MyLongSource extends SourceFunction[Long] {

  // 程序运行状态
  var isRunning = true
  var count = 0L

  override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
    while (isRunning) {
      count += 1
      ctx.collect(count)
      TimeUnit.SECONDS.sleep(1)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}

// 实现一个从1开始递增的数字,每隔一秒生成一次

class MyStringSource extends SourceFunction[String] {

  // 程序运行状态
  var isRunning = true
  var count = 0L

  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (isRunning) {
      count += 1
      ctx.collect("str_" + count)
      TimeUnit.SECONDS.sleep(1)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}