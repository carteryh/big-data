package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 使用mapPartition操作，将以下数据
  *
  * "1,张三", "2,李四", "3,王五", "4,赵六"
  *
  * 转换为一个scala的样例类。
  */

/**
  * 开发步骤:
  * 1. 创建样例类 Person(id,name)
  * 2. 创建ENV
  * 3. 加载本地集合
  * 4. mapPartition
  * 5. 打印
  */
case class Person(id:String,name: String)

object MapPartitionTrans {

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    val listDataSet: DataSet[String] = env.fromCollection(List("1,张三", "2,李四", "3,王五", "4,赵六"))

    // mapPartition
    val mapDataSet: DataSet[Person] = listDataSet.mapPartition {

      // 开启redis或者mysql的链接
      iterable => {

        iterable.map {
          text =>
            val arrs: Array[String] = text.split(",")
            Person(arrs(0), arrs(1))
        }
      }
      // 关闭redis或者mysql的链接
    }
    mapDataSet.print()
  }
}
