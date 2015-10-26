<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	JSONObject json = JSONObject.fromObject(request.getAttribute("objectdata"));
%>

<div id="vpc_detail_dlg_container">
	<%
		if("server".equals(json.get("type"))){
	%>
	<div id="vpc_detail_dlg" class="easyui-dialog" title="服务器详情"
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
	<%
		} else {
	%>
	<div id="vpc_detail_dlg" class="easyui-dialog" title="云主机详情"
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
	<%
		}
	%>
<form id="vpc_detail_dlg_form" method="post">
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
				<tr>
					<td style="padding:5px;" rowspan="4"><img height="80" width="70" src="<%=request.getContextPath()%>/images/computer.png"> </td>
					<td style="padding:5px;">名称：<%=json.get("name") %></td>
					<td style="padding:5px;">状态：<%=json.get("status") %></td>
				</tr>
				<tr>
					<td style="padding:5px;">CPU：<%=json.get("cpu_count") %></td>
					<td style="padding:5px;">CPU利用率：<%=json.get("cpu_usage") %></td>
				</tr>
				<tr>
					<td style="padding:5px;">内存：<%=json.get("memory") %></td>
					<td style="padding:5px;">内存利用率：<%=json.get("memory_usage") %></td>
				</tr> 
				<tr>
					<td style="padding:5px;">磁盘：<%=json.get("disk") %></td>
					<td style="padding:5px;">磁盘利用率：<%=json.get("disk_usage") %></td>
				</tr>
			</table>
			
		</form>		
	</div>
</div>
<script type="text/javascript">


//-----------------------------

//var si = null;
var name = '${objectdata.name }';
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
	
	function showstatus(obj){
		var str = "";
		if(obj=="0"){
			str = "正常";
		}
		if(obj=="1"){
			str = "告警";
		}
		
	}
	
	//--------------------------
	$(document).ready(function() {
		// 关闭
		$("#vpc_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



