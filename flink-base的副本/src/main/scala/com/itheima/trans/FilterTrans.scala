package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 过滤出来以下以`h`开头的单词。
  *
  * "hadoop", "hive", "spark", "flink"
  */
object FilterTrans {

  def main(args: Array[String]): Unit = {
    // 1. 创建批处理环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 2. Source 加载本地集合
    val words: DataSet[String] = env.fromCollection(List("hadoop", "hive", "spark", "flink"))

    // 3. Trans filter 过滤出来以h开头的单词
    val h_words: DataSet[String] = words.filter(_.startsWith("h"))

    // 4. sink 打印输出
    h_words.print()

  }

}
