<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.AgentVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String all = (String)request.getAttribute("all"); 
	AgentVO vo = (AgentVO)request.getAttribute("agentVO");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>"); 
$(document).ready(function(){
	init(3);
	inituser("<%=loginInfo.getAccount()%>",0);
	
	$("#consumption_list_show_btn").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=rechargePage";
	});
	$("#recharge_right_now").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=rechargePage";
	});
	
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=managePageForAgent";
	});
	$("#changeemail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=baseInfoPageEmailEdit";
	});
	$("#changephone").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=baseInfoPagePhoneEdit";
	});	
	$("#changepassword").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=updatePasswordPage";
	});
});

$(function(){
	var pager = $('#recharge_record_list').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
});


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
	}else{
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
  <div class="header">
     <div class="top">
	   <a class="logo l" href="#"><img src="<%=request.getContextPath()%>/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a>
	   <div class="nav l">
	    <a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />业务信息</a>
	    <a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />用户管理</a>
	    <a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />我的账户</a>
	    <a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />操作日志</a>
	   </div>
	   <div class="user l">
	    <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	    <a id="logoutlink" href="javascript:void(0);">注销</a>
	    <span>|</span>
	    <a id="userlink" href="javascript:void(0);"></a>
	   </div>
	   <div class="clear"></div>
	  </div>
  </div>
  <div class="main">
    <div class="titlebar"><div class="links"><a href="#" class="active" id="base_info">基本信息</a>　｜　<a href="#" id="recharge">账户充值</a>　｜　<a href="#" id="invoice_manage">发票管理</a></div></div>
    <div style="width:920px; margin-top:30px;">
      <div class="sidebar l" style="height:516px">
        <div style="width:238px; height:20px; padding:15px 0 0 0; text-align:center"><b>余额</b></div>
        <div style="width:238px; height:40px; line-height:40px; padding:0 0 15px 0; font-size:36px; text-align:center"><b><%=vo.getAccountBalance() %></b></div>
        <a class="greenbutton" id="recharge_right_now" href="#" style="margin:0 auto; width:160px">立即充值</a>
        <div style="width:238px; padding:30px 0 0 0; border-top:solid 1px #b2b2b2;margin-top:15px;">
          <div class="accounttitle1">代理折扣</div>
          <div class="accounttitle2" style="margin-bottom:30px"><b><%=vo.getPercentOff().intValue() %>% Off</b></div>
          <div class="accounttitle1">邮箱 <a href="#" id="changeemail"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
          <div class="accounttitle2"><b><%=vo.getEmail() %></b></div>
          <div class="accounttitle1">手机 <a href="#" id="changephone"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
          <div class="accounttitle2"><b><%=vo.getPhone() %></b></div>
          <div class="accounttitle1">密码 <a href="#" id="changepassword"><img src="<%=request.getContextPath()%>/image/icon_editbutton.png" width="20" height="20" alt="编辑" style="vertical-align:middle" /></a></div>
        </div>
      </div>
      <div class="r" style="width:650px;">
        <div class="titlebar" style="width:650px;">
          <div class="l links"><a href="#" id="consumption_list_show_btn">扣款记录</a>　｜　<a href="#" class="active">充值记录</a></div>
          <div class="r">充值总额<b>：<%=all %>元</b></div>
        </div>
        <table id="recharge_record_list" class="easyui-datagrid"  
          	data-options="
          			url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=accountBalanceService&method=queryRechargeRecord',
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
                <th data-options="field:'changeTime',width:175" formatter="timeFormatter">充值时间</th>
                <th data-options="field:'payType',width:175" formatter="typeFormatter">充值方式</th>
                <th data-options="field:'amount',width:100">金额(元)</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
       </div>
    </div>
    <div class="clear">&nbsp;</div>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html>