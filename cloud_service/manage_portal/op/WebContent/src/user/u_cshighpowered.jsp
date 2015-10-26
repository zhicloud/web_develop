<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_cshighpowered.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>高性能云服务器 -- 致云 ZhiCloud</title>
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
	<div class="g-cshp-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>高性能云服务器</h3>
				<p class="s1-cshp-cont">基于高性能、高可靠性的SSD硬盘架设的高性能版本云服务器，整体I/O是业界普通云服务器的百倍以上，从此I/O再无压力</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="javascript:;">立即购买</a></p>
				<div class="server" style="padding-top:24px;"><img src="img/cshp_server.png" alt="高性能云服务器"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cshp-sec2">
			<div class="wrap" style="overflow:hidden;">
				<div class="s2-cshp-cont f-fl">
					<h3>更纯粹的性能提升</h3>
					<p>从系统盘到数据盘，全部采用超高性能<br/>SSD。具有高IOPS、高吞吐量、低延时等特性。</p>
					<p>轻松满足更大磁盘压力的手游App、<br/>电商、大数据、高并发访问站点等应用架构对云服务器的需求。</p>
				</div>
				<div class="s2-cshp-show f-fr">
					<ul>
						<li><img alt="15000以上 iops随机能力" src="img/cshp_show3.png"/><label>15000以上<br/>iops随机能力</label></li>
						<li><img alt="500MB/S 的吞吐量能力" src="img/cshp_show2.png"/><label>500MB/S<br/>的吞吐量能力</label></li>
						<li><img alt="1/1000s 微秒级别访问延时" src="img/cshp_show1.png"/><label>1/1000s<br/>微秒级别访问延时</label></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cshp-sec3">
			<div class="wrap">
				<h3>更优质的骨干网络</h3>
				<p>高品质电信级骨干网络，在时间延迟和传输带宽两方面得到高质量的保证，双线BGP,三十万公网独立IP。</p>
				<div class="s3-ggwl"><img src="img/cshp_ggwl.png" alt="更优质的骨干网络"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec4" style="height:540px;">
			<div class="wrap">
				<h3>更安全更稳定的服务体验</h3>
				<div class="pro-advn-list">
					<div class="pa-iteam">
						<img class="f-db" src="img/cshp_pa1.png" alt="极速快照"/>
						<h4>极速快照</h4>
						<p>让你的数据备份万无一失。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cshp_pa2.png" alt="全网智能监控"/>
						<h4>全网智能监控</h4>
						<p>7x24小时客服服务。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cshp_pa3.png" alt="高性能"/>
						<h4>高性能</h4>
						<p>从系统盘到数据盘，全部采用超高性能SSD纳秒级响应速度、500mb/s的吞吐量、15000以上IOPS随即能力</p>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cshp-sec5">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="apt-snr-list">
					<div class="as-iteam" style="margin-top:20px;">
						<div class="cshp_as1 f-fl"><img src="img/cshp_as1.png" alt="适合于关系型数据库或 其他对IO性能要求较高的数据库应用"/></div>
						<h4 class="f-fl">适合于关系型数据库或<br/>其他对IO性能要求较高的数据库应用</h4>
					</div>
					<div class="as-iteam" style="margin-top:20px;">
						<h4 class="f-fr">适合于高IO密集型应用<br/>对高IOPS、高吞吐、低延时要求较高的应用</h4>
						<div class="cshp_as2 f-fr"><img src="img/cshp_as2.png" alt="适合于高IO密集型应用 对高IOPS 高吞吐 低延时要求较高的应用"/></div>
					</div>
					<div class="as-iteam" style="margin-top:50px;">
						<div class="cshp_as3 f-fl"><img src="img/cshp_as3.png" alt="适合于更大磁盘压力的游戏 APP 电商 在线处理等"/></div>
						<h4 class="f-fl">适合于更大磁盘压力的游戏<br/>APP、电商、在线处理等</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6">
			<div class="wrap">
				<p><a class="emer-buy" href="javascript:;">立即购买</a></p>
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
		navHighlight("upro","ucshp");
	})
</script>
</body>
</html>