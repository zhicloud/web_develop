<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_mirrorhost.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>镜像主机 -- 致云 ZhiCloud</title>
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
	<div class="g-mh-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>云镜像</h3>
				<p>云镜像是一种针对定制化云服务器需求的产品，利用云镜像可以为开源或商用软件开发商一键部署云服务器。<br/>云镜像具有稳定可靠、快速灵活等特性，并经过严格审核确保云服务器安全性</p>
				<p style="margin:30px;"><a class="emer-buy min-buy" href="<%=request.getContextPath()%>/main.do?target=mhapplication" target="_blank">立即申请</a></p>
				<div class="server"><img src="img/mh_server.png" alt="云镜像"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-mh-sec2">
			<div class="wrap" style="overflow:hidden;">
				<div class="s2-mh-cont f-fl">
					<h3>稳定可靠</h3>
					<p>丰富的云服务器配置及维护经验，专业的运维团队保障镜像的可靠及稳定性。</p>
				</div>
				<div class="s2-mh-show f-fr"><img class="f-db" src="img/mh_show.png" alt="稳定可靠"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-mh-sec3">
			<div class="wrap" style="overflow:hidden;">
				<div class="s3-mh-show f-fl"><img src="img/mh_ygsh.png" alt="严格审核"/></div>
				<div class="s3-mh-cont f-fr">
					<h3>严格审核</h3>
					<p>所有镜像都经过多项严格安全检查，保障云服务器的安全性。</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-mh-sec4">
			<div class="wrap" style="overflow:hidden;">
				<div class="s4-mh-cont f-fl">
					<h3>快速灵活</h3>
					<p>镜像灵活定制，快速复制</p>
				</div>
				<div class="s4-mh-show f-fr"><img class="f-db" src="img/mh_kslh.png" alt="快速灵活"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-mh-sec5">
			<div class="wrap" style="overflow:hidden;">
				<div class="s5-mh-show f-fl"><img src="img/mh_yjbs.png" alt="一键部署"/></div>
				<div class="s5-mh-cont f-fr">
					<h3>一键部署</h3>
					<p>利用云镜像一键生成专用云服务器，无需关心系统环境配置<br/>软件产品选型及安装。</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6" style="background:#eff2f4;">
			<div class="wrap">
				<p><a class="emer-buy" href="u_mhapplication.jsp" target="_blank">立即申请</a></p>
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
		navHighlight("upro","umh");
	})
</script>
</body>
</html>