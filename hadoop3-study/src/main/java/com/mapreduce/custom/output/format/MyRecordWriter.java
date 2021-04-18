package com.mapreduce.custom.output.format;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：MyRecordReader
 * 类 描 述：TODO
 * 创建时间：2021/4/4 下午10:14
 * 创 建 人：chenyouhong
 */
public class MyRecordWriter extends RecordWriter<Text, NullWritable> {

    private FSDataOutputStream good = null;
    private FSDataOutputStream bad = null;

    public MyRecordWriter() {
        super();
    }

    public MyRecordWriter(FSDataOutputStream good, FSDataOutputStream bad) {
        this.good = good;
        this.bad = bad;
    }

    @Override
    public void write(Text text, NullWritable value) throws IOException, InterruptedException {
        String[] split = text.toString().split("\t");
        int num = Integer.parseInt(split[9]);
        if (num <= 1) {
            good.write(text.toString().getBytes());
            good.write("\r\n".getBytes());
        } else {
            bad.write(text.toString().getBytes());
            bad.write("\r\n".getBytes());
        }

    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        IOUtils.closeStream(good);
        IOUtils.closeStream(bad);
    }

}
