<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String hostId = (String)request.getAttribute("host_id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- add_domain.jsp -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var a = '<%= request.getContextPath() %>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;



	$(document).ready(function() {
		init(10, 2);
		if (name != '') {
			inituser(name, 0);
		} else {

			inituser();
		}
		initstep(1);
	});

	function getLoginInfo(name, message, userId) {
		slideright();
		inituser(name, message);
		window.location.reload();
	};
	
	function add_domain_btn() {
		if(!checkParam()) {
			return;
		}
		var formData = $.formToBean(add_domain_form);
		ajax.remoteCall("bean://cloudHostService:addDomain", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					window.history.back();
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning', function(){
						window.history.back();
					});
				}
			}
		);
	};

function checkParam() {
	if(checkDomain() && checkName() && checkAdminName() && checkEmail() && checkPhone()){
		return true;
	} else {
		return false;
	}
}
	
function checkDomain() {
	
	var domain = $("#domain").val();
 	var domainreg = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
 
	if(domain==null || domain==""){
		$("#tip-domain").html("域名不能为空");
		return false;
	}
 	if(!domainreg.test(domain)){
		$("#tip-domain").html("域名格式不正确");
		return false;
	}
	ajax.remoteCall("bean://cloudHostService:checkDomainAvaliable",
			[domain], 
			function(reply) {
				if(reply.result.status == "success") {
					$("#tip-domain").html("该域名已被绑定");
					return false;
				}
			}
		);
	$("#tip-domain").html("");
	return true;
};

function checkName() {
	var name = $("#name").val();
	if(name==null || name==""){
		$("#tip-name").html("站点名不能为空");
		return false;
	}
	
	if(name.length<2||name.length>20){ 
		$("#tip-name").html("请输入2-20个字符");
		return false;
	} 
	
	$("#tip-name").html("");
	return true;
};

function checkAdminName() {
	var admin_name = $("#admin_name").val();
	if(admin_name==null || admin_name==""){
		$("#tip-admin_name").html("管理员姓名不能为空");
		return false;
	}
	
	if(admin_name.length<2||admin_name.length>20){ 
		$("#tip-admin_name").html("请输入2-20个字符");
		return false;
	} 
	
	$("#tip-admin_name").html("");
	return true;
};

function checkEmail() {
	var email = $("#email").val();
	var emailreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(email==null || email==""){
		$("#tip-email").html("邮箱不能为空");
		return false;
	}
	if(!emailreg.test(email)){
		$("#tip-email").html("邮箱格式不正确");
		return false;
	}
	
	$("#tip-email").html("");
	return true;
};

function checkPhone() {
	var phone = $("#phone").val();
	if(phone==null || phone==""){
		$("#tip-phone").html("电话号码不能为空");
		return false;
	}
	
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip-phone").html("请输入正确的手机号码");
		return false;
	}
	
	$("#tip-phone").html("");
	return true;
};
	
