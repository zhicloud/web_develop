<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
	String userId = loginInfo.getUserId();
	List<CloudHostVO> myCloudHostList = (List<CloudHostVO>)request.getAttribute("myCloudHostList");
	List<Map<String,String>> realHostIdList = (List<Map<String,String>>)request.getAttribute("realIdList");
	String tips = (String)request.getAttribute("tips");
	String message = "";
	String newRegion = (String)request.getAttribute("region");
	if(myCloudHostList.isEmpty()){
		message = "没有相关记录";
	}else{
		message = "共找到"+myCloudHostList.size()+"条相关记录";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><%=AppConstant.PAGE_TITLE %></title>
	<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/goup.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/src/common/js/common.js"></script>
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
        <div class="tabbar l"><a id="all_cloud_host" name="cloud_host" region="0" href="javascript:void(0);" class="active">全部</a>　｜　<a id="guangzhou_cloud_host" region="1" name="cloud_host" href="javascript:void(0);">广州</a>　｜　<a id="hongkang_cloud_host" region="4" name="cloud_host" href="javascript:void(0);">香港</a> &nbsp;&nbsp; | &nbsp;&nbsp; <a id="chengdu_cloud_host" region="2" name="cloud_host" href="javascript:void(0);">成都</a></div>
        <a href="<%=request.getContextPath() %>/buy.do?flag=buy" style="cursor:pointer;" class="greenbutton r">创建云主机</a></div>
         <div style="width:980px;padding: 16px 0 0 0; margin:0 auto;">
        <div style="display:none">
        <div class="sa l" style="font-size:14px; padding-left:7px;"><input id="sa" name="" type="checkbox" value="" onchange="selectall();" /><label for="sa"> 全选</label></div>
        <div class="sa l" style="font-size:14px; padding-left:113px;"><a id="restart-all" href="javascript:void(0);" restart-all="1">重启</a><a id="shutdown-all" href="javascript:void(0);" shutdown-all="1">关机</a><a id="start-all" href="javascript:void(0);" start-all="1">开机</a>
        </div>
        <div class="clear"></div>
        </div>
        <div class="clear"></div>
      </div>
      <div id="one_cloud_host" style="width:980px;padding: 6px 0 0 0; margin:0 auto;">
        <table id="my_warehouse_datagrid" class="easyui-datagrid" data-options="border:false,showHeader:false,scrollbarSize:0,width:980">
          <thead>
            <tr>
              <th data-options="field:'data',width:980"><%=message %></th>
            </tr>
          </thead>
          <tbody>
          <%
		 	if(myCloudHostList.isEmpty()){
		  %>
		 	<center>没有相关记录</center>
          <%
		 	}
          	for(CloudHostVO cloudHost : myCloudHostList){
          %>
            <tr>
              <td><div class="box" cloudHostId="<%=cloudHost.getId()%>" ip="<%=cloudHost.getOuterIp() %>" password="<%=cloudHost.getPassword()%>">
                  <div class="listleft l">
                    <div class="div1 l">
                     <%if(cloudHost.getType()==2){%>
                    	 <img src="<%=request.getContextPath() %>/image/icon_agent.png" width="20" height="20" class="agenticon" />
                     <%} %>
                      <div class="listicon">
                      <%if(cloudHost.getSysImageName()!=null){ %>
	                      <%if(cloudHost.getSysImageName().contains("indows7")){%>
	                      		<a href="javascript:void(0);" class="newbtn windows7" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2003")){%>
	                    	  	<a href="javascript:void(0);" class="newbtn windows2003" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2008")){%>
	              	      		<a href="javascript:void(0);" class="newbtn windows2008" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2012")){%>
	        	      			<a href="javascript:void(0);" class="newbtn windows2012" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	          	      	  <%}else if(cloudHost.getSysImageName().contains("entos6.4") || cloudHost.getSysImageName().contains("entos_6.4")){%>
	                  	  		<a href="javascript:void(0);" class="newbtn centos6-4" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("entos6.5") || cloudHost.getSysImageName().contains("entos_6.5")){%>
	            	      		<a href="javascript:void(0);" class="newbtn centos6-5" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("buntu12.04") || cloudHost.getSysImageName().contains("buntu_12.04")){%>
	      	      				<a href="javascript:void(0);" class="newbtn ubuntu12-04" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	        	      	  <%}else if(cloudHost.getSysImageName().contains("buntu14.04") || cloudHost.getSysImageName().contains("buntu_14.04")){%>
    	      					<a href="javascript:void(0);" class="newbtn ubuntu14-04" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
  	        	      	  <%}else{%>
		                        <a href="javascript:void(0);" class="newbtn default-system" title="未知系统">&nbsp;</a>
		                  <%} %>
	                   <%}else{ %>
	                   	  <%if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows7")){%>
	                      		<a href="javascript:void(0);" class="newbtn windows7" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2003")){%>
	                    	  	<a href="javascript:void(0);" class="newbtn windows2003" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2008")){%>
	              	      		<a href="javascript:void(0);" class="newbtn windows2008" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2012")){%>
	        	      			<a href="javascript:void(0);" class="newbtn windows2012" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	          	      	  <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("entos6.4") || cloudHost.getSysImageNameOld().contains("entos_6.4"))){%>
                	  			<a href="javascript:void(0);" class="newbtn centos6-4" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("entos6.5") || cloudHost.getSysImageNameOld().contains("entos_6.5"))){%>
	          	      			<a href="javascript:void(0);" class="newbtn centos6-5" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("buntu12.04") || cloudHost.getSysImageNameOld().contains("buntu_12.04"))){%>
	    	      				<a href="javascript:void(0);" class="newbtn ubuntu12-04" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	      	      	  	  <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("buntu14.04") || cloudHost.getSysImageNameOld().contains("buntu_14.04"))){%>
		      					<a href="javascript:void(0);" class="newbtn ubuntu14-04" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
        	      	  <%}else{%>
	                        	<a href="javascript:void(0);" class="newbtn default-system" title="未知系统">&nbsp;</a>
	                       <%} %>
	                   <%} %>
                        <%if("创建中".equals(cloudHost.getSummarizedStatusText()) || "创建失败".equals(cloudHost.getSummarizedStatusText())){ %>
                       		<div class="text">
	                          <input id="t1<%=cloudHost.getId() %>" name="" disabled='disabled' title="<%=cloudHost.getDisplayName() %>" type="text" value="<%=cloudHost.getDisplayName()%>"/>
	                        </div>
                        <%}else{ %>
                         <div class="text">
<%--                           <input id="t1<%=cloudHost.getId() %>" class="host_name" onblur="changeHostName()" maxlength="100" name="" type="text" value="<%=cloudHost.getDisplayName()%>"/> --%>
                          <input id="t1<%=cloudHost.getId() %>" class="host_name"  onblur="$(this).next('label').css('left','0');changeHostName()"  maxlength="100" name="" type="text" value="<%=cloudHost.getDisplayName()%>"/>
                          <label for="t1<%=cloudHost.getId() %>" onclick="$(this).css('left','-99999px');" title="<%=cloudHost.getDisplayName() %>" class="smalllabel l" style="cursor:pinter; background:url(<%=request.getContextPath()%>/image/icon_edit.png) no-repeat center right"></label>
                         </div>
                        <%} %>
                      </div>
                    </div>
                    <div class="div2 l">
                    <div class="listicon l" style="width:80px">
                        <div class="btn btn1">&nbsp;</div>
                        <div class="text"><%=cloudHost.getCpuCore()%>核</div>
                      </div>
                      <div class="listicon l" style="width:80px">
                        <div class="btn btn2">&nbsp;</div>
                        <div class="text"><%=cloudHost.getMemoryText(0)%></div>
                      </div>
                      <div class="listicon l" style="width:80px">
                        <div class="btn btn3">&nbsp;</div>
                        <div class="text"><%=cloudHost.getDataDiskText(0)%></div>
                      </div>
                    </div>
                    <%if("关机".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div id="cloud-status-id<%=cloudHost.getId()%>" class="div3 l"><strong class="gray">已关机</div>
                    <%}else if("运行".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div id="status<%=cloudHost.getId()%>" class="div3 l">运行中</div>
                    <%}else if("创建中".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建中</div>
                    <%}else if("创建失败".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建失败</div>
                    <%}else if("告警".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="orange">告警</div>
                    <%}else if("故障".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="red">故障</div>
                    <%}else if("停机".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">已停机</div> 
                    <%}else if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN){ %>
                    <div id="status<%=cloudHost.getId()%>" class="div3 l">关机中...</div> 
                    <%}else if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING){ %>
                    <div id="status<%=cloudHost.getId()%>" class="div3 l">开机中...</div>
                    <%}else if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING){ %>
                    <div id="status<%=cloudHost.getId()%>" class="div3 l">重启中...</div>
                    
                    <%} %>
                    <%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
                    	<div class="div4 l"><div class="l" style="margin-left:24px;" title="外网IP:<%=cloudHost.getOuterIp()==null ? "" : cloudHost.getOuterIp()%> 端口:<%=cloudHost.getOuterPort()==null ? "" : cloudHost.getOuterPort()%>(致云客户端监控端口) "><img src="<%=request.getContextPath() %>/image/icon_ip.png" width="20" height="20" alt=" " style="vertical-align:middle"/> <%=StringUtil.isBlank(cloudHost.getOuterIp()) ? "" : cloudHost.getOuterIp() + ":" + (cloudHost.getOuterPort()==null ? "" : cloudHost.getOuterPort())%></div>
                        <div class="r" style="margin-right:24px;"><img src="<%=request.getContextPath() %>/image/icon_bandwidth.png" width="20" height="20" alt=" " style="vertical-align:middle" /><%=CapacityUtil.toMBValue(cloudHost.getBandwidth(), 0) %>MB/s</div>
                    </div>
                    <%} else{%>
                    <div class="div4 l"><div class="l" style="margin-left:24px;" title="外网IP:<%=cloudHost.getOuterIp()==null ? "" : cloudHost.getOuterIp()%> "><img src="<%=request.getContextPath() %>/image/icon_ip.png" width="20" height="20" alt=" " style="vertical-align:middle"/> <%=cloudHost.getOuterIp()==null ? "" : cloudHost.getOuterIp()%></div>
                      <div class="r" style="margin-right:24px;"><img src="<%=request.getContextPath() %>/image/icon_bandwidth.png" width="20" height="20" alt=" " style="vertical-align:middle" /><%=CapacityUtil.toMBValue(cloudHost.getBandwidth(), 0) %>MB/s</div>
                    </div>
                    <%} %>
                  </div>
                  <%if("关机".equals(cloudHost.getSummarizedStatusText())) {%>
                  	<div class="listright l" name="a<%=cloudHost.getId()%>">
	                  <a id="allStart<%=cloudHost.getId()%>" href="javascript:void(0);" class="btn btn1 l launch_host" title="开机" startHost="1">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn2 alpha l shutdown_host" title="关机" stopHost="2">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn3 alpha l restart_host" title="重启" restartHost="2">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btnreset alpha l force_restart_host" title="强制重启" restartHost="2">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn4 l alpha halt_host" title="强制关机" haltHost="2">&nbsp;</a>
<%--  	                  <a href="javascript:void(0);" class="btn btnbackup l backup_host" title="备份" id = "backup<%=cloudHost.getId() %>" uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" backup="1">&nbsp;</a> --%>
<%-- 					  <a href="javascript:void(0);" class="btn btnreset l reset_host" title="重装系统" id="reset<%=cloudHost.getId() %>" uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" reset="1">&nbsp;</a> --%>
	                  <%if(cloudHost.getType()==2){ %>
	                  	<a href="javascript:void(0);" class="btn newbtn5 alpha l" title="停用">&nbsp;</a>
	                  	<a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>	                  	
	                  	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>	                  	
	                  <%}}else{ %>
	                  	<a id="outOfService<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn newbtn5 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
	                  	<a id="allocation<%=cloudHost.getId()%>" href="javascript:void(0);" class="btn btn7 l modify-allocation" title="修改配置" modifyAllocation="1">&nbsp;</a>
	                  	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>	
	                  <%}} %>
	                  <a href="javascript:void(0);" class="graybutton r my_cloud_host_detail"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                    </div>	
					<%}else if("运行".equals(cloudHost.getSummarizedStatusText())) {%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a id="start<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn1 alpha l launch_host" title="开机" startHost="2">&nbsp;</a>
		                  <a id="shutdown<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn2 l shutdown_host" title="关机" stopHost="1">&nbsp;</a>
		                  <a id="restart<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn3 l restart_host" title="重启" restartHost="1">&nbsp;</a>
		                  <a id="forcerestart<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btnreset l force_restart_host" title="强制重启" restartHost="1">&nbsp;</a>
		                  <a id="halt<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn4 l halt_host" title="强制关机" haltHost="1">&nbsp;</a>
<%-- 							<a href="javascript:void(0);" class="btn btnbackup alpha l backup_host" title="备份" id = "backup<%=cloudHost.getId() %>" uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" backup="2">&nbsp;</a> --%>
<%-- 							<a href="javascript:void(0);" class="btn btnreset alpha l reset_host" title="重装系统" id="reset<%=cloudHost.getId() %>"  uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" reset="2">&nbsp;</a> --%>
<!-- 		                  <a href="javascript:void(0);" class="graybutton l enter_my_cloud_host"><div class="r">进入主机</div><div class="btn10 l">&nbsp;</div></a> -->
		                  <%if(cloudHost.getType()==2){ %>
		                  	<a href="javascript:void(0);" class="btn newbtn5 alpha l" title="停用">&nbsp;</a>
		                    <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置" modifyAllocation="2">&nbsp;</a>
		                    <%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
		                  <%}}else{ %>
		                  	<a id="outOfService<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn newbtn5 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
		                    <a id="modify<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn7 l alpha modify-allocation" title="修改配置" modifyAllocation="2">&nbsp;</a>
		                 	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
		                  <%}} %>
		                  <a href="javascript:void(0);" class="graybutton r my_cloud_host_detail"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}else if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN || cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING || cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING) {%>
					<div class="listright l" name="a<%=cloudHost.getId()%>">
	                  <a id="start<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn1 alpha l launch_host" title="开机" startHost="2">&nbsp;</a>
	                  <a id="shutdown<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn2 alpha l shutdown_host" title="关机" stopHost="2">&nbsp;</a>
	                  <a id="restart<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn3 alpha l restart_host" title="重启" restartHost="2">&nbsp;</a>
	                  <a id="forcerestart<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btnreset alpha l force_restart_host" title="强制重启" restartHost="2">&nbsp;</a>
	                  
 	                  <a id="halt<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn4 alpha l halt_host" title="强制关机" haltHost="2">&nbsp;</a>
<%-- 						<a href="javascript:void(0);" class="btn btnbackup alpha l backup_host" title="备份" id = "backup<%=cloudHost.getId() %>" uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" backup="2">&nbsp;</a> --%>
<%-- 						<a href="javascript:void(0);" class="btn btnreset alpha l reset_host" title="重装系统" id="reset<%=cloudHost.getId() %>" uuid="<%=cloudHost.getRealHostId()%>" region="<%=cloudHost.getRegion()%>" reset="2">&nbsp;</a> --%>
<!-- 		                  <a href="javascript:void(0);" class="graybutton l enter_my_cloud_host"><div class="r">进入主机</div><div class="btn10 l">&nbsp;</div></a> -->
	                  <%if(cloudHost.getType()==2){ %>
	                  	<a href="javascript:void(0);" class="btn newbtn5 alpha l" title="停用">&nbsp;</a>
	                    <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置" modifyAllocation="2">&nbsp;</a>
	                    <%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
	                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
	                  <%}}else{ %>
	                  	<a id="outOfService<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn newbtn5 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
	                    <a id="modify<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn7 l alpha modify-allocation" title="修改配置" modifyAllocation="2">&nbsp;</a>
	                 	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
	                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
	                  <%}} %>
	                  <a href="javascript:void(0);" class="graybutton r my_cloud_host_detail"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
           	 	</div>
				<%}else if("创建中".equals(cloudHost.getSummarizedStatusText())){%>
						<div class="listright l">
		                    <div class="listinfo">您的主机正在创建中，预计3分钟后完成创建。</div>
		                    <div id="<%=cloudHost.getId() %>" style="width:240px;margin-left:20px;margin-top:0px;" text="云主机创建中({value}%)"></div>
	               		</div>
					<%}else if("停机".equals(cloudHost.getSummarizedStatusText())){%>
						<%if(cloudHost.getType()==2){ %>
							<div class="listright l">
								<div class="listinfo">
									您的主机已停用，请联系代理商。
								</div>
							</div>
						<%}else{ %>
							<div class="listright l">
			                    <div class="listinfo">您的主机已停用，我们将为您保存数据至 
			                    <%
			                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			                    Date date = StringUtil.stringToDate(cloudHost.getInactivateTime(),"yyyyMMddHHmmssSSS");
			                    Calendar calendar = Calendar.getInstance();
			                    calendar.setTime(date);
			                    calendar.add(Calendar.DAY_OF_MONTH,7);
			                    %>
			                    <%=sdf.format(calendar.getTime())%>
			                  	  ，<br />到期主机及数据将会删除。</div>
			                    <a href="javascript:void(0);" class="graybutton r reactivate"><div class="r">恢复服务</div><div class="btn11 l">&nbsp;</div></a>
		           			</div>
	           			<%} %>
					<%}else if("创建失败".equals(cloudHost.getSummarizedStatusText())){%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a href="javascript:void(0);" class="btn btn1 alpha l" title="开机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn2 alpha l" title="关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn3 alpha l" title="重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btnreset alpha l" title="强制重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn4 l alpha" title="强制关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn5 l delete-cloud-host" title="删除">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>
		                  <a href="javascript:void(0);" class="graybutton r alpha"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}else{%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a href="javascript:void(0);" class="btn btn1 alpha l" title="开机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn2 alpha l" title="关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn3 alpha l" title="重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btnreset alpha l" title="强制重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn4 l halt_host" title="强制关机" haltHost="1">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn newbtn5 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>
		                  <a href="javascript:void(0);" class="graybutton r alpha"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}%>
                </div>
                </td>
            </tr>
            <%
          	}
            %>
          </tbody>
        </table>
         <div class="bottombar"> <a href="<%=request.getContextPath() %>/download/<%=AppProperties.getValue("client_version_name", "")%>"><img src="<%=request.getContextPath() %>/image/guide_down.png" width="20" height="20" />下载致云客户端</a>　　　<a href="javascript:void();" onclick="showbrief();"><img src="<%=request.getContextPath() %>/image/guide_guide.png" width="20" height="20" />新手引导</a></div>
