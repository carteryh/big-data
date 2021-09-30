package com.mapreduce.word;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：MyCombiner
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午2:56
 * 创 建 人：chenyouhong
 */
public class MyCombiner extends Reducer<Text, LongWritable, Text, LongWritable>  {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        for (LongWritable value : values) {
            count += value.get();
        }
        context.write(key, new LongWritable(count));
    }

}
