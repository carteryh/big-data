package cn.itcast.xc.common

import java.io.FileInputStream
import java.util.Properties

import cn.itcast.xc.utils.StrUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * <P>
 * build spark session
 * </p>
 *
 */
object EtlEnvironment {

  /**
   * 根据配置文件判断使用那种模式
   *
   * @param appName
   */
  def getSparkSession(appName: String): SparkSession = {
    if (isCluster) {
      initCluster(appName)
    } else {
      init(appName)
    }
  }

  /**
   * spark-submit提交集群
   *
   * @param appName
   */
  def initCluster(appName: String) = {
    val sparkConf: SparkConf = new SparkConf().setAppName(StrUtils.getClassName(appName))
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
  }

  /**
   * 开发环境调试
   *
   * @param appName
   */
  def init(appName: String) = {

    // 本地模式， 集群不进行计算
    val master = "local"
    // spark配置
    val sparkConf = new SparkConf()
      .setMaster(master)
      // 根据类名设置appName
      .setAppName(StrUtils.getClassName(appName))
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
  }

  /**
   * 获取是否使用集群
   */
  def isCluster = {
    val properties = new Properties()
    // 获取配置文件
    val inputStream = this.getClass.getClassLoader.getResourceAsStream("application.properties")
    // properties加载数据
    properties.load(inputStream)
    // 获取值, 如果没有获取到, 设置为false
    properties.getProperty("useingCluster", "false").toBoolean
  }

}
