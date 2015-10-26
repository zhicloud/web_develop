<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_aggplan.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>蛋壳+计划 -- 致云 ZhiCloud</title>
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
	<div class="g-ep-banner">
		<div class="wrap">
			<div class="epb-tit"><img src="img/epb_tit.png" alt="蛋壳+计划"/></div>
			<div class="ept-cont">
				<p style="margin-top:-5px;">蛋壳<sup>+</sup>计划意为创业团队实现拧包入驻——完善的办公环境、强大的致云公有云平台支持、雄厚的创投资金、快速的审核机制以及上下游资源的整合等</p>
				<p>形成完美闭环，大大减少创业团队的入驻成本。在国家大力倡导“万众创新 大众创业”的环境下，以及结合国内孵化器存在的现状，致云科技运用互联网</p>
				<p style="margin-top:-5px;">模式和强大的致云公有云平台支持将重新定义孵化器，蛋壳<sup>+</sup>计划将带领孵化器/创业创新团队进入全新的资本+IT云服务时代。</p>
			</div>
			<a class="emer-apply" href="<%=request.getContextPath()%>/main.do?target=eggplanapplication" target="_black">立即申请</a>
		</div>
	</div>
	<div class="g-ep-bd">
		<div class="ep_sec1">
			<div class="s-cont-top">
				<div class="wrap">
					<h3>云服务</h3>
					<p>为孵化器提供云服务或搭建云服务平台，带领进入云孵化器时代</p>
					<ul>
						<li><img src="img/ep_yfwq.png" alt="云服务器"/><label>云服务器</label></li>
						<li><img src="img/ep_yyp.png" alt="云硬盘"/><label>云硬盘</label></li>
						<li><img src="img/ep_zsy.png" alt="专属云"/><label>专属云</label></li>
						<li class="ep-last"><img src="img/ep_ynccc.png" alt="云内存存储"/><label>云内存存储</label></li>
						<li><img src="img/ep_kzhf.png" alt="快照恢复"/><label>快照恢复</label></li>
						<li><img src="img/ep_ysjk.png" alt="云数据库"/><label>云数据库</label></li>
						<li><img src="img/ep_kfapi.png" alt="开放api"/><label>开放api</label></li>
						<li class="ep-last"><img src="img/ep_fzjh.png" alt="负载均衡"/><label>负载均衡</label></li>
					</ul>
				</div>
			</div>
			<div class="s-cont-bottom">
				<div class="wrap">
					<h3>咨询服务</h3>
					<ul>
						<li><img src="img/ep_zxfw1.png" alt="规划、搭建、管理云服务器"/><label>规划、搭建、管理云服务器</label></li>
						<li><img src="img/ep_zxfw2.png" alt="按需设计云服务器部署架构"/><label>按需设计云服务器部署架构</label></li>
						<li><img src="img/ep_zxfw3.png" alt="网络维护及网络管理技术咨询"/><label>网络维护及网络管理技术咨询</label></li>
						<li><img src="img/ep_zxfw4.png" alt="Web、服务安全评测及指导"/><label>Web、服务安全评测及指导</label></li>
						<li class="ep-last"><img src="img/ep_zxfw5.png" alt="服务器环境配置、升级、数据迁移的指导"/><label>服务器环境<br/>配置、升级、数据迁移的指导</label></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="ep_sec2">
			<div class="wrap">
				<h3>蛋壳基金</h3>
				<p>十亿产业扶持基金 上下游资源整合  为孵化器提供资金资源全面保障</p>
				<img src="img/ep_billion.png" alt="10亿"/>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="ep_sec3">
			<div class="wrap">
				<h3>合作伙伴</h3>
				<ul>
					<li><img src="img/ep_tfrjy.png" alt="天府软件园"/></li>
					<li class="ep-middle"><img src="img/ep_cyc.png" alt="创业场"/></li>
					<li><img src="img/ep_cykj.png" alt="创业空间"/></li>
				</ul>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="ep_sec4">
			<div class="wrap">
				<h3>合作方案</h3>
				<div class="s-hzfa-cont">
					<div class="ep-left-cont f-fl">
						<h4>方案1</h4>
						<dl>
							<dt>致云向进驻孵化器的团队扶持</dt>
							<dd>1：为初创团队免费提供云服务器；</dd>
							<dd>2：配置：2核CPU/4G内存/100G存储/5M带宽/1独立IP；</dd>
							<dd>3：时长：6个月或折算为2000元的代金券提供给初创团队。</dd>
						</dl>
					</div>
					<div class="ep-right-cont f-fr">
						<h4>方案2</h4>
						<dl>
							<dt>致云利用自身云计算产品与服务为孵化器建立专有的云服务平台</dt>
							<dd>1：由孵化器统一向其所属初创团队免费提供云服务器</dd>
							<dd>2：配置：2核CPU/4G内存/100G存储/5M带宽/1独立IP</dd>
							<dd>3：时长：6个月或折算为2000元的代金券提供给初创团队。</dd>
						</dl>
						<p>注：免费额度到期后的续费均直接通过孵化器云平台完成，孵化器与致云共享后期收益。</p>
					</div>
				</div>
				<div class="ep-link"><a href="u_epapplication.jsp?do=uep" target="_black">立即申请</a></div>
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
		navHighlight("uep","");
	})
</script>
</body>
</html>