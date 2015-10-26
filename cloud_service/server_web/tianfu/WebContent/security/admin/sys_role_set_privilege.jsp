<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String roleId = (String)request.getParameter("roleId");
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
%>

<div id="sys_role_set_privilege_dlg_container">
	<div id="sys_role_set_privilege_dlg" class="easyui-dialog" title="设置权限" 
		style="
			width:600px; 
			height:400px; 
			padding:10px;
		"
		data-options="
			iconCls: 'icon-edit',
			buttons: '#sys_role_set_privilege_dlg_buttons',
			modal: true,
			onMove:_sys_role_set_privilege_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _sys_role_set_privilege_dlg_scope_;
			}
		">
		<form id="sys_role_set_privilege_dlg_form" method="post">
			<ul id="privilege_tree" class="easyui-tree" data-options="
				url: '<%=request.getContextPath() %>/bean/ajax.do?userType=<%=userType%>&bean=sysPrivilegeService&method=getPrivilegeTree&roleId=<%=roleId%>', 
				method: 'get',
				lines: true,
				checkbox: true
			">
			</ul>
		</form>
	</div>
	
	<div id="sys_role_set_privilege_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sys_role_set_privilege_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="sys_role_set_privilege_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>

<script type="text/javascript">

var _sys_role_set_privilege_dlg_scope_ = {};		// 作用域
_sys_role_set_privilege_dlg_scope_.onMove = function(){
	var thisId = "#sys_role_set_privilege_dlg";
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
_sys_role_set_privilege_dlg_scope_.save = function()
{
	var roleId = "<%=roleId%>";
	var nodes = $('#privilege_tree').tree('getChecked');
	
	var privilegeIdArr = [];
	for( var i=0; i<nodes.length; i++ )
	{
		privilegeIdArr.push(nodes[i].id);
	}
	
	ajax.remoteCall("bean://sysPrivilegeService:saveSysRolePrivilege",
		[roleId, privilegeIdArr],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				top.$.messager.alert("提示","权限设置成功","info",function(){
						var data = $("#sys_role_set_privilege_dlg_container").parent().prop("_data_");
						$("#sys_role_set_privilege_dlg").dialog("close");
						data.onClose(reply.result);
				});
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
};

// 关闭
_sys_role_set_privilege_dlg_scope_.close = function()
{
	$("#sys_role_set_privilege_dlg").dialog("close");
};

//--------------------------

// 保存
$("#sys_role_set_privilege_dlg_save_btn").click(function(){
	_sys_role_set_privilege_dlg_scope_.save();
});

// 关闭
$("#sys_role_set_privilege_dlg_close_btn").click(function(){
	_sys_role_set_privilege_dlg_scope_.close();
});

</script>



