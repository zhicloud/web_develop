<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%> 
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- trail_success.jsp -->
<html>
<head> 
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>试用成功- 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/menu.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/menubutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/icon.css" />
<script src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script src="<%=request.getContextPath() %>/js/easyui/jquery.easyui.min.js"></script>
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath() %>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
        <%
				if(loginInfo!=null){
			%>
			<div class="headerregister r">
				<a href="#" id="logout_link">注销</a>
			</div>
			<div class="headerlogin r">
				 <%=loginInfo.getAccount()%> 
			</div>
			<%
				} else{
			%>
			<div class="headerregister r">
				<a href="<%=request.getContextPath()%>/user/register.do">注册</a>
			</div>
			<div class="headerlogin r">
				<a href="<%=request.getContextPath()%>/user.do?url=<%=request.getContextPath()%>/user/buy.do">登录</a>
			</div>
			<%
				}
			%>
					</div>
      </div> 
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l" ><a href="<%=request.getContextPath()%>">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"  ><a href="#" id="pbtn">产品<img src="<%=request.getContextPath()%>/images/button_navlist.gif" width="12" height="12" /></a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"  ><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
        <div class="navcontrol r"><a href="<%=request.getContextPath()%>/user.do"><span>我的云端</span></a></div>
        <div id="productmenu"  >
        <a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
       <a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a> 
       <a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
       <a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
       <a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
       <a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
      </div>
      </div> 
    </div>
    <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">试用成功</div>
        </div>
        <div class="fullcontent">
<div class="fullcenter"><img src="<%=request.getContextPath()%>/images/image_trail_success.gif" width="118" height="110" alt="试用成功" /><br /><br />您已试用成功，请等待管理员确认。<br /><br /><br /><br /></div>
        </div>
      </div>
      <div class="clear">&nbsp;</div>
    </div>
  </div>
    </div> 
  <div class="pagefooter">
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
      </div>
    </div>
  </div> 
</div> 
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.cookie.js"></script>
	<script type="text/javascript"> 

	//--------------------------
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	// 初始化
	$(document).ready(function(){ 
		$.cookie('type',null,{path:"/"});
		// 注销
		$("#logout_link").click(function(){
			logout();
		});
	}); 
   // 注销
	function logout()
	{
		ajax.remoteCall("bean://sysUserService:logout",
			[],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("成功退出，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						top.location.href = "<%=request.getContextPath()%>";
					}, 500);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	}
	$(document).ready(function(){
		$('body').mousemove(function(e) {  
			var xx=e.pageX;
			var yy=e.pageY; 
			var movement=-(xx/$(window).width()-0.5);
			$(".scloud1").css("margin-left",(movement*200)+180+"px");
		$(".scloud2").css("margin-left",(movement*120)-700+"px");
		$(".scloud3").css("margin-left",(movement*20)-200+"px");
	});
		setInterval("parallax()",20);
		var b3;
		var b4;
		var s3;
		var s4;
		$("#pbtn").mouseenter(function(){
			b3=setTimeout("$('#productmenu').fadeIn(300)",300);
			s3 && clearTimeout(s3);
			s4 && clearTimeout(s4);
		});
		$("#pbtn").mouseleave(function(){
			s3=setTimeout("$('#productmenu').fadeOut(300)",300);
			b3 && clearTimeout(b3);
			b4 && clearTimeout(b4);
		});
		$("#productmenu").mouseenter(function(){
			b4=setTimeout("$('#productmenu').fadeIn(300)",300);
			s3 && clearTimeout(s3);
			s4 && clearTimeout(s4);
		});
		$("#productmenu").mouseleave(function(){
			s4=setTimeout("$('#productmenu').fadeOut(300)",300);
			b3 && clearTimeout(b3);
			b4 && clearTimeout(b4);
		});
	});
	function parallax(){
		$(".banner").html(movement);
		var movement=$(document).scrollTop()/($(document).height()-$(window).height())-0.5;
		$(".scloud1").css("margin-top",(movement*200)+"px");
		$(".scloud2").css("margin-top",(movement*120)+"px");
		$(".scloud3").css("margin-top",(movement*20)+"px");
	}
	</script>
</html>
