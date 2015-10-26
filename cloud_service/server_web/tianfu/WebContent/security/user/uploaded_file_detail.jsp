<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.MyFileVO"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	MyFileVO myFile = (MyFileVO)request.getAttribute("myFile");
%>

<!-- uploaded_file_detail.jsp -->

<div id="upload_file_detial_dlg_container">
	<div id="upload_file_detial_dlg" class="easyui-dialog" title="绑定云终端"
		style="width:700px; height:250px; padding:10px;"
		data-options="
			buttons: '#upload_file_detial_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _upload_file_detial_dlg_scope_;
			}
		">
			<form id="upload_file_detial_dlg_form" method="post">
		
			<table border="0" style="height: auto; margin: auto;">
			
				<tr>
					<td>文件名：&nbsp;&nbsp;&nbsp;<input id="#" name="#"  value ="<%=myFile.getFileName() %>" readonly="readonly"  disabled="disabled" maxlength="100" style="width:500px" /></td>
				</tr>
				
				<tr>
					<td>文件大小：<input id="file_size" name="#" value ="" readonly="readonly"  disabled="disabled"  maxlength="100" style="width:500px"/></td>
				</tr>	
				
				<tr>	
					<td>上传时间：<input id="#" name="#" value ="<%=myFile.getUploadTime() %>"  disabled="disabled"  readonly="readonly" maxlength="100" style="width:500px"/></td>
				</tr>
				
				<tr>	
					<td>下载地址：<input id="#" name="#" value ="<%=AppProperties.getValue("address_of_this_system", "")%>fileDownload.do?fileId=<%=myFile.getId() %>"  readonly="readonly"  disabled="disabled"  maxlength="100" style="width:500px"/></td>
				</tr>
				
			</table>
		</form>
	</div>

	<div id="upload_file_detial_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="upload_file_detial_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>


<script type="text/javascript">

var _upload_file_detial_dlg_scope_ = new function(){
	
		var self = this;
	
		var size = "<%=myFile.getSize() %>";
		
		size = CapacityUtil.toCapacityLabel(size,0);
		document.getElementById("file_size").setAttribute("value",size);
	
	// 关闭
	self.close = function() {
		$("#upload_file_detial_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
			
		// 关闭
		$("#upload_file_detial_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



