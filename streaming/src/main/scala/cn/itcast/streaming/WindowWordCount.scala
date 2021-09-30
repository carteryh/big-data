package cn.itcast.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowWordCount {

  def main(args: Array[String]): Unit = {
    // 1. 创建 Context
    val conf = new SparkConf()
      .setAppName("updateStateBykey")
      .setMaster("local[6]")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.sparkContext.setLogLevel("WARN")

    // 2. 读取数据生成 DStream
    val source = ssc.socketTextStream(
      hostname = "192.168.169.101",
      port = 9998,
      storageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
    )

    // 3. 词频转换
    val wordsTuple = source.flatMap(_.split(" ")).map((_, 1))

    // 4. 按照窗口来统计, 需求: 统计 30 秒内的词频, 10 秒刷新一次
//    val wordsWindow = wordsTuple.window(Seconds(30), Seconds(30))
//    val wordCounts = wordsWindow.reduceByKey((newValue, runningValue) => newValue + runningValue)
    val wordCounts = wordsTuple.reduceByKeyAndWindow(
      reduceFunc = (newValue, runningValue) => newValue + runningValue,
      windowDuration = Seconds(30),
      slideDuration = Seconds(10)
    )

    // 5. 输出
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
