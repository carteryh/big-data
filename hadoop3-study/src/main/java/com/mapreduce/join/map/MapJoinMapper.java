package com.mapreduce.join.map;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：FlowCounterMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午3:31
 * 创 建 人：chenyouhong
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, Text, Text> {

    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();

        FileSystem fileSystem = FileSystem.get(cacheFiles[0], context.getConfiguration());
        FSDataInputStream inputStream = fileSystem.open(new Path(cacheFiles[0]));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(",");
            map.put(split[0], line);
        }

        bufferedReader.close();
        fileSystem.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取pid
        String[] split = value.toString().split(",");
        String productId = split[2];
        String productLine = map.get(productId);

        String valueLine = productLine + "\t" + value.toString();
        context.write(new Text(productId), new Text(valueLine));
    }

}
