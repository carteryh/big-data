<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="cn.itcast.entity.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<meta charset="utf-8">
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title><spring:message code="title">
	</spring:message></title>

<!-- 时间插件 -->
<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/date/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/date/daterangepicker.jQuery.js"></script>
<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
	type="text/css" title="ui-theme" />

<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>

<!-- 直方图控制JS -->
<script type="text/javascript" src="<%=basePath%>js/histogramControl.js"></script>

<!-- 样式控制 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">
<link type="text/css" href="<%=basePath%>css/conditions.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">

<script type="text/javascript">
	$(function() {
		var uls = $('.sidebar-nav > ul > *').clone();
		uls.addClass('visible-xs');
		$('#main-menu').append(uls.clone());
	});
	//调用时间插件
	$(function() {
		$('input').daterangepicker();
	});
	

</script>
<style type="text/css">
.control-label {
	text-align: right;
	padding-top: 7px;
}

/* .form-control {
	width: 80%;
} */

.btn-primary {
	width: 70px;
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
<body class="theme-blue">
	<div style="width: 100%; height: 100%">
		<div class="header">
			<div class="panel panel-default">
				<p class="panel-heading">
					<spring:message code="condition"></spring:message>
				</p>
				<div class="panel-body">
					<div class="row">
						<div id="control">
							<div id="control1" class="form-group" style="width: 24%;">
								<label class="col-sm-1 control-label" for="operator" style="width: 25%;"> <spring:message
										code="date"></spring:message>:
								</label>
								<div class="col-sm-2" style="width: 75%;">
									<input type="text" id="dateControl" value="${dayTime}"
										name="date" class="form-control">
								</div>
							</div>
							<div id="control4" class="form-group">
								<label class="col-sm-1 control-label" for="nwName"
									style="width: 8%;"> <spring:message code="nmname"></spring:message>:
								</label>
								<div class="col-sm-1" style="width: 12%;">
									<select type="text" id="nwName" class="form-control">
										<option value="ALL"><spring:message code="all"></spring:message></option>
										<option value="CMCC">CMCC</option>
										<option value="CTCC">CTCC</option>
										<option value="CUCC">CUCC</option>
									</select>
								</div>
							</div>
							<div id="control3" class="form-group">
								<label class="col-sm-1 control-label" for="nwType" style="width: 7.7%;"> <spring:message 
										code="nmtype"></spring:message>:
								</label>
								<div class="col-sm-1" style="width: 10%;">
									<select type="text" id="nwType" class="form-control">
										<option value="ALL"><spring:message code="all"></spring:message></option>
										<option value="4G">4G</option>
										<option value="3G">3G</option>
										<option value="2G">2G</option>
									</select>
								</div>
							</div>
							<div id="control2" class="form-group">
								<label class="col-sm-1 control-label" for="speedType" style="width: 8%;">
									<spring:message code="networkQuality"></spring:message>:
								</label>
								<div class="col-sm-2" style="width: 12.5%;">
									<select type="text" id="speedType" class="form-control">
										<option value="DLSpeed"><spring:message code="downSpeed"></spring:message></option>
										<option value="ULSpeed"><spring:message code="upSpeed"></spring:message></option>
										<option value="Latency"><spring:message code="latency"></spring:message></option>
									</select>
								</div>
							</div>
							<div class="col-sm-1">
								<button type="submit" type="button" class="btn btn-danger" onclick="hotTelRank_control()">
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
					<div class="row" id="report">
						<div style="height: 600px" id="main"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="<%=basePath%>js/echarts-all.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/load.js"></script>
	<script type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	
		//提交筛选信息，触发JS(直方图)
		function hotTelRank_control(){
			 /* 调用加载层 */
		    reqLoading(true);
			var dateControl = $('#dateControl').val();
			var speedType = $('#speedType').val();
			var nwType = $('#nwType').val();
			var nwName = $('#nwName').val();
		    $.ajax({
				type : "post",
				url : "<%=basePath%>nwquality_mobileNQRank_control",
				data : {
					"daytime" : dateControl,
					"speedType" : speedType,
					"nwType" : nwType,
					"nwOperator":nwName
				},
				ContentType : "application/x-www-form-urlencoded; charset=UTF-8",
				dataType : "json",
				success : function(data) {
					/* 调用加载层 */
				     reqLoading(false);
					var mobileNQRank_control = echarts.init(document.getElementById('main'));
					mobileNQRank_control.setOption(data);
				}
			});
		}
	
		//直方图
	 	var mobileNQRank = echarts.init(document.getElementById('main'));
		var option=${optionStr};

		mobileNQRank.setOption(option); 
		
		
	</script>
</html>