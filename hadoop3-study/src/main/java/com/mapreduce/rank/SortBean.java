package com.mapreduce.rank;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 项目名称：hadoop-study
 * 类 名 称：SortBean
 * 类 描 述：TODO
 * 创建时间：2021/4/3 下午11:15
 * 创 建 人：chenyouhong
 */
public class SortBean implements WritableComparable<SortBean> {

    private String word;

    private int num;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int compareTo(SortBean o) {
        int result = this.word.compareTo(o.word);
        if (result == 0) {
           return this.num - o.num;
        }
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(word);
        out.writeInt(num);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
       this.word = in.readUTF();
       this.num = in.readInt();
    }

    @Override
    public String toString() {
        return word + '\t' + num;
    }

}
