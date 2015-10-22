<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
	String userId  = "";
	String userName = "";
	if(loginInfo != null){
		userId = loginInfo.getUserId();
		userName = loginInfo.getAccount();
	}
	String terminalUserId = (String)request.getAttribute("terminalUserId");
	String balance = (String)request.getAttribute("balance");
	String diskName = (String)request.getAttribute("diskName");
	BigDecimal totalPrice = (BigDecimal)request.getAttribute("totalPrice");
	if(balance==null){
		balance="0";
	}
	if(totalPrice==null){
		totalPrice=new BigDecimal("0");
	}
	if(diskName==null||diskName==""){
		diskName = "未分配";
	}
	String cloudDiskMin = AppProperties.getValue("cloudDiskMin","");
	String cloudDiskMax = AppProperties.getValue("cloudDiskMax","");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
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
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/common.js"></script>
<script type="text/javascript">
var totalPrice='<%=totalPrice%>';
var balance='<%=balance%>';
var diskName = '<%=diskName%>';
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
var userName = "<%=userName%>";
var userId = "";
var final_region = "1";
var cloudDiskMin = "<%=cloudDiskMin%>";
var cloudDiskMax = "<%=cloudDiskMax%>";
var diskMin = parseInt(cloudDiskMin);
var diskMax = parseInt(cloudDiskMax);
$(document).ready(function(){
	init();
	if(userName==""){
		inituser();
	}else{
		inituser(userName,0);
	}
	initstep(1);
	//---------
	var loginInfo = "<%=loginInfo%>";
// 	if(loginInfo == "null"){
// 		$("#btn_confirm").empty();
// 		$("#btn_confirm").append("登陆后确认");
// 	}
	//---------
	$("input[name='disk']").click(function(){ 
		$("#disk_other").next("label").html("自定义(G)");
		$("#disk_other").next("label").removeClass("checked");
		var disk = $(this).val();
		$("#disk_size_one").html(disk);
		$("#disk_size_two").html(disk);
	});
	//---------
	$("input[name='region']").click(function(){ 
		var region = $(this).val();
		final_region = region;
		var my_region = "";
		if(region == "1"){
			my_region = "广州 ";
		}else if(region == "4"){
			my_region = "香港";
		}
		$("#disk_region_one").html(my_region);
		$("#disk_region_two").html(my_region);
	});
	//---------
	$("#disk_other").focus(function(){
		$("input[name='disk']:checked").next("label").removeClass("checked");
		$("input[name='disk']:checked").removeAttr("checked");
		
	});
	$("#disk_other").blur(function(){
		var disk = $(this).val();
		if(disk == null || disk == "" || disk == undefined){
			$("#disk20").click();
			disk = 20;
		}else{
			var curDisk = parseInt(disk);
		}
		if(curDisk<diskMin || curDisk>diskMax){
			$("#disk_other").val("");
			$("#disk20").click();
			$("#disk_other").next("label").html("自定义(G)");
			$("#disk_other").next("label").removeClass("checked");
			top.$.messager.alert("提示","硬盘大小应在["+diskMin+"G-"+diskMax+"G]的范围内","info",function(){
				
			});
			return;
		}
		$("#disk_size_one").html(disk);
		$("#disk_size_two").html(disk);
	});
	$("#disk_invitation_code").blur(function(){
		checkDiskinvitationCode();
	});
	$("#add_cloud_disk_button").click(function(){
		if(userName==""){
			slideleft(1);
			event.stopPropagation();
		}else{
			save();
		}
	});
	//------
	$("#next_button").click(function(){
		if(userName!=""){
			ajax.remoteCall("bean://cloudDiskService:getCreateInfo", 
				[final_region],
				function(reply) {
					if (reply.status == "exception") {
						if(reply.errorCode=="RMC_1"){
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									slideleft(1);
							});
						}else{
							
							top.$.messager.alert('警告','获取信息失败','warning'); 
					  }
				    }
					else if (reply.result.status == "success") 
					{ 
						$("#disk_name").html(reply.result.properties.name);
						$("#new_disk_name").val(reply.result.properties.name);
					} 
					else 
					{
						top.$.messager.alert('警告',reply.result.message,'warning');
						}
					}
				); 
		}
	});
	
	$("#goRechargeRightNow").click(function(){
		var html = '<form id="gift_form" action="'+a+'/bean/page.do" method="post">'+
		'<input type="hidden" name="bean" value="accountBalanceService">'+          
		'<input type="hidden" name="method" value="toRechargeGiftPage">'+          
		'<input type="hidden" name="userType" value="4">'+          
		'<input type="hidden" name="month" value="'+month+'">'+          
		'</form>';
		$("#month_form").html(html);
		$("#gift_form").submit();
  	});
	$("#finish").click(function(){
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudDiskService&method=managePage";
	});
	$("input[name='month']").click(function(){ 
		$("#rh").next("label").html("自定义(月)");
		$("#rh").next("label").removeClass("checked");
		$(this).next("label").addClass("checked");
		var count = ($(this).val()-(parseInt($(this).val()/12))*2)*$("#monthlyPrice").val();
		$("#price").empty();
		$("#price").append(count.toFixed(2)+"元");
		month=$(this).val();
		 
		
	});
	$("#rh").click(function(){
		
		$("#rh1").next("label").removeClass("checked");
		$("#rh2").next("label").removeClass("checked");
		$("#rh3").next("label").removeClass("checked");
	});
	$("#rh").blur(function(){
		if($(this).val()!=''){			
			$("#rh1").next("label").removeClass("checked");
			$("#rh2").next("label").removeClass("checked");
			$("#rh3").next("label").removeClass("checked");  
			$("#rh").next("label").addClass("checked");
			var count = ($(this).val()-(parseInt($(this).val()/12))*2)*$("#monthlyPrice").val();
			$("#price").empty();
			$("#price").append(count.toFixed(2)+"元");
			month=$(this).val();
		}else{
			$("#rh3").click();
			$("#rh3").next("label").addClass("checked");
		}
		
	}); 

});
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	userName = "yes";
	ajax.remoteCall("bean://cloudDiskService:getCreateInfo", 
			[final_region],
			function(reply) {
				if (reply.status == "exception") {
					if(reply.errorCode=="RMC_1"){
							top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
								slideleft(1);
						});
					}else{
						
						top.$.messager.alert('警告','获取信息失败','warning'); 
				  }
			    }
				else if (reply.result.status == "success") 
				{ 
					$("#disk_name").html(reply.result.properties.name);
					$("#new_disk_name").val(reply.result.properties.name);
					$("#monthlyPrice").val(reply.result.properties.totalPrice);
// 					$("#btn_confirm").empty();
// 					$("#btn_confirm").append("确认生成");
					$("#showbalance").html(reply.result.properties.balance+"元");
				} 
				else 
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
					}
				}
			); 
}
//--------
function checkDiskinvitationCode(){
	var disk_invitation_code = $("#disk_invitation_code").val();
	if(disk_invitation_code==null || disk_invitation_code==""){
		top.$.messager.alert("提示","邀请码不能为空","warning",function(){});
		return false;
	}else{
		ajax.remoteCall("bean://cloudDiskService:checkInvitationCode", 
				[ disk_invitation_code ],
				function(reply) {
					if (reply.status == "exception")
					{
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						$("#next_button").attr("class","disabledbutton r");    		
			    		$("#next_button").removeAttr("onclick");  
					}
					else if (reply.result.status == "success")
					{ 
						$("#next_button").attr("class","bluebutton r");    		
			    		$("#next_button").attr("onclick","nextstep();"); 
					}
					else
					{
						top.$.messager.alert('警告',reply.result.message,'warning',function(){
						});
						$("#next_button").attr("class","disabledbutton r");    		
			    		$("#next_button").removeAttr("onclick");  
					}
				}
			);
	}
	
}
function save(){
		var formData = $.formToBean(add_cloud_disk);
		$("#rh3").click();
		ajax.remoteCall("bean://cloudDiskService:addCloudDisk", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					nextstep();
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
};
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
      <div class="titlebar"><a href="javascript:void(0);" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">创建硬盘</div>
      </div>
      <ul class="steps" style="width:360px;">
        <li class="l">基础配置<span>1</span></li>
        <li class="l">确认配置<span>2</span></li>
        <li class="l">生成硬盘<span>3</span></li>
      </ul>
      <form class="wizard" id="add_cloud_disk" method="post">
      <input type="hidden" name="disk_name" id="new_disk_name"/>
      <input type="hidden" name="terminalUserId" id="terminalUserId" value="<%=terminalUserId%>"/>
        <ul style="width:3000px;">
          <li>
          <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">邀请码</span></div>
            <div class="l" style="width:600px;"><input id="disk_invitation_code" maxlength="8" name="" type="text" class="textbox" style="height:28px; width:193px; margin-left:5px;"/></div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">地域选择</span></div>
            <div class="l" style="width:600px;">
              <input name="region" id="region1" class="radio" type="radio" value="1" checked="checked"/>
              <label for="region1" class="smalllabel l">广州</label>
              <input name="region" id="region4" class="radio" type="radio" value="4"/>
              <label for="region4" class="smalllabel l">香港</label>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
              <input name="disk" id="disk20" class="radio" type="radio" value="20" checked="checked" />
              <label for="disk20" class="smalllabel l">20G</label>
              <input name="disk" id="disk50" class="radio" type="radio" value="50" />
              <label for="disk50" class="smalllabel l">50G</label>
              <input name="disk" id="disk100" class="radio" type="radio" value="100" />
              <label for="disk100" class="smalllabel l">100G</label>
              <input name="disk" id="disk200" class="radio" type="radio" value="200" />
              <label for="disk200" class="smalllabel l">200G</label>
              <input name="disk" id="disk500" class="radio" type="radio" value="500" />
              <label for="disk500" class="smalllabel l">500G</label>
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input id="disk_other" name="diskDIY" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="smalllabel" title="G"/>
                <label for="disk_other" class="smalllabel l">自定义(G)</label>
              </div>
            </div>
            <div class="clear"></div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2">
            <a id="next_button"  href="javascript:void(0);" class="disabledbutton r"  >下一步</a>
 <!--             	<i>单价：<strong>0.0元/小时</strong>　约合：<strong>0元/月</strong></i><br/>  -->
            	地域:<span id="disk_region_one">广州</span>　硬盘:<span id="disk_size_one">20</span>G
            </div>
          </li>
          <li>
            <div style="width:658px; padding:30px 30px 15px 30px; border:solid 1px #b2b2b2">
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">硬盘名称</span></div>
              <div id="disk_name" class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">未分配</div>
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">地域</span></div>
              <div id="disk_region_two" class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">广州</div>
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">硬盘容量</span></div>
              <div  class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;"><span id="disk_size_two">20</span>G</div>
              <div class="clear"></div>
            </div>
            <div class="buttonbar"><a id="add_cloud_disk_button" class="bluebutton r" href="javascript:void(0);"><span id="btn_confirm">确认生成</span></a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a>
<!--             <i style="line-height:40px;">单价：<strong>0.0元/小时</strong>　约合：<strong>0元/月</strong></i> -->
            </div>
          </li>
          <li>
            <div class="infoicon" style="font-size:16px; font-weight:bold">创建成功</div> 
            <div class="info">当前余额：<strong id="showbalance"><%=balance %>元</strong>
                          <!--  ，您的硬盘还可使用：<strong style="color:#4cda64">3个月23天</strong> -->
            </div>
            <div style="width:720px; margin:30px auto 0 auto;line-height:30px; text-align:right;"> <span class="bluebutton topradius l">立即充值</span><b>优惠：现在充值一年只需支付10个月的费用</b>
              <div class="clear"></div>
            </div>
            <div style="width:700px; margin:0 auto; background:#e2e2e2; border-top:solid 1px #b2b2b2; padding:20px 0 20px 20px;">
                
              <input name="userType" value="4" type="hidden"/>
              <input name="bean" value="accountBalanceService" type="hidden"/>
              <input name="method" value="toRechargeGiftPage" type="hidden"/>
              <input name="monthlyPrice" id="monthlyPrice" value="<%=totalPrice %>" type="hidden"/> 
              <input name="month" id="rh1" class="radio" type="radio" value="3"  />
              <label for="rh1" class="biglabel l" style="margin:0 20px 0 0;width:153px">3月</label>
              <input name="month" id="rh2" class="radio" type="radio" value="6" />
              <label for="rh2" class="biglabel l" style="margin:0 20px 0 0;width:153px">6月</label>
              <input name="month" id="rh3" class="radio" type="radio" value="12" checked="checked" />
              <label for="rh3" class="biglabel l" style="margin:0 20px 0 0;width:153px">1年</label>
              <div class="custom biglabel l" style="border:none; margin:0; width:153px;">
                <input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" id="rh" name="monthDIY" type="text" class="biglabel" title="月" style="margin:0 20px 0 0;width:153px"/>
                <label for="rh" class="biglabel l" style="margin:0 20px 0 0;width:153px">自定义(月)</label>
              </div>
              
              <div class="clear"></div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" id="finish">进入硬盘列表</a><a class="bluebutton r" href="javascript:void(0);" id="goRechargeRightNow">立即充值</a><i class="r" style="line-height:40px;">充值金额：<strong id="price">600.00元</strong></i></div>
          </li>
        </ul>
      </form>
    </div>
    <div class="clear"></div>
    <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
  </div> 
  <div id="month_form">
  </div>
</div>
</body>
</html>
 




