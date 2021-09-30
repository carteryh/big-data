package cn.itcast.xc.entity

/**
  * <P>
  * TODO
  * </p>
  *
  */

/**
  * 平台看板离线业务表对应实体
  *
  * @param clazz           分类: 课程访问量, 课程购买量, 听课人数, 机构数, 已定义枚举@PlatformEnum
  * @param total_count     总量
  * @param yesterday_count 昨日
  * @param years           分区年
  * @param months          分区月
  * @param days            分区日
  */
case class PlatformOfflineBiz(clazz: String,
                              total_count: Int,
                              yesterday_count: Int,
                              years: String,
                              months: String,
                              days: String)
