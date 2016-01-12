<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.OperatorVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_OPERATOR;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	OperatorVO operator = (OperatorVO)request.getAttribute("operator");
	Integer suggestionCount = (Integer)request.getAttribute("suggestionCount");
	Integer invoiceCount = (Integer)request.getAttribute("invoiceCount");
%>
<!DOCTYPE html>
<!-- operator_index.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 运营概览</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/newoperator/common/css/global.css" media="all"/>
   	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/newoperator/css/operation.css" media="all"/>
</head>
	
<body>
<div class="ov-wrap" style="overflow:hidden;">
	<div class="sec-floor-one">
		<ul>
			<li>
				<label>上次登录 IP：</label>
				<span>172.18.10.5</span>
			</li>
			<li>
				<label>上次登录时间：</label>
				<span>2015年11月25日  15：35</span>
			</li>
		</ul>
	</div>
	<div class="sec-floor-two">
		<div class="fb-row" style="margin-bottom: 1px;">
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-comptsug"></i>
				<a class="comptsug" href="javascript:;">投诉建议（<label><%=suggestionCount %></label>）</a>
			</div>
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-workorder"></i>
				<a class="workorder" href="javascript:;">工单（<label>2</label>）</a>
			</div>
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-cusconsul"></i>
				<a class="cusconsul" href="javascript:;">客户咨询（<label>63</label>）</a>
			</div>
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-invorqust"></i>
				<a class="invorqust" href="javascript:;">发票申请（<label><%=invoiceCount %></label>）</a>
			</div>
			<div class="fb-item">
				<i class="icon-facebook icon-facebook-filiappl"></i>
				<a class="filiappl" href="javascript:;">备案申请（<label>0</label>）</a>
			</div>
		</div>
		<div class="fb-row">
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-eggadd"></i>
				<a class="eggadd" href="javascript:;">蛋壳+（<label>4</label>）</a>
			</div>
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-cudmirrappl"></i>
				<a class="cudmirrappl" href="javascript:;">云镜像申请（<label>0</label>）</a>
			</div>
			<div class="fb-item fb-item-sidebar">
				<i class="icon-facebook icon-facebook-endtime"></i>
				<div class="term">
					<label>已到期</label><br />
					<span><b>客户：</b><a class="cuspro" href="javascript:;">22</a><b class="f-ml15">产品：</b><a class="cuspro" href="javascript:;">36</a></span>
				</div>
			</div>
			<div class="fb-item fb-itemlast">
				<i class="icon-facebook icon-facebook-sevenrev"></i>
				<div class="term">
					<label>七天内到期</label><br />
					<span><b>客户：</b><a class="cuspro" href="javascript:;">22</a><b class="f-ml15">产品：</b><a class="cuspro" href="javascript:;">36</a></span>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-floor-three">
		<div class="chart-list">
			<div class="chart-item">
				<div class="c-box-tit">
					<h3>营收</h3>
					<ul>
						<li>最近7天</li>
						<li><label>充值营收：</label><span class="c-257">¥ 2563.20</span></li>
						<li><label>扣费营收：</label><span class="c-257">¥ 56565.00</span></li>
					</ul>
					<p><label>累计总额：</label><span class="c-257">¥ 32323.00</span></p>
				</div>
				<div class="chart-show"><img src="<%=request.getContextPath()%>/newoperator/img/revenue-show.png" alt="营收" width="293" height="161"/></div>
				<div class="botm-tips">
					<ul>
						<li><i class="icon-tips icon-tips-one"></i><label>充值营收</label></li>
						<li class="f-ml20"><i class="icon-tips icon-tips-two"></i><label>扣费营收</label></li>
					</ul>
				</div>
			</div>
			<div class="chart-item chart-middle">
				<div class="c-box-tit">
					<h3>客户</h3>
					<ul>
						<li>最近7天</li>
						<li><label>新增客户：</label><a class="cumpro" href="javascript:;">¥ 1350</a></li>
					</ul>
					<p><label>客户总数：</label><span class="c-257">15464</span></p>
				</div>
				<div class="chart-show"><img src="<%=request.getContextPath()%>/newoperator/img/customer-show.png" alt="客户" width="256" height="161"/></div>
				<div class="botm-tips">
					<ul>
						<li><i class="icon-tips icon-tips-one"></i><label>新增客户</label></li>
					</ul>
				</div>
			</div>
			<div class="chart-item">
				<div class="c-box-tit">
					<h3>产品</h3>
					<ul>
						<li>最近7天</li>
						<li><label>新增产品：</label><a class="cumpro" href="javascript:;">14</a></li>
					</ul>
					<p><label>产品总数：</label><span class="c-257">157</span></p>
				</div>
				<div class="chart-show"><img src="<%=request.getContextPath()%>/newoperator/img/product-show.png" alt="产品" width="256" height="161"/></div>
				<div class="botm-tips">
					<ul>
						<li><i class="icon-tips icon-tips-one"></i><label>云主机</label></li>
						<li class="f-ml20"><i class="icon-tips icon-tips-two"></i><label>云硬盘</label></li>
						<li class="f-ml20"><i class="icon-tips icon-tips-three"></i><label>专属云</label></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-floor-four">
		<div class="sysmontrfault-list">
			<div class="sysmontr-item">
				<div class="sysmontr-tit">
					<h5>计算资源使用率</h5>
					<p class="c-4db"><label>使用状态：</label><span>良好</span></p>
				</div>
				<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/cmpt-resource.png" alt="计算资源使用率" width="120" height="120" /></div>
			</div>
			<div class="sysmontr-item">
				<div class="sysmontr-tit">
					<h5>存储资源使用率</h5>
					<p class="c-ffb"><label>使用状态：</label><span>告警</span></p>
				</div>
				<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/storage-resource.png" alt="存储资源使用率" width="120" height="120" /></div>
			</div>
			<div class="sysmontr-item">
				<div class="sysmontr-tit">
					<h5>地址资源使用率</h5>
					<p class="c-ff8"><label>使用状态：</label><span>危险</span></p>
				</div>
				<div class="sysmontr-show"><img src="<%=request.getContextPath()%>/newoperator/img/address-resource.png" alt="地址资源使用率" width="120" height="120" /></div>
			</div>
			<div class="fault-info">
				<div class="flt-list">
					<div class="flt-item">
						<div class="flt-left-cont">
							<h5>平台故障</h5>
							<p class="c-ff8"><label>故障数：</label><span>5</span></p>
						</div>
						<div class="flt-right-icon"><a class="icon-fault" href="javascript:;"></a></div>
					</div>
					<div class="flt-item">
						<div class="flt-left-cont">
							<h5>产品故障</h5>
							<p class="c-ff8"><label>故障数：</label><span>14</span></p>
						</div>
						<div class="flt-right-icon"><a class="icon-fault" href="javascript:;"></a></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
// 	window.onload=function(){
// 		$(window.parent.document).find("#content_frame").load(function(){
// 			var main = $(window.parent.document).find("#content_frame");
// 			var thisheight = $('.ov-wrap').height();
// 			main.height(thisheight);
// 		});
// 	}
</script>
<!-- JavaScript_end -->
</body>
</html>