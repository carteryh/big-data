package cn.itcast.xc.utils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

/**
 * <P>
 * TODO
 * </p>
 *
 */
object DateUtils {

  /**
   * 用于测试工具类中的方法
   */
  def main(args: Array[String]): Unit = {
    // 指定日期, 输出一年中的第几周
    //    println(getWeekNum("2019-01-11"))
    //
    //    // 输出本周区间
    //    println(getLastWeekIntervalWithMillis(0))
    //        println(getWeekInterval(0))
    //    println(getWeekInterval("2019-07-11"))
    //    // 输出上周区间
    //    println(getLastWeekIntervalWithMillis(1))

    //    println(getDateStr(1559185407L * 1000, "yyyy-MM-dd-H"))

    val date_info = "2019-07-18";
    //        println(getWeekRelativelyInterval(date_info))
    //    println(getMonthNum(date_info) + " 月")
    println(getMonthRelativelyInterval(date_info))
  }


  /**
   * 获取两月的相对区间
   * 获取本周 周一到昨天(周N)的区间和上周 周一到昨天(周N)的区间, 如果是今天是周一刚返回上周和上上周的区间,  返回格式: 年-月-日:年-月-日,年-月-日:年-月-日
   *
   * @return
   */
  def getMonthRelativelyInterval(date_info: String): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(date_info)
    val calendar = Calendar.getInstance()
    calendar.setTime(date)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    // 先判断是不是周一, 这个地方有bug只能正常获取上周和上上周的数据, 也就是无论传入的是那个周一, 获取的都是上周和上上周
    if (1 == day) {
      return getMonthInterval(1) + "," + getMonthInterval(2)
    }

    calendar.setTimeInMillis(date.getTime - 86400000)
    val date_info_new = sdf.format(calendar.getTime)
    var n = 0
    val newMonth = date_info_new.split("-")(1).toInt
    if (newMonth != getCurrentMonth) {
      n = getCurrentMonth - newMonth
    }

