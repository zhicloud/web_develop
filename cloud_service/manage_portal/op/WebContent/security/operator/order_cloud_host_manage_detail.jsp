<%@page import="java.util.List"%> 
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
    UserOrderVO detail = (UserOrderVO)request.getAttribute("detail"); 
    BigDecimal i = new BigDecimal("1073741824");
    BigDecimal j = new BigDecimal("1000000");
%>

<div id="cloud_host_view_detail_dlg_container">
	<div id="cloud_host_view_detail_dlg" class="easyui-dialog" title="订购云主机详情"
		style="width:800px; height:600px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove:_cloud_host_view_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_view_detail_dlg_scope_;
			}
		">
		<form id="cloud_host_view_detail_dlg_form" method="post">
			
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
				<tr>
					<td style="padding:5px;">订购时间：<%=detail.getCreateTime() %></td>
					<td style="padding:5px;">状态：
					<%
					  if(detail.getProcessStatus()==0){
						  out.println("未处理");
					  }else if(detail.getProcessStatus()==1){
						  out.println("已创建");
						  
					  }else if(detail.getProcessStatus()==2){
						  out.println("失败");
						  
					  }else if(detail.getProcessStatus()==3){
						  out.println("正在创建");
						  
					  }
					%>
					</td>
				</tr>
				<tr>
					<td style="padding:5px;">CPU：<%=detail.getCpuCore() %>核</td>
					<td style="padding:5px;">内存：<%=detail.getMemory().divide(i) %>G</td>
				</tr>
				<tr>
					<td style="padding:5px;">系统磁盘：<%=detail.getSysDisk().divide(i) %>G</td>
					<td style="padding:5px;">数据磁盘：<%=detail.getDataDisk().divide(i) %>G</td>
				</tr>
				<tr>
					<td style="padding:5px;">网络带宽：<%=detail.getBandwidth().divide(j) %>Mbps</td>
					<td style="padding:5px;">所属用户：<%=detail.getAccount() %></td>
				</tr>
				<tr>
					<td style="padding:5px;">镜像：<%=detail.getImageName() %></td> 
				</tr>
				<tr>
 
					<td colspan="99"><hr /></td>
				</tr>
				 
				
			</table>
			
		</form>
		 
	</div>

</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript">
var _cloud_host_view_detail_dlg_scope_ = new function(){
	self.onMove = function(){
		var thisId = "#cloud_host_view_detail_dlg";
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

 


