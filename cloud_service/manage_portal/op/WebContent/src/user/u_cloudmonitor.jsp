<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<base href="<%=request.getContextPath() %>/src/user/u_cloudmonitor.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>云监控 -- 致云 ZhiCloud</title>
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
	<div class="g-cm-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>云监控</h3>
				<p>提供全面的云产品数据监控，智能预警、智能分析数据、<br/>自定义告警方式，准确及时了解您的云产品状况，为您的云产品保驾护航。</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="javascript:;">立即购买</a></p>
				<div class="server"><img src="img/cm_yjk.png" alt="云监控"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cm-sec2">
			<div class="wrap" style="overflow:hidden;">
				<div class="s2-cm-cont f-fl">
					<h3>秒级告警</h3>
					<p>全国分布式监测网络，稳定性可用性实时分析<br/>捕捉深层次性能指标。</p>
				</div>
				<div class="s2-cm-show f-fr"><img class="f-db" src="img/cm_zbff.png" alt="秒级告警"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cm-sec3">
			<div class="wrap" style="overflow:hidden;">
				<div class="s3-cm-show f-fl"><img src="img/cm_zdyjg.png" alt="自定义告警"/></div>
				<div class="s3-cm-cont f-fr">
					<h3>自定义告警</h3>
					<p>所有指标均可自定义设置阀值，每个阀值可关联不同云产品。<br/>默认阀值自动关联新建产品。用户也可自定义告警接收人和<br/>多种接收终端（app、短信、邮件）</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cm-sec4">
			<div class="wrap" style="overflow:hidden;">
				<div class="s4-cm-cont f-fl">
					<h3>监控分析</h3>
					<p>云资源指标综合分析，计算出整体系统健康得分<br/>第一时间了解系统健康。</p>
				</div>
				<div class="s4-cm-show f-fr"><img class="f-db" src="img/cm_jkfx.png" alt="监控分析"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-cm-sec5">
			<div class="wrap" style="overflow:hidden;">
				<div class="s5-cm-show f-fl"><img src="img/cm_jdyy.png" alt="轻量级体验"/></div>
				<div class="s5-cm-cont f-fr">
					<h3>轻量级体验</h3>
					<p>向导式部署和图表化展现，即使没有专业运维团队也<br/>可以第一时间掌握系统运行情况。</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-sec5" style="height:450px;">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="cm-snr-list cmas">
					<div class="as-iteam">
						<div class="cm_as1"></div>
						<h4>游戏行业</h4>
					</div>
					<div class="as-iteam cm_middle">
						<div class="cm_as2"></div>
						<h4>电商行业</h4>
					</div>
					<div class="as-iteam">
						<div class="cm_as3"></div>
						<h4>互联网平台公司</h4>
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
		navHighlight("upro","ucm");
	})
</script>
</body>
</html>