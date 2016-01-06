<%@page import="com.zhicloud.op.vo.BillVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
%>
<!DOCTYPE html>
<!-- bill_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>运营商 - 账单管理</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
<body>
<div class="oper-wrap">
	<div class="tab-container">
		<div class="panel-header">
			<div class="panel-title">账单查询</div>
			<div class="panel-tool"></div>
		</div>
		<div id="toolbar" class="datagrid-toolbar">
			<div style="display: table; width: 100%;">
				<div style="display: table-cell; text-align: right;">
					<span class="sear-row">
						<label class="f-mr5">用户名</label> 
						<input type="text" id="account" class="messager-input" /> 
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_bill_btn">查询</a>
					</span>
				</div>
			</div>
		</div>
		<div id="bill_item_all_box" class="datagrid-toolbar" style="height:400px; border:0px;"></div>
	</div>
</div>

<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var billId = "";
// 初始化
$(document).ready(function(){
	var self = {};
	// 查询的函数
	self.query = function(){
		// 清除之前的内容，然后加载新的内容
		$("#bill_item_all_box").empty().load(
			"<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=billQueryResultPartPage",
			{
				"account" : $("#account").val().trim()
			},
			function(){
				$.parser.parse(this);
				// 在鼠标移上去的时候将bill_id存起来
				$("#bill_item_all_box .bill_item_outer_box").hover(function(evt){
					billId = $(this).attr("billId");
				});
				// 查看详情
				$("#bill_item_all_box .view-detail").click(function(evt){
					top.showSingleDialog({
						url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=queryBillDetailPage&billId="+encodeURIComponent(billId),
						onClose : function(data) {
						}
					});
				});
			}
		);
	};
	
	// 查询
	$("#query_bill_btn").click(function(){
		self.query();
	});
	
	$('#account').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	$("#query_bill_btn").click();
        	return false;
        }
    });
	// 初始仳
	self.query();
});
</script>
</body>
</html>