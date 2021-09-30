package com.itheima.env

import java.util.Date

import org.apache.flink.api.scala._

object BatchLocalEven {

  def main(args: Array[String]): Unit = {
    // 开始时间
    val startTime = new Date().getTime
    // env
    val localEnv: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(2)
    val collectEnv: ExecutionEnvironment = ExecutionEnvironment.createCollectionsEnvironment

    // load list
    val listDataSet: DataSet[Int] = localEnv.fromCollection(List(1,2,3,4))
    
    // print
    listDataSet.print()
    // 开始时间
    val endTime = new Date().getTime
    println(endTime-startTime)
  }

}