    var dayNum = date_info_new.split("-")(2)
    val lastMonth = getMonthInterval(1 + n)
    getMonthInterval(0 + n).substring(0, 19) + dayNum + "," + lastMonth.substring(0, 19) + dayNum


  }


  /**
   * 获取上(N)月的区间, 获取上月就传1, 上上月就传2, 本月就传0, 返回: 年-月-日,年-月-日
   *
   * @param num
   * @return
   */
  def getMonthInterval(num: Int): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-")
    val calendar = Calendar.getInstance()
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.add(Calendar.MONTH, -1 * num)
    val startDay = dayOfMonthPathZero(calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
    val endDay = dayOfMonthPathZero(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    val ym = sdf.format(calendar.getTime)

    println("当前月: " + (calendar.get(Calendar.MONTH) + 1) + " 开始日期: " + ym + startDay + " 结束日期: " + ym + endDay)

    // 返回天(yyyy-MM-dd-H)
    ym + startDay + ":" + ym + endDay
  }

  /**
   * 日期-月中的天补0操作
   */

  def dayOfMonthPathZero(day: Int): String = {
    if (day < 10) {
      return "0" + day
    }
    day + ""
  }


  /**
   * 获取月份
   * 格式: 2019-07
   *
   * @param date_info
   * @return
   */
  def getMonthNum(date_info: String) = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(date_info)
    var calendar = Calendar.getInstance()
    calendar.setTime(date)
    val month = calendar.get(Calendar.MONTH) + 1
    calendar.get(Calendar.YEAR) + "-" + month

  }


  /**
   * 获取两周的相对区间
   * 获取本周 周一到昨天(周N)的区间和上周 周一到昨天(周N)的区间, 如果是今天是周一刚返回上周和上上周的区间,  返回格式: 年-月-日:年-月-日,年-月-日:年-月-日
   *
   * @return
   */
  def getWeekRelativelyInterval(date_info: String): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(date_info)
    getWeekRelativelyInterval(date)
  }


  /**
   * 获取两周的相对区间
   * 获取本周 周一到昨天(周N)的区间和上周 周一到昨天(周N)的区间, 如果是今天是周一刚返回上周和上上周的区间,  返回格式: 年-月-日:年-月-日,年-月-日:年-月-日
   *
   * @return
   */
  def getWeekRelativelyInterval(date_info: Date): String = {
    var calendar = Calendar.getInstance()
    calendar.setTime(date_info)
    val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
    // 先判断是不是周一
    if (1 == week) {
      return getWeekInterval(1) + "," + getWeekInterval(2)
    }
    val lastMonday = geLastWeekMonday(date_info)
    val thisMonday = getThisWeekMonday(date_info)
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

    calendar.setTime(lastMonday)
    val between_days = (date_info.getTime - thisMonday.getTime) / (1000 * 3600 * 24);
    calendar.add(Calendar.DATE, between_days.intValue() - 1)

    sdf.format(thisMonday) + ":" + sdf.format(new Date(date_info.getTime - 86400000)) + "," + sdf.format(lastMonday) + ":" + sdf.format(calendar.getTime())
  }


  /**
   * 根据指定日期获取上周一
   *
   * @param date
   * @return
   */
  def geLastWeekMonday(date: Date): Date = {
    val cal = Calendar.getInstance
    cal.setTime(getThisWeekMonday(date))
    cal.add(Calendar.DATE, -7)
    cal.getTime
  }

  /**
   * 根据指定日期获取本周一
   *
   * @param date
   * @return
   */
  def getThisWeekMonday(date: Date): Date = {
    val cal = Calendar.getInstance
    cal.setTime(date)
    // 获得当前日期是一个星期的第几天
    val dayWeek = cal.get(Calendar.DAY_OF_WEEK)
    if (1 == dayWeek) cal.add(Calendar.DAY_OF_MONTH, -1)
    // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
    cal.setFirstDayOfWeek(Calendar.MONDAY)
    val day = cal.get(Calendar.DAY_OF_WEEK)
    // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
    cal.add(Calendar.DATE, cal.getFirstDayOfWeek - day)
    cal.getTime
  }


  /**
   * 根据毫秒值格式化日期为字符串
   *
   * @param millis
   * @param pattern
   * @return
   */
  def getDateStr(millis: Long, pattern: String): String = {
    val dateFormat = new SimpleDateFormat(pattern)
    val datestr = dateFormat.format(new Date(millis))
    datestr
  }

  /**
   * 获昨天的时间
   *
   * @return
   */
  def getYesterday(): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    val yesterday = dateFormat.format(cal.getTime())
    yesterday
  }

  /**
   * 获取上(N)周的区间, 获取上周就传1, 上上周就传2, 本周就传0, 返回毫秒值
   *
   * @param num
   * @return
   */
  def getLastWeekIntervalWithMillis(num: Int): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.add(Calendar.WEEK_OF_YEAR, -1 * num)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val offset = 1 - dayOfWeek
    calendar.add(Calendar.DATE, offset)
    val weekStartDay = sdf.format(calendar.getTime()) + "-0"
    calendar.add(Calendar.DATE, 6)
    val weekEndDay = sdf.format(calendar.getTime()) + "-23"
    val sdfM: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-H")
    println("当前周: " + calendar.get(Calendar.WEEK_OF_YEAR) + " 开始日期: " + weekStartDay + " 结束日期: " + weekEndDay)
    // 返回毫秒值
    sdfM.parse(weekStartDay).getTime / 1000 + "," + sdfM.parse(weekEndDay).getTime / 1000
    // 返回天(yyyy-MM-dd-H)
    //    weekStartDay + "," + weekEndDay
  }


  /**
   * 获取上(N)周的区间, 获取上周就传1, 上上周就传2, 本周就传0, 返回: 年-月-日,年-月-日
   *
   * @param num
   * @return
   */
  def getWeekInterval(num: Int): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.add(Calendar.WEEK_OF_YEAR, -1 * num)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val offset = 1 - dayOfWeek
    calendar.add(Calendar.DATE, offset)
    val weekStartDay = sdf.format(calendar.getTime())
    calendar.add(Calendar.DATE, 6)
    val weekEndDay = sdf.format(calendar.getTime())
    println("当前周: " + calendar.get(Calendar.WEEK_OF_YEAR) + " 开始日期: " + weekStartDay + " 结束日期: " + weekEndDay)
    // 返回天(yyyy-MM-dd-H)
    weekStartDay + ":" + weekEndDay
  }

  /**
   * 根据传入的日期, 参数格式为: yyyy-MM-dd, 获取当前周的获取, 返回: 年-月-日,年-月-日
   *
   * @param date
   * @return
   */
  def getWeekInterval(date: String): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.setTime(sdf.parse(date))

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val offset = 1 - dayOfWeek
    calendar.add(Calendar.DATE, offset)
    val weekStartDay = sdf.format(calendar.getTime())
    calendar.add(Calendar.DATE, 6)
    val weekEndDay = sdf.format(calendar.getTime())
    println("当前周: " + calendar.get(Calendar.WEEK_OF_YEAR) + " 开始日期: " + weekStartDay + " 结束日期: " + weekEndDay)
    // 返回天(yyyy-MM-dd-H)
    weekStartDay + ":" + weekEndDay
  }

  /**
   * 获取指定日期在一年中是第几周
   *
   * @param date 格式: yyyy-MM-dd
   * @return yyyy-week_num
   */
  def getWeekNum(date: String): String = {
    val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.setTime(sdf.parse(date))

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val offset = 1 - dayOfWeek
    calendar.add(Calendar.DATE, offset)
    val year = calendar.get(Calendar.YEAR)
    val weekNum = calendar.get(Calendar.WEEK_OF_YEAR)
    println("当前周: " + year + "-" + weekNum)
    year + "-" + weekNum
  }


  /**
   * 获取当前年
   *
   * @return
   */
  def getCurrentYear: Int = {
    val cal = Calendar.getInstance
    cal.get(Calendar.YEAR)
  }

  /**
   * 获取当前月
   *
   * @return
   */
  def getCurrentMonth: Int = {
    val cal = Calendar.getInstance
    cal.get(Calendar.MONTH) + 1
  }

  def getMonthStartEndDay(year: Int, month: Int): String = { //格式化日期
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val cal = Calendar.getInstance
    //设置年份
    cal.set(Calendar.YEAR, year)
    //设置月份
    cal.set(Calendar.MONTH, month - 1)
    //获取某月最小天数
    val firstDay = cal.getMinimum(Calendar.DATE)
    //设置日历中月份的最小天数
    cal.set(Calendar.DAY_OF_MONTH, firstDay)
    val start = cal.getTime.getTime / 1000
    println("开始日期: " + sdf.format(cal.getTime))
    //获取某月最大天数
    val lastDay = cal.getActualMaximum(Calendar.DATE)
    //设置日历中月份的最大天数
    cal.set(Calendar.DAY_OF_MONTH, lastDay)
    val end = cal.getTime.getTime / 1000
    println("结束日期: " + sdf.format(cal.getTime))
    start + "," + end
  }



  /**
   * 秒转: 天:时:分:秒
   *
   * @param seconds
   * @return
   */
  private def millisConvertDayHourMin(seconds: Long): String = {
    if (seconds < 0) return "秒数必须大于0"
    val one_day = 60 * 60 * 24
    val one_hour = 60 * 60
    val one_minute = 60
    var day = 0L
    var hour = 0L
    var minute = 0L
    var second = 0L
    day = seconds / one_day
    hour = seconds % one_day / one_hour
    minute = seconds % one_day % one_hour / one_minute
    second = seconds % one_day % one_hour % one_minute
    if (seconds < one_minute) seconds + "秒"
    else if (seconds >= one_minute && seconds < one_hour) minute + "分:" + second + "秒"
    else if (seconds >= one_hour && seconds < one_day) hour + "时:" + minute + "分:" + second + "秒"
    else day + "天:" + hour + "时:" + minute + "分:" + second + "秒"
  }

  /**
   * 分钟 转化成 天:时:分
   *
   * @param min
   * @return
   */
  def minConvertDayHourMin(min: Long): String = {
    var result = ""
    if (min != null) {
      val m = min.asInstanceOf[Double]
      val days = (m / (60 * 24)).toInt
      val hours = (m / 60 - days * 24).toInt
      val minutes = (m - hours * 60 - days * 24 * 60).toInt
      if (days > 0) {
        result = s"${days}天:${hours}时:${minutes}分"
      }
      else if (hours > 0) {
        result = s"${hours}时:${minutes}分"
      }
      else {
        result = s"${minutes}分"
      }
    }
    result
  }

}