<!--         <div class="box"><div class="easyui-pagination" data-options="showPageList: false,showRefresh: false,displayMsg: '',beforePageText: '',afterPageText: ''"></div></div> -->
      </div>
    </div>
    <div class="clear"></div>
    <jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>
  </div>
  <%-- <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div> --%>
</div>
<div class="briefbg" style="display:none">
  <div class="brief">
    <div style="height:16px; padding:10px;float:right;"><a href="javascript:void(0);" onclick="hidebrief();"><img id="brief2" class="swapimage" src="<%=request.getContextPath() %>/image/brief2_i.png" width="50" height="16" /></a></div>
    <div class="clear"></div>
    <div class="briefpages">
      <div class="briefpage">
        <div class="step"><img src="<%=request.getContextPath() %>/image/brief_1.png" width="625" height="60" /></div>
        <div class="content"><img src="<%=request.getContextPath() %>/image/brief_1_1.png" width="225" height="49" />
          <p>下载并运行“<a href="<%=request.getContextPath() %>/download/<%=AppProperties.getValue("client_version_name", "")%>">致云客户端</a>”，输入用户名和密码登录，选择一个云主机，点击<strong>“进入”</strong>即可</p>
          <img src="<%=request.getContextPath() %>/image/brief_1_2.png" width="289" height="47" />
          <p>打开Windows自带<strong>“远程桌面连接”</strong>，输入云主机公网IP连接，<br />
            <br />
            默认用户名：<strong>administrator</strong>，默认密码：<strong>zhicloud@123</strong><br/><br />成都地域的主机，用户连接时请用端口详情里的RDP端口连接</p>
          
          <img src="<%=request.getContextPath() %>/image/brief_1_3.png" width="235" height="43" />
          <p>打开SSH工具，如<strong>“SSH Secure Shell”</strong>，输入云主机公网IP、用户名（默认<strong>root</strong>）、<br />
            <br />
            密码（默认<strong>zhicloud@123</strong>）后进入云主机系统。<br/><br />成都地域的主机，用户连接时请用端口详情里的SSH端口连接</p>
        </div>
        <div class="pager"><a href="javascript:void(0);" onclick="nextbrief();"><img class="swapimage" id="brief4" src="<%=request.getContextPath() %>/image/brief4_i.png" width="101" height="18" /></a></div>
      </div>
      <div class="briefpage">
        <div class="step"><img src="<%=request.getContextPath() %>/image/brief_2.png" width="619" height="60" /></div>
        <div class="content"><img src="<%=request.getContextPath() %>/image/brief_2_1.png" width="144" height="49" />
          <p>以win2003系统参考，进入云主机后，右键<strong>“我的电脑”->“管理”—>“磁盘管理”</strong>，系统会<br />
            <br />
            提示需初始化磁盘，根据向导提示初始化及格式化数据盘。</p>
          <img src="<%=request.getContextPath() %>/image/brief_2_2.png" width="126" height="48" />
          <p>通过ssh登录后，执行命令<strong>“fdisk -l”</strong>查看数据盘信息<br />
            <br />
            格式化数据盘，如sdb，执行命令<strong>“mkfs.ext4 /dev/sdb”</strong><br />
            <br />
            新建一个目录用于挂载数据盘，如：<strong>”mkdir /data”</strong><br />
            <br />
            挂载数据盘，如：<strong>“mount /dev/sdb /data”</strong></p>
        </div>
        <div class="pager"><a href="javascript:void(0);" onclick="prevbrief();"><img class="swapimage" id="brief3" src="<%=request.getContextPath() %>/image/brief3_i.png" width="99" height="19" /></a>　　　　<a href="javascript:void(0);" onclick="nextbrief();"><img class="swapimage" id="brief4"  src="<%=request.getContextPath() %>/image/brief4_i.png" width="101" height="18" /></a></div>
      </div>
      <div class="briefpage">
        <div class="step"><img src="<%=request.getContextPath() %>/image/brief_3.png" width="629" height="60" /></div>
        <div class="content"><img src="<%=request.getContextPath() %>/image/brief_3_1.png" width="118" height="49" />
          <p>通过云主机默认开启的FTP服务上传/下载，默认用户名/密码：<strong>admin/zhicloud123</strong></p>
          <img src="<%=request.getContextPath() %>/image/brief_3_2.png" width="164" height="47" />
          <p>通过<strong>Windows RDP</strong>【直接映射本地磁盘/文件拖拽方式】或者<strong>Linux SSH</strong>【SSH文件传输工具】<br />
            连接主机后，上传文件。</p>
          <img src="<%=request.getContextPath() %>/image/brief_3_3.png" width="128" height="44" />
          <p>Linux默认已打开，通过访问<strong>http://IP:10000</strong>来访问，输入用户名/密码：<strong>root/zhicloud@123</strong></p>
          <img src="<%=request.getContextPath() %>/image/brief_3_4.png" width="193" height="44" />
          <p>在<strong>“我的云端/文件夹”</strong>中可上传300M资源，用户可通过<strong>“文件夹”</strong>功能下载文件到云主机。</p>
        </div>
        <div class="pager"><a href="javascript:void(0);" onclick="prevbrief();"><img class="swapimage" id="brief3" src="<%=request.getContextPath() %>/image/brief3_i.png" width="99" height="19" /></a>　　　　<a href="javascript:void(0);" onclick="hidebrief();"><img class="swapimage" id="brief5" src="<%=request.getContextPath() %>/image/brief5_i.png" width="113" height="19" /></a></div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = true;
