package com.itheima.trans

import org.apache.flink.api.scala._

/**
  * 两个CSV文件 进行join
  */

case class Score(id:String,name:String,subjecid:String,score:String)
case class Subject(id:String,name:String)

object JoinTrans {

  def main(args: Array[String]): Unit = {
    // env
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // readCsvFile
    val scoreDataSet: DataSet[Score] = env.readCsvFile[Score]("E:\\bigdata_ws\\flink-base\\src\\main\\resources\\score.csv")
    val subjectDataSet: DataSet[Subject] = env.readCsvFile[Subject]("E:\\bigdata_ws\\flink-base\\src\\main\\resources\\subject.csv")

    // join
    // A.join(B).where(A的哪一个元素).equalTo(和B的哪个元素相等)
    val joinDataSet: JoinDataSet[Score, Subject] = scoreDataSet.join(subjectDataSet).where(2).equalTo(0)

    // print
    joinDataSet.print()
  }

}
