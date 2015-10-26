<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>  
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userName = "";
	if(loginInfo!=null){
		userName = loginInfo.getAccount();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云 ZhiCloud -- 致力于云计算，打造非同凡响的云服务体验</title>
<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>

 

<script src="javascript/page.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4"); 
var userName = '<%=userName%>';
$(document).ready(function(){
	init(1);  
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	}
	initfeature(1,7);
	si = setInterval(nextfeature,3000);
	$(".feature").hover(function(event){
		$(".nextfeature").css("opacity","1");
		$(".prevfeature").css("opacity","1");
		clearInterval(si);
	},
	function(event){
		$(".nextfeature").css("opacity","0");
		$(".prevfeature").css("opacity","0");
		si = setInterval(nextfeature,3000);
	});
	$(".iproduct").hover(function(event){
		$(this).find(".iproducthover").css("top","0");
	},
	function(event){
		$(this).find(".iproducthover").css("top","-225px");
	});
});
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body>
<div class="page">
  <div class="pagewhite">
    <div class="header">
        <div class="top">
			<a class="logo l" href="<%=request.getContextPath()%>/"> 
			<img src="<%=request.getContextPath()%>/image/logo_big.png" width="153" height="33" alt="致云 ZhiCloud" /></a>
			<div id="beforelogin" class="user r">
				<a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span>
				<a id="reglink" href="javascript:void(0);">注册</a>
			</div>
			<div id="afterlogin" class="user r" style="display: none;">
				<img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
				<a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span>
				<a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
			</div>
			<div class="nav r">
				<a href="<%=request.getContextPath()%>/" style="background: transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding: 8px 0" /> </a>
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
				<a href="<%=request.getContextPath()%>/vpcserver.do">专属云</a>
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
				<a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
				<a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
				<a href="#" style="display: none"></a>
				<a href="<%=request.getContextPath()%>/user.do?flag=login" style="display: none"></a>
				<a href="#" style="display: none"></a>
				<a href="#" style="display: none">我的云端</a>
			</div>
		</div>
		<div class="subnav">
			<div class="box">1</div>
			<div class="box">2</div>
			<div class="box">3</div>
			<div class="box">4</div>
			<div class="box">5</div>
			<div class="box">6</div>
			<div class="box">7</div>
			<div class="box">8</div>
			<div class="box">9</div>
			<div class="box">
				<a href="#"><img id="nav_10_1" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a><a
					href="#"><img id="nav_10_2" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a><a
					href="#"><img id="nav_10_3" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a><a
					href="#"><img id="nav_10_4" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a><a
					href="#"><img id="nav_10_5" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a><a
					href="#"><img id="nav_10_6" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a><a
					href="#"><img id="nav_10_7" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
			</div>
		</div>
	</div>
    <div class="pagemain">
      <div class="feature" style="height:450px;">
        <ul style="width:700%;">
          <li style="width:14.2907%;height:450px;background:#f4f4f4 url(image/bannera.png) no-repeat center center"></li>
          <li style="width:14.2907%;height:450px;background:#3188fa url(image/bannerb.png) no-repeat center center">
          <a href="cloudsever.do" style="display:block; width:100%; height:100%; text-align:center">&nbsp;</a>
          </li>
          <li style="width:14.2907%;height:450px;background:#cee6ff url(image/banner_2_bg.png)">
            <div style="width:100%; height:100%;background:url(image/banner_2.png) no-repeat center center"><a href="cloudstorage.do" style="display:block; width:100%; height:100%; text-align:center"></a></div>
          </li>
          <li style="width:14.2907%;height:450px;background:#13bd9c url(image/bannerc.png) no-repeat center center"><a href="solution.do" style="display:block; width:100%; height:100%; text-align:center">&nbsp;</a></li>
          <li class="index4" style="width:14.2958%;height:450px; text-align:center"><img src="image/banner_4.png" width="1000" height="450" /></li>
          <li class="index5" style="width:14.2557%;height:450px; text-align:center"><img src="image/banner_5.png" width="1000" height="450" /></li>
          <li style="width:14.28%;height:450px;background:#f4f4f4 url(image/bannera.png) no-repeat center center"></li>
        </ul>
        <a href="javascript:void(0)" class="prevfeature" onclick="prevfeature()"><img src="image/banner_prev.png" width="66" height="122" alt=" " /></a> <a href="javascript:void(0)" class="nextfeature" onclick="nextfeature()"><img src="image/banner_next.png" width="66" height="122" alt=" " /></a> </div>
		<div class="box" style="width:980px;padding:70px 0 0 0;position:relative;"> 
			<a class="iproduct" style="background:url(image/pro_a1.png) no-repeat;margin-right:46px;" href="buy.do?flag=buy">
				<div class="iproducthover" style="background:url(image/pro_a2.png) no-repeat">立即购买</div>
			</a> 
			 <a class="iproduct" style="background:url(image/pro_d1.png) no-repeat" href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage">
				<div class="iproducthover" style="background:url(image/pro_d2.png) no-repeat">专属云</div>
			 </a>
			<a class="iproduct" style="background:url(image/pro_b1.png) no-repeat;margin-right:46px;" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudDiskService&method=addPage">
				<div class="iproducthover" style="background:url(image/pro_b2.png) no-repeat">立即购买</div>
			</a> 
			<a class="iproduct" style="background:url(image/pro_c1.png) no-repeat;margin-right:48px;" href="solution.do">
				<div class="iproducthover" style="background:url(image/pro_c2.png) no-repeat">了解详情</div>
			 </a>
			 <div class="clear"></div>
		</div>
      <div class="box" style="width:980px;padding:20px 0 20px 0;position:relative;">
        <div class="iproductinfo" style="margin-right:46px;">
          <div class="title">云主机</div>
          <p>云主机是全新云服务器租用服务，能够帮助您快速上线业务，降低IT运维成本，提高IT管理效率具有快速部署安全可靠、弹性伸缩等特性。</p>
          <a href="cloudsever.do">了解更多</a>
          <div class="clear"></div>
        </div>
        <div class="iproductinfo">
          <div class="title">专属云</div>
          <p>由于安全性和隔离性等方面的考虑，或客户需要将多台云服务器放置在隔离的内网中进行集群作业，从而组合而成的一个带有软路由器和软防火墙的虚拟专属内网。
		</p>
          <a href="vpcserver.do">了解更多</a>
          <div class="clear"></div>
        </div>
        <div class="iproductinfo" style="margin-right:46px;">
          <div class="title">云硬盘</div>
          <p>自主研发的分布式云存储，高吞吐、高并发、高可用，可直接代替原有传统存储设备，且应用无需重新开发帮助传统信息系统无缝升级到云计算时代。</p>
          <a href="cloudstorage.do">了解更多</a>
          <div class="clear"></div>
        </div>
        <div class="iproductinfo" style="margin-right:46px;">
          <div class="title">解决方案</div>
          <p>依托自主研发的大规模、高可靠、高弹性、高性能的云管理平台、云存储、云桌面提供整体的云融合解决方案。</p>
          <a href="solution.do">了解更多</a>
          <div class="clear"></div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于致云</a><br />
				<a href="<%=request.getContextPath()%>/job.do">加入我们</a><br />
				<a href="<%=request.getContextPath()%>/aboutus.do">联系我们</a>
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.zhicloud.com"
					target="_blank">致云科技有限公司</a>, All rights reserved.
				蜀ICP备14004217号-1
			</div>
		</div> 
	</div>
  </div>
  <div class="pageright">
    <div style="display:none">
    <img id="verification_code"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" width="100" height="40" alt="验证码" class="code"/>
    </div>
  
    <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>