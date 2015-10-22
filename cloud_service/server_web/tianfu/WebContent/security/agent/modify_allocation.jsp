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
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath() %>/javascript/agent.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
ajax.async = false;
$(document).ready(function(){
	var userName = "<%=loginInfo.getAccount()%>";
	init(2);
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
    <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html> 
