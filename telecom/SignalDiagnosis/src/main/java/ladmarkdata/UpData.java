package ladmarkdata;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class UpData {

	public static String user = "cometl";
	 
	public static String password = "cometl"; 
	public static String url = "jdbc:mysql://192.168.102.100:3306/MUCBASE?useUnicode=true&characterEncoding=UTF8";
 	
	public static String driver = "com.mysql.jdbc.Driver";
	public static String update="";
	
	public static Connection con = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;
	
	public static void main(String[] args) throws NumberFormatException, JSONException, Exception {
		// TODO Auto-generated method stub
		int b;
		BufferedReader bufferedReader = null;
		String line = "";
		int i=0;
		String update="";
		loadlandmarktolocal lan=new loadlandmarktolocal();
		 Class.forName(driver);
		 con = DriverManager.getConnection(url, user, password);
		 stmt = con.createStatement(); 
		try
		{
			bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream (new File ("E:\\landmarkdata\\landmarkdata.txt")),"UTF-8"));
			while ((line=bufferedReader.readLine())!=null)
			{
				i++;
				selectanduodate(line.split("&")[0],line.split("&")[1],line.split("&")[2],line.split("&")[3],Integer.parseInt(line.split("&")[4]),line.split("&")[5],Double.parseDouble(line.split("&")[6]), Double.parseDouble(line.split("&")[7]));	
			System.out.println("第"+i+"条"+"\r\n");
			}
		}
		catch (IOException e)
		{
			System.out.println("文件操作错误："+e.toString());
		}
	}
	
private static void selectanduodate(String landmark,String province,String city,String address,int type1,String type2, double lon,double lat) throws NumberFormatException, JSONException, InterruptedException, IOException {
		 
		
		
	
		//数据库谅解字符串
	
	//更新的数据条数
	int n=0;
	
	 try {
 		 update="INSERT INTO landmarkdata  VALUES(\""+landmark+"\", \""+province+"\", \""+city+"\", \""+address+"\", "+type1+","+type2+","+lon+","+lat+")";
			
			 System.out.println("开始执行更新      "+update);
			 //开始更新
			 n=stmt.executeUpdate(update);
			  if (n!=0){
				  System.out.println("更新完成"+n+"条数据！"); 
			 }else{
				 System.out.println("无更新！"); 
			 }
			 
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


}








