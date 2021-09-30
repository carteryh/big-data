package cn.itcast.xc.utils

import org.apache.spark.sql.SparkSession

/**
  * <P>
  * 和课程相关的工具类
  * </p>
  *
  */
object CourseUtils {

  /**
    * 在线选课根据日期进行统计选课数, 及涨幅
    *
    * @param spark spark
    * @param current 当前日期区间
    * @param last 要对比的日期区间
    * @return
    */
  def courseLearnSql(spark: SparkSession, current: Array[String], last: Array[String]) = {
    spark.sql(
      s"""
         |select
         |a.course_category_dim_id, a.course_dim_id,
         |a.user_num,
         |IFNULL(((a.user_num - b.user_num) / b.user_num), 1.0) user_percent
         |from
         |(select course_category_dim_id, course_dim_id, count(user_dim_id) user_num
         |from data_course.learning_course_fact where concat_ws('-',years,months,days) between '${current(0)}'  and '${current(1)}'
         |group by course_dim_id, course_category_dim_id ) as a
         |left join
         |(select course_dim_id, count(user_dim_id) user_num
         |from data_course.learning_course_fact where concat_ws('-',years,months,days) between '${last(0)}'  and '${last(1)}'
         |group by course_dim_id) as b
         |on a.course_dim_id = b.course_dim_id
       """.stripMargin)
  }

}