var userName = "<%=loginInfo.getAccount()%>";
var current_input_id = "";
var old_host_name = "";
var current_host_id = "";
$(document).ready(function(){
	navHighlight("umc","umcs");
	
	initbrief(1);
	//----
	if('<%=tips%>'=='yes'){
		showbrief();
	}
	//===================全局操作：启动、关机、重启==========================
	$('#my_warehouse_datagrid').datagrid({
		onUnselect: function(rows){
			$('#sa').removeAttr("checked");
		},
		onSelect: function(rows){
			if($('#my_warehouse_datagrid').datagrid('getSelections').length>=$('#my_warehouse_datagrid').datagrid('getRows').length){
			$('#sa').prop("checked",true);
			}
		}
	});
	$('#sa').hover(function(event){
		$(this).next('label').addClass('active');
	},
	function(event){
		$(this).next('label').removeClass('active');
	});
	$('#sa').next('label').hover(function(event){
		$(this).addClass('active');
	},
	function(event){
		$(this).removeClass('active');
	});
	//全部启动
	$("#start-all").click(function(){

		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allStart = $('#start-all').attr("start-all");
		if(allStart=="2"){
			return;
		}
		var flagStatus = 0;
		
		top.$.messager.confirm("确认", "确定启动所有选中的云主机？", function (r) {
			if(r){
				loadingbegin(); 
				setTimeout("startAllHost()",1000);
				
			}
		}); 
		
			
	});
 
	//全部关机
	$("#shutdown-all").click(function(){
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allShutdown = $('#shutdown-all').attr("shutdown-all");
		if(allShutdown=="2"){
			return;
		} 
		
		top.$.messager.confirm("确认", "确定关机所有选中的云主机？", function (r) {
			if(r){ 
				loadingbegin(); 
				setTimeout("stopAllHost()",1000);
			} 
		});  
			
			
	});
	//全部重启
	$("#restart-all").click(function(){
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allRestart = $('#restart-all').attr("restart-all");
		if(allRestart=="2"){
			return;
		}
		top.$.messager.confirm("确认", "确定重启所有选中的云主机？", function (r) {
			if(r){
				loadingbegin(); 
				setTimeout("restartAllHost()",1000);
				
			}else{
				return;
			}
		});
	});
 	//==========================================================
	var self = {};
 	var ids = new Array();
 	var ids_starting = new Array();
 	var ids_restarting = new Array();
 	var id_and_realid = new Array();
 	var ids_shutting_down = new Array();
 	<% for(int i=0;i<myCloudHostList.size();i++){%>
 		<%if("创建中".equals(myCloudHostList.get(i).getSummarizedStatusText())){%>
		ids.push("<%=myCloudHostList.get(i).getId()%>"); 		<%}%>
 		<%if(myCloudHostList.get(i).getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING){%>
 		ids_starting.push("<%=myCloudHostList.get(i).getId()%>"); 		<%}%>
 		<%if(myCloudHostList.get(i).getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN){%>
 		ids_shutting_down.push("<%=myCloudHostList.get(i).getId()%>"); 		<%}%>
 		<%if(myCloudHostList.get(i).getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING){%>
 		ids_restarting.push("<%=myCloudHostList.get(i).getId()%>"); 		<%}%>
 	<%}%>
 	
 	<% for(int i=0;i<realHostIdList.size();i++){%>
 		var id_realid = {};
 		id_realid.type = "<%=realHostIdList.get(i).get("type")%>";
 		id_realid.id = "<%=realHostIdList.get(i).get("id")%>";
 		id_realid.real_id = "<%=realHostIdList.get(i).get("realId")%>";
 		id_and_realid.push(id_realid);
 	<%}%>
 	
	var new_region = "<%=newRegion%>";
	if(new_region == "1"){
		$("#all_cloud_host").removeClass("active");
		$("#guangzhou_cloud_host").addClass("active");
	}else if(new_region == "4"){
		$("#all_cloud_host").removeClass("active");
		$("#hongkang_cloud_host").addClass("active");
	}else if(new_region == "2"){
		$("#all_cloud_host").removeClass("active");
		$("#chengdu_cloud_host").addClass("active");
	}
	
	self.cloudHostId = "";
	self.ip = "";
	self.password="";
	self.cloudHostStartId="";
	self.cloudHostShutdownId = "";
	self.cloudHostRestartId = "";
	self.cloudHostInactivateId = "";
	self.cloudHostHaltId = "";
	self.cloudHostDeleteId = "";
	// 查询
	$("a[name='cloud_host']").click(function(){
		var region = $(this).attr("region");
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=myCloudHostPage&region="+encodeURIComponent(region);
		return;
	});	
	$("#one_cloud_host .box").hover(function(evt){
		self.cloudHostId = $(this).attr("cloudHostId");
		self.ip = $(this).attr("ip");
		self.password = $(this).attr("password");
	});
	// 查看详情
	$("#one_cloud_host .my_cloud_host_detail").click(function(evt){
		self.cloudHostViewDetail(self.cloudHostId);
	});
	// 进入云主机
	$("#one_cloud_host .enter_my_cloud_host").click(function(evt){
		var ip = self.ip;
		var password = self.password;
		window.open("<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostPage&ip="+encodeURIComponent(ip)+"&password="+encodeURIComponent(password));
	});
	// 启动
	$("#one_cloud_host .launch_host").click(function(evt){
		
		self.cloudHostStartId = self.cloudHostId;
		var startHost = $(this).attr("startHost");
		$.cloudHostStart = $(this);
		if(startHost == "1"){
			top.$.messager.confirm("确认", "确定启动云主机？", function (r) {
				if(r){
					$('#cloud-status-id'+self.cloudHostStartId).html("正在启动...");
					$.cloudHostStart.removeClass("launch_host");
					$.cloudHostStart.addClass("alpha");
					$.cloudHostStart.attr("startHost","2");
					$('#allocation'+self.cloudHostStartId).addClass("alpha");
					$('#allocation'+self.cloudHostStartId).attr("modifyAllocation","2");
					self.startCloudHost();
				}else{
					return;
				}
			});  
		}
		sp();
	});
	// 关机
	$("#one_cloud_host .shutdown_host").click(function(evt){
		
		self.cloudHostShutdownId = self.cloudHostId;
		var stopHost = $(this).attr("stopHost");
		$.cloudHostStop = $(this);
		if(stopHost == "1"){
			top.$.messager.confirm("确认", "确定要关闭正在运行的云主机吗?", function (r) {
				if(r){
					$('#status'+self.cloudHostShutdownId).html("关机中...");
					$.cloudHostStop.addClass("alpha");
					$.cloudHostStop.attr("stopHost","2");
					$('#restart'+self.cloudHostShutdownId).addClass("alpha");
					$('#restart'+self.cloudHostShutdownId).attr("restartHost","2");
					$('#halt'+self.cloudHostShutdownId).addClass("alpha");
					$('#halt'+self.cloudHostShutdownId).attr("haltHost","2");
					$('#outOfService'+self.cloudHostShutdownId).addClass("alpha");
					$('#outOfService'+self.cloudHostShutdownId).attr("outofservice","2");
					self.stopCloudHost();
				}else{
					return;
				}
			});
		}else{
			return;
		}
		sp();
	});
	// 强制关机
	$("#one_cloud_host .halt_host").click(function(evt){
		
		self.cloudHostHaltId = self.cloudHostId;
		var haltHost = $(this).attr("haltHost");
		if(haltHost=="1"){
			self.haltCloudHost();
		}else{
			return;
		}
		sp();
	});
	// 重启
	$("#one_cloud_host .restart_host").click(function(evt){
		
		self.cloudHostRestartId = self.cloudHostId;
		var restartHost = $(this).attr("restartHost");
		if(restartHost=="1"){
			top.$.messager.confirm("确认", "确定要重新启动吗?", function (r) {
				if(r){
					$('#status'+self.cloudHostRestartId).html("正在重启...");
					$('#shutdown'+self.cloudHostRestartId).addClass("alpha");
					$('#shutdown'+self.cloudHostRestartId).attr("stopHost","2");
					$('#restart'+self.cloudHostRestartId).addClass("alpha");
					$('#restart'+self.cloudHostRestartId).attr("restartHost","2");
					$('#forcerestart'+self.cloudHostRestartId).addClass("alpha");
					$('#forcerestart'+self.cloudHostRestartId).attr("restartHost","2");
					$('#halt'+self.cloudHostRestartId).addClass("alpha");
					$('#halt'+self.cloudHostRestartId).attr("haltHost","2");
					self.restartCloudHost();
				}else{
					return;
				}
			});
		}
		sp();
	});
	
	// 重启
	$("#one_cloud_host .force_restart_host").click(function(evt){
		
		self.cloudHostRestartId = self.cloudHostId;
		var restartHost = $(this).attr("restartHost");
		if(restartHost=="1"){
			top.$.messager.confirm("确认", "确定要强制重新启动吗?", function (r) {
				if(r){
					$('#status'+self.cloudHostRestartId).html("正在重启...");
					$('#shutdown'+self.cloudHostRestartId).addClass("alpha");
					$('#shutdown'+self.cloudHostRestartId).attr("stopHost","2");
					$('#restart'+self.cloudHostRestartId).addClass("alpha");
					$('#restart'+self.cloudHostRestartId).attr("restartHost","2");
					$('#forcerestart'+self.cloudHostRestartId).addClass("alpha");
					$('#forcerestart'+self.cloudHostRestartId).attr("restartHost","2");
					$('#halt'+self.cloudHostRestartId).addClass("alpha");
					$('#halt'+self.cloudHostRestartId).attr("haltHost","2");
					self.forceRestartCloudHost();
				}else{
					return;
				}
			});
		}
		sp();
	});
	
	// 弹出光盘
	$("#cloud_host_item_other_oper_menu .popup-iso").click(function(){
		self.popupIsoImage();
	});
	// 插入光盘
	$("#cloud_host_item_other_oper_menu .insert-iso").click(function(){
		self.insertIsoImage();
	});
	// 开放端口
	$("#cloud_host_item_other_oper_menu .open-port").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=addPortPage&hostId="+encodeURIComponent(self.cloudHostId)
		});
	});
	// 修改配置
	$("#one_cloud_host .modify-allocation").click(function(){
		
		var modifyAllocation = $(this).attr("modifyAllocation");
		if(modifyAllocation == "1"){
			window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyAllocationPage&hostId="+encodeURIComponent(self.cloudHostId);
		}
		return;
		ajax.remoteCall("bean://cloudHostService:modifyAllocationPage",
				[ self.cloudHostId ], 
				function(reply) {
					if (reply.status=="exception")
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else
					{
						top.$.messager.alert("信息", reply.result.message, "info");
					}
				}
			 );
	});
	//修改端口 
	$("#one_cloud_host .modify-port").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyPortPage&hostId="+encodeURIComponent(self.cloudHostId);
	});
	//备份&恢复 
	$("#one_cloud_host .backup_host").click(function(){

		var backup = $(this).attr("backup");
		if(backup=="1"){
			var uuid=$(this).attr("uuid");
			var region=$(this).attr("region");
			var hostName=$(this).attr("hostName");
			window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=backupHostPage&uuid="+encodeURIComponent(uuid)+"&region="+encodeURIComponent(region);
		}else{
			return;
		}
	});
	
	//重装系统 
	$("#one_cloud_host .reset_host").click(function(){
		var reset = $(this).attr("reset");
		if(reset=="1") {
			var uuid = $(this).attr("uuid");
			var region = $(this).attr("region");
			window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=resetHostPage&uuid=" + encodeURIComponent(uuid) + "&region=" + encodeURIComponent(region);
		} else {
			return;
		}
	});
	
	// 绑定云终端
	$("#cloud_host_item_other_oper_menu .binding-terminal").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudTerminalBindingPage&hostId="+encodeURIComponent(self.cloudHostId)+"&userId="+encodeURIComponent(userId)
		});
		 
	});
	// 修改监控密码
	$("#cloud_host_item_other_oper_menu .modify_password").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=updatePasswordPage&hostId="+encodeURIComponent(self.cloudHostId)+"&userId="+encodeURIComponent(userId)
		});
	});
	// 申请停机
	$("#one_cloud_host .out-of-service").click(function(){
		
		var outOfService = $(this).attr("outofservice");
		if(outOfService=="2"){
			return;
		}
		self.cloudHostInactivateId = self.cloudHostId;
		top.$.messager.confirm("确认", "确定要停用该云主机吗？", function (r) { 
			if(r){
				self.inactivateCloudHost();
			}
		});
		sp();
	});
	// 申请停机恢复
	$("#one_cloud_host .reactivate").click(function(){
		
		self.cloudHostInactivateId = self.cloudHostId;
		top.$.messager.confirm("确认", "确定要恢复该云主机吗？", function (r) { 
			if(r){
				self.reactivateCloudHost();
			}
		});
		sp();
	});
	$("#one_cloud_host .delete-cloud-host").click(function(){
		
		self.cloudHostHaltId = self.cloudHostId;
		if( self.cloudHostHaltId.trim()=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {  
				ajax.remoteCall("bean://cloudHostService:deleteCloudHostByIds",
					[ [self.cloudHostHaltId] ], 
					function(reply) {
						if (reply.status=="exception")
						{
							if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
    						{
    							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
    								window.location.reload();
    							});
    						}
    						else 
    						{
    							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
    						}
						}
						else
						{
							window.location.reload();
						}
					}
				);
	        }  
	    });
		sp();
	});
	// 启动云主机
	self.startCloudHost = function()
	{
		if( self.cloudHostStartId=="" )
		{
			return;
		}
		 ajax.remoteCall("bean://cloudHostService:startCloudHost", 
				[ self.cloudHostStartId ],
				function(reply) {
					if( reply.status == "exception" )
					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else if (reply.result.status == "success")
					{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning',function(){
							window.location.reload();
						});
					}
				}
			) ;
	};
	
	// 强制关机
	self.haltCloudHost = function()
	{
		if( self.cloudHostHaltId=="" )
		{
			return;
		}

		top.$.messager.confirm("确认", "确定要强制关闭正在运行的云主机吗?", function (r) {  
	        if (r) { 
	        	ajax.remoteCall("bean://cloudHostService:haltCloudHost",
    				[ self.cloudHostHaltId ], 
    				function(reply) {
    					if (reply.status=="exception")
    					{
    						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
    						{
    							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
    								window.location.reload();
    							});
    						}
    						else 
    						{
    							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
    						}
    					}
    					else
    					{
    						top.$.messager.alert("信息", reply.result.message, "info",function(){
    							window.location.reload();
    						});
    					}
    				}
    			);
	        }  
	    }); 
	};
	// 关机
	self.stopCloudHost = function()
	{
		if( self.cloudHostShutdownId=="" )
		{
			return;
		}

      	ajax.remoteCall("bean://cloudHostService:stopCloudHost",
 				[ self.cloudHostShutdownId ], 
 				function(reply) {
 					if (reply.status=="exception")
 					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
 						{
 							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
 								window.location.reload();
 							});
 						}
 						else 
 						{
 							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
 								window.location.reload();
 							});
 						}
 					}
 					else if(reply.result.status=="success")
 					{
 						
 						top.$.messager.alert("信息", reply.result.message, "info",function(){
 							refreshStatus(self.cloudHostShutdownId);
					});
 						
 					}else{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
 						});
 					}
	    }); 
	};
	
	// 重启(先关机再开机)
	self.restartCloudHost = function()
	{
		if( self.cloudHostRestartId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:stopCloudHost",
 				[ self.cloudHostRestartId ], 
 				function(reply) {
 					if (reply.status=="exception")
 					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
 						{
 							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
 								window.location.reload();
 							});
 						}
 						else 
 						{
 							top.$.messager.alert("警告", "重启失败", "warning",function(){
 								window.location.reload();
 							});
 						}
 					}
 					else if(reply.result.status=="success")
 					{
 						//关机命令发送成功,则循环查询关机结果
 						refreshStatusForRestart(self.cloudHostRestartId,0);
 						
 					}else{
						top.$.messager.alert("信息", "重启失败", "info",function(){
							window.location.reload();
 						});
 					}
	    });
