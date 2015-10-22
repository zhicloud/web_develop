<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@ page import="com.zhicloud.op.vo.SmsConfigVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
    SmsConfigVO smsConfigVO = (SmsConfigVO) request.getAttribute("smsConfigVO");

%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="sms_config_mod_dlg_container">
	<div id="sms_config_mod_dlg" class="easyui-dialog" title="修改配置"
			style="
				width:480px;
				height:300px;
				padding:10px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#sms_config_mod_dlg_buttons',
				modal: true,
				onMove:_sms_config_mod_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _sms_config_mod_dlg_scope_;
				}
			">
		<form id="sms_config_mod_dlg_form" method="post">
			<input type="hidden" name="id" value="<%=smsConfigVO.getId()%>"/>
		<table border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />配置名：</td>
                <td class="inputcont"><input class="textbox inputtext" type="text" id="config_name" name="config_name" old="<%=smsConfigVO.getConfigName()%>" value="<%=smsConfigVO.getConfigName()%>" onblur="checkName()"/></td>
                <td class="inputtip" id="config-tip-name"><i>请输入配置名</i></td>
            </tr>
            <tr>
                <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />短信配置ID：</td>
                <td class="inputcont"><input class="textbox inputtext" type="text" id="sms_id" name="sms_id" value="<%=smsConfigVO.getSmsId()%>" onblur="checkSmsId()"/></td>
                <td class="inputtip" id="config-tip-sms-id"><i>请输入短信配置ID</i></td>
            </tr>
            <tr>
                <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />服务端地址：</td>
                <td class="inputcont"><input class="textbox inputtext" type="text" id="service_url" name="service_url" value="<%=smsConfigVO.getServiceUrl()%>" onblur="checkServiceUrl()"/></td>
                <td class="inputtip" id="config-tip-service-url"><i>请输入服务端地址</i></td>
            </tr>
            <tr>
                <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />发送人账号：</td>
                <td class="inputcont"><input class="textbox inputtext" type="text" id="name" name="name" value="<%=smsConfigVO.getName()%>" onblur="checkSender()"/></td>
                <td class="inputtip" id="config-tip-sender"><i>请输入发送人账号</i></td>
            </tr>
            <tr>
                <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />短信密码：</td>
                <td class="inputcont"><input class="textbox inputtext" type="password" id="password" name="password" value="<%=smsConfigVO.getPassword()%>" onblur="checkPassword()"/></td>
                <td class="inputtip" id="config-tip-password"><i>请输入短信密码</i></td>
            </tr>
		</table>
		</form>
	</div>
	
	<div id="sms_config_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sms_config_mod_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="sms_config_mod_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

	var path = "<%=request.getContextPath()%>";

    function checkName(){
        var config_name = new String($("#config_name").val()).trim();
        var old = new String($("#config_name").attr("old")).trim();
        var flag = true;
        if(config_name == null || config_name == ""){
            $("#config-tip-name").html("<b>配置名不能为空</b>");
            return false;
        }
        if(config_name.length<2 || config_name.length>16){
            $("#config-tip-name").html("<b>配置名长度为2-16个字符</b>");
            return false;
        }

        if(old != config_name) {
            ajax.remoteCall("bean://smsConfigService:checkConfigName",
                    [ config_name ],
                    function(reply) {
                        if( reply.result.status == "fail" ) {
                            flag = false;
                        }
                    }
            );
        }

        if(flag == false) {
            $("#config-tip-name").html("<b>该配置名已存在</b>");
            return false;
        }

        $("#config-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
        return true;
    }
    function checkSmsId(){
        var sms_id = new String($("#sms_id").val()).trim();
        if(sms_id == null || sms_id == ""){
            $("#config-tip-sms-id").html("<b>短信配置ID不能为空</b>");
            return false;
        }
        if(sms_id.length<2 || sms_id.length>16){
            $("#config-tip-sms-id").html("<b>短信配置ID长度为2-16个字符</b>");
            return false;
        }
        $("#config-tip-sms-id").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
        return true;
    }

    function checkServiceUrl(){
        var service_url = new String($("#service_url").val()).trim();
        if(service_url == null || service_url == ""){
            $("#config-tip-service-url").html("<b>服务端地址不能为空</b>");
            return false;
        }
        if(service_url.length<2 || service_url.length>80){
            $("#config-tip-service-url").html("<b>服务端地址长度为2-80个字符</b>");
            return false;
        }
        $("#config-tip-service-url").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
        return true;
    }

    function checkSender(){
        var name = new String($("#name").val()).trim();
        if(name == null || name == ""){
            $("#config-tip-sender").html("<b>发送人账号不能为空</b>");
            return false;
        }
        if(name.length<2 || name.length>16){
            $("#config-tip-sender").html("<b>发送人账号长度为2-16个字符</b>");
            return false;
        }
        $("#config-tip-sender").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
        return true;
    }


    function checkPassword(){
        var password = new String($("#password").val()).trim();
        if(password == null || password == ""){
            $("#config-tip-password").html("<b>密码不能为空</b>");
            return false;
        }
        if(password.length<2 || password.length>50){
            $("#config-tip-password").html("<b>密码长度为2-50个字符</b>");
            return false;
        }
        $("#config-tip-password").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
        return true;
    }

//================================
var _sms_config_mod_dlg_scope_ = {};		// 作用域
_sms_config_mod_dlg_scope_.onMove = function(){
	var thisId = "#sms_config_mod_dlg";
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
_sms_config_mod_dlg_scope_.save = function()
{
	var formData = $.formToBean(sms_config_mod_dlg_form);
	ajax.remoteCall("bean://smsConfigService:updateConfigById",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if(reply.result.status=="fail"){
				$("#config-tip-name").html("<b>"+reply.result.message+"</b>");
			}
			else if( reply.result.status=="success" )
			{
				var data = $("#sms_config_mod_dlg_container").parent().prop("_data_");
				$("#sms_config_mod_dlg").dialog("close");
				data.onClose(reply.result);
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
};

// 关闭
_sms_config_mod_dlg_scope_.close = function()
{
	$("#sms_config_mod_dlg").dialog("close");
};

//--------------------------

// 保存
$("#sms_config_mod_dlg_save_btn").click(function(){
	if(checkName() && checkSender() && checkSmsId() && checkPassword() && checkServiceUrl()){
		_sms_config_mod_dlg_scope_.save();
	}
});

// 关闭
$("#sms_config_mod_dlg_close_btn").click(function(){
	_sms_config_mod_dlg_scope_.close();
});

</script>
