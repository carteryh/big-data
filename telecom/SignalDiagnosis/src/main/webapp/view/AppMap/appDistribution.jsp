<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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


<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">

<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
	
	<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
	
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
	background:#fffaf0;
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
	border-left-style: groove;
	border-top-style: groove;
	border-bottom-style: groove;
	border-right-style: groove;
	 border: 1px solid #ccc;
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

.element-style {
    background-color: #f3f1ec;
    color: #000;
    overflow: hidden;
    position: relative;
    text-align: left;
    top: 10px;
    z-index: 0;
    
    
}

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
<body  onload="gettime()" >

	<div style="width: 100%; height: 15%;">
		<div class="header">
			<div class="panel panel-default">
				<p class="panel-heading">
					<spring:message code="condition"></spring:message>
				</p>
				<div class="panel-body">
					<div class="row">
						<div id="control">


			
				<div id="control1" class="form-group">
					<label class="col-sm-1 control-label" for="dateControl"> <spring:message
							code="date"></spring:message>:
					</label>
					<div class="col-sm-2  from-position">
						<input type="text" id="dateControl" value="" class="form-control">
					</div>
				</div>
				<!-- 运行商 -->
				<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
						<div id="control2" class="form-group">
							<label class="col-sm-1 control-label" for="operator"> <spring:message
									code="operators"></spring:message>:
							</label>
							<div class="col-sm-2">
								<select type="text" id="operator"
									class="form-control form-controloperator">
									<option value="${nwQuality.nwOperator}" >${nwQuality.nwOperator}</option>
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
							<div class="col-sm-2">
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
								<!-- <option value="WI-FI">WI-FI</option> -->
								<option value="4G">4G</option>
								<option value="3G">3G</option>
								<option value="2G">2G</option>
						</select>
					</div>
				</div>
				
				<!-- 网络延迟 -->
                <div id="control4" class="form-group">
					<label class="col-sm-1 control-label" for="nwSpeed"
						> <spring:message code="appflow"></spring:message>：
					</label>
					<div class="col-sm-2" >
						<select type="text" id="nwSpeed" name="speedType"  class="form-control form-controlprovince">
						<option value=""></option>
							<option value="Latency" selected="selected"><spring:message code="allflow"></spring:message></option>
						<option value="DLSpeed"><spring:message code="downflow"></spring:message></option>
					
						<option value="ULSpeed"><spring:message code="upflow"></spring:message></option>
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
	</div>



	<div id="map" class="element-style"   style=" height:87%"></div>
		<script src="<%=basePath%>js/load.js"></script>
	<script type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	
	var map = new BMap.Map("map",{});
	map.centerAndZoom(new BMap.Point(116.363545,39.965913), 14);
	map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放 
	map.enableKeyboard(); // 开启键盘控制 
	map.enableContinuousZoom(); // 开启连续缩放效果 
	map.enableInertialDragging(); // 开启惯性拖拽效果 
	map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
	map.addControl(new BMap.OverviewMapControl()); // 缩略图控件
	//map.addEventListener("tilesloaded",function(){showNewMap();});
	map.addControl(new BMap.ScaleControl(
			{
				anchor : BMAP_ANCHOR_BOTTOM_LEFT,
				 offset : new BMap.Size(15,50)
			})); //添加比例尺控件(左下角显示的比例尺控件)offset 第一个参数是距离下边左边多少像素，第二个参数是距离下边多少像素
	
	map.addControl(new BMap.MapTypeControl({

		anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
		 offset : new BMap.Size(15, 50)
	})); //2D图，卫星图
          //启用滚轮放大缩小
          

    
    
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
	    reqLoading(true);
		/* 运营商 */
    	var opera=$("#operator").val();
		/* 日期  */
		var dat=$("#dateControl").val();
		/* 网络制式  */
		var NWType=$("#NWType").val();
		/*  网络延迟  */
		//var nwSpeed=$("#nwSpeed").val();
       	map.clearOverlays(); 
       	chag(opera,dat,NWType);  
   }	
	
	 var avgSpeed2 = new Array();
	  
    function chag(opera,dat,NWType){
  
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
     
	  $.ajax({
   	   
   	   type:"post",
   	   url : '<%=basePath%>showHeadApp',

				dataType : "json",
				async : true,
				data : {
					"tileLng" : avgLng,//平均经度
					"tileLat" : avgLat,//平均纬度
					"minGpslat" : itemLat1,//左下纬度
					"minGpslon" : itemLng,//左下经度
					"gpslat" : itemLat,//右上纬度
					"gpslon" : itemLng1,//右上经度
					"network_name":opera,
					"Daytime":dat,
					"networkType":NWType//,
					//"speedType":nwSpeed
				},

				success : function(datas) {
				    reqLoading(false);
					 showtable(avgSpeed2,bssw,bsne,avgLng,avgLat,itemLng,itemLat);
					$.each(datas, function(i, item) {
						//alert(item.app_name);
						//avgSpeed2[i]=item;
						 showlogo(item.tableCenterLng,item.tableCenterLat,item.package_name,item.count);
					});
					
				}
			});	

	 
		}
    //async : false,
	function gettime() {
    	
	  $.ajax({
  	   type:"post",
  	   url:"<%=basePath%>getdate",
  	  dataType:"json",
         success:function(data){
        	  $("#dateControl").val(data.date);
         }
     });
	  showNewMap();
}
    function showtable(avgSpeed2,bssw,bsne,avgLng,avgLat,itemLng,itemLat){ 


        var oldLat=itemLat;
    	var l=0;
    	//i 列    j 行
    	for (var i = 0; i < 25; i++) {
		for (var j = 0; j < 12; j++) {
			colr = "grey";
			polygon = new BMap.Polygon([
					new BMap.Point(itemLng, itemLat),
					new BMap.Point(itemLng + avgLng, itemLat),
					new BMap.Point(itemLng + avgLng, itemLat - avgLat),
					new BMap.Point(itemLng, itemLat - avgLat) ], {
					strokeColor : "red",
					strokeWeight : 1,
					strokeOpacity : 0.001,
					fillColor : colr,
					fillOpacity : 0.001
			}); //创建多边形
			map.addOverlay(polygon);
			itemLat = itemLat - avgLat;
			l++;
		}
		l=i*25;
		itemLat = oldLat;
		itemLng = itemLng + avgLng;
	}
    	
    	
    }
    
    
    function showlogo(tableCenterLng,tableCenterLat,package_name,count){ 

		//alert("aaaaaaaaaaaaaa  "+count);
    	if (count>5000){
    		//alert("经度  "+tableCenterLng+"    纬度  "+tableCenterLat+"  报名  "+package_name);
         	if (package_name=="edu.bupt.anttestaa"){
        		var pt1 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon1 = new BMap.Icon("<%=basePath%>applogo/yaoyao.png", new BMap.Size(40,40));
            	var marker1 = new BMap.Marker(pt1,{icon:myIcon1});  // 创建标注
            	map.addOverlay(marker1);     
        	}else 
        		if (package_name=="com.qihoo.video"){
        	var pt2 = new BMap.Point(tableCenterLng, tableCenterLat);
        	var myIcon2 = new BMap.Icon("<%=basePath%>applogo/360yingshi.png", new BMap.Size(40,40));
        	var marker2 = new BMap.Marker(pt2,{icon:myIcon2});  // 创建标注
        	map.addOverlay(marker2);   
        	}else if (package_name=="com.qiyi.video"){
            	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon3 = new BMap.Icon("<%=basePath%>applogo/aiqiyi.png", new BMap.Size(40,40));
            	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
            	map.addOverlay(marker3);   
            	}
        	else if (package_name=="com.tencent.mm"){
            	var pt4= new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon4 = new BMap.Icon("<%=basePath%>applogo/weixin.png", new BMap.Size(40,40));
            	var marker4 = new BMap.Marker(pt4,{icon:myIcon4});  // 创建标注
            	map.addOverlay(marker4);   
            	}
        	else if (package_name=="com.UCMobile"){
            	var pt5 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon5 = new BMap.Icon("<%=basePath%>applogo/uc.png", new BMap.Size(40,40));
            	var marker5 = new BMap.Marker(pt5,{icon:myIcon5});  // 创建标注
            	map.addOverlay(marker5);   
            	}
        	else if (package_name=="com.tencent.mobileqq"){
            	var pt6 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon6 = new BMap.Icon("<%=basePath%>applogo/qq.png", new BMap.Size(40,40));
            	var marker6 = new BMap.Marker(pt6,{icon:myIcon6});  // 创建标注
            	map.addOverlay(marker6);   
            	}
        	else if (package_name=="com.tencent.qqlive"){
            	var pt7 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon7 = new BMap.Icon("<%=basePath%>applogo/tengxun.png", new BMap.Size(40,40));
            	var marker7 = new BMap.Marker(pt7,{icon:myIcon7});  // 创建标注
            	map.addOverlay(marker7);   
            	}
        	else if (package_name=="com.xunlei.kankan"){
            	var pt8 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon8 = new BMap.Icon("<%=basePath%>applogo/xunlei.png", new BMap.Size(40,40));
            	var marker8 = new BMap.Marker(pt8,{icon:myIcon8});  // 创建标注
            	map.addOverlay(marker8);   
            	}
        	else if (package_name=="com.pplive.androidph"){
            	var pt9 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon9 = new BMap.Icon("<%=basePath%>applogo/pptv.png", new BMap.Size(40,40));
            	var marker9 = new BMap.Marker(pt9,{icon:myIcon9});  // 创建标注
            	map.addOverlay(marker9);   
            	}
        	else if (package_name=="com.wandoujia.phoeni"){
            	var pt10 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon10 = new BMap.Icon("<%=basePath%>applogo/wandoujia.png", new BMap.Size(40,40));
            	var marker10 = new BMap.Marker(pt10,{icon:myIcon10});  // 创建标注
            	map.addOverlay(marker10);   
            	}
        	else if (package_name=="com.smile.gifmaker"){
            	var pt11 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon11 = new BMap.Icon("<%=basePath%>applogo/kuaishou.png", new BMap.Size(40,40));
            	var marker11 = new BMap.Marker(pt11,{icon:myIcon11});  // 创建标注
            	map.addOverlay(marker11);   
            	}
        	else if (package_name=="com.storm.smart"){
            	var pt12 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon12 = new BMap.Icon("<%=basePath%>applogo/baofeng.png", new BMap.Size(40,40));
            	var marker12 = new BMap.Marker(pt12,{icon:myIcon12});  // 创建标注
            	map.addOverlay(marker12);   
            	}
        	else if (package_name=="com.qvod.player"){
            	var pt13 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon13 = new BMap.Icon("<%=basePath%>applogo/kuaibo.png", new BMap.Size(40,40));
            	var marker13 = new BMap.Marker(pt13,{icon:myIcon13});  // 创建标注
            	map.addOverlay(marker13);   
            	}
        	else if (package_name=="com.letv.android.cli"){
            	var pt14= new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon14 = new BMap.Icon("<%=basePath%>applogo/leshi.png", new BMap.Size(40,40));
            	var marker14 = new BMap.Marker(pt14,{icon:myIcon14});  // 创建标注
            	map.addOverlay(marker14);   
            	}
        	else if (package_name=="cn.cmvideo.isj"){
            	var pt15 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon15 = new BMap.Icon("<%=basePath%>applogo/heshiye.png", new BMap.Size(40,40));
            	var marker15 = new BMap.Marker(pt15,{icon:myIcon15});  // 创建标注
            	map.addOverlay(marker15);   
            	}
        	else if (package_name=="com.tencent.mtt"){
            	var pt16 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon16 = new BMap.Icon("<%=basePath%>applogo/hengxunliulanqi.png", new BMap.Size(40,40));
            	var marker16 = new BMap.Marker(pt16,{icon:myIcon16});  // 创建标注
            	map.addOverlay(marker16);   
            	}
        	else if (package_name=="com.youku.phone"){
            	var pt17 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon17 = new BMap.Icon("<%=basePath%>applogo/youku.png", new BMap.Size(40,40));
            	var marker17 = new BMap.Marker(pt17,{icon:myIcon17});  // 创建标注
            	map.addOverlay(marker17);   
            	}
        	else if (package_name=="com.elinkway.infinit"){
            	var pt18 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon18 = new BMap.Icon("<%=basePath%>applogo/kuaikan.png", new BMap.Size(40,40));
            	var marker18 = new BMap.Marker(pt18,{icon:myIcon18});  // 创建标注
            	map.addOverlay(marker18);   
            	}
        	else if (package_name=="com.taobao.taobao"){
            	var pt19 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon19 = new BMap.Icon("<%=basePath%>applogo/taobao.png", new BMap.Size(40,40));
            	var marker19 = new BMap.Marker(pt19,{icon:myIcon19});  // 创建标注
            	map.addOverlay(marker19);   
            	}
            	
        	
    	}
    	
    }
    
	
	var  bnt=new BMap.CopyrightControl({
		anchor: BMAP_ANCHOR_TOP_RIGHT,
		 offset : new BMap.Size(5, 125)
	});
	
	map.addControl(bnt); //2D图，卫星图

	
	</script>
</body>
</html>