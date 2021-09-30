package cn.itcast.util.charts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;

public class JsonUtil {
	
	private static final String SORT_ALL = "ALL";
	private static final String SORT_NULL = "NULL";
	private static final String SORT_UNKNOWN = "unkown";
	private static final String SORT_OTHERS = "Others";
	private static final String SORT_OTHERS_CN = "其它";
	
	private static final String UNIVERSITY = "university";
	private static final String UNIVERSITY_CN = "大学";
	private static final String RESIDENTIAL = "residential";
	private static final String RESIDENTIAL_CN = "住宅区";
	private static final String HOTEL = "hotel";
	private static final String HOTEL_CN = "旅馆";
	private static final String COMMERCIAL = "commercial";
	private static final String COMMERCIAL_CN = "商业区";
	
	
	
	/**
	 * 将x轴的name中的unknown变为其它，university变为大学，commercial变为商业区
	 * @param bars
	 * @return
	 */
	public static String nameChinese(String landmark){
		if(UNIVERSITY.equals(landmark)){
			return UNIVERSITY_CN;
		}else if(RESIDENTIAL.equals(landmark)){
			return RESIDENTIAL_CN;
		}else if(HOTEL.equals(landmark)){
			return HOTEL_CN;
		}else if(COMMERCIAL.equals(landmark)){
			return COMMERCIAL_CN;
		}else if(SORT_UNKNOWN.equals(landmark)){
			return SORT_OTHERS_CN;
		}else{
			return landmark;
		}
	}
	
	/**
	 * 跟踪图（曲线图），去除data中的null项

	 */
	public static List<Series> lineSort(List<Series> lines){

		List<Series> linesNull = new ArrayList<Series>();
		for(Series<Line> l : lines){
			if(!(l.getName().equals(SORT_NULL)) && !(l.getName().equals(SORT_OTHERS))){
				linesNull.add(l);
			}
		}
		return linesNull;
	//	for(Series<Line> l : lines){
	//		if(l.getName().equals("NULL")){
	//			l.setName("Others");
	//		}
	//	}
	//	return lines;
	}
	
