package cn.itcast.xc.analysis.common

import java.io.FileInputStream
import java.util.Properties

import cn.itcast.xc.utils.StrUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * <P>
 * build SparkSession
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
      initCluster(conf(appName))
    } else {
      init(conf(appName))
    }
  }

  /**
   * spark-submit提交集群
   *
   * @param sparkConf
   */
  def initCluster(sparkConf: SparkConf) = {
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
  }

  /**
   * 开发环境调试
   *
   * @param sparkConf
   */
  def init(sparkConf: SparkConf) = {

    // 本地模式， 集群不进行计算
    val master = "local"
    sparkConf.setMaster(master)
    // 根据类名设置appName
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
  }

  def conf(appName: String): SparkConf = {
    new SparkConf().setAppName(StrUtils.getClassName(appName))
  }

  def esConf(sparkConf: SparkConf, esTableName: String): SparkConf = {
    sparkConf.set("es.nodes", "xc-online-es")
    sparkConf.set("es.port", "9200")
    sparkConf.set("es.index.auto.create", "true")
    sparkConf.set("es.resource", esTableName)
    sparkConf.set("es.nodes.wan.only", "true")
    sparkConf.set("es.name", "docker-cluster")
    sparkConf
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
