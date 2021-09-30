package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 请将以下元组数据，使用`distinct`操作去除重复的单词
  *
  * ("java" , 1) , ("java", 2) ,("scala" , 1)
  *
  * 去重得到
  * ("java", 1), ("scala", 1)
  */
object DistinctTrans {

  def main(args: Array[String]): Unit = {

    // env
    val env = ExecutionEnvironment.getExecutionEnvironment

    // load list
    val list: DataSet[(String, Int)] = env.fromCollection(List(("java" , 1) ,("java" , 1), ("java", 2) ,("scala" , 1)))

    // distinct 0: 根据元组的第一个元素进行去重
    val distinctDataSet: DataSet[(String, Int)] = list.distinct(0,1)
    // print
    distinctDataSet.print()

  }

}