	/**
	 * 将z轴中的"ALL"项前置到第一位
	 * @param z_Axis
	 * @return
	 */
	public static Set<String> zSort2D(Set<String> z_Axis){
		Set<String> z_Sort = new LinkedHashSet<String>();
		//如果查询多个，个数大于2
		if(2 < z_Axis.size()){
			for(String z : z_Axis){
				if(z.equals(SORT_ALL)){
					z_Sort.add(z);
					return z_Sort;
				}
			}
		}else{
			for(String z : z_Axis){
				if(!z.equals(SORT_ALL)){
					z_Sort.add(z);
					return z_Sort;
				}
			}
		}
		return z_Axis;

	}
	public static Set<String> zSort(Set<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_ALL)){
				Set<String> z_Sort = new LinkedHashSet<String>();
				z_Sort.add(z);
				for(String zz : z_Axis){
					if(!zz.equals(SORT_ALL)){
						z_Sort.add(zz);
					}
				}
				return z_Sort;
			}
		}
		return z_Axis;
	}
	public static List<String> zSort(List<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_ALL)){
				List<String> z_Sort = new ArrayList<String>();
				z_Sort.add(z);
				for(String zz : z_Axis){
					if(!zz.equals(SORT_ALL)){
						z_Sort.add(zz);
					}
				}
				return z_Sort;
			}
		}
		return z_Axis;
	}
	
	/**
	 * 去掉null项
	 * z轴中的"NULL"项后置到最后一位，并改名为"Others"	【删除】
	 * @param z_Axis
	 * @return
	 */
	public static Set<String> zSortOthers(Set<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_NULL)){
				Set<String> z_Sort = new LinkedHashSet<String>();
				for(String zz : z_Axis){
					if(!zz.equals(SORT_NULL)){
						z_Sort.add(zz);
					}
				}
				//z_Sort.add(SORT_OTHERS);
				return z_Sort;
			}
		}
		return z_Axis;
	}
	public static List<String> zSortOthers(List<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_NULL)){
				List<String> z_Sort = new ArrayList<String>();
				z_Sort.add(z);
				for(String zz : z_Axis){
					if(!zz.equals(SORT_NULL)){
						z_Sort.add(zz);
					}
				}
				//z_Sort.add(SORT_OTHERS);
				return z_Sort;
			}
		}
		return z_Axis;
	}
	
	/**
	 * 删除Others项
	 * @param z_Axis
	 * @return
	 */
	public static Set<String> deleteOthers(Set<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_OTHERS)){
				Set<String> z_Sort = new LinkedHashSet<String>();
				for(String zz : z_Axis){
					if(!zz.equals(SORT_OTHERS)){
						z_Sort.add(zz);
					}
				}
				return z_Sort;
			}
		}
		return z_Axis;
	}
	public static List<String> deleteOthers(List<String> z_Axis){
		for(String z : z_Axis){
			if(z.equals(SORT_OTHERS)){
				List<String> z_Sort = new ArrayList<String>();
				for(String zz : z_Axis){
					if(!zz.equals(SORT_OTHERS)){
						z_Sort.add(zz);
					}
				}
				return z_Sort;
			}
		}
		return z_Axis;
	}

	/**
	 * 1.配置初始化页面时，哪个Z轴项默认不选中
	 * 2.以下方法除ALL之外都设为不选中
	 * 3.默认选中
	 * @param optionStr
	 * @param z_Axis
	 * @return
	 */
	public static String allToFirst(String optionStr,Set<String> z_Axis) {
		// "legend": {
		String legend = "\"legend\": {";
		if (optionStr.contains(legend)) {
			for (String z : z_Axis) {
				if (z.equals(SORT_ALL)) {
					String insert = "";
					for (String zz : z_Axis) {
						if (!zz.equals(SORT_ALL)) {
							insert += " \"" + zz + "\" : false,";
						}
					}
					if (insert.endsWith(",")) {
						insert = insert.substring(0, insert.length() - 1);
					}
					insert = " \"selected\" : { " + insert + " }, ";
					optionStr = optionStr.substring(0,
							optionStr.indexOf(legend) + legend.length())
							+ insert
							+ optionStr.substring(optionStr.indexOf(legend)
									+ legend.length(), optionStr.length());
					break;
				}
			}
		}
		return optionStr;
	}
	
	public static String allToFirst(String optionStr,List<String> z_Axis) {
		// "legend": {
		String legend = "\"legend\": {";
		if (optionStr.contains(legend)) {
			for (String z : z_Axis) {
				if (z.equals(SORT_ALL)) {
					String insert = "";
					for (String zz : z_Axis) {
						if (!zz.equals(SORT_ALL)) {
							insert += " \"" + zz + "\" : false,";
						}
					}
					if (insert.endsWith(",")) {
						insert = insert.substring(0, insert.length() - 1);
					}
					insert = " \"selected\" : { " + insert + " }, ";
					optionStr = optionStr.substring(0,
							optionStr.indexOf(legend) + legend.length())
							+ insert
							+ optionStr.substring(optionStr.indexOf(legend)
									+ legend.length(), optionStr.length());
					break;
				}
			}
		}
		return optionStr;
	}
	
	public static String allToFirst_List(String optionStr,ArrayList<Object> z_Axis) {
		// "legend": {
		String legend = "\"legend\": {";
		if (optionStr.contains(legend)) {
			for (Object z : z_Axis) {
				if (z.equals(SORT_ALL)) {
					String insert = "";
					for (Object zz : z_Axis) {
						if (!zz.equals(SORT_ALL)) {
							insert += " \"" + zz + "\" : false,";
						}
					}
					if (insert.endsWith(",")) {
						insert = insert.substring(0, insert.length() - 1);
					}
					insert = " \"selected\" : { " + insert + " }, ";
					optionStr = optionStr.substring(0,
							optionStr.indexOf(legend) + legend.length())
							+ insert
							+ optionStr.substring(optionStr.indexOf(legend)
									+ legend.length(), optionStr.length());
					break;
				}
			}
		}
		return optionStr;
	}
}
