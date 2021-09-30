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
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/date/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/date/daterangepicker.jQuery.js"></script>
<link rel="stylesheet" href="<%=basePath%>css/ui.daterangepicker.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css"
	type="text/css" title="ui-theme" />

<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>

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

.form-control {
	width: 80%;
}

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
<body class=" theme-blue">
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
								<div class="col-sm-3">
									<input type="text" id="dateControl" value="${dayTime }" class="form-control">
								</div>
							</div>
							<c:choose>
								<c:when test="${sessionScope.user.role=='admin'}">
									<div id="control2" class="form-group">
										<label class="col-sm-1 control-label" for="operator">
											<spring:message code="operators"></spring:message>：
										</label>
										<div class="col-sm-3" style="width: 25%;">
											<select type="text" id="operator" class="form-control">
												<option value="ALL">ALL</option>
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
											<spring:message code="operators"></spring:message>：
										</label>
										<div class="col-sm-3" style="width: 25%;">
											<select type="text" id="operator" class="form-control"
												disabled="disabled">
												<option value="${sessionScope.user.NWOperator}">${sessionScope.user.NWOperator}</option>
											</select>
										</div>
									</div>
								</c:otherwise>
							</c:choose>
							<div class="col-sm-2">
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
						<div id="main" style="height: 600px"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="<%=basePath%>js/echarts-all.js"></script>
	<script src="<%=basePath%>js/load.js"></script>
	<script type="text/javascript">
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	
		//提交筛选信息，触发JS
		function testPointRank_control(){
			 /* 调用加载层 */
		    reqLoading(true);
			var dateControl = $('#dateControl').val();
			var operator = $('#operator').val();
			$.ajax({
				type : "post",
				url : "<%=basePath%>nwquality_testpointRank_control",
				async : false,
				data : {"daytime" : dateControl,"nwOperator" : operator},
				dataType : "json",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				success : function(data) { 
					/* 调用加载层 */
				     reqLoading(false);
					var testPointRankChart_control = echarts.init(document.getElementById('main'));
					testPointRankChart_control.setOption(data);
				}
			}); 
		}
		//左边树导航栏触发
		var testPointRankChart = echarts.init(document.getElementById('main'));
		var option = ${optionStr};
		testPointRankChart.setOption(option);
		
	</script>
</html>