package ladmarkdata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;  



public class Addlandmark{

	
	public static class update{
		
			private static void selectanduodate() throws NumberFormatException, JSONException, InterruptedException {
			//数据库谅解字符串
			String user = "cometl";
			String password = "cometl";
			String url = "jdbc:mysql://192.168.102.100:3306/MUCBASE?useUnicode=true&characterEncoding=UTF8";
			String driver = "com.mysql.jdbc.Driver";
			String sql = "select user_lon,user_lat from Signal_Strength WHERE user_lon>116.363155 and user_lat>39.96823 GROUP BY user_lon,user_lat limit 2";
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
					 System.out.println("开始更新经度  "+rs.getString("user_lon")+"      纬度 "+rs.getString("user_lat")+"   的数据  ");
					
					 //将查到的经度纬度传给landmark查询接口。通过POI获得更新语句
					 update= landmark( Double.parseDouble(rs.getString("user_lon")), Double.parseDouble(rs.getString("user_lat")));
					 System.out.println("开始执行更新      "+update);
					 //不为空开始更新
					 if (update!=null||update!=""){
						 n=stmt2.executeUpdate(update);
					 }
					 System.out.println("更新完成"+n+"条数据！");  
				}
				
				 
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	
	public static void main(String[] args) throws JSONException, NumberFormatException, InterruptedException {
		
		selectanduodate();
		
	}
	


	private static String landmark(double lng, double lat) throws JSONException {
		// TODO Auto-generated method stub
		//定义需要获得的名称
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
		String  updates = null;
		//局和数据连接地址
		String url = "http://apis.juhe.cn/baidu/getLocate?";//urlΪ�����api�ӿ
		//密钥ڵ�ַ
		//8a1314e08909b005039bcc1f33bb3c02
		//3608a68290d1f5abc1d5101c172dc57e
		String key= "8a1314e08909b005039bcc1f33bb3c02";//����Ķ�Ӧkey
		String urlAll = new StringBuffer(url).append("key=").append(key).append("&dtype=json&lng=").append(lng).append("&lat=").append(lat).append("&r=").append(r).append("&cid=&page=&pnums=").append(pnums).toString();
		String charset ="UTF-8";
		System.out.print(urlAll);
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
		   updates="update Signal_Strength set Landmark=\""+tags+"\", province=\""+province+"\", city=\""+city+"\" where user_lon="+lng+" and user_lat="+lat;

			
		}else{
		System.out.println("error_code:"+code+",reason:"+object.getString("reason"));
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



