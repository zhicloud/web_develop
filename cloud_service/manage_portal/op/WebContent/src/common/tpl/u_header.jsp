<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String name = "";
	if(loginInfo!=null){
		name=loginInfo.getAccount();
	}
	 
%>
<base href="<%=request.getContextPath() %>" />
<!-- <div class="s-scroll"><div id="scrollText"></div></div> -->
<div class="g-hd">
	<div class="wrap">
 		<div class="logo f-fl"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath() %>/src/common/img/logo.png" alt="LOGO" /></a></div>
		<div id="beforelogin" class="user beforelog f-fr">
  			<a class="reglink" href="javascript:;">注册</a>
			<span>|</span>
			<a class="loginlink" href="javascript:;">登录</a>
		</div>
		<div id="afterlogin" class="user afterlog f-fr" style="display:none;">
			<img class="portrait" src="<%=request.getContextPath() %>/src/common/img/portrait.png" alt="用户头像" />
			<a class="mycloud c-29f" href="<%=request.getContextPath() %>/user.do">我的云端</a>
			<span>|</span>
 			<a class="logoutlink" id="logoutlink" href="javascript:;" onclick="commonlogout();">注销</a>
		</div>
		<div class="nav f-fr">
			<div class="nav-list">
				<div class="n-iteam"><a class="lev1 current" href="<%=request.getContextPath() %>/">首页</a></div>
				<div class="n-iteam">
					<a class="lev1" href="<%=request.getContextPath() %>/main.do?target=cloudser">云服务产品</a>
					<div class="submenu pro-submenu">
						<span class="sm-top"></span>
						<ul style="width:254px;">
							<li style="width:98px;">
								<!-- <label>计算</label> -->
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=cloudser">云服务器</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=cshighpowered">高性能云服务器</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=cloudcdn">云CDN</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=loadbalance">负载均衡</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=cloudmonitor">云监控</a>
							</li>
							<li style="width:56px;">
								<!-- <label>存储</label> -->
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=exclucloud">专属云</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=clouddrive">云硬盘</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=clouddatabase">云数据库</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=mirrorhost">镜像主机</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="n-iteam"><a class="lev1" href="<%=request.getContextPath() %>/main.do?target=eggplan" style="margin-top:-5px;">蛋壳<sup>+</sup>计划</a></div>
				<div class="n-iteam"><a class="lev1" href="<%=request.getContextPath() %>/main.do?target=solution">解决方案</a></div>
				<div class="n-iteam">
					<a class="lev1" href="<%=request.getContextPath() %>/main.do?target=serconcept">技术与支持</a>
					<div class="submenu ts-submenu" style="left:3px;">
						<span class="sm-top"></span>
						<ul>
							<li>
								<label>致云服务</label>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=serconcept">服务理念</a>
								<!-- <a class="lev2" href="u_seronline.jsp?do=uts&view=usol">在线客服</a> -->
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=serconsult">咨询服务</a>
							</li>
							<li>
								<label>技术支持</label>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=helpcenter">帮助中心</a>
								<!-- <a class="lev2" href="u_apidoc.jsp?do=uts&view=uad">API文档</a> -->
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=arcser">备案服务</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="n-iteam">
					<a class="lev1" href="<%=request.getContextPath() %>/main.do?target=comprofile">关于我们</a>
					<div class="submenu au-submenu" style="left:3px;">
						<span class="sm-top"></span>
						<ul>
							<li>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=comprofile">公司简介</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=jionus">加入我们</a>
							</li>
							<li>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=newsdynamics">新闻动态</a>
								<a class="lev2" href="<%=request.getContextPath() %>/main.do?target=contactus">联系我们</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var name = '<%=name%>';
 $(document).ready(function(){ 
	if(name!= ''){  
		   loaduser(name,0);
		}else{ 
		   loaduser();
	 	}
}); 
 
</script>