</script>
<style type="text/css">
#porttable td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
}
#porttable .headtr td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
	background:#ddedef;
}
#porttable .nochange .datagrid-cell {
	color: #a2a2a2;
}
</style>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>
	<body>
	<div class="page">
  		<div class="pageleft">
    		<div class="header"> 
			   <div class="top"> 
			    <a class="logo l" href="<%=request.getContextPath()%>/"><img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a> 
			    <div id="beforelogin" class="user r"> 
			     <a id="loginlink" href="javascript:void(0);" class="graylink">登录</a>
			     <span>|</span> 
			     <a id="reglink" href="javascript:void(0);">注册</a> 
			    </div> 
			    <div id="afterlogin" class="user r" style="display:none;">
			     <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
			     <a id="logoutlink" href="javascript:void(0);">注销</a>
			     <span>|</span>
			     <a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
			    </div>
			    <div class="nav r">
			     <a href="<%=request.getContextPath()%>/" style="background:transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" /> </a>
			     <a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
			     <a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			     <a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
			     <a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
			     <a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
			     <a href="#" style="display:none"></a>
			     <a href="<%=request.getContextPath()%>/user.do?flag=login" style="display:none"></a>
			     <a href="#" style="display:none"></a>
			     <a href="#" style="display:none">我的云端</a>
			    </div>
			   </div>
			   <div class="subnav">
			    <div class="box">
			     1
			    </div>
			    <div class="box">
			     2
			    </div>
			    <div class="box">
			     3
			    </div>
			    <div class="box">
			     4
			    </div>
			    <div class="box">
			     5
			    </div>
			    <div class="box">
			     6
			    </div>
			    <div class="box">
			     7
			    </div>
			    <div class="box">
			     8
			    </div>
			    <div class="box">
			     9
			    </div>
			    <div class="box">
			     <a id="overview" onclick="onSwitch(this);" href="#"><img id="nav_10_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a>
			     <a id="my_cloud_host_link" onclick="onSwitch(this);" href="#"><img id="nav_10_2" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a>
			     <a href="#" id="my_cloud_disk_link" onclick="onSwitch(this);"><img id="nav_10_3" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a>
			     <a href="#" id="recharge_record" onclick="onSwitch(this);"><img id="nav_10_4" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a>
			     <a href="#" id="oper_log" onclick="onSwitch(this);"><img id="nav_10_5" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a>
			     <a href="#" id="suggestion" onclick="onSwitch(this);"><img id="nav_10_6" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a>
			     <a href="#" id="my_uploaded_file_link" onclick="onSwitch(this);"><img id="nav_10_7" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
			    </div>
			   </div> 
			</div>
    <div class="main">
    <!--  <div class="titlebar" style="width:720px;padding:15px 0 5px 0;"> -->
              
      <form id="add_domain_form" method="post">
      
      <input type="hidden" name="host_id" value="<%=hostId%>"/>
		 
        <div style="width:720px; margin:0 auto 0 auto;">
       		<table border="0" cellpadding="5" cellspacing="0" style="padding-left:212px;">
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />域名：</td>
				<td class="inputcont"> <input class="textbox" type="text" name="domain" id="domain" style="width:200px;height:28px" onblur="checkDomain()"></input></td>
				<td>					
	        			<span class="err" id="tip-domain" style="font-size: 12px;"></span> 
				</td>
				
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />站点名：</td>
				<td> <input  class="textbox" type="text" name="name" id="name" style="width:200px;height:28px" onblur="checkName()"></input></td>
				<td>
	        			<span class="err" id="tip-name" style="font-size: 12px;"></span> 
     			</td>
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />管理员姓名：</td>
				<td> <input  class="textbox" type="text" name="admin_name" id="admin_name" style="width:200px;height:28px" onblur="checkAdminName()"></input></td>
				<td>
	        			<span class="err" id="tip-admin_name" style="font-size: 12px;"></span> 
     			</td>
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱：</td>
				<td> <input  class="textbox" type="text" name="email" id="email" style="width:200px;height:28px" onblur="checkEmail()"></input></td>
				<td>
	        			<span class="err" id="tip-email" style="font-size: 12px;"></span> 
     			</td>
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />电话号码：</td>
				<td> <input  class="textbox" type="text" name="phone" id="phone" style="width:200px;height:28px" onblur="checkPhone()"></input></td>
				<td>
	        			<span class="err" id="tip-phone" style="font-size: 12px;"></span> 
     			</td>
			</tr>
           </table>
           <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a id="add_domain_btn" class="bluebutton r" href="javascript:void(0);" onclick="add_domain_btn()">提交</a><a class="graybutton r" href="javascript:void(0);" onclick="window.history.back()">返回</a></div>
           </div>
      </form>
      
    </div>
    <div class="clear"></div>
     <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a><br /> 
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.tianfusoftwarepark.com" target="_blank">成都天府软件园有限公司</a>, All rights reserved.
				蜀ICP备11001370号-3
			</div>
		</div> 
	</div>
  </div>
  <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
<!-- </div>
 --></body>
</html>
