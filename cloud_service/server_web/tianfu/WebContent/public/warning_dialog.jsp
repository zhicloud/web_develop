<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
String message = StringUtil.trim(request.getAttribute("message"));
String notexsit = StringUtil.trim(request.getAttribute("notexsit"));
%>
<script type="text/javascript">
if('<%=notexsit%>'=='yes'){
		top.$.messager.alert('提示','账户不存在，请联系管理员！','info',function(){ 
			parent.logout();
		});
	 
}else{
	top.$.messager.alert('警告','<%=StringUtil.escape(message)%>','warning'); 
}
</script>

