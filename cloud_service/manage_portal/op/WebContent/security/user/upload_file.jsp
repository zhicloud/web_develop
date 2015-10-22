<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.app.helper.AppHelper"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import = "java.math.BigInteger "%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String userId = (String)request.getAttribute("user_id");
	BigInteger size = (BigInteger)request.getSession().getAttribute("size");	
	BigInteger totalSize = (BigInteger)request.getSession().getAttribute("total_size");	
%>

<div id="upload_file_dlg_container" >
	<div id="upload_file_dlg" class="easyui-dialog" title="上传文件"
		style="width:450px; height:250px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#upload_file_dlg_buttons',
			modal: true,
			onClose: function(){
				location.reload(false);
			},
			onDestroy: function(){
				delete _upload_file_dlg_scope_;
			}
		">
		<form  id = "upload_file_form"  method = "POST" enctype="multipart/form-data" >
			<table border="0" style="height: auto; margin: auto;">
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">选择文件：</td>
					<td>
						<div>
							<input type="file"  id=attach name="attach" width="auto"/>
						</div>
					</td>
				</tr>
			</table>
			<div id="upload_file_dlg_buttons">
			<a href="javascript:void(0);" class="easyui-linkbutton" id="upload_file_dlg_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
			</div>
		
		</form>
	</div>
</div>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/uploadify/uploadify.css" ></link>  

<script type="text/javascript" src="<%=request.getContextPath()%>/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript">


var _upload_file_dlg_scope_ = new function(){
	
	var self = this;
	
	
	// 关闭
	self.close = function() {
		$("#upload_file_dlg").dialog("close");
	};
	//--------------------------
	
	
	
	$(function() {
		
		 $("#attach").uploadify({ 
				swf: "<%=request.getContextPath()%>/uploadify/uploadify.swf",
	 			uploader: "<%=request.getContextPath()%>/user/uploadFile.do?userId=<%=userId %>",
	 		 	buttonImage : '<%=request.getContextPath()%>/uploadify/browse.png',
	 			width: 100,
	 			high: 80,
	 			fileSizeLimit: '<%=AppProperties.getValue("upload_file_upper_limit", "")%>MB',
	 			method: 'POST',
	 			buttonText : '选择文件',
	 			fileObjName: 'attach',
	 			multi: false,
	 			cancelImg: "<%=request.getContextPath()%>/uploadify/uploadify-cancel.png",
	 			preventCaching  : true,
				queueSizeLimit: 1,
	 			uploadLimit: 1,
	 			
	 			onSelect : function(file) {
	 				
	 				var fileName = file.name;
					var userId = '<%=userId%>';
	 				
	 				ajax.remoteCall("bean://cloudUserService:getMyFileByUserIdAndFileName",
							[ fileName = fileName, userId = userId ], 
							function(reply) {
								if(reply.result.status == "success") {
									top.$.messager.alert('警告', '该文件已存在！','warring',function(){
									});
									$('#attach').uploadify('cancel');
									return false; 
								}else{
									if(file.size+size > totalSize){
										top.$.messager.alert('警告', '超出最大使用空间了！','warring',function(){
										});
										$('#attach').uploadify('cancel');
										return false; 
									}
								}
							}
						); 
	 	        },
	 			
	 			//选择文件后异常处理
	 			onSelectError: function(file,errorCode,errorMsg) {
	 				switch (errorCode) {
	 		           case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED: errorMsg = "本系统暂不支持多文件上传";
	 						break;
	 		          case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT: errorMsg = "本系统支持的最大上传文件大小为 " + this.settings.fileSizeLimit;
		 		        	 break;
	 		          case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE: errorMsg = "本系统支持的最大上传文件大小为 " + this.settings.fileSizeLimit;
	 		                break;
	 				}
	 				top.$.messager.alert('警告', errorMsg,'warring');
	 				window.alert = function(str) {
	 					 return ;
	 					}
	 			},
	 			
	 			onUploadError : function(file, errorCode, errorMsg, errorString) {
	 				//手动取消不弹出错误提示
	 				if (errorCode == SWFUpload.UPLOAD_ERROR.FILE_CANCELLED  
	 		                || errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {  
	 		            return;  
	 		        }  
	 				top.$.messager.alert('警告', '上传失败，请稍候再试','warring',function(){
		        		 var data = $("#upload_file_dlg_container").parent().prop("_data_");
		        		 $("#upload_file_dlg").dialog("close");
		        		 data.onClose();
 					 });
	 	        },
	         
	 		//上传成功后的方法
	         onUploadSuccess : function(file, data, response) {
		        	 top.$.messager.alert('提示', '上传成功','info',function(){
		        		 var data = $("#upload_file_dlg_container").parent().prop("_data_");
		        		 $("#upload_file_dlg").dialog("close");
 		        		 data.onClose();
 				 });
		         }
	  });
	});
	
	$(document).ready(function(){
		
		
		// 关闭
		$("#upload_file_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};

</script>



