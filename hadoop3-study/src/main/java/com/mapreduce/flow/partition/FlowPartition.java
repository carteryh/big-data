package com.mapreduce.flow.partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：FlowPartition
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午5:01
 * 创 建 人：chenyouhong
 */
public class FlowPartition  extends Partitioner<Text, FlowBean> {

    public int getPartition(Text text, FlowBean flowBean, int i) {
        String line = text.toString();
        if (line.startsWith("135")) {
            return 0;
        } else if (line.startsWith("136")) {
            return 1;
        } else if (line.startsWith("137")) {
            return 2;
        } else {
            return 3;
        }
    }

}
