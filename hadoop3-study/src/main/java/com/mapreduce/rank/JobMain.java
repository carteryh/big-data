package com.mapreduce.rank;

import com.mapreduce.word.WordCountMapper;
import com.mapreduce.word.WordCountReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：MapReduce
 * 类 描 述：TODO
 * 创建时间：2021/1/1 下午8:53
 * 创 建 人：chenyouhong
 */
public class JobMain extends Configured implements Tool {

    private static String path = "hdfs://node01:9000";
    private static String pathInput = path + "/wordcount";
    private static String pathOut = path + "/wordcount_out";
    private static String localPathInput = "/Users/chenyouhong/Desktop/test/sort";
    private static String localPathOut = "/Users/chenyouhong/Desktop/test/output";

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
//        configuration.set("mapreduce.framework.name","local");
//        configuration.set("yarn.resourcemanager.hostname","local");

        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }


    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(super.getConf(), JobMain.class.getSimpleName());
        job.setJarByClass(JobMain.class);
        job.setInputFormatClass(TextInputFormat.class);
//        TextInputFormat.addInputPath(job, new Path(pathInput));
        TextInputFormat.addInputPath(job, new Path(localPathInput));

        job.setMapperClass(SortMapper.class);
        job.setMapOutputKeyClass(SortBean.class);
        job.setMapOutputValueClass(NullWritable.class);

//        job.setPartitionerClass(PartitonerOwn.class);
//        job.setCombinerClass(MyCombiner.class);


        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(SortBean.class);
        job.setOutputValueClass(NullWritable.class);

//        job.setNumReduceTasks(2);

        job.setOutputFormatClass(TextOutputFormat.class);
//        TextOutputFormat.setOutputPath(job, new Path(pathOut));
        TextOutputFormat.setOutputPath(job, new Path(localPathOut));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;

//        Job job = Job.getInstance(super.getConf(), "mapredicer_wordcount");
//        job.setJarByClass(JobMain.class);
//
//        job.setInputFormatClass(TextInputFormat.class);
//        TextInputFormat.addInputPath(job, new Path(pathInput));
//
//        job.setMapperClass(WordCountMapper.class);
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(LongWritable.class);
//
//        job.setReducerClass(WordCountReducer.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(LongWritable.class);
//
//        job.setOutputFormatClass(TextOutputFormat.class);
//        TextOutputFormat.setOutputPath(job, new Path(pathOut));
//
//        boolean b = job.waitForCompletion(true);
//
//        return b ? 0 : 1;
    }

}
