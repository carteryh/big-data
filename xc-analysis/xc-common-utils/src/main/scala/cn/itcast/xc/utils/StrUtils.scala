package cn.itcast.xc.utils

import java.util.UUID

/**
  * <P>
  * 字符工具类
  * </p>
  *
  */
object StrUtils {
  /**
    * 用于测试工具类中的方法
    */
  def main(args: Array[String]): Unit = {
    //    val user_agent = "ozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36"
    //        val user_agent = "Mozilla/5.0 (X11; Linux x86_64; rv:67.0) Gecko/20100101 Firefox/67.0"
    //    val user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36 OPR/48.0.2685.52"
    //    println(getBrowserInfo(user_agent))

    //    println(getUuId())

//    println(mapUnderscoreToCamelCase("ab_cd_ef"))
//    println(mapCamelCaseToUnderscore("bCddfDsafsDsdfs").toLowerCase)

//    println(getTempTableName(this.getClass.getSimpleName))

    println(getClassName(this.getClass.getSimpleName))
  }

  /**
   * 获取类名
   *
   * @param name 类名
   */
  def getClassName(name: String) = {
    name.substring(0, name.length - 1)
  }

  /**
    * 传入类名, 生成临时表名
    * 表名规则是:类名转下划线加_tmp
    *
    * @param name 类名
    */
  def getTempTableName(name: String) = {
    mapCamelCaseToUnderscore(name).replaceAll("\\$","").toLowerCase().substring(1) + "_tmp"
  }


  /**
    * 浏览器中获取浏览器名称
    *
    * @param user_agent
    * @return
    */
  def getBrowserInfo(user_agent: String): String = {
    val body_ls: Array[String] = user_agent.split(" ").map(_.trim())
    val body_sp: Array[String] = body_ls(body_ls.length - 2).split("/")
    val bs_name: String = body_sp(0)
    return bs_name
  }

  /**
    * 获取一个随机数
    *
    * @return
    */
  def getUuId() = {
    UUID.randomUUID().toString.replaceAll("-", "")
  }

  import java.util.regex.Pattern

  /**
    * 将下划线映射到骆驼命名使用的正则表达式, 预编译正则用于提高效率
    */
  private val PATTERN_FOR_CONVERSION = Pattern.compile("_([a-z]){1}")

  /**
    * 将下划线映射到骆驼命名
    *
    * @param str
    * @return
    */
  def mapUnderscoreToCamelCase(str: String): String = {
    // 先转成全小写
    var result = str.toLowerCase
    val matcher = PATTERN_FOR_CONVERSION.matcher(result)
    while (matcher.find) {
      result = result.replaceAll(matcher.group, matcher.group(1).toUpperCase)
    }
    result
  }

  /**
    * 将骆驼命名映射到下划线, 必须是标准的驼峰命名, 否则会出现奇怪的结果
    *
    * @param str
    * @return
    */
  def mapCamelCaseToUnderscore(str: String): String = str.replaceAll("([A-Z]){1}", "_$1").toUpperCase
}
