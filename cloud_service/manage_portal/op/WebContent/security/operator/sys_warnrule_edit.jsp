﻿﻿<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.SysWarnRuleVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	SysWarnRuleVO rule = (SysWarnRuleVO) request.getAttribute("rulevo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警规则管理 - 修改配置</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 
		<style type="text/css">
		.messager-input {
		  width: 150px;
		  padding: 2px 2px 3px 2px;
		  height:16px;
		  border: 1px solid #95B8E7;
		  margin:2px;
		}
		</style>
</head>
<body>
<div id="agent_add_dlg_container">
	<div id="agent_add_dlg" class="easyui-dialog" title="修改预警配置"
		style="width:600px; height:460px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#agent_add_dlg_buttons',
			modal: true,
			onMove:_agent_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _agent_add_dlg_scope_;
			}
		">
		<form id="agent_add_dlg_form" method="post">
		<input type="hidden" id="type" name="type" value="mod"/>
		<input type="hidden" id="ruleid" name="ruleid" value="<%=rule.getId()%>"/>
  <table border="0" cellpadding="0" cellspacing="0">
  	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />规则名称：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="name" name="name" onblur="checkName()" value="<%=StringUtil.trim(rule.getName())%>"/></td>
      <td class="inputtip" id="operator-tip-name"> <i>请输入2-16个字符的规则名称</i></td>
    </tr> 
  	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />规则标示符：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="code" name="code" onblur="checkCode()" value="<%=StringUtil.trim(rule.getCode())%>"/></td>
      <td class="inputtip" id="operator-tip-code"> <i>唯一标示,请不要轻易更改</i></td>
    </tr>       
  	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />规则类型：</td>
      <td class="inputcont" style="padding-left:2px;"><select class="easyui-combobox"  id="ruletype" name="ruletype" panelHeight="100px" style="width:154px;">
          <option value="0" <% if(rule.getRuletype()==0){ %>selected<%} %>>告警</option>
          <option value="1" <% if(rule.getRuletype()==1){ %>selected<%} %>>故障</option>
        </select></td>
        <td class="inputtip" id="operator-tip-ruletype"><i>请选择规则类型</i></td>
    </tr>   
  	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />采样频率：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="frequency" name="frequency" onblur="checkFrequency()" value="<%=StringUtil.trim(rule.getFrequency())%>"/></td>
      <td class="inputtip" id="operator-tip-frequency"> <i>每隔多少时间采样一次,单位是秒</i></td>
    </tr>    
  	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />采样次数：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="sampletime" name="sampletime" onblur="checkSampletime()" value="<%=StringUtil.trim(rule.getSampletime())%>"/></td>
      <td class="inputtip" id="operator-tip-sampletime"> <i>需要采样多少次</i></td>
    </tr>             
  	<tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />是否发送通知：</td>
		      <td class="inputcont" style="padding-left:2px;"><select class="easyui-combobox"  id="isnotify" name="isnotify" panelHeight="100px" style="width:154px;">
		          <option value="1" <% if(rule.getIsnotify()==1){ %>selected<%} %>>是</option>
		          <option value="0" <% if(rule.getIsnotify()==0){ %>selected<%} %>>否</option>
		        </select></td>
		        <td class="inputtip" id="operator-tip-isnotify"><i>请选择是否发送通知</i></td>
	</tr>
  	<tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />通知类型：</td>
		      <td class="inputcont" style="padding-left:2px;"><select class="easyui-combobox"  id="realtime" name="realtime" panelHeight="100px" style="width:154px;">
		          <option value="1" <% if(rule.getRealtime()==1){ %>selected<%} %>>定时</option>
		          <option value="2" <% if(rule.getRealtime()==2){ %>selected<%} %>>实时</option>
		        </select></td>
		        <td class="inputtip" id="operator-tip-realtime"><i>请选择通知类型</i></td>
	</tr>
    <tr id="sendtimetr">
    <% if(rule.getRealtime()==1){ %>
	    <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />定时发送时间：</td>
		<td class="inputcont">
		<input class="messager-input" type="text" id="sendtime" name="sendtime" onblur="checkSendtime()" value="<%=StringUtil.trim(rule.getSendtime())%>"/></td>
		<td class="inputtip" id="operator-tip-sendtime"><i>请输入定时发送时间</i></td>
	<%} %>	
    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />通知电话：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="notify_phone" name="notify_phone" onblur="checkPhone()" value="<%=StringUtil.trim(rule.getNotify_phone())%>"/></td>
      <td class="inputtip" id="operator-tip-phone"><i>请输入11位手机号码</i></td>
    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />通知邮件：</td>
      <td class="inputcont"><input class="messager-input" type="text" id="notify_email" name="notify_email" onblur="checkEmail()" value="<%=StringUtil.trim(rule.getNotify_email())%>"/></td>
      <td class="inputtip" id="operator-tip-email"><i>请输入正确的邮箱</i></td>
    </tr>
  </table>
