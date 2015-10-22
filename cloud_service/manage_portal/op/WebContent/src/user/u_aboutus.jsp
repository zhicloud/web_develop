<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_aboutus.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>关于我们 -- 致云 ZhiCloud</title>
	<link rel="shortcut icon" href="../common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../common/css/global.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/usersite.css" media="all"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
</head>
<body>
<div class="g-doc">
	<!-- header -->
	<%@ include file="../common/tpl/u_header.jsp"%>
	<!-- /header -->
	<div class="f-cb"></div>
	<div class="g-au-banner">
		<div class="wrap">
			<div class="banner-cont"><img class="f-db" src="img/au_banner_cont.png" alt="内容"/></div>
		</div>
	</div>
	<div class="f-cb"></div>
	<div class="g-au-bd">
		<div class="wrap">
			<ul>
				<li>
					<div class="au_compprof"></div>
					<h4>公司简介</h4>
					<p>致云科技有限公司是一家专注于云计算领域的创新型高科技企业，拥有云计算领域具备丰富经验的资深技术和运营团队</p>
					<a class="au-more" href="u_comprofile.jsp?do=uau">了解更多&gt;</a>
				</li>
				<li>
					<div class="au_jionus"></div>
					<h4>加入我们</h4>
					<p>我们知悉你的工作也是重要的人生事业组成，我们提供业内有竞争力的薪酬及完善的福利，并不断完善服务</p>
					<a class="au-more" href="u_jionus.jsp?do=uau">了解更多&gt;</a>
				</li>
				<li>
					<div class="au_contus"></div>
					<h4>联系我们</h4>
					<p>每位关注致云的朋友，无论您是我们的客户或合作伙伴，我们都以诚相待，热诚欢迎您来函来电</p>
					<a class="au-more" href="u_contactus.jsp?do=uau">了解更多&gt;</a>
				</li>
				<li>
					<div class="au_news"></div>
					<h4>新闻动态</h4>
					<p>由于安全性和隔离性等方面的考虑，或客户需要将多台云服务器放置在隔离的内网中进行集群作业，从而组合而成的一个带有软路由器和软防火墙的虚拟专属内网</p>
					<a class="au-more" href="u_news.jsp?do=uau">了解更多&gt;</a>
				</li>
			</ul>
		</div>
	</div>
	<div class="f-cb"></div>
	<!-- footer -->
	<%@ include file="../common/tpl/u_footer.jsp"%>
	<!-- /footer -->
</div>
<!-- login -->
<%@ include file="../common/tpl/u_login.jsp"%>
<!-- /login -->
</body>
</html>