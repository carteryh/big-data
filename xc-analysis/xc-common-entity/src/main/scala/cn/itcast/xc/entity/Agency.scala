package cn.itcast.xc.entity

/**
  * <P>
  * 机构相关实体
  * </p>
  *
  */

/**
  * 机构销量-周
  *
  * @param company_id   机构id
  * @param company_name 机构名
  * @param sale_num     销量
  * @param sale_percent 涨幅
  * @param years        分区年
  * @param week_of_year 分区周
  */
case class AgencySaleWeekBiz(company_id: String,
                             company_name: String,
                             sale_num: Int,
                             sale_percent: Float,
                             years: String,
                             week_of_year: String)

/**
  * 机构销量-月
  *
  * @param company_id    机构id
  * @param company_name  机构名
  * @param sale_num      销量
  * @param sale_percent  涨幅
  * @param years         分区年
  * @param month_of_year 分区周
  */
case class AgencySaleMonthBiz(company_id: String,
                              company_name: String,
                              sale_num: Int,
                              sale_percent: Float,
                              years: String,
                              month_of_year: String)


/**
  * 机构销售额-周
  *
  * @param company_id     机构id
  * @param company_name   机构名
  * @param sale_amount    销售额
  * @param amount_percent 涨幅
  * @param years          分区年
  * @param week_of_year   分区周
  */
case class AgencyAmountWeekBiz(company_id: String,
                               company_name: String,
                               sale_amount: Float,
                               amount_percent: Float,
                               years: String,
                               week_of_year: String)


/**
  * 机构销售额-月
  *
  * @param company_id     机构id
  * @param company_name   机构名
  * @param sale_amount    销售额
  * @param amount_percent 涨幅
  * @param years          分区年
  * @param month_of_year  分区月
  */
case class AgencyAmountMonthBiz(company_id: String,
                                company_name: String,
                                sale_amount: Float,
                                amount_percent: Float,
                                years: String,
                                month_of_year: String)


/**
  * 机构学生人数-周
  *
  * @param company_id   机构id
  * @param company_name 机构名
  * @param user_num     用户数
  * @param user_percent 涨幅
  * @param years        分区年
  * @param week_of_year 分区周
  */
case class AgencyUserWeekBiz(company_id: String,
                             company_name: String,
                             user_num: Int,
                             user_percent: Float,
                             years: String,
                             week_of_year: String)

/**
  * 机构学生人数-月
  *
  * @param company_id    机构id
  * @param company_name  机构名
  * @param user_num      用户数
  * @param user_percent  涨幅
  * @param years         分区年
  * @param month_of_year 分区月
  */
case class AgencyUserMonthBiz(company_id: String,
                              company_name: String,
                              user_num: Int,
                              user_percent: Float,
                              years: String,
                              month_of_year: String)