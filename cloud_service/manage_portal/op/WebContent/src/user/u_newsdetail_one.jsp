<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_newsdetail_one.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>新闻详情 -- 致云 ZhiCloud</title>
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
	<div class="g-nd-bd">
		<div class="wrap">
			<div class="nd-cont">
				<img src="img/ne_detail_img3.jpg" alt="致云科技携手天府软件园共建云服务平台"/>
				<h3>致云科技携手天府软件园共建云服务平台</h3>
				<p>9月11日，致云科技有限公司（以下简称致云科技）与成都天府软件园正式达成了“国家软件公共服务平台—云服务平台建设”战略合作协议。</p>
				<p>云服务平台的建设，将对天府软件园公共技术平台进行云改造和云升级，利用现有IT基础设施，架构云计算平台，提供计算资源、存储资源、网络资源等即租即用的云服务，实现软件园区企业“拎包入住”式服务功能。</p>
				<p>战略合作依托于天府软件园公共技术平台，及致云科技成熟的云管理平台、分布式云存储、移动云桌面等技术，双方进行了深度资源整合、合作建设。本项目的实施将使得公共网络资源有效扩容，相对于传统的企业自行购买设备自行维护，可节省服务器、存储、网络、机房环境、安全维护设施、数据库软件、运维等方面的资金费用，为入驻企业提供科技化、现代化、经济化配套的IT服务。</p>
				<p>致云科技有限公司是一家专注于云计算领域的创新型高科技企业，拥有在云计算领域具备丰富经验的资深技术和运营团队，总部及研发中心设在成都高新区，目前在成都、北京、上海、广州和深圳设立全资子公司。</p>
				<p>致云科技始终以技术创新为企业核心竞争力，已拥有几十项自主研发、自有知识产权的云计算核心专利技术；结合商业模式创新，以“市场主导、技术先行、自主创新、服务为本”为经营理念，打造一个共生、共赢、共进、共享的云计算产业链，致力成为全球领先的云计算解决方案提供商和服务商。</p>
			</div>
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
<script type="text/javascript">
	$(function(){
		navHighlight("uau","und");
	})
</script>
</body>
</html>