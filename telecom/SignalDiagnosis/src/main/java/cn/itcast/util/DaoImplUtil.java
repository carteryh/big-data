/**
 *@auther:ZHL
 *@date:2015年1月16日下午1:37:35
 */
package cn.itcast.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.itcast.entity.VarDesc;

public class DaoImplUtil<E> {
	
	final static String AND="AND";
	
	/**
 	 *@method:从数据库获取前几行数据
	 *@param:Object[]占位符的参数topRow获取前几行数据new Object[] {beginDate,endDate}
	 *@param:sqlfield数据库对应的字段
	 *@return:返回值是一个sql中的in的内容的一个字符串
 	 */
	public static String getSqlInVal(JdbcTemplate jdbcTemplate,String sql,Object[] sqlVar,final String sqlfield) {
 
		String sqlInVal="";
		List<String> tops = jdbcTemplate.query(sql,sqlVar,new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(sqlfield);
					}
				});
 		for (int i = 0; i <tops.size(); i++) {
				if((tops.size()-1)==i)
					sqlInVal=sqlInVal+" '"+tops.get(i)+"' ";
				else
					sqlInVal=sqlInVal+" '"+tops.get(i)+"', ";	
			}
		if(!sqlInVal.equals("")){
			sqlInVal= " AND "+sqlfield+" IN ("+sqlInVal+")";
		}
		
		return sqlInVal;
	}
	/**
 	 *@method:拼接where条件，要放在group的前面
	 *@param:Map<String,String>前一个String是key也就是对应的是数据库的字段，
	 *第二个String是value也就是等号之后的值
	 *@return:返回值是一个sql中的where的字符串
 	 */
	public static String getSqlWhere(Map<String,String> db_v){
		String  where="";
		Set<String> key = db_v.keySet();
		for (String k : key) {
			String v=db_v.get(k);
			if(v.equals(VarDesc.ALL)){
			where+="";	
			}else{
			where+=(" "+k+" = '"+v+"'"+" "+DaoImplUtil.AND);
			}
		}
		if(!where.equals("")){
			if(where.substring(where.length()-3,where.length()).equals(DaoImplUtil.AND)){
				where=where.substring(0,where.length()-3);
			}
			where=DaoImplUtil.AND+" "+where;
		}
		return where;
	}
	
	
	public static void main(String[] args) {
		Map<String,String> test= new HashMap<String,String>();
		test.put("network_type", "ALL");
		test.put("network_name", "ALL");
	System.out.println(getSqlWhere(test));	
	}
	
}
