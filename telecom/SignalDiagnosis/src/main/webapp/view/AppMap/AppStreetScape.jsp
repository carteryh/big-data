<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

	


<!-- 时间插件 -->
<%-- 
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/date/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/date/daterangepicker.jQuery.js"></script>
<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
	type="text/css" title="ui-theme" /> 
	--%>
	
	
	<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">
	
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>

 <style type="text/css">
    html{
        margin-top:0px;
        margin-bottom:1px;
        width:100%;
        height:100%;
        background:#eeeeee;
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
    #panorama{
        width:100%;
        height:97%;
        margin-right: 3px;
        border-width:3px;
        border-left-style: groove;
        border-top-style:groove;
        border-bottom-style: groove;
        border-right-style:groove;
        
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
margin-left:5%;
width: 90%;
}
  </style>
</head>
<body>
  
    <div id="top" >
                        <div class="row">
						<!-- <form class="form-horizontal" action="#"></form> -->
						<div id="control">
							<div id="control1" class="form-group" >
								<label class="col-sm-1 control-label" for="startDateControl"> <spring:message
										code="startdate"></spring:message>:
								</label>
								<div class="col-sm-3  from-position">
									<input type="text" id="startDateControl"   value="" class="ui_timepicker form-control">
								</div>
							</div>
							
							
							
								<div id="control1" class="form-group" >
								<label class="col-sm-1 control-label" for="endDateControl"> <spring:message
										code="enddate"></spring:message>:
								</label>
								<div class="col-sm-3  from-position">
								     <input type="text" id="endDateControl"    value=""  class="ui_timepicker form-control">
								</div>
							</div>
							
					 <c:choose>
						<c:when test="${sessionScope.user.role=='admin'}">
						 <div id="control2" class="form-group">
								<label class="col-sm-1 control-label" for="operator"> <spring:message
										code="operators"></spring:message>:
								</label>
								<div class="col-sm-3">
									<select  id="operator" class="form-control form-controloperator">
									    
										<option selected="selected" value="ALL" >ALL</option>
										<option value="CMCC">CMCC</option>
										<option value="CTCC">CTCC</option>
										<option value="CUCC">CUCC</option>
									</select>
								</div>
							</div>
						</c:when>

						<c:otherwise>
						<div id="control2" class="form-group">
								<label class="col-sm-1 control-label" for="operator"> 
								<spring:message code="operators"></spring:message>：</label>
								<div class="col-sm-2" >
									<select  id="operator" class="form-control" disabled="disabled" >
										<option value="${sessionScope.user.NWOperator}" selected="selected" >${sessionScope.user.NWOperator}</option>
									</select>
								</div>
							</div>		
						</c:otherwise>
						</c:choose>
						</div >
					</div>
					</div>
	<div id="panorama"></div>
