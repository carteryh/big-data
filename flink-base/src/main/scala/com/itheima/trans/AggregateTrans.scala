package com.itheima.trans

import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala._

/**
  * 请将以下元组数据，使用`aggregate`操作进行单词统计
  *
  * ("java" , 1) , ("java", 1) ,("scala" , 1)
  */
object AggregateTrans {

  def main(args: Array[String]): Unit = {

    // env
    val env = ExecutionEnvironment.getExecutionEnvironment
    // load list
    val list: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))

    // groupby
    val groupDataSet: GroupedDataSet[(String, Int)] = list.groupBy(0)

    // aggregate
    val aggregateDataSet: AggregateDataSet[(String, Int)] = groupDataSet.aggregate(Aggregations.SUM,1)

    // print
    aggregateDataSet.print()

  }

}
