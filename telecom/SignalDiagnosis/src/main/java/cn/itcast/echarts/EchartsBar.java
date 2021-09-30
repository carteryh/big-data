package cn.itcast.echarts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.itcast.entity.NWQuality;
import cn.itcast.entity.SignalStrength;
import cn.itcast.util.EchartsUtil;
import org.springframework.stereotype.Component;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonUtil;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.TextStyle;
import cn.itcast.entity.AppTraffic;
import cn.itcast.entity.DataConnection;
import cn.itcast.entity.VarDesc;
import cn.itcast.util.charts.JsonUtil;

@Component
public class EchartsBar {
	/**
	 *  网络质量统计图
 	 */
	public String networkQualityStatistics(List<NWQuality> nwQualitys, String speedType) {
		String option = null;
		
		String reportName=null;
		if(speedType.equals(VarDesc.SPEEDTYPE.DLSPEED))
			reportName="网络速率(下行)";
		else if(speedType.equals(VarDesc.SPEEDTYPE.ULSPEED))
			reportName="网络速率(上行)";
		else if(speedType.equals(VarDesc.SPEEDTYPE.LATENCY))
			reportName="网络延迟";
		else
			reportName="网络速率统计图";
			
		try {
			EchartsUtil<NWQuality> echartsUtil = new EchartsUtil<NWQuality>();
			if(speedType.equals(VarDesc.SPEEDTYPE.LATENCY)){
				option = echartsUtil.getEchartsBaryanchiMS(nwQualitys, reportName,
				EchartsUtil.ORDERTYPE.X_AXIS, EchartsUtil.UNIT.AVG,
				"getNwType", "getSpeed", "getNwOperator");
			}else{
				option = echartsUtil.getEchartsBar(nwQualitys, reportName,
				EchartsUtil.ORDERTYPE.X_AXIS, EchartsUtil.UNIT.AVG,
				"getNwType", "getSpeed", "getNwOperator");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}

	/**
	 *  手机OS流量统计图
 	 */
	public String hotTelOsStatistics(List<AppTraffic> appTraffics) {
		String option = null;
		try {
			EchartsUtil<AppTraffic> echartsUtil = new EchartsUtil<AppTraffic>();
			/*
			 * @param:clazz，对应bar的类的class
			 * 
			 * @param:objects带泛型的list对象
			 * 
			 * @param:orderType按照什么进行排序一般为有all，或者是x轴，没有规则noRule就是按照数据库查出来的顺序
			 * 
			 * @param:UnitType，y轴的单位是AVG，或者是SUM
			 * 
			 * @param:m_x_g，对应x轴clazz中的get方法名
			 * 
			 * @param:m_x_s，对应x轴clazz中的set方法名
			 * 
			 * @param:m_y_g，对应y轴clazz中的get方法名
			 * 
			 * @param:m_y_s，对应y轴clazz中的set方法名
			 * 
			 * @param:m_z_g，对应z轴clazz中的get方法名
			 * 
			 * @param:m_z_s，对应z轴clazz中的set方法名
			 */

			option = echartsUtil.getEchartsBarWithALL(
					AppTraffic.class, appTraffics,
					"手机OS流量排名", EchartsUtil.ORDERTYPE.ALL,
					EchartsUtil.UNIT.SUM, "getOsAndVersion", "setOsAndVersion",
					"getSpeed", "setSpeed", "getNetworkType", "setNetworkType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}

	/**
	 *  热门手机统计图
 	 */
	public String hotAppTrafficStatistics(List<AppTraffic> appTraffics) {
		// 已被取消
		return null;
	}

	
	/**
	 *  终端流量排名图
 	 */
	public String terminalTrafficRateRank(List<NWQuality> nwQualities) {
		// 已被取消
		return null;
	}


	public String signalStrengthStatistics(List<SignalStrength> signalStrengths) {

	 
		String option = null;
		try {
			EchartsUtil<SignalStrength> echartsUtil = new EchartsUtil<SignalStrength>();
			/*
			 * @param:clazz，对应bar的类的class
			 * 
			 * @param:objects带泛型的list对象
			 * 
			 * @param:orderType按照什么进行排序一般为有all，或者是x轴，没有规则noRule就是按照数据库查出来的顺序
			 * 
			 * @param:UnitType，y轴的单位是AVG，或者是SUM
			 * 
			 * @param:m_x_g，对应x轴clazz中的get方法名
			 * 
			 * @param:m_x_s，对应x轴clazz中的set方法名
			 * 
			 * @param:m_y_g，对应y轴clazz中的get方法名
			 * 
			 * @param:m_y_s，对应y轴clazz中的set方法名
			 * 
			 * @param:m_z_g，对应z轴clazz中的get方法名
			 * 
			 * @param:m_z_s，对应z轴clazz中的set方法名
			 */
			option = echartsUtil.getEchartsBarSST(signalStrengths,
					"典型地标信号强度统计图", EchartsUtil.ORDERTYPE.X_AXIS,
					EchartsUtil.UNIT.DBM, "getLandmark", "getEchart_rssi",
					"getNetworktype");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}
	/**
	 *  网络速率排名图
 	 */
	public String networkRateRank(List<NWQuality> nwQualities) {
		// X轴设置
		Set<String> x_Axis_old = new TreeSet<String>();
		for (NWQuality awquality : nwQualities) {
			x_Axis_old.add(awquality.getProvince());
		}
		
		List<NWQuality> avgList=new ArrayList<NWQuality>();
		// 添加All，也就是把不同的z轴对应x数据相加
				for (String x : x_Axis_old) {
					long allSpeed = 0;
					int z_size=0;
					for (NWQuality nwQuality : nwQualities) {
						if (x.equals(nwQuality.getProvince())) {
							z_size++;
							allSpeed = allSpeed + nwQuality.getSpeed();
						}
					}
					allSpeed=allSpeed/z_size;
					NWQuality nwQuality = new NWQuality();
					nwQuality.setSpeed(allSpeed);
					nwQuality.setProvince(x);
					nwQuality.setNwOperator(VarDesc.OPERATOR.ALL);
					nwQualities.add(nwQuality);
					avgList.add(nwQuality);
				}

				NWQualityComparatorSpeed comparatorSpeed = new NWQualityComparatorSpeed();
				Collections.sort(avgList, comparatorSpeed);
				
				
				
				//重新设置x轴
				List<String> x_Axis = new ArrayList<String>();
				for (NWQuality nwQuality : avgList) {
					x_Axis.add(nwQuality.getProvince());
				}
				
				
				// Z轴设置
				Set<String> z_Axis_old = new HashSet<String>();
				for (NWQuality awquality : nwQualities) {
 
					z_Axis_old.add(awquality.getNwOperator());
				}
				
				//如果只有CMCC，CTCC，CUCC中的一项，则去掉z轴的ALL项
				Set<String> z_Axis_all = new HashSet<String>();
				if(2 == z_Axis_old.size()){
					for(String z : z_Axis_old){
						if(! VarDesc.OPERATOR.ALL.equals(z)){
							z_Axis_all.add(z);
						}
					}
				}else{
					z_Axis_all = z_Axis_old;
				}
				
				//给z轴重新进行排序
				ArrayList<Object> z_Axis=(ArrayList) new EchartsUtil<NWQuality>().Sort(new ArrayList(z_Axis_all), "toString",EchartsUtil.ORDER.ASC);
		
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (NWQuality awquality : nwQualities) {
			y_Axis.add(awquality.getSpeed());
		}
	
		
		int times = EchartsUtil.devidedTimes(Collections.max(y_Axis));
		String subtext = EchartsUtil.unitConversion(times);

		Option option = new Option();
		option.title().text("网络速率排名").subtext("(单位" + subtext+"/S)");
		option.tooltip().trigger(Trigger.axis).axisPointer(new AxisPointer().type(PointerType.shadow));
		option.legend().setData(z_Axis);
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());
		// Z轴设置在X轴的设置
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (Object z : z_Axis) {
			List<Object> y_Values = new ArrayList<Object>();
			// 创建对应的z轴的bar
			Bar bar = new Bar((String) z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应的x轴和z轴的值
				for (NWQuality awquality : nwQualities) {
					if (z.equals(awquality.getNwOperator())&& x.equals(awquality.getProvince())) {
						y_Values.add(EchartsUtil.valueConversion(awquality.getSpeed(),times));
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
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		
		optionStr = JsonUtil.allToFirst_List(optionStr, z_Axis);
		return optionStr;
	}

	// /////////////////////////////////////下面的方法已经被封装到了EchartsUtil中//////////////////////////////////////////////////
	/**
	 * NetworkType比较器
	 */
	class Z_AxisComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			String s1 = (String) arg0;
			String s2 = (String) arg1;
			return s2.compareTo(s1);
		}
	}

	/**
	 * 排序规则（继承Comparator接口，重写compare()方法）
	 */
	public class AppTrafficComparator implements Comparator<AppTraffic> {
		@Override
		public int compare(AppTraffic a1, AppTraffic a2) {
			Long speed1 = a1.getSpeed();
			Long speed2 = a2.getSpeed();
			return speed2.compareTo(speed1);
		}

	}

	// /////////////////////////////////////////上面的方法已经被封装到了EchartsUtil中//////////////////////////////////////////////


	// 连接点数排名图
	public String TestOptionRank(List<NWQuality> nwQualities) {
		Option option = new Option();
		option.title().text("连接点数排名")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().show(true);
		CategoryAxis categoryAxis = new CategoryAxis();
		List<Object> provinces = new ArrayList<Object>();
		List<Object> counts = new ArrayList<Object>();
		for (NWQuality nwQuality : nwQualities) {
			provinces.add(nwQuality.getProvince());
			counts.add(nwQuality.gettCount());
		}
		categoryAxis.setData(provinces);
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		Bar bar = new Bar();
		bar.setData(counts);

		option.series(bar);
		String optionStr = GsonUtil.format(option);
		return optionStr;

	}

	// 数据连接成功率统计图
	public String DataConnectionSStatistics(List<DataConnection> dataConns) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (DataConnection dataConn : dataConns) {
			z_Axis.add(dataConn.getNetwork_name());
		}
		// X轴设置
		Set<String> x_Axis = new TreeSet<String>();
		for (DataConnection dataConn : dataConns) {
			x_Axis.add(dataConn.getNetwork_type());
		}
		// Y轴设置
		DecimalFormat df = new DecimalFormat("######0.00");

		Option option = new Option();
		option.title().text("数据连接率统计图").subtext("单位（%）")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		option.legend().setData(new ArrayList<Object>(z_Axis));
		// 设置鼠标不能拖动
		option.calculable(false);
		CategoryAxis categoryAxis = new CategoryAxis();
		// 设置X轴的值
		categoryAxis.setData(new ArrayList<Object>(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应x轴和轴的值
				for (DataConnection dataConn : dataConns) {
					if (z.equals(dataConn.getNetwork_name())
							&& x.equals(dataConn.getNetwork_type())) {
						y_values.add(Double.parseDouble(df.format(dataConn
								.getD_percent())));
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
		}
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		System.out.println("数据连接成功率统计图Json: "+optionStr);
		return optionStr;
	}

	// App流量排名图
	public String AppRateRank(List<AppTraffic> appTraffics) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		z_Axis.add(VarDesc.NETWORKTYPE.ALL);
		for (AppTraffic appTraffic : appTraffics) {
			z_Axis.add(appTraffic.getNetworkType());
		}
		// X轴设置
		Set<String> x_AxisOld = new TreeSet<String>();
		for (AppTraffic appTraffic : appTraffics) {
			x_AxisOld.add(appTraffic.getApp_name());

		}

		// 添加All,也就是把不同的z轴的对应x的数据起来
		for (String x : x_AxisOld) {
			long allSpeed = 0;
			for (AppTraffic appTraffic : appTraffics) {
				if (x.equals(appTraffic.getApp_name())) {
					allSpeed = allSpeed + appTraffic.getSpeed();
				}

			}
			AppTraffic appTraffic = new AppTraffic();
			appTraffic.setApp_name(x);
			appTraffic.setSpeed(allSpeed);
			appTraffic.setNetworkType(VarDesc.NETWORKTYPE.ALL);
			appTraffics.add(appTraffic);
		}
		// 排序操作
		AppTrafficComparator aComparator = new AppTrafficComparator();
		Collections.sort(appTraffics, aComparator);

		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		for (AppTraffic appTraffic : appTraffics) {
			if (!(x_Axis.contains(appTraffic.getApp_name()))) {
				x_Axis.add(appTraffic.getApp_name());
			}
			if (x_Axis.size() >= 5) {
				break;
			}
		}
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (AppTraffic appTraffic : appTraffics) {
			y_Axis.add(appTraffic.getSpeed());
		}
		int times = EchartsUtil.devidedTimes(Collections.max(y_Axis));
		String subtext = EchartsUtil.unitConversion(times);

		Option option = new Option();
		option.title().text("热门App流量排名").subtext("单位（"+subtext+"）")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		//option.legend().setData(new ArrayList<Object>(z_Axis));
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		// option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		// 设置x轴的值
		categoryAxis.setData(new ArrayList<Object>(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();

		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应x轴和z轴的值
				for (AppTraffic appTraffic : appTraffics) {
					if (z.equals(appTraffic.getNetworkType())
							&& x.equals(appTraffic.getApp_name())) {
						y_values.add(EchartsUtil.valueConversion(
								appTraffic.getSpeed(), times));
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
			
		}
		
		for(Series<Bar> b : bars){
			if(b.getName().equals("ALL")){
				List<Series> bars_Sort = new ArrayList<Series>();
				bars_Sort.add(b);
				for(Series<Bar> bb : bars){
					if(!bb.getName().equals("ALL")){
						bars_Sort.add(bb);
					}
				}
				bars = bars_Sort;
			}
		}
		
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		
		//z轴初始默认只显示ALL项
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		System.out.println("App流量排名Json：------------\n"+optionStr);
		
		return optionStr;
	}

	// 热门手机流量排名(图)
	public String terminalStatistics(List<AppTraffic> appTraffics, int dayCount) {

		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		z_Axis.add(VarDesc.NETWORKTYPE.ALL);
		for (AppTraffic appTraffic : appTraffics) {
			z_Axis.add(appTraffic.getNetworkType());
		}
		// X轴设置
		Set<String> x_Axis = new TreeSet<String>();
		for (AppTraffic appTraffic : appTraffics) {
			x_Axis.add(appTraffic.getCompanyModel());
		}

		// 添加All，也就是把不同的z轴对应x数据相加
		for (String x : x_Axis) {
			long allSpeed = 0;
			for (AppTraffic appTraffic : appTraffics) {
				if (x.equals(appTraffic.getCompanyModel())) {
					allSpeed = allSpeed + appTraffic.getSpeed();
				}
			}
			AppTraffic appTraffic = new AppTraffic();
			appTraffic.setCompanyModel(x);
			appTraffic.setSpeed(allSpeed);
			appTraffic.setNetworkType(VarDesc.NETWORKTYPE.ALL);
			appTraffics.add(appTraffic);
		}
		// 排序操作
		AppTrafficComparator nComparator = new AppTrafficComparator();
		Collections.sort(appTraffics, nComparator);
		// 排序之后，重新设置x轴
		List<String> x_AxisAll = new ArrayList<String>();
		for (AppTraffic appTraffic : appTraffics) {
			if (!(x_AxisAll.contains(appTraffic.getCompanyModel()))) {
				x_AxisAll.add(appTraffic.getCompanyModel());
			}
			if (x_AxisAll.size() >= 5) {
				break;
			}
		}

		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (AppTraffic appTraffic : appTraffics) {
			y_Axis.add(appTraffic.getSpeed());
		}
		int times = EchartsUtil.devidedTimes(Collections.max(y_Axis));
		String subtext = EchartsUtil.unitConversion(times);
		Option option = new Option();
		option.title().text("热门手机流量排名").subtext("单位（"+subtext+")").itemGap(10)
				.textStyle(new TextStyle().fontSize(14));
		
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		//option.legend().setData(new ArrayList<Object>(z_Axis));
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList<Object>(x_AxisAll));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_AxisAll) {
				boolean flag = false;
				// 查找对应x轴和轴的值
				for (AppTraffic appTraffic : appTraffics) {
					if (z.equals(appTraffic.getNetworkType())
							&& x.equals(appTraffic.getCompanyModel())) {
						y_values.add(EchartsUtil.valueConversion(appTraffic.getSpeed() / dayCount,times));
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
		}
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		
		return optionStr;
	}


	public String MobileNetworkQualityRank2D(NWQuality nw,List<NWQuality> nwQualities, int sum) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		z_Axis.add(VarDesc.OPERATOR.ALL);
		for (NWQuality nwQuality : nwQualities) {
			z_Axis.add(nwQuality.getNwOperator());
		}
		// X轴设置
		Set<String> x_Axis = new TreeSet<String>();
		for (NWQuality nwQuality : nwQualities) {
			x_Axis.add(nwQuality.getDeviceModel());
		}
		// 添加All，也就是把不同的z轴对应x数据相加
		for (String x : x_Axis) {
			long allSpeed = 0;
			for (NWQuality nwQuality : nwQualities) {
				if (x.equals(nwQuality.getDeviceModel())) {
					allSpeed = allSpeed + nwQuality.getSpeed();
				}
			}
			NWQuality nwQuality = new NWQuality();
			nwQuality.setDeviceModel(x);
			nwQuality.setSpeed(allSpeed);
			nwQuality.setNwOperator(VarDesc.OPERATOR.ALL);
			nwQualities.add(nwQuality);
		}
		// 排序操作
		NWQualityComparator nComparator = new NWQualityComparator();
		Collections.sort(nwQualities, nComparator);
		
		// 排序之后，重新设置x轴
		List<String> x_AxisAllDesc = new ArrayList<String>();
		for (NWQuality nwQuality : nwQualities) {
			if (!(x_AxisAllDesc.contains(nwQuality.getDeviceModel()))) {
				x_AxisAllDesc.add(nwQuality.getDeviceModel());
			}
			if (x_AxisAllDesc.size() >= 6) {
				break;
			}
		}

		Option option = new Option();
		String subtext = null;
		
		List<String> x_AxisAll = new ArrayList<String>();
		if (VarDesc.SPEEDTYPE.LATENCY.equals(nw.getSpeedType())) {
			subtext ="单位（ms）";
			x_AxisAll = x_AxisAllDesc;
		}else {
			subtext ="单位（ KB/S）";
			x_AxisAll = x_AxisAllDesc;
		}
		option.title().text("热门手机网络质量排名").subtext(subtext).itemGap(10)
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		Set<String> z_Sort = JsonUtil.zSort2D(z_Axis);
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList<Object>(x_AxisAll));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		System.out.println("z轴个数："+z_Axis.size());
		System.out.println("处理后z轴个数："+z_Sort.size());
		
		for (String z : z_Sort) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_AxisAll) {
				boolean flag = false;
				// 查找对应x轴和轴的值
				for (NWQuality nwQuality : nwQualities) {
					if (z.equals(nwQuality.getNwOperator())
							&& x.equals(nwQuality.getDeviceModel())) {
						y_values.add(nwQuality.getSpeed() / sum);
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			int count = 0;
			List<Object> y_Sort = new ArrayList<Object>();
			for(Object o : y_values){
				if(count<x_AxisAll.size()){
					y_Sort.add(y_values.get(count));
					count++;
				}else{
					break;
				}
			}
			System.out.println("循环次数:"+count);
			bar.setData(y_Sort);
			bars.add(bar);
		}
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		System.out.println("热门手机网络质量排名Json：------------\n"+optionStr);
		return optionStr;
	}
	public String MobileNetworkQualityRank(List<NWQuality> nwQualities, int sum) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		z_Axis.add(VarDesc.OPERATOR.ALL);
		for (NWQuality nwQuality : nwQualities) {
			z_Axis.add(nwQuality.getNwOperator());
		}
		// X轴设置
		Set<String> x_Axis = new TreeSet<String>();
		for (NWQuality nwQuality : nwQualities) {
			x_Axis.add(nwQuality.getDeviceModel());
		}
		// 添加All，也就是把不同的z轴对应x数据相加
		for (String x : x_Axis) {
			long allSpeed = 0;
			for (NWQuality nwQuality : nwQualities) {
				if (x.equals(nwQuality.getDeviceModel())) {
					allSpeed = allSpeed + nwQuality.getSpeed();
				}
			}
			NWQuality nwQuality = new NWQuality();
			nwQuality.setDeviceModel(x);
			nwQuality.setSpeed(allSpeed);
			nwQuality.setNwOperator(VarDesc.OPERATOR.ALL);
			nwQualities.add(nwQuality);
		}
		// 排序操作
		NWQualityComparator nComparator = new NWQualityComparator();
		Collections.sort(nwQualities, nComparator);
		// 排序之后，重新设置x轴
		List<String> x_AxisAll = new ArrayList<String>();
		for (NWQuality nwQuality : nwQualities) {
			if (!(x_AxisAll.contains(nwQuality.getDeviceModel()))) {
				x_AxisAll.add(nwQuality.getDeviceModel());
			}
			if (x_AxisAll.size() >= 8) {
				break;
			}
		}
		
		Option option = new Option();
		option.title().text("热门手机网络质量排名图(Spark)").subtext("单位  KB/S").itemGap(10)
		.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
		.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		//option.legend().setData(new ArrayList<Object>(z_Axis));
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList<Object>(x_AxisAll));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());
		
		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_AxisAll) {
				boolean flag = false;
				// 查找对应x轴和轴的值
				for (NWQuality nwQuality : nwQualities) {
					if (z.equals(nwQuality.getNwOperator())
							&& x.equals(nwQuality.getDeviceModel())) {
						y_values.add(nwQuality.getSpeed() / sum);
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
		}
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
//		System.out.println("热门手机网络质量排名Json：------------\n"+optionStr);
		return optionStr;
	}

	// 典型地标热门App流量排名(图)
	public String landmarkAppTrafficRank(List<AppTraffic> appTraffics) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		z_Axis.add(VarDesc.NETWORKTYPE.ALL);
		for (AppTraffic appTraffic : appTraffics) {
			z_Axis.add(appTraffic.getNetworkType());
		}
		// X轴设置
		Set<String> x_AxisOld = new TreeSet<String>();
		for (AppTraffic appTraffic : appTraffics) {
			x_AxisOld.add(appTraffic.getApp_name());
		}

		// 添加All,也就是把不同的z轴的对应x的数据起来
		for (String x : x_AxisOld) {
			long allSpeed = 0;
			for (AppTraffic appTraffic : appTraffics) {
				if (x.equals(appTraffic.getApp_name())) {
					allSpeed = allSpeed + appTraffic.getSpeed();
				}
			}
			AppTraffic appTraffic = new AppTraffic();
			appTraffic.setApp_name(x);
			appTraffic.setSpeed(allSpeed);
			appTraffic.setNetworkType(VarDesc.NETWORKTYPE.ALL);
			appTraffics.add(appTraffic);
		}
		// 排序操作
		AppTrafficComparator aComparator = new AppTrafficComparator();
		Collections.sort(appTraffics, aComparator);

		// 排序完之后,重新设置X轴
		List<String> x_Axis = new ArrayList<String>();
		for (AppTraffic appTraffic : appTraffics) {
			if (!(x_Axis.contains(appTraffic.getApp_name()))) {
				x_Axis.add(appTraffic.getApp_name());
			}
			if (x_Axis.size() >= 6) {
				break;
			}
		}
		// Y轴设置
		Set<Long> y_Axis = new TreeSet<Long>();
		for (AppTraffic appTraffic : appTraffics) {
			y_Axis.add(appTraffic.getSpeed());
		}
		int times = EchartsUtil.devidedTimes(Collections.max(y_Axis));
		String subtext = EchartsUtil.unitConversion(times);

		Option option = new Option();
		option.title().text("典型地标热门App流量排名").subtext("单位（"+subtext+"）")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		/*
		 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
		 */
		option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
		
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		// option.grid().y(80).y2(40).x2(40);
		CategoryAxis categoryAxis = new CategoryAxis();
		// 设置x轴的值
		categoryAxis.setData(new ArrayList<Object>(x_Axis));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();

		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_Axis) {
				boolean flag = false;
				// 查找对应x轴和z轴的值
				for (AppTraffic appTraffic : appTraffics) {
					if (z.equals(appTraffic.getNetworkType())
							&& x.equals(appTraffic.getApp_name())) {
						y_values.add(EchartsUtil.valueConversion(
								appTraffic.getSpeed(), times));
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
		}

		option.series(bars);
		String optionStr = GsonUtil.format(option);
		
		/*
		 * 默认只显示all项
		 */
		optionStr = JsonUtil.allToFirst(optionStr, z_Axis);
		
		return optionStr;
	}
	public String landmarkAppTrafficRankNull(List<AppTraffic> appTraffics) {
		try{
			// Z轴设置
			Set<String> z_Axis = new HashSet<String>();
			z_Axis.add(VarDesc.NETWORKTYPE.ALL);
			for (AppTraffic appTraffic : appTraffics) {
				z_Axis.add(appTraffic.getNetworkType());
			}
			// X轴设置
			Set<String> x_AxisOld = new TreeSet<String>();
			for (AppTraffic appTraffic : appTraffics) {
				x_AxisOld.add(appTraffic.getApp_name());
			}
			
			// 添加All,也就是把不同的z轴的对应x的数据起来
			for (String x : x_AxisOld) {
				long allSpeed = 0;
				for (AppTraffic appTraffic : appTraffics) {
					if (x.equals(appTraffic.getApp_name())) {
						allSpeed = allSpeed + appTraffic.getSpeed();
					}
				}
				AppTraffic appTraffic = new AppTraffic();
				appTraffic.setApp_name(x);
				appTraffic.setSpeed(allSpeed);
				appTraffic.setNetworkType(VarDesc.NETWORKTYPE.ALL);
				appTraffics.add(appTraffic);
			}
			
			// 排序完之后,重新设置X轴
			List<String> x_Axis = new ArrayList<String>();
			for (AppTraffic appTraffic : appTraffics) {
				if (!(x_Axis.contains(appTraffic.getApp_name()))) {
					x_Axis.add(appTraffic.getApp_name());
				}
				if (x_Axis.size() >= 6) {
					break;
				}
			}
			// Y轴设置
			Set<Long> y_Axis = new TreeSet<Long>();
			for (AppTraffic appTraffic : appTraffics) {
				y_Axis.add(appTraffic.getSpeed());
			}
			int times = 0;
			
			Option option = new Option();
			option.title().text("典型地标热门App流量排名").textStyle(new TextStyle().fontSize(14));
			option.tooltip().trigger(Trigger.axis)
			.axisPointer(new AxisPointer().type(PointerType.shadow));
			// 图例
			/*
			 * 此处将z轴中的“ALL”的项提前，其他项顺序不变
			 */
			option.legend().setData(new ArrayList<Object>(JsonUtil.zSort(z_Axis)));
			
			// 设置鼠标不能拖动
			option.calculable(false);
			// 直角坐标系内绘图网格
			// option.grid().y(80).y2(40).x2(40);
			CategoryAxis categoryAxis = new CategoryAxis();
			// 设置x轴的值
			categoryAxis.setData(new ArrayList<Object>(x_Axis));
			option.xAxis(categoryAxis);
			option.yAxis(new ValueAxis());
			
			List<Series> bars = new ArrayList<Series>();
			
			// 循环z轴
			for (String z : z_Axis) {
				List<Object> y_values = new ArrayList<Object>();
				// 创建对应z轴的bar
				Bar bar = new Bar(z);
				// 循环x轴
				for (String x : x_Axis) {
					boolean flag = false;
					// 查找对应x轴和z轴的值
					for (AppTraffic appTraffic : appTraffics) {
						if (z.equals(appTraffic.getNetworkType())
								&& x.equals(appTraffic.getApp_name())) {
							y_values.add(EchartsUtil.valueConversion(
									appTraffic.getSpeed(), times));
							flag = true;
						}
					}
					// 对应的y值没有找到，添加0占位符
					if (!flag) {
						y_values.add(0);
					}
				}
				bar.setData(y_values);
				bars.add(bar);
			}
			
			option.series(bars);
			String optionStr = GsonUtil.format(option);
			

			return optionStr;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "nodata";
		
	}

	// 典型地标网络质量统计图
	public String LandmarkNetworkQualityStatistics(List<NWQuality> nwQualities) {
		// Z轴设置
		Set<String> z_Axis = new HashSet<String>();
		for (NWQuality nwQuality : nwQualities) {
			z_Axis.add(nwQuality.getNwOperator());
		}
		// X轴设置
		Set<String> x_Axis = new TreeSet<String>();
		for (NWQuality nwQuality : nwQualities) {
			x_Axis.add(nwQuality.getLandmark());
		}
		// 排序操作
		NWQualityComparator nComparator = new NWQualityComparator();
		Collections.sort(nwQualities, nComparator);
		// 排序之后，重新设置x轴
		List<String> x_AxisAll = new ArrayList<String>();
		List<String> x_AxisAllCN = new ArrayList<String>();
		for (NWQuality nwQuality : nwQualities) {
			if (!(x_AxisAll.contains(nwQuality.getLandmark()))) {
				x_AxisAll.add(nwQuality.getLandmark());
				x_AxisAllCN.add(JsonUtil.nameChinese(nwQuality.getLandmark()));
			}
			if (x_AxisAll.size() >= 10) {
				break;
			}
		}

		Option option = new Option();
		option.title().text("典型地标网络质量统计图").subtext("单位  KB/S").itemGap(10)
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.axis)
				.axisPointer(new AxisPointer().type(PointerType.shadow));
		// 图例
		option.legend().setData(new ArrayList<Object>(z_Axis));
		// 设置鼠标不能拖动
		option.calculable(false);
		// 直角坐标系内绘图网格
		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.setData(new ArrayList<Object>(x_AxisAllCN));
		option.xAxis(categoryAxis);
		option.yAxis(new ValueAxis());

		List<Series> bars = new ArrayList<Series>();
		// 循环z轴
		for (String z : z_Axis) {
			List<Object> y_values = new ArrayList<Object>();
			// 创建对应z轴的bar
			Bar bar = new Bar(z);
			// 循环x轴
			for (String x : x_AxisAll) {
				boolean flag = false;
				// 查找对应x轴和轴的值
				for (NWQuality nwQuality : nwQualities) {
					if (z.equals(nwQuality.getNwOperator())
							&& x.equals(nwQuality.getLandmark())) {
						y_values.add(nwQuality.getDspeed());
						flag = true;
					}
				}
				// 对应的y值没有找到，添加0占位符
				if (!flag) {
					y_values.add(0);
				}
			}
			bar.setData(y_values);
			bars.add(bar);
		}
		option.series(bars);
		String optionStr = GsonUtil.format(option);
		System.out.println("典型地表网络质量统计直方图Json：   "+optionStr);
		return optionStr;
	}

	// 根据AllSpeed排序
	public class NWQualityComparator implements Comparator<NWQuality> {
		@Override
		public int compare(NWQuality n1, NWQuality n2) {
			Long speed1 = n1.getSumSpeed();
			Long speed2 = n2.getSumSpeed();
			return speed2.compareTo(speed1);
		}

	}
	public class NWQualityComparatorDesc implements Comparator<NWQuality> {
		@Override
		public int compare(NWQuality n1, NWQuality n2) {
			Long speed1 = n1.getSumSpeed();
			Long speed2 = n2.getSumSpeed();
			return speed1.compareTo(speed2);
		}
		
	}
	// 根据AllSpeed排序
	public class NWQualityComparatorSpeed implements Comparator<NWQuality> {
		@Override
		public int compare(NWQuality n1, NWQuality n2) {
			Long speed1 = n1.getSpeed();
			Long speed2 = n2.getSpeed();
			return speed2.compareTo(speed1);
		}

	}

}
