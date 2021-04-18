package com.mapreduce.topn;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 项目名称：hadoop-study
 * 类 名 称：MyPartitoner
 * 类 描 述：TODO
 * 创建时间：2021/4/3 下午8:50
 * 创 建 人：chenyouhong
 */
public class OrderPartitoner extends Partitioner<OrderBean, Text> {

    @Override
    public int getPartition(OrderBean orderBean, Text text, int numPartitions) {
        return (orderBean.getOrderId().hashCode() & 2147483647) % numPartitions;
    }

}
