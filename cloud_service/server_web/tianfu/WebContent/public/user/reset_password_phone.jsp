<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<% 
	TerminalUserVO terminalUserVO = (TerminalUserVO)request.getSession().getAttribute("myterminalUserVO");
	             Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
// 	             System.out.println(terminalUserVO.getId());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重置密码 - 云端在线</title> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 
</head>
<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
        <div class="headerregister r"><a href="<%=request.getContextPath()%>/public/user/register.jsp">注册</a></div>
        <div class="headerlogin r"><a href="<%=request.getContextPath()%>/user.do">登录</a></div>
      </div>
    </div>
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l" ><a href="<%=request.getContextPath()%>">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"  ><a href="#" id="pbtn">产品<img src="<%=request.getContextPath()%>/images/button_navlist.gif" width="12" height="12" /></a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l" ><a href="<%=request.getContextPath()%>/public/downloadPage.jsp">相关下载</a></div>
        <div class="navcontrol r"><a href="<%=request.getContextPath()%>/user.do"><span>我的云端</span></a></div>
        <div id="productmenu"  >
        <a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
       <a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a> 
       <a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
       <a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
       <a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
       <a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
      </div>
      </div> 
    </div>
    <div class="main">
      <div class="container l">
        <div class="titlebar">
          <div class="tab tabactive" id="anchor" id="hook">重置密码</div>
        </div>
        <div class="content">
          <input name="isTrue" id="isTrue" type="hidden" value="false"/>
          <form id="terminal_user_add_dlg_form" method="post"> 
          <%if(terminalUserVO==null){
            	response.sendRedirect(request.getContextPath()+"/public/user/forget_password.jsp"); 
            }else {%>
            <input name="terminalUser_id" id="terminalUser_id" type="hidden" value="<%=terminalUserVO.getId()%>"></input>
           <%}%>
		            <span>新设密码</span>
            		<input class="textfield"  id="password" name="password" type="password" onblur="checkPassword()"/>
           			 <span class="tip" id="tip-password"></span> 
          
           			<span>手机号码</span>
            		<input class="textfield" id="phone" name="phone" type="text" onblur="checkPhone()"/>
            		<span class="tip" id="tip-phone"></span>
            
            		<span>短信验证码</span>
           			<input class="shortfield" id="messageCode" name="messageCode" type="text" onblur="checkMessage()"/>
           			<div class="sendcode"><a onclick="countDown(this,60);" style="cursor:pointer">获取短信验证码</a></div>
           			<span class="tip" id="tip-messageCode"></span>
             
            <input type="button" class="inputbutton" id="terminal_user_add_dlg_save_btn" value="设置新密码"/>
          </form>
          <div class="clear">&nbsp;</div>
        </div>
      </div>
      <div class="sidebar r">
		  <div class="sidebarimagelogin">&nbsp;</div>
		  <div class="sidebarcenter">还没有云端在线账户？<br />
		  <a href="<%=request.getContextPath()%>/register.do">立即注册<img class="pointer" src="<%=request.getContextPath()%>/images/pointer_blue_5_10.gif" width="5" height="9" alt="现在注册" /></a></div>
		</div>
      <div class="clear">&nbsp;</div>
    </div>
  </div>
  <div class="pagefooter">
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
      </div>
    </div>
  </div>
</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
//==================check begin==================
	
function checkPassword(){
	var myPassword = new String($("#password").val()).trim();
	if(/^\w{8,20}$/.test(myPassword)){
		$("#tip-password").html("<img src='<%=request.getContextPath()%>/images/check_alt.png' width='24' height='24' alt='正确'/>");
		return true;
	}else{
		$("#tip-password").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/><font size='1'>密码由字母、数字和下划线组成,长度为8-20字符</font>");
		return false;
	}
}

function checkPhone(){
	var phone = new String($("#phone").val());
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>请输入正确的手机号码");
		return false;
	}
	else{
		$("#tip-phone").html("");
		return true;
	}
}

function checkMessage(){
	var messageCode = new String($("#messageCode").val()).trim();
	if(messageCode==null || messageCode==""){
		$("#tip-messageCode").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>手机验证码不能为空");
		return false;
	}else{
		$("#tip-messageCode").html("");
		return true;
	}
}



