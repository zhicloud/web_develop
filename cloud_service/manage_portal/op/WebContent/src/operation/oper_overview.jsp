<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>运营平台－运营概览</title>
	<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/src/common/img/favicon.ico" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/oper_common.css" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/operation/css/operation.css" media="all"/>
	<!--[if lt IE 9]>
	<script src="<%=request.getContextPath()%>/src/common/js/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/src/common/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
<!-- header -->
<%@ include file="../common/tpl/oper_header.jsp"%>
<!-- sideNav -->
<%-- <%@ include file="../common/tpl/oper_sidenav.jsp"%> --%>
<nav class="sidebar">
	<div class="nav-list">
		<div class="nav-item">
			<a class="oper-sty current" href="oper_overview.jsp"><i class="icon-tit icon-operational-overview"></i>运营概览</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-user-management"></i>用户管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="oper_customerlist.jsp">客户列表</a></li>
				<li><a class="sub-sty" href="oper_customertype.jsp">客户类型</a></li>
				<li><a class="sub-sty" href="oper_operationlog.jsp">操作日志</a></li>
				<li><a class="sub-sty" href="oper_consumptionrecord.jsp">消费记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-product-management"></i>产品管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">自用云主机</a></li>
				<li><a class="sub-sty" href="javascritp:;">云服务器管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">用户专属云管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">云硬盘管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-package-management"></i>套餐管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">套餐项管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">套餐价格自定义</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-resource-management"></i>资源池管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">计算资源池管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">存储资源池管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">地址资源池管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">端口资源池管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">数据库监控</a></li>
				<li><a class="sub-sty" href="javascritp:;">平台资源监控</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;"><i class="icon-tit icon-routing-management"></i>智能路由管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-whmirror-management"></i>仓库和镜像管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">系统镜像管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">仓库类型管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-statistical-analysis"></i>统计分析<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">报表统计</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-financial-management"></i>财务管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">现金券管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发票管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-system-management"></i>系统管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">基本信息</a></li>
				<li><a class="sub-sty" href="javascritp:;">修改密码</a></li>
				<li><a class="sub-sty" href="javascritp:;">意见反馈</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-mail-management"></i>邮件管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">配置管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">模块管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-mesg-management"></i>短信管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">配置管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">模块管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;"><i class="icon-tit icon-eggplan-management"></i>蛋壳计划管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;"><i class="icon-tit icon-mirrorhost-management"></i>镜像主机申请管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-monitinfor-management"></i>监控信息管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
			    <li><a class="sub-sty" href="javascritp:;">服务管理</a></li>		
				<li><a class="sub-sty" href="javascritp:;">总体概况查看</a></li>
				<li><a class="sub-sty" href="javascritp:;">机房信息查看</a></li>
				<li><a class="sub-sty" href="javascritp:;">机架信息查看</a></li>
				<li><a class="sub-sty" href="javascritp:;">服务器信息查看</a></li>
				<li><a class="sub-sty" href="javascritp:;">云主机信息查看</a></li>
				<li><a class="sub-sty" href="javascritp:;">预警规则管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">资源监控管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">系统全局监控</a></li>
			</ul>
		</div>
	</div>
