<%@ page pageEncoding="utf-8"%>
<%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.loginpage { background: #fff url(../images/patternbg.png); }
.loginbox { 
	width: 350px; padding: 5px; background: #fff; margin: 7% auto 0 auto; -moz-border-radius: 2px; -webkit-border-radius: 2px;
	border-radius: 2px; -moz-box-shadow: 0 0 2px rgba(0,0,0,0.3); -webkit-box-shadow: 0 0 2px rgba(0,0,0,0.3); 
	box-shadow: 0 0 2px rgba(0,0,0,0.3);
}
.loginboxinner { 
	padding: 20px; background: #32415a url(../images/patternbg.png); -moz-border-radius: 0 2px 2px 0; 
	-webkit-border-radius: 0 0 2px 2px; border-radius: 0 0 2px 2px;
}
.loginbox .logo { text-align: center; }
.loginbox .logo h1 { 
	font-family: 'RobotoCondensed', Arial, Helvetica, sans-serif; font-size: 32px; color: #fff; border-bottom: 1px solid #56647d; 
	line-height: normal; margin-bottom: 5px;
}
.loginbox .logo h1 span { color: #FB9337; }
.loginbox .logo p { font-weight: bold; color: #eee; font-style: italic; }

.loginbox form { display: block; margin-top: 20px; }
.loginbox .username { 
	background: #eee url(../images/icons/username.png) no-repeat 13px center; -moz-border-radius: 2px; -webkit-border-radius: 2px; 
	border-radius: 2px; -moz-box-shadow: 0 1px 2px rgba(0,0,0,0.4); -webkit-box-shadow: 0 1px 2px rgba(0,0,0,0.4); 
	box-shadow: 0 1px 2px rgba(0,0,0,0.4); margin: 20px 0;
}
.loginbox .usernameinner { 
	margin-left: 45px; border-left: 1px solid #ddd; background: #fff; padding-right: 20px; 
	-moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; 
}
.loginbox .username input { 
	padding: 15px 10px; border: 0; font-size: 14px; width: 100%; box-shadow: none; color: #666; 
	font-family: 'RobotoCondensed', Arial, Helvetica, sans-serif; -moz-border-radius: 0 2px 2px 0; -webkit-border-radius: 0 2px 2px 0;
	border-radius: 0 2px 2px 0;
}
.loginbox .password { 
	background: #eee url(../images/icons/password.png) no-repeat 13px center; -moz-border-radius: 2px; -webkit-border-radius: 2px; 
	border-radius: 2px; -moz-box-shadow: 0 1px 2px rgba(0,0,0,0.4); -webkit-box-shadow: 0 1px 2px rgba(0,0,0,0.4); 
	box-shadow: 0 1px 2px rgba(0,0,0,0.4); margin: 20px 0; overflow: hidden;
}
.loginbox .passwordinner { margin-left: 45px; border-left: 1px solid #ddd; background: #fff; }
.loginbox .password input { 
	padding: 15px 10px; border: 0; font-size: 14px; width: 330px; box-shadow: none; color: #666; 
	font-family: 'RobotoCondensed', Arial, Helvetica, sans-serif;
}

.loginbox button { 
	background: #f0801d url(../images/btngrad.png) repeat-x top left; border: 0; padding: 15px 0; text-align: center; 
	font-family: 'RobotoCondensed', Arial, Helvetica, sans-serif; font-size: 14px; font-weight: normal; width: 100%; text-transform: uppercase;  
	-moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; -moz-box-shadow: 0 1px 2px rgba(0,0,0,0.4); 
	-webkit-box-shadow: 0 1px 2px rgba(0,0,0,0.4); box-shadow: 0 1px 2px rgba(0,0,0,0.4);
}
.loginbox button:hover { background-color: #f0721e; }
.loginbox .keep { margin-top: 20px; font-weight: bold; color: #ccc; font-size: 11px; }
.loginbox .loginmsg { 
	background: #fffccc; color: #333; margin-bottom: 10px; padding: 5px; text-align: center; font-size: 11px; 
	-moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px;
}
.loginf { 
	padding: 10px; background: #2e3e59; -moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px; 
	-moz-box-shadow: inset 0 1px 1px #23324b; -webkit-box-shadow: inset 0 1px 1px #23324b; box-shadow: inset 0 1px 1px #23324b;
	border-bottom: 1px solid #475875;
}

.loginpage .nousername, .loginpage .nopassword { display: none; }
.loginpage .nopassword { color: #fff; }
.loginpage .nopassword .thumb { 
	padding: 5px; background: #fff; display: inline-block; vertical-align: top; 
	-moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px;
}
.loginpage .nopassword .userlogged { display: inline-block; margin-left: 10px; font-weight: bold; }
.loginpage .nopassword .userlogged h4 { font-size: 14px; font-family: 'RobotoCondensed', Arial, Helvetica, sans-serif; }
.loginpage .nopassword .userlogged a { color: #f0801d; font-style: italic; }
.loginpage .nopassword .userlogged a:hover { text-decoration: underline; }
.loginpage .notibar { border: 0; }
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.11.2.js"></script>
<script type="text/javascript">
	function login(){
		$("#login").submit();
	}
</script>
<title>Insert title here</title>
</head>
<body class="loginpage">
	<div class="loginbox">
    	<div class="loginboxinner">
        	
            <div class="logo">
            	<h1 class="logo"><span>致云科技</span></h1>
				<span class="slogan">云桌面管理系统</span>
            </div><!--logo-->
            
            <br clear="all" /><br />
            
            <div class="nousername">
				<div class="loginmsg">密码不正确.</div>
            </div><!--nousername-->
            
            <div class="nopassword">
				<div class="loginmsg">密码不正确.</div>
                <div class="loginf">
                    <div class="thumb"><img alt="" src="images/thumbs/avatar1.png" /></div>
                    <div class="userlogged">
                        <h4></h4>
                        <a href="index.html">Not <span></span>?</a> 
                    </div>
                </div><!--loginf-->
            </div><!--nopassword-->
            
            <form id="login" action="<%=path %>/user/login" method="post">
            	
                <div class="username">
                	<div class="usernameinner">
                    	<input type="text" name="username" id="username" />
                    </div>
                </div>
                
                <div class="password">
                	<div class="passwordinner">
                    	<input type="password" name="password" id="password" />
                    </div>
                </div>
                
                <button onclick="login();">登录</button>
                
                <div class="keep"><input type="checkbox" /> 记住密码</div>
            
            </form>
            
        </div><!--loginboxinner-->
    </div><!--loginbox-->


</body>
</html>