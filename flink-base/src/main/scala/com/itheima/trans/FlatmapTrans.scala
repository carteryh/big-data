package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 张三,中国,江西省,南昌市
  *
  * 张三,中国
  * 张三,中国江西省
  * 张三,中国江西省南昌市
  */
object FlatmapTrans {

  def main(args: Array[String]): Unit = {
    // 批处理环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 去加载本地数据
    val listDataSet: DataSet[String] = env.fromCollection(List(
      "张三,中国,江西省,南昌市",
      "李四,中国,河北省,石家庄市",
      "Tom,America,NewYork,Manhattan"
    ))

    // 使用flatmap进行转换

    val dataSet: DataSet[Product] = listDataSet.flatMap {
      text => {
        var array = text.split(",")
        List(
          (array(0), array(1)),
          (array(0), array(1), array(2)),
          (array(0), array(1), array(2), array(2)))
      }
    }
    dataSet.print()
  }
}
