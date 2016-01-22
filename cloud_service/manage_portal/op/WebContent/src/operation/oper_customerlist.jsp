<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>运营平台－用户管理－客户列表</title>
	<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/src/common/img/favicon.ico" />
	<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/icon.css" />  --%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/ui.jqgrid.css" />
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
			<a class="oper-sty" href="oper_overview.jsp"><i class="icon-tit icon-operational-overview"></i>运营概览</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty current" href="javascritp:;" data-dropdown="1"><i class="icon-tit icon-user-management"></i>用户管理<i class="icon-arrow icon-arrow-right"></i></a>
			<ul class="list-unstyled submenu open">
				<li><a class="sub-sty current" href="oper_customerlist.jsp">客户列表</a></li>
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
<!-- mianContent -->
<section class="main-content">
	<div class="oper-wrap">
		<div class="oper-page-cont">
			<div class="page-head-info">
				<div class="layer-one"><a id="createNewuser" class="btn-sty" href="javascript:;"><i class="icon-create"></i>&nbsp;添加新用户</a></div>
				<div class="layer-two">
					<div class="condition">
						<label>客户类型：</label>
						<select class="form-control slct-sty">
							<option>全部</option>
							<option>客户类型</option>
							<option>致云员工</option>
							<option>商用</option>
						</select>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>折扣情况：</label>
						<input type="text" name="discountconditionstart" class="form-control txtipt-sty sty45" id="discountConditionStart" placeholder="¥"
						/><span class="icon-rowline"></span
						><input type="text" name="discountconditionend" class="form-control txtipt-sty sty45" id="discountConditionEnd" placeholder="¥"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>账户余额：</label>
						<input type="text" name="accountbalancestart" class="form-control txtipt-sty sty45" id="accountBalanceStart" placeholder="¥"
						/><span class="icon-rowline"></span
						><input type="text" name="accountbalanceend" class="form-control txtipt-sty sty45" id="accountBalanceEnd" placeholder="¥"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>累计扣费：</label>
						<input type="text" name="cumulativedeductionstart" class="form-control txtipt-sty sty45" id="cumulativeDeductionStart" placeholder="¥"
						/><span class="icon-rowline"></span
						><input type="text" name="cumulativedeductionend" class="form-control txtipt-sty sty45" id="cumulativeDeductionEnd" placeholder="¥"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>累计充值：</label>
						<input type="text" name="cumulativechargestart" class="form-control txtipt-sty sty45" id="cumulativechargeStart" placeholder="¥"
						/><span class="icon-rowline"></span
						><input type="text" name="cumulativechargeend" class="form-control txtipt-sty sty45" id="cumulativechargeEnd" placeholder="¥"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>状态：</label>
						<select class="form-control slct-sty">
							<option>全部</option>
							<option>正常</option>
							<option>禁用</option>
						</select>
					</div>
				</div>
				<div class="layer-three">
					<div class="condition">
						<label>创建时间：</label>
						<input type="text" name="creationtimestart" class="form-control txtipt-sty runcode lhg-calendar" id="creationTimeStart" readonly style="width:141px;" placeholder=""
						/><span class="icon-rowline"></span
						><input type="text" name="creationtimeend" class="form-control txtipt-sty runcode lhg-calendar" id="creationTimeEnd" readonly style="width:142px;" placeholder=""/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>用户名称：</label>
						<input type="text" name="username" class="form-control txtipt-sty" id="userName"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>注册邮箱：</label>
						<input type="text" name="registermail" class="form-control txtipt-sty" id="registermMail"/>
					</div
					><span class="icon-splitline"></span
					><div class="condition">
						<label>手机号码：</label>
						<input type="text" name="phonenumber" class="form-control txtipt-sty" id="phoneNumber"/>
					</div
					><span class="icon-splitline"></span
					><div class="search-btn-cont">
						<a class="shbtn-sty oper-search" href="javascript:;">搜索</a
						><a class="shbtn-sty oper-reset" href="javascript:;" style="margin-left:11px;">重置</a>
					</div>
				</div>
			</div>
			<div class="page-tab-info">
				<div class="tab-tit-cont">
					<div class="row">
						<div class="col-sm-6">
							<div class="batch-operation-box">
								<a class="bo-disable bo-sty current" href="javascripot:;"><i class="icon-disable mr05 icon-oper"></i>禁用</a>
								<a class="bo-recharge bo-sty mf20" href="javascripot:;"><i class="icon-recharge mr05 icon-oper"></i>充值</a>
								<a class="bo-customerclassification bo-sty mf20" href="javascripot:;"><i class="icon-customerclassification mr05 icon-oper"></i>客户归类</a>
								<a class="bo-grantvouchers bo-sty mf20" href="javascripot:;"><i class="icon-grantvouchers mr05 icon-oper"></i>发放代金券</a>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="export-data-box">
								<a class="ed-exportdata ed-sty" href="javascripot:;"><i class="icon-exportdata mr05 icon-oper"></i>导出</a>
							</div>
						</div>
					</div>
				</div>
				<div class="jqGrid-tab-cont">
					<div class="row">
						<div class="col-sm-12">
							<div class="jqGrid_wrapper">
								<table id="jqGrid" class="table-striped table-hover"></table>
					    			<div id="jqGridPager"></div>
							</div>
						</div>
					</div>
			    </div>
				<!-- <div class="tab-data-cont">
					<table id="tabDataGrid" class="easyui-datagrid" style="width:100%;"></table>
					<table id="tabDataGrid" class="table table-striped table-hover">
						<thead>
							<tr>
								<th>客户名称</th>
								<th>客户类型</th>
								<th>手机</th>
								<th>邮箱</th>
								<th>产品个数</th>
								<th>折扣率</th>
								<th>账户余额</th>
								<th>累计充值</th>
								<th>累计扣费</th>
								<th>创建时间</th>
								<th>最后操作时间</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
				    </table>
				</div> -->
			</div>
		</div>
	</div>
</section>
<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/jquery.min.js"></script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/easyui-lang-zh_CN.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/lhgcalendar.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/oper_common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/src/operation/js/oper_customerlist.js"></script>
</body>
</html>