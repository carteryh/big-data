package canal;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriteData {
    //将数据写入磁盘的方法
    public static   void writeData(String path ,   String datas  ) throws IOException {
        //实例输出流
        FileOutputStream fos =   new FileOutputStream(path);
        //写数据
        fos.write(datas.getBytes());
        fos.flush();
    }
}
