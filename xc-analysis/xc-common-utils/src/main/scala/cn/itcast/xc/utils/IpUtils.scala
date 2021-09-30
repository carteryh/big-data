package cn.itcast.xc.utils

import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

/**
  * <P>
  * TODO
  * </p>
  *
  */
object IpUtils {
  val config = new DbConfig
  val searcher = new DbSearcher(config, "ip2region.db")

  def getArea(ip: String): String = {
    val method = searcher.getClass.getMethod("memorySearch", classOf[String])
    val dataBlock = method.invoke(searcher, ip).asInstanceOf[DataBlock]
      dataBlock.getRegion.split("\\|")(3)
  }

  def main(args: Array[String]): Unit = {
    println(getArea("218.29.75.103"))
  }

}
