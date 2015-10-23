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
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.VPCAmountAndPrice"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<CloudHostVO> hostList  = (List<CloudHostVO>)request.getAttribute("hostList");
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
	String vpcName = (String)request.getAttribute("vpcName");
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
	if(vpcName==null||vpcName==""){
		vpcName = "未分配";
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>

 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/createvpchost.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/plugins/jquery.blockUI.js"></script> 

<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript">
var totalPrice='<%=totalPrice%>';
var vpcName = '<%=vpcName%>';
var balance='<%=balance%>';
var name = '<%=name%>';
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
	if(name==""){
		inituser();
		init();
	}else{
		init(10,3);
		inituser(name,0);
	}
	initstep(1); 
	$("#hostPage").hide();
	$("#ipAmount").spinner({  
		onSpinUp:function(){
			getPriceInfo();
		} ,
		onSpinDown:function(){
			getPriceInfo();
		}
	 });
	$("#hostAmount").spinner({  
		onSpinUp:function(){
			getDescriptionAndPrice();
		} ,
		onSpinDown:function(){
			getDescriptionAndPrice();
		}
	 });
	
});

function page(id){
	var show_per_page = 18; 
	var number_of_items = $('#'+id).children().size();
	if(number_of_items>9){
		$("#hostPage").show();
	}else{
		$("#hostPage").hide();
	}
	var number_of_pages = Math.ceil(number_of_items/show_per_page);
	$('#current_page').val(0);
	$('#show_per_page').val(show_per_page);
	$('#number_of_pages').val(number_of_pages);
	if(number_of_pages>1){
	var navigation_html = '<a class="arrow" onclick="javascript:previous()"><img src="<%=request.getContextPath()%>/image/button_prev_disabled.gif" width="16" height="16" alt="上一页" /></a>';
	var current_link = 0;
	while(number_of_pages > current_link){
		navigation_html += '<a class="num" onclick="javascript:go_to_page(' + current_link +')" longdesc="' + current_link +'">'+ (current_link + 1) +'</a>';
		current_link++;
	}
	navigation_html += '<a class="arrow" onclick="javascript:next()"><img src="<%=request.getContextPath()%>/image/button_next.gif" width="16" height="16" alt="下一页" /></a>';
	$('.pagebar .page').html(navigation_html);
	$('.pagebar .page .num:first').addClass('active');
	$('#createHostSelected').children().css('display', 'none');
	$('#createHostSelected').children().slice(0, show_per_page).css('display', 'block');	
	}
}
function previous(){
	new_page = parseInt($('#current_page').val()) - 1;
	if($('.pagebar .page .active').prev('.num').length==true){
		go_to_page(new_page);
	}
}
function next(){
	new_page = parseInt($('#current_page').val()) + 1;
	//if there is an item after the current active link run the function
	if($('.pagebar .page .active').next('.num').length==true){
		go_to_page(new_page);
	}
}
function go_to_page(page_num){
	var show_per_page = parseInt($('#show_per_page').val());
	start_from = page_num * show_per_page;
	end_on = start_from + show_per_page;
	$('#createHostSelected').children().css('display', 'none').slice(start_from, end_on).css('display', 'block');
	$('.num[longdesc=' + page_num +']').addClass('active').siblings('.active').removeClass('active');
	$('#current_page').val(page_num);
	if(page_num<=0){
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev_disabled.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next.gif");
	}else if (parseInt($('#number_of_pages').val())-1 <= page_num){
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next_disabled.gif")
	}else{
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next.gif")
	}
}
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
function showHosts(){	
	jQuery.blockUI({ message: jQuery("#portbox"),  
	        css: {border:'3px solid #aaa',
	              backgroundColor:'#FFFFFF',
	              overflow: 'hide',
	              width: '60%', 
	              height: 'auto', 
	              left:'20%',
	              top:'20%'} 
	        }); 
	jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);  
}
function showCreateHosts(){	
	jQuery.blockUI({ message: jQuery("#ddfff"),  
	        css: {border:'3px solid #aaa',
	              backgroundColor:'#FFFFFF',
	              overflow: 'hide',
	              width: '70%', 
	              height: 'auto', 
	              left:'15%',
	              top:'10%'} 
	        }); 
	jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);  
}

