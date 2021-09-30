package cn.itcast.xc.utils

import org.apache.spark.sql.SparkSession

/**
  * <P>
  * 和课程相关的工具类
  * </p>
  *
  */
object AgencyUtils {

  /**
    * 机构 课程销量 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def aegncyLearnSql(spark: SparkSession, current: Array[String], last: Array[String], ofYear: Array[String], types: String) = {
    spark.sql(
      s"""
         |select
         |aa.company_id,
         |aa.company_name,
         |cast(aa.user_num as int) user_num,
         |cast(IFNULL(((aa.user_num - bb.user_num) / bb.user_num), 1.0) as float) user_percent,
         |'${ofYear(0)}' years,
         |'${ofYear(1)}' ${types}_of_year
         |from
         |(SELECT  c.company_id, c.name company_name, count(a.user_dim_id ) user_num
         |FROM (
         |SELECT course_dim_id,user_dim_id FROM data_course.learning_course_fact
         |WHERE concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}') as a
         |LEFT JOIN data_dimen.course_dim b on a.course_dim_id = b.course_dim_id
         |LEFT JOIN data_dimen.company_dim c on b.company_id = c.company_id
         |GROUP BY c.company_id, c.name) as aa
         |left join
         |(SELECT  c.company_id, c.name company_name, count(a.user_dim_id ) user_num
         |FROM (
         |SELECT course_dim_id,user_dim_id FROM data_course.learning_course_fact
         |WHERE concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}') as a
         |LEFT JOIN data_dimen.course_dim b on a.course_dim_id = b.course_dim_id
         |LEFT JOIN data_dimen.company_dim c on b.company_id = c.company_id
         |GROUP BY c.company_id, c.name) as bb
         |on aa.company_id = bb.company_id
       """.stripMargin)
  }


  /**
    * 机构 课程销量 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def agencyBuyCountSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.company_id,
         |a.sale_num,
         |IFNULL(((a.sale_num - b.sale_num) / b.sale_num), 1.0) sale_percent
         |from
         |(select company_id, sum(salesvolume) sale_num
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by company_id ) as a
         |left join
         |(select company_id, sum(salesvolume) sale_num
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by company_id) as b
         |on a.company_id = b.company_id
       """.stripMargin)
  }

  /**
    * 机构 课程销售额 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def agencyBuyAmountSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.company_id,
         |a.sale_amount,
         |IFNULL(((a.sale_amount - b.sale_amount) / b.sale_amount), 1.0) amount_percent
         |from
         |(select company_id, sum(sales) sale_amount
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by company_id ) as a
         |left join
         |(select company_id, sum(sales) sale_amount
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by company_id) as b
         |on a.company_id = b.company_id
       """.stripMargin)
  }

}
