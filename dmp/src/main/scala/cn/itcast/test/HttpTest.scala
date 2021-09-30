package cn.itcast.test

import okhttp3.{OkHttpClient, Request}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.junit.Test

class HttpTest {
  private val client = new OkHttpClient()

  @Test
  def http(): Unit = {
    val url = "https://restapi.amap.com/v3/geocode/regeo?key=bec6562dc27d1762044c1b20cc56327e&location=116.481488,39.990464"

    val request = new Request.Builder()
      .url(url)
      .get()
      .build()

    try {
      val response = client.newCall(request).execute()

      if (response.isSuccessful) {
        println(response.body().string())
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  @Test
  def json(): Unit = {
    val json =
      """
        |{
        |  "info": "OK",
        |  "infocode": "10000",
        |  "status": "1",
        |  "regeocode": {
        |      "country": "中国",
        |      "township": "燕园街道",
        |      "businessAreas": [
        |        {
        |          "location": "116.303364,39.97641",
        |          "name": "万泉河",
        |          "id": "110108"
        |        },
        |        {
        |          "location": "116.314222,39.98249",
        |          "name": "中关村",
        |          "id": "110108"
        |        },
        |        {
        |          "location": "116.294214,39.99685",
        |          "name": "西苑",
        |          "id": "110108"
        |        }
        |      ]
        |  }
        |}
      """.stripMargin

    implicit val formats = Serialization.formats(NoTypeHints)
    val gaode = Serialization.read[Gaode](json)
    println(gaode)
  }
}

case class Gaode(infocode: String, status: String, regeocode: Option[RegeoCode])

case class RegeoCode(businessAreas: Option[List[BusinessArea]])

case class BusinessArea(name: String)
