<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.CloudHostBillDetailVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	CloudHostBillDetailVO cloudHostBillDetail = (CloudHostBillDetailVO)request.getAttribute("cloudHostBillDetail");
%>
<div id="bill_detail_cloud_host_detail_dlg_container">

	<style type="text/css" scoped="scoped">
	#bill_detail_cloud_host_detail_dlg .bill_detail_cloud_host_detail_label {
		display: inline-block;
		padding: 5px 10px 5px 10px;
	}
	</style>

	<div id="bill_detail_cloud_host_detail_dlg" class="easyui-dialog" title="云主机费用详情"
		style="width:600px; height:350px; padding:5px;"
		data-options="
			modal: true,
			onMove:_bill_detail_cloud_host_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _bill_detail_cloud_host_detail_dlg_scope_;
			}
		">
		<div  style="
 				background-color:#fdfdfd;  
				margin:1px; 
				padding:1px;
			">
			<table style="width:100%;">
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">云主机：<%=cloudHostBillDetail.getSysImageId() %></span>
					</td>
					<td width="280px"></td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">CPU：<%=cloudHostBillDetail.getCpuCore() %>&nbsp;&nbsp;核</span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">内存：<%=cloudHostBillDetail.getMemoryText(2) %></span>
					</td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">系统磁盘：<%=cloudHostBillDetail.getSysDiskText(2) %></span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">数据磁盘：<%=cloudHostBillDetail.getDataDiskText(2) %></span>
					</td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">网络带宽：<%=cloudHostBillDetail.getNetworkTrafficText(2) %></span>
					</td>
					<td width="280px">
					</td>
				</tr>
			</table>
		</div>
		<hr>
		<div  style="
				background-color:#fdfdfd; 
				margin:1px; 
				padding:1px;
			">
			<table style="width:100%;">
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">CPU运算时长：<%=cloudHostBillDetail.getCpuUsed() %>小时</span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">内存占用均值：<%=cloudHostBillDetail.getMemoryUsedText(2) %></span>
					</td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">磁盘读总流量：<%=cloudHostBillDetail.getDiskReadText(2) %></span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">磁盘写总流量：<%=cloudHostBillDetail.getDiskWriteText(2) %></span>
					</td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">网络读总流量：</span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">网络写总流量：</span>
					</td>
				</tr>
			</table>
		</div>
		<hr>
		<div  style="
				background-color:#fdfdfd; 
				margin:1px; 
				padding:1px;
			">
			<table style="width:100%;">
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">付费方式：<%=cloudHostBillDetail.getTypeText() %></span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">购买时段：
						<%=StringUtil.formatDateString(cloudHostBillDetail.getStartTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd") %>
						至
						<%=StringUtil.formatDateString(cloudHostBillDetail.getEndTime(),   "yyyyMMddHHmmssSSSS", "yyyy-MM-dd") %>
						</span>
					</td>
				</tr>
				<tr>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">总价：<%=cloudHostBillDetail.getFee() %></span>
					</td>
					<td width="280px">
						<span class="bill_detail_cloud_host_detail_label">付费状态：<%=cloudHostBillDetail.getIsPaidText() %></span>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">

var _bill_detail_cloud_host_detail_dlg_scope_ = new function(){
	self.onMove = function(){
		var thisId = "#bill_detail_cloud_host_detail_dlg";
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
	};
	
};

</script>
