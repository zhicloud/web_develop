<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.CloudHostBillDetailVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
    Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	List<CloudHostBillDetailVO> chbdList = (List<CloudHostBillDetailVO>)request.getAttribute("cloudHostBillDetailList");
%>
<div id="bill_detail_dlg_container">

	<style type="text/css" scoped="scoped">
	#bill_detail_dlg .bill_detail_label {
		display: inline-block;
		padding: 5px 10px 5px 10px;
	}
	</style>

	<div id="bill_detail_dlg" class="easyui-dialog" title="账单详情"
		style="width:700px; height:400px; padding:5px;"
		data-options="
			modal: true,
			onMove:_bill_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _bill_detail_dlg_scope_;
			}
		">
		<%
			for(CloudHostBillDetailVO chbd : chbdList){
		%>
		<div class="bill_detail_outer_box"
			billDetailId="<%=chbd.getId()%>"  
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
							<span class="bill_detail_label">云主机：<%=chbd.getSysImageId()%></span>
							<span class="bill_detail_label">CPU：<%=chbd.getCpuCore()%>核</span>
							<span class="bill_detail_label">内存：<%=chbd.getMemoryText(2)%></span>
							<span class="bill_detail_label">系统磁盘：<%=chbd.getSysDiskText(2)%></span>
							<span class="bill_detail_label">数据磁盘：<%=chbd.getDataDiskText(2)%></span>
						</div>
						<div>
							<span class="bill_detail_label">付费方式：<%=chbd.getTypeText()%></span>
							<span class="bill_detail_label">购买时段：
								<%=StringUtil.formatDateString(chbd.getStartTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd")%>
								至
								<%=StringUtil.formatDateString(chbd.getEndTime(),   "yyyyMMddHHmmssSSSS", "yyyy-MM-dd")%>
							</span>
							<span class="bill_detail_label">总价：<%=chbd.getFee()%>元</span>
							<span class="bill_detail_label">状态：<%=chbd.getIsPaidText()%></span>
						</div>
						<div class="bill_detail_buttons_box" style="text-align:right;">
							<a href="javascript:void(0);" class="easyui-linkbutton view-detail" data-options="iconCls:'icon-search'">查看详情</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
</div>

<script type="text/javascript">

var _bill_detail_dlg_scope_ = new function(){
		
	self.billDetailId = "";
	self.onMove = function(){
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
	};	
	self.showSingleDialogInThisPage = function(param)
	{
		var container = $("#cloud_host_bill_detail_container");
		if( container.length==0 )
		{
			container = $("<div id='cloud_host_bill_detail_container' style='display:none;'></div>").appendTo(document.body);
		}
		container.load(
			param.url, 
			function(){
				$.parser.parse(container.get(0));
				container.prop("_data_", param);
			}
		);
	};
	
	$(document).ready(function(){
		// 在鼠标移上去的时候将bill_id存起来
		$("#bill_detail_dlg .bill_detail_outer_box").hover(function(evt){
			self.billDetailId = $(this).attr("billDetailId");
		});
		// 查看详情
		$("#bill_detail_dlg .view-detail").click(function(evt){
			self.showSingleDialogInThisPage({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=queryCloudHostBillDetailPage&billDetailId="+encodeURIComponent(self.billDetailId),
				onClose : function(data) {
				}
			});
		});
	});
	
};
</script>

