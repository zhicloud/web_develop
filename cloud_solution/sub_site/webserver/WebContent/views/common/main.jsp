<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>服务器管理</title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/calendar.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"></script>
		<link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico" type="image/x-icon" /> 
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css" media="all"/>
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/server.css" media="all"/>
	</head>
	<body>
	<div class="main">
	<div class="g-hd" id="headdiv">
	    <div class="hd-logo"><img src="<%=request.getContextPath() %>/images/sermag_logo.png" alt="logo" /></div>
	    <div class="hd-cont">
    		<label>${user.username}</label>
    		<span>&nbsp;|&nbsp;</span>
    		<a class="modify-pwd" id="modify_pwd" href="javascript:;">修改密码</a>
    		<span>&nbsp;|&nbsp;</span>
    		<a class="login-out" href="#" onclick="logout()">退出</a>
	    </div>
	</div>
	<div class="g-sd">
	    <ul>
    		<li><a href="#" id="runstate"><i class="icon-runstate"></i>运行状态<span class="icon-arrow"></span></a></li>
    		<li><a href="#" id="network"><i class="icon-netmanage"></i>网络管理<span class="icon-arrow"></span></a></li>
    		<li><a href="#" id="domainmanage"><i class="icon-dommanage"></i>域名管理<span class="icon-arrow"></span></a></li>
    		<li><a href="#" id="sysmanage"><i class="icon-sysmanage"></i>系统管理<span class="icon-arrow"></span></a></li>
    		<li><a class="current" href="#" id="loglist"><i class="icon-loglist"></i>日志清单<span class="icon-arrow"></span></a></li>
	    </ul>
	</div>
	<div id="contentdiv" style="height:-1px;">
		<iframe id="content_frame" scrolling="no" frameborder="0" hspace="0" vspace="0" style="width:100%; height:100%; border:0px solid #005500;"
			src=""></iframe>
	</div>
	<div class="g-ft"></div>
</div>	
</body>
<script type="text/javascript">
function logout(){
	window.location.href = "<%=request.getContextPath()%>/login/logout";
}
function isSwitchable(target)
{
	var result = true;
	if( $("#content_frame").prop("contentWindow").onBeforeExit!=null )
	{
		result = $("#content_frame").prop("contentWindow").onBeforeExit(target);
	}
	return (result==false) ? false : true;
}

function onSwitch(target)
{
	// 日志清单
	if( target.id=="loglist" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/log/manage");
	}
	// 域名管理
	if( target.id=="domainmanage" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/domain/manage");
	}
	// 网络管理
	if( target.id=="network" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/net/all");
	}
	// 运行状态
	if( target.id=="runstate" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/state/manage");
	}
	// 系统管理
	if( target.id=="sysmanage" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath() %>/sysmanage/manage");
	}
}	
$(function(){
	// 布局初始化
	$("#headdiv").css("width", $(document.body).width());
	$("#contentdiv").css("height", $(document.body).height());
	
	$(".g-sd li a").click(function(){
		if( isSwitchable(this)==true )
		{
			$(".g-sd li a").removeClass("current");
			$(this).addClass("current");
			onSwitch(this);
		}
	});
	$("#content_frame").attr("src", "<%=request.getContextPath()%>/log/manage");
});
</script>
</html>
