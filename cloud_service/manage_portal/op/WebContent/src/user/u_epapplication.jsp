<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
   <base href="<%=request.getContextPath() %>/src/user/u_application.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>蛋壳+计划申请 -- 致云 ZhiCloud</title>
	<link rel="shortcut icon" href="../common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/usersite.css" media="all"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
<div class="g-doc">
	<!-- header -->
	<%@ include file="../common/tpl/u_header.jsp"%>
	<!-- /header -->
	<div class="f-cb"></div>
	<div class="g-epa-bd">
		<div class="wrap">
			<h3>申请加入蛋壳<sup>+</sup>计划</h3>
			<form id="applicationForm"  action="" method="post">
			<ul>
				<li>
					<label><i>*</i>孵化器/园区全称：</label>
					<input type="text" name="incubatorName" id="ep_incubatorname" class="a-ipt ep_incubatorname" maxlength="25" required/>
					<span class="tip-error block" id="ep_tip_incubatorname"></span>
				</li>
				<li>
					<label><i>*</i>联系人姓名：</label>
					<input type="text" name="contacts" id="ep_contactname" class="a-ipt ep_contactname" maxlength="10" required/>
					<span class="tip-error block" id="ep_tip_contactname"></span>
				</li>
				<li>
					<label>联系人职位：</label>
					<input type="text" name="contactsPosition" id="ep_contactjob" class="a-ipt ep_contactjob" maxlength="10" />
					<span class="optional">不超过10个汉字字符</span>
				</li>
				<li>
					<label><i>*</i>联系人电话：</label>
					<input type="text" name="contactsPhone" id="ep_contacttel" maxlength="13" class="a-ipt ep_contacttel" required/>
					<span class="tip-error block" id="ep_tip_contacttel"></span>
				</li>
				<li>
					<label><i>*</i>联系人QQ或微信：</label>
					<input type="text" name="qqOrWeixin" id="ep_contactqqwx" class="a-ipt ep_contactqqwx" required/>
					<span class="tip-error block" id="ep_tip_contactqqwx"></span>
				</li>
				<li>
					<label><i>*</i>简介或运营情况：</label>
					<textarea class="ep_incubatorprof" name="summary" id="ep_incubatorprof" maxlength="500" required></textarea>
					<span class="tip-error block" id="ep_tip_incubatorprof"></span>
				</li>
				<li>
					<label><i>*</i>验证码：</label>
					<input type="text" name="verificationCode" id="ep_identifying" class="a-ipt ep_identifying" required/>
					<a class="a-vercode"   href="javascript:;"><img id="verification_code_application"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4" width="90" height="36" alt="验证码"/></a>
					<span class="tip-error block" id="ep_tip_code"></span>
				</li>
				<li style="text-align:center;"><a id="ep_sub_apply" class="sub-apply" href="javascript:;">提交申请</a></li>
			</ul>
			</form>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/src/user/js/u_epapplication.js"></script>
<script type="text/javascript">
var a = '<%=request.getContextPath()%>' ;
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=4");
ajax.async = false;
$(function(){
	navHighlight("uep","");
})
</script>
</body>
</html>