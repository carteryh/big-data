/**
 *@auther:ZHL
 *@date:2015年1月16日下午3:36:12
 */
package cn.itcast.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonUtil;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.TextStyle;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.PieObject;
import cn.itcast.entity.VarDesc;
import cn.itcast.util.charts.JsonUtil;

public class EchartsUtil<E> {
	
	
	public <E> E getObject(Class<E> c) throws InstantiationException, IllegalAccessException{
		E e =c.newInstance();
		return e;
	}
	
	
	public static enum ORDER {
        //注：枚举写在最前面，否则编译出错
		ORDER;
        public static final String DESC  = "DESC";
		public static final String ASC  = "ASC";
    }
	public static enum UNIT {
        //注：枚举写在最前面，否则编译出错
		ORDER;
        public static final String AVG  = "AVG";
		public static final String SUM  = "SUM";
		public static final String DBM  = "dBm";
    }
	
	public static enum ORDERTYPE {
        //注：枚举写在最前面，否则编译出错
		ORDERTYPE;
        public static final String ALL  = "ALL";
		public static final String X_AXIS  = "X_AXIS";
		public static final String NORULE  = "NORULE";
    }
	/**
	 *@method:对list进行排序
	 *@param:List<E>带泛型的list对象；method调用泛型的那个方法；order顺序
	 *@return:返回一个通过method方法排序的一个list对象
	 */
	public  List Sort(List<E> list, final String method, final String order){
		  Collections.sort(list, new Comparator() {   
		    public int compare(Object a, Object b) {
		       int ret = 0;
		       try{
		        Method m1 = ((E)a).getClass().getMethod(method, null);
		        Method m2 = ((E)b).getClass().getMethod(method, null);
		        if(m2.getReturnType().toString().equals(String.class.toString())){
		        	if(EchartsUtil.ORDER.DESC.equals(order))
				        //倒序
				         ret = m2.invoke(((E)b), null).toString().compareTo(m1.invoke(((E)a), null).toString()); 
				        else if(EchartsUtil.ORDER.ASC.equals(order))
				        //正序
				         ret = m1.invoke(((E)a), null).toString().compareTo(m2.invoke(((E)b), null).toString());
				        else
				        System.out.println("输入的order顺序有错误，请用EchartsUtil.ORDER.DESC，或者EchartsUtil.ORDER.ASC");
				    	}else{
				    	if(EchartsUtil.ORDER.DESC.equals(order))
				    	//倒序
		                ret = Long.valueOf(m2.invoke(((E)b), null).toString()).compareTo(Long.valueOf(m1.invoke(((E)a), null).toString())); 
				    	else if(EchartsUtil.ORDER.ASC.equals(order))
				    	//正序
		               	 ret = Long.valueOf(m1.invoke(((E)b), null).toString()).compareTo(Long.valueOf(m2.invoke(((E)a), null).toString()));
				    	else
					    System.out.println("输入的order顺序有错误，请用EchartsUtil.ORDER.DESC，或者EchartsUtil.ORDER.ASC");
				    	}
		     }catch(NoSuchMethodException ne){
		     System.out.println(ne);
		    }catch(IllegalAccessException ie){
		     System.out.println(ie);
		    }catch(InvocationTargetException it){
		     System.out.println(it);
		    }
		       return ret;
		      }
		   });
		  return list;
		}
	
	/**
	 *@method:返回一个统计直方图
	 *@param:objects带泛型的list对象	
	 *@param:UnitType，y轴的单位是AVG，或者是SUM 
	 *@param:m_x_g，对应x轴clazz中的get方法名
	 *@param:m_y_g，对应y轴clazz中的get方法名
	 *@param:m_z_g，对应z轴clazz中的get方法名
	 *@return:返回一个echarts的返回类型的一个String
	 */
	public String getEchartsBar (
			List<E> objects,String barName,String sortType,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			
			if (!(x_Axis.contains(method_x_g.invoke(((E)aObject), null).toString()))) {
				x_Axis.add(method_x_g.invoke(((E)aObject), null).toString());
			}
		}
		if(ORDERTYPE.X_AXIS.equals(sortType)){
			x_Axis=Sort(new ArrayList(x_Axis), "toString",EchartsUtil.ORDER.DESC);
		}else if(ORDERTYPE.NORULE.equals(sortType)){
			
		}else if(ORDERTYPE.ALL.equals(sortType)){
			
		}
	
