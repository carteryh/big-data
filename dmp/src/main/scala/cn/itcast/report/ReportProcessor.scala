package cn.itcast.report

import org.apache.kudu.Schema
import org.apache.spark.sql.DataFrame

/**
  * 所有的 Report 生成, 都继承自这个类
  * 这是一个公共的父类, 是一个基类
  */
trait ReportProcessor {

  /**
    * 对外提供源表的表名
    */
  def sourceTableName(): String

  /**
    * 对外提供数据处理的过程
    * 拿到一个 DataFrame, 经过处理以后, 返回一个新的 DataFrame
    */
  def process(dataFrame: DataFrame): DataFrame

  /**
    * 提供一个目标表名出去
    */
  def targetTableName(): String

  /**
    * 提供目标表的 Schema 信息
    */
  def targetTableSchema(): Schema

  /**
    * 提供目标表的分区键
    */
  def targetTableKeys(): List[String]
}
