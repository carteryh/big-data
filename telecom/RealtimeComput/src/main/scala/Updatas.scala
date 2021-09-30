import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}

import java.sql.{DriverManager, ResultSet}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies
import org.apache.spark.streaming.kafka010.LocationStrategies
import org.apache.kafka.common.serialization.StringDeserializer

object Updatas {


  def main(args: Array[String]): Unit = {
    //network 已知的编号与运营商的对应关系
    val network =Map(
      "46000"->"CMCC",
      "46001"->"CUCC",
      "46002"->"CMCC",
      "46008"->"CMCC",
      "46003"->"CTCC",
      "46011"->"CTCC",
      "46006"->"CUCC",
      "46007"->"CMCC",
      "46020"->"CMCC",
      "46005"->"CTCC"
    )

    //设置日志级别
    LoggerLevels.setStreamingLogLevels()

    //1 创建spark conf
    val conf =new SparkConf().setAppName("DataProcess").setMaster("local[2]")
    //2 创建sparkzContext
    val sc =new SparkContext(conf)
    //3 创建SparkStreamingContext
    val ssc =new StreamingContext(sc,Seconds(2))
    // kafka集群
//    val kafkaParams=Map("metadata.broker.list"->"node01:9092")

    // kafka参数
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "192.168.3.30:9092",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (true: java.lang.Boolean),
      ConsumerConfig.GROUP_ID_CONFIG -> "groupId"
    )

    //kafka topic
    val topics= Set("Telecoms")

    //读取kafka内的数据
//    val kafkaDatas: InputDStream[(String, String)] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))
    val kafkaDatas: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))

    // original
    //    val kafkaDatas: InputDStream[(String, String)]=KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)
    //获取数据本身
//    val kafkaValues: DStream[String] = kafkaDatas.map(_.value())
    val kafkaValues: DStream[String] = kafkaDatas.map(record => {
      println("=========" + record.value)
      println("-----" + record.value())

      (record.value())
    })

//    val kafkaValues: DStream[String] = kafkaDatas.map(_._2)


    //定义new_cell_strength计数器，用于记录cell_strength新增的数据条数
    var new_cell_strength = sc.longAccumulator("new_cell_strength")
    //定义new_networkqualityinfo计数器，用于记录networkqualityinfo新增的数据条数
    var new_networkqualityinfo=   sc.longAccumulator("new_networkqualityinfo")
    //定义new_data_connection计数器，用于记录data_connection新增的数据条数
    val new_data_connection = sc.longAccumulator("new_data_connection")



    //实例三个变量，用于存储数据库中三个对应的值
    var ALL_cell_strength=0
    var ALL_data_connection=0
    var ALL_networkqualityinfo=0


    //连接dashbord所需数据的数据库
    val conn_str = "jdbc:mysql://node01:3306/telecom?user=root&password=12345678"
    classOf[com.mysql.jdbc.Driver]
    //连接数据库
    val conn = DriverManager.getConnection(conn_str)
    val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    // 执行查询语句
    val rs = statement.executeQuery("select NWQuality_count,Signal_Strength_count,DataConnection_count from tb_counts where NWOperator=\"ALL\"")
    // Iterate Over ResultSet
    while (rs.next) {
      //将数据库中的值赋值给变量
      ALL_cell_strength=rs.getString("Signal_Strength_count").toInt
      ALL_data_connection=rs.getString("DataConnection_count").toInt
      ALL_networkqualityinfo=rs.getString("NWQuality_count").toInt
    }

    println("mysql  原始数据    "+ALL_cell_strength)
    println("mysql  原始数据    "+ALL_data_connection)
    println("mysql  原始数据    "+ALL_networkqualityinfo)



    //遍历RDD中的每一个数据

    kafkaValues.foreachRDD(rdd=>{
      rdd.foreach(message=>{
        //使用#CS#将数据拆分开
        //INSERT#CS#
        // APP#CS#
        // networkqualityinfo#CS#
        // #CS#
        // 1    100     152     1923    385     511     -58     39.961248       116.358825              357242041311082http://ccpn.3322.org/speedtest   3.0h            2017-05-19 20:11:08     2017-05-19 20:12:19     Wi-Fi   CUCC    00:1b:2f:63:97:aa       460015228355   BeiJing  BeiJing


        //INSERT#CS#
        // APP#CS#
        // cell_strength#CS#
        // 46001-41023-5184265#CS#
        // 17     46001-41023-5184265     UMTS    17      -1      -1      -1      -1      -1      39.962232       116.360972      gcj02-Network -1507     -1      -1      2017-07-08 17:17:27     357841032749893

        val datas= message.split("#CS#")
        if ("cell_strength".equalsIgnoreCase(datas(2))){ //若数据是cell_strength表的数据
          //获取出network_id的数据
          val network_id=datas(3)
          //截取出network_id数据的前5位，并在Map中获取对应的value
          val networkid= network.get(network_id.substring(0,5))
          //若networkid为 CMCC\CUCC\CTCC中的任何一个，则计数器new_cell_strength + 1
          if (networkid.contains("CMCC")||networkid.contains("CUCC")||networkid.contains("CTCC")){
            new_cell_strength.add(1)
          }
        }else if ("data_connection".equalsIgnoreCase(datas(2))){//若数据是data_connection表的数据
        //获取出network_id的数据
        val network_id=datas(3)
          //截取出network_id数据的前5位，并在Map中获取对应的value
          val networkid= network.get(network_id.substring(0,5))
          //若networkid为 CMCC\CUCC\CTCC中的任何一个，则计数器new_data_connection + 1
          if (networkid.contains("CMCC")||networkid.contains("CUCC")||networkid.contains("CTCC")){
            new_data_connection.add(1)
          }
        }else if ("networkqualityinfo".equalsIgnoreCase(datas(2))){//若数据是networkqualityinfo表的数据   计数器new_networkqualityinfo + 1
          new_networkqualityinfo.add(1)
        }

      })
      //每个RDD将最新的数据向数据库中写入一次
      println("new_data_connection  "+new_data_connection.value)
      println("new_cell_strength  "+new_cell_strength.value)
      println("new_networkqualityinfo  "+new_networkqualityinfo.value)

      //更新语句
      val ps = conn.prepareStatement("UPDATE tb_counts SET NWQuality_count = ?,Signal_Strength_count= ?,DataConnection_count= ? WHERE NWOperator=?")

      // 设置更新语句的参数
      // 每次的更新都是将  第一次在数据库中读取的数据（不变化）加上计数器的数据（持续递增）和     更新到数据库中（）
      ps.setInt(1, ALL_networkqualityinfo+new_networkqualityinfo.value.toInt)
      ps.setInt(2, ALL_cell_strength+new_cell_strength.value.toInt)
      ps.setInt(3, ALL_data_connection+new_data_connection.value.toInt)

      ps.setString(4, "ALL")
      val res = ps.executeUpdate()
      ps.close()


    })
    //开启任务开启循环
    ssc.start()
    ssc.awaitTermination()
  }

}
