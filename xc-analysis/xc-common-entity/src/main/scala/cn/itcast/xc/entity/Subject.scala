package cn.itcast.xc.entity

/**
  * <P>
  * 课程相关po
  * </p>
  *
  */

/**
  * 热门学课-学生人数(选课)-周
  *
  * @param course_category_dim_id 课程分类id
  * @param course_category_name   课程分类名
  * @param user_num               用户数
  * @param user_percent           涨幅
  * @param years                  分区年
  * @param week_of_year           分区年中的周
  */
case class SubjectLearnUserWeekBiz(course_category_dim_id: String,
                                   course_category_name: String,
                                   user_num: Int,
                                   user_percent: Float,
                                   years: String,
                                   week_of_year: String)

/**
  * 热门学课-学生人数(选课)-月
  *
  * @param course_category_dim_id 课程分类id
  * @param course_category_name   课程分类名
  * @param user_num               用户数
  * @param user_percent           涨幅
  * @param years                  分区年
  * @param month_of_year          分区年中的月
  */
case class SubjectLearnUserMonthBiz(course_category_dim_id: String,
                                    course_category_name: String,
                                    user_num: Int,
                                    user_percent: Float,
                                    years: String,
                                    month_of_year: String)

/**
  * 热门学课-销量-周
  *
  * @param course_category_dim_id 学科id
  * @param course_category_name   学科名
  * @param sale_num               销售量
  * @param sale_percent           涨幅
  * @param years                  分区年
  * @param week_of_year           分区年中的周
  */
case class SubjectSaleWeekBiz(course_category_dim_id: String,
                              course_category_name: String,
                              sale_num: Int,
                              sale_percent: Float,
                              years: String,
                              week_of_year: String)

/**
  * 热门学课-销量-月
  *
  * @param course_category_dim_id 学科id
  * @param course_category_name   学科名
  * @param sale_num               销售量
  * @param sale_percent           涨幅
  * @param years                  分区年
  * @param month_of_year          分区年中的周
  */
case class SubjectSaleMonthBiz(course_category_dim_id: String,
                               course_category_name: String,
                               sale_num: Int,
                               sale_percent: Float,
                               years: String,
                               month_of_year: String)

/**
  * 热门学课-销售额-周
  *
  * @param course_category_dim_id 学科id
  * @param course_category_name   学科名
  * @param sale_amount            销售额
  * @param amount_percent         涨幅
  * @param years                  分区年
  * @param week_of_year           分区年中的周
  */
case class subjectAmountWeekBiz(course_category_dim_id: String,
                                course_category_name: String,
                                sale_amount: Float,
                                amount_percent: Float,
                                years: String,
                                week_of_year: String)

/**
  * 热门学课-销售额-月
  *
  * @param course_category_dim_id 学科id
  * @param course_category_name   学科名
  * @param sale_amount            销售额
  * @param amount_percent         涨幅
  * @param years                  分区年
  * @param month_of_year          分区年中的月
  */
case class subjectAmountMonthBiz(course_category_dim_id: String,
                                 course_category_name: String,
                                 sale_amount: Float,
                                 amount_percent: Float,
                                 years: String,
                                 month_of_year: String)