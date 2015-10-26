<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_solution.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>解决方案 -- 致云 ZhiCloud</title>
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
	<div class="g-s-banner"></div>
	<div class="g-s-bd">
		<div class="s-nav">
			<div class="wrap">
				<div class="s-top-cont">
					<h3>产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;介</h3>
					<img src="<%=request.getContextPath()%>/src/user/img/pro_brief.png" alt="Product Brief"/>
					<p>针对不同的项目需求提供云计算解决方案——云管理平台、云存储、云桌面，以及三种融合的解决方案。</p>
				</div>
				<div class="s-list-cont">
					<ul>
						<li>
							<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/mag_plat.png" alt="云管理平台"/></div>
							<h4>云管理平台</h4>
							<p>云管理平台能够增强企业IT资源管控和服务能力，提高IT资源利用率，具有虚拟化，高可靠性，高安全性，高弹性、大规模等特点，能够实现数据中心的监控与管理的自动化。</p>
							<a href="#s-mp">&gt;&nbsp;&nbsp;&nbsp;&nbsp;了解更多</a>
						</li>
						<li>
							<div class="s-img"><img style="vertical-align:top;" src="<%=request.getContextPath()%>/src/user/img/cld_stora.png" alt="云存储"/></div>
							<h4>云存储</h4>
							<p>致存™是致云科技自主研发的云存储产品系列，内置自主研发的分布式集群系统ZFS（ZhiCloud File System），采用X86架构服务器，具有大规模、高性能、高稳定、高可用等特点。</p>
							<a href="#s-cs">&gt;&nbsp;&nbsp;&nbsp;&nbsp;了解更多</a>
						</li>
						<li>
							<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/cld_dest.png" alt="云桌面"/></div>
							<h4>云桌面</h4>
							<p>基于ARM架构，构建在云管理平台和云存储上的新一代桌面云平台；采用“云终端”办公模式，具有节能减排、集中管控、数据安全、降低成本等特点。</p>
							<a href="#s-cd">&gt;&nbsp;&nbsp;&nbsp;&nbsp;了解更多</a>
						</li>
						<li>
							<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/cld_audit.png" alt="云审计"/></div>
							<h4>云审计</h4>
							<p>致云云审计是基于自主研发的云计算技术推出的网络操作行为审计管理产品。它通过对自然人身份以及资源、资源账号的集中管理建立“自然人账号——资源——资源账号”对应关系，实现自然人对资源的统一授权</p>
							<a href="#s-ca">&gt;&nbsp;&nbsp;&nbsp;&nbsp;了解更多</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="s-mag-platform s-layout" id="s-mp">
			<div class="s-nav-cont" id="m1">
				<div class="s-logo" id="mag_plat">
					<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/mag_plat.png" alt="云管理平台"/></div>
					<h4>云管理平台</h4>
					<span>Management platform</span>
				</div>
				<ul>
					<li><a onclick="gotoSolution(1,2)" href="javascript:;">整体架构</a></li>
					<li><a onclick="gotoSolution(1,3)" href="javascript:;">产品功能</a></li>
					<li><a onclick="gotoSolution(1,4)" href="javascript:;">特点优势</a></li>
					<li><a onclick="gotoSolution(1,5)" href="javascript:;">应用场景</a></li>
				</ul>
			</div>
			<div class="s-cont-list" id="s1">
				<div class="s-iteam s-iteam_1_1"></div>
				<div class="s-iteam s-iteam_1_2"></div>
				<div class="s-iteam s-iteam_1_3"></div>
				<div class="s-iteam s-iteam_1_4"></div>
				<div class="s-iteam s-iteam_1_5"></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="s-cld_stora s-layout" id="s-cs">
			<div class="s-nav-cont" id="m2">
				<div class="s-logo" id="cld_stora">
					<div class="s-img"><img style="vertical-align:top;" src="<%=request.getContextPath()%>/src/user/img/cld_stora.png" alt="云存储"/></div>
					<h4>云存储</h4>
					<span>Cloud storage</span>
				</div>
				<ul>
					<li><a onclick="gotoSolution(2,2)" href="javascript:;">整体架构</a></li>
					<li><a onclick="gotoSolution(2,3)" href="javascript:;">产品功能</a></li>
					<li><a onclick="gotoSolution(2,4)" href="javascript:;">特点优势</a></li>
					<li><a onclick="gotoSolution(2,5)" href="javascript:;">应用场景</a></li>
				</ul>
			</div>
			<div class="s-cont-list" id="s2">
				<div class="s-iteam s-iteam_2_1"></div>
				<div class="s-iteam s-iteam_2_2"></div>
				<div class="s-iteam s-iteam_2_3"></div>
				<div class="s-iteam s-iteam_2_4"></div>
				<div class="s-iteam s-iteam_2_5"></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="s-cld_dest s-layout" id="s-cd">
			<div class="s-nav-cont" id="m3">
				<div class="s-logo" id="cld_dest">
					<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/cld_dest.png" alt="云桌面"/></div>
					<h4>云桌面</h4>
					<span>Cloud desktop</span>
				</div>
				<ul>
					<li><a onclick="gotoSolution(3,2)" href="javascript:;">整体架构</a></li>
					<li><a onclick="gotoSolution(3,3)" href="javascript:;">产品功能</a></li>
					<li><a onclick="gotoSolution(3,4)" href="javascript:;">特点优势</a></li>
					<li><a onclick="gotoSolution(3,5)" href="javascript:;">应用场景</a></li>
				</ul>
			</div>
			<div class="s-cont-list" id="s3">
				<div class="s-iteam s-iteam_3_1"></div>
				<div class="s-iteam s-iteam_3_2"></div>
				<div class="s-iteam s-iteam_3_3"></div>
				<div class="s-iteam s-iteam_3_4"></div>
				<div class="s-iteam s-iteam_3_5"></div>
			</div>
		</div>
		<div class="f-cb"></div>
		<div class="s-cld_audit s-layout" id="s-ca">
			<div class="s-nav-cont" id="m4">
				<div class="s-logo" id="cld_audit">
					<div class="s-img"><img src="<%=request.getContextPath()%>/src/user/img/cld_audit.png" alt="云审计"/></div>
					<h4>云审计</h4>
					<span>Cloud desktop</span>
				</div>
				<ul>
					<!-- <li><a onclick="gotoSolution(4,2)" href="javascript:;">整体架构</a></li> -->
					<li><a onclick="gotoSolution(4,2)" href="javascript:;">产品功能</a></li>
					<li><a onclick="gotoSolution(4,3)" href="javascript:;">特点优势</a></li>
					<li><a onclick="gotoSolution(4,4)" href="javascript:;">应用场景</a></li>
				</ul>
			</div>
			<div class="s-cont-list" id="s4" style="width:400%;">
				<div class="s-iteam sca s-iteam_4_1"></div>
				<!-- <div class="s-iteam s-iteam_4_2"></div> -->
				<div class="s-iteam sca s-iteam_4_2"></div>
				<div class="s-iteam sca s-iteam_4_3"></div>
				<div class="s-iteam sca s-iteam_4_4"></div>
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
<script type="text/javascript" src="js/u_solution.js"></script>
<!-- /JavaScript -->
<script type="text/javascript">
	$(function(){
		navHighlight("us","");
	})
</script>
</body>
</html>