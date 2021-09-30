package com.mapreduce.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：WordCountMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/1 下午8:53
 * 创 建 人：chenyouhong
 */
public class SortMapper extends Mapper<LongWritable, Text, PairWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Counter counter = context.getCounter("MR_COUNT", "MapReducerCounter");
        counter.increment(1l);

        String line = value.toString();
        String[] split = line.split(",");
        PairWritable pairWritable = new PairWritable();
        pairWritable.setFirst(split[0]);
        pairWritable.setSecond(Integer.parseInt(split[1]));
        context.write(pairWritable, value);
    }

}
