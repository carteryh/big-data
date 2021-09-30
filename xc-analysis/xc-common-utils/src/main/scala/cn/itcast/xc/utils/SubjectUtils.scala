package cn.itcast.xc.utils

import org.apache.spark.sql.SparkSession

/**
  * <P>
  * 和课程相关的工具类
  * </p>
  *
  */
object SubjectUtils {

  /**
    * 在线选课根据日期进行统计选课数, 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def subjectLearnSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.course_category_dim_id,
         |a.user_num,
         |IFNULL(((a.user_num - b.user_num) / b.user_num), 1.0) user_percent
         |from
         |(select course_category_dim_id, count(user_dim_id) user_num
         |from data_course.learning_course_fact where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by course_category_dim_id ) as a
         |left join
         |(select course_category_dim_id, count(user_dim_id) user_num
         |from data_course.learning_course_fact where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by course_category_dim_id) as b
         |on a.course_category_dim_id = b.course_category_dim_id
       """.stripMargin)
  }


  /**
    * 在线购买量根据日期进行统计, 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def subjectBuyCountSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.course_category_dim_id,
         |a.sale_num,
         |IFNULL(((a.sale_num - b.sale_num) / b.sale_num), 1.0) sale_percent
         |from
         |(select course_category_dim_id, sum(salesvolume) sale_num
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by course_category_dim_id ) as a
         |left join
         |(select course_category_dim_id, sum(salesvolume) sale_num
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by course_category_dim_id) as b
         |on a.course_category_dim_id = b.course_category_dim_id
       """.stripMargin)
  }

  /**
    * 销售额 根据日期进行统计 及涨幅
    *
    * @param spark   spark
    * @param current 当前日期区间
    * @param last    要对比的日期区间
    * @return
    */
  def subjectBuyAmountSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.course_category_dim_id,
         |a.sale_amount,
         |IFNULL(((a.sale_amount - b.sale_amount) / b.sale_amount), 1.0) amount_percent
         |from
         |(select course_category_dim_id, sum(sales) sale_amount
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by course_category_dim_id ) as a
         |left join
         |(select course_category_dim_id, sum(sales) sale_amount
         |from data_course.course_buy_dwm where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by course_category_dim_id) as b
         |on a.course_category_dim_id = b.course_category_dim_id
       """.stripMargin)
  }

}
