package com.mapreduce.topn;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：OrderBean
 * 类 描 述：TODO
 * 创建时间：2021/4/5 下午12:07
 * 创 建 人：chenyouhong
 */
public class OrderBean implements WritableComparable<OrderBean> {

    private String orderId;

    private Double price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return orderId + '\t' + price;
    }

    @Override
    public int compareTo(OrderBean o) {
        int i = this.orderId.compareTo(o.getOrderId());
        if (i==0) {
            i = this.price.compareTo(o.price) * -1;
        }
        return i;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderId);
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderId = in.readUTF();
        this.price = in.readDouble();
    }

}