		// Z轴设置
		List<String> z_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			if (!(z_Axis.contains(method_z_g.invoke(((E)aObject), null).toString())))
			z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		
		z_Axis=Sort(new ArrayList(z_Axis), "toString",EchartsUtil.ORDER.DESC);
		
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
		}
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		
		Option option = new Option();
		option.title().text(barName)
				.textStyle(new TextStyle().fontSize(14));
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
			option.title().subtext("单位(" + subtext+"/S)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));

		/*
		 * 此处将z轴中的“Others”的项删除
		 */
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.deleteOthers(z_Axis));
		option.legend().setData(z_SortOthers);
		// 直角坐标系内绘图网格
		option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());
		// Z轴设置在X轴的设置
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
						&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						y_Values.add(EchartsUtil.valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			bar.setData(y_Values);
			bars.add(bar);
		}
		bars = JsonUtil.lineSort(bars);
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		System.out.println("统计直方图(通用)Json：------------\n"+optionStr);
		return optionStr;
	}
	public String getEchartsBaryanchi (
			List<E> objects,String barName,String sortType,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			
			if (!(x_Axis.contains(method_x_g.invoke(((E)aObject), null).toString()))) {
				x_Axis.add(method_x_g.invoke(((E)aObject), null).toString());
			}
		}
		if(ORDERTYPE.X_AXIS.equals(sortType)){
			x_Axis=Sort(new ArrayList(x_Axis), "toString",EchartsUtil.ORDER.DESC);
		}else if(ORDERTYPE.NORULE.equals(sortType)){
			
		}else if(ORDERTYPE.ALL.equals(sortType)){
			/*x_Axis=Sort(new ArrayList(x_Axis), m_x_g,EchartsUtil.ORDER.DESC);*/
		}
		// Z轴设置
		List<String> z_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			if (!(z_Axis.contains(method_z_g.invoke(((E)aObject), null).toString())))
				z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		z_Axis=Sort(new ArrayList(z_Axis), "toString",EchartsUtil.ORDER.DESC);
		
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
		}
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		
		Option option = new Option();
		option.title().text(barName)
		.textStyle(new TextStyle().fontSize(14));
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
//			option.title().subtext("单位(" + subtext+"/S)");
			option.title().subtext("单位(S)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		option.tooltip().trigger(Trigger.axis)
		.axisPointer(new AxisPointer().type(PointerType.shadow));
		
		/*
		 * 此处将z轴中的“Others”的项删除
		 */
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.deleteOthers(z_Axis));
		option.legend().setData(z_SortOthers);
