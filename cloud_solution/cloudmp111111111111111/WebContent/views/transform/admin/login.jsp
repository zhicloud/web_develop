<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- login.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>登录页面</title>
<%@include file="/views/common/common.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery.cookie.js"></script>
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
	background-image:url("<%=request.getContextPath() %>/assets/images/loginBg.png");
}
body #content .inside-block .l-box{
	position:absolute;
	top:50%;
	left:50%;
	min-width: 560px;
	min-height: 663px;
	margin-top:-331.5px;
	margin-left:-280px;
	padding:47px 70px 93px;
}
body #content .inside-block .l-box .m-logo{
	margin-top:40px;
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
body #content.full-page .inside-block .logindiv .input-group input:focus, #content.full-page .inside-block form .input-group input:focus + .input-group-addon {background-color: rgba(0, 0, 0, 0.3) !important;-webkit-box-shadow: none !important;box-shadow: none !important;}
body #content.full-page .inside-block .logindiv .input-group input::-webkit-input-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .input-group input:-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .input-group input::-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.full-page .inside-block .logindiv .input-group input:-ms-input-placeholder {color: rgba(255, 255, 255, 0.6);}
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
body #content div.checkbox label:before {
	top: 3.5px;
	width: 14px;
	height: 14px;
	margin-right: 8px;
}
body #content div.checkbox label:after {
	top: 0.5px;
	left: 0.5px;
	width: 14px;
	height: 14px;
	margin-right: 8px;
}
body #content .novice-guide{
	overflow:hidden;
	margin:25px auto 0;
	width:333px;
}
body #content .novice-guide a{
	display:block;
	color:#fff;
	font-size:14px;
	height:24px;
	line-height:24px;
	text-decoration:none;
	/* border-bottom:1px solid #fff;*/
	filter:alpha(opacity=60);  
    -moz-opacity:0.6;  
    -khtml-opacity: 0.6;  
    opacity: 0.6; 
    padding-bottom:1px;
}
body #content .novice-guide a:hover{
	/* border-bottom:1px solid #fff; */
	filter:alpha(opacity=100);  
    -moz-opacity:1.0;  
    -khtml-opacity: 1.0;  
    opacity: 1.0;
}
body #content .novice-guide a.c-dsk-client{float:left;background:url("<%=request.getContextPath() %>/assets/images/c_dsk_client.png") no-repeat center bottom;}
body #content .novice-guide a.use-guide{float:right;background:url("<%=request.getContextPath() %>/assets/images/use_guide.png") no-repeat center bottom;}
.tips-info{display:none;font-size:12px;width:333px;margin:0 auto;text-align:right;}
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
	margin-top:-23px;
	margin-left:-95px;
	margin-bottom:0;
	z-index:99999;
	width:189px;
	height:46px;
	line-height:46px;
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
</style>
<script type="text/javascript">
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
var path = '<%=request.getRequestURI()%>';
//保存用户信息 
function saveUserInfo() {
    if (jQuery("#remember").get(0).checked == true) {
        var username = jQuery("#username").val();
        var password = jQuery("#password").val();
        jQuery.cookie("remember", "true", {expires: 7}); // 存储一个带7天期限的 cookie 
        jQuery.cookie("username", username, {expires: 7}); // 存储一个带7天期限的 cookie 
        jQuery.cookie("password", password, {expires: 7}); // 存储一个带7天期限的 cookie 
    } else {
        jQuery.cookie("remember", "false", {expires: -1});
        jQuery.cookie("username", '', {expires: -1});
        jQuery.cookie("password", '', {expires: -1});
    }
}
//登录
function login(){
	var usercount = jQuery("#username").val();
	var password = jQuery("#password").val();
	if(usercount==undefined||usercount==""){
		$("#utipsInfo").html("用户名不能为空");
		$("#utipsInfo").show();
		return;
	}else{
		$("#utipsInfo").html("");
		$("#utipsInfo").hide();
	}
	if(password==undefined||password==""){
		$("#ptipsInfo").html("密码不能为空");
		$("#ptipsInfo").show();
		return;
	}else{
		$("#ptipsInfo").html("");
		$("#ptipsInfo").hide();
	}
	var flag = 0;
	if(jQuery("#remember").get(0).checked==true){
		flag = 1;
	}
	jQuery.ajax({
		type: "POST",
		async:true,
		url: "<%=request.getContextPath() %>/transform/login",
		data: "usercount="+usercount+"&password="+password+"&flag="+flag,
		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		success: function(result){
			var obj = eval("("+result+")");
			if(obj.status=="success"){
				saveUserInfo();
				if(path=='/op/'||obj.url.indexOf("error.jsp")>-1||obj.url.indexOf("/transform/userlogin")>-1){
					window.location.href=obj.url;
				}else{	
					window.location.reload();
				}
			}else if(obj.status=="fail"){
				$("#errorinfo").html("<i class='icon-exclamation'></i>"+obj.result);
				$("#errorMask").show();
				$("#errorinfo").show();
				setTimeout(function(){
					$("#errorinfo").html("");
					$("#errorMask").hide();
					$("#errorinfo").hide();
				},3000);
			}else{
				$("#errorinfo").html("未知错误,请联系管理员");
				$("#errorinfo").show();
			}
		}
	});
	return false;
}
//重置
function reset(){
	$("#username").val("");
	$("#password").val("");
	$("#errorinfo").html("");
	$("#errorinfo").hide();
}

