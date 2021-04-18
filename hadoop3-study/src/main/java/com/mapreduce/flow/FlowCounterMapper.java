package com.mapreduce.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：FlowCounterMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午3:31
 * 创 建 人：chenyouhong
 */
public class FlowCounterMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1:拆分手机号
        String[] split = value.toString().split("\t");
        String phoneNum = split[1];
        //2:获取四个流量字段
        FlowBean flowBean = new FlowBean();
        flowBean.setUpFlow(Integer.parseInt(split[6]));
        flowBean.setDownFlow(Integer.parseInt(split[7]));
        flowBean.setUpCountFlow(Integer.parseInt(split[8]));
        flowBean.setDownCountFlow(Integer.parseInt(split[9]));
        //3:将k2和v2写入上下文中
        context.write(new Text(phoneNum), flowBean);
    }

}
