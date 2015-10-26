<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.SysUserVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo  = LoginHelper.getLoginInfo(request);

SysDiskImageVO sysDiskImage = (SysDiskImageVO) request.getAttribute("sysDiskImage");
List<CloudHostVO> cloudHostList = (List<CloudHostVO>)request.getAttribute("cloudHostList");
List<SysUserVO> userList = (List<SysUserVO>)request.getAttribute("userList");
Integer userType = Integer.valueOf(request.getParameter("userType"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的云端 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

</head>
<body>
<div id="sysDiskImage_mod_dlg_container">

	<div id="sysDiskImage_mod_dlg" class="easyui-dialog" title="修改系统镜像"
		style="width: 700px; height: 600px; padding: 10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#sysDiskImage_mod_dlg_buttons',
			modal: true,
			onMove:_sysDiskImage_mod_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _sysDiskImage_mod_dlg_scope_;
			}
		">
		
		<form id="sysDiskImage_mod_dlg_form" method="post">

			<input type="hidden" id="sysDiskImage_id" name="sysDiskImage_id" value="<%=sysDiskImage.getId()%>" />
            <input type="hidden" id="region" name="region" value="<%=sysDiskImage.getRegion()%>" />
            <input type="hidden" name="agentIds" id="agentIds" value=""/>
            <table border="0" style="margin:0 auto 0 auto;">
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />镜像名称：</td>
					<td class="inputcont">
						<input  type="text" value="<%=sysDiskImage.getName()%>"  id="name" name="name" class="textbox inputtext" onblur="checkName()"/>
					 
					</td>
					<td class="inputtip" id="operator-tip-name"><i>请输入2-50个字符的镜像名称</i></td>
					
				</tr>
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />显示名称：</td>
					<td class="inputcont">
						<input  type="text" id="display_name" value="<%=sysDiskImage.getDisplayName()%>"  name="display_name" class="textbox inputtext" onblur="checkDisplayName()"/>
					 
					</td>
					<td class="inputtip" id="operator-tip-display_name"><i>请输入2-50个字符的显示名称</i></td>
					
				</tr>
				<tr>
					<td class="inputtitle" style="width:100px">来自云主机：</td>
					<td class="inputcont">
						<select id="from_host_id" name="from_host_id" class="inputselect" onblur="checkImage()">
							<option value="">请选择</option>
							<%
								for( CloudHostVO cloudHost : cloudHostList )
								{
							%>
							<option value="<%=cloudHost.getId()%>"><%=cloudHost.getHostName()%></option>
							<%
								}
							%>
						</select> 
					<td class="inputtip" id="operator-tip-image"><i>请选择一个云主机</i></td>
					</td>
				</tr>
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />序号：</td>
					<td class="inputcont">
						<input type="text" value="<%=sysDiskImage.getSort()%>" id="sort" name="sort"  onblur="checkSort();" class="textbox inputtext"/>
					</td>
					<td class="inputtip" id="operator-tip-sort"><i>请输入长度不超过11位的整数</i></td>
				</tr>
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />标签：</td>
					<td class="inputcont">
						<label for="tag1_windows" style="padding:0 10px 0 0;">
							<input type="radio" id="tag1_windows" name="tag1" value="Windows" checked="checked" />Windows
						</label>
						<label for="tag1_linux" style="padding:0 10px 0 0;">
							<input type="radio" id="tag1_linux" name="tag1" value="Linux" />Linux
						</label>
						<label for="tag1_unix" style="padding:0 10px 0 0;">
							<input type="radio" id="tag1_unix" name="tag1" value="Unix" />Unix
						</label>
					</td>
					<td class="inputtip" id="operator-tip-tag1"></td>
				</tr>
				<tr>
					<td class="inputtitle"></td>
					<td class="inputcont">
						<label for="tag2_32bit" style="padding:0 10px 0 0;">
							<input type="radio" id="tag2_32bit" name="tag2" value="32bit" checked="checked" />32bit
						</label>
						<label for="tag2_64bit" style="padding:0 10px 0 0;">
							<input type="radio" id="tag2_64bit" name="tag2" value="64bit" />64bit
						</label>
					</td>
					<td class="inputtip" id="operator-tip-tag2"></td>
				</tr>
				<tr>
					<td class="inputtitle">描述：</td>
					<td class="inputcont">
						<textarea name="description" id="description" onblur="checkDescription();" rows="6" cols="30" style="resize:none;"><%=sysDiskImage.getDescription()%></textarea>
                    </td>
					<td class="inputtip" id="operator-tip-description"> <i>最多输入100个字符</i></td>
				</tr>
				
				<tr>
					<td class="inputtitle" style="width:100px"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />镜像类型：</td>
					<td class="inputcont">
						<select id="type" name="type" class="inputselect" onchange="changeImageType();">  
							<option value="1">通用镜像</option> 
							<option value="2">代理商定制镜像</option> 
						</select> 
					<td class="inputtip" id="operator-tip-type"><i>请选择一个镜像类型</i></td> 
				</tr>
				<tr id="getAgent"  >
				   <td colspan="3">
				   <table border="0" style="margin:20px auto 0 auto;">
				    <tr>
					<td>
						<%-- 左边的表格 { --%>
						<table id="left_datagrid" class="easyui-datagrid" title="可选"  
							style="
								width:250px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysUserService&method=querySysUserNotGetImage',
								queryParams: {
									imageId: '<%=sysDiskImage.getId() %>'
								},
								toolbar: '#left_datagrid_toolbar',
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true,
								pagination: true,
								pageList: [10, 20, 50, 100, 200],
								pageSize: 10
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'type'" formatter="_sys_group_set_member_dlg_scope_.formatUserType">类型</th>
					                <th data-options="field:'account'">账号</th>
					            </tr>
					        </thead>
					    </table>
					    
					    <div id="left_datagrid_toolbar" style="padding:3px; text-align:right;"> 
							<span style="position: relative; top: 2px;">账号：</span> 
							<input type="text" id="left_account" style="width:60px;position:relative; top:2px; height:18px;" /> 
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="left_query_btn">查询</a>
						</div>
						<%-- 左边的表格 } --%>
					</td>
					
					<%-- 中间的按钮部份 { --%>
					<td style="padding:0 5px 0 5px;">
						<a href="#" class="easyui-linkbutton" id="sys_group_set_member_dlg_select_btn"> 选择 >> </a>
						<div style="height:5px;"></div>
						<a href="#" class="easyui-linkbutton" id="sys_group_set_member_dlg_cancel_btn"> << 取消 </a>
					</td>
					<%-- 中间的按钮部份 } --%>
					
					<td>
						<%-- 右边的按钮部份 { --%>
						<table id="right_datagrid" class="easyui-datagrid" title="已选"  
							style="
								width:250px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								toolbar: '#right_datagrid_toolbar',
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'type'" formatter="_sys_group_set_member_dlg_scope_.formatUserType">类型</th>
					                <th data-options="field:'account'">账号</th>
					            </tr>
					        </thead>
					        <tbody> 
					           <%
					              if(userList!=null&&userList.size()>0){
					            	  
										for( SysUserVO sysUser : userList )
										{
								%>
								<tr>
					                <td><%=sysUser.getId()%></td>
					                <td><%=sysUser.getType()%></td>
					                <td><%=sysUser.getAccount()%></td>
					            </tr>
								<%
					              		}
								}
								%>
					        </tbody>
					    </table>
					    
					    <div id="right_datagrid_toolbar" style="height:30px; text-align:right;">
						</div>
						<%-- 右边的按钮部份 } --%>
					</td>
				   
				 </tr>
				</table>
				   </td>
				
				</tr>
				
			</table>
			 
		</form>
	</div>

	<div id="sysDiskImage_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sysDiskImage_mod_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="sysDiskImage_mod_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
	
</div>

<script type="text/javascript">
function checkName(){
	var myName = new String($("#name").val());
	if(myName==null || myName==""){
		$("#operator-tip-name").html("<b>镜像名称不能为空</b>");
		return false;
	}
	if(/[\w._-]{2,50}/.test(myName)){
		$("#operator-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	
		return true;
	}else{
		$("#operator-tip-name").html("<b>镜像名称由字母、数字和下划线组成,长度为2-50字符</b>");
		 
		return false;
		
	}
}
function checkDisplayName(){
	var name = new String($("#display_name").val());
	if(name==null || name==""){
		$("#operator-tip-display_name").html("<b>镜像名称不能为空</b>");
		return false;
	}
	if(name.length>=2&&name.length<=50){
		$("#operator-tip-display_name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	
		return true;
	}else{
		$("#operator-tip-display_name").html("<b>显示名称长度为2-50字符</b>");
		 
		return false;
		
	}
}
// function checkImage(){
// 	var myImage = new String($("#from_host_id").val());
// 	if(myImage==null || myImage==""){
// 		$("#operator-tip-image").html("<b>请选择一个云主机</b>");
		 
// 		return false;
// 	}
<%-- 	$("#operator-tip-image").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />"); --%>
	 
// 	return true;
// }
function checkDescription(){
	var description = new String($("#description").val()); 
	if(description.length>100){
		$("#operator-tip-description").html("<b>最多输入100个字符</b>");
		 
		return false;
	}
	$("#operator-tip-description").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");

	return true;
}
function checkSort(){
	var sort = new String($("#sort").val()); 
	if(sort.length==0){
		$("#operator-tip-sort").html("<b>序号不能为空</b>");
		 
		return false;
		
	}
	if(/^[1-9]*[1-9][0-9]*$/.test(sort)){
		if(sort.length>11){
			$("#operator-tip-sort").html("<b>最大长度为11位</b>");
			 
			return false;
			
		}
		$("#operator-tip-sort").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	
		return true;
	}else{
		$("#operator-tip-sort").html("<b>请输入整数</b>");
		 
		return false;
		
	}
}
function changeImageType(){
	var type = new String($("#type").val());
	if(type=="1"){
 //      $("#getAgent").css("display","none");
       $("#getAgent").hide();
	}else{
    //   $("#getAgent").css("display","block");
       $("#getAgent").show();
       if('<%=sysDiskImage.getType()%>'=='1'){   	   
	       $("#left_query_btn").click(); 
	       var $rightDatagrid = $("#right_datagrid");
			var rightRows = $rightDatagrid.datagrid("getRows");
			if( rightRows.length==0 )
			{
				return ;
			}		 
			// 删除左边的
			for( var i=rightRows.length-1; i>=0; i-- )
			{
				var row = rightRows[i];
				var rowIndex = $rightDatagrid.datagrid("getRowIndex", row);
				$rightDatagrid.datagrid("deleteRow", rowIndex);
			}
       }
		
	}	 
	return true;
}

//---------------------------------------------
var _sysDiskImage_mod_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#sysDiskImage_mod_dlg";
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
	// 保存
	self.save = function() {
		var selectedUserIds = $("#right_datagrid").datagrid("getRows").joinProperty("id");
		$("#agentIds").val(selectedUserIds);
		var formData = $.formToBean(sysDiskImage_mod_dlg_form);
		ajax.remoteCall("bean://sysDiskImageService:updateSysDiskImageById",
			[ formData ], 
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if (reply.result.status == "success")
				{
					var data = $("#sysDiskImage_mod_dlg_container").parent().prop("_data_");
					$("#sysDiskImage_mod_dlg").dialog("close");
					data.onClose(reply.result);
				} 
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 初始化
	$(document).ready(function(){
		// 关闭
		self.close = function(){
			$("#sysDiskImage_mod_dlg").dialog("close");
		};
		// 保存
		$("#sysDiskImage_mod_dlg_save_btn").click(function(){
			if(checkName() && checkDescription() && checkSort()){
				self.save();
			}
		});
		// 关闭
		$("#sysDiskImage_mod_dlg_close_btn").click(function(){
			self.close();
		});
		// 设置“来自云主机”
		$("#from_host_id").val("<%=sysDiskImage.getFromHostId()%>");
		$("#type").val(<%=sysDiskImage.getType()%>);
		if('<%=sysDiskImage.getType()%>'=='1'){
			 $("#getAgent").hide();
		}
		// 设置标签
		var tag = "<%=sysDiskImage.getTag()%>";
		var tagArr = tag.split(",");
		if( tagArr.length>0 )
		{
			$.setRadioGroupValue("tag1", tagArr[0]);
		}
		if( tagArr.length>1 )
		{
			$.setRadioGroupValue("tag2", tagArr[1]);
		}
		
	});
	
	
}; // 作用域

var _sys_group_set_member_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#sys_group_set_member_dlg";
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
	this.formatUserType = function(val)
	{
		if( val==1 ) {  
		    return "超级管理员";  
		} else if( val==2 ) {
		    return "运营商";  
		} else if( val==3 ) {
		 	return "代理商";
		} else if( val==4 ) {
		 	return "终端用户";
		} else {
			return "其它";
		}
	};
	
	// 选择
	this.selectLeftToRight = function()
	{
		var $leftDatagrid = $("#left_datagrid");
		var leftRows = $leftDatagrid.datagrid("getSelections");
		if( leftRows.length==0 )
		{
			return ;
		}
		// 添加到右边
		var rightRows = $("#right_datagrid").datagrid("getRows");
		var newRows = rightRows.concat(leftRows);
		$("#right_datagrid").datagrid("loadData", newRows);
		
		// 删除左边的
		for( var i=leftRows.length-1; i>=0; i-- )
		{
			var row = leftRows[i];
			var rowIndex = $leftDatagrid.datagrid("getRowIndex", row);
			$leftDatagrid.datagrid("deleteRow", rowIndex);
		}
	};
	
	// 取消
	self.cancelRightToLeft = function()
	{
		var $rightDatagrid = $("#right_datagrid");
		var rightRows = $rightDatagrid.datagrid("getSelections");
		if( rightRows.length==0 )
		{
			return ;
		}
		// 添加到右边
		var leftRows = $("#left_datagrid").datagrid("getRows");
		var newRows = leftRows.concat(rightRows);
		$("#left_datagrid").datagrid("loadData", newRows);
		
		// 删除左边的
		for( var i=rightRows.length-1; i>=0; i-- )
		{
			var row = rightRows[i];
			var rowIndex = $rightDatagrid.datagrid("getRowIndex", row);
			$rightDatagrid.datagrid("deleteRow", rowIndex);
		}
	};
	
	// 保存
	this.save = function()
	{
		var groupId = "";
		var selectedUserIds = $("#right_datagrid").datagrid("getRows").joinProperty("id");
		ajax.remoteCall("bean://sysGroupService:setGroupMembers",
			[groupId, selectedUserIds],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					var data = $("#sys_group_set_member_dlg_container").parent().prop("_data_");
					$("#sys_group_set_member_dlg").dialog("close");
					data.onClose(reply.result);
					top.$.messager.alert("返回结果", reply.result.message);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
	
	// 关闭
	this.close = function()
	{
		$("#sys_group_set_member_dlg").dialog("close");
	};
	
	// 初始化
	$(document).ready(function(){
		// 查询
		$("#left_query_btn").click(function(){
			var queryParams = {}; 
			var selectedUserIds = $("#right_datagrid").datagrid("getRows").joinProperty("id");
			$("#agentIds").val(selectedUserIds);
			queryParams.agentIds =  $("#agentIds").val(); 
			queryParams.imageId = '<%=sysDiskImage.getId()%>';
			
			$('#left_datagrid').datagrid({
				"queryParams": queryParams
			}); 
		});
		// 选择
		$("#sys_group_set_member_dlg_select_btn").click(function(){
			self.selectLeftToRight();
		});
		// 取消
		$("#sys_group_set_member_dlg_cancel_btn").click(function(){
			self.cancelRightToLeft();
		});
		// 保存
		$("#sys_group_set_member_dlg_save_btn").click(function(){
			self.save();
		});
		// 关闭
		$("#sys_group_set_member_dlg_close_btn").click(function(){
			self.close();
		}); 
	});
};

	
</script>




