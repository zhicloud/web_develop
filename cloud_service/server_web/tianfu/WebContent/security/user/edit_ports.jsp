<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<CloudHostOpenPortVO> openPortVO  = (List<CloudHostOpenPortVO>)request.getAttribute("ports");
	List<CloudHostOpenPortVO> defaultPortVO  = (List<CloudHostOpenPortVO>)request.getAttribute("default_portsVO");
	String defaultPorts  = (String)request.getAttribute("default_ports");
	String defaultNames  = (String)request.getAttribute("default_names");
	String host_id = (String)request.getAttribute("host_id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- edit_port.jsp -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var a = '<%= request.getContextPath() %>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;
var defaultPorts = '<%=defaultPorts %>';
var defaultNames = '<%=defaultNames %>';
var defaultPortsArr = new Array();
defaultPortsArr = defaultPorts.split(",");
var defaultNamesArr = new Array();
defaultNamesArr = defaultNames.split(",");
var maxPorts = '<%=AppProperties.getValue("cd_max_ports", "") %>';
var globe_total = 0;



	$(document).ready(function() {
		init(10, 2);
		if (name != '') {
			inituser(name, 0);
		} else {

			inituser();
		}
		initstep(1);
		checkPortsCount();
	});

	function getLoginInfo(name, message, userId) {
		slideright();
		inituser(name, message);
		window.location.reload();
	};

	function addPort() {
		var boxArray = document.getElementsByName('ports'); 
		var total = 0;
	 	for(var i=0;i<boxArray.length;i++){
		 	if(boxArray[i].checked){
		 	  total++;
		 	}
	 	}
	 	if(total == maxPorts){
	 		top.$.messager.alert('警告','端口达到最大上限<br>如果需要开通更多端口，请联系管理员!','warning'); 
			$("#newPort").val("");
	 		return;
	 	}
		var protocol = $("#protocol").combobox('getValue');
		var protocolName = "";
		var name = "自定义";
		if (protocol == "1") {
			protocolName = "TCP";
		} else if (protocol == "2") {
			protocolName = "UDP";
		}
		var newPort = $("#newPort").val();
		if (newPort == 0 || newPort > 65535) {
			top.$.messager.alert('警告', '端口范围在1-65535之间，请重新输入', 'warning');
			$("#newPort").val("");
			return;
		}else {
			var value = "";
			var boxArray = document.getElementsByName('ports');
			for (var i = 0; i < boxArray.length; i++) {
				value = value + boxArray[i].value + ",";
			}
			if (value.indexOf(protocol + '&' + newPort + ",") != -1) {
				top.$.messager.alert('提示', '该端口已存在，请确认后重新输入', 'warning');
				$("#newPort").val("");
				return;
			}
			var newPortInt = parseInt(newPort)
			var n = defaultPortsArr.indexOf(newPortInt);
			if(n >= 0) {
				name = defaultNamesArr[n];
			}
			var html = '<tr>'
					+ '  <td style="width:28px"><div class="datagrid-cell">'
					+ '<input name="ports" type="checkbox" checked="checked" onclick="checkPortsCount(this);" value="'
					+ protocol
					+ '&'
					+ newPort
					+ '" />'
					+ '     </div></td>'
					+ '   <td style="width:231px"><div class="datagrid-cell">'+name+'</div></td>'
					+ '   <td style="width:231px"><div class="datagrid-cell">'
					+ protocolName
					+ '</div></td>'
					+ '   <td style="width:230px"><div class="datagrid-cell">'
					+ newPort
					+ '</div></td>'
					+ '   <td style="width:230px"><div class="datagrid-cell">未分配</div></td>'
					+ ' </tr>';

			$("#porttable").append(html);
			$("#newPort").val("");
			checkPortsCount(this);
		}
	};
	
	function checkPortsCount(obj) {
		var boxArray = document.getElementsByName('ports');
		var total = 0;
		for (var i = 0; i < boxArray.length; i++) {
			if (boxArray[i].checked) {
				total++;
			}
		}
		globe_total = total;
		if(globe_total > maxPorts){
	 		$(obj).removeAttr("checked");
	 		top.$.messager.alert('警告','端口达到最大上限<br>如果需要开通更多端口，请联系管理员!','warning');
	 		return;
	 	}
		$("#portsCount").html(total);
	};

	function modify_port_btn() {
		var formData = $.formToBean(modify_port_form);
		ajax.remoteCall("bean://cloudHostService:addPort", [ formData ],
				function(reply) {
					if (reply.status == "exception") {
						top.$.messager.alert('警告', reply.exceptionMessage,
								'warning');
					} else if (reply.result.status == "success") {
						top.$.messager.alert('提示', reply.result.message,
								'info', function() {
									window.location.reload();
								});
					} else {
						top.$.messager.alert('警告', reply.result.message,
								'warning');
					}
				});
	};
	
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
     <div class="titlebar" style="width:720px;padding:15px 0 5px 0;">
              <div class="blocks l">
                <div class="l"><b id="portsCount"></b>种服务的端口已开启</div>
              </div>
              <div class="blocks r"><a href="javascript:void(0);" class="bluebutton r" style="width:80px;"  onclick="addPort();">添加</a>
                 <div class="r" style="padding:0 10px 0 0">
                  <input  class="textbox" type="text" name="newPort" id="newPort" style="width:58px;height:28px" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></input>
                </div>
                <div class="r" style="padding:0 10px 0 0">
                  <select class="easyui-combobox" id="protocol" data-options="width:'60',height:'30',panelHeight:'auto',editable:false">
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
      <form id="modify_port_form" method="post">
      <input type="hidden" name="host_id" value="<%=host_id%>"/>
		<div class="box" style="overflow-y:scroll; width:720px; height:410px;margin:0 auto 0 auto">
              <table id="porttable" cellpadding="0" cellspacing="0" border="0" width="100%">
               <tr class="headtr">
                  <td style="width:28px">&nbsp;</td>
                  <td style="width:231px"><div class="datagrid-cell">服务名称</div></td>
                  <td style="width:231px"><div class="datagrid-cell">协议</div></td>
                  <td style="width:230px"><div class="datagrid-cell">端口</div></td>
                  <td style="width:230px"><div class="datagrid-cell">外网端口</div></td>
                  
                </tr>
              <%
				for(CloudHostOpenPortVO defaultPort : defaultPortVO){
				%>
				<tr>
				<td style="width:28px"><div class="datagrid-cell">
				<input name="ports" type="checkbox" checked="checked" value="<%=defaultPort.getProtocol() %>&<%=defaultPort.getPort() %>" disabled="disabled"/>
				</div></td>
				<td style="width:231px"><div class="datagrid-cell"><%=defaultPort.getName() == null ? "" : defaultPort.getName()%></div></td>
				<td style="width:231px"><div class="datagrid-cell"><%=(defaultPort.getProtocol() == 1 ? "TCP" : "UDP") %></div></td>
				<td style="width:230px"><div class="datagrid-cell"><%=defaultPort.getPort() %></div></td>
				<td style="width:230px"><div class="datagrid-cell"><%=defaultPort.getOuterPort() %></div></td>
				</tr>

				<% 
				}

                   for(CloudHostOpenPortVO port : openPortVO){
              	   %>
	                <tr>
	                  <td style="width:28px"><div class="datagrid-cell">
	                      <input name="ports" type="checkbox" checked="checked"  onclick="checkPortsCount(this);" value="<%=port.getProtocol() %>&<%=port.getPort() %>" />
	                    </div></td>
	                  <td style="width:231px"><div class="datagrid-cell"><%=port.getName() == null ? "自定义" : port.getName()%></div></td>
	                  <td style="width:231px"><div class="datagrid-cell"><%=(port.getProtocol() == 1 ? "TCP" : "UDP") %></div></td>
	                  <td style="width:230px"><div class="datagrid-cell"><%=port.getPort() %></div></td>
	                  <td style="width:230px"><div class="datagrid-cell"><%=port.getOuterPort() %></div></td>
	                </tr>
              	   
              	   <% 
                   }
                %>   
              </table>
            </div>
        <div style="width:720px; margin:0 auto 0 auto;">

             <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" onclick="modify_port_btn()">确认修改</a><a class="graybutton r" href="javascript:void(0);" onclick="window.history.back()">返回</a></div>
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
    <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>
