<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="cn.itcast.entity.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String viewId = request.getParameter("viewId");
%>
<%
	Object obj = request.getSession().getAttribute("user");
	if (!(obj instanceof User)) {
		response.sendRedirect(basePath + "timeout.jsp");
	}
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title>CNitcast</title>

<!-- 时间插件 -->
<link type="text/css"
	href="<%=basePath%>js/dateToSecond/css/jquery-ui-1.8.17.custom.css"
	rel="stylesheet" />
<link type="text/css"
	href="<%=basePath%>js/dateToSecond/css/jquery-ui-timepicker-addon.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/dateToSecond/js/jquery-ui-timepicker-en-US.js"></script>

<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>

<!-- 样式控制 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">
<link type="text/css" href="<%=basePath%>css/conditions.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap.css">

<script type="text/javascript">
	$(function() {
		var uls = $('.sidebar-nav > ul > *').clone();
		uls.addClass('visible-xs');
		$('#main-menu').append(uls.clone());
	});
	//调用时间插件
	 $(function () {
        $(".ui_timepicker").datetimepicker({
            //showOn: "button",
            //buttonImage: "./css/images/icon_calendar.gif",
            //buttonImageOnly: true,
            showSecond: true,
            timeFormat: 'hh:mm:ss',
            stepHour: 1,
            stepMinute: 1,
            stepSecond: 1
        })
    })
</script>
<style type="text/css">
.control-label {
	text-align: right;
	padding-top: 7px;
}

.btn-primary {
	width: 70px;
}

.onerowControl {
	display: inline;
	float: left;
}
.control1{
	width: 35%;
}
.control2-5{
	width: 14%;
}
.control6{
	width: 9%;
	text-align: center;
}
.label1{
	width: 14%; 
	float: left; 
	text-align: center;
	padding-top: 7px;
}
.label2-5{
	width: 50%; 
	text-align: center;
	padding-top: 7px;
}
.control1>input{
	width: 43%;
}
.control2-5>select{
	width: 50%;
}
</style>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->


<!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
<!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
<!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
<!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->

<!--<![endif]-->
</head>
<body class=" theme-blue">
	<div style="width: 100%; height: 100%;">
		<div class="header">
			<div class="panel panel-default">
				<p class="panel-heading">
					<spring:message code="condition"></spring:message>
				</p>
				<div class="panel-body">
					<div class="row">
						<div>
							<divc class="onerowControl control1">
								<label for="startDate" class="label1">
									<spring:message code="date"></spring:message>:
								</label>
								<input type="text"  id="startDate" class="onerowControl ui_timepicker form-control">
								<input type="text" id="endDate" class="onerowControl ui_timepicker form-control">
							</div>
							<div class="onerowControl control2-5">
								<label for="dateSize" class="onerowControl label2-5">时间粒度:</label>
								<select class="onerowControl form-control" type="text" id="dateSize">
									<option>月</option>
									<option>日</option>
									<option>时</option>
								</select>
							</div>
							<div class="onerowControl control2-5">
								<label for="landmark" class="onerowControl label2-5">典型地标:</label>
								<select type="text" id="landmark" class="onerowControl form-control">
									<option>国贸</option>
								</select>
							</div>
							<div class="onerowControl control2-5">
								<label for="speedType" class="onerowControl label2-5">网络质量:</label>
								<select type="text" id="speedType" class="onerowControl form-control">
									<option value="DLSpeed"><spring:message code="downSpeed"></spring:message></option>
									<option value="ULSpeed"><spring:message code="upSpeed"></spring:message></option>
									<option value="Latency"><spring:message code="latency"></spring:message></option>
								</select>
							</div>
							<div class="onerowControl control2-5">
								<label for="networkType" class="onerowControl label2-5">网络制式:</label>
								<select type="text" id="networkType" class="onerowControl form-control">
									<option>4G</option>
									<option>3G</option>
									<option>2G</option>
									<option>WI-FI</option>
								</select>
							</div>
							<div class="onerowControl control6">
								<button type="button" class="btn btn-danger" onclick="testPointRank_control()">
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
			<div class="panel panel-default">
				<a href="#page-stats" class="panel-heading" data-toggle="collapse">
					<spring:message code="chart"></spring:message>
				</a>
				<div id="page-stats" class="panel-collapse panel-body collapse in">
					<div class="row">
						<div id="main" style="height: 380px"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$('.landmark_img').click(function(){
		$('.landmark_img').css("background-image","url('<%=basePath%>img/radio.png')");
		$(this).css("background-image","url('<%=basePath%>img/radio-red.png')");
	});
	</script>
	<script type="text/javascript">
		//提交筛选信息，触发JS
		function testPointRank_control(){
			var dateControl = $('#dateControl').val();
			var operator = $('#operator').val();
			$.ajax({
				type : "post",
				url : "<%=basePath%>
		nwquality_testpointRank_control",
						async : false,
						data : {
							"daytime" : dateControl,
							"nwOperator" : operator
						},
						dataType : "json",
						contentType : "application/x-www-form-urlencoded; charset=UTF-8",
						success : function(data) {
							var testPointRankChart_control = echarts
									.init(document.getElementById('main'));
							testPointRankChart_control.setOption(data);
							$('#dateControl').val(dateControl);
						}
					});
		}
		//左边树导航栏触发
		var testPointRankChart = echarts.init(document.getElementById('main'));
		var option = $
		{
			optionStr
		};
		testPointRankChart.setOption(option);
	</script>
</html>