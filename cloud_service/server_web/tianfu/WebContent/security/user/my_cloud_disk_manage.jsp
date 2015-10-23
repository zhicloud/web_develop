<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="java.util.List"%>
<%@page import = "java.math.BigInteger "%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
	List<CloudDiskVO> cloudDiskList = (List<CloudDiskVO>)request.getAttribute("cloudDiskList");
	String message = "";
	String newRegion = (String)request.getAttribute("region");
	if(cloudDiskList.isEmpty()){
		message = "没有相关记录";
	}else{
		message = "共找到"+cloudDiskList.size()+"条相关记录";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- cloud_disk_manage.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
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

window.name = "selfWin";

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var userName = "<%=loginInfo.getAccount()%>";
ajax.async = false;

// 布局初始化
$("#uploaded_file_datagrid").height( $(document.body).height());

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

$(document).ready(function(){
	init(10,3);
	if(userName!= ''){ 
	   inituser(userName,0);
	}else{
		
	   inituser();
	}   
	initstep(1);
	//--------------
	var new_region = "<%=newRegion%>";
	if(new_region == "1"){
		$("#all_cloud_disk").removeClass("active");
		$("#guangzhou_cloud_disk").addClass("active");
	}else if(new_region == "4"){
		$("#all_cloud_disk").removeClass("active");
		$("#hongkang_cloud_disk").addClass("active");
	}else{
		
	}
	//鼠标移上获取ID
	var cloudDiskId = "";
	$("#one_cloud_disk .box").hover(function(evt){
		cloudDiskId = $(this).attr("cloudDiskId");
	});
	// 查询
	$("a[name='cloud_disk']").click(function(){
		var region = $(this).attr("region");
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudDiskService&method=managePage&region="+encodeURIComponent(region);
		return;
	});	
	//创建云硬盘
	$("#create_cloud_disk").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudDiskService&method=addPage";
	});
	//修改配置
	$("#one_cloud_disk .modify-allocation").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudDiskService&method=modPage&diskId="+encodeURIComponent(cloudDiskId);
	});
	//监控资源
	$("#one_cloud_disk .my-cloud-disk-detail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudDiskService&method=cloudDiskDetailPage&cloudDiskId="+encodeURIComponent(cloudDiskId);
	});
	//申请停用云硬盘
	$("#one_cloud_disk .out-of-service").click(function(){
		if(cloudDiskId="undefined" || cloudDiskId=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定要停用该云硬盘吗？", function (r) { 
			if(r){
				ajax.remoteCall("bean://cloudDiskService:inactivateCloudDisk",
					[ cloudDiskId ], 
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
	});
	//申请恢复云硬盘
	$("#one_cloud_disk .reactivate").click(function(){
		if(cloudDiskId="undefined" || cloudDiskId=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定要恢复该云硬盘吗？", function (r) { 
			if(r){
				ajax.remoteCall("bean://cloudDiskService:reactivateCloudDisk",
					[ cloudDiskId ], 
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
	});
	//删除云硬盘
	$("#one_cloud_disk .delete-cloud-disk").click(function(){
		if(cloudDiskId="undefined" || cloudDiskId.trim()=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {  
				ajax.remoteCall("bean://cloudDiskService:deleteCloudDiskById",
					[ cloudDiskId ], 
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
    							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){});
    						}
						}
						else
						{
							top.$.messager.alert("提示", reply.result.message, "info",function(){
								window.location.reload();
							});
						}
					}
				);
	        }  
	    }); 
	});
	
});
function operateColumnFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton del_btn'>删除</a>\
			</div>";
}
 
function dataFormatter(val)	{   
	return CapacityUtil.toCapacityLabel(val,0);
}  

function statusFormatter(val)	{   
	if(val == 1){
		return "关机";
	}else if(val == 2){
		return "使用中";
	}else if(val == 3){
		return "警告";
	}else if(val == 4){
		return "故障";
	}
}  

