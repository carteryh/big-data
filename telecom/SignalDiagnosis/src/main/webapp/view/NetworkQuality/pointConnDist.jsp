<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="varDesc" scope="page" class="cn.itcast.entity.VarDesc"></jsp:useBean>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
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

<!--  -->
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<!--  -->

<!-- 加载层start -->
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<!-- 加载层end -->



	
<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
	type="text/css" title="ui-theme" />
	
	
	<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">
	
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
	<script type="text/javascript">
	//调用时间插件
	$(function() {
		$('#dateControl').daterangepicker();
	});
	</script>
 <style type="text/css">
    html{
        margin-top:0px;
        margin-bottom:1px;
        width:100%;
        height:100%;
        background: #fffaf0;
        margin-right: 2px;
    }
    body{
        margin-top:0px;
        margin-bottom:5px;
        width:99%;
        height:97%;
        background:#eeeeee;
        margin-right: 3px;
    }
    #map{
        width:100%;
        height:97%;
        margin-right: 3px;
        border-width:3px;
        margin-left: 3px;
    }
    #panel {
        position: absolute;
        top:30px;
        left:10px;
        z-index: 999;
        color: #fff;
    }
    #login{
        position:absolute;
        width:300px;
        height:40px;
        left:50%;
        top:50%;
        margin:-40px 0 0 -150px;
    }
    #login input[type=password]{
        width:200px;
        height:30px;
        padding:3px;
        line-height:30px;
        border:1px solid #000;
    }
    #login input[type=submit]{
        width:80px;
        height:38px;
        display:inline-block;
        line-height:38px;
    }
    #top{
    width: 100%;
    height: 10%;
    margin-bottom:1%; 
    margin-top: 0.5%;
    overflow: visible;
    }
    .control-label {
	text-align: right;
	padding-top: 7px;
}

.form-control {
	width: 80%;
}
.form-controloperator {
	width: 60%;
}
.form-controlprovince {
	width: 70%;
}
.row{
width: 100%;
}
  </style>
