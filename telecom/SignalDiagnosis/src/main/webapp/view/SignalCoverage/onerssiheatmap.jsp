<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM"></script>
    
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    
   <!--   <script type="text/javascript" src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>-->
	
<title>热力图功能示例</title>

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
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript"  src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js">

	</script>
<script type="text/javascript">

//调用时间插件
$(function() {
	$('#dateControl').daterangepicker();
});
</script>

<style type="text/css" src="http://api.map.baidu.com/api?v=2.0&ak=oEyxGHjElrlxlcK7EBpr9tIM">
 html{
        margin-top:0px;
        margin-bottom:1px;
        width:100%;
        height:100%;
       background:#fffaf0;
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
    #container{
        width:100%;
        height:97%;
        margin-right: 3px;
        border-width:3px;
        border-left-style: groove;
        border-top-style:groove;
        border-bottom-style: groove;
        border-right-style:groove;
         border: 1px solid #ccc;
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
.element-style {
    background-color: #f3f1ec;
    color: #000;
    overflow: hidden;
    position: relative;
    text-align: left;
    top: 5px;
    z-index: 0;
}
.row{
/* margin-left:10%; */
width: 100%;
}
</style>
</head>
<body  onload="init()" >
  
    

			<div style="width: 100%; height: 15%;">
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
    					 <c:choose>
						<c:when test="${sessionScope.user.role=='admin'}">
						 <div id="control2" class="form-group">
								<label class="col-sm-3 control-label" for="operator"> <spring:message
										code="operators"></spring:message>:
								</label>
								<div class="col-sm-3">
									<select type="text" id="operator" class="form-control form-controloperator">
									   <option value="all" selected="selected">ALL</option>
										<option value="CMCC">CMCC</option>
										<option value="CTCC">CTCC</option>
										<option value="CUCC">CUCC</option>
									</select>
								</div>
							</div>
						</c:when>

						<c:otherwise>
						<div id="control2" class="form-group" >   <!-- onload="loadnetworktype()" > -->
								<label class="col-sm-3 control-label" for="operator"> 
								<spring:message code="operators"></spring:message>：</label>
								<div class="col-sm-3" >
									<select type="text" id="operator" class="form-control" disabled="disabled" >
									   <option value="all" selected="selected">ALL</option>
										<option value="${sessionScope.user.NWOperator}" id="operatoruser" selected="selected" >${sessionScope.user.NWOperator}</option>
									</select>
								</div>
							</div>
							
						</c:otherwise>
						</c:choose>
    						<div class="col-sm-2"></div>
							<div class="col-sm-1" >
								<button type="submit" class="btn btn-danger" onclick="selctPointRank()">
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
    
    
    </div>

	<div id="container"  class="element-style"></div>   <!--  onload="loadnetworktype()" ></div>-->
	
	<script src="<%=basePath%>js/load.js"></script>
	<script language="javascript" type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	
	  var now= new Date();

	  var year=now.getFullYear();

	  var month=now.getMonth();

	  var date=now.getDate();

	 function check(){
			var operator=$("#operator").val();
			var dateControl=$("#dateControl").val();
			var mobile_network_type=$("#mobile_network_type").val();
			var regSize = /^[1-9]+[0-9]*]*$/; 
			var regName =/^[a-zA-Z]{2,128}$/;
			if(operator=='' || dateControl=='' || dateControl==null || operator==null  || mobile_network_type=='' ||mobile_network_type==null){
			return false;
			}
			else
		    {  
		    return true;
		     }  
		}
	   function selctPointRank(){
				init();
	   }
	   function returns(){
		   
		   window.location.href="<%=basePath%>view/SignalCoverage/showRSSI.jsp";
			
	   }
	
	   

	var map = new BMap.Map("container");          // 创建地图实例
	var point = new BMap.Point(<%=request.getParameter("lon")%>,<%=request.getParameter("lat")%>);
	map.centerAndZoom(point, 19);             // 初始化地图，设置中心点坐标和地图级别
	map.enableScrollWheelZoom(); // 允许滚轮缩放
	map.addControl(new BMap.NavigationControl()); //添加标准地图控件(左上角的放大缩小左右拖拽控件)
	map.addControl(new BMap.MapTypeControl({
		mapTypes : [ BMAP_NORMAL_MAP, BMAP_HYBRID_MAP ]
	}));
	//map.addEventListener("tilesloaded",function(){init();});
	heatmapOverlay = new BMapLib.HeatmapOverlay({"radius" : 20,"opacity":0.4});
	map.addOverlay(heatmapOverlay);
	map.disableDragging();
	var xhr;
	function createXHR(){
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
	    }else if (window.XMLHttpRequest) {
	        xhr=new XMLHttpRequest();
	    }else{
	        alert("您的浏览器版本过低请求不能够创建");
	    }
	}

	 
var points = null;


	function init() {
	    reqLoading(true);
		var opera=$("#operator").val();
		var dat="2/27/2015 - 3/6/2019";
		var networktype="ALL";
		var user=$("#operatoruser").val();
		
		var bs = map.getBounds();
	    //用户为中心的点
		//左下
		 zxj=point.lng-0.0014;
		 zxw=point.lat-0.000464;
		//右上
		 ysj=point.lng+0.0014;
		 ysw=point.lat+0.000464;
		$.ajax({
			xhr :createXHR(),
			url : '<%=basePath%>oneloadheatmap?daytime='+dat+'&nwOperator='+opera+'&networktype='+networktype,
				async : false,
				data : {
					bssw_lng : zxj,
					bsne_lng : ysj,
					bssw_lat : zxw,
					bsne_lat : ysw
				},
				//请求回调结果
				success : function(datas) {
				    reqLoading(false);
					var dataObj=eval("("+datas+")");//转换为json对象
					showdata(dataObj);
					datas="";
				},
				error: function(request, status, error){    
				    alert(" 系统错误！请重试或联系管理员。");
				     }     
			});

		}

	 function showdata(datas){
		 var points =datas;
			heatmapOverlay.setDataSet({data : points,max : 120,opacity:20});
			setGradient();
	    }
	   function setGradient(){
	      // var gradient = { .0165 :"rgb(255,0,0)",   .72 :"rgb(225,215,0)", .86 :"rgb(225,215,0)", .825 :"rgb(225,215,0)", .975 :"rgb(0,225,75)"};
	      var gradient = { .1965 :"rgb(255,0,0)",   .37 :"rgb(225,215,0)",   .55 :"rgb(34,139,34)", .74 :"rgb(0,206,209)", .88 :"rgb(0,0,209)", .99 :"rgb(155,48,255)"};
	
	      var colors = document.querySelectorAll("input[type='color']");
	     	colors = [].slice.call(colors,0);
	     	colors.forEach(function(ele){
				gradient[ele.getAttribute("data-key")] = ele.value; 
	     	});
	        heatmapOverlay.setOptions({"gradient":gradient});
	    }
	   
	   var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_RIGHT,offset : new BMap.Size(8, 31)});   //设置版权控件位置
		map.addControl(cr); //添加版权控件

	   var bs = map.getBounds();   //返回地图可视区域
		cr.addCopyright({id: 1, content: '<img  src=<%=basePath%>img/rssibs.png>', bounds: bs});   


	</script>
</body>
</html>
