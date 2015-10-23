<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_clouddrive.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>云硬盘 -- 致云 ZhiCloud</title>
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
	<div class="g-cd-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>CloudSAN</h3>
				<p>CloudSAN是一种数据块级读写方式的存储服务，可直接替换原有传统存储设备且应用无需重新开放<br/>是您获得更稳定更安全的存储服务，按量付费的方式能够帮助您降低IT总体成本。</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudDiskService&method=addPage">立即购买</a></p>
				<div class="server" style="padding-top:30px;"><img src="img/cd_server.png" alt="服务器"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec2">
			<div class="wrap">
				<h3 class="f-fl">CloudSAN是由传统存储服务器<br/>通过虚拟化技术形成</h3>
				<div class="cd-show f-fr"><img class="f-db" src="img/cd_show.png" alt="云服务器展示"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec3" style="height:502px;">
			<div class="wrap">
				<h3>可根据您不同的业务需求“按需定制”CloudSAN，并可“实时变量”容量</h3>
				<ul class="cd-var-cont">
					<li>
						<div class="cd_etylev"></div>
						<h4>20M</h4>
					</li>
					<li>
						<div class="cd_stanlev"></div>
						<h4>50M</h4>
					</li>
					<li>
						<div class="cd_busilev"></div>
						<h4>100M</h4>
					</li>
					<li>
						<div class="cd_complev"></div>
						<h4>200M</h4>
					</li>
					<li>
						<div class="cd_proflev"></div>
						<h4>500M</h4>
					</li>
				</ul>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec4">
			<div class="wrap">
				<h3>产品优势</h3>
				<p class="tips">云硬盘相比传统存储设备有明显的优点</p>
				<div class="pro-advn-list pa">
					<div class="pa-iteam">
						<img class="f-db" src="img/cd_pa1.png" alt="稳定可靠"/>
						<h4>无缝衔接</h4>
						<p>原有IT系统无缝接入CloudSAN；可直接替换企业原有的传统存储设备，企业应用无需重新开发。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cd_pa2.png" alt="安全保障"/>
						<h4>稳定可靠</h4>
						<p>CloudSAN数据三重备份，服务可靠性高达99.99%自动修复，CloudSAN平台后台持续检查数据一致性与完整性，并自动修复，无需人工干预，保障系统可用性。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cd_pa3.png" alt="弹性计费"/>
						<h4>在线扩容</h4>
						<p>存储空间可根据您的业务发展，在线快速扩展到更大容量，无需暂停正在运行的业务系统，从而提供了您业务系统的可用性。</p>
					</div>
					<div class="pa-iteam">
						<img class="f-db" src="img/cd_pa4.png" alt="优质网络"/>
						<h4>大规模&nbsp;&nbsp;高性能</h4>
						<p>系统支持100PB以上存储规模IO读写响应时间保持纳秒级，事务级别超高速缓冲，平滑负载均衡。</p>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec5">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="apt-snr-list as">
					<div class="as-iteam">
						<div class="cd_as1"></div>
						<h4>数据库备份</h4>
					</div>
					<div class="as-iteam middle">
						<div class="cd_as2"></div>
						<h4>文件系统备份</h4>
					</div>
					<div class="as-iteam">
						<div class="cd_as3"></div>
						<h4>操作系统备份</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec6">
			<div class="wrap">
				<p><a class="emer-buy" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudDiskService&method=addPage">立即购买</a></p>
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
		navHighlight("upro","ucd");
	})
</script>
</body>
</html>