</nav>
<!-- mainContent -->
<section class="main-content">
	<div class="ov-wrap">
		<!-- floorOne -->
		<div class="sec-floor-one">
			<ul class="list-unstyled">
				<li><label>上次登录 IP：</label><span>172.18.10.5</span></li>
				<li><label>上次登录时间：</label><span>2015年11月25日 15:35</span></li>
			</ul>
		</div>
		<!-- floorTwo -->
		<div class="sec-floor-two">
			<div class="fb-row" style="margin-bottom: 1px;">
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-comptsug"></i>
					<span class="singlerow">投诉建议（<a class="comptsug" href="javascript:;">0</a>）</span>
				</div>
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-workorder"></i>
					<span class="singlerow">工单（<a class="workorder" href="javascript:;">2</a>）</span>
				</div>
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-cusconsul"></i>
					<span class="singlerow">客户咨询（<a class="cusconsul" href="javascript:;">63</a>）</span>
				</div>
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-invorqust"></i>
					<span class="singlerow">发票申请（<a class="invorqust" href="javascript:;">24</a>）</span>
				</div>
				<div class="fb-item">
					<i class="icon-facebook icon-facebook-filiappl"></i>
					<span class="singlerow">备案申请（<a class="filiappl" href="javascript:;">0</a>）</span>
				</div>
			</div>
			<div class="fb-row">
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-eggadd"></i>
					<span class="singlerow">蛋壳+（<a class="eggadd" href="javascript:;">4</a>）</span>
				</div>
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-cudmirrappl"></i>
					<span class="singlerow">云镜像申请（<a class="cudmirrappl" href="javascript:;">0</a>）</span>
				</div>
				<div class="fb-item fb-item-sidebar">
					<i class="icon-facebook icon-facebook-endtime"></i>
					<div class="term">
						<label>已到期</label><br />
						<span><b>客户：</b><a class="cuspro" href="javascript:;">22</a><b class="mf15">产品：</b><a class="cuspro" href="javascript:;">36</a></span>
					</div>
				</div>
				<div class="fb-item fb-itemlast">
					<i class="icon-facebook icon-facebook-sevenrev"></i>
					<div class="term">
						<label>七天内到期</label><br />
						<span><b>客户：</b><a class="cuspro" href="javascript:;">22</a><b class="mf15">产品：</b><a class="cuspro" href="javascript:;">36</a></span>
					</div>
				</div>
			</div>
		</div>
		<!-- floorThree -->
		<div class="sec-floor-three">
			<div class="chart-list">
				<div class="chart-item">
					<div class="c-box-tit">
						<h4>营收</h4>
						<ul class="list-unstyled">
							<li>最近7天</li>
							<li><label>充值营收：</label><span class="c-257">¥ 2563.20</span></li>
							<li><label>扣费营收：</label><span class="c-257">¥ 56565.00</span></li>
						</ul>
						<p><label>累计总额：</label><span class="c-257">¥ 32323.00</span></p>
					</div>
					<div class="chart-show">
						<img src="<%=request.getContextPath()%>/newoperator/img/revenue-show.png" alt="营收" width="293" height="161"/>
					</div>
					<div class="botm-tips">
						<ul class="list-unstyled">
							<li><i class="icon-tips icon-tips-one"></i><label>充值营收</label></li>
							<li class="mf20"><i class="icon-tips icon-tips-two"></i><label>扣费营收</label></li>
						</ul>
					</div>
				</div>
				<div class="chart-item chart-middle">
					<div class="c-box-tit">
						<h4>客户</h4>
						<ul class="list-unstyled">
							<li>最近7天</li>
							<li><label>新增客户：</label><a class="cumpro" href="javascript:;">¥ 1350</a></li>
						</ul>
						<p><label>客户总数：</label><span class="c-257">15464</span></p>
					</div>
					<div class="chart-show">
						<img src="<%=request.getContextPath()%>/newoperator/img/customer-show.png" alt="客户" width="256" height="161"/>
					</div>
					<div class="botm-tips">
						<ul class="list-unstyled">
							<li><i class="icon-tips icon-tips-one"></i><label>新增客户</label></li>
						</ul>
					</div>
				</div>
				<div class="chart-item">
					<div class="c-box-tit">
						<h4>产品</h4>
						<ul class="list-unstyled">
							<li>最近7天</li>
							<li><label>新增产品：</label><a class="cumpro" href="javascript:;">14</a></li>
						</ul>
						<p><label>产品总数：</label><span class="c-257">157</span></p>
					</div>
					<div class="chart-show">
						<img src="<%=request.getContextPath()%>/newoperator/img/product-show.png" alt="产品" width="256" height="161"/>
					</div>
					<div class="botm-tips">
						<ul class="list-unstyled">
							<li><i class="icon-tips icon-tips-one"></i><label>云主机</label></li>
							<li class="mf20"><i class="icon-tips icon-tips-two"></i><label>云硬盘</label></li>
							<li class="mf20"><i class="icon-tips icon-tips-three"></i><label>专属云</label></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- floorFour -->
		<div class="sec-floor-four">
			<div class="sysmontrfault-list">
				<div class="sysmontr-item">
					<div class="sysmontr-tit">
						<h4>计算资源使用率</h4>
						<p class="c-4db"><label>使用状态：</label><span>良好</span></p>
					</div>
					<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/cmpt-resource.png" alt="计算资源使用率" width="120" height="120" /></div>
				</div>
				<div class="sysmontr-item">
					<div class="sysmontr-tit">
						<h4>存储资源使用率</h4>
						<p class="c-ffb"><label>使用状态：</label><span>告警</span></p>
					</div>
					<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/storage-resource.png" alt="存储资源使用率" width="120" height="120" /></div>
				</div>
				<div class="sysmontr-item">
					<div class="sysmontr-tit">
						<h4>地址资源使用率</h4>
						<p class="c-ff8"><label>使用状态：</label><span>危险</span></p>
					</div>
					<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/address-resource.png" alt="地址资源使用率" width="120" height="120" /></div>
				</div>
				<div class="fault-info">
					<div class="flt-list">
						<div class="flt-item">
							<div class="flt-left-cont">
								<h4>平台故障</h4>
								<p class="c-ff8"><label>故障数：</label><span>5</span></p>
							</div>
							<div class="flt-right-icon"><a class="icon-fault" href="javascript:;"></a></div>
						</div>
						<div class="flt-item">
							<div class="flt-left-cont">
								<h4>产品故障</h4>
								<p class="c-ff8"><label>故障数：</label><span>14</span></p>
							</div>
							<div class="flt-right-icon"><a class="icon-fault" href="javascript:;"></a></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/oper_common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/operation/js/oper_overview.js"></script>
</body>
</html>