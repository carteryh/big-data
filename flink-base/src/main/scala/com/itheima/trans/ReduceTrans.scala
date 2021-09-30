package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 请将以下元组数据，使用`reduce`操作聚合成一个最终结果
  *
  * ("java" , 1) , ("java", 1) ,("java" , 1)
  *
  * 将上传元素数据转换为`("java",3)`
  */
object ReduceTrans {

  def main(args: Array[String]): Unit = {

    // env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // load list
    val list: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("java", 1)))

    // reduce
    // p1 ("java" , 1) p2 ("java", 1) ("java",2)
    // p1 ("java",2) p2 ("java" , 1) ("java",3)
    val reduceDataSet: DataSet[(String, Int)] = list.reduce {
      (p1, p2) => (p1._1, p1._2 + p2._2)
    }


    // print
    reduceDataSet.print()

  }

}