function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
}
</script>	
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
        <div class="tabbar l"><a id="all_cloud_disk" name="cloud_disk" region="0"  href="#" class="active">全部</a>　｜　<a id="guangzhou_cloud_disk" region="1" name="cloud_disk"  href="#">广州</a>　｜　<a id="hongkang_cloud_disk" region="4" name="cloud_disk" href="#">香港</a></div>
        <a id="create_cloud_disk" href="#" style="cursor:pointer;" class="greenbutton r">创建云硬盘</a></div>
       <div id="one_cloud_disk" style="width:720px;padding: 10px 0 0 0; margin:0 auto;">
			<table id="cloud_disk_datagrid" class="easyui-datagrid" data-options="border:false,showHeader:false,singleSelect:true,scrollbarSize:0,fitColumns:true">
			<thead>
            <tr>
              <th data-options="field:'data',width:720"><%=message %></th>
            </tr>
          	</thead>
			<tbody>
		 <%
		 	if(cloudDiskList.isEmpty()){
		 %>
		 	<center>没有相关记录</center>
		 <%
		 	}
          	for(CloudDiskVO cloudDiskVO : cloudDiskList){
          %>
         <tr>
              <td><div class="box" cloudDiskId="<%=cloudDiskVO.getId()%>">
                  <div class="listleft l" style="width:492px">
                    <div class="div1 l">
                      <div class="listicon"> <a href="javascript:void(0);" class="btn storage">&nbsp;</a>
                        <div class="text">
                          <input id="t1<%=cloudDiskVO.getId()%>" name="" type="text" value="<%=cloudDiskVO.getName()%>"/>
                          <label for="t1<%=cloudDiskVO.getId()%>" class="smalllabel l"   style="cursor:pinter; background:url(<%=request.getContextPath()%>/image/icon_edit.png) no-repeat center right" title="<%=cloudDiskVO.getName()%>"></label>
                        </div>
                      </div>
                    </div>
                    <div class="div2 l" style="width:389px;">
                      <div class="listicon l" style="width:120px">
                        <div class="btn btn101">&nbsp;</div>
                        <div class="text"><%=CapacityUtil.toGBValue(cloudDiskVO.getDisk(),0)%></div>
                      </div>
                      <div class="listicon l" style="width:149px">
                        <div class="btn btn102">&nbsp;</div>
                        <div class="text">IP:<%=cloudDiskVO.getInnerIp()==null?"无":cloudDiskVO.getInnerIp()%></div>
                      </div>
                      <div class="listicon l" style="width:120px">
                        <div class="btn btn103">&nbsp;</div>
                        <div class="text">端口:<%=cloudDiskVO.getInnerPort()==null?"无":cloudDiskVO.getInnerPort()%></div>
                      </div>
                    </div>
                    <%if("关机".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">已关机</strong></div>
                    <%}else if("运行".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l">运行中</div>
                    <%}else if("创建中".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建中</strong></div>
                    <%}else if("创建失败".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建失败</strong></div>
                    <%}else if("告警".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="orange">告警</strong></div>
                    <%}else if("故障".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="red">故障</strong></div>
                    <%} %>
                    <div class="div4 l" style="width:341px;">iqn:2010-06.iscsiraid:raid-2718000358:default-target
                    </div>
                  </div>
                  <%if("创建失败".equals(cloudDiskVO.getSummarizedStatusText())){ %>
                  	<div class="listright l" style="width:208px; padding:23px 0 0 0" name="a<%=cloudDiskVO.getId()%>">
		                <input name="" type="button" class="btn btn5 l delete-cloud-disk" title="删除"/>
	                    <input name="" type="button" class="btn btn7  l modify-allocation" title="修改配置"/>
	                    <input name="" type="button" class="btn btn12 l my-cloud-disk-detail" title="资源监控"/>
                    </div>
                  <%} else if("停机".equals(cloudDiskVO.getSummarizedStatusText())){%>
                  	<div class="listright l">
		                <div class="listinfo">您的云硬盘已停用，我们将为您保存数据至 
		                    <%
		                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                    Date date = StringUtil.stringToDate(cloudDiskVO.getInactiveTime(),"yyyyMMddHHmmssSSS");
		                    Calendar calendar = Calendar.getInstance();
		                    calendar.setTime(date);
		                    calendar.add(Calendar.DAY_OF_MONTH,7);
		                    %>
		                    <%=sdf.format(calendar.getTime())%>
		                   	 ，<br />到期数据将会删除。</div>
		                    <a href="#" class="graybutton r reactivate">恢复服务<div class="btn11 l">&nbsp;</div></a>
	           			</div>
                  <%}else{ %>
                  	<div class="listright l" style="width:208px; padding:23px 0 0 0" name="a<%=cloudDiskVO.getId()%>">
		                <input name="" type="button" class="btn btn5 l out-of-service" title="停用"/>
	                    <input name="" type="button" class="btn btn7  l modify-allocation" title="修改配置"/>
	                    <input name="" type="button" class="btn btn12 l my-cloud-disk-detail" title="资源监控"/>
                    </div>	
                <%} %>
                </div></td>
            </tr>
            <%
          	}
            %>
            </tbody>
            </table>
            </div>
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