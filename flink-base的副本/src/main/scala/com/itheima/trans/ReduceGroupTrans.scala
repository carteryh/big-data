package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 请将以下元组数据，下按照单词使用`groupBy`进行分组，再使用`reduceGroup`操作进行单词计数
  *
  * ("java" , 1) , ("java", 1) ,("scala" , 1)
  */
object ReduceGroupTrans {

  def main(args: Array[String]): Unit = {

    // env
    val env = ExecutionEnvironment.getExecutionEnvironment
    // load list
    val list: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))

    // groupby

    val groupDataSet: GroupedDataSet[(String, Int)] = list.groupBy(_._1)

    // reduceGroup
    val reduceDataSet: DataSet[(String, Int)] = groupDataSet.reduceGroup {
      iter =>
        iter.reduce {
          (p1, p2) => (p1._1, p1._2 + p2._2)
        }
    }

    // print
    reduceDataSet.print()
  }

}
