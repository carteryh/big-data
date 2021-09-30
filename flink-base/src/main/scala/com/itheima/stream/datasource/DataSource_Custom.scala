package com.itheima.stream.datasource

import java.util.UUID
import java.util.concurrent.TimeUnit

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

import scala.util.Random

object DataSource_Custom {

  def main(args: Array[String]): Unit = {
    //    1. 创建订单样例类
    // 订单ID`、`用户ID`、`订单金额`、`时间戳`)
    case class Order(id:String,userId:String,money:Int,time:Long)
    //      2. 获取流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //      3. 创建自定义数据源
    val customDataStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        //      - 循环1000次

        for (i <- 0 until 1000) {
          //    - 随机构建订单信息
          // - 随机生成订单ID（UUID）
          val id = UUID.randomUUID().toString
          // - 随机生成用户ID（0-2）
          val userId = Random.nextInt(3).toString
          // - 随机生成订单金额（0-100）
          val money = Random.nextInt(101)
          // - 时间戳为当前系统时间
          val time = System.currentTimeMillis()
          //    - 上下文收集数据
          ctx.collect(Order(id, userId, money, time))
          //    - 每隔一秒执行一次循环
          TimeUnit.SECONDS.sleep(1)
        }


      }

      override def cancel(): Unit = {

      }
    })


    //    4. 打印数据
    customDataStream.print()
    //      5. 执行任务
    env.execute()
  }
}
