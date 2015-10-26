<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_newsdynamics.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>新闻动态 -- 致云 ZhiCloud</title>
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
	<div class="g-ne-bd">
		<div class="wrap">
			<h3>致云动态</h3>
			<div class="ne-list">
				<div class="ne-iteam">
					<div class="ne-left">
						<img src="img/ne_img1.jpg" alt="ZhiCloud云服务平台正式商用" />
						<span class="f-db ne-data"><i class="icon-nedata"></i>2014.09.12</span>
					</div>
					<div class="ne-right">
						<h4>ZhiCloud云服务平台正式商用</h4>
						<p>10月1日，致云科技云服务平台www.zhicloud.com全新上线，该平台是致云科技继今年6月29日推出试商用平台后的全面改版和升级，新平台上线后将会为用户带来更为极致的云端体验......</p>
						<a href="u_newsdetail.jsp?do=uau&view=und" target="_black">查看详情</a>
					</div>
				</div>
				<div class="ne-iteam">
					<div class="ne-left">
						<img src="img/ne_img2.jpg" alt="致云科技携手天府软件园共建云服务平台" />
						<span class="f-db ne-data"><i class="icon-nedata"></i>2014.09.20</span>
					</div>
					<div class="ne-right">
						<h4>致云科技携手天府软件园共建云服务平台</h4>
						<p>9月11日，致云科技有限公司（以下简称致云科技）与成都天府软件园正式达成了“国家软件公共服务平台—云服务平台建设”战略合作协议。云服务平台的建设，将对天府软件园公共技术平台进行云改......</p>
						<a href="u_newsdetail_one.jsp?do=uau&view=und" target="_black">查看详情</a>
					</div>
				</div>
				<div class="ne-iteam">
					<div class="ne-left">
						<img src="img/ne_img3.jpg" alt="致云科技荣膺“2014年度云计算行业最具成长力企业奖“" />
						<span class="f-db ne-data"><i class="icon-nedata"></i>2014.10.08</span>
					</div>
					<div class="ne-right">
						<h4>致云科技荣膺“2014年度云计算行业最具成长力企业奖“</h4>
						<p>9月19日，“第六届中国行业信息化奖项评选活动暨2014中国行业信息化颁奖盛典”在北京顺利落下帷幕，致云科技有限公司（以下简称致云科技）荣获“2014年度云计算行业最具成长力企业奖”殊荣，奖项结果是经由中国工......</p>
						<a href="u_newsdetail_two.jsp?do=uau&view=und" target="_black">查看详情</a>
					</div>
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
		navHighlight("uau","und");
	})
</script>
</body>
</html>