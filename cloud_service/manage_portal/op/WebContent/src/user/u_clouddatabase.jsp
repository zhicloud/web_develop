<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<base href="<%=request.getContextPath() %>/src/user/u_clouddatabase.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>云数据库 -- 致云 ZhiCloud</title>
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
	<div class="g-cdb-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>云数据库</h3>
				<p>云数据库是一种稳定可靠、可弹性伸缩的在线数据库服务，基于MySQL关系型数据库<br/>并提供数据库集群、负载均衡、备份恢复、性能监控及数据分析功能</p> 
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="javascript:;">即将开放</a></p> 
				<div class="server"><img src="img/cdb_server.png" alt="云数据库"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cdb-sec2">
			<div class="wrap" style="overflow:hidden;">
				<div class="s2-cdb-cont f-fl">
					<h3>稳定可靠</h3>
					<p>采用分布式存储集群，提供99.95%服务可用性，99.999%数据可靠性<br/>多重副本备份数据库，提供数据容灾、数据恢复</p>
				</div>
				<div class="s2-cdb-show f-fr"><img class="f-db" src="img/cdb_show.png" alt="稳定可靠"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cdb-sec3">
			<div class="wrap" style="overflow:hidden;">
				<div class="s3-cdb-show f-fl"><img src="img/cdb_lhkz.png" alt="灵活扩展"/></div>
				<div class="s3-cdb-cont f-fr">
					<h3>灵活扩展</h3>
					<p>根据数据库的实际负载情况进行扩容升级<br/>从而获取更高的数据库性能及可靠</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cdb-sec4">
			<div class="wrap" style="overflow:hidden;">
				<div class="s4-cdb-cont f-fl">
					<h3>无缝兼容</h3>
					<p>无缝兼容多版本的MySQL，不需要做任何改动<br/>一键数据迁移，简单易用</p>
				</div>
				<div class="s4-cdb-show f-fr"><img src="img/cdb_wfjr.png" alt="无缝兼容"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cdb-sec5">
			<div class="wrap" style="overflow:hidden;">
				<div class="s5-cdb-show f-fl"><img src="img/cdb_jktj.png" alt="监控统计"/></div>
				<div class="s5-cdb-cont f-fr">
					<h3>监控统计</h3>
					<p>多项性能资源在线监控，可对资源项设置阈值报警<br/>并提供WEB管理操作、数据报表、日志及数据分析功能</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6" style="background:#eff2f4;">
			<div class="wrap">
				<p><a class="emer-buy" href="javascript:;">即将开放</a></p>
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
		navHighlight("upro","ucdb");
	})
</script>
</body>
</html>
