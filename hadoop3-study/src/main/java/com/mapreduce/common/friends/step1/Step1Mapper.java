package com.mapreduce.common.friends.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Step1Mapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(":");
        String[] friends = split[1].split(",");

        for (String friend: friends) {
            context.write(new Text(friend), new Text(split[0]));
        }

    }

}