//调整找回密码页面
function toFindPage(){
	window.location.href = "<%=request.getContextPath()%>/transform/findPwdPage";
}
</script>
</head>

<body class="bg-1">
<div id="wrap" >
	<div class="row" >
		<div id="content" class="col-md-12 full-page login" style="padding-left: 0;padding-right: 0;" >
			<div class="inside-block">
				<div class="l-box" style="padding-right: 0;padding-left: 0;">
					<div class="m-logo"><img src="<%=request.getContextPath()%>/assets/images/m_logo.png" alt="致云云管理中心 LOGO"/></div>
					<div class="m-title">
					<h3 style="margin-top:0;margin-bottom:5px;">${productName}</h3>
					<img src="<%=request.getContextPath()%>/assets/images/m_title.png" alt="致云云管理中心 ZHICLOUD"/></div>
					<div id="logindiv" class="logindiv">
						<div style="overflow:hidden;">
							<div class="input-group">
								<input type="text" class="form-control" id="username" placeholder="用 户 名"/>
								<div class="input-group-addon"><i class="fa fa-user"></i></div>
							</div>
							<div class="tips-info" id="utipsInfo"></div>
							<div class="input-group">
								<input type="password" class="form-control" id="password" placeholder="密 码"/>
								<div class="input-group-addon"><i class="fa fa-key"></i></div>
							</div>
							<div class="tips-info" id="ptipsInfo"></div>
						</div>
						<div class="log-in">
							<button class="btn btn-primary" onclick="login()" style="display: inline-block; height: 45px; line-height: 45px; width: 151px; color: #fcf7f5; padding: 0; border: none; font-size: 16px;">登 录</button>
							<span></span>
							<button class="btn btn-slategray" onclick="reset()" style="display: inline-block; height: 45px; line-height: 45px; width: 151px; color: #fcf7f5; padding: 0; border: none; font-size: 16px;">重 置</button>
						</div>
						<div class="controls" style="overflow: hidden; width: 333px; margin: 10px auto 0;">
							<div class="checkbox check-transparent" style="padding: 0; display: block; margin: 0 !important; float:left;">
								<input type="checkbox" id="remember"/>
								<label for="remember">记住密码</label>
							</div>
							<a class="psw-forget" href="javascript:toFindPage();" style="font-size: 12px; display: block; float:right; color: rgba(255, 255, 255, 0.6);">忘记密码?</a>
						</div>
						<div class="novice-guide">
							<a class="c-dsk-client" href="${pageContext.request.contextPath}/download/btngrad.png">云桌面客户端下载</a>
							<a class="use-guide" href="javascript:;">使用指南</a>
						</div>
					</div>
					<div class="tile-body">
						<a href="#modalDialog" id="dia" role="button"  data-toggle="modal"></a>
						<div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content" style="width:60%;margin-left:20%;">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
										<h3 class="modal-title" id="modalDialogLabel"><strong>提示</strong></h3>
									</div>
									<div class="modal-body"><p id="tipscontent"></p></div>
								</div>
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
