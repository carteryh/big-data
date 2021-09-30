package com.mapreduce.udf;

import eu.bitwalker.useragentutils.UserAgent;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * Created by carter
 */
public class UAParseUDF extends UDF {

    //hive自定义函数实现对UA的解析
    public String evaluate(final String userAgent){
        StringBuilder builder = new StringBuilder();
        //todo 利用第三方的jar包构建一个UserAgent对象  基于该对象实现UA的解析
        UserAgent ua = new UserAgent(userAgent);
        builder.append(ua.getOperatingSystem()+"\t"+ua.getBrowser()+"\t"+ua.getBrowserVersion());
        return  (builder.toString());
    }

}
