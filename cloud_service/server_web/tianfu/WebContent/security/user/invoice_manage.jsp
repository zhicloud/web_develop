<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import = "java.math.BigInteger "%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userId = loginInfo.getUserId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- invoice_manage.jsp -->
<head>
<!-- 		<meta charset="UTF-8" /> -->
<!-- 		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	init(10,4);
	if(name!= ''){ 
	   inituser(name,0);
	}else{
		
	   inituser();
	}   
	initstep(1);
	
	// 按 id 查询对应的用户的发票
	var queryParams = {};
	queryParams.user_id = '<%=userId%>';
	$('#invoice_datagrid').datagrid({
		"queryParams": queryParams
	});
	
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=invoiceService&method=managePage";
	});
	$("#cash_coupon").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cashCouponService&method=cashCouponPage";
	});
});

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

window.name = "selfWin";

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;


// 布局初始化
$("#invoice_datagrid").height( $(document.body).height());
 

function statusFormatter(val)	
{   
	if(val == 2 || val == 4){
		return "审核中";
	}else if(val == 3){
		return "已寄送";
	}
	else if(val == 5){
		return "寄送完成";
	}
}  

function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	
	
}

function timeFormat(val)	
{   
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}  

$(function(){
	//索取发票
	$("#add_invoice_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=addInvoicePage",
			onClose: function(data){
				$('#invoice_datagrid').datagrid('reload');
			}
		});
	});
	//邮寄地址管理
	$("#invoice_address_manage_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceAddressService&method=managePage",
			onClose: function(data){
				$('#invoice_datagrid').datagrid('reload');
			}
		});
	});
	
})



</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
	</head>
	<body>
	<div class="page">
  		<div class="pageleft">
    	<div class="header"> 
		   <div class="top"> 
		    <a class="logo l" href="<%=request.getContextPath()%>/"><img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a> 
		    <div id="beforelogin" class="user r"> 
		     <a id="loginlink" href="javascript:void(0);" class="graylink">登录</a>
		     <span>|</span> 
		     <a id="reglink" href="javascript:void(0);">注册</a> 
		    </div> 
		    <div id="afterlogin" class="user r" style="display:none;">
		     <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
		     <a id="logoutlink" href="javascript:void(0);">注销</a>
		     <span>|</span>
		     <a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
		    </div>
		    <div class="nav r">
		     <a href="<%=request.getContextPath()%>/" style="background:transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" /> </a>
		     <a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
		     <a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
		     <a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
		     <a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
		     <a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
		     <a href="#" style="display:none"></a>
		     <a href="<%=request.getContextPath()%>/user.do?flag=login" style="display:none"></a>
		     <a href="#" style="display:none"></a>
		     <a href="#" style="display:none">我的云端</a>
		    </div>
		   </div>
		   <div class="subnav">
		    <div class="box">
		     1
		    </div>
		    <div class="box">
		     2
		    </div>
		    <div class="box">
		     3
		    </div>
		    <div class="box">
		     4
		    </div>
		    <div class="box">
		     5
		    </div>
		    <div class="box">
		     6
		    </div>
		    <div class="box">
		     7
		    </div>
		    <div class="box">
		     8
		    </div>
		    <div class="box">
		     9
		    </div>
		    <div class="box">
		     <a id="overview" onclick="onSwitch(this);" href="#"><img id="nav_10_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a>
		     <a id="my_cloud_host_link" onclick="onSwitch(this);" href="#"><img id="nav_10_2" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a>
		     <a href="#" id="my_cloud_disk_link" onclick="onSwitch(this);"><img id="nav_10_3" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a>
		     <a href="#" id="recharge_record" onclick="onSwitch(this);"><img id="nav_10_4" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a>
		     <a href="#" id="oper_log" onclick="onSwitch(this);"><img id="nav_10_5" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a>
		     <a href="#" id="suggestion" onclick="onSwitch(this);"><img id="nav_10_6" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a>
		     <a href="#" id="my_uploaded_file_link" onclick="onSwitch(this);"><img id="nav_10_7" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
		    </div>
		   </div> 
		</div>
    	<div class="main">
      <div class="titlebar">
        <div class="tabbar l"><a href="#" id="base_info">基本信息</a>　｜　<a href="#" id="recharge">账户充值</a>　｜　<a href="#" id="cash_coupon">现金券</a>　｜　<a href="#" class="active">发票管理</a></div>
      </div>
      <div class="box" style="padding: 30px 0 0 0;">
      <div class="titlebar">
      <div class="l">已开发票</div>
        <a href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=addInvoicePage" class="greenbutton r">索取发票</a>
		</div>
			<table id="invoice_datagrid" class="easyui-datagrid" 
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceService&method=queryInvoice',
					queryParams: {},
					toolbar: '#toolbar',
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="field:'totalAmount',width:140" >金额（元）</th>
						<th data-options="field:'invoiceTitle',width:380">发票抬头</th>
						<th data-options="field:'submitTime',width:150" formatter= "timeFormat"sortable=true>提交时间</th>
						<th data-options="field:'status',width:100" formatter="statusFormatter">状态</th>
					</tr>
				</thead>
			</table>
		 </div>
      <div class="clear">&nbsp;</div>
    </div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a><br /> 
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.tianfusoftwarepark.com" target="_blank">成都天府软件园有限公司</a>, All rights reserved.
				蜀ICP备11001370号-3
			</div>
		</div> 
	</div>
  </div>
		<div class="pageright">
		     <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    		 <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
 		 </div>
 	</div>
 	<script type="text/javascript">
		$(function(){
		var pager = $('#invoice_datagrid').datagrid('getPager');
		pager.pagination({
			showPageList: false,
			showRefresh: false,
			displayMsg: '',
			beforePageText: '',
			afterPageText: ''
		 });            
	})
</script>
</body>
</html>