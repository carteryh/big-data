package com.itheima.batch

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.java.DataSet
import org.apache.flink.api.java.ExecutionEnvironment


/**
 * @author ：caizhengjie
 * @description：TODO
 * @date ：2021/3/8 9:58 上午
 */
object MapJava {
  @throws[Exception]
  def main(args: Array[String]): Unit = { // 准备环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    val dateSource = env.fromElements("java java spark hive", "hive java java spark", "java java hadoop")
    /**
     * map
     */
    val mapSource = dateSource.map(new MapFunction[String, String]() {
      @throws[Exception]
      override def map(line: String): String = line.toUpperCase
    })
    mapSource.print()

    /**
     * JAVA JAVA SPARK HIVE
     * HIVE JAVA JAVA SPARK
     * JAVA JAVA HADOOP
     */
  }
}
