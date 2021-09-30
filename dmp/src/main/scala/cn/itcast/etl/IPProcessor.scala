package cn.itcast.etl
import com.maxmind.geoip.LookupService
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DoubleType, StringType}
import org.apache.spark.sql.{Dataset, Row}
import org.lionsoul.ip2region.{DbConfig, DbSearcher}

/**
  * 把一个没有省市信息和经纬度的数据集, 加入这些信息, 再返回
  */
object IPProcessor extends Processor {

  override def process(dataset: Dataset[Row]): Dataset[Row] = {
    // 原始数据集: c1, c2, c3, ..., c4, c5, ip -> Schema
    // 转换过的数据集: c1, c2, c3, ..., c4, c5, ip, region, city, longitude, latitude

    // 如何实现转换?
    // 方法一: 生成只有 IP 的数据集, 转换, 把 IP 对象转换为 (ip, region, city, longitude, latitude)
    // 接下来, 生成 Dataset, 和原始数据集按照 IP 列来进行 Join
    // 方法二: 能否直接插入四列? withColumn, 编写 UDF
    // 方法三: 生成新的数据, 然后修改原始的 Schema

    // 此处采用方法三, 因为修改 Schema 信息, 也是一个比较常见的操作

    // 1. 把 dataset 转为新的一个数据集, 添加数据, region, city, longitude, latitude
    // 使用 Map 还是 MapPartitions
    val converted: RDD[Row] = dataset.rdd.mapPartitions(convertIP)

    // 2. 生成新的 Schema
    val schema = dataset.schema
      .add("region", StringType)
      .add("city", StringType)
      .add("longitude", DoubleType)
      .add("latitude", DoubleType)

    // 3. 使用数据(RDD), 加上新的 Schema, 生成新的 Dataset
    val datasetWithIP = dataset.sparkSession.createDataFrame(converted, schema)
    datasetWithIP
  }

  def convertIP(iterator: Iterator[Row]): Iterator[Row] = {
    // 1. 生成 省市信息
    val dbSearcher = new DbSearcher(new DbConfig(), "dataset/ip2region.db")
    // 2. 生成 经纬度信息
    val lookupService = new LookupService(
      "dataset/GeoLiteCity.dat",
      LookupService.GEOIP_MEMORY_CACHE
    )

    iterator.map(row => {
      val ip = row.getAs[String]("ip")

      val regionAll = dbSearcher.btreeSearch(ip).getRegion
      val region = regionAll.split("\\|")(2)
      val city = regionAll.split("\\|")(3)

      val location = lookupService.getLocation(ip)
      val longitude = location.longitude.toDouble
      val latitude = location.latitude.toDouble

      val rowSeq = row.toSeq :+ region :+ city :+ longitude :+ latitude
      Row.fromSeq(rowSeq)
    })
  }
}