// 		ajax.remoteCall("bean://cloudHostService:restartCloudHost", 
// 				[ self.cloudHostRestartId ],
// 				function(reply) {
// 					if( reply.status == "exception" )
// 					{
<%--  						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" ) --%>
// 						{
// 							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
// 								window.location.reload();
// 							});
// 						}
// 						else 
// 						{
// 							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
// 								window.location.reload();
// 							});
// 						}
// 					}
// 					else if (reply.result.status == "success")
// 					{
// 						top.$.messager.alert("信息", reply.result.message, "info",function(){
// 							window.location.reload();
// 						});
// 					}
// 					else
// 					{
// 						top.$.messager.alert('警告', reply.result.message, 'warning',function(){
// 							window.location.reload();
// 						});
// 					}
// 				}
// 			) ;
	};
	
	// 强制重启
	self.forceRestartCloudHost = function()
	{
		if( self.cloudHostRestartId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:forceRestartCloudHost", 
				[ self.cloudHostRestartId ],
				function(reply) {
					if( reply.status == "exception" )
					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
								window.location.reload();
							});
						}
					}
					else if (reply.result.status == "success")
					{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning',function(){
							window.location.reload();
						});
					}
				}
			) ;
	};
	
	// 加载光盘镜像
	self.insertIsoImage = function()
	{
		if( self.cloudHostId=="" )
		{
			return;
		}
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=insertIsoImagePage",
			params: {
				"cloudHostId" : self.cloudHostId
			},
			onClose : function(data) {
				window.location.reload();
			}
		});
	};
	
	// 弹出光盘镜像
	self.popupIsoImage = function()
	{
		if( self.cloudHostId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:popupIsoImage",
				[ self.cloudHostId ], 
				function(reply) {
					if (reply.status=="exception")
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else
					{
						top.$.messager.alert("信息", reply.result.message, "info");
					}
				}
			);
	};
	
	// 申请停机
	self.inactivateCloudHost = function()
	{
		if( self.cloudHostInactivateId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:inactivateCloudHost",
				[ self.cloudHostInactivateId ], 
				function(reply) {
					if (reply.status=="exception")
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else
					{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
				}
			);
	};

	// 申请停机恢复
	self.reactivateCloudHost = function()
	{
		if( self.cloudHostInactivateId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:reactivateCloudHost",
				[ self.cloudHostInactivateId ], 
				function(reply) {
					if (reply.status=="exception")
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else
					{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
				}
			);
	};
	
	// 查看云主机详情
	self.cloudHostViewDetail = function(cloudHostId){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage&cloudHostId="+encodeURIComponent(cloudHostId);
		return;
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage",
			params: {
				"cloudHostId" : cloudHostId
			},
			onClose : function(data) {
// 				self.query();
			}
		});
	};
	//获取当前主机ID
	$(".box .host_name").focus(function(){
		current_input_id = $(this).attr("id");
		old_host_name = $(this).val();
		current_host_id = current_input_id.substring(2,current_input_id.length);
	});
	// 查询
	$("#query_agent_btn").click(function(){
// 		self.query();
	});
	
	// 创建云主机
	$("#create_cloud_host_btn").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=createCloudHostPage",
			onClose : function(data) {
				self.query();
			}
		});
	});
	for(var i=0;i<ids.length;i++){
		$('#'+ids[i]).progressbar({value:0});
	}
	for(var i=0;i<ids.length;i++){
		refreshProgress(ids[i]);
	}
	for(var i=0;i<ids_shutting_down.length;i++){
		refreshStatus(ids_shutting_down[i]);
	}
	for(var i=0;i<ids_starting.length;i++){
		refreshStartStatus(ids_starting[i]);
	}
	for(var i=0;i<ids_restarting.length;i++){
		refreshRestartStatus(ids_restarting[i]);
	}
	for(var i=0;i<id_and_realid.length;i++){
		if(id_and_realid[i].type=='backup'){
			var p_div = "<div class='listinfo'>您的主机正在备份中，预计1小时后完成备份。</div>"+
             "<div id="+id_and_realid[i].real_id+" style='width:240px;margin-left:20px;margin-top:0px;' text='云主机备份中({value}%)'></div>";
            $(".box div[name=a"+id_and_realid[i].id+"]").empty(); 
            $(".box div[name=a"+id_and_realid[i].id+"]").append(p_div); 
            $('#'+id_and_realid[i].real_id).progressbar({value:0});
            refreshBackupProgress(id_and_realid[i].real_id);
		}else if(id_and_realid[i].type=='resume'){
			var p_div = "<div class='listinfo'>您的主机正在恢复系统，预计1小时后完成恢复。</div>"+
            "<div id="+id_and_realid[i].real_id+" style='width:240px;margin-left:20px;margin-top:0px;' text='云主机恢复中({value}%)'></div>";
            $(".box div[name=a"+id_and_realid[i].id+"]").empty(); 
           $(".box div[name=a"+id_and_realid[i].id+"]").append(p_div);
           $('#'+id_and_realid[i].real_id).progressbar({value:0});
           refreshResumeProgress(id_and_realid[i].real_id);
		}else if(id_and_realid[i].type=='reset'){
			var p_div = "<div class='listinfo'>您的主机正在重装系统，预计1小时后完成重装。</div>"+
            "<div id="+id_and_realid[i].real_id+" style='width:240px;margin-left:20px;margin-top:0px;' text='云主机重装中({value}%)'></div>";
            $(".box div[name=a"+id_and_realid[i].id+"]").empty(); 
           $(".box div[name=a"+id_and_realid[i].id+"]").append(p_div);
           $('#'+id_and_realid[i].real_id).progressbar({value:0});
           refreshResetProgress(id_and_realid[i].real_id);
		}
	}
	
	
});
//--------------------------
function selectall(){
	if($("#sa").prop("checked")==true){
		$('#my_warehouse_datagrid').datagrid('selectAll');
	}else{
		$('#my_warehouse_datagrid').datagrid('unselectAll');
	}
}
function sp(){
	event.stopPropagation();
}