//		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		//option.legend().setData(new ArrayList(z_Axis));
		
		// 直角坐标系内绘图网格
		option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());
		// Z轴设置在X轴的设置
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						y_Values.add(EchartsUtil.valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			bar.setData(y_Values);
			bars.add(bar);
		}
		bars = JsonUtil.lineSort(bars);
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		System.out.println("统计直方图(通用)Json：------------\n"+optionStr);
		return optionStr;
	}
	public String getEchartsBaryanchiMS (
			List<E> objects,String barName,String sortType,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			
			if (!(x_Axis.contains(method_x_g.invoke(((E)aObject), null).toString()))) {
				x_Axis.add(method_x_g.invoke(((E)aObject), null).toString());
			}
		}
		if(ORDERTYPE.X_AXIS.equals(sortType)){
			x_Axis=Sort(new ArrayList(x_Axis), "toString",EchartsUtil.ORDER.DESC);
		}else if(ORDERTYPE.NORULE.equals(sortType)){
			
		}else if(ORDERTYPE.ALL.equals(sortType)){
			/*x_Axis=Sort(new ArrayList(x_Axis), m_x_g,EchartsUtil.ORDER.DESC);*/
		}
		// Z轴设置
		List<String> z_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			if (!(z_Axis.contains(method_z_g.invoke(((E)aObject), null).toString())))
				z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		z_Axis=Sort(new ArrayList(z_Axis), "toString",EchartsUtil.ORDER.DESC);
		
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
		}
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		
		Option option = new Option();
		option.title().text(barName)
		.textStyle(new TextStyle().fontSize(14));
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
//			option.title().subtext("单位(" + subtext+"/S)");
			option.title().subtext("单位(ms)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		option.tooltip().trigger(Trigger.axis)
		.axisPointer(new AxisPointer().type(PointerType.shadow));
		
		/*
		 * 此处将z轴中的“Others”的项删除
		 */
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.deleteOthers(z_Axis));
		option.legend().setData(z_SortOthers);
		// 直角坐标系内绘图网格
		option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());
		// Z轴设置在X轴的设置
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						y_Values.add(EchartsUtil.valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			bar.setData(y_Values);
			bars.add(bar);
		}
		bars = JsonUtil.lineSort(bars);
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		System.out.println("统计直方图(通用)Json：------------\n"+optionStr);
		return optionStr;
	}
	public String getEchartsBarSST (
			List<E> objects,String barName,String sortType,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		List<String> x_AxisAllCN = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			if (!(x_Axis.contains(method_x_g.invoke(((E)aObject), null).toString()))) {
				x_Axis.add(method_x_g.invoke(((E)aObject), null).toString());
				x_AxisAllCN.add(JsonUtil.nameChinese(method_x_g.invoke(((E)aObject), null).toString()));
			}
		}
		
		if(ORDERTYPE.X_AXIS.equals(sortType)){
			x_Axis=Sort(new ArrayList(x_Axis), "toString",EchartsUtil.ORDER.DESC);
		}else if(ORDERTYPE.NORULE.equals(sortType)){
			
		}else if(ORDERTYPE.ALL.equals(sortType)){
			
		}
		
		// Z轴设置
		List<String> z_Axis = new ArrayList<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			if (!(z_Axis.contains(method_z_g.invoke(((E)aObject), null).toString())))
				z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		z_Axis=Sort(new ArrayList(z_Axis), "toString",EchartsUtil.ORDER.DESC);
		
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
		}
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		
		Option option = new Option();
		option.title().text(barName)
		.textStyle(new TextStyle().fontSize(14));
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
			option.title().subtext("单位(" + subtext+"/S)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		option.tooltip().trigger(Trigger.axis)
		.axisPointer(new AxisPointer().type(PointerType.shadow));
		
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		//option.legend().setData(new ArrayList(z_Axis));
		
		// 直角坐标系内绘图网格
		option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList(x_AxisAllCN));
		option.xAxis(categoryAxis);
		/*
		 * 将纵轴刻度设置为-120,0
		 */
		ValueAxis yV = new ValueAxis();
		yV.setMax(0);
		yV.setMin(-120);
		yV.setSplitNumber(4);
		option.yAxis(yV);
		
		// Z轴设置在X轴的设置
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						y_Values.add(EchartsUtil.valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			bar.setData(y_Values);
			bars.add(bar);
		}
		
		//将x轴的name中的unknown变为其它，university变为大学，commercial变为商业区
		//bars = JsonUtil
		//TODO
		
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		System.out.println("统计直方图(通用SST)Json：------------\n"+optionStr);
		return optionStr;
	}
	
	
	/**
	 *@method:返回一个带有All的一个统计直方图
	 *@param:clazz，对应bar的类的class
	 *@param:objects带泛型的list对象
	 *@param:UnitType，y轴的单位是AVG，或者是SUM	 
	 *@param:m_x_g，对应x轴clazz中的get方法名
	 *@param:m_x_s，对应x轴clazz中的set方法名
	 *@param:m_y_g，对应y轴clazz中的get方法名
	 *@param:m_y_s，对应y轴clazz中的set方法名
	 *@param:m_z_g，对应z轴clazz中的get方法名
	 *@param:m_z_s，对应z轴clazz中的set方法名
	 *@return:返回一个echarts的返回类型的一个String
	 */
	public  String getEchartsBarWithALL(Class clazz,List<E> objects,
			String barName,String sortType,String unitType,
			final String m_x_g,final String m_x_s,
			final String m_y_g,final String m_y_s,
			final String m_z_g,final String m_z_s) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		
		// X轴设置
		Set<String> x_AxisOld = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			x_AxisOld.add(method_x_g.invoke(((E)aObject), null).toString());
		}
		
		
		// 添加All,也就是把不同的z轴的对应x的数据起来
		for (String x : x_AxisOld) {
			long allSpeed = 0;
			for (Object  aObject: objects) {
				Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
				if (x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					allSpeed = allSpeed + Long.valueOf(method_y_g.invoke(((E)aObject), null).toString());
				}
			}
			
			
			
			//通过class类创建一个实例
			Object aOjbect = this.getObject(clazz); 
			//x轴
			Method method_x_s = ((E)aOjbect).getClass().getMethod(m_x_s, new Class[] { String.class});
			method_x_s.invoke(((E)aOjbect), x);
			//y轴
			Method method_y_s = ((E)aOjbect).getClass().getMethod(m_y_s, new Class[] { long.class});
			method_y_s.invoke(((E)aOjbect), allSpeed);
			//z轴
			Method method_z_s = ((E)aOjbect).getClass().getMethod(m_z_s, new Class[] { String.class});
			method_z_s.invoke(((E)aOjbect), VarDesc.OPERATOR.ALL);
			//想list中添加
			
			objects.add(((E)aOjbect));
		}

		// 排序操作
		objects=Sort(objects, m_y_g,EchartsUtil.ORDER.DESC);
		//把all添加进去之后，在调用不带all的方法
		return getEchartsBar(objects,barName,sortType,unitType,m_x_g,m_y_g,m_z_g);
	}

	
	/**
	 *@method:返回一个曲线图
	 *@param:objects带泛型的list对象
	 *@param:lineName，曲线图的名字
	 *@param:x_type，x轴按照月、日、小时的类型	 
	 *@param:m_x_g，对应x轴clazz中的get方法名
	 *@param:m_y_g，对应y轴clazz中的get方法名
	 *@param:m_z_g，对应z轴clazz中的get方法名
	 *@return:返回一个echarts的返回类型的一个String
	 */
	public String getEchartsLine (
			List<E> objects,String lineName,String x_type,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
		// Y轴设置
		Set<Long> y_Axis = new HashSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			Object obj=method_y_g.invoke(((E)aObject), null);
			
			if(obj instanceof Double){
				String y=method_y_g.invoke(((E)aObject), null).toString();
				y_Axis.add(Long.valueOf(y.substring(0,y.indexOf("."))));
			}else{
				y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
			}
		}
		

		
		// 单位转换
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		
		CategoryAxis categoryAxis = new CategoryAxis();
		
			//判断x轴的类型
			int xSize=0;
			String xType=null;
			if(VarDesc.AXISTYPE.HOUR.equals(x_type)){
				xType="H";
				xSize=24;
				//设置横轴单位
				categoryAxis.setName("单位(小时)");
			}else if(VarDesc.AXISTYPE.DAY.equals(x_type)){
				xType="D";
				xSize=31;
				categoryAxis.setName("单位(天)");
			}else if(VarDesc.AXISTYPE.MONTH.equals(x_type)){
				xType="M";
				xSize=12;
				categoryAxis.setName("单位(月)");
			}
			
			// X轴设置
			List<Object> x_Axis = new ArrayList<Object>();
			for (int i = 1; i <=9 ; i++) {
				x_Axis.add("0"+String.valueOf(i));
			}
			for (int j = 10; j <=xSize ; j++) {
				x_Axis.add(String.valueOf(j));
			}
			
			// Z轴设置在X轴的设置
			List<Series> lines = new ArrayList<Series>();
			// 循环z轴
			for (String z : z_Axis) {
				List<Object> y_Values = new ArrayList<Object>();
				// 创建对应的z轴的线
				//Line line = new Line(z).smooth(true).name(z).stack("总流量").symbol(Symbol.none);
				Line line = new Line(z);
				line.smooth(true).itemStyle().normal().areaStyle().typeDefault();
				// 循环x轴
				for (int i = 0; i < x_Axis.size(); i++) {
					String x = (String) x_Axis.get(i);
					boolean flag = false;
					// 查找对应的x轴和z轴的值
					for (Object  aObject: objects) {
						Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
						Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
						Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
						if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
								&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
							Object obj=method_y_g.invoke(((E)aObject), null);
							if(obj instanceof Double){
								String y=method_y_g.invoke(((E)aObject), null).toString();
								y_Values.add(valueConversion(Long.valueOf(y.substring(0,y.indexOf("."))),times));
								}else{
								y_Values.add(valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
							}
							flag = true;
						}
					}
					// 对应的y值没找到，添加0占位值
					if (!flag) {
						y_Values.add(0);
					}
				}
				line.setData(y_Values);
				lines.add(line);
			}
			Option option = new Option();
			
			if(unitType.equals(EchartsUtil.UNIT.SUM)){
				option.title().text(lineName).subtext("单位(" + subtext+")");
			}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
				option.title().text(lineName).subtext("单位(" + subtext+"/S)");
			}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
				option.title().text(lineName).subtext("单位(" + EchartsUtil.UNIT.DBM+")");
			}
			
			
		    option.tooltip().trigger(Trigger.axis);
		    
			/*
			 * 将ALL和Others项排序
			 */
		    //option.legend().data(new ArrayList(z_Axis));
			Set<String> z_SortAll = JsonUtil.zSort(z_Axis);
			List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.zSortOthers(z_SortAll));
			option.legend().setData(z_SortOthers);
			/*
			 * 将z轴的Others名称覆盖掉lines里的NULL
			 */
			lines = JsonUtil.lineSort(lines);
			
		    option.calculable(true);
			
			categoryAxis.boundaryGap(false);
			categoryAxis.setData(x_Axis);
			
			option.xAxis(categoryAxis);
			
		    option.yAxis(new ValueAxis());
			
		    option.series(lines);
		
		String optionStr = GsonUtil.format(option);
		System.out.println("通用曲线图json:---------------------\n"+optionStr);
		
		return optionStr;
	}
	public String getEchartsLineDW (
			List<E> objects,String lineName,String x_type,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
		// Y轴设置
		Set<Long> y_Axis = new HashSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			Object obj=method_y_g.invoke(((E)aObject), null);
			
			if(obj instanceof Double){
				String y=method_y_g.invoke(((E)aObject), null).toString();
				y_Axis.add(Long.valueOf(y.substring(0,y.indexOf("."))));
			}else{
				y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
			}
		}
		
		
		// 单位转换
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		
		CategoryAxis categoryAxis = new CategoryAxis();
		
		//判断x轴的类型
		int xSize=0;
		String xType=null;
		if(VarDesc.AXISTYPE.HOUR.equals(x_type)){
			xType="H";
			xSize=24;
			//设置横轴单位
			categoryAxis.setName("单位(小时)");
		}else if(VarDesc.AXISTYPE.DAY.equals(x_type)){
			xType="D";
			xSize=31;
			categoryAxis.setName("单位(天)");
		}else if(VarDesc.AXISTYPE.MONTH.equals(x_type)){
			xType="M";
			xSize=12;
			categoryAxis.setName("单位(月)");
		}
		
		// X轴设置
		List<Object> x_Axis = new ArrayList<Object>();
		for (int i = 1; i <=9 ; i++) {
			x_Axis.add("0"+String.valueOf(i));
		}
		for (int j = 10; j <=xSize ; j++) {
			x_Axis.add(String.valueOf(j));
		}
		
		// Z轴设置在X轴的设置
		List<Series> lines = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的线
			//Line line = new Line(z).smooth(true).name(z).stack("总流量").symbol(Symbol.none);
			Line line = new Line(z);
			line.smooth(true).itemStyle().normal().areaStyle().typeDefault();
			// 循环x轴
			for (int i = 0; i < x_Axis.size(); i++) {
				String x = (String) x_Axis.get(i);
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						Object obj=method_y_g.invoke(((E)aObject), null);
						if(obj instanceof Double){
							String y=method_y_g.invoke(((E)aObject), null).toString();
							y_Values.add(valueConversion(Long.valueOf(y.substring(0,y.indexOf("."))),times));
						}else{
							y_Values.add(valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						}
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			line.setData(y_Values);
			lines.add(line);
		}
		Option option = new Option();
		
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().text(lineName).subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
			option.title().text(lineName).subtext("单位(S)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().text(lineName).subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		
		option.tooltip().trigger(Trigger.axis);
		
		/*
		 * 将ALL和Others项排序
		 */
		//option.legend().data(new ArrayList(z_Axis));
		Set<String> z_SortAll = JsonUtil.zSort(z_Axis);
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.zSortOthers(z_SortAll));
		option.legend().setData(z_SortOthers);
		/*
		 * 将z轴的Others名称覆盖掉lines里的NULL
		 */
		lines = JsonUtil.lineSort(lines);
		
		option.calculable(true);
		
		categoryAxis.boundaryGap(false);
		categoryAxis.setData(x_Axis);
		
		option.xAxis(categoryAxis);
		
		option.yAxis(new ValueAxis());
		
		option.series(lines);
		
		String optionStr = GsonUtil.format(option);
		System.out.println("通用曲线图json:---------------------\n"+optionStr);
		
		return optionStr;
	}
	public String getEchartsLineDWMS (
			List<E> objects,String lineName,String x_type,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
		// Y轴设置
		Set<Long> y_Axis = new HashSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			Object obj=method_y_g.invoke(((E)aObject), null);
			
			if(obj instanceof Double){
				String y=method_y_g.invoke(((E)aObject), null).toString();
				y_Axis.add(Long.valueOf(y.substring(0,y.indexOf("."))));
			}else{
				y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
			}
		}

		// 单位转换
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		
		CategoryAxis categoryAxis = new CategoryAxis();
		
		//判断x轴的类型
		int xSize=0;
		String xType=null;
		if(VarDesc.AXISTYPE.HOUR.equals(x_type)){
			xType="H";
			xSize=24;
			//设置横轴单位
			categoryAxis.setName("单位(小时)");
		}else if(VarDesc.AXISTYPE.DAY.equals(x_type)){
			xType="D";
			xSize=31;
			categoryAxis.setName("单位(天)");
		}else if(VarDesc.AXISTYPE.MONTH.equals(x_type)){
			xType="M";
			xSize=12;
			categoryAxis.setName("单位(月)");
		}
		
		// X轴设置
		List<Object> x_Axis = new ArrayList<Object>();
		for (int i = 1; i <=9 ; i++) {
			x_Axis.add("0"+String.valueOf(i));
		}
		for (int j = 10; j <=xSize ; j++) {
			x_Axis.add(String.valueOf(j));
		}
		
		// Z轴设置在X轴的设置
		List<Series> lines = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的线
			//Line line = new Line(z).smooth(true).name(z).stack("总流量").symbol(Symbol.none);
			Line line = new Line(z);
			line.smooth(true).itemStyle().normal().areaStyle().typeDefault();
			// 循环x轴
			for (int i = 0; i < x_Axis.size(); i++) {
				String x = (String) x_Axis.get(i);
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						Object obj=method_y_g.invoke(((E)aObject), null);
						if(obj instanceof Double){
							String y=method_y_g.invoke(((E)aObject), null).toString();
							y_Values.add(valueConversion(Long.valueOf(y.substring(0,y.indexOf("."))),times));
						}else{
							y_Values.add(valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()),times));
						}
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(0);
				}
			}
			line.setData(y_Values);
			lines.add(line);
		}
		Option option = new Option();
		
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().text(lineName).subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
			option.title().text(lineName).subtext("单位(ms)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().text(lineName).subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		
		option.tooltip().trigger(Trigger.axis);
		
		/*
		 * 将ALL和Others项排序
		 */
		//option.legend().data(new ArrayList(z_Axis));
		Set<String> z_SortAll = JsonUtil.zSort(z_Axis);
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.zSortOthers(z_SortAll));
		option.legend().setData(z_SortOthers);
		/*
		 * 将z轴的Others名称覆盖掉lines里的NULL
		 */
		lines = JsonUtil.lineSort(lines);
		
		option.calculable(true);
		
		categoryAxis.boundaryGap(false);
		categoryAxis.setData(x_Axis);
		
		option.xAxis(categoryAxis);
		
		option.yAxis(new ValueAxis());
		
		option.series(lines);
		
		String optionStr = GsonUtil.format(option);
		System.out.println("通用曲线图json:---------------------\n"+optionStr);
		
		return optionStr;
	}
	/**
	 * 典型地标信号强度跟踪曲线图（强制纵轴刻度）
	 * @param objects
	 * @param lineName
	 * @param x_type
	 * @param unitType
	 * @param m_x_g
	 * @param m_y_g
	 * @param m_z_g
	 */
	public String getEchartsLineSST (
			List<E> objects,String lineName,String x_type,String unitType,
			final String m_x_g,
			final String m_y_g,
			final String m_z_g) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		
		// Y轴设置
		Set<Long> y_Axis = new HashSet<Long>();
		for (Object  aObject: objects) {
			Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
			Object obj=method_y_g.invoke(((E)aObject), null);
			
			if(obj instanceof Double){
				String y=method_y_g.invoke(((E)aObject), null).toString();
				y_Axis.add(Long.valueOf(y.substring(0,y.indexOf("."))));
			}else{
				y_Axis.add(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString()));
			}
		}
		
		
		
		// 单位转换
		int times = devidedTimes(Collections.max(y_Axis));
		String subtext = unitConversion(times);
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
			z_Axis.add(method_z_g.invoke(((E)aObject), null).toString());
		}
		
		CategoryAxis categoryAxis = new CategoryAxis();
		
		//判断x轴的类型
		int xSize=0;
		String xType=null;
		if(VarDesc.AXISTYPE.HOUR.equals(x_type)){
			xType="H";
			xSize=24;
			//设置横轴单位
			categoryAxis.setName("单位(小时)");
		}else if(VarDesc.AXISTYPE.DAY.equals(x_type)){
			xType="D";
			xSize=31;
			categoryAxis.setName("单位(天)");
		}else if(VarDesc.AXISTYPE.MONTH.equals(x_type)){
			xType="M";
			xSize=12;
			categoryAxis.setName("单位(月)");
		}
		
		// X轴设置
		List<Object> x_Axis = new ArrayList<Object>();
		for (int i = 1; i <=9 ; i++) {
			x_Axis.add("0"+String.valueOf(i));
		}
		for (int j = 10; j <=xSize ; j++) {
			x_Axis.add(String.valueOf(j));
		}
		
		// Z轴设置在X轴的设置
		List<Series> lines = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的线
			Line line = new Line(z);
			//是否显示阴影
			line.smooth(true);
			// 循环x轴
			for (int i = 0; i < x_Axis.size(); i++) {
				String x = (String) x_Axis.get(i);
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (Object  aObject: objects) {
					Method method_z_g = ((E)aObject).getClass().getMethod(m_z_g, null);
					Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
					Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
					if (z.equals(method_z_g.invoke(((E)aObject), null).toString())
							&& x.equals(method_x_g.invoke(((E)aObject), null).toString())) {
						Object obj=method_y_g.invoke(((E)aObject), null);
						if(obj instanceof Double){
							String y=method_y_g.invoke(((E)aObject), null).toString();
							y_Values.add(valueConversion(Long.valueOf(y.substring(0,y.indexOf(".")))-120,times));
						}else{
							y_Values.add(valueConversion(Long.valueOf(method_y_g.invoke(((E)aObject), null).toString())-120,times));
						}
						flag = true;
					}
				}
				// 对应的y值没找到，添加0占位值
				if (!flag) {
					y_Values.add(-120);
				}
			}
			line.setData(y_Values);
			lines.add(line);
		}
		Option option = new Option();
		
		if(unitType.equals(EchartsUtil.UNIT.SUM)){
			option.title().text(lineName).subtext("单位(" + subtext+")");
		}else if(unitType.equals(EchartsUtil.UNIT.AVG)){
			option.title().text(lineName).subtext("单位(" + subtext+"/S)");
		}else if(unitType.equals(EchartsUtil.UNIT.DBM)){
			option.title().text(lineName).subtext("单位(" + EchartsUtil.UNIT.DBM+")");
		}
		
		
		option.tooltip().trigger(Trigger.axis);
		
		/*
		 * 将ALL和Others项排序
		 */
		Set<String> z_SortAll = JsonUtil.zSort(z_Axis);
		List<Object> z_SortOthers = new ArrayList<Object>(JsonUtil.zSortOthers(z_SortAll));
		option.legend().setData(z_SortOthers);
		/*
		 * 将z轴的Others名称覆盖掉lines里的NULL
		 */
		lines = JsonUtil.lineSort(lines);
		
		option.calculable(true);
		
		categoryAxis.boundaryGap(false);
		categoryAxis.setData(x_Axis);
		
		//将x轴坐标刻度移动到上面，默认下面
		categoryAxis.position("top");
		
		option.xAxis(categoryAxis);
		
		/*
		 * 将纵轴刻度设置为-120,0
		 */
		ValueAxis yV = new ValueAxis();
		yV.setMax(0);
		yV.setMin(-120);
		yV.setSplitNumber(4);
		option.yAxis(yV);
		
		option.series(lines);
		
		String optionStr = GsonUtil.format(option);
		System.out.println("典型地标信号强度跟踪曲线图json:---------------------\n"+optionStr);
		
		return optionStr;
	}
	
	/**
	 *@method:返回一个饼图
	 *@param:objects带泛型的list对象
	 *@param:m_x_g，对应x轴clazz中的get方法名
	 *@param:m_y_g，对应y轴clazz中的get方法名
	 *@return:返回一个echarts的返回类型的一个String
	 */
	public String getEchartsPie (List<E> objects,final String m_x_g,final String m_y_g,long allSpeed) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// X轴设置
		Set<String> x_data = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			x_data.add(method_x_g.invoke(((E)aObject), null).toString());
		}
		//设置X轴对应的值
		List<Object> x_Values=new ArrayList<Object>();
		long  all_sum=0;
		for (String x : x_data) {
			long x_sum=0;
			PieObject pieObject =new PieObject();
			for (Object  aObject: objects) {
				Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
				Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
				if(x.equals(method_x_g.invoke(((E)aObject), null).toString())){
					x_sum=x_sum+Long.valueOf(method_y_g.invoke(((E)aObject), null).toString());
				}
			}
			pieObject.setName(x);
			pieObject.setValue(x_sum);
			all_sum=all_sum+x_sum;
			x_Values.add(pieObject);
		}
		
		
		List x_Axis=new ArrayList(x_data);
		x_Axis.add("others");
		PieObject pieObject =new PieObject();
		pieObject.setName("others");
		pieObject.setValue((allSpeed-all_sum));
		x_Values.add(pieObject);
		
		Option option = new Option();
		option.tooltip().trigger(Trigger.item).formatter("{b} : {d}%");
		option.title().textStyle(new TextStyle().fontSize(14)).x("center");
		option.legend().orient(Orient.vertical).x("left").data(x_Axis);
		option.calculable(true);
		Pie pie = new Pie();
		pie.center("55%", "50%").radius("50%");
		//pie.center("50%", "60%").radius("30%");
		pie.setData(x_Values);
		option.series(pie);
		String pieStr = GsonUtil.format(option);
		return pieStr;
	}
	public String getEchartsPieNoOthers (List<E> objects,final String m_x_g,final String m_y_g,long allSpeed) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// X轴设置
		Set<String> x_data = new HashSet<String>();
		for (Object  aObject: objects) {
			Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
			x_data.add(method_x_g.invoke(((E)aObject), null).toString());
		}
		//设置X轴对应的值
		List<Object> x_Values=new ArrayList<Object>();
		long  all_sum=0;
		for (String x : x_data) {
			long x_sum=0;
			PieObject pieObject =new PieObject();
			for (Object  aObject: objects) {
				Method method_x_g = ((E)aObject).getClass().getMethod(m_x_g, null);
				Method method_y_g = ((E)aObject).getClass().getMethod(m_y_g, null);
				if(x.equals(method_x_g.invoke(((E)aObject), null).toString())){
					x_sum=x_sum+Long.valueOf(method_y_g.invoke(((E)aObject), null).toString());
				}
			}
			pieObject.setName(x);
			pieObject.setValue(x_sum);
			all_sum=all_sum+x_sum;
			x_Values.add(pieObject);
		}
		
		
		List x_Axis=new ArrayList(x_data);
		Option option = new Option();
		option.tooltip().trigger(Trigger.item).formatter("{b} : {d}%");
		option.title().textStyle(new TextStyle().fontSize(14)).x("center");
		option.legend().orient(Orient.vertical).x("left").data(x_Axis);
		option.calculable(true);
		Pie pie = new Pie();
		pie.center("55%", "50%").radius("50%");
		//pie.center("50%", "60%").radius("30%");
		pie.setData(x_Values);
		option.series(pie);
		String pieStr = GsonUtil.format(option);
		return pieStr;
	}
	
	/*
	 * 单位换算(值)
	 */
	public static long valueConversion(long v, int times) {
		for (int i = 0; i < times; i++) {
			v = v / 1024;
		}
		return v;
	}

	/**
	 * @description:单位换算，计算整除的次数
	 */
	public static int devidedTimes(long maxValue) {
		int times = 0;
		while ((maxValue / 1024) > 1024) {
			maxValue = maxValue / 1024;
			times++;
		}
		if (times > 6) {
			times = 6;
			return times;
		}
		return times;
	}

	/*
	 * 单位换算(单位)
	 */
	public static String unitConversion(int times) {
		String unit = null;
		if (times >= 0 && times < 1) {
			unit = "KB";
		} else if (times >= 1 && times < 2) {
			unit = "MB";
		} else if (times >= 2 && times < 3) {
			unit = "GB";
		} else if (times >= 3 && times < 4) {
			unit = "TB";
		} else if (times >= 4 && times < 5) {
			unit = "PB";
		} else if (times >= 5 && times < 6) {
			unit = "EB";
		}
		return unit;
	}

	
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		String m_y_g="getApp_name";
		AppTraffic ss=new AppTraffic();
		Method method_y_g = ss.getClass().getMethod(m_y_g, null);
		Object obj=method_y_g.invoke((ss), null);
		System.out.println(obj);

	}
	
	
	
}
