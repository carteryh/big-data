package com.itheima.env

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._

object BatchRemoteEnv {

  def main(args: Array[String]): Unit = {

    // 1. 创建远程执行环境
    val env = ExecutionEnvironment.createRemoteEnvironment("node01",8081,"E:\\bigdata_ws\\flink-base\\target\\flink-base-1.0-SNAPSHOT.jar")

    // 2. 读取hdfs中csv文件,转换为元组

    val csvFile: DataSet[(Long, String, Long, Double)] = env.readCsvFile[(Long,String,Long,Double)]("hdfs://node01:8020/flink-datas/score.csv")

    // 3. 根据元组的姓名分组,以成绩降序,取第一个值
    val result: DataSet[(Long, String, Long, Double)] = csvFile.groupBy(1).sortGroup(3,Order.DESCENDING).first(1)

    // 4. 打印
    result.print()

  }

}