</form>
	</div>

	<div id="agent_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="agent_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> <a
			href="javascript:" class="easyui-linkbutton"
			id="agent_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

</body>
<script type="text/javascript">
//==================check begin==================
function checkIsnotify(){ 
	var isnotify = $("#isnotify").combobox("getValue");
	if(isnotify == null || isnotify == ""){
		$("#operator-tip-isnotify").html("<b>请选择是否发送通知</b>");
		return false
	}
	$("#operator-tip-isnotify").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkRuletype(){
	var ruletype = $("#ruletype").combobox("getValue")
	if(ruletype==null || ruletype==""){
		$("#operator-tip-ruletype").html("<b>请选择规则类型</b>");
		 return false;
	}
	$("#operator-tip-ruletype").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkRealtime(){
	var realtime = $("#realtime").combobox("getValue")
	if(realtime==null || realtime==""){
		$("#operator-tip-realtime").html("<b>请选择通知类型</b>");
		 return false;
	}
	
	$("#operator-tip-realtime").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkName(){
	var myName = new String($("#name").val()).trim();
	if(myName.length<2 || myName.length>16){
		$("#operator-tip-name").html("<b>请输入2-16个字符的规则名称</b>");
		return false;  
	}
	$("#operator-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkCode(){
	var myCode = new String($("#code").val()).trim();
	if(myCode == null || myCode == ""){
		$("#operator-tip-code").html("<b>请输入规则编码</b>");
		return false
	}
	if(myCode.length>20){
		$("#operator-tip-code").html("<b>请输入20个字符以内的规则编码</b>");
		return false;  
	}
	$("#operator-tip-code").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkFrequency(){
	var frequency = new String($("#frequency").val()).trim();
	if(frequency == null || frequency == ""){
		$("#operator-tip-frequency").html("<b>请输入采样频率</b>");
		return false
	}	
	var reg = /^[1-9][0-9]{1,10}$/;
	if(!reg.test(frequency)){
		$("#operator-tip-frequency").html("<b>请输入10位以内数字</b>");
		return false;  
	}
	$("#operator-tip-frequency").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}

function checkSampletime(){
	var sampletime = new String($("#sampletime").val()).trim();
	if(sampletime == null || sampletime == ""){
		$("#operator-tip-sampletime").html("<b>请输入采样次数</b>");
		return false
	}	
	var reg = /^[1-9]{1}([0-9]{1})?$/;
	if(!reg.test(sampletime)){
		$("#operator-tip-sampletime").html("<b>请输入1-100以内数字</b>");
		return false;  
	}
	$("#operator-tip-sampletime").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkSendtime(){
	var realtime = $("#realtime").combobox("getValue")
	var sendtime = new String($("#sendtime").val()).trim();
	if(realtime==1){
	if(sendtime==null||sendtime==""){
	    $("#operator-tip-sendtime").html("<b>请输入定时发送时间</b>");		
		return false;
	}
	if(!/^[0-9]{2}:[0-9]{2}:[0-9]{2}$/.test(sendtime)){
		$("#operator-tip-sendtime").html("<b>请输入【24:00:00】格式的定时发送时间</b>");
		return false;
	}
    $("#operator-tip-sendtime").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	}
    return true;
}
function checkPhone(){
	var notify_phone = new String($("#notify_phone").val());
	if(notify_phone==null || notify_phone==""){
	    $("#operator-tip-phone").html("<b>手机号码不能为空</b>");		
	    return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(notify_phone))){ 
	    $("#operator-tip-phone").html("<b>请输入正确的手机号码</b>");		
		return false;
	}
    $("#operator-tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");		
	return true;
}

function checkEmail(){
	var notify_email = new String($("#notify_email").val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(notify_email==null || notify_email==""){
		$("#operator-tip-email").html("<b>邮箱不能为空</b>");
		return false;
	}
	if(!myreg.test(notify_email)){
	    $("#operator-tip-email").html("<b>请输入正确的邮箱</b>");		
		return false;
	}
	if(notify_email.length>30){
		$("#operator-tip-email").html("<b>输入不能超过30个字符</b>");
		return false;
	}
    $("#operator-tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");		
	return true;
}


//====================check end================
var _agent_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#agent_add_dlg";
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
		if(checkName()&&checkCode()&&checkRuletype()&&checkFrequency()&&checkSampletime()&&checkIsnotify()&&checkRealtime()&&checkSendtime()&&checkPhone()&&checkEmail()){
		var formData = $.formToBean(agent_add_dlg_form);
		ajax.remoteCall("bean://sysWarnService:SaveRuleInfo", 
			[ formData ],
			function(reply) {
			if (reply.status == "exception") {
				if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
				{
					top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
						window.location.reload();
					});
				}else{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
			} else if(reply.result.status == "success"){
					top.$.messager.alert('提示',reply.result.message,'保存成功',function(){
						var data = $("#agent_add_dlg_container").parent().prop("_data_");
						$("#agent_add_dlg").dialog("close");
						data.onClose(reply.result);
					});
				}else{
					top.$.messager.alert('警告',reply.result.message,'保存失败');
				}
			}
		);
		}
	};
	
	// 关闭
	self.close = function() {
		$("#agent_add_dlg").dialog("close");
	};
	
	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		var sendtimehtml = "<td class=\"inputtitle\"><img src=\"<%=request.getContextPath() %>/images/button_required_red_16_16.gif\" width=\"12\" height=\"12\" alt=\"必填\" />定时发送时间：</td>"+
		      "<td class=\"inputcont\">"+
		"<input class=\"messager-input\" type=\"text\" id=\"sendtime\" name=\"sendtime\" onblur=\"checkSendtime()\"/></td>"+
		      "<td class=\"inputtip\" id=\"operator-tip-sendtime\"><i>请输入定时发送时间</i></td>";

		// 保存
		$("#agent_add_dlg_save_btn").click(function() {
				self.save();
		});
		// 关闭
		$("#agent_add_dlg_close_btn").click(function() {
			self.close();
		});
		$("#isnotify").combobox({ 
			onChange: function (newValue,oldValue) { 
				if(newValue!=undefined&&newValue!=""){
					$("#operator-tip-isnotify").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
				}else{
					$("#operator-tip-isnotify").html("<b>请选择是否发送通知</b>");
				}
			} 

		});
		$("#ruletype").combobox({ 
			onChange: function (newValue,oldValue) { 
				if(newValue!=undefined&&newValue!=""){
					$("#operator-tip-ruletype").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
				}else{
					$("#operator-tip-ruletype").html("<b>请选择规则类型</b>");
				}
			} 

		});
		$("#realtime").combobox({ 
			onChange: function (newValue,oldValue) { 
				if(newValue!=undefined&&newValue!=""){
					$("#operator-tip-realtime").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
					if(newValue==1){
						$("#sendtimetr").append(sendtimehtml);
					}
					else{
						$("#sendtimetr").html("");
					}
				}else{
					$("#sendtimetr").html("");
					$("#operator-tip-realtime").html("<b>请选择通知类型</b>");
				}
			} 

		});
	});
};

</script>
</html>