function getNewCreate(){
	if((parseInt(countHostAmount())+parseInt($("#hostAmount").val()) )>15){
		top.$.messager.alert('警告','一个专属云最多可配置15个主机，请重新配置','warning'); 
		return;
	}
 	var iteminfo="";
	var sysImageName="";
	var ports = "";
	var chkObjs = document.getElementsByName("item");
    var item = "";
    var bandwidth = "";
    var memory = "";
    var cpu = "";
    var dataDisk = "";
    var monthlyprice = 0;
    var hourprice = "";
    var flag = true;  
    var free = "";
    var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    } 
    var region_info; 
    if(region_val=="1"){
    	region_info = "广州";
    }else if(region_val=="2"){
    	region_info = "成都";
    	
    }else if(region_val=="4"){
    	region_info = "香港";
    	
    } 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	if(chkObjs[i].value==""){
        		break;
        	}
        	item = chkObjs[i];       	 
        	cpu = $(item).attr("cpu");
        	memory = $(item).attr("memory");
        	dataDisk = $(item).attr("disk");
        	bandwidth = $(item).attr("bandwidth");    	 
        	monthlyprice = $(item).attr("price");  
        	item = $(item).val();  
        	flag = false;
            break;
        }
    }
    if(flag){  
    	chkObjs = document.getElementsByName("cpu");
    	var cpu = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			cpu = chkObjs[i].value;
    			break;
    		}
    	}
    	chkObjs = document.getElementsByName("memory");
    	var memory = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			memory = chkObjs[i].value;
    			break;
    		}
    	}
    	var diy = "true";
    	chkObjs = document.getElementsByName("dataDisk");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			dataDisk = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	} 
    	if(diy=="true"){
    		dataDisk = $("#dataDisk").val().replace("G","");
    	}
    	diy = "true";
    	chkObjs = document.getElementsByName("bandwidth");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			bandwidth = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	}
    	if(diy=="true"){
    		bandwidth = $("#bandwidth").val().replace("M","");
    	}
    }
    var sysImageId = $("#sysImageId").combobox('getValue');
    var uuid = UUID.prototype.createUUID ();
    iteminfo1 = $("#hostAmount").val()+"个:"+cpu+"核 /"+memory+"G/"+dataDisk+"G/"+bandwidth+"M"; 
    iteminfo2 = $("#hostAmount").val()+"个:"+$('#sysImageId').combobox('getText')+"/"+cpu+"核 /"+memory+"G/"+dataDisk+"G/"+bandwidth+"M"; 
    var value = $("#hostAmount").val()+"/"+cpu+"/"+memory+"/"+dataDisk+"/"+bandwidth+"/"+sysImageId+"/"+item;
    var str = '<input name="createhostinfo" id="host_'+uuid+'"  class="checkbox" type="checkbox" value="'+value+'" onchange="getPriceInfo();" />'+
              '<label for="host_'+uuid+'" class="biglabel l" title="'+iteminfo2+'">'+iteminfo1+'</label>';
             
    $("#createHostSelected").append(str);
     $("#host_"+uuid).change(function(){ 
	    $(".checkbox").next("label").removeClass("checked");
		$(".checkbox:checked").next("label").addClass("checked");
	});
     $("#host_"+uuid).click();
    page('createHostSelected');
    getPriceInfo();
    $.unblockUI();
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
  <div class="g-doc">
  <div class="pageleft">
    <jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
    <div class="main">
    		<div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
      <div class="titlebar"><a href="javascript:void(0);"  onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">创建专属云</div>
      </div> 
      <br></br>
      <form class="wizard" id="cloud_host_config_form"  action="<%=request.getContextPath()%>/bean/page.do" target="_blank" method="post">
        <ul id="wizardbox" style="width:5000px;heigth:900px">
          <li>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">专属云名称</span></div> 
            
            <div class="l" style="width:600px;">
            <input id="vpcname" name="vpcname" type="text" value="<%=vpcName %>" class="einput"  style="margin-left:270px;" onblur="$('#vpcname_display').html($(this).val());checkVpcInfo();"/>
                <label for="vpcname" class="ebutton" style="margin-left:270px;">
                <div class="l" id="vpcname_display"><%=vpcName %></div>
                <div class="l"><img src="<%=request.getContextPath() %>/image/icon_edit.png" width="20" height="20" /></div>
                </label> 
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px; margin:0 15px 10px 0">地域选择</span></div>
            <div class="l" style="width:600px;">
              <input name="region" id="region1" class="radio" type="radio"   value="1" checked="checked" />
              <label for="region1" class="smalllabel l">广州</label>
              <input name="region" id="region4" class="radio" type="radio"  value="4" />
              <label for="region4" class="smalllabel l">香港</label>
              <input name="region" id="region2" class="radio" type="radio"   value="2" />
              <label for="region2" class="smalllabel l">成都</label>
            </div>
            <div class="clear"></div>
            <div id="hostSelect">
            
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px; margin:0 15px 10px 0">选择已有主机</span></div>
            <div class="l" style="width:600px;display:inline; " id="hostSelected"> 
                  <div id="host_region_1">
                  <%
 	              for(CloudHostVO vo : hostList){
 	            	  if(vo.getRegion() != 1){
 	            		  continue;
 	            	  }
 	          	  %>
	              <input name="hostId" id="host_<%=vo.getId() %>"  class="checkbox" type="checkbox" onchange="if(countHostAmount()>15){$(this).click();}else{getPriceInfo();}" value="<%=vo.getId() %>" />
	              <label style="width: 192px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" for="host_<%=vo.getId() %>" class="biglabel l" title=" <%=vo.getDisplayName() %> - <%=vo.getCpuCore()%>核/<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G/<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G/<%=CapacityUtil.toMBValue(vo.getBandwidth(), 0) %>M">
	              <%=vo.getDisplayName() %>      
	              </label>
	              <%
	              }
	              %>
	               
              </div>
              <div id="host_region_2" style="display:none;">
	              <%
 	              for(CloudHostVO vo : hostList){
 	            	  if(vo.getRegion() != 2){
 	            		  continue;
 	            	  }
 	          	  %>
	              <input name="hostId" id="host_<%=vo.getId() %>"  class="checkbox" type="checkbox" onchange="getPriceInfo();" value="<%=vo.getId() %>" />
	              <label style="width: 192px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" for="host_<%=vo.getId() %>" class="biglabel l" title=" <%=vo.getDisplayName() %> - <%=vo.getCpuCore()%>核/<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G/<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G/<%=CapacityUtil.toMBValue(vo.getBandwidth(), 0) %>M">
	              <%=vo.getDisplayName() %>      
	              </label>
	              <%
	              }
	              %>
	               
              </div>
              <div id="host_region_4" style="display:none"> 
                 <%
 	              for(CloudHostVO vo : hostList){
 	            	  if(vo.getRegion() != 4){
 	            		  continue;
 	            	  }
 	          	  %>
	              <input name="hostId" id="host_<%=vo.getId() %>"  class="checkbox" type="checkbox" onchange="getPriceInfo();" value="<%=vo.getId() %>" />
	              <label style="width: 192px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" for="host_<%=vo.getId() %>" class="biglabel l" title=" <%=vo.getDisplayName() %> - <%=vo.getCpuCore()%>核/<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G/<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G/<%=CapacityUtil.toMBValue(vo.getBandwidth(), 0) %>M">
	              <%=vo.getDisplayName() %>      
	              </label>
	              <%
	              }
	              %>
              </div>
              
              
                </div>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px; margin:0 15px 10px 0">创建主机</span></div>
            <div class="l" style="width:600px;display:inline; " id="createHostSelected">
               <a  class="biglabel l" href="javascript:void(0);"     onclick="showCreateHosts();">创建主机</a>
              </div>
            <div class="clear"></div>
            <div class="pagebar" id="hostPage">
              <div class="page"></div>
              <input type='hidden' id='current_page' />
              <input type='hidden' id='show_per_page' />
              <input type='hidden' id='number_of_pages' />
            </div>
            
             <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0;">申请IP个数</span></div>
            <div class="l" style="width:600px;"><input class="easyui-numberspinner" onchange="getPriceInfo();" precision="0" min="1" id="ipAmount" name="ipAmount" value="1" increment="1" style="height:28px; width:100px; margin-left:5px;text-align:center;"/>
            <lable id="for_description" style="color:red;font-size:13px;"></lable>
            </div>
             
              
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">描述</span></div>
            <div class="l" style="width:600px;"><input id="description"   name="description" onblur="checkVpcInfo();" type="text" class="textbox" style="height:28px; width:260px; margin-left:5px;"/>
            <lable id="for_description" style="color:red;font-size:13px;"></lable>
            </div>
             
              
            <div class="clear"></div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a   class="disabledbutton r" href="javascript:void(0);"   id="goCreate">确认创建</a>
            <div id="description_1">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i>&nbsp;(主机/3个　IP/4个)<br/>
             </div>
              
              </div>
          </li> 
          
          <li>
            <div class="infoicon" style="font-size:16px; font-weight:bold" id="cloudhostcreateprogress">创建成功</div>
