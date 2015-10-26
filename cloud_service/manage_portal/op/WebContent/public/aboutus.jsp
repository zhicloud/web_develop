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
<title>关于我们 -- 致云 ZhiCloud</title>
<meta name="keywords" content="关于我们，关于致云，公司介绍，致云发展，联系致云，联系方式，加入致云，人才招聘，致云" />
<meta name="description" content="致云科技是一家专注于云计算领域的创新型高科技企业，拥有在云计算领域具备丰富经验的资深技术和运营团队，总部及研发中心设在成都高新区，目前在成都、北京、上海、广州和深圳设立全资子公司。" />
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
	init(7);
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	}
	$(".page6").css("height",$(".page6").width()/1198*264);
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
    <div class="header pageheader">
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
      <div class="box page5" style="height:540px;">
        <div class="titlebar" style="padding-top:24px">
          <div class="l">关于我们</div>
          <div class="r"><a href="aboutus.do" class="active">公司简介</a>　|　<a href="job.do">加入我们</a></div>
          <div class="clear"></div>
        </div>
        <div style="margin:0 auto; width:619px; height:245px; padding:120px 0 0 0"><img src="image/about_banner.png" width="619" height="245" /></div>
        <div class="r" style="width:212px; height:16px; padding:80px 16px 0 0"><img src="image/about_script.png" width="212" height="16" /></div>
        <div class="clear"></div>
      </div>
      <div class="abouttext"><img src="image/about_1.png" width="189" height="51" /><br />
        <br />
        致云科技有限公司是一家专注于云计算领域的创新型高科技企业，拥有在云计算领域具备丰富经验的资深技术和运营团队，总部及研发中心设在成都高新区，目前在成都、北京、上海、广州和深圳设立全资子公司。<br />
        致云科技始终以技术创新为企业核心竞争力，已拥有几十项自主研发、自有知识产权的云计算核心专利技术；结合商业模式创新，以“市场主导、技术先行、自主创新、服务为本”为经营理念，打造一个共生、共赢、共进、共享的云计算产业链，致力成为全球领先的云计算解决方案提供商和服务商。 </div>
      <div class="abouttext" style="border-bottom:none;"><img src="image/about_2.png" width="188" height="52" /><br />
        <br />
        <strong>企业文化</strong><br />
        激情、创新、智慧、责任<br />
        <strong>企业愿景</strong><br />
        打造一个共生、共赢、共进、共享的云计算产业链<br />
        <strong>企业使命</strong><br />
        打造极致之云、引领服务变革<br />
        <strong>企业目标</strong><br />
        致力成为国内外领先的云计算解决方案提供商及服务商</div>
         <div class="box page6"></div>
      <div class="abouttext"><img src="image/about_3.png" width="209" height="52" /><br />
        <br />
        自主研发——运营商级云计算运营支撑平台<br />
        自主研发——各产品线均可按需满足客户的定制化、个性化需求。<br />
        拥有几十项云计算领域相关的技术专利<br />
        领先的云平台、云存储、云桌面技术，提供完整的端到端解决方案<br />
        经验丰富的技术研发和运营团队</div>
      <div class="abouttext" style="border-bottom:none"><img src="image/about_4.png" width="202" height="52" /><br />
        <br />
       致云科技总部<br />
        地址：成都市天府软件园C区12栋15层<br />
        座机：028-69189999<br />
        传真：028-61291999<br />
        <br />
        致云科技广州公司<br />
        地址：广州市天河区黄埔大道西100号富力盈泰广场A塔1011<br />
        座机：020-38260999<br />
        传真：020-38260988<br />
        <br />
        致云科技上海公司<br />
        地址：上海市普陀区真北路915号1505<br />
        座机：021-62868099<br />
        传真：021-62869177</div>
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
</div>
<div class="pageright">
  <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
</div> 
</body>
</html>