<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title>CNitcast</title>

<!-- bootstrap加载 -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">
<!-- 直方图控制JS -->
<script type="text/javascript" src="<%=basePath%>js/histogramControl.js"></script>



<!-- 地图js -->	
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
<!-- 地图js end -->
<!-- Le fav and touch icons -->
<!-- Demo page code -->

<style type="text/css">
body {
	font-family: Arial, "Microsoft YaHei", "Helvetica Neue", Helvetica,
		sans-serif;
	font-size: 14px;
}

.background_img {
	background-image: url("<%=basePath%>img/testpointCount.png");
	width: 200px;
	height: 70px;
	margin-left: auto;
	margin-right: auto;
}
.p_title{
	padding-top: 10px;
	color: #FFFFFF;
	text-align: center;
	font-size: 18px;
}
.p_count{
	padding-bottom: 5px;
	color: #FFFFFF;
	text-align: center;
	font-size: 18px;
}
 #map{
        width:100%;
        height:97%;
        margin-right: 3px;
        border-width:3px;
        border-style: none;
 }
</style>

</head>
<body  onload="reqLoading(true);">
	<div id="main" class="container-fluid" style="width: 100%; height: 100%;">
		<div id="top" class="row-fluid">
			<div id="testPointCount" class="col-md-12">
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="col-sm-4">
							<div class="background_img">
								<p class="p_title"><spring:message code="NETWORKQUALITY"> </spring:message></p>
								<p class="p_count" id="num0">${nwQualityCount }</p>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="background_img">
								<p class="p_title"><spring:message code="SIGNALSTRENGTH"> </spring:message></p>
								<p class="p_count" id="num1">${signalStrengthCount }</p>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="background_img">
								<p class="p_title"><spring:message code="DATACONNECTION"> </spring:message></p>
								<p class="p_count" id="num2">${dataConnectionCount }</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="below" class="row-fluid">
		    <!-- 地图起始 -->
			<div id="testPointMap" class="col-md-5">
				<span style="font-size: 16px;font-family: cursive;font-weight: bold;">连接点地理分布 </span>
				<a href="<%=basePath%>showAllPointGeog?nwOperator=ALL"
					target="rightFrame">
					<div id="map" class="col-md-12"></div>
				</a>
			</div>
		   <!-- 地图结束 -->
			<div class="col-md-7">
				<div class="row-fluid">
 				    
			  	   	<!-- 网络质量统计图ZHL -->
				   	<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<a  href="<%=basePath%>nwquality_networkQualityStatistics?speedType=ULSpeed&nwOperator=ALL">
				    <div id="networkqualityU" class="col-md-12"></div> 
					</a>
					</c:when>
					<c:otherwise>
					<a href="<%=basePath%>nwquality_networkQualityStatistics?speedType=ULSpeed&nwOperator=${sessionScope.user.NWOperator}">
				    <div id="networkqualityU" class="col-md-12"></div> 
					</a>
					</c:otherwise>
					</c:choose>
					</div>
				<div class="row-fluid">
					<%-- <a href="<%=basePath %>dataConnectionStatistics" target="rightFrame">
						<div id="dataConnStatistics" class="col-md-12"></div>
						
				    </a> --%>
				    <c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<a  href="<%=basePath%>nwquality_networkQualityStatistics?speedType=DLSpeed&nwOperator=ALL">
				    <div id="networkqualityD" class="col-md-12"></div>
					</a>
					</c:when>
					<c:otherwise>
					<a href="<%=basePath%>nwquality_networkQualityStatistics?speedType=DLSpeed&nwOperator=${sessionScope.user.NWOperator}">
					<div id="networkqualityD" class="col-md-12"></div> 
					</a>
					</c:otherwise>
					</c:choose>
				</div>
				<div class="row-fluid">
					<a href="<%=basePath %>app_appRateRank" target="rightFrame">
						 <div id="appTrafficRank" class="col-md-6"></div>
					</a>
					<a href="<%=basePath%>app_hotTelRank" target="rightFrame" onclick="reqLoading(true);">
						<div id="mobileDeviceTrafficRank" class="col-md-6"></div>
					</a>
				</div>
			</div>
		</div>
	</div>
	<script src="<%=basePath%>js/echarts-all.js"></script>
	<script type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	//高度控制
	//reqLoading(true);
	var mianHight = $(window).height();
	$('#testPointCount').css('height',mianHight*0.13);
	$('#testPointMap').css('height',mianHight*0.87);
	$('#networkqualityU').css('height',mianHight*0.28);
	$('#networkqualityD').css('height',mianHight*0.28);
	$('#appTrafficRank').css('height',mianHight*0.30);
	$('#mobileDeviceTrafficRank').css('height',mianHight*0.30);
	
	//直方图显示
	//数据连接率统计直方图
 	var networkqualityDs = echarts.init(document.getElementById('networkqualityD'));
	var networkqualityDa = ${networkqualityD};
	networkqualityDs.setOption(networkqualityDa);
	 
 	/* 网络速率排名 */
  	var networkqualityURank = echarts.init(document.getElementById('networkqualityU'));
	var networkqualityUa = ${networkqualityU};
	networkqualityURank.setOption(networkqualityUa);
 	  
	
	//App流量排名
	var appTrafficRank = echarts.init(document.getElementById('appTrafficRank'));
	var option3 = ${app_optionStr_pie};
	appTrafficRank.setOption(option3);
	
	//热门手机流量排名
	var mobileDeviceTrafficRank = echarts.init(document.getElementById('mobileDeviceTrafficRank'));
	var option4 = ${md_optionStr_pie};
	mobileDeviceTrafficRank.setOption(option4);
	
	/* 地图显示js -----start*/
	 //经度
	 var longitudes = new Array();
	 //维度数组
	 var dimensionalitys = new Array();
	 //运营商数组
	 var nwOperator =new Array();
	 
	 //经度
	 var longitudes1 = new Array();
	 //维度数组
	 var dimensionalitys1 = new Array();
	 //运营商数组
	 var nwOperator1 =new Array();
	 
	 
	 //经度
	 var longitudes2 = new Array();
	 //维度数组
	 var dimensionalitys2 = new Array();
	 //运营商数组
	 var nwOperator2 =new Array();

	 var temp=new Array();

	 var temp2= new Array();

	var map = new BMap.Map("map",{});
	map.centerAndZoom(new BMap.Point(110.404, 35.915), 5);
	map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放 
	map.enableKeyboard(); // 开启键盘控制 
	map.enableContinuousZoom(); // 开启连续缩放效果 
	map.enableInertialDragging(); // 开启惯性拖拽效果 
	map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
	map.addControl(new BMap.ScaleControl(
			{
				anchor : BMAP_ANCHOR_BOTTOM_LEFT,
				 offset : new BMap.Size(10,18)
			})); //添加比例尺控件(左下角显示的比例尺控件)
	map.addControl(new BMap.OverviewMapControl()); // 缩略图控件
          //启用滚轮放大缩小
	$(document).ready(
				function() {
					if (document.createElement('canvas').getContext) {

						// 判断当前浏览器是否支持绘制海量点
						var points = []; // 添加海量点数据

						//遍历数组添加坐标

						var obj = '${nwQualities}';

						//alert(obj);
						var obj2 = JSON.parse(obj);

						for (var l = 0; l < obj2.length; l++) {
							dimensionalitys[l] = obj2[l].gpslat;
							longitudes[l] = obj2[l].gpslon;
							nwOperator[l] = obj2[l].nwOperator;
						}

						for (var i = 0; i < dimensionalitys.length; i++) {
							points.push(new BMap.Point(longitudes[i],
									dimensionalitys[i]));
						}

						var options = {
							size : BMAP_POINT_SIZE_SMALL,//设置点的大小
							shape : BMAP_POINT_SHAPE_CIRCLE,//设置点的形状为小星星，下面参数为设置点的颜色
							color : '#df0001'
						}
						var pointCollection = new BMap.PointCollection(points,
								options); // 初始化PointCollection
						pointCollection.addEventListener('click',
								function(e) {
									alert('点的地理做标为：' + e.point.lng + ','
											+ e.point.lat); // 监听点击事件
								});
						//添加覆盖物  添加Overlay（覆盖物）
						map.addOverlay(pointCollection);
					} else {
						alert('请在firefox、chrome、safari、IE8+以上浏览器查看本示例');
					}
	});
	/* 地图显示js -----end*/
	


		/* 每隔10s刷新一次 */
		function dashBoard(){
			 $.ajax({
				type : "post",
				url : "<%=basePath%>dashBoard_flash",
				ContentType : "application/x-www-form-urlencoded; charset=UTF-8",
				dataType : "json",
				success : function(data) {
					value= data;
					for(var i = 0; i < value.length; i++){
						$('#num'+i).text(value[i]);
					}
				}
			});
		}
	    //创建ajax请求参数
		var xhr;
		function createXHR1(){
		    //ie4.0  ie5.0
		  if (window.ActiveXObject) {
		        var msxmls=['Msxml2.XMLHTTP','Microsoft.XMLHTTP'];
		       for (var i = 0; i < msxmls.length; i++) {
		           try {
		                xhr=new ActiveXObject(msxmls[i]);
		               return ;
		            } catch (exception) { 
		                continue;
		            }
		            
		        }
		        //其他浏览器
		    }else if (window.XMLHttpRequest) {
		        xhr=new XMLHttpRequest();
		    }else{
		        alert("您的浏览器版本过低请求不能够创建");
		    }
		}
		function freshMapPoint(){
			//alert(1);
			 $.ajax({
					xhr :createXHR1(),
					url : "<%=basePath%>freshMapPoint",
					type : "post",
					//请求回调结果
					success : function(data1) {
					  //经度
					 longitudes1.length =0;
					 //维度数组
					 dimensionalitys1.length =0;
					 //运营商数组
					 nwOperator1.length =0; 

					 if(data2!=null){
					 $.each(data1, function(i, item) {
						dimensionalitys1[i] =item.gpslat;
						longitudes1[i] = item.gpslon;
						nwOperator1[i] = item.nwOperator;
					});
					 }
					 addfreshMapPoint(dimensionalitys1,longitudes1,nwOperator1);
					 }
					});	
			 }

		  function addfreshMapPoint(dim,lon,nw) {
			 // var map = new BMap.Map("map",{});
			//	map.centerAndZoom(new BMap.Point(110.404, 35.915), 5);
			//	map.clearOverlays(); 
					if (document.createElement('canvas').getContext) {
						// 判断当前浏览器是否支持绘制海量点
						var points = []; // 添加海量点数据
						//遍历数组添加坐标
						for (var n = 0; n < dim.length;n++) {
							points.push(new BMap.Point(lon[n],
									dim[n]));
						}

						var options = {
							size : BMAP_POINT_SIZE_SMALL,//设置点的大小
							shape : BMAP_POINT_SHAPE_CIRCLE,//设置点的形状为小星星，下面参数为设置点的颜色
							color : '#0000ef'
						}
						var pointCollection = new BMap.PointCollection(points,
								options); // 初始化PointCollection
						pointCollection.addEventListener('click',
								function(e) {
									alert('点的地理做标为：' + e.point.lng + ','
											+ e.point.lat); // 监听点击事件
								});
						//添加覆盖物  添加Overlay（覆盖物）
						map.addOverlay(pointCollection);
					} else {
						alert('请在firefox、chrome、safari、IE8+以上浏览器查看本示例');
					}
				}
		  
			function freshMapOldPoint(){
				//alert(2);
				 $.ajax({
						xhr :createXHR1(),
						url : "<%=basePath%>freshMapOldPoint",
						type : "post",
						//请求回调结果
						success : function(data2) {
						 //经度
						 longitudes2.length =0;
						  //维度数组
						 dimensionalitys2.length =0;
						 //运营商数组
						 nwOperator2.length =0; 

						 if(data2!=null){
							 $.each(data2, function(i, item) {
									dimensionalitys2[i] =item.gpslat;
									longitudes2[i] = item.gpslon;
									nwOperator2[i] = item.nwOperator;
								}); 
						 }
						 addfreshMapOldPoint(dimensionalitys2,longitudes2,nwOperator2); 
						 }
						});	
				 }

			  function addfreshMapOldPoint(dim,lon,nw) {
				  //var map = new BMap.Map("map",{});
				//	map.centerAndZoom(new BMap.Point(110.404, 35.915), 5);
				//	map.clearOverlays();   
						if (document.createElement('canvas').getContext) {
							// 判断当前浏览器是否支持绘制海量点
							var points = []; // 添加海量点数据
							//遍历数组添加坐标
							for (var n = 0; n < dim.length;n++) {
								points.push(new BMap.Point(lon[n],
										dim[n]));
							}

							var options = {
								size : BMAP_POINT_SIZE_SMALL,//设置点的大小
								shape : BMAP_POINT_SHAPE_CIRCLE,//设置点的形状为小星星，下面参数为设置点的颜色
								color : '#00ff01'
							}
							var pointCollection = new BMap.PointCollection(points,
									options); // 初始化PointCollection
							pointCollection.addEventListener('click',
									function(e) {
										alert('点的地理做标为：' + e.point.lng + ','+ e.point.lat); // 监听点击事件
									});
							//添加覆盖物  添加Overlay（覆盖物）
							map.addOverlay(pointCollection);
						} else {
							alert('请在firefox、chrome、safari、IE8+以上浏览器查看本示例');
						}
					}
		//刷新触发
 		$(function (){
			flash_start();
		});
		function flash_start(){
			dashBoard();
			
			freshMapPoint();
			
			freshMapOldPoint();
			setTimeout("flash_start()", 2000);
		}
	</script>
</body>
</html>