</head>
<body>
<div style="width: 100%; height: 100%;">
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
								<div class="col-sm-3  from-position">
									<input type="text" id="dateControl" value="${dataConnection.time_index_client}"  class="form-control" >
								</div>
							</div>
				
							
						<c:choose>
						<c:when test="${sessionScope.user.role=='admin'}">
						 <div id="control2" class="form-group">
								<label class="col-sm-1 control-label" for="operator"> <spring:message
										code="operators"></spring:message>:
								</label>
								<div class="col-sm-2">
									<select type="text" id="operator" class="form-control form-controloperator">
										<c:choose>
											<c:when test="${dataConnection.network_name == varDesc.getOperator('CMCC')}">
												<option value="CMCC">CMCC</option>
												<option value="ALL">ALL</option>
												<option value="CTCC">CTCC</option>
												<option value="CUCC">CUCC</option>
											</c:when>
											<c:when test="${dataConnection.network_name == varDesc.getOperator('CTCC')}">
												<option value="CTCC">CTCC</option>
												<option value="ALL">ALL</option>
												<option value="CMCC">CMCC</option>
												<option value="CUCC">CUCC</option>
											</c:when>
											<c:when test="${dataConnection.network_name == varDesc.getOperator('CUCC')}">
												<option value="CUCC">CUCC</option>
												<option value="ALL">ALL</option>
												<option value="CMCC">CMCC</option>
												<option value="CTCC">CTCC</option>
											</c:when>
											<c:otherwise>
												<option value="ALL">ALL</option>
												<option value="CMCC">CMCC</option>
												<option value="CTCC">CTCC</option>
												<option value="CUCC">CUCC</option>
											</c:otherwise>
										</c:choose>
									    <%-- <option value="${dataConnection.network_name}" >${dataConnection.network_name}</option>
										<option value="ALL" >ALL</option>
										<option value="CMCC">CMCC</option>
										<option value="CTCC">CTCC</option>
										<option value="CUCC">CUCC</option> --%>
									</select>
								</div>
							</div>
						</c:when>

						<c:otherwise>
						<div id="control2" class="form-group">
								<label class="col-sm-1 control-label" for="operator"> 
								<spring:message code="operators"></spring:message>：</label>
								<div class="col-sm-2" >
									<select type="text" id="operator" class="form-control" disabled="disabled" >
										<option value="${sessionScope.user.NWOperator}" selected="selected" >${sessionScope.user.NWOperator}</option>
									</select>
								</div>
							</div>
						
							
						</c:otherwise>
						</c:choose>
							
							<div id="control2" class="form-group" >
								<label class="col-sm-1 control-label" for="mobile_network_type"> <spring:message
										code="mobile_network_type"></spring:message>:
								</label>
								<div class="col-sm-2">
									<select  id="mobile_network_type" class="form-control form-controlprovince" >
									   <c:choose>
											<c:when test="${dataConnection.mobile_network_type == varDesc.getNetworkType('4G')}">
												<option value="4G">4G</option>
												<option value="ALL">ALL</option>
												<option value="3G">3G</option>
												<option value="2G">2G</option>
											</c:when>
											<c:when test="${dataConnection.mobile_network_type == varDesc.getNetworkType('3G')}">
												<option value="3G">3G</option>
												<option value="ALL">ALL</option>
												<option value="4G">4G</option>
												<option value="2G">2G</option>
											</c:when>
											<c:when test="${dataConnection.mobile_network_type == varDesc.getNetworkType('2G')}">
												<option value="2G">2G</option>
												<option value="ALL">ALL</option>
												<option value="4G">4G</option>
												<option value="3G">3G</option>
											</c:when>
											<c:otherwise>
												<option value="ALL">ALL</option>
												<option value="4G">4G</option>
												<option value="3G">3G</option>
												<option value="2G">2G</option>
											</c:otherwise>
										</c:choose>
									   <%--
									    <option value="${dataConnection.mobile_network_type}" selected="selected" >${dataConnection.mobile_network_type}</option>
									   
									    <option value="ALL" selected="selected" >ALL</option>
									    <option value="2G"  >2G</option>
									    <option value="3G"  >3G</option>
									    <option value="4G"  >4G</option>
									    --%>
									 </select>
								</div>
							</div>
							
							<div class="col-sm-2">
								<button type="submit" class="btn btn-danger" onclick="selctPointConDist()">
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
						<div id="map" style="height: 790px;width: 100%;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false); 
	 
   function check(){
		var operator=$("#operator").val();
		var dateControl=$("#dateControl").val();
		var mobile_network_type=$("#mobile_network_type").val();
		
		if(operator=='' || dateControl=='' || dateControl==null || operator==null || mobile_network_type=='' ||mobile_network_type==null){
		return false;
		}
		else
	    {  
	    return true;
	     }  
	}
   function selctPointConDist(){
	 if (check()) {
			var opera=$("#operator").val();
			var dat=$("#dateControl").val();
			var mobile_network_type=$("#mobile_network_type").val();
			
		     /* 调用加载层 */
		     reqLoading(true);
			location.href="<%=basePath%>showPointConnDist?time_index_client="+dat+"&network_name="+opera+"&mobile_network_type="+mobile_network_type;

		     /* 关闭加载层 */
		     reqLoading(false);
	 }
   }
   
   
   
 
   
   
   

   //经度
	 var longitudes = new Array();
	 	//维度数组
	 var dimensionalitys = new Array();
	 	//运营商数组
	 var nwOperator =new Array();

	 var temp=new Array();

	 var temp2= new Array();
	 
	 
	 var longitudes1 = new Array();
	 	//维度数组
	 var dimensionalitys1 = new Array();
	 	//运营商数组
	 var nwOperator1 =new Array();

	 var temp1=new Array();

	 var temp21= new Array();

	var map = new BMap.Map("map",{});
	map.centerAndZoom(new BMap.Point(116.354313,39.963929), 18);
	map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放 
	map.enableKeyboard(); // 开启键盘控制 
	map.enableContinuousZoom(); // 开启连续缩放效果 
	map.enableInertialDragging(); // 开启惯性拖拽效果 
	map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
	map.addControl(new BMap.ScaleControl()); //添加比例尺控件(左下角显示的比例尺控件)
	map.addControl(new BMap.OverviewMapControl()); // 缩略图控件
          //启用滚轮放大缩小
          

