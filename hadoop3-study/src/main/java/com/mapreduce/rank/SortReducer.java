package com.mapreduce.rank;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：SortReducer
 * 类 描 述：TODO
 * 创建时间：2021/4/3 下午11:27
 * 创 建 人：chenyouhong
 */
public class SortReducer extends Reducer<SortBean, NullWritable, SortBean, NullWritable> {

    @Override
    protected void reduce(SortBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }

}
