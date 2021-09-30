package com.itheima.batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration

object BatchFromFolder {



  def main(args: Array[String]): Unit = {

    // env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 读取目录
    def params: Configuration = new Configuration()
    params.setBoolean("recursive.file.enumeration",true)
    val folderDataSet: DataSet[String] = env.readTextFile("E:\\bigdata_ws\\flink-base\\src\\main\\resources\\").withParameters(params)

    folderDataSet.print()

  }
}
