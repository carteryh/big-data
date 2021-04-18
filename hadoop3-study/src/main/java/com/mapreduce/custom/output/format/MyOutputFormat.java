package com.mapreduce.custom.output.format;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：MyInputFormat
 * 类 描 述：TODO
 * 创建时间：2021/4/4 下午10:10
 * 创 建 人：chenyouhong
 */
public class MyOutputFormat extends FileOutputFormat<Text, NullWritable> {

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(context.getConfiguration());
        FSDataOutputStream good = fileSystem.create(new Path("file:///Users/chenyouhong/Desktop/test/output/good/good.txt"));
        FSDataOutputStream bad = fileSystem.create(new Path("file:///Users/chenyouhong/Desktop/test/output/bad/bad.txt"));

        MyRecordWriter myRecordWriter = new MyRecordWriter(good, bad);
        return myRecordWriter;
    }

}
