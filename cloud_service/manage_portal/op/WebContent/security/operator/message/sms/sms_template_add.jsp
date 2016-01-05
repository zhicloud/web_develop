<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@ page import="com.zhicloud.op.vo.SmsConfigVO" %>
<%@ page import="java.util.List" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_OPERATOR;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
    List<SmsConfigVO> smsConfigList =  (List<SmsConfigVO>)request.getAttribute("smsConfigList");
%>

<!DOCTYPE html>
<!-- sms_template_add.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 短信模版管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/editor/themes/default/default.css" />
	<style type="text/css">
		/************/
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header,
		.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
	</style>
</head>
<body>
<div id="sms_template_add_dlg_container">
<form id="sms_template_add_dlg_form" method="post">

	<div class="panel-header">
		<div class="panel-title">新增模版</div>
		<div class="panel-tool"></div>
	</div>

	<table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td style="vertical-align:middle; text-align:right;">
                <img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />
                短信服务器：</td>
            <td style="padding:5px;"><select id="config_id" name="config_id" onblur="checkConfig()" style="width:150px;height:26px;font-family:'微软雅黑';vertical-align:middle;">
                <option value="">请选择</option>
                <%
                    for( SmsConfigVO smsConfigVO : smsConfigList)
                    {
                %>
                    <option value="<%=smsConfigVO.getId()%>"><%=smsConfigVO.getConfigName()%></option>
                <%
                    }
                %>
            </select>
            </td>
            <td><span id="template-tip-config"></span></td>
        </tr>
		<tr>
            <td style="vertical-align:middle; text-align:right;">
                <img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />
                模版名：</td>
            <td style="padding:5px;">
                <input type="text" id="name" name="name" style="width:300px;" onblur="checkName()"/>
            </td>
            <td><span id="template-tip-name"></span></td>
		</tr>
        <tr>
            <td style="vertical-align:middle; text-align:right;">
                <img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />
                标识码：</td>
            <td style="padding:5px;">
                <input type="text" id="code" name="code" style="width:300px;" onblur="checkCode()"/>
                (用于程序接口调用，请勿轻易修改，例如：DB_WARN表示数据库告警)
            </td>
            <td><span id="template-tip-code"></span></td>
        </tr>
        <tr>
            <td style="vertical-align:middle; text-align:right;">
                收件人：</td>
            <td style="padding:5px;">
                <input type="text" id="recipient" name="recipient" style="width:300px;" onblur="checkRecipient()"/>
                (多个收件人用分号隔开)
            </td>
            <td><span id="template-tip-recipient"></span></td>
        </tr>
		<tr>
            <td style="vertical-align:middle; text-align:right;">
                <img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />
                短信正文：</td>
            <td style="padding:5px;">
			    <textarea id="content" name="content" style="width:650px;height:200px;" onclick="checkContent()"></textarea>
            </td>
            <td><span id="template-tip-content"></span></td>
		</tr>
        <tr>
            <td></td>
            <td align="right" style="padding:5px;">
                <a href="javascript:" class="easyui-linkbutton" id="sms_template_add_dlg_save_btn">
                    &nbsp;保&nbsp;存&nbsp;
                </a>
                &nbsp;&nbsp;
                <a href="javascript:" class="easyui-linkbutton" id="sms_template_add_dlg_close_btn">
                    &nbsp;关&nbsp;闭&nbsp;
                </a>
            </td>
        </tr>
	</table>

