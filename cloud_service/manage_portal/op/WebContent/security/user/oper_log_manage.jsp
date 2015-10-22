<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title><%=AppConstant.PAGE_TITLE %></title>
 	<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
	<style type="text/css">.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber {text-align: center;}</style>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
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
        <div class="r">操作日志</div>
      </div>
      
		<div class="box">
		    <form id="big_form"  method="post">
				<table id="oper_log_datagrid" class="easyui-datagrid" data-options="url: '<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=queryOperLog',queryParams: {},border:false,singleSelect:true,scrollbarSize:0,pagination:true,toolbar: '#toolbar',remoteSort:false,fitColumns: true,pagination: true,pageList: [10, 20, 50, 100, 200],pageSize: 10,onLoadSuccess: onLoadSuccess">
					<thead>
						<tr>
							<th data-options="field:'operTime',width:140" formatter="formatTime">时间</th>
							<th data-options="field:'content',width:580">操作</th>
							<th data-options="field:'status',width:140" formatter="formatStatus">状态</th>
						</tr>
					</thead>
				</table> 
			</form>
		</div>

      <div class="clear">&nbsp;</div>
    </div>
    <jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>
  </div>
  <%-- <div class="pageright">
	<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div> --%>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(document).ready(function(){
	navHighlight("umc","umol");
});
window.name = "selfWin";

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;

$(function(){
	var pager = $('#oper_log_datagrid').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
})

function formatTime(val, row)
{
	if(val==null||val==''){
		return "";
	}
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}

function formatStatus(val, row)
{  
	if(val == 1){  
	    return "成功";  
	}else if(val == 2) {
	    return "失败";  
	} 
}  

function ColumnFormatter(value, row, index)
{
	if(value!=null&&value!=''){				
		return "<div row_index='"+index+"'>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton view-bill-detail'>查看详情</a>\
					</div>";
	}else{
		return "<div row_index='"+index+"'>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton view-recharge-detail'>查看详情</a>\
					</div>"; 
	}
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

function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	// 每一行的'查看详情列表'按钮
	$("a.view-bill-detail").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#oper_log_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].billId; 
		var createTime = data.rows[rowIndex].createTime;
		var totalPrice = data.rows[rowIndex].totalPrice;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=queryBillDetailPage",
			params: {
				"billId" : id,
				"createTime" :createTime,
				"totalPrice" :totalPrice
			},
			onClose : function(data) {
				self.query();
			}
		});
	});
	// 每一行的'查看详情列表'按钮
	$("a.view-recharge-detail").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#oper_log_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;  
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=queryRechargeDetailPage",
			params: {
				"id" : id 
			},
			onClose : function(data) {
				self.query();
			}
		});
	});
	 
}

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
	
}
$(function(){
	var pager = $('#logtable').datagrid().datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
})
</script>
</body>

</html>