var my_flag = 0;
function refreshStatus(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					$('#status'+id).html("<strong class='gray'>已关机</strong>");
					$('#start'+id).removeClass("alpha");
					$('#start'+id).addClass("launch_host");
					$('#start'+id).attr("startHost","1");
					$('#shutdown'+id).addClass("alpha");
					$('#shutdown'+id).removeClass("shutdown_host");
					$('#shutdown'+id).attr("stopHost","2");
					$('#restart'+id).addClass("alpha");
					$('#restart'+id).removeClass("restart_host");
					$('#restart'+id).attr("restartHost","2");
					$('#forcerestart'+id).addClass("alpha");
					$('#forcerestart'+id).removeClass("force_restart_host");
					$('#forcerestart'+id).attr("forcerestart","2");
					$('#halt'+id).addClass("alpha");
					$('#halt'+id).removeClass("halt_host");
					$('#halt'+id).attr("haltHost","2");
					$('#modify'+id).removeClass("alpha");
					$('#modify'+id).attr("modifyAllocation","1");
					$('#outOfService'+id).removeClass("alpha");
					$('#outOfService'+id).attr("outofservice","1");

					$('#backup'+id).removeClass("alpha");
					$('#backup'+id).attr("backup", "1");
					$('#reset'+id).removeClass("alpha");
					$('#reset'+id).attr("reset", "1");

					return;
				} 
				else 
				{
// 					if(my_flag++ < 30){
						window.setTimeout(function(){
							refreshStatus(id);
						},4000);
// 					}else{
// 						return;
// 					}
				}
			}
		);
		
	}else{
		 return;
	}
}

