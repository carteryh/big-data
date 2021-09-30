package com.mapreduce.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：PartitonerOwn
 * 类 描 述：TODO
 * 创建时间：2021/1/2 下午4:03
 * 创 建 人：chenyouhong
 */
public class PartitonerOwn extends Partitioner<Text, LongWritable> {

    public int getPartition(Text text, LongWritable longWritable, int i) {
        if (text.toString().length() >= 5) {
            return 0;
        }
        return 1;
    }

}
