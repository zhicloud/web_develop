﻿﻿<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.SysWarnValueVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String ruleid = (String)request.getAttribute("ruleid");
	List<SysWarnValueVO> values = (List<SysWarnValueVO>)request.getAttribute("warnvalues");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- sys_warnvalue_edit.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警规则管理 - 内容编辑</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 
<style type="text/css">
table {
    *border-collapse: collapse; /* IE7 and lower */
    border-spacing: 0;
    width: 100%;    
}

.bordered {
    border: solid #ccc 1px;
    -moz-border-radius: 6px;
    -webkit-border-radius: 6px;
    border-radius: 6px;
    -webkit-box-shadow: 0 1px 1px #ccc; 
    -moz-box-shadow: 0 1px 1px #ccc; 
    box-shadow: 0 1px 1px #ccc;         
}

.bordered tr:hover {
    background: #fbf8e9;
    -o-transition: all 0.1s ease-in-out;
    -webkit-transition: all 0.1s ease-in-out;
    -moz-transition: all 0.1s ease-in-out;
    -ms-transition: all 0.1s ease-in-out;
    transition: all 0.1s ease-in-out;     
}    
    
.bordered td, .bordered th {
    border-left: 1px solid #ccc;
    border-top: 1px solid #ccc;
    padding: 0px;
    text-align: center;    
}

