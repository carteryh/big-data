package cn.itcast.utils

import com.typesafe.config.{ConfigFactory, ConfigOrigin, ConfigValue}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.SparkSession

class SparkConfigHelper(builder: SparkSession.Builder) {
  val config = ConfigFactory.load("spark")

  def loadConfig(): SparkSession.Builder = {
    // builder.option(...).option(...)
    // 二: 为 Builder 添加配置
    // 问题: 配置文件的修改. 如果直接按照 Key 读取配置文件的话, 会出现配置文件修改, 代码也要跟着修改的问题
    // 解决方案: 自动的访问配置文件, 加载其中所有内容

    // 1. 获取所有的 Config 中的键值对
    import scala.collection.JavaConverters._
    val entrySet = config.entrySet().asScala

    // 2. 遍历键值对
    for (entry <- entrySet) {
      // 3. 获取其中所有的 key, 和所有的 value
      val key = entry.getKey
      val value = entry.getValue.unwrapped().asInstanceOf[String]

      // 判断: 判断配置的来源
      val origin = entry.getValue.origin().filename()
      if (StringUtils.isNotBlank(origin)) {
        // 4. 设置给 builder
        builder.config(key, value)
      }
    }

    builder
  }
}

object SparkConfigHelper {

  // 一: 为 SparkSession 添加一个方法
  // 使用隐式转换, 将 SparkSession -> SparkConfigHelper
  implicit def sparkToHelper(builder: SparkSession.Builder): SparkConfigHelper = {
    new SparkConfigHelper(builder)
  }

  def main (args: Array[String] ): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("test")
      .master("local[6]")
      .loadConfig()
      .getOrCreate()
  }
}
