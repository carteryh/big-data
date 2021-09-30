import org.apache.log4j.{Level, Logger}

object LoggerLevels {//extends Logging {

  def setStreamingLogLevels() {
    val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if (!log4jInitialized) {
      //logInfo("Setting log level to [WARN] for streaming example." +
      //  " To override add a custom log4j.properties to the classpath.")
      Logger.getRootLogger.setLevel(Level.WARN)
      // 设置日志级别
      Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
      Logger.getLogger("org.apache.kafka").setLevel(Level.WARN)
      Logger.getLogger("org.apache.spark.sql").setLevel(Level.WARN)
      Logger.getLogger("org.apache.spark.streaming").setLevel(Level.WARN)
      Logger.getLogger("com.air").setLevel(Level.WARN)
    }
  }
}
