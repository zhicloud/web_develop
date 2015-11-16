
// 点击登录
$(function(){
	$("#login_submit").click(function(){ 
		var logStatus = checkEmail() & checkPassword() & checkVcode();
		if(logStatus){
			login();
		}
	});
	
	$("#reg_submit").click(function() {
		var regStatus = checkRegEmail() & checkRegPassword() & checkRegPasswordConf() & checkRegPhone() & checkRegVcode() & checkMessage() & checkRule();
		if(regStatus){
			register(); 
		}
	});
})

//登录时邮箱检测
function checkEmail() {
	var account = $.trim($('#log_ipteml').val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	var preg = /^1[3|4|5|8][0-9]\d{8,8}$/;
	if(account==null || account==""){
		$("#tip_email").html("登录账户不能为空");
		return false;
	}
	if((!myreg.test(account)) && (!preg.test(account))){
		$("#tip_email").html("登陆账号格式不正确");
		return false;
	}
	if(account.length>30){
		$("#tip_email").html("输入不能超过30个字符");
		return false;
	}else{
		$("#tip_email").html("");
		return true;
	}
}
//登录时密码检测
function checkPassword(){
	var myPassword = $.trim($('#log_iptpwd').val());
	
	if(myPassword==null || myPassword==""){
		$("#tip_password").html("密码不能为空");
		return false;
	}else if(myPassword.length<6||myPassword.length>20){ 
		$("#tip_password").html("密码长度为6-20个字符");
		return false;
	}else{
		$("#tip_password").html("");
		return true;
	}
}
//登录时验证码检测
function checkVcode(){
	var verificationCode = $.trim($('#log_iptcode').val());
	if(verificationCode==null || verificationCode==""){
		$("#tip_code").html("验证码不能为空");
		return false;
	}else{
		$("#tip_code").html("");
		return true;
	}
}
//登录
function login()
{ 
	var formData = $.formToBean(logForm);
	ajax.remoteCall("bean://sysUserService:login",
		[formData],
		function(reply)
		{ 
			if( reply.status=="exception" )
			{
				top.$.messager.alert("提示", reply.exceptionMessage, "warning");
			} 
			else if( reply.result.status=="success" )
			{ 
				// 跳转页面 
				var userId = reply.result.properties.userId;
				$("#beforelogin").css("display","none");
				$("#afterlogin").css("display","block"); 
				slideright();
				getLoginInfo($("#inputemail").val(),0,userId);
			}
			else
			{ 
//				top.$.messager.alert("提示",reply.result.message,"warning");
				$("#tip_code").html(""+reply.result.message+"");
				$("#log_iptpwd").val("");
				$("#verification_code_img").click();
				$("#log_iptcode").val("");
			}
		}
	);
}


//注册时邮箱检测
function checkRegEmail() {
	var account = $.trim($('#reg_ipteml').val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
 
	if(account==null || account==""){
		$("#tip_regemail").html("邮箱不能为空");
		return false;
	}
	if(!myreg.test(account)){
		$("#tip_regemail").html("邮箱格式不正确");
		return false;
	}
	if(account.length>30){
		$("#tip_regemail").html("输入不能超过30个字符");
		return false;
	}
	else{
		
		var flag = false;
		var formData = $.formToBean(regForm);
		ajax.remoteCall("bean://terminalUserService:checkAccount", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") 
				{
					alert(reply.exceptionMessage);
				}else if(reply.result.status=="fail")
				{
					$("#tip_regemail").html("邮箱已经注册，请更换邮箱");
				}else
				{
					$("#tip_regemail").html("");
					flag = true;
				}
			}
		);
		return flag;
	} 
	$("#tip_regemail").html("");
	return true; 
}
//注册时密码检测
function checkRegPassword(){
	var myPassword = $.trim($('#reg_iptpwd').val());
	if(myPassword==null || myPassword==""){
		$("#tip_regipassword").html("密码不能为空");
		return false;
	}
	if(myPassword.length>=6&&myPassword.length<=20){
		$("#tip_regipassword").html("");
		return true;
	}else{
		$("#tip_regipassword").html("密码长度为6-20个字符");
		return false;
	}
}
//注册时确认密码检测
function checkRegPasswordConf(){
	var passwordOne = $.trim($('#reg_iptpwd').val());
	var passwordTwo = $.trim($('#reg_cfmpwd').val());
	if(passwordTwo.length<6||passwordTwo.length>20){ 
		$("#tip_regcpassword").html("请输入6-20个字符");
		return false;
	} 
	if(passwordTwo==null || passwordTwo==""){
		$("#tip_regcpassword").html("确认密码不能为空");
		return false;
	}
	if(passwordOne!=passwordTwo){
		$("#tip_regcpassword").html("两次输入的密码不一致");
		return false;
	}else{
		$("#tip_regcpassword").html("");
		return true;
	}
}
//注册时手机号码检测
function checkRegPhone(){
	var phone = $.trim($('#reg_iptnum').val());
	if(phone==null || phone==""){
		$("#tip_regphone").html("手机号码不能为空");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip_regphone").html("请输入正确的手机号码");
		return false;
	}else{
		$("#tip_regphone").html("");
		return true;
	}
}
//注册时验证码检测
function checkRegVcode(){
	var verificationCode = $.trim($('#reg_iptcode').val());
	if(verificationCode==null || verificationCode==""){
		$("#tip_regcode").html("验证码不能为空");
		return false;
	}if(verificationCode.length>2){
		$("#tip_regcode").html("验证码不正确");
		return false;
	}
	else{
		$("#tip_regcode").html("");
		return true;
	}
}
//注册时短信验证检测
function checkMessage(){
	var messageCode = $.trim($('#reg_iptmsg').val());
	if(messageCode==null||messageCode==""){
		$("#tip_regmsg").html("短息验证码不能为空");
		return false;
	}if(messageCode.length>10){
		$("#tip_regmsg").html("短息验证码长度不能超过10位");
		return false;
	}else{
		$("#tip_regmsg").html("");
		return true;
	}
}
//注册时服务条款检测
function checkRule(){
	if($("#rule").is(':checked')){		
		$("#tip_regrule").html("");
		return true;
	}else{
		$("#tip_regrule").html("您未同意相关服务条款");
		return false;
	} 
}
//获取手机验证码
function countDown(obj,second){
	if(!(checkRegEmail() & checkRegPassword() & checkRegPasswordConf() & checkRegPhone() & checkRegVcode()))
	{
		return;
	}
	var flag = 0; 
	$("#reg_iptnum").attr("disabled",true);
	$('#sendcode').addClass("dis");
 	$("#sendcode").html(second+"秒后可重新获取"); 
 	$("#sendcode").unbind();	
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==60){
		var formData = $.formToBean(regForm);
		ajax.remoteCall("bean://terminalUserService:checkSmsRegister", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					alert(reply.exceptionMessage);
					$("#sendcode").html("获取短信验证码"); 
					$("#sendcode").click(function(){
						countDown(this,60);
					});
				}
				else if(reply.result.status=="fail"){   
					$("#reg_iptnum").attr("disabled",false); 
					top.$.messager.alert("提示", reply.result.message, "warning",function(){
						$("#sendcode").html("获取短信验证码"); 
						$("#sendcode").click(function(){
							countDown(this,60);
						});
						$('#sendcode').removeClass("dis");
						$("#reg_iptcode").val("");
						$("#verification_code_register_img").click();
					});
					flag = 1;
				}
				else if(reply.result.status == "success") { 
					$("#tip_regmsg").html(""); 
			        $("#isTrue").val("true");
				}
				else {
					$("#tip_regmsg").html("请输入正确短信验证码！");  
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
        action = setTimeout(function(){countDown(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$("#sendcode").html("获取短信验证码"); 
    	$('#sendcode').removeClass("dis");
		$("#reg_iptnum").attr("disabled",false);
		$("#sendcode").click(function(){
			countDown(this,60);
		});
    } 
}


