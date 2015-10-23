<%@page import="com.zhicloud.op.service.constant.AppConstant"%>

<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<% 
	String balance = (String)request.getAttribute("balance");
	Integer cloudHostCount = (Integer)request.getAttribute("cloudHostCount");
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
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/dep/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
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
	<%@ include file="/src/common/tpl/u_header.jsp"%>
    <div class="main">
    		<div class="wrap"><%@ include file="/src/common/tpl/u_mcslider.jsp"%></div>
      <div class="box">
        <div class="sidebar l" style="height:516px">
          <div style="width:238px; height:20px; padding:15px 0 0 0; text-align:center"><b>余额</b></div>
          <div style="width:238px; height:40px; line-height:40px; padding:0 0 15px 0; font-size:36px; text-align:center"><b><%=balance %></b></div>
          <a  id="toRecharge" class="greenbutton" href="javascript:void(0);" style="margin:0 auto; width:160px">立即充值</a> <a href="javascript:void(0);" id="cloudhost" style="display:block;margin:15px auto 0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_server_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262; border-top:solid 1px #b2b2b2"><%=cloudHostCount %></a> <a class="bluebutton" href="javascript:void(0);" id="cloudhost1" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">云主机</a> <a href="javascript:void(0);" id="my_cloud_disk1" style="display:block;margin:0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_storage_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262;">0</a> <a class="bluebutton" href="javascript:void(0);" id="my_cloud_disk" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">云硬盘</a> </div>
        <div class="r" style="width:720px;">
          <div id="title_message">&nbsp;</div>
          <table id="messagetable" class="easyui-datagrid" data-options="
           url: '<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=userMessageService&method=queryUserMessage',
		   queryParams: {},
           border:false,showHeader:false,singleSelect:true,scrollbarSize:0,view: createView(),pagination:true,onLoadSuccess: onLoadSuccess">
            <thead id="hang">
              <tr>
                <th data-options="field:'icon',width:40" formatter="formatImg"></th>
                <th data-options="field:'content',width:640"></th>
                <th data-options="field:'id',width:40" formatter="formatImg_remove" ></th>
              </tr>
            </thead> 
          </table>
        </div>
      </div>
      <div class="clear">&nbsp;</div>
    </div>
    <%@ include file="/src/common/tpl/u_footer.jsp"%>
  </div>  
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");

$(document).ready(function(){ 
	$("#toRecharge").click(function(){
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage";
	});
	$("#cloudhost").click(function(){
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage";
		
	});
	$("#cloudhost1").click(function(){
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage";
		
	});
	$("#my_cloud_disk").click(function(){
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cloudDiskService&method=managePage";
		
	});
	$("#my_cloud_disk1").click(function(){
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cloudDiskService&method=managePage";
		
	});
});
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
	
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
}
function  onLoadSuccess(){   
	$("tr").each(function() { 
		$(this).find("a[name=remove]").click(function (){
			$this = $(this);
			rowIndex = $this.attr("row_index");
			var data = $("#messagetable").datagrid("getData");
			var id = data.rows[rowIndex].id;  
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://userMessageService:deleteUserMessage",
						[ id ], 
						function(reply) {
							if (reply.status == "exception") {
								if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
									top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
										window.location.reload();
									});
								}else{
									top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
										window.location.reload();
									});
								}
							} else { 
								$('#messagetable').datagrid('reload');
							}
						}
					); 
		        }  
		    });
		});
		$(this).find("a[name=remove]").hide();   
    });
	$("tr").each(function() {

        $(this).hover(function() { 
        	$(this).find("a[name=remove]").show(); 
        },
	    function() { 
			   $(this).find("a[name=remove]").hide();  
	     });
  });
}
$(function(){
	navHighlight("umc","umov");
})
$(function(){
	var pager = $('#messagetable').datagrid().datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
})
function formatImg(val, row)
{ 
	return '<img src="<%=request.getContextPath()%>/image/icon_server_blue.png" width="20" height="20" alt=" " />';
}
function formatImg_remove(val, row,index)
{ 
	return '<a href="javascript:void(0);" name="remove" row_index="'+index+'"><img   src="image/icon_remove_black.png" width="20" height="20" alt=" "  class="view-detail"/></a>';
}

</script>
</body>
</html>