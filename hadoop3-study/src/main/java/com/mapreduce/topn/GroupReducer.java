package com.mapreduce.topn;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：PartitionerReducer
 * 类 描 述：TODO
 * 创建时间：2021/4/3 下午9:06
 * 创 建 人：chenyouhong
 */
public class GroupReducer extends Reducer<OrderBean, Text, Text, NullWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int i=0;
        for (Text value: values) {
            context.write(value, NullWritable.get());
            i++;
            if (i >= 2) {
                break;
            }
        }
    }

}
