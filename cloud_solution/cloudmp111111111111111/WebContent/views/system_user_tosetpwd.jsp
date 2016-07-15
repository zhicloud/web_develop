<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- login.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>重置密码</title>
<script src="<%=request.getContextPath()%>/assets/js/jquery.js"></script>
<script src="<%=request.getContextPath() %>/js/artDialog/artDialog.js?skin=chrome"></script>
<script src="<%=request.getContextPath() %>/js/artDialog/jquery.artDialog.source.js"></script>
<style type="text/css">
html {padding-top: 0;}
.inside-block{width:650px;}
.center{text-align: center;margin-top: 50px;background-color:rgba(240, 245, 248, 1.0)}
.body #content.div{height: 25px;margin-top:10px;}
.top{height: 25px;margin-top: 20px;}
.but{
background: #b977ff;
display: inline-block;
height: 37px;
line-height: 38px;
width: 83px;
color: #fcf7f5;
padding: 0;
border: none;
font-size: 16px;
}
.textColor{
color:gray;
}
.input{
height: 25px;
}


body #content.div input:focus, #content.full-page .inside-block form .input-group input:focus + .input-group-addon {background-color: rgba(0, 0, 0, 0.3) !important;-webkit-box-shadow: none !important;box-shadow: none !important;}
body #content.div input::-webkit-input-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input:-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input::-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input:-ms-input-placeholder {color: rgba(255, 255, 255, 0.6);}

</style>
</head>

<body class="center">
<div id="content">
	<div class="div"><h3 style="margin-top:0;margin-bottom:5px;">设置新密码</h3></div>
	<div class="top"><input type="password" class="input" id="pwd" placeholder="新 密 码"/></div>
	<input type="hidden" id="uid" value="${uid}"/>
	<div class="top textColor">密码长度至少为6位</div>
	<!-- • 密码长度最小6位，上限为50个字符。 -->
	<div class="top">
		<button class="but" onclick="reset()">取 消</button>
		<button class="btn btn-slategray but" onclick="upWpd()">提 交</button>
	</div>
</div>
<div class="alert-mask" id="errorMask"></div>
<div class="alert alert-red" id="errorinfo"></div>
</body>
</html>
<script type="text/javascript">
var path = '<%=request.getRequestURI()%>';
function upWpd(){
	var password = $("#pwd").val();
	var uid = $("#uid").val();
	if(password.length<6){
		alert("密码长度最小6位，上限为50个字符。");
		return false;
	}else{
		jQuery.ajax({
			type: "POST",
			async:true,
			url: "<%=request.getContextPath() %>/transform/findUserPassWord",
			data: "uid="+uid+"&password="+password,
			contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
			success: function(result){
				var obj = eval("("+result+")");
				//验证用户输入用户信息的是否存在
				if(obj == 'success'){
					window.parent.goToLogin();//修改成功调用父页面方法跳转登录页面
					window.parent.window.art.dialog({ id: 'setpwdpage' }).close();
				}else{
					$("#errorinfo").html("密码重置失败。");
					$("#errorinfo").show();
					return false;
				}
			}
		});
	}
	
}

//重置
function reset(){
	$("#pwd").val("");
	$("#errorinfo").html("");
	$("#errorinfo").hide();
}
</script>
