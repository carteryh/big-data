package com.itheima.broad

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem

object CounterDemo {

  def main(args: Array[String]): Unit = {

    // 1. 创建批处理环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    // 2. 加载本地集合
    val words: DataSet[String] = env.fromElements("a","b","c","d")

    // 3. map转换
    val resultDataSet: DataSet[String] = words.map(new RichMapFunction[String, String] {

      // 1. 创建累加器
      val intCounter = new IntCounter

      override def open(parameters: Configuration): Unit = {
        // 2. 注册累加器
        // 参数1: 累加器的名称  参数2:累加器对象
        getRuntimeContext.addAccumulator("wordsCount", intCounter)
      }

      override def map(value: String): String = {
        // 3.数据累加
        intCounter.add(1)
        value
      }
    })

    // 4. 输出到文件
//    resultDataSet.print()
    resultDataSet.writeAsText("./data/couter",FileSystem.WriteMode.OVERWRITE)

    // 5. 执行任务
    val jobExecutionResult: JobExecutionResult = env.execute("counterDemo")

    // 6. 获取累加器的数值
    val sumResult: Int = jobExecutionResult.getAccumulatorResult[Int]("wordsCount")

    // 7. 打印结果
    println(sumResult)

  }
}
