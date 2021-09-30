package cn.itcast.test

import com.maxmind.geoip.{Location, LookupService}
import org.junit.Test
import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

class IPTest {

  /**
    * IP -> Region, City
    */
  @Test
  def ip2Region(): Unit = {
    val searcher = new DbSearcher(new DbConfig(), "dataset/ip2region.db")
    val region = searcher.btreeSearch("121.76.98.134").getRegion
    println(region)
  }

  /**
    * IP -> Longitude, Latitude
    */
  @Test
  def ip2Location(): Unit = {
    // 1. 创建入口
    val service = new LookupService(
      "dataset/GeoLiteCity.dat",
      LookupService.GEOIP_MEMORY_CACHE
    )

    // 2. 搜索
    val location: Location = service.getLocation("121.76.98.134")

    // 3. 打印
    println(location.longitude, location.latitude, location.region, location.city)
  }
}
