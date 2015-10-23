<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_helpcenter.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>帮助中心 -- 致云 ZhiCloud</title>
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
	<div class="g-hc-bd">
		<div class="wrap">
			<div class="hc-cont">
				<div class="slider-left f-fl" id="subNav">
					<div class="m-tab">
	                    <h3><i class="hc_icons hc_icons_cjwt"></i><a class="hc-link" href="javascript:;" data-href="1">常见问题</a></h3>
	                </div>
	                <div class="m-tab">
	                    <h3 class="mt20"><i class="hc_icons hc_icons_zhxg"></i>账户相关</h3>
	                    <ul>
	                        <li><a class="hc-link" href="javascript:;" data-href="10">新用户注册</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="11">账户充值</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="12">获取发票</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="13">现金券兑换</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="14">修改邮箱</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="15">修改手机</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="16">修改密码</a></li>
	                    </ul>
	                </div>
	                <div class="m-tab">
	                    <h3 class="mt20"><i class="hc_icons hc_icons_yzj"></i>云主机</h3>
	                    <ul>
	                        <li><a class="hc-link" href="javascript:;" data-href="20">创建云主机</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="21">连接云主机</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="22">使用数据盘</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="23">相关初始密码</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="24">修改密码</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="25">上传文件</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="26">更改配置</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="27">停用/启用</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="28">资源监控</a></li>
	                        <li><a class="hc-link" href="javascript:;" data-href="29">ICP备案</a></li>
	                    </ul>
	                </div>
				</div>
				<div class="cont-right f-fr">
					<div id="getContents"></div>
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
<!-- JavaScript -->
<script type="text/javascript">var propath = '<%= request.getContextPath()%>';</script>
<script type="text/javascript" src="js/u_helpcenter.js"></script>
<script type="text/javascript">
	$(function(){
		navHighlight("uts","uhc");
	})
</script>
<!-- /JavaScript -->
</body>
</html>