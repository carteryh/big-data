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


<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>

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
.element-style {
    background-color: #f3f1ec;
    color: #000;
    overflow: hidden;
    position: relative;
    text-align: left;
    top: 7px;
    z-index: 0;
}
/* .form-control {
	width: 90%;
}

.form-controloperator {
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
<body   onload="gettime()"  >

			<div style="width: 100%; height: 12%;">
				<div class="header">
					<div class="panel panel-default">
						<p class="panel-heading">
							<spring:message code="condition"></spring:message>
						</p>
						<div class="panel-body">
							<div class="row">
								<div id="control">
<!-- //////////////////////////////////////// -->
							<label class="col-sm-1 control-label" for="dateControl"> <spring:message
									code="date"></spring:message>:
							</label>
							<div class="col-sm-2  from-position">
								<input type="text" id="dateControl" value="" class="form-control">
							</div>
						<c:choose>
							<c:when test="${sessionScope.user.role=='admin'}">
									<label class="col-sm-1 control-label" for="operator"> <spring:message
											code="operators"></spring:message>:
									</label>
									<div class="col-sm-1" style="width: 10%">
										<select type="text" id="operator"
											class="form-control form-controloperator">
											<option value="ALL" selected="selected">ALL</option>
											<option value="CMCC">CMCC</option>
											<option value="CTCC">CTCC</option>
											<option value="CUCC">CUCC</option>
										</select>
									</div>
							</c:when>
							<c:otherwise>
									<label class="col-sm-1 control-label" for="operator"> <spring:message
											code="operators"></spring:message>：
									</label>
									<div class="col-sm-1" style="width: 10%">
										<select type="text" id="operator" class="form-control"
											disabled="disabled">
											<option value="${sessionScope.user.NWOperator}"
												selected="selected">${sessionScope.user.NWOperator}</option>
										</select>
									</div>
							</c:otherwise>
						</c:choose>
						
							<label class="col-sm-1 control-label" for="NWType">
								<spring:message code="mobile_network_type"></spring:message>:
							</label>
							<div class="col-sm-1" style="width: 10%">
								<select type="text" id="NWType"
									class="form-control" style="width: 100%">
									    <option value="ALL" selected="selected">ALL</option>
										<option value="WI-FI">WI-FI</option>
										<option value="4G">4G</option>
										<option value="3G">3G</option>
										<option value="2G">2G</option>
								</select>
							</div>
									<label class="col-sm-1 control-label" for="nwSpeed"
										> <spring:message code="telTraffic"></spring:message>:
									</label>
							<div class="col-sm-2" >
								<select type="text" id="nwSpeed" name="speedType"  class="form-control form-controlprovince">
								<option value=""></option>
								 <option value="Latency"  selected="selected"><spring:message code="allflow"></spring:message></option>
								 <option value="ULSpeed"><spring:message code="upflow"></spring:message></option>
								 <option value="DLSpeed"><spring:message code="downflow"></spring:message></option>
								   
								</select>
							</div>
						<div class="col-sm-1">
							<button type="submit" class="btn btn-danger"
								onclick="showNewMap()">
								<spring:message code="submit"></spring:message>
							</button>
						</div>
<!-- //////////////////////////////////////// -->

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		

	<div id="map"  class="element-style"  style="height: 750px";></div>
	
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
   	   url : '<%=basePath%>showHeadMobile',

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
						//alert(datas)
						//alert(item.count);
						//avgSpeed2[i]=item;
						 showlogo(item.tableCenterLng,item.tableCenterLat,item.companyModel,item.count);
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
    
    
    function showlogo(tableCenterLng,tableCenterLat,companyModel,count){  
     	if (count>100 ){
     		if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("GT-I9308") != -1){
            	var pt2 = new BMap.Point(tableCenterLng, tableCenterLat);
            	var myIcon2 = new BMap.Icon("<%=basePath%>mobilelogo/i9308.png", new BMap.Size(80,50));
            	var marker2 = new BMap.Marker(pt2,{icon:myIcon2});  // 创建标注
            	map.addOverlay(marker2);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("SCH-I535") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/1535.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("SCH-I939D") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/1939D.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);    
                	
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("SCH-N719") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/sch-n719.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("GT-N7108") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/n7108.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("GT-I9100") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/i9100.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("GT-I9300") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/i9300.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("SM-N9006") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/SM-N9006.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("GT-N7100") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/N7100.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("samsung") != -1&&companyModel.indexOf("Nexus S") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/Nexus-S.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}
 
 
 
 
 
            	else if (companyModel.indexOf("ZTE") != -1&&companyModel.indexOf("V889D") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/889d.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("ZTE") != -1&&companyModel.indexOf("N980") != -1){
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/n980.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("ZTE") != -1&&companyModel.indexOf("U880E") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/u880e.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("ZTE") != -1&&companyModel.indexOf("N880F") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/u880f.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("HUAWEI") != -1&&companyModel.indexOf("U9200E") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/U9200E.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}else if (companyModel.indexOf("Xiaomi") != -1&&companyModel.indexOf("MI 3") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/m13.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
            	}
            	
            	
            	
            	
            	
            	else if (companyModel.indexOf("YuLong") != -1&&companyModel.indexOf("Coolpad 5879") != -1){
                	var pt3 = new BMap.Point(tableCenterLng, tableCenterLat);
                	var myIcon3 = new BMap.Icon("<%=basePath%>mobilelogo/5879.png", new BMap.Size(80,50));
                	var marker3 = new BMap.Marker(pt3,{icon:myIcon3});  // 创建标注
                	map.addOverlay(marker3);   
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