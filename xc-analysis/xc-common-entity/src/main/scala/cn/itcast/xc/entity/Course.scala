package cn.itcast.xc.entity

/**
 * Created by root on 2019/6/10 0010.
 */

//  课程访问量第一次处理落地至资源库
case class CvSourceMap(client_ip: String,
                       course_dim_id: String,
                       bs_name: String,
                       client_sessionid: String,
                       visit_time: Long)

//  课程访问量第二次处理落地数据至主题库
case class CvThemeMap(client_ip: String,
                      client_sessionid: String,
                      course_dim_id: String,
                      visit_time: Long,
                      date_info: String)

//  课程详细页下的课程访问总量
case class CvCourseTotalMap(course_dim_id: String,
                            count: Long,
                            visit_time: String)


/**
 * 课程访问量增加涨幅
 */
case class VisitThemeMap(client_ip: String,
                         client_sessionid: String,
                         course_dim_id: String,
                         visit_time: Long,
                         time_str: String,
                         date_info: String)


/**
 * 购买量 主题表
 */
case class CourseBuyTheme(company_id: String,
                          course_dim_id: String,
                          time_dim_id: String,
                          area_dim_id: String,
                          company_dim_id: String,
                          price: Float,
                          status: String,
                          use_dim_id: String)

/**
 * 课程访问量落地
 */
case class CourseVisitBusiness(date_info: String,
                               count: Int)


/**
 * =============================================
 * ==================  开始  ====================
 * =============================================
 **/

/**
 * 课程访问量
 *
 * @param course_visit_fact_id 事实id, 生成
 * @param course_dim_id        课程id
 * @param time_dim_id          时间id
 * @param area_dim_id          区域id( 根据用户ip计算 )
 * @param company_dim_id       公司
 * @param client_ip            用户i  course_dim_id: String,
 *                             bs_name: String,
 *                             client_sessionid: String,
 *                             visit_time: Long)
 *                             *
 *                             //  课程访问量第二次处理落地数据至主题库
 *                             case class CvThemeMap(client_ip: String,p
 * @param client_sessionid     用户session id
 * @param client_type          浏览器user-agent
 * @param visit_time           浏览时间
 * @param years                分区年
 * @param months               分区月
 * @param days                 分区日
 */
case class CourseVisitFact(
                            course_visit_fact_id: String,
                            course_dim_id: String,
                            time_dim_id: String,
                            area_dim_id: String,
                            company_dim_id: String,
                            client_ip: String,
                            client_sessionid: String,
                            // user-agent
                            client_type: String,
                            visit_time: Long,
                            years: String,
                            months: String,
                            days: String)

/**
 * 区域维度
 *
 * @param area_dim_id   区域id
 * @param province      省id
 * @param city          市id
 * @param county        县id
 * @param longitude     经度
 * @param latitude      纬度
 * @param province_name 省
 * @param city_name     市
 * @param county_name   县
 */
case class AreaDimen(area_dim_id: Int,
                     province: String,
                     city: String,
                     county: String,
                     longitude: Float,
                     latitude: Float,
                     province_name: String,
                     city_name: String,
                     county_name: String)


/**
 * 用户维度
 *
 * @param user_dim_id              用户id
 * @param username                 登陆名
 * @param name                     用户名
 * @param userpic                  用户头像
 * @param utype                    用户类型
 * @param utype_name               用户类型名
 * @param birthday                 生日
 * @param sex                      性别
 * @param email                    邮箱
 * @param phone                    电话
 * @param qq                       qq
 * @param ip                       注册时ip
 * @param status                   用户状态
 * @param create_time              注册时间
 * @param update_time              更新时间
 * @param last_password_reset_date 下次修改密码提醒时间
 */
case class UserDimen(user_dim_id: String,
                     username: String,
                     name: String,
                     userpic: String,
                     utype: String,
                     utype_name: String,
                     birthday: String,
                     sex: String,
                     email: String,
                     phone: String,
                     qq: String,
                     ip: String,
                     status: String,
                     // 以下三个值是LONG类型, 但数据有null, 导致不能封装, 这里使用string接收, 使用时转longs
                     create_time: String,
                     update_time: String,
                     last_password_reset_date: String)