function refreshStatusForRestart(id,time){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success")//关机成功，启动云主机 
				{
					ajax.remoteCall("bean://cloudHostService:startCloudHost", 
							[ id ],
							function(reply) {
								if( reply.status == "exception" )
								{
			 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
									{
										top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
											window.location.reload();
										});
									}
									else 
									{
										top.$.messager.alert("警告", "重启失败", "warning");
									}
								}
								else if (reply.result.status == "success")
								{
// 									top.$.messager.alert("信息", reply.result.message, "info",function(){
// 										window.location.reload();
// 									});
									refreshStartStatusForRestart(id);
								}
								else
								{
									top.$.messager.alert('警告', '重启失败', 'warning',function(){
										window.location.reload();
									});
								}
							}
						) ;
				} 
				else //关机未成功则一直查询关机状态，超过2min则直接强制关机
				{
					if(time < 120){
						window.setTimeout(function(){
							refreshStatusForRestart(id,time+4);
						},4000);
					}else{//此处强制关机
						ajax.remoteCall("bean://cloudHostService:haltCloudHost",
			    				[ id ], 
			    				function(reply) {
			    					if (reply.status=="exception")
			    					{
			    						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
			    						{
			    							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
			    								window.location.reload();
			    							});
			    						}
			    						else 
			    						{
			    							top.$.messager.alert("警告", "重启失败", "warning");
			    						}
			    					}
			    					else//关机成功，启动云主机
			    					{
			    						ajax.remoteCall("bean://cloudHostService:startCloudHost", 
			    								[ id ],
			    								function(reply) {
			    									if( reply.status == "exception" )
			    									{
			    				 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
			    										{
			    											top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
			    												window.location.reload();
			    											});
			    										}
			    										else 
			    										{
			    											top.$.messager.alert("警告", "重启失败", "warning");
			    										}
			    									}
			    									else if (reply.result.status == "success")
			    									{
			    										top.$.messager.alert("信息", reply.result.message, "info",function(){
			    											window.location.reload();
			    										});
			    									}
			    									else
			    									{
			    										top.$.messager.alert('警告', '重启失败', 'warning',function(){
			    											window.location.reload();
			    										});
			    									}
			    								}
			    							) ;
			    					}
			    				}
			    			);
					}
				}
			}
		);
		
	}else{
		 return;
	}
}
function refreshStartStatus(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostStartStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					$('#status'+id).html("<strong class='gray'>运行中</strong>");
					$('#start'+id).addClass("alpha");
					$('#start'+id).removeClass("launch_host");
					$('#start'+id).attr("startHost","2");
					$('#shutdown'+id).removeClass("alpha");
					$('#shutdown'+id).addClass("shutdown_host");
					$('#shutdown'+id).attr("stopHost","1");
					$('#restart'+id).removeClass("alpha");
					$('#restart'+id).addClass("restart_host");
					$('#restart'+id).attr("restartHost","1");
					$('#forcerestart'+id).addClass("alpha");
					$('#forcerestart'+id).removeClass("force_restart_host");
					$('#forcerestart'+id).attr("forcerestart","1");
					$('#halt'+id).removeClass("alpha");
					$('#halt'+id).addClass("halt_host");
					$('#halt'+id).attr("haltHost","1");
 					$('#modify'+id).attr("modifyAllocation","2");
					$('#outOfService'+id).removeClass("alpha");
					$('#outOfService'+id).attr("outofservice","1");

					$('#backup'+id).addClass("alpha");
					$('#backup'+id).attr("backup", "2");
					$('#reset'+id).addClass("alpha");
					$('#reset'+id).attr("reset", "2");
					return;
				} 
				else 
				{
// 					if(my_flag++ < 30){
						window.setTimeout(function(){
							refreshStartStatus(id);
						},4000);
// 					}else{
// 						return;
// 					}
				}
			}
		);
		
	}else{
		 return;
	}
}
function refreshStartStatusForRestart(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostStartStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					top.$.messager.alert('提示', '重启成功', 'info',function(){
						window.location.reload();
					});
				} 
				else 
				{
					window.setTimeout(function(){
						refreshStartStatusForRestart(id);
					},4000);
				}
			}
		);
		
	}else{
		 return;
	}
}

