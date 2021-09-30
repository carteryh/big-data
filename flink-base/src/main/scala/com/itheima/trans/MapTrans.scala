package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 使用map操作，将以下数据
  *
  * "1,张三", "2,李四", "3,王五", "4,赵六"
  *
  * 转换为一个scala的样例类。
  *
  * map是一对一
  */


object MapTrans {
  case class Person(id:String,name:String)
  def main(args: Array[String]): Unit = {

    // env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 加载本地集合
    val listDataSet: DataSet[String] = env.fromCollection(List("1,张三", "2,李四", "3,王五", "4,赵六"))

    // map
    val personDataSet: DataSet[Person] = listDataSet.map {
      text: String =>
        val arr: Array[String] = text.split(",")
        Person(arr(0), arr(1))
    }

    // 打印
    personDataSet.print()
  }
}