$(document).ready(function(){
	
	
	if ("${sessionScope.user.NWOperator}"!="admin") {
		var jsonNwType='${strnwType}';
		//alert(jsonNwType);
		var NwType=  JSON.parse(jsonNwType);
		//alert(NwType);
		 $("#mobile_network_type").html("");
         $("#mobile_network_type").append("<option value='ALL'>ALL</option>");
         for (var x = 0; x < NwType.length; x++) {
        	// alert(NwType[x].mobile_network_type);
        	 $("#mobile_network_type").append("<option value="+NwType[x].mobile_network_type+">"+NwType[x].mobile_network_type+"</option>");
        	}
	}

	
	
	if (document.createElement('canvas').getContext) { 

		// 判断当前浏览器是否支持绘制海量点
	   var points1 = [];  // 添加海量点数据

	   //遍历数组添加坐标

	  var obj1='${strnwConnectionFails}';
	 // alert(obj1);
	  var obj21=  JSON.parse(obj1);
	 for (var l = 0; l < obj21.length; l++) {
 
		  dimensionalitys1[l]=obj21[l].user_lat;
		  longitudes1[l]=obj21[l].user_lon;
		  nwOperator1[l]=obj21[l].network_name;
	}



	   for (var i = 0; i < dimensionalitys1.length; i++) {
	   	points1.push(new BMap.Point(longitudes1[i], dimensionalitys1[i]));
	   }
	   
	   var options = {
	       size: BMAP_POINT_SIZE_SMALL,//设置点的大小
	       shape: BMAP_POINT_SHAPE_CIRCLE,//设置点的形状为小星星，下面参数为设置点的颜色
	       color: '#FF0000'
	   }
	   var pointCollection = new BMap.PointCollection(points1, options);  // 初始化PointCollection
	   pointCollection.addEventListener('click', function (e) {
	     alert('点的地理做标为：' + e.point.lng + ',' + e.point.lat);  // 监听点击事件
	   });
	   //添加覆盖物  添加Overlay（覆盖物）
	   map.addOverlay(pointCollection);  
	   

	} else {
	   alert('请在firefox、chrome、safari、IE8+以上浏览器查看本示例');
	}
	
	
	if (document.createElement('canvas').getContext) { 
		
		// 判断当前浏览器是否支持绘制海量点
	   var points = [];  // 添加海量点数据

	   //遍历数组添加坐标

	  var obj='${strnwQualities}';
	 // alert(obj);
	  var obj2=  JSON.parse(obj);
	 for (var l = 0; l < obj2.length; l++) {

		  dimensionalitys[l]=obj2[l].user_lat;
		  longitudes[l]=obj2[l].user_lon;
		  nwOperator[l]=obj2[l].network_name;
	}



	   for (var i = 0; i < dimensionalitys.length; i++) {
	   	points.push(new BMap.Point(longitudes[i], dimensionalitys[i]));
	   }
	   
	   var options = {
	       size: BMAP_POINT_SIZE_SMALL,//设置点的大小
	       shape: BMAP_POINT_SHAPE_CIRCLE,//设置点的形状为小星星，下面参数为设置点的颜色
	       color: '#00FF00'
	   }
	   var pointCollection = new BMap.PointCollection(points, options);  // 初始化PointCollection
	   pointCollection.addEventListener('click', function (e) {
	     alert('点的地理做标为：' + e.point.lng + ',' + e.point.lat);  // 监听点击事件
	   });
	   //添加覆盖物  添加Overlay（覆盖物）
	   map.addOverlay(pointCollection);  
	   

	} else {
	   alert('请在firefox、chrome、safari、IE8+以上浏览器查看本示例');
	}
}); 
	
	

	var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset : new BMap.Size(5, 5)});   //设置版权控件位置
		map.addControl(cr); //添加版权控件
	
	var bs = map.getBounds();   //返回地图可视区域
		cr.addCopyright({id: 1, content: '<img   src=<%=basePath%>img/connect.png>', bounds: bs});   


   
  </script>
</body>
</html>