/**
 * 公司维度
 *
 * @param companyId   公司id
 * @param name        公司名称
 * @param linkname    联系人名称
 * @param mobile      联系人手机
 * @param email       联系人mail
 * @param intro       公司简介
 * @param logo        公司logo
 * @param identitypic 身份证照片
 * @param worktype    工具性质
 * @param businesspic 营业执照
 * @param status      企业状态
 */
case class CompanyDimen(companyId: String,
                        name: String,
                        linkname: String,
                        mobile: String,
                        email: String,
                        intro: String,
                        logo: String,
                        identitypic: String,
                        worktype: String,
                        businesspic: String,
                        status: String)

/**
 * 课程分类 维度
 * 源表为自关联字典表
 *
 * @param course_category_dim_id 分类id
 * @param name                   分类名
 * @param label                  分类标签
 * @param parentid               分类父id
 * @param isshow                 是否展示
 * @param orderby                排序值
 * @param isleaf                 是否是子节点
 */
case class CourseCategoryDim(course_category_dim_id: String,
                             name: String,
                             label: String,
                             parentid: String,
                             isshow: String,
                             orderby: String,
                             isleaf: String)


/**
 * 课程维度
 * s
 *
 * @param course_dim_id 课程id
 * @param name          课程名
 * @param mt            课程大分类id
 * @param mt_name       课程大分类名
 * @param grade         课程等级id
 * @param grade_name    课程等级名
 * @param st            课程小分类id
 * @param st_name       课程小分类名
 * @param company_id    公司id
 * @param company_name  公司名
 * @param users         适用用户
 * @param status        课程状态 id
 * @param status_name   课程状态名
 * @param description   课程介绍
 */
case class CourseDimen(course_dim_id: String,
                       name: String,
                       mt: String,
                       mt_name: String,
                       grade: String,
                       grade_name: String,
                       st: String,
                       st_name: String,
                       company_id: String,
                       company_name: String,
                       users: String,
                       status: String,
                       status_name: String,
                       description: String)

/**
 * 每天访问量
 *
 * @param date_info 日期, 格式: yyyy-MM-dd
 * @param count     访问次数
 * @param years     分区年
 * @param months    分区月
 * @param days      分区日
 */
case class CourseVisitBiz(date_info: String,
                          count: Int,
                          years: String,
                          months: String,
                          days: String)

/**
 * 每天购买量
 *
 * @param date_info 日期, 格式: yyyy-MM-dd
 * @param count     购买量
 * @param years     分区年
 * @param months    分区月
 * @param days      分区日
 */
case class CourseBuyBiz(date_info: String,
                        count: Int,
                        years: String,
                        months: String,
                        days: String)

/**
 * 用户选课事实
 * s
 *
 * @param learning_course_dim_id 选课表id
 * @param course_category_dim_id 课程分类表id
 * @param course_dim_id          课程id
 * @param user_dim_id            用户id
 * @param time_dim_id            时间维度表id
 * @param status                 选课状态
 * @param years                  分区年
 * @param months                 分区月
 * @param days                   分区日
 */
case class LearningCourseFact(learning_course_dim_id: String,
                              course_category_dim_id: String,
                              course_dim_id: String,
                              user_dim_id: String,
                              time_dim_id: String,
                              status: String,
                              years: String,
                              months: String,
                              days: String)


/**
 * 课程购买量 事实
 *
 * 源数据: 用户id  课程id  订单id    订单金额    订单状态    订单时间    用户ip    购买时间
 *
 * @param course_buy_fact_id 订单id
 * @param course_dim_id      课程id
 * @param time_dim_id        时间id
 * @param area_dim_id        区域id
 * @param company_dim_id     公司id
 * @param price              价格
 * @param status             订单状态( 401002 已完成, 401003, 已取消, 未付款, 401001 )
 * @param user_dim_id        用户id
 * @param years              分区年
 * @param months             分区月
 * @param days               分区日
 */
case class CourseBuyFact(course_buy_fact_id: String,
                         course_dim_id: String,
                         time_dim_id: String,
                         area_dim_id: String,
                         company_dim_id: String,
                         price: Float,
                         status: String,
                         user_dim_id: String,
                         years: String,
                         months: String,
                         days: String)

