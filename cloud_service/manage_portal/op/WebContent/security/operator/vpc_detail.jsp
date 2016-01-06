<%@page import="com.zhicloud.op.app.pool.CloudHostData"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.VpcBaseInfoVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	VpcBaseInfoVO vpcBaseInfoVO = (VpcBaseInfoVO)request.getAttribute("vpc");
%>
<!-- vpc_detail.jsp -->
<div id="vpc_detail_dlg_container">
	<div id="vpc_detail_dlg" class="easyui-dialog" title="专属云详情-[<%=vpcBaseInfoVO.getDisplayName() %>]"
		style="width:800px; height:300px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: _vpc_detail_dlg_scope_.onMove ,
			onClose: _vpc_detail_dlg_scope_.myOnClose ,
			onDestroy: function(){
 				delete _vpc_detail_dlg_scope_; 
			}
		">
		<form id="vpc_detail_dlg_form" method="post">
			
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
				<tr>
					<td style="padding:5px;">名称：<%=vpcBaseInfoVO.getDisplayName() %></td>
					<td style="padding:5px;">状态：<%=vpcBaseInfoVO.getStatusFormat() %></td>
				</tr>
				<tr>
					<td style="padding:5px;">所属用户：<%=vpcBaseInfoVO.getUserName() %></td>
					<td style="padding:5px;">地域：<%=vpcBaseInfoVO.getRegionFormat() %></td>
				</tr>
				<tr>
					<td style="padding:5px;">绑定IP数：<%=vpcBaseInfoVO.getIpAmount() %></td>
					<td style="padding:5px;">绑定主机数：<%=vpcBaseInfoVO.getHostAmount() %></td>
				</tr> 
				<tr>
					<td style="padding:5px;">每月资费：<%=vpcBaseInfoVO.getMonthlyPrice()==null?"无":vpcBaseInfoVO.getMonthlyPrice() %></td>
					<td style="padding:5px;">创建时间：<%=vpcBaseInfoVO.getCraeteTimeFormat() %></td>
				</tr>
				<tr>
					<td colspan="2" style="padding:5px;">描述：<%=vpcBaseInfoVO.getDescription() %></td>
				</tr>
				<!-- </tr>
				<tr>
					<td style="padding:5px;">磁盘读总流量：0</td>
					<td style="padding:5px;">磁盘写总流量：0</td>
				</tr>
				<tr>
					<td style="padding:5px;">网络读总流量：100G</td>
					<td style="padding:5px;">网络写总流量：100G</td>
				</tr>
				<tr>
					<td colspan="99"><hr /></td>
				</tr> -->
				
			</table>
			
		</form>
	</div>

</div>


<script type="text/javascript">


//-----------------------------

//var si = null;

var _vpc_detail_dlg_scope_ = new function(){
	
	var self = this;
	
	self.si = null;
	
	self.myOnClose = function(){
		window.clearInterval(self.si);
	    $("#vpc_detail_dlg").dialog('destroy');
		return true; 
	};
	
	self.onMove = function(){
		var thisId = "#vpc_detail_dlg";
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
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(vpc_detail_dlg_form);
		ajax.remoteCall("bean://cloudHostService:addCloudHost", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#vpc_detail_dlg_container").parent().prop("_data_");
					$("#vpc_detail_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#vpc_detail_dlg").dialog("close");
	};
	
	//--------------------------
	$(document).ready(function() {
		
		// “从镜像创建”、“空白系统”的选择切换
		$(':radio[name=check1]').change(function() {
			var checkval = $(this).val();
			$('#disk1').prop('disabled', checkval == '1');
			if(checkval == 1) {
				$('#disk2').slider("enable");
			}else{
				$('#disk2').slider("disable");
			}
		});
		// 是否使用数据磁盘
		$(':checkbox[name=check2]').change(function() {
			var checkval = $(this).prop("checked");
			if(checkval == true) {
				$('#disk3').slider("enable");
			}else{
				$('#disk3').slider("disable");
			}
		});
		// 保存
		$("#vpc_detail_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#vpc_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



