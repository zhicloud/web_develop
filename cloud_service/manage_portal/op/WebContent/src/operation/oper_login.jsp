<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>运营平台－登录</title>
	<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/src/common/img/favicon.ico" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/operation/css/operation.css" media="all"/>
	<!--[if lt IE 9]>
	<script src="<%=request.getContextPath()%>/src/common/js/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/src/common/js/respond.min.js"></script>
	<![endif]-->
</head>
<body>
<div class="container-fluid" style="height:100%;">
	<div class="log-layout">
		<div class="log-wrap">
			<div class="log-cont">
				<div class="l-left-section f-fl"><img class="img-responsive center-block" src="<%=request.getContextPath()%>/src/operation/img/oper_login_scenery.png" alt="scenery"/></div>
				<div class="l-right-section f-fr">
					<div class="rs-tit">
						<img class="img-responsive log-logo" src="<%=request.getContextPath()%>/src/operation/img/oper_login_logo.png" alt="logo" />
						<h6>致云公有云运营平台v2.0</h6>
					</div>
					<div class="rs-form">
						<form class="form-signin" action="oper_overview.jsp">
							<ul class="list-unstyled">
								<li class="form-group">
									<label for="iptusername" class="sr-only">用户名</label>
									<input type="text" name="username" id="iptusername" class="form-control input-lg ipt-sty" placeholder="用户名" />
								</li>
								<li class="form-group">
									<label for="iptpassword" class="sr-only">密码</label>
									<input type="password" name="password" id="iptpassword" class="form-control input-lg ipt-sty" placeholder="密码" />
								</li>
								<li class="form-group">
									<label for="iptverifycode" class="sr-only">验证码</label>
									<input type="text" name="verifycode" id="iptverifycode" class="form-control input-lg ipt-sty" placeholder="验证码" style="display:inline-block;width:120px;" />
									<img class="verify-code" src="<%=request.getContextPath()%>/src/operation/img/verify_code.jpg" alt="验证码" />
									<a class="btn btn-link link-sty active" href="javascript:;" role="button">换一个</a>
								</li>
								<li class="pt10">
									<button class="btn btn-lg btn-primary btn-block" type="submit">立即登录</button>
								</li>
							</ul>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/operation/js/oper_login.js"></script>
<!-- JavaScript_end -->
</body>
</html>