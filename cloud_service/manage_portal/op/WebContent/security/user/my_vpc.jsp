<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.VpcBaseInfoVO"%>
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
 	String newRegion = (String)request.getAttribute("region"); 
	List<VpcBaseInfoVO> vpcList = (List<VpcBaseInfoVO>)request.getAttribute("vpcList");
	
	String message = "";
 	if(vpcList.isEmpty()){
		message = "没有相关记录";
	}else{
		message = "共找到"+vpcList.size()+"条相关记录";
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
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/dep/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
	
</head>

<body>
<div class="page">
	<div class="pageleft">
		<jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
		<div class="main">
			<div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
			<div class="titlebar">
				<div class="tabbar l"><a id="all_vpc"   name="vpc_region" region="0"  href="javascript:void(0);" class="active" onclick="reloadList('');">全部</a>　｜　<a   region="1" id="guangzhou_vpc"  name="vpc_region" onclick="reloadList('1');" href="javascript:void(0);">广州</a>　｜　<a id="hongkang_vpc" region="4" name="vpc_region" onclick="reloadList('4');" href="javascript:void(0);">香港</a>　｜　<a id="chengdu_vpc" region="2" name="vpc_region" href="javascript:void(0);" onclick="reloadList('2');">成都</a></div>
				<a id="create_vpc" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage" style="cursor:pointer;" class="greenbutton r">创建专属云</a>
			</div>
			<div id="one_cloud_disk" style="width:980px;padding: 10px 0 0 0; margin:0 auto;">
			<table id="cloud_disk_datagrid" class="easyui-datagrid" data-options="border:false,showHeader:false,singleSelect:true,scrollbarSize:0,fitColumns:true">
			<thead>
            <tr>
              <th data-options="field:'data',width:980"><%=message %></th>
             </tr>
          	</thead>
			<tbody> 
              <%
                for(VpcBaseInfoVO vpc : vpcList){
                	
               %>
            <tr>
              <td><div class="box" >
                  <div class="listleft l" style="width:550px">
                    <div class="div1 l">
                      <%if(vpc.getType()==2){%>
                    	 <img src="<%=request.getContextPath() %>/image/icon_agent.png" width="20" height="20" class="agenticon" />
                     <%} %>
                      <div class="listicon vpc_name"> <a href="javascript:void(0);" class="btn vpc">&nbsp;</a>
                        <div class="text">
                          <input id="<%=vpc.getId() %>" class="vpc_display_name" type="text" onblur="changeVpcName()" value="<%=vpc.getDisplayName()%>"/>
                          <label for="<%=vpc.getId() %>" class="smalllabel l"   style="cursor:pinter; background:url(<%=request.getContextPath()%>/image/icon_edit.png) no-repeat center right" title="VPC名称"></label>
                        </div>
                      </div>
                    </div>
                    <div class="div2 l" style="width:200px;">
                      <div class="listicon l" style="width:100px" title="主机个数">
                        <div class="btn btnhost">&nbsp;</div>
                        <div class="text" style="overflow:hidden;text-overflow:ellipsis; "><%=vpc.getHostAmount() %></div>
                      </div>
                      <div class="listicon l" style="width:100px" title="IP个数">
                        <div class="btn btnip">&nbsp;</div>
                        <div class="text" style="overflow:hidden;text-overflow:ellipsis; "><%=vpc.getIpAmount() %></div>
                      </div>  
                    </div>
                    <div class="div4 l" style="width:526px;margin:-1px 0 0 0px;">描述:<%=vpc.getDescription() %>
                    </div>
                  </div> 
                  <%if(vpc.getStatus()!=2){ %>
                     <div class="listright l" style="width:290px; padding:1px 0 0 0" name="a">
	                    <input name="" type="button" class="btn btn14  l modify-allocation" onclick="window.location.href='<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=toVpcHostList&vpcId=<%=vpc.getId() %>';sp();" title="主机详情"/>
	                    <input name="" type="button" class="btn btn15  l modify-allocation" onclick="window.location.href='<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=vpcIpManagePage&vpcId=<%=vpc.getId() %>';sp();" title="IP"/>
	                    <input name="" type="button" class="btn btn17  l modify-allocation" onclick="window.location.href='<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=vpcNetworkManagePage&vpcId=<%=vpc.getId() %>';sp();" title="网络配置"/>
<!-- 		                <input name="" type="button" class="btn btn7 l  modify-allocation" title="修改信息"/> -->
		                <input name="" type="button" class="btn btn16 l out-of-service" onclick="disabledVpc('<%=vpc.getId() %>');sp();" title="停用"/>
<%-- 		                <input name="" type="button" class="btn newbtn5 l out-of-service" onclick="abledVpc('<%=vpc.getId() %>');" title="恢复"/> --%>
<!-- 		                <input name="" type="button" class="btn btn5 l" title="删除"/> -->
                     </div>	
                     <%}else{ %>
                     	<div class="listright l">
			                    <div class="listinfo">您的VPC已停用，我们将为您保存数据至 
			                    <%
			                    if(vpc.getModifyTime()==null){
			                    %>
			                    7天后
			                    <%	
			                    }else{
				                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				                    Date date = StringUtil.stringToDate(vpc.getModifyTime(),"yyyyMMddHHmmssSSS");
				                    Calendar calendar = Calendar.getInstance();
				                    calendar.setTime(date);
				                    calendar.add(Calendar.DAY_OF_MONTH,7);
			                    %>
			                    <%=sdf.format(calendar.getTime())%>
			                    <%} %>
			                  	  ，<br />到期VPC及数据将会删除。</div>
			                    <a href="javascript:void(0);" onclick="abledVpc('<%=vpc.getId()%>');sp();" class="graybutton r reactivate"><div class="r">恢复服务</div><div class="btn11 l">&nbsp;</div></a>
		           			</div>
                     <%} %>
                 </div>
                 </td>
            </tr> 
            <%
                }
            %>
            </tbody>
            </table>
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
<script type="text/javascript">
window.name = "selfWin";
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var userName = "<%=loginInfo.getAccount()%>";
ajax.async = false;
var current_vpc_id = "";
var old_vpc_name = "";
// 布局初始化
$("#uploaded_file_datagrid").height( $(document.body).height());

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

$(document).ready(function(){
	navHighlight("umc","umec"); 
	initstep(1);
	//-------------- 
	var new_region = "<%=newRegion%>";
	if(new_region == "1"){
		$("#all_vpc").removeClass("active");
		$("#guangzhou_vpc").addClass("active");
	}else if(new_region == "4"){
		$("#all_vpc").removeClass("active");
		$("#hongkang_vpc").addClass("active");
	}else if(new_region == "2"){
		$("#all_vpc").removeClass("active");
		$("#chengdu_vpc").addClass("active");
	}
	//获取当前主机ID
	$(".box .vpc_display_name").focus(function(){
		current_vpc_id = $(this).attr("id");
		old_vpc_name = $(this).val();
	});
});

function reloadList(region){
	window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=managePage&region="+region;
}
 
function dataFormatter(val)	{   
	return CapacityUtil.toCapacityLabel(val,0);
}  

function disabledVpc(id){
	top.$.messager.confirm("确认", "确定停用该VPC？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:disableVpc", 
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
		}else{
			return;
		}
	});
}
function abledVpc(id){
	top.$.messager.confirm("确认", "确定恢复该VPC？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:ableVpc", 
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
		}else{
			return;
		}
	});
}
 
function changeVpcName(){
	var new_vpc_name = $("#"+current_vpc_id).val();
	if(old_vpc_name == new_vpc_name){
		return;
	}
	if(new_vpc_name == ''||new_vpc_name.length>30){ 
		top.$.messager.alert("提示","VPC名必须为1-30个字符","info",function(){
			window.location.reload();
		});
		return ;
	}
	if(current_vpc_id!='' && new_vpc_name!=''){
		ajax.remoteCall("bean://vpcService:updateVpcDisplayNameById", 
			[ current_vpc_id,new_vpc_name],
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
function sp(){
	event.stopPropagation();
}
function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
}
</script>	
</body>
 
</html>
