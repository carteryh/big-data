package cn.itcast.test

import org.apache.spark.sql.SparkSession
import org.graphframes.GraphFrame

object Graph {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("pmt json etl")
      .master("local[6]")
      .getOrCreate()

    import spark.implicits._

    val vertex = spark.createDataFrame(List(
      ("a", "Alice", 34),
      ("b", "Bob", 36),
      ("c", "Charlie", 30),
      ("d", "David", 29),
      ("e", "Esther", 32),
      ("f", "Fanny", 36),
      ("g", "Gabby", 60)
    )).toDF("id", "name", "age")

    val edge = spark.createDataFrame(List(
      ("a", "b", "friend"),
      ("b", "c", "follow"),
      ("c", "b", "follow"),
      ("f", "c", "follow"),
      ("e", "f", "follow"),
      ("e", "d", "friend"),
      ("d", "a", "friend"),
      ("a", "e", "friend")
    )).toDF("src", "dst", "relationship")

    spark.sparkContext.setCheckpointDir("checkpoint")

    val graph = GraphFrame(vertex, edge).connectedComponents.run()
    graph.show()
  }
}
