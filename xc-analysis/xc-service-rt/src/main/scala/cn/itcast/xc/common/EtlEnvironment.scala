package cn.itcast.xc.common

import java.io.FileInputStream
import java.util.Properties

import cn.itcast.xc.utils.StrUtils
import org.apache.spark.SparkConf

/**
 * <P>
 * build spark config
 * </p>
 *
 */
object EtlEnvironment {

  /**
   * 根据配置文件判断使用那种模式
   *
   * @param appName
   */
  def getSparkConf(appName: String): SparkConf = {
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
    new SparkConf()
      .setAppName(StrUtils.getClassName(appName))
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
    new SparkConf()
      .setMaster(master)
      // 根据类名设置appName
      .setAppName(StrUtils.getClassName(appName))
  }

  /**
   * 获取是否使用集群
   */
  def isCluster = {
    val properties = new Properties()
    // 获取配置文件
    val path = Thread.currentThread().getContextClassLoader.getResource("application.properties").getPath
    // properties加载数据
    properties.load(new FileInputStream(path))
    // 获取值, 如果没有获取到, 设置为false
    properties.getProperty("useingCluster", "false").toBoolean
  }

}
