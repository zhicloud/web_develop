<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String all = (String)request.getAttribute("all");
	String balance = (String)request.getAttribute("balance");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
<script src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	init(10,4);
	inituser('<%=loginInfo.getAccount()%>',0);  
	$("#recharge_list_show_btn").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargeRecordPage";
	});
	$("#consumption_list_show_btn").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage";
	});
	$("#recharge_right_now").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage";
	});
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=invoiceService&method=managePage";
	});
	$("#create_sever").click(function(){
		window.location.href="<%=request.getContextPath()%>/buy.do?flag=buy";
	});
	$("#cash_coupon").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cashCouponService&method=cashCouponPage";
	});
	$("#changepassword").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=terminalUserService&method=changePasswordPage";
	});
	$("#changeemail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=terminalUserService&method=baseInfoPageEmailEdit";
	});
	$("#changephone").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=terminalUserService&method=baseInfoPagePhoneEdit";
	});
});

$(function(){
	var pager = $('#consumption_record_list').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
});
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;


// 布局初始化
$("#consumption_record_list").height( $(document.body).height());

function typeFormatter(val, row)
{  
	if(val == 1){  
	    return "支付宝";  
	}else if(val == 2) {
	    return "银联";  
	}else if(val == 3){
		return "系统赠送";
	}else if(val == 5){
		return "兑换现金券";
	}
	else{
	 	return "系统扣费";
	}
}
function timeFormatter(val, row)
{ 
	if(val==null||val==''){
		return "";
	}
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
	
}
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
        <div class="tabbar l"><a href="#" class="active" id="base_info">基本信息</a>　｜　<a href="#" id="recharge">账户充值</a>　｜　<a href="#" id="cash_coupon">现金券</a>　｜　<a href="#" id="invoice_manage">发票管理</a></div>
        <a href="#" class="greenbutton r" id="create_sever">创建主机</a></div>
      <div class="box" style="margin-top:30px;">
        <div class="sidebar l" style="height:516px">
          <div style="width:238px; height:20px; padding:15px 0 0 0; text-align:center"><b>余额</b></div>
          <div style="width:238px; height:40px; line-height:40px; padding:0 0 15px 0; font-size:36px; text-align:center"><b><%=balance %></b></div>
          <a class="greenbutton" href="#" style="margin:0 auto; width:160px" id="recharge_right_now">立即充值</a>
          <div style="width:238px; padding:30px 0 0 0; border-top:solid 1px #b2b2b2;margin-top:15px;">
            <div class="accounttitle1">邮箱 <a href="#" id="changeemail"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
            <div class="accounttitle2"><b><%=loginInfo.getAccount()%></b></div>
            <div class="accounttitle1">手机 <a href="#" id="changephone"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
            <div class="accounttitle2"><b><%=loginInfo.getPhone()%></b></div>
            <div class="accounttitle1">密码 <a href="#" id="changepassword"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
          </div>
        </div>
        <div class="r" style="width:450px;">
          <div class="titlebar2" style="text-align:right">
            <div class="tabbar l"><a href="#" class="active" id="consumption_list_show_btn">消费记录</a>　｜　<a href="#" id="recharge_list_show_btn">充值记录</a></div>实际消费总额<b>：<%=all %>元</b></div>
          
         <div id="consumption_record_1">
          <table id="consumption_record_list" class="easyui-datagrid"
          	data-options="
          			url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=accountBalanceService&method=queryConsumptionRecord',
          			queryParams: {},
					border:false,
					singleSelect:true,
					scrollbarSize:0,
					pagination:true,
					toolbar: '#toolbar',
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10
          				">
            <thead>
              <tr>
                <th data-options="field:'changeTime',width:130" formatter="timeFormatter">消费时间</th>
                <th data-options="field:'payType',width:58" formatter="typeFormatter">消费方式</th>
                <th data-options="field:'resourceName',width:218">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;资源名</th>
                <th data-options="field:'amount',width:55">金额(元)</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
          </div>
          
          
          
        </div>
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
</body>
</html>