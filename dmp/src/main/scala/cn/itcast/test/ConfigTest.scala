package cn.itcast.test

import com.typesafe.config.ConfigFactory

object ConfigTest {

  def main(args: Array[String]): Unit = {
    // 1. 创建工具类
    val config = ConfigFactory.load("test.conf")

    // 2. 读取
    val bar = config.getInt("foo.bar")
    val baz = config.getInt("foo.baz")

    // 3. 打印
    println(bar, baz)
  }
}
