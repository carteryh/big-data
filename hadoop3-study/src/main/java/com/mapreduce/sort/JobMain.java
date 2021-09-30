package com.mapreduce.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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

    private static String path = "hdfs://node01:8020";
    private static String pathInput = path + "/input/sort";
    private static String pathOut = path + "/out/sort_out";

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
//        configuration.set("mapreduce.framework.name","local");
//        configuration.set("yarn.resourcemanager.hostname","local");

        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }


    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(super.getConf(), JobMain.class.getSimpleName());
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path(pathInput));
        job.setMapperClass(SortMapper.class);

        job.setMapOutputKeyClass(PairWritable.class);
        job.setMapOutputValueClass(Text.class);

//        job.setPartitionerClass(PartitonerOwn.class);

        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(PairWritable.class);
        job.setOutputValueClass(NullWritable.class);

//        job.setNumReduceTasks(2);

        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(pathOut));
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;

    }

}
