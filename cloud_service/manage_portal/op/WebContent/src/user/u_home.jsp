<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_home.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>致云 ZhiCloud -- 致力于云计算，打造非同凡响的云服务体验</title>
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
	<div class="g-banner">
		<ul>
			<li style="background: url('img/banner3.jpg') no-repeat center top;"><a target="_blank" href="javascript:;" title="致云科技"></a></li>
			<li style="background: url('img/banner5.jpg') no-repeat center top;"><a target="_blank" href="javascript:;" title="秒级创建云主机"></a></li>
			<li style="background: url('img/banner4.jpg') no-repeat center top;"><a target="_blank" href="<%=request.getContextPath() %>?target=eggplan" title="蛋壳+计划"></a></li>
			<li style="background: url('img/banner2.jpg') no-repeat center top;"><a target="_blank" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage" title="专属云-简单.极致"></a></li>
			<li style="background: url('img/banner6.jpg') no-repeat center top;"><a target="_blank" href="javascript:;" title="复杂数据处理性能"></a></li>
		</ul>
	</div>
	<div class="f-cb"></div>
	<div class="g-h-bd">
	    <div class="wrap">
	     	<div class="pro-list">
	     		<div class="iteam">
	     			<a class="iproduct" href="<%=request.getContextPath() %>/buy.do?flag=buy">
	     				<img alt="云主机" src="img/icon_yzj.png"/>
	     				<img class="iproducthover" alt="云主机" src="img/icon_yzj_hov.png"/>
	     			</a>
	     			<h3>云主机</h3>
	     			<p>云主机是全新云服务器租用服务，能够帮助您快速上线业务，降低IT运维成本提高IT管理效率具有快速部署安全可靠、弹性伸缩等特性。</p>
	     			<a class="more" href="<%=request.getContextPath() %>/main.do?target=cloudser">了解更多&gt;</a>
	     		</div>
	     		<div class="iteam" style="margin-left:116px;">
	     			<a class="iproduct" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage">
	     				<img alt="云主机" src="img/icon_zsy.png"/>
	     				<img class="iproducthover" alt="云主机" src="img/icon_zsy_hov.png"/>
	     			</a>
	     			<h3>专属云</h3>
	     			<p>由于安全性和隔离性等方面的考虑，或客户需要将多台云服务器放置在隔离的内网中进行集群作业，从而组合而成的一个带有软路由器和软防火墙的虚拟专属内网。</p>
	     			<a class="more" href="<%=request.getContextPath() %>/main.do?target=exclucloud">了解更多&gt;</a>
	     		</div>
	     		<div class="iteam" style="margin: 0 117px;">
	     			<a class="iproduct" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudDiskService&method=addPage">
	     				<img alt="云主机" src="img/icon_yyp.png"/>
	     				<img class="iproducthover" alt="云主机" src="img/icon_yyp_hov.png"/>
	     			</a>
	     			<h3>云硬盘</h3>
	     			<p>自主研发的分布式云存储，高吞吐、高并发、高可用，可直接代替原有传统存储设备，且应用无需重新开发帮助传统信息系统无缝升级到云计算时代。</p>
	     			<a class="more" href="<%=request.getContextPath() %>/main.do?target=clouddrive">了解更多&gt;</a>
	     		</div>
	     		<div class="iteam">
	     			<a class="iproduct" href="<%=request.getContextPath() %>/main.do?target=solution">
		     			<img alt="云主机" src="img/icon_jjfa.png"/>
		     			<img class="iproducthover" alt="云主机" src="img/icon_jjfa_hov.png"/>
	     			</a>
	     			<h3>解决方案</h3>
	     			<p>依托自主研发的大规模、高可靠、高弹性、高性能的云管理平台、云存储、云桌面提供整体的云融合解决方案。</p>
	     			<a class="more" href="<%=request.getContextPath() %>/main.do?target=solution">了解更多&gt;</a>
	     		</div>
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
<!-- JavaScript -->
<script type="text/javascript" src="js/u_home.js"></script>
<!-- /JavaScript -->
</body>
</html>