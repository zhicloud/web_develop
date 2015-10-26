<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_serconsult.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>咨询服务 -- 致云 ZhiCloud</title>
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
	<div class="g-scs-bd">
		<div class="wrap">
			<div class="scs-cont">
				<h3 class="c-474 font24 f-fwn">咨询服务</h3>
				<ul>
					<li><img src="img/scc_img1.png" alt="帮助团队规划、搭建和管理云服务器"/><label>帮助团队规划、搭建和管理云服务器</label></li>
					<li><img src="img/scc_img2.png" alt="根据团队产品特性、产品目标用户规模设计云服务器部署架构。"/><label>根据团队产品特性、产品目标用户规模设计云服务器部署架构。</label></li>
					<li><img src="img/scc_img3.png" alt="网络维护及网络管理的技术咨询服务"/><label>网络维护及网络管理的技术咨询服务</label></li>
					<li><img src="img/scc_img4.png" alt="提供Web安全、服务器安全评测及指导建议"/><label>提供Web安全、服务器安全评测及指导建议</label></li>
					<li><img src="img/scc_img5.png" alt="提供服务器环境配置、服务器升级、数据迁移的指导"/><label>提供服务器环境配置、服务器升级、数据迁移的指导</label></li>
				</ul>
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
		navHighlight("uts","uscs");
	})
</script>
</body>
</html>