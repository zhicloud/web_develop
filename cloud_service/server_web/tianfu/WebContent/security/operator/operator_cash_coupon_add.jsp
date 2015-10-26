<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="invite_code_add_dlg_container">
	<div id="invite_code_add_dlg" class="easyui-dialog" title="添加现金券" 
			style="
				width:500px; 
				height:400px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-add',
				buttons: '#invite_code_add_dlg_buttons',
				modal: true,
				onMove:_invite_code_add_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _invite_code_add_dlg_scope_;
				}
			">
		<form id="invite_code_add_dlg_form" method="post">
			<table border="0" cellpadding="0" cellspacing="0">
			    <tr>
			      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />代金券张数：</td>
			      <td class="inputcont"><input class="textbox inputtext" type="text" maxlength="3" id="code_number" name="code_number" onblur="checkNumber()"/></td>
			    </tr>
			    <tr>
			    	<td></td>
			        <td class="inputtip" id="tip-code"><i>请输入需要创建的现金券张数,一次最多添加999张现金券</i></td>
			    </tr>
			    <tr>
			      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />价值(元)：</td>
			      <td class="inputcont"><input class="textbox inputtext" type="text" id="cash_value" name="cash_value" onblur="checkCash()"/></td>
			    </tr>
			    <tr>
			    	<td></td>
			        <td class="inputtip" id="tip-cash"><i>请输入每张现金券的价值</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="invite_code_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="invite_code_add_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="invite_code_add_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">
function checkNumber(){
	var InviteCode = new String($("#code_number").val()).trim();
	if(InviteCode==null||InviteCode==""){
		$("#tip-code").html("<b>请注意填写现金券的张数</b>");
		return false;
	}
	if(!(/^[1-9][0-9]*$/.test(InviteCode))){
		$("#tip-code").html("<b>现金券的张数必须是正整数</b>");
		return false;
	}
	if(InviteCode.length>3){
		$("#tip-code").html("<b>一次最多添加999张现金券券</b>");
		return false;
	}
	$("#tip-code").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>正确");
	return true;
}
function checkCash(){
	var cashValue = new String($("#cash_value").val()).trim();
	var cash_upper = "<%=AppProperties.getValue("cash_upper_limit", "")%>";
	if(cashValue==null||cashValue==""||cashValue==0||cashValue==0.0||cashValue==0.00){
		$("#tip-cash").html("<b>请注意填写每张现金券的价值</b>");
		return false;
	}
	if(!(/^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/.test(cashValue))){
		$("#tip-cash").html("<b>现金券价值可以最高精确到两位小数</b>");
		return false;
	} 
// 	if(!(/^[1-9][0-9]*$/.test(cashValue))){
// 		$("#tip-cash").html("<b>每张现金券价值必须是正整数</b>");
// 		return false;
// 	} 
	if((cashValue-cash_upper)>0){
		$("#tip-cash").html("<b>每张现金券价值不能超过"+cash_upper+"元</b>");
		return false;
	}
	$("#tip-cash").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>正确");
	return true;
}
//=============================
var _invite_code_add_dlg_scope_ = {};		// 作用域
_invite_code_add_dlg_scope_.onMove = function(){
	var thisId = "#invite_code_add_dlg";
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
_invite_code_add_dlg_scope_.save = function()
{
	var formData = $.formToBean(invite_code_add_dlg_form);
	ajax.remoteCall("bean://cashCouponService:addCashCoupon",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
					top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
						top.location.reload();
					});
				}
				else{
					top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
						top.location.reload();
					});
				} 
			}
			else if( reply.result.status=="success" )
			{
				var data = $("#invite_code_add_dlg_container").parent().prop("_data_");
				$("#invite_code_add_dlg").dialog("close");
				data.onClose(reply.result);
				top.$.messager.alert('提示','添加成功','info')
			}
			else
			{
				$("#tip-cash").html("<b>"+reply.result.message+"</b>");
			}
		}
	);
};

// 关闭
_invite_code_add_dlg_scope_.close = function()
{
	$("#invite_code_add_dlg").dialog("close");
};

//--------------------------

// 保存
$("#invite_code_add_dlg_save_btn").click(function(){
	if(checkNumber()&&checkCash()){
		_invite_code_add_dlg_scope_.save();
	}
});

// 关闭
$("#invite_code_add_dlg_close_btn").click(function(){
	_invite_code_add_dlg_scope_.close();
});
$(function(){
    $("#code_number,cash_value").bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	_invite_code_add_dlg_scope_.save();
        }
    });
}); 

</script>



