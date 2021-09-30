package com.itheima.stream.watermark

import java.util.UUID
import java.util.concurrent.TimeUnit

import org.apache.commons.lang.time.FastDateFormat
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.Random

object WatermarkDemo {

  //  3. 创建一个订单样例类`Order`，包含四个字段（订单ID、用户ID、订单金额、时间戳）
  case class Order(orderId: String, userId: Int, money: Long, timestamp: Long)

  def main(args: Array[String]): Unit = {
    //  1. 创建流处理运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //  2. 设置处理时间为`EventTime`
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //  4. 创建一个自定义数据源
    val orderDataStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      var isRunning = true

      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        while (isRunning) {
          //   - 随机生成订单ID（UUID）
          // - 随机生成用户ID（0-2）
          // - 随机生成订单金额（0-100）
          // - 时间戳为当前系统时间
          // - 每隔1秒生成一个订单
          val order = Order(UUID.randomUUID().toString, Random.nextInt(3), Random.nextInt(101), new java.util.Date().getTime)
          ctx.collect(order)
          TimeUnit.SECONDS.sleep(1)
        }
      }

      override def cancel(): Unit = isRunning = false
    })
    //  5. 添加水印

    val watermarkDataStream: DataStream[Order] = orderDataStream.assignTimestampsAndWatermarks(
      new AssignerWithPeriodicWatermarks[Order] {

      // 允许延迟2秒
      val delayTime = 2000L
      // 当前时间戳
      var currentTimestamp = 0L

      // 获取水印时间
      override def getCurrentWatermark: Watermark = {
        val watermark = new Watermark(currentTimestamp - delayTime)
        // - 在获取水印方法中，打印水印时间、事件时间和当前系统时间
        val dateFormat: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
        println(s"水印时间:${dateFormat.format(watermark.getTimestamp)}," +
          s"事件时间:${dateFormat.format(currentTimestamp)}," +
          s"系统时间:${dateFormat.format(System.currentTimeMillis())}")

        watermark
      }

      // 抽取当前时间戳
      override def extractTimestamp(element: Order, previousElementTimestamp: Long): Long = {
        // 比对两个元素的时间,求最大值
        currentTimestamp = Math.max(element.timestamp, previousElementTimestamp)
        currentTimestamp
      }
    })



    //  6. 按照用户进行分流
    //  7. 设置5秒的时间窗口
    //  8. 进行聚合计算
    val resultDataStream: DataStream[Order] = watermarkDataStream.
      keyBy(_.userId).
      timeWindow(Time.seconds(5))
      .reduce((p1, p2) => Order(p1.orderId, p1.userId, p1.money + p2.money, 0L))

    //  9. 打印结果数据
    resultDataStream.print()
    
    //  10. 启动执行流处理
    env.execute()
  }

}
