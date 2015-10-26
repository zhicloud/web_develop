<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<div class="g-log">
	<div class="u-login">
		<div class="log-cont">
			<div class="welcome">&nbsp;</div>
			<form id="logForm" class="log-form" action="" method="post">
			<input type="hidden" id="type" name="type" value="4"/>
				<ul>
					<li>
						<input type="text" name="account" id="log_ipteml" class="ipteml" autocomplete="off" placeholder="请输入邮箱" onblur="checkEmail()"/>
						<span class="f-db font12 c-f60" id="tip_email"></span> 
					</li>
					<li>
						<input type="password" name="password" id="log_iptpwd" onpaste='return false' class="iptpwd" autocomplete="off" placeholder="请输入密码" onblur="checkPassword()"/>
						<span class="f-db font12 c-f60" id="tip_password"></span>
					</li>
					<li>
						<input type="text" name="verification_code" id="log_iptcode" class="iptcode" autocomplete="off" placeholder="验证码" onblur="checkVcode()" />
						<a class="vcrefresh vcf1" href="javascript:;"><img id="verification_code"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4" width="100" height="40" alt="验证码"/></a>
						<a class="vcrefresh vcf2" href="javascript:;" id="verification_code_img" name='verification_code_img'>看不清<br>换一张</a>
						<span class="f-db font12 c-f60" id="tip_code"></span>
					</li>
					<li class="f-btn">
 						<a id="login_submit" class="btn-sty btn-login" href="javascript:;" >登录</a>  
						<a class="btn-sty btn-back" href="javascript:;">返回</a>
					</li>
					<li class="f-extra">
						<a class="reglink" href="javascript:;">注册账户</a>
						<span style="margin-right: 0;">&nbsp;|&nbsp;</span>
						<a href="<%=request.getContextPath() %>/forgetpassword.do" target="_blank">忘记密码？</a>
					</li>
<%-- 					<li style="margin-top:20px;color:red;margin-right: 30px;font-size:16px;">重要通知： <a class="impor-info" href="<%=request.getContextPath()%>/src/user/u_impotnotice.jsp" target="_blank">《关于广州机房搬迁通知》</a></li>
 --%>				</ul>
			</form>
		</div>
	</div>
	<div class="u-register">
		<div class="reg-cont">
			<div class="reg-t">账号注册</div>
			<input name="isTrue" id="isTrue" type="hidden" value="false"/>
			<form id="regForm" class="reg-form" action="" method="post">
				<ul>
					<li>
						<input type="text" name="account" id="reg_ipteml" class="ipteml" autocomplete="off" placeholder="输入邮箱" onblur="checkRegEmail()" />
						<span class="f-db font12 c-f60" id="tip_regemail"></span> 
					</li>
					<li>
						<input type="password" name="password" id="reg_iptpwd" class="iptpwd" onpaste='return false' autocomplete="off" placeholder="输入密码" onblur="checkRegPassword()" />
						<span class="f-db font12 c-f60" id="tip_regipassword"></span> 
					</li>
					<li>
						<input type="password" name="confpassword" id="reg_cfmpwd" class="cfmpwd" onpaste='return false' autocomplete="off" placeholder="确认密码" onblur="checkRegPasswordConf()" />
						<span class="f-db font12 c-f60" id="tip_regcpassword"></span> 
					</li>
					<li style="overflow:hidden;">
						<input type="text" name="phone" id="reg_iptnum" class="iptnum" autocomplete="off" placeholder="手机号码" onblur="checkRegPhone()" />
						<span class="f-db font12 c-f60" id="tip_regphone">绑定手机接收系统重要通知</span>
					</li>
					<li>
						<input type="text" name="verificationCode" id="reg_iptcode" class="iptcode" autocomplete="off" placeholder="验证码" onblur="checkRegVcode()"/>
						<a class="vcrefresh vcf1" href="javascript:;"><img id="verification_code_register"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4" width="100" height="40" alt="验证码"/></a>
						<a class="vcrefresh vcf2" href="javascript:;" id="verification_code_register_img" name='verification_code_img'>看不清<br>换一张</a>
						<span class="f-db font12 c-f60" id="tip_regcode"></span>
					</li>
					<li>
 						<input type="text" name="messageCode" id="reg_iptmsg" class="iptmsg" autocomplete="off" placeholder="短信验证码" onblur="checkMessage()" />
						<a id="sendcode"class="sendcode" href="javascript:;">获取短信验证码</a>
						<span class="f-db font12 c-f60" id="tip_regmsg"></span> 
					</li>
					<li style="vertical-align:middle;text-align:left;margin-left:30px;">
						<input type="checkbox" name="rule" id="rule" value="1" checked="checked" style="width:14px;height:14px;"/>
						<label class="font13 f-ib">我已阅读并同意相关</label>
						<a class="font13 f-ib credit-terms" href="<%=request.getContextPath() %>/public/eula.jsp" target="_blank">服务条款</a>
						<span class="f-db font12 c-f60" id="tip_regrule"></span> 
					</li>
					<li class="f-btn">
						<a id="reg_submit" class="btn-sty btn-reg" href="javascript:;">注册</a>
						<a class="btn-sty btn-back" href="javascript:;">返回</a>
					</li>
				</ul>
			</form>
		</div>
	</div>
</div>
<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/check.js"></script>
<script type="text/javascript">
var a = '<%=request.getContextPath()%>' ;
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=4");
ajax.async = false;
$(function(){
	// 换一个验证码 
	$("a[name=verification_code_img],#verification_code,#verification_code_register").click(function(){
		$("#verification_code").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4&ts="+Math.random());
	});
	// 换一个验证码 
	$("#verification_code_register_img,#verification_code_register").click(function(){
		$("#verification_code_register").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=4&ts="+Math.random());
	});
	// 回车
	$("#log_ipteml, #log_iptpwd,#log_iptcode").keypress(function(evt){
		if( evt.keyCode==13 ){
			$("#login_submit").click();
		}
	}); 
	
	//倒计时
	$("#sendcode").click(function(){
		countDown(this,60);
	});
}); 
function reshVerification()
{  
	$("#verification_code_img").click(); 	 
}


var t2 = window.setInterval(reshVerification,1740000);

//注册
function register(){
	var formData = $.formToBean(regForm);
	ajax.remoteCall("bean://terminalUserService:addTerminalUserWithoutLogin", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception") {
				$("#reg_submit").attr("value","注  册");
				$("#reg_submit").removeAttr("disabled");
				top.$.messager.alert('警告','请重新注册','warning');
				window.location.reload();
			} else if (reply.result.status == "success") {
				// 注册成功
				clearTimeout(action);
				$("<div class=\"datagrid-mask\"></div>").css({
					display:"block",
					width:"100%",
					height:"100%"
				}).appendTo("body"); 
				$("<div class=\"datagrid-mask-msg\"></div>").html("注册成功，正在跳转页面。。。").appendTo("body").css({
					display:"block",
					left:($(document.body).outerWidth(true) - 190) / 2,
					top:($(window).height() - 45) / 2
				});
				window.setTimeout(function(){  
				    parent.window.location.href="<%=request.getContextPath()%>/user.do";
				}, 1000);
				// 跳转页面
			}
			else {  
				$("tip_regmsg").html(""+reply.result.message+""); 
				$("#verification_code_register_img").click();
				$("#reg_iptcode").val("");
				$("#reg_submit").removeAttr("disabled");
				$("#reg_submit").attr("value","注  册");
			} 
		}
	);
}
</script> 
<!-- /JavaScript -->