function refreshRestartStatus(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostRestartStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					if(reply.result.properties.runningstatus == 2){
						$('#status'+id).html("<strong class='gray'>运行中</strong>");
						$('#start'+id).addClass("alpha");
						$('#start'+id).removeClass("launch_host");
						$('#start'+id).attr("startHost","2");
						$('#shutdown'+id).removeClass("alpha");
						$('#shutdown'+id).addClass("shutdown_host");
						$('#shutdown'+id).attr("stopHost","1");
						$('#restart'+id).removeClass("alpha");
						$('#restart'+id).addClass("restart_host");
						$('#restart'+id).attr("restartHost","1");
						$('#forcerestart'+id).addClass("alpha");
						$('#forcerestart'+id).removeClass("force_restart_host");
						$('#forcerestart'+id).attr("forcerestart","1");
						$('#halt'+id).removeClass("alpha");
						$('#halt'+id).addClass("halt_host");
						$('#halt'+id).attr("haltHost","1");
	 					$('#modify'+id).attr("modifyAllocation","2");
						$('#outOfService'+id).removeClass("alpha");
						$('#outOfService'+id).attr("outofservice","1");

						$('#backup'+id).addClass("alpha");
						$('#backup'+id).attr("backup", "2");
						$('#reset'+id).addClass("alpha");
						$('#reset'+id).attr("reset", "2");
					}else if(reply.result.properties.runningstatus == 1){
						$('#status'+id).html("<strong class='gray'>已关机</strong>");
						$('#start'+id).removeClass("alpha");
						$('#start'+id).addClass("launch_host");
						$('#start'+id).attr("startHost","1");
						$('#shutdown'+id).addClass("alpha");
						$('#shutdown'+id).removeClass("shutdown_host");
						$('#shutdown'+id).attr("stopHost","2");
						$('#restart'+id).addClass("alpha");
						$('#restart'+id).removeClass("restart_host");
						$('#restart'+id).attr("restartHost","2");
						$('#forcerestart'+id).addClass("alpha");
						$('#forcerestart'+id).removeClass("force_restart_host");
						$('#forcerestart'+id).attr("forcerestart","2");
						$('#halt'+id).addClass("alpha");
						$('#halt'+id).removeClass("halt_host");
						$('#halt'+id).attr("haltHost","2");
						$('#modify'+id).removeClass("alpha");
						$('#modify'+id).attr("modifyAllocation","1");
						$('#outOfService'+id).removeClass("alpha");
						$('#outOfService'+id).attr("outofservice","1");

						$('#backup'+id).removeClass("alpha");
						$('#backup'+id).attr("backup", "1");
						$('#reset'+id).removeClass("alpha");
						$('#reset'+id).attr("reset", "1");
					}else{
						window.setTimeout(function(){
							refreshRestartStatus(id);
						},4000);
						return 
					} 
				} 
				else 
				{
// 					if(my_flag++ < 30){
						window.setTimeout(function(){
							refreshRestartStatus(id);
						},4000);
// 					}else{
// 						return;
// 					}
				}
			}
		);
		
	}else{
		 return;
	}
}
var fail_flag = "0";
//--------------------------
function refreshProgress(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostCreationResult", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
//					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					$('#'+id).progressbar('setValue', parseInt(reply.result.properties.progress));
					if( self.refreshProgress==null )
					{
						return ;
					}
					if( reply.result.properties.creation_status==null )
					{
						window.setTimeout(function(){
							refreshProgress(id);
						}, 3000);
					}
					else if( reply.result.properties.creation_status==false )
					{
						if(fail_flag == "0"){
							fail_flag = "1";
							top.$.messager.alert("提示","云主机创建失败","warning",function(){
							});
						}
						
// 						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建失败</div>");
					}
					else if( reply.result.properties.creation_status==true )
					{
						window.setTimeout(function(){
							top.$.messager.alert("提示","云主机创建成功","info",function(){
								window.location.reload();
							});
						}, 1000);
						
// 						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建成功</div>");
					}
				} 
				else 
				{
// 					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
		
	}else{
		 window.setTimeout(refreshProgress(id), 5000);
	}
}
//备份进度
function refreshBackupProgress(uuid){ 
	ajax.remoteCall("bean://cloudHostService:getBackupHostResult", 
			[ uuid ],
			function(reply) {
				console.info(reply);
				if( reply.status == "exception" ) 
				{ 
					
				} 
				else if(reply.result.status == "success") 
				{  
					if( self.refreshBackupProgress==null )
					{
						return ;
					}
					if(reply.result.properties.backup_status!="false"){
						$('#'+uuid).progressbar('setValue', parseInt(reply.result.properties.progress));
					}
					if( reply.result.properties.backup_status=="pending" )
					{
						window.setTimeout(function(){
							refreshBackupProgress(uuid);
						}, 3000);
					}
					else if( reply.result.properties.backup_status=="false" )
					{ 								
						top.$.messager.alert("提示","云主机备份失败","warning",function(){
							window.location.reload();
						});
					}
					else if( reply.result.properties.backup_status=="true" )
					{
						top.$.messager.alert("提示","云主机备份失败","warning",function(){
							window.location.reload();
						});
					}
				} 
				else 
				{ 
					// 云主机尚未开始备份，继续获取信息
					window.setTimeout(refreshBackupProgress(uuid), 3000);
				}
			}
		); 
	
}
//重装进度
function refreshResumeProgress(uuid){ 
	ajax.remoteCall("bean://cloudHostService:getResumeHostResult", 
			[ uuid ],
			function(reply) {
				if( reply.status == "exception" ) 
				{ 
					
				} 
				else if(reply.result.status == "success") 
				{  
					if( self.refreshResumeProgress==null )
					{
						return ;
					}
					if(reply.result.properties.resume_status!=false){
						$('#'+uuid).progressbar('setValue', parseInt(reply.result.properties.progress));
					}
					if( reply.result.properties.resume_status==null )
					{
						window.setTimeout(function(){
							refreshResumeProgress(uuid);
						}, 3000);
					}
					else if( reply.result.properties.resume_status==false )
					{ 								
						top.$.messager.alert("提示","云主机恢复失败","warning",function(){
							window.location.reload();
						});

					}
					else if( reply.result.properties.resume_status==true )
					{
						top.$.messager.alert("提示","云主机恢复成功","warning",function(){
							window.location.reload();
						});

					}
				} 
				else 
				{ 
					// 云主机尚未开始恢复，继续获取信息
					window.setTimeout(self.refreshResumeProgress(uuid), 1000);
				}
			}
		); 
	
}

function refreshResetProgress(uuid){
	ajax.remoteCall("bean://cloudHostService:getResetHostResult", 
			[ uuid ],
			function(reply) {
				if( reply.status == "exception" ) 
				{ 
					
				} 
				else if(reply.result.status == "success") 
				{  
					if( self.refreshResetProgress==null )
					{
						return ;
					}
					if(reply.result.properties.resume_status!=false){
						$('#'+uuid).progressbar('setValue', parseInt(reply.result.properties.progress));
					}
					if( reply.result.properties.resume_status==null )
					{
						window.setTimeout(function(){
							refreshResetProgress(uuid);
						}, 3000);
					}
					else if( reply.result.properties.resume_status==false )
					{ 								
						top.$.messager.alert("提示","云主机恢复失败","warning",function(){
							window.location.reload();
						});
		
					}
					else if( reply.result.properties.resume_status==true )
					{
						top.$.messager.alert("提示","云主机恢复成功","warning",function(){
							window.location.reload();
						});
		
					}
				} 
				else 
				{ 
					// 云主机尚未开始恢复，继续获取信息
					window.setTimeout(self.refreshResetProgress(uuid), 1000);
				}
			}
	);
	
}


