package cn.itcast.xc.utils

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.kafka.common.TopicPartition
import redis.clients.jedis.JedisPool


class RedisUtils extends Serializable {
  @transient private var pool: JedisPool = null

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int): Unit = {
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle, true, false, 10000)
  }

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean,
               testOnReturn: Boolean, maxWaitMillis: Long): Unit = {
    if (pool == null) {
      val poolConfig: GenericObjectPoolConfig = new GenericObjectPoolConfig
      poolConfig.setMaxTotal(maxTotal)
      poolConfig.setMaxIdle(maxIdle)
      poolConfig.setMinIdle(minIdle)
      poolConfig.setTestOnBorrow(testOnBorrow)
      poolConfig.setTestOnReturn(testOnReturn)
      poolConfig.setMaxWaitMillis(maxWaitMillis)
      // 无密码
      pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)
      // 有密码
      // pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout, "qwer1234")

      val hook = new Thread {
        override def run = pool.destroy()
      }
      sys.addShutdownHook(hook.run)
    }
  }

  def getPool: JedisPool = {
    assert(pool != null)
    pool
  }

  /** *
   * 初始化redis配置
   */
  def initRedisPool() = {
    val maxTotal = 20
    val maxIdle = 10
    val minIdle = 1
    val redisHost = "xc-online-redis"
    val redisPort = 6379
    val redisTimeout = 30000
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle)
  }

  /** *
   * 存储offset
   *
   * @param key
   * @param keyinfo
   * @param value
   */
  def storeOffsetRedis(key: String, keyinfo: String, value: Long): Unit = {
    val jedis = getPool.getResource
    val ppl = jedis.pipelined()
    ppl.multi() //开启事务
    //    ppl.hincrBy(key, keyinfo, value)
    ppl.hset(key, keyinfo, value.toString)
    ppl.exec() //提交事务
    ppl.sync //关闭pipeline
    jedis.close()
  }

  /** *
   * 存储结果集
   *
   * @param key
   * @param keyinfo
   * @param value
   */
  def storeResultRedis(key: String, keyinfo: String, value: String): Unit = {
    val jedis = getPool.getResource
    val ppl = jedis.pipelined()
    ppl.multi() //开启事务
    ppl.hset(key, keyinfo, value)
    ppl.exec() //提交事务
    ppl.sync //关闭pipeline
    jedis.close()
  }

  /**
   * 异常重启的情况, 获取redis中已有的数据
   */
  def getResultRedis(key: String, keyinfo: String): String = {
    val jedis = getPool.getResource
    val ppl = jedis.pipelined()
    val value = ppl.hget(key, keyinfo)
    jedis.close()
    value.get()
  }

  /** *
   * 从redis中获取上次消费的offset
   *
   * @param topicName
   * @param partitions
   * @return
   */
  def getLastCommittedOffsets(rt_visit_online_offset_key: String, topicName: String, partitions: Int): Map[TopicPartition, Long] = {

    // 从Redis获取上一次存的Offset
    val jedis = getPool.getResource
    val fromOffsets = collection.mutable.HashMap.empty[TopicPartition, Long]
    for (partition <- 0 to partitions - 1) {
      val topic_partition_key = topicName + "_" + partition
      val lastSavedOffset = jedis.hget(rt_visit_online_offset_key, topic_partition_key)
      val lastOffset = if (lastSavedOffset == null) 0L else lastSavedOffset.toLong
      fromOffsets += (new TopicPartition(topicName, partition) -> lastOffset)
    }
    jedis.close()
    println("offset from Redis==================", fromOffsets)

    fromOffsets.toMap
  }
}
