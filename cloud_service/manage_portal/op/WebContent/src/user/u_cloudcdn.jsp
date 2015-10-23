<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
     <base href="<%=request.getContextPath() %>/src/user/u_cloudcdn.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>云CDN -- 致云 ZhiCloud</title>
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
	<div class="g-ccdn-bd">
		<div class="g-sec1">
			<div class="wrap" style="overflow:hidden;">
				<h3>云CDN</h3>
				<p>云CDN CDN（Content Delivery Network）内容分发网络，是指将源站内容分发至全国所有的节点，智能为用户选择最<br/>佳访问节点，缩短延迟，大幅提高网站响应速度及用户资源利用率。</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="javascript:;">即将开放</a></p>
			</div>
			<div class="ccdn_server"><img src="img/ccdn_server.png" alt="云数据库"/></div>
		</div>
		<div class="f-cb"></div>
		<div class="g-ccdn-sec2">
			<div class="wrap">
				<div class="s2-ccdn-cont f-fl">
					<h3>全面加速</h3>
					<p>提供web、文件下载、图片视频等多种业务加速能力，<br/>让您轻松应对高并发访问。</p>
				</div> 
				<div class="s2-ccdn-show f-fr">
					<ul>
						<li><img alt="web" src="img/ccdn_show1.png"><label>web</label></li>
						<li><img alt="文件下载" src="img/ccdn_show2.png"><label>文件下载</label></li>
						<li><img alt="图片视频" src="img/ccdn_show3.png"><label>图片视频</label></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-ccdn-sec3">
			<div class="wrap">
				<h3>全业务支撑</h3>
				<div class="pro-qywzc-list">
					<div class="qywzc-iteam">
						<img class="f-db" src="img/ccdn_qywzc1.png" alt="支持多种接入方式"/>
						<h4>支持多种接入方式</h4>
						<p>域名、ftp、svn、api</p>
					</div>
					<div class="qywzc-iteam">
						<img class="f-db" src="img/ccdn_qywzc2.png" alt="支持多种协议类型"/>
						<h4>支持多种协议类型</h4>
						<p>HTTP、HTTPS<br/>HLS、RTSP、RTMP</p>
					</div>
					<div class="qywzc-iteam">
						<img class="f-db" src="img/ccdn_qywzc3.png" alt="支持多种网络类型"/>
						<h4>支持多种网络类型</h4>
						<p>2G、3G、4G、<br/>WLAN、宽带接入</p>
					</div>
					<div class="qywzc-iteam">
						<img class="f-db" src="img/ccdn_qywzc4.png" alt="支持多种终端加速"/>
						<h4>支持多种终端加速</h4>
						<p>PC、笔记本、PAD<br/>手机、OTT机顶盒</p>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-ccdn-sec4">
			<div class="wrap" style="overflow:hidden;">
				<div class="s4-ccdn-show f-fl"><img src="img/ccdn_dcb.png" alt="低成本"/></div>
				<div class="s4-ccdn-cont f-fr">
					<h3>低成本</h3>
					<p>弹性扩张，按需计费，用更少的成本达到更好的体验</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-ccdn-sec5">
			<div class="wrap" style="overflow:hidden;">
				<div class="s5-ccdn-cont f-fl">
					<h3>安全稳定</h3>
					<p>拥有强大的DDOS、CC防护能力，实时阻断隐藏源站IP<br/>7x24小时保障网站访问的正常运行</p>
				</div>
				<div class="s5-ccdn-show f-fr"><img src="img/ccdn_aqwd.png" alt="安全稳定"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-ccdn-sec6">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="s6-ccdn-list">
					<div class="ccdn-iteam" style="margin-right:50px;">
						<div class="ccdn_yycj1"><img src="img/ccdn_yycj1.png" alt="web加速"/></div>
						<h4>web加速</h4>
					</div>
					<div class="ccdn-iteam" style="margin-right:50px;">
						<div class="ccdn_yycj2"><img src="img/ccdn_yycj2.png" alt="下载平台"/></div>
						<h4>下载平台</h4>
					</div>
					<div class="ccdn-iteam" style="margin-right:50px;">
						<div class="ccdn_yycj3"><img src="img/ccdn_yycj3.png" alt="音视频加速"/></div>
						<h4>音视频加速</h4>
					</div>
					<div class="ccdn-iteam">
						<div class="ccdn_yycj4"><img src="img/ccdn_yycj4.png" alt="app加速"/></div>
						<h4>app加速</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6">
			<div class="wrap">
				<p><a class="emer-buy" href="javascript:;">即将开放</a></p>
			</div>
		</div>
	</div>
	<!-- footer -->
	<%@ include file="../common/tpl/u_footer.jsp"%>
	<!-- /footer -->
</div>
<!-- login -->
<%@ include file="../common/tpl/u_login.jsp"%>
<!-- /login -->
<script type="text/javascript">
	$(function(){
		navHighlight("upro","uccdn");
	})
</script>
</body>
</html>