package com.mapreduce.flow.sort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：FlowCounterMapper
 * 类 描 述：TODO
 * 创建时间：2021/1/3 下午3:31
 * 创 建 人：chenyouhong
 */
public class FlowCounterSortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {

    @Override
    protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(value, key);
        }
    }
}
