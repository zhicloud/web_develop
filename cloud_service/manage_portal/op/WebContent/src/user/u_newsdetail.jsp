<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_newsdetail.jsp" />
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
				<img src="img/ne_detail_img1.jpg" alt="ZhiCloud云服务平台正式商用"/>
				<h3>ZhiCloud云服务平台正式商用</h3>
				<p>10月1日，致云科技云服务平台www.zhicloud.com全新上线，该平台是致云科技继今年6月29日推出试商用平台后的全面改版和升级，新平台上线后将会为用户带来更为极致的云端体验。</p>
				<p>致云科技是目前国内极少数完全依托自有知识产权，拥有云计算核心专利技术，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。公司研发基地坐落于有电子科大、西南科大等知名IT高校依托的成都，在北京、上海、广州分别设立了运营中心。本次上线的是广州、香港云计算中心，北京、成都云计算中心后续将会陆续上线，三至五年内，东南亚、欧洲、北美云计算中心也将全面开放。致云科技力争打造极致的云服务，为用户提供无上的体验，“我们专注于为用户提供云服务，着力于极致的用户体验及满意度，为用户创造最大化价值是致云追求的目标”，致云科技CEO杨继祖如是说。</p>
				<p>此次改版升级的云服务是基于公司新一代自主研发的云计算平台，有着几十项专利技术的支撑。平台依据软件定义一切的理念设计，所有计算、存储、网络资源均由软件定义，可实现实时计费、实时变更、动态调度、自我管理。采用块级存储，可以无缝衔接原有系统。平台所提供的云服务资源，与物理资源、运营支撑环境完全隔离，完全可控。同时，还能根据用户需求按需定制服务。</p>
				<p>ZhiCloud云服务平台跟国内其他云服务平台相比，有着许多不可比拟的优势，其核心优势有以下几点：<br/>1.秒级创建云主机；<br/>2.国内首家可提供无缝独立快存储服务的云服务商；<br/>3.单主控、单集群，支持10W+物理服务器；<br/>4.超大规模部署，采用零配置、自管理智能组网方式。<br/>云服务将实现变“买”为“租”，不再购买繁多的IT设备，只需在云服务平台上按需租用计算资源、存储资源及网络资源，这将在很大程度上节约公司固资成本及运营成本。云服务平台的使用除了为企业带来福音，还能为个人及公共服务平台创造便捷，只要在有互联网的地方，便能如办公场所般自如办公，更能让公共服务平台（如天府软件园）为入驻企业提供拎包式入驻服务。</p>
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