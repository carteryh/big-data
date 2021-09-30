package com.itheima.batch

import org.apache.flink.api.scala._

object BatchFromCsvFile {

  def main(args: Array[String]): Unit = {

    // env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 加载CSV文件, csv是以,分割的文本内容
    case class Subject(id:Long,name:String)
    val caseClassDataSet: DataSet[Subject] = env.readCsvFile[Subject]("E:\\bigdata_ws\\flink-base\\src\\main\\resources\\subject.csv")

    caseClassDataSet.print()
  }

}
