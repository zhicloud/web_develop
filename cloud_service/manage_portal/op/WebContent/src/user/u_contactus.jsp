<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_contactus.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>联系我们 -- 致云 ZhiCloud</title>
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
	<div class="g-cu-bd">
		<div class="wrap">
			<div class="cu-list">
				<div class="cu-iteam">
					<img src="img/cu_chengdu.png" alt="致云科技总部" />
					<h3>致云科技总部</h3>
					<p><label>地址：</label>成都市天府软件园C区12栋15层</p>
					<p><label>座机：</label>028-69189999</p>
					<p><label>传真：</label>028-61291999</p>
				</div>
				<div class="cu-iteam">
					<img src="img/cu_guangzhou.png" alt="致云科技广州公司" />
					<h3>致云科技广州公司</h3>
					<p><label>地址：</label>广州市天河区黄埔大道西100号富力盈泰广场A塔1011</p>
					<p><label>座机：</label>020-38260999</p>
					<p><label>传真：</label>020-38260988</p>
				</div>
				<div class="cu-iteam">
					<img src="img/cu_shanghai.jpg" alt="致云科技上海公司" />
					<h3>致云科技上海公司</h3>
					<p><label>地址：</label>上海市普陀区真北路915号1505</p>
					<p><label>座机：</label>021-62868099</p>
					<p><label>传真：</label>021-62869177</p>
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
<script type="text/javascript">
	$(function(){
		navHighlight("uau","ucu");
	})
</script>
</body>
</html>