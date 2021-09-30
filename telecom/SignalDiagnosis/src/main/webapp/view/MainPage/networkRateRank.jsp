<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="varDesc" scope="page" class="cn.itcast.entity.VarDesc"></jsp:useBean>
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
<title><spring:message code="title">
	</spring:message></title>

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

<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>

<!-- 直方图控制JS -->
<script type="text/javascript" src="<%=basePath%>js/histogramControl.js"></script>
<!--  -->
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<!--  -->
<!-- 加载层start -->
<script type="text/javascript" src="<%=basePath %>js/load.js"></script>
<!-- 加载层end -->
<!-- 样式控制 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">
<link type="text/css" href="<%=basePath%>css/conditions.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">

<script type="text/javascript">
/* 关闭父页面的遮罩层 */
window.parent.reqLoading(false);

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
	<div style="width: 100%; height: 100%">
		<div class="header">

			<form action="<%=basePath%>nwquality_networkRateRank">
				<div class="panel panel-default">
					<p class="panel-heading">
						<spring:message code="condition"></spring:message>
					</p>
					<div class="panel-body">
						<div class="row">
							<div id="control">
								<div id="control1" class="form-group">
									<label class="col-sm-1 control-label" for="operator"> <spring:message
											code="date"></spring:message>：
									</label>
									<div class="col-sm-3">
										<input type="text" id="dateControl"  value="${dayTime}" name="daytime" class="form-control">
									</div>
								</div>
								<c:choose>
									<c:when test="${sessionScope.user.role==varDesc.getRole('ADMIN')}">
										<div id="control2" class="form-group">
											<label class="col-sm-1 control-label" for="operator">
												<spring:message code="operators"></spring:message>：
											</label>
											<div class="col-sm-3" style="width: 15%;">
												<select type="text" id="nwOperator" name="nwOperator" class="form-control">
													
											<c:choose>
											<c:when test="${nwOperator==varDesc.getOperator('CMCC')}">
											<option value="CMCC">CMCC</option>
											<option value="ALL">ALL</option>
											<option value="CTCC">CTCC</option>
											<option value="CUCC">CUCC</option>
											</c:when>
											<c:when test="${nwOperator==varDesc.getOperator('CTCC')}">
											<option value="CTCC">CTCC</option>
											<option value="ALL">ALL</option>
											<option value="CMCC">CMCC</option>
											<option value="CUCC">CUCC</option>
											</c:when>
											<c:when test="${nwOperator==varDesc.getOperator('CUCC')}">
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
											
											</select>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div id="control2" class="form-group">
											<label class="col-sm-1 control-label" for="operator">
												<spring:message code="operators"></spring:message>：
											</label>
											<div class="col-sm-3" style="width: 15%;">
												<select type="text" id="nwOperator" name="nwOperator"class="form-control">	
													<option value="${sessionScope.user.NWOperator}">${sessionScope.user.NWOperator}</option>
												</select>
											</div>
										</div>
									</c:otherwise>
								</c:choose>
								<div id="control3" class="form-group">
									<label class="col-sm-1 control-label" for="operator"
										style="width: 9%;"> <spring:message code="speedType"></spring:message>：
									</label>
									<div class="col-sm-3" style="width: 15%;">
										<select type="text" id="speedType" name="speedType"  class="form-control">
											
											
											<c:choose>
											<c:when test="${speedType==varDesc.getSpeedType('DLSPEED')}">
											<option value="DLSpeed"><spring:message
													code="downSpeed"></spring:message></option>
											<option value="ULSpeed"><spring:message
													code="upSpeed"></spring:message></option>
											</c:when>
											<c:otherwise>
											<option value="ULSpeed"><spring:message
													code="upSpeed"></spring:message></option>
											<option value="DLSpeed"><spring:message
													code="downSpeed"></spring:message></option>
											</c:otherwise>
											</c:choose>
											
									
										</select>
									</div>
								</div>
								<div class="col-sm-2">
									<button type="submit"  type="button" class="btn btn-danger" 
										onclick="javascript:reqLoading(true);">
										<spring:message code="submit"></spring:message>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>

		<div class="main-content">
			<!-- 右侧上层图表 -->
			
			<c:choose>
			<c:when test="${optionStr==varDesc.getNodata()}">
			<div class="panel panel-default">
				<a href="#page-stats" class="panel-heading" data-toggle="collapse">
					<spring:message code="chart"></spring:message>
				</a>
				<div id="page-stats" class="panel-collapse panel-body collapse in">
					<div class="row">
						<div style="text-align:center;">没有数据!</div>
					</div>
				</div>
			</div>
			
			
			</c:when>
			<c:otherwise>
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
			</c:otherwise>
			</c:choose>
			</div>
	</div>

	<script src="lib/bootstrap/js/bootstrap.js"></script>
	<script src="<%=basePath%>js/echarts-all.js"></script>
	<script type="text/javascript">
		var networkRateRank = echarts.init(document
				.getElementById('main'));
		var option = ${optionStr};
		/* alert(option); */
		networkRateRank.setOption(option);
	</script>
 
</html>