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
<style type="text/css">
.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber {
text-align: center;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>

<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){  
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
    <jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
		<div class="main">
			<div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
      <div class="titlebar">
        <div class="tabbar l"><a href="javascript:void(0);" class="active" id="base_info">基本信息</a>　｜　<a href="javascript:void(0);" id="recharge">账户充值</a>　｜　<a href="javascript:void(0);" id="cash_coupon">现金券</a>　｜　<a href="javascript:void(0);" id="invoice_manage">发票管理</a></div>
        <a href="javascript:void(0);" class="greenbutton r" id="create_sever">创建主机</a></div>
      <div class="box" style="margin-top:30px;">
        <div class="sidebar l" style="height:516px">
          <div style="width:238px; height:20px; padding:15px 0 0 0; text-align:center"><b>余额</b></div>
          <div style="width:238px; height:40px; line-height:40px; padding:0 0 15px 0; font-size:36px; text-align:center"><b><%=balance %></b></div>
          <a class="greenbutton" href="javascript:void(0);" style="margin:0 auto; width:160px" id="recharge_right_now">立即充值</a>
          <div style="width:238px; padding:30px 0 0 0; border-top:solid 1px #b2b2b2;margin-top:15px;">
            <div class="accounttitle1">邮箱 <a href="javascript:void(0);" id="changeemail"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
            <div class="accounttitle2"><b><%=loginInfo.getAccount()%></b></div>
            <div class="accounttitle1">手机 <a href="javascript:void(0);" id="changephone"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
            <div class="accounttitle2"><b><%=loginInfo.getPhone()%></b></div>
            <div class="accounttitle1">密码 <a href="javascript:void(0);" id="changepassword"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
          </div>
        </div>
        <div class="r" style="width:720px;">
          <div class="titlebar2" style="text-align:right">
            <div class="tabbar l"><a href="javascript:void(0);" class="active" id="consumption_list_show_btn">消费记录</a>　｜　<a href="javascript:void(0);" id="recharge_list_show_btn">充值记录</a></div>实际消费总额<b>：<%=all %>元</b></div>
          
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
                <th data-options="field:'resourceName',width:218">资源名</th>
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
        		<jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>

  </div>
  <div class="pageright">
	<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	navHighlight("umc","uma");  
});
</script>
</body>
</html>