<script type="text/javascript">

	//调用时间插件
	 $(function () {
       $(".ui_timepicker").datetimepicker({
           showSecond: true,
           timeFormat: 'hh:mm:ss',
           stepHour: 1,
           stepMinute: 1,
           stepSecond: 1
       });
   });
   function check(){
			var opera=$("#operator").val();
			var startDate=$("#startDateControl").val();
			var endDate=$("#endDateControl").val();


		if(startDate=='' || opera=='' || endDate==''|| startDate==null || opera==null || endDate==null ){
		return false;
		}
		else if(startDate>endDate)
	    {  
			return false;
	     } else{	
	    	 return true;
	     } 
	}

    
    
   
   
   var labelPosition=new Array();
   var labelOptions=new Array();

   var label=new Array();
   
   //经度
   var longitudes = new Array();
   //维度数组
   var dimensionalitys = new Array();
   var appname=new Array();
   
    
    var panorama = new BMap.Panorama('panorama');
    panorama.setPosition(new BMap.Point(116.403925,39.913903));//坐标点在天安门


   
                         
 	$(document).ready(function(){
 	 //遍历数组添加坐标
 	  var obj='${strappTraffics}';
 	 var obj2;
 	 // alert(obj);
 	 if (obj!=null || obj!="") {
 		obj2 = JSON.parse(obj);
 	 	  alert(obj2);
 	 	 for (var l = 0; l < obj2.length; l++) {
 	 		/*  alert(obj2[l].user_lat);
 	 		alert(obj2[l].user_lon);
 	 		  */
 	 		  dimensionalitys[l]=obj2[l].gpslat;
 	 		  longitudes[l]=obj2[l].gpslon;
 	 		  appname[l]=obj2[l].app_name;
 	 	} 
 	 	alert(obj2[1].user_lon);
 	 	showApp(dimensionalitys,longitudes,appname);
	} 
 	});
        
    /*
 	 labelPosition[0] = new BMap.Point(116.403925,39.913903);
     labelOptions[0] = {
         position: labelPosition[0],
       altitude:5
     };//设置标注点的经纬度位置和高度
     label[0] = new BMap.PanoramaLabel('天安门广场', labelOptions[0]);
     panorama.addOverlay(label[0]);//在全景地图里添加该标注
     panorama.setPov(label[0].getPov()); //修改点的视角，朝向该label

     label[0].addEventListener('click', function() { //给标注点注册点击事件
       panorama.setPov({  //修改点的视角
         pitch: 10, 
         heading: 20
       });
     });
          
     labelPosition[1] = new BMap.Point(116.404925,39.914903);
     labelOptions[1] = {
         position: labelPosition[1],
       altitude:5
     };//设置标注点的经纬度位置和高度
     label[1] = new BMap.PanoramaLabel('天安门广场2', labelOptions[1]);
     panorama.addOverlay(label[1]);//在全景地图里添加该标注
     panorama.setPov(label[1].getPov()); //修改点的视角，朝向该label

     label[1].addEventListener('click', function() { //给标注点注册点击事件
       panorama.setPov({  //修改点的视角
         pitch: 10, 
         heading: 20
       });
     });
            
 	*/
 	
 	function showApp(dimensionalitys,longitudes,appname){
 		
 		for (var i = 0; i < dimensionalitys.length; i++) {
 			labelPosition[i] = new BMap.Point(longitudes[i],dimensionalitys[i]);
 			alert(5555);
 	 	     labelOptions[i] = {
 	 	         position: labelPosition[i],
 	 	       altitude:5
 	 	     };//设置标注点的经纬度位置和高度
 	 	     label[i] = new BMap.PanoramaLabel(appname[i], labelOptions[i]);
 	 	     panorama.addOverlay(label[i]);//在全景地图里添加该标注
 	 	     panorama.setPov(label[i].getPov()); //修改点的视角，朝向该label

 	 	     label[i].addEventListener('click', function() { //给标注点注册点击事件
 	 	       panorama.setPov({  //修改点的视角
 	 	         pitch: 10, 
 	 	         heading: 20
 	 	       });
 	 	     });
		}
 	     
 	}
 	
 	
     panorama.addEventListener('position_changed', function(e){ //全景位置改变事件
  	   panoramaCallBack(e);
  	});
  	panorama.addEventListener('pov_changed', function(e){ //全景视角改变事件
  		panoramaCallBack(e);
  	});
  	function panoramaCallBack(e){ //事件回调函数
  		if (e.type == 'onpov_changed') { 
  			document.getElementById('svInfoText').innerHTML="全景视角为：<br/>"+"水平视角："+panorama.getPov().heading+"<br/>垂直视角："+panorama.getPov().pitch;
  		}
  		else if (e.type=='onposition_changed') {
  			chagStreet();
  			document.getElementById('svInfoText').innerHTML="全景位置点为："+panorama.getPosition().lng+","+panorama.getPosition().lat;
  		}
  	}
    
    
        function chagStreet(){
           if (check()) {
			var opera=$("#operator").val();
			var startDate=$("#startDateControl").val();
			var endDate=$("#endDateControl").val();
			
			 $.ajax({
	         	   type:"post",
	         	   url : '<%=basePath%>showAllStreetScape',
	      				dataType : "json",
	      				async : true,
	      				data : {
	      					"network_name":opera,
	      					"start_time":startDate,
	      					"end_time":endDate
	      				},

	      				success : function(datas) {
	      					$.each(datas, function(i, item) {
	      		 	 		  dimensionalitys[i]=item.gpslat;
	      		 	 		  longitudes[i]=item.gpslon;
	      		 	 		  appname[i]=item.app_name;
	      					});
	      					showApp(dimensionalitys,longitudes,appname);
	      				}
	      			});	
			 }
        }
      	 
   
  </script>
</body>
</html>