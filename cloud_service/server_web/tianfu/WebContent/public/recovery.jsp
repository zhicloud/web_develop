<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%> 
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>云容灾- 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/sequencejs-theme.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/menu.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/metro/menubutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/icon.css" />
<script src="<%=request.getContextPath() %>/js/jquery.js"></script>
<script src="<%=request.getContextPath() %>/js/jquery.sequence-min.js"></script>
<script src="<%=request.getContextPath() %>/js/sequencejs-options.js"></script>
<script src="<%=request.getContextPath() %>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
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
<!--[if IE 6]>
    <script type="text/javascript" src="js/DD_belatedPNG.js"></script>
    <script language="javascript" type="text/javascript">
    DD_belatedPNG.fix("*");
    </script>
<![endif]-->
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l">
				<a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a>
			</div>
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
        <div class="navbutton l" ><a href="<%=request.getContextPath()%>/">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l" id="navactive"><a href="#" id="pbtn">产品<img src="<%=request.getContextPath()%>/images/button_navlist.gif" width="12" height="12" /></a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
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
    <div class="banner">
      <div class="bannerbox">
        <h2><img src="<%=request.getContextPath()%>/images/icon_product_6.png" width="40" height="40" alt="云容灾" /> 云容灾</h2>
        <p>云容灾是指在异地云计算中心建立一套或多套针对计算资源、存储资源、网络资源相同的资源池环境，<br/>各云计算中心可以进行健康状态监视和容灾切换，当一处系统因意外（如火灾、地震等）停止工作时，<br/>整个应用系统可以切换到异地云计算中心，使得该系统功能可以继续正常工作。 </p>
      </div>
    </div>
    <div class="bannertitlebar">
<ul class="sequence-pagination">
          <li>稳定可靠</li>
          <li>压缩去重</li>
          <li>自动化容灾</li>
          <li>秒级恢复</li>
          <li>监控统计</li>
        </ul>
    </div>
    
    <div class="sequence-theme">
    <div class="scloud">
            <img class="scloud1" src="<%=request.getContextPath()%>/images/cloud2.png" alt=" " />
            <img class="scloud2" src="<%=request.getContextPath()%>/images/cloud1.png" alt=" " />
            <img class="scloud3" src="<%=request.getContextPath()%>/images/cloud1.png" alt=" " />
    </div>
      <div id="sequence"><img class="sequence-prev" src="<%=request.getContextPath()%>/images/bt-prev.png" alt="上一页" /><img class="sequence-next" src="<%=request.getContextPath()%>/images/bt-next.png" alt="下一页" />
        <ul class="sequence-canvas">
          <li class="animate-in">
            <h2 class="stitle">稳定可靠</h2>
            <h3 class="ssubtitle">▪ 多地云计算中心冗余设计，包括计算资源、存储资源、网络资源，高可用性达99.99%</h3>
            <img class="simg" src="<%=request.getContextPath()%>/images/icon_product_6_1.png" alt="稳定可靠" />
          </li>
          <li>
            <h2 class="stitle">压缩去重</h2>
            <h3 class="ssubtitle">▪ 数据压缩和去重技术，优化存储空间，降低您购买成本</h3>
            <img class="simg" src="<%=request.getContextPath()%>/images/icon_product_6_2.png" alt="压缩去重" />
          </li>
          <li>
            <h2 class="stitle">自动化容灾</h2>
            <h3 class="ssubtitle">▪ 支持同步传输和异步传输，各地云计算中心可自动进行健康状态监控和容灾切换</h3>
            <img class="simg" src="<%=request.getContextPath()%>/images/icon_product_6_3.png" alt="自动化容灾" />
          </li>
          <li>
            <h2 class="stitle">秒级恢复</h2>
            <h3 class="ssubtitle">▪ 数据副本保证完整性、数据不丢失，秒级恢复到任意时间点或指定快照</h3>
            <img class="simg" src="<%=request.getContextPath()%>/images/icon_product_6_4.png" alt="秒级恢复" />
          </li>
          <li>
            <h2 class="stitle">监控统计</h2>
            <h3 class="ssubtitle">▪ 提供主动监控，报表分析，以及短信、电子邮件通知</h3>
            <img class="simg" src="<%=request.getContextPath()%>/images/icon_product_6_5.png" alt="监控统计" />
          </li>
        </ul>
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

<script type="text/javascript">
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;
var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	 
	 

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){  
		// 注销
		$("#logout_link").click(function(){
			logout();
		}); 
	});
};
 
// 注销
   function logout(){
	ajax.remoteCall("bean://sysUserService:logout",
			[],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('提示','会话超时，请重新登录','info',function(){
						window.location.href = "<%=request.getContextPath()%>/user.do";
					});
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
						top.location.href = "<%=request.getContextPath()%>/user.do";
					}, 500);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	}
</script>
</html>
