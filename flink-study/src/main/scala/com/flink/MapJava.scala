package com.flink


import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

        object MapScala {
        def main(args: Array[String]): Unit = {
                val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

        val dataSource = env.fromElements(
        "java java spark hive",
        "hive java java spark",
        "java java hadoop"
        ).map(line => line.toUpperCase())
        .print()
        }
        }

