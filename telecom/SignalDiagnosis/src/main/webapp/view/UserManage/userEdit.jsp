<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="cn.itcast.entity.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String id=request.getParameter("id");
	String uname=request.getParameter("uname");
	String upassword=request.getParameter("upassword");
%>
<%
	Object obj = request.getSession().getAttribute("user");
	if (!(obj instanceof User)) {
		response.sendRedirect(basePath + "timeout.jsp");
	}
	
%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title>CNitcast</title>
<!-- bootstrap加载 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>

<!-- 样式控制 -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/theme.css">

<link type="text/css" href="<%=basePath%>css/conditions.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap.css">

<!-- layer -->
<script src="<%=basePath%>js/jquery-1.11.1.js"></script>
<script src="<%=basePath%>layer/layer.min.js"></script>
<style type="text/css">
.control-label{
	display: inline;
	float: left;
	position: relative;
	margin-top: 15px; 
	margin-left: 30px;
}
.form-control{
	display: inline;
	float: left;
	width : 300px;
	position: relative;
	margin-top: 15px; 
}
.submit{
	margin-top: 15px; 
}

</style>

</head>
<body>
	<div id="add_main">
			<div class="form-group">
				<label for="uname" class="col-sm-4 control-label"><spring:message code="uname"></spring:message>:</label>
				<div class="col-sm-8">
					<input id="id" type="hidden" value="<%=id %>" name="id">
					<input id="uname" type="text" value="<%=uname %>" class="form-control" name="uname">
				</div>
			</div>
			<div class="form-group">
				<label for="upassword" class="col-sm-4 control-label"><spring:message code="upassword"></spring:message>&nbsp;&nbsp;:</label>
				<div class="col-sm-8">
					<input id="upassword" type="password" class="form-control" value="<%=upassword %>" name="upassword">
				</div>
			</div>
			<div class="form-group">
				<label for="role" class="col-sm-4 control-label"><spring:message code="role"></spring:message>&nbsp;&nbsp;:</label>
				<div class="col-sm-8">
					<select id="role" class="form-control" name="role">
						<option value="admin"><spring:message code="admin"></spring:message></option>
						<option value="operator"><spring:message code="operator"></spring:message></option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="NWOperator" class="col-sm-4 control-label"><spring:message code="operator"></spring:message>:</label>
				<div class="col-sm-8">
					<select id="NWOperator" class="form-control" name="NWOperator">
					    <option value="admin">admin</option>
						<option value="CMCC">CMCC</option>
						<option value="CTCC">CTCC</option>
						<option value="CUCC">CUCC</option>
					</select>
				</div>
			</div>
			<div class="form-group" style="text-align: center;">
      			<button type="submit" id="edit" class="btn btn-info"><spring:message code="edit"></spring:message></button>
  			</div>
	</div>
	<script type="text/javascript">
	 $('#edit').on('click',function(){
		var id =$('#id').val();
		var uname = $('#uname').val();
		var upassword = $('#upassword').val();
		var role = $('#role').val();
		var NWOperator = $('#NWOperator').val();
		 $.ajax({
				type : "post",
				url : "<%=basePath%>user_edit",
				data : {"id":id,"uname" : uname,"upassword" : upassword,"role" : role,"NWOperator":NWOperator},
				dataType : "json",
				async : false,
				success : function(data) {
				}
			});
		 window.parent.location.href='<%=basePath%>user_manage';
		 parent.layer.closeAll();
	}); 
	</script>
</body>
</html>