//--------
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}
function showSingleDialog(param)
{
	var container = $("#single_dialog_dynamic_container");
	if( container.length==0 )
	{
		container = $("<div id='single_dialog_dynamic_container' style='display:none;'></div>").appendTo(document.body);
	}
	if( param.params==null )
	{
		param.params = {};
	}
	container.load(
		param.url, 
		param.params,
		function(){
			$.parser.parse(container.get(0));
			container.prop("_data_", param);
		}
	);
}
function changeHostName(){
	var new_host_name = $("#"+current_input_id).val();
	if(old_host_name == new_host_name){
		return;
	}
	if(new_host_name == ''||new_host_name.length>30){ 
		top.$.messager.alert("提示","主机名必须为1-30个字符","info",function(){
			window.location.reload();
		});
		return ;
	}
	if(current_host_id!='' && new_host_name!=''){
		ajax.remoteCall("bean://cloudHostService:updateHostNameById", 
			[ current_host_id,new_host_name],
			function(reply) {
				if (reply.status == "exception") 
				{
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							window.location.reload();
						});
					}
					else 
					{
						top.$.messager.alert("警告", "修改失败", "warning",function(){
							window.location.reload();
						});
					}
				} 
				else if (reply.result.status == "success") 
				{
					top.$.messager.alert('提示',reply.result.message,'info',function(){}); 
				} 
				else 
				{
					top.$.messager.alert('警告',reply.result.message,'warning',function(){
						
					});
				}
			}
		);
		
	}else{
		 window.location.reload();
	}
}
function startAllHost(){  
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		$('#start-all').addClass("disable");
		$('#start-all').attr("start-all","2");
		$('#shutdown-all').addClass("disable");
		$('#shutdown-all').attr("shutdown-all","2");
		$('#restart-all').addClass("disable");
		$('#restart-all').attr("restart-all","2");
		var flag = 0;
		var hostIds="";
		for(var i=0;i<allHost.length;i++){ 
			var str = new String(allHost[i].data);
			var hostId = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
			var startHost = $('#allStart'+hostId).attr("startHost");
			if(str.indexOf("已关机",0)<0 || startHost=="2"){
				flag++;
				if(flag>=allHost.length){
					top.$.messager.alert("提示","未找到需要启动的云主机","info");
					$('#start-all').removeClass("disable");
					$('#start-all').attr("start-all","1");
					$('#shutdown-all').removeClass("disable");
					$('#shutdown-all').attr("shutdown-all","1");
					$('#restart-all').removeClass("disable");
					$('#restart-all').attr("restart-all","1");
					loadingend();
					return;
				}
				continue;
			}else{
				hostIds  = hostIds+hostId+"/";
				$('#cloud-status-id'+hostId).html("正在启动...");
				$('#allStart'+hostId).removeClass("launch_host");
				$('#allStart'+hostId).addClass("alpha");
				$('#allStart'+hostId).attr("startHost","2");
				$('#allocation'+hostId).addClass("alpha");
				$('#allocation'+hostId).attr("modifyAllocation","2"); 
				ajax.remoteCall("bean://cloudHostService:startCloudHost", 
					[ hostId ],
					function(reply) {
					        hostIds = hostIds.replace(reply.result.properties.hostId+"/","");
							if(hostIds.length<=1){
								loadingend();
								top.$.messager.alert("信息", "启动完成", "info",function(){
									window.location.reload();
								});
							}
						if( reply.status == "exception" )
						{
							
	 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
								}
								}
								else if (reply.result.status == "success")
								{
									
// 									top.$.messager.alert("信息", reply.result.message, "info",function(){
// 									});
								}
								else
								{
// 									top.$.messager.alert('警告', reply.result.message, 'warning',function(){
									
// 									});
								}
							}
						) ;
					} 
				}
		        
}
function restartAllHost(){  
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		$('#start-all').addClass("disable");
		$('#start-all').attr("start-all","2");
		$('#shutdown-all').addClass("disable");
		$('#shutdown-all').attr("shutdown-all","2");
		$('#restart-all').addClass("disable");
		$('#restart-all').attr("restart-all","2");
		var flag = 0;
		var hostIds="";
		for(var i=0;i<allHost.length;i++){
			var str = new String(allHost[i].data);
			var hostId = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
			var restartHost = $('#restart'+hostId).attr("restartHost");
			if(str.indexOf("运行中",0)<0 || restartHost=="2"){
				flag++;
				if(flag>=allHost.length){
					top.$.messager.alert("提示","未找到需要重启的云主机","info");
					$('#start-all').removeClass("disable");
					$('#start-all').attr("start-all","1");
					$('#shutdown-all').removeClass("disable");
					$('#shutdown-all').attr("shutdown-all","1");
					$('#restart-all').removeClass("disable");
					$('#restart-all').attr("restart-all","1");
					loadingend();
					return;
				}
				continue;
			}else{
				hostIds  = hostIds+hostId+"/";
				$('#status'+hostId).html("正在重启...");
				$('#shutdown'+hostId).addClass("alpha");
				$('#shutdown'+hostId).attr("stopHost","2");
				$('#restart'+hostId).addClass("alpha");
				$('#restart'+hostId).attr("restartHost","2");
				$('#halt'+hostId).addClass("alpha");
				$('#halt'+hostId).attr("haltHost","2");
				ajax.remoteCall("bean://cloudHostService:restartCloudHost", 
					[ hostId ],
					function(reply) {
						hostIds = hostIds.replace(reply.result.properties.hostId+"/","");
						if(hostIds.length<=1){
							loadingend(); 
						}
						if( reply.status == "exception" )
						{
	 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
							{
								top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
									window.location.reload();
								});
							}
							else 
							{
								top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
								});
							}
						}
						else if (reply.result.status == "success")
						{
//								top.$.messager.alert("信息", reply.result.message, "info",function(){
//								});
						}
						else
						{
//								top.$.messager.alert('警告', reply.result.message, 'warning',function(){
//								});
						}
					}
				);
			}
		}
		loadingend();
		top.$.messager.alert('提示','已重启完毕', 'info',function(){
			window.location.reload();
		});
		        
}
function stopAllHost(){  
	var allHost = [];
	allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
	$('#start-all').addClass("disable");
	$('#start-all').attr("start-all","2");
	$('#shutdown-all').addClass("disable");
	$('#shutdown-all').attr("shutdown-all","2");
	$('#restart-all').addClass("disable");
	$('#restart-all').attr("restart-all","2");
	var idList = new Array();
	var index = 0;
	var flag = 0; 
	for(var i=0;i<allHost.length;i++){
		var str = new String(allHost[i].data);
		var id = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
		var stopHost = $('#shutdown'+id).attr("stopHost");
		if(str.indexOf("运行中",0)<0 || stopHost=="2"){
			flag++;
			if(flag>=allHost.length){
				top.$.messager.alert("提示","未找到需要关闭的云主机","info");
				$('#start-all').removeClass("disable");
				$('#start-all').attr("start-all","1");
				$('#shutdown-all').removeClass("disable");
				$('#shutdown-all').attr("shutdown-all","1");
				$('#restart-all').removeClass("disable");
				$('#restart-all').attr("restart-all","1");
				loadingend();
				return;
			}
			continue;
		}else{
			idList.push(id);
			$('#status'+id).html("关机中...");
			$('#shutdown'+id).addClass("alpha");
			$('#shutdown'+id).attr("stopHost","2");
			$('#restart'+id).addClass("alpha");
			$('#restart'+id).attr("restartHost","2");
			$('#halt'+id).addClass("alpha");
			$('#halt'+id).attr("haltHost","2");
			$('#outOfService'+id).addClass("alpha");
			$('#outOfService'+id).attr("outofservice","2");
			ajax.remoteCall("bean://cloudHostService:stopCloudHost",
 				[ id ], 
 				function(reply) {
 					if (reply.status=="exception")
 					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
 						{
 							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
 								window.location.reload();
 							});
 						}
 						else 
 						{
 							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
 							});
 						}
 					}
 					else if(reply.result.status=="success")
 					{
 						
							if(index<idList.length){
								refreshStatus(idList[index++]);
							}
 						
 					}else{
				//		top.$.messager.alert("信息", reply.result.message, "info",function(){
 							
 				//		});
 					}
		    }); 
		}
	}
	loadingend();
		        
}
</script>
</body>
</html> 
