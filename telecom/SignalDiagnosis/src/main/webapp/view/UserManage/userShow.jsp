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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title>CNitcast</title>

<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>

<!-- 样式控制 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">

<link type="text/css" href="<%=basePath%>css/conditions.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">

<!-- layer -->
<script src="<%=basePath%>js/jquery-1.11.1.js"></script>
<script src="<%=basePath%>layer/layer.min.js"></script>
</head>
<body class=" theme-blue">
	<div style="width: 100%; height: 100%;">
		<div class="main-content">
			<!-- 右侧上层图表 -->
			<div class="panel panel-default">
				<div class="panel-heading no-collapse">
					<span class="pull-right">
						<button id="userAdd" type="button" class="btn btn-info"
							style="line-height: 0.8em;">
							<spring:message code="userAdd"></spring:message>
						</button>
					</span>
					<spring:message code="userManage"></spring:message>
				</div>
				<div id="page-stats" class="panel-collapse panel-body collapse in">
					<div class="row">
						<table class="table">
							<tr>
								<th width="20%">用户名</th>
								<th width="20%">密码</th>
								<th width="20%">角色</th>
								<th width="20%">运营商</th>
								<th width="20%">操作</th>
							</tr>
							<c:forEach var="user" items="${users }">
								<tr>
									<td>${user.uname}</td>
									<td>${user.upassword}</td>
									<td>${user.role}</td>
									<td>${user.NWOperator}</td>
									<td>
										<button type="button" onclick="userEdit(${user.id})"
											class="btn btn-warning">修改</button>
										<button id="userDel" onclick="userDel(${user.id})"
											type="button" class="btn btn-danger">删除</button>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	//删除用户
	function userDel(id){
		$.layer({
		    shade: [0],
		    area: ['auto','auto'],
		    dialog: {
		        msg: '您真的确定要删除吗？\n\n请确认！',
		        btns: 2,                    
		        type: 4,
		        btn: ['确定','取消'],
		        yes: function(){
		        	window.location.href='<%=basePath%>user_del?id='+id;
		        }, 
		    }
		});
	}
	//修改用户
	function userEdit(id){
		//window.location.href='<%=basePath%>user_get?id='+id;
		 $.ajax({
				type : "post",
				url : "<%=basePath%>user_get",
				data : {"id" : id},
				dataType : "json",
				async : false,
				success : function(data) {
					//window.location.href='<%=basePath%>view/UserManage/userEdit.jsp?id='+data.id+'&uname='+data.uname;
					var edit =	$.layer({
							type : 2,
							title : '修改用户信息',
							closeBtn : [ 0, true ],
							fix: true,
							offset : [ ($(window).height() - 400) / 2 + 'px', '' ],
							border : [ 5, 0.5, '#666' ],
							area : [ '450px', '300px' ],
							iframe : {
								src : '<%=basePath%>view/UserManage/userEdit.jsp?id='+data.id+'&uname='+data.uname+'&upassword='+data.upassword,
								scrolling: 'no'
							}
						});
				}
			});
	}
	
	//添加用户
	layer.useradd = function() {
	 var add =	$.layer({
			type : 2,
			title : '添加用户',
			closeBtn : [ 0, true ],
			fix: true,
			offset : [ ($(window).height() - 400) / 2 + 'px', '' ],
			border : [ 5, 0.5, '#666' ],
			area : [ '450px', '300px' ],
			iframe : {
				src : '<%=basePath%>view/UserManage/userAdd.jsp',
				scrolling: 'no'
			}
		});
	};
	$('#userAdd').on('click', function() {
		layer.useradd();
	});
	
	</script>
</body>
</html>