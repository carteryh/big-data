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
<script src="<%=basePath%>js/jquery-1.11.1.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">

<!-- Le fav and touch icons -->
<!-- Demo page code -->

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

.form-control {
	margin-left: 40px;
	width: 60%;
	
}
</style>

<script type="text/javascript">
	$(function() {
		var uls = $('.sidebar-nav > ul > *').clone();
		uls.addClass('visible-xs');
		$('#main-menu').append(uls.clone());
	});
</script>
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
			<div class="panel panel-default">
				<p class="panel-heading">筛选条件</p>
				<div class="panel-body">

					<div class="row">
						<div id="control1" class="col-xs-4">
							<select type="text" class="form-control">
							<option>1</option>
							<option>2</option>
							<option>3</option>
							</select>
						</div>
						<div id="control2" class="col-xs-4">
							<select type="text" class="form-control">
							<option>1</option>
							<option>2</option>
							<option>3</option>
							</select>
						</div>
						<div id="control3" class="col-xs-4">
							<select type="text" class="form-control">
							<option>1</option>
							<option>2</option>
							<option>3</option>
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="main-content">
			<!-- 右侧上层图表 -->
			<div class="panel panel-default">
				<a href="#page-stats" class="panel-heading" data-toggle="collapse">图表</a>
				<div id="page-stats" class="panel-collapse panel-body collapse in">

					<div class="row">
						<div id="main" style="height: 600px"></div>
					</div>

				</div>
			</div>
			<!-- 右侧下面表格 -->
			<div class="row">
				<div class="col-sm-12 col-md-12">
					<div class="panel panel-default">
						<div class="panel-heading no-collapse">
							表格<span class="label label-warning">+10</span>
						</div>
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>First Name</th>
									<th>Last Name</th>
									<th>Username</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>Mark</td>
									<td>Tompson</td>
									<td>the_mark7</td>
								</tr>
								<tr>
									<td>Ashley</td>
									<td>Jacobs</td>
									<td>ash11927</td>
								</tr>
								<tr>
									<td>Audrey</td>
									<td>Ann</td>
									<td>audann84</td>
								</tr>
								<tr>
									<td>John</td>
									<td>Robinson</td>
									<td>jr5527</td>
								</tr>
								<tr>
									<td>Aaron</td>
									<td>Butler</td>
									<td>aaron_butler</td>
								</tr>
								<tr>
									<td>Chris</td>
									<td>Albert</td>
									<td>cab79</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>


	<script src="lib/bootstrap/js/bootstrap.js"></script>
	<script src="<%=basePath%>js/echarts-all.js"></script>
	<script type="text/javascript">
		// 基于准备好的dom，初始化echarts图表
		var myChart = echarts.init(document.getElementById('main'));

		var option = {
			tooltip : {
				show : true
			},
			legend : {
				data : [ '销量' ]
			},
			xAxis : [ {
				type : 'category',
				data : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				"name" : "销量",
				"type" : "bar",
				"data" : [ 5, 20, 40, 10, 10, 20 ]
			} ]
		};

		// 为echarts对象加载数据 
		myChart.setOption(option);
	</script>
</html>