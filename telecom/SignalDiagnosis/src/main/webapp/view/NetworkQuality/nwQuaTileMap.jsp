<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	Date date1=new Date();
	//long temp=7 * 24 * 60 * 60 * 1000l;
	long temp=1928 * 24 * 60 * 60 * 1000l;
	SimpleDateFormat  sf= new SimpleDateFormat("yyyyMMdd");
	Date date2=new Date(date1.getTime()-temp);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="pointMap">
	</spring:message></title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">


	<!-- 时间插件 -->
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript"
			src="<%=basePath%>js/date/jquery-ui-1.7.1.custom.min.js"></script>
	<script type="text/javascript"
			src="<%=basePath%>js/date/daterangepicker.jQuery.js"></script>
	<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
		  type="text/css" />
	<link rel="stylesheet"
		  href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
		  type="text/css" title="ui-theme" />
	<!--  -->
	<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
	<!--  -->

	<!-- 加载层start -->
	<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
	<!-- 加载层end -->


	<link rel="stylesheet" type="text/css"
		  href="<%=basePath%>css/bootstrap.css">

	<script type="text/javascript"
			src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
	<script type="text/javascript">
		//调用时间插件
		$(function() {
			$('#dateControl').daterangepicker();
		});
	</script>
	<style type="text/css">
		html {
			margin-top: 0px;
			margin-bottom: 1px;
			width: 100%;
			height: 100%;
			background: #fffaf0;
			margin-right: 2px;
		}

		body {
			margin-top: 0px;
			margin-bottom: 5px;
			width: 99%;
			height: 97%;
			background: #eeeeee;
			margin-right: 3px;
			margin-left: 3px;
		}

		#map {
			width: 100%;
			height: 97%;
			margin-right: 3px;
			border-width: 3px;
		}

		#panel {
			position: absolute;
			top: 30px;
			left: 10px;
			z-index: 999;
			color: #fff;
		}

		#login {
			position: absolute;
			width: 300px;
			height: 40px;
			left: 50%;
			top: 50%;
			margin: -40px 0 0 -150px;
		}

		#login input[type=password] {
			width: 200px;
			height: 30px;
			padding: 3px;
			line-height: 30px;
			border: 1px solid #000;
		}

		#login input[type=submit] {
			width: 80px;
			height: 38px;
			display: inline-block;
			line-height: 38px;
		}

		#top {
			width: 100%;
			height: 10%;
			margin-bottom: 1%;
			margin-top: 0.5%;
			overflow: visible;
		}

		.control-label {
			text-align: right;
			padding-top: 7px;
		}

		/* .form-control {
            width: 90%;
        } */

		/* .form-controloperator {
            width: 60%;
        }

        .form-controlprovince {
            width: 90%;
        } */

		.row {
			width: 100%;
		}
	</style>
</head>

