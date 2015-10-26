<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<SysGroupVO> sysGroupList = (List<SysGroupVO>)request.getAttribute("sysGroupList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/big.min.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
ajax.async = false;
$(document).ready(function(){
	init(2);
	inituser("<%=loginInfo.getAccount()%>",0);
	
	$("#save_user_btn").click(function(){
		var flag = $("#save_user_btn").attr("flag");
		console.info(flag);
		if(flag==2){
			return;
		}
		$("#save_user_btn").attr("flag","2");
		if(!(checkAccount() && checkPhone())){
			$("#save_user_btn").attr("flag","1");
			return;
		}
		var result = null;
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:addTerminalUser", 
			[ formData ],
			function(reply) {
				result = new String(reply.result.message);
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					$("#account").val("");
					$("#phone").val("");
					$("#agent-tip-account").html("");
					$("#agent-tip-phone").html("");
					top.$.messager.alert('提示','添加成功','info');
				} else {
					if(result.endsWith("账号")){
						$("#agent-tip-phone").html(reply.result.message);
					}else{
						$("#agent-tip-account").html(reply.result.message);
					}
//						top.$.messager.alert('警告',reply.result.message,'warning');
//						$("#agent-tip-account").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
		$("#save_user_btn").attr("flag","1");
	});
});
//检查用户名
function checkAccount(){
	var account = new String($("#account").val()).trim();
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(account==null || account==""){
		$("#agent-tip-account").html("邮箱不能为空");
		return false;
	}
	if(!myreg.test(account)){
		$("#agent-tip-account").html("邮箱格式不正确");
		return false;
	}
	if(account.length>30){
		$("#agent-tip-account").html("输入不能超过30个字符");
		return false;
	}
	$("#agent-tip-account").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
//检查手机号码
function checkPhone(){
	var phone = new String($("#phone").val());
	if(phone==null || phone==""){
		$("#agent-tip-phone").html("手机号码不能为空");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#agent-tip-phone").html("请输入正确的手机号码");
		return false;
	}
	$("#agent-tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
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
    <div class="titlebar">
      <div class="blocks l">
      <div class="l"><a href="javascript:void(0);" onclick="self.location=document.referrer;""><img src="<%=request.getContextPath() %>/image/button_back.png" width="22" height="30" alt="返回" /></a></div>
      <div class="clear"></div>
      </div>
      <div class="blocks r">添加用户</div>
      <div class="clear"></div>
    </div>
    <div style="width:920px; margin-top:30px;">
        <div style="width: 375px;margin: 0 auto;line-height:30px;">
        <form id="terminal_user_add_dlg_form">
        <table width="375" border="0" cellspacing="0">
          <tr>
   　         <td width="105" style="padding:5px"><span class="bluebutton" style="width:105px;">邮箱</span></td>
            <td width="270" style="padding:5px"><input id="account" name="account" type="text" class="textbox" style="height:28px; width:268px" onblur="checkAccount()"/></td>
          </tr>
          <tr>
            <td style="padding:0; line-height:16px">&nbsp;</td>
            <td id="agent-tip-account" style="text-align:right; padding:0 5px; line-height:16px;color:#f06050"></td>
          </tr>
          <tr>
   　         <td width="105" style="padding:5px"><span class="bluebutton" style="width:105px;">手机</span></td>
            <td width="270" style="padding:5px"><input id="phone" name="phone" type="text" class="textbox" style="height:28px; width:268px" onblur="checkPhone()"/></td>
          </tr>
          <tr>
            <td style="padding:0; line-height:16px">&nbsp;</td>
            <td id="agent-tip-phone" style="text-align:right; padding:0 5px; line-height:16px;color:#f06050"></td>
          </tr>
        </table>
          
          <a id="save_user_btn" href="#" flag="1" class="bluelinebutton" style="margin:15px 0 0 120px;width:150px">添加用户</a>
          </form>
        </div>
      
    </div>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html>