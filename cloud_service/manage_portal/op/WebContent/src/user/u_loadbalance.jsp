<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_loadbalance.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>负载均衡 -- 致云 ZhiCloud</title>
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
	<div class="g-lb-bd">
		<div class="g-sec1">
			<div class="wrap">
				<h3>负载均衡</h3>
				<p>负载均衡是对多台云服务器之间进行流量分发的一种均衡服务<br/>通过流量分发扩展应用系统对外的服务能力，能够帮助您提升业务系统的可用性</p>
				<p style="margin:30px 0;"><a class="emer-buy min-buy" href="javascript:;">立即购买</a></p>
				<div class="server"><img src="img/lb_server.png" alt="负载均衡"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-lb-sec2">
			<div class="wrap" style="overflow:hidden;">
				<div class="s2-lb-cont f-fl">
					<h3>安全保障</h3>
					<p>DDOS攻击防护，提供例如SYN COOKIES特性和延时绑定；<br/>HTTP安全，可以自定义HTTP出错页面，删除HTTP响应头中的服务器标示信息<br/>加密cookies以防止用户修改。</p>
				</div>
				<div class="s2-lb-show f-fr"><img class="f-db" src="img/lb_show.png" alt="安全保障"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-lb-sec3">
			<div class="wrap" style="overflow:hidden;">
				<div class="s3-lb-show f-fl"><img src="img/lb_lljh.png" alt="低成本"/></div>
				<div class="s3-lb-cont f-fr">
					<h3>低成本</h3>
					<p>无需采购昂贵的设备，免运维实现性能优化<br/>大量的并发访问或数据流量分担到多台云服务器上分别处理<br/>减少用户等待响应的时间。</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-lb-sec4">
			<div class="wrap" style="overflow:hidden;">
				<div class="s4-lb-cont f-fl">
					<h3>丰富的协议</h3>
					<p>提供4层（TCP协议）和7层（HTTP和HTTPS协议）的SLB服务</p>
				</div>
				<div class="s4-lb-show f-fr"><img src="img/lb_lljk.png" alt="流量监控"/></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-lb-sec3">
			<div class="wrap" style="overflow:hidden;">
				<div class="s3-lb-show f-fl" style="margin-top:100px;"><img src="img/lb_gky.png" alt="高可用"/></div>
				<div class="s3-lb-cont f-fr">
					<h3>高可用</h3>
					<p>冗余设计，无单点，可用性达99.99%，根据应用负载进行弹性扩容，<br/>流量波动情况下不中断对外服务。</p>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="g-lb-sec5">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="apt-snr-list">
					<div class="as-iteam">
						<div class="lb_as1"><img src="img/lb_as1.png" alt="横向提高业务可用性"/></div>
						<h4>横向提高业务可用性</h4>
					</div>
					<div class="as-iteam">
						<div class="lb_as2"><img src="img/lb_as2.png" alt="高并发业务需求"/></div>
						<h4>高并发业务需求</h4>
					</div>
					<div class="as-iteam">
						<div class="lb_as3"><img src="img/lb_as3.png" alt="智能无缝伸缩业务"/></div>
						<h4>智能无缝伸缩业务</h4>
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
		navHighlight("upro","ulb");
	})
</script>
</body>
</html>