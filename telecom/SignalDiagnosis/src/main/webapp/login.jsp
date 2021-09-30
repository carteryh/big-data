<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib uri="http://www.springframework.org/tags" prefix="spring"  %>
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
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title><spring:message code="title"></spring:message></title>

<!-- bootstrap加载 -->
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath %>js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="css/theme.css">

<script type="text/javascript">
	$(function() {
		var match = document.cookie.match(new RegExp('color=([^;]+)'));
		if (match)
			var color = match[1];
		if (color) {
			$('body').removeClass(function(index, css) {
				return (css.match(/\btheme-\S+/g) || []).join(' ')
			})
			$('body').addClass('theme-' + color);
		}

		$('[data-popover="true"]').popover({
			html : true
		});

	});
</script>
<style type="text/css">
#line-chart {
	height: 300px;
	width: 800px;
	margin: 0px auto;
	margin-top: 1em;
}

.navbar-default .navbar-brand, .navbar-default .navbar-brand:hover {
	color: #fff;
}
</style>

<script type="text/javascript">
	$(function() {
		var uls = $('.sidebar-nav > ul > *').clone();
		uls.addClass('visible-xs');
		$('#main-menu').append(uls.clone());
	});
</script>
 
</head>
<body class=" theme-blue">
	<div class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<a class="" href="./"><span class="navbar-brand"><span
					class="glyphicon glyphicon-send"></span>CNItcast</span></a>
		</div>
	</div>

	<div class="dialog">
		<div class="panel panel-default">
			<p class="panel-heading no-collapse"><spring:message code="login"></spring:message></p>
			<div class="panel-body">
				<form>
					<div class="form-group">
						<label><spring:message code="username"></spring:message></label> <input type="text"
							class="form-control span12" id="uname">
					</div>
					<div class="form-group">
						<label><spring:message code="pwd"></spring:message></label> <input type="password"
							class="form-control span12 form-control" id="upassword">
					</div>
					<a href="javascript:login();" class="btn btn-primary pull-right"><spring:message code="login"></spring:message></a>
					<a href="#" id="loginWrong" style="display:none;"><spring:message code="loginWrong"></spring:message></a>
					<div class="clearfix"></div>
				</form>
			</div>
		</div>
		<p>
			
		</p>
	</div>
	<script type="text/javascript">
		$("[rel=tooltip]").tooltip();
		$(function() {
			$('.demo-cancel-click').click(function() {
				return false;
			});
		});
		
		
		function login(){
			var uname=$("#uname").val();
			var upassword=$("#upassword").val();
	        var url="<%=basePath%>user_login";
	        var args={"uname":uname,"upassword":upassword};

 			$.getJSON(url, args, function(json) {
			if(json){
			location.href='<%=basePath%>main.jsp';
			 }else{
				 $("#loginWrong").show();
			 }
			});  
		}

	</script>
</html>