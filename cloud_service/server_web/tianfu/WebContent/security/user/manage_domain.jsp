<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.vo.DomainCloudHostBindingVO"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
	String hostId = (String)request.getParameter("host_id");
	String displayName = (String)request.getParameter("display_name");	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- manage_domain.jsp -->
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
var host_id = "<%=hostId%>";
var display_name = "<%=displayName%>";
ajax.async = false;

	$(document).ready(function() {
		init(10, 2);
		if (name != '') {
			inituser(name, 0);
		} else {

			inituser();
		}
		initstep(1);
		
		$("#display_name").html(display_name);
	});
	
	function getLoginInfo(name, message, userId) {
		slideright();
		inituser(name, message);
		window.location.reload();
	};

	
	function dataGrid(){
		var pager = $('#manage_domain_datagrid').datagrid('getPager');
		pager.pagination({
			showPageList: false,
			showRefresh: false,
			displayMsg: '',
			beforePageText: '',
			afterPageText: ''
		 });            
	}
	
	function timeFormat(val)	
	{   
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}  


	//查询结果为空
	 function createView(){
		return $.extend({},$.fn.datagrid.defaults.view,{
		    onAfterRender:function(target){
		        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
		        var opts = $(target).datagrid('options');
		        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		        vc.children('div.datagrid-empty').remove();
		        if (!$(target).datagrid('getRows').length){
		            var d = $('<div class="datagrid-empty"></div>').html( '没有相关记录').appendTo(vc);
		            d.css({
		                position:'absolute',
		                left:0,
		                top:50,
		                width:'100%',
		                textAlign:'center'
		            });
		        }
		    }
	    });
	};
	
	function onLoadSuccess()
	{
		
		dataGrid();
			
	}

$(function(){
		// 绑定域名
		$("#add_domain_btn").click(function(){
			window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=addDomainPage&host_id="+encodeURIComponent(host_id);
		});
		
		// 查询
		$("#query_domain_btn").click(function(){
			var queryParams = {};
			queryParams.name = $("#name").val().trim();
			$('#manage_domain_datagrid').datagrid({
				"queryParams": queryParams
			});
			dataGrid();
		});
		
		// 删除
		$("#remove_domain_btn").click(function() {
			var rows = $('#manage_domain_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择解除绑定项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			var domains = rows.joinProperty("domain");
			top.$.messager.confirm("确认", "确定解除绑定?", function (r) {  
		        if (r) {    
						ajax.remoteCall("bean://cloudHostService:deleteDomainByIds",
							[ ids,  domains], 
							function(reply) {
								if (reply.status == "exception") {
									top.$.messager.alert('警告',reply.exceptionMessage,'warning');
								} else if (reply.result.status == "success") {
									$('#manage_domain_datagrid').datagrid('reload');
								} else {
									top.$.messager.alert('警告',reply.result.message,'warning', function(){
										$('#manage_domain_datagrid').datagrid('reload');
									});
								}
							}
						);
		        }  
		    });   
		});
		
		$('#name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_domain_btn").click();
	        }
	    });
});
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
	   		<div class="titlebar">
				<div class="l" style="font-size:12px;">主机名：<b><span id="display_name"></span></b></div>
       			<div class="r" style="font-size:12px;">提示：<b><span> 请先确保您的域名已绑定到公网IP</span></b></div>
      		</div>
	    		<div class="titlebar" style="padding-top:30px">
	      		<a href="javascript:void(0);" id="add_domain_btn" class="bluebutton l" style="margin-right:10px;width:110px"><img src="<%=request.getContextPath()%>/image/icon_upload.png" width="20" height="20" style="vertical-align:middle;margin-right:10px" />绑定域名</a>
	      		<a href="javascript:void(0);" id="remove_domain_btn" class="bluebutton l" style="margin-right:10px;width:110px"><img src="<%=request.getContextPath()%>/image/button_delete.png" width="20" height="20" style="vertical-align:middle;" />解除绑定</a>
	      		 <div class="r"><input id = "name" name="name" type="text" class="textbox" style="height:28px; width:108px;margin-right:10px;background:transparent"/>
	      		 	<a href="javascript:void(0);" id="query_domain_btn"><img src="<%=request.getContextPath()%>/image/button_search.png" width="30" height="30" style="vertical-align:middle;" /></a></div>
	      		</div>
	      		<div class="box">
				<table id="manage_domain_datagrid" class="easyui-datagrid"
					data-options="
						url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostService&method=getDomain&host_id=<%=hostId %>',
						queryParams: {},
						toolbar: '#toolbar',
						remoteSort:false,
						pagination: true,
						fitColumns: true,
						pageSize: 10,
						view: createView(),
						onLoadSuccess: onLoadSuccess
						">
					<thead>
						<tr>
							<th data-options="checkbox:true"></th>
	              			<th data-options="field:'domain', width:70" >域名</th>
	              			<th data-options="field:'name', width:40" >站点名称</th>
	              			<th data-options="field:'adminName', width:50" >网站管理员</th>
	              			<th data-options="field:'phone', width:70" >管理员手机</th>
	              			<th data-options="field:'email', width:70" >邮箱地址</th>
	             			<th data-options="field:'createTime', width:70"  formatter = "timeFormat">绑定时间</th>
						</tr>
					</thead>
				</table>
				</div>
	      <div class="clear">&nbsp;</div>
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
