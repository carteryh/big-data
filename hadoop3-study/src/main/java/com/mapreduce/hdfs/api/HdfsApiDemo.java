package com.mapreduce.hdfs.api;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;

import java.net.URL;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：HdfsApiDemo
 * 类 描 述：TODO
 * 创建时间：2021/3/31 下午9:43
 * 创 建 人：chenyouhong
 */
public class HdfsApiDemo {

    public void urlHdfs() {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());



    }


}



