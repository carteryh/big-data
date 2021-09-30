package cn.itcast.tags

import ch.hsr.geohash.GeoHash
import cn.itcast.area.BusinessAreaRunner
import cn.itcast.etl.ETLRunner
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.graphframes.GraphFrame

import scala.collection.{immutable, mutable}

object TagRunner {
  private val ODS_TABLE_NAME = ETLRunner.ODS_TABLE_NAME
  private val AREA_TABLE_NAME = BusinessAreaRunner.AREA_TABLE_NAME

  def main(args: Array[String]): Unit = {
    import cn.itcast.utils.KuduHelper._
    import cn.itcast.utils.SparkConfigHelper._

    // 1. 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("tag")
      .master("local[6]")
      .loadConfig()
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

    val geoHash = udf(toGeoHash _)

    // 2. 读取数据
    val odsOption = spark.readKuduTable(ODS_TABLE_NAME)
    val areaOption = spark.readKuduTable(AREA_TABLE_NAME)

    if (odsOption.isEmpty || areaOption.isEmpty) return

    // 3. 标签生成
    // 3.1 将 ODS 的表加入 GeoHash
    val odsWithGeoHash = odsOption.get.withColumn(
      "geoHash", geoHash('longitude, 'latitude)
    )

    // 3.2 生成一个新的数据集, 这个数据集包含了商圈信息
    val odsWithArea: DataFrame = odsWithGeoHash.join(
      areaOption.get,
      odsWithGeoHash.col("geoHash") === areaOption.get.col("geoHash"),
      "left"
    )

    // 3.3 生成标签数据
    val idsAndTags: Dataset[IdsWithTags] = odsWithArea.map(createTags)

    // 4 统一用户识别
    // 需求: 有些数据中, 有uuid, 有一些有mac, 有一些有udid
    // 场景: 拿着手机移动, 上午在家里, 下午在教室, 晚上在宿舍
    // 情况: 用户1, 在一个时间点上, 汇报了 mac, udid, 另外一个时间点汇报了 mac, uuid
    // 环境: Vertex, Edge
    // 格式: mainId, tags
    //  mainId -> 主ID, 按照顺序取得, 第一个非空的就是主 ID
    //  tags -> GDmale:1,A20:1
    // 步骤::
    // 1. 将数据集转换为 Vertex 和 Edge 的数据集
    val vertex = idsAndTags.map(item => Vertex(item.mainId, item.ids, item.tags))
    val edge = idsAndTags.flatMap(item => {
      // uuid -> lasdkfakjh     mac -> jaksdhfiah1231kjjh
      // 一定要注意边的生成, 是一对多的, 是一个笛卡尔积
      val ids: Map[String, String] = item.ids
      val result: immutable.Iterable[Edge] = for (id <- ids; otherId <- ids if id != otherId) yield Edge(id._2, otherId._2)
      result
    })

    // 2. 图计算
    spark.sparkContext.setCheckpointDir("checkpoint")
    val components: Dataset[VertexComponent] = GraphFrame(vertex.toDF(), edge.toDF())
      .connectedComponents
      .run()
      .as[VertexComponent]

    // 3. 聚合
    // 结果集格式: mainId, tagsString
    val grouped = components.groupByKey(component => component.component)
    val agg: Dataset[(Long, VertexComponent)] = grouped.reduceGroups(reduceVertex _)
    val result = agg.map(mapTags)
    result.show()
  }

  def mapTags(vertexComponent: (Long, VertexComponent)): Tags = {
    val mainId = getMainId(vertexComponent._2.ids)

    // tag1:1,tag2:1,tag3:1
    val tags = vertexComponent._2.tags
        .map(item => item._1 + ":" + item._2)
        .mkString(",")

    Tags(mainId, tags)
  }

  def reduceVertex(curr: VertexComponent, mid: VertexComponent): VertexComponent = {
    val id = curr.id
    val ids = curr.ids ++ mid.ids

    // Map(a -> 1, b -> 2) ++ Map(c -> 3, a -> 4)
    // 结果是 Map(a -> 4, b -> 2, c -> 3)
    // Map(A20 -> 1) ++ Map(A20 -> 1)
    // 结果应该是: Map(A20 -> 2), 但是并不是
    val tags = {
      // Map(A20 -> 1, Gmale -> 1) ++ Map(A20 -> 1)
      // Map(A20 -> 1, Gmale -> 1) ++ Map(A20 -> 1, KWshuaige -> 1)
      val temp = curr.tags.map {
        case (key, value) => if (mid.tags.contains(key)) (key, value + mid.tags(key)) else (key, value)
      }
      mid.tags ++ temp
    }

    VertexComponent(id, ids, tags, curr.component)
  }

  /**
    * 3.3 生成标签数据
    * Row -> mainId, ids, tags
    * Row -> IdsWithTags
    */
  def createTags(row: Row): IdsWithTags = {
    // 3.4 生成标签数据
    val tags = mutable.Map[String, Int]()
    // 3.4.1 广告标识
    tags += ("AD" + row.getAs[Long]("adspacetype") -> 1)
    // 3.4.2 渠道信息
    tags += ("CH" + row.getAs[String]("channelid") -> 1)
    // 3.4.3 关键词 keywords -> 帅哥,有钱
    row.getAs[String]("keywords").split(",")
      .map("KW" + _ -> 1)
      .foreach(tags += _)
    // 3.4.4 省市标签
    tags += ("PN" + row.getAs[String]("region") -> 1)
    tags += ("CN" + row.getAs[String]("city") -> 1)
    // 3.4.5 性别标签
    tags += ("GD" + row.getAs[String]("sex") -> 1)
    // 3.4.6 年龄标签
    tags += ("AG" + row.getAs[String]("age") -> 1)
    // 3.4.7 商圈标签, 正常情况下, 在这里需要生成 GeoHash, 然后再查询 Kudu 表, 获取数据
    // 优化写法, 直接先进行 join, 把商圈信息添加进来, 然后直接取
    row.getAs[String]("area").split(",")
        .map("A" + _ -> 1)
        .foreach(tags += _)

    // 3.5 生成
    // mainId 的生成需要按照一个顺序来进行
    val ids = genIdMap(row)
    val mainId = getMainId(ids)

    IdsWithTags(mainId, ids, tags.toMap)
  }

  def genIdMap(row: Row): Map[String, String] = {
    val keyList = List(
      "imei", "imeimd5", "imeisha1", "mac", "macmd5", "macsha1", "openudid",
      "openudidmd5", "openudidsha1", "idfa", "idfamd5", "idfasha1"
    )

    keyList.map(key => (key, row.getAs[String](key)))
      .filter(kv => StringUtils.isNotBlank(kv._2))
      .toMap
  }

  def getMainId(ids: Map[String, String]): String = {
    val keyList = List(
      "imei", "imeimd5", "imeisha1", "mac", "macmd5", "macsha1", "openudid",
      "openudidmd5", "openudidsha1", "idfa", "idfamd5", "idfasha1"
    )

    keyList.map(key => ids.get(key))
      .filter(option => option.isDefined)
      .map(option => option.get)
      .head
  }

  def toGeoHash(longitude: Double, latitude: Double): String = {
    GeoHash.withCharacterPrecision(latitude, longitude, 8).toBase32
  }
}

case class IdsWithTags(mainId: String, ids: Map[String, String], tags: Map[String, Int])

case class Vertex(id: String, ids: Map[String, String], tags: Map[String, Int])

case class Edge(src: String, dst: String)

case class VertexComponent(id: String, ids: Map[String, String], tags: Map[String, Int], component: Long)

case class Tags(mainId: String, tags: String)
