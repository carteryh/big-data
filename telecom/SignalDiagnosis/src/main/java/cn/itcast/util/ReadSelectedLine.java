package cn.itcast.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class ReadSelectedLine {
//  读取文件指定行。 
 static void readAppointedLineNumber() throws IOException {

	 RandomAccessFile randomFile=new RandomAccessFile("A:/NWQuality.txt", "rw");
     //String s = reader.readLine();
    randomFile.seek(517);
    System.out.println(randomFile.length());
    System.out.println(randomFile.getFilePointer());
    
    String line=null;
    while ((line=randomFile.readLine())!=null) {
		System.out.println(line);
	}
 }


  
 public static void main(String[] args) throws IOException {
      
readAppointedLineNumber();
      
 }

  
}
