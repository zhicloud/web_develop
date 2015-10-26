<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.BillVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	
	List<BillVO> billList = (List<BillVO>)request.getAttribute("billList");
	if(billList.isEmpty()){
%>
		<center>没有相关记录</center>
<%
}
	for( BillVO bill : billList )
	{
%>
<div class="bill_item_outer_box"
	billId="<%=bill.getId()%>"  
	style="
		border:1px solid #dddddd; 
		background-color:#fdfdfd; 
		margin:5px; 
		padding:3px;
	">
	<table style="width:100%;">
		<tr>
			<td>
				<div>
					<span class="bill_item_label">用户名：<%=bill.getUserAccount()%></span>
					<span class="bill_item_label">总价：<%=bill.getFee()%>元</span>
					<span class="bill_item_label">应付费时间：<%=StringUtil.formatDateString(bill.getPayableTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd")%></span>
					<span class="bill_item_label">实际付费时间：<%=StringUtil.formatDateString(bill.getPaymentTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm")%></span>
				</div>
				<div class="bill_item_buttons_box" style="text-align:right;">
					<a href="#" class="easyui-linkbutton view-detail" data-options="iconCls:'icon-search'">查看详情</a>
				</div>
			</td>
		</tr>
	</table>
</div>
<%
	}
%>

