package com.mapreduce.topn;


import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 项目名称：hadoop-study
 * 类 名 称：OrderGroupComparator
 * 类 描 述：TODO
 * 创建时间：2021/4/5 下午12:25
 * 创 建 人：chenyouhong
 */
public class OrderGroupComparator extends WritableComparator {

    public OrderGroupComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean first = (OrderBean)a;
        OrderBean second = (OrderBean)b;

        return first.getOrderId().compareTo(second.getOrderId());
    }

}
