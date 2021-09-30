package cn.itcast.area

import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_zh_CN
import com.typesafe.config.ConfigFactory
import okhttp3.{OkHttpClient, Request}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization

object HttpUtils {
  private val config = ConfigFactory.load("common")
  private val baseUrl: String = config.getString("amap.baseUrl")
  private val key: String = config.getString("amap.key")

  private val client = new OkHttpClient()

  def getLocationInfo(longitude: Double, latitude: Double): Option[String] = {
    // 1. 确定 URL, 通过确定 URL, 从而确定参数
    val url = s"${baseUrl}v3/geocode/regeo" +
      s"?key=$key&location=$longitude,$latitude"

    try {
      // 2. 发送请求
      val request = new Request.Builder()
        .url(url)
        .get()
        .build()

      // 3. 获取结果
      val response = client.newCall(request).execute()

      if (response.isSuccessful) {
        val result = response.body().string()
        Some(result)
      } else {
        None
      }
    } catch {
      case e: Exception => {
        e.printStackTrace()
        None
      }
    }
  }

  def parseJson(json: String): AMapLocation = {
    // json -> AMapLocation
    // 1. 拷贝要解析的字符串, 去掉无用内容
//    val json =
//      """
//        |{
//        |	"regeocode": {
//        |		"addressComponent": {
//        |			"businessAreas": [{
//        |				"location": "116.470293,39.996171",
//        |				"name": "望京",
//        |				"id": "110105"
//        |			}]
//        |		}
//        |	}
//        |}
//      """.stripMargin

    // 2. 编写对象

    // 3. 解析
    implicit val formats = Serialization.formats(NoTypeHints)
    val result = Serialization.read[AMapLocation](json)
    result
  }
}

case class AMapLocation(regeocode: Option[RegeoCode])

case class RegeoCode(addressComponent: Option[AddressComponent])

case class AddressComponent(businessAreas: Option[List[BusinessArea]])

case class BusinessArea(name: String)
