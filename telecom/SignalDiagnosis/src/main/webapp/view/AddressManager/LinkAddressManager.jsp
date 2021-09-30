<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>CNItcast</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="<%=basePath%>img/logo.ico" rel="Shortcut Icon">

<script type="text/javascript"
	src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/theme.css">
<!-- bootstrap加载 -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/bootstrap.css">
	
	<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
	
	<style type="text/css">
	a:HOVER {
	background-color: red;
	text-decoration: none;
	background: #f7ece1;
}
a:FOCUS {
	text-decoration: none;
	background-color: red;
	background: #f7ece1;
}
a:ACTIVE {
	background-color: red;
	background: #f7ece1;
}
span{
float: left;
margin-top: 15px;
margin-bottom: 10px;
}
	
	</style>
</head>
<body style="background-repeat: repeat;background-image: url('<%=basePath%>img/274.jpg'); ">
<div class="container-fluid" id="addAddr" style="display: none;">
		<div class="row-fluid" >
		  <div class="col-sm-2" >
		  </div>
		  
			<div class="col-sm-8" style="background-color: #f8f7f5;margin-top: 8%;margin-bottom: 20%;opacity:0.8;filter:alpha(opacity=70);height: 305px;padding-right: 4px;">

				<h3 class="text-center" >常用网址</h3>
				
				<p>
				 <a class="glyphicon glyphicon-edit" style="float: right;" id="reBack"   href="#">返回</a>
                </p>
                
						<div class="row-fluid">
							<div class="col-sm-12">
								<div class="col-sm-1" >
								</div>
								
								<div class="col-sm-10 text-center;" style="margin-top: 8%;">
								<form class="form-inline" role="form" style="float: left">
                                       <div class="form-group" style="float: left">
                                            <label class="sr-only" for="rname">名称</label>
                                            <input type="text" class="form-control" id="rname" value=""  placeholder="请输入名称">
                                        </div>
                                        <div class="form-group" style="float: left">
                                            <label class="sr-only" for="UrlAddress">地址</label>
                                            <input type="text" class="form-control" id="UrlAddress" value="" placeholder="请输入地址">
                                        </div>
                                    <div class="form-group" id="naviList" style="float: left">
                                            <label for="naviSelect" class="sr-only">选择列表</label>
                                               <select class="form-control" name="" id="naviSelect" >
                                                         <option value="">请选择分类</option>
                                                         <c:forEach items="${navigationAddresses}" var="navAddress">
                                                         <option value="${navAddress.nid}">${navAddress.nvaName}</option>
                                                         </c:forEach>
                                                         <option style="color: blue" id="addNavi" class="glyphicon glyphicon-plus" value="">添加分类</option>
                                                </select>
                                    </div>
                                    <!-- 添加分组 -->
                                    <div class="form-group" style="float: left;display: none;" id="naviName">
                                            <label class="sr-only" for="navigs">分组名称</label>
                                            <input type="text" class="form-control" id="navigs" value="" placeholder="分类名称">
                                        </div>
                                    <button type="submit" id="naviSub" class="btn btn-info" style="display: none;float: left" onclick="addNavi();">添加</button>
                                    <button type="submit" id="naviRest" class="btn btn-default" style="display: none;float: left">取消</button>
                                    
                                    <button type="submit" id="addAdds" class="btn btn-info" style="float: left" onclick="addRelaAdd()">添加网址</button>
                                </form>
								</div>							
								<div class="col-sm-1" >
								</div>
							</div>
						</div>
						</div>  
		  <div class="col-sm-2" >
		  </div>
		</div>
	</div>
	
	
	
	
	<div class="container-fluid" id="listAddr">
		<div class="row-fluid" >
		  <div class="col-sm-2" >
		  </div>
			<div class="col-sm-8" style="background-color: #f8f7f5;margin-top: 8%;margin-bottom: 4%;opacity:0.8;filter:alpha(opacity=70);">
			<!-- background-color: #f7ece1 style="margin: 0 0;padding: 0 0;"-->
				<h3 class="text-center" >常用网址</h3>
				
				<p>
				 <a class="glyphicon glyphicon-edit" style="float: right;" id="eidtAdress"   href="#">编辑</a>
                 <a class="glyphicon glyphicon-plus" style="float: right;" id="addAdress"   href="#">添加</a>
                </p>
                
				<ul style="list-style: none;">
				<!-- 分类不为空的地址栏 -->
					<c:forEach items="${navigationAddresses}" var="navAddress">
						<div class="row-fluid">
							<div class="col-sm-12">
								<div class="col-sm-1" style="padding: 0 0;">
									<p class="text-center h4" >
									<span  class="glyphicon glyphicon-remove-sign" style="display: none;cursor: pointer;margin-top: 2px;margin-bottom: 11px;"  onclick="deleteNavi('${navAddress.nid}');"></span>
									<b>${navAddress.nvaName}</b>
									</p>
								</div>
								<div class="col-sm-11 ">
									<c:forEach items="${addresses}" var="relaAddress">

                                        <c:choose>
										
                                         <c:when test="${relaAddress.category==navAddress.nid}">
											<li style="float: left;" id="${relaAddress.did}">
												<div style="margin-right: 15px;">
													<span  class="glyphicon glyphicon-remove-sign" style="display: none;cursor: pointer;"  onclick="deleteAddr('${relaAddress.did}');"></span>
													
													<a href="${relaAddress.address}" 
														target="_blank" style="float: left;">
														<h4 class="list-group-item-info">
														<img  src="<%=basePath%>img/be.png"  width="25" height="25" > ${relaAddress.name}
														</h4>
													</a>
													
												<span  class="glyphicon glyphicon-edit"  style="display: none;cursor: pointer;text-align: center;"  onclick="editAddr('${relaAddress.did}','${relaAddress.name}','${relaAddress.address}');"></span>
												</div>
												
											</li>
										</c:when>
										</c:choose>
									</c:forEach>
								</div>
							</div>
						</div>

					</c:forEach>
					
					<!-- 分类为空的地址栏 -->
					
						<div class="row-fluid">
							<div class="col-sm-12">
								<div class="col-sm-1" style="padding: 0 0;">
									<p class="text-center h4" ><b>&nbsp;&nbsp;&nbsp;&nbsp;</b></p>
								</div>
								<div class="col-sm-11 ">
								<c:forEach items="${addresses}" var="relaAddress">
								<c:if test="${relaAddress.category==0}">
									<li style="float: left;" id="${relaAddress.did}">
										<div style="margin-right: 15px;">
													<span  class="glyphicon glyphicon-remove-sign" style="display: none;cursor: pointer;"  onclick="deleteAddr('${relaAddress.did}');"></span>
													<a href="${relaAddress.address}" 
														target="_blank" style="float: left;">
														<h4 class="list-group-item-info">
														<img  src="<%=basePath%>img/be.png"  width="25" height="25" > ${relaAddress.name}
														</h4>
													</a>	
												<span  class="glyphicon glyphicon-edit"  style="display: none;cursor: pointer;text-align: center;"  onclick="editAddr('${relaAddress.did}','${relaAddress.name}','${relaAddress.address}');"></span>
										</div>			
									</li>
									 </c:if>
									 	</c:forEach>
								</div>
							</div>
						</div>
                     
				
					
				</ul>
			</div>
			 <div class="col-sm-2" >
		  </div>
		</div>
	</div>
	<script type="text/javascript">
	
	/* 关闭父页面的遮罩层 */
	 window.parent.reqLoading(false);
	
	   function checkRelaAddr(){
			var rname=$("#rname").val();
			var UrlAddress=$("#UrlAddress").val();
			var navname=$("#naviSelect option:selected").val();
			
			if(rname=='' || UrlAddress=='' || UrlAddress==null || rname==null || navname=='' ||navname==null){
			return false;
			}
			else
		    {  
		    return true;
		     }  
		}
	   function checkNaviAddr(){
		   var navi=$("#navigs").val();
			
			if(navi=='' || navi==null){
			return false;
			}
			else
		    {  
		    return true;
		     }  
		}
	   function checkRelaEditAddr(){

		}
	//取消添加分类
	$('#naviRest').on('click', function(){
		$("#naviList").css({display:"block"});
		$("#addAdds").css({display:"block"});
		$("#naviSub").css({display:"none"});
		$("#naviRest").css({display:"none"});
		$("#naviName").css({display:"none"});
	});
	
	
	//添加分类连接
	$('#addNavi').on('click', function(){
		$("#naviList").css({display:"none"});
		$("#addAdds").css({display:"none"});
		$("#naviSub").css({display:"block"});
		$("#naviName").css({display:"block"});
		$("#naviRest").css({display:"block"});
	});
	//添加分类
	function addNavi(){
		var navi=$("#navigs").val();

		if(checkNaviAddr()){
			$.ajax({
			   	   type:"post",
			   	   url : '<%=basePath%>addNaviAddress',
				   dataType : "json",
				   async :true,
				   data : {
					"nvaName" : navi
					},
				   success : function(data) {

							$("#naviList").css({display:"block"});
							$("#addAdds").css({display:"block"});
							$("#naviSub").css({display:"none"});
							$("#naviRest").css({display:"none"});
							$("#naviName").css({display:"none"});
					   $("#naviSelect").html("");
			              $("#naviSelect").append("<option >请选择分类</option>");
			              $.each(data,function(i,item){
			                  $("#naviSelect").append("<option value="+item.nid+">"+item.nvaName+"</option>");
			              });
				   }
					});	
		}
		
	}
	//添加网址
	function addRelaAdd(){
		var rname=$("#rname").val();
		var UrlAddress=$("#UrlAddress").val();
		var navname=$("#naviSelect option:selected").val();

	/* 	alert(rname+" "+UrlAddress+" "+navname); */
		if(checkRelaAddr()){
			location.href="<%=basePath%>addAdress?name="+rname+"&address="+UrlAddress+"&category="+navname;
		}
	}
	//添加网址
	function deleteNavi(nid){
		location.href="<%=basePath%>deleteNaviAdress?nid="+nid;
	}

	//编辑切换
	$('#eidtAdress').on('click', function(){
		$("span").toggle();
	});


	//添加地址
	$('#addAdress').on('click', function(){
		$("#addAddr").css({display:"block"});
		$("#listAddr").css({display:"none"});
	});
	
	
	//取消地址添加
	$('#reBack').on('click', function(){
		$("#addAddr").css({display:"none"});
		$("#listAddr").css({display:"block"});
	});
	
	//删除地址
	function deleteAddr(did){

		$("#"+did).remove();
		 $.ajax({
		   	   
		   	   type:"post",
		   	   url : '<%=basePath%>deleteAdress',
			   dataType : "json",
			   async :true,
			   data : {
				"did" : did
				},
			   success : function(data) {	
						}
					});	

	}
	
	//编辑地址
	function editAddr(did,name,address){
		var pageii=$.layer({
		    type: 1,
		    title:  ['<b>修改网址</b>', 'background:#EFEEEA;'],
		    area: ['auto', 'auto'],
		    maxmin: false,//是否添加最大化、最小化工具
		    border: [1, 0.3, '#EFEEEA'], //边框大小、透明度、颜色
		    shade: [0.5, '#000'],//添加遮罩
		    shadeClose: false,
		    closeBtn: [1, true], //去掉默认关闭按钮
		    shift: 'top', //从左动画弹出
		    bgcolor: '#ffffff',
		    fix: false,
		    time: 0, 
		    move: false,
		    page: {
		    html: '<div style="width:420px; height:200px; padding:20px; border:1px solid #ccc; text-align: center;">'+  
		   '<div ><form action="<%=basePath%>editRelaAddress" method="post"  >'+ 
		   '<input name="did" class="form-control" style="height:30px;width:220px;display:none;" value="'+did+'" ><br> '+
		   '<input name="name" class="form-control" style="height:30px;width:220px;margin-left:20%;" value="'+name+'" id="addName"><br> '+
		   '<input name="address" class="form-control" style="height:30px;width:220px;margin-left:20%;" value="'+address+'" id="addAddress"><br> '+
		   '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="tip-name" style="display:none; color: #008d97; "></span><br>'+
		   '<button class="btn btn-xs btn-success" style="background: #d21d45;width:40px;height:20px;" id="pageSub" type="submit"  >确定</button>'+
		   '<button class="btn btn-xs btn-success" style="background: #d21d45;width:40px;height:20px;" id="pageclose" type="reset"  >取消</button>&nbsp;&nbsp;&nbsp;&nbsp;'+
		   '</form></div></div>'
		    }
		});
		//自设关闭
		$('#pageclose').on('click', function(){
		    layer.close(pageii);
		});
		//提交之前进行检查
		$('#pageSub').on('click', function(){
		    var name=$("#addName").val();
		    var address=$("#addAddress").val();
		    
		    if(name=='' ){
			$("#tip-name").text("⊙ o ⊙ 请填写收藏网站名称.").fadeIn(2000);
			return false;
			}else if(address==''){
				$("#tip-name").text("⊙ o ⊙ 请填写收藏网站地址.").fadeIn(2000);
				return false;
			}
 
			return true;
		});


	}
	</script>
</body>
</html>