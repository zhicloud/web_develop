<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.AccountBalanceDetailVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
    Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
    AccountBalanceDetailVO record = (AccountBalanceDetailVO)request.getAttribute("record");
%>
<div id="bill_detail_dlg_container">

	<style type="text/css" scoped="scoped">
	#bill_detail_dlg .bill_detail_label {
		display: inline-block;
		padding: 5px 10px 5px 10px;
	}
	</style>

	<div id="bill_detail_dlg" class="easyui-dialog" title="充值详情详情"
		style="width:700px; height:400px; padding:5px;"
		data-options="
			modal: true,
			onMove:onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _bill_detail_dlg_scope_;
			}
		"> 
		<div class="bill_detail_outer_box"
			billDetailId="<%=record.getId()%>"  
			style="
				border:1px solid #dddddd; 
				background-color:#fdfdfd; 
				margin:5px; 
				padding:3px;
			">
			<table >
				<tr>
					<td>
						<div>
							<span class="bill_detail_label">充值金额：<%=record.getAmount()%></span>
							<span class="bill_detail_label">充值方式：<%=record.getPayType() %> </span>
							<span class="bill_detail_label">充值时间：<%=StringUtil.formatDateString(record.getChangeTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd")%></span>
 
						</div>
						 
					</td>
				</tr>
			</table>
		</div> 
	</div>
</div>

<script type="text/javascript">
function onMove(){
	var thisId = "#bill_detail_dlg";
	var topValue = $(thisId).offset().top;
	var leftValue = $(thisId).offset().left;
	if(topValue==0){
		topValue = 30;
	}
	if(topValue<30){
		$(thisId).dialog('move',{
			left:leftValue,
			top:30
		});
		return;
	}
	if(leftValue>1315){
		$(thisId).dialog('move',{
			left:1300,
			top:topValue
		});
		return;
	}
	if(topValue>600){
		$(thisId).dialog('move',{
			left:leftValue,
			top:570
		});
		return;
	}
} 
</script>

