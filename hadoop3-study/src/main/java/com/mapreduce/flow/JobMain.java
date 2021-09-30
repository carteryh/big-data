package com.mapreduce.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：FlowCounterMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午3:31
 * 创 建 人：chenyouhong
 */
public class JobMain extends Configured implements Tool {

    private static String path = "hdfs://node01:9000";
    private static String pathInput = path + "/wordcount";
    private static String pathOut = path + "/wordcount_out";
    private static String localPathInput = "/Users/chenyouhong/Desktop/test/flow";
    private static String localPathOut = "/Users/chenyouhong/Desktop/test/output";

    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(super.getConf(), this.getClass().getName());

//        TextInputFormat.addInputPath(job, new Path(pathInput));
        TextInputFormat.addInputPath(job, new Path(localPathInput));

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(FlowCounterMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setReducerClass(FlowCounterReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setOutputFormatClass(TextOutputFormat.class);
//        TextOutputFormat.setOutputPath(job, new Path(pathOut));
        TextOutputFormat.setOutputPath(job, new Path(localPathOut));

        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }

}
