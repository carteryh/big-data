package cn.itcast.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object GlobalWordCount {

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
      port = 9999,
      storageLevel = StorageLevel.MEMORY_AND_DISK_SER_2
    )

    // 3. 词频统计
    val wordsTuple = source.flatMap(_.split(" "))
      .map((_, 1))

    // 4. 全局聚合
    ssc.checkpoint("checkpoint")

    def updateFunc(newValue: Seq[Int], runningValue: Option[Int]): Option[Int] = {
      // newValue : 对应当前批次中 Key 对应的所有 Value
      // runningValue : 当前的中间结果

      val currBatchValue = newValue.sum
      val state = runningValue.getOrElse(0) + currBatchValue

      Some(state)
    }

    val result = wordsTuple.updateStateByKey[Int](updateFunc _)

    // 5. 输出
    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
