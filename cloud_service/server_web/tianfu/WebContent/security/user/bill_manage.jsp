<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<% 
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- bill_manage.jsp -->
<html>
	<head> 
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="textml; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>用户 - 我的对账单</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#user_bill_datagrid {
			border: 0px solid red;
		}
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header,.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	
			<table id="user_bill_datagrid" class="easyui-datagrid" title="我的对账单"
				data-options="
					url: '<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=billQueryResultPartPage',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view:createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr> 
						<th field="userAccount" width="20%" sortable=true>用户名</th> 
						<th field="type" formatter="formatStatus" width="20%">类型</th>
						<th field="amount" width="20%">金额</th>
						<th field="changeTime" formatter="formatTime"  width="20%" sortable=true>时间</th> 
						<th field="billId" formatter="ColumnFormatter" width="10%">操作</th>
					</tr>
				</thead>
			</table> 
	
		</form>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		
		window.name = "selfWin";
		
		var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
		ajax.async = false;
		

		// 布局初始化
		$("#user_bill_datagrid").height( $(document.body).height());
		
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
			    return "充值";  
			}else if(val == 2) {
			    return "消费";  
			} 
		}  
		
		function ColumnFormatter(value, row, index)
		{
			if(value!=null&&value!=''){				
				return "<div row_index='"+index+"'>\
							<a href='#' class='datagrid_row_linkbutton view-bill-detail'>查看详情</a>\
	 					</div>";
			}else{
				return "<div row_index='"+index+"'>\
							<a href='#' class='datagrid_row_linkbutton view-recharge-detail'>查看详情</a>\
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
				var data = $("#user_bill_datagrid").datagrid("getData");
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
				var data = $("#user_bill_datagrid").datagrid("getData");
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
		
	</script>
</html>