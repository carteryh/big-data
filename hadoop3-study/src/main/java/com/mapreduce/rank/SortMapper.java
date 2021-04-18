package com.mapreduce.rank;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：SortMapper
 * 类 描 述：TODO
 * 创建时间：2021/4/3 下午11:23
 * 创 建 人：chenyouhong
 */
public class SortMapper extends Mapper<LongWritable, Text, SortBean, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        SortBean sortBean = new SortBean();
        sortBean.setWord(split[0]);
        sortBean.setNum(Integer.parseInt(split[1]));
        context.write(sortBean, NullWritable.get());
    }

}


