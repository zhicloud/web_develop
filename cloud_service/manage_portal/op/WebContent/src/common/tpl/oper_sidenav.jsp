<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<nav class="sidebar">
	<div class="nav-list">
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="oper_overview.jsp" data-state="0"><i class="icon-tit icon-operational-overview"></i>运营概览</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-user-management"></i>用户管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="oper_customerlist.jsp">客户列表</a></li>
				<li><a class="sub-sty" href="oper_customertype.jsp">客户类型</a></li>
				<li><a class="sub-sty" href="oper_operationlog.jsp">操作日志</a></li>
				<li><a class="sub-sty" href="oper_consumptionrecord.jsp">消费记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-product-management"></i>产品管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">自用云主机</a></li>
				<li><a class="sub-sty" href="javascritp:;">云服务器管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">用户专属云管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">云硬盘管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-package-management"></i>套餐管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">套餐项管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">套餐价格自定义</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-resource-management"></i>资源池管理<i class="icon-arrow icon-arrow-right"></i></a>
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
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="0"><i class="icon-tit icon-routing-management"></i>智能路由管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-whmirror-management"></i>仓库和镜像管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">系统镜像管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">仓库类型管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-statistical-analysis"></i>统计分析<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">报表统计</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-financial-management"></i>财务管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">现金券管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发票管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-system-management"></i>系统管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">基本信息</a></li>
				<li><a class="sub-sty" href="javascritp:;">修改密码</a></li>
				<li><a class="sub-sty" href="javascritp:;">意见反馈</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-mail-management"></i>邮件管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">配置管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">模块管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-mesg-management"></i>短信管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu">
				<li><a class="sub-sty" href="javascritp:;">配置管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">模块管理</a></li>
				<li><a class="sub-sty" href="javascritp:;">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="0"><i class="icon-tit icon-eggplan-management"></i>蛋壳计划管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="0"><i class="icon-tit icon-mirrorhost-management"></i>镜像主机申请管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty menuParentItem" href="javascritp:;" data-state="1"><i class="icon-tit icon-monitinfor-management"></i>监控信息管理<i class="icon-arrow icon-arrow-right"></i></a>
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