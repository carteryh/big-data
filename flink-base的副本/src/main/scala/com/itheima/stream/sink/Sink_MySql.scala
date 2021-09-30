package com.itheima.stream.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

object Sink_MySql {

  def main(args: Array[String]): Unit = {

    // 1. env
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 2. load list
    val listDataSet: DataStream[(Int, String, String, String)] = env.fromCollection(List(
      (10, "dazhuang", "123456", "大壮"),
      (11, "erya", "123456", "二丫"),
      (12, "sanpang", "123456", "三胖")
    ))


    // 3. add sink
    listDataSet.addSink(new MySqlSink)

    // 4. execute
    env.execute()

  }

}

class MySqlSink extends RichSinkFunction[(Int, String, String, String)] {


  var connection: Connection = null;
  var ps: PreparedStatement = null;

  override def open(parameters: Configuration): Unit = {

    // 1. 加载MySql驱动
    Class.forName("com.mysql.jdbc.Driver")
    // 2. 创建连接
    connection = DriverManager.getConnection("jdbc:mysql:///test", "root", "123456")
    // 3. 创建PreparedStatement
    val sql = "insert into user(id,username,password,name) values(?,?,?,?)"
    ps = connection.prepareStatement(sql)
  }

  override def invoke(value: (Int, String, String, String)): Unit = {

    // 执行插入
    ps.setInt(1,value._1)
    ps.setString(2,value._2)
    ps.setString(3,value._3)
    ps.setString(4,value._4)

    ps.executeUpdate()
  }


  override def close(): Unit = {
    // 关闭连接
    if(connection!=null){
      connection.close()
    }
    if(ps!=null){
      ps.close()
    }
  }


}
