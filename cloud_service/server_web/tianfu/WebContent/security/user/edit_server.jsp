<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@page import="com.zhicloud.op.app.helper.CloudHostPrice"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
CloudHostVO cloudHost = (CloudHostVO)request.getAttribute("cloudHost");
List<CpuPackageOptionVO> cpuOptions = (List<CpuPackageOptionVO>)request.getAttribute("cpuOptions");
List<MemoryPackageOptionVO> memoryOptions = (List<MemoryPackageOptionVO>)request.getAttribute("memoryOptions");
List<Integer> cpuList =  (List<Integer>)request.getAttribute("cpuList");
List<BigInteger> memoryList =  (List<BigInteger>)request.getAttribute("memoryList");
List<BigInteger> bandwidthList = (List<BigInteger>)request.getAttribute("bandwidthList");
String bandwidthMin_1             = AppProperties.getValue("bandwidthMin_1","");
String bandwidthMin_4             = AppProperties.getValue("bandwidthMin_4","");
String bandwidthMax_1             = AppProperties.getValue("bandwidthMax_1","");
String bandwidthMax_4             = AppProperties.getValue("bandwidthMax_4","");
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
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;
$(document).ready(function(){
	var userName = "<%=loginInfo.getAccount()%>";
	init(10,2);
	inituser(userName,0);
	initstep(1);
	
		
	$("#cloud_host_modify_allocation_save_btn").click(function() {
	    if($("#beforprice").val()==0){
	    	
			top.$.messager.confirm("确认", "您的云主机将以"+$("#price_hour").html()+"元/小时收费<br/>您确定要修改该免费推广套餐吗？", function (r) { 
				if(r){
					save();
				}
			});
	    }else{
			top.$.messager.confirm("确认", "您的云主机将以"+$("#price_hour").html()+"元/小时收费<br/>您确定要修改吗？", function (r) { 
				if(r){
					save();
				}
			});
	    	
	    }
//		save();
	});
		/* self.checkCPUCore = function(){
			var core = $("input[name='cpu_core']:checked").val(); 
			if(core==null || core==""){
				$("#tip-cpu_core").html("<b>请选择CPU核心数</b>");
				return false;
			} 
			$("#tip-cpu_core").html("");
			return true;
		};
		self.checkMemory = function(){
			var memory = $("input[name='memory']:checked").val(); 
			if(memory==null || memory==""){
				$("#tip-host-memory").html("<b>请选择内存</b>");
				return false;
			} 
			$("#tip-host-memory").html("");
			return true;
		}; */
		// 提交
	var core = "<%=cloudHost.getCpuCore()%>";
	var memory = "<%=CapacityUtil.toGBValue(cloudHost.getMemory(),0)%>";
	var bandWidth = "<%=FlowUtil.toMbpsValue(cloudHost.getBandwidth(),  0)%>";
	$("#cpu_core_"+core).click();
	$("#memory_"+memory).click();
	$("#bandwidth"+bandWidth).click();
	var bandwidth = $("input[name='bandwidth']:checked").val();
	if(bandwidth == null){
	  $("#bandwidth").click();
	  $("#bandwidth").val(bandWidth);
	  $("#bandwidth").next("label").addClass("checked");
	  $("#bandwidth").next("label").html(bandWidth+"M").attr("title");
 	} 
	var region = "<%=cloudHost.getRegion()%>";
	var dataDisk = "<%=CapacityUtil.toGBValue(cloudHost.getDataDisk(), 0)%>";
	var curNewPrice = refreshPrice(region,core,memory,dataDisk,bandWidth);
	var newPrice = (curNewPrice / 1).toFixed(2);
	var newPrice_hour = (newPrice / 30/ 24).toFixed(2);
	$("#current_core").html(core);
	$("#current_memory").html(memory);
	$("#current_bandwidth").html(bandWidth+"Mbps");
	$("#price_hour").html(newPrice_hour);
	$("#price").html(newPrice);
	
});
function save(){ 
	var formData = $.formToBean(my_cloud_host_modify_allocation_form);
	ajax.remoteCall("bean://cloudHostService:modifyAllocation", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception")
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			}
			else if (reply.result.status == "success")
			{
				top.$.messager.alert('提示',reply.result.message,'info',function(){
				});
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
};
//--------------------
function updatePrice(){
	var core = $("input[name='cpu_core']:checked").val();
	if(core == null){
		core = "<%=cloudHost.getCpuCore()%>";
	}
	var memory = $("input[name='memory']:checked").val();
	if(memory == null){
		memory = "<%=CapacityUtil.toGBValue(cloudHost.getMemory(),0)%>";
	}
	var curBandwidth = $("input[name='bandwidth']:checked").val();
	var region = "<%=cloudHost.getRegion()%>";
	var bandwidthMax_1 = "<%=bandwidthMax_1%>";
	var bandwidthMax_4 = "<%=bandwidthMax_4%>";
	var Max_1 = parseInt(bandwidthMax_1);
	var Max_4 = parseInt(bandwidthMax_4);
	if(curBandwidth == null){
		curBandwidth = $("input[name='bandwidthDIY']").val(); 
		if(region=="1"){ 
			if(curBandwidth>Max_1){
				curBandwidth = Max_1;
				$("#bandwidth").val(Max_1);
				$("#bandwidth").next("label").html(Max_1+"M");
				top.$.messager.alert('警告','广州带宽最大支持'+Max_1+'M','warning');  
			}
		}else if(region=="4"){ 
			if(curBandwidth>Max_4){
				curBandwidth = Max_4;
				$("#bandwidth").val(Max_4);
				$("#bandwidth").next("label").html(Max_4+"M");
				top.$.messager.alert('警告','香港带宽最大支持'+Max_4+'M','warning');  
			}
			
		}
	} 
	/* if(core == 1){
		if(memory > 4){
			document.getElementById("memory_1").checked = true;
			memory = 1;
		}
		document.getElementById("memory_1").disabled = false;
		document.getElementById("memory_2").disabled = false;
		document.getElementById("memory_4").disabled = false;
		document.getElementById("memory_6").disabled = true;
		document.getElementById("memory_8").disabled = true;
		document.getElementById("memory_12").disabled = true;
		document.getElementById("memory_16").disabled = true;
		document.getElementById("memory_24").disabled = true;
		document.getElementById("memory_32").disabled = true;
		document.getElementById("memory_40").disabled = true;
		document.getElementById("memory_48").disabled = true;
		document.getElementById("memory_6").checked = false;
		document.getElementById("memory_8").checked = false;
		document.getElementById("memory_12").checked = false;
		document.getElementById("memory_16").checked = false;
		document.getElementById("memory_24").checked = false;
		document.getElementById("memory_32").checked = false;
		document.getElementById("memory_40").checked = false;
		document.getElementById("memory_48").checked = false;
	}
	if(core == 2){
		if(memory > 8){
			document.getElementById("memory_1").checked = true;
			memory = 1;
		}
		document.getElementById("memory_1").disabled = false;
		document.getElementById("memory_2").disabled = false;
		document.getElementById("memory_4").disabled = false;
		document.getElementById("memory_6").disabled = false;
		document.getElementById("memory_8").disabled = false;
		document.getElementById("memory_12").disabled = true;
		document.getElementById("memory_16").disabled = true;
		document.getElementById("memory_24").disabled = true;
		document.getElementById("memory_32").disabled = true;
		document.getElementById("memory_40").disabled = true;
		document.getElementById("memory_48").disabled = true;
		document.getElementById("memory_12").checked = false;
		document.getElementById("memory_16").checked = false;
		document.getElementById("memory_24").checked = false;
		document.getElementById("memory_32").checked = false;
		document.getElementById("memory_40").checked = false;
		document.getElementById("memory_48").checked = false;
	}
	if(core == 4){
		if(memory<2 || memory>16){
			document.getElementById("memory_2").checked = true;
			memory = 2;
		}
		document.getElementById("memory_1").disabled = true;
		document.getElementById("memory_2").disabled = false;
		document.getElementById("memory_4").disabled = false;
		document.getElementById("memory_6").disabled = false;
		document.getElementById("memory_8").disabled = false;
		document.getElementById("memory_12").disabled = false;
		document.getElementById("memory_16").disabled = false;
		document.getElementById("memory_24").disabled = true;
		document.getElementById("memory_32").disabled = true;
		document.getElementById("memory_40").disabled = true;
		document.getElementById("memory_48").disabled = true;
		document.getElementById("memory_1").checked = false;
		document.getElementById("memory_24").checked = false;
		document.getElementById("memory_32").checked = false;
		document.getElementById("memory_40").checked = false;
		document.getElementById("memory_48").checked = false;
	}
	if(core == 8){
		if(memory<4 || memory>32){
			document.getElementById("memory_4").checked = true;
			memory = 4;
		}
		document.getElementById("memory_1").disabled = true;
		document.getElementById("memory_2").disabled = true;
		document.getElementById("memory_4").disabled = false;
		document.getElementById("memory_6").disabled = false;
		document.getElementById("memory_8").disabled = false;
		document.getElementById("memory_12").disabled = false;
		document.getElementById("memory_16").disabled = false;
		document.getElementById("memory_24").disabled = false;
		document.getElementById("memory_32").disabled = false;
		document.getElementById("memory_40").disabled = true;
		document.getElementById("memory_48").disabled = true;
		document.getElementById("memory_1").checked = false;
		document.getElementById("memory_2").checked = false;
		document.getElementById("memory_40").checked = false;
		document.getElementById("memory_48").checked = false;
	}
	if(core == 12){
		if(memory<8){
			document.getElementById("memory_8").checked = true;
			memory = 8;
		}
		document.getElementById("memory_1").disabled = true;
		document.getElementById("memory_2").disabled = true;
		document.getElementById("memory_4").disabled = true;
		document.getElementById("memory_6").disabled = true;
		document.getElementById("memory_8").disabled = false;
		document.getElementById("memory_12").disabled = false;
		document.getElementById("memory_16").disabled = false;
		document.getElementById("memory_24").disabled = false;
		document.getElementById("memory_32").disabled = false;
		document.getElementById("memory_40").disabled = false;
		document.getElementById("memory_48").disabled = false;
		document.getElementById("memory_1").checked = false;
		document.getElementById("memory_2").checked = false;
		document.getElementById("memory_4").checked = false;
		document.getElementById("memory_6").checked = false;
	} */
	var dataDisk = "<%=CapacityUtil.toGBValue(cloudHost.getDataDisk(), 0)%>";
<%-- 	var bandWidth = "<%=FlowUtil.toMbpsValue(cloudHost.getBandwidth(),  0)%>"; --%>
	var curNewPrice = refreshPrice(region,core,memory,dataDisk,curBandwidth+"");
	var newPrice = (curNewPrice / 1).toFixed(2);
	var newPrice_hour = (newPrice / 30/ 24).toFixed(2);
	$("#current_core").html(core);
	$("#current_memory").html(memory);
	$("#current_bandwidth").html(curBandwidth+"Mbps");
	$("#price_hour").html(newPrice_hour);
	$("#price").html(newPrice);
	return;
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
      <div class="titlebar"><a href="javascript:void(0);" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">修改配置</div>
      </div>
      <form id="my_cloud_host_modify_allocation_form" method="post">
      		<input type="hidden" id="region" name="region" value="<%=cloudHost.getRegion()%>" />
			<input type="hidden" id="cloudHostName" name="cloudHostName" value="<%=cloudHost.getHostName()%>" />
			<input type="hidden" id="cloudHostId" name="cloudHostId" value="<%=cloudHost.getId()%>" />
			<input type="hidden" id="realCloudHostId" name="realCloudHostId" value="<%=cloudHost.getRealHostId()%>" />
<%-- 			<input type="hidden" id="bandWidth" name="bandWidth" value="<%=cloudHost.getBandwidth()%>" /> --%>
			<input type="hidden" id="sysDisk" name="sysDisk" value="<%=cloudHost.getSysDisk()%>" />
			<input type="hidden" id="dataDisk" name="dataDisk" value="<%=cloudHost.getDataDisk()%>" />
			<input type="hidden" id="beforprice" name="beforprice" value="<%=cloudHost.getMonthlyPrice()%>" />
        <div style="width:720px; margin:30px auto 0 auto;">
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">CPU</span></div>
            <div class="l" style="width:600px;">
            <%for(Integer cpu : cpuList){ %>
            	<input name="cpu_core" id="cpu_core_<%=cpu %>" class="radio" type="radio" value="<%=cpu %>" onclick="updatePrice()"/>
                <label for="cpu_core_<%=cpu %>" class="smalllabel l"><%=cpu %>核</label>
            <%} %>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">内存</span></div>
            <div class="l" style="width:600px;">
              <%for(BigInteger memory : memoryList){ %>
              	 <input name="memory" id="memory_<%=memory %>" class="radio" type="radio" value="<%=memory %>" onclick="updatePrice()" />
              	 <label for="memory_<%=memory %>" class="smalllabel l"><%=memory %>G</label>
              <%} %>
            </div>
            <div class="clear"></div>
            <!-- <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
              <input name="re" id="re1" class="radio" type="radio" value="" checked="checked" />
              <label for="re1" class="smalllabel l">20G</label>
              <input name="re" id="re2" class="radio" type="radio" value="" />
              <label for="re2" class="smalllabel l">50G</label>
              <input name="re" id="re3" class="radio" type="radio" value="" />
              <label for="re3" class="smalllabel l">100G</label>
              <input name="re" id="re4" class="radio" type="radio" value="" />
              <label for="re4" class="smalllabel l">200G</label>
              <input name="re" id="re5" class="radio" type="radio" value="" />
              <label for="re5" class="smalllabel l">500G</label>
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input id="re" name="" type="text" class="smalllabel" title="G"/>
                <label for="re" class="smalllabel l">自定义(G)</label>
              </div>
            </div>
            <div class="clear"></div>-->
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">带宽</span></div>
            <div class="l" style="width:600px;">
            <%for(BigInteger bandwidth : bandwidthList){ %>
            	<input name="bandwidth" id="bandwidth<%=bandwidth %>" class="radio" type="radio" value="<%=bandwidth %>"  onclick="updatePrice()"  />
                <label for="bandwidth<%=bandwidth %>" class="smalllabel l"><%=bandwidth %>M</label>
            <%} %>
              <div class="custom smalllabel l" style="border:none; margin-left:0;">
                <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" id="bandwidth" name="bandwidthDIY" type="text" class="smalllabel" title="M" value="" onblur="updatePrice()"/>
                <label for="bandwidth" class="smalllabel l">自定义(M)</label>
              </div> 
              <div class="clear"></div>
            </div>
            <div class="clear"></div>
             <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a id="cloud_host_modify_allocation_save_btn" class="bluebutton r" href="javascript:void(0);">确认修改</a><a class="graybutton r" href="javascript:void(0);" onclick="self.location=document.referrer;">返回</a><i>单价：<strong><span id="price_hour">0.24</span>元/小时</strong>　约合：<strong><span id="price">170</span>元/月</strong></i><br/>
              地域:<% if(cloudHost.getRegion()==1){%>广州<%}else if(cloudHost.getRegion()==2){ %>北京<% }else if(cloudHost.getRegion()==3){%>成都<%}else { %>香港<%} %>　CPU:<span id="current_core">1</span>核　内存:<span id="current_memory">1</span>G　硬盘:<%=cloudHost.getDataDiskText(0)%>　带宽:<span id="current_bandwidth"><%=cloudHost.getBandwidthText(0) %></span></div>
          </div>
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
</div>
</body>
</html>
