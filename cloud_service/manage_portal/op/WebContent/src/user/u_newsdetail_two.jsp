<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_newsdetail_two.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>新闻详情 -- 致云 ZhiCloud</title>
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
	<div class="g-nd-bd">
		<div class="wrap">
			<div class="nd-cont">
				<img src="img/ne_detail_img2.jpg" alt="致云科技荣膺“2014年度云计算行业最具成长力企业奖“"/>
				<h3>致云科技荣膺“2014年度云计算行业最具成长力企业奖“</h3>
				<p>9月19日，“第六届中国行业信息化奖项评选活动暨2014中国行业信息化颁奖盛典”在北京顺利落下帷幕，致云科技有限公司（以下简称致云科技）荣获“2014年度云计算行业最具成长力企业奖”殊荣，奖项结果是经由中国工程院院士、中国电子信息产业发展研究院院长、中国计算机行业协会副会长等行业权威机构专家评选出来。</p>
				<p>本次大会得到国家工业和信息化部计算机与微电子发展研究中心（中国软件测评中心）、国家信息中心、中国软件协会等单位的大力支持，由中国信息化推进联盟、中国计算机协会、中国计算机报社主办。大会响应政府“两化融合”的概念，分析了行业信息化领域发展现状，展望未来市场，旨在加速企业信息技术集成应用模式和信息化服务模式的创新，带动中国行业信息化的迅猛发展，推动信息化和工业化的深度融合。会上重点展示了信息化在行业应用中的优秀产品、服务、解决方案及优秀应用案例。</p>
				<p>致云科技始是目前国内极少数完全依托自有知识产权，提供公有云、私有云、混合云等全产品线的云计算服务商，始终以技术创新为企业核心竞争力，已拥有几十项云计算核心专利技术。致云科技以成都为研发基地，布局北京、上海、广州、深圳、香港等地，以“市场主导、技术先行、自主创新、服务为本”为经营理念，力争打造一个共生、共赢、共进、共享的云计算产业链，成为全球领先的云计算解决方案提供商和服务商。在公司不断发展的过程中，“2014年度云计算行业最具成长力企业“此项殊荣是行业权威机构对致云科技的莫大肯定和鼓励。</p>
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
		navHighlight("uau","und");
	})
</script>
</body>
</html>