.bordered th {
    background-color: #dce9f9;
    background-image: -webkit-gradient(linear, left top, left bottom, from(#ebf3fc), to(#dce9f9));
    background-image: -webkit-linear-gradient(top, #ebf3fc, #dce9f9);
    background-image:    -moz-linear-gradient(top, #ebf3fc, #dce9f9);
    background-image:     -ms-linear-gradient(top, #ebf3fc, #dce9f9);
    background-image:      -o-linear-gradient(top, #ebf3fc, #dce9f9);
    background-image:         linear-gradient(top, #ebf3fc, #dce9f9);
    -webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; 
    -moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;  
    box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;        
    border-top: none;
    text-shadow: 0 1px 0 rgba(255,255,255,.5); 
    padding: 10px;
    font-size: 12px;
    font-family: "宋体";
}

.bordered td:first-child, .bordered th:first-child {
    border-left: none;
}

.bordered th:first-child {
    -moz-border-radius: 6px 0 0 0;
    -webkit-border-radius: 6px 0 0 0;
    border-radius: 6px 0 0 0;
}

.bordered th:last-child {
    -moz-border-radius: 0 6px 0 0;
    -webkit-border-radius: 0 6px 0 0;
    border-radius: 0 6px 0 0;
}

.bordered th:only-child{
    -moz-border-radius: 6px 6px 0 0;
    -webkit-border-radius: 6px 6px 0 0;
    border-radius: 6px 6px 0 0;
}

.bordered tr:last-child td:first-child {
    -moz-border-radius: 0 0 0 6px;
    -webkit-border-radius: 0 0 0 6px;
    border-radius: 0 0 0 6px;
}

.bordered tr:last-child td:last-child {
    -moz-border-radius: 0 0 6px 0;
    -webkit-border-radius: 0 0 6px 0;
    border-radius: 0 0 6px 0;
}
.messager-input {
  width: 90%;
  padding: 2px 2px 3px 2px;
  height:90%;
  border-style: none;
  margin:2px;
}
.combo{
border-style:none;
}
</style>
</head>
<body>
<div id="agent_add_dlg_container">
	<div id="agent_add_dlg" class="easyui-dialog" title="编辑预警规则"
		style="width:600px; height:400px; padding:10px;"
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
  <table class="bordered" id="bordered_table">
  <tr>
	  <th>名称</th>
	  <th>编码</th>
	  <th>操作符</th>
	  <th>阀值</th>
	  <th>连接符</th>
  </tr>
  <% if(values!=null&&values.size()>0){
     for(int i=0;i<values.size();i++){
         SysWarnValueVO valuevo = values.get(i);
     %>
	<tr><td><input class="messager-input" type="text" id="name<%=(i+1)%>" name="name" value="<%=valuevo.getName()%>"/></td>
	 <td><input class="messager-input" type="text" id="code<%=(i+1)%>" name="code" value="<%=valuevo.getCode()%>"/></td>
	 <td><select class="easyui-combobox"  id="operator<%=(i+1)%>" name="operator">
			          <option value="equal" <% if("equal".equals(valuevo.getOperator())) {%> selected <%} %>>等于</option>
			          <option value="high" <% if("high".equals(valuevo.getOperator())) {%> selected <%} %>>大于</option>
			          <option value="low" <% if("low".equals(valuevo.getOperator())) {%> selected <%} %>>小于</option>
			          <option value="between" <% if("between".equals(valuevo.getOperator())) {%> selected <%} %>>在...之间</option>
			        </select></td>
	  <td><input class="messager-input" type="text" id="value<%=(i+1)%>" name="value" value="<%=valuevo.getValue()%>"/></td>
	  <td><select class="easyui-combobox"  id="contact<%=(i+1)%>" name="contact" style="width:100px;">
	  				  <option value="" <% if("".equals(valuevo.getContact())) {%> selected <%} %>>空</option>
	  				  <option value="and" <% if("and".equals(valuevo.getContact())) {%> selected <%} %>>并且</option>
			          <option value="or" <% if("or".equals(valuevo.getContact())) {%> selected <%} %>>或者</option>
			        </select></td>
	  </tr>
      
  <%} }%>    
  </table>
</form>
	</div>

	<div id="agent_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="agent_add_dlg_add_btn"> &nbsp;增&nbsp;加&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="agent_add_dlg_del_btn"> &nbsp;删&nbsp;除&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="agent_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="agent_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

</body>
<script type="text/javascript">
//==================check begin==================
function checkTable(){ 
	var returnflag = true;
	var floatreg = /^[1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2}$/;
	var wordreg  = /^[a-zA-Z]+(_)?[a-zA-Z]*$/;
	var betweenreg = /^([1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2})-([1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2})$/;
	var len = $("#bordered_table tr").length;
	for(var i=1;i<len;i++){
		var name = $("#name"+i).val();
		var code = $("#code"+i).val();
		var value = $("#value"+i).val();
		var operator = $("#operator"+i).combobox("getValue");
		var contact = $("#contact"+i).combobox("getValue");
		if(name.length>50){
			top.$.messager.alert("提示", "第 "+i+" 行名称长度超出定义的50个字符", "warning");
			returnflag = false;
			break;
		}
		if(code.length>50){
			top.$.messager.alert("提示", "第 "+i+" 行编码长度超出定义的50个字符", "warning");
			returnflag = false;
			break;
		}
		if(!wordreg.test(code)){
			top.$.messager.alert("提示", "第 "+i+" 行编码格式不正确", "warning");
			returnflag = false;
			break;
		}
		if(value.leng>20){
			top.$.messager.alert("提示", "第 "+i+" 行值长度超出定义的20个字符", "warning");
			returnflag = false;
			break;
		}
		if(operator=="between"){
			if(!betweenreg.test(value)){
				top.$.messager.alert("提示", "第 "+i+" 行阀值格式不正确,请录入[0.00-0.00]的格式", "warning");
				returnflag = false;
				break;
			}else{
				if(parseFloat(value.split("-")[1])<parseFloat(value.split("-")[0])){
					top.$.messager.alert("提示", "第 "+i+" 行阀值的值后面的值不能小于前面的值", "warning");
					returnflag = false;
					break;
				}
			}
		}else{
			if(!floatreg.test(value)){
				top.$.messager.alert("提示", "第 "+i+" 行阀值格式不正确", "warning");
				returnflag = false;
				break;
			}
		}
		if((i!=len-1)&&contact==""){
			top.$.messager.alert("提示", "第 "+i+" 行连接符不能为空", "warning");
			returnflag = false;
			break;
		}
	}
	
	return returnflag;
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
		if(checkTable()){
		var formData = new Array();
		var len = $("#bordered_table tr").length;
		for(var i=1;i<len;i++){
			var object = new Object();
			object["name"] = $("#name"+i).val();
			object["code"] = $("#code"+i).val();
			object["operator"] = $("#operator"+i).combobox("getValue");	
			object["value"] = $("#value"+i).val();
			object["contact"] = $("#contact"+i).combobox("getValue");
			object["sort"] = i;
			formData.push(object);
		}
		var ruleid = "<%=ruleid%>";
		ajax.remoteCall("bean://sysWarnService:SaveValueInfo", 
			[ formData, ruleid],
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
				} else if (reply.result.status == "success") {
					top.$.messager.alert("提示",reply.result.message , "success", function(){
						var data = $("#agent_add_dlg_container").parent().prop("_data_");
						$("#agent_add_dlg").dialog("close");
						data.onClose(reply.result);
					});
				} else {
					top.$.messager.alert("错误",reply.result.message , "error");
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
		// 增加
		$("#agent_add_dlg_add_btn").click(function() {
			var len = $("#bordered_table tr").length;
				var trhtml = "<tr><td><input class=\"messager-input\" type=\"text\" id=\"name"+len+"\" name=\"name\"\/><\/td>"+
				 "<td><input class=\"messager-input\" type=\"text\" id=\"code"+len+"\" name=\"code\"\/><\/td>"+
				 "<td><select class=\"easyui-combobox\"  id=\"operator"+len+"\" name=\"operator\">"+
						          "<option value=\"equal\">等于<\/option>"+
						          "<option value=\"high\">大于<\/option>"+
						          "<option value=\"low\">小于<\/option>"+
						          "<option value=\"between\">在...之间<\/option>"+
						        "<\/select><\/td>"+
				  "<td><input class=\"messager-input\" type=\"text\" id=\"value"+len+"\" name=\"value\"/></td>"+
				  "<td><select class=\"easyui-combobox\"  id=\"contact"+len+"\" name=\"contact\" style=\"width:100px;\">"+
				  				  "<option value=\"\">空<\/option>"+
				  				  "<option value=\"and\">并且<\/option>"+
						          "<option value=\"or\">或者<\/option>"+
						        "<\/select></\td>"+
				  "<\/tr>";
			$("#bordered_table").append(trhtml);
			$.parser.parse("#bordered_table");
				  
		});
		// 删除
		$("#agent_add_dlg_del_btn").click(function() {
			var len = $("#bordered_table tr").length;
			if(len>1){
				$("#bordered_table").find("tr:last").remove();
			}
		});		
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