package cn.itcast.echarts;

import java.util.List;

import cn.itcast.util.EchartsUtil;
import org.springframework.stereotype.Component;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonUtil;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.TextStyle;
import cn.itcast.entity.AppTraffic;

@Component
public class EchartsPie {

	// App流量排名饼图
	public String AppRateRankPie(List<Object> appPies, List<Object> app_names,
			String showType) {
		Option option = new Option();
		option.title().text("热门App排名").x("center")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.item).formatter("{b} : {d}%");
		Pie pie = new Pie();
		if ("dashBoard".equals(showType)) {
			pie.center("50%", "50%").radius("55%");
		} else {
			option.legend().orient(Orient.vertical).x("left").data(app_names);
			pie.center("55%", "55%").radius("50%");
		}
		option.calculable(true);
		pie.setData(appPies);
		option.series(pie);
		String optionStr = GsonUtil.format(option);
		return optionStr;
	}

	// 热门手机流量排名(饼图)
	public String HotTelRank(List<Object> telPies, List<Object> tel_name, String showType) {
		Option option = new Option();
		option.title().text("热门手机排名").x("center")
				.textStyle(new TextStyle().fontSize(14));
		option.tooltip().trigger(Trigger.item).formatter("{b} : {d}%");
		Pie pie = new Pie();
		if ("dashBoard".equals(showType)) {
			pie.center("50%", "50%").radius("55%");
		} else {
			option.legend().orient(Orient.vertical).x("left").data(tel_name);
			pie.center("55%", "55%").radius("50%");
		}
		option.calculable(true);
		pie.setData(telPies);
		option.series(pie);
		String optionStr = GsonUtil.format(option);
		return optionStr;
	}

	/**
	 *  手机OS流量饼图
 	 */
	public String hotTelOsStatistics_pie(List<AppTraffic> appTraffics,
			long allSpeed) {
		String option = null;
		try {
			/*
			 * @param:objects带泛型的list对象
			 * 
			 * @param:m_x_g，对应x轴clazz中的get方法名
			 * 
			 * @param:m_y_g，对应y轴clazz中的get方法名
			 */
			EchartsUtil<AppTraffic> echartsUtil = new EchartsUtil<AppTraffic>();
 
			option = echartsUtil.getEchartsPie(appTraffics, "getOsAndVersion",
					"getSpeed", allSpeed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return option;
	}

	/**
	 * 热门手机App流量饼图
 	 */
	public String hotAppTrafficStatistics_pie(List<AppTraffic> appTraffics,
			long allSpeed) {
		// 已经被取消
		return null;
	}

}