/**
 * 课程购买过程数据
 *
 * @param course_dim_id          课程id
 * @param course_category_dim_id 课程分类id
 * @param sales                  课程销售额
 * @param salesvolume            课程销量
 * @param years                  分区年
 * @param months                 分区月
 * @param days                   分区日
 */
case class CourseBuyDWM(course_dim_id: String,
                        company_id: String,
                        course_category_dim_id: String,
                        sales: String,
                        salesvolume: String,
                        years: String,
                        months: String,
                        days: String)


/**
 * 热门课程业务表-按周
 *
 * @param course_dim_id          课程id
 * @param course_category_dim_id 课程分类id
 * @param course_name            课程名称
 * @param user_num               用户数
 * @param week_compare           与上周比较涨跌(1代表上升，0不变，-1下降)
 * @param week_percent           周涨幅
 * @param years                  分区年
 * @param week_of_year           分区周
 */
case class CourseHotByWeekBiz(course_dim_id: String,
                              course_category_dim_id: String,
                              course_name: String,
                              user_num: Int,
                              week_compare: Int,
                              week_percent: Float,
                              years: String,
                              week_of_year: String)

/**
 * 热门课程业务表-按月
 *
 * @param course_dim_id          课程id
 * @param course_category_dim_id 课程分类id
 * @param course_name            课程名称
 * @param user_num               用户数
 * @param month_compare          与上月比较涨跌(1代表上升，0不变，-1下降)
 * @param month_percent          月涨幅
 * @param years                  分区年
 * @param month_of_year          分区周
 */
case class CourseHotByMonthBiz(course_dim_id: String,
                               course_category_dim_id: String,
                               course_name: String,
                               user_num: Int,
                               month_compare: Int,
                               month_percent: Float,
                               years: String,
                               month_of_year: String)

/**
 * mysql实时采集, 课程访问量对应实体
 *
 * @param order_number 订单号
 * @param price        价格
 * @param status       状态
 * @param order_time   订单时间
 * @param user_id      用户id
 * @param details      交易详情
 */
case class CourseOrdersSource(order_number: String,
                              price: Float,
                              status: String,
                              order_time: BigInt,
                              user_id: String,
                              details: String
                             )


/**
 * mysql实时采集, 用户选课 对应实体
 *
 * @param learning_course_id
 * @param course_id
 * @param user_id
 * @param choose_time
 * @param status
 */
case class LearningCourseSource(learning_course_id: String,
                                course_id: String,
                                user_id: String,
                                choose_time: BigInt,
                                status: String
                               )


/**
 * 用户选课 中间数据
 *
 * @param learning_course_online_id 选课id
 * @param course_id                 课程id
 * @param course_name               课程名
 * @param video_name                视频名
 * @param user_id                   用户id
 * @param user_name                 用户名
 * @param learn_time                学习时间
 * @param learn_count               学习时长
 * @param date_info                 日期
 */
case class LearningCourseOnlineDwm(learning_course_online_id: String,
                                   course_id: String,
                                   course_name: String,
                                   video_id: String,
                                   video_name: String,
                                   user_id: String,
                                   user_name: String,
                                   learn_time: String,
                                   learn_count: String,
                                   date_info: String)


/**
 * 用户在线学习采集数据对应实体
 *
 * @param user_id         用户id
 * @param course_id       课程id
 * @param course_video_id 视频id
 * @param user_ip         用户ip
 * @param user_session_id 用户session
 * @param user_agent      user-agent
 * @param learn_time      学习时间
 */
case class UserLeanOnline(user_id: String,
                          course_id: String,
                          course_video_id: String,
                          user_ip: String,
                          user_session_id: String,
                          user_agent: String,
                          learn_time: Long)


/**
 * 用户选课 ES 索引实体
 *
 * @param learning_course_online_id 选课id
 * @param course_name               课程名
 * @param video_name                视频名
 * @param user_name                 用户名
 * @param date_info                 日期
 */
case class LearningCourseOnlineEsIndex(learning_course_online_id: String,
                                       course_name: String,
                                       video_name: String,
                                       user_name: String,
                                       date_info: String)


/**
 * =============================================
 * ==================  结束  ====================
 * =============================================
 **/