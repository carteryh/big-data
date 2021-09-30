package cn.itcast.xc.utils

import java.io.ByteArrayInputStream

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.IOUtils

/**
  * <P>
  * hdfs 相关工具类
  * </p>
  *
  */
object HdfsUtils {

  /**
    * 根据路径把指定str, 写文件
    * @param path
    * @param str
    */
  def saveToPath(path: String, str: String) = {
    // 获取hdfs
    val hdfs = getHdfs()
    // 创建输出流
    val out = hdfs.create(new Path(path))
    // 把str写文件
    IOUtils.copyBytes(new ByteArrayInputStream(str.getBytes("UTF-8")), out, 4096, false)
    // 关闭资源
    IOUtils.closeStream(out)
  }


  /**
    * 获取hdfs
    * @return
    */
  def getHdfs() = {
    val conf = new Configuration()
    FileSystem.get(conf)
  }

}
