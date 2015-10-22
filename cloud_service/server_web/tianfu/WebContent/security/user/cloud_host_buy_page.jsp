<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page import="com.zhicloud.op.vo.ShoppingCartVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.PackagePriceVO"%>
<%@page import="com.zhicloud.op.vo.CloudHostSysDefaultPortsVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@ page import="java.util.*"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<SysDiskImageVO> sysDiskImageOptions  = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptions");
	List<PackagePriceVO> cpuRegion1  = (List<PackagePriceVO>)request.getAttribute("cpuRegion1");
	List<PackagePriceVO> cpuRegion2  = (List<PackagePriceVO>)request.getAttribute("cpuRegion2");
	List<PackagePriceVO> cpuRegion4  = (List<PackagePriceVO>)request.getAttribute("cpuRegion4");
	List<PackagePriceVO> memoryRegion1  = (List<PackagePriceVO>)request.getAttribute("memoryRegion1");
	List<PackagePriceVO> memoryRegion2  = (List<PackagePriceVO>)request.getAttribute("memoryRegion2");
	List<PackagePriceVO> memoryRegion4  = (List<PackagePriceVO>)request.getAttribute("memoryRegion4");
	List<PackagePriceVO> diskRegion1  = (List<PackagePriceVO>)request.getAttribute("diskRegion1");
	List<PackagePriceVO> diskRegion2  = (List<PackagePriceVO>)request.getAttribute("diskRegion2");
	List<PackagePriceVO> diskRegion4  = (List<PackagePriceVO>)request.getAttribute("diskRegion4");
	List<PackagePriceVO> bandwidthRegion1  = (List<PackagePriceVO>)request.getAttribute("bandwidthRegion1");
	List<PackagePriceVO> bandwidthRegion2  = (List<PackagePriceVO>)request.getAttribute("bandwidthRegion2");
	List<PackagePriceVO> bandwidthRegion4  = (List<PackagePriceVO>)request.getAttribute("bandwidthRegion4");
	List<PackagePriceVO> package1  = (List<PackagePriceVO>)request.getAttribute("package1");
	List<PackagePriceVO> package2  = (List<PackagePriceVO>)request.getAttribute("package2");
	List<PackagePriceVO> package4  = (List<PackagePriceVO>)request.getAttribute("package4");
	List<CloudHostSysDefaultPortsVO> defaultPorts  = (List<CloudHostSysDefaultPortsVO>)request.getAttribute("defaultPorts");
	
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType); 
	Integer chance = (Integer)request.getAttribute("trailchance");
	if( chance==null )
	{
		chance = 1;
	}
	String balance = (String)request.getAttribute("balance");
	String hostName = (String)request.getAttribute("hostName");
	BigDecimal totalPrice = (BigDecimal)request.getAttribute("totalPrice");
	String dataDiskMin = (String)request.getAttribute("dataDiskMin");
	String dataDiskMax = (String)request.getAttribute("dataDiskMax");
	String bandwidthMin_1 = (String)request.getAttribute("bandwidthMin_1");
	String bandwidthMin_2 = (String)request.getAttribute("bandwidthMin_2");
	String bandwidthMin_4 = (String)request.getAttribute("bandwidthMin_4");
	String bandwidthMax_1 = (String)request.getAttribute("bandwidthMax_1");
	String bandwidthMax_2 = (String)request.getAttribute("bandwidthMax_2");
	String bandwidthMax_4 = (String)request.getAttribute("bandwidthMax_4");
	if(balance==null){
		balance="0";
	}
	if(totalPrice==null){
		totalPrice=new BigDecimal("0");
	}
	String name = "";
	String userId = "";
	if(loginInfo!=null){
		name=loginInfo.getAccount();
		userId=loginInfo.getUserId();
	}
	if(hostName==null||hostName==""){
		hostName = "未分配";
	}
	
	
	
