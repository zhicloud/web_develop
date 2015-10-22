<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.SuggestionVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@ page import="java.util.*"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<SuggestionVO> list  = (List<SuggestionVO>)request.getAttribute("suggestionList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><%=AppConstant.PAGE_TITLE %></title>
	<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
	<style type="text/css">.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber {text-align: center;}</style>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/dep/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
	<!--[if IE 6]>
	<script src="javascript/DD_belatedPNG.js"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix("*");
	</script>
	<![endif]-->
</head>

<body>
<div class="page">
  <div class="pageleft">
  <jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
     <div class="main">
     <div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
      <div class="titlebar"><div class="l">客服：<b>小蘑菇</b></div>
        <div class="r" style="font-size:12px; color:#999">意见与建议如经采纳我们将赠送价值20-100元不等的现金券一张</div>
      </div>
      <div class="box" style="height:auto;  padding:10px 0 10px 0;" id="box">
      <%
       if(list!=null&&list.size()>0){
    	   for(SuggestionVO vo:list){
    		   
    	%>
        <div class="usertalk">
          <div class="talkinfo r">
            <div class="avatar r"><img src="<%=request.getContextPath()%>/image/user_avatar.png" width="60" height="60" alt=" " /></div>
            <div class="triangle r">&nbsp;</div>
            <div class="talktime r"><%=StringUtil.formatDateString(vo.getSubmitTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd HH:mm:ss") %></div>
          </div>
          <div class="r" style="width:450px">
            <div class="ballnoon r"><%=vo.getContent() %></div>
          </div>
          <div class="clear"></div>
        </div>
        <%
        if(vo.getResult()!=null){ 
        %>
        <div class="servicetalk">
          <div class="talkinfo l">
            <div class="avatar l"><img src="<%=request.getContextPath()%>/image/service_avatar.png" width="60" height="60" alt=" " /></div>
            <div class="triangle l">&nbsp;</div>
            <div class="talktime l"><%=StringUtil.formatDateString(vo.getReplyTime(), "yyyyMMddHHmmssSSSS", "yyyy-MM-dd HH:mm:ss") %></div>
          </div>
          <div class="l" style="width:450px">
            <div class="ballnoon l"><%=vo.getResult() %></div>
          </div>
          <div class="clear"></div>
        </div>
    	<%	
         }
    	   }
       }
      %>
         
      </div>
      <form id="suggestion_dlg_form" name="suggestion_dlg_form" method="post">
      <div class="talktip">
      <textarea id="inputtalk" cols="" rows="" onfocus="inputfocus('talk');" onblur="inputblur('talk');" class="talkbox" name="talk"></textarea>
        <label id="talklabel" for="inputtalk" class="talklabel">点击输入意见</label>
      </div>
       </form>
      <div class="box" style="padding:15px 0 0 0"><a href="javascript:void(0);" class="bluebutton r" id="send">发　送</a></div>
      <div class="clear">&nbsp;</div>
    </div> 
    
    <jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>
  </div>
  <%-- <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div> --%>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
$(document).ready(function(){
	navHighlight("umc","umfb");
	$("#send").click(function(){
		toSuggestion();
	});
	$("#inputtalk").keypress(function(evt){
		if( evt.keyCode==13 ){
			toSuggestion();
		}
	});
});

function toSuggestion(){
	if($("#inputtalk").val().length==0){
		top.$.messager.alert('警告','请输入意见','warning');
		return;
	}
	if($("#inputtalk").val().length<500){
		var str = $("#inputtalk").val().replace('/[\r\n]/g','');
		$("#inputtalk").val(str);
		var formData = $.formToBean(suggestion_dlg_form);
		ajax.remoteCall("bean://suggestionService:addSuggestion", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							window.location.reload();
						});
					}
					else {
						
						top.$.messager.alert('警告',"提交失败",'warning');
					}
				}
				else if (reply.result.status == "success")
				{ 
					var time = $.formatDateString(reply.result.message, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
					var content = $("#inputtalk").val();
					$("#inputtalk").val(""); 
					var str = '<div class="usertalk">'+
				    '<div class="talkinfo r"> <div class="avatar r"><img src="<%=request.getContextPath()%>/image/user_avatar.png" width="60" height="60" alt=" " /></div>' +
		            '<div class="triangle r">&nbsp;</div> <div class="talktime r">'+time+'</div>'+
		            '</div> <div class="r" style="width:450px"> <div class="ballnoon r">'+content+'</div>'+
		            '</div> <div class="clear"></div> </div>';
		            $(str).appendTo($("#box"));
		            document.getElementById('box').scrollTop = document.getElementById('box').scrollHeight;
				}
				else
				{
					top.$.messager.alert('警告',"提交失败",'warning');
				}
			}
		);
	}else{
		top.$.messager.alert('警告','输入不能超过500个字符','warning');
		
	}
}
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
	
}
</script>
</body>
</html>