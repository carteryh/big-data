package cn.itcast.xc.dimen

import cn.itcast.xc.common.EtlEnvironment

/**
 * <p>
 * 区域维度数据处理
 * </p>
 **/
object AreaDimInsert {

  // 经纬度实体
  case class regionMap(sheng: String, shi: String, xian: String, lng: Float, lat: Float)

  // 区域实体
  case class areaCodeMap(sheng: String, shengCode: String, shi: String, shiCode: String, xian: String, xianCode: String)

  /**
   * 获取spark session
   */
  val spark = EtlEnvironment.getSparkSession(this.getClass.getSimpleName)

  def main(args: Array[String]): Unit = {
    // 获取spark context
    val sc = spark.sparkContext
    // 获取sql context
    val hive = spark.sqlContext

    val area_code = sc.textFile("/area_code.txt")
    val region = sc.textFile("/region.txt")

    // 区域转换实体
    val area = area_code.map(line => {
      val strings = line.split(",")
      val sheng = strings(0).trim().split(" ")
      val shi = strings(1).trim().split(" ")
      val xian = strings(2).trim().split(" ")
      areaCodeMap(sheng(0), sheng(1), shi(0), shi(1), xian(0), xian(1))
    })
    // 转换为临时表
    hive.createDataFrame(area).createOrReplaceTempView("area_code")

    // 经纬度转换实体
    val region_temp = region.map(line => {
      val strings = line.split(",")
      regionMap(strings(0), strings(1), strings(2), strings(3).toFloat, strings(4).toFloat)
    })
    // 转换为临时表
    hive.createDataFrame(region_temp).createOrReplaceTempView("region_temp")

    // 给region_temp这个表加id
    val result = hive.sql("select row_number() over(order by sheng) as id, * from region_temp")
    // 转为临时表
    result.createOrReplaceTempView("region")

    val areaTale = hive.sql(
      """
        |select
        | a.id area_dim_id,  b.shengCode province , b.shiCode city, b.xianCode county,
        | a.lng longitude, a.lat latitude,
        | a.sheng province_name, a.shi	city_name, a.xian county_name
        |from region a
        |left join area_code b
        |on a.xian = b.xian and a.shi = b.shi
        |order by a.id
        |""".stripMargin)

    // 输出结果
    areaTale.show()

    areaTale
      // 分区修改1， 减少小文件
      .repartition(1)
      // 字段分割方式
      .write.option("delimiter", "\\t")
      // 保存路径
      .option("path", "/user/hive/external/data_dimen/area_dim")
      // 写入模式
      .mode("overwrite")
      // 库名.表名
      .saveAsTable("data_dimen.area_dim")

    // 关闭资源
    sc.stop()
    spark.close()
  }

}
