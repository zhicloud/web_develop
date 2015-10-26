<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_cloudser.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>云服务器 -- 致云 ZhiCloud</title>
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
	<div class="g-cs-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>全新云服务租用服务</h3>
				<p>云服务器是全新服务器租用服务，能够帮助您快速上线业务、降低运维成本、提高管理效率<br/>并具有快速部署、安全可靠、弹性伸缩等特性</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="<%=request.getContextPath() %>/buy.do?flag=buy">立即购买</a></p>
				<div class="server"style="padding-top:90px;"><img src="img/cs_server.png" alt="服务器"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec2">
			<div class="wrap">
				<h3 class="f-fl">云主机是由传统物理服务器<br/>通过虚拟化技术形成</h3>
				<div class="cs-show f-fr"><img class="f-db" src="img/cs_show.png" alt="云服务器展示"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec3">
			<div class="wrap">
				<h3>可根据您不同的业务需求“按需定制”云服务器，并可“实施变更”服务器配置</h3>
				<ul>
					<li>
						<div class="cs_etylev"></div>
						<h4>入门级</h4>
						<p>2核/2G/20G/2M</p>
					</li>
					<li>
						<div class="cs_stanlev"></div>
						<h4>标准级</h4>
						<p>2核/4G/50G/2M</p>
					</li>
					<li>
						<div class="cs_busilev"></div>
						<h4>商务级</h4>
						<p>4核/4G/100G/4M</p>
					</li>
					<li>
						<div class="cs_complev"></div>
						<h4>企业级</h4>
						<p>8核/8G/200G/6M</p>
					</li>
					<li>
						<div class="cs_proflev"></div>
						<h4>专业级</h4>
						<p>12核/12G/500G/10M</p>
					</li>
				</ul>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec4">
			<div class="wrap">
				<h3>产品优势</h3>
				<p class="tips">云主机是一种全新的服务器租用服务</p>
				<div class="pro-advn-list">
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa1.png" alt="稳定可靠"/>
						<h4>秒级创建</h4>
						<p>CPU、内存、磁盘、镜像、网络2秒交付使用。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa2.png" alt="安全保障"/>
						<h4>安全保障</h4>
						<p>采用自主沙盒技术隔绝工作网络和云主机，各主机资源相互隔离，防DDoS系统，防火墙安全组规则保护。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa3.png" alt="弹性计费"/>
						<h4>弹性计费</h4>
						<p>灵活的计费方式:支持按时/按量的弹性收费，也支持传统云主机产品组合式收费，还能基于实时资源监控系统指定更加个性化的计费策略。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa4.png" alt="优质网络"/>
						<h4>优质网络</h4>
						<p>云服务平台提供电信五星机房BGP线路，独立IP地址。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa5.png" alt="统一监控"/>
						<h4>统一监控</h4>
						<p>实时高精度的统一资源监控，每个cpu内核计算时间、瞬时的内存用量到io用量增幅。	</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cs_pa6.png" alt="自助服务"/>
						<h4>自助服务</h4>
						<p>一键式自助服务：用户申请1秒即可用，由平台统一完成对计算资源、存储资源、网络资源的创建和调配。</p>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec5">
			<div class="wrap">
				<h3>应用场景</h3>
				<p>云主机适用于各种有服务器需求的用户</p>
				<div class="apt-snr-list">
					<div class="as-iteam">
						<div class="cs_as1"></div>
						<h4>企业或个人门户网站</h4>
					</div>
					<div class="as-iteam">
						<div class="cs_as2"></div>
						<h4>企业各种业务系统应用服务器</h4>
					</div>
					<div class="as-iteam">
						<div class="cs_as3"></div>
						<h4>电商/游戏/移动APP等平台服务器</h4>
					</div>
					<div class="as-iteam">
						<div class="cs_as4"></div>
						<h4>企业或个人开发环境部署托管</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6">
			<div class="wrap">
				<p><a class="emer-buy" href="<%=request.getContextPath() %>/buy.do?flag=buy">立即购买</a></p>
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
		navHighlight("upro","ucs");
	})
</script>
</body>
</html>