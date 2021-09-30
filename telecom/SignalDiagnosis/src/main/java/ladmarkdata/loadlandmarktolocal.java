package ladmarkdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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



public class loadlandmarktolocal{

	
	public  static class update{
		
			private static void selectanduodate() throws NumberFormatException, JSONException, InterruptedException, IOException {
			
				
				 File file = new File("E:/landmarkdata.txt"); 
				 File file2 = new File("E:/landmarklog.txt"); 
				  if (file.exists()) {
				  file.deleteOnExit(); 
				  file.createNewFile(); 
				  file2.deleteOnExit(); 
				  file2.createNewFile(); 
				  } else { 
					  file.createNewFile();
					  file2.createNewFile();
				  } 
				  BufferedWriter out = new BufferedWriter(new FileWriter(file)); 
				  BufferedWriter out2 = new BufferedWriter(new FileWriter(file2)); 
				//数据库谅解字符串
			//String user = "cometl";
			//String password = "cometl";
			String user = "root";
			String password = "root";
			//String url = "jdbc:mysql://192.168.102.100:3306/MUCBASE?useUnicode=true&characterEncoding=UTF8";
			String url = "jdbc:mysql://localhost:3306/handu?useUnicode=true&characterEncoding=UTF8";
			
			String driver = "com.mysql.jdbc.Driver";
			String sql = "select user_lon,user_lat from Signal_Strength WHERE user_lon>116.36233 and user_lat>39.97072 GROUP BY user_lon,user_lat limit 900";
			String update="";
			
			Connection con = null;
			Statement stmt = null;
			Statement stmt2 = null;
			ResultSet rs = null;
			//更新的数据条数
			int n=0;
			
			 try {
				Class.forName(driver);
				con = DriverManager.getConnection(url, user, password);
				stmt = con.createStatement();  
				stmt2 = con.createStatement(); 
				rs = stmt.executeQuery(sql);
				 while(rs.next())
				{	
					 
					 String log ="经度  "+rs.getString("user_lon")+"      纬度 "+rs.getString("user_lat")+"   的数据  ";
					 System.out.println(log);
					 out2.write(log+ "\r\n");
					 //将查到的经度纬度传给landmark查询接口。通过POI获得更新语句
					 update= landmark( Double.parseDouble(rs.getString("user_lon")), Double.parseDouble(rs.getString("user_lat")));
					 if (update.equals("")||update.equalsIgnoreCase("null")){
						 String log1 ="以该点为圆心，50米为半径内，无结果。";
						 System.out.println(log1);
						 out2.write(log1+ "\r\n");
						 
					 }else{
						 String log2 =update+ "\r\n";
						 out.write(log2);
						 out2.write(log2);
						 System.out.println(update);
					 }
 
				}
				 out.close(); 
				 out2.close(); 
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	
	public static void main(String[] args) throws JSONException, NumberFormatException, InterruptedException, IOException {
		
		selectanduodate();
		
	}
	


	public static  String landmark(double lng, double lat) throws JSONException {
		// TODO Auto-generated method stub
		//定义需要获得的名称
		//地址
		String address;
		//类别一
		String type1;
		//类别二
		String type2;
		//landmark类别
		String tags;
		//省份
		String province;
		//城市
		String city;
		//半径
		int r=50;
		//或得到的数据个数
		int pnums=1;
		String  updates = "";
		//局和数据连接地址
		String url = "http://apis.juhe.cn/baidu/getLocate?";
		//密钥ڵ�ַ
		//8a1314e08909b005039bcc1f33bb3c02
		//3608a68290d1f5abc1d5101c172dc57e
		String key= "8a1314e08909b005039bcc1f33bb3c02";
		String urlAll = new StringBuffer(url).append("key=").append(key).append("&dtype=json&lng=").append(lng).append("&lat=").append(lat).append("&r=").append(r).append("&cid=&page=&pnums=").append(pnums).toString();
		String charset ="UTF-8";
		//System.out.print(urlAll);
		String jsonResult = get(urlAll, charset);
		JSONObject object=new JSONObject(jsonResult);
		int code = object.getInt("error_code");
		//
		if(code==0){
		  JSONObject resultObject = object.getJSONObject("result");
		  JSONArray array = resultObject.getJSONArray("data");
		  String  a =array.get(0).toString();
		  JSONObject jsonObject=new JSONObject(a);
		   tags = jsonObject.getString("tags");
		   province = jsonObject.getString("province");
		   city = jsonObject.getString("city");
		   address= jsonObject.getString("address");
		   type1= jsonObject.getString("type1");
		   type2= jsonObject.getString("type2"); 
		   updates=tags+"&"+province+"&"+city+"&"+address+"&"+type1+"&"+type2+"&"+lng+"&"+lat;
			   
			
		}else{
 		}
		return updates;
	}
	}



	public static String get(String urlAll,String charset){
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";//ģ�������
			try {
				URL url = new URL(urlAll);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.setReadTimeout(30000);
				connection.setConnectTimeout(30000);
				connection.setRequestProperty("User-agent",userAgent);
				connection.connect();
				InputStream is = connection.getInputStream();
				reader = new BufferedReader(new InputStreamReader(
				is, charset));
					String strRead = null;
					while ((strRead = reader.readLine()) != null) {
					sbf.append(strRead);
					sbf.append("\r\n");
					}
				reader.close();
				result = sbf.toString();
			} catch (Exception e) {
			e.printStackTrace();
			}
		return result;
		}



	



	
	
	
	
	

}



