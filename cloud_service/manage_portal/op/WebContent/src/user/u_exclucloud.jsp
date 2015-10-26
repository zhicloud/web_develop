<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_exclucloud.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>专属云 -- 致云 ZhiCloud</title>
	<link rel="shortcut icon" href="../common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../common/css/global.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/usersite.css" media="all"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
</head>
<body>
<div class="g-doc">
	<!-- header -->
	<%@ include file="../common/tpl/u_header.jsp"%>
	<!-- /header -->
	<div class="f-cb"></div>
	<div class="g-ec-bd">
		<section class="main">
			<section class="floor_one">
				<div class="wrap">
					<div class="cont">
						<h3>致云专属云</h3>
						<p>由于安全性和隔离性等方面的考虑，或用户需要将多台云服务器放置在隔离的内网中进行集群作业，从而组合而成的一个带有软路由器和软防火墙的虚拟专属内网。</p>
						<a href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage">立即购买</a>
					</div>
				</div>
			</section>
			<div class="f-cb"></div>
			<section class="floor_two">
				<div class="wrap">
					 <div class="cont">
					 	<h4 class="title">多台云服务器组成一个专有的网络</h4>
						<div class="show">
							<div class="server_info">
								<div class="server1"><img src="img/server1.png" alt="服务器一"/></div>
								<div class="server2"><img src="img/server2.png" alt="服务器二"/></div>
								<div class="server3"><img src="img/server3.png" alt="服务器三"/></div>
								<div class="server4"><img src="img/server4.png" alt="服务器四"/></div>
							</div>
							<div class="ellipse"><img src="img/ellipse.png" alt="圆点" /></div>
							<div class="line"><img src="img/line.png" alt="连线" /></div>
							<!--<div class="shadow"><img src="img/shadow.png" alt="阴影" /></div>-->
						</div>
					 </div>
				</div>
			</section>
			<div class="f-cb"></div>
			<section class="floor_three">
				<div class="wrap">
					<div class="cont">
					 	<h4 class="title">并将其放置在隔离的内网环境中进行集群作业</h4>
					 	<div class="show">
					 		<div class="server"><img src="img/ser_shadow_big.png" alt="服务器" /></div>
						 	<div class="tri_bottom"><img src="img/tri_bottom.png" alt="三角隔离罩底部" /></div>
						 	<div class="ser_shadow"><img src="img/ser_shadow.png" alt="服务器阴影" /></div>
						 	<div class="tri_mask"><img src="img/tri_mask.png" alt="三角隔离罩" /></div>
						 	<div class="tri_shadow"><img src="img/tri_shadow.png" alt="三角隔离罩阴影" /></div>
					 	</div>
					 </div>
				</div>
			</section>
			<div class="f-cb"></div>
			<section class="floor_four">
				<div class="wrap">
					<div class="cont">
					 	<h4 class="title">并集成一个由 防火墙 路由 交换机组成的统一网络开关</h4>
					 	<div class="show">
					 		<div class="tri_shield"><img src="img/tri_shield.png" alt="三角隔离罩(全)" /></div>
					 		<div class="switch"><img src="img/switch.png" alt="开关"/></div>
					 		<div class="net_switch"><img src="img/net_switch.png" alt="网络开关"/></div>
					 	</div>
					 </div>
				</div>
			</section>
			<div class="f-cb"></div>
			<section class="floor_five">
				<div class="wrap">
					<div class="cont">
					 	<h4 class="title">统一网络出口连接外网</h4>
					 	<div class="show">
					 		<div class="tri_shield"><img src="img/tri_shield.png" alt="三角隔离罩(全)" /></div>
					 		<div class="f_switch"><img src="img/switch.png" alt="开关"/></div>
					 		<div class="f_net_switch"><img src="img/net_switch.png" alt="网络开关"/></div>
					 		<div class="projection"><img src="img/projection.png" alt="投影"/></div>
					 		<div class="yahoo"><img src="img/yahoo.png" alt="雅虎"/></div>
					 		<div class="sina"><img src="img/sina.png" alt="新浪"/></div>
					 		<div class="baidu"><img src="img/baidu.png" alt="百度"/></div>
					 		<div class="qq"><img src="img/qq.png" alt="QQ"/></div>
					 		<div class="weibo"><img src="img/weibo.png" alt="微博"/></div>
					 		<div class="taobao"><img src="img/taobao.png" alt="淘宝"/></div>
					 	</div>
					 </div>
				</div>
			</section>
			<!-- <div class="f-cb"></div>
			<section class="floor_six">
				<div class="wrap">
					<div class="cont">
						<h4 class="s_title">产品优势</h4>
						<ul>
							<li>
								<span><img src="img/pro_advantage_one.png" alt="安全封闭" /></span>
								<p class="line"></p>
								<h5>安全封闭</h5>
								<p class="c">各个专属云之间完全间隔互不干扰</p>
							</li>
							<li>
								<span><img src="img/pro_advantage_two.png" alt="自我掌控" /></span>
								<p class="line"></p>
								<h5>自我掌控</h5>
								<p class="c">自我掌控进出规则的专属虚拟内网</p>
							</li>
							<li>
								<span><img src="img/pro_advantage_three.png" alt="简便直观" /></span>
								<p class="line"></p>
								<h5>简便直观</h5>
								<p class="c">简便的专属网络配置<br/>直观的图形化可视操作</p>
							</li>
						</ul>
					</div>
				</div>
			</section> -->
			<div class="f-cb"></div>
			<div class="g-sec4" style="height:944px;">
				<div class="wrap">
					<h3>产品优势</h3>
					<p class="tips">云主机是一种全新的服务器租用服务</p>
					<div class="pro-advn-list pa">
						<div class="pa-iteam">
							<img class="f-db" src="img/exc_pa1.png" alt="3分钟定义自己的网络"/>
							<h4>3分钟定义自己的网络</h4>
							<p>CPU、内存、磁盘、镜像、网络2秒交付使用。</p>
						</div>
						<div class="pa-iteam">
							<img class="f-db" src="img/exc_pa2.png" alt="安全隔离"/>
							<h4>安全隔离</h4>
							<p>使用隧道技术实现独立的专属网络区，与其他租户网络安全隔离</p>
						</div>
						<div class="pa-iteam">
							<img class="f-db" src="img/exc_pa3.png" alt="访问控制"/>
							<h4>访问控制</h4>
							<p>灵活的访问控制规则，满足政务、金融用户的安全隔离规范</p>
						</div>
						<div class="pa-iteam">
							<img class="f-db" src="img/exc_pa4.png" alt="丰富的网络连接方式"/>
							<h4>丰富的网络连接方式</h4>
							<p>支持VPN连接</p>
						</div>
					</div>
				</div>
			</div>
			<div class="f-cb"></div>
			<div class="g-sec5">
			<div class="wrap">
				<h3>应用场景</h3>
				<div class="apt-snr-list as">
					<div class="as-iteam">
						<div class="exc_as1"></div>
						<h4>要求有自己的私有云环境，确保安全的企事业单位</h4>
					</div>
					<div class="as-iteam">
						<div class="exc_as2"></div>
						<h4>电商、游戏、电子政务等行业</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="f-cb"></div>
			<section class="buy-cont">
				<div class="wrap">
					<p><a class="emer-buy" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage">立即购买</a></p>
				</div>
			</section>
		</section>
	</div>
	<div class="f-cb"></div>
	<!-- footer -->
	<%@ include file="../common/tpl/u_footer.jsp"%>
	<!-- /footer -->
</div>
<!-- login -->
<%@ include file="../common/tpl/u_login.jsp"%>
<!-- /login -->
<!-- JavaScript -->
<script type="text/javascript" src="js/u_exclucloud.js"></script>
<!-- /JavaScript -->
<script type="text/javascript">
	$(function(){
		navHighlight("upro","uec");
	})
</script>
</body>
</html>