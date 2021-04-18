package com.mapreduce.partition;

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
public class PartitionerReducer extends Reducer<Text, NullWritable, Text, NullWritable> {

    public static enum Counter {
        MY_INPUT_RECOREDS, MY_INPUT_BYTES
    }

    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.getCounter(Counter.MY_INPUT_RECOREDS).increment(1L);
        context.write(key, NullWritable.get());
    }

}
