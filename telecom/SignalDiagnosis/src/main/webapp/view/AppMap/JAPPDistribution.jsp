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
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>JJT</title>
 <link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">
 
 <!-- 时间插件 -->
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/date/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/date/daterangepicker.jQuery.js"></script>
	
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
	
<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
	type="text/css" title="ui-theme" />
 
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
 
  
 <!--   <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  -->
   <style type="text/css">
    body, html{width: 100%;height: 100%;margin:0;font-family:"å¾®è½¯éé»";}
    #panorama {width:100%; height: 640px;}
    #result {width:100%;font-size:12px;}
  </style>
   
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

.form-control {
	width: 90%;
}
.element-style {
    background-color: #f3f1ec;
    color: #000;
    overflow: hidden;
    position: relative;
    text-align: left;
    top: 5px;
    z-index: 0;
}
.form-controloperator {
	width: 60%;
}

.form-controlprovince {
	width: 90%;
}

.row {
	width: 100%;
}
</style>
</head>
<body onload="showUserNearMap()" >


			<div style="width: 100%; height:15%;">
				<div class="header">
					<div class="panel panel-default">
						<p class="panel-heading">
							<spring:message code="condition"></spring:message>
						</p>
						<div class="panel-body">
							<div class="row">
								<div id="control">
<!-- ///////////////////////////////// -->
											<div class="col-sm-1"></div>
								    		<div class="col-sm-1" >
													<button type="submit" class="btn btn-danger" onclick="returns()">
														<spring:message code="return"></spring:message>
													</button>
												</div>
					   
									<!-- 运行商 -->
									<c:choose>
										<c:when test="${sessionScope.user.role=='admin'}">
											<div id="control2" class="form-group">
												<label class="col-sm-3 control-label" for="operator"> <spring:message
														code="operators"></spring:message>:
												</label>
												<div class="col-sm-3">
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
												<label class="col-sm-3 control-label" for="operator"> <spring:message
														code="operators"></spring:message>：
												</label>
												<div class="col-sm-3">
													<select type="text" id="operator" class="form-control"
														disabled="disabled">
														<option value="${sessionScope.user.NWOperator}"
															selected="selected">${sessionScope.user.NWOperator}</option>
													</select>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="col-sm-2"></div>
									<div class="col-sm-1">
										<button type="submit" class="btn btn-danger"
											onclick="showUserNearMap()">
											<spring:message code="submit"></spring:message>
										</button>
									</div>

<!-- //////////////////////////////// -->

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>


  <div id="panorama"  style="height: 650px;" class="element-style"></div>
  <div id="result">
  </div>


	<script src="<%=basePath%>js/load.js"></script>
<script type="text/javascript">

		var panorama = new BMap.Panorama('panorama');
		var point=new BMap.Point(<%=request.getParameter("lon")%>,<%=request.getParameter("lat")%>);
		panorama.setPosition(point);//

  
  /* 显示瓷砖图 */
  function showUserNearMap(){
	    reqLoading(true);
		/* 运营商 */
  		var opera=$("#operator").val();
  	
		/* 日期  */
		//var dat=$("#dateControl").val();
		var dat="2/27/2000 - 3/6/2015";
		/* 网络制式  */
		var NWType="ALL";
		

     	chag(opera,dat,NWType);  
 }	

	  
  function chag(opera,dat,NWType){

    //用户为中心的点
		//左下
		var zxj=point.lng-0.0114;
		var zxw=point.lat-0.004464;
		//右上
		var ysj=point.lng+0.0114;
		var ysw=point.lat+0.004464;

	    avgLng=(ysj-zxj)/3.0;//经度分为3列
	    avgLat=(ysw-zxw)/3.0;//维度分为3行
     itemLng = zxj;
	   itemLat = ysw;
	   itemLng1=ysj;
	   itemLat1=zxw;
	 
    if (ysj<=0 || zxj<=0 || ysw<=0 || zxw<=0 || ysj<=zxj || ysw<=zxw) {
		itemLng1=175.0;
		 itemLng = 1.0;
	 	  itemLat = 70.0;	
	 	 itemLat1=1.0;		
		avgLng=(175.0-1.0)/3.0;
		avgLat=(70.0-1.0)/3.0;
	}

   
	  $.ajax({
 	   
 	   type:"post",
 	   url : '<%=basePath%>showNearapp',

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
				},

				success : function(datas) {
				    reqLoading(false);
					 //showtable(avgLng,avgLat,itemLng,itemLat);
					$.each(datas, function(i, item) {
						//avgSpeed2[i]=item;
						 showlogo(item.tableCenterLng,item.tableCenterLat,item.app_name,item.count);
					});
					
				}
			});	

	 
		}
  
	function gettime() {
		  $.ajax({
	  	   type:"post",
	  	   url:"<%=basePath%>getdate",
	  	  dataType:"json",
	         success:function(data){
	        	  $("#dateControl").val(data.date);
	         }
	     });
		  top10app();
	}
  
  function showtable(avgLng,avgLat,itemLng,itemLat){ 


      var oldLat=itemLat;
  	var l=0;
  	//i 列    j 行
  	for (var i = 0; i < 3; i++) {
		for (var j = 0; j < 3; j++) {
			colr = "grey";
			polygon = new BMap.Polygon([
					new BMap.Point(itemLng, itemLat),
					new BMap.Point(itemLng + avgLng, itemLat),
					new BMap.Point(itemLng + avgLng, itemLat - avgLat),
					new BMap.Point(itemLng, itemLat - avgLat) ], {
					strokeColor : "red",
					strokeWeight : 1,
					strokeOpacity : 0.301,
					fillColor : colr,
					fillOpacity : 0.001
			}); //创建多边形
			map.addOverlay(polygon);
			itemLat = itemLat - avgLat;
			l++;
		}
		l=i*3;
		itemLat = oldLat;
		itemLng = itemLng + avgLng;
	}
  	
  }
  
  function returns(){
	   window.location.href="<%=basePath%>view/AppMap/showJA.jsp";
  }
  function showlogo(tableCenterLng,tableCenterLat,app_name,count){ 

	  

	  if (app_name=="phoneTotalTraffic"){
		 // alert(2);
	  }else{
		  var labelPosition = new BMap.Point(tableCenterLng,tableCenterLat);
		  var labelOptions = {
		      position: labelPosition,
		    altitude:5
		  }
		  var label = new BMap.PanoramaLabel(app_name, labelOptions);
		  panorama.addOverlay(label);
	  }
	  

  }
  
  
  
  
  panorama.setPov(label.getPov()); 
  label.addEventListener('click', function() { //
    panorama.setPov({  //
      pitch: 10, 
      heading: 14
    });
  });
</script>
</body>
</html>