<!--             <div id="progressbar_id"> -->
<!--             <div id="progressbar" class="easyui-progressbar" value="0"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div> -->
<!--             </div> -->
            <div class="info" id="description_4">当前余额：<strong><%=balance %>元</strong></div>
            <div style="width:720px; margin:30px auto 0 auto;line-height:30px; text-align:right;"> <span class="bluebutton topradius l">立即充值</span> 
              <div class="clear"></div>
            </div>
            <div style="width:700px; margin:0 auto; background:#e2e2e2; border-top:solid 1px #b2b2b2; padding:20px 0 20px 20px;">
                
              <input name="userType" value="4" type="hidden"/>
              <input name="bean" value="accountBalanceService" type="hidden"/>
              <input name="method" value="toRechargePage" type="hidden"/>
               <input name="amount" id="rh1" class="radio" type="radio" value="100"  checked="checked"/>
              <label for="rh1" class="biglabel l" style="margin:0 20px 0 0;width:153px">100</label>
              <input name="amount" id="rh2" class="radio" type="radio" value="500" />
              <label for="rh2" class="biglabel l" style="margin:0 20px 0 0;width:153px">500</label>
              <input name="amount" id="rh3" class="radio" type="radio" value="1000"  />
              <label for="rh3" class="biglabel l" style="margin:0 20px 0 0;width:153px">1000</label>
              <div class="custom biglabel l" style="border:none; margin:0; width:153px;">
                <input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" id="rh" name="amountDIY" type="text" class="biglabel" title="月" style="margin:0 20px 0 0;width:153px"/>
                <label for="rh" class="biglabel l" style="margin:0 20px 0 0;width:153px">自定义(月)</label>
              </div>
              
              <div class="clear"></div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" onclick="window.location.href='<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=managePage'">进入专属云列表</a><a class="bluebutton r" href="javascript:void(0);" id="goRechargeRightNow">立即充值</a><i class="r" style="line-height:40px;">充值金额：<strong id="price">100元</strong></i></div>
          </li>
          
          <div id="ddfff" style="margin:10px;display:none;margin-left:70px;">
             
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">创建主机个数</span></div>
            <div class="l" style="width:100px; align:left;"><input class="easyui-numberspinner" id="hostAmount" name="hostAmount" onchange="getDescriptionAndPrice();" precision="0" min="1" value="1" increment="1" style="height:28px; width:100px; margin-left:5px;text-align:center;"/>
            <lable id="for_vpchosts" style="color:red;font-size:13px;"></lable>
            </div>
            <div class="clear"></div>
             <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">推荐配置</span></div>
            <div class="l" style="width:600px;">
              <div id="package1">
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
            <div id="package2" style="display:none;">
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
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">CPU</span></div>
            <div class="l" style="width:600px;">
            <div id="cpuPackage1">
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
            <div id="cpuPackage2" style="display:none;">
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
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">内存</span></div>
            <div class="l" style="width:600px;">
            <div id="memoryPackage1">
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
            <div id="memoryPackage2" style="display:none;">
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
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
            <div id="diskPackage1">
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
            <div id="diskPackage2" style="display:none;">
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
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">带宽</span></div>
            <div class="l" style="width:600px;">
            <div id="bandwidthPackage1">
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
            <div id="bandwidthPackage2" style="display:none;">
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
            
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">镜像</span></div>
            <div class="l" style="width:210px;"> 
               <select class="easyui-combobox" id="sysImageId" name="sysImageId" data-options="width:'200',height:'30',panelHeight:'auto',editable:false"> 
                </select>
                
               
            </div>
                
              <div class="clear"></div>
             
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2">
            <br/>
            <a class="bluebutton r" href="javascript:void(0);" onclick="getNewCreate();" style="margin-left:10px;">确认添加</a> &nbsp;&nbsp;
            <a class="graybutton r" href="javascript:void(0);" onclick="$.unblockUI();">取消</a> 
            <div id="description_2" style="width:300px;margin-top:-30px;text-align:left;">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i>&nbsp;(主机/3个　IP/4个)
             </div>
              </div> 
          </div>
          
          
           
          
          
           
          
        </ul>
      </form>
    </div>
    <div class="clear"></div>
            		<jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>

  </div>
  </div>
    <jsp:include page="/src/common/tpl/u_login.jsp"></jsp:include>
  <div class="pageright">
	<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
  <div id="month_form">
  </div>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	navHighlight("umc","umec"); 
});
</script>
</body>
</html>
