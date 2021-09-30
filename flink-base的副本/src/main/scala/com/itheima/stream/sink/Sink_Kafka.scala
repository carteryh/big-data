package com.itheima.stream.sink

import com.itheima.stream.datasource.DataSource_MySql.MySql_Source
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010

object Sink_Kafka {

  def main(args: Array[String]): Unit = {

    //  1. 创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //  2. 设置并行度
    env.setParallelism(1)
    //  3. 添加自定义MySql数据源
    val mySqlDataStream: DataStream[(Int, String, String, String)] = env.addSource(new MySql_Source())
    //  4. 转换元组数据为字符串
    val mapDataStream: DataStream[String] = mySqlDataStream.map {
      line => line._1 + "," + line._2 + "" + line._3 + "" + line._4
    }
    //  5. 构建`FlinkKafkaProducer010`
    val kafkaCluster = "node01:9092,node02:9092,node03:9092"
    val flinkKafkaProducer010= new FlinkKafkaProducer010[String](kafkaCluster,
      "test2", new SimpleStringSchema())
    //  6. 添加sink
    mapDataStream.addSink(flinkKafkaProducer010)
    //  7. 执行任务
    env.execute()
  }

}
