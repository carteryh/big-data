package com.mapreduce.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：WordCountMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/1 下午8:53
 * 创 建 人：chenyouhong
 */
public class SortReducer extends Reducer<PairWritable, Text, PairWritable, NullWritable> {

    public static enum MyCounter {
        REDUCE_INPUT_KEY_RECOURDS, REDUCE_INPUT_VALUE_RECOURDS
    }

    @Override
    protected void reduce(PairWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        context.getCounter(MyCounter.REDUCE_INPUT_KEY_RECOURDS).increment(1l);

        for (Text value : values) {
            context.getCounter(MyCounter.REDUCE_INPUT_VALUE_RECOURDS).increment(1l);
            context.write(key, NullWritable.get());
        }

    }

}
