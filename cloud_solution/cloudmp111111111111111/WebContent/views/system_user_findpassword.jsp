<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- login.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>找回密码页面</title>
<%@include file="/views/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery.cookie.js"></script>
<script src="<%=request.getContextPath() %>/js/artDialog/artDialog.js?skin=chrome"></script>
<script src="<%=request.getContextPath() %>/js/artDialog/plugins/iframeTools.source.js"></script>

<style type="text/css">
html {padding-top: 0;}
body #content.full-page .inside-block {
	position:relative;
	display: block;
	margin: 0 auto;
	text-align: center;
	width: 100%;
	height: 100%;
	-webkit-box-sizing: content-box;
	-moz-box-sizing: content-box;
	box-sizing: content-box;
	padding: 0;
	color: white;
	overflow: hidden;
	background-color: transparent;
	background-repeat:no-repeat;
	background-position:center center;
	<!--background-image:url("<%=request.getContextPath() %>/assets/images/loginBg.png");-->
}
body #content .inside-block .l-box{
	position:absolute;
	top:50%;
	left:50%;
	min-width: 560px;
	min-height: 663px;
	margin-top:-331.5px;
	margin-left:-333px;
	padding:47px 70px 93px;
}
body #content .inside-block .l-box .m-logo{
	margin-top:40px;height: 45px;
}
body #content .inside-block .l-box .m-title{
	margin-top:15px;
	margin-bottom:30px;
}
body #content.full-page .inside-block .logindiv .input-group {width:333px;margin:0 auto;padding:6px 0;}
body #content.full-page .inside-block .logindiv .input-group .input-group-addon {background-color: rgba(0, 0, 0, 0.2);-webkit-border-radius: 0 4px 4px 0 !important;-moz-border-radius: 0 4px 4px 0 !important;-ms-border-radius: 0 4px 4px 0 !important;-o-border-radius: 0 4px 4px 0 !important;border-radius: 0 4px 4px 0 !important;-webkit-transition: all 0.1s linear;-moz-transition: all 0.1s linear;transition: all 0.1s linear;border: 0;}
body #content.full-page .inside-block .logindiv .input-group .input-group-addon i {width: 18px;color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .input-group .input-group-addon i:after {content: "";height: 50%;margin-left: -25px;position: absolute;top: 25%;}
body #content.full-page .inside-block .logindiv .input-group input {

	background-color: rgba(0, 0, 0, 0.2);

    border: 0 !important;
	-webkit-border-radius: 4px 0 0 4px !important;
	-moz-border-radius: 4px 0 0 4px !important;
	-ms-border-radius: 4px 0 0 4px !important;
	-o-border-radius: 4px 0 0 4px !important;
	border-radius: 4px 0 0 4px !important;
	color: white !important;
	-webkit-transition: all 0.1s linear;
	-moz-transition: all 0.1s linear;
	transition: all 0.1s linear;
	height:45px;
	line-height:45px;
}
body #content.full-page .inside-block .logindiv .dtCss input:focus, #content.full-page .inside-block form .input-group input:focus + .input-group-addon {background-color: rgba(0, 0, 0, 0.3) !important;-webkit-box-shadow: none !important;box-shadow: none !important;}
body #content.full-page .inside-block .logindiv .dtCss input::-webkit-input-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .dtCss input:-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .dtCss input::-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .dtCss input:-ms-input-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv section.controls {
	overflow:hidden;
	width:333px;
	margin:10px auto 0;
}
body #content.full-page .inside-block .logindiv section.controls .checkbox {
	padding: 0;
	display: block;
	margin: 0 !important; 
	float:left;
}
body #content.full-page .inside-block .logindiv section.controls a {
	font-size: 12px;
	display: block;
	float:right;
	color: rgba(255, 255, 255, 0.6);
}
body #content.full-page .inside-block .logindiv section.controls a.psw-forget:hover{
	color:#fff;
	color: rgba(255, 255, 255, 1.0);
}
body #content.full-page .inside-block .logindiv section.controls a:hover {
	color: white;
	text-decoration: none;
}
body #content.full-page .inside-block .logindiv section.controls a:focus {
	text-decoration: none;
}
body #content.full-page .inside-block .logindiv section.log-in{padding-top:6px;}
body #content.full-page .inside-block .logindiv section.log-in .btn {
	display: inline-block;
	height:45px;
	line-height:45px;
	width:151px;
	color:#fcf7f5;
	padding:0;
	border:none;
	font-size:16px;
}
.btn-primary{background:#5cbcb2;}
.btn-primary:hover,.btn-primary:focus,.btn-primary:active{background-color:#50b3a9;}
.btn.btn-slategray{background:#b977ff;}
.btn.btn-slategray:hover,.btn.btn-slategray:focus,.btn.btn-slategray:active{background-color:#af6bf7;}
body #content.full-page .inside-block .logindiv section.log-in span {
	display: inline-block;
	vertical-align: bottom;
	line-height: 36px;
	margin: 0 10px;
	font-style: italic;
	font-family: Georgia, serif; 
}
body #content div.checkbox.check-transparent label{
	font-size:12px;
	line-height:20px;
	padding-left: 22px;
	color: rgba(255, 255, 255, 0.6);
}

body #content .novice-guide a.c-dsk-client{float:left;background:url("<%=request.getContextPath() %>/assets/images/c_dsk_client.png") no-repeat center bottom;}
body #content .novice-guide a.use-guide{float:right;background:url("<%=request.getContextPath() %>/assets/images/use_guide.png") no-repeat center bottom;}
.tips-info{display:none;font-size:12px;height: 33px;margin:0 auto;margin-top: -26px;margin-left: 171px;color: red;}
.email-info{display:none;font-size:12px;height: 33px;margin:0 auto;margin-top: -26px;margin-left: 171px;color: red;}
.code-info{display:none;font-size:12px;height: 33px;margin:0 auto;margin-top: -22px;margin-left: 171px;color: red;}
.alert-mask{
	display:none;
	position:absolute;
	top:0;
	left:0;
	width:100%;
	height:100%;
	z-index:9999;
	background:rgba(0, 0, 0, 0.05);
}
.alert {
	display: none;
	position:absolute;
	top:50%;
	left:50%;
	margin-top:-28px;
	margin-left:-99px;
	margin-bottom:0;
	z-index:99999;
	width:177px;
	height:39px;
	line-height:33px;
	padding: 0;
	font-size: 14px; 
	border:none;
}
.alert.alert-red {
	text-align:center;
	color: #fff;
	background: url("<%=request.getContextPath() %>/assets/images/alertBg.png") no-repeat;
}
.alert .icon-exclamation{
	display:inline-block;
	width:15px;
	height:15px;
	vertical-align:middle;
	margin:0 5px 3px 0;
	background: url("<%=request.getContextPath() %>/assets/images/icon_exclamation.png") no-repeat;
}
.but{
	display: inline-block; height: 37px; line-height: 38px; width: 83px; color: #fcf7f5; padding: 0; border: none; font-size: 16px;
}
.butTop{padding-top: 56px;}
.span{color: #b977ff;margin-left: 10px;font-size: 15px;}
.dtCss{
	font-size: 15px;height:60px;color: black;
}
.inputUserName{
	margin-left: -4px;
}
.inputEmail{
	margin-left: 97px;
}
.inputCode{
	margin-left: 104px;
}
.divWidth{height: 64px;}
.inputheight{margin-left: 36px;color: #fff;}
.label1{margin-right: 107px;}
</style>
<script type="text/javascript">
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
var path = '<%=request.getRequestURI()%>';
var sendCode;
var uid;
//登录
function toSetWpd(){
	var username = $("#username").val();
	var email = $("#email").val();
	if(username == "" || username == null || username == undefined){
		$("#utipsInfo").html("*用户名不能为空");
		$("#utipsInfo").show();
		return false;
	}
	
	if(email == "" || email == null || email == undefined){
		$("#emailInfo").html("*绑定邮箱不能为空");
		$("#emailInfo").show();
		return false;
	}
	//条件都满足提交后台重置密码
	var code = $("#code").val();
	if(code==undefined||code==""||code==null){
		$("#codeInfo").html("*验证码不能为空");
		$("#codeInfo").show();
		return false;
	}else{
		if(code == sendCode){
			//跳转到重置密码页面
	        art.dialog.open("<%=request.getContextPath()%>/transform/toSetPasswordPage?uid="+uid, {
	        	id:"setpwdpage",
                title:'重置密码',
                width: 300,
                height: 300,
                lock: true
            });
		}
	}
}

function goToLogin(){
	window.location.href = "<%=request.getContextPath()%>";
}
//验证用户输入的信息是否存在
function cheackCode(){
	var flag = false;
	var username = $("#username").val();
	var email = $("#email").val();
	if(username == "" || username == null || username == undefined){
		$("#utipsInfo").html("*用户名不能为空");
		$("#utipsInfo").show();
		return flag;
	}
	
	if(email == "" || email == null || email == undefined){
		$("#emailInfo").html("*绑定邮箱不能为空");
		$("#emailInfo").show();
		return flag;
	}
	
	jQuery.ajax({
		type: "POST",
		async:true,
		url: "<%=request.getContextPath() %>/transform/cheackuNameAndEmail",
		data: "username="+username+"&email="+email,
		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		success: function(result){
			var obj = eval("("+result+")");
			//验证用户输入用户信息的是否存在
			if(result == 'null'){
				$("#utipsInfo").html("您输入的用户名或绑定邮箱错误！");
				$("#utipsInfo").show();
				return flag;
			}else{
				if(obj.usercount != username){
					$("#emailInfo").html("用户名输入错误，请可对！");
					$("#emailInfo").show();
					return flag;
				}
				if(obj.email != email){
					$("#codeInfo").html("绑定邮箱输入错误，请可对！");
					$("#codeInfo").show();
					return flag;
				}
				sendCode = obj.password;//后台设置成code了
				uid = obj.billid;
				flag = true;
				$("#codeInfo").html("【获取验证码】按钮，单次点击后60秒之内不可点击，60秒后可再次点击！！");
				$("#codeInfo").show();
			}
		}
	});
}

//重置
function reset(){
	$("#username").val("");
	$("#emailInfo").val("");
	$("#codeInfo").val("");
	$("#errorinfo").html("");
	$("#errorinfo").hide();
}
</script>
</head>

<body class="bg-1">
<div id="wrap" >
	<div class="row" >
		<div id="content" class="col-md-12 full-page login divWidth" style="padding-left: 0;padding-right: 0;" >
			<div class="inside-block divWidth">
				<div class="l-box divWidth" style="padding-right: 0;padding-left: 0;">
					<div class="m-logo divWidth"><h3 style="margin-top:0;margin-bottom:5px;">账号验证</h3></div>
					<div class="divWidth">系统将发送验证码至绑定邮箱进行账号，验证成功方可重置密码</div>
					<div class="m-title divWidth">
					
					<div id="logindiv" class="">
						<div style="overflow:hidden;" class="">
							<div style="overflow:hidden;"class="">
							<div class="divWidth">
								<label class="">用 户 名</label><input type="text" class="inputheight" id="username" placeholder="用 户 名"/>
							</div>
							<div class="tips-info" id="utipsInfo"></div>
							<div class="divWidth" style="margin-left: 34px;">
								<span style="margin-left: 23px;"><label>绑定邮箱</label></span><input type="text" class="inputheight" style="margin-left: 41px;" id="email" placeholder="绑定邮箱"/><label class="span">没有邮箱</label>
							</div>
							<div class="email-info" id="emailInfo"></div>
							<div class="divWidth" style="margin-left: 86px;">
								<label>验 证 码</label><input type="text" class="inputheight" id="code" style="margin-top: 10px;" placeholder="验证码"/>
								<button class="btn btn-slategray but" onclick="cheackCode()" id="getCode">获取验证码</button>
							</div>
							<div class="code-info" id="codeInfo"></div>
						</div>
						<div class="divWidth">
							<button class="btn btn-slategray but" onclick="reset()">取 消</button>
							<span></span>
							<button class="btn btn-slategray but" style="margin-left: 56px;" onclick="toSetWpd()">提 交</button>
						</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
</div>
<div class="alert-mask" id="errorMask"></div>
<div class="alert alert-red" id="errorinfo"></div>
<!-- JavaScript -->
<script type="text/javascript">
jQuery(document).ready(function(){
	if (jQuery.cookie("remember") == "true") {
		jQuery("#remember").click(); 
		jQuery("#username").val(jQuery.cookie("username"));
		jQuery("#password").val(jQuery.cookie("password"));
	}
	$(this).keydown(function (e){
		if(e.which == "13"){
			login();
		}
	})


	/* $("body").click(function(){
		$("#errorinfo").html("");
		$("#errorMask").hide();
		$("#errorinfo").hide();
	}) */
});
</script>     
</body>
</html>
