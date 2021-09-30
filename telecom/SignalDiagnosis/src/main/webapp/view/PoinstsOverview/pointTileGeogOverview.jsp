<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.Date" %>
<%@page import="java.text.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	 Date date=new Date();
	 Date date1=new Date();
	 long temp=7 * 24 * 60 * 60 * 1000l;
	 SimpleDateFormat  sf= new SimpleDateFormat("yyyyMMddHHmmss");
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
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-timepicker-en-US.js"></script>
<link type="text/css"
	href="<%=basePath%>js/dateToSecond/css/jquery-ui-1.8.17.custom.css"
	rel="stylesheet" />
<link type="text/css"
	href="<%=basePath%>js/dateToSecond/css/jquery-ui-timepicker-addon.css"
	rel="stylesheet" />

	
<!--  -->
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<!--  -->

<!-- 加载层start -->
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<!-- 加载层end -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap.css">
	
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
   <style type="text/css">
    body, html{width: 100%;height: 100%;margin:0;font-family:"å¾®è½¯éé»";}
    #panorama {width:100%; height: 640px;}
    #result {width:100%;font-size:12px;}
  </style>
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
        margin-left: 3px;
    }
    #map{
        width:100%;
        height:97%;
        margin-right: 3px;
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
	width: 100%;
}
.form-controloperator {
	width: 60%;
}
.form-controlprovince {
	width: 80%;
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
						
							<div id="control1" class="col-sm-3 control-label"></div>
						<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
						<div id="control2" class="form-group">
							<label class="col-sm-1 control-label" for="operator"> <spring:message
									code="operators"></spring:message>:
							</label>
							<div class="col-sm-2">
								<select id="operator" class="form-control form-controloperator">

									<option selected="selected" value="ALL">ALL</option>
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
								<select id="operator" class="form-control" disabled="disabled">
									<option value="${sessionScope.user.NWOperator}"
										selected="selected">${sessionScope.user.NWOperator}</option>
								</select>
							</div>
						</div>


					</c:otherwise>
				</c:choose>
							
	 				<!-- 网络延迟 -->
				<div id="control4" class="form-group">
					<label class="col-sm-1 control-label" for="nwSpeed">
						<spring:message code="networkQuality"></spring:message>：
					</label>
					<div class="col-sm-2">
						<select id="nwSpeed" name="speedType"
							class="form-control form-controlprovince">
							<option value=""></option>
							<option value="DLSpeed" selected="selected"><spring:message
									code="downSpeed"></spring:message></option>
							<option value="Latency"><spring:message code="latency"></spring:message></option>

							<option value="ULSpeed"><spring:message code="upSpeed"></spring:message></option>
						</select>
					</div>
				</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="panorama"   style="height: 650px;"  class="element-style"></div>
		  <div id="result" >
	</div>
	
	
	
	
	
	
<script  language="javascript" type="text/javascript"> 

	   var panorama = new BMap.Panorama('panorama');
	   //第一个地图获取的经纬度点
		var point=new BMap.Point(<%=request.getParameter("lng")%>,<%=request.getParameter("lat")%>);
		panorama.setPosition(point);

		/* 关闭父页面的遮罩层 */
 	 window.parent.reqLoading(false);
	
   function check(){
			var opera=$("#operator").val();
			var nwSpeed=$("#nwSpeed").val();

		if( opera==''  || opera==null || nwSpeed=='' || nwSpeed==null){
			return false;
		} else{
	    	 return true;
	     } 
 		
	} 
		
	
		var clickLon=point.lng;
        var clickLat=point.lat;
	
         chag(clickLon,clickLat); 
    
         var nwSpeed=$("#nwSpeed").val();
       
        function chag(clickLon,clickLat){

             avgLng=0.05/3.0;
             avgLat=0.02/3.0;

			if (check()) {

        	var opera=$("#operator").val();
			
			var startDate="2010-01-01 00:00:00";
			var endDate="2020-01-01 00:00:00";
			var nwSpeed=$("#nwSpeed").val();
	     
		     reqLoading(true);
			 $.ajax({
	         	   type:"post",
	         	   url : '<%=basePath%>showAllUserPointTileGeog',
	      				dataType : "json",
	      				async : true,
	      				data : {
	      					"tileLng" : avgLng,
	      					"tileLat" : avgLat,
	      					"minGpslat" : clickLat,
	      					"minGpslon" : clickLon,
	      					"nwOperator":opera,
	      					"startDateToSecond":startDate,
	      					"endDateToSecond":endDate,
	      					"speedType":nwSpeed
	      				},

	      				success : function(datas) {

	      				     /* 关闭加载层 */
	      				     reqLoading(false);
	    					$.each(datas, function(i, item) {
	    						 showlogo(item.gpslon,item.gpslat,item.speedss);

	    					});
						}
	      			});	

				}
             }
        
         
        
           function showlogo(tableCenterLng,tableCenterLat,speeds){ 

     			  var labelPosition = new BMap.Point(tableCenterLng,tableCenterLat);
        			  var labelOptions = {
        			      position: labelPosition,
        			    altitude:5
        			  }
        			  var label = new BMap.PanoramaLabel(speeds, labelOptions);
        			  panorama.addOverlay(label);
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