<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>服务器管理登录</title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/common.js"></script>
		<link rel="shortcut icon" href="<%=request.getContextPath() %>/images/favicon.ico" type="image/x-icon" /> 
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css" media="all"/>
	    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/server.css" media="all"/>
	</head>
<body class="log-bg">
<div class="main">
	<div class="l-wrap">
		<div class="l-tit">
			<img src="<%=request.getContextPath() %>/images/slogo.png" alt="slogo" />
			<h3>服务器管理登陆</h3>
		</div>
		<div class="l-sub">
			<div style="width:100%;text-align: center;color: red;" id="errorinfo"></div>
			<div class="l-mail"><i class="icon-mail"></i><input type="text" name="username" id="username" class="ipt-sty" placeholder="输入邮箱地址"/></div>
			<div class="l-password"><i class="icon-password"></i><input type="password" name="password" class="ipt-sty" id="password" placeholder="输入密码"/></div>
			<div style="width:100%;text-align: right;">
				<a href="#" id="forgetpwd" onclick="forgetpwd()">忘记密码</a>
			</div>			
			<div class="l-logbtn"><a class="btn-login" href="#" id="s_logbtn" onclick="beforelogin()">登录</a></div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
// 忘记密码
function forgetpwd(){
	var username = $("#username").val();
	if(username==undefined||username==""||username==null){
		$("#errorinfo").html("用户名不能为空");
		return;
	}
	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath() %>/login/getMachineKey",
        data:{username:username},
       	datatype: "json",
       	async: false,
       	success:function(data){
        	var re = eval(data);
       		if(re.status == "success"){
       			$("#errorinfo").html("机器码：" + re.machinekey);
       		}else{
       			$("#errorinfo").html("获取机器码失败。");
       		}       
     	},
        complete: function(XMLHttpRequest, textStatus){
        },
        error: function(){
      		$("#errorinfo").html("获取机器码失败1。");
        }
	});
}

//登录
function beforelogin(){
	var username = $("#username").val();
	var password = $("#password").val();	
	if(username==undefined||username==""||username==null){
		$("#errorinfo").html("用户名不能为空");
		return;
	}
	if(password==undefined||password==""||password==null){
		$("#errorinfo").html("密码不能为空");
		return;
	}
	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath() %>/login/beforelogin",
        data:{username:username,password:password},
        datatype: "json",
        success:function(data){
        	var re = eval(data);
       		if(re.status == "success"){
       			window.location.href = "<%=request.getContextPath() %>/login/main";
       		}else{
       			$("#errorinfo").html(re.message);
       		}       
     	},
        complete: function(XMLHttpRequest, textStatus){
        },
        error: function(){
        }         
	});
}
</script>
</html>
