<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.AgentVO" %>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	AgentVO agent = (AgentVO)request.getAttribute("agent");
	String totalFee = (String) request.getAttribute("total_fee");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title> 
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>");
$(document).ready(function(){
	init(3);
	inituser("<%=loginInfo.getAccount()%>",0);
	
	$("input[name='total_fee']").click(function(){ 
		$("#re").next("label").html("其他金额");
		$("#re").next("label").removeClass("checked");
		$(this).next("label").addClass("checked"); 
		var gift =parseInt($(this).val()/1000);
		if(gift<=0){
			$("#info").html("充值"+$(this).val()+"元");
		}else{
			$("#info").html("充值"+$(this).val()+"元，赠送"+parseInt($(this).val()/1000)*200+"元");
			
		}
	});
	$("#re").click(function(){
		$("#re1").removeAttr("checked");
		$("#re1").next("label").removeClass("checked");
		$("#re2").removeAttr("checked");
		$("#re2").next("label").removeClass("checked");
		$("#re3").removeAttr("checked");
		$("#re3").next("label").removeClass("checked");
		$("#re4").removeAttr("checked");
		$("#re4").next("label").removeClass("checked");
		$("#re5").removeAttr("checked");
		$("#re5").next("label").removeClass("checked");
	});
	$("#re").blur(function(){
		if($(this).val()!=''){	
			if(checkMoney()){				
				$("#rh1").next("label").removeClass("checked");
				$("#rh2").next("label").removeClass("checked");
				$("#rh3").next("label").removeClass("checked");  
				$("#rh").next("label").addClass("checked");
				var gift =parseInt($(this).val()/1000);
				if(gift<=0){
					$("#info").html("充值"+$(this).val()+"元");
				}else{
					$("#info").html("充值"+$(this).val()+"元，赠送"+parseInt($(this).val()/1000)*200+"元");
					
				}
			}
		}else{
			$("#re1").click();
			$("#re1").next("label").addClass("checked");
		}
		
	});
	$("#toPay").click(function(){
		gotoPay();
	});
	
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=rechargePage";
	}); 
	
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=managePageForAgent";
	});
	var totalFee = '<%=totalFee%>';
	if(totalFee=='1000'){
		$("#re3").click();
	}else if(totalFee=='2000'){
		$("#re4").click();		
	}else if(totalFee=='5000'){
		$("#re5").click();		
	}else if(totalFee!=null&&totalFee.length>0){
		$("#re").val(totalFee);
		$("#re").click();
		$("#re").next("label").addClass("checked");
		$("#re").next("label").html(totalFee+"元");
	}
});

function checkMoney(){  
	if($("#re").next("label").attr("class").indexOf('checked')>0){
		var patrn=/^\d+$/;
		if(!(patrn.test($("#re").val()))){
			top.$.messager.alert('警告','请输入整数','warning'); 
			return false;
		}else if($("#re").val()<10){
			top.$.messager.alert('警告','充值金额应大于10','warning'); 
			return false;
			
		}else if($("#re").val()>50000){
			top.$.messager.alert('警告','充值金额应小于50000','warning');	 
			return false;
		}else{
			return true;
		} 
		
	}else{
		return true;
	}
} 

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");


 function gotoPay(){  
	 var val=$('input:radio[name="payType"]:checked').val();
	 if(val == null){
		 top.$.messager.alert('警告','请选择支付方式！','warning');
	 } else{
		 if(checkMoney()){
			 
		  $("#pay_form").submit();
		 }
	 }
	 
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
	   		<a href="#" id="business">
				<img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />
				<label>业务信息</label>
			</a>
			<a href="#" id="user_manage">
				<img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />
				<label>用户管理</label>
			</a>
			<a href="#" id="my_account">
				<img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />
				<label>我的账户</label>
			</a>
			<a href="#" id="oper_log">
				<img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />
				<label>操作日志</label>
			</a>
			<a href="#" id="user_manual">
				<img id="agent_nav_5" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_5_i.png" width="21" height="21"/>
				<label>使用手册</label>
			</a>
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
    <div class="titlebar"><div class="links"><a href="#" id="base_info" >基本信息</a>　｜　<a href="#" id="recharge" class="active">账户充值</a>　｜　<a href="#" id="invoice_manage">发票管理</a></div></div>
    <div style="width:920px; margin-top:30px;">
        <form id="pay_form" action="<%=request.getContextPath()%>/bean/page.do" method="post"  target="_blank">
          <input name="email" value="mali@zhicloud.com" type="hidden"/> 
          <input name="WIDsubject" value="云主机" type="hidden"/> 
          <input name="userId" value="<%=loginInfo.getUserId()%>" type="hidden"/> 
          <input name="userType" value="<%=userType %>" type="hidden"/>
          <input name="bean" value="paymentService" type="hidden"/>
          <input name="method" value="toAccountPage" type="hidden"/> 
        <div style="width:492px; height:197px; margin:0 auto">
          <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 25px 0">余额</span></div>
          <div class="l" style="width:372px; margin:0 0 25px 0; line-height:30px"><%=agent.getAccountBalance() %>元</div>
          <div class="l" style="width:115px;"><span class="bluebutton" style="width:105px;margin:0 10px 25px 0">充值金额</span></div>
          <div class="l" style="width:377px; margin:0 0 10px 0;">
            <input name="total_fee" id="re1" class="radio" type="radio" value="200" checked="checked" style="width:53px"/>
              <label for="re1" class="smalllabel l" style="width:53px">200元</label>
              <input name="total_fee" id="re2" class="radio" type="radio" value="500"  style="width:53px"/>
              <label for="re2" class="smalllabel l" style="width:53px">500元</label>
              <input name="total_fee" id="re3" class="radio" type="radio" value="1000"  style="width:53px"/>
              <label for="re3" class="smalllabel l" style="width:53px">1000元</label>
              <input name="total_fee" id="re4" class="radio" type="radio" value="2000"  style="width:54px"/>
              <label for="re4" class="smalllabel l" style="width:54px">2000元</label>
              <input name="total_fee" id="re5" class="radio" type="radio" value="5000"  style="width:54px"/>
              <label for="re5" class="smalllabel l" style="width:54px">5000元</label>
              <div class="custom smalllabel l" style="border:none; margin:0; width:68px">
                <input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="5" id="re" name="total_fee_diy" type="text" class="smalllabel" title="元" style="width:68px"/>
                <label for="re" class="smalllabel l" style="width:68px">其他金额</label>
              </div>
          </div>
          <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 27px 0">充值方式</span></div>
          <div class="l" style="width:372px; margin:0 0 25px 0; line-height:30px">
            <input checked="checked" id="pm1" name="payType" type="radio" value="1" style="vertical-align:middle" />
            <label for="pm1"><img src="<%=request.getContextPath()%>/image/alipay.png" width="70" height="30" alt="支付宝"  style="vertical-align:middle" /></label>
            <input id="pm2" name="payType" type="radio" value="2" style="vertical-align:middle" />
            <label for="pm2"><img src="<%=request.getContextPath()%>/image/yinlian.png" width="60" height="30" alt="银联"  style="vertical-align:middle" /></label>
           </div>
          <a href="#" class="bluelinebutton r" id="toPay">前往支付</a>
        </div>
        </form>
      
    </div>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html>