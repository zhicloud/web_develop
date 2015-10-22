<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<base href="<%=request.getContextPath() %>/src/user/u_serconcept.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>服务理念 -- 致云 ZhiCloud</title>
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
	<div class="g-scc-banner"></div>
	<div class="f-cb"></div>
	<div class="g-scc-bd">
		<div class="wrap">
			<div class="scc-cont">
				<h3 class="font24 c-474 f-fwn">致云服务理念</h3>
				<p>致云服务体现了“关注随时随地，满意无处不在”的核心理念。我们以最专业性的服务队伍，及时和全方位地关注客户每一个服务需求；通过提供广泛、全面和快捷的服务，使客户体验到无处不在的满意和可信赖的贴心感受。</p>
				<p>我们对坚持服务质量和服务满意度上的提出了4个标准， smiling（微笑）、sincere（诚挚）、speciality（专业）、speedy（快速），建构我们规范和专业的服务体系，第一时间解决客户应用中的问题，为客户提供量身定做的专业性服务；通过长期不懈、坚持永续的服务，持续提升客户服务价值。</p>
				<p>在四个标准的基础上，我们形成了“技术专精、客户为本”的服务准则——技术能力是致云服务的核心，客户需求是致云服务的基石。致云服务力求在公司产品技术不断积累的基础上，通过服务人员技术的不断提升，满足行业客户全方位的服务需求，并通过技术服务，实现客户的业务增值和技术同步提升。</p>
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
		navHighlight("uts","uscc");
	})
</script>
</body>
</html>