// 	BigDecimal i = new BigDecimal("1073741824");
// 	BigDecimal k = new BigDecimal("1000000");
// 	BigInteger j = new BigInteger("1073741824"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8">
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
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/cloudhostcreate.js"></script> 
<script type="text/javascript">
var totalPrice='<%=totalPrice%>';
var balance='<%=balance%>';
var name = '<%=name%>';
var hostName = '<%=hostName%>';
var userId = '<%=userId%>'; 
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var month;
var dataDiskMin = '<%=dataDiskMin%>';
var dataDiskMax = '<%=dataDiskMax%>';
var bandwidthMin_1 = '<%=bandwidthMin_1%>';
var bandwidthMin_2 = '<%=bandwidthMin_2%>';
var bandwidthMin_4 = '<%=bandwidthMin_4%>';
var bandwidthMax_1 = '<%=bandwidthMax_1%>';
var bandwidthMax_2 = '<%=bandwidthMax_2%>';
var bandwidthMax_4 = '<%=bandwidthMax_4%>';
var cdMaxPorts = '<%=AppProperties.getValue("cd_max_ports", "")%>';
$(document).ready(function(){
	init(); 
	if(name!= ''){ 
	   inituser(name,0);
	}else{
		
	   inituser();
	}   
	initstep(1);
	setports(1);
});
function setports(x){
	if(x==0){
		$("#step1").addClass("steps");
		$("#step1").css("display","block");
		$("#step2").css("display","none");
		$("#step2").removeClass("steps");
		initstep(1);
		$("#wizardbox").css("width","4000px");
		$("#portbox").css("display","none");
		$("#confirm1").css("display","block");
		$("#confirm2").css("display","none");
	}else if(x==1){
		$("#step1").css("display","none");
		$("#step1").removeClass("steps");
		$("#step2").addClass("steps");
		$("#step2").css("display","block");
		initstep(1);
		$("#wizardbox").css("width","5000px");
		$("#portbox").css("display","block");
		$("#confirm1").css("display","none");
		$("#confirm2").css("display","block");
	}
}
</script>
<style type="text/css">
#porttable td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
}
#porttable .headtr td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
	background:#ddedef;
}
#porttable .nochange .datagrid-cell {
	color: #a2a2a2;
}
</style>
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
      <div class="titlebar"><a href="javascript:void(0);"  onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">创建主机</div>
      </div>
      <ul id="step1" class="steps" style="width:480px;">
      <li class="l">基础配置<span>1</span></li>
      <li class="l">镜像选择<span>2</span></li>
      <li class="l">确认配置<span>3</span></li>
      <li class="l">生成主机<span>4</span></li>
    </ul>
    <ul id="step2" class="steps" style="width:600px;display:none">
      <li class="l">基础配置<span>1</span></li>
      <li class="l">镜像选择<span>2</span></li>
      <li class="l">端口配置<span>3</span></li>
      <li class="l">确认配置<span>4</span></li>
      <li class="l">生成主机<span>5</span></li>
    </ul>
      <form class="wizard" id="cloud_host_config_form"  action="<%=request.getContextPath()%>/bean/page.do" target="_blank" method="post">
        <ul id="wizardbox" style="width:4000px;heigth:900px">
          <li>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">地域选择</span></div>
            <div class="l" style="width:600px;">
              <input name="region" id="region2" class="radio" type="radio" onclick="setports(1);" value="2" checked="checked"/>
              <label for="region2" class="smalllabel l">成都</label>
              <input name="region" id="region1" class="radio" type="radio" onclick="setports(0);" value="1"  />
              <label for="region1" class="smalllabel l">广州</label>
              <input name="region" id="region4" class="radio" type="radio" onclick="setports(0);" value="4" />
              <label for="region4" class="smalllabel l">香港</label>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">推荐配置</span></div>
            <div class="l" style="width:600px;">
              <div id="package1" style="display:none;">
	            <%
	              int i = 1;
	              for(PackagePriceVO vo : package1){ 
	          	  %>
	                <input name="item" id="item1_<%=i %>" class="radio" type="radio" value="<%=vo.getId() %>"  price="<%=vo.getPrice()%>" cpu="<%=vo.getCpuCore()%>" memory="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>" disk="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>" bandwidth="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"/>
                    <label for="item1_<%=i %>" class="biglabel l"><%=vo.getDescription() %></label>
	          	  <%
	          	    i++;
	              }
	            %>            
            </div>
            <div id="package2" >
	            <%
	              i = 1;
	              for(PackagePriceVO vo : package2){
	          	  %>
	              <input name="item" id="item2_<%=i %>" class="radio" type="radio" value="<%=vo.getId() %>" price="<%=vo.getPrice()%>" cpu="<%=vo.getCpuCore()%>" memory="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>" disk="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>" bandwidth="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"/>
                    <label for="item2_<%=i %>" class="biglabel l"><%=vo.getDescription() %></label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div>
            <div id="package4" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : package4){
	          	  %>
	              <input name="item" id="item4_<%=i %>" class="radio" type="radio" value="<%=vo.getId() %>" price="<%=vo.getPrice()%>" cpu="<%=vo.getCpuCore()%>" memory="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>" disk="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>" bandwidth="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"/>
                    <label for="item4_<%=i %>" class="biglabel l"><%=vo.getDescription() %></label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div>
            
             
              <input name="item" id="item6" class="radio" type="radio" value="" />
              <label for="item6" class="biglabel r" >自定义</label>
            </div>
            <div class="clear"></div>
            <div id="diy" style="display:none">
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">CPU</span></div>
            <div class="l" style="width:600px;">
            <div id="cpuPackage1" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : cpuRegion1){ 
	          	  %>
	              <input name="cpu" id="cpuRegion1_<%=i %>" class="radio" type="radio" value="<%=vo.getCpuCore() %>"  />
	              <label for="cpuRegion1_<%=i %>" class="smalllabel l"><%=vo.getCpuCore() %>核</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>            
            </div>
            <div id="cpuPackage2"  >
	            <%
	              i = 1;
	              for(PackagePriceVO vo : cpuRegion2){
	          	  %>
	              <input name="cpu" id="cpuRegion2_<%=i %>" class="radio" type="radio" value="<%=vo.getCpuCore() %>"  />
	              <label for="cpuRegion2_<%=i %>" class="smalllabel l"><%=vo.getCpuCore() %>核</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div>
            <div id="cpuPackage4" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : cpuRegion4){
	          	  %>
	              <input name="cpu" id="cpuRegion4_<%=i %>" class="radio" type="radio" value="<%=vo.getCpuCore() %>"  />
	              <label for="cpuRegion4_<%=i %>" class="smalllabel l"><%=vo.getCpuCore() %>核</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">内存</span></div>
            <div class="l" style="width:600px;">
            <div id="memoryPackage1" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : memoryRegion1){ 
	          	  %>
	              <input name="memory" id="memoryRegion1_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>"  />
                  <label for="memoryRegion1_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G</label>
	          	  <%
	          	    i++;
	              }
	            %>            
            </div>
            <div id="memoryPackage2"  >
	            <%
	              i = 1;
	              for(PackagePriceVO vo : memoryRegion2){
	          	  %>
	              <input name="memory" id="memoryRegion2_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>"  />
                  <label for="memoryRegion2_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            <div id="memoryPackage4" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : memoryRegion4){
	          	  %>
	              <input name="memory" id="memoryRegion4_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>"  />
                  <label for="memoryRegion4_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
            <div id="diskPackage1" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : diskRegion1){ 
	          	  %>
	          	  <input name="dataDisk" id="diskRegion1_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>"   />
                  <label for="diskRegion1_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G</label> 
	          	  <%
	          	    i++;
	              }
	            %>            
            </div>
            <div id="diskPackage2"  >
	            <%
	              i = 1;
	              for(PackagePriceVO vo : diskRegion2){
	          	  %>
	              <input name="dataDisk" id="diskRegion2_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>"   />
                  <label for="diskRegion2_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G</label> 
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            <div id="diskPackage4" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : diskRegion4){
	          	  %>
	              <input name="dataDisk" id="diskRegion4_<%=i %>" class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>"   />
                  <label for="diskRegion4_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G</label> 
	          	  
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            
              
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" id="dataDisk" name="dataDiskDIY" type="text" class="smalllabel" title="G"/>
                <label for="dataDisk" class="smalllabel l">自定义(G)</label>
              </div>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">带宽</span></div>
            <div class="l" style="width:600px;">
            <div id="bandwidthPackage1" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : bandwidthRegion1){ 
	          	  %>
	          	  <input name="bandwidth" id="bandwidthRegion1_<%=i %>" class="radio" type="radio" value="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"   />
                  <label for="bandwidthRegion1_<%=i %>" class="smalllabel l"><%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>M</label>
	          	  <%
	          	    i++;
	              }
	            %>            
            </div>
            <div id="bandwidthPackage2" >
	            <%
	              i = 1;
	              for(PackagePriceVO vo : bandwidthRegion2){
	          	  %>
	          	  <input name="bandwidth" id="bandwidthRegion2_<%=i %>" class="radio" type="radio" value="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"   />
                  <label for="bandwidthRegion2_<%=i %>" class="smalllabel l"><%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>M</label>
	               
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            <div id="bandwidthPackage4" style="display:none;">
	            <%
	              i = 1;
	              for(PackagePriceVO vo : bandwidthRegion4){
	          	  %>
	          	  <input name="bandwidth" id="bandwidthRegion4_<%=i %>" class="radio" type="radio" value="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"   />
                  <label for="bandwidthRegion4_<%=i %>" class="smalllabel l"><%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>M</label>
	               
	          	  <%
	          	    i++;
	              }
	            %>             
            </div> 
            
              
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" id="bandwidth" name="bandwidthDIY" type="text" class="smalllabel" title="M"/>
                <label for="bandwidth" class="smalllabel l">自定义(M)</label>
              </div>
            </div>
            </div>
            <div class="clear"></div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" onclick="checkRegion();nextstep();">下一步</a>
            <div id="description_1">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i><br/>
              地域/成都　CPU/4核　内存/2G　硬盘/2048G　带宽/9M
            </div>
              
              </div>
          </li>
          <li>
            <div id="isotitle" style="text-align:center;">版本信息:PHP5.2.17sp1、Apache2.2.27、MySQL5.1.73、vsFTPd 2.2.2、phpMyAdmin 4.0.10</div>
            <div id="isoselect"]>
            <div id="image_region_1" >
             <% 
				for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
				{
					
					if(sysDiskImageOption.getRealImageId()==null||sysDiskImageOption.getStatus()!=2||sysDiskImageOption.getRealImageId().length()==0||sysDiskImageOption.getRegion()!=1){
						continue;
					} 
				%> 
				
				    <input imgdescription="<%=sysDiskImageOption.getDescription() %>"  name="sysImageId" id="<%=sysDiskImageOption.getName()%>_1" class="radio" type="radio" value="<%=sysDiskImageOption.getId()%>"   />
 				 
              <label onmouseout="showDescription('');" onmouseover="showDescription('<%=sysDiskImageOption.getDescription() %>');" for="<%=sysDiskImageOption.getName()%>_1" class="biglabel l"><%=sysDiskImageOption.getName()%></label> 
 				<%
				}
				%>
            </div>
            <div id="image_region_2">
             <% 
				for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
				{
					
					if(sysDiskImageOption.getRealImageId()==null||sysDiskImageOption.getStatus()!=2||sysDiskImageOption.getRealImageId().length()==0||sysDiskImageOption.getRegion()!=2){
						continue; 
					}
				%> 
				
				    <input   imgdescription="<%=sysDiskImageOption.getDescription() %>" name="sysImageId" id="<%=sysDiskImageOption.getName()%>_2" class="radio" type="radio" value="<%=sysDiskImageOption.getId()%>"   />
 				 
              <label onmouseout="showDescription('');" onmouseover="showDescription('<%=sysDiskImageOption.getDescription() %>');" for="<%=sysDiskImageOption.getName()%>_2" class="biglabel l"><%=sysDiskImageOption.getName()%></label> 
 				<%
				}
				%>
            </div>
            <div id="image_region_4">
             <% 
				for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
				{
					
					if(sysDiskImageOption.getRealImageId()==null||sysDiskImageOption.getStatus()!=2||sysDiskImageOption.getRealImageId().length()==0||sysDiskImageOption.getRegion()!=4){
						continue; 
					}
				%> 
				
				    <input   imgdescription="<%=sysDiskImageOption.getDescription() %>" name="sysImageId" id="<%=sysDiskImageOption.getName()%>_4" class="radio" type="radio" value="<%=sysDiskImageOption.getId()%>"   />
 				 
              <label onmouseout="showDescription('');" onmouseover="showDescription('<%=sysDiskImageOption.getDescription() %>');" for="<%=sysDiskImageOption.getName()%>_4" class="biglabel l"><%=sysDiskImageOption.getName()%></label> 
 				<%
				}
				%>
            </div>
                
              <div class="clear"></div>
              <div class="easyui-pagination" data-options="showPageList: false,showRefresh: false,displayMsg: '',beforePageText: '',afterPageText: ''"></div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2">
            <a id="getImage" href="javascript:void(0);" class="bluebutton r" onclick="getCreateInfo();nextstep();getComfirmInfo();getDefaultPort();">下一步</a>
                       
            <a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a>
            <div id="description_2">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i><br/>
              地域/成都　CPU/4核　内存/2G　硬盘/2048G　带宽/9M
            </div></div>
          </li>
          
          <li id="portbox" style="display:none">
          <div class="box">
            <div class="titlebar" style="width:720px;padding:15px 0 5px 0;">
              <div class="blocks l">
                <div class="l"><b id="portsCount">4</b>种服务的端口已开启</div>
              </div>
              <div class="blocks r"><a href="javascript:void(0);" class="bluebutton r" style="width:80px;"  onclick="addPort();">添加</a>
                 <div class="r" style="padding:0 10px 0 0">
                  <input  class="textbox" type="text" name="newPort" id="newPort" style="width:58px;height:28px" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></input>
                </div>
                <div class="r" style="padding:0 10px 0 0">
                  <select class="easyui-combobox"   id="protocol" data-options="width:'60',height:'30',panelHeight:'auto',editable:false">
                    <option value="1">TCP</option>
                    <option value="2">UDP</option>
                  </select>
                </div>
                <div class="r" style="padding:0 10px 0 0;">
                                                           添加端口：
                </div>
                <div class="clear"></div>
              </div>
              <div class="clear"></div>
            </div>
            <div class="box" style="overflow-y:scroll; height:410px;">
              <table id="porttable" cellpadding="0" cellspacing="0" border="0" width="100%"> 
              <tr class="headtr">
                  <td style="width:28px">&nbsp;</td>
                  <td style="width:231px"><div class="datagrid-cell">服务名称</div></td>
                  <td style="width:231px"><div class="datagrid-cell">协议</div></td>
                  <td style="width:230px"><div class="datagrid-cell">端口</div></td>
                </tr>
                <%
                   for(CloudHostSysDefaultPortsVO port : defaultPorts){
                	   if(port.getName()==null || port.getName().equals("FTP") || port.getName().equals("RDP") || port.getName().equals("SSH") || port.getName().equals("主机面板")){
                		   continue;
                	   }
              	   %>
              	   
	                <tr>
	                  <td style="width:28px"><div class="datagrid-cell">
	                      <input name="ports" type="checkbox"   onclick="checkPortsCount(this);" value="<%=port.getName() %>&<%=port.getProtocol() %>&<%=port.getPort() %>" />
	                    </div></td>
	                  <td style="width:231px"><div class="datagrid-cell"><%=port.getName() %></div></td>
	                  <td style="width:231px"><div class="datagrid-cell"><%=port.getProtocolName() %></div></td>
	                  <td style="width:230px"><div class="datagrid-cell"><%=port.getPort() %></div></td>
	                </tr>
              	   
              	   <% 
                   }
                %>                  
              </table>
            </div>
          </div>
          <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" onclick="nextstep();getComfirmInfo();">下一步</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a>
         <div id="description_5">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i><br/>
              地域/成都　CPU/4核　内存/2G　硬盘/2048G　带宽/9M
            </div></div>
        </li>
          
          
          <li>
            <div style="width:658px; padding:30px 30px 15px 30px; border:solid 1px #b2b2b2" id="comfirm">
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">主机名称</span></div>
              <div class="l" style="width:538px;height:30px;margin:0 0 15px 0;font-size:14px;line-height:30px; position:relative;">
                <input id="t1" name="" type="text" value="Host_1" class="einput"/>
                <label for="t1" class="ebutton">
                <div class="l">Host_1</div>
                <div class="l"><img src="<%=request.getContextPath() %>/image/icon_edit.png" width="20" height="20" /></div>
                </label>
              </div>
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">地域</span></div>
              <div class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">广州</div>
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">基本配置</span></div>
              <div class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">CPU/4核　内存/2G　硬盘/2048G　带宽/9M</div>
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">镜像</span></div>
              <div class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">CentOS 7 64bit</div>
              <div class="clear"></div>
              <div id="confirm1">
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">独立 IP</span></div>
              <div class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;">我们将为您的云主机免费提供一个独立 IP</div>
              <div class="clear"></div>
            </div>
            <div id="confirm2" style="display:none">
              <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 15px 0">端口</span></div>
              <div class="l" style="width:538px;margin:0 0 15px 0;font-size:14px;line-height:30px;"><b>4</b>个端口已启用：3389,22,80,81,3389,22,80,81,3389,22,80,81...</div>
              <div class="clear"></div>
            </div>
            <div class="clear"></div>
              <div class="clear"></div>
            </div>
            <div class="buttonbar"><a class="bluebutton r" href="javascript:void(0);"  id="goCreate">确认生成</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a>
            <div id="description_3"> 
            <i style="line-height:40px;">
                               单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i></div>
            </div>
          </li>
          <li>
            <div class="infoicon" style="font-size:16px; font-weight:bold" id="cloudhostcreateprogress">创建中</div>
            <div id="progressbar_id">
            <div id="progressbar" class="easyui-progressbar" value="0"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div>
            </div>
            <div class="info" id="description_4">当前余额：<strong>370.12元</strong>，您的主机还可使用：<strong style="color:#4cda64">3个月23天</strong></div>
            <div style="width:720px; margin:30px auto 0 auto;line-height:30px; text-align:right;"> <span class="bluebutton topradius l">立即充值</span><b>优惠：现在充值一年只需支付10个月的费用</b>
              <div class="clear"></div>
            </div>
            <div style="width:700px; margin:0 auto; background:#e2e2e2; border-top:solid 1px #b2b2b2; padding:20px 0 20px 20px;">
                
              <input name="userType" value="4" type="hidden"/>
              <input name="bean" value="accountBalanceService" type="hidden"/>
              <input name="method" value="toRechargeGiftPage" type="hidden"/>
              <input name="monthlyPrice" id="monthlyPrice" type="hidden"/> 
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
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" id="finish">进入主机列表</a><a class="bluebutton r" href="javascript:void(0);" id="goRechargeRightNow">立即充值</a><i class="r" style="line-height:40px;">充值金额：<strong id="price">600.00元</strong></i></div>
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
	<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
  <div id="month_form">
  </div>
</div>
</body>
</html>
