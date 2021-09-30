package cn.itcast.kudu

import org.apache.kudu.ColumnSchema.ColumnSchemaBuilder
import org.apache.kudu.{Schema, Type}
import org.apache.kudu.client.{CreateTableOptions, KuduClient}
import org.apache.kudu.client.KuduClient.KuduClientBuilder
import org.junit.Test

class KuduAPI {

  @Test
  def createTable(): Unit = {
    // 1. 创建 API 入口
    val KUDU_MASTER = "192.168.169.101:7051"
    val kuduClient = new KuduClientBuilder(KUDU_MASTER).build()

    // 2. 创建表的 Schema
    val columns = List(
      new ColumnSchemaBuilder("key", Type.STRING).key(true).build(),
      new ColumnSchemaBuilder("value", Type.STRING).key(false).build()
    )
    import scala.collection.JavaConverters._
    val schema = new Schema(columns.asJava)

    // 3. 指定分区
    val options = new CreateTableOptions()
      .setRangePartitionColumns(List("key").asJava)
      // Tablet 是存储数据的位置, Tablet 是不是可以有多个, 有主从的概念
      .setNumReplicas(1)

    // 4. 创建表
    kuduClient.createTable("simple", schema, options)
  }

  @Test
  def insertData(): Unit = {
    val KUDU_MASTER = "192.168.169.101:7051"
    val kuduClient = new KuduClientBuilder(KUDU_MASTER).build()

    // 得到表对象, 表示对一个表进行操作
    val table = kuduClient.openTable("simple")

    // 表示操作, 组织要操作的行数据
    val insert = table.newInsert()
    val row = insert.getRow
    row.addString(0, "A")
    row.addString(1, "1")

    // 开启会话开始操作
    val session = kuduClient.newSession()
    session.apply(insert)
  }

  @Test
  def scan(): Unit = {
    val KUDU_MASTER = "192.168.169.101:7051"
    val kuduClient = new KuduClientBuilder(KUDU_MASTER).build()

    // 1. 设置投影
    import scala.collection.JavaConverters._
    val projects = List("key", "value").asJava

    // 2. scanner 租装
    val scanner = kuduClient.newScannerBuilder(kuduClient.openTable("simple"))
      .setProjectedColumnNames(projects)
      .build()

    // 3. 获取结果
    while (scanner.hasMoreRows) {
      // 这个 Results 是一整个 tablet
      val results = scanner.nextRows()

      while (results.hasNext) {
        // 这才是一条具体的数据
        val result = results.next()

        println(result.getString(0), result.getString(1))
      }
    }
  }
}
