package com.mapreduce.custom.input.format;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：SquenceFileMapper
 * 类 描 述：TODO
 * 创建时间：2021/4/4 下午10:39
 * 创 建 人：chenyouhong
 */
public class SquenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        String fileName = inputSplit.getPath().getName();
        context.write(new Text(fileName), value);
    }

}