</form>
	</div>

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/editor/kindeditor-min.js" type="text/javascript"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/editor/zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">

    window.name = "selfWin";

    var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
    ajax.async = false;

	//=========================

    function checkConfig(){
        var config_id = new String($("#config_id").val()).trim();
        if(config_id == null || config_id == ""){
            $("#template-tip-config").html("<font color='red'>请选择发送短信服务器</font>");
            return false;
        }
        $("#template-tip-config").html("");
        return true;
    }

    function checkCode(){
        var code = new String($("#code").val()).trim();
        var flag = true;
        if(code == null || code == ""){
            $("#template-tip-code").html("<font color='red'>标识码不能为空</font>");
            return false;
        }
        if(code.length<2 || code.length>25){
            $("#template-tip-code").html("<font color='red'>标识码长度为2-25个字符</font>");
            return false;
        }

        ajax.remoteCall("bean://smsTemplateService:checkTemplateCode",
                [ name ],
                function(reply) {
                    if( reply.result.status == "fail" ) {
                        flag = false;
                    }
                }
        );

        if(flag == false) {
            $("#template-tip-code").html("<font color='red'>该标识码已存在</font>");
            return false;
        }

        $("#template-tip-code").html("");
        return true;
    }

    function checkName(){
        var name = new String($("#name").val()).trim();
        var flag = true;
        if(name == null || name == ""){
            $("#template-tip-name").html("<font color='red'>模版名不能为空</font>");
            return false;
        }
        if(name.length<2 || name.length>16){
            $("#template-tip-name").html("<font color='red'>模版名长度为2-16个字符</font>");
            return false;
        }

        ajax.remoteCall("bean://smsTemplateService:checkTemplateName",
                [ name ],
                function(reply) {
                    if( reply.result.status == "fail" ) {
                        flag = false;
                    }
                }
        );

        if(flag == false) {
            $("#template-tip-name").html("<font color='red'>该模版名已存在</font>");
            return false;
        }

        $("#template-tip-name").html("");
        return true;
    }

    function checkRecipient(){
        var recipient = new String($("#recipient").val()).trim();
        if(recipient.length<2 || recipient.length>150){
            $("#template-tip-recipient").html("<font color='red'>手机号长度为2-150个字符</font>");
            return false;
        }
        if(recipient != null && recipient != ""){
        	if(!(/^1[3|4|5|8][0-9]\d{8,8}(;1[3|4|5|8][0-9]\d{8,8})*$/.test(recipient))){
                $("#template-tip-recipient").html("<font color='red'>请输入正确的手机号码</font>");
                return false;
            }
        }
        $("#template-tip-recipient").html("");
        return true;
    }

    function checkContent(){
        var content = new String($("#content").val());
        if(content == null || content == ""){
            $("#template-tip-content").html("<font color='red'>模版内容不能为空</font>");
            return false;
        }
        $("#template-tip-content").html("");
        return true;
    }

    var _sms_template_add_dlg_scope_ = {};		// 作用域
    
    // 保存
    _sms_template_add_dlg_scope_.save = function()
    {
        var formData = $.formToBean(sms_template_add_dlg_form);
        var content = new String($("#content").val());
        content.replace(new RegExp('(["\"])', 'g'),"\\\"");
        formData.content = content.replace(new RegExp('(["\n\r"])', 'g'),'');
        ajax.remoteCall("bean://smsTemplateService:addTemplate",
                [formData],
                function(reply)
                {
                    if( reply.status=="exception" )
                    {
                        top.$.messager.alert('警告',reply.exceptionMessage,'warning');
                    }
                    else if(reply.result.status=="fail"){
                        $("#template-tip-name").html("<b>"+reply.result.message+"</b>");
                    }
                    else if( reply.result.status=="success" )
                    {
                        window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsTemplateService&method=managePage";
                    }
                    else
                    {
                        top.$.messager.alert('警告',reply.result.message,'warning');
                    }
                }
        );
    };

    // 关闭
    _sms_template_add_dlg_scope_.close = function()
    {
        window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsTemplateService&method=managePage";
    };
    
    // 保存
    $("#sms_template_add_dlg_save_btn").click(function(){
        if(checkConfig() && checkName() && checkCode() && checkRecipient() && checkContent()){
            _sms_template_add_dlg_scope_.save();
        }
    });

    // 关闭
    $("#sms_template_add_dlg_close_btn").click(function(){
        _sms_template_add_dlg_scope_.close();
    });

</script>
</html>
