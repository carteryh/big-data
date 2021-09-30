package cn.itcast.area

import ch.hsr.geohash.GeoHash
import cn.itcast.etl.ETLRunner
import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 这个功能的目的: 根据经纬度, 查询高德的 API 获取商圈信息, 生成数据库放置商圈信息
  * 结果: geo, area
  * 为什么要做这个功能: 如果不做这个表的话, 在生成标签的时候, 都要访问高德
  *      高德的 API 不是一直能用的, 每天有使用次数的, 可能出现失败的情况
  *      针对数据来进行一次处理, 获取其商圈信息, 缓存下来, 这个缓存, 就是商圈表
  *      核心点, 就是按照经纬度的范围作为 Key, 商圈信息作为 Value, 生成一张表
  */
object BusinessAreaRunner {

  def main(args: Array[String]): Unit = {
    import cn.itcast.utils.SparkConfigHelper._
    import cn.itcast.utils.KuduHelper._

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .master("local[6]")
      .appName("business_area_table")
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 2. 数据读取
    val odsOption = spark.readKuduTable(ODS_TABLE_NAME)
    val areaOption = spark.readKuduTable(AREA_TABLE_NAME)

    // 注册 UDF
    spark.udf.register("geoHash", locationToGeoHash _)
    spark.udf.register("fetchArea", fetchArea _)

    // 3. 数据处理
    // 数据计算平台: 1. 批量计算 2. 流计算
    // 对于商圈标签生成的时候, 数据可能是流式的, 也可能是批的
    // 不能每一次都读取全量的数据来生成标签
    // 应该尽量的减少查询高德 API 的次数
    // 读取 ODS 层的数据, 生成每条数据所在的地理位置的范围, 其实就是表中的 Key
    // 和商圈库做一次差集, 如果 Key 存在了, 则去掉这条数据, 后再进行高德 API 的查询
    var result: DataFrame = null

    if (odsOption.isDefined && areaOption.isEmpty) {
      // 走到这个地方, 说明 ODS 表是存在的, 但是商圈表不存在
      // 意味着, 在这个分支中, 不需要进行差集
      // ods 只需要两列, 一列是 经度, 一列是 纬度
      // 根据经度和纬度生成一个范围
      // 根据经纬度, 查询高德 API, 从而得到商圈信息
      // 1. 投影经纬度
      // 2. 根据经纬度查询高度 API
      // 3. 根据经纬度, 生成一个范围 ID
      // 4. 处理数据

      val ods = odsOption.get

      result = ods.select('longitude, 'latitude)
        .selectExpr("geoHash(longitude, latitude) as geoHash", "fetchArea(longitude, latitude) as area")
    }

    if (odsOption.isDefined && areaOption.isDefined) {
      // 走到这个地方, 说明 ODS 表是存在的, 商圈表也存在
      // 先进行差集, 去掉已有的数据, 再查询高德 API
      // ods 中如果针对某个经纬度范围已经生成过商圈了, 就不要再生成一次
      // 1. 在 ods 表中, 投影出经纬度, 最终生成 geoHash 列和 area 列, 叫做 odsWithArea
      // 2. odsWithArea 和 area 进行 join, 左外连接
      // 3. join 完毕以后, 如果结果表中有数据的 area 为 null, 那么就可以从高德 API 中获取数据
      val ods = odsOption.get
      val area = areaOption.get

      val odsWithGeoHash = ods.selectExpr("geoHash(longitude, latitude) as geoHash", "longitude", "latitude")
        .withColumn("area", lit(null))

      result = odsWithGeoHash.join(
          area,
          odsWithGeoHash.col("geoHash") === area.col("geoHash"),
          "left"
        )
        .where(area.col("area").isNull)
        .select(
          odsWithGeoHash.col("geoHash"),
          expr("fetchArea(longitude, latitude) as area")
        )
    }

    // 4. 数据落地
    if (result != null) {
      // 走到这里, 说明数据至少处理过了, 所以, 可以进行落地
      spark.createKuduTable(AREA_TABLE_NAME, schema, keys, isDelete = false)
      result.saveToKudu(AREA_TABLE_NAME)
    }
  }

  def locationToGeoHash(longitude: Double, latitude: Double): String = {
    // (116.123123123, 31.456456123), (116.123123125, 31.456456122) -> JDHFS757
    // GeoHash 是一种算法, 这种算法能够将一定范围内的所有经纬度, 都对照一个 String 值
    // 可以间接的实现范围的概念
    GeoHash.withCharacterPrecision(latitude, longitude, 8).toBase32
  }

  def fetchArea(longitude: Double, latitude: Double): String = {
    val jsonOption: Option[String] = HttpUtils.getLocationInfo(longitude, latitude)
    // Option[String] map -> Option[AMapLocation]
    jsonOption.map(json => HttpUtils.parseJson(json))
      .map(location => {
        val area = location.regeocode.get.addressComponent.get.businessAreas.getOrElse(List())
        // List[BusnessArea] -> 东单,西单,西二旗
        area.map(_.name).mkString(",")
      }).getOrElse("")
  }

  private val ODS_TABLE_NAME = ETLRunner.ODS_TABLE_NAME

  val AREA_TABLE_NAME: String = "BUSINESS_AREA"

  private val keys = List("geoHash")
  import scala.collection.JavaConverters._
  private val schema = new Schema(
    List(
      new ColumnSchemaBuilder("geoHash", Type.STRING).nullable(false).key(true).build(),
      new ColumnSchemaBuilder("area", Type.STRING).nullable(true).key(false).build()
    ).asJava
  )
}
