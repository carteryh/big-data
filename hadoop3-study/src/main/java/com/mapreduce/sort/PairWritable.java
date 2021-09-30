package com.mapreduce.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 项目名称：hadoop3-study
 * 类 名 称：PairWritable
 * 类 描 述：TODO
 * 创建时间：2021/1/2 下午6:51
 * 创 建 人：chenyouhong
 */
public class PairWritable implements WritableComparable<PairWritable> {

    private String first;

    private int second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return first + "-----" + second ;
    }

    public int compareTo(PairWritable o) {
        int result = this.first.compareTo(o.first);
        if (result == 0) {
            return this.second - o.second;
        }
        return result;
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.first);
        dataOutput.writeInt(this.second);
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readInt();
    }

}
