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
	<div class="g-mha-bd">
		<div class="wrap">
			<h3>申请云镜像</h3>
			<form id="applicationForm"  action="" method="post">
			<ul>
				<li>
					<label><i>*</i>公司全称：</label>
					<input type="text" name="name" id="mh_incubatorname" class="a-ipt mh_incubatorname" maxlength="25" required/>
					<span class="tip-error block" id="mh_tip_incubatorname"></span>
				</li>
				<li>
					<label><i>*</i>联系人姓名：</label>
					<input type="text" name="contacts" id="mh_contactname" class="a-ipt mh_contactname" maxlength="10" required/>
					<span class="tip-error block" id="mh_tip_contactname"></span>
				</li>
				<li>
					<label>联系人职位：</label>
					<input type="text" name="contactsPosition" id="mh_contactjob" class="a-ipt mh_contactjob" maxlength="10" />
<!-- =======
					<input type="text" name="name" id="mh_incubatorname" class="a-ipt mh_incubatorname mh-plan" maxlength="25" required/>
					<span class="tip-error">不超过25个汉字字符</span>
				</li>
				<li>
					<label><i>*</i>联系人姓名：</label>
					<input type="text" name="contacts" id="mh_contactname" class="a-ipt mh_contactname mh-plan" maxlength="10" required/>
					<span class="tip-error">不超过10个汉字字符</span>
				</li>
				<li>
					<label>联系人职位：</label>
					<input type="text" name="contactsPosition" id="mh_contactjob" class="a-ipt mh_contactjob mh-plan" maxlength="10" />
>>>>>>> eac150ced38a6bce00f4800d164f0840b80e671b -->
					<span class="optional">不超过10个汉字字符</span>
				</li>
				<li>
					<label><i>*</i>联系人电话：</label>
					<input type="text" name="contactsPhone" id="mh_contacttel" class="a-ipt mh_contacttel" maxlength="16" required/>
					<span class="tip-error block" id="mh_tip_contacttel"></span>
				</li>
				<li>
					<label><i>*</i>联系人QQ或微信：</label>
					<input type="text" name="qqOrWeixin" id="mh_contactqqwx" class="a-ipt mh_contactqqwx" required/>
					<span class="tip-error block" id="mh_tip_contactqqwx"></span>
				</li>
				<li>
					<label><i>*</i>公司简介：</label>
					<textarea class="mh_incubatorprof" name="summary" id="mh_incubatorprof" maxlength="500" required></textarea>
					<span class="tip-error block" id="mh_tip_incubatorprof"></span>
<!-- =======
					<input type="text" name="contactsPhone" id="mh_contacttel" class="a-ipt mh_contacttel mh-plan number" maxlength="13" required/>
					<span class="tip-error">必须是数字或“-”</span>
				</li>
				<li>
					<label><i>*</i>联系人QQ或微信：</label>
					<input type="text" name="qqOrWeixin" id="mh_contactqqwx" class="a-ipt mh_contactqqwx mh-plan" required/>
					<span class="tip-error">例：QQ：123456789</span>
				</li>
				<li>
					<label><i>*</i>公司简介：</label>
					<textarea class="mh_incubatorprof mh-plan"  name="summary" id="mh_incubatorprof" maxlength="500" required></textarea>
					<span class="tip-error">不超过500个汉字字符</span>
>>>>>>> eac150ced38a6bce00f4800d164f0840b80e671b -->
				</li>
				<li>
					<label><i>*</i>验证码：</label>
					<input type="text" name="verificationCode" id="mh_identifying" class="a-ipt mh_identifying" required/>
					<a class="a-vercode" href="javascript:;"><img id="verification_code_application"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4" width="100" height="40" alt="验证码"/></a>
					<span class="tip-error block" id="mh_tip_code"></span>
				</li>
				<li style="text-align:center;"><a id="mh_sub_apply" class="sub-apply" href="javascript:;">提交申请</a></li>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/src/user/js/u_mhapplication.js"></script>
<script type="text/javascript">
<%-- =======
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/user/js/u_mhapplication.js"></script>


 <script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/check.js"></script>
 <script type="text/javascript">
>>>>>>> eac150ced38a6bce00f4800d164f0840b80e671b --%>
	$(function(){
		
		navHighlight("upro","ucshp");
		// 换一个验证码 
		$("#verification_code_application").click(function(){
			$("#verification_code_application").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4&ts="+Math.random());
		});
	})
	
	function application(){ 
	var formData = $.formToBean(applicationForm);
	ajax.remoteCall("bean://imageHostApplicationService:addImageHostApplication", 
		[ formData ],
		function(reply) {
			$("#verification_code_application").click();
			if (reply.status == "exception") { 
				top.$.messager.alert("警告", "申请失败", "warning", function(){
					window.location.reload();
				});
				} else if (reply.result.status == "success") {
					top.$.messager.alert("提示", "申请成功", "warning", function(){
					window.location.reload();
				});
				}else {  
					top.$.messager.alert("警告", reply.result.message, "warning", function(){
					window.location.reload();
				});
				} 
		}
	);
}
</script>
</body>
</html>