function countDown(obj,second){
	if(!(checkPassword()&checkPhone()))
	{
		return;
	}
	var flag = 0;
	$(".sendcode").html(second+"秒后可重新获取");
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==60){
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:checkSmsResetPassword", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					alert(reply.exceptionMessage);
				}
				else if(reply.result.status=="fail"){
					$(".sendcode").html('<a onclick="countDown(this,60);" style="cursor:pointer">获取短信验证码</a>');
					$("#tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>手机号码有误！");  
// 					alert(reply.result.message);
					top.$.messager.alert("提示", reply.result.message, "warning");
					flag = 1;
				}
				else if(reply.result.status == "success") { 
					$("#tip-phone").html("<img src='<%=request.getContextPath()%>/images/check_alt.png' width='24' height='24' alt='正确'/>手机验证码已发送"); 
			        $("#isTrue").val("true");
				}
				else {
					$("#tip-messageCode").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>请输入正确短信验证码！");  
				}
			}
		);
	}
    if(flag == 1){
    	return;
    }
    if(second>0){
        // 时间减一
        second--;
        // 一秒后重复执行
        setTimeout(function(){countDown(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$(".sendcode").html('<a onclick="countDown(this,60);" style="cursor:pointer">获取短信验证码</a>');
    } 
}

//====================check end================
	
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	// 保存
	self.save = function() {
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:resetPasswordByPhone", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					$("#terminal_user_add_dlg_save_btn").attr("value","设置新密码");
					$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
				} else if (reply.result.status == "success") {
					// 注册成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("设置成功，请重新登陆。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						top.location.href = "<%=request.getContextPath()%>/user.do"; 
					}, 1000);
				}
				else { 
					alert(reply.result.message);
					$("#change_verification_code_link").click();
					$("#verification_code").val("");
					$("#terminal_user_add_dlg_save_btn").attr("value","设置新密码");
					$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
				} 
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};
	
	// 初始化
	$(document).ready(function(){
		// 换一个验证码
		$("#change_verification_code_link").click(function(){
			$("#verification_code_img").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
		});
		// 保存
		$("#terminal_user_add_dlg_save_btn").click(function() {
			$("#terminal_user_add_dlg_save_btn").attr("value","正在提交");
			$("#terminal_user_add_dlg_save_btn").attr("disabled","disabled");
			if(checkPassword()&checkPhone()&checkMessage()&$("#isTrue").val()=="true"){
				self.save();
			}else{
				$("#terminal_user_add_dlg_save_btn").attr("value","设置新密码");
				$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
// 				top.$.messager.show({
// 					title:'提示',
// 					msg:'输入信息有误或不完整,无法提交',
// 					timeout:10000,
// 					showType:'slide'
// 				});
			}
		});
		// 关闭
		$("#terminal_user_add_dlg_close_btn").click(function() {
			self.close();
		}); 
	});
};

//=================================================
$(document).ready(function(){
	$('body').mousemove(function(e) {  
		var xx=e.pageX;
		var yy=e.pageY; 
		var movement=-(xx/$(window).width()-0.5);
		$(".scloud1").css("margin-left",(movement*200)+180+"px");
	$(".scloud2").css("margin-left",(movement*120)-700+"px");
	$(".scloud3").css("margin-left",(movement*20)-200+"px");
});
	setInterval("parallax()",20);
	var b3;
	var b4;
	var s3;
	var s4;
	$("#pbtn").mouseenter(function(){
		b3=setTimeout("$('#productmenu').fadeIn(300)",300);
		s3 && clearTimeout(s3);
		s4 && clearTimeout(s4);
	});
	$("#pbtn").mouseleave(function(){
		s3=setTimeout("$('#productmenu').fadeOut(300)",300);
		b3 && clearTimeout(b3);
		b4 && clearTimeout(b4);
	});
	$("#productmenu").mouseenter(function(){
		b4=setTimeout("$('#productmenu').fadeIn(300)",300);
		s3 && clearTimeout(s3);
		s4 && clearTimeout(s4);
	});
	$("#productmenu").mouseleave(function(){
		s4=setTimeout("$('#productmenu').fadeOut(300)",300);
		b3 && clearTimeout(b3);
		b4 && clearTimeout(b4);
	});
});
function parallax(){
	$(".banner").html(movement);
	var movement=$(document).scrollTop()/($(document).height()-$(window).height())-0.5;
	$(".scloud1").css("margin-top",(movement*200)+"px");
	$(".scloud2").css("margin-top",(movement*120)+"px");
	$(".scloud3").css("margin-top",(movement*20)+"px");
}

</script>
</html>
