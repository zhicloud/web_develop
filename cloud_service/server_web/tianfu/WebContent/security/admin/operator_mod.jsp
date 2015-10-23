<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.OperatorVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
OperatorVO operator = (OperatorVO) request.getAttribute("operator");
List<SysGroupVO> sysGroupList = (List<SysGroupVO>)request.getAttribute("sysGroupList");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="operator_mod_dlg_container">

	<div id="operator_mod_dlg" class="easyui-dialog" title="修改运营商员工信息"
		style="width:450px; height:320px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#operator_mod_dlg_buttons',
			modal: true,
			onMove:_operator_mod_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _operator_mod_dlg_scope_;
			}
		">
		<form id="operator_mod_dlg_form" method="post">
		<input type="hidden" id="operator_id" name="operator_id"
				value="<%=operator.getId()%>" />
  		<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />权限组：</td>
		      <td class="inputcont"><select class="inputselect" id="group_id" name="group_id" onblur="checkGroupId()" style="width:150px;height:26px;font-family:'微软雅黑';vertical-align:middle;">
		          <option value="">请选择</option>
		          	<%
						for( SysGroupVO sysGroup : sysGroupList)
						{
					%>
					<option value="<%=sysGroup.getId()%>"><%=sysGroup.getGroupName()%></option>
					<%
						}
					%>
		        </select></td>
		        <td class="inputtip" id="admin-tip-group"><i>请选择一个权限组</i></td>
	    	</tr>
<!-- 		    <tr> -->
<%-- 		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />用户名：</td> --%>
<%-- 		      <td class="inputcont"><input class="textbox inputtext" type="text" id="account" name="account" onblur="checkAccount()" value="<%=operator.getAccount()%>"/></td> --%>
<!-- 		      <td class="inputtip" id="admin-tip-account"><i>请输入2-16个字符的用户名</i></td> -->
<!-- 		    </tr> -->
		   <%--  <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />姓名：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="name" name="name" onblur="checkName()" value="<%=operator.getName()%>"/></td>
		      <td class="inputtip" id="admin-tip-name"><i>请输入2-16个字符的姓名</i></td>
		    </tr> --%>
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="email" name="email" onblur="checkEmail()" value="<%=operator.getEmail()%>"/></td>
		      <td class="inputtip" id="admin-tip-email"><i>请输入有效邮箱</i></td>
		    </tr>
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />手机：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="phone" name="phone" onblur="checkPhone()" value="<%=operator.getPhone()%>"/></td>
		      <td class="inputtip" id="admin-tip-phone"><i>请输入11位手机号码</i></td>
		    </tr>
		    <tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />状态：</td>
				<td><select class="inputselect" id="status" name="status">
						<option value="1">正常</option>
						<option value="2">禁用</option>
						<option value="3">结束</option>
				</select></td>
				<td class="inputtip" id="admin-tip-status"></td>
			</tr>
		  </table>
		</form>
	</div>

	<div id="operator_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="operator_mod_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> 
		<a href="javascript:" class="easyui-linkbutton" id="operator_mod_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>
<script type="text/javascript">
//==================check begin==================
function checkGroupId(){
	var groupId = new String($("#group_id").val()).trim();
	if(groupId == null || groupId == ""){
		$("#admin-tip-group").html("<b>请选择一个权限组</b>");
		return false;
	}
	$("#admin-tip-group").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkAccount(){
	var account = new String($("#account").val()).trim();
	if(account==null || account==""){
		$("#admin-tip-account").html("<b>用户名不能为空</b>");
		return false;
	}
	if(account.length<2 || account.length>16){
		$("#admin-tip-account").html("<b>用户名长度为2-16个字符</b>");
		return false;
	}
	$("#admin-tip-account").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkName(){
	var myName = new String($("#name").val()).trim();
	if(myName==null || myName==""){
		$("#admin-tip-name").html("<b>姓名不能为空</b>");
		return false;
	}
	if(myName.length<2 || myName.length>16){
		$("#admin-tip-name").html("<b>姓名长度为2-16个字符</b>");
		return false;
	}
	$("#admin-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkEmail(){
	var email = new String($("#email").val());
	if(email==null || email==""){
		$("#admin-tip-email").html("<b>邮箱不能为空</b>");
		return false;
	}
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(!reg.test(email)){
		$("#admin-tip-email").html("<b>请输入正确的邮箱</b>");
		return false;
	}
	$("#admin-tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkPhone(){
	var phone = new String($("#phone").val());
	if(phone==null || phone==""){
		$("#admin-tip-phone").html("<b>手机号码不能为空</b>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#admin-tip-phone").html("<b>请输入正确的手机号码</b>");
		return false;
	}
	$("#admin-tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

//====================check end================
var _operator_mod_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#operator_mod_dlg";
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
	this.save = function() {
		var formData = $.formToBean(operator_mod_dlg_form);
		ajax.remoteCall("bean://operatorService:updateOperatorById",
			[ formData ], 
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					var data = $("#operator_mod_dlg_container").parent().prop("_data_");
					$("#operator_mod_dlg").dialog("close");
					data.onClose(reply.result);
				}
				else
				{
					$("#admin-tip-email").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
	};
	
	// 关闭
	this.close = function() {
		$("#operator_mod_dlg").dialog("close");
	};
	
	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#operator_mod_dlg_save_btn").click(function(){
			if(checkGroupId()& checkEmail()& checkPhone()){
				self.save();
			}
		});
		// 关闭
		$("#operator_mod_dlg_close_btn").click(function(){
			self.close();
		});
		
		//
		$("#group_id").val("<%=operator.getGroupId()%>");
		$("#status").val("<%=operator.getStatus()%>");
	});
		
};


</script>




