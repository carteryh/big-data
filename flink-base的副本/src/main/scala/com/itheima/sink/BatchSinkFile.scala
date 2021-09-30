package com.itheima.sink

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem

/**
  * 基于下列数据,写入到文件中
  *
  * Map(1 -> "spark", 2 -> "flink")
  */
object BatchSinkFile {

  def main(args: Array[String]): Unit = {

    // 1. env
    val env= ExecutionEnvironment.getExecutionEnvironment

    // 2. load map
    val mapDataSet: DataSet[(Int, String)] = env.fromCollection(Map(1 -> "spark", 2 -> "flink"))

    // 3. write as text setParallelism(4) 如果并行度大于1 会输出多个文件,test就是一个目录,如果并行度为1,那么test就是文件
    mapDataSet.setParallelism(4).writeAsText("./data/sink/test",FileSystem.WriteMode.OVERWRITE)

    // 4. 执行程序

    mapDataSet.print()
    env.execute("sinkFile")
  }

}
