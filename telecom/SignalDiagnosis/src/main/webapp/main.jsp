<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="cn.itcast.entity.User" %>
<%@page import="java.util.Date" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	 request.setAttribute("date",new Date());
%>
<%
	Object obj = request.getSession().getAttribute("user");
	if (!(obj instanceof User)) {
		response.sendRedirect(basePath + "timeout.jsp");
	}
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<Link href="img/logo.ico" rel="Shortcut Icon">
<title>ITcast</title>

<!-- bootstrap加载 -->
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/bootstrap.css">
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap.js"></script>

<!--  -->
<script type="text/javascript" src="<%=basePath%>css/layer/layer.min.js"></script>
<!--  -->


<link rel="stylesheet" type="text/css" href="<%=basePath %>css/theme.css">
<!-- Le fav and touch icons -->
<!-- Demo page code -->
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
	
	
	
	/* 加载层 */
	var pageii;
	
	function reqLoading(flag){
		if (flag) {
			pageii=$.layer({
			    type: 3,
			   /*  area: ['400px', '530px'], */
			    shade: [0.5, '#fff'],//添加遮罩
			    loading: {
			        type: 0
			    }
			}); 
			
		}else{
			 layer.close(pageii);
		}
		
	}
</script>
<style type="text/css">
#line-chart {
	height: 300px;
	width: 800px;
	margin: 0px auto;
	margin-top: 1em;
}
.glyphicon-user{
	margin-right: 10px; 
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

<body class=" theme-blue" >
	<div class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<a class="" href="#"><span class="navbar-brand"><span
					class="glyphicon glyphicon-send"></span>ITcast</span></a>
		</div>

		<div class="navbar-collapse collapse" style="height: 1px;">
			<ul id="main-menu" class="nav navbar-nav navbar-right">
				<li class="dropdown hidden-xs"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"> <span
						class="glyphicon glyphicon-user"></span> ${sessionScope.user.uname}<span
						class="caret"></span>
				</a>

					<ul class="dropdown-menu">
						<li><a href="#">My Account</a></li>
						<li class="divider"></li>
						<li class="dropdown-header">Admin Panel</li>
						<li><a href="#">Users</a></li>
						<li><a href="#">Security</a></li>
						<li><a tabindex="-1" href="#">Payments</a></li>
						<li class="divider"></li>
						<li><a tabindex="-1" href="<%=basePath%>user_exit">Logout</a></li>
					</ul></li>
			</ul>
		</div>
	</div>

	<!-- 左侧导航栏 -->
	<div class="left-nav"  id="menu_main"  >
		<ul style="font-weight: bold;">
		
		
		<!-- 仪表盘开始                   NWQualityController      -->
		    <li><a href="<%=basePath %>dashBoard?showType=dashBoard" target="rightFrame" class="nav-header" onclick="reqLoading(true);"> 
		    	<span class="glyphicon glyphicon-home"></span><spring:message code="dashboard"> </spring:message></a>
			</li>
		<!-- 仪表盘结束 -->
		
		
		<!-- 信号覆盖开始 -->
			<li>
			<a href="#" data-target=".mainpage-menu" class="nav-header"  >
			 <span class="glyphicon glyphicon-zoom-in"></span><spring:message code="signalRange"> </spring:message><span class="glyphicon glyphicon-chevron"></span>
			</a>
			</li>
			<li>
			<ul class="mainpage-menu nav nav-list" >
			
					<!-- 信号强度分布                        SignalStrengthController        -->
					<li><a href="<%=basePath %>view/SignalCoverage/rssiheatmap.jsp" target="rightFrame" onclick="reqLoading(true);"> <span class="glyphicon glyphicon-play"></span><spring:message code="singMap"> </spring:message></a></li>
					<!-- 典型地标信号强度跟踪图                     SignalStrengthController       -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a  href="<%=basePath%>signalStrength_getSignalStrengthTracks?networkname=ALL&x_type=day&networktype=ALL&landmark=university" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="singLandmark"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>signalStrength_getSignalStrengthTracks?networkname=${sessionScope.user.NWOperator}&x_type=day&networktype=ALL&landmark=university" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="singLandmark"> </spring:message></a></li>
					</c:otherwise>
					</c:choose> 
					<!-- 典型地标信号强度跟踪图                 SignalStrengthController     -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a href="<%=basePath%>signalStrength_getSignalStrengthStatistics?networkname=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="LandmarkSignalStrengthStatistics"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>signalStrength_getSignalStrengthStatistics?networkname=${sessionScope.user.NWOperator}" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="LandmarkSignalStrengthStatistics"> </spring:message></a></li>
					</c:otherwise>
					</c:choose> 
				</ul>
				
				</li>
		<!-- 信号强度结束 -->	
		<!-- 网络质量开始 -->
			
			<li><a href="#" data-target=".signalCoverage-menu"
				class="nav-header collapsed"  > <i
					class="glyphicon glyphicon-signal"></i><spring:message code="networkQuality"> </spring:message><i
					class="glyphicon glyphicon-chevron"></i></a>
			</li>
			<li>
			<ul class="signalCoverage-menu nav nav-list" >
					<!-- 网络质量分布图       NWQualityController         -->
					<li><a href="<%=basePath %>view/NetworkQuality/nwQuaTileMap.jsp" target="rightFrame" onclick="reqLoading(true);"><span
							class="glyphicon glyphicon-play"></span><spring:message code="nwquaMap"> </spring:message></a></li>
					<!-- 网络质量统计图ZHL        NWQualityController    -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a  href="<%=basePath%>nwquality_networkQualityStatistics?speedType=DLSpeed&nwOperator=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="networkQualitySta"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>nwquality_networkQualityStatistics?speedType=DLSpeed&nwOperator=${sessionScope.user.NWOperator}" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="networkQualitySta"> </spring:message></a></li>
					</c:otherwise>
					</c:choose> 
					<!-- 网络速率排名ZHL       NWQualityController    -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a href="<%=basePath%>nwquality_networkRateRank?viewId=networkRateRank&speedType=DLSpeed&nwOperator=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="nwspRank"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>nwquality_networkRateRank?viewId=networkRateRank&speedType=DLSpeed&nwOperator=${sessionScope.user.NWOperator}" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="nwspRank"> </spring:message></a></li>
					</c:otherwise>
					</c:choose>
					
					<!-- 典型地标网络质量跟踪图ZHL       NWQualityController    -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a  href="<%=basePath%>nwquality_networkQualityTracks?speedType=DLSpeed&nwOperator=ALL&x_type=day&nwType=4G&landmark=university" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="nwLandmark"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>nwquality_networkQualityTracks?speedType=DLSpeed&nwOperator=${sessionScope.user.NWOperator}&x_type=day&nwType=4G&landmark=university" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="nwLandmark"> </spring:message></a></li>
					</c:otherwise>
					</c:choose> 
					<!-- 典型地标网络质量统计图         NWQualityController    -->
					<li><a href="<%=basePath %>nwquality_landmarkNQStatistics" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="LandmarkNetworkQualityStatistics"> </spring:message></a></li>
					
				</ul></li>
				
				
			<!-- 网络质量结束 -->				
			<!-- 数据连接开始 -->
			<li><a href="#" data-target=".network-menu" class="nav-header collapsed" > <i
					class="glyphicon glyphicon-th-list"></i><spring:message code="dataConnections"> </spring:message> <span
					class="glyphicon glyphicon-chevron"></span>
			</a></li>
			<li>
			
			<ul class="network-menu nav nav-list" >
				<!-- 数据链接率分布图                      DataConnectionController       -->
				<li><a href="<%=basePath %>showPointConnDist?network_name=ALL&mobile_network_type=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="pointConMap"> </spring:message></a></li>
				<!-- 数据链接率统计图                      DataConnectionController       -->
				<li><a href="<%=basePath %>dataConnectionStatistics" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="dataconSta"> </spring:message></a></li>
				
				
				</ul>
				</li>
				
				
				
				<!-- 数据连接结束 -->
				
				
				<!-- 热门App开始 -->
					<li><a href="#" data-target=".app-menu"
					class="nav-header collapsed"  > <i class="glyphicon glyphicon-phone"></i><spring:message code="hotApp"> </spring:message><span
					class="glyphicon glyphicon-chevron"></span>
					</a></li>
					<li><ul class="app-menu nav nav-list" >
					<!--热门APP流量分布图             AppTrafficController     -->
					<li><a href="<%=basePath %>view/AppMap/HeatAppDistribution.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="appFlowMap"> </spring:message></a></li>
					<!--热门APP地图           AppTrafficController      -->
					<li><a href="<%=basePath %>view/AppMap/appDistribution.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="appMap"> </spring:message></a></li>
					<!--热门APP流量排名             AppTrafficController   -->
					<li><a href="<%=basePath %>app_appRateRank" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="traRank"> </spring:message></a></li>
					<!-- 热门App流量跟踪图              AppTrafficController   -->
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<li><a  href="<%=basePath%>appTraffic_appTrafficTracks?speed_type=ALL&network_name=ALL&x_type=hour&networkType=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="appSta"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>appTraffic_appTrafficTracks?speed_type=ALL&network_name=${sessionScope.user.NWOperator}&x_type=hour&networkType=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="appSta"> </spring:message></a></li>
					</c:otherwise>
					</c:choose> 
					<!--典型地标热门APP流量排名               AppTrafficController   -->
					<li><a href="app_landmarkAppTrafficRank" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="appLandmark"> </spring:message></a></li>
				</ul></li>
				<!-- 热门App结束 -->
				
				
				<!-- 热门手机开始 -->
			<li><a href="#" data-target=".device-menu"
				class="nav-header collapsed"  > <i
					class="glyphicon glyphicon-hd-video"></i><spring:message code="hotcellphone"> </spring:message><span
					class="glyphicon glyphicon-chevron"></span>
			</a></li>
			<li>
			
			<ul class="device-menu nav nav-list" >
			
			
 					<!--热门手机流量分布图             AppTrafficController     -->
					<li><a href="<%=basePath %>view/AppMap/mobileFlowDistribution.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="mobileFlowMap"> </spring:message></a></li>
					<!--热门手机网络质量排名（无显示）                 NWQualityController        -->
					 <li><a href="nwquality_mobileNQRank" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play" ></span><spring:message code="MobileDeviceNetworkQualityRank"> </spring:message></a></li>
					 
					 
					 <!--热门手机流量排名（err）              AppTrafficController      -->
					<li><a href="<%=basePath%>app_hotTelRank" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="MobileDeviceTrafficRank"> </spring:message></a></li>
					
					
					<c:choose>
					<c:when test="${sessionScope.user.role=='admin'}">
					<!-- 手机OS流量排名              AppTrafficController      -->
					<li><a  href="<%=basePath%>appTraffic_hotTelOsStatistics?speed_type=download_traffic&network_name=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="MobileOSTrafficRank"> </spring:message></a></li>
					</c:when>
					<c:otherwise>
					<li><a href="<%=basePath%>appTraffic_hotTelOsStatistics?speed_type=download_traffic&network_name=${sessionScope.user.NWOperator}" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="MobileOSTrafficRank"> </spring:message></a></li>
					</c:otherwise>
					</c:choose>  
					 <!--热门手机分布图              AppTrafficController      -->
					 <li><a href="<%=basePath %>view/AppMap/mobileDistribution.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="mobmap"> </spring:message></a></li>
					<!--手机OS分布图                   AppTrafficController     -->
					<li><a href="<%=basePath %>view/AppMap/OSDistribution.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="MobileOSMap"> </spring:message></a></li>
			
 			
			


					 
					 
					 
				<!-- 热门手机结束 -->
				</ul></li>
				<!-- 连接点开始 -->
			<li><a href="#" data-target=".testPoinsts-menu" class="nav-header collapsed" > 
				<i class="glyphicon glyphicon-asterisk"></i><spring:message code="testPoints"> </spring:message> 
				<span class="glyphicon glyphicon-chevron"></span></a>
			</li>
			<li>
				<ul class="testPoinsts-menu nav nav-list">
				<!-- 连接点排名                NWQualityController      -->
				<li><a href="<%=basePath %>nwquality_testpointRank" target="rightFrame" onclick="reqLoading(true);">
					<span class="glyphicon glyphicon-play"></span><spring:message code="pointRank"> </spring:message></a></li>
				<!-- 连接点地理分布                 NWQualityController          -->
				<li><a href="<%=basePath %>showAllPointGeog?nwOperator=ALL" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="pointMap"> </spring:message></a></li>
			</ul>
			</li>
				<!-- 连接点结束 -->
				
				
				
			<!-- 个人----开始 -->
			<li><a href="#" data-target=".testPoinstsOverview-menu" class="nav-header collapsed" > 
				<i class="glyphicon glyphicon-film"></i><spring:message code="testPoinstsOverview"> </spring:message> 
				<span class="glyphicon glyphicon-chevron"></span></a>
			</li>
			<li>
				<ul class="testPoinstsOverview-menu nav nav-list">
					<!--网络质量街景图               NWQualityController    -->
 				    <li><a href="<%=basePath %>view/PoinstsOverview/showSP.jsp" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-play"></span><spring:message code="networkQuality"></spring:message></a></li>
					<!-- APP街景图               AppTrafficController    -->
					<li><a href="<%=basePath %>view/AppMap/showJA.jsp" target="rightFrame" onclick="reqLoading(true);"> <span class="glyphicon glyphicon-play"></span><spring:message code="appStreetScape"></spring:message></a></li>
					<!-- OS街景图               AppTrafficController     -->
					<li><a href="<%=basePath %>view/AppMap/showJO.jsp" target="rightFrame" onclick="reqLoading(true);"> <span class="glyphicon glyphicon-play"></span><spring:message code="OSStreetScape"></spring:message></a></li>
					<!--信号覆盖                     SignalStrengthController     -->
					<li><a href="<%=basePath %>view/SignalCoverage/showRSSI.jsp" target="rightFrame" onclick="reqLoading(true);"> <span class="glyphicon glyphicon-play"></span><spring:message code="signalRange"> </spring:message></a></li>
					
				</ul>
			</li>
			<!-- 连接点概况总览----结束 -->
			
			<!-- 用户管理开始 -->
 		    <li><a href="<%=basePath %>user_manage" target="rightFrame" class="nav-header"> 
		    	<span class="glyphicon glyphicon-user"></span><spring:message code="userManage"></spring:message></a>
			</li>
			<!-- 用户管理结束 -->
				
			<!-- 地址管理----开始 -->
			<!--
			<li><a href="#" data-target=".otherAddresse-menu"
				class="nav-header collapsed" > <i
					class="glyphicon glyphicon-leaf"></i><spring:message code="otherAddresseEntrance"> </spring:message><span
					class="glyphicon glyphicon-chevron"></span>
			</a></li>
			<li><ul class="otherAddresse-menu nav nav-list">
			        <li><a href="<%=basePath%>showAllAddress" target="rightFrame" onclick="reqLoading(true);"><span class="glyphicon glyphicon-plus-sign"></span><spring:message code="addresseEntrance"> </spring:message></a></li>
				</ul></li>
		 地址管理----结束 -->
		</ul>
	</div>
	
	
	
	
	
	
	
	<!-- 右侧内容 -->
	<div class="content" style="height: 91vh">
		<iframe src="<%=basePath %>dashBoard?showType=dashBoard" name="rightFrame" id="righrtFrame"
			frameborder="0" scrolling="no" style="width: 100%; height: 100%;font-weight: bold;" onclick="reqLoading(true);"></iframe>
	</div>
	
	
	<script type="text/javascript">
		$(function($) {
			$(document).ready(function (){
				//目录切换
				$('#menu_main').children("ul").children("li").children("ul").css({display:"none"});
				//控制高度和宽度
				$(".content").css('min-height',$(window).height()*0.91);
                $(".content").css('height',$(window).height()*0.91);
			});
		});


	//目录切换
	$('#menu_main').children("ul").children("li").children("a").on('click', function(){
		$(this).parent("li").parent("ul").children("li").children("ul").css({display:"none"});
		
		
		$(this).parent("li").parent("ul").children("li").children("a").css({background:"#f7f7f7",color:"#444"});
		/* 改变下拉列表颜色 */
		$(this).parent("li").parent("ul").children("li").children("ul").children("li").children("a").css({"font-weight": "bold",color:"#557093"});
		/* 改变当前点击目录颜色 */
		$(this).css({background:"#cc0033",color:"#fff"});
		/* 显示当前的下拉列表 */
		$(this).parent("li").next("li").children("ul").css({display:"block"});
	});



	</script>
</html>