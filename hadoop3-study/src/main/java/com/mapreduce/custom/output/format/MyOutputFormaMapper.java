package com.mapreduce.custom.output.format;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：MyOutputFormaMapper
 * 类 描 述：TODO
 * 创建时间：2021/4/4 下午10:00
 * 创 建 人：chenyouhong
 */
public class MyOutputFormaMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullWritable.get());
    }

}
