package com.mapreduce.partition;

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
public class MyPartitoner extends Partitioner<Text, NullWritable> {

    @Override
    public int getPartition(Text text, NullWritable value, int numPartitions) {
        String[] split = text.toString().split("\t");
        String numStr = split[5];
        if (Integer.parseInt(numStr) > 15) {
            return 1;
        }
        return 0;
    }

}
