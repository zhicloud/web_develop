<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
	String userId  = "";
	String userName = "";
	if(loginInfo != null){
		userId = loginInfo.getUserId();
		userName = loginInfo.getAccount();
	}
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
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/common.js"></script>
<script type="text/javascript">
var totalPrice='<%=totalPrice%>';
var balance='<%=balance%>';
var diskName = '<%=diskName%>';
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var userName = "<%=userName%>";
var userId = "";
var final_region = "1";
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
            <div class="infoicon" style="font-size:16px; font-weight:bold">创建中</div>
            <div id="progressbar" class="easyui-progressbar" value="0" text="云硬盘创建中({value}%)"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div>
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
    <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
  <div id="month_form">
  </div>
</div>
</body>
</html>




