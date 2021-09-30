<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>CNitcast</title>

<!-- bootstrap加载 -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">
<script src="<%=basePath%>js/jquery-1.11.1.js"></script>
<script src="<%=basePath%>js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">

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

<style>
* {
	margin: 0;
	padding: 0;
}

body {
	font-family: 'Audiowide', cursive, arial, helvetica, sans-serif;
	color: white;
	font-size: 18px;
	padding-bottom: 20px;
}

.error-code {
	font-family: 'Creepster', cursive, arial, helvetica, sans-serif;
	font-size: 200px;
	color: white;
	color: rgba(255, 205, 255, 0.98);
	width: 50%;
	text-align: right;
	margin-top: 5%;
	text-shadow: 5px 5px hsl(0, 0%, 25%);
	float: left;
}

.not-found {
	width: 47%;
	float: right;
	margin-top: 5%;
	font-size: 50px;
	color: green;
	text-shadow: 2px 2px 5px hsl(0, 0%, 61%);
	padding-top: 70px;
}

.clear {
	float: none;
	clear: both;
}

.content1 {
	text-align: center;
	color: #EE9DFF;
	line-height: 30px;
}

input[type=text] {
	border: hsl(247, 89%, 72%) solid 1px;
	outline: none;
	padding: 5px 3px;
	font-size: 16px;
	border-radius: 8px;
}

a {
	text-decoration: none;
	color: #9ECDFF;
	text-shadow: 0px 0px 2px white;
}

a:hover {
	color: green;
}
</style> 
</head>
<body>
	<div class="dialog">
		<div style="position: relative;">
			超时啦，
			<p class="error-code">
				<img src="<%=basePath%>/img/han.png" width="100px" height="100px">
			</p>
			<p class="not-found" style="font-size: 15px;">此路不通，请绕行.</p>
		</div>
		<div class="clear"></div>
		<div class="content1">
			<br /> <a href="<%=basePath%>login.jsp" target="_parent">Go Home</a>
		</div>
	</div>
	<script src="lib/bootstrap/js/bootstrap.js"></script>
</html>