<body onload="showNewMap()" >
<div style="width: 100%; height: 100%;">
	<div class="header">
		<div class="panel panel-default">
			<p class="panel-heading">
				<spring:message code="condition"></spring:message>
			</p>
			<div class="panel-body">
				<div class="row">
					<div id="control">
						<!-- 日期 -->
						<div id="control1" class="form-group">
							<label class="col-sm-1 control-label" for="dateControl" style="margin-left:-15px;"> <spring:message
									code="date"></spring:message>:
							</label>
							<div class="col-sm-2  from-position">
								<input type="text" id="dateControl" value='<fmt:formatDate value="<%=date2 %>" pattern="M/d/yyyy"/>-<fmt:formatDate value="<%=date1 %>" pattern="M/d/yyyy"/>' class="form-control">
							</div>

						</div>
						<!-- 运行商 -->
						<!-- 运行商 -->
						<c:choose>
							<c:when test="${sessionScope.user.role=='admin'}">
								<div id="control2" class="form-group">
									<label class="col-sm-1 control-label" for="operator"> <spring:message
											code="operators"></spring:message>:
									</label>
									<div class="col-sm-1">
										<select type="text" id="operator"
												class="form-control form-controloperator">
											<option value="ALL" selected="selected">ALL</option>
											<option value="CMCC">CMCC</option>
											<option value="CTCC">CTCC</option>
											<option value="CUCC">CUCC</option>
										</select>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div id="control2" class="form-group">
									<label class="col-sm-1 control-label" for="operator"> <spring:message
											code="operators"></spring:message>：
									</label>
									<div class="col-sm-1">
										<select type="text" id="operator" class="form-control"
												disabled="disabled">
											<option value="${sessionScope.user.NWOperator}"
													selected="selected">${sessionScope.user.NWOperator}</option>
										</select>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						<!-- 网络制式 -->
						<div id="control3" class="form-group">
							<label class="col-sm-1 control-label" for="NWType">
								<spring:message code="mobile_network_type"></spring:message>:
							</label>
							<div class="col-sm-1">
								<select type="text" id="NWType"
										class="form-control form-controlprovince">
									<option value="ALL" selected="selected">ALL</option>
									<option value="WI-FI">WI-FI</option>
									<option value="4G">4G</option>
									<option value="3G">3G</option>
									<option value="2G">2G</option>
								</select>
							</div>
						</div>

						<!-- 网络质量 -->
						<div id="control4" class="form-group">
							<label class="col-sm-1 control-label" for="nwSpeed"
							> <spring:message code="networkQuality"></spring:message>：
							</label>
							<div class="col-sm-2" >
								<select type="text" id="nwSpeed" name="speedType"  class="form-control form-controlprovince">
									<option value=""></option>
									<option value="DLSpeed" selected="selected"><spring:message code="downSpeed"></spring:message></option>
									<option value="Latency"><spring:message code="latency"></spring:message></option>

									<option value="ULSpeed"><spring:message code="upSpeed"></spring:message></option>
								</select>
							</div>
						</div>
						<!-- 提交 -->
						<div class="col-sm-1">
							<button type="submit" class="btn btn-danger"
									onclick="showNewMap()">
								<spring:message code="submit"></spring:message>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="main-content">
		<!-- 右侧上层图表 -->
		<div class="panel panel-default" >
			<div id="page-stats" class="panel-collapse panel-body collapse in" >
				<div class="row" style="width: 100%;margin-left: 3px;">
					<div id="map" style="height: 650px;width: 100%;"></div>

				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	/* 关闭父页面的遮罩层 */
	window.parent.reqLoading(false);

	var map = new BMap.Map("map",{});
	map.centerAndZoom(new BMap.Point(116.363545,39.965913), 10);
	map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放
	map.enableKeyboard(); // 开启键盘控制
	map.enableContinuousZoom(); // 开启连续缩放效果
	map.enableInertialDragging(); // 开启惯性拖拽效果
	map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
	map.addControl(new BMap.OverviewMapControl()); // 缩略图控件
	map.addControl(new BMap.ScaleControl(
			{
				anchor : BMAP_ANCHOR_BOTTOM_LEFT,
				offset : new BMap.Size(3,20)
			})); //添加比例尺控件(左下角显示的比例尺控件)offset 第一个参数是距离下边左边多少像素，第二个参数是距离下边多少像素

	map.addControl(new BMap.MapTypeControl({

		anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
		offset : new BMap.Size(5,5)
	})); //2D图，卫星图
	//启用滚轮放大缩小

	// 定义一个控件类,即function
	function ZoomControl(){
		// 默认停靠位置和偏移量
		this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
		this.defaultOffset = new BMap.Size(5, 5);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	ZoomControl.prototype = new BMap.Control();

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	ZoomControl.prototype.initialize = function(map){
		// 创建一个DOM元素
		var div = document.createElement("div");
		// 也可以添加文字说明
		//div.appendChild(document.createTextNode("放大2级"));
		div.innerHTML="<ol style='font-size:15px;background: white;list-style: none;font-style: inherit;padding-left: 1px;margin-left:1px;margin-bottom:1px;'> " +
				"<li style=''><a style='background-color:grey ;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&lt;0KB/s</li>" +
				"<li ><a style='background-color:#600000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>0-50KB/s</li>" +
				"<li ><a style='background-color:#ea0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>50-100KB/s</li>" +
				"<li ><a style='background-color:#ff9797'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>100-150KB/s</li>" +
				"<li ><a style='background-color:#bbffbb'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>150-200KB/s</li>" +
				"<li ><a style='background-color:#28ff28'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>200-300KB/s</li><li >" +
				"<a style='background-color:#009100'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&gt;300KB/s</li></ol>"



		// 设置样式
		div.style.cursor = "pointer";
		div.style.border = "0px solid gray";
		div.style.backgroundColor = "white";
		// 还可以绑定事件
		/*  div.onclick = function(e){
             //点击一次放大两级
             map.setZoom(map.getZoom() + 2);
             alert(1);
         } ;  */
		// 添加DOM元素到地图中
		map.getContainer().appendChild(div);
		// 将DOM元素返回
		return div;
	} ;
	// 创建控件
	var myZoomCtrl = new ZoomControl();
	// 添加到地图当中
	map.addControl(myZoomCtrl);




	var colr;
	var red1="#600000";
	var red2="#ea0000";
	var red3="#ff9797";
	var green1="#bbffbb";
	var green2="#28ff28";
	var green3="#009100";
	var polygon;



	var bs= map.getBounds();   //获取可视区域
	var bssw = bs.getSouthWest();   //可视区域左下角
	var bsne = bs.getNorthEast();   //可视区域右上角

	var  avgLng=(bsne.lng-bssw.lng)/25.0;//经度分为25列
	var  avgLat=(bsne.lat-bssw.lat)/12.0;//维度分为12行

	//async : false,

	var itemLng = bssw.lng;
	var itemLat = bsne.lat;




	/* 显示瓷砖图 */
	function showNewMap(){
		/* 运营商 */
		var opera=$("#operator").val();
		/* 日期  */
		var dat=$("#dateControl").val();
		/* 网络制式  */
		var NWType=$("#NWType").val();
		/*  网络延迟  */
		var nwSpeed=$("#nwSpeed").val();
		map.clearOverlays();

		//需要加判断数值是否为空的判断@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		chag(opera,dat,NWType,nwSpeed);

	}

	var avgSpeed2 = new Array();

	function chag(opera,dat,NWType,nwSpeed){

		bs= map.getBounds();   //获取可视区域
		bssw = bs.getSouthWest();   //可视区域左下角
		bsne = bs.getNorthEast();   //可视区域右上角

		avgLng=(bsne.lng-bssw.lng)/25.0;//经度分为25列
		avgLat=(bsne.lat-bssw.lat)/12.0;//维度分为12行
		itemLng = bssw.lng;
		itemLat = bsne.lat;
		itemLng1=bsne.lng;
		itemLat1=bssw.lat;

		if (bsne.lng<=0 || bssw.lng<=0 || bsne.lat<=0 || bssw.lat<=0 || bsne.lng<=bssw.lng || bsne.lat<=bssw.lat) {
			itemLng1=175.0;
			itemLng = 1.0;
			itemLat = 70.0;
			itemLat1=1.0;
			avgLng=(175.0-1.0)/25.0;
			avgLat=(70.0-1.0)/12.0;
		}

		//async : false,
		/* 调用加载层 */
		reqLoading(true);

		//NWQualityController
		$.ajax({

			type:"post",
			url : '<%=basePath%>showNwQuaTileSpeed',

			dataType : "json",
			async : true,
			data : {
				"tileLng" : avgLng, //平均经度
				"tileLat" : avgLat, //平均纬度
				"minGpslat" : itemLat1,//左下角的纬度
				"minGpslon" : itemLng, //左下角的经度
				"gpslat" : itemLat,  //右上角的纬度
				"gpslon" : itemLng1, //右上角的经度


				"nwOperator":opera,
				"daytime":dat,
				"nwType":NWType,
				"speedType":nwSpeed
			},

			success : function(datas) {
				$.each(datas, function(i, item) {
					avgSpeed2[i]=item;
				});

				/* 关闭加载层 */
				reqLoading(false);

				showColr(avgSpeed2,bssw,bsne,avgLng,avgLat,itemLng,itemLat);
			}
		});


	}

	function showColr(avgSpeed2,bssw,bsne,avgLng,avgLat,itemLng,itemLat){


		var oldLat=itemLat;
		var l=0;
		for (var i = 0; i <25; i++) {
			for (var j = 0; j < 12; j++) {
				var ram =avgSpeed2[l];
				if (ram <= 0.0 || ram==null || ram =="") {
					colr = "grey";
				} else if (ram > 0.0 && ram <= 50.0) {
					colr = red1;
				}else if (ram > 50.0 && ram <= 100.0) {
					colr = red2;
				} else if (ram > 100.0 && ram < 150.0) {
					colr = red3;
				} else if (ram > 150.0 && ram <= 200.0) {
					colr = green1;
				} else if (ram > 200.0 && ram <= 300.0) {
					colr = green2;
				} else if (ram > 300.0) {
					colr = green3;
				} else {
					colr = "grey";
				}
				polygon = new BMap.Polygon([
					new BMap.Point(itemLng, itemLat),     //  左上
					new BMap.Point(itemLng + avgLng, itemLat),  //右上
					new BMap.Point(itemLng + avgLng, itemLat - avgLat),  //右下
					new BMap.Point(itemLng, itemLat - avgLat) ], {   //左下
					strokeColor : "white",
					strokeWeight : 1,
					strokeOpacity : 0.68,
					fillColor : colr,
					fillOpacity : 0.23
				}); //创建多边形
				map.addOverlay(polygon);
				itemLat = itemLat - avgLat;
				l++;
			}
			l=i*12;
			itemLat = oldLat;
			itemLng = itemLng + avgLng;
		}
	}


</